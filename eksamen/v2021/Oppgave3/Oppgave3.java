class Oppgave3 {
    // Main-metoden er ikke den del av løsningsforslaget:
    public static void main(String[] args) {
        System.out.println("Oppgave3 start");

        Idrettslag medlemer = hentTestData();
        Medlem[] medlemerMedNr = sortereMedlemer(medlemer.medlemer);

        System.out.println("\nAlle medlemsnummmer:");

        for (Medlem medlem : medlemerMedNr) {
            System.out.print(medlem.hentMedlesnummer()+" ");
        }

        // Tester mapping av medlemsNr i array; indeks 0 = medlemsNr 1000:
        int indeksTest = 0;
        System.out.println("\nMedlem nummer:" + medlemerMedNr[indeksTest].hentMedlesnummer() + " ligger på indeks:" + indeksTest);
        indeksTest = 385;
        // Tester mapping av medlemsNr i array; indeks 385 har medlesmnummer 1385
        System.out.println("Medlem nummer:" +medlemerMedNr[indeksTest].hentMedlesnummer() + " ligger på indeks:" + indeksTest);

        // Sjekker evt. hva som kom på indeks 1500, som var null i opprinelig medlem array
        //indeksTest = 1500;
        //System.out.println("Medlemnummer: " +medlemsNummer[indeksTest].hentMedlesnummer() + " ligger på indeks:" + indeksTest);

        System.out.println("Oppgave3 ferdig");
    }

    // Denne metoden er ikke den del av løsningsforslaget:
    public static Idrettslag hentTestData() {
        int antallMedlemer = 2000;
        Idrettslag temp = new Idrettslag(antallMedlemer);

        // Skal ha medlemsnummer fra 1000-2999
        // Starter på 1000:
        int medlemsNummer = 1000;

        for (int index = 0; index < temp.medlemer.length; index++) {    
            temp.medlemer[index] = new Medlem("Person" + index, + medlemsNummer);

            medlemsNummer += 1;

        }

        // Legger også inn noen null-refernaser for å teste sortereMedlemer:
        temp.medlemer[1500] = null;
        temp.medlemer[1600] = null;
        temp.medlemer[1700] = null;

        return temp;
    }

    public static Medlem[] sortereMedlemer(Medlem[] medlemArray) {
        Medlem[] temp = new Medlem[2000];
        int antall = 0;

        for (Medlem medlem : medlemArray) {
            
            if(medlem == null){
                    System.out.println("Hopper over medlem...");
            }

            if (medlem != null) {
                if (medlem.harMedlemsNr()) {
                    temp[antall] = medlem;
                    antall += 1;
                    
                }
            }
        }

        Medlem[] medlemerMedNr = new Medlem[antall];
        medlemerMedNr = kopierTilNyttArray(temp, antall);

        //Alternativ løsing med java sin System.arraycopy istendenfor for å bruke hjelpe-metoden; kopierTilNyttArray
        //System.arraycopy(temp,0,medlemerMedNr,0,antall);

        return medlemerMedNr;
    }

    public static Medlem[] kopierTilNyttArray(Medlem[] temp, int antall){
        Medlem[] personerMedMedlemsNr = new Medlem[antall];

        for (int index = 0; index < antall; index++) {
            personerMedMedlemsNr[index] = temp[index];
        }
        
        return personerMedMedlemsNr;
    }
}

class Medlem {
    private String navn;
    private int medlemsNr;

    Medlem(String navn, int medlemsNr) {
        this.navn = navn;
        this.medlemsNr = medlemsNr;
    }

    public boolean harMedlemsNr() {
        return this.medlemsNr != 0;
    }

    public int hentMedlesnummer() {
        return medlemsNr;
    }
}

// Denne klassen trenger ikke være med i løsningen, men den blir bruk i dette løsningsforslaget for å teste sortereMedlemer.
class Idrettslag {
    private int lengde;
    private int antall;
    Medlem[] medlemer;

    Idrettslag(int lengde) {
        this.medlemer = new Medlem[lengde];
        this.lengde = lengde;
        this.antall = 0;
    }

    public void settInn(Medlem medlem) {
        medlemer[antall] = medlem;
        antall += 1;
    }
}
