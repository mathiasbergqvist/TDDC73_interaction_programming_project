package matbe371.swing.passwordstrengthmeeter;

//Importerar paketet regex f�r hantering av reguli�ra uttryck och str�ngmatchningar.
import java.util.regex.*;

public class PasswordValidator{
	
	
	//M�nster och matchare.
	private Pattern pattern;
	private Matcher matcher;
	
	//Matchningsstr�ngar f�r de olika m�nstren.
	private static final String stong_pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&?]).{5,20})";
	private static final String good_pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])).{5,20}";
	private static final String fair_pattern_1 = "((?=.*\\d)(?=.*[A-Z])).{5,20}";
	private static final String fair_pattern_2 = "((?=.*\\d)(?=.*[a-z])).{5,20}";
	
	/**
	 * Klassvariabel instans anv�nds f�r att implementera desingm�nstret Singleton, 
	 * som kan anv�ndas eftersom klassen endast kommer att anv�ndas av ett objekt.
	 */
	private static final PasswordValidator instance = new PasswordValidator();  
	
	private PasswordValidator(){}
	
	//getInstanse retunerar instansvariablen, och kan d�rmed anv�ndas utifr�n f�r att komma �t klassmetoder.
	public static PasswordValidator getInstance() {
		return instance;
	}
	
	
	/**
	 * H�r sker l�senordets matchning mot aktuellt m�nster.
	 * @param pwd
	 * @param matchString
	 * @return true eller false beroende p� om m�nstret kunde matchas eller inte.
	 */
	private boolean match(String pwd, String matchString){
		pattern = Pattern.compile(matchString);
		matcher = pattern.matcher(pwd);
		return matcher.matches();
	}
	
	/**
	 * Vailderar l�senordet beroende p� hur det matchas mot olika s�kstr�ngar.
	 * @param pwd
	 * @return 
	 */
	public String vaidatePassword(String pwd, int minLength){

		if(pwd.length() < minLength){
			return "Too short";
		}
		else{
			if(match(pwd, stong_pattern)){
				return "Strong";
			}
			else if(match(pwd, good_pattern)){
				return "Good";
			}
			else if((match(pwd, fair_pattern_1))||(match(pwd, fair_pattern_2))){
				return "Fair";
			}
			else
				return "Weak";
		}

	}
}
