package matbe371.swing.passwordstrengthmeeter;

//Importerar paketet regex för hantering av reguliära uttryck och strängmatchningar.
import java.util.regex.*;

public class PasswordValidator{
	
	
	//Mönster och matchare.
	private Pattern pattern;
	private Matcher matcher;
	
	//Matchningssträngar för de olika mönstren.
	private static final String stong_pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&?]).{5,20})";
	private static final String good_pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])).{5,20}";
	private static final String fair_pattern_1 = "((?=.*\\d)(?=.*[A-Z])).{5,20}";
	private static final String fair_pattern_2 = "((?=.*\\d)(?=.*[a-z])).{5,20}";
	
	/**
	 * Klassvariabel instans används för att implementera desingmönstret Singleton, 
	 * som kan användas eftersom klassen endast kommer att användas av ett objekt.
	 */
	private static final PasswordValidator instance = new PasswordValidator();  
	
	private PasswordValidator(){}
	
	//getInstanse retunerar instansvariablen, och kan därmed användas utifrån för att komma åt klassmetoder.
	public static PasswordValidator getInstance() {
		return instance;
	}
	
	
	/**
	 * Här sker lösenordets matchning mot aktuellt mönster.
	 * @param pwd
	 * @param matchString
	 * @return true eller false beroende på om mönstret kunde matchas eller inte.
	 */
	private boolean match(String pwd, String matchString){
		pattern = Pattern.compile(matchString);
		matcher = pattern.matcher(pwd);
		return matcher.matches();
	}
	
	/**
	 * Vailderar lösenordet beroende på hur det matchas mot olika söksträngar.
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
