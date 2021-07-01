package facades.casosdeuso;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

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
	
	public static void main(String[] args) {
		DAOXMLMembroConta daoxmlMembroConta = new DAOXMLMembroConta();
		FacadeCasoDeUso1 facadeCasoDeUso1 = new FacadeCasoDeUso1();
		
		facadeCasoDeUso1.cadastrarConta("admin", 123456789l, "admin@admin.com", "admin123");
			
		try {
			System.out.println(daoxmlMembroConta.consultarPorMatricula(123456789l));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				if(setarAtributos(membro, matricula, nome, login, senha)) {					
					membro.ativar();
					
					Set<Membro> provisorio = daoxmlMembroConta.getTodosRegistros();
					
					membro.setAdministrador(provisorio.isEmpty()); // Se nao houver nenhum membro cadastrado esta instancia de membro será o administrador.
					
					daoxmlMembroConta.criar(membro);
					
					myLogger.info(this.getClass() + " - Membro Criado e gravado com sucesso!");
					
					return true;
				}
			}
			
			myLogger.warning(this.getClass() +" Formato de Login invalido");
			
		} catch(Exception e) {
			myLogger.severe(e.toString());
		}
			
		return false;
	}
	
	public boolean atualizar(long matricula , String nome, String login, String senha) {
		
		Membro membro = null;
		
		try {
			// Localiza e recebe o membro com a matricula pesquisada.
			membro = daoxmlMembroConta.consultarPorMatricula(matricula);
		
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
	private boolean setarAtributos(Membro membro, long matricula, String nome, String email, String senha) {
		setarMatricula(membro, matricula);
		setarNome(membro, nome);
		setarEmail(membro, email);
		setarSenha(membro, senha);
		
		return true;
	}
	
	public boolean setarMatricula(Membro membro, long matricula) {
		// Matrículas de membros deve ser um valor numérico de 9 dígitos
		if(Long.toString(matricula).length() != 9)
			return false;
		
		membro.setMatricula(matricula);
		return true;
	}

	public boolean setarNome(Membro membro, String nome) {
		// Nomes devem ter entre 15 e 60 caracteres
		if(nome.length() < 15 || nome.length() > 60)
			return false;
		
		membro.setNome(nome);
		return true;
	}

	public boolean setarEmail(Membro membro, String email) {
		// E-mails devem obedecer o formato convencional
		if(!ValidadoraFormatoEmailLogin.validarLoginComum(email))
			return false;
		
		membro.setEmail(email);
		return true;
	}

	public boolean setarSenha(Membro membro, String senha) {
		// Senhas devem ter entre 6 e 8 caracteres
		if(senha.length() < 6 || senha.length() > 8)
			return false;
		
		membro.setSenha(senha);
		return true;
	}
	
	
	public Membro[] getTodosOsMembros() {
		Set<Membro> membrosRegistrados = daoxmlMembroConta.getTodosRegistros();
		myLogger.info("[XMLMembroConta] - Recuperou membros registrados");
		return membrosRegistrados.toArray(new Membro[membrosRegistrados.size()]);
	}
}
