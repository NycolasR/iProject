package gabriel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import controller.projetos.ControllerTelaCadastroProjetos;
/**
 * 
 * @author NPG
 *
 */
public class TestesValoresCusteioCapital {
	
	@Test
	void testarValorCusteioCapitalNumeros() {
		
		//Expressão aceitável
		assertDoesNotThrow(() -> ControllerTelaCadastroProjetos.validarSeENumero("234.56"));
		
		//Expressão não aceitável
		Exception excecao = assertThrows(Exception.class, () -> {ControllerTelaCadastroProjetos.validarSeENumero("cyfgwqf");});
		assertEquals("Valores incorretos", excecao.getMessage());
	}
	
	@Test
	void testarValorMensalCusteio() {
		
		//Expressão aceitável
		assertDoesNotThrow(() -> ControllerTelaCadastroProjetos.validarMensalidade(100, 1000, 10));
		
		//Expressão aceitável
		Exception excecao = assertThrows(Exception.class, () -> {ControllerTelaCadastroProjetos.validarMensalidade(101, 1000, 10);});
		assertEquals("Mensalidade excede o valor de custeio", excecao.getMessage());
		
	}
}
