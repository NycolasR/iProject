package model.projetos;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;


import model.autenticacao.Membro;
import ponto.model.projetos.HorarioPrevistoParteExtrinseca;
import ponto.model.projetos.PontoTrabalhado;
import util.ValidadoraDatas;

/**
 * @author NPG
 *
 *Essa classe faz parte da implementação do padrão composite como sendo um tipo 
 *concreto e uma classe de objeto simples. classes de objetos compostos podem possuir
 *objetos simples dessa classe, tratando-a pelo super tipo também
 */
public class Participacao extends ItemProjeto implements Comparable<Participacao> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static long idParticipacao = 1;
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private float aporteCusteioMensalReais;
	private short qtdMesesCusteados;
	private short qtdMesesPagos;
	private boolean coordenador ;
	private Membro membro;
	private ArrayList<HorarioPrevistoParteExtrinseca> horariosPrevistos = new ArrayList<HorarioPrevistoParteExtrinseca>();
	private ArrayList<PontoTrabalhado> pontosTrabalhados = new ArrayList<PontoTrabalhado>();
	
	/**
	 * Construtor sobrescrito para setar ao codigo de participacao um id do tipo long
	 * e setar o tipo de item correspondente a essa classe
	 */
	public Participacao() {
		super.setCodigo(idParticipacao++);
		super.setTipoItemProjeto(TiposItemProjeto.PARTICIPACAO);
	}
	
	/*
	 * O metodo abaixo modifica o atributo ativo para verdadeiro caso o metodo 
	 * validarParaAtivarOuDesativar(dataTermino) retorne true;
	 */
	public void ativar() {
		if(ValidadoraDatas.validarDataHojeMenorQueDataRecebida(dataTermino)) {
			super.setAtivo(true);
		}	
	}

	/*
	 *O metodo abaixo modifica o atributo ativo para falso caso o metodo  
	 *validarParaAtivarOuDesativar(dataTermino) retorne false
	 */
	public void desativar() {
		if(!ValidadoraDatas.validarDataHojeMenorQueDataRecebida(dataTermino)) {
			super.setAtivo(false);
		}
	}

	/*
	 * Esse metodo retorna o custo total dos gastos pela participacao de um membro em um projeto, ou seja,
	 * o valor a ser pago de cada mes, multiplicado pela quantidade de meses que o projeto vai durar.
	 */
	public float getCustoTotal() {	 	
		return getAporteCusteioMensalReais() * getQtdMesesCusteados();
	}

	/*
	 * O metodo a baixo retorna o custo total subtraido da quantidade de meses decorridos desde que iniciou o projeto,
	 * multiplicado pelo custo de cada mes
	 */
	public float getCusteioReaisNaoGastoTotal() {
				
		//a condicao a abaixo verifica se a data atual ainda eh anterior a data do comeco do projeto, 
		//e caso seja, nao pode-se contar o total gasto ate o momento
		if(!ValidadoraDatas.validarDataHojeMenorQueDataRecebida(dataInicio)) {
			return getCustoTotal()-getQtdMesesPagos() * getAporteCusteioMensalReais();
		}
		
		return getCustoTotal();
	}

	/*
	 * Os metodos a baixo foram sobrescritos, ja que na superclasse sao abstratos, no entanto
	 * lancam excessao, ou retornam 0, pois nao fazem parte da classe Participacao.
	 */
	public float getCapitalReaisNaoGastoTotal() {
		return 0;
	}
	
	public String getNome() {
		return null;
	}
	
	public void setNome(String nome) {}
	
		
	/*
	 * O metodo privado (esta privado pois nao queremos que "todos" tenham
	 * acesso ao mesmo) abaixo, faz a validacao da data a partir da data de hoje. 
	 * Caso a data de hoje seja anterior ou igual a data passada, o m�todo
	 * retorna true, caso nao seja, retorna false.
	 */
		
	//Os metodos abaixo sao particulares dessa classe, pois retornam e adicionam os atributos pertencentes a mesma.
		
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
	
	public boolean isCoordenador() {
		return coordenador;
	}

	public void setCoordenador(boolean coordenador) {
		this.coordenador = coordenador;
	}

	public short getQtdMesesCusteados() {
		 // a formula a baixo calcula a quantidade de meses exixtente entre uma data e outra.
		qtdMesesCusteados = (short) Period.between(dataInicio, dataTermino).getMonths();
		
		return qtdMesesCusteados;
	}

	public short getQtdMesesPagos() {
		// a formula a baixo calcula a quantidade de meses exixtentes entre uma data e outra.
		qtdMesesPagos = (short) Period.between(dataInicio, LocalDate.now()).getMonths();
		
		if(qtdMesesPagos < 0) {
			
			qtdMesesPagos = 0;
		}
		
		return qtdMesesPagos;
	}
	
	public void setAporteCusteioMensalReais(float aporteCusteioMensalReais) {
		this.aporteCusteioMensalReais = aporteCusteioMensalReais;
	}
	
	public float getAporteCusteioMensalReais() {
		return aporteCusteioMensalReais;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}
		
	public ArrayList<HorarioPrevistoParteExtrinseca> getHorariosPrevistos() {
		return horariosPrevistos;
	}

	public void addHorarioPrevisto(HorarioPrevistoParteExtrinseca horarioPrevisto) {
		horariosPrevistos.add(horarioPrevisto);
	}

	public ArrayList<PontoTrabalhado> getPontosTrabalhados() {
		return pontosTrabalhados;
	}

	public void addPontoTrabalhado(PontoTrabalhado pontoTrabalhado) {
		pontosTrabalhados.add(pontoTrabalhado);
	}

	@Override
	public void setCodigo(long codigo) {
		throw new UnsupportedOperationException("Não é permitido setar código nesse objeto");
	}

	// TODO usar isso pra ordenar as participacoes em projeto
	@Override
	public int compareTo(Participacao arg0) {
		if(this.dataInicio.isBefore(arg0.getDataInicio()))
			return -1;
		else if(this.dataInicio.equals(arg0.getDataInicio()))
			return 0;
		else // this.dataInicio.isAfter(arg0.getDataInicio())
			return 1;
	}
}
