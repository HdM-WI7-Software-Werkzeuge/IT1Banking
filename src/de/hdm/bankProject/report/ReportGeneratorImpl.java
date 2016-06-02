package de.hdm.bankProject.report;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

import de.hdm.bankProject.BankAdministration;
import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Bank;
import de.hdm.bankProject.data.Customer;

/**
 * Implementierung des <code>ReportGenerator</code>-Interface.
 * 
 * @see ReportGenerator
 * @author thies
 */
public class ReportGeneratorImpl implements ReportGenerator {

	/*
	 * Ein ReportGenerator ben�tigt Zugriff auf die BankVerwaltung, da diese die
	 * essentiellen Methoden f�r die Koexistenz von Datenobjekten (vgl.
	 * data-Package) bietet.
	 */
	private BankAdministration verwaltung = null;

	/**
	 * Jeder ReportGenerator ben�tigt Zugriff auf die BankVerwaltung. Deren
	 * Angabe wird bereits bei Instantiierung des Generators durch diesen
	 * Konstruktor erzwungen.
	 * 
	 * @param bv
	 *            die BankVerwaltung
	 * @throws RemoteException
	 * @see BankVerwaltung
	 */
	public ReportGeneratorImpl(BankAdministration bv) {
		this.verwaltung = bv;
	}

	/**
	 * Auslesen der zugeh�rigen BankVerwaltung (interner Gebrauch).
	 * 
	 * @return das BankVerwaltungsobjekt
	 */
	protected BankAdministration getBankVerwaltung() {
		return this.verwaltung;
	}

	/**
	 * Hinzuf�gen des Report-Impressums. Diese Methode ist aus den
	 * <code>create...</code>-Methoden ausgegliedert, da jede dieser Methoden
	 * diese T�tigkeiten redundant auszuf�hren h�tte. Stattdessen rufen die
	 * <code>create...</code>-Methoden diese Methode auf.
	 * 
	 * @param r
	 *            der um das Impressum zu erweiternde Report.
	 */
	protected void addImprint(Report r) {
		/*
		 * Das Impressum soll wesentliche Informationen �ber die Bank enthalten.
		 */
		Bank bank = this.verwaltung.getBank();

		/*
		 * Das Imressum soll mehrzeilig sein.
		 */
		CompositeParagraph imprint = new CompositeParagraph();

		imprint.addSubParagraph(new SimpleParagraph(bank.getName()));
		imprint.addSubParagraph(new SimpleParagraph(bank.getStreet()));
		imprint.addSubParagraph(new SimpleParagraph(bank.getZip() + " " + bank.getCity()));

		// Das eigentliche Hinzuf�gen des Impressums zum Report.
		r.setImprint(imprint);

	}

	/**
	 * Erstellen von <code>AllAccountsOfCustomerReport</code>-Objekten.
	 * 
	 * @param c
	 *            das Kundenobjekt bzgl. dessen der Report erstellt werden soll.
	 * @return der fertige Report
	 */
	public AllAccountsOfCustomerReport createAllAccountsOfCustomerReport(Customer c) {

		/*
		 * Zun�chst legen wir uns einen leeren Report an.
		 */
		AllAccountsOfCustomerReport result = new AllAccountsOfCustomerReport();

		// Jeder Report hat einen Titel (Bezeichnung / �berschrift).
		result.setTitle("Alle Konten des Kunden");

		// Imressum hinzuf�gen
		this.addImprint(result);

		/*
		 * Datum der Erstellung hinzuf�gen. new Date() erzeugt autom. einen
		 * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
		 */
		result.setCreated(new Date());

		/*
		 * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die
		 * oben auf dem Report stehen) des Reports. Die Kopfdaten sind
		 * mehrzeilig, daher die Verwendung von CompositeParagraph.
		 */
		CompositeParagraph header = new CompositeParagraph();

		// Name und Vorname des Kunden aufnehmen
		header.addSubParagraph(new SimpleParagraph(c.getLastName() + ", " + c.getFirstName()));
		// Kundennummer aufnehmen
		header.addSubParagraph(new SimpleParagraph("Kd.-Nr.: " + c.getId()));

		// Hinzuf�gen der zusammengestellten Kopfdaten zu dem Report
		result.setHeaderData(header);

		/*
		 * Ab hier erfolgt ein zeilenweises Hinzuf�gen von Konto-Informationen.
		 */
		/*
		 * Zun�chst legen wir eine Kopfzeile f�r die Konto-Tabelle an.
		 */
		Row headline = new Row();

		/*
		 * Wir wollen Zeilen mit 2 Spalten in der Tabelle erzeugen. In die erste
		 * Spalte schreiben wir die jeweilige Kontonummer und in die zweite den
		 * aktuellen Kontostand. In der Kopfzeile legen wir also entsprechende
		 * �berschriften ab.
		 */
		headline.addColumn(new Column("Kto.-Nr."));
		headline.addColumn(new Column("Kto.-Stand"));

		// Hinzuf�gen der Kopfzeile
		result.addRow(headline);

		/*
		 * Nun werden s�mtliche Konten des Kunden ausgelesen und deren Kto.-Nr.
		 * und Kontostand sukzessive in die Tabelle eingetragen.
		 */
		Vector<Account> accounts = this.verwaltung.getAccountsOf(c);

		for (int i = 0; i < accounts.size(); i++) {
			/*
			 * Ein Vector enth�lt Objects. Wir m�ssen diese jedoch wieder auf
			 * einen brauchbaren Typ casten.
			 */
			Account a = (Account) accounts.elementAt(i);

			// Eine leere Zeile anlegen.
			Row accountRow = new Row();

			// Erste Spalte: Kontonummer hinzuf�gen
			accountRow.addColumn(new Column(String.valueOf(a.getId())));

			// Zweite Spalte: Kontostand hinzuf�gen
			accountRow.addColumn(new Column(String.valueOf(a.getBalance())));

			// und schlie�lich die Zeile dem Report hinzuf�gen.
			result.addRow(accountRow);
		}

		/*
		 * Zum Schlu� m�ssen wir noch den fertigen Report zur�ckgeben.
		 */
		return result;
	}

	/**
	 * Erstellen von <code>AllAccountsOfAllCustomersReport</code>-Objekten.
	 * 
	 * @return der fertige Report
	 */
	public AllAccountsOfAllCustomersReport createAllAccountsOfAllCustomersReport() {

		/*
		 * Zun�chst legen wir uns einen leeren Report an.
		 */
		AllAccountsOfAllCustomersReport result = new AllAccountsOfAllCustomersReport();

		// Jeder Report hat einen Titel (Bezeichnung / �berschrift).
		result.setTitle("Alle Konten aller Kunden");

		// Imressum hinzuf�gen
		this.addImprint(result);

		/*
		 * Datum der Erstellung hinzuf�gen. new Date() erzeugt autom. einen
		 * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
		 */
		result.setCreated(new Date());

		/*
		 * Da AllAccountsOfAllCustomersReport-Objekte aus einer Sammlung von
		 * AllAccountsOfCustomerReport-Objekten besteht, ben�tigen wir keine
		 * Kopfdaten f�r diesen Report-Typ. Wir geben einfach keine Kopfdaten
		 * an...
		 */

		/*
		 * Nun m�ssen s�mtliche Kunden-Objekte ausgelesen werden. Anschlie�end
		 * wir f�r jedes Kundenobjekt c ein Aufruf von
		 * createAllAccountsOfCustomerReport(c) durchgef�hrt und somit jeweils
		 * ein AllAccountsOfCustomerReport-Objekt erzeugt. Diese Objekte werden
		 * sukzessive der result-Variable hinzugef�gt. Sie ist vom Typ
		 * AllAccountsOfAllCustomersReport, welches eine Subklasse von
		 * CompositeReport ist.
		 */
		Vector<Customer> allCustomers = this.verwaltung.getAllCustomers();

		for (int i = 0; i < allCustomers.size(); i++) {
			Customer c = (Customer) allCustomers.elementAt(i);

			/*
			 * Anlegen des jew. Teil-Reports und Hinzuf�gen zum Gesamt-Report.
			 */
			result.addSubReport(this.createAllAccountsOfCustomerReport(c));
		}

		/*
		 * Zu guter Letzt m�ssen wir noch den fertigen Report zur�ckgeben.
		 */
		return result;
	}

}
