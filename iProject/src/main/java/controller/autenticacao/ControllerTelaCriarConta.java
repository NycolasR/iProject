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
		
		try {
			facadeCasoDeUso1.cadastrarConta(nome, matricula, login, senha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void atualizarConta(long matricula , String nome, String login, String senha) throws Exception {
		
		try {
			facadeCasoDeUso1.atualizar(matricula, nome, login, senha);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static boolean isPrimeiroAcesso() {
		Membro[] membros = facadeCasoDeUso1.getTodosOsMembros();
		
		if(membros.length == 0)
			return true;
		
		return false;
		
	}
}
