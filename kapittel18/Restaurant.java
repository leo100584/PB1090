import java.util.Scanner;

/* Løsningsforslag oppgave 2 og 3, kapittel 18.
 * Lagres på fil Restaurant.java
 * 
 * Feilen oppstår i den ene kokken har sjekket at fler tallerkner skal
 * leveres og går inn i while løkken. Før kallet på rest.putDish
 * er det ingen lås, og den andre kokken rekker å komme inn i sin
 * while-løkke. Dermed vil begge kokkene befinne seg i while-løkka og
 * det blir produsert minst en tallerken for mye. Det hjelper med
 * andre ord ikke at kokkFerdig-metoden er synkronisert, så lenge
 * kokken slipper låsen før kallet på putDish-metoden.
 *
 * En mulig løsning er å legge kallet på kokkFerdig-metoden i
 * putDish-metoden. Hvis vi i tillegg lar putDish-metoden
 * returnere svaret fra kokkFerdig-metoden kan vi bruke denne som
 * betingelsen i while-løkka.
 */


public class Restaurant {
    int numberOrdered;
    int numberMade = 0, numberServed = 0; // tallerkenrertter
    int numberOfChefs = 5, antServitorer = 7;

    Restaurant(int ant) {
	numberOrdered = ant;
	for (int i = 0; i < numberOfChefs; i++) {
	    Chef k = new Chef(this, "Chef nr. " + i);
	    k.start();
	}
	
	for (int i = 0; i < antServitorer; i++) {
	    Waiter s = new Waiter(this, "Waiter nr. " + i);
	    s.start();
	}
	
    }

    public static void main(String[] args) {
		Scanner readInput = new Scanner(System.in);
		System.out.println("Antall bestilt: ");
		int dishes = readInput.nextInt();


		new Restaurant(dishes);
        readInput.close();
    }

    synchronized boolean chefFinished() {
	return numberMade == numberOrdered;
    }

    synchronized boolean waiterFinished() {
	return numberServed == numberOrdered;
    }

    synchronized boolean putDish(Chef k) {
	// Chefetråden blir eier av låsen.

	while (numberMade - numberServed >= 2) {
	    /* s� lenge det er minst 2 tallerkner
	     * som ikke er servert, skal kokken vente. */
	    try {
		wait(); /* Chefetråden gir fra seg 
			 * låsen og sover til den 
			 * blir vekket */
	    } catch (InterruptedException e) {}
	    // Chefetråden blir igjen eier av låsen

	}

	boolean ferdig = chefFinished();
	if (!ferdig) {
	    numberMade++;
	    System.out.println(k.getName() + " laget nr: " + numberMade);
	}
	
	notify(); /* Si ifra til servitøren. */

	return !ferdig;
    }

    synchronized boolean getDish(Waiter s) {
	// Servitørtråden blir eier av låsen.
	
	while (numberMade == numberServed && !waiterFinished()) {
	    /* så lenge kokken ikke har plassert 
	     * en ny tallerken. Dermed skal 
	     * servitøren vente. */
	    try {
		wait(); /* ServitørTråden gir fra seg
			 * låsen og sover til den
			 * blir vekket */
	    } catch(InterruptedException e) {}
	    // ServitørTråden blir igjen eier av låsen.
	}

	boolean ferdig = waiterFinished();
	if (!ferdig) {
	    numberServed++;
	    System.out.println(s.getName() + " serverer nr: " + numberServed);
	}


	notify(); /* si ifra til kokken */
	return !ferdig;
    }
}

class Chef extends Thread {
    Restaurant rest;
    
    Chef(Restaurant rest, String navn) { 
	super(navn); // Denne tråden heter nå 
	this.rest = rest;
    }

    public void run() {
	while (rest.putDish(this)) {
	    // levert tallerken.
	    
	    try {
		sleep((long) (1000 * Math.random()));
	    } catch (InterruptedException e) {}
	}
	// kokken er ferdig
    }
}

class Waiter extends Thread {
    Restaurant rest;

    Waiter(Restaurant rest, String navn) {
	super(navn); // Denne tråden heter nå
	this.rest = rest;
    }

    public void run() {
	while (rest.getDish(this)) {
	    try {
		sleep((long) (1000 * Math.random()));
	    } catch (InterruptedException e) {}
	}
	// Servitøren er ferdig

    }
}



