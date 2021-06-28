package controller.projetos;

import java.time.LocalDate;
import facades.casosdeuso.FacadeCasoDeUso3;
import model.autenticacao.RegistradorSessaoLogin;
import model.projetos.Grupo;
import util.ValidadoraDatas;

/**
 * 
 * @author NPG
 *
 *Essa classe é o controller como realiza operações do JPanel Tela cadastro grupos
 */
public class ControllerTelaCadastroGrupos {

	private FacadeCasoDeUso3 facadeCasoDeUso3 = new FacadeCasoDeUso3();
	private Grupo[] grupos;
	private RegistradorSessaoLogin registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	private long matriculaDoAdmin = registradorSessaoLogin.getMembroLogado().getMatricula();
	
	public void adicionarNovoGrupo( String nome, String dataCriacaoDoGrupo, String linkCNPq ) throws Exception{
	
		if(nome.isEmpty() || dataCriacaoDoGrupo.equals("  /  /    ") || linkCNPq.isEmpty() ) {
			
			throw new Exception("Informe todos os campos");
		}
		
		LocalDate dataCriacao = ValidadoraDatas.criarData(dataCriacaoDoGrupo);
		long codigo = System.currentTimeMillis() % 1000000000;
		
		if(!facadeCasoDeUso3.adicionar(matriculaDoAdmin, nome, dataCriacao, linkCNPq, codigo)) {
			
			throw new Exception("Grupo não adicionado. Dados inválidos");
		}
	}
	
	public void removerGrupoExistente(int posicao) throws Exception{
		
		if(posicao == -1) {
			throw new Exception("Escolha um Grupo para ser removido");
		
		}
		
		long ID = grupos[posicao].getCodigo();
		
		if(!facadeCasoDeUso3.remover(matriculaDoAdmin, ID)) {
			
			throw new Exception("Grupo não removido. Dados inválidos");
		}
	}
	
	public void atualizarGrupoExistente(String nome, String dataCriacaoDoGrupo, String linkCNPq, int posicao) throws Exception{
		
		if(posicao == -1) {
			throw new Exception("Escolha um Grupo para ser atualizado");
		
		}
		
		Grupo grupo = grupos[posicao];
		
		LocalDate dataCriacao = grupo.getDataCriacao();
		long codigo = grupo.getCodigo();
		
		if(nome == null)
			
			nome = grupo.getNome();
		
		if(!dataCriacaoDoGrupo.equals("  /  /    "))
			
			dataCriacao = ValidadoraDatas.criarData(dataCriacaoDoGrupo);
		
		if(linkCNPq == null)
			
			linkCNPq = grupo.getLinkCNPq();
		
		if(!facadeCasoDeUso3.atualizar(matriculaDoAdmin, nome, dataCriacao, linkCNPq, codigo)) {
			
			throw new Exception("Grupo não atualizado. Dados inválidos");
		}
	}
	
	public String[] getCamposParaPreencher(int posicao) throws Exception{
		
		/*
		 * Esse método retorna os valores de um Grupo para seus respectivos campos, onde...
		 * posicao 0 = data de criação do grupo
		 * posicao 1 = link CNPq
		 * posicao 2 = nome do Grupo
		 */
		if(posicao != -1) {
			
			Grupo grupo = grupos[posicao];
			
			String[] campos = new String[3];
			
			LocalDate dataCriacao = grupo.getDataCriacao();
			
			campos[0] = String.valueOf(ValidadoraDatas.transformarDataEmString(dataCriacao));
			campos[1] = grupo.getLinkCNPq();
			campos[2] = String.valueOf(grupo.getNome());
			
			return campos;
			
		}
		
		return  null ;
	}
	
	public Grupo[] getTodoOsGruposDoSistema() {
		
		grupos = facadeCasoDeUso3.getTodosOsGrupos();
		return grupos;
	}
	
	
}
