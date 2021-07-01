package gabriel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import util.ValidadoraDatas;

public class TesteEditalProjeto {

	@Test
	void testarProjetoEmEditalFinalizado() {
		
		//Projeto pode ser adicionado
		assertDoesNotThrow(() -> ValidadoraDatas.criarData("01/12/2001"));

		//Projeto não pode ser adicionado
		Exception excecao1 = assertThrows(Exception.class, () -> {ValidadoraDatas.criarData("01/13/2001");});
		assertEquals("Essa data não existe", excecao1.getMessage());
	}
}
