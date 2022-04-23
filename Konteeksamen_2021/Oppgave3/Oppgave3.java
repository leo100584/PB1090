import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

class Oppgave3 implements Runnable {
        int antallSekunderEnDag = 864000000;
        int femSekunderTilMS = 5000;

        static Kunde[] kunder;
        public static void main(String[] args) throws Exception{

            kunder = new Kunde[2];

            Butikk butikk = new Butikk(kunder);

            butikk.leggTilKunde(new privatKunde("1", "Gamleveien", "22226666", "Nora", "131021"));
            //Tester med dagens dato..?
            butikk.leggTilKunde(new privatKunde("2", "Aksla", "22226666", "Kevin", "140403"));

            for(Kunde kunde: kunder){
                System.out.println(kunde);
            }

            Scanner input = new Scanner(System.in);
            Runnable runnable = new Oppgave3();
            Thread thread = new Thread(runnable);

            thread.start();
            System.out.println("Trykk en tast for å avslutte programmet");
            input.nextLine();
            thread.interrupt();
            Thread.sleep(1000);
            System.out.println("main tråden er ferdig");
            input.close();
        }

    public void run(){
        try{
            while(true){
                long start = System.nanoTime();
                skrivTilFil(kunder);
                long slutt = System.nanoTime();
                long eksekveringsTid = (slutt - start);

                //Her kan eksekveringsTid brukes, men når vi tester brukes bare 5ms (5000s)
                long totalTid = (femSekunderTilMS);
                Thread.sleep(totalTid);

            }
        }
        catch(InterruptedException ie){
            System.out.println("SkriveTråd ble ikke ferdig");
        }
    }

    void skrivTilFil(Kunde[] kunder){

        System.out.println("skriver til fil dersom kunde har bursdag...");

        File file = new File("kundefil.txt");
        try{
            PrintWriter skriv = new PrintWriter(file);
            for (int i = 0; i < kunder.length; i++){
                if((((privatKunde)kunder[i]).getDato().equals(get_date()))){
        
                    skriv.write("Navn: " + ((privatKunde)kunder[i]).getNavn() + "; Bursdag: " + ((privatKunde)kunder[i]).getDato() + "\n");
                    System.out.println(((privatKunde)kunder[i]).getNavn() + " har Bursdag i dag");
                }
            }
            skriv.close();

        }catch(Exception ex){
            //System.out.println("kan ikke skrive bursdag til fil.");
            System.out.println(ex);
        }
    
    }
    //Legger inn 131021 fra Oppgave3 for å teste mot en kunde:
    String get_date(){
        return new String("131021");
    }

}

//Koden under er fra tidligere oppgave, dette er så programmet kjører

class privatKunde extends Kunde {
    private String navn;
    private String fodselsdato;

    privatKunde(String kunde_nr, String adresse, String telefon_nr, String navn, String fodselsdato){
        super(kunde_nr, adresse, telefon_nr);
        this.navn = navn;
        this.fodselsdato = fodselsdato;
    }

    public String getDato(){
        return fodselsdato;
    }

    public String getNavn(){
        return navn;
    }

    public String toString(){
        return super.toString() + " | navn: " + this.navn + " | bursdag: " + this.fodselsdato;
    }
}

class bedriftsKunde extends Kunde {
    int organisjons_nr;
    String kontakt_person;

    bedriftsKunde(String kunde_nr, String adresse, String telefon_nr, int organisjons_nr, String kontakt_person){
        super(kunde_nr, adresse, telefon_nr);
        this.organisjons_nr = organisjons_nr;
        this.kontakt_person = kontakt_person;
    }

    public String toString(){
        return super.toString() + " | organisasjonsNr: " + this.organisjons_nr + " | kontaktperson: " + this.kontakt_person;
    }

}
class Butikk {

    private String butikkNavn;
    Kunde[] kundeliste = new Kunde[5];
    int antallKunder = 0;
    Vare[] vareliste = new Vare[5];
    int antallVarer = 0;


    public Butikk(Kunde[] kundeliste){
        this.kundeliste = kundeliste;
    }

    public void leggTilKunde(Kunde k){
        kundeliste[antallKunder] = k;
        antallKunder++;
    }

    public void slettKunde(String id){
        for (int i = 0; i < kundeliste.length; i++){
            if (kundeliste[i] != null){
                if (kundeliste[i].kunde_nr.equals(id)){
                    for (int j = i; j < kundeliste.length - 1; j++){
                        kundeliste[j] = kundeliste[j+1];
                    }
                }
            }
        }
    }
    public void printKunder(){
        for (int i = 0; i < kundeliste.length; i++){
            System.out.println("Oppgave1.Kunde #" + (i+1) + ": " + kundeliste[i]);
        }
    }

    public void leggTilVare(Vare v){
        vareliste[antallVarer] = v;
        antallVarer++;
    }

    public void legg_inn_bestilling(String k_id, String v_id, Vare vare){

        int count = 0;
        for (int i = 0; i < kundeliste.length; i++){
            if (kundeliste[i] != null){
                if (kundeliste[i].kunde_nr.equals(k_id)){
                    for (int j = 0; j < vareliste.length; j++){
                        if(vareliste[j].id.equals(v_id)){
                            System.out.println("Lagt inn bestillingsordre for vareNr: " +
                                    vareliste[j].id + " til kundeNr: " + kundeliste[i].kunde_nr);
                            count++;
                            System.out.println("Beholdning before bestilling: " + vare.beholdning);
                            try{
                                vare.oppdaterBeholdning();
                            }catch(Exception ex){};
                            System.out.println("beholdning etter bestilling: " + vare.beholdning );
                            System.out.println(count + " av vareNr " + vareliste[j].id + " er bestilt" );

                        }
                    }
                }
            }
        }
    }
}

class Kunde {
    String kunde_nr;
    String adresse;
    String telefon_nr;

    Kunde(String kunde_nr, String adresse, String telefon_nr){
        this.kunde_nr = kunde_nr;
        this.adresse = adresse;
        this.telefon_nr = telefon_nr;
    }

    public String toString(){
        return "kundeNr: " + this.kunde_nr + " | adresse: " + this.adresse + " | tlf: " + this.telefon_nr;
    }
}

class Vare{
    String id;
    String navn;
    private String pris;
    int beholdning = 100;

    public Vare(String id){
        this.id = id;
    }

    public Vare(String id, String navn, String pris, int beholdning){
        this.id = id;
        this.navn = navn;
        this.pris = pris;
        this.beholdning = beholdning;
    }
    String getVareNavn(){
        return navn;
    }

    public void oppdaterBeholdning() throws Exception {

        if (beholdning == 0){
            throw new tomBeholdningUnntak("Beholdningen er tom");
        }

        else{
            beholdning--;
        }
    }
}

class tomBeholdningUnntak extends Exception{

    tomBeholdningUnntak(String melding){
        super(melding);
    }

}
