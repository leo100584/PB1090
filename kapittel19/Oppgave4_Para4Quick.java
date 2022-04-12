import java.util.*;
import java.util.concurrent.*;

class Oppgave4_Para4Quick {
	int [] a;
	static int numThr =1;  // main
	final static int PARA_LIMIT = 10000; // mindre tas rekursivt
	static int LEVEL_MAX =0;
	int numThreads =2;
	CyclicBarrier b;

	void pQuick(int [] a) {
		 int antKjerner = Runtime.getRuntime().availableProcessors();
		   int n = a.length;
		   while (n > PARA_LIMIT
		   && numThreads < 20*antKjerner
		   ) {
			   LEVEL_MAX++;
			   numThreads *= 2;
			   n = n/2;
		   }
		    b = new CyclicBarrier (numThreads-1);
		    pQuick(0,a, 0,a.length-1);
		    try {  // main-traade venter herwait common barrier
					b.await();
			} catch (Exception e) {return;}
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

			if ( level < LEVEL_MAX){
				 new Thread(new Para(level+1,a,lav,mindre-2)).start();// sorter alle <= piv (untatt piv)
				 new Thread(new Para(level+1,a,mindre,hoy  )).start(); //sorter alle > piv
			} else {
				 // resten av treet raskere med rekursjon
		    	 if ( mindre-lav > 2) pQuick (level+1,a, lav,mindre-2); // sorter alle <= piv (untatt piv)
			     if ( hoy-mindre > 0) pQuick (level+1,a, mindre, hoy);  // sorter alle > piv
			}
	     } // end sortering av a[i..j]

	}// end pQuick

	Oppgave4_Para4Quick(int n) {
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
		 	  numThr+" paa:"+	((double)(t)/1000000.0)+ " millisek.");
	 } // end utforOgTest


	  public static void main (String [] args) {
		 new Oppgave4_Para4Quick(Integer.parseInt(args[0])).utforOgTest();
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
			   try {  // wait common barrier
			   		b.await();
			    } catch (Exception e) {return;}
		   } // end run
         } // end Para
} //end Oppgave4_Para4Quick
