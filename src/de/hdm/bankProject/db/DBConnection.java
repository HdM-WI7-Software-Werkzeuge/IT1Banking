package de.hdm.bankProject.db;

import java.sql.*;

/**
 * Verwalten einer Verbindung zur Datenbank.
 * <p>
 * <b>Vorteil:</b> Sehr einfacher Verbindungsaufbau zur Datenbank.
 * <p>
 * <b>Nachteil:</b> Durch die Singleton-Eigenschaft der Klasse kann nur auf eine
 * fest vorgegebene Datenbank zugegriffen werden.
 * <p>
 * In der Praxis kommen die meisten Anwendungen mit einer einzigen Datenbank
 * aus. Eine flexiblere Variante fuer mehrere gleichzeitige
 * Datenbank-Verbindungen waere sicher leistungsfaehiger. Dies wuerde allerdings
 * den Rahmen dieses Projekts sprengen bzw. die Software unnoetig
 * verkomplizieren, da dies fuer diesen Anwendungsfall nicht erforderlich ist.
 * 
 * @author Thies
 */
public class DBConnection {

	/**
	 * Die Klasse DBConnection wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * fuer saemtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 *
	 * @see AccountMapper.accountMapper()
	 * @see CustomerMapper.customerMapper()
	 */
	private static Connection con = null;
	
	/**
	 * Der Name des Datenbank-Servers in form eines Hostnames.
	 */
	private static String dbHost = 
			//"localhost";
			//"ceresvm.iuk.hdm-stuttgart.de:3306";
			"db4free.net";
	/**
	 * Der Name der Datenbank. Diese Datenbank wird entweder in Derby mit Hilfe von
	 * Dateien im Dateisystem realisiert. Dann ist der Name der Datenbank ist gleichzeit
	 * der Name des Verzeichnisses, in dem sich die Datenbankdateien befinden. Alternativ
	 * kann die Datenbank als MySQL-Datenbank realisiert werden.
	 */
	private static String dbName = "it1banking";
	/**
	 * Die URL, mit deren Hilfe die Datenbank angesprochen wird.
	 */
	private static String connectionUrl = 
			"jdbc:mysql://" + dbHost + "/" + dbName + "?user=demodemo&password=demodemo";
			//"jdbc:derby:" + dbName + ";create=true";

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>DBConnection.connection()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafuer sorgt, dass nur eine
	 * einzige Instanz von <code>DBConnection</code> existiert.
	 * <p>
	 *
	 * <b>Fazit:</b> DBConnection sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen
	 * Methode.
	 * <p>
	 *
	 * <b>Nachteil:</b> Bei Zusammenbruch der Verbindung zur Datenbank - dies
	 * kann z.B. durch ein unbeabsichtigtes Herunterfahren der Datenbank
	 * ausgeloest werden - wird keine neue Verbindung aufgebaut, so dass die in
	 * einem solchen Fall die gesamte Software neu zu starten ist. In einer
	 * robusten Loesung wuerde man hier die Klasse dahingehend modifizieren,
	 * dass bei einer nicht mehr funktionsfuehigen Verbindung stets versucht
	 * wuerde eine neue Verbindung aufzubauen. Dies wuerde allerdings ebenfalls
	 * den Rahmen dieses Projekts sprengen.
	 *
	 * @return DAS <code>DBConncetion</code>-Objekt.
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @see con
	 */
	public static Connection connection()  {
		// Wenn es bisher keine Conncetion zur DB gab, ...
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				/*
				 * Der DriverManager nimmt eine Verbindung mit den
				 * oben in der Variable url angegebenen Verbindungsinformationen
				 * auf.
				 *
				 * Diese Verbindung wird in der statischen Variable con
				 * abgespeichert und fortan verwendet.
				 */
				con = DriverManager.getConnection(connectionUrl);
			} catch (Exception e1) {
				con = null;
				e1.printStackTrace();
			}
		}

		// Zurueckgegeben der Verbindung
		return con;
	}
}
