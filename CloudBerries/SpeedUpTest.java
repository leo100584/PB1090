import java.util.Arrays;
import java.util.Random;

public class SpeedUpTest {

    public static void main(String[] args){
        int antallTråder = Runtime.getRuntime().availableProcessors();
        int radLengde = 16000;

        System.out.println("Antall datapunker totalt: " + radLengde +"*"+radLengde + " = "+ radLengde*radLengde);

        int convertFactor = 1000*1000;
        long startTidGenerering = System.nanoTime();
        Matrise matrise = new Matrise(radLengde);
        long sluttTidGenerering = System.nanoTime();
        long totalTidGenerering = (sluttTidGenerering - startTidGenerering)/convertFactor;  //divide by 1000*1000 to get milliseconds.

        System.out.println("Generering av data: "+totalTidGenerering + "ms");
        System.out.println("Generering av data: "+totalTidGenerering/1000 + "sek");

        //Sekvensiell algoritme:
        long startTidSekvensiell = System.nanoTime();
        int maksSumSekvensiell = matrise.finnMaksSumRader(matrise.rader,0,matrise.rader.length);
        long sluttTimeSekvensiell = System.nanoTime();
        long totalTidSekvensiell = (sluttTimeSekvensiell - startTidSekvensiell)/convertFactor;

        System.out.println("Sekvensiell algoritme tok: "+totalTidSekvensiell + "ms");
        System.out.println("Sekvensiell algoritme tok: "+totalTidSekvensiell/convertFactor + "sek");

        System.out.println("Maks sum av rad: "+maksSumSekvensiell);

        //Parallel algoritme:
        System.out.println("Antall kjerner på CPU: " + Runtime.getRuntime().availableProcessors());

        System.out.println("Antall tråder i bruk: " + antallTråder);
        System.out.println("Antall rader per tråd: " + radLengde / antallTråder);

        long startTidParallel = System.nanoTime();

        //------------------------------------------------------------------------------------------//
        //Her starter parallell algoritme:
        int lokaltAntallRader = radLengde / antallTråder;

        Thread[] tråder = new Thread[antallTråder];
        Worker[] arbeidere = new Worker[antallTråder];

        int[] globalMaksSummer = new int[antallTråder];

        for (int indeks = 0; indeks < antallTråder; indeks++) {
            int start = lokaltAntallRader * indeks;
            int stop = (lokaltAntallRader * (indeks + 1) - 1);

            arbeidere[indeks] = new Worker(matrise, lokaltAntallRader, start, stop);
            tråder[indeks] = new Thread(arbeidere[indeks]);

        }

        //Start arbeidstråder:
        for (int indeks = 0; indeks < tråder.length; indeks++)
        {
            tråder[indeks].start();
        }

        //PP6
        //Sammler inn svar før main-tråden skal finn best rad/sum/tråd
        for (int indeks = 0; indeks < antallTråder; indeks++) {

            try {
                tråder[indeks].join();
            }
            catch (InterruptedException ie) {
                System.out.println(ie);
            }
            globalMaksSummer[indeks] = arbeidere[indeks].maksSumRad;

        }

        //litt mer arbeid for main-tråden:
        int maksSumParallel = finnBesteSumFraAlleTråder(globalMaksSummer);
        long sluttTidParallel = System.nanoTime();

        long totalTidParallel = (sluttTidParallel - startTidParallel)/convertFactor;

        System.out.println("Maks sum av rad: "+maksSumParallel);

        System.out.println("Beregning tok:" + totalTidParallel + " ms");
        System.out.println("Beregning tok:" + totalTidParallel/1000 + " sek");

        //PP6
        testResultatSekvensiellParallel(maksSumSekvensiell,maksSumParallel);
        System.out.println("Oblig4 ferdig");
        //--------------------------------------------------------------------------------------------------------------
    }
    //PP7
    static public void testResultatSekvensiellParallel(int sekvensieltResultat, int paralleltResultat){
        if(sekvensieltResultat==paralleltResultat){
            System.out.println("Test OK");
        }
        else{
            System.out.println("Sekvensielt resultat er ikke det samme som parallelt resultat...");
        }
    }

    static public int finnBesteSumFraAlleTråder(int[] localMax) {
        int max = localMax[0];

        for (int index = 1; index < localMax.length; index++) {
            if (max < localMax[index]) {
                max = localMax[index];
            }
        }
        return max;
    }
}

class Worker implements Runnable{
    int antall;
    int start;
    int stop;
    public int maksSumRad;

    Matrise matrise;
    Rad[] lokaleRader;

    Worker(Matrise matrise, int antall, int start, int stop){
        this.matrise = matrise;
        this.antall = antall;
        this.start = start;
        this.stop = stop;
    }

    public void run(){
        System.out.println("Antall rader: " + this.antall + ":" + this.start + ":" + this.stop);

        this.lokaleRader = matrise.rader;
        maksSumRad = matrise.finnMaksSumRader(matrise.rader,start,stop);
        //PP7
        //System.out.println("Lokal maks sum av rad: " + maksSumRad);
    }
}

class Matrise {
    Rad[] rader;
    //int maksSum;
    int besteRadNr;

    Matrise(int lengde){
        this.rader = fyllRaderRandomInt(lengde);
        //maksSum = finnMaksSumRad(rader);
    }

    public Rad[] fyllRaderRandomInt(int lengde) {
        rader = new Rad[lengde];
        Random random = new Random();

        for (int row = 0; row < lengde; row++)
        {

            int[] values = new int[lengde];
            for (int col = 0; col < lengde; col++)
            {
                values[col] = random.nextInt(16);
            }
            rader[row] = new Rad(values);
        }

        return rader;
    }
    //Metoden under kan brukes til testing:
    void skrivUtVerdier(){
        for(int i = 0; i < rader.length; i++){
            for(int j = 0; j < rader[i].verdier.length; j++){
                System.out.print(rader[i].verdier[j] + " ");
            }
            System.out.println();
        }
    }

    int finnMaksSumRader(Rad[] rader, int start, int stop){
        int radSum = rader[0].sum;
        this.besteRadNr = 1;

        for(int indeks = start+1; indeks <stop; indeks++) {
            //need to do some extra work for every row before sum...
            rader[indeks].doWork();

            if(radSum < rader[indeks].sum){
                radSum = rader[indeks].sum;
                this.besteRadNr = indeks + 1;
                //System.out.println("besteRadNr: "+ besteRadNr);
            }
        }
        return radSum;
    }
}

class Rad{
    int verdier[];
    int sum;

    Rad(int[] verdier){

        this.verdier = new int[verdier.length];

        for(int indeks = 0; indeks < verdier.length; indeks++){
            this.verdier[indeks] = verdier[indeks];
        }

        this.sum = beregnSum(verdier);
    }

    int beregnSum(int[] verdier){
        int sum = 0;
        for(int indeks = 0; indeks < verdier.length; indeks++){
            sum += verdier[indeks];
        }
        return sum;
    }
    //For simlulation of more work..
    public void doWork(){
        Arrays.sort(verdier);

        //Do som more "random" work
        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] *= 2;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] += 5;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] -= 1;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] /= 2;
        }

        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] += 8;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] *= 3;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] -= 1;
        }


        for(int i = 0 ; i < verdier.length; i++)
        {
            verdier[i] /= 3;
        }
    }
}
