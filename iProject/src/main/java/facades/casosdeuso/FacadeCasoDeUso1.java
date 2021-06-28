package facades.casosdeuso;

import java.util.Set;
import java.util.logging.Logger;

import model.autenticacao.Membro;
import persistencia.DAOXMLMembroConta;
import util.MyLogger;
import util.ValidadoraFormatoEmailLogin;

/**
 * @author NPG
 *
 *	A implementação do padrão facade facilita aos usuários o uso de funções que exigiriam um esforço e conhecimento desnecessário sobre os métodos
 * 	disponibilizados no programa. Para o caso de uso 1, é simplificado a relação de criar um membro, criar uma conta, e atualizar suas informações de forma simples e direta,
 * 	sem a necessidade de um conhecimento prévio ou amplo sobre os métodos e eliminando a necessidade de ter contado direto com todos os objetos envolvidos.
 *
 */
public class FacadeCasoDeUso1 {

	private DAOXMLMembroConta daoxmlMembroConta;
	
	private Logger myLogger = MyLogger.getInstance();
	
	//A sobrescrita do construtor vai criar as instâncias dos atributos dessa fachada
	public FacadeCasoDeUso1() {
		daoxmlMembroConta = new DAOXMLMembroConta();
	}
	
	public boolean cadastrarConta(
			String nome,
			long matricula,
			String login,
			String senha) {
		
		try {
			// Verifica se o email é valido.
			if(ValidadoraFormatoEmailLogin.validarLoginComum(login)) {
				Membro membro = new Membro();
				setarAtributos(membro, matricula, nome, login, senha);
				membro.ativar();
				
				Set<Membro> provisorio = daoxmlMembroConta.getTodosRegistros();
				
				membro.setAdministrador(provisorio.isEmpty()); // Se nao houver nenhum membro cadastrado esta instancia de membro será o administrador.
				
				daoxmlMembroConta.criar(membro);
				
				myLogger.info(this.getClass() + " - Membro Criado e gravado com sucesso!");
				
				return true;
			}
			
			myLogger.warning(this.getClass() +" Formato de Login invalido");
			
		}catch(Exception e) {
			myLogger.severe(e.toString());
		}
			
		return false;
	}
	
	public boolean atualizar(long matricula , String nome, String login, String senha) {
		
		Membro membro = null;
		
		try {
			// Localiza e recebe o membro com a matricula pesquisada.
			membro = daoxmlMembroConta.consultarPorID(matricula);
		
			// Seta os novos valores dos atributos do membro.
			setarAtributos(membro, matricula,  nome, login , senha);		
		
			daoxmlMembroConta.atualizar(matricula, membro);
			
			myLogger.info(this.getClass() +" - Membro Atualizado com sucesso!");
			
			return true;
		} catch (Exception e) {
			myLogger.severe(e.toString());
		}
		
		return false;
	}
	
		
	// Método que seta os valores dos atributos de um membro.
	// TODO FEITO [UML] alterar nome
	private void setarAtributos(Membro membro, long matricula ,String nome, String login, String senha) {
		membro.setNome(nome);
		membro.setEmail(login);
		membro.setSenha(senha);
		membro.setMatricula(matricula);
	}
	
	public Membro[] getTodosOsMembros() {
		Set<Membro> membrosRegistrados = daoxmlMembroConta.getTodosRegistros();
		myLogger.info("[XMLMembroConta] - Recuperou membros registrados");
		return membrosRegistrados.toArray(new Membro[membrosRegistrados.size()]);
	}
}
