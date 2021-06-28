package controller.projetos;

import java.time.LocalDate;
import facades.casosdeuso.FacadeCasoDeUso4;
import facades.casosdeuso.FacadeCasoDeUso8;
import model.autenticacao.RegistradorSessaoLogin;
import model.projetos.Edital;
import model.projetos.TiposItemProjeto;
import util.ValidadoraDatas;


/**
 * 
 * @author NPG
 *
 *Essa classe é o controller como realiza operações do JPanel Tela cadastro Editais
 */
public class ControllerTelaCadastroEditais {

	FacadeCasoDeUso4 facadeCasoDeUso4 = new FacadeCasoDeUso4();
	FacadeCasoDeUso8 facadeCasoDeUso8 = new FacadeCasoDeUso8();
	private Edital[] editais ;
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	private long matriculaDoMembro = registradorSessaoLogin.getMembroLogado().getMatricula();
	
	public void adicionarNovoEdital( String nomeEdital, String dataInicioEdital, String dataTerminoEdital) throws Exception{
		
		
		if(nomeEdital.isEmpty() || dataInicioEdital.equals("  /  /    ")|| dataTerminoEdital.equals("  /  /    ") ) {
			
			throw new Exception("Preencha todos os campos");
		}
		
		LocalDate dataInicio = ValidadoraDatas.criarData(dataInicioEdital);
		LocalDate dataTermino = ValidadoraDatas.criarData(dataTerminoEdital);
		long codigo = System.currentTimeMillis() % 1000000000;
		
		if(!facadeCasoDeUso4.adicionarEdital(matriculaDoMembro, nomeEdital, dataInicio, dataTermino, codigo)) {
			throw new Exception("Edital não adicionado. Dados inválidos");
		}
	}
	
	public void removerEditalExistente(int posicao)  throws Exception{
		
		if(posicao < 0) {
			
			throw new Exception("Escolha um Edital para ser removido");
		
		}

		long id = editais[posicao].getCodigo();
		
		if(!facadeCasoDeUso4.removerEdital(matriculaDoMembro, id)) {
			throw new Exception("Edital não removido. Dados inválidos");
		}
	}
	
	public void atualizarEditalExistente(String nomeEdital, String dataInicioEdital, String dataTerminoEdital, int posicao) throws Exception{
		
		if(posicao < 0) {
			
			throw new Exception("Escolha um Edital para atualizar");
			
		}
		
		Edital edital = editais[posicao];
				
		LocalDate dataInicio = edital.getDataInicio();
		LocalDate dataTermino = edital.getDataTermino();
		long codigo = edital.getCodigo();
		
		
		if(nomeEdital.isEmpty()) 
			
			nomeEdital = edital.getNome();
		
		if(!dataInicioEdital.equals( "  /  /    " )) 
			
			dataInicio = ValidadoraDatas.criarData(dataInicioEdital);
			
		if(!dataTerminoEdital.equals( "  /  /    " ))
		
			dataTermino = ValidadoraDatas.criarData(dataTerminoEdital);
		

		if(!facadeCasoDeUso4.atualizarEdital(matriculaDoMembro, codigo, nomeEdital, dataInicio, dataTermino)) {
			throw new Exception("Edital não atualizado. Dados inválidos");
		}
	}
	
	public String[] getCamposParaPreencher(int posicao) throws Exception{
		
		/*
		 * Esse método retorna os valores de um Edital para seus respectivos campos, onde...
		 * posicao 0 = data início do projeto
		 * posicao 1 = data término do projeto
		 * posicao 2 = nome do projeto
		 */
		if(posicao != -1) {
			
			Edital edital = editais[posicao];
			
			String[] campos = new String[3];
			
			LocalDate dataInicioProjeto = edital.getDataInicio();
			LocalDate dataTerminoProjeto = edital.getDataTermino();
			
			campos[0] = String.valueOf(ValidadoraDatas.transformarDataEmString(dataInicioProjeto));
			campos[1] = String.valueOf(ValidadoraDatas.transformarDataEmString(dataTerminoProjeto));
			campos[2] = String.valueOf(edital.getNome());
			
			return campos;
			
		}
		
		return  null ;
	}
	
	public Edital[] getTodoOsEditaisDoSistema() {
		
		editais = facadeCasoDeUso4.getTodosOsEditais();
		
		return editais;
	}
	
	public void mostrarDetalhesEmHTML(int posicao) {
		
		
		try {

			facadeCasoDeUso8.gerarRelatorioHTML(editais[posicao].getCodigo(), TiposItemProjeto.EDITAL);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarDetalhesEmTela(int posicao) {
		
		try {

			facadeCasoDeUso8.gerarRelatorioSwing(editais[posicao].getCodigo(), TiposItemProjeto.EDITAL);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	
}


