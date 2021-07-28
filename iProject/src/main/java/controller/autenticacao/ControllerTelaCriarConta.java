package controller.autenticacao;

import facades.casosdeuso.FacadeCasoDeUso1;
import model.autenticacao.Membro;

/**
 * 
 * @author NPG
 *
 *  Essa classe é o controller que realiza operações do JPanel TelaCriarConta.
 *  
 */
public class ControllerTelaCriarConta {

	private static FacadeCasoDeUso1 facadeCasoDeUso1 = new FacadeCasoDeUso1();
	
	public void cadastrarConta(String nome, long matricula, String login, String senha) throws Exception {
		
		if(!facadeCasoDeUso1.cadastrarConta(nome, matricula, login, senha)) {
			throw new Exception("[ERRO] Não foi possivel cadastrar a conta");
		}
	}
	
	public void atualizarConta(long matricula , String nome, String login, String senha) throws Exception {
		
		if(!facadeCasoDeUso1.atualizar(matricula, nome, login, senha)) {
			throw new Exception("[ERRO] Não foi possivel atualizar a conta");
		}
	}
	
	public static boolean isPrimeiroAcesso() {
		Membro[] membros = facadeCasoDeUso1.getTodosOsMembros();
		
		if(membros.length == 0)
			return true;
		
		return false;
		
	}
}
