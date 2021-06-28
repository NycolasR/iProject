package model.autenticacao;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

/**
 * @author NPG
 * Esta classe é um tipo concreto de implementação.
 * Faz parte da implementação do padrão Bridge.
 */
public class ContaAutenticacaoProvedorEmailSMTP extends ProvedorConta {
	
	private String provedorHost;
	private String provedorPorta;

	public String getProvedorHost() {
		return provedorHost;
	}

	public void setProvedorHost(String provedorHost) {
		this.provedorHost = provedorHost;
	}
	

	public String getProvedorPorta() {
		return provedorPorta;
	}

	public void setProvedorPorta(String provedorPorta) {
		this.provedorPorta = provedorPorta;
	}
	
	@Override
	public boolean autenticar(String login, String senha, boolean isSenhaEncriptada) {
		System.out.println("Autenticando com ContaAutenticacaoProvedorEmailSMTP");
		if(isSenhaEncriptada)
			throw new UnsupportedOperationException("[ERRO] Para se autenticar por SMTP, a senha não pode ser encriptada.");
			
		// create properties field
		Properties properties = new Properties();

		properties.put("mail.pop3s.host", "pop.gmail.com");
		properties.put("mail.pop3s.port", "995");
		properties.put("mail.pop3s.starttls.enable", "true");

		// Setup authentication, get session
		Session emailSession = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(login, senha);
					}
         		}
		);
		
		// create the POP3 store object and connect with the pop server
		Store store = null;
		
		try {
			store = emailSession.getStore("pop3s");
			store.connect();
			
			System.out.println("Login realizado com sucesso!");
			
			store.close();
			
			return true;
		} catch (MessagingException e) {
			System.out.println("Houve um erro. Verifique as credenciais e tente novamente.");
			e.printStackTrace();
		}
		
		return false;
	}
}
