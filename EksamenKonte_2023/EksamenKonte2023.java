import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

//NB. Løsingsforlaget er laget fra C++ versjon og portert med chatPGT, det kan inneholde små feil og manglere.

class Person {
    private String personNr;
    private String navn;

    private List<Kjoretoy> kjoretoy = new ArrayList<>();

    public Person(String personNr, String navn) {
        this.personNr = personNr;
        this.navn = navn;
    }

    public void leggTilKjoretoy(Kjoretoy kj) {
        kjoretoy.add(kj);
    }

    public float beregnAvgift(float avgiftPrBil, float avgiftPrMotorsykkel) {
        float avgift = 0.0f;
        for (Kjoretoy kj : kjoretoy) {
            if (kj instanceof Bil) {
                avgift += avgiftPrBil;
            } else if (kj instanceof Motorsykkel) {
                avgift += avgiftPrMotorsykkel;
            }
        }
        return avgift;
    }

    public String getPersonNr() {
        return personNr;
    }

    public String getNavn() {
        return navn;
    }

    public int getAntKjoretoy() {
        return kjoretoy.size();
    }

    //Ikke en del av løsingsforslaget, bare for testing:
    public List<Kjoretoy> getKjoretoy() {
        return kjoretoy;
    }
}

class Kjoretoy {
    private String regnr;
    private String merke;
    private String modell;
    private Person eier;

    public Kjoretoy(String regnr, String merke, Person eier) {
        this.regnr = regnr;
        this.merke = merke;
        this.eier = eier;
    }

    //Ikke en del av løsingsforslaget, bare for testing:
    public String toString() {
        return "Kjoretoy{" +
                "regnr='" + regnr + '\'' +
                ", merke='" + merke + '\'' +
                ", modell='" + modell + '\'' +
                ", eier=" + eier +
                '}';
    }
}

class Bil extends Kjoretoy {
    private int antallSeter;

    public Bil(String regnr, String merke, Person eier, int antallSeter) {
        super(regnr, merke, eier);
        this.antallSeter = antallSeter;
    }
}

class Motorsykkel extends Kjoretoy {
    private boolean sidevogn;

    public Motorsykkel(String regnr, String merke, Person eier, boolean sidevogn) {
        super(regnr, merke, eier);
        this.sidevogn = sidevogn;
    }
}

class PersonIkkeFunnet extends Exception {
    public PersonIkkeFunnet(String feilMelding){ //skriver ut en feilmelding
        super(feilMelding);
    }
}

class KjoretoyRegister {

    private List<Person> eiere = new ArrayList<>();

    private Person finnPerson(String personNr) {
        for (Person person : eiere) {
            if (person.getPersonNr().equals(personNr)) {
                return person;
            }
        }
        return null;
    }

    public void leggTilPerson(String personNr, String navn) {
        eiere.add(new Person(personNr, navn));
    }

    public void leggTilBil(String eiersPersonNr, String regnr, String merke, int antallSeter) throws PersonIkkeFunnet {
        Person eier = finnPerson(eiersPersonNr);
        if (eier == null) {
            throw new PersonIkkeFunnet("PersonIkkeFunnet");
        }
        eier.leggTilKjoretoy(new Bil(regnr, merke, eier, antallSeter));
    }

    public void leggTilMotorsykkel(String eiersPersonNr, String regnr, String merke, boolean sidevogn) throws PersonIkkeFunnet {
        Person eier = finnPerson(eiersPersonNr);
        if (eier == null) {
            throw new PersonIkkeFunnet("PersonIkkeFunnet");
        }
        eier.leggTilKjoretoy(new Motorsykkel(regnr, merke, eier, sidevogn));
    }

    public void beregnAvgifter(float avgiftPrBil, float avgiftPrMotorsykkel) {
        try {
            FileWriter avgiftFil = new FileWriter("avgift.txt");
            float totalAvgift = 0.0f;
            for (Person person : eiere) {
                float avgift = person.beregnAvgift(avgiftPrBil, avgiftPrMotorsykkel);
                avgiftFil.write(person.getPersonNr() + " " + person.getNavn() + " skal betale " + avgift + "kr\n");
                totalAvgift += avgift;
            }
            avgiftFil.write("\nTotalt skal det betales " + totalAvgift + "kr\n");
            avgiftFil.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sorterPersonregister() {
        eiere.sort((a, b) -> Integer.compare(b.getAntKjoretoy(), a.getAntKjoretoy()));
    }

    //Ikke en del av løsingsforslaget, bare for testing:
    public List<Person> getEiere() {
        return eiere;
    }
}

public class EksamenKonte2023 {
    public static void main(String[] args) {
        KjoretoyRegister biltilsynet = new KjoretoyRegister();
        biltilsynet.leggTilPerson("12129412345", "Gunnar Jensen");
        biltilsynet.leggTilPerson("10049978901", "Helle Vik");
        biltilsynet.leggTilPerson("05088711548", "Berit Hansen");
        try {
            biltilsynet.leggTilBil("12129412345", "EL12345", "Tesla", 5);
            biltilsynet.leggTilMotorsykkel("12129412345", "DH6789", "Yamaha", false);
            biltilsynet.leggTilBil("05088711548", "BP32987", "BMW", 5);
            biltilsynet.leggTilBil("05088711548", "EH23423", "Nissan", 2);
            biltilsynet.sorterPersonregister();
            biltilsynet.beregnAvgifter(3000.0f, 1500.0f);
        } catch (PersonIkkeFunnet e) {
            e.printStackTrace();
        }
        //Ikke en del av løsingsforslaget, bare for testing:
        for(Person p: biltilsynet.getEiere()){
            System.out.println(p.getKjoretoy());
        }
    }
}
