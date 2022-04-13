
/* Losning paa oppgave 2 i kapittel 16
 *
 * Vi antar at det dreier seg om en FIFO-liste.
 *
*/

class Oppgave16_2{
    Node forste;
    Node siste;
   
    Object taUt() {
        Object returobjekt = null;
        if (forste != null) {
            returobjekt = forste.data; // returobjekt skal peke paa den naavarende forstes data-objekt
        
            // Vi oppdaterer peker-kjeden
            if (forste.neste != null) {
                forste = forste.neste;
                forste.forrige = null;
            } else {
                forste = null;
                siste  = null;
            }
        }

        // objektet som hentes ut returneres til kallstedet
        return returobjekt;
    }

    class Node{
        Node neste;
        Node forrige;
        Object data;

        public Node(){
        }
    }
}
