package com.polenta.driver;

public class PolentaDataSource {
	
	private PolentaDataSource() {
		
	}
	
	public static PolentaConnection getConnection(String host, int port) {
		return new PolentaConnection(host, port);
	}

}
