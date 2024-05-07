import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

class Oppgave3 implements Runnable {
    // Her brukes kunde-array som en global variable for main, run og skrivTilFil-metoden:
    static PrivatKunde[] kunder;

    //Dersom vi skal følge alle opplysninger fra Oppgave3:
    int antallSekunderEnDag = 864000000;

    // Bruker 5s for å teste programmet
    int femSekunderTilMS = 5000;

    public static void main(String[] args) throws Exception {
        kunder = new PrivatKunde[2];

        Butikk butikk = new Butikk("Telebitikken AS" , kunder);

        butikk.leggTilKunde(new PrivatKunde("1", "Gamleveien", "22226666", "Nora", "131021"));
        butikk.leggTilKunde(new PrivatKunde("2", "Aksla", "22226666", "Kevin", "140403"));

        Scanner input = new Scanner(System.in);
        Runnable filSkriver = new Oppgave3();
        Thread thread = new Thread(filSkriver);

        thread.start();
        System.out.println("Trykk en tast for å avslutte programmet");
        input.nextLine();

        thread.interrupt();
        // bare for at Main-tråden skal skrive ut tilslutt:
        Thread.sleep(1000);

        System.out.println("Main tråden er ferdig");
        input.close();
    }

    public void run() {
        try {
            while (true) {
                long start = System.nanoTime();
                skrivTilFil(kunder);
                long slutt = System.nanoTime();
                long eksekveringsTid = (slutt - start);

                //Her kan eksekveringsTid brukes, men når vi tester brukes bare 5ms (5000s)
                long totalTid = femSekunderTilMS;
                Thread.sleep(totalTid);
            }
        } catch (InterruptedException ie) {
            System.out.println("SkriveTråd ble ikke ferdig, eller ble avsluttet");
        }
    }

    void skrivTilFil(PrivatKunde[] kunder) {
        System.out.println("skriver til fil dersom kunde har bursdag...");

        File file = new File("kundefil.txt");
        try {
            PrintWriter skriv = new PrintWriter(file);
            for (int i = 0; i < kunder.length; i++) {
                if( kunder[i].getFodselsdato().equals(get_date()) ){
                    skriv.write("Navn: " + ( kunder[i]).getNavn() + "; Bursdag: " +  kunder[i].getFodselsdato() + "\n");
                    System.out.println( kunder[i].getNavn() + " har Bursdag i dag");
                }
            }
            skriv.close();
        } catch (Exception ex) {
            System.out.println("kan ikke skrive bursdag til fil...");
            System.out.println(ex);
        }
    }

    //Legger inn 131021 fra Oppgave3 for å teste mot en kunde:
    public String get_date() {
        return "131021";
    }
}

//Koden under er fra tidligere oppgave, dette er for å få programmet til kjører
class PrivatKunde extends Kunde {
    private String navn;
    private String fodselsdato;

    PrivatKunde(String kunde_nr, String adresse, String telefon_nr, String navn, String fodselsdato) {
        super(kunde_nr, adresse, telefon_nr);
        this.navn = navn;
        this.fodselsdato = fodselsdato;
    }

    public String toString() {
        return super.toString() + " | navn: " + this.navn + " | bursdag: " + this.fodselsdato;
    }

    public String getFodselsdato() {
        return fodselsdato;
    }

    public String getNavn(){
        return navn;
    }
}

class Kunde {
    private String kunde_nr;
    private String adresse;
    private String telefon_nr;

    Kunde(String kunde_nr, String adresse, String telefon_nr) {
        this.kunde_nr = kunde_nr;
        this.adresse = adresse;
        this.telefon_nr = telefon_nr;
    }

    public String toString() {
        return "kundeNr: " + this.kunde_nr + " | adresse: " + this.adresse + " | tlf: " + this.telefon_nr;
    }
}

class Butikk {
    private String navn;
    private PrivatKunde[] kundeliste;
    private int antallKunder = 0;
    private int antallVarer = 0;

    Butikk(String navn, PrivatKunde[] kundeliste) {
        this.navn = navn;
        this.kundeliste = kundeliste;
    }

    public void leggTilKunde(PrivatKunde k) {
        kundeliste[antallKunder] = k;
        antallKunder +=1;
    }
}
