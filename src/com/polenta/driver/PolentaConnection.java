package com.polenta.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PolentaConnection {

	private String host;
	private int port;
	private Socket socket;
	private boolean connected;
	
	PolentaConnection(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void open() throws Exception {
		try {
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
			connected = true;
		} catch (java.net.ConnectException ce) {
			throw new PolentaConnectionException("It was not possible connect to Polenta server on host [" + this.host + "] and port [" + this.port + "].\n");
		} catch (UnknownHostException uhe) {
			throw new PolentaConnectionException("It was not possible connect to Polenta server on host [" + this.host + "] and port [" + this.port + "].\n");
		}
	}
	
	public PolentaStatement createStatement() {
		return new PolentaStatement(this);
	}
	
	//public void close()
	//public void commit()
	//public void rollback
	//public void createTransaction
	//public void setAutoCommit
	//public void getAutoCommit
	//public PolentaMetaData getMetaData
	
	public String writeToSocket(String statement) throws PolentaConnectionException, IOException {
		BufferedWriter writer;
		BufferedReader reader;
		
		if (this.connected) {
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			writer.write(statement);
			writer.newLine();
			writer.flush();

			String response = reader.readLine();
			if (response == null) {
				 this.connected = false;
				 throw new PolentaConnectionException("Connection to server is no longer active.");
			}
			
			return response;
		} else {
			throw new PolentaConnectionException("Connection to server has been closed.");
		}
		
	}
	
	public void close() throws Exception {
		this.connected = false;
		socket.close();	
	}

	public boolean isConnected() {
		return this.connected;
	}
}
