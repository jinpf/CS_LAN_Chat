package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * connection server,listen to connections,handle client register on-line off-line
 * @author jinpf
 *
 */
public class CServer {
	private ServerSocket Cserver;
	private boolean state; 
	private String DBDRIVER = "org.gjt.mm.mysql.Driver" ;
	private String DBURL = "jdbc:mysql://localhost:3306/socket_test" ;
	private String DBUSER = "jinpf" ;
	private String DBPASS = "123456" ;
	private Connection conn=null;
	/**
	 * connection server,listen to connections,handle client register on-line off-line
	 * @param s
	 * ServerSocket,listening socket
	 */
	public CServer(ServerSocket s) {
		Cserver=s;
		state=true;
		
		//connect MySQL
		
		try{
			Class.forName(DBDRIVER) ;	// 加载驱动程序
			conn = DriverManager.getConnection(DBURL,DBUSER,DBPASS) ;		// 数据库连接
			while(state){
				try {
					final Socket client=Cserver.accept();//client socket,each user use one socket
					Thread CSlistenT=new Thread(
							new Runnable(){
								public void run(){
									int CID=-1; //use when exception,mark the Connect ID
									try{
										DataInputStream in=new DataInputStream(client.getInputStream());
										DataOutputStream out=new DataOutputStream(client.getOutputStream());
										while(state){
											try{
												String istr;
												istr=in.readUTF();
												System.out.println(istr);
												Gson cigson=new Gson();
												CMessage Cmessage=cigson.fromJson(istr, CMessage.class);
												
												int type=Cmessage.gettype();
												CID=Cmessage.getID();
												if (type==0){//sign up
													//check SQL to see if contain the user
													Statement stmt = conn.createStatement() ;	// 数据库的操作对象
													String sql="select * from user where name='"+Cmessage.getname()+"'";
													ResultSet rs = null ;		// 保存查询结果
													rs = stmt.executeQuery(sql) ;
													if(rs.next()){
														//send Error message
														JsonObject message9=new JsonObject();
														message9.addProperty("type", 9);
														message9.addProperty("inform", "用户已存在！");
														Gson cogson09=new Gson();
														String ostr9=cogson09.toJson(message9);
														out.writeUTF(ostr9);
													}else{
														//add user information in SQL
														sql = "INSERT INTO user(name,password) "+
																" VALUES ('"+Cmessage.getname()+"','"+Cmessage.getpassword()+"')" ;
														stmt.executeUpdate(sql) ;
														sql = "select id from user where name='"+Cmessage.getname()+"'";
														rs = stmt.executeQuery(sql) ;
														rs.next();
														int ID=rs.getInt("id");
														
														//send OK message
														JsonObject message8=new JsonObject();
														message8.addProperty("type", 8);
														message8.addProperty("ID", ID);
														Gson cogson08=new Gson();
														String ostr8=cogson08.toJson(message8);
														out.writeUTF(ostr8);
													}
													rs.close() ;
													stmt.close();
											
												}else if(type==1){//sign in
													//check SQL to see if user password correct
													Statement stmt = conn.createStatement() ;	// 数据库的操作对象
													String sql="select id from user where name='"+Cmessage.getname()+"' and password='"+Cmessage.getpassword()+"'";
													ResultSet rs = null ;		// 保存查询结果
													rs = stmt.executeQuery(sql) ;
													if(rs.next()){
														//send OK message
														JsonObject message8=new JsonObject();
														message8.addProperty("type", 8);
														message8.addProperty("ID", rs.getInt("id"));
														Gson cogson08=new Gson();
														String ostr8=cogson08.toJson(message8);
														out.writeUTF(ostr8);
													}else{
														//send Error message
														JsonObject message9=new JsonObject();
														message9.addProperty("type", 9);
														message9.addProperty("inform", "用户名或密码错误！");
														Gson cogson09=new Gson();
														String ostr9=cogson09.toJson(message9);
														out.writeUTF(ostr9);
													}
													rs.close() ;
													stmt.close();
													
												}else if(type==2){//on-line
													Statement stmt = conn.createStatement() ;	// 数据库的操作对象
													String sql="UPDATE user SET state=1 where id="+Cmessage.getID();
													stmt.executeUpdate(sql) ;
													stmt.close();
													Thread SendlistT=new Thread(
															new Runnable(){
																public void run(){
																	while(true){
																		try{
																			Statement stmt = conn.createStatement() ;	// 数据库的操作对象
																			String sql="select id,name,state from user";
																			ResultSet rs = null ;		// 保存查询结果
																			rs = stmt.executeQuery(sql) ;
																			
																			JsonObject message=new JsonObject();
																			message.addProperty("type", 4);
																			JsonArray ulists=new JsonArray();
																			while(rs.next()){
																				JsonObject user=new JsonObject();
																				user.addProperty("ID", rs.getInt("id"));
																				user.addProperty("name", rs.getString("name"));
																				user.addProperty("state", rs.getInt("state"));
																				ulists.add(user);
																			}
																			message.add("ulists", ulists);
																			Gson gson=new Gson();
																			String str=gson.toJson(message);
																			DataOutputStream out=new DataOutputStream(client.getOutputStream());
																			System.out
																					.println(client.getPort()+str);
																			out.writeUTF(str);
																			Thread.sleep(2000);
																			
																		}catch(Exception e){
																			System.out
																					.println("type 4!exception"+e.getMessage());
																			break;
																		}
																	}
																}
															});
													SendlistT.start();
												}else if(type==3){//off-line
													Statement stmt = conn.createStatement() ;	// 数据库的操作对象
													String sql="UPDATE user SET state=0 where id="+Cmessage.getID();
													stmt.executeUpdate(sql) ;
													stmt.close();
													break;//break the while loop ,close the connect socket then
												}
												
												
											}catch(Exception e2){
												System.out.println(e2.getMessage());
												if(CID!=-1){
													Statement stmt = conn.createStatement() ;	// 数据库的操作对象
													String sql="UPDATE user SET state=0 where id="+CID;
													stmt.executeUpdate(sql) ;
													stmt.close();
												}
												
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
		}catch(Exception e){
			//MySQL connect failure
			System.out.println(e.getMessage());
			System.out.println("pay attention of your mysql service!");
			System.exit(1) ;
		}
		
	}
	
	public void close_Cserver(){
		state=false;
		if(conn!=null){
			try{
				if(!conn.isClosed())
					conn.close();
			}catch(Exception e){
				
			}
		}
	}
}
