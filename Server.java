package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * This is the listener class , a server , a stand alone application.
 */
public class Server {

	private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();

	private ServerSocket ss;// will be used to accept new connections
	
	public Server(int port) throws IOException {
		listen(port);
	}

	private void listen(int port) throws java.io.IOException {
		// ...create a new server socket

		ss = new ServerSocket(port);

		System.out.println("Listening on port: " + ss);

		while (true) {
			// ... ss.accept() returns a Socket that represents the incoming connection.
			Socket s = ss.accept();
			System.out.println("Incoming connection from : " + s);
			
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			
			//... save the DataOutputStream object so that we do not need create it again. 
			outputStreams.put(s, dout);
			
			//... This creates a new thread that takes of the connection
			new ServerThread(this, s);

		}

	}

	// get an enumeration for all the active connections
	Enumeration<DataOutputStream> getoutputStreams() {
		return outputStreams.elements();
	}

	// send message to all the clients
	void sentToAll(String message) {
		synchronized (outputStreams) {
			for (Enumeration<?> e = getoutputStreams(); e.hasMoreElements();) {
				// ...get the output stream
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				// ..send the message
				try {
					dout.writeUTF(message);

				} catch (IOException ie) {
					System.out.println(ie);
				}
			}
		}
	}

	/*
	 * Remove a socket , and its corresponding output stream , from our list .
	 * This is usually called by a connection thread that has discovered the
	 * connection to he client is dead.
	 */
	void removeConnection(Socket S) {
		synchronized (outputStreams) {
			System.out.println("Removing conneciton to " + S);
			outputStreams.remove(S);
		}
		try {
			S.close();
		} catch (IOException ie) {
			System.out.println("Error closing : " + S);
			ie.printStackTrace();

		}
	}

	public static void main(String[] args) throws Exception {
		// ...Get the port number from the command line
		int portNumber = Integer.parseInt(args[0]);

		// ...Now we create a new server object that will listen to the given port number
		// ...Accepting connections
		new Server(portNumber);

	}

}
