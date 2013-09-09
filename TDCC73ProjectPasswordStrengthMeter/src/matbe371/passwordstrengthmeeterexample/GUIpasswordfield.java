package matbe371.passwordstrengthmeeterexample;

//Improterar paketet som jag sj�lv har skapat som inneh�ller sj�lva komponenten. 
import matbe371.swing.passwordstrengthmeeter.*; 

import javax.swing.*;

import java.awt.FlowLayout;

//Anv�ndargr�nssnittet, som �rver egenskaper fr�n Swing-komponenten JFrame.
public class GUIpasswordfield extends JFrame{
	
	
	PasswordStrengthMeter pwdComponent;
	
	//Konstruktorn som genererar sj�lva anv�ndargr�nssnittet.
	public GUIpasswordfield() {
		
		//L�gger till sj�lva komponenten.
		pwdComponent = new PasswordStrengthMeter();
		this.add(pwdComponent);
		
		//S�tter egenskaper f�r JFrame
		this.setSize(600,300);
		this.setTitle("Exempel p� komponenten PasswordStrengthMeter");
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
	}

}
