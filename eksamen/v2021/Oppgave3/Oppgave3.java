
class Oppgave3 {
    //Main-metoden er ikke den del av løsningsforslaget:
    public static void main(String[] args) {
        System.out.println("Oppgave3 start");

        MedlemArray personArray = hentTestData();
        Medlem[] medlemsNummer = sortereMedlemer(personArray.medlemer);

        for (Medlem medlem : medlemsNummer) {
            System.out.println(medlem.hentMedlesnummer());
        }

        //Tester mapping av medlemsNr i array; indeks0 = medlemsNr1600:
        System.out.println("medlem på indeks 0 er nr "+medlemsNummer[0].hentMedlesnummer());

        System.out.println("Oppgave3 ferdig");
    }

    //Denne metoden er ikke den del av løsningsforslaget:
    public static MedlemArray hentTestData() {
        int startNummer = 1000;
        MedlemArray temp = new MedlemArray(2000);

        for (int index = 0; index < temp.medlemer.length; index++) {
            //Legger til medlems nr. på hver 7.person:
            if (index % 7 == 0) {
                temp.medlemer[index] = new Medlem("Person" + index, startNummer + index);
            }
        }
        return temp;
    }

    public static Medlem[] sortereMedlemer(Medlem[] personer) {
        Medlem[] temp = new Medlem[2000];
        int antall = 0;

        for (Medlem person : personer) {
            if (person != null) {
                if (person.harMedlemsNr()) {
                    temp[antall] = person;
                    antall += 1;
                }
            }
        }

        Medlem[] personerMedMedlemsNr = new Medlem[antall];

        for (int index = 0; index < antall; index++) {
            personerMedMedlemsNr[index] = temp[index];
        }

        //evt. bruk metode fra System-klassen for å kopiere temp-array til personerMedMedlemsNr-array:
        //System.arraycopy(temp,0,personerMedMedlemsNr,0,antall);

        return personerMedMedlemsNr;
    }

}

class Medlem {
    private final String navn;
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
class MedlemArray {
    private final int lengde;
    private int antall;
    Medlem[] medlemer;

    MedlemArray(int lengde) {
        this.medlemer = new Medlem[lengde];
        this.lengde = lengde;
        this.antall = 0;
    }

    public void settInn(Medlem medlem) {
        medlemer[antall] = medlem;
        antall += 1;
    }
}
