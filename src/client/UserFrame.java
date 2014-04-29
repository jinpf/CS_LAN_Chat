package client;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * user frame, with list show who can chat with
 * @author jinpf
 *
 */
public class UserFrame{
	private JFrame jfuser;
	private JList jluser;
	private JScrollPane jspuser;
	private String Name;
	private int ID;
	private boolean state=true;
	private Ulist[] ulists;
	private Socket sconnect;
	private Vector<String> v=new Vector<String>();
	
	private LoginFrame loginf;
	
	public UserFrame(String name,int id,Socket sc,LoginFrame lgf){
		Name=name;
		sconnect=sc;
		loginf=lgf;
		jfuser=new JFrame(Name);
		ID=id;
		jluser=new JList();
		jluser.setBorder(BorderFactory.createTitledBorder("所有用户："));
		jluser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jluser.setBackground(new Color(245,245,245));
		jspuser=new JScrollPane(jluser);
		
		jspuser.setBounds(20, 20, 195, 360);
		
		jfuser.setLayout(null);
		jfuser.setResizable(false);
		jfuser.add(jspuser);
		jfuser.setSize(240, 430);
		//set frame at the center of the screen
		jfuser.setLocationRelativeTo(null);
		jfuser.setVisible(true);
		
		jfuser.addWindowListener(
				new WindowAdapter(){
					/**
					 * it happens when close the windows
					 */
					public void windowClosing(WindowEvent e){
						//off-line
						Offline();
						state=false;
						
						if (sconnect!=null&&!sconnect.isClosed()){
							try {
								sconnect.close();
							} catch (Exception e2) {
							}
						}
						jfuser.dispose();
						loginf.setvisible(true);
					}
				});
		
		//send on-line
		Online();
		
	}
	/**
	 * send on-line data and refresh list
	 */
	public void Online(){
		try{
			DataOutputStream out=new DataOutputStream(sconnect.getOutputStream());
			
			//construct on line string
			JsonObject message=new JsonObject();
			message.addProperty("ID", ID);
			message.addProperty("type", 2);
			Gson cgson=new Gson();
			String str=cgson.toJson(message);
			out.writeUTF(str);
			Thread listenT=new Thread(
					new Runnable(){
						public void run(){
							try {
								DataInputStream in =new DataInputStream(sconnect.getInputStream());
								sconnect.setSoTimeout(0);
								while(state){
									String str=in.readUTF();
									Gson gson=new Gson();
									CMessage message=gson.fromJson(str, CMessage.class);
									int type=message.gettype();
									if(type==4){
										ulists=message.getulist();
										v.clear();
										for (Ulist ulist:ulists){
											String Vstr=ulist.getID()+"  "+ulist.getname()+"  "+(ulist.getstate()==1?"在线":"离线");
											v.add(Vstr);
										}
										Collections.sort(v);
										jluser.setListData(v);
									}
									
								}			
							} catch (Exception e) {
								if (sconnect!=null&&!sconnect.isClosed()){
									JOptionPane.showMessageDialog(jfuser.getContentPane(),
											"网络发生故障请重新登录"+e.getMessage(), "sorry！", JOptionPane.WARNING_MESSAGE);
									try {
										sconnect.close();
									} catch (Exception e2) {
									}
								}
								jfuser.dispose();
								loginf.setvisible(true);
							}
						}
					}
					);
			listenT.start();
			
			
		}catch(Exception e){
			
		}
	}
	/**
	 * send off-line data
	 */
	public void Offline(){
		try{
			DataOutputStream out=new DataOutputStream(sconnect.getOutputStream());
			
			//construct on line string
			JsonObject message=new JsonObject();
			message.addProperty("ID", ID);
			message.addProperty("type", 3);
			Gson cgson=new Gson();
			String str=cgson.toJson(message);
			out.writeUTF(str);
		}catch(Exception e){
			
		}
	}
}