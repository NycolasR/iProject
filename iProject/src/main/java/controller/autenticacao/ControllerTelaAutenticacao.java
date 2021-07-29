package controller.autenticacao;

import facades.casosdeuso.FacadeCasoDeUso2;
import model.autenticacao.RegistradorSessaoLogin;
import model.autenticacao.TipoProvedorAutenticacao;
import view.projetos.TelaPonto;
import view.projetos.TelaPrincipal;

/**
 * 
 * @author NPG
 * 
 *  Essa classe é o controller que realiza operações do JPanel TelaAutenticacao.
 *
 */
public class ControllerTelaAutenticacao {
	
	private FacadeCasoDeUso2 facadeCasoDeUso2 = new FacadeCasoDeUso2();
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	
	private boolean isAdmin;
	
	public boolean fazerLogin(String email, String senha, boolean tipoProvedorInterno) throws Exception {
		
		TipoProvedorAutenticacao tipoProvedor = TipoProvedorAutenticacao.INTERNO;
		
		if(!tipoProvedorInterno) {
			tipoProvedor = TipoProvedorAutenticacao.EMAIL_SMTP;
		}
		
		
		if(facadeCasoDeUso2.fazerLogin(email, senha, tipoProvedor)) {
			
			isAdmin = registradorSessaoLogin.getMembroLogado().isAdministrador();
			return true;
		}
		
		return false;
		
	}
	
	public void proximaTela() {
		if(isAdmin) {
			TelaPrincipal telaPrincipal = new TelaPrincipal();
			
			telaPrincipal.validarAdminCoordenador(isAdmin);
		}else {
			new TelaPonto();
		}
		ControllerTelaPrincipalAutenticacao.encerrarJanela();
		
	}
	
}
