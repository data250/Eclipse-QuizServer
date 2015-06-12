
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class QuizServer extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//GUI
    //Serwer
    private int numerPortu = 2345;
    private boolean uruchomiony = false;
    private Vector<Polaczenie> klienci = new Vector<Polaczenie>();
    private Serwer srw;
    public QuizServer() {
        super("Serwer");
        srw = new Serwer();
        uruchomiony = true;
        srw.start();
        
        // srw.kill();
        
    }

   

    private class Serwer extends Thread implements CzatProtokol {


		private ServerSocket server;

        public void kill() {
            try {
                server.close();
                for (Polaczenie klient : klienci) {
                    try {
                        klient.wyjscie.println(LOGOUT_COMMAND + "Serwer przestał działać!");
                        klient.socket.close();
                    } catch (IOException e) {
                    }
                }
                wyswietlKomunikat("Wszystkie Połączenia zostały zakończone.\n");
            } catch (IOException e) {
            }
        }

        public void run() {

            try {

                server = new ServerSocket(new Integer(numerPortu));
                wyswietlKomunikat("Serwer uruchomiony na porcie: " + numerPortu + "\n");

                while (uruchomiony) {
                    Socket socket = server.accept();
                    wyswietlKomunikat("Nowe połączenie.\n");
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

    private class Polaczenie extends Thread implements CzatProtokol {

        private BufferedReader wejscie;
        private PrintWriter wyjscie;
        private Socket socket;
        private String nick;
        private boolean polaczony;

        public Polaczenie(Socket w) {
            socket = w;
            polaczony = true;

            synchronized (klienci) {
                klienci.add(this);
            }
        }

        public void run() {

            try {
                wejscie = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                wyjscie = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                while (uruchomiony && polaczony) {

                    String lancuch = wejscie.readLine();

                    if (lancuch.startsWith(POST_COMMAND)) {
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(POST_COMMAND + "<" + nick + "> " + lancuch.substring(POST_COMMAND.length()));
                        }
                    }
                    if (lancuch.startsWith(NICK_COMMAND)) {
                        //sprawdzenie poprawności nicka

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
                            wyjscie.println(LOGIN_COMMAND + "Taki nick już jest na czacie! Podaj inny.");
                            continue;
                        }

                        StringBuilder lista = new StringBuilder();

                        for (Polaczenie klient : klienci) {
                            lista.append(klient.nick + ",");
                            klient.wyjscie.println(POST_COMMAND + "Użytkownik " + nick + " dołączył do czatu");
                        }
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(NICKLIST_COMMAND + lista.toString());
                        }
                    }
                    if (lancuch.startsWith(LOGIN_COMMAND)) {
                        wyjscie.println(LOGIN_COMMAND + "Witaj na serwerze!\n");
                    }
                    if (lancuch.startsWith(LOGOUT_COMMAND)) {

                        wyjscie.println(LOGOUT_COMMAND + "Żegnaj.\n");

                        synchronized (klienci) {
                            klienci.remove(this);
                        }
                        StringBuilder lista = new StringBuilder();

                        for (Polaczenie klient : klienci) {
                            lista.append(klient.nick + ",");
                            klient.wyjscie.println(POST_COMMAND + "Użytkownik " + nick + " opuścił czat.");
                        }
                        for (Polaczenie klient : klienci) {
                            klient.wyjscie.println(NICKLIST_COMMAND + lista.toString());
                        }
                        polaczony = false;

                    }

                }
                wyswietlKomunikat("Połączenie zostało zakończone.\n");
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

        public boolean equals(Polaczenie p) {
            return (p.nick.equals(nick));
        }
    }

    private void wyswietlKomunikat(String tekst) {
    	System.out.println(tekst);
    }

    public static void main(String[] args) {

        new QuizServer();
    }
}