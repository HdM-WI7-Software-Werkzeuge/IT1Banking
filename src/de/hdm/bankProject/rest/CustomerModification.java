package de.hdm.bankProject.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import de.hdm.bankProject.data.Customer;

public class CustomerModification {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/IT1_Banking/bankservice");

		WebTarget customerWebTarget = webTarget.path("customer/11");
		Customer c = customerWebTarget.request("application/json").get(Customer.class);

		System.out.println(c);

		WebTarget modifyCustomerTarget = webTarget.path("modifyCustomer");
		modifyCustomerTarget.queryParam("vorname", "Chris").request().post(Entity.entity(c, "application/json"));
	}

}
