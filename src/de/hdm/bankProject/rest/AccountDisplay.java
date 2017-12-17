package de.hdm.bankProject.rest;

import javax.ws.rs.client.ClientBuilder;

public class AccountDisplay {

	public static void main(String[] args) {
		System.out.println(
				  ClientBuilder.newClient().
				  target( "http://localhost:8080/IT1_Banking/bankservice/customer/Adenauer" ).request().get(String.class));
	}

}
