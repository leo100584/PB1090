// NB. Dersom man bruker norsk bokstaver på klasse, metode eller variable-navn kan man bruke:
// javac -encoding UTF8 <java-fil>, f.eks:
// javac -encoding UTF8 Oppgave1.java

abstract class Lån{
    abstract boolean beregnLåneGrense();
}

class Kreditt extends Lån{
    private float beløp;
    private float avdrag;

    public boolean innvilgeKreditt(){ return false; };
    public boolean beregnLåneGrense(){return false;};
}

class Kunde extends Person{

    private Kreditt kreditt;
    private Konto[] kontoer;
    private float inntekt;

    public boolean søkeLån(){return false;};
}

 class Person{
     private int personNr;
     private int telefonNr;
     private String navn;
}

class Konto {
    private String kontoNr;
    private float saldo;
    private Kunde kunde;

    public boolean innskudd(){return false;};
    public double uttak(){return 0.0;};
}
