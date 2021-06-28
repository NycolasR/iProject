package util;

import java.util.ArrayList;

import model.autenticacao.Membro;

/**
 * @author NPG
 *
 */

public class ValidadoraMembros {
	
	public static Membro validarExistenciaDeMembro(ArrayList<Membro> membros, Membro membro) {
		for(int i = 0; i < membros.size(); i++) {
			if(membros.get(i).getMatricula() == membro.getMatricula()) {
				return membro;
			}
		}
		return null;
	}
}
