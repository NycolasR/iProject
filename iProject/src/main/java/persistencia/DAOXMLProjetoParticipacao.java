package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import model.projetos.Projeto;

/**
 * @author NPG
 *
 */
public class DAOXMLProjetoParticipacao {

	private static HashMap< Long, Projeto> persistidos;
	
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
	public DAOXMLProjetoParticipacao() {
		arquivoColecao = new File("DAOXMLProjetoParticipacao.xml");
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
			persistidos = new HashMap<Long,Projeto>();
		}
	}
	
	// O MÉTODO CRIAR() ADICIONA O OBJETO PASSADO COMO PARAMETRO AO HASHMAP.
	public boolean criar(Projeto projeto) {
		if(verificar(projeto)) {			
			persistidos.put(projeto.getCodigo(), projeto) ;
			salvarXML();
			return true;
		}
		return false;
	}
	
	// O MÉTODO VERIFICAR() VERIFICA SE EXISTE ALGUM PROJETO COM O MESMO CODIGO JÁ REGISTRADO.
	public boolean verificar(Projeto projeto) {
		String codigo = String.valueOf(projeto.getCodigo());
		
		String[] valores = {codigo};
		String[] atributos = {"codigo"};
		
		return consultarOr(atributos, valores).size() == 0;
	}
	
	// O MÉTODO ATUALIZAR() SUBSTITUI UM OBJETO EM UMA DETERMINADA CHAVE(ID) NO HASHMAP.
	// CASO A CHAVE(ID) NÃO SEJA ENCONTRADA, IMFORMA QUE O ID É INEXISTENTE.
	public boolean atualizar(long ID, Projeto projeto) {
			for(long key : persistidos.keySet()){
				if(key == ID) {
					persistidos.replace(key, projeto);
					salvarXML();  // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
					return true;
				}
			}System.out.println(ID + "Inexistente");
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
		persistidos = new HashMap<Long,Projeto>();
		salvarXML();
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
			System.out.println((e.getMessage ()));
			
		}
		
	}

	// O MÉTODO CARREGARXML() RECUPERA OS DADOS ARMAZENADOS NO ARQUIVO XML NO ENDEREÇO DESCRITO POR ARQUIVOCOLECAO.
	@SuppressWarnings("unchecked")
	private void carregarXML() {

		try {
				
			FileInputStream fis = new FileInputStream(arquivoColecao);
			persistidos = (HashMap<Long, Projeto>) getXstream().fromXML(fis); // OS DADOS RECUPERADOS SÃO PASSADOS AO HASHMAP PERSISTIDOS, 
			
			return; // CASO NENHUMA ALTERAÇÃO SEJA FEITA, O METODO SALVARXML() NÃO SERÁ ACIONADO.
					
		} catch (FileNotFoundException e) {
			System.out.println((e.getMessage ()));
			
		}
		
	}
	
	// o METODO CONSULTARAND() FAZ UMA BUSCA POR OBJETOS ARMAZENADOS NO HASHMAP PERSISTIDOS QUE CONTENHAM TODOS OS ATRIBUTOS CORRESPONDENTES AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A TODOS OS REQUISITOS.
	public Set<Projeto> consultarAnd(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException{
		Set<Projeto> projetos = new HashSet<Projeto>();
		
		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UMA EXCEPTION SERÁ LANÇADA.
		if(atributos.length != respectivosValoresAtributos.length || atributos.length == 0) {
			throw new UnsupportedOperationException("Os arrays devem ter o mesmo tamanho");
		}
		
		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Projeto projeto = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.
			
			// ESTA VARIAVEL É IMPORTANTE POIS CONSTITUI UMA LOGICA SIMPLES E EFICAZ, CERTIFICA QUE O OBJETO CORESPONDE A TODOS OS REQUESITOS BUSCADOS.
			int validarAfirmacao = 0;
			
			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS.
			for(int i = 0 ;i <= atributos.length - 1; i++) {
				if(atributos[i].equals("nome")) {
					if(projeto.getNome().equals(respectivosValoresAtributos[i])) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("aportecusteioreais")) {
					if(String.valueOf(projeto.getAporteCusteioReais()).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("aportecapitalreais")) {
					if(String.valueOf(projeto.getAporteCapitalReais()).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("gastoexecutadocusteioreais")) {
					if(String.valueOf(projeto.getGastoExecutadoCusteioReais()).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("gastoexecutadocapitalreais")) {
					if(String.valueOf(projeto.getGastoExecutadoCapitalReais()).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf((projeto.isAtivo())).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
				
				else if(atributos[i].equals("codigo")) {
					if(String.valueOf((projeto.getCodigo())).equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					}else
						break;
				}
			}
			
			// CASO O VALOR QUARDADO NA VARIAVEL VALIDARAFIRMCAO SEJA IGUAL AO NUMERO DE ATRIBUTOS BUSCADOS, O MEMBRO É ADICIONADO AO SET.
			if(validarAfirmacao == atributos.length) 
				projetos.add(projeto);
			
		}return projetos; // APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}

	// O MÉTODO CONSULTAROR() FAZ UMA BUSCA POR OBJETOS ARAZENADOS NO HASMAP PERSISTIDOS QUE CONTENHAM AO MENOS UM ATRIBUTO CORRESPONDENTE AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A AO MENO UM DOS REQUISITOS.
	public Set<Projeto> consultarOr(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException {
		Set<Projeto> projetos = new HashSet<Projeto>();

		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UMA EXCEPTION SERÁ LANÇADA.
		if(atributos.length != respectivosValoresAtributos.length || atributos.length == 0) {
			throw new UnsupportedOperationException("Os arrays devem ter o mesmo tamanho");
		}

		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Projeto projeto = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.

			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS
			for(int i = 0 ;i <= atributos.length - 1; i++) {
				if(atributos[i].equals("nome")) {
					if(projeto.getNome().equals(respectivosValoresAtributos[i])) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("aportecusteioreais")) {
					if(String.valueOf(projeto.getAporteCusteioReais()).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("aportecapitalreais")) {
					if(String.valueOf(projeto.getAporteCapitalReais()).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("gastoexecutadocusteioreais")) {
					if(String.valueOf(projeto.getGastoExecutadoCusteioReais()).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("gastoexecutadocapitalreais")) {
					if(String.valueOf(projeto.getGastoExecutadoCapitalReais()).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf((projeto.isAtivo())).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
				
				else if(atributos[i].equals("codigo")) {
					if(String.valueOf((projeto.getCodigo())).equals(respectivosValoresAtributos[i]) ) {
						projetos.add(projeto);
						break;
					}
				}
			}
			
		}return projetos; // APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}
	
	// O MÉTODO GETTODOSREGISTOS() RETORNA UM SET COMO TODOS OS OBJETOS ARMAZENADOS NO HASHMAP.
	public Set<Projeto> getTodosRegistros(){
		Set<Projeto> projetos = new HashSet<Projeto>();
		for(long key : persistidos.keySet()) {
			Projeto projeto  = persistidos.get(key);
			projetos.add(projeto);
		}
		return projetos;
	}

	// OMÉTODO CONSULTARPORID() RETORNA UM OBJETO DO HASHMAP COM A CHAVE INFOMADA COMO PARAMETRO DE ENTRADA.
	// CASO NENHUM OBJETO SEJA ENCONTRADO COM ESSA CHAVE, UMA EXCEPTION SERÁ LANÇADA.
	public Projeto consultarPorID(long ID) throws Exception {
		for(long key: persistidos.keySet()) {
			if(ID == key) {
				return persistidos.get(key);
			}
		} throw new Exception("Objeto não encontrado");
	}
	
}
