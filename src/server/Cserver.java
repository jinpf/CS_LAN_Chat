package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * connection server,listen to connections,handle client register on-line off-line
 * @author jinpf
 *
 */
public class Cserver {
	private ServerSocket Cserver;
	private Socket client;
	private static boolean state;
	/**
	 * connection server,listen to connections,handle client register on-line off-line
	 * @param s
	 * ServerSocket,listening socket
	 */
	public Cserver(ServerSocket s) {
		Cserver=s;
		state=true;
		while(state){
			try {
				client=Cserver.accept();
				Thread CSlistenT=new Thread(
						new Runnable(){
							public void run(){
								try{
									DataInputStream in=new DataInputStream(client.getInputStream());
									DataOutputStream out=new DataOutputStream(client.getOutputStream());
									while(state){
										try{
											String str;
											str=in.readUTF();
											////////////////
											System.out.println(str);
											
										}catch(Exception e2){
											System.out.println(e2.getMessage());
											break;
										}
									}
									client.close();
								}catch(Exception e1){
									System.out.println(e1.getMessage());
								}
							}
						});
				CSlistenT.start();
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void close_Cserver(){
		state=false;
	}
	/**
	 * connection information
	 * @author jinpf
	 *
	 */
	public class connection{
		private int ID;
		private String name;
		private String password;
		//type: 0 sign in ;1 on-line;2 off-line ;3 list;8 ok;9 error
		private int type;
		private String ip;
		private int mport;
		private int fport;
		private String inform;
		private Ulist[] ulists;
		
		public int getID(){
			return ID;
		}
		
		public String getname(){
			return name;
		}
		
		public String getpassword(){
			return password;
		}
		
		public int gettype(){
			return type;
		}
		
		public String getip(){
			return ip;
		}
		
		public int getmport(){
			return mport;
		}
		
		public int getfport(){
			return fport;
		}
		
		public String getinform(){
			return inform;
		}
		
		public Ulist[] getulist(){
			return ulists;
		}
		
	}
	/**
	 * list information
	 * @author jinpf
	 *
	 */
	public class Ulist{
		private int ID;
		private String name;
		private int state;
		
		public int getID(){
			return ID;
		}
		
		public String name(){
			return name;
		}
		
		public int state(){
			return state;
		}
	}
}
