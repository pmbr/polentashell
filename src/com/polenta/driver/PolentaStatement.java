package com.polenta.driver;

public class PolentaStatement {

	private PolentaConnection connection;
	
	PolentaStatement(PolentaConnection connection) {
		this.connection = connection;
	}
	
	public String execute(String statement) throws Exception {
		return connection.writeToSocket(statement);
	}
	
}
