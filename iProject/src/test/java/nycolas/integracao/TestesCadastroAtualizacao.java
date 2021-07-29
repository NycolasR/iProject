package nycolas.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import controller.autenticacao.ControllerTelaCriarConta;

class TestesCadastroAtualizacao {
	
	private ControllerTelaCriarConta controllerTelaCriarConta = new ControllerTelaCriarConta();

	@Test
	void testCriarConta() {
		/*
		 * Tamanho da matricula: OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertDoesNotThrow(() -> 
			controllerTelaCriarConta.cadastrarConta("Administrador do Sistema", 123456789l, "admin@admin.com", "admin123"));
		
		/*
		 * Tamanho da matricula: NOT OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.cadastrarConta("Administrador do Sistema", 12345678l, "admin@admin.com", "admin123");
		});
			
		
		/*
		 * Tamanho da matricula: OK
		 * Tamanho do nome: 	 NOT OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.cadastrarConta("Admin", 123456789l, "admin@admin.com", "admin123");
		});

		
		/*
		 * Tamanho da matricula: OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    NOT OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.cadastrarConta("Administrador do Sistema", 123456789l, "adminadmin.com", "admin123");
		});
		
		/*
		 * Tamanho da matricula: OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 NOT OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.cadastrarConta("Administrador do Sistema", 123456789l, "admin@admin.com", "admin");
		});
	}
	
	@Test
	void testAtualizarConta() {
		/*
		 * Usuário existente:    OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertDoesNotThrow(() -> 
			controllerTelaCriarConta.atualizarConta(123456789l, "Administrador do iProject", "administracao@admin.com", "P@ssw0rd"));
		
		/*
		 * Usuario existente:    NOT OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.atualizarConta(999999999l, "Administrador do iProject", "administracao@admin.com", "P@ssw0rd");
		});
			
		
		/*
		 * Usuário existente:    OK
		 * Tamanho do nome: 	 NOT OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.atualizarConta(123456789l, "Admin", "administracao@admin.com", "P@ssw0rd");
		});

		
		/*
		 * Usuário existente:    OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    NOT OK
		 * Tamanho da senha: 	 OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.atualizarConta(123456789l, "Admin", "administracaoadmin.com", "P@ssw0rd");
		});
		
		/*
		 * Usuário existente:    OK
		 * Tamanho do nome: 	 OK
		 * Formato do e-mail:    OK
		 * Tamanho da senha: 	 NOT OK
		 */
		assertThrows(Exception.class, () -> {
			controllerTelaCriarConta.atualizarConta(123456789l, "Admin", "administracao@admin.com", "P@ssw");
		});
	}
}
