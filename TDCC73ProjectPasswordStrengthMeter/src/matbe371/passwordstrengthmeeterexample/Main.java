package matbe371.passwordstrengthmeeterexample;

import javax.swing.*;

//Main �r en klass som bara startar apikationen
public class Main {
	
	//K�r exempelgr�nssinttent GUIpasswordfield i Swing-tr�den SwingUtilities.
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new GUIpasswordfield();
			}
		});
	}
	
	

}
