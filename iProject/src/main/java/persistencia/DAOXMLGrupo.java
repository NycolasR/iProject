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

import model.projetos.Grupo;

/**
 * @author NPG
 *
 */
public class DAOXMLGrupo {
	
	private static HashMap<Long, Grupo> persistidos;
	
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
	public DAOXMLGrupo() {
		arquivoColecao = new File("DAOXMLGrupo.xml");
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
		
		if (arquivoColecao.exists()) {
			carregarXML();
		} else {
			persistidos = new HashMap<Long,Grupo>();
		}
	}
	
	// O MÉTODO CRIAR() ADICIONA O OBJETO PASSADO COMO PARAMETRO AO HASHMAP.
	public boolean criar(Grupo grupo) {
		if(verificar(grupo)) {
			persistidos.put(grupo.getCodigo(), grupo);
			salvarXML();
			return true;			
		}
		return false;
	}
	
	// O MÉTODO VERIFICAR() VERIFICA SE EXISTE ALGUM GRUPO COM O MESMO CODIGO JÁ REGISTRADO.
	public boolean verificar(Grupo grupo) {
		String codigo = String.valueOf(grupo.getCodigo());
		
		String[] valores = {codigo};
		String[] atributos = {"codigo"};
		
		return consultarOr(atributos, valores).size() == 0;
	}
		
	// O MÉTODO ATUALIZAR() SUBSTITUI UM OBJETO EM UMA DETERMINADA CHAVE(ID) NO HASHMAP.
	// CASO A CHAVE(ID) NÃO SEJA ENCONTRADA, IMFORMA QUE O ID É INEXISTENTE.
	public boolean atualizar(long ID, Grupo grupo) {
		for(long key : persistidos.keySet()){
			if(key == ID) {
				persistidos.replace(key, grupo);
				salvarXML();  // TODA VEZ QUE ESTA FUNÇÃO FOR UTILIZADA, O AQUIVO XML É ATUALIZADO.
				return true;
			}			
		}
		System.out.println("Não foi possível atualizar este grupo. ID: " + ID + " inexistente");
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
		System.out.println("Não foi possível remover este grupo. ID: " + ID + " inexistente");
		return false;
	}
	
	// O MÉTODO REMOVERTODOS() SUBISTITUI O HASHMAP POR UM NOVO HASHMAP VAZIO.
	public void removerTodos() {
		persistidos = new HashMap<Long,Grupo>();
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
			e.printStackTrace();
		}
	}

	// O MÉTODO CARREGARXML() RECUPERA OS DADOS ARMAZENADOS NO ARQUIVO XML NO ENDEREÇO DESCRITO POR ARQUIVOCOLECAO.
	@SuppressWarnings("unchecked")
	private void carregarXML() {
				
		try {
				
			FileInputStream fis = new FileInputStream(arquivoColecao);
			persistidos = (HashMap<Long, Grupo>) getXstream().fromXML(fis); // OS DADOS RECUPERADOS SÃO PASSADOS AO HASHMAP PERSISTIDOS, 
			
			return; // CASO NENHUMA ALTERAÇÃO SEJA FEITA, O METODO SALVARXML() NÃO SERÁ ACIONADO.
					
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// o METODO CONSULTARAND() FAZ UMA BUSCA POR OBJETOS ARMAZENADOS NO HASHMAP PERSISTIDOS QUE CONTENHAM TODOS OS ATRIBUTOS CORRESPONDENTES AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A TODOS OS REQUISITOS.
	public Set<Grupo> consultarAnd(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException{
		Set<Grupo> grupos = new HashSet<Grupo>();
		
		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UMA EXCEPTION SERÁ LANÇADA.
		if(atributos.length != respectivosValoresAtributos.length || atributos.length == 0) {
			throw new UnsupportedOperationException("Os arrays devem ter o mesmo tamanho");
		}
		
		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Grupo grupo = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.
			
			// ESTA VARIAVEL É IMPORTANTE POIS CONSTITUI UMA LOGICA SIMPLES E EFICAZ, CERTIFICA QUE O OBJETO CORESPONDE A TODOS OS REQUESITOS BUSCADOS.
			int validarAfirmacao = 0;
			
			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS
			for(int i = 0 ;i <= atributos.length - 1; i++) {
				if(atributos[i].equals("nome")) {
					if(grupo.getNome().equals(respectivosValoresAtributos[i])) {
						validarAfirmacao += 1;
						continue;
					} else {
						break;
					}
				}
				
				else if(atributos[i].equals("datacriacao")) {
					if(grupo.getDataCriacao().toString().equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					} else {
						break;
					}
				}
				
				else if(atributos[i].equals("linkcnpq")) {
					if(grupo.getLinkCNPq().equals(respectivosValoresAtributos[i]) ) {
						validarAfirmacao += 1;
						continue;
					} else {
						break;
					}
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf(grupo.isAtivo()).equals(respectivosValoresAtributos[i])) {
						validarAfirmacao += 1;
						continue;
					} else {
						break;
					}
				}
				
				else if(atributos[i].equals("codigo")) {
					if(String.valueOf(grupo.getCodigo()).equals(respectivosValoresAtributos[i])) {
						validarAfirmacao += 1;
						continue;
					} else {
						break;
					}
				}
			}
			
			// CASO O VALOR QUARDADO NA VARIAVEL VALIDARAFIRMCAO SEJA IGUAL AO NUMERO DE ATRIBUTOS BUSCADOS, O MEMBRO É ADICIONADO AO SET.
			if(validarAfirmacao == atributos.length) 
				grupos.add(grupo);
			
		}
		return grupos;// APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}

	// O MÉTODO CONSULTAROR() FAZ UMA BUSCA POR OBJETOS ARAZENADOS NO HASMAP PERSISTIDOS QUE CONTENHAM AO MENOS UM ATRIBUTO CORRESPONDENTE AOS ATRIBUTOS PASSADOS COMO PARAMETRO.
	// RETORNA UM SET COM OS OBJETOS QUE CORRESPONDAM A AO MENO UM DOS REQUISITOS.
	public Set<Grupo> consultarOr(String[] atributos, String[] respectivosValoresAtributos) throws UnsupportedOperationException{
		Set<Grupo> grupos = new HashSet<Grupo>();

		// CASO HAJA DISCREPANCIA ENTRE OS PARAMETROS RECEBIDOS UMA EXCEPTION SERÁ LANÇADA.
		if(atributos.length != respectivosValoresAtributos.length || atributos.length == 0) {
			throw new UnsupportedOperationException("Os arrays devem ter o mesmo tamanho");
		}
		
		// ESTE FOR IRÁ "CORRER" TODAS AS CHAVES DO HASHMAP.
		for(long key : persistidos.keySet()) {
			Grupo grupo = persistidos.get(key); // O OBJETO CORRESPONDENTE A CHAVE ATUAL DO FOR SERÁ CAPTURADO.

			// O CONTADOR I CORRESPONDE AO ATRIBUTO EM TERTERMINADA POSIÇÃO NO VETOR DE ATRIBUTOS
			for(int i = 0 ;i < atributos.length; i++) {
				if(atributos[i].equals("nome")) {
					if(grupo.getNome().equals(respectivosValoresAtributos[i])) {
						grupos.add(grupo);
						break;
					}
				}
				
				else if(atributos[i].equals("datacriacao")) {
					if(grupo.getDataCriacao().toString().equals(respectivosValoresAtributos[i])) {
						grupos.add(grupo);
						break;
					}
				}
				
				else if(atributos[i].equals("linkcnpq")) {
					if(grupo.getLinkCNPq().equals(respectivosValoresAtributos[i])) {
						grupos.add(grupo);
						break;
					}
				}
				
				else if(atributos[i].equals("ativo")) {
					if(String.valueOf((grupo.isAtivo())).equals(respectivosValoresAtributos[i])) {
						grupos.add(grupo);
						break;
					}
				}
				
				else if(atributos[i].equals("codigo")) {
					if(String.valueOf((grupo.getCodigo())).equals(respectivosValoresAtributos[i])) {
						grupos.add(grupo);
						break;
					}
				}
			}
		}
		return grupos; // APÓS "CORRER" POR TODOS OS OBJETOS DO HASHMAP, A FUNÇÃO RETORNA O SET COM OS OBJETOS QUE CORESPONDEM A TODOS OS  REQUISITOS DA PERSQUISA.
	}
	
	// O MÉTODO GETTODOSREGISTOS() RETORNA UM SET COMO TODOS OS OBJETOS ARMAZENADOS NO HASHMAP.
	public Set<Grupo> getTodosRegistros(){
		Set<Grupo> grupos = new HashSet<Grupo>();
		for(long key : persistidos.keySet()) {
			Grupo grupo = persistidos.get(key);
			grupos.add(grupo);
		}
		return grupos;
	}
	
	// OMÉTODO CONSULTARPORID() RETORNA UM OBJETO DO HASHMAP COM A CHAVE INFOMADA COMO PARAMETRO DE ENTRADA.
	// CASO NENHUM OBJETO SEJA ENCONTRADO COM ESSA CHAVE, UMA EXCEPTION SERÁ LANÇADA.
	public Grupo consultarPorID(long ID) throws Exception {
		for(long key: persistidos.keySet()) {
			if(ID == key) {
				return persistidos.get(key);
			}
		}
		throw new Exception("Objeto não encontrado");
	}
}
