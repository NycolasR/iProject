package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import ponto.model.projetos.HorarioPrevisto;
/**
 * 
 * @author NPG
 *
 */
public class DAOXMLHorarioPrevisto {

	private HorarioPrevisto horarioPrevisto;

	private File arquivoColecao;

	private XStream xstream;

	public DAOXMLHorarioPrevisto() {
		arquivoColecao = new File("DAOXMLHorarioPrevisto.xml");
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
	}
	
	public HorarioPrevisto recuperarHorarios() {
		if (arquivoColecao.exists())
			return carregarXML();
		else
			return new HorarioPrevisto();
	}
	
	public void salvarHorarios(HorarioPrevisto horarioPrevisto) {
		salvarXML(horarioPrevisto);
	}

	private void salvarXML(HorarioPrevisto horarioPrevisto) {
		this.horarioPrevisto = horarioPrevisto;
		
		String xmlPersistidos = xstream.toXML(horarioPrevisto);
//		xstream.alias("map", java.util.Map.class);
		
		try {

			arquivoColecao.createNewFile();
			PrintWriter gravador = new PrintWriter (arquivoColecao);
			gravador.print(xmlPersistidos);
			gravador.close();

		} catch ( IOException e) {
			System.out.println((e.getMessage ()));

		}

	}

	private HorarioPrevisto carregarXML() {
		try {

			FileInputStream fis = new FileInputStream(arquivoColecao);
			horarioPrevisto = (HorarioPrevisto) xstream.fromXML(fis); // OS DADOS RECUPERADOS SÃO PASSADOS AO HASHMAP PERSISTIDOS, 

			return horarioPrevisto; // CASO NENHUMA ALTERAÇÃO SEJA FEITA, O METODO SALVARXML() NÃO SERÁ ACIONADO.

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
