import java.util.*;

/** Sekvensiell implementasjon av Quicksort - meget rask for faa elementer (<500) */
class Oppgave1_SeqQuickArraysSort{
    int [] a,b;

    /** Innpakning for for sQuick  - enklere kall  */
    void sQuick(int [] a) { sQuick(a, 0,a.length-1);}

    void bytt(int [] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j]=t;
     }// end bytt

    /** Rekursiv, sekvensiell QuickSort av a[lav..hoy] */
    void sQuick (int [] a,  int lav, int hoy) {
        int ind =(lav+hoy)/2,
            piv = a[ind];
        int    storre=lav+1,  // hvor lagre neste '> piv'
               mindre=lav+1;  // hvor lagre neste '<=  piv'
        bytt (a,ind,lav);     // flytt 'piv' til a[lav] , sorter resten

        while (storre <= hoy) {
            // test iom vi har et 'mindre enn piv' element
            if (a[storre] < piv) {
                // bytt om  a[storre] og a[mindre]
                bytt(a,storre,mindre);
                ++mindre;
            } // end if - fant mindre enn 'piv'
            ++storre;
        } // end gaa gjennom a[lav+1..hoy]

        bytt(a,lav,mindre-1);        // Plassert pivotel-em mellom store og smaa

       if ( mindre-lav > 2) sQuick (a, lav,mindre-2); // sorter alle <= piv  (untatt piv)
       if ( hoy-mindre > 0) sQuick (a , mindre, hoy); // sorter alle > piv
    }// end sQuick

    Oppgave1_SeqQuickArraysSort(int n) {
        a = new int [n];
        b = new int[n];
        Random r = new Random(1337157);
        for (int i =0; i< a.length;i++)
            b[i]= a[i] = r.nextInt(a.length); // random fill >=0
     }

     void utforOgTest() {
        long t2, t = System.nanoTime();       // start klokke
        sQuick(a);
        t = System.nanoTime()-t;
        System.out.println("Sekvensiell QSort       av "+a.length+" tall paa:"+
        ((double)(t)/1000000.0)+ " millisek.");

        // test av Arrays.sort
        t2 = System.nanoTime();       // start klokke
         Arrays.sort(b);
         t2 = System.nanoTime()-t2;
         System.out.println("Sekvensiell Arrays.sort av "+b.length+" tall paa:"+
        ((double)(t2)/1000000.0)+ " millisek.");

         // test
         for (int i = 0; i<a.length; i++)
            if (b[i] != a[i] ) {
              System.out.println("FEIL  b["+i+"]:"+b[i]+"!= a["+i+"]:"+a[i]);
              return;
         }
     } // end utforOgTest


     public static void main (String [] args) {
         new Oppgave1_SeqQuickArraysSort(Integer.parseInt(args[0])).utforOgTest();
     }

} //end ParaQuick
