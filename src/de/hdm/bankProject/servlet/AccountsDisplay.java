package de.hdm.bankProject.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hdm.bankProject.BankAdministration;
import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;

/**
 * Demonstrator f�r die Interaktion mit dem <code>BankServer</code>.
 * <p>
 * <b>Anwendungsfall:</b> Erstellen eines Reports.
 * 
 * @see de.hdm.bankProject.BankServer
 * 
 * @author Thies, Rathke
 */
public class AccountsDisplay extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BankAdministration verwaltung = new BankAdministration();
		Vector<Customer> customers;
		if (request.getParameter("name")=="") {
			customers = verwaltung.getAllCustomers();
		} else {
			customers = verwaltung.getCustomerByName(request.getParameter("name"));
		}

		// Set response content type
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		out.println(docType);
		out.println("<html><body>");
		
		out.println("<h2>Kontenanzeige</h2>\n" + "<form action=\"AccountsDisplay\" method=\"POST\">\n"
				+ "Nachname: <input type=\"text\" name=\"name\" />\n" + "<input type=\"submit\" value=\"Anzeigen\" />\n"
				+ "</form>");

		if (customers.size() > 0) {
			out.println("<ol>");
			for (Customer c : customers) {
				out.println("<li>" + c.getFirstName() + " " + c.getLastName() + "</li>");
				Vector<Account> accounts = verwaltung.getAccountsOf(c);
				if (accounts.size() > 0) {
					out.println("<ul>");
					for (Account a : accounts) {
						out.println("<li>" + a.getId() + ": " + a.getBalance() + "</li>");
					}
					out.println("</ul>");
				}
			}
			out.println("</ol>");
		}
		out.println("</body></html>");
	}

}