package controller.autenticacao;

import facades.casosdeuso.FacadeCasoDeUso1;
import facades.casosdeuso.FacadeCasoDeUso7;
import model.autenticacao.Membro;
import model.autenticacao.RegistradorSessaoLogin;

/**
 * 
 * @author NPG
 * 
 *  Essa classe é o controller que realiza operações do JPanel TelaConfiguracaoAdmin.
 *
 */
public class ControllerTelaConfiguracaoAdmin {

	private FacadeCasoDeUso7 facadeCasoDeUso7 = new FacadeCasoDeUso7();
	private FacadeCasoDeUso1 facadeCasoDeUso1 = new FacadeCasoDeUso1();
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	
	public void tornarOuDesabilitarMembroAdministrador(long matriculaAMudar , String isAdmin){
		
		long matriculaMembroAdministrador = registradorSessaoLogin.getMembroLogado().getMatricula();
		boolean isAdministrador = isAdmin.equals("NAO");
		
		try {
			
			facadeCasoDeUso7.tornarOuDesabilitarMembroAdministrador(matriculaMembroAdministrador, matriculaAMudar, isAdministrador);
			
		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public Membro[] getTodosOsMembros() {
		return facadeCasoDeUso1.getTodosOsMembros();
	}
}
