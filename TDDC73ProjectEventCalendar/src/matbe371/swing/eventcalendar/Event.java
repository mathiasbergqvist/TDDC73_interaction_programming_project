package matbe371.swing.eventcalendar;

/**
 * Klass som beskriver egensaper hos ett event som �r t�nkt att anv�ndas i kalendrar av klassen EventCalendar.
 * @author Mathias Bergqvist, matbe371@student.liu.se
 *
 */
public class Event {
	
	private int day;
	private int month;
	private int year;
	private String description;
	
	
	/**
	 * Kostruktor med tom parameterlista.
	 */
	public Event() {}
	
	/**
	 * Konstruktor d� parametrar har skickats med.
	 * @param day - aktuell dag f�r eventent.
	 * @param month - aktuell m�nad f�r eventet.
	 * @param year - aktuellt �r f�r eventet.
	 * @param desc - aktuell beskrivning f�r eventet.
	 */
	public Event(int day, int month, int year, String desc){
		setDay(day);
		setMonth(month);
		setYear(year);
		setDescription(desc);
	}
	
	/**
	 * Set-funktion f�r dag.
	 * @param d - numret p� dagen i m�naden.
	 */
	public void setDay(int d){
		day = d;
	}
	
	/**
	 * Get-funktion f�r dag.
	 * @return - numret d� dagen i m�naden.
	 */
	public int getDay(){
		return day;
	}
	
	/**
	 * Set-funktion f�r m�nad.
	 * @param m - numret p� m�nden i �ret.
	 */
	public void setMonth(int m){
		month = m;
	}
	
	/**
	 * Get-funktion f�r m�naden i �ret.
	 * @return numret p� m�nden i �ret.
	 */
	public int getMonth(){
		return month;
	}
	
	/**
	 * Set-funktion f�r �r.
	 * @param y �rets nummer.
	 */
	public void setYear(int y){
		year = y;
	}
	
	/**
	 * Get-funktion f�r �r.
	 * @return �rets nummer.
	 */
	public int getYear(){
		return year;
	}
	
	/**
	 * S�tter eventtext.
	 * @param d - eventtext i form av textstr�ng.
	 */
	public void setDescription(String d){
		description = d;
	}
	
	/**
	 * Get eventtexten.
	 * @return eventtext i form av textstr�ng.
	 */
	public String getDescription(){
		return description;
	}

}
