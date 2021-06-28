package util;

import java.util.ArrayList;

import model.projetos.ItemProjeto;

/**
 * 
 * @author NPG
 *
 */
public class CalculadoraCusteioCapitalTotal {

	// TODO [UML] adicionar ao UML
	/**
	 * Metodo utilitario usado pelos geradores de relatorio de Edital e Grupo.
	 * 
	 * @return Retora um array de float onde, no indice 0 se encontra o valor total em reais de custeio e
	 * no indice 1, o valor total em reais de capital.
	 */

	public static float[] calcularCusteioCapitalTotal(ArrayList<ItemProjeto> projetos) {
		float valorTotalCusteioReais = 0;
		float valorTotalCapitalReais = 0;

		for(int contador = 0; contador < projetos.size(); contador++) {

			try {
				valorTotalCusteioReais += projetos.get(contador).getAporteCusteioReais();
				valorTotalCapitalReais += projetos.get(contador).getAporteCapitalReais();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		float[] valores = new float[2];
		valores[0] = valorTotalCusteioReais;
		valores[1] = valorTotalCapitalReais;

		return valores;
	}

}
