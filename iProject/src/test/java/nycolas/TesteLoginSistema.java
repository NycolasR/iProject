package nycolas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.TipoProvedorAutenticacao;

class TesteLoginSistema {

	private FacadeCasoDeUso2 facadeCasoDeUso2 = new FacadeCasoDeUso2();
	
	@Test
	void testarLogin() {
		// Senhas incorretas
		assertEquals(false, facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin12", TipoProvedorAutenticacao.INTERNO));
		assertEquals(false, facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin1234", TipoProvedorAutenticacao.INTERNO));
		
		// E-mail inexistente
		assertEquals(false, facadeCasoDeUso2.fazerLogin("user@admin.com", "admin123", TipoProvedorAutenticacao.INTERNO));
		
		// E-mail e senha corretos
		assertEquals(true, facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin123", TipoProvedorAutenticacao.INTERNO));
		
	}
}
