package de.hdm.bankProject.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.hdm.bankProject.BankAdministration;
import de.hdm.bankProject.data.Account;
import de.hdm.bankProject.data.Customer;


@ApplicationPath("/bankservice")

public class BankApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public BankApplication() {
		//singletons.add(new HelloWorld());
		classes.add(BankAdministration.class);
		classes.add(Customer.class);
		classes.add(Account.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
