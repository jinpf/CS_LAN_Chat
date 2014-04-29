package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
	private JList jlgroup;
	private JScrollPane jspgroup;
	private JTabbedPane tabbedPane;
	private JPanel jpgroup;
	private JButton jbjoingroup;
	
	private String Name;
	private int ID;
	private boolean state=true;
	private Ulist[] ulists;
	private Socket sconnect;
	private Vector<String> v=new Vector<String>();
	
	private LoginFrame loginf;
	/**
	 * user communicate frame
	 * @param name
	 * String, user name
	 * @param id
	 * int, user id
	 * @param sc
	 * Socket, connect socket
	 * @param lgf
	 * LoginFrame, login frame, the start frame, to set visible 
	 */
	public UserFrame(String name,int id,Socket sc,LoginFrame lgf){
		Name=name;
		sconnect=sc;
		loginf=lgf;
		ID=id;
		
		jfuser=new JFrame(Name);
		jluser=new JList();
		jlgroup=new JList();
		jbjoingroup=new JButton("创建 / 加入群组");
		jpgroup=new JPanel();
		tabbedPane = new JTabbedPane();
		
		Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
		jbjoingroup.setFont(fnt);
		tabbedPane.setFont(fnt);
		
		jluser.setBorder(BorderFactory.createTitledBorder("所有用户："));
		jlgroup.setBorder(BorderFactory.createTitledBorder("所有群组："));
		
		jluser.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jluser.setBackground(new Color(245,245,245));
		jlgroup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlgroup.setBackground(new Color(245,245,245));
		
		jspuser=new JScrollPane(jluser);
		jspgroup=new JScrollPane(jlgroup);
		
		jpgroup.setLayout(new BorderLayout(2,2));
		jpgroup.add(jspgroup,BorderLayout.CENTER);
		jpgroup.add(jbjoingroup,BorderLayout.SOUTH);
		
		tabbedPane.addTab("联系人", jspuser);
		tabbedPane.addTab("群   组", jpgroup);
		tabbedPane.setBounds(20, 20, 195, 360);
		
		jfuser.setLayout(null);
		jfuser.setResizable(false);
		jfuser.add(tabbedPane);
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