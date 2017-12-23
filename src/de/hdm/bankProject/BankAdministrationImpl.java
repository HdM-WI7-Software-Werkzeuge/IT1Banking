package de.hdm.bankProject;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Bank;
import de.hdm.bankProject.data.Customer;
import de.hdm.bankProject.db.AccountMapper;
import de.hdm.bankProject.db.CustomerMapper;
import de.hdm.bankProject.report.ReportGenerator;
import de.hdm.bankProject.report.ReportGeneratorImpl;

/**
 * <b>Frage:</b> Warum werden diese Methoden nicht als Teil der Klassen
 * <code>Bank</code> oder <code>Customer</code> implementiert?<br>
 * <b>Antwort:</b> Z.B. das Loeschen eines Kunden erfordert Kenntnisse ueber die
 * Verflechtung eines Kunden mit Konto-Objekten. Um die Klasse <code>Bank</code>
 * bzw. <code>Customer</code> nicht zu stark an andere Klassen zu koppeln, wird
 * das Wissen darueber, wie einzelne "Daten"-Objekte koexistieren, in der
 * vorliegenden Klasse gekapselt.
 * 
 * @author Thies
 */

@Path("")
@Produces("application/json")
public class BankAdministrationImpl implements BankAdministration {

	/**
	 * Referenz auf das zugehoerige Bank-Objekt.
	 */
	private Bank bank = null;
	/**
	 * Referenz auf den DatenbankMapper, der Kundenobjekte mit der Datenbank
	 * abgleicht.
	 */
	private CustomerMapper cMapper = null;
	/**
	 * Referenz auf den DatenbankMapper, der Kontoobjekte mit der Datenbank
	 * abgleicht.
	 */
	private AccountMapper aMapper = null;

	/**
	 * Referenz auf den ReportGenerator.
	 */
	private ReportGenerator reportGenerator = null;

	/**
	 * Konstruktor. Empfaengt als Parameter die Bank, die zu verwalten ist sowie ein
	 * <code>CustomerMapper</code>-Objekt und ein <code>AccountMapper</code>-Objekt
	 * zwecks Datenbankanbindung.
	 */
	public BankAdministrationImpl() {
		this.cMapper = CustomerMapper.customerMapper();
		this.aMapper = AccountMapper.accountMapper();
		this.bank = new Bank();
		this.reportGenerator = new ReportGeneratorImpl(this);

		bank.setId(76543210);
		bank.setName("SparNix Bank AG");
		bank.setCity("Stuttgart");
		bank.setStreet("Wolframstrasse");
		bank.setZip(70191);
	}

	/**
	 * Initiliasieren der Datenbank. Tabellen f�r Konten und Kunden werden
	 * eingerichtet.
	 */

	public void initializeDB() {
		CustomerMapper.customerMapper().reCreateTable();
		AccountMapper.accountMapper().reCreateTable();
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#createAccountFor(de.hdm.bankProject.data.Customer)
	 */
	@Override
	@POST
	@Path("createAccountFor")
	@Consumes("application/json")
	public Account createAccountFor(Customer c) {
		Account a = new Account();
		a.setOwner(c);
		/*
		 * Setzen einer vorlaeufigen Kontonr. Der insert-Aufruf liefert dann ein Objekt,
		 * dessen Nummer mit der Datenbank konsistent ist.
		 */
		a.setId(1);

		// Objekt in der DB speichern.
		return this.aMapper.insert(a);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#createCustomer(java.lang.String, java.lang.String)
	 */
	@Override
	@GET
	@Path("createCustomer")
	public Customer createCustomer(@QueryParam("first") String first, @QueryParam("last") String last) {
		Customer c = new Customer();
		c.setFirstName(first);
		c.setLastName(last);
		/*
		 * Setzen einer vorlaeufigen Kundennr. Der insert-Aufruf liefert dann ein
		 * Objekt, dessen Nummer mit der Datenbank konsistent ist.
		 */
		c.setId(1);

		// Objekt in der DB speichern.
		return this.cMapper.insert(c);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#deleteAccount(de.hdm.bankProject.data.Account)
	 */
	@Override
	@POST
	@Path("deleteAccount")
	@Consumes("application/json")
	public void deleteAccount(Account a) {
		// Account aus der DB entfernen
		this.aMapper.delete(a);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#deleteCustomer(de.hdm.bankProject.data.Customer)
	 */
	@Override
	@POST
	@Path("deleteCustomer")
	@Consumes("application/json")
	public void deleteCustomer(Customer c) {

		// Zunaechst saemtl. Konten des Kunden entfernen.
		Vector<Account> accounts = this.getAccountsOf(c);
		for (int i = 0; i < accounts.size(); i++) {
			this.deleteAccount(accounts.elementAt(i));
		}
		// Anschliessend den Kunden entfernen
		this.cMapper.delete(c);
	}

	/**
	 * Auslesen der zugeordneten Bank.
	 * 
	 * @return Bank-Objekt
	 */
	public Bank getBank() {
		return this.bank;
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getAccountsOf(de.hdm.bankProject.data.Customer)
	 */
	@Override
	@POST
	@Path("getAccountsOf")
	@Consumes("application/json")
	public Vector<Account> getAccountsOf(Customer c) {

		return this.aMapper.findByOwner(c);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getCustomerByName(java.lang.String)
	 */
	@Override
	@GET
	@Path("customer/{name: [a-zA-Z]+}")
	public Vector<Customer> getCustomerByName(@PathParam("name") String lastName) {

		return this.cMapper.findByLastName(lastName);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getCustomerById(int)
	 */
	@Override
	@GET
	@Path("customer/{id: [0-9]+}")
	public Customer getCustomerById(@PathParam("id") int id) {

		return this.cMapper.findByKey(id);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getAllCustomers()
	 */
	@Override
	@GET
	@Path("customers")
	public Vector<Customer> getAllCustomers() {
		return this.cMapper.findAll();
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getAllAccounts()
	 */
	@Override
	@GET
	@Path("accounts")
	public Vector<Account> getAllAccounts() {
		return this.aMapper.findAll();
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#getAllCustomersAndAccounts()
	 */
	@Override
	@GET
	@Path("all")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getAllCustomersAndAccounts() {
		Vector customers = null, accounts = null, customersAndAccounts = new Vector();
		Customer c;
		// alle Kunden
		customers = getAllCustomers();
		for (int i = 0; i < customers.size(); i++) {
			c = (Customer) customers.elementAt(i);
			// alle Konten eines Kunden
			accounts = getAccountsOf(c);
			// Kundenobjekt wird als erstes Element hinzugefuegt
			accounts.add(0, c);
			customersAndAccounts.add(accounts);
		}
		return customersAndAccounts;
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#modifyAccount(de.hdm.bankProject.data.Account, float)
	 */
	@Override
	@POST
	@Path("modifyAccount")
	@Consumes("application/json")
	public float modifyAccount(Account a, @QueryParam("amount") float amount) {
		if (amount < 0) {
			a.makeWithdrawal(-amount);
		} else {
			a.makeDeposit(amount);
		}
		save(a);
		return a.getBalance();
	}

	/**
	 * Speichern eines Account-Objekts in der Datenbank.
	 * 
	 * @param a
	 *            zu sicherndes Objekt.
	 */
	public void save(Account a) {
		aMapper.update(a);
	}

	/* (non-Javadoc)
	 * @see de.hdm.bankProject.BankAdministration#modifyCustomer(de.hdm.bankProject.data.Customer, java.lang.String, java.lang.String)
	 */
	@Override
	@POST
	@Path("modifyCustomer")
	@Consumes("application/json")
	public Customer modifyCustomer(Customer c, @QueryParam("vorname") String vorname, @QueryParam("nachname") String nachname) {
		if (vorname != null) c.setFirstName(vorname);
		if (nachname != null) c.setLastName(nachname);
		save(c);
		return c;
	}

	/**
	 * Speichern eines Customer-Objekts in der Datenbank.
	 * 
	 * @param c
	 *            zu sicherndes Objekt.
	 */
	public void save(Customer c) {
		cMapper.update(c);
	}

	/**
	 * @return Returns the reportGenerator.
	 * @uml.property name="reportGenerator"
	 */
	public ReportGenerator getReportGenerator() {
		return this.reportGenerator;
	}
}
