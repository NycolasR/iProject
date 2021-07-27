package nycolas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import facades.casosdeuso.FacadeCasoDeUso1;
import facades.casosdeuso.FacadeCasoDeUso5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import model.autenticacao.Membro;
import model.projetos.Participacao;
import model.projetos.Projeto;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;

class TestesMembrosProjetos {
	
	private FacadeCasoDeUso5 facade = new FacadeCasoDeUso5();
	
	private DAOXMLProjetoParticipacao dao = new DAOXMLProjetoParticipacao();
	
	private ArrayList<Participacao> participacoes = mock(ArrayList.class);
	
	/**
	 * Método usado para testar se o membro que cria um
	 * projeto será o coordenador do dito projeto
	 */
	@Test
	void test1() {
		// 1 - Simular a criação de um projeto por um membro e ver se ele é o coordenador
		
		Membro membro = mock(Membro.class);
		Projeto projetoMock = mock(Projeto.class);
		
		when(membro.getMatricula()).thenReturn(123456789l);
		
		assertDoesNotThrow(() ->
			facade.criarProjeto(membro.getMatricula(), 1l, "Projeto 1", 
			1000, 1500, LocalDate.now(), LocalDate.of(2022, 2, 28), 100));

		when(projetoMock.getCodigo()).thenReturn(1l);
		
		Projeto projeto = null;
		try {
			projeto = dao.consultarPorID(projetoMock.getCodigo());
			
			assertNotNull(membro.getMatricula());
			assertNotNull(projeto.getCoordenador().getMatricula());
			
			assertEquals(membro.getMatricula(), projeto.getCoordenador().getMatricula());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void test2() {
		// 2 - Fazer com que um membro tenha 3 participações e verificando que só uma pode estar ativa
		
		Projeto projetoMock = new Projeto();
		
		Membro membro = mock(Membro.class);
		
		Participacao participacao1 = mock(Participacao.class);
		Participacao participacao2 = mock(Participacao.class);
		Participacao participacao3 = mock(Participacao.class);
		
		when(participacao1.isAtivo()).thenReturn(false);
		when(participacao2.isAtivo()).thenReturn(false);
		when(participacao3.isAtivo()).thenReturn(true);
		
		when(membro.getParticipacoes()).thenReturn(participacoes);
		
		when(participacoes.get(0)).thenReturn(participacao1);
		when(participacoes.get(1)).thenReturn(participacao2);
		when(participacoes.get(2)).thenReturn(participacao3);
		
		
		when(membro.getParticipacoes()).thenReturn(participacoes);
		
		assertFalse(membro.getParticipacoes().get(0).isAtivo());
		assertFalse(membro.getParticipacoes().get(1).isAtivo());
		assertTrue(membro.getParticipacoes().get(2).isAtivo());
		
	}
	
	private FacadeCasoDeUso1 facade1 = mock(FacadeCasoDeUso1.class);
	private Membro membro = new Membro();
	
	@Test
	void testarEmailsMembros() {
		
		when(facade1.setarEmail(membro, "nycolas.ramon@academico.ifpb.edu.br")).thenReturn(true); // 1 @ e nome de domínio -> aceitável
		when(facade1.setarEmail(membro, "nycolas.ramon@academico@ifpb.edu.br")).thenReturn(false); // 2 @ e nome de domínio -> inaceitável
		when(facade1.setarEmail(membro, "nycolas.ramon.academico.ifpb.edu.br")).thenReturn(false); // 0 @ e nome de domínio -> inaceitável
		
		assertTrue(facade1.setarEmail(membro, "nycolas.ramon@academico.ifpb.edu.br"));
		assertFalse(facade1.setarEmail(membro, "nycolas.ramon@academico@ifpb.edu.br")); 
		assertFalse(facade1.setarEmail(membro, "nycolas.ramon.academico.ifpb.edu.br")); 
		
	}
	
	@Test
	void testarSenhasMembros() {
		
		when(facade1.setarSenha(membro, "abcde")).thenReturn(false);
		when(facade1.setarSenha(membro, "abcdef")).thenReturn(true);
		
		when(facade1.setarSenha(membro, "hgfedbca")).thenReturn(true);
		when(facade1.setarSenha(membro, "ihgfedbca")).thenReturn(false);
		
		assertFalse(facade1.setarSenha(membro, "abcde"));     // 5 caracteres ->  inaceitável
		assertTrue (facade1.setarSenha(membro, "abcdef"));    // 6 caracteres ->  aceitável
		assertTrue (facade1.setarSenha(membro, "hgfedbca"));  // 8 caracteres ->  aceitável
		assertFalse(facade1.setarSenha(membro, "ihgfedbca")); // 9 caracteres ->  inaceitável
	}
}










