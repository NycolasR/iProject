package model.autenticacao;

import java.util.Set;

import persistencia.DAOXMLMembroConta;
import util.Encriptador;

/**
 * @author NPG
 * Esta classe é um tipo concreto de implementação.
 * Faz parte da implementação do padrão Bridge.
 */
public class ContaAutenticacaoProvedorInterno extends ProvedorConta {

	@Override
	public boolean autenticar(String login, String senha, boolean isSenhaEncriptada) {
		System.out.println("Autenticando com ContaAutenticacaoProvedorInterno");
		
		DAOXMLMembroConta daoxmlMembroConta = new DAOXMLMembroConta();
		
		String[] atributos = {"email"};
		String[] respectivosValoresAtributos = {login};
		
		Set<Membro> membrosRecuperados = daoxmlMembroConta.consultarAnd(atributos, respectivosValoresAtributos);
		Membro[] membros = (Membro[]) membrosRecuperados.toArray(new Membro[membrosRecuperados.size()]);
		
		if(membrosRecuperados.size() == 1) {
			Membro membro = membros[0];
			
			String senhaMembroRecuperada = membro.getSenha();
			
			// Compara com a senha normal ou encriptada, dependendo do estado informado
			// da senha
			if(isSenhaEncriptada) 
				return senha.equals(Encriptador.criptografarSenha(senhaMembroRecuperada));
			else
				return senha.equals(senhaMembroRecuperada);
		}
		
		
		return false;
	}
}
