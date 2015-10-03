
package pkg2048like;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe qui implante une séquence d'entiers positifs ou nuls initialement vide.
 * Les éléments y sont ajoutés un par un jusqu'à un appel à shift
 * qui "compresse" ces éléments qui peuvent ensuite être enlevés un
 * par un, lorsqu'il ne reste plus de valeur dans la série, enlever retourne 0
 *
 */
public class Serie {
	
	private List <Integer> serie;
	private boolean shifted;
	
	/**
	 * création de la série initialement vide 
	 */
	Serie(){
		serie=new ArrayList<Integer>();
		shifted=false;
	}

	public String toString(){
		return serie+":"+shifted;
	}
	
	/**
	 * Ajoute un entier i positif ou nul à la série
	 * Lève une <code>IllegalArgumentException</code> si <code>shift</code> a déjà été appelé
	 * Lève une <code>IllegalArgumentException</code> si <code>i</code> est négatif
	 * @param i entier ajouté
	 */
	public void ajouter(int i){
		if(shifted)
			throw new IllegalArgumentException("ajouter après shift:"+this);
		if(i<0)
			throw new IllegalArgumentException("ajouter("+i+") argument négatif:"+this);
		serie.add(i);
	}
	
	/**
	 * Enlève l'élément du début de la série et le retourne.
	 * Retourne 0 s'il ne reste plus d'élément dans la série.
	 * Lève une <code>IllegalArgumentException</code> si <code>shift</code> n'a pas été appelé 
	 * 
	 * @return valeur de l'élément enlevé
	 */
	public int enlever(){
		if(!shifted)
			throw new IllegalArgumentException("enlever avant shift:"+this);
		if(serie.size()==0) return 0;
		return serie.remove(0);
	}
	
	/**
	 * Compresse une séquence en enlevant d'abord toutes les valeurs égales à 0.
	 * Parcourt ensuite la séquence résultante en comprimant deux valeurs égales.
	 * 
	 * @return le total des valeurs comprimées.
	 */
	public int shift(){
		int pts=0;
		// enlever tous les 0
		for(Iterator<Integer> it=serie.iterator();it.hasNext();)
			if(it.next()==0)it.remove();
		// éliminer les deux identiques
		for(int i=1;i<serie.size();i++){
			int v=serie.get(i-1);
			if(serie.get(i)==v){
				serie.set(i-1,v*2);
				pts+=v*2;
				serie.remove(i);
			}
		}
		shifted=true;
		return pts;
	}
	
}