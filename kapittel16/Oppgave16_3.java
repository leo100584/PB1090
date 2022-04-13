/* Losning paa oppgave 3 i kapittel 16
*
* Vi trenger ikke holde kontrollen paa noen siste-peker i en 
* LIFO-liste
*
*/

class Oppgave16_3 {
    Node forste = null;

    void settInn(Object p) {
	    Node n = new Node();
	    n.data = p;
	    if (forste == null) {
	        forste = n;
	    } else {
	        n.neste = forste;
	        forste = n;
	    }
    }
    
    Object taUt() {
	    if (forste == null) {
	        return null;
	    } else {
	        Object p = forste.data;
	        if (forste.neste != null) {
		        forste = forste.neste;
	        } else {
		        forste = null;
	        }
	        return p;
	    }
    }
}

class Node{
    Node neste;
    Object data;

    public Node(){
    }
}
