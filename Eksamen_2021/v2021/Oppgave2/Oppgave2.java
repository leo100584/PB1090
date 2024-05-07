import java.util.*;

class Medlem {
    public String navn;
    public String telefonNr;
    public String medlemsNummer;

    public boolean kontigentBetalt;

    Medlem() {
    }

    Medlem(String navn, String telefonNr) {
        this.navn = navn;
        this.telefonNr = telefonNr;

        int temp = new Random().nextInt(9000) + 1000;
        medlemsNummer = "" + temp;
    }

    public String toString() {
        return this.navn + " " + this.telefonNr + " " + this.medlemsNummer;
    }
}

class Idrettslag {
    private int antall;
    public Medlem[] medlemer;

    Idrettslag(int lengde) {
        this.medlemer = new Medlem[lengde];
        this.antall = 0;
    }

    void leggTilMedlem(Medlem medlem) {
        medlemer[antall] = medlem;
        antall++;
    }

    void slettMedlem(String medlemsnr) {
        for (int index = 0; index < antall; index++) {
            if (medlemer[index].medlemsNummer != null) {
                if (medlemer[index].medlemsNummer.equals(medlemsnr)) {
                    medlemer[index] = null;
                    antall -= 1;
                    System.out.println("medlem med nummer: " + medlemsnr + " er slettet");
                }
            }
        }
    }

    Medlem finnMedlem(String medlemsnr) {
        for (Medlem medlem : medlemer) {
            if (medlem.medlemsNummer != null) {
                if (medlem.medlemsNummer.equals(medlemsnr)) {
                    return medlem;
                }
            }
        }

        return null;
    }

    void registrerBetaling(String medlemsnr) {
        for (int index = 0; index < antall; index++) {
            if (medlemer[index].medlemsNummer != null) {
                if (medlemer[index].medlemsNummer.equals(medlemsnr)) {
                    medlemer[index].kontigentBetalt = true;
                    return;
                }
            }
        }

        System.out.println("Medlem med medlemsnummer: " + medlemsnr + " finnes ikke");
    }

    void skrivPurreliste() {
        for (Medlem medlem : medlemer) {
            if (!medlem.kontigentBetalt) {
                System.out.println(medlem + " har ikke betalt kontigent");
            }
        }
    }

    void harLikeTlfnr() {
        for(int indeks = 0; indeks < medlemer.length; indeks++) {

            if (medlemer[indeks].telefonNr != null){

                //sjekker medlem[indeks] mot RESTEN av medlem-array:
                for (int indeksResten = indeks + 1; indeksResten < medlemer.length; indeksResten++) {
                    if (medlemer[indeks].telefonNr.equals(medlemer[indeksResten].telefonNr)) {
                        System.out.println(medlemer[indeks] + " og " + medlemer[indeksResten] + " har like telefonnummer");
                    }
                }
            }
        }
    }

    void SkrivUtAlle() {
        for (Medlem medlem : medlemer) {
            System.out.println(medlem);
        }
    }


}
//Klassen Oppgave2 med main-metode er ikke en del av løsingsforslaget, men den blir bruk i dette løsningsforslaget for å teste Idrettslag-klassen og metodene.
class Oppgave2 {

    public static void main(String[] args) {
        System.out.println("Oppgave2 start");
        int lengde = 10;

        Idrettslag idrettslag = new Idrettslag(lengde + 1);

        for (int index = 0; index < idrettslag.medlemer.length - 2; index++) {
            idrettslag.leggTilMedlem(new Medlem());
        }

        idrettslag.leggTilMedlem(new Medlem("Leo", "90284216"));
        idrettslag.leggTilMedlem(new Medlem("Keo", "90284216"));
        idrettslag.harLikeTlfnr();

        idrettslag.SkrivUtAlle();

        Medlem medlem = idrettslag.finnMedlem(idrettslag.medlemer[lengde].medlemsNummer);

        idrettslag.registrerBetaling(medlem.medlemsNummer);

        //idrettslag.skrivPurreliste();

        idrettslag.slettMedlem(medlem.medlemsNummer);

        System.out.println("Oppgave2 ferdig");
    }
}
