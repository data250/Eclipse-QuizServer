
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;


/**
 * G³owna klasa odpowiedzialna za prace serwera.
 *
 * @author Pablo
 */
public class QuizServer extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** numer portu na ktorym pracuje serwer */
    private int numerPortu = 2345;
    
    /** Serwer uruchomiony. */
    private boolean uruchomiony = false;
    
    /** Klienci. */
    private Vector<Polaczenie> klienci = new Vector<Polaczenie>();
    
    /** Obiekt instancji serwera. */
    private Serwer srw;
    
    /**
     * Konstruktor uruchamiajacy serwer.
     */
    public QuizServer() {
        super("Serwer");
        srw = new Serwer();
        uruchomiony = true;
        srw.start();
        
        // srw.kill();
        
    }

    /** Obiekt klasy GameCore. */
    public GameCore gra = new GameCore();
    
    /** Obiekt klasy Pytania. */
    public Pytania baza =new Pytania();
    

    /**
     * Klasa serwera odpowiada za obsluge polaczen i prace serwera.
     */
    private class Serwer extends Thread implements GameProtokol {


		/** The server. */
		private ServerSocket server;
		  

        /**
         * Kill.
         */
        public void kill() {
            try {
                server.close();
                for (Polaczenie klient : klienci) {
                    try {
                        klient.wyjscie.println(LOGOUT_COMMAND + "Serwer przesta³ dzia³aæ!");
                        klient.socket.close();
                    } catch (IOException e) {
                    }
                }
                wyswietlKomunikat("Zakoñczono po³¹czenia.\n");
            } catch (IOException e) {
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {

            try {

                server = new ServerSocket(new Integer(numerPortu));
                wyswietlKomunikat("Serwer uruchomiony na porcie: " + numerPortu + "\n");
                
                while (uruchomiony) {
                    Socket socket = server.accept();
                    wyswietlKomunikat("Nowe polaczenie.\n");
                    new Polaczenie(socket).start();
                }
            } catch (SocketException e) {
            } catch (Exception e) {
                wyswietlKomunikat(e.toString());
            } finally {
                try {
                    if (server != null) {
                        server.close();
                    }
                } catch (IOException e) {
                    wyswietlKomunikat(e.toString());
                }
            }
            wyswietlKomunikat("Serwer zatrzymany.\n");
        }
    }

    /**
     * Klasa odpowiadajaca za Polaczenie.
     */
    private class Polaczenie extends Thread implements GameProtokol {

        /** Strumien wejscia. */
        private BufferedReader wejscie;
        
        /** Strumien wyjscia. */
        private PrintWriter wyjscie;
        
        /** Socket. */
        private Socket socket;
        
        /** Nick klienta. */
        private String nick;
        
        /** id gracza. */
        private int idGracza = 0;
        
        /** Czy polaczony. */
        private boolean polaczony;

        /**
         * Konstruktor tworzacy polaczenie.
         *
         * @param w Socket
         */
        public Polaczenie(Socket w) {
            socket = w;
            polaczony = true;

            synchronized (klienci) {
                klienci.add(this);
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        public void run() {

            try {
                wejscie = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                wyjscie = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                
                String testowana;
                
                while (uruchomiony && polaczony) {

                    String lancuch = wejscie.readLine();
                    wyswietlKomunikat("Czysty lancuch tekstu: " + lancuch);
                    if (lancuch.startsWith(POST_COMMAND)) {
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(POST_COMMAND + "<" + nick + "> " + lancuch.substring(POST_COMMAND.length()));
                        }
                    }
                    if (lancuch.startsWith(NICK_COMMAND)) {

                        nick = lancuch.substring(NICK_COMMAND.length()).trim();

                        if (nick.equals("null") || nick.trim().equals("")) {
                            wyjscie.println(LOGIN_COMMAND + "Podaj poprawny nick!");
                            continue;
                        }

                        boolean prawidlowy = true;

                        for (Polaczenie klient : klienci) {
                            if (klient.equals(this) && klient != this) {
                                prawidlowy = false;
                            }
                        }

                        if (!prawidlowy) {
                            wyjscie.println(LOGIN_COMMAND + "Nick zajety");
                            continue;
                        }

                        StringBuilder lista = new StringBuilder();

                        for (Polaczenie klient : klienci) {
                        	
                            lista.append(klient.nick + gra.punkty(idGracza) + ",");
                            klient.wyjscie.println(POST_COMMAND + "Gracz " + nick + " wchodzi do gry.");
                            
                        }
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(NICKLIST_COMMAND + lista.toString());
                        }
                        idGracza = gra.nowyGracz(nick) ;
                    }
                    if (lancuch.startsWith(LOGIN_COMMAND)) {
                        wyjscie.println(LOGIN_COMMAND + "Witaj na serwerze!\n");
                    }
                    
                    if (lancuch.startsWith(READY_COMMAND)) {
                    	
                    	
						wyswietlKomunikat("Gracz " + nick + " jest gotowy!");
						gra.graczGotowy(idGracza);
						if (gra.sprawdzGotowosc()){
							synchronized (klienci) {
							
								for (Polaczenie klient : klienci) {
									gra.zakonczGotowosc();
									
		                            klient.wyjscie.println(QUESTION_COMMAND + baza.pytanie());
									klient.wyjscie.println(VAR_A_COMMAND + baza.varA());
									klient.wyjscie.println(VAR_B_COMMAND + baza.varB());
									klient.wyjscie.println(VAR_C_COMMAND + baza.varC());
									klient.wyjscie.println(VAR_D_COMMAND + baza.varD()); 
									
								}
	                        }
						}
						
              
                    }                  
                    
                    if (lancuch.startsWith(ANSWER_COMMAND)) {
                    	testowana = lancuch.substring(ANSWER_COMMAND.length()).trim();
                    	System.out.println(testowana);
                    	gra.odpowiedz(idGracza, baza.poprawna() , testowana);
                    	synchronized (klienci) {
                    	if (gra.sprawdzRunde()){
                    		gra.zakonczRunde();
                    		baza.kolejnePytanie();
                    		
                    		for (Polaczenie klient : klienci) {
								gra.zakonczGotowosc();
	                            klient.wyjscie.println(NEXT_COMMAND );
							}

                    		StringBuilder lista = new StringBuilder();
                    		for (Polaczenie klient : klienci) {
                                lista.append(klient.nick + "(" + gra.punkty(klient.idGracza) + " PKT)" + ",");
                                klient.wyjscie.println(POST_COMMAND + "RUNDA ZAKONCZONA! Poprawna odpowiedŸ to: " +  baza.poprawna());

                            }
                            for (Polaczenie klient : klienci) {
                                klient.wyjscie.println(NICKLIST_COMMAND + lista.toString());
                            }
                    		
                    	}
                    	}
                    	
                    }
                    
                    if (lancuch.startsWith(LOGOUT_COMMAND)) {

                        wyjscie.println(LOGOUT_COMMAND + "¯egnaj.\n");

                        synchronized (klienci) {
                            klienci.remove(this);
                        }
                        StringBuilder lista = new StringBuilder();

                        for (Polaczenie klient : klienci) {
                            lista.append(klient.nick + ",");
                            klient.wyjscie.println(POST_COMMAND + "Gracz " + nick + " wyszedl z gry.");
                           //dezaktywuj gracza
                        }
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(NICKLIST_COMMAND + lista.toString());
                        }
                        polaczony = false;

                    }
                    wyswietlKomunikat(gra.toString());
                }
                wyswietlKomunikat("Po³¹czenie zakoñczone.\n");
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    wejscie.close();
                    wyjscie.close();
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        /**
         * Porownanie nickow.
         *
         * @param p Polaczenie
         * @return true, jesli rowne
         */
        public boolean equals(Polaczenie p) {
            return (p.nick.equals(nick));
        }
    }

    /**
     * Wyswietla komunikat.
     *
     * @param tekst Tresc
     */
    private void wyswietlKomunikat(String tekst) {
    	System.out.println(tekst);
    }

    /**
     * Metoda main.
     *
     * @param args 
     */
    public static void main(String[] args) {
        new QuizServer();
    }
}