// TODO: Auto-generated Javadoc
/**
 * Klasa zawieraj¹ca zbior pytan. 
 * @author Pablo
 *
 */
public class Pytania {
	
	/** The pytanie. */
	String pytanie;
	
	/** The var a. */
	String varA;
	
	/** The var b. */
	String varB;
	
	/** The var c. */
	String varC;
	
	/** The var d. */
	String varD;
	
	/** The numer pytania. */
	int numerPytania=0;
	
	
	/** Tresc pytania. */
	String[] pytanie_test = {"Jak nazywa siê stolica Niemiec?", "Jaki kolor liœci ma klon japoñski?", "Jak ma na imiê siê g³ówny bohater serialu Breaking Bad?"};
	
	/** Odpowiedzi A. */
	String[] varA_test	=	{"Berlin",							"zielone",								"Waldemar"};
	
	/** Odpowiedzi B. */
	String[] varB_test	=	{"Wiedeñ",							"czerwone",								"William"};
	
	/** Odpowiedzi C. */
	String[] varC_test	=	{"Warszawa",						"bia³e",								"Winston"};
	
	/** Odpowiedzi D. */
	String[] varD_test	=	{"Rzym",							"rude",									"Walter"};
	
	/** Odpowiedz poprawna. */
	String[] poprawna_test=	{"a",								"b",									"d"};

	
	/**
	 * Kolejne pytanie.
	 */
	public void kolejnePytanie(){
		numerPytania++;
		if (numerPytania==3) numerPytania=0;
	}
	
	/**
	 * Pytanie.
	 *
	 * @return string Tresc pytania
	 */
	public String pytanie(){
		return pytanie_test[numerPytania];
	}
	
	/**
	 * Odpowiedz A.
	 *
	 * @return string Tresc odpowiedzi A
	 */
	public String varA(){
		return varA_test[numerPytania];
	}
	
	/**
	 * Odpowiedz B.
	 *
	 * @return string Tresc odpowiedzi B
	 */
	public String varB(){
		return varB_test[numerPytania];
	}
	
	/**
	 * Odpowiedz C.
	 *
	 * @return string Tresc odpowiedzi C
	 */
	public String varC(){
		return varC_test[numerPytania];
	}
	
	/**
	 * Odpowiedz D.
	 *
	 * @return string Tresc odpowiedzi D
	 */
	public String varD(){
		return varD_test[numerPytania];
	}
	
	/**
	 * Poprawna odpowiedz.
	 *
	 * @return string Odpowiedz poprawna
	 */
	public String poprawna(){
		return poprawna_test[numerPytania];
	}
	
	

	
	
	
	
}
