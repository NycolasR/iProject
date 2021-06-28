package model.projetos;

import java.time.LocalDate;
import java.util.ArrayList;

import model.autenticacao.Membro;
import util.ValidadoraItemProjeto;
import util.ValidadoraMembros;

/**
 * @author NPG
 *
 *Essa classe faz parte da implementação do padrão composite como sendo um tipo 
 *concreto e uma classe de objeto composto, capaz de possuir Projetos, que é 
 *um outro tipo de objeto composto. Ela evita conhecer o tipo concreto
 *de um projeto, então faz uso do supertipo para armazená-lo.
 */
public class Grupo extends ItemProjeto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDate dataCriacao;
	private String linkCNPq;
	private ArrayList<ItemProjeto> projetos = new ArrayList<ItemProjeto>();
	private ArrayList<Membro> membros = new ArrayList<Membro>();
	

	/**
	 * Esse construtor seta na superclasse o tipo de item projeto correspondente a essa classe
	 */
	public Grupo () {
		super.setTipoItemProjeto(TiposItemProjeto.GRUPO);
	}
	
	
	/*
	 * Os m�todos a baixo s�o implementa��es com propaga��o de chamada de m�todo.
	 * Sao as implementa��es dos m�todos abstratos da superclasse
	 */
	
	public void ativar() {
		for(int i = 0; i < projetos.size(); i++) {
			projetos.get(i).ativar();
		}
		super.setAtivo(true);

	}

	public void desativar() {
		for(int i = 0; i < projetos.size(); i++) {
			projetos.get(i).desativar();
		}
		super.setAtivo(false);
	}

	public float getCustoTotal() {
		float valorTotal = 0;
		
		for(int i = 0; i < projetos.size(); i++) {
			valorTotal += projetos.get(i).getCustoTotal();
		}
		
		return valorTotal;
	}

	public float getCusteioReaisNaoGastoTotal() {
		float valorCusteioReaisNaoGastoTotal = 0;
		
		for(int i = 0; i < projetos.size(); i++) {
			valorCusteioReaisNaoGastoTotal += projetos.get(i).getCusteioReaisNaoGastoTotal();
		}
		
		return valorCusteioReaisNaoGastoTotal;
	}

	public float getCapitalReaisNaoGastoTotal() {
		float valorCapitalReaisNaoGastoTotal = 0;
		
		for(int i = 0; i < projetos.size(); i++) {
			valorCapitalReaisNaoGastoTotal += projetos.get(i).getCapitalReaisNaoGastoTotal();
		}
		
		return valorCapitalReaisNaoGastoTotal;
	}
	
	/**
	 * O metodo abaixo verifica a existencia de um projeto nesse grupo (O cliente deve tomar
	 * cuidado para nao passar um item projeto incompativel como parametro de entrada, evitando
	 * assim o instanceOf, ou seja, evitando o acoplamento entre classes), 
	 * para so entao adiciona-lo aos projetos dessa classe.
	 * 
	 * TODO [CODIGO] agora é possível verificar o tipo de item projeto, atraves do atripo tipoItemProjeto
	 * sem precisar fazer classes componentes se acoplarem a outras
	 */	
	public void adicionarItemProjeto(ItemProjeto itemProjeto)throws Exception {
		if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.PROJETO.getValor() &&
				ValidadoraItemProjeto.validarExistenciaDeItemProjeto(projetos, itemProjeto) == null) {
			itemProjeto.adicionarItemProjeto(this);
			projetos.add(itemProjeto);
			
		} else {
			throw new Exception("Item n�o adicionado. projeto ja existente");
		}
	}

	/*
	 *Da mesma forma que o adicionar, o remover verifica a existencia de um
	 *projeto nesse grupo, levando em consideracao que o cliente � quem fica
	 *respon�vel de passar um item projeto compativel como par�metro de entrada,
	 *evitando assim acoplamento entre classes ao usar o instanceOf, para s� ent�o remov�-lo do mesmo. 
	 */
	public void removerItemProjeto(ItemProjeto itemProjeto) throws Exception {
		
		if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.PROJETO.getValor()) {
			ItemProjeto projetoRetornado = ValidadoraItemProjeto.validarExistenciaDeItemProjeto(projetos, itemProjeto);
			
			if(projetoRetornado != null) {
				projetos.remove(projetoRetornado);
			} else {
				throw new Exception("Item nao removido. Projeto nao encontrado");
			}
			
		} else {
			throw new Exception("O item projeto não é um projeto");
		}	
	}

	/*
	 * Esse metodo, antes de adicionar um membro, verifica se ele existe nesse grupo 
	 * e se tem alguma participacao em algum projeto no mesmo, para entao adiciona-lo 
	 * aos membros dessa classe. 
	 */
	public void adicionarMembro(Membro membro) throws Exception {
		
		if(ValidadoraMembros.validarExistenciaDeMembro(membros, membro) == null
			&& validarParticipacaoDeMembroEmProjeto(membro) != null) {
			
			membros.add(membro);
		} else {
			throw new Exception("O membro ja existe");
		}
	}

	/*
	 * Antes de remover um membro, verifica-se a exist�ncia do mesmo nesse grupo, e
	 * caso o resultado seja verdadeiro, retorna-se o membro encontrado. A partir do mesmo 
	 * pode-se acessar suas participacoes para s� ent�o, mudar a data de termino de sua participacao
	 * para o dia de hoje e assim desativar suas participacoes (ativo = false).
	 */
	public void removerMembro(Membro membro) throws Exception{
		
		Membro membroRecebido = ValidadoraMembros.validarExistenciaDeMembro(membros, membro);
		
		if(membroRecebido != null) {
			
			for(int i = 0; i < membroRecebido.getParticipacoes().size(); i++) {
				
				Participacao participacao = membroRecebido.getParticipacoes().get(i);
				participacao.setDataTermino(LocalDate.now().plusDays(1));	//a data termino setada � a data de hoje como acrescimo de um dia
				participacao.desativar();									//por conta da verificacao feita na participacao
			}
		}else {
			throw new Exception("Membro n�o existente");
		}
	}
			
	//		Esse metodo verifica se o membro possui uma participacao em um dos projetos 
	//desse grupo, ja que para poder adicionar um membro a esse grupo, necessita
	//fazer tal verificacao.
	private Membro validarParticipacaoDeMembroEmProjeto(Membro membro) throws Exception{
		
		for(int i = 0; i < projetos.size(); i++) {
			
			ArrayList<Membro> membrosDeProjetoRecebido = projetos.get(i).getMembros();
			
			for(int j = 0; j < membrosDeProjetoRecebido.size(); j++) {
				
				if(membrosDeProjetoRecebido.get(j).getMatricula() == membro.getMatricula()) {
					
					return membrosDeProjetoRecebido.get(j);
				}
			}
			
		}
		return null;
	}

	//esses metodos abaixo retornam ou adicionam os atributos dessa classe
	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getLinkCNPq() {
		return linkCNPq;
	}

	public void setLinkCNPq(String linkCNPq) {
		this.linkCNPq = linkCNPq;
	}

	public ArrayList<ItemProjeto> getProjetos() {
		return projetos;
	}

	public ArrayList<Membro> getMembros() {
		return membros;
	}
}