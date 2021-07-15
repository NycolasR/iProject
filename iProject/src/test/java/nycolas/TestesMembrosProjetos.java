package nycolas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
import persistencia.DAOXMLProjetoParticipacao;

class TestesMembrosProjetos {
	
	/*
	 * 1 - Simular a criação de um projeto por um membro e ver se ele é o coordenador
	 * 2 - fazer com membro e participações alegando que só uma pode estar ativa
	 * 3 - fazer com membro e DAO de Membro retornando membros com uma dada senha
	 */
	
	private Membro membro = mock(Membro.class);
	
	private FacadeCasoDeUso5 facade = new FacadeCasoDeUso5();
	
	private Projeto projetoMock = mock(Projeto.class);
	
	private DAOXMLProjetoParticipacao dao = new DAOXMLProjetoParticipacao();;
	
	@Mock
	private List<Participacao> participacoes;
	
	/**
	 * Método usado para testar se o membro que cria um
	 * projeto será o coordenador do dito projeto
	 */
	@Test
	void test1() {
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
}










