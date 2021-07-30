package gabriel.integracao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import controller.projetos.ControllerTelaCadastroProjetos;
import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.TipoProvedorAutenticacao;

public class TestesCadastrarProjeto {

	private ControllerTelaCadastroProjetos controllerTelaCadastroProjetos;
	private FacadeCasoDeUso2 casoDeUso2 = new FacadeCasoDeUso2();
	
	@Test
	void testeCriarProjeto() {
				
		try {
			casoDeUso2.fazerLogin("user1@user.com", "senha1", TipoProvedorAutenticacao.INTERNO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		controllerTelaCadastroProjetos = new ControllerTelaCadastroProjetos();
		
		//Esse projeto pode ser criado 
		assertDoesNotThrow(() -> controllerTelaCadastroProjetos.adicionarNovoProjeto("Projeto 1", "5000", "2000", "29/07/2021", "29/12/2021", "500"));
		
		//Esse projeto não pode ser criado 
		Exception excecao1 = assertThrows(Exception.class, () -> {
			controllerTelaCadastroProjetos.adicionarNovoProjeto("Projeto 1", "3000", "2000", "29/07/2021", "29/12/2021", "");});
		assertEquals("Preencha todos os campos", excecao1.getMessage());
		
		//Esse projeto não pode ser criado 
		Exception excecao2 = assertThrows(Exception.class, () -> {
			controllerTelaCadastroProjetos.adicionarNovoProjeto("Projeto 1", "3000", "2000", "29/13/2021", "00/05/2022", "500");});
		assertEquals("Essa data não existe", excecao2.getMessage());
	}
	
	@Test
	void testarAdicionarMembroAoProjeto() {
		
		try {
			casoDeUso2.fazerLogin("user1@user.com", "senha1", TipoProvedorAutenticacao.INTERNO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		controllerTelaCadastroProjetos = new ControllerTelaCadastroProjetos();
		controllerTelaCadastroProjetos.getTodoOsProjetosDoSistema();
		controllerTelaCadastroProjetos.getTodosOsMembros();
		
		//Esse membro pode ser adicionado
		assertDoesNotThrow(() -> controllerTelaCadastroProjetos.adicionarMembro(0, 0, "29/07/2021", "29/12/2021", "200"));
		
		//Esse membro não pode ser adicionado
		Exception excecao1 = assertThrows(Exception.class, () -> {
			controllerTelaCadastroProjetos.adicionarMembro(0, 0, "29/07/2021", "29/12/2021", "1001");});
		assertEquals("Mensalidade excede o valor de custeio", excecao1.getMessage());
		
		

	}
	
	@Test
	void testarSegundoAdicionarMembroAoProjeto() {
		
		try {
			casoDeUso2.fazerLogin("user2@user.com", "senha2", TipoProvedorAutenticacao.INTERNO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		controllerTelaCadastroProjetos = new ControllerTelaCadastroProjetos();
		controllerTelaCadastroProjetos.getTodoOsProjetosDoSistema();
		controllerTelaCadastroProjetos.getTodosOsMembros();
		
		//Esse membro não pode ser adicionado
		Exception excecao1 = assertThrows(Exception.class, () -> {
			controllerTelaCadastroProjetos.adicionarMembro(0, 0, "29/07/2021", "29/12/2021", "200");});
		assertEquals("Você não tem permissão de adicionar um membro nesse projeto", excecao1.getMessage());
		
		
		//Esse membro não pode ser remover o projeto
			Exception excecao2 = assertThrows(Exception.class, () -> {
					controllerTelaCadastroProjetos.removerProjetoExistente(0);});
				assertEquals("Você não tem permissão de remover esse projeto", excecao2.getMessage());
	}
	
	@Test
	void testarAdicionarProjetoAEdital() {
		
		try {
			casoDeUso2.fazerLogin("user1@user.com", "senha1", TipoProvedorAutenticacao.INTERNO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		controllerTelaCadastroProjetos = new ControllerTelaCadastroProjetos();
		controllerTelaCadastroProjetos.getTodoOsProjetosDoSistema();
		controllerTelaCadastroProjetos.getTodosOsEditaisDoSistema();
		
		//Esse projeto pode ser adicionado ao Edital
		assertDoesNotThrow(() -> controllerTelaCadastroProjetos.adicionarProjetoAUmEdital(0, 580267946L));
		
		//Esse projeto não pode ser adicionado ao Edital
		Exception excecao = assertThrows(Exception.class, () -> {
			controllerTelaCadastroProjetos.adicionarProjetoAUmEdital(-1, 580267946L);});
		assertEquals("Escolha um projeto", excecao.getMessage());
	}
	
}
