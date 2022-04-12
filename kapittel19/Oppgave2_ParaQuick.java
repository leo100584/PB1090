import java.util.*;
import java.util.concurrent.*;

// Losning paa oppg. 19.2 - en MEGET daarlig ide aa fjerne vanlige sevensielle
// Quicksort-kall  etter att et mindre antall traader er laget


class Oppgave2_ParaQuick {
    int [] a;
    static int numThr =1;  // main


    void pQuick(int [] a) {
        int antKjerner = Runtime.getRuntime().availableProcessors();
        int n = a.length;

        pQuick(0,a, 0,a.length-1);

    }// end pQuick

    void bytt(int [] a, int i, int j) {
            int t = a[i];
            a[i] = a[j];
            a[j]=t;
    }// end bytt

    synchronized void tellOppNumThr() { numThr++;}  // bare for utskrift

    /**
     *  QuickSort a[lav..hoy]  fom a[lav] tom a[hoy]- parallell & rekursiv
     */
    void pQuick (int level,int [] a, int lav, int hoy) {
        Thread t1=null,t2=null;

        if (hoy-lav > 0) {
            // only sort arrayseggments > len =1
            int ind =(lav+hoy)/2,
                piv = a[ind];
            int    storre=lav+1,  // hvor lagre neste 'storre enn piv'
                   mindre=lav+1;  // hvor lagre neste 'mindre enn piv'
            bytt (a,ind,lav);     // flytt 'piv' til a[lav] , sorter resten

            while (storre <= hoy) {
                // test iom vi har et 'mindre enn piv' element
                if (a[storre] < piv) {
                    // bytt om  a[storre] og a[mindre]
                    bytt(a,storre,mindre);
                    ++mindre;
                } // end if - fant mindre enn 'piv'
                ++storre;
            } // end gaa gjennom a[i+1..j]

            bytt(a,lav,mindre-1);        // Plassert 'piv' mellom store og smaa


           if(mindre-2 > lav) {
              t1 =new Thread(new Para(level+1,a,lav,mindre-2));
              t1.start();// sorter alle <= piv (untatt piv)
           }
           if(hoy > mindre)  {
               t2= new Thread(new Para(level+1,a,mindre,hoy  ));
               t2 .start(); //sorter alle > piv
           }

         } // end sortering av a[lav..hoy]

         try{
            if (t1 != null) t1.join();
            if (t2 != null) t2.join();
         } catch(Exception e) { System.out.println("ERROR"); return;}

    }// end pQuick

    Oppgave2_ParaQuick(int n) {
        a = new int [n];
        Random r = new Random(1337);
        for (int i =0; i< a.length;i++)
            a[i] = r.nextInt(a.length); // tilfeldige tall >=0
     } // end konstruktor

     void utforOgTest() {
           int antKjerner = Runtime.getRuntime().availableProcessors();
            long t = System.nanoTime();       // start klokke
            pQuick(a);
            t = System.nanoTime()-t;
            for (int i = 1; i<a.length; i++)
            if (a[i-1] > a[i] ) {
                System.out.println("FEIL  a["+(i-1)+"]:"+a[i-1]+" a["+i+"]:"+a[i]);
                return;
            }
            System.out.println("Parallell qSort,:"+ a.length+" tall med:"+antKjerner+
            " kjerner og \ntot antall traader:"+
              numThr+" paa:"+   ((double)(t)/1000000.0)+ " millisek.");
     } // end utforOgTest


      public static void main (String [] args) {
         new Oppgave2_ParaQuick(Integer.parseInt(args[0])).utforOgTest();
      }

      class Para implements Runnable{
          int [] a;
          int low,high,level;

          Para(int level, int []a,int i,int j) {
              this.a=a;this.level=level;low =i; high=j;
              tellOppNumThr();
           }

           public void run() {
               pQuick(level,a,low,high);
           } // end run
         } // end Para
} //end Oppgave2_ParaQuick
