package com.polenta.driver;

public class PolentaDataSource {
	
	private PolentaDataSource() {
		
	}
	
	//read connection info from file
	//public static PolentaConnection getConnection() 
	
	//public static PolentaConnection getConnection(String username, String password, int port)
	
	public static PolentaConnection getConnection(String host, int port) {
		return new PolentaConnection(host, port);
	}

}
