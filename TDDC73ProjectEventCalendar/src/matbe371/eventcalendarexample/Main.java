package matbe371.eventcalendarexample;

import javax.swing.SwingUtilities;

public class Main {
	//Kör exempelgränssinttent GUIeventcalendar i Swing-tråden SwingUtilities.
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new GUIeventcalendar();
			}
		});
	}
}
