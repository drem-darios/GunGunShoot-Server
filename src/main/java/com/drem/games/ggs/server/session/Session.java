package com.drem.games.ggs.server.session;

import java.io.IOException;
import java.net.Socket;

import com.drem.games.ggs.player.RemotePlayer;
import com.drem.games.ggs.weapon.WeaponAction;

/**
 * @author drem
 */
public class Session implements Runnable {
	
	private RemotePlayer player1;
	private RemotePlayer player2;

	private WeaponAction action1;
	private WeaponAction action2;
	private boolean player1Moved;
	private boolean player2Moved;
	
	public Session(Socket client1, Socket client2) throws IOException {
		player1 = new RemotePlayer(client1);
		player2 = new RemotePlayer(client2);
	}

	public void run() {
		System.out.println("Session started!");
		
		try {
			player1.write("Player 1 begin!");
			player2.write("Player 2 begin!");
		
			while(true) {
				Thread readMove1 = new Thread(new Runnable() {
					public void run() {
						action1 = player1.readMove();
						player1Moved = true;
					}
				});

				Thread readMove2 = new Thread(new Runnable() {
					public void run() {
						action2 = player2.readMove();
						player2Moved = true;
					}
				});
				
				readMove1.start();
				readMove2.start();
				
				waitForOpponent();
				
				player1.writeMove(action2);
				player2.writeMove(action1);
				
				player1Moved = false;
				player2Moved = false;
	
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void waitForOpponent() {
		System.out.print("Waiting for opponent...");
		int count = 0;
		int prettyPrint = 10;

		while ((!hasPlayer1Moved() || !hasPlayer2Moved()) && count < 60) {
			count++;
			System.out.print('.');
			if ((count % prettyPrint) == 0) {
				// Just to make output look a little nicer while waiting
				System.out.println();
				prettyPrint--;
			}
			sleep();
		}
		System.out.println();
	}
	
	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean hasPlayer1Moved() {
		return player1Moved;
	}
	
	private boolean hasPlayer2Moved() {
		return player2Moved;
	}
	
}
