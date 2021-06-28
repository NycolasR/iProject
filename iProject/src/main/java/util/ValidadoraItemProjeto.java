package util;

import java.util.ArrayList;

import model.projetos.ItemProjeto;

/**
 * @author NPG
 *
 */
public class ValidadoraItemProjeto {

	public static ItemProjeto validarExistenciaDeItemProjeto(ArrayList<ItemProjeto> itensProjetos, ItemProjeto itemProjeto){
		
		for(int i = 0; i < itensProjetos.size(); i++) {
			
			if(itensProjetos.get(i).getNome().equals(itemProjeto.getNome())) {
				
				return itensProjetos.get(i);
			}
		}
		return null;
	}
	
	public static ItemProjeto validarExistenciaDeItemProjeto(ArrayList<ItemProjeto> itensProjetos,String nomeDoItemProjeto){
		
		for(int i = 0; i < itensProjetos.size(); i++) {
			
			if(itensProjetos.get(i).getNome().equals(nomeDoItemProjeto)) {
				
				return itensProjetos.get(i);
			}
		}
		return null;
	}
}
