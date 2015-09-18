package com.drem.games.ggs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.drem.games.ggs.server.session.SessionManager;

/**
 * @author drem
 */
public class Server {
	private SessionManager sessionManager = SessionManager.getInstance();
	private ServerSocket serverSocket;
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		System.out.println("Starting the socket server at port:" + port);
		serverSocket = new ServerSocket(port);
		while (true) {
			// Listen for clients. Block till one connects
			System.out.println("Waiting for clients...");
			Socket client = serverSocket.accept();
			sessionManager.offerClient(client);
		}
	}

	/**
	 * Creates a SocketServer object and starts the server.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// Setting a default port number.
		int portNumber = 3736;

		try {
			// initializing the Socket Server
			Server socketServer = new Server(portNumber);
			socketServer.start();
		} catch (IOException e) {
			e.printStackTrace();
			System.err
					.println("Cannot establish connection. Server may not be down. "
							+ e.getMessage());
		}
	}

}
