package com.polenta.shell.app;

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
import com.polenta.driver.PolentaResultSet;
import com.polenta.driver.PolentaStatement;

public class PolentaShellCLIApp implements PolentaShellApp {

	private String serverHost;
	private int serverPort;
	
	private Console console;
	
	private List<String> executedStatements = new ArrayList<String>();
	
	private PolentaConnection connection;

	@Override
	public void startApp(String[] args) throws Exception {
		console = System.console();
		
		String statement;
		
		console.printf("\nWelcome to Polenta Shell...\n\n");
		
		serverHost = extractHostFromArguments(args);

		String port = null;
		try {
			port = extractPortFromArguments(args);
			serverPort = Integer.parseInt(port); 
		} catch (Exception e) {
			console.printf("%s is not a valid port. PolentaShell will close.", port);
			System.exit(0);
		}
		
		connection = PolentaDataSource.getConnection(serverHost, serverPort);
		
		try {
			connection.open();
			console.printf("Connected to PolentaServer on host [%s] and port [%d]\n\n", serverHost, serverPort);
		} catch (Exception e) {
			console.printf("PolentaShell will close. An exception occured: " + e.getMessage() + "\n");
			System.exit(0);
		}

		while (true) {
			statement = console.readLine("%s", "Enter a Polenta statement >> ");
			if (statement.equalsIgnoreCase("QUIT")) {
				console.printf("PolentaShell will be terminated...\n\n");
				System.exit(0);
			} else if (statement.toUpperCase().startsWith("REPEAT")) {
				String sequence = extractCommandSequence(statement);
				if (sequence == null) {
					console.printf("Enter a command sequence when using repeat.\n\n");
				} else {
					int intSequence;
					try {
						intSequence = Integer.parseInt(sequence);
					} catch (Exception e) {
						intSequence = -1;
					}
					if ((intSequence <= 0) || (intSequence > executedStatements.size())) {
						console.printf("Enter a valid command sequence when using repeat.\n\n");
					} else {
						executeStatement(executedStatements.get(intSequence - 1));
					}
				}
			} else if (statement.startsWith("@@")) {
				processFile(statement.substring(2));
			} else {
				executeStatement(statement);
			}
		
		}
	}
	

	protected String extractCommandSequence(String statement) {
		if (statement.contains("[") && statement.contains("]") && statement.indexOf("[") < statement.indexOf("]")) {
			return statement.substring(statement.indexOf("[") + 1, statement.indexOf("]"));
		} else {
			return null;
		}
	}

	protected String extractHostFromArguments(String[] args) throws Exception {
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

	protected String extractPortFromArguments(String[] args) throws Exception {
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
	
	protected void executeStatement(String statement) {
		PolentaStatement ps = connection.createStatement();
		int commandSequence = executedStatements.size() + 1;
		try {
			executedStatements.add(statement);
			console.printf("\nExecuting statement [" + commandSequence + "] >> " + statement + "\n");
			if (statement.toUpperCase().startsWith("SELECT")) {
				PolentaResultSet resultSet = ps.executeQuery(statement);
				console.printf(resultSet.toString());
				console.printf("Size of returned result set: " + resultSet.size() + "\n\n");
			} else {
				ps.execute(statement);
				console.printf("PolentaServer response [" + commandSequence + "] >> Statement or command successfully processed. \n\n");
			}
		} catch (Exception e) {
			console.printf("Statement [" + commandSequence + "] failed to execute. Error: " + e.getMessage() + "\n\n");
		}
	}

	protected void processFile(String fileName) {
		console.printf("\nProcessing script file: " + fileName + "\n");
		File file = new File(fileName);
		if (!file.exists()) {
			console.printf("PolentaShell could not find this file.\n\n");
		} else {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				console.printf("PolentaShell could not open this file.\n\n");
			}
			List<String> statements = new ArrayList<String>();
			if (reader != null) {
				try {
					String line = "";
					while ((line = reader.readLine()) != null) {
						statements.add(line);
					}
				} catch (IOException e) {
					console.printf("PolentaShell could not read this file.\n\n");
				}
			}
			if (!statements.isEmpty()) {
				for (String statement: statements) {
					executeStatement(statement);
				}
			} else {
				console.printf("File is empty.");
			}
		}
	}
	
	
	
}
