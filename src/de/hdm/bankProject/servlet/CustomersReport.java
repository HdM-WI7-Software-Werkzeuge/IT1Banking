package de.hdm.bankProject.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.hdm.bankProject.BankAdministration;
import de.hdm.bankProject.report.AllAccountsOfAllCustomersReport;
import de.hdm.bankProject.report.HTMLReportWriter;
import de.hdm.bankProject.report.PlainTextReportWriter;
import de.hdm.bankProject.report.ReportGenerator;

/**
 * Demonstrator f�r die Interaktion mit dem <code>BankServer</code>.
 * <p>
 * <b>Anwendungsfall:</b> Erstellen eines Reports.
 * 
 * @see de.hdm.thies.bankProjekt.BankServer
 * 
 * @author Thies
 */
public class CustomersReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		AllAccountsOfAllCustomersReport report = createAllAccountsOfAllCustomersReport();

		if (report != null) {
			HTMLReportWriter htmlWriter = new HTMLReportWriter();
			htmlWriter.processAllAccountsOfAllCustomersReport(report);

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
		AllAccountsOfAllCustomersReport report = createAllAccountsOfAllCustomersReport();

		if (report != null) {
			PlainTextReportWriter textWriter = new PlainTextReportWriter();
			textWriter.processAllAccountsOfAllCustomersReport(report);
			System.out.println(textWriter.getReportText());
		}
	}

	private static AllAccountsOfAllCustomersReport createAllAccountsOfAllCustomersReport() {

		BankAdministration verwaltung = new BankAdministration();
		ReportGenerator gen = verwaltung.getReportGenerator();

		
		return gen.createAllAccountsOfAllCustomersReport();
	}

}