package dominio;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CuentaTest {

	Cuenta cuenta;

	@BeforeEach
	public void setUp() {
		cuenta = new Cuenta("123456", "Nombre");
	}

	@Test
	public void testIngresar() {
		try {
			cuenta.ingresar(0);
			fail("Debería lanarse la excepción: cantidad negativa");
		} catch (Exception e) {
			System.err.println(e);
		}
		try {
			cuenta.ingresar(100);
		} catch (Exception e) {
			fail("No debería lanarse ninguna excepción");
		}
		Movimiento ultimoMovimiento = (Movimiento) cuenta.mMovimientos.get(cuenta.mMovimientos.size() - 1);
		assertThat(ultimoMovimiento.getImporte(), is(100.0));
	}

	@Test
	public void testRetirar() {
		try {
			cuenta.ingresar(100); // Primero, es necesario añadir fondos
		} catch (Exception e) {
			fail("No debería lanzarse ninguna excepción");
		}
		try {
			cuenta.retirar(0);
			fail("Debería lanarse la excepción: cantidad negativa");
		} catch (Exception e) {
			System.err.println(e);
		}
		try {
			cuenta.retirar(100);
		} catch (Exception e) {
			fail("No debería lanarse ninguna excepción");
		}
		try {
			cuenta.retirar(100);
			fail("Debería lanarse la excepción: saldo insuficiente");
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Test
	public void testGetSaldo() throws Exception {
		assertThat(cuenta.getSaldo(), is(0.0));
		cuenta.ingresar(1000); // Ingresamos para probar el bucle
		double resultado = 1000;
		for(int i = 0; i < 10; i++) {
			Double cantidad = Math.random() * 100;
			if(cantidad != 0) {
				if(i % 2 == 0) {
					cuenta.ingresar(cantidad);
					resultado += cantidad;
				} else {
					cuenta.retirar(cantidad);
					resultado -= cantidad;
				}
			}
		}
		assertThat(cuenta.getSaldo(), is(resultado));
	}

}
