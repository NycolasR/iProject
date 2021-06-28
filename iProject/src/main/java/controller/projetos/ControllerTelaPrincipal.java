package controller.projetos;

import model.autenticacao.RegistradorSessaoLogin;
import view.autenticacao.TelaPrincipalAutenticacao;
import view.projetos.TelaPonto;
import view.projetos.TelaPrincipal;

/**
 * 
 * @author NPG
 *
 *Essa classe é o controller como realiza operações da tela Principal
 */
public class ControllerTelaPrincipal {
	
	private TelaPrincipal telaprincipal;
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	
	public ControllerTelaPrincipal(TelaPrincipal telaprincipa){
		this.telaprincipal = telaprincipa;
	}
	
	public void fazerLogoff() {
		registradorSessaoLogin.registrarOffline(registradorSessaoLogin.getMembroLogado().getEmail());
		telaprincipal.dispose();
		new TelaPrincipalAutenticacao();
	}
	
	public void voltaPraTelaPonto() {
		
		new TelaPonto();
	}
	
	
	
}
