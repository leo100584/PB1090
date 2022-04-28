class Oppgave2 {
    public static void main(String[] args) throws Exception{
        //Denne main-metoden er ikke en del av løsningsforslaget!
        int antallTestKunder = 3;

        Kunde[] kunder = new Kunde[antallTestKunder];

        Butikk butikk = new Butikk("Telebutikken AS", kunder);
        butikk.leggTilKunde(new privatKunde("6", "Gamleveien", "22226666", "Rakel", "130121"));

        //Legger til varer. Dette er ikke en del av oppgave2, men det gjøres for å teste oppdaterBeholdning.
        Vare vare1 = new Vare("1", "banan", "6", 5);
        butikk.leggTilVare(vare1);
        butikk.legg_inn_bestilling("6", "1", 4);
        butikk.legg_inn_bestilling("6", "1", 1);

        //Test at unntak blir kastet når beholding er mindre enn 0:
        butikk.legg_inn_bestilling("6", "1", 1);

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

class Butikk {
    private String navn;
    private Kunde[] kundeliste;
    private Vare[] varelager = new Vare[5];
    private int antallKunder = 0;
    private int antallVarer = 0;

    Butikk(String navn, Kunde[] kundeliste) {
        this.navn = navn;
        this.kundeliste = kundeliste;
    }

    public void leggTilKunde(Kunde k) {
        kundeliste[antallKunder] = k;
        antallKunder +=1;
    }

    public void leggTilVare(Vare v) {
        varelager[antallVarer] = v;
        antallVarer +=1;
    }

    public Kunde hentKunde(String k_id) {
        for (int index = 0; index < kundeliste.length; index++) {
            if (kundeliste[index] != null) {
                if (kundeliste[index].getKunde_nr().equals(k_id)) {
                    return kundeliste[index];
                }
            }
        }
        return null;
    }

    public Vare hentVare(String v_id) {
        for (int index = 0; index < kundeliste.length; index++) {
            if (varelager[index].getId().equals(v_id) && varelager[index] != null) {
                return varelager[index];
            }
        }
        return null;
    }

    public void legg_inn_bestilling(String k_id, String v_id, int antall)  {
        System.out.println("Starter med legg_inn_bestilling...");
        Kunde kunde = hentKunde(k_id);
        Vare vare = hentVare(v_id);

        System.out.println(kunde + "\nhar bestilt: " + antall + " " + vare.getNavn());

        if (antall < 0) {
            System.out.println("Antall er negativ");
        } else {
            //setter antall til negativ tall (litt som å gjøre * -1 for å få et negativt tall når vi regner matte
            try{
                vare.oppdaterBeholdning(-antall);
            }
            catch( Exception exception){
                System.out.print(exception.getMessage());
            }
        }
    }
}

class Vare {
    private String id;
    private String navn;
    private String pris;
    private int beholdning;

    public Vare(String id, String navn, String pris, int beholdning) {
        this.id = id;
        this.navn = navn;
        this.pris = pris;
        this.beholdning = beholdning;
    }

    public void oppdaterBeholdning(int modifikator) throws Exception {
        System.out.println("Beholdning er: " + beholdning);
        System.out.println("Bestilling er:" + -1*modifikator);
        
        //Dersom beholding blir mindre enn 0
        if (beholdning + modifikator < 0) {
            System.out.println("Kaster et unntak her:");

            //Kaster et unntak:
            throw new Exception("Beholdning er tom");
            
        } else {
            beholdning += modifikator;
            System.out.println("Oppdaterer beholding til: " + beholdning);
        }
    }

    public String getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }
}
