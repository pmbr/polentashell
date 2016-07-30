package com.polenta.driver;

import java.util.List;
import java.util.Map;

public class PolentaStatement {

	private PolentaConnection connection;
	
	PolentaStatement(PolentaConnection connection) {
		this.connection = connection;
	}
	
	public boolean execute(String statement) throws Exception {
		String response = connection.writeToSocket(statement);
		if (response.equals("OK")) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<Map<String, Object>> executeQuery(String statement) throws Exception {
		throw new Exception("Not implemented");
	}
	
}
