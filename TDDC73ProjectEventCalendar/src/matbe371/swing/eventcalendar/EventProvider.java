package matbe371.swing.eventcalendar;

import java.util.Vector;

/**
 * Superklass som definierar en klass som �r till f�r att hantera lagring och 
 * h�mtning av event s� att en klass som anv�nder denna kan vara oberoende av hur detta sker.
 * @author Mathias Bergqvist, matbe371@student.liu.se
 *
 */
public abstract class EventProvider {
	
	/**
	 * H�mtar variabler av klassen Event ur n�got typ av lagringsmedie
	 * (ex. textfil, databas) och retunerar dessa i en vektor.
	 * @return - alla lagrade event.
	 */
	public abstract Vector<Event> getEvents();
	
	/**
	 * L�gger till ett event till lagringsmediet.
	 * @param e - eventet som ska l�ggas till.
	 */
	public abstract void addEvent(Event e);

}
