package es.ucm.fdi.tp.launcher;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

	@Test //Primer argumento falla
	public void MainTestArgumentoInvalido()
	{
		Main_p4 prueba = new Main_p4();
		boolean comprobar = prueba.arguments("ggg","smart","smart");
		assertTrue(comprobar == false);
	}
	@Test //Numero incorrecto de argumentos
	public void MainTestArgumentosNoExactos()
	{
		Main_p4 prueba = new Main_p4();
		boolean comprobar = prueba.arguments("was","random");
		assertTrue(comprobar == false);
		comprobar = prueba.arguments("ttt","random","random", "random");
		assertTrue(comprobar == false);
	}
}
