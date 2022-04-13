import java.util.*;

class Klokke {

    public static void main(String[] args) {
	
	Scanner readInput = new Scanner(System.in);

	System.out.println("Trykk [ENTER] for å starte og stoppe");
	
	readInput.nextLine();
	


	// Her lages stoppeklokke-objektene:
	Stoppeklokke sekundviser = new Stoppeklokke(1000, 60, "sek");
	Stoppeklokke minuttviser = new Stoppeklokke(60 * 1000, 60, "min");

    sekundviser.start();
    minuttviser.start();

    readInput.nextLine();    
	readInput.close();

	// Klokkene stoppes. Kallet på interrupt (en metode i
	// Thread-klassen) vekker tråden hvis den sover, slik at den
	// stopper umiddelbart.
	sekundviser.end();
	sekundviser.interrupt();
	minuttviser.end();
	minuttviser.interrupt();
    }
}

class Stoppeklokke extends Thread {

    boolean stop = false;
    int intervall, reset;
    String tekst;
    
    /**
     * Intervall oppgis i millisekunder
     * reset angir når telleren skal nullstilles, for sekunder og minutter
     * er det 60 (60 sekunder på et minutt, 60 minutter på en time)
     */

    Stoppeklokke(int intervall, int reset, String tekst) {
        this.intervall = intervall;
        this.reset = reset;
        this.tekst = tekst;
    }
    
    
    // blir kalt opp av superklassens start-metode.
    public void run() {
        int tid = 0;
        while (!stop) {
            // Bruker modularegning for ikke å skrive ut når verdien er 0.
            if ((tid % reset) != 0) {
                System.out.println(tekst + " " + (tid % reset));
                System.out.print("\007");
            }
        
            tid++;
        
            try { 
                Thread.sleep(intervall); 
            } catch (InterruptedException e) {}
        }
    }
    
    public void end() {
        stop = true;
    }
    
}