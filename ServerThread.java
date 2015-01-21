package chatserver;

import java.net.*;
import java.io.*;

/*
 * Message protocol:
 * 1. When user types in something on their chat window it will be sent as a string
 * through a DataOutputStream object
 * 2.Server receives  that message through a DataInputStream , sends that message to all the user
 * as a string using a DataOutputStream object.
 * 3.The users will receive the messages using DataInputStream object.
 */

public class ServerThread extends Thread {
	// ...The server that spawned us
	private Server server;
	// ...The socket connected to our client
	private Socket socket;

	public ServerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		start();
	}

	/*
	 * The run method starts running in a separate thread when start() is called
	 * in the constructor
	 * The following code does the actual chat work
	 */
	public void run() {
		
		try {

			DataInputStream din = new DataInputStream(socket.getInputStream());

			while (true) {
				// ..read the next message
				String message = din.readUTF();
				// ...print the message
				System.out.println("Sending " + message);
				// ... send the message to all clients
				server.sentToAll(message);
			}
		} catch (EOFException ie) {

		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			// ... The connection is closed for one reason or another
			// ... so let the server deal with it by removing dead connections
			server.removeConnection(socket);
		}

	}

}
