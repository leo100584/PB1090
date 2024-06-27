import java.util.ArrayList;
import java.util.Scanner;

// Forhåndsbetingelse for testing og kjøring av løsningsforslag på eksamensoppgave V2024:
// Vi har hentet ut "alle" studenter fra en vilkårlig database (DB) og Student-objekter ligger nå i Institutt-objekter, se konstruktør.
// NB. Bruk av private, getters & setters er ikke med på dette løsningsforslaget. Men Kandidater skal ha det med på sin besvarelse.

// Oppgave 4 og 5:
public class StudentAdministrasjonSystem extends Thread{
    // NB. Institutt-objekt må være global her pga. den sendes inn til SokeFunksjoner ift. testing
    static Institutt ims = new Institutt();
    // Start-verdi for ordreløkke:
    static int funksjonsvalg = -1;

    // Main-metoden kaster InterruptedException pga. tråden bruker join for å avslutte. Trenger ikke være med på kandidaten sin løsning
    public static void main(String[] args) throws FeilInntastingValg, InterruptedException {

        Scanner input = new Scanner(System.in);

        while(funksjonsvalg !=0){
            System.out.println("\nVelkommen til hovedmenyen fra Main-tråden");
            System.out.println("Du kan velge følgende:");

            System.out.println("1. Skriv ut alle emner");
            System.out.println("2. Søk etter en student med navn");
            System.out.println("3. Finn alle studenter i et emne med en karakter (A,B,C,D,E,F)");
            System.out.println("4. Finn karakter til en student i et emne");

            System.out.println("0. for å avslutte");

            funksjonsvalg = input.nextInt();
            System.out.println("Du valgte: " + funksjonsvalg);

            if(funksjonsvalg >= 0 && funksjonsvalg < 5){
                // Her startes en ny tråd for å kjøre søkeFunksjoner, se run-metode under main
                Thread sokeTråd = new StudentAdministrasjonSystem();
                sokeTråd.start();
                // For å avslutte tråden etter søk er ferdig, slik at det ikke startes flere enn nødvendig. Trenger ikke være med på kandidat sin løsning pga. det er ikke den del av oppgaven.
                sokeTråd.join();
            }
            else{
                throw new FeilInntastingValg("Du har tastet feil, velg et tall mellom 0-4");
            }
        }
        input.close();
    }

    public void run(){
        SokFunksjonsVelger(ims, funksjonsvalg);
    }
    static void SokFunksjonsVelger(Institutt institutt, int funksjonsvalg){
        // NB. Under her er det vi søker etter for å teste søkeFunksjoner (2,3,4) i løsningsforslaget
        String studentNavn = "Lars";
        String emnekode = "PB1170";
        String bokstakarakter = "A";

        SokeFunksjoner sokeFunksjoner = new SokeFunksjoner(institutt,funksjonsvalg,studentNavn,emnekode,bokstakarakter);

        switch (funksjonsvalg) {
            case 1 -> institutt.skrivUtAlleEmner();
            case 2 -> sokeFunksjoner.finnStudent(studentNavn);
            case 3 -> sokeFunksjoner.finnAlleStudenterMedKarakter(emnekode, bokstakarakter);
            //case 3 -> sokeFunksjoner.finnAlleStudenterMedKarakterV2(emnekode, bokstakarakter);
            case 4 -> sokeFunksjoner.finnEmneKarakter(studentNavn, emnekode);
            //case 4 -> sokeFunksjoner.finnEmneKarakterV2(studentNavn, emnekode);

            //default -> System.out.println("Ingen funksjon valgt..");    //Denne trengs egentlig  ikke, blant annet pga. vi bruker eget unntak.
        }
    }
}
// Oppgave5:
class FeilInntastingValg extends Exception{
    FeilInntastingValg(String msg){
        super(msg);
    }
}
// Oppgave3:
class SokeFunksjoner{
    Institutt institutt;
    int funksjonsvalg = -1;

    // Navn på studenten som søket angår:
    String studentNavn;
    String emnekode;
    String bokstavkarakter;
    SokeFunksjoner(Institutt institutt, int valg, String studentnavn, String emnekode, String bokstavkarakter){
        this.funksjonsvalg = valg;
        this.studentNavn = studentnavn;
        this.emnekode = emnekode;
        this.bokstavkarakter = bokstavkarakter;
        this.institutt = institutt;
    }
    // Finner en student ved å søke på studentnavn:
    public void finnStudent(String studentnavn) {
        System.out.println("Søker etter: " + studentnavn + " med tråden som kjører søkeFunksjoner!");
        for (Student student : institutt.studentListe) {
            if(student.navn != null) {
                if (student.navn.equals(studentnavn)) {
                    System.out.println("Student funnet: " + student);
                }
            }
        }
    }
    // Finner alle studenter med f.eks. karakter 'A':
    public void finnAlleStudenterMedKarakter(String emnekode, String bokstavkarakter){
        System.out.println("Søker etter alle studenter med: " + bokstavkarakter + " i " + emnekode + " med tråden som kjører søkeFunksjoner!");

        System.out.println("Studenter: ");

        for(Student student: institutt.studentListe){
            if (student.emneListe != null) {

                for(Emne emne: student.emneListe){
                    if (emne.emnekode.equals(emnekode)) {

                        for (Karakter karakter : emne.karakter) {
                            //må sjekke mer her...
                            if (karakter.bokstavKarakter.equals(bokstavkarakter)) {

                                System.out.println(student.navn + " med:"+ karakter.bokstavKarakter);
                            }
                        }
                    }
                }
            }
        }
    }
    // V2 blir litt feil under her:
    // Denne løsning vil ikke gi full uttelling på eksamen pga. den vil ikke klare å finne alle, kun '1'
    public void finnAlleStudenterMedKarakterV2(String emnekode, String bokstavkarakter) {
        Student studentObjekt = null;

        for (Student student : institutt.studentListe) {
            if (student.emneListe != null) {
                studentObjekt = student;
            }
        }

        System.out.println(studentObjekt);

        Emne emneObjekt = null;

        for (Emne emne : studentObjekt.emneListe) {
            if (emne.emnekode.equals(emnekode)) {
                emneObjekt = emne;
            }
        }

        for (Karakter karakter : emneObjekt.karakter) {
            //må sjekke mer her...
            if (karakter.bokstavKarakter.equals(bokstavkarakter)) {
                System.out.println(studentObjekt.navn + " med:"+ karakter.bokstavKarakter);
            }
        }
    }
    // Finner en karakter for en student i et enkelt emne:
    public void finnEmneKarakter(String studentnavn,String emnekode){
        System.out.println("Søker etter karakter for: " + studentnavn + " i " + emnekode + " med tråden som kjører søkeFunksjoner!");

        for(Student student: institutt.studentListe){
            if(student != null) {
                if (student.navn.equals(studentnavn)) {

                    for (Emne emne : student.emneListe) {
                        if (emne.emnekode.equals(emnekode)) {

                            for (Karakter karakter : emne.karakter) {
                                System.out.println("Følgende student: " + studentnavn + " har karkater: " + karakter.bokstavKarakter + " i " + emnekode);
                            }
                        }
                    }
                }
            }
        }
    }

    public void finnEmneKarakterV2(String studentnavn,String emnekode) {
        System.out.println("Søker etter karakter for: " + studentnavn + " i " + emnekode + " med tråden som kjører søkeFunksjoner V2!");

        Student studentObjekt = null;

        for(Student student: institutt.studentListe){
            if(student != null) {
                if (student.navn.equals(studentnavn)) {

                    studentObjekt = student;

                }
            }
        }

        Emne emneObjekt = null;

        for (Emne emne : studentObjekt.emneListe) {
            if (emne.emnekode.equals(emnekode)) {
                emneObjekt = emne;
            }
        }

        for (Karakter karakter : emneObjekt.karakter) {
            System.out.println("Følgende student: " + studentnavn + " har karkater: " + karakter.bokstavKarakter + " i " + emnekode);
        }

        // Alt. bare bruke først karakter objekt som under her, men noe usikkert ift. get(0), så det gir ikke full uttelling sammenlignet med for-løkke over her.
        // System.out.println("Følgende student: " + studentnavn + " har karakter: " + emneObjekt.karakter.get(0).bokstavKarakter + " i " + emnekode);

    }
}
class Institutt {
    String navn;
    String adresse;
    ArrayList<Ansatt> ansattListe = new ArrayList<>();

    // emneListe er emner Institutt har tilgjengelig:
    ArrayList<Emne> emneListe;

    // studentListe er studenter objekter fra f.eks. en datakilde slik som en DB el...
    // Derfor blir listen under fylt opp i konstruktør lenger ned
    ArrayList<Student> studentListe;

    Institutt(){
        // koden under er for testing og kjøring, men trenger ikke være en del en løsning.
        // Legger til 2 studenter med navn, bare for å test søkeFunksjoner:
        String studentnavn = "Lars";
        String studentnavn2 = "Erik";

        emneListe = new ArrayList<>();
        studentListe = new ArrayList<>();
        studentListe.add(new Student(studentnavn,"N/A","N/A"));
        studentListe.add(new Student(studentnavn2,"N/A","N/A"));

        // For å teste systemet er det lagt til 998+2 = 1000 studenter, som finnes når Institutt-objektet blir opprettet
        System.out.println("Test av StudentAdministrasjonsSystem:");

        for(int i = 0; i < 998;i++){
            studentListe.add(new Student());
        }

        // Her tester vi at leggTilEmne-metoden fungere og vi får test-data til main-metoden og SøkeTråden...
        leggTilEmne("PB1090","Objektorientert programmering","Leo");
        leggTilEmne("PB1170","Programmering og mikrokontroller","Leo");
        // Tester for duplikat
        leggTilEmne("PB1170","Programmering og mikrokontroller","Leo");

        leggTilEmne("IB1020","Programmering for beregning","Leo");
        leggTilEmne("TSD2491","Programvareutvikling","Leo");

        // Her tester vi at registrereEmneForStudent-metoden fungere og vi får test-data til main-metoden og SøkeTråden...
        //System.out.println("registrereEmneForStudent = " + registrereEmneForStudent("PB1090",studentnavn) );
        System.out.println("registrereEmneForStudent = " + registrereEmneForStudent("PB1170",studentnavn) );
        System.out.println("registrereEmneForStudent = " + registrereEmneForStudent("PB1170",studentnavn2) );

        // Bare legger til en karakter for student; Lars (0) og Erik (1) for å teste søkeFunksjoner
        studentListe.get(0).emneListe.get(0).karakter.add(new Karakter("A"));
        System.out.println("Registrert karakter:"+"A" + " på " + "PB1170" +" for "+studentnavn);

        studentListe.get(1).emneListe.get(0).karakter.add(new Karakter("A"));
        System.out.println("Registrert karakter:"+"A" + " på " + "PB1170" +" for "+studentnavn2);
    }
    // Oppgave2:
    public void leggTilEmne(String emnekode,String emnenavn, String ansattnavn){
        // Pass på at det blir eget emne objekt på hver student-objekt.

        if(!emneListe.contains(finnEmne(emnekode))){
            emneListe.add(new Emne(emnekode,emnenavn,ansattnavn));
            System.out.println("Emne lagt til: " +emnekode);
        }
        else{
            System.out.println("Kunne ikke legge til emne: " + emnekode + "(finnes fra før)");
        }
    }
    public void skrivUtAlleEmner(){
        for(Emne e: emneListe){
            System.out.println(e.emnekode +":"+e.emnenavn);
        }
    }
    // Hjelpemetode for å finne et Emne-objekt
    public Emne finnEmne(String emnekode){
        Emne emne = null;
        for (Emne e : emneListe) {
            if (e.emnekode.equals(emnekode)) {
                emne = e;
                //Debug:
                //System.out.println("Emne funnet:" + emne.emnekode);
            }
        }
        return emne;
    }
    // Dersom registrereEmneForStudent er void slik om i UML, burde det være en utskrift som sier f.eks. "Emne x er lag til.." etter registering er fullført
    public boolean registrereEmneForStudent(String emnekode, String studentnavn) {
        // 1. Finner emne-objekt med hjelpemetode:
        Emne emne = finnEmne(emnekode);

        if (emne != null) {
            // 2. Finner student og legger til i begge lister:
            for (Student student : studentListe) {
                if (student.navn.equals(studentnavn)) {
                    // 3. Legger emnet til i studentens emneliste:
                    // student.emneListe.add(emne);

                    // Eller legger nytt emnet til i studentens emneliste:
                    student.emneListe.add(new Emne("PB1170"));

                    // 4. Legger studenten til i emnets liste for studenter:
                    emne.studenter.add(student);
                    return true;
                }
            }
        }
        return false;
    }
}
// Oppgave1:
class Person {
    String navn;
    String adresse;
    String telefonnummer;

    Person(String navn){
        this.navn = navn;
    }
    public Person(String navn, String adresse, String telefonnummer) {
        this.navn = navn;
        this.adresse = adresse;
        this.telefonnummer = telefonnummer;
    }
    public String toString(){
        return "Navn: "+navn +" tlf: "+telefonnummer + " adresse: " + adresse;
    }
}
class Ansatt extends Person {
    String stilling;
    boolean deltid;
    Ansatt(String navn){
        super(navn);
        this.stilling = "Foreleser";
        this.deltid = false;
    }
}
class Student extends Person {
    String studentID;
    ArrayList<Emne> emneListe;

    Student(){
        super("TestStudent");
        emneListe = new ArrayList<>();
    }
    public Student(String navn) {
        super(navn);
        emneListe = new ArrayList<>();
    }
    public Student(String navn, String adresse, String telefonnummer) {
        super(navn,adresse,telefonnummer);
        emneListe = new ArrayList<>();
    }
}
class Emne {
    String emnekode;
    String emnenavn;
    String foreleser;
    ArrayList<Student> studenter;
    ArrayList<Karakter> karakter;
    public Emne(String emnekode, String emnenavn, String foreleser) {
        this.emnekode = emnekode;
        this.emnenavn = emnenavn;
        this.foreleser = foreleser;
        studenter = new ArrayList<>();
        karakter = new ArrayList<>();
    }

    public Emne(String emnekode) {
        this.emnekode = emnekode;
        studenter = new ArrayList<>();
        karakter = new ArrayList<>();
    }
}
class Karakter {
    String studentnavn;
    Emne emne;
    String bokstavKarakter;
    String emnekode;
    public Karakter(String studentnavn, Emne emne, String bokstavKarakter) {
        this.studentnavn = studentnavn;
        this.emne = emne;
        this.bokstavKarakter = bokstavKarakter;
    }
    public Karakter(String studentnavn, String emnekode, String bokstavKarakter) {
        this.studentnavn = studentnavn;
        this.emnekode = emnekode;
        this.bokstavKarakter = bokstavKarakter;
    }
    public Karakter(String bokstavKarakter) {
        this.bokstavKarakter = bokstavKarakter;
    }
}