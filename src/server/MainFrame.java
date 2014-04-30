package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

public class MainFrame {
	private static DatagramSocket listener;
	public MainFrame() {
		
	}
	
	public static void main(String args[]){
		try {
			ServerSocket connection=new ServerSocket(0);
			
			final String sends=String.valueOf(connection.getLocalPort());
			for (int i=3000;i<3005;i++){
				try{
					listener=new DatagramSocket(i);
					Thread listenT=new Thread(
							new Runnable(){
								public void run(){
									try{
										DatagramPacket Receive=new DatagramPacket(new byte[128],128);
										while(true){
											try{
												listener.receive(Receive);
												DatagramPacket send=new DatagramPacket(sends.getBytes("UTF-8"),sends.length(),Receive.getAddress(),Receive.getPort());
												listener.send(send);
											}catch(Exception e1){
												System.out.println(e1.getMessage());
												break;
											}
										}
									}catch(Exception e){
										System.out.println(e.getMessage());
									}
								}
							});
					listenT.start();
					break;
				}catch(Exception e1){
					
				}
			}
			CServer cs=new CServer(connection);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
