package dominio;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class CreditoTest {

	Credito tarjetaCredito;

	@BeforeEach
	public void setUp() {
		tarjetaCredito = new Credito("123456", "Nombre", new Date(), 100.0);
		tarjetaCredito.setCuenta(new Cuenta("123456", "Nombre"));
	}

	@Test
	public void testRetirar() {

		try {
			tarjetaCredito.retirar(96);
			fail("Debería lanzarse una excepción: crédito insuficiente");
		} catch (Exception e) {
			System.err.println(e);
		}
		/*
		 * Debería lanzarse la excepción, porque no hay crédito suficiente para realizar
		 * la retirada y abonar la comisión simultáneamente. El método está implementado
		 * incorrectamente. Se codificará uno corregido en la clase original con el que
		 * se continuará el test.
		 */
	}

	@Test
	public void testRetirarCorregido() throws Exception {
		try {
			tarjetaCredito.retirarCorregido(96); // Primera cantidad entera no válida
			fail("Debería lanzarse una excepción: crédito insuficiente");
		} catch (Exception e) {
			System.err.println(e);
		}
		try {
			tarjetaCredito.retirarCorregido(95); // Última cantidad entera válida
		} catch (Exception e) {
			fail("No debería lanzarse ninguna excepción");
		}
		assertThat(tarjetaCredito.getCreditoDisponible(), is(0.25)); // Crédito disponible
	}

	@Test
	public void testIngresar() throws Exception {
		tarjetaCredito.ingresar(50);
		assertThat(tarjetaCredito.getCreditoDisponible(), is(100));
		assertThat(tarjetaCredito.mCuentaAsociada.getSaldo(), is(50));
		/*
		 * El ingreso se hace en la cuenta subyacente, por lo que no debería modificarse
		 * el crédito disponible en la tarjeta de crédito. El método está implementado
		 * incorrectamente. Se codificará uno corregido en la clase original con el que
		 * se continuará el test.
		 */
	}

	@Test
	public void testIngresarCorregido() throws Exception {
		tarjetaCredito.ingresarCorregido(50);
		assertThat(tarjetaCredito.getCreditoDisponible(), is(100.0)); // El crédito disponible no se modifica
		assertThat(tarjetaCredito.mCuentaAsociada.getSaldo(), is(50.0)); // Los fondos de la cuenta subyacente sí
	}

	@Test
	public void testPagoEnEstablecimiento() {
		try {
			tarjetaCredito.pagoEnEstablecimiento(null, 200);
			fail("Debería lanzarse una excepción: crédito insuficiente");
		} catch (Exception e) {
			System.err.println(e);
		}
		/*
		 * No se lanza la excepción, porque el método no controla que el crédito sea
		 * suficiente para realizar el pago. Tampoco se controla que el concepto no sea
		 * nulo, aunque eso lo ignoraremos. El método está implementado incorrectamente.
		 * Se codificará uno corregido en la clase original con el que se continuará el
		 * test.
		 */
	}

	@Test
	public void testPagoEnEstablecimientoCorregido() {
		try {
			tarjetaCredito.pagoEnEstablecimientoCorregido(null, 200);
			fail("Debería lanzarse una excepción: crédito insuficiente");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Test
	public void testLiquidar() throws Exception {
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago1", 10);
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago2", 20);
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago3", 30);
		assertThat(tarjetaCredito.getSaldo(), is(60.0));
		assertThat(tarjetaCredito.getCreditoDisponible(), is(40.0));
		tarjetaCredito.liquidar(3, 2024);
		assertThat(tarjetaCredito.getSaldo(), is(0.0));
		/*
		 * El saldo (deuda) no disminuye, el método no está liquidando la deuda
		 * crediticia sino realizando un movimiento a favor en la cuenta subyacente. El
		 * método está implementado incorrectamente. Se codificará uno corregido en la
		 * clase original con el que se continuará el test.
		 */
	}

	@Test
	public void testLiquidarCorregido() throws Exception {
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago1", 10);
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago2", 20);
		tarjetaCredito.pagoEnEstablecimientoCorregido("Pago3", 30);
		assertThat(tarjetaCredito.getSaldo(), is(60.0));
		assertThat(tarjetaCredito.getCreditoDisponible(), is(40.0));
		tarjetaCredito.liquidarCorregido(3, 2024);
		assertThat(tarjetaCredito.getSaldo(), is(0.0)); // Saldo (deuda) se liquida
		assertThat(tarjetaCredito.getCreditoDisponible(), is(100.0)); // Deuda nula => crédito disponible máximo
		assertThat(tarjetaCredito.mCuentaAsociada.getSaldo(), is(-60.0)); // La cuenta subyacente queda al descubierto
																			// al haberse utilizado para liquidar la
																			// deuda
	}

	// Método getSaldo() idéntico al de Cuenta, y ya testeado indirectamente

}
