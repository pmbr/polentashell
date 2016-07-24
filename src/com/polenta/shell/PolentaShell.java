package com.polenta.shell;

import java.io.Console;

import com.polenta.driver.PolentaConnection;
import com.polenta.driver.PolentaDataSource;
import com.polenta.driver.PolentaStatement;

public class PolentaShell {
	
	public static void main(String[] args) {
		Console console = System.console();
		
		String statement;
		
		console.printf("Welcome to Polenta Shell\n");
		
		while (true) {
			statement = console.readLine("%s", "Enter a Polenta statement: ");
			if (statement.equals("exit")) {
				System.exit(0);
			}
		
			PolentaConnection connection = PolentaDataSource.getConnection(3110);
			PolentaStatement ps = connection.createStatement();
			try {
				ps.execute(statement);
			} catch (Exception e) {
				System.out.println("Statement failed to execute.");
			}
			
		}
	}
	
}
