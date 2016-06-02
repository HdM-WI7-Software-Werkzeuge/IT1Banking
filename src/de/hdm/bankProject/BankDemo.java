package de.hdm.bankProject;

import de.hdm.bankProject.db.AccountMapper;
import de.hdm.bankProject.db.CustomerMapper;

public class BankDemo {
	public static void main(String[] args) {
		BankAdministration ba = new BankAdministration();
		ba.initializeDB();
		
		String accountDataString = 
				"(1,1,300.23), "
				+ "(2,7,1000.94), "
				+ "(3,5,978278), "
				+ "(4,6,215652), "
				+ "(5,6,121232), "
				+ "(6,3,32), "
				+ "(7,10,10105.8), "
				+ "(8,6,120), "
				+ "(9,6,3e+006)";
		AccountMapper.accountMapper().fillTable(accountDataString);
		
		String cutomerDataString =
				"(1,'Herbert','Müller'), "
				+ "(2,'Erna','Sparbier'), "
				+ "(3,'Sandra','Schweigemeier'), "
				+ "(4,'Helmut','Schmidt'), "
				+ "(5,'Helmut','Kohl'), "
				+ "(6,'Gerhard','Schröder'), "
				+ "(7,'Willy','Brand'), "
				+ "(8,'Kurt Georg','Kiesinger'), "
				+ "(9,'Ludwig','Erhard'), "
				+ "(10,'Konrad','Adenauer') ";
		CustomerMapper.customerMapper().fillTable(cutomerDataString);
	}
}
