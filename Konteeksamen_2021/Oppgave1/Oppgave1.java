class Oppgave1 {

    public static void main(String[] args) {
        int antallTestKunder = 3;
        Kunde[] kunder = new Kunde[antallTestKunder];

        Butikk b = new Butikk("Telebutikk AS", kunder);
        System.out.println("Legger til bedriftskunder og privatkunder");

        //Legger til kunder:
        b.leggTilKunde(new bedriftsKunde("22", "Bakkenteigen", "72835462", 9632, "Roar"));
        b.leggTilKunde(new privatKunde("6", "Gamleveien", "22226666", "Rakel", "130121"));
        b.leggTilKunde(new bedriftsKunde("3", "Borre", "12345678", 5612, "Birgitte"));

        //Skriv ut kunder:
        b.printKunder();

        //Fjerner kunde med kundenummer 2:2
        System.out.println("\nSletter kunde med kundeNr = 22\n");
        b.slettKunde("22");

        //Skriv ut kunder på nytt etter sletting:
        System.out.println();
        b.printKunder();
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

    public String getKunde_nr() {
        return kunde_nr;
    }
}

class privatKunde extends Kunde {
    private String navn;
    private String fodselsdato;

    privatKunde(String kunde_nr, String adresse, String telefon_nr, String navn, String fodselsdato) {
        super(kunde_nr, adresse, telefon_nr);
        this.navn = navn;
        this.fodselsdato = fodselsdato;
    }

    public String toString() {
        return super.toString() + " | navn: " + this.navn + " | bursdag: " + this.fodselsdato;
    }
}

class bedriftsKunde extends Kunde {
    private int organisasjons_nr;
    private String kontakt_person;

    bedriftsKunde(String kunde_nr, String adresse, String telefon_nr, int organisasjons_nr, String kontakt_person) {
        super(kunde_nr, adresse, telefon_nr);
        this.organisasjons_nr = organisasjons_nr;
        this.kontakt_person = kontakt_person;
    }

    public String toString() {
        return super.toString() + " | organisasjonsNr: " + this.organisasjons_nr + " | kontaktperson: " + this.kontakt_person;
    }
}

class Butikk {
    private String navn;
    private Kunde[] kundeliste;
    private int antallKunder = 0;

    Butikk(String navn, Kunde[] kundeliste) {
        this.navn = navn;
        this.kundeliste = kundeliste;
    }

    public void leggTilKunde(Kunde k) {
        kundeliste[antallKunder] = k;
        antallKunder++;
    }

    public void slettKunde(String kunde_nr) {
        for (int i = 0; i < kundeliste.length; i++) {
            if (kundeliste[i] != null) {
                if (kundeliste[i].getKunde_nr().equals(kunde_nr)) {
                    System.out.println("Tar ut kunde_nr: " + kundeliste[i].getKunde_nr());
                    kundeliste[i] = null;
                    antallKunder -= 1;
                }
            }
        }
    }

    public void printKunder() {
        for (int i = 0; i < kundeliste.length; i++) {
            if (kundeliste[i] != null) {
                System.out.println("Kunde #" + (i + 1) + ": " + kundeliste[i]);
            }
        }
    }
}
