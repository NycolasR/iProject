package gabriel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import facades.casosdeuso.FacadeCasoDeUso4;
import persistencia.DAOXMLEdital;
import persistencia.DAOXMLProjetoParticipacao;
/**
 * 
 * @author NPG
 *
 */
public class TesteEditalProjeto {

	public DAOXMLEdital daoxmlEdital = new DAOXMLEdital();
	public DAOXMLProjetoParticipacao daoProjeto = new DAOXMLProjetoParticipacao();
	public FacadeCasoDeUso4 facade4 = new FacadeCasoDeUso4();
	
	@Test
	void testarProjetoEmEditalFinalizado() {
		
		//Projeto pode ser adicionado
		assertDoesNotThrow(() -> facade4.adicionarProjetoAUmEdital(
				daoxmlEdital.consultarPorID(157285535l),daoProjeto.consultarPorID(158924708l)));

		//Projeto não pode ser adicionado
		Exception excecao = assertThrows(Exception.class, () -> {facade4.adicionarProjetoAUmEdital(
				daoxmlEdital.consultarPorID(157329326l),daoProjeto.consultarPorID(158935788l));});
		assertEquals("Não é possível adicionar um projeto ou grupo (Edital fora de vigência)", excecao.getMessage());
	}
}
