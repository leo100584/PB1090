/* Losning paa oppgave 4 i kapittel 16 */

import java.util.Scanner;
import java.io.*;

class Oppgave16_4 {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
	
        // Leser filnavn 
	    System.out.println("Oppgi filnavn paa innfil: ");
        String filnavn = input.nextLine();

	    Ordliste liste = new Ordliste();

        try{
            File fil = new File(filnavn);
            Scanner reader = new Scanner(fil);
	        // leser ord fra innfilen 
	        while (reader.hasNext()) {    
	            String ord = reader.nextLine();
	            if(ord != null) {
		            ord = ord.toLowerCase();
		            liste.settInn(ord);
	            }
	        }
            reader.close();
        }catch(FileNotFoundException e){
            System.out.println("Error");
        }

	    // skriver ut i terminal 
	    liste.skrivut();

	    // skriver ut litt statistikk
	    System.out.println("Antall ord lest : " + liste.ordlest);
	    System.out.println("Antall unike ord: " + Ordliste.unikeord);

	    input.close();
    }
}

class Ordliste {
    Ord forste = null;
    Ord siste = null;
    int ordlest = 0;
    public static int unikeord = 0;

    Ordliste() {
    }

    void settInn(String o) {
	    ordlest++;
	    if (forste == null) {
	        Ord ord = new Ord(o);
	        forste = ord;
	        siste = ord;
	    } else {
	        Ord aktuell = forste;
	        boolean plassert = false;
            
	        while (aktuell != null) {
		        String as = aktuell.toString();
		        if (o.equals(as)) {
		            aktuell.frekvens++;
		            // markerer at ordet er funnet og at lokka er ferdig
		            aktuell = null;
		            plassert = true;
		        }else if (o.compareTo(as) < 0) {
		            // nytt skal inn i lista
		            Ord ord = new Ord(o);
		            if (aktuell == forste) {
			            ord.neste = forste;
			            forste.forrige = ord;
			            forste = ord;
		            } else {
			            ord.neste = aktuell;
			            aktuell.forrige.neste = ord;
			            ord.forrige = aktuell.forrige;
			            aktuell.forrige = ord;
		            }
		            // markerer at nytt ord er plassert 
		            aktuell = null;
		            plassert = true;
		        } else {
		            aktuell = aktuell.neste;
		        }
	        }

	        if (!plassert) {
		        // nytt ord skal til slutt
		        Ord ord = new Ord(o);
		        siste.neste = ord;
		        ord.forrige = siste;
		        siste = ord;
	        }
	    }
    }

    void skrivut() {
	    Ord aktuell = forste;
    	while (aktuell != null) {
	        System.out.println(aktuell.toString() + " " + aktuell.frekvens);
	        aktuell = aktuell.neste;
	    }
    } 
 }

class Ord {
    int frekvens;
    String ord;
    Ord forrige = null;
    Ord neste = null;

    Ord(String t) {
	    ord = t;
	    frekvens = 1;
	    Ordliste.unikeord++;
    }

    public String toString() {
	    return ord.toLowerCase();
    }
}

