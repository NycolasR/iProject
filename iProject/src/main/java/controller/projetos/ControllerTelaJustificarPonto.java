package controller.projetos;

import facades.casosdeuso.FacadeCasoDeUso12;
import model.autenticacao.RegistradorSessaoLogin;
import model.projetos.Projeto;
import ponto.model.projetos.PontoTrabalhado;
import view.projetos.TelaPonto;

/**
 * 
 * @author NPG
 *
 *Essa classe é o controller como realiza operações do JPanel Justificar ponto
 */
public class ControllerTelaJustificarPonto {

	
	private FacadeCasoDeUso12 facadeCasoDeUso12;
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	private String email = registradorSessaoLogin.getMembroLogado().getEmail().trim();
	private Projeto[] projetos;
	private PontoTrabalhado[] pontosTrabalhados;
	
	public ControllerTelaJustificarPonto() {
		try {
			
			facadeCasoDeUso12 = new FacadeCasoDeUso12();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void justificarPonto(int posicaoDoPonto,String justificativa, int posicaoDoProjeto) throws Exception{
		
		if(!facadeCasoDeUso12.justificarPontoInvalido(pontosTrabalhados[posicaoDoPonto].getId(),
				email, justificativa, projetos[posicaoDoProjeto].getCodigo())) {
			
			throw new Exception("Erro ao justificar ponto");
		}
	}
	
	public PontoTrabalhado[] getPontosTrabalhados( int posicaoDoProjeto) throws Exception{
		
		if(posicaoDoProjeto < 0) {
			
			throw new Exception("Escolha um projeto");
		}
		
		pontosTrabalhados = facadeCasoDeUso12.getPontosInvalidosDoMembro(email, projetos[posicaoDoProjeto].getCodigo());
		
		return pontosTrabalhados;
		
	}
	
	public Projeto[] getProjetos() throws Exception{
		
		projetos = facadeCasoDeUso12.getProjetosAtivosDoMembro(email);
		
		return projetos;

	}
	
	public void gerarTelaPonto() {
		
		new TelaPonto();
	}
	
}
