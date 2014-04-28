package server;

import java.io.IOException;
import java.net.ServerSocket;

public class MainFrame {

	public MainFrame() {
		
	}
	
	public static void main(String args[]){
		try {
			ServerSocket connection=new ServerSocket(8888);
			Cserver cs=new Cserver(connection);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
