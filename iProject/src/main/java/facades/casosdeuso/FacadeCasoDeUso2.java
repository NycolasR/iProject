package facades.casosdeuso;

import java.util.logging.Logger;

import model.autenticacao.Conta;
import model.autenticacao.ContaAutenticacaoProvedorEmailSMTP;
import model.autenticacao.ContaAutenticacaoProvedorInterno;
import model.autenticacao.ContaEmailIFPB;
import model.autenticacao.ContaEmailLivre;
import model.autenticacao.Membro;
import model.autenticacao.ProvedorConta;
import model.autenticacao.RegistradorSessaoLogin;
import model.autenticacao.TipoProvedorAutenticacao;
import persistencia.DAOXMLMembroConta;
import util.MyLogger;
import util.ValidadoraFormatoEmailLogin;

/**
 * @author NPG
 *
 * 	A implementação do padrão facade facilita aos usuários o uso de funções que exigiriam um esforço e conhecimento desnecessário sobre os métodos
 * 	disponibilizados no programa. Para o caso de uso 2, é simplificada a função de fazer login tornandoa simples e direta sem a necessidade de um 
 * 	conhecimento prévio ou amplo sobre o método e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 *
 */
public class FacadeCasoDeUso2 {

	private DAOXMLMembroConta daoxmlMembroConta;
	private RegistradorSessaoLogin registradorSessaoLogin;
	
	private Logger myLogger = MyLogger.getInstance();
	
	
	//A sobrescrita do construtor vai setar as instancias nos atributos desta fachada
	public FacadeCasoDeUso2() {
		daoxmlMembroConta = new DAOXMLMembroConta();
		registradorSessaoLogin = RegistradorSessaoLogin.getInstance();
	}
	
	public boolean fazerLogin(String email,
			String senha,
			TipoProvedorAutenticacao tipo)  {
		
		// Resgatando o usuario dos usuarios registrados
		Membro membro;
		try {
			membro = daoxmlMembroConta.getMembroPeloLogin(email);
			
			// Especificando a abstracao e conta que o usuario preferir
			ProvedorConta conta =
					tipo == TipoProvedorAutenticacao.INTERNO ?
							new ContaAutenticacaoProvedorInterno() : new ContaAutenticacaoProvedorEmailSMTP();
							
							Conta contaAbstracao = identificarConta(email);
							
							// Setando os atributos da implementacao de conta
							conta.setLogin(email);
							conta.setSenha(senha);
							
							/*
							 * TODO FEITO 
							 * Este enum poderia ser realocado para Membro. Codigo da fachada em operacoes que usem ou precisem devidir
							 * qual a implementacao de provedo de conta devem setar tal classe de implementacao com base neste enum, configurado no membro.
							 * TODO FEITO [UML] Acrescentar atributo em Membro e removê-lo de Conta
							 */
							membro.setTipo(tipo);
							
							contaAbstracao.setConta(conta);
							membro.setContaAbstracao(contaAbstracao);
							
							// Tentará fazer a autenticacao
							if(contaAbstracao.autenticar(email, senha, false)) {
								registradorSessaoLogin.registrarOnline(membro);
								
								myLogger.info(this.getClass() +" "+ email + " fez login com sucesso!");
								
								return true;
							}
							
							myLogger.warning(this.getClass() + " Dados de login invalidos");
							
							
		} catch (Exception e) {
			myLogger.severe(e.toString());
		}
		
		return false;
	}
	
	public boolean isAdministrador(String email)  throws Exception {
		Membro membro = daoxmlMembroConta.getMembroPeloLogin(email);
		
		return membro.isAdministrador();
		
	}
	
		
	/**
	 * Simple factory para fabricar abstracoes de conta com base no email passado como parametro de entrada.
	 * @param email Email para ser validado como um dos tipos de conta.
	 * @return uma nova instancia de um tipo concreto de Conta.
	 * @throws Exception Lanca excecao caso o formato do email informado nao corresponder a nenhum tipo concreto de Conta.
	 */
	private Conta identificarConta(String email) throws Exception {
		if(ValidadoraFormatoEmailLogin.validarLoginIFPB(email))
			return new ContaEmailIFPB();
		
		else if(ValidadoraFormatoEmailLogin.validarLoginComum(email))
			return new ContaEmailLivre();
		
		throw new Exception("Formato de email inválido.");
	}
}
