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
	
	private static String serverHost;
	private static int serverPort;
	
	private static Console console;
	
	public static void logConsole(String message) {
		console.printf(message);
	}
	
	public static void main(String[] args) throws Exception  {
		console = System.console();
		
		String statement;
		
		console.printf("\nWelcome to Polenta Shell...\n");
		
		PolentaShell.serverHost = extractHostFromArguments(args);

		String port = null;
		try {
			port = extractPortFromArguments(args);
			PolentaShell.serverPort = Integer.parseInt(port); 
		} catch (Exception e) {
			console.printf("ERROR: %s is not a valid port. PolentaShell will close.", port);
			System.exit(0);
		}
		
		console.printf("PolentaServer host: %s, port: %d.\n\n", serverHost, serverPort);
		
		while (true) {
			statement = console.readLine("%s", "Enter a Polenta statement >> ");
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

	protected static String extractHostFromArguments(String[] args) throws Exception {
		String host = "localhost";
		if (args != null && args.length > 0) {
			for (String arg: args) {
				if (arg.startsWith("--host=")) {
					host = arg.substring(7);
				}
			}
		}
		return host;
	}

	protected static String extractPortFromArguments(String[] args) throws Exception {
		String port = "3110";
		if (args != null && args.length > 0) {
			for (String arg: args) {
				if (arg.startsWith("--port=")) {
					port = arg.substring(7);
				}
			}
		}
		return port;
	}
	
	protected static void executeStatement(String statement) {
		PolentaConnection connection = PolentaDataSource.getConnection(serverHost, serverPort);
		if (!connection.isConnected()) return;
		PolentaStatement ps = connection.createStatement();
		try {
			ps.execute(statement);
		} catch (Exception e) {
			console.printf("ERROR: Statement failed to execute.\n");
		}
	}

	protected static void processFile(String fileName) {
		System.out.println("Processing file " + fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			console.printf("ERROR: PolentaShell could not find this file.\n");
		} else {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				console.printf("ERROR: PolentaShell could not open this file.\n");
			}
			List<String> statements = new ArrayList<String>();
			if (reader != null) {
				try {
					String line = "";
					while ((line = reader.readLine()) != null) {
						statements.add(line);
					}
				} catch (IOException e) {
					console.printf("ERROR: PolentaShell could not read this file.\n");
				}
			}
			if (!statements.isEmpty()) {
				for (String statement: statements) {
					PolentaShell.executeStatement(statement);
				}
			} else {
				console.printf("ERROR: File is empty.");
			}
		}
	}

}
