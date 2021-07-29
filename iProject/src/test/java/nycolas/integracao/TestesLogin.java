package nycolas.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.autenticacao.ControllerTelaAutenticacao;

class TestesLogin {
	
	private ControllerTelaAutenticacao controllerTelaAutenticacao = new ControllerTelaAutenticacao();

	@Test
	void testLogin() {
		/*
		 * E-mail existente: OK
		 * Senha correta: 	 OK
		 */
		assertDoesNotThrow(() -> 
			controllerTelaAutenticacao.fazerLogin("administracao@admin.com", "P@ssw0rd", true));
		
		/*
		 * E-mail existente: NOT OK
		 * Senha correta: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaAutenticacao.fazerLogin("admin@admin.com", "P@ssw0rd", true);
		});
		
		/*
		 * E-mail existente: OK
		 * Senha correta: 	 NOT OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaAutenticacao.fazerLogin("administracao@admin.com", "admin123", true);
		});
	}
}
