package matbe371.swing.passwordstrengthmeeter;

import java.awt.*;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PasswordStrengthMeter extends JComponent implements AncestorListener{
	
	private int minPwdLength = 5;
	private boolean sizeCheck = true;
	
	private JPasswordField pwdField;
	private JWindow validateWindow;
	private PasswordValidationBar validationBar;
	
	//Konstruktor f�r hela komponenten.
	public PasswordStrengthMeter() {
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(150,30));
		this.addAncestorListener(this);
		
		//Skapar och  l�gger till Swing-komponenten JPasswordField.
		pwdField = new JPasswordField();
		pwdField.setSize(150,30);
		pwdField.setLocation(0, 0);
		this.add(pwdField);
		
		//L�gger p� en dokumentlyssnare som kommer att triggas n�r inneh�llet i dokumetet (pwdField) �ndras. Det �r d� vi vill att l�senordsevalueringen ska triggas.
		pwdField.getDocument().addDocumentListener(new PwdFieldDocumentListener());
			
	}

	/**
	 * Generell l�sning f�r att �ndra minsta till�tna l�ngd p� l�senordet.
	 *@param antal tecken. 
	*/ 
	public void setMinPasswordLength(int length){
		minPwdLength = length;
	}
	
	private void PlaceWindow(JWindow w){
		Point pwdFieldPlacement = pwdField.getLocationOnScreen();
		w.setLocation(pwdFieldPlacement.x + pwdField.getWidth(), pwdFieldPlacement.y-validationBar.getHeight()+pwdField.getHeight());
	}
	
	private void displayEvaluatorComponent(String vaildation){
			
		//Om det redan finns ett f�nster framme tas det bort.
		if(validateWindow != null){
			validateWindow.dispose();
		}
		
		//Skapar ett nytt f�nster och fyller den med komponenter som skall ing�.
		validateWindow = new JWindow(getFrame(null));
		
		//Skapar valideringskompnenten
		validationBar = new PasswordValidationBar();
		validateWindow.setContentPane(validationBar);
		validationBar.setValidation(vaildation);

		//Placerar ut f�nstret p� sk�rmen i f�rh�llande till l�senordsf�ltet.
		PlaceWindow(validateWindow);
		
		//G�r f�nstret synligt
		validateWindow.pack();
		validateWindow.toFront();
		validateWindow.setVisible(true);
		validateWindow.requestFocusInWindow();
		
		//Ritar om komponenten p� sk�rmen med de nya parameterv�rdena insatta.
		validationBar.repaint();
	}
	
	/**
	 * H�mtar root-f�nstret till konponenten rekursivt s� att f�stret vet vilken komponent den placeras i.
	 * @param c - Det f�nster som jag skickar in f�r att skapa komponenter i.
	 * @return Rood-f�nstret som c ligger placerad i.
	 */
	private Frame getFrame(Component c){
		if(c == null){
			c = this;
		}
		if (c.getParent() instanceof Frame){
			return (Frame) c.getParent();
		}
		return getFrame(c.getParent());
	}
	
	@Override
	public void ancestorAdded(AncestorEvent arg0) {
		// G�r valideringsf�nstret osynligt d� komponenten skapas. 
		if(validateWindow != null){
			validateWindow.setVisible(false);
		}
		
	}
	
	@Override
	public void ancestorMoved(AncestorEvent arg0) {
		// G�r s� att valderingsf�nstret f�ljer med d� komponenten flyttas.
		if(validateWindow!=null){
			PlaceWindow(validateWindow);
		}
		
	}
	
	@Override
	public void ancestorRemoved(AncestorEvent arg0) {
		// Valideringsf�nstret f�rsvinner d� komponenten st�ngs.
		if(validateWindow!=null){
			validateWindow.dispose();
		}
		
	}
	
	private class PasswordValidatorExecutor extends SwingWorker<String, String>{
		
		/**
		 *  Validerar trextstr�ngen i l�senordf�ltet i en tr�d separead fr�n Swing-tr�den. Anv�nder en instans av PasswordValidator-klassen.
		 */
		@Override
		protected String doInBackground() throws Exception {
			
			String pwdValidation = PasswordValidator.getInstance().vaidatePassword(pwdField.getText(), minPwdLength);
			return pwdValidation;
		}
		
		/**
		 * Anropas n�r doInBackground() k�rt klart. K�rs i Swingtr�den.
		 */
		@Override
		protected void done() {
			try {
				displayEvaluatorComponent(get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {		
				e.printStackTrace();
			}
		}
		
	}
	
	
	//Klass av DocumentListener-typ som hanterar event f�r l�senordsf�ltet.
	private class PwdFieldDocumentListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			// Detta event g�r ingenting, f�r det kommer aldrig att triggas av textkomponeneter.
		}

		//Triggas vid inmatining i l�senordf�ltet.
		@Override
		public void insertUpdate(DocumentEvent e) {
			new PasswordValidatorExecutor().execute();
		}

		//Triggas vid brottagning av text i l�senordsf�ltet. 
		@Override
		public void removeUpdate(DocumentEvent e) {
			new PasswordValidatorExecutor().execute();
		}
		
	}
	
	private class PasswordValidationBar extends JPanel{
		
		private int width;
		private int height;
		private int padding;
		
		private int barHeight = 8;
		private int barWidth = 150;
		private int meterLength = 0;
		private Color vaildationColor;
		
		private Font valuationFont;
		private String validation; 
		
		public PasswordValidationBar() {
			
			width = 200;
			height = 30;
			padding = 5;
			
			valuationFont = new Font("Sans-Serif", Font.BOLD, 16);
			this.setPreferredSize(new Dimension(width, height));
			validation = "Too short";
		}
		

		public void setWidth(int w){
			width = w;
		}
		
		public void setHeight(int h){
			height = h;
		}
		
		public int getWidth(){
			return width;
		}
		
		public int getHeight(){
			return height;
		}
		
		public void setPadding(int p){
			padding = p;
		}
		
		public int getPadding(){
			return padding;
		}
		
		
		
		public void setValidation(String v){
			validation = v;
			if(validation.equals("Too Short")){
				meterLength = 0;
			}
			else if(validation.equals("Weak")){
				meterLength = (int) Math.ceil(barWidth/4);
				vaildationColor = Color.RED;
			}
			else if(validation.equals("Fair")){
				meterLength = (int) Math.ceil(barWidth/2);
				vaildationColor = Color.YELLOW;
			}
			else if(validation.equals("Good")){
				meterLength = (int) Math.ceil((3*barWidth)/4);
				vaildationColor = Color.BLUE;
			}
			else if(validation.equals("Strong")){
				meterLength = barWidth;
				vaildationColor = Color.GREEN;
			}
		}
		
		
		@Override
		public void paint(Graphics g) {
		
		//Ritar ut m�tarens bakgrund
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(padding, height-barHeight, barWidth, barHeight);
		
		//Ritar ut sj�lva m�taren
		g.setColor(vaildationColor);
		g.fillRect(padding, height-barHeight, meterLength, barHeight);
		
		//Ritar ut ord
		FontMetrics metrics = getFontMetrics(valuationFont);
		int wordWidth = metrics.stringWidth(validation);
		
		g.setFont(valuationFont);
		g.setColor(vaildationColor);
		g.drawString(validation, barWidth-wordWidth, height-(barHeight+padding));
		}
	}
	

}
