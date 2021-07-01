package gabriel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

//import org.junit.jupiter.api.Test;

import util.ValidadoraDatas;
/**
 * 
 * @author NPG
 *
 */
public class TestesDatas {

	@Test()
	void testarCriarData() {
					
		//Data existe e é aceitável
		assertDoesNotThrow(() -> ValidadoraDatas.criarData("01/12/2001"));

		//Data não existe
		Exception excecao1 = assertThrows(Exception.class, () -> {ValidadoraDatas.criarData("01/13/2001");});
		assertEquals("Essa data não existe", excecao1.getMessage());
		
		//Isso nem é data
		Exception excecao2 = assertThrows(Exception.class, () -> {ValidadoraDatas.criarData("fepwqfehfifb");});
		assertEquals("Isso não é uma data", excecao2.getMessage());

	}
	
	@Test
	void testarDataHojeAnteriorAOutraData() {
		
		//Data de hoje anterior ou igual a data passadapor parâmetro - É aceitável
		assertEquals(true, ValidadoraDatas.validarDataHojeMenorQueDataRecebida(LocalDate.of(2021, 07, 01)));
		
		//Data de hoje posterior ou igual - não é aceitável
		assertNotEquals(true, ValidadoraDatas.validarDataHojeMenorQueDataRecebida(LocalDate.of(2021, 06, 30)));
	}
	
}
