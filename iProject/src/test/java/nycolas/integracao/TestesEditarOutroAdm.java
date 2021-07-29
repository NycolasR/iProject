package nycolas.integracao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import controller.autenticacao.ControllerTelaAutenticacao;
import controller.autenticacao.ControllerTelaConfiguracaoAdmin;

@TestMethodOrder(OrderAnnotation.class)
class TestesEditarOutroAdm {

	private ControllerTelaConfiguracaoAdmin controllerTelaConfiguracaoAdmin = new ControllerTelaConfiguracaoAdmin();
	private ControllerTelaAutenticacao autenticacao = new ControllerTelaAutenticacao();
	
	@BeforeEach
	void logarAdm() {
		// O ADM deve estar logado antes
		assertDoesNotThrow(() -> 
			autenticacao.fazerLogin("administracao@admin.com", "P@ssw0rd", true));
	}
	
	@Test
	@Order(1)
	void testCriarAdministrador() {
		assertTrue(controllerTelaConfiguracaoAdmin.tornarOuDesabilitarMembroAdministrador(111101111, "NAO"));
	}
	
	@Test
	@Order(2)
	void testRemoverAdministrador() {
		// O ADM deve estar logado antes
//		assertDoesNotThrow(() -> 
//			autenticacao.fazerLogin("administracao@admin.com", "P@ssw0rd", true));
		
		assertTrue(controllerTelaConfiguracaoAdmin.tornarOuDesabilitarMembroAdministrador(111101111, "SIM"));
	}
}
