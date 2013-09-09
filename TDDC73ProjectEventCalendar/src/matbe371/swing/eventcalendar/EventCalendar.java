package matbe371.swing.eventcalendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Exempel p� klassen EventCalendar, som kan anv�ndas f�r att planera in notationer p� ett visst datum p� ett kalendergr�nssnitt. 
 * @author Mathias Bergqvist, matbe371@student.liu.se
 *
 */
public class EventCalendar extends JComponent implements AncestorListener, ActionListener, MouseListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JWindow calendarWindow;
	private JButton nxtBtn, prevBtn;
	private CalendarComponent calendar;
	private int padding;
	private Vector<Event> events;
	private EventProvider eventProvider;
	
	//Variabler som h�ller koll p� datum och kalenderegenskaper.
	private int realDay, realMonth,	realYear, currentMonth,	currentYear, numberOfDays, startOfMonth;
	
	/*Konstruktor----------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Konstruktor med medskickad eventprovider i parameterlistan.
	 * @param e - Klassen d�r eventen lagras.
	 */
	public EventCalendar(EventProvider e){
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(500, 500));
		this.addAncestorListener(this);
		
		eventProvider = e;
		
		//Skapar knappar till f�r att navigera i kalendern.
		nxtBtn = new JButton(">>");
		prevBtn = new JButton("<<");
		this.add(nxtBtn);
		this.add(prevBtn);
		nxtBtn.setSize(50,30);
		prevBtn.setSize(50,30);
		nxtBtn.addActionListener(this);
		prevBtn.addActionListener(this);
		nxtBtn.setLocation(277,150);
		prevBtn.setLocation(40, 150);
		
		padding = 100;
	}
	
	/*Publika funtioner----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Funktion s� att anv�ndaren utifr�n kan s�tta padding mellan f�nstret och kalendern.
	 * @param p - padding i pixlar.
	 */
	public void setPadding(int p){
		padding = p;
	}
	
	/**
	 * Metod f�r att byta f�rg p� kalendern och dess knappar utifr�n.
	 * @param c - f�rgen som anv�ndaren valt.
	 */
	public void setCalendarColor(Color c){
		nxtBtn.setBackground(c);
		prevBtn.setBackground(c);
		calendar.setCalendarColor(c);
	}
	
	/*Privata funktioner----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Sj�lva kalendern ritas h�r ut med de r�tta parametrarna satta.
	 */
	private void displayCalendar(){
		
		//Om det redan finns en synlig kalender skall denna g�ras av med innan den nya ritas ut.
		if(calendarWindow != null){
			calendarWindow.dispose();
		}
		
		//H�mtar alla aktuella event som finns lagrade.
		events = eventProvider.getEvents();
		
		//Skapar det nya f�nstret och sj�lva kalenderkomponenten.
		calendarWindow = new JWindow(getFrame(this));
		calendar = new CalendarComponent();
		calendarWindow.setContentPane(calendar);
		placeWindow(calendarWindow);
		
		setCalendarColor(calendar.getCalendarColor());
		
		//G�r f�nstret synligt
		calendarWindow.pack();
		calendarWindow.toFront();
		calendarWindow.setVisible(true);
		calendarWindow.requestFocusInWindow();
		
		//L�gger till museventshanterare
		calendarWindow.addMouseListener(this);
		calendarWindow.addMouseMotionListener(this);
		
		//Ritar ut kalendern.
		calendar.repaint();
	}
	
	
	/**
	 * R�knar ut klassvariabler som kalendern �r beroende av innan den visas.
	 * @param month - den m�ndad som skall visas i kalendern.
	 * @param year - det �r som skall visas i kalendern.
	 */
	private void calculateDateParameters(int month, int year){
	
		//Referenskalender f�r inskickade v�rden.
		GregorianCalendar currentCalendar = new GregorianCalendar(year, month, 1);
		numberOfDays = currentCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		startOfMonth = currentCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		displayCalendar();
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
	
	/**
	 * S�tter klassvariabler till dagsaktuella v�rden. 
	 */
	private void getTodaysDate(){
        
		//Anv�nder en referenskalender f�r att plocka ut dagsaktuell dag, m�nad och �r.
		GregorianCalendar cal = new GregorianCalendar();
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
		realMonth = cal.get(GregorianCalendar.MONTH);
		realYear = cal.get(GregorianCalendar.YEAR);
	}

	
	/**
	 * Placearar ut kalenderkomponenten i f�rh�llande till f�r�ldraelementet.
	 * @param w
	 */
	private void placeWindow(JWindow w){
		Point pointParent = calendarWindow.getParent().getLocationOnScreen();
		w.setLocation(pointParent.x+padding, pointParent.y+padding);
	}
	
	
	/**
	 * Om man tryckt p� ett datum visas olika alternativ upp beroende p� om det d�r finns ett event inplanerat eller ej.
	 * @param day - datumet som man klickat p� i kalendergr�nssnittet.
	 */
	private void viewEvent(int day){
		 
		boolean hasEvents = false;
		String input;
		String decription = "";
		Vector<String> displayEvents = new Vector<String>();
		
		for(int i=0; i<events.size(); i++){
			
			//Kollar om det finns ett event inlagt p� just detta datum. 
			if(events.get(i).getYear() == currentYear && events.get(i).getMonth() == currentMonth+1 && events.get(i).getDay() == day){
				hasEvents = true;
				displayEvents.add(events.get(i).getDescription());
			}
		}
		
		//Om det finns ett event inlagt skall detta visas upp, annars skall man kunna l�gga in ett nytt. man kan �ven l�gga till  ett ytterligare event till det tidigare.
		if(hasEvents == true){
			for(int j=0; j<displayEvents.size(); j++){
				decription += displayEvents.get(j)+"\n";
			}
			input = JOptionPane.showInputDialog(this, currentYear+"-"+currentMonth+"-"+day+"\n"+decription+"\n L�gg till ett till event:");
		}
		else{
			input = JOptionPane.showInputDialog(currentYear+"-"+currentMonth+"-"+day+"\n"+"Inget inlagt meddelande. L�gg till nytt:");
		}
		
		if(input != null){
			
			//Kollar s� att inte texten inleds med ett blankslag eller att textf�ltet �r tomt.
			if(input.startsWith(" ")||input.equals("")){
				JOptionPane.showMessageDialog(this, "Textf�ltet f�r inte vara tom eller inledas med ett blankslag.");
			}
			else{
				
				//L�gger till eventet och ritar om kalendern. +1 pga nollindexering.
				Event event = new Event(day, currentMonth+1, currentYear, input); 
				eventProvider.addEvent(event);
				displayCalendar();
			}
		}
		else{
			//Ta bort markeringen och rita om kalendern.
			calendar.setClickedRow(0);
			calendar.setClickedColumn(0);
			calendar.repaint();
		}
			
	}
	
	
	/**
	 * Ber�knar vilken rad anv�ndaren har tryckt p�/hovrat mha koordinater.
	 * @param hover - mushanteringseventet
	 * @return numret p� raden i ordningen.
	 */
	private int setCurrentRow(MouseEvent hover){
		Point location = hover.getLocationOnScreen(); 
		int localYCoord = location.y - calendar.getLocationOnScreen().y - calendar.getLocalOrigo().y;
		
		int squareSize = calendar.getCalenderSquareHeight();
		int currentRow = (int) Math.ceil((double)localYCoord/squareSize);
		return currentRow;
	}
	
	/**
	 * Ber�knar vilken kolumn anv�ndaren har tryckt p�/hovrat mha koordinater.
	 * @param hover - mushanteringseventet
	 * @return numret p� raden i ordningen.
	 */
	private int setCurrentColumn(MouseEvent hover){
		Point location = hover.getLocationOnScreen(); 
		int localXCoord = location.x - calendar.getLocationOnScreen().x - calendar.getLocalOrigo().x;
		
		int squareSize = calendar.getCalenderSquareHeight();
		int currentColumn = (int) Math.ceil((double)localXCoord/squareSize); 
		return currentColumn;
	}
	
	/*Eventhanterare----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	@Override
	public void ancestorAdded(AncestorEvent event) {
		
		//F�rsta g�ngen f�nstret laddas vill vi att det ska laddas med aktuell m�nad och �r.
		getTodaysDate();
		currentMonth = realMonth;
		currentYear = realYear;
		calculateDateParameters(currentMonth, currentYear);
	}
	
	@Override
	public void ancestorMoved(AncestorEvent event) {
		if(calendarWindow!=null){
			placeWindow(calendarWindow);
		}
	}
	
	@Override
	public void ancestorRemoved(AncestorEvent event) {
		if(calendarWindow!=null){
			calendarWindow.dispose();
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == nxtBtn){
			
			//Om vi befinner oss i december skall �ven �ret laddas om.
			if (currentMonth == 11){
				currentMonth = 0;
				currentYear += 1;
			}
			else{
				currentMonth += 1;
			}
			
			//R�knar om parametrar och ritar ut kalenderenheten baserade p� dessa.
			calculateDateParameters(currentMonth, currentYear);
		}
		else if(arg0.getSource() == prevBtn){
			
			//Vill backa ett �r om vi befinner oss i januari.
			if (currentMonth == 0){
				currentMonth = 11;
				currentYear -= 1;
			}
			else{
				currentMonth -= 1;
			}
			calculateDateParameters(currentMonth, currentYear);
		}
		
	}
	
	//Om man klickar p� ett datum.
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//R�knar ut vilken rad resp. column i kalenden man klickat p�.
		int clickedRow = setCurrentRow(e);
		int clickedColumn = setCurrentColumn(e);
		
		//S�tter v�rden f�r 
		calendar.setClickedRow(clickedRow);
		calendar.setClickedColumn(clickedColumn);
		
		calendar.repaint();
		
		//H�mtar datumet som man har klickat p� och skriver ut information baserat p� det.
		int date = calendar.getClickedDate();
		viewEvent(date);
	}
	
	//Triggas n�r man f�r mark�ren �ver kalendern.
	@Override
	public void mouseEntered(MouseEvent e) {
		
		
		//Ber�knar och s�tter aktuell rad/kolumn innan kalendern ritas ut p� nytt.
		int enteredRow = setCurrentRow(e);
		int enteredColumn = setCurrentColumn(e);
		
		calendar.setCurrentRow(enteredRow);
		calendar.setCurrentColumn(enteredColumn);
		
		calendar.repaint();
	}
	
	//Om mark�ren f�rflyttas utanf�r f�nstret nollst�lls aktuell markering
	@Override
	public void mouseExited(MouseEvent e) {
		calendar.setCurrentRow(0);
		calendar.setCurrentColumn(0);
		calendar.repaint();
	}
	
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
		//Ber�knar och s�tter aktuell rad/kolumn innan kalendern ritas ut p� nytt.
		int enteredRow = setCurrentRow(e);
		int enteredColumn = setCurrentColumn(e);
		
		calendar.setCurrentRow(enteredRow);
		calendar.setCurrentColumn(enteredColumn);
		
		calendar.repaint();
		
	}
	
	/*Privata klasser----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Klassen f�r sj�lva kalenderkomponenten. 
	 * @author Mathias
	 *
	 */
	private class CalendarComponent extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int width, height, paddingCalendar, paddingCell, headerHeight, subHeaderHeight, daysFontHeight, currentRow, currentColumn, clickedRow, clickedColumn;
		
		private Font headerFont, subHeaderFont, daysFont;
		FontMetrics metricsHeader, metricsSubHeader, metricsDays; 
		private String monthHeader = "S  M  T  W  T  F  S";
		private Color calendarColor;
		
		/**
		 * Kostrunktor som s�tter standardv�rden f�r komponenten.
		 */
		public CalendarComponent() {
		
			paddingCalendar = 10;
			paddingCell = 2;
			headerFont = new Font("Sans-Serif", Font.BOLD, 19);
			subHeaderFont = new Font("Sans-Serif", Font.BOLD, 17);
			daysFont = new Font("Sans-Serif", Font.PLAIN, 16);
			calendarColor = new Color(202,202,175);
			
			//Ber�kningar av font metrics f�r att avg�ra textstorlek.
			metricsHeader = getFontMetrics(headerFont);
			headerHeight = metricsHeader.getHeight();
			
			metricsSubHeader = getFontMetrics(subHeaderFont);
			subHeaderHeight = metricsSubHeader.getHeight();
			
			metricsDays = getFontMetrics(daysFont);
			daysFontHeight = metricsDays.getHeight();
			
			//Kalenderns storlek
			width = 7*daysFontHeight+paddingCalendar*2;
			height = 6*daysFontHeight+paddingCalendar*2+headerHeight+subHeaderHeight; 
			
			this.setPreferredSize(new Dimension(width, height));
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		}
		
		/**
		 * Retunerar loakalt origo f�r rutn�tssystemt.
		 * @return punkten hos lokalt origo.
		 */
		public Point getLocalOrigo(){
			Point p = new Point();
			p.x = paddingCalendar;
			p.y = paddingCalendar+headerHeight+subHeaderHeight;
			return p;
		}
		
		/**
		 * Ger storleken hos klaenderns celler, som �r kvadratiska.
		 * @return H�jd/bredd hos en cell.
		 */
		public int getCalenderSquareHeight(){
			return daysFontHeight;
		}
		
		/**
		 * Funktion f�r att h�mta kalenderf�rg.
		 * @return kalenderns f�rg.
		 */
		public Color getCalendarColor(){
			return calendarColor;
		}
		
		
		/**
		 * S�tter kanlendern f�rg.
		 * @param c - nya f�rgen.
		 */
		public void setCalendarColor(Color c){
			calendarColor = c;
		}
		
		
		/**
		 * S�tter aktuell rad som anv�ndaren hovrat �ver.
		 * @param row - index f�r raden.
		 */
		public void setCurrentRow(int row){
			currentRow = row;
		}
		
		/**
		 * S�tter aktuell kolumn som anv�ndaren hovrat �ver.
		 * @param column - index f�r kolumnen.
		 */
		public void setCurrentColumn(int column){
			currentColumn = column;
		}
		
		/**
		 * S�tter aktuell rad som anv�ndaren har klickat p�.
		 * @param row - index f�r raden.
		 */
		public void setClickedRow(int row){
			clickedRow = row;
		}
		
		/**
		 * S�tter aktuell kolumn som anv�ndaren har klickat p�.
		 * @param column - index f�r kolumnen.
		 */
		public void setClickedColumn(int column){
			clickedColumn = column;
		}
		
		/**
		 * S�tter kalenderns rubrik baserat p� aktuellt datum/�r.
		 * @return rubriken i textformat.
		 */
		private String getHeader(){
			
			String year = Integer.toString(currentYear);
			if(currentMonth == 0){
				return "January "+year;
			}
			else if(currentMonth == 1){
				return "February "+year;
			}
			else if(currentMonth == 2){
				return "March "+year;
			}
			else if(currentMonth == 3){
				return "April "+year;
			}
			else if(currentMonth == 4){
				return "May "+year;
			}
			else if(currentMonth == 5){
				return "June "+year;
			}
			else if(currentMonth == 6){
				return "July "+year;
			}
			else if(currentMonth == 7){
				return "August "+year;
			}
			else if(currentMonth == 8){
				return "September "+year;
			}
			else if(currentMonth == 9){
				return "October "+year;
			}
			else if(currentMonth == 10){
				return "November "+year;
			}
			else if(currentMonth == 11){
				return "December "+year;
			}
			else 
				return "";
		}
		
		/**
		 * Best�mmer vilket datum som anv�ndaren har klickat p�.
		 * @return datumet i m�naden som anv�ndaren klickat p�.
		 */
		public int getClickedDate(){
			for(int i=1; i<=numberOfDays; i++){
				
				//Ber�knar klassvariabler f�r rad  resp. koumn i kalendeen mha avrundning.
				int row = new Integer((i+startOfMonth-2)/7);
				int column = (i+startOfMonth-2)%7;
				
				//-1 pga nollindexering.
				if(clickedRow-1 == row && clickedColumn-1 == column){
					return i;
				}
			}
			return 0;
		}
		
		@Override
		public void paint(Graphics g) {
					
			
			//Ritar ut bakgrunden och ram till kalendern.
			g.setColor(calendarColor);
			g.fillRect(0, 0, width, height);
			
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, width, height);
			
			//Ritar ut rubrik f�r m�nad och �r.
			g.setFont(headerFont);
			g.drawString(getHeader(), paddingCalendar, headerHeight);
			
			//Ritar ut rubrik f�r bokst�ver som representerar dagar
			g.setFont(subHeaderFont);
			g.drawString(monthHeader, paddingCalendar, headerHeight+subHeaderHeight);
			
			//Ritar ut kalenderns rutn�t och datum.
			g.setFont(daysFont);
			for(int i=0; i<7; i++){
				for(int j=0; j<6; j++){
					g.setColor(Color.WHITE);
					g.fillRect(i*daysFontHeight+paddingCalendar,j*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
					g.setColor(Color.BLACK);
					g.drawRect(i*daysFontHeight+paddingCalendar,j*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
			}
			
			//Ritar ut aktuella m�nadens datum baserat p� aktuella variabler.
			for(int k=1; k<=numberOfDays; k++){
				
				//Aktuell rad och kolumn f�s genom att anv�nda restprodukter vid division.
				int row = new Integer((k+startOfMonth-2)/7);
				int column = (k+startOfMonth-2)%7;
				
		
				//Om ett event finns inlaggt p� aktuellt datum markeras detta med cyan
				for(int l=0; l<events.size(); l++){
					if(events.get(l).getYear() == currentYear && events.get(l).getMonth() == (currentMonth+1) && events.get(l).getDay() == k){
						g.setColor(Color.CYAN);
						g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
					}
				}
				
				
				//Om dagens datum finns representerat i m�naden markeras det med bl�tt.
				if(currentYear == realYear && currentMonth == realMonth && realDay == k){
					g.setColor(Color.BLUE);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Om man f�r mark�ren �ver ett datum ska det markeras med gult
				if(currentRow-1 == row && currentColumn-1 == column){
					g.setColor(Color.YELLOW);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Om man har klickat p� ett datum markeras det och meddelandekomponenten ritas ut. Datumet sparas i en variabel.
				if(clickedRow-1 == row && clickedColumn-1 == column){
					//clickedDate = k;
					g.setColor(Color.YELLOW);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Sj�lva siffrorna som representerar datum ritas ut.
				g.setColor(Color.BLACK);
				String date = Integer.toString(k);
				g.drawString(date, column*daysFontHeight+paddingCalendar+paddingCell, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight+daysFontHeight-paddingCell);
			}
			
		}
		
	}
	
	
}
