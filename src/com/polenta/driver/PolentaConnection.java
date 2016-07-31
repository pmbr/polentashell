package com.polenta.driver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.polenta.shell.PolentaShell;

public class PolentaConnection {

	int port;
	private Socket socket;
	private boolean connected;
	
	PolentaConnection(String host, int port) {
		try {
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
			connected = true;
		} catch (UnknownHostException e) {
			PolentaShell.logConsole("ERROR: PolentaShell could not connect to PolentaServer.\n");
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public String writeToSocket(String statement) {
		BufferedWriter writer;
		BufferedReader reader;
		
		if (connected) {
			try {
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				writer.write(statement);
				writer.newLine();
				writer.flush();

				String response = reader.readLine();
				
				return response;
			} catch (IOException e) {
				return "IO_EXCEPTION";
			}
		} else {
			return "NOT_CONNECTED";
		}
		
	}

	public boolean isConnected() {
		return this.connected;
	}
}
