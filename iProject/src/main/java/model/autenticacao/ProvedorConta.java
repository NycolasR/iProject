package model.autenticacao;

/**
 * @author NPG
 * Supertipo uniformizador para implementações para formas
 * de autenticacao. E usado pelas abstracoes e nao se acopla
 * a elas. Faz parte da implementacao do padrao Bridge.
 */
/*
 * TODO FEITO
 * O nome desta classe de implementacao deveria remeter a ideia
 * do aspecto que varia que eh o tipo de provedor de autenticacao de conta.
 * Sugestao de nome: ProvedorConta.
 */
public abstract class ProvedorConta {
	
	/*
	 * TODO FEITO
	 * Este enum poderia ser realocado para Membro. Codigo da fachada em operacoes que usem ou precisem devidir
	 * qual a implementacao de provedo de conta devem setar tal classe de implementacao com base neste enum, configurado no membro.
	 * TODO FEITO [UML] Acrescentar atributo em Membro e removê-lo de Conta
	 */
	private String login;
	private String senha;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	/**
	 * 
	 * @param login o login informado que deve estar no registro
	 * @param senha a senha passada como parametro
	 * @param isSenhaEncriptada atributo que determina se a senha informada deve ser encriptada
	 * @return true se o login foi bem sucedido e false caso contrario
	 */
	public abstract boolean autenticar(String login, String senha, boolean isSenhaEncriptada);
}
