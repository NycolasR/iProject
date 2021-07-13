package facades.casosdeuso;

import java.time.LocalDate;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import model.autenticacao.Membro;
import model.projetos.Projeto;
import persistencia.DAOXMLMembroConta;
import persistencia.DAOXMLProjetoParticipacao;
import util.CentralAtualizacaoItemProjeto;
import util.MyLogger;

/**
 * @author NPG
 *
 * * 	A implementacao do padrao facade facilita aos usuarios o uso de funcaes que exigiriam um esforco e conhecimento desnecessÃ¡rio sobre os metodos
 * 	disponibilizados no programa. Para o caso de uso 1, Ã© simplificado a relacao de adicionar ou remover participacoes em rpojetos e o envio de email 
 *  de forma simples e direta, sem a necessidade de um conhecimento previo ou amplo sobre os metodos e eliminando a necessidade de ter contado direto 
 *  com todos os objetos envolvidos.
 *  
 */
public class FacadeCasoDeUso6 {

	//	Um membro pode adicionar ou remover participações de membros em seu projeto,
	//	desde que seja o coordenador do respectivo projeto. Ele pode adicionar
	//	somente membros que já possuem conta no sistema. Quando ele adicionar ou
	//	remover participações, o membro deverá receber um e-mail de notificação (assunto + mensagem),
	//	com o nome do projeto, o coordenador e o que aconteceu (se foi adicionado ou removido).

	private DAOXMLProjetoParticipacao daoxmlProjetoParticipacao;
	private DAOXMLMembroConta daoxmlMembroConta;
	private Logger logger = MyLogger.getInstance();

	public FacadeCasoDeUso6() {
		// Só inicializa as instâncias quando o objeto for criado
		daoxmlProjetoParticipacao = new DAOXMLProjetoParticipacao();
		daoxmlMembroConta = new DAOXMLMembroConta();
	}

	public void adicionarParticipante (
			long matriculaCoordenador,
			long matriculaCandidato,
			long codigoProjeto,
			LocalDate dataInicio,
			LocalDate dataTermino,
			float aporteCusteioReaisMensal) throws Exception {

		Membro membroCoordenador = null;
		Membro membroCandidato = null;
		Projeto projeto = null;
		
		try {
			// Resgata o projeto com o codigo passado nos parametros de entrada
			projeto = daoxmlProjetoParticipacao.consultarPorID(codigoProjeto);

			// Resgata o membro coordenador do projeto 
			membroCoordenador = projeto.getCoordenador();

			// Resgada o membro candidato (consequentemente, confirma se o membroCandidato possui um registro no sistema)
			membroCandidato = daoxmlMembroConta.consultarPorMatricula(matriculaCandidato);

			// Verifica se o coordenador passado como parametro de entrada e mesmo coordenador
			if(membroCoordenador.getMatricula() == matriculaCoordenador) {

				projeto.adicionarMembro(membroCandidato, dataInicio, dataTermino, false, aporteCusteioReaisMensal);
				logger.info("Membro " + membroCandidato.getNome() + " adicionado ao projeto " + projeto.getNome());
				enviarEmail(true, projeto, membroCoordenador, membroCandidato);

			} else {
				logger.severe("Membro tentou adicionar participantes no projeto " + projeto.getNome() + " sem permissão");
				throw new Exception("Este membro não tem permissão de adicionar participantes neste projeto");
			}

			CentralAtualizacaoItemProjeto.atualizarProjeto(codigoProjeto, projeto);
			daoxmlMembroConta.atualizar(matriculaCandidato, membroCandidato);

		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public void removerParticipante(
			long matriculaCoordenador,
			long matriculaCandidatoRemovivel,
			long codigoProjeto) throws Exception {

		Membro membroCoordenador = null;
		Membro membroASerRemovido = null;
		Projeto projeto = null;

		try {
			// Resgata o projeto com o codigo passado nos parametros de entrada
			projeto = daoxmlProjetoParticipacao.consultarPorID(codigoProjeto);

			// Resgata o membro coordenador do projeto 
			membroCoordenador = projeto.getCoordenador();

			// Resgada o membro candidato (consequentemente, confirma se o membroCandidato possui um registro no sistema)
			membroASerRemovido = daoxmlMembroConta.consultarPorMatricula(matriculaCandidatoRemovivel);

			// Verifica se o coordenador passado como parametro de entrada e mesmo coordenador
			if(membroCoordenador.getMatricula() == matriculaCoordenador) {

				projeto.removerMembro(membroASerRemovido);
				logger.info("Membro " + membroASerRemovido.getNome() + " removido do projeto " + projeto.getNome());
				enviarEmail(false, projeto, membroCoordenador, membroASerRemovido);

			} else {
				logger.severe("Membro tentou remover participantes no projeto " + projeto.getNome() + " sem permissão");
				throw new Exception("Este membro não tem permissão de remover participantes neste projeto");
			}

			CentralAtualizacaoItemProjeto.atualizarProjeto(codigoProjeto, projeto);
			daoxmlMembroConta.atualizar(matriculaCandidatoRemovivel, membroASerRemovido);

		} catch (Exception e) {
			logger.severe(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	private void enviarEmail(boolean isParticipanteAdicionado, Projeto projeto, Membro membroCoordenador, Membro membroCandidato) {
		String assunto = "Projeto " + projeto.getNome();
		String mensagem = "Coordenador: " + membroCoordenador.getNome() + "\n";

		mensagem += isParticipanteAdicionado ? "Bem vindo ao projeto " + projeto.getNome() : "Você foi removido deste projeto";

		String email = membroCandidato.getEmail();
		String senha = membroCandidato.getSenha();

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(email, senha);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@vendajava.pp.ads"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(assunto);
			message.setText(mensagem);

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public Membro[] pegarTodosOsMembros() {
		Set<Membro> membros = daoxmlMembroConta.getTodosRegistros();
		return membros.toArray(new Membro[membros.size()]);
	}
}

