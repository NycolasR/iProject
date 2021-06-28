package view.projetos;

import view.autenticacao.TelaAutenticacao;
import view.autenticacao.TelaAutenticacaoSwing;
import view.autenticacao.TelaConfiguracaoAdmin;
import view.autenticacao.TelaConfiguracaoAdminSwing;
import view.autenticacao.TelaCriarConta;
import view.autenticacao.TelaCriarContaSwing;

/**
 * 
 * @author NPG
 * 
 * Essa classe representa a fabrica concreta do padrão abstract factory
 *
 */
public class FabricaTelaSwing implements FabricaTela{

	
	public TelaCadastroProjetos fabricarTelaCadastroProjetos() {
		
		TelaCadastroProjetosSwing telaCadastroProjetosSwing = new TelaCadastroProjetosSwing();
		
		return telaCadastroProjetosSwing;
	}

	public TelaCadastroGrupos fabricarTelaCadastroGrupos() {
		
		TelaCadastroGruposSwing telaCadastroGruposSwing = new TelaCadastroGruposSwing();
		
		return telaCadastroGruposSwing;
	}

	public TelaCadastroEditais fabricarTelaCadastroEditais() {
		
		TelaCadastroEditaisSwing telaCadastroEditaisSwing = new TelaCadastroEditaisSwing();
		
		return telaCadastroEditaisSwing;
	}
	
	public TelaJustificarPonto fabricaTelaJustificarPonto() {
		
		TelaJustificarPonto telaJustificarPonto = new TelaJustificarPontoSwing();
		
		return telaJustificarPonto;
	}
	
	public TelaAutenticacao fabricarTelaAutenticacao() {
		
		TelaAutenticacaoSwing telaAutenticacao = new TelaAutenticacaoSwing();
		
		return telaAutenticacao;
	}

	public TelaCriarConta fabricarTelaCriarConta() {

		TelaCriarContaSwing telaCriarConta = new TelaCriarContaSwing();
		
		return telaCriarConta;
	}

	public TelaConfiguracaoAdmin fabricarTelaConfiguracaoAdmin() {

		TelaConfiguracaoAdminSwing telaConfiguracaoAdmin = new TelaConfiguracaoAdminSwing();
	
		return telaConfiguracaoAdmin;
	}

}
