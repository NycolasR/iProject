package model.projetos;

import java.time.LocalDate;
import java.util.ArrayList;

import model.autenticacao.Membro;
import util.ValidadoraDatas;
import util.ValidadoraMembros;

/**
 * @author NPG
 *
 *Essa classe faz parte da implementação do padrão composite como sendo um tipo 
 *concreto e uma classe de objeto composto, capaz de possuir participações, que é 
 *um outro tipo de objeto simples. Ela evita conhecer o tipo concreto
 *de participação, então faz uso do supertipo para armazenar participações.
 *
 */
public class Projeto extends ItemProjeto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float aporteCusteioReais;
	private float aporteCapitalReais;
	private float gastoExecutadoCusteioReais;
	private float gastoExecutadoCapitalReais;
	private ArrayList<Membro> membros = new ArrayList<Membro>();
	private ArrayList<ItemProjeto> participacoes = new ArrayList<ItemProjeto>();
	/**
	 * Os atributos grupo e edital foram criados, pois no diagrama de casos de uso esta
	 * explicito a atribuicao de um Edital e um Grupo e um projeto. 
	 */
	private ItemProjeto grupo;
	private ItemProjeto edital;
	

	/**
	 * Esse construtor seta na superclasse o tipo de item projeto correspondente a essa classe
	 */
	public Projeto () {
		super.setTipoItemProjeto(TiposItemProjeto.PROJETO);
	}
	
	/**
	 * Os metodos a baixo s�o implementacoes com propagacao de chamada de metodo.
	 * Sao as implementacoes dos metodos abstratos da superclasse
	 */
	public void ativar() {
		for(int i = 0; i < participacoes.size(); i++) {
			participacoes.get(i).ativar();
		}

		ativarUltimasParticipacoesDeCadaMmebro();
		super.setAtivo(true);
	}

	public void desativar() {
		for(int i = 0; i < participacoes.size(); i++) {
			participacoes.get(i).desativar();
		}
		
		super.setAtivo(false);
	}

	/**
	 * O custo total nesse caso, eh o valor total somado de cada participacao do projeto mais os gastos de custeio a parte.
	 * Como os mEtodos abaixo [getCustoTotal(), getCusteioReaisNaoGastoTotal(), getCapitalReaisNaoGastoTotal()],
	 * no proprio UML est� especificado que deve ter propagacao de chamada, 
	 * ent�o os calculos para os mesmos devem ser direcionados para a classe em que o projeto
	 * est� se relacionando, que nesse caso eh a classe Participacao (ja que o projeto possui
	 * varias participacoes). Os atributos que somente o projeto possui, vao ser utilizados a parte, 
	 * por exemplo, nao posso fazer o metodo getCusteioReaisNaoGastoTotal() retornar o 
	 * aporteCusteioReais - gastoExecutadoCusteioReais, pois o mesmo perderia sua ess�ncia, uma vez que 
	 * esse metodo precisa fazer propagacao de chamada.
	 */
	
	public float getCustoTotal() {
		float valorTotal = 0;
		
		for(int i = 0; i < participacoes.size(); i++) {
			valorTotal += participacoes.get(i).getCustoTotal();
		}
		
		/*
		 * o valor retornado na propagação da chamada, eh o tatal de custos para pagar as participacoes de cada membro
		 * nesse projeto, mas além desse pagamento, podem existir outos tipos de custo para custeio, por exemplo,
		 * pagamento de comida ou transporte. Assim sendo o custo total retornado por esse método é valor 
		 * total retornado da propagação da chamada, mais os gastosExecutadoCusteioReais.
		 */
		return valorTotal + gastoExecutadoCusteioReais;
	}

	/*
	 * No caso do custeio nao gasto total, o valor deriva 
	 * da soma dos custeios nao gastos de cada participacao. 
	 */
	public float getCusteioReaisNaoGastoTotal() {
		float valorCusteioReaisNaoGastoTotal = 0;
		
		for(int i = 0; i < participacoes.size(); i++) {
			valorCusteioReaisNaoGastoTotal += participacoes.get(i).getCusteioReaisNaoGastoTotal();
		}	
		/*
		 * No caso desse metodo, como ele precisa fazer a propagacao da chamada tambem, o mesmo
		 * calcula o valor de custeio reias não gastos com as participações dos membros nesse 
		 * projeto, depois disso, eu preciso diminuir aporteCusteioReais todos os gastos do 
		 * projeto ate o momento, e os gastos totais, é a soma do gastoExecutadoCusteioReais
		 * mais o pagamento das participações dos membros do projeto, então para adquirir esse valor
		 * eu utilizo do método getCustoTotal(). Porém ele me retorna a soma de todo os meses
		 * que o projeto vai pagar as participações dos membros, no entanto, eu só quero os 
		 * pagamentos das participações feitos até hoja, para isso eu subtraio do getCustoTotal(),
		 * o valorCusteioReaisNaoGastoTotal, que é a quantidade de dinheiro dos meses que ainda
		 * faltam pagar as participações dos membro, dessa forma, quando eu faço essa operação, 
		 * obtem-se o valor gasto até o momento com o projeto, então posso subtrair esse valor 
		 * do aporte custeioReais para obter o custeioReaisNaoGastoTotal.
		 */
		return aporteCusteioReais-(getCustoTotal()-valorCusteioReaisNaoGastoTotal);
	}

	/*
	 * O capital nao gasto sera, o capital que o projeto possui subtraido do gastoExecutatoCapitalReais.
	 * como podemos ver no m�todo abaixo.
	 */
	public float getCapitalReaisNaoGastoTotal() {
		return aporteCapitalReais - gastoExecutadoCapitalReais;
	}
	
	/*
	 * Como o projeto contem uma referencia para um edital e para um grupo,
	 * criou-se o metodo setItemProjeto que recebe dois item projeto como
	 * par�metro de entrada, onde o cliente tem que tomar o cuidado de passar um 
	 * item projeto que seja um edital e um que seria um grupo respectivamente, pois
	 * as condicoes abaixo nao fazem instanceOf para evitar o acoplamento, apenas 
	 * fazem verificacoes basicas para adicionar os mesmos.
	 * PS: Essa classe nao permite remover um item projeto, como visto no UML.
	 * 
	 * TODO [CODIGO] agora é possível verificar o tipo de item projeto, atraves do atripo tipoItemProjeto
	 * sem precisar fazer classes componentes se acoplarem a outras, e não precisa do setItemProjeto
	 */
	
	public void adicionarItemProjeto(ItemProjeto itemProjeto) throws Exception {
		if(itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.EDITAL.getValor() && 
				ValidadoraDatas.validarExistenciaDeDataEntreDuasDatas(itemProjeto.getDataInicio(), itemProjeto.getDataTermino())) {
			
			if(edital != null ) {
				throw new Exception("Esse projeto já possui em edital");
			}
			
			this.edital = itemProjeto;
			
		} else if(grupo == null && itemProjeto.getTipoItemProjeto().getValor() == TiposItemProjeto.GRUPO.getValor()) {
			this.grupo = itemProjeto;
		}
	}
	
	/*
	 * No metodo abaixo. Verifica-se a existencia de um membro antes de adiciona-lo aos membros do 
	 * projeto, dessa forma ao ser adicionado um novo membro ao projeto,
	 * cria-se uma nova participacao para aquele membro nesse projeto com os parametros de entrada passados.
	 * Caso o membro ja exista apenas se cria uma nova participacao aquele membro, ja que 
	 * um projeto pode ter varios membros, e um membro pode ter varias participacoes.
	 */
	public void adicionarMembro(Membro membro, LocalDate dataInicio, LocalDate dataTermino, boolean isCordenador,
								float aporteCusteioReaisMensal)throws Exception{
		
		Membro membroRetornado = ValidadoraMembros.validarExistenciaDeMembro(membros, membro);
		Participacao participacao = criarParticipacao(dataInicio, dataTermino, aporteCusteioReaisMensal, isCordenador);		
		
		desativarParticipacoesDeUmMembro(membro);
		participacao.setMembro(membro);
		participacoes.add(participacao);			//Eu adiciono a referencia do membro a sua participacao
		membro.adicionarParticipacao(participacao);  
		
		if(membroRetornado == null) {
			membros.add(membro);
		}
	}

	/*
	 * Da mesma forma que o adicionar, o remover verifica a existencia de um membro
	 * para poder remove-lo do projeto e suas participacoes tambem
	 */
	public void removerMembro(Membro membro) throws Exception {
		Membro membroRetornado = ValidadoraMembros.validarExistenciaDeMembro(membros, membro);
		
		if(membroRetornado != null) {
			removerParticipacoesDeUmMembro(membroRetornado);
			membros.remove(membroRetornado);
		} else {
			throw new Exception("Membro nao encontrado nesse projeto");
		}
	}
	
	/*
	 * O metodo a baixo vai mover o geupo desse projeto para outro grupo
	 * passado como parametro de entrada. 
	 */
	public void mover(ItemProjeto grupo) {
		this.grupo = grupo;
	}
		
	/*
	 * O metodo posterior recebe um membro e remove todas as participacoes desse projeto que fazem
	 * referencia a esse membro, ou seja, remove as participacoes desse membro nesse projeto.
	 */
	private void removerParticipacoesDeUmMembro(Membro membro) throws Exception {
		for(int i = 0; i < participacoes.size(); i++) {
			if(((Participacao)participacoes.get(i)).getMembro().getMatricula() == membro.getMatricula()) {
				membro.getParticipacoes().remove(participacoes.get(i));
				
				//se der algum erro aqui, desative as participacoes, mas antes, set a data t�rmino como sendo a de hoje + 1 dia
				participacoes.remove(i);
				i = i-1;
			}
		}
	}
	
//	O metodo abaixo cria uma nova participacao a um nvo membro do projeto, ou a um membro ja existente.
	private Participacao criarParticipacao(LocalDate dataInicio, LocalDate dataTermino,
											float aporteCusteioReaisMensal, boolean isCordenador) throws Exception{
		
		Participacao participacao = new Participacao();
		/*
		 * A condicao abaixo valida se a data de inicio vem depois da data de termino.
		 * E se a data de hoje vem antes da data de início, pois nao faz sentido 
		 * uma participação comecar em uma data que é anterior a data de hoje, ou seja, uma
		 * data que ja se passou. 
		 * Se sim lanca excecao, senao cria a participacao.
		 */
		
		if(participacoes.size() > 0) {
			
			LocalDate dataInicioProjeto = participacoes.get(0).getDataInicio();
			LocalDate dataTerminoProjeto = participacoes.get(0).getDataTermino();
			
			if(dataTerminoProjeto.isBefore(LocalDate.now())) {
				
				throw new Exception("O projeto se encontra finalizado");
			
			}else if(dataInicio.isBefore(dataInicioProjeto) || dataTermino.isBefore(dataInicioProjeto) || dataInicio.isAfter(dataTerminoProjeto) || dataTermino.isAfter(dataTerminoProjeto)){
				
				throw new Exception("Datas de início e término fora da vigência do projeto");
			}
		}
		
		
		if(ValidadoraDatas.validarDataHojeMenorQueDataRecebida(dataInicio) && dataInicio.isBefore(dataTermino)) {
																													
			participacao.setDataInicio(dataInicio);
			participacao.setDataTermino(dataTermino);
			participacao.setAporteCusteioMensalReais(aporteCusteioReaisMensal);
			participacao.setCoordenador(isCordenador);
			participacao.ativar();
			
			return participacao;
		}
		throw new Exception("Data inválida");
	}
	
	/**
	 * Esse método privado torna todas as participações de um membro desativadas nesse projeto
	 */
	private int desativarParticipacoesDeUmMembro(Membro membro) {
		
		int posicao = -1;
		
		for(int i = 0; i < participacoes.size(); i++) {
			Participacao participacao = (Participacao) participacoes.get(i);
			if(participacao.getMembro().getMatricula() == membro.getMatricula()) {
				participacao.setAtivo(false);
				posicao = i;
			}
		}
		
		return posicao;
	}
	
	private void ativarUltimasParticipacoesDeCadaMmebro() {
		
		for(int i = 0; i < membros.size(); i++) {
			
			int posicao = desativarParticipacoesDeUmMembro(membros.get(i));
			
			if(posicao != -1) {
				participacoes.get(posicao).ativar();
			}
			
		}
	}
	
	/*
	 * O método a baixo, retorna o membro que é coordenador desse projeto, e
	 * lança excessão caso o mesmo não seja encontrado
	 */
	public Membro getCoordenador() throws Exception{
		
		for(int i = 0; i < participacoes.size(); i++) {
			
			if(participacoes.get(i).isCoordenador()) {
				
				return ((Participacao)participacoes.get(i)).getMembro();
			}
		}
		throw new Exception("Membro não encontrado");
	}
	
	
	

	/*
	 * Os m�todos a baixo s�o particulares dessa classe, j� que o coordenador adiciona 
	 * os valores antecipadamente desses atributos do projeto
	 */	 
	public void setAporteCapitalReais(float aporteCapitalReais) {
		
		this.aporteCapitalReais = aporteCapitalReais;
	}
	
	public ArrayList<Membro> getMembros (){
		
		return membros;
	}
	
	public ArrayList<ItemProjeto> getParticipacoes() {
		
		return participacoes;
	}

	public ItemProjeto getGrupo() {
		
		return grupo;
	}

	public void setGrupo(ItemProjeto grupo) {
		
		this.grupo = grupo;
	}

	public ItemProjeto getEdital() {
		
		return edital;
	}

	public void setEdital(ItemProjeto edital) {
		
		this.edital = edital;
	}

	public float getAporteCusteioReais() {
				
		return aporteCusteioReais;
	}

	public void setAporteCusteioReais(float aporteCusteioReais) {
		
		this.aporteCusteioReais = aporteCusteioReais;
	}

	public float getGastoExecutadoCusteioReais() {
		
		return gastoExecutadoCusteioReais;
	}

	public void setGastoExecutadoCusteioReais(float gastoExecutadoCusteioReais) {
		
		this.gastoExecutadoCusteioReais = gastoExecutadoCusteioReais;
	}

	public float getGastoExecutadoCapitalReais() {
		
		return gastoExecutadoCapitalReais;
	}

	public void setGastoExecutadoCapitalReais(float gastoExecutadoCapitalReais) {
		
		this.gastoExecutadoCapitalReais = gastoExecutadoCapitalReais;
	}

	public float getAporteCapitalReais() {
		
		return aporteCapitalReais;
	}
}
