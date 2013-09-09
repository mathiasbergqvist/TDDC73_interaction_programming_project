package matbe371.passwordstrengthmeeterexample;

//Improterar paketet som jag själv har skapat som innehåller själva komponenten. 
import matbe371.swing.passwordstrengthmeeter.*; 

import javax.swing.*;

import java.awt.FlowLayout;

//Användargränssnittet, som ärver egenskaper från Swing-komponenten JFrame.
public class GUIpasswordfield extends JFrame{
	
	
	PasswordStrengthMeter pwdComponent;
	
	//Konstruktorn som genererar själva användargränssnittet.
	public GUIpasswordfield() {
		
		//Lägger till själva komponenten.
		pwdComponent = new PasswordStrengthMeter();
		this.add(pwdComponent);
		
		//Sätter egenskaper för JFrame
		this.setSize(600,300);
		this.setTitle("Exempel på komponenten PasswordStrengthMeter");
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
	}

}
