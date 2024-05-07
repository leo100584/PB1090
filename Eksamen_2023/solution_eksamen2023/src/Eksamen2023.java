import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


class IkkeDekningException extends Exception {
    public IkkeDekningException(String errorMessage) {
        super(errorMessage);
    }
}

class IkkeMuligMedNeggativtBelopException extends Exception {
    public IkkeMuligMedNeggativtBelopException(String errorMessage) {
        super(errorMessage);
    }
}

class Konto implements Runnable {
    private long kontoNummer;
    private double saldo;
    private double akkumulertRente;
    private double rente;
    ArrayList<Transaksjon> transaksjonsListe = new ArrayList<>();

    Konto(long kontoNummer) {
        this.kontoNummer = kontoNummer;
        this.saldo = 0.0;
        this.akkumulertRente = 0.0;
    }

    public void endreRentefotProAnno(double rentefotProAnno) {
        this.rente = rentefotProAnno;
    }

    public double hentSaldo() {
        return this.saldo;
    }

    private void utforTransaksjon(double belop, String tekst, Dato dato) throws IkkeDekningException {
        if ((belop + this.hentSaldo()) < 0)
            throw new IkkeDekningException("utakksbeløpet er større en tilgjengelig saldo");

        transaksjonsListe.add(new Transaksjon(belop, tekst, dato));
        this.saldo += belop;
    }

    public void uttak(double belop) throws IkkeDekningException, IkkeMuligMedNeggativtBelopException {
        if (belop < 0)
            throw new IkkeMuligMedNeggativtBelopException("Kan ikke gjøre uttak med neggativt belop\n Bruk inskudd i stedet for");

        this.utforTransaksjon(belop * -1, "Utakk", new Dato());
    }

    public void gebyr(double belop, String gebyrType) throws IkkeDekningException {
        this.utforTransaksjon(belop * -1, "Gebyr: " + gebyrType, new Dato());
    }

    public void inskudd(double belop) throws IkkeMuligMedNeggativtBelopException, IkkeDekningException {
        if (belop < 0)
            throw new IkkeMuligMedNeggativtBelopException("Kan ikke sette inn neggativt belop\n Bruk uttakk i stedet for");
        this.utforTransaksjon(belop, "Inskudd", new Dato());
    }

    //Alt1. For å teste koden:
    private void arbeidsTraadForRenteBeregning_alt1() {

        Dato startTidsPunktForrenterTraad = new Dato();
        // Skjekk en gang i hver 6 time om dagen har endret seg
        //   -> Renter vil bli beregnet /utbetalt mellom kl 00.00 og 06.00
        try {
            while (true) {

                Thread.sleep(1000*60*6);  // Vent i 6 timer
                Dato dagensDato = new Dato();

                //Renter beregenes hvær dag
                if (startTidsPunktForrenterTraad.hentDag() != dagensDato.hentDag())
                    this.beregnOgAkumulerEnDagsRente();
                {    //Renter utbetales en gang i måneden
                    if (startTidsPunktForrenterTraad.hentMaaned() != dagensDato.hentMaaned())
                        this.utbetalRenter();
                    startTidsPunktForrenterTraad = dagensDato;
                }
            }
        } catch (InterruptedException ie) {
            System.out.println("arbeidsTraadForRenteBeregning er ferdig");
        }

    }



    //Alt2. mer korrekt i forhold til løsning  av oppgaven:
    private void arbeidsTraadForRenteBeregning_alt2() throws InterruptedException {
        Dato startTidsPunktForrenterTraad = new Dato();
        //Må bruke LocalDate direkte pga. compareTo-metoden skal være tilgjengelig.
        LocalDate tid1 = LocalDate.now();

        // Venter 5 min. for å se til at det blir en differanse for tid1 og tid2, når dem skal sammenlignes med compareTo
        Thread.sleep(1000 * 60 * 5);

        // Sjekk at tiden er ved midnatt før beregning utføres og rente-tråden sover til neste dag.
        try {
            while (true) {

                LocalDate tid2 = LocalDate.now();

                // Tiden ved start (tid1) er mindre enn tid2, dvs. at vi har en ny dag (etter midnatt)
                if(tid1.compareTo(tid2) < 0) {

                    Dato dagensDato = new Dato();

                    //Renter beregenes vær dag
                    if (startTidsPunktForrenterTraad.hentDag() != dagensDato.hentDag())
                        this.beregnOgAkumulerEnDagsRente();
                    {    //Renter utbetales en gang i måneden
                        if (startTidsPunktForrenterTraad.hentMaaned() != dagensDato.hentMaaned())
                            this.utbetalRenter();
                        startTidsPunktForrenterTraad = dagensDato;
                    }

                    // Vent i til neste dag:
                    // 1 dag = 86 400 000 ms
                    Thread.sleep(24000*60*60);

                    // Sett ny start tid:
                    tid1 = LocalDate.now();
                }
            }
        } catch (InterruptedException ie) {
            System.out.println("arbeidsTraadForRenteBeregning er ferdig");
        }

    }

    public void beregnOgAkumulerEnDagsRente() {
        double renteFotProDag = this.rente / 360;
        akkumulertRente = (akkumulertRente + saldo) * renteFotProDag;
    }

    public void utbetalRenter() {
        try {
            utforTransaksjon(this.akkumulertRente, "Utbetalt Renter", new Dato());
            this.akkumulertRente = 0.0;
        } catch (Exception e) {
            System.out.println("System feil \n Renter kunne ikke utbetales");
        }
    }

    @Override
    public String toString() {
        return "Konto{" +
                "kontoNummer=" + kontoNummer +
                ", saldo=" + saldo +
                ", akkumulertRente=" + akkumulertRente +
                ", rentefotProAnno=" + rente +
                '}';
    }

    public String kontoUtskrift() {

        String utskriften = "Nåværense saldo på konto :" + this.saldo + '\n';

        for (int transaksjonsNummer = transaksjonsListe.size() - 1; transaksjonsNummer >= 0; transaksjonsNummer--) {
            utskriften += "  " + transaksjonsListe.get(transaksjonsNummer).toString() + '\n';
        }
        return utskriften;
    }

    @Override
    public void run() {
        this.arbeidsTraadForRenteBeregning_alt1();
        //NB. Det må legges til try-catch blokk for Runtime Exception dersom alt2 skal testes her.
    }
}


// 5 gratis uttak
//  over 5 utak  vil gebyret tilsvare 1 prosent av det beløpet du tar ut, minimum 250 kroner.
class SpareKonto extends Konto {
    private long antallUttak;
    private final long antallGratisUttak = 5;

    public SpareKonto(long kontoNummer) {
        super(kontoNummer);
        super.endreRentefotProAnno(1.6);
        antallUttak = 0;
    }

    //Hvis flere uttak en 5 skal gebyret være 1% av uttaks beleløpet, men dog ikke mindre en 250NOK
    @Override
    public void uttak(double belop) throws IkkeDekningException, IkkeMuligMedNeggativtBelopException {

        //Hvis 4 utakk allerede er utført vil dette
        // (det 5 uttaket bli belastet med et gebyr
        if (antallUttak >= antallGratisUttak) {
            double gebyr = belop * 0.01;
            if (gebyr < 250)
                gebyr = 250;
            if (hentSaldo() < (belop + gebyr))
                throw new IkkeDekningException("utakksbeløpet og gebyr er større en tilgjengelig saldo");

            super.gebyr(gebyr, "Utak nr :" + (antallUttak + 1) + " av 5 gratis");
        }

        super.uttak(belop);
        antallUttak++;

    }

}

class BruksKonto extends Konto {
    public BruksKonto(long kontoNummer) {
        super(kontoNummer);
        super.endreRentefotProAnno(0.25);
    }
}

class Dato {
    private final int dag;
    private final int maaned;
    private final int aar;


    public Dato() {
        LocalDate dato = LocalDate.now();
        dag = dato.getDayOfMonth();
        maaned = dato.getMonthValue();
        aar = dato.getYear();
    }


    public int hentAar() {
        return aar;
    }

    public int hentMaaned() {
        return maaned;
    }

    public int hentDag() {
        return dag;
    }


    @Override
    public String toString() {
        return " " + this.hentDag() + "." + this.hentMaaned() + "." + this.hentAar();
    }
}

class Transaksjon {
    Dato dato;
    double belop;
    String tekst;

    public Transaksjon(double belop, String tekst, Dato dato) {
        this.belop = belop;
        this.tekst = tekst;
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "Transaksjon{" +
                "dato=" + dato +
                ", belop=" + String.format("%.2f ", belop) +
                ", tekst='" + tekst + '\'' +
                '}';
    }
}

public class Eksamen2023 {
    static final int UTTAK = 7;
    public static void main(String[] args) throws IkkeDekningException, IkkeMuligMedNeggativtBelopException {
        BruksKonto bruksKonto = new BruksKonto(00112124);
        bruksKonto.inskudd(100000.0);

        for(int indeks = 1; indeks <= UTTAK; indeks++){
            bruksKonto.uttak(1000*indeks);
            System.out.println("Tar ut: " + 1000*indeks + "kr");
        }

        System.out.println(bruksKonto.kontoUtskrift());

        Konto minKonto = new SpareKonto(00112123);
        System.out.println("Startet renteberegnings / utbetalings -tråden");
        Thread renteTraad = new Thread(minKonto);
        //Dette kallet beregner renter for et konto-objekt:
        renteTraad.start();   // Start  Tråden (kjører metoden  .run() )
        //Alt. kan det opprettes konto-array eller konto-list som blir iterert over, med kall på renteTrad for hvert objekt.

        System.out.println("Konto er opprettet " + minKonto);
        try {
            System.out.println("Skal inbetale 400   " + minKonto);
            minKonto.inskudd(400.0);
            System.out.println("inbetalt 400 på Konto  " + minKonto);
            minKonto.uttak(25);
            minKonto.uttak(25);
            minKonto.uttak(25);
            minKonto.uttak(25);
            minKonto.uttak(25);
            minKonto.uttak(100);
            minKonto.uttak(100);
            minKonto.uttak(10000);

        } catch (Exception e) {
            System.out.println("Beklager , det har oppstått en feil \n" + e.getMessage());
        }

        System.out.println(minKonto.kontoUtskrift());

        //Vent på at programmet skal avsluttes
        Scanner input = new Scanner(System.in);
        System.out.println("Trykk enter-tasten for å stoppe rente-tråden");
        input.nextLine();
        renteTraad.interrupt();   //  Stopp tråden
        //   (sleep vil throw InterupptedException)
        System.out.println(minKonto.kontoUtskrift());
        System.out.println("Ha en fin dag ");


    }
}