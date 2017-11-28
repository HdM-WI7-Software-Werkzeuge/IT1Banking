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
	 * Die Klasse DBConnection wird nur einmal instantiiert. Man spricht hierbei von
	 * einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal fuer
	 * saemtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 *
	 * @see AccountMapper.accountMapper()
	 * @see CustomerMapper.customerMapper()
	 */
	private static Connection con = null;

	private static String connectionType = //"mySQL";
	// "javaDB";
	"sqlServer";

	/**
	 * Die URL, mit deren Hilfe die Datenbank angesprochen wird.
	 */
	private static String connectionUrl = getConnectionUrl();

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>DBConnection.connection()</code>. Sie stellt die Singleton-Eigenschaft
	 * sicher, indem Sie dafuer sorgt, dass nur eine einzige Instanz von
	 * <code>DBConnection</code> existiert.
	 * <p>
	 *
	 * <b>Fazit:</b> DBConnection sollte nicht mittels <code>new</code> instantiiert
	 * werden, sondern stets durch Aufruf dieser statischen Methode.
	 * <p>
	 *
	 * <b>Nachteil:</b> Bei Zusammenbruch der Verbindung zur Datenbank - dies kann
	 * z.B. durch ein unbeabsichtigtes Herunterfahren der Datenbank ausgeloest
	 * werden - wird keine neue Verbindung aufgebaut, so dass die in einem solchen
	 * Fall die gesamte Software neu zu starten ist. In einer robusten Loesung
	 * wuerde man hier die Klasse dahingehend modifizieren, dass bei einer nicht
	 * mehr funktionsfuehigen Verbindung stets versucht wuerde eine neue Verbindung
	 * aufzubauen. Dies wuerde allerdings ebenfalls den Rahmen dieses Projekts
	 * sprengen.
	 *
	 * @return DAS <code>DBConncetion</code>-Objekt.
	 * @throws Exception
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @see con
	 */
	public static Connection connection() {
		// Wenn es bisher keine Conncetion zur DB gab, ...
		if (con == null) {
			try {
				/*
				 * Falls die Driver-Klasse nicht vor dieser Klasse geladen wird,
				 * ist der entsprechende Treiber noch nicht registriert.
				 * Hiermit wird das Laden erzwungen, was zur Registrierung einer
				 * Instanz beim DriverManager führt.
				 */
				switch (connectionType) {
				case "mySQL":
					Class.forName("com.mysql.jdbc.Driver");
					break;
				case "javaDB":
					Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
					break;
				case "sqlServer":
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					break;
				}
				
				/*
				 * Der DriverManager nimmt eine Verbindung mit den oben in der Variable url
				 * angegebenen Verbindungsinformationen auf.
				 *
				 * Diese Verbindung wird in der statischen Variable con abgespeichert und fortan
				 * verwendet.
				 */
				con = DriverManager.getConnection(connectionUrl);
			} catch (Exception e1) {
				con = null;
				e1.printStackTrace();
			}
		}

		// Zurueckgeben der Verbindung
		return con;
	}

	private static String getConnectionUrl() {
		switch (connectionType) {
		case "mySQL":
			return "jdbc:mysql://localhost/it1banking?user=demodemo&password=demodemo&useSSL=false";
		// connectionUrl =
		// "jdbc:mysql://db4free.net/it1banking?user=demodemo&password=demodemo&useSSL=false";
		// connectionUrl =
		// "jdbc:mysql://ceresvm.iuk.hmd-stuttgart.de/it1banking?user=demodemo&password=demodemo&useSSL=false";
		case "javaDB":
			return "jdbc:derby:it1banking;create=true";
		case "sqlServer":
			return "jdbc:sqlserver://edu.hdm-server.eu;user=demodemo;password=demodemo";
		}
		return "";
	}
}
