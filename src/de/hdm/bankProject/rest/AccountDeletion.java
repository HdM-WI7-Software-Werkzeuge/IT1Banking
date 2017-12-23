package de.hdm.bankProject.rest;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;

public class AccountDeletion {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://localhost:8080/IT1_Banking/bankservice");
		
		WebTarget customerWebTarget = webTarget.path("customer/11");
		Customer c = customerWebTarget.request("application/json").get(Customer.class);
		
		WebTarget getAccountsTarget = webTarget.path("getAccountsOf");
		List<Account> accounts = getAccountsTarget.request("application/json").
				post(Entity.entity(c, "application/json"), new GenericType<List<Account>>() {});
		
		Account firstAccount = accounts.get(0);
		System.out.println(firstAccount);
		
		WebTarget deleteAccountTarget = webTarget.path("deleteAccount");
		deleteAccountTarget.request().post(Entity.entity(firstAccount, "application/json"));
	}

}
