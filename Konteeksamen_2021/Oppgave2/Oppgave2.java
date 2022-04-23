class Oppgave2 {
    public static void main(String[] args) throws Exception {

            Kunde[] kunde = new Kunde[5];

            Butikk b = new Butikk(kunde);

            //Legger til varer. Dette er ikke en del av oppgave2, men det gjøres for å teste oppdaterBeholdning.
            Vare vare1 = new  Vare("1", "banan", "6", 5);
            b.leggTilVare(vare1);

            b.legg_inn_bestilling("6", "1",4);

            //Få frem unntak:
            b.legg_inn_bestilling("3","1",1);
    }

    static void skrivUtVarer(Vare[] varelager){
        for(int indeks = 0; indeks< varelager.length;indeks++)
        System.out.println("varelager["+indeks+"]"+varelager[indeks]);
    }
}

class Kunde {
    private String kunde_nr;
    private String adresse;
    private String telefon_nr;

    Kunde(String kunde_nr, String adresse, String telefon_nr){
        this.kunde_nr = kunde_nr;
        this.adresse = adresse;
        this.telefon_nr = telefon_nr;
    }

    public String toString(){
        return "kundeNr: " + this.kunde_nr + " | adresse: " + this.adresse + " | tlf: " + this.telefon_nr;
    }

    public String getKunde_nr(){
        return kunde_nr;
    }

    public String getAddresse(){
        return adresse;
    }

    public String getTelefon_nr(){
        return telefon_nr;
    }
}

class privatKunde extends Kunde {
    private String navn;
    private String fodselsdato;

    privatKunde(String kunde_nr, String adresse, String telefon_nr, String navn, String fodselsdato){
        super(kunde_nr, adresse, telefon_nr);
        this.navn = navn;
        this.fodselsdato = fodselsdato;
    }

    public String toString(){
        return super.toString() + " | navn: " + this.navn + " | bursdag: " + this.fodselsdato;
    }

     public String getFodselsDato(){
        return fodselsdato;
    }

     public String getNavn(){
        return navn;
    }
}

class bedriftsKunde extends Kunde {
    private int organisasjons_nr;
    private String kontakt_person;

    bedriftsKunde(String kunde_nr, String adresse, String telefon_nr, int organisasjons_nr, String kontakt_person){
        super(kunde_nr, adresse, telefon_nr);
        this.organisasjons_nr = organisasjons_nr;
        this.kontakt_person = kontakt_person;
    }

    public String toString(){
        return super.toString() + " | organisasjonsNr: " + this.organisasjons_nr + " | kontaktperson: " + this.kontakt_person;
    }

    public int getOrganisasjonsNr(){
        return organisasjons_nr;
    }

    public String getKontaktPerson(){
        return kontakt_person;
    }
}


class Butikk {

    private String navn;
    private Kunde[] kundeliste;
    private Vare[] varelager = new Vare[5];
    private int antallKunder = 0;
    private int antallVarer = 0;

    Butikk(Kunde[] kundeliste){
        this.kundeliste = kundeliste;
    }

    public void leggTilKunde(Kunde k){
        kundeliste[antallKunder] = k;
        antallKunder++;
    }

    public void slettKunde(String kunde_nr){
        for (int i = 0; i < kundeliste.length; i++){
            if (kundeliste[i] != null){
                if (kundeliste[i].getKunde_nr().equals(kunde_nr)){
                    System.out.println("Tar ut kunde_nr: " + kundeliste[i].getKunde_nr());
                    kundeliste[i] = null;
                    antallKunder -= 1;
                }
            }
        }
    }

   public void printKunder(){
        for (int i = 0; i < kundeliste.length; i++){
            if(kundeliste[i] != null) {
                System.out.println("Kunde #" + (i + 1) + ": " + kundeliste[i]);
            }
        }
    }

    public void leggTilVare(Vare v){
        varelager[antallVarer] = v;
        antallVarer++;
    }

    public Kunde hentKunde(String k_id){
       for(int index = 0; index < kundeliste.length; index++){
           if(kundeliste[index] != null){
                if(kundeliste[index].getKunde_nr().equals(k_id) ) {
                    return kundeliste[index];
                }
           }
       }
       return null;
    }

    public Vare hentVare(String v_id){
        for(int index = 0; index < kundeliste.length; index++){
            if(varelager[index].getId().equals(v_id) && varelager[index] != null){
                return varelager[index];
            }
        }
        return null;
    }

    public void legg_inn_bestilling(String k_id, String v_id, int antall) throws Exception {
        System.out.println("Starter med legg_inn_bestilling...");
        Kunde kunde = hentKunde(k_id);
        Vare vare = hentVare(v_id);
        
        if(antall < 0){
            System.out.println("Antall er negativ");
        }else{
            vare.oppdaterBeholdning(-antall);
        }
    }

   public String getNavn(){
       return navn;
   }

   public int getAntallKunder(){
       return antallKunder;
   }

   public Kunde[] getKundeliste(){
       return kundeliste;
   }
}

class Vare{
    private String id;
    private String navn;
    private String pris;
    private int beholdning;

    public Vare(String id){
        this.id = id;
    }

    public Vare(String id, String navn, String pris, int beholdning){
        this.id = id;
        this.navn = navn;
        this.pris = pris;
        this.beholdning = beholdning;
    }
    
    
    public void oppdaterBeholdning(int modifikator) throws Exception {
        System.out.println("Beholdning er: " + beholdning);
        if (beholdning + modifikator == 0) {
            System.out.println("Skal kaste et unntak her:");

            try {
                // ink. throws Exception i metode-signatur.
                throw new Exception("Beholdning er tom");
            }
            catch(Exception exception){
                //Programmet kan kjøre videre etter et unntak er fanget:
                System.out.println(exception);
                // Eller vi kan avslutte med return:
                //return;
            }
        }else{
            beholdning += modifikator;
            System.out.println("Oppdaterer beholding.." + "(" + beholdning + ")");
        }

    }
    
    public String getNavn(){
        return navn;
    }

    public String getId(){
        return id;
    }

    public String getPris(){
        return pris;
    }

    public int getBeholdning(){
        return beholdning;
    }
}
