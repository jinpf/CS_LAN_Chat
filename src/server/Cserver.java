package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
											String istr;
											istr=in.readUTF();
											System.out.println(istr);
											Gson cigson=new Gson();
											Connection Cmessage=cigson.fromJson(istr, Connection.class);
											int type=Cmessage.gettype();
											if (type==0){//sign up
												//check SQL to see if contain the user
												
												
												
												
												//send OK message
												JsonObject message8=new JsonObject();
												message8.addProperty("type", 8);
												Gson cogson08=new Gson();
												String ostr8=cogson08.toJson(message8);
												out.writeUTF(ostr8);
												
//												//send Error message
//												JsonObject message9=new JsonObject();
//												message9.addProperty("type", 9);
//												message9.addProperty("inform", "用户已存在！");
//												Gson cogson09=new Gson();
//												String ostr9=cogson09.toJson(message9);
//												out.writeUTF(ostr9);
											}else if(type==1){
												//check SQL to see if user password correct
												
												//send OK message
												JsonObject message8=new JsonObject();
												message8.addProperty("type", 8);
												Gson cogson08=new Gson();
												String ostr8=cogson08.toJson(message8);
												out.writeUTF(ostr8);
												
//												//send Error message
//												JsonObject message9=new JsonObject();
//												message9.addProperty("type", 9);
//												message9.addProperty("inform", "用户名或密码错误！");
//												Gson cogson09=new Gson();
//												String ostr9=cogson09.toJson(message9);
//												out.writeUTF(ostr9);
											}
											
											
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
	public class Connection{
		private int ID;
		private String name;
		private String password;
		//type: 0 sign up ;1 sign in;2 on-line;3 off-line ;4 list;8 OK;9 error
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
