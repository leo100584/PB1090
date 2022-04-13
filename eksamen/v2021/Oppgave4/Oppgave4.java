import java.util.*;

class Konto{
    String kundeNavn;
    int saldo;

    Konto(String kundeNavn, int saldo){
        this.kundeNavn = kundeNavn;
        this.saldo = saldo;
    }

    int hentSaldo(){
        return saldo;
    }

    String hentKundeNavn(){
        return kundeNavn;
    }

    public String toString(){
        return kundeNavn;
    }
}

class Worker implements Runnable {
    int numberOfElements;
    int start;
    int stop;
    public int max;

    Konto[] localKonto;

    Worker(Konto[] localCustomers, int numberOfElements, int start, int stop) {
        this.numberOfElements = numberOfElements;
        this.start = start;
        this.stop = stop;

        this.localKonto = localCustomers;
    }

    public void run() {
        System.out.println("Antall elementer: " + this.numberOfElements + ":" + this.start + ":" + this.stop);
        max = FindMaxLocal(localKonto, start, stop);
        System.out.println("lokal max: " + max);
    }

    public int FindMaxLocal(Konto[] localCustomers, int start, int stop) {
        int localMax = Integer.MIN_VALUE;

        for (int index = start; index < stop; index++) {
            if (localMax < localCustomers[index].hentSaldo()) {
                localMax = localCustomers[index].hentSaldo();
            }
        }
        return localMax;
    }
}

class Oppgave4 {
    static int size = 10000;

    // Dette er ikke en del av løsingsforslaget, dvs. hentAlleKontoerFraDatabasen-metoden.
    // hentAlleKontoerFraDatabasen-metoden brukes for å kunne teste Oppgave4 i main-metoden
    public static Konto[] hentAlleKontoerFraDatabasen() {
        Konto[] kontoer = new Konto[size];
        int balance = 0;

        for (int index = 0; index < size; index++) {
            balance = new Random().nextInt(9000000) + 1000;
            kontoer[index] = new Konto(("Customer" + index),balance);
        }
        return kontoer;
    }

    //Main-metoden er en del av løsningsforslaget for Oppgave4:
    public static void main(String[] args) {

        System.out.println("Oppgave4 start");

        Runtime runtime = Runtime.getRuntime();

        int numberOfProcessors = runtime.availableProcessors(); //Samme som int availableProcessors() fra opgaveteksten

        System.out.println("Antall kjerner på CPU: " + numberOfProcessors);

        System.out.println("Antall elementer på tråder: " + size / numberOfProcessors);

        Konto[] kontoer = hentAlleKontoerFraDatabasen();

        int localLength = size / numberOfProcessors;
        Thread[] threads = new Thread[numberOfProcessors];
        Worker[] workers = new Worker[numberOfProcessors];

        int[] globalMax = new int[numberOfProcessors];
        int max = Integer.MIN_VALUE;

        for (int index = 0; index < numberOfProcessors; index++) {
            int start = localLength * index;
            int stop = (localLength * (index + 1) - 1);

            workers[index] = new Worker(kontoer, localLength, start, stop);
            threads[index] = new Thread(workers[index]);

            threads[index].start();

        }

        for (int index = 0; index < numberOfProcessors; index++) {
            try {
                //threads[index].wait();
                threads[index].join(1000);
            } catch (InterruptedException ie) {
            }
            globalMax[index] = workers[index].max;
        }

        max = FindGlobalMax(globalMax);
        //printAllCustomers(kontoer);

        for(Konto konto: kontoer){
            if(konto.hentSaldo()==max){
                System.out.println("Kunde med høyeste banksaldo i kundedatabasen er " + konto.hentSaldo() + " hos " + konto.hentKundeNavn());
            }
        }

        System.out.println("Oppgave4 ferdig");
    }

    static public int FindGlobalMax(int[] localMax) {
        int max = Integer.MIN_VALUE;

        for (int index = 0; index < localMax.length; index++) {
            if (max < localMax[index]) {
                max = localMax[index];
            }
        }
        return max;
    }

    static void printAllCustomers(Konto[] customers) {
        for (int index = 0; index < size; index++) {
            System.out.println(customers[index] + " med saldo: " + customers[index].hentSaldo());
        }
    }

}
