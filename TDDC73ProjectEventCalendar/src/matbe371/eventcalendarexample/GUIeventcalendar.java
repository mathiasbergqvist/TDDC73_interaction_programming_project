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
		
		//S�tter Eventprovidren
		eventProvider = new TextfileEventProvider(textfileSrc);
		
		//L�gger till sj�lva komponenten
		calendar = new EventCalendar(eventProvider);
		this.add(calendar);
		
		//S�tter egenskaper f�r JFrame
		this.setSize(500,500);
		this.setTitle("Exempel p� komponenten EventCalendar");
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
