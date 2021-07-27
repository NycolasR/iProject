package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import model.autenticacao.Membro;

/**
 * @author NPG
 *
 */
public class DAOXMLMembroConta {

	private HashMap<Long, Membro> persistidos;
	
	private File arquivoColecao;
	
	private XStream xstream;

	public XStream getXstream() {
		return xstream;
	}

	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}
	
	// NESTE CONSTRUTOR, FOI INSTANCIADO O (FILE) ARQUIVOCOLECAO E O XSTREAM, TAMBEM É SETADO AS SUAS RESTRIÇOES DE PERMISSÕES.
	// CASO O AQUIVO XML EXISTA, É USADO O MÉTODO CARREGARXML(), CASO CONTRARIO, UM NOVO HASHMAP<> É CRIADO.
	public DAOXMLMembroConta() {
		arquivoColecao = new File("DAOXMLMembroConta.xml");
		xstream = new XStream(new DomDriver("utf-8"));

		// Restringindo permissoes
		xstream.addPermission(NoTypePermission.NONE);
		xstream.allowTypesByRegExp(new String[] { 
			"model.*",
			"persistencia.*",
			"java.util.*",
			"java.lang.Long",
			"ponto.*"
		});

		if(arquivoColecao.exists()) {
			carregarXML();
		} else {
			persistidos = new HashMap<Long,Membro>();
		}
	}
	
	// O MÉTODO CRIAR() VERIFICA SE EXISTE ALGUM MEMBRO JÁ REGISTRADO COMO O EMAIL OU MATRICULA IGUAIS AOS DO MEMBRO QUE DESEJA SER REGISTRADO.
	// CASO NÃO SEJA DETECTADO CAMPOS IGUAIS, O MEMBRO É ASICIONADO NO HASHMAP, CASO CONTRARIO, IMFORMA A IMPOSSIBILIDADE DA OPERAÇÃO.
	public boolean criar(Membro membro) throws Exception {
		if(verificar(membro)) {
			persistidos.put(membro.getMatricula(), membro); // É PASSADO AO MÉTODO PUT() UM VALOR ENTREGUE PELO MÉTODO ID(), E O OBJETO MEMBRO.
			salvarXML();  // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
			return true;
		}
		throw new Exception("[XMLMembroConta] - [ERROR] Email ou matricula já cadastrada");
	}
	
	// O MÉTODO ATUALIZAR() SUBSTITUI UM MEMBRO EM UMA DETERMINADA CHAVE(ID) NO HASHMAP.
	// CASO A CHAVE(ID) NÃO SEJA ENCONTRADA, IMFORMA QUE O ID É INEXISTENTE.
	public boolean atualizar(long ID, Membro membro) {
		if(verificarParaAtualizar(membro)) {
			for(long key : persistidos.keySet()){
				if(key == ID) {
					persistidos.replace(key, membro);
					salvarXML();  // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
					return true;
				}
			}System.out.println(ID + "Inexistente");
		}
	return false;
	}
	
	// O MÉTODO REMOVER() RECEBE UM VALOR CHAVE(ID), APÓS LOCALIZALO, O REMOVE DO HASHMAP.
	// CASO A CHAVE(ID) NÃO SEJA ENCONTRADA, IMFORMA QUE O ID É INEXISTENTE.
	public boolean remover(long ID) {
		for(long key : persistidos.keySet()){
			if(key == ID) {
				persistidos.remove(key);
				salvarXML(); // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
				return true;
			}
		}
		System.out.println("ID: " + ID + " Inexistente");
		return false;
	}

	// O MÉTODO REMOVERTODOS() SUBISTITUI O HASHMAP POR UM NOVO HASHMAP VAZIO.
	public void removerTodos() {
		persistidos = new HashMap<Long,Membro>();
		salvarXML(); // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
	}

	// O MÉTODO SALVARXML() CRIA UM XML QUE COTEM O HASHMAP PERSISTIDOS.
	// LOGO A PÓS, CRIA UM ARQUIVO CONTENDO ESTE XML E O ARMAZENA NO ENDERECO DE ARQUIVOCOLECAO.
	private void salvarXML() {
		
		String xmlPersistidos = getXstream().toXML(persistidos);
		getXstream().alias("map", java.util.Map.class);
		
		try {
			
			arquivoColecao.createNewFile();
			PrintWriter gravador = new PrintWriter (arquivoColecao);
			gravador.print(xmlPersistidos);
			gravador.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
	}

	// O MÉTODO CARREGARXML() RECUPERA OS DADOS ARMAZENADOS NO ARQUIVO XML NO ENDEREÇO DESCRITO POR ARQUIVOCOLECAO.
	@SuppressWarnings("unchecked")
	private void carregarXML() {
				
		try {
				
			FileInputStream fis = new FileInputStream(arquivoColecao);
			persistidos = (HashMap<Long, Membro>) getXstream().fromXML(fis); // OS DADOS RECUPERADOS SÃO PASSADOS AO HASHMAP PERSISTIDOS, 
			
			return; // CASO NENHUMA ALTERAÇÃO SEJA FEITA, O METODO SALVARXML() NÃO SERÁ ACIONADO.
					
		} catch (FileNotFoundException e) {
			System.out.println((e.getMessage ()));
			
		}
		
	}

	// o METODO CONSULTARAND() FAZ UMA BUSCA POR OBJETOS ARMAZENADOS NO HASHMAP PERSISTIDOS QUE CONTENHAM TODOS OS ATRIBUTOS CORRESPONDENTES AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A TODOS OS REQUISITOS.
	public Set<Membro> consultarAnd(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException{
		Set<Membro> membros = new HashSet<Membro>();
		
		
		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UM ALERTA SERÁ MOSTRADO.
		if(atributos.length != respectivosValoresAtributos.length) { 
			System.out.println("atributos/valoresAtributos está imcompleta");
			return null;
		}
		
		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Membro membro = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.
			
			// ESTA VARIAVEL É IMPORTANTE POIS CONSTITUI UMA LOGICA SIMPLES E EFICAZ, CERTIFICA QUE O OBJETO CORESPONDE A TODOS OS REQUESITOS BUSCADOS.
			int validarAfirmacao = 0;
			
			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS
			for(int i = 0; i < atributos.length; i++) {
				if(atributos[i].equals("matricula")) {
					if(String.valueOf(membro.getMatricula()).equals(respectivosValoresAtributos[i])) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
				else if(atributos[i].equals("nome")) {
					if(membro.getNome().equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf(membro.isAtivo()).equals(respectivosValoresAtributos[i] )) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
				else if(atributos[i].equals("email")) {
					if(membro.getEmail().equals(respectivosValoresAtributos[i] )) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
				else if(atributos[i].equals("administrador")) {
					if(String.valueOf(membro.isAdministrador()).equals(respectivosValoresAtributos[i] )) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
				else if(atributos[i].equals("senha")) {
					if(membro.getSenha().equals(respectivosValoresAtributos[i] )) {
						validarAfirmacao += 1;
					}else
						break;
				}
				
			}
			
			// CASO O VALOR QUARDADO NA VARIAVEL VALIDARAFIRMCAO SEJA IGUAL AO NUMERO DE ATRIBUTOS BUSCADOS, O MEMBRO É ADICIONADO AO SET.
			if(validarAfirmacao == atributos.length) 
				membros.add(membro);
			
		}return membros; // APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}

	// O MÉTODO CONSULTAROR() FAZ UMA BUSCA POR OBJETOS ARAZENADOS NO HASMAP PERSISTIDOS QUE CONTENHAM AO MENOS UM ATRIBUTO CORRESPONDENTE AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A AO MENO UM DOS REQUISITOS.
	public Set<Membro> consultarOr(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException {
		Set<Membro> membros = new HashSet<Membro>();
		
		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UMA EXCEPTION SERÁ LANÇADA.
		if(atributos.length != respectivosValoresAtributos.length || atributos.length == 0) {
			throw new UnsupportedOperationException("Os arrays devem ter o mesmo tamanho");
		}
		
		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Membro membro = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.

			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS
			for(int i = 0 ;i <= atributos.length - 1; i++) {
				if(atributos[i].equals("matricula")) {
					if(String.valueOf(membro.getMatricula()).equals(respectivosValoresAtributos[i])) {
						membros.add(membro);
						break;
					}
				}
				
				else if(atributos[i].equals("nome")) {
					if(membro.getNome().equals(respectivosValoresAtributos[i]) ) {
						membros.add(membro);
						break;
					}
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf(membro.isAtivo()).equals(respectivosValoresAtributos[i] )) {
						membros.add(membro);
						break;
					}
				}
				
				else if(atributos[i].equals("email")) {
					if(membro.getEmail().equals(respectivosValoresAtributos[i] )) {
						membros.add(membro);
						break;
					}
				}
				
				else if(atributos[i].equals("administrador")) {
					if(String.valueOf(membro.isAdministrador()).equals(respectivosValoresAtributos[i] )) {
						membros.add(membro);
						break;
					}
				}
				
				else if(atributos[i].equals("senha")) {
					if(membro.getSenha().equals(respectivosValoresAtributos[i] )) {
						membros.add(membro);
						break;
					}
				}
			}
			
		}return membros; // APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}
	
	// O MÉTODO GETTODOSREGISTOS() RETORNA UM SET COMO TODOS OS OBJETOS ARMAZENADOS NO HASHMAP.
	public Set<Membro> getTodosRegistros(){
		Set<Membro> membros = new HashSet<Membro>();
		for(long key : persistidos.keySet()) {
			Membro membro = persistidos.get(key);
			membros.add(membro);
		}
		return membros;
	}

	// OMÉTODO CONSULTARPORID() RETORNA UM OBJETO DO HASHMAP COM A CHAVE INFOMADA COMO PARAMETRO DE ENTRADA.
	// CASO NENHUM OBJETO SEJA ENCONTRADO COM ESSA CHAVE, UMA EXCEPTION SERÁ LANÇADA.
	public Membro consultarPorMatricula(long ID) throws Exception {
		for(long key: persistidos.keySet()) {
			if(ID == key) {
				return persistidos.get(key);
			}
		} throw new Exception("Membro não encontrado");
	}
	
	// O MÉTODO VERIFICAR(), VERFICA SE O EMAIL OU MATRICULA DO MEMBRO PASSADO COMO PARAMENRO JA FOI RESGISTRADO EM ALGUM MEMBRO JÁ ARMAZENADO NO HASHMAP.
	public boolean verificar(Membro membro) {
		String matricula = String.valueOf(membro.getMatricula());
		String email = membro.getEmail();
		String[] novoMembro = {email,matricula};
		
		String[] verificar = {"email", "matricula"};
		
		if(consultarOr(verificar, novoMembro).size() == 0) {
			return true;
		}return false;
		
	}
	
	// O MÉTODO VERIFICARPARAATUALIZAR(), VERFICA SE O EMAIL DO MEMBRO PASSADO COMO PARAMENRO JA FOI RESGISTRADO EM ALGUM MEMBRO JÁ ARMAZENADO NO HASHMAP.
	public boolean verificarParaAtualizar(Membro membro)  {
		String email = membro.getEmail();
		
		String[] novoMembro = {email};
		String[] verificar = {"email"};
		
		if(consultarOr(verificar,novoMembro).size() == 1) { // DEVE SER IGUAL A 1 POIS A FACADE SETA O ENDEREÇO NA MEMORIA(DIRETAMENTE NO HASMAP) 
			//EMTÃO AO VERIFICAR ENCONTRAMOS UM QUE É PROPRIO MEMBRO MODIFICADO NA FACHADA, ENTÃO RESTA APENAS SALVALO NO XML.
			
			return true;
		}
		System.out.println("Já existe um membro que este endereço de Email");
		return false;
	}
	
	public Membro getMembroPeloLogin(String login) throws Exception {
		
		Iterator<Membro> iterator = getTodosRegistros().iterator();
		
		while(iterator.hasNext()) {
			Membro membro = iterator.next();
			
			if(membro.getEmail().equals(login)) {
				return membro;
			}
		}
		throw new Exception("Este membro não existe");
	}
	
}
