package matbe371.eventcalendarexample;

import javax.swing.SwingUtilities;

public class Main {
	//K�r exempelgr�nssinttent GUIeventcalendar i Swing-tr�den SwingUtilities.
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new GUIeventcalendar();
			}
		});
	}
}
