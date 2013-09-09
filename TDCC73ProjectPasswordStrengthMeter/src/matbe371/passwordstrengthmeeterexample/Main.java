package matbe371.passwordstrengthmeeterexample;

import javax.swing.*;

//Main är en klass som bara startar apikationen
public class Main {
	
	//Kör exempelgränssinttent GUIpasswordfield i Swing-tråden SwingUtilities.
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new GUIpasswordfield();
			}
		});
	}
	
	

}
