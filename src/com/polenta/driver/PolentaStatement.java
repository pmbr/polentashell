package com.polenta.driver;

import java.io.IOException;

public class PolentaStatement {

	private PolentaConnection connection;
	
	PolentaStatement(PolentaConnection connection) {
		this.connection = connection;
	}
	
	public String execute(String statement) throws IOException, PolentaConnectionException {
		if (connection.isConnected()) {
			return connection.writeToSocket(statement);
		} else {
			throw new PolentaConnectionException("Connection to server has been closed. Statement cannot be executed"); 
		}
	}
	
}
