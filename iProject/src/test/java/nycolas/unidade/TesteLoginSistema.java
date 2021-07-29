package nycolas.unidade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.TipoProvedorAutenticacao;

class TesteLoginSistema {

	private FacadeCasoDeUso2 facadeCasoDeUso2 = new FacadeCasoDeUso2();
	
	@Test
	void testarLogin() {
		// Senhas incorretas
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin12", TipoProvedorAutenticacao.INTERNO);
		});
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin1234", TipoProvedorAutenticacao.INTERNO);
		});
		
		// E-mail inexistente
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso2.fazerLogin("user@admin.com", "admin123", TipoProvedorAutenticacao.INTERNO);
		});
		
		// E-mail e senha corretos
		assertDoesNotThrow(() -> 
			facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin123", TipoProvedorAutenticacao.INTERNO));
		
	}
}
