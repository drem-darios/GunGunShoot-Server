package com.drem.games.ggs.server.session;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author drem
 */
public class SessionManager {

	private static SessionManager instance;
	private Queue<Socket> waitingClients;
	
	private SessionManager() {
		this.waitingClients = new ConcurrentLinkedQueue<Socket>();
	}
	public static SessionManager getInstance() {
		
		if (instance == null) {
			instance = new SessionManager();
		}
		
		return instance;
	}
	
	public void offerClient(Socket client) {
		if (waitingClients.isEmpty()) {
			System.out.println("Adding client to waiting pool...");
			waitingClients.add(client);
		} else {
			System.out.println("Client found. Starting session.");
			Session session;
			try {
				session = new Session(waitingClients.poll(), client);
				new Thread(session).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
