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
 * Exempel på klassen EventCalendar, som kan användas för att planera in notationer på ett visst datum på ett kalendergränssnitt. 
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
	
	//Variabler som håller koll på datum och kalenderegenskaper.
	private int realDay, realMonth,	realYear, currentMonth,	currentYear, numberOfDays, startOfMonth;
	
	/*Konstruktor----------------------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Konstruktor med medskickad eventprovider i parameterlistan.
	 * @param e - Klassen där eventen lagras.
	 */
	public EventCalendar(EventProvider e){
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(500, 500));
		this.addAncestorListener(this);
		
		eventProvider = e;
		
		//Skapar knappar till för att navigera i kalendern.
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
	 * Funktion så att användaren utifrån kan sätta padding mellan fönstret och kalendern.
	 * @param p - padding i pixlar.
	 */
	public void setPadding(int p){
		padding = p;
	}
	
	/**
	 * Metod för att byta färg på kalendern och dess knappar utifrån.
	 * @param c - färgen som användaren valt.
	 */
	public void setCalendarColor(Color c){
		nxtBtn.setBackground(c);
		prevBtn.setBackground(c);
		calendar.setCalendarColor(c);
	}
	
	/*Privata funktioner----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Själva kalendern ritas här ut med de rätta parametrarna satta.
	 */
	private void displayCalendar(){
		
		//Om det redan finns en synlig kalender skall denna göras av med innan den nya ritas ut.
		if(calendarWindow != null){
			calendarWindow.dispose();
		}
		
		//Hämtar alla aktuella event som finns lagrade.
		events = eventProvider.getEvents();
		
		//Skapar det nya fönstret och själva kalenderkomponenten.
		calendarWindow = new JWindow(getFrame(this));
		calendar = new CalendarComponent();
		calendarWindow.setContentPane(calendar);
		placeWindow(calendarWindow);
		
		setCalendarColor(calendar.getCalendarColor());
		
		//Gör fönstret synligt
		calendarWindow.pack();
		calendarWindow.toFront();
		calendarWindow.setVisible(true);
		calendarWindow.requestFocusInWindow();
		
		//Lägger till museventshanterare
		calendarWindow.addMouseListener(this);
		calendarWindow.addMouseMotionListener(this);
		
		//Ritar ut kalendern.
		calendar.repaint();
	}
	
	
	/**
	 * Räknar ut klassvariabler som kalendern är beroende av innan den visas.
	 * @param month - den måndad som skall visas i kalendern.
	 * @param year - det år som skall visas i kalendern.
	 */
	private void calculateDateParameters(int month, int year){
	
		//Referenskalender för inskickade värden.
		GregorianCalendar currentCalendar = new GregorianCalendar(year, month, 1);
		numberOfDays = currentCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		startOfMonth = currentCalendar.get(GregorianCalendar.DAY_OF_WEEK);
		displayCalendar();
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
	
	/**
	 * Sätter klassvariabler till dagsaktuella värden. 
	 */
	private void getTodaysDate(){
        
		//Använder en referenskalender för att plocka ut dagsaktuell dag, månad och år.
		GregorianCalendar cal = new GregorianCalendar();
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
		realMonth = cal.get(GregorianCalendar.MONTH);
		realYear = cal.get(GregorianCalendar.YEAR);
	}

	
	/**
	 * Placearar ut kalenderkomponenten i förhållande till föräldraelementet.
	 * @param w
	 */
	private void placeWindow(JWindow w){
		Point pointParent = calendarWindow.getParent().getLocationOnScreen();
		w.setLocation(pointParent.x+padding, pointParent.y+padding);
	}
	
	
	/**
	 * Om man tryckt på ett datum visas olika alternativ upp beroende på om det där finns ett event inplanerat eller ej.
	 * @param day - datumet som man klickat på i kalendergränssnittet.
	 */
	private void viewEvent(int day){
		 
		boolean hasEvents = false;
		String input;
		String decription = "";
		Vector<String> displayEvents = new Vector<String>();
		
		for(int i=0; i<events.size(); i++){
			
			//Kollar om det finns ett event inlagt på just detta datum. 
			if(events.get(i).getYear() == currentYear && events.get(i).getMonth() == currentMonth+1 && events.get(i).getDay() == day){
				hasEvents = true;
				displayEvents.add(events.get(i).getDescription());
			}
		}
		
		//Om det finns ett event inlagt skall detta visas upp, annars skall man kunna lägga in ett nytt. man kan även lägga till  ett ytterligare event till det tidigare.
		if(hasEvents == true){
			for(int j=0; j<displayEvents.size(); j++){
				decription += displayEvents.get(j)+"\n";
			}
			input = JOptionPane.showInputDialog(this, currentYear+"-"+currentMonth+"-"+day+"\n"+decription+"\n Lägg till ett till event:");
		}
		else{
			input = JOptionPane.showInputDialog(currentYear+"-"+currentMonth+"-"+day+"\n"+"Inget inlagt meddelande. Lägg till nytt:");
		}
		
		if(input != null){
			
			//Kollar så att inte texten inleds med ett blankslag eller att textfältet är tomt.
			if(input.startsWith(" ")||input.equals("")){
				JOptionPane.showMessageDialog(this, "Textfältet får inte vara tom eller inledas med ett blankslag.");
			}
			else{
				
				//Lägger till eventet och ritar om kalendern. +1 pga nollindexering.
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
	 * Beräknar vilken rad användaren har tryckt på/hovrat mha koordinater.
	 * @param hover - mushanteringseventet
	 * @return numret på raden i ordningen.
	 */
	private int setCurrentRow(MouseEvent hover){
		Point location = hover.getLocationOnScreen(); 
		int localYCoord = location.y - calendar.getLocationOnScreen().y - calendar.getLocalOrigo().y;
		
		int squareSize = calendar.getCalenderSquareHeight();
		int currentRow = (int) Math.ceil((double)localYCoord/squareSize);
		return currentRow;
	}
	
	/**
	 * Beräknar vilken kolumn användaren har tryckt på/hovrat mha koordinater.
	 * @param hover - mushanteringseventet
	 * @return numret på raden i ordningen.
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
		
		//Första gången fönstret laddas vill vi att det ska laddas med aktuell månad och år.
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
			
			//Om vi befinner oss i december skall även året laddas om.
			if (currentMonth == 11){
				currentMonth = 0;
				currentYear += 1;
			}
			else{
				currentMonth += 1;
			}
			
			//Räknar om parametrar och ritar ut kalenderenheten baserade på dessa.
			calculateDateParameters(currentMonth, currentYear);
		}
		else if(arg0.getSource() == prevBtn){
			
			//Vill backa ett år om vi befinner oss i januari.
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
	
	//Om man klickar på ett datum.
	@Override
	public void mouseClicked(MouseEvent e) {
		
		//Räknar ut vilken rad resp. column i kalenden man klickat på.
		int clickedRow = setCurrentRow(e);
		int clickedColumn = setCurrentColumn(e);
		
		//Sätter värden för 
		calendar.setClickedRow(clickedRow);
		calendar.setClickedColumn(clickedColumn);
		
		calendar.repaint();
		
		//Hämtar datumet som man har klickat på och skriver ut information baserat på det.
		int date = calendar.getClickedDate();
		viewEvent(date);
	}
	
	//Triggas när man för markören över kalendern.
	@Override
	public void mouseEntered(MouseEvent e) {
		
		
		//Beräknar och sätter aktuell rad/kolumn innan kalendern ritas ut på nytt.
		int enteredRow = setCurrentRow(e);
		int enteredColumn = setCurrentColumn(e);
		
		calendar.setCurrentRow(enteredRow);
		calendar.setCurrentColumn(enteredColumn);
		
		calendar.repaint();
	}
	
	//Om markören förflyttas utanför fönstret nollställs aktuell markering
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
		
		//Beräknar och sätter aktuell rad/kolumn innan kalendern ritas ut på nytt.
		int enteredRow = setCurrentRow(e);
		int enteredColumn = setCurrentColumn(e);
		
		calendar.setCurrentRow(enteredRow);
		calendar.setCurrentColumn(enteredColumn);
		
		calendar.repaint();
		
	}
	
	/*Privata klasser----------------------------------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Klassen för själva kalenderkomponenten. 
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
		 * Kostrunktor som sätter standardvärden för komponenten.
		 */
		public CalendarComponent() {
		
			paddingCalendar = 10;
			paddingCell = 2;
			headerFont = new Font("Sans-Serif", Font.BOLD, 19);
			subHeaderFont = new Font("Sans-Serif", Font.BOLD, 17);
			daysFont = new Font("Sans-Serif", Font.PLAIN, 16);
			calendarColor = new Color(202,202,175);
			
			//Beräkningar av font metrics för att avgöra textstorlek.
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
		 * Retunerar loakalt origo för rutnätssystemt.
		 * @return punkten hos lokalt origo.
		 */
		public Point getLocalOrigo(){
			Point p = new Point();
			p.x = paddingCalendar;
			p.y = paddingCalendar+headerHeight+subHeaderHeight;
			return p;
		}
		
		/**
		 * Ger storleken hos klaenderns celler, som är kvadratiska.
		 * @return Höjd/bredd hos en cell.
		 */
		public int getCalenderSquareHeight(){
			return daysFontHeight;
		}
		
		/**
		 * Funktion för att hämta kalenderfärg.
		 * @return kalenderns färg.
		 */
		public Color getCalendarColor(){
			return calendarColor;
		}
		
		
		/**
		 * Sätter kanlendern färg.
		 * @param c - nya färgen.
		 */
		public void setCalendarColor(Color c){
			calendarColor = c;
		}
		
		
		/**
		 * Sätter aktuell rad som användaren hovrat över.
		 * @param row - index för raden.
		 */
		public void setCurrentRow(int row){
			currentRow = row;
		}
		
		/**
		 * Sätter aktuell kolumn som användaren hovrat över.
		 * @param column - index för kolumnen.
		 */
		public void setCurrentColumn(int column){
			currentColumn = column;
		}
		
		/**
		 * Sätter aktuell rad som användaren har klickat på.
		 * @param row - index för raden.
		 */
		public void setClickedRow(int row){
			clickedRow = row;
		}
		
		/**
		 * Sätter aktuell kolumn som användaren har klickat på.
		 * @param column - index för kolumnen.
		 */
		public void setClickedColumn(int column){
			clickedColumn = column;
		}
		
		/**
		 * Sätter kalenderns rubrik baserat på aktuellt datum/år.
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
		 * Bestämmer vilket datum som användaren har klickat på.
		 * @return datumet i månaden som användaren klickat på.
		 */
		public int getClickedDate(){
			for(int i=1; i<=numberOfDays; i++){
				
				//Beräknar klassvariabler för rad  resp. koumn i kalendeen mha avrundning.
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
			
			//Ritar ut rubrik för månad och år.
			g.setFont(headerFont);
			g.drawString(getHeader(), paddingCalendar, headerHeight);
			
			//Ritar ut rubrik för bokstäver som representerar dagar
			g.setFont(subHeaderFont);
			g.drawString(monthHeader, paddingCalendar, headerHeight+subHeaderHeight);
			
			//Ritar ut kalenderns rutnät och datum.
			g.setFont(daysFont);
			for(int i=0; i<7; i++){
				for(int j=0; j<6; j++){
					g.setColor(Color.WHITE);
					g.fillRect(i*daysFontHeight+paddingCalendar,j*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
					g.setColor(Color.BLACK);
					g.drawRect(i*daysFontHeight+paddingCalendar,j*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
			}
			
			//Ritar ut aktuella månadens datum baserat på aktuella variabler.
			for(int k=1; k<=numberOfDays; k++){
				
				//Aktuell rad och kolumn fås genom att använda restprodukter vid division.
				int row = new Integer((k+startOfMonth-2)/7);
				int column = (k+startOfMonth-2)%7;
				
		
				//Om ett event finns inlaggt på aktuellt datum markeras detta med cyan
				for(int l=0; l<events.size(); l++){
					if(events.get(l).getYear() == currentYear && events.get(l).getMonth() == (currentMonth+1) && events.get(l).getDay() == k){
						g.setColor(Color.CYAN);
						g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
					}
				}
				
				
				//Om dagens datum finns representerat i månaden markeras det med blått.
				if(currentYear == realYear && currentMonth == realMonth && realDay == k){
					g.setColor(Color.BLUE);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Om man för markören över ett datum ska det markeras med gult
				if(currentRow-1 == row && currentColumn-1 == column){
					g.setColor(Color.YELLOW);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Om man har klickat på ett datum markeras det och meddelandekomponenten ritas ut. Datumet sparas i en variabel.
				if(clickedRow-1 == row && clickedColumn-1 == column){
					//clickedDate = k;
					g.setColor(Color.YELLOW);
					g.fillRect(column*daysFontHeight+paddingCalendar, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight, daysFontHeight, daysFontHeight);
				}
				
				//Själva siffrorna som representerar datum ritas ut.
				g.setColor(Color.BLACK);
				String date = Integer.toString(k);
				g.drawString(date, column*daysFontHeight+paddingCalendar+paddingCell, row*daysFontHeight+paddingCalendar+headerHeight+subHeaderHeight+daysFontHeight-paddingCell);
			}
			
		}
		
	}
	
	
}
