package nycolas.unidade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.RegistradorSessaoLogin;
import model.autenticacao.TipoProvedorAutenticacao;

class TesteRegistradorSessaoLogin {

	@Test
	void testarRegistradorUsuariosLogados() {
		RegistradorSessaoLogin registrador = RegistradorSessaoLogin.getInstance();
		FacadeCasoDeUso2 facadeCasoDeUso2 = new FacadeCasoDeUso2();
		
		try {
			facadeCasoDeUso2.fazerLogin("admin@admin.com", "admin123", TipoProvedorAutenticacao.INTERNO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals(true, registrador.isOnline("admin@admin.com"));
	}

}
