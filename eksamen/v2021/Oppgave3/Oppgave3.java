import java.util.Scanner;
class Oppgave3 {
    //Main-metoden er ikke den del av løsningsforslaget:
    public static void main(String[] args) {
        System.out.println("Oppgave3 start");

        Idrettslag medlemArray = hentTestData();
        Medlem[] medlemsNummer = sortereMedlemer(medlemArray.medlemer);

        for (Medlem medlem : medlemsNummer) {
            System.out.println(medlem.hentMedlesnummer());
        }

        //Tester mapping av medlemsNr i array; indeks0 = medlemsNr1600:
        System.out.println("medlem på indeks 0 er nr "+medlemsNummer[0].hentMedlesnummer());

        System.out.println("Oppgave3 ferdig");
    }

    //Denne metoden er ikke den del av løsningsforslaget:
    public static Idrettslag hentTestData() {
        int startNummer = 1000;
        Idrettslag temp = new Idrettslag(2000);

        for (int index = 0; index < temp.medlemer.length; index++) {
            //Legger til medlems nr. på hver 7.person:
            if (index % 7 == 0) {
                temp.medlemer[index] = new Medlem("Person" + index, startNummer + index);
            }
        }
        return temp;
    }

    public static Medlem[] sortereMedlemer(Medlem[] medlemArray) {
        Medlem[] temp = new Medlem[2000];
        int antall = 0;

        for (Medlem medlem : medlemArray) {
            if (medlem != null) {
                if (medlem.harMedlemsNr()) {
                    temp[antall] = medlem;
                    antall += 1;
                }
            }
        }

        Medlem[] medlemerMednr = new Medlem[antall];
        medlemerMednr = kopierTilNyttArray(temp, antall);
        //Alternativ losing kommentert ut under
        //System.arraycopy(temp,0,medlemerMednr,0,antall);
        return medlemerMednr;
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

//Denne klassen trenger ikke være med i løsningen, men den blir bruk i dette løsningsforslaget for å teste Medlems-metodene.
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
