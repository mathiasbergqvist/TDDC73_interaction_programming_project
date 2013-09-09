package matbe371.swing.eventcalendar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * KLass som hanterar lagring och hämtning av event via textfilshantering.
 * @author Mathias Bergqvist, matbe371@student.liu.se
 *
 */
public class TextfileEventProvider extends EventProvider{
	
	private String textfileSrc;
	private Vector<Event> allEvents = new Vector<Event>();
	
	/**
	 * Klassens kontruktor måste ta emot en sökväg till en textfil för att inte ge ett nullpointer-error vid användning.
	 * @param src
	 */
	public TextfileEventProvider(String src) {
		textfileSrc = src;
	}
	
	/**
	 * Sätt vilken fil som ska läsas/skrivas till.
	 * @param src - sökvägen till textfilen.
	 */
	public void setSource(String src){
		textfileSrc = src;
	}
	
	/**
	 * Get-funktion för att hämta länken till textfilen
	 * @return URL till textfilen.
	 */
	public String getSource(){
		return textfileSrc;
	}
	
	@Override
	public Vector<Event> getEvents(){
		
		if(!allEvents.isEmpty()){
			allEvents.clear();
		}
		try{
			Scanner sc = new Scanner(new File(textfileSrc));
			while(sc.hasNext()){
	
				String date = sc.next();
				String description = sc.nextLine();
				
				String trimDescription = description.trim();
				
				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(5, 7));
				int day = Integer.parseInt(date.substring(8));
				
				Event event = new Event(day, month, year, trimDescription);
				
				allEvents.add(event);
				
			}
			sc.close();
			return allEvents;
		}
		catch(FileNotFoundException a){
			JOptionPane.showMessageDialog(null, "File not found");
		}
		return allEvents;
	}
	
	@Override
	public void addEvent(Event e){
		
		Vector<Event> events = getEvents();
		events.add(e);
		
		try {
			PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(textfileSrc)));
			
			for(int i=0; i<events.size(); i++){
				String eventText = events.get(i).getYear()+"-"+events.get(i).getMonth()+"-"+events.get(i).getDay()+" "+events.get(i).getDescription();
				outFile.println(eventText);
			}
			
			outFile.close();
		} 
		catch(FileNotFoundException a){
			JOptionPane.showMessageDialog(null, "File not found");
		}
		catch (IOException exception) {
			System.err.println("Error: " + exception.getMessage());
		}
	}
	
}
	
	
	