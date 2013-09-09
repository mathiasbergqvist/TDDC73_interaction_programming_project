package matbe371.swing.eventcalendar;

/**
 * Klass som beskriver egensaper hos ett event som är tänkt att användas i kalendrar av klassen EventCalendar.
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
	 * Konstruktor då parametrar har skickats med.
	 * @param day - aktuell dag för eventent.
	 * @param month - aktuell månad för eventet.
	 * @param year - aktuellt år för eventet.
	 * @param desc - aktuell beskrivning för eventet.
	 */
	public Event(int day, int month, int year, String desc){
		setDay(day);
		setMonth(month);
		setYear(year);
		setDescription(desc);
	}
	
	/**
	 * Set-funktion för dag.
	 * @param d - numret på dagen i månaden.
	 */
	public void setDay(int d){
		day = d;
	}
	
	/**
	 * Get-funktion för dag.
	 * @return - numret då dagen i månaden.
	 */
	public int getDay(){
		return day;
	}
	
	/**
	 * Set-funktion för månad.
	 * @param m - numret på månden i året.
	 */
	public void setMonth(int m){
		month = m;
	}
	
	/**
	 * Get-funktion för månaden i året.
	 * @return numret på månden i året.
	 */
	public int getMonth(){
		return month;
	}
	
	/**
	 * Set-funktion för år.
	 * @param y årets nummer.
	 */
	public void setYear(int y){
		year = y;
	}
	
	/**
	 * Get-funktion för år.
	 * @return årets nummer.
	 */
	public int getYear(){
		return year;
	}
	
	/**
	 * Sätter eventtext.
	 * @param d - eventtext i form av textsträng.
	 */
	public void setDescription(String d){
		description = d;
	}
	
	/**
	 * Get eventtexten.
	 * @return eventtext i form av textsträng.
	 */
	public String getDescription(){
		return description;
	}

}
