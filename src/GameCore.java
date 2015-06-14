/*
 * 
 */
import java.util.Arrays;



/**
 * Klasa odpowiedzialna za przebieg gry, lista graczy, numer rundy, punkty, gotowoœæ graczy.
 * 
 * @author Pablo
 */
public class GameCore {
	
	/** Tablica nickow graczy. */
	private String[] gracze = new String[10];
	
	/** Tablica punktow. */
	private int[] punkty = new int[10];
	
	/** Tablica pokazujaca ktorzy gracze juz udzielili odpowiedzi. */
	private boolean[] odpowiedzi_runda = new boolean[10];
	
	/** Tablica pokazujaca ktorzy gracze sa juz gotowi na nastepna runde. */
	private boolean[] gotowosc = new boolean[10];
	
	/** Numer rundy. */
	private int runda;
	
	/** Liczba graczy w grze. */
	private int liczba_graczy;
	
	
	/**
	 * Konstruktor klasy GameCore, tworzy now¹ grê.
	 */
	public GameCore() {
		//super();
		this.liczba_graczy = 0;
		this.runda = 0;
		for (int i = 0; i<10; i++) {
			odpowiedzi_runda[i]=true;
			if (i>=2) gotowosc[i]=true;
		}
		
		
	}
	
	/**
	 * Dodaje nowego gracza do gry.
	 *
	 * @param nick Nick gracza
	 * @return int Identyfikator nowego gracza
	 */
	public int nowyGracz(String nick){
		
	this.gracze[liczba_graczy] = nick;
	this.punkty[liczba_graczy] = 0;
	this.odpowiedzi_runda[liczba_graczy] = false;
	this.gotowosc[liczba_graczy] = false;
		this.liczba_graczy++;
	return liczba_graczy-1;
		
	}
	
/**
 * Sprawdz gotowosc graczy.
 *
 * @return true jesli gracze gotowi
 */
public boolean sprawdzGotowosc(){
		
		boolean test = true;
		for (int i = 0; i<liczba_graczy; i++){
			if (gotowosc[i]==false) test = false;
		}
		return test;
	}

	/**
	 * Zglasza gotowosc gracza.
	 *
	 * @param idGracza identifikator gracza
	 */
	public void graczGotowy(int idGracza){
		this.gotowosc[idGracza]=true;
	}
	
	/**
	 * Zmienia gotowsc graczy na false.
	 */
	public void zakonczGotowosc(){
		for (int i = 0; i<liczba_graczy; i++){
			gotowosc[i]=false;
		}
	}
	
	/**
	 * Sprawdza czy wszyscy gracze udzielili odpowiedzi.
	 *
	 * @return true jesli wszyscy odpowiedzieli
	 */
	public boolean sprawdzRunde(){
		
		boolean test = true;
		for (int i = 0; i<liczba_graczy; i++){
			if (odpowiedzi_runda[i]==false) test = false;
		}
		return test;
	}

	/**
	 * Konczy runde.
	 *
	 * @return true jeœli zakoñczono
	 */
	public boolean zakonczRunde(){
		boolean runda_koniec = true;
		if (this.sprawdzRunde()){
			for (int i = 0; i<liczba_graczy; i++) {
			odpowiedzi_runda[i]=false;
			this.runda++;
			//zakonczGotowosc();
			
			}
		} else runda_koniec = false;
		return runda_koniec;
	}
	
	/**
	 * Sprawdza odpowiedz.
	 *
	 * @param poprawna Poprawna odpowiedz
	 * @param testowana Testowana odpowiedz
	 * @return true, jeœli poprawna
	 */
	public boolean sprawdzOdpowiedz(String poprawna, String testowana){
		if (poprawna==testowana){
			return true;
		} else return false;
	}
	
	/**
	 * Zglasza odpowiedz gracza.
	 *
	 * @param gracz Id gracza
	 * @param poprawna Poprawna odpowiedz
	 * @param testowana Testowana odpowiedz
	 */
	public void odpowiedz(int gracz, String poprawna, String testowana){
		this.odpowiedzi_runda[gracz]=true;
		System.out.println("poprawna " + poprawna);
		System.out.println("testowana " + testowana);
		if ( poprawna.equals(testowana)) punkty[gracz]++;

	}
	
	/**
	 * Zwraca Punkty.
	 *
	 * @param IdGracza id gracza
	 * @return int Punkty gracza
	 */
	public int punkty(int IdGracza){
		return this.punkty[IdGracza];
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GameCore [gracze=" + Arrays.toString(gracze) + ", punkty="
				+ Arrays.toString(punkty) + ", odpowiedzi_runda="
				+ Arrays.toString(odpowiedzi_runda) + ", gotowosc="
				+ Arrays.toString(gotowosc) + ", runda=" + runda
				+ ", liczba_graczy=" + liczba_graczy + "]";
	}
	
	



	
	
}
