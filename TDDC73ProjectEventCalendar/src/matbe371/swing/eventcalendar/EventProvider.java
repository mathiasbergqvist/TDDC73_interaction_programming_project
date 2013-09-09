package matbe371.swing.eventcalendar;

import java.util.Vector;

/**
 * Superklass som definierar en klass som är till för att hantera lagring och 
 * hämtning av event så att en klass som använder denna kan vara oberoende av hur detta sker.
 * @author Mathias Bergqvist, matbe371@student.liu.se
 *
 */
public abstract class EventProvider {
	
	/**
	 * Hämtar variabler av klassen Event ur något typ av lagringsmedie
	 * (ex. textfil, databas) och retunerar dessa i en vektor.
	 * @return - alla lagrade event.
	 */
	public abstract Vector<Event> getEvents();
	
	/**
	 * Lägger till ett event till lagringsmediet.
	 * @param e - eventet som ska läggas till.
	 */
	public abstract void addEvent(Event e);

}
