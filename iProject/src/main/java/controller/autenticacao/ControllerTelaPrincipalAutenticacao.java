package controller.autenticacao;

import view.autenticacao.TelaPrincipalAutenticacao;

/**
 * 
 * @author NPG
 *
 *  Essa classe é o controller que realiza operações do JFrame TelaPrincipalAutenticacao.
 *  
 */
public class ControllerTelaPrincipalAutenticacao {
	
	private static TelaPrincipalAutenticacao telaPrincipalAutenticacao;
	
	public ControllerTelaPrincipalAutenticacao(TelaPrincipalAutenticacao tpa) {
		telaPrincipalAutenticacao = tpa;
	}
	
	public boolean isPrimeiroAcesso() {
		return ControllerTelaCriarConta.isPrimeiroAcesso();
		
	}
	
	public static void encerrarJanela() {
		telaPrincipalAutenticacao.dispose();
	}
	
}
