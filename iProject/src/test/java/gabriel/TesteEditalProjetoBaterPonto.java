package gabriel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import controller.projetos.ControllerTelaPonto;
import facades.casosdeuso.FacadeCasoDeUso4;
import model.autenticacao.RegistradorSessaoLogin;
import persistencia.DAOXMLEdital;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;
/**
 * 
 * @author NPG
 *
 */
public class TesteEditalProjetoBaterPonto {

	private DAOXMLEdital daoxmlEdital = new DAOXMLEdital();
	private DAOXMLMembroConta daoxmlMembro = new DAOXMLMembroConta();
	private DAOXMLProjetoParticipacao daoProjeto = new DAOXMLProjetoParticipacao();
	private FacadeCasoDeUso4 facade4 = new FacadeCasoDeUso4();
	private RegistradorSessaoLogin registrador = RegistradorSessaoLogin.getInstance();
	private ControllerTelaPonto controlerPonto = new ControllerTelaPonto();
	
	
	@Test
	void testarProjetoEmEditalFinalizado() {
		
		//Projeto pode ser adicionado
		assertDoesNotThrow(() -> facade4.adicionarProjetoAUmEdital(
				daoxmlEdital.consultarPorID(157285535l),daoProjeto.consultarPorID(158924708l)));

		//Projeto não pode ser adicionado
		Exception excecao = assertThrows(Exception.class, () -> {facade4.adicionarProjetoAUmEdital(
				daoxmlEdital.consultarPorID(157329326l),daoProjeto.consultarPorID(158935788l));});
		assertEquals("Não é possível adicionar um projeto ou grupo (Edital fora de vigência)", excecao.getMessage());
	}
	
	@Test
	void testarBaterPonto() {
		try {
		registrador.registrarOnline(daoxmlMembro.consultarPorMatricula(222222222l));
		}catch (Exception e) {}
		
		//É possível o user 2 bater ponto no projeto 1 já que ele está cadastrado no projeto
		assertEquals(true, controlerPonto.registrarPonto(157423848l));
		
		//Não é possível o user 2 bater ponto no projeto 3 já que ele não está cadastrado no projeto
		assertNotEquals(true, controlerPonto.registrarPonto(158924708l));
	}
	
	
}
