package de.hdm.bankProject;

import java.util.Vector;

import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;

public interface BankAdministration {

	/**
	 * Ein neues Konto fuer einen gegebenen Kunden eroeffnen.
	 *
	 * @param c
	 *            der Kunde
	 * @return fertiges Konto-Objekt Das Anlegen eines neuen Kontos fuer den
	 *         uebergebenen Kunden fuehrt implizit zu einem Speichern des neuen,
	 *         leeren Kontos in der Datenbank.
	 *         <p>
	 *
	 *         <b>HINWEIS:</b> Aenderungen an Account-Objekten muessen stets durch
	 *         Aufruf von <code>save(Account a)</code> in die Datenbank transferiert
	 *         werden.
	 *
	 * @see save(Account a)
	 * @see save(Customer c)
	 */
	Account createAccountFor(Customer c);

	/**
	 * Einen Kunden anlegen.
	 *
	 * @param first
	 *            Vorname
	 * @param last
	 *            Nachname
	 * @return Ein fertiges Kunden-Objekt.
	 *
	 *         Das Anlegen eines neuen Kunden fuehrt implizit zu einem Speichern des
	 *         neuen Kunden in der Datenbank.
	 *         <p>
	 *
	 *         <b>HINWEIS:</b> Aenderungen an Customer-Objekten muessen stets durch
	 *         Aufruf von <code>save(Customer c)</code> in die Datenbank
	 *         transferiert werden.
	 *
	 * @see save(Customer c)
	 * @see save(Account a)
	 */
	Customer createCustomer(String first, String last);

	/**
	 * Ein Konto loeschen.
	 * 
	 * @param a
	 *            zu loeschendes Konto
	 */
	void deleteAccount(Account a);

	/**
	 * Einen Kunden loeschen.
	 * 
	 * @param c
	 *            zu loeschender Kunde
	 */
	void deleteCustomer(Customer c);

	/**
	 * Konten eines Kunden auslesen.
	 * 
	 * @param Kundenobjekt
	 * @return Vector-Objekt mit Account-Objekten bzgl. des Kunden.
	 */
	Vector<Account> getAccountsOf(Customer c);

	/**
	 * Suchen von Customer-Objekten, von denen der Zuname bekannt ist.
	 * 
	 * @param lastName
	 *            ist der Nachname.
	 * @return Alle Customer-Objekte, die die Suchkriterien erfuellen.
	 */
	Vector<Customer> getCustomerByName(String lastName);

	/**
	 * Suchen eines Customer-Objekts, dessen Kundennummer bekannt ist.
	 * 
	 * @param id
	 *            ist die Kundennummer.
	 * @return Das erste Customer-Objekt, dass den Suchkriterien entspricht.
	 */
	Customer getCustomerById(int id);

	/**
	 * Saemtliche Kunden der Bank auslesen.
	 * 
	 * @return Vector saemtlicher Kunden
	 */
	Vector<Customer> getAllCustomers();

	/**
	 * Saemtliche Konten der Bank auslesen.
	 * 
	 * @return Vector saemtlicher Konten
	 * @author Thies
	 */
	Vector<Account> getAllAccounts();

	/**
	 * Der Kontostand wird um den angegebenen Betrag veraendert.
	 * 
	 * @param a
	 * @param amount
	 * @return der neue Betrag als float
	 */
	float modifyAccount(Account a, float amount);

	/**
	 * Vor- und Nachname eines Kunden werden aktualisiert.
	 * 
	 * @param c
	 * @param vorname
	 * @param nachname
	 * @return das modifizierte Kundenobjekt.
	 */
	Customer modifyCustomer(Customer c, String vorname, String nachname);

}