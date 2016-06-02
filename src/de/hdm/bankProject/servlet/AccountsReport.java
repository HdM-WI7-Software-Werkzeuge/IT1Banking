package de.hdm.bankProject.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hdm.bankProject.BankAdministration;
import de.hdm.bankProject.data.Customer;
import de.hdm.bankProject.report.AllAccountsOfCustomerReport;
import de.hdm.bankProject.report.HTMLReportWriter;
import de.hdm.bankProject.report.PlainTextReportWriter;
import de.hdm.bankProject.report.ReportGenerator;

/**
 * Demonstrator f�r die Interaktion mit dem <code>BankServer</code>.
 * <p>
 * <b>Anwendungsfall:</b> Erstellen eines Reports.
 * 
 * @see de.hdm.bankProject.BankServer
 * 
 * @author Thies, Rathke
 */
public class AccountsReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AllAccountsOfCustomerReport report = createAllAccountsOfCustomerReport();

		if (report != null) {
			HTMLReportWriter htmlWriter = new HTMLReportWriter();
			htmlWriter.processAllAccountsOfCustomerReport(report);

			// Set response content type
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
			out.println(docType + htmlWriter.getReportText());
		}
	}

	// Method to handle POST method request.
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public static void main(String[] args) {
		AllAccountsOfCustomerReport report = createAllAccountsOfCustomerReport();

		if (report != null) {
			PlainTextReportWriter textWriter = new PlainTextReportWriter();
			textWriter.processAllAccountsOfCustomerReport(report);
			System.out.println(textWriter.getReportText());
		}
	}

	private static AllAccountsOfCustomerReport createAllAccountsOfCustomerReport() {

		BankAdministration verwaltung = new BankAdministration();
		ReportGenerator gen = verwaltung.getReportGenerator();

		// Die Bank fragen wir, ob sie einen Kunden mit der Kd.-Nr. 1 kennt.
		Customer c = verwaltung.getCustomerById(1);

		if (c != null) {
			return gen.createAllAccountsOfCustomerReport(c);
		} else {
			return null;
		}
	}

}