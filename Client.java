package chatserver;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.*;

public class Client extends Panel implements Runnable {
	/**
	 * It sets up the client interface, initiates the connection with the server host using the socket,
	 * When user enters a message it delivers the message by writing to the OutPutStream object.
	 */
	private static final long serialVersionUID = 1L;
	//.. GUI components
	private TextField userText = new TextField();
	private TextArea chatArea = new TextArea();
	
	//... The socket connecting us to the server
	private Socket socket;
	
	//... The streams we communicate to the server and they come from the socket
	private DataOutputStream dout;
	private DataInputStream din;
	
	//.. Constructor
	public Client (String host, int port){
		
		//set up the chat screen 
		setLayout(new BorderLayout());
		add("North", userText);
		add("Center", chatArea);
		
		userText.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				processMessage(e.getActionCommand());
			}
		});
		
		//Connect to the server
		try{
			//... Initiate the connection
			socket = new Socket(host,port);
			//Print the the connection info
			System.out.println("Connected to : "+socket);
			
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream (socket.getOutputStream());
			// Start a background thread for receiving messages
			new  Thread (this).start();
		}
		catch(IOException ie){
			System.out.println(ie);
		}			
	}
	
	private void processMessage (String message){
		try{
			// send the message to the server
			dout.writeUTF(message);
			userText.setText("");
		}
		catch (IOException ie){
			System.out.println(ie);
		}
	}
	
	//Background thread runs this: show messages from other window
	public void run() {
		try{
			//... Receive messages one after another ; forever
			while(true){
				String message = din.readUTF();
				//Add the message to the chatArea
				chatArea.append(message+"\n");
			}
		}
		catch(IOException ie){
			System.out.println(ie);;
		}
	}

}
