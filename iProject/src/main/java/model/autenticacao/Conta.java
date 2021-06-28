package model.autenticacao;

/**
 * @author NPG
 * Supertipo para abstracoes que usa uma implementacao de
 * conta.
 * Faz parte da implementacao do padrão Bridge.
 */
/*
 * TODO FEITO
 * O nome desta classe deveria remeter ao tipo abstrativo perante o cliente
 * que eh o conceito de conta. Sugestao de nome: Conta.
 * TODO FEITO [UML] Alterar nome da classe (ContaAbstracao -> Conta)
 */
public abstract class Conta {
	// Referencia para o supertipo de implementacoes de conta
	private ProvedorConta conta;
	
	/*
	 * TODO FEITO
	 * Atualizar pelo novo nome do supertipo da implentacao.
	 */
	public ProvedorConta getConta() {
		return conta;
	}
	
	/*
	 * TODO FEITO
	 * Atualizar pelo novo nome do supertipo da implentacao.
	 */
	public void setConta(ProvedorConta conta) {
		this.conta = conta;
	}
	
	/**
	 * Faz uso da implementacao de Conta vista pelo supertipo para fazer a autenticacao.
	 * @param login o login informado que deve estar no registro
	 * @param senha a senha passada como parametro
	 * @param isSenhaEncriptada atributo que determina se a senha informada deve ser encriptada
	 * @return true se o login foi bem sucedido e false caso contrario
	 */
	public boolean autenticar(String login, String senha, boolean isSenhaEncriptada) {
		// Usando a referencia para o supertipo de implementacoes de conta
		return conta.autenticar(login, senha, isSenhaEncriptada);
	}
	
	/*
	 * TODO FEITO
	 * Seria interessante deixar este metodo abstrato para nao acoplar indicacoes de 
	 * mecanimos de validacao no supertipo de abstracao (esta classe). Dessa forma, novas classes de
	 * abstracoes nao afetariam seu supertipo para utilizar esse aspecto. 
	 * TODO FEITO [UML] Alterar nome atributos e abstact
	 */
	public abstract boolean validarLogin(String login);
}
