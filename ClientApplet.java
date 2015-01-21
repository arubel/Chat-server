package chatserver;
import java.applet.*;
import java.awt.*;

public class ClientApplet extends Applet {
	
	/**
	 * Web deployable clinet applet
	 */
	private static final long serialVersionUID = -8485728440237899383L;

	public void init(){
		String host = getParameter ("host");
		int port = Integer.parseInt(getParameter("port"));
		setLayout(new BorderLayout());
		add("Center", new Client(host, port));
		
	}
	

}
