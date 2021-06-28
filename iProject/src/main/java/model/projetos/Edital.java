package model.projetos;

import java.time.LocalDate;
import java.util.ArrayList;

import util.ValidadoraDatas;
import util.ValidadoraItemProjeto;

/**
 * 
 * @author NPG
 *
 * Essa classe faz parte da implementação do padrão composite como sendo um tipo 
 * concreto e uma classe de objeto composto, capaz de possuir mais de um tipo de 
 * objeto composto, que são Grupos e Projetos, no entanto ela evita conhecer o tipo concreto
 * desses objetos compostos, então faz uso do supertipo para armazená-los.
 */
public class Edital extends ItemProjeto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private ArrayList<ItemProjeto> projetos = new ArrayList<ItemProjeto>();
	private ArrayList<ItemProjeto> grupos = new ArrayList<ItemProjeto>();
	

	/**
	 * Esse construtor seta na superclasse o tipo de item projeto correspondente a essa classe
	 */
	public Edital () {
		super.setTipoItemProjeto(TiposItemProjeto.EDITAL);
	}
	
	/*
	 * Os metodos a baixo s�o implementacoes com propagacao de chamada de metodo.
	 * Sao as implementacoes dos metodos abstratos da superclasse
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
		float valorCusteioReaisNaoGastos = 0;
		
		for(int i = 0; i < projetos.size(); i++) {
			valorCusteioReaisNaoGastos += projetos.get(i).getCusteioReaisNaoGastoTotal();
		}
		
		return valorCusteioReaisNaoGastos;
	}

	public float getCapitalReaisNaoGastoTotal() {
		float valorCapitalReaisNaoGastos = 0;
		
		for(int i = 0; i < projetos.size(); i++) {
			valorCapitalReaisNaoGastos += projetos.get(i).getCapitalReaisNaoGastoTotal();
		}
		return valorCapitalReaisNaoGastos;
	}
	
	/*
	 * O metodo abaixo recebe um grupo ou um projeto, ou os dois de uma vez s�,
	 * caso seja um um projeto deve-se validar a exist�ncia dele no edital, caso seja 
	 * um grupo al�m de verificar a existencia dele no edital, valida-se
	 * tamb�m a existencia desse grupo em um projeto do edital antes de adicionar. 
	 * E como percebe-se, nao foram utilizados instanceof's para evitar acoplamento, dessa
	 * forma o cliente deve ter cuidado para n�o passar um item projeto incompativel 
	 * no par�metro de entrada.
	 * 
	 * TODO [CODIGO] agora é possível verificar o tipo de item projeto, atraves do atripo tipoItemProjeto
	 * sem precisar fazer classes componentes se acoplarem a outras
	 */
	
	
	public void adicionarItemProjeto(ItemProjeto itemProjeto) throws Exception {
		
		if(dataTermino.isBefore(LocalDate.now())) {
			
			throw new Exception("Não é possível adicionar um projeto ou grupo (Edital fora de vigência)");
		}

		if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.PROJETO.getValor() &&
				ValidadoraDatas.validarExistenciaDeDataEntreDuasDatas(dataInicio, dataTermino)) {
			
			if(ValidadoraItemProjeto.validarExistenciaDeItemProjeto(projetos, itemProjeto) != null) {
				
				throw new Exception("Esse edital já possui esse projeto");
			}
			
			itemProjeto.adicionarItemProjeto(this);
			projetos.add(itemProjeto);
			
		} else if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.GRUPO.getValor()&& 
				ValidadoraItemProjeto.validarExistenciaDeItemProjeto(grupos, itemProjeto) == null &&
				validarExistenciaDeGrupoEmUmProjeto(itemProjeto) != null) {
			
			grupos.add(itemProjeto);
		}
	}

	/*
	 * O m�todo de remover item projeto, antes de remover, verifica a exist�ncia do 
	 * projeto ou do grupo nesse edital, para s� ent�o remover, caso contr�rio lan�a excess�o.
	 * Assim como o adicionar, o m�todo de remover n�o faz nenhum instanceOf para
	 * evitar acoplamento, dessa forma, o cliente deve tomar cuidado para n�o
	 * passar um item projeto incompat�vel no par�metro de entrada desse m�todo.
	 * 
	 * TODO [CODIGO] agora é possível verificar o tipo de item projeto, atraves do atripo tipoItemProjeto
	 * sem precisar fazer classes componentes se acoplarem a outras
	 */		
	
	
	public void removerItemProjeto(ItemProjeto itemProjeto) throws Exception {

		if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.PROJETO.getValor()) {
			
			ItemProjeto projetoRetornado = ValidadoraItemProjeto.validarExistenciaDeItemProjeto(projetos, itemProjeto);
			
			if(projetoRetornado != null) {
				
				projetos.remove(projetoRetornado);
			}else {
				throw new Exception("Item projeto n�o removido. Projeto n�o encontrado");
			}
		}else if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.GRUPO.getValor()) {
			
			ItemProjeto grupoRetornado = ValidadoraItemProjeto.validarExistenciaDeItemProjeto(grupos, itemProjeto);
			
			if(grupoRetornado != null) {
				
				grupos.remove(grupoRetornado);
			}else {
				throw new Exception("Item projeto n�o removido. Grupo n�o encontrado");
			}
		}
	}
		
	/*
	 * Os m�todos abaixo retornam e adicionam atributos particulares dessa classe.
	 */
	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(LocalDate dataTermino) {
		this.dataTermino = dataTermino;
	}
	
	public ArrayList<ItemProjeto> getProjetos() {
		return projetos;
	}

	public ArrayList<ItemProjeto> getGrupos() {
		return grupos;
	}
		
	/*
	 * Esse m�todo valida a exist�ncia de um grupo em um projeto do edital em quest�o,
	 * al�m disso, ele s� retorna um itemProjeto
	 */
	private ItemProjeto validarExistenciaDeGrupoEmUmProjeto(ItemProjeto itemProjeto) throws Exception{
		
		for(int i = 0; i < projetos.size(); i++) {
			
			ItemProjeto projeto =  projetos.get(i);
			
			if(projeto.getGrupo().getNome().equals(itemProjeto.getNome())) {
				
				return projeto.getGrupo();
			}
		}
		return null;
	}


}
