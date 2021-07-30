package gabriel.integracao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import controller.autenticacao.ControllerTelaAutenticacao;
import controller.projetos.ControllerTelaJustificarPonto;
import controller.projetos.ControllerTelaPonto;

public class TestesBaterPonto {

	private ControllerTelaPonto controllerTelaPonto;
	private ControllerTelaAutenticacao controllerTelaAutenticacao = new ControllerTelaAutenticacao();
	private ControllerTelaJustificarPonto controllerTelaJustificarPonto;
	
	@Test
	void testarBaterPonto() {
		
		try {
			controllerTelaAutenticacao.fazerLogin("user1@user.com", "senha1", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		controllerTelaPonto = new ControllerTelaPonto();
				
		//É possível bater ponto 
		assertTrue(controllerTelaPonto.registrarPonto(598459322L));
		
		//Não é possíel bater ponto pois o usuário não está no projeto
		assertEquals(false, controllerTelaPonto.registrarPonto(592685596L));
		
		
	}
	
	@Test
	void testarJustificarPontoInvalido() {
		
		try {
			controllerTelaAutenticacao.fazerLogin("user1@user.com", "senha1", true);
			controllerTelaJustificarPonto = new ControllerTelaJustificarPonto();
//			System.out.println(controllerTelaJustificarPonto.getProjetos().length);
			controllerTelaJustificarPonto.getPontosTrabalhados(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//Esse ponto pode ser justificado, uma vez que o membro que eá logado pertence ao projeto e também contém um ponto inválido
		assertDoesNotThrow(() -> 
				controllerTelaJustificarPonto.justificarPonto(0, "Não me deu vontade de vir nesse dia trabalhar no projeto", 0));
		
	}
}
