package model.autenticacao;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author NPG
 *
 */
public class RegistradorSessaoLogin {
	private Membro membroLogado;
	
	/**
	 * Parte da implementacao do padrão de responsabilidade SINGLETON: referencia para a unica instancia desta classe
	 */
	private static RegistradorSessaoLogin registradorSessaoLogin;
	
	/**
	 * Parte da implementacao do padrão de responsabilidade SINGLETON: construtor privado para uso restrito a esta classe
	 */
	private RegistradorSessaoLogin() {}
	
	/**
	 * Parte da implementacao do padrão de responsabilidade SINGLETON: metodo acessivel globalmente para acesso a instancia desta classe
	 * @return a unica instancia desta classe
	 */
	public static synchronized RegistradorSessaoLogin getInstance() {
		if(registradorSessaoLogin == null)
			registradorSessaoLogin = new RegistradorSessaoLogin();
		
		return registradorSessaoLogin;
	}
	
	Set<Membro> membrosOnline = new LinkedHashSet<Membro>();
	
	/**
	 * Recebe um membro como parametro de entrada e o adiciona na lista de membros online
	 * @param membro Membro a ser adicionado na lista
	 */
	public void registrarOnline(Membro membro) {
		membrosOnline.add(membro);
		membroLogado = membro;
	}
	
	/**
	 * Recebe um login como parametro de entrada e remove o membro associado da lista de membros online
	 * @param login Login do usuario que, se for encontrado, será removido da lista
	 */
	public boolean registrarOffline(String login) {
		Iterator<Membro> iterator = membrosOnline.iterator();
		while(iterator.hasNext()) {
			Membro membro = iterator.next();
			
			if(membro.getEmail().equals(login)) {
				membrosOnline.remove(membro);
				membroLogado = null;
				return true; // Caso o membro seja removido com sucesso, a execucao do metodo sera interrompida
			}
		}
		return false;
	}
	
	/**
	 * Recebe um login como parametro de entrada e verifica se ele esta online verificando pela lista de membros online
	 * @param login Login a ser consultado na lista
	 * @return true para membro encontrado (online) e false para membro nao encontrado (offline)
	 */
	public boolean isOnline(String login) {
		Iterator<Membro> iterator = membrosOnline.iterator();
		while(iterator.hasNext()) {
			Membro membro = iterator.next();
			
			if(membro.getEmail().equals(login)) {
				return true;
			}
		}
		return false;
	}
	
	public Membro getMembroLogado() {
		return membroLogado;
	}
	
}