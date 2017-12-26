package de.hdm.bankProject;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;

/**
 * 
 * 
 * @author Rathke
 */
public class BankAdministrationProxy implements BankAdministration {

	WebTarget webTarget = ClientBuilder.newClient().target("http://localhost:8080/IT1_Banking/bankservice");

	/**
	 * Konstruktor.
	 */
	public BankAdministrationProxy(String hostname) {
		webTarget = ClientBuilder.newClient().target("http://" + hostname + "/IT1_Banking/bankservice");
	}

	/**
	 * Ein neues Konto fuer einen gegebenen Kunden eroeffnen.
	 *
	 * @param c
	 *            der Kunde
	 * @return fertiges Konto-Objekt
	 */

	public Account createAccountFor(Customer c) {
		WebTarget createAccountTarget = webTarget.path("createAccountFor");
		Account a = createAccountTarget.request().post(Entity.entity(c, "application/json"), Account.class);
		return a;
	}

	/**
	 * Einen Kunden anlegen.
	 *
	 * @param first
	 *            Vorname
	 * @param last
	 *            Nachname
	 * @return Ein fertiges Kunden-Objekt.
	 */
	public Customer createCustomer(String first, String last) {
		WebTarget createCustomerTarget = webTarget.path("createCustomer");
		createCustomerTarget = createCustomerTarget.queryParam("first", first);
		createCustomerTarget = createCustomerTarget.queryParam("last", last);
		Customer c = createCustomerTarget.request().get(Customer.class);
		return c;
	}

	/**
	 * Ein Konto loeschen.
	 * 
	 * @param a
	 *            zu loeschendes Konto
	 */
	public void deleteAccount(Account a) {
		WebTarget deleteAccountTarget = webTarget.path("deleteAccount");
		deleteAccountTarget.request().post(Entity.entity(a, "application/json"));
	}

	/**
	 * Einen Kunden loeschen.
	 * 
	 * @param c
	 *            zu loeschender Kunde
	 */
	public void deleteCustomer(Customer c) {
		WebTarget deleteCustomerTarget = webTarget.path("deleteCustomer");
		deleteCustomerTarget.request().post(Entity.entity(c, "application/json"));
	}

	/**
	 * Konten eines Kunden auslesen.
	 * 
	 * @param Kundenobjekt
	 * @return Vector-Objekt mit Account-Objekten bzgl. des Kunden.
	 */
	public Vector<Account> getAccountsOf(Customer c) {
		WebTarget getAccountsTarget = webTarget.path("getAccountsOf");
		List<Account> accounts = getAccountsTarget.request().post(Entity.entity(c, "application/json"),
				new GenericType<List<Account>>() {
				});
		return new Vector<Account>(accounts);
	}

	/**
	 * Suchen von Customer-Objekten, von denen der Zuname bekannt ist.
	 * 
	 * @param lastName
	 *            ist der Nachname.
	 * @return Alle Customer-Objekte, die die Suchkriterien erfuellen.
	 */
	public Vector<Customer> getCustomerByName(String lastName) {
		WebTarget customerTarget = webTarget.path("customer").path(lastName);
		List<Customer> customers = customerTarget.request().get(new GenericType<List<Customer>>() {
		});
		return new Vector<Customer>(customers);
	}

	/**
	 * Suchen eines Customer-Objekts, dessen Kundennummer bekannt ist.
	 * 
	 * @param id
	 *            ist die Kundennummer.
	 * @return Das erste Customer-Objekt, dass den Suchkriterien entspricht.
	 */
	public Customer getCustomerById(int id) {
		WebTarget customerTarget = webTarget.path("customer").path(Integer.toString(id));
		return customerTarget.request().get(Customer.class);
	}

	/**
	 * Saemtliche Kunden der Bank auslesen.
	 * 
	 * @return Vector saemtlicher Kunden
	 */
	public Vector<Customer> getAllCustomers() {
		WebTarget customerTarget = webTarget.path("customers");
		List<Customer> customers = customerTarget.request().get(new GenericType<List<Customer>>() {
		});
		return new Vector<Customer>(customers);
	}

	/**
	 * Saemtliche Konten der Bank auslesen.
	 * 
	 * @return Vector saemtlicher Konten
	 */
	public Vector<Account> getAllAccounts() {
		WebTarget accountTarget = webTarget.path("accounts");
		List<Account> accounts = accountTarget.request().get(new GenericType<List<Account>>() {
		});
		return new Vector<Account>(accounts);
	}

	/**
	 * Alle Kunden mit allen Konten.
	 * 
	 * @return ein Vektor, dessen Elemente Vektoren sind. Jeder Teilvektor besteht
	 *         aus einem Kundenobjekt an Position 0 und den zugehoerenden
	 *         Kontenobjekten ab Position 1.
	 */
	public Vector<Object> getAllCustomersAndAccounts() {
		Vector<Customer> customers = null;
		Vector<Object> accounts = null;
		Vector<Object> customersAndAccounts = new Vector<Object>();
		Customer c;
		// alle Kunden
		customers = getAllCustomers();
		for (int i = 0; i < customers.size(); i++) {
			c = customers.elementAt(i);
			// alle Konten eines Kunden
			accounts = new Vector<Object>(getAccountsOf(c));
			// Kundenobjekt wird als erstes Element hinzugefuegt
			accounts.add(0, c);
			customersAndAccounts.add(accounts);
		}
		return customersAndAccounts;
	}

	/**
	 * Der Kontostand wird um den angegebenen Betrag veraendert.
	 * 
	 * @param a
	 * @param amount
	 * @return der neue Betrag als float
	 */
	public float modifyAccount(Account a, float amount) {
		WebTarget modifyAccountTarget = webTarget.path("modifyAccount");
		modifyAccountTarget = modifyAccountTarget.queryParam("amount", amount);
		Float balance = modifyAccountTarget.request().post(Entity.entity(a, "application/json"), Float.class);
		a.setBalance(balance);
		return balance;
	}

	/**
	 * Vor- und Nachname eines Kunden werden aktualisiert.
	 * 
	 * @param c
	 * @param vorname
	 * @param nachname
	 * @return das modifizierte Kundenobjekt.
	 */
	public Customer modifyCustomer(Customer c, String vorname, String nachname) {

		WebTarget modifyCustomerTarget = webTarget.path("modifyCustomer");
		modifyCustomerTarget = modifyCustomerTarget.queryParam("vorname", vorname);
		modifyCustomerTarget = modifyCustomerTarget.queryParam("nachname", nachname);
		c = modifyCustomerTarget.request().post(Entity.entity(c, "application/json"), Customer.class);

		return c;
	}
}
