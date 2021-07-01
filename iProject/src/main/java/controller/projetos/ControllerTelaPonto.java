package controller.projetos;

import java.rmi.Naming;
import java.time.LocalDateTime;

import model.autenticacao.RegistradorSessaoLogin;
import model.projetos.Projeto;
import ponto.model.projetos.ProxyInterface;
import view.projetos.FabricaTela;
import view.projetos.FabricaTelaSwing;
import view.projetos.TelaDetalhe;
import view.projetos.TelaJustificarPonto;
//import view.projetos.TelaJustificarPontoJFrame;
import view.projetos.TelaPrincipal;

/**
 * @author NPG
 * TODO FEITO [UML] atualizar com os métodos
 */
public class ControllerTelaPonto {
	
	private ProxyInterface registradorProxy;
	private RegistradorSessaoLogin registradorSessaoLogin;
	private FabricaTela fabricaTela;
	
	public ControllerTelaPonto(){
		try {
		 registradorProxy = (ProxyInterface) Naming.lookup("RegistradorPontoCentral");
		 registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
		 fabricaTela = new FabricaTelaSwing();
		}catch (Exception e) {}
	}
	
	
	public Projeto[] getProjetos() throws Exception {
		return registradorProxy.getTodosOsProjetos();
	}
		
	public boolean registrarPonto(long codProjeto){
		
		try {
			registradorProxy.registrarPonto(codProjeto, registradorSessaoLogin.getMembroLogado().getEmail());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		

	}
	
	public void pegarDetalhes(LocalDateTime dataAgora,long codProjeto) throws Exception {
		
		StringBuffer detalhes = registradorProxy.pegarDetalhes(dataAgora,  registradorSessaoLogin.getMembroLogado().getEmail(), codProjeto);
		
		new TelaDetalhe(detalhes);
		
	}
	
	public void cadastrarProjeto() {
		
		TelaPrincipal telaPrincipal = new TelaPrincipal();
		
		telaPrincipal.validarAdminCoordenador(false);
	}
	
	public void JustificarPonto() {
		
		TelaJustificarPonto telaJustificarPonto = fabricaTela.fabricaTelaJustificarPonto();
		telaJustificarPonto.justificarPonto();
		
	}
	
	
	
	
	
	
	
}