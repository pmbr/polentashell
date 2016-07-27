package com.polenta.shell;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			
			if (statement.startsWith("@@")) {
				PolentaShell.processFile(statement.substring(2));
			} else {
				PolentaShell.executeStatement(statement);
			}
		
		}
	}
	
	protected static void executeStatement(String statement) {
		PolentaConnection connection = PolentaDataSource.getConnection(3110);
		PolentaStatement ps = connection.createStatement();
		try {
			ps.execute(statement);
		} catch (Exception e) {
			System.out.println("Statement failed to execute.");
		}
	}

	protected static void processFile(String fileName) {
		System.out.println("Processing file " + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("PolentaShell could not find this file.");
		} else {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				System.out.println("PolentaShell could not open this file.");
			}
			List<String> statements = new ArrayList<String>();
			if (reader != null) {
				try {
					String line = "";
					while ((line = reader.readLine()) != null) {
						statements.add(line);
					}
				} catch (IOException e) {
					System.out.println("PolentaShell could not read this file.");
				}
			}
			if (!statements.isEmpty()) {
				for (String statement: statements) {
					PolentaShell.executeStatement(statement);
				}
			} else {
				System.out.println("File is empty.");
			}
		}
	}

}
