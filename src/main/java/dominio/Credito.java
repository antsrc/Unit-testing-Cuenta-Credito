package dominio;

import java.util.Vector;
import java.util.Date;

public class Credito extends Tarjeta {
	protected double mCredito;
	protected Vector mMovimientos;

	public Credito(String numero, String titular, Date fechaCaducidad, double credito) {
		super(numero, titular, fechaCaducidad);
		mCredito = credito;
		mMovimientos = new Vector();
	}

	public void retirar(double x) throws Exception {
		Movimiento m = new Movimiento();
		m.setConcepto("Retirada en cajero autom�tico");
		x = (x * 0.05 < 3.0 ? 3 : x * 0.05); // A�adimos una comisi�n de un 5%, m�nimo de 3 euros.
		m.setImporte(x);
		mMovimientos.addElement(m);
		if (x > getCreditoDisponible())
			throw new Exception("Cr�dito insuficiente");
	}

	public void retirarCorregido(double x) throws Exception {
		x += (x * 0.05 < 3.0 ? 3 : x * 0.05); // A�adimos una comisi�n de un 5%, m�nimo de 3 euros.
		if (x > getCreditoDisponible()) {
			throw new Exception("Cr�dito insuficiente");
		} else {
			Movimiento m = new Movimiento();
			m.setConcepto("Retirada en cajero autom�tico");
			m.setImporte(x);
			mMovimientos.addElement(m);
		}
	}

	public void ingresar(double x) throws Exception {
		Movimiento m = new Movimiento();
		m.setConcepto("Ingreso en cuenta asociada (cajero autom�tico)");
		m.setImporte(x);
		mMovimientos.addElement(m);
		mCuentaAsociada.ingresar(x);
	}

	public void ingresarCorregido(double x) throws Exception {
		Movimiento m = new Movimiento();
		m.setConcepto("Ingreso en cuenta asociada (cajero autom�tico)");
		m.setImporte(x);
		mCuentaAsociada.ingresar(x);
	}

	public void pagoEnEstablecimiento(String datos, double x) throws Exception {
		Movimiento m = new Movimiento();
		m.setConcepto("Compra a cr�dito en: " + datos);
		m.setImporte(x);
		mMovimientos.addElement(m);
	}

	public void pagoEnEstablecimientoCorregido(String datos, double x) throws Exception {
		if (x > getCreditoDisponible()) {
			throw new Exception("Cr�dito insuficiente");
		} else {
			Movimiento m = new Movimiento();
			m.setConcepto("Retirada en cajero autom�tico");
			m.setImporte(x);
			mMovimientos.addElement(m);
		}
	}

	public double getSaldo() {
		double r = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento m = (Movimiento) mMovimientos.elementAt(i);
			r += m.getImporte();
		}
		return r;
	}

	public double getCreditoDisponible() {
		return mCredito - getSaldo();
	}

	@SuppressWarnings("deprecation")
	public void liquidar(int mes, int ano) {
		Movimiento liq = new Movimiento();
		liq.setConcepto("Liquidaci�n de operaciones tarj. cr�dito, " + (mes + 1) + " de " + (ano + 1900));
		double r = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento m = (Movimiento) mMovimientos.elementAt(i);
			if (m.getFecha().getMonth() + 1 == mes && m.getFecha().getYear() + 1900 == ano)
				r += m.getImporte();
		}
		liq.setImporte(r);
		if (r != 0)
			mCuentaAsociada.addMovimiento(liq);
	}
	
	@SuppressWarnings("deprecation")
	public void liquidarCorregido(int mes, int ano) {
		Movimiento liq = new Movimiento();
		liq.setConcepto("Liquidaci�n de operaciones tarj. cr�dito, " + (mes + 1) + " de " + (ano + 1900));
		double r = 0.0;
		for (int i = 0; i < this.mMovimientos.size(); i++) {
			Movimiento m = (Movimiento) mMovimientos.elementAt(i);
			if (m.getFecha().getMonth() + 1 == mes && m.getFecha().getYear() + 1900 == ano)
				r += m.getImporte();
		}
		liq.setImporte(-r);
		if (r != 0)
			this.mMovimientos.addElement(liq);
			mCuentaAsociada.addMovimiento(liq);
	}
}