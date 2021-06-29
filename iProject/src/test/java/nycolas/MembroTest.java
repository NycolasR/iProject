package nycolas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.autenticacao.Membro;

/**
 * @author NPG
 *
 */
class MembroTest {

	@Test
	void setarNome() {
		Membro membro = new Membro();
		
		assertEquals(true, membro.setNome("teste"));
	}

}
