package model.autenticacao;

import java.io.Serializable;
import java.util.ArrayList;

import model.projetos.Participacao;
import util.ValidadoraFormatoEmailLogin;

/**
 * @author NPG
 *
 */
public class Membro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long matricula;
	private String nome;
	private boolean ativo;
	private String email;
	private boolean administrador;
	private String senha;

	private ArrayList<Participacao> participacoes = new ArrayList<Participacao>();

	
	
	public void adicionarParticipacao(Participacao participacao) {
//		Esse metodo foi criado para tratar a operacao da classe Projeto 
//		na parte do composite do projeto.
		participacoes.add(participacao);

	}

	public ArrayList<Participacao> getParticipacoes(){
//		Esse metodo foi criado para ajudar na implementacao do composite, mais precisamente
//		no metodo remover membro da classe Grupo.
		return participacoes;
	}
	
	/*
	 * TODO FEITO
	 * Este enum poderia ser realocado para Membro. Codigo da fachada em operacoes que usem ou precisem devidir
	 * qual a implementacao de provedo de conta devem setar tal classe de implementacao com base neste enum, configurado no membro.
	 * TODO FEITO [UML] Acrescentar atributo em Membro e removÃª-lo de Conta
	 */
	private TipoProvedorAutenticacao tipo;
	public TipoProvedorAutenticacao getTipo() {
		return tipo;
	}
	public void setTipo(TipoProvedorAutenticacao tipo) {
		this.tipo = tipo;
	}

	private Conta contaAbstracao;

	public long getMatricula() {
		return matricula;
	}
	public void setMatricula(long matricula) {
		if(Long.toString(matricula).length() != 9)
			throw new UnsupportedOperationException("[ERRO] As matrículas devem ter 9 dígitos.");
			
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}
	public boolean setNome(String nome) {
		if(nome.length() < 15 || nome.length() > 60)
			throw new UnsupportedOperationException("[ERRO] Nomes de membros devem ter entre 15 e 60 caracteres.");
			
		this.nome = nome;
		return true;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(!ValidadoraFormatoEmailLogin.validarLoginComum(email))
			throw new UnsupportedOperationException("[ERRO] O email não possui um formato válido");
			
		this.email = email;
	}

	public boolean isAdministrador() {
		return administrador;
	}
	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public Conta getContaAbstracao() {
		return contaAbstracao;
	}
	public void setContaAbstracao(Conta contaAbstracao) {
		this.contaAbstracao = contaAbstracao;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		if(senha.length() < 6 || senha.length() > 8)
			throw new UnsupportedOperationException("[ERRO] Senhas de membros devem ter entre 6 e 8 caracteres.");
			
		
		this.senha = senha;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void ativar() {
		this.ativo = true;
	}

	public void desativar() {
		this.ativo = false;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
