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
	
	//Konstruktor för hela komponenten.
	public PasswordStrengthMeter() {
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(150,30));
		this.addAncestorListener(this);
		
		//Skapar och  lägger till Swing-komponenten JPasswordField.
		pwdField = new JPasswordField();
		pwdField.setSize(150,30);
		pwdField.setLocation(0, 0);
		this.add(pwdField);
		
		//Lägger på en dokumentlyssnare som kommer att triggas när innehållet i dokumetet (pwdField) ändras. Det är då vi vill att lösenordsevalueringen ska triggas.
		pwdField.getDocument().addDocumentListener(new PwdFieldDocumentListener());
			
	}

	/**
	 * Generell lösning för att ändra minsta tillåtna längd på lösenordet.
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
			
		//Om det redan finns ett fönster framme tas det bort.
		if(validateWindow != null){
			validateWindow.dispose();
		}
		
		//Skapar ett nytt fönster och fyller den med komponenter som skall ingå.
		validateWindow = new JWindow(getFrame(null));
		
		//Skapar valideringskompnenten
		validationBar = new PasswordValidationBar();
		validateWindow.setContentPane(validationBar);
		validationBar.setValidation(vaildation);

		//Placerar ut fönstret på skärmen i förhållande till lösenordsfältet.
		PlaceWindow(validateWindow);
		
		//Gör fönstret synligt
		validateWindow.pack();
		validateWindow.toFront();
		validateWindow.setVisible(true);
		validateWindow.requestFocusInWindow();
		
		//Ritar om komponenten på skärmen med de nya parametervärdena insatta.
		validationBar.repaint();
	}
	
	/**
	 * Hämtar root-fönstret till konponenten rekursivt så att föstret vet vilken komponent den placeras i.
	 * @param c - Det fönster som jag skickar in för att skapa komponenter i.
	 * @return Rood-fönstret som c ligger placerad i.
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
		// Gör valideringsfönstret osynligt då komponenten skapas. 
		if(validateWindow != null){
			validateWindow.setVisible(false);
		}
		
	}
	
	@Override
	public void ancestorMoved(AncestorEvent arg0) {
		// Gör så att valderingsfönstret följer med då komponenten flyttas.
		if(validateWindow!=null){
			PlaceWindow(validateWindow);
		}
		
	}
	
	@Override
	public void ancestorRemoved(AncestorEvent arg0) {
		// Valideringsfönstret försvinner då komponenten stängs.
		if(validateWindow!=null){
			validateWindow.dispose();
		}
		
	}
	
	private class PasswordValidatorExecutor extends SwingWorker<String, String>{
		
		/**
		 *  Validerar trextsträngen i lösenordfältet i en tråd separead från Swing-tråden. Använder en instans av PasswordValidator-klassen.
		 */
		@Override
		protected String doInBackground() throws Exception {
			
			String pwdValidation = PasswordValidator.getInstance().vaidatePassword(pwdField.getText(), minPwdLength);
			return pwdValidation;
		}
		
		/**
		 * Anropas när doInBackground() kört klart. Körs i Swingtråden.
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
	
	
	//Klass av DocumentListener-typ som hanterar event för lösenordsfältet.
	private class PwdFieldDocumentListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent e) {
			// Detta event gör ingenting, för det kommer aldrig att triggas av textkomponeneter.
		}

		//Triggas vid inmatining i lösenordfältet.
		@Override
		public void insertUpdate(DocumentEvent e) {
			new PasswordValidatorExecutor().execute();
		}

		//Triggas vid brottagning av text i lösenordsfältet. 
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
		
		//Ritar ut mätarens bakgrund
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(padding, height-barHeight, barWidth, barHeight);
		
		//Ritar ut själva mätaren
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
