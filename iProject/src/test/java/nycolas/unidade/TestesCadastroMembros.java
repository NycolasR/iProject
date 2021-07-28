package nycolas.unidade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import facades.casosdeuso.FacadeCasoDeUso1;
import model.autenticacao.Membro;

class TestesCadastroMembros {
	
	private FacadeCasoDeUso1 facadeCasoDeUso1 = new FacadeCasoDeUso1();
	
	private Membro membro = new Membro();
	
	@Test
	void testarMatriculasMembros() {
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarMatricula(membro, 12345678l);
		});
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarMatricula(membro, 123456789l)); // Formato aceito
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarMatricula(membro, 1234567890l);
		});
	}
	
	@Test
	void testarNomesMembros() {
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarNome(membro, "José G da Silv"); // 14 caracteres -> inaceitável
		});
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarNome(membro, "Nycolas R Alves")); // 15 caracteres ->  aceitável
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarNome(membro, "Pedro de Alcântara João Carlos Leopoldo Salvador Bibiano F X")); // 60 caracteres ->  aceitável
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarNome(membro, "Pedro de Alcântara João Carlos Leopoldo Salvador Bibiano F Xa"); // 61 caracteres ->  inaceitável
		});
	}
	
	@Test
	void testarEmailsMembros() {
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarEmail(membro, "nycolas.ramon@academico.ifpb.edu.br")); // 1 @ e nome de domínio -> aceitável
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarEmail(membro, "nycolas.ramon@academico@ifpb.edu.br"); // 2 @ e nome de domínio -> inaceitável
		});
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarEmail(membro, "nycolas.ramon.academico.ifpb.edu.br"); // 0 @ e nome de domínio -> inaceitável
		});
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarEmail(membro, "nycolasramonacademicoifpbedubr"); // 0 @ e sem nome de domínio -> inaceitável
		});
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarEmail(membro, "nycolasramon@academicoifpbedubr"); // 1 @ e sem nome de domínio -> inaceitável
		});
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarEmail(membro, "nyc()l#sramon@academicoifpbedubr"); // Caracteres especiais -> inaceitável
		});
	}
	
	@Test
	void testarSenhasMembros() {
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarSenha(membro, "abcde"); // 5 caracteres -> inaceitável
		});
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarSenha(membro, "abcdef")); // 6 caracteres ->  aceitável
		
		assertDoesNotThrow(() -> facadeCasoDeUso1.setarSenha(membro, "hgfedbca")); // 8 caracteres ->  aceitável
		
		assertThrows(Exception.class, () -> {
			facadeCasoDeUso1.setarSenha(membro, "ihgfedbca"); // 9 caracteres ->  inaceitável
		});
	}
}














