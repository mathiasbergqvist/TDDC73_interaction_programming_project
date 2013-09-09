package matbe371.eventcalendarexample;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import matbe371.swing.eventcalendar.*;

public class GUIeventcalendar extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	EventCalendar calendar;
	EventProvider eventProvider; 
	String textfileSrc = "src/matbe371/swing/eventcalendar/Events.txt";
	
	public GUIeventcalendar() {
		
		//Sätter Eventprovidren
		eventProvider = new TextfileEventProvider(textfileSrc);
		
		//Lägger till själva komponenten
		calendar = new EventCalendar(eventProvider);
		this.add(calendar);
		
		//Sätter egenskaper för JFrame
		this.setSize(500,500);
		this.setTitle("Exempel på komponenten EventCalendar");
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
