package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MainFrame {
	private frmlogin loginf;
	private frmsignup signupf;
	private frmuser userf;
	private Socket sconnect;
	
	public MainFrame() {
		loginf=new frmlogin();
	}
	
	/**
	 * login frame
	 * @author jinpf
	 *
	 */
	public class frmlogin{
		private JFrame jflogin=new JFrame("登录");
		private JLabel jlname=new JLabel("用户名");
		private JLabel jlpassword=new JLabel("密  码");
		private JTextField jtfname=new JTextField();
		private JPasswordField  jpfpassword=new JPasswordField ();
		private JButton jbsignup=new JButton("注册");
		private JButton jblogin=new JButton("登录");
		/**
		 * login frame
		 */
		public frmlogin(){
			Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
			jflogin.setFont(fnt);
			jlname.setFont(fnt);
			jlpassword.setFont(fnt);
			jbsignup.setFont(fnt);
			jblogin.setFont(fnt);
			
			jpfpassword.setEchoChar('*');
			jlname.setBounds(40, 30, 50, 20);
			jtfname.setBounds(85, 30, 120, 20);
			jlpassword.setBounds(40, 60, 50, 20);
			jpfpassword.setBounds(85, 60, 120, 20);
			jbsignup.setBounds(40, 100, 70, 25);
			jblogin.setBounds(135, 100, 70, 25);
			
			jflogin.setLayout(null);
			jflogin.setResizable(false);
			
			jflogin.add(jlname);
			jflogin.add(jtfname);
			jflogin.add(jlpassword);
			jflogin.add(jpfpassword);
			jflogin.add(jbsignup);
			jflogin.add(jblogin);
			
			jflogin.setSize(250, 180);
			jflogin.setLocationRelativeTo(null);
			jflogin.setVisible(true);
			jflogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//click sign up button ,start sign up frame
			jbsignup.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							if(e.getSource()==jbsignup){
								
								//start sign up frame
								jflogin.setVisible(false);
								signupf=new frmsignup();
								
							}
						}
					});
			
			//click login button ,start user frame
			jblogin.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							if(e.getSource()==jblogin){
								if (jtfname.getText().equals("")){
									JOptionPane.showMessageDialog(jflogin.getContentPane(),
											"用户名不能为空", "请检查！", JOptionPane.WARNING_MESSAGE);
								}else{
									
									//send login message to check
									try {
										sconnect=new Socket("localhost", 8888);
										DataInputStream in =new DataInputStream(sconnect.getInputStream());
										DataOutputStream out=new DataOutputStream(sconnect.getOutputStream());
										jblogin.setEnabled(false);
										
										//construct on-line string
										JsonObject message=new JsonObject();
										message.addProperty("name", jtfname.getText());
										message.addProperty("password", String.valueOf(jpfpassword.getPassword()));
										message.addProperty("type", 1);
										Gson cgson=new Gson();
										String str=cgson.toJson(message);
										out.writeUTF(str);
										Thread listenT=new Thread(
												new Runnable(){
													public void run(){
														try {
															DataInputStream in =new DataInputStream(sconnect.getInputStream());
															sconnect.setSoTimeout(500);
															String str=in.readUTF();
															Gson gson=new Gson();
															Connection message=gson.fromJson(str, Connection.class);
															int type=message.gettype();
															if (type==8){//ok!
																//start user frame
																userf=new frmuser(jtfname.getText());
																loginf.setvisible(false);
															}else if(type==9){
																JOptionPane.showMessageDialog(jflogin.getContentPane(),
																		message.getinform(), "注册失败！", JOptionPane.WARNING_MESSAGE);
																if (sconnect!=null&&!sconnect.isClosed()){
																	try {
																		sconnect.close();
																	} catch (Exception e2) {
																	}
																}
															}
																	
														} catch (Exception e) {
															if (sconnect!=null&&!sconnect.isClosed()){
																try {
																	sconnect.close();
																} catch (Exception e2) {
																}
															}
															JOptionPane.showMessageDialog(jflogin.getContentPane(),
																	e.getMessage(), "登录失败！", JOptionPane.WARNING_MESSAGE);
														}
														jblogin.setEnabled(true);
													}
												});
										listenT.start();
										
									} catch (Exception e1) {
										jblogin.setEnabled(true);
										JOptionPane.showMessageDialog(jflogin.getContentPane(),
												e1.getMessage(), "连接失败！", JOptionPane.WARNING_MESSAGE);
										if (sconnect!=null&&!sconnect.isClosed()){
											try {
												sconnect.close();
											} catch (Exception e2) {
											}
										}
									}
								}

								
							}
						}
					});
		}
		/**
		 * set login frame visible
		 * @param state
		 * boolean
		 */
		public void setvisible(boolean state){
			jflogin.setVisible(state);
		}
	}
	
	/**
	 * sign up frame
	 * @author jinpf
	 *
	 */
	public class frmsignup{
		private JFrame jfsignup=new JFrame("注册新用户");
		private JLabel jlname=new JLabel("用 户 名");
		private JLabel jlpassword=new JLabel("密     码");
		private JLabel jlconfirmpw=new JLabel("确认密码");
		private JTextField jtfname=new JTextField();
		private JPasswordField  jpfpassword=new JPasswordField ();
		private JPasswordField  jpfconfirmpw=new JPasswordField ();
		private JButton jbsignup=new JButton("注册");
		/**
		 * sign up frame
		 */
		public frmsignup(){
			Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
			jfsignup.setFont(fnt);
			jlname.setFont(fnt);
			jlpassword.setFont(fnt);
			jlconfirmpw.setFont(fnt);
			jbsignup.setFont(fnt);
			
			jpfpassword.setEchoChar('*');
			jpfconfirmpw.setEchoChar('*');
			
			jfsignup.setLayout(null);
			jfsignup.setResizable(false);
			
			jlname.setBounds(30, 20, 60, 20);
			jtfname.setBounds(85, 20, 120, 20);
			jlpassword.setBounds(30, 50, 60, 20);
			jpfpassword.setBounds(85, 50, 120, 20);
			jlconfirmpw.setBounds(30, 80, 60, 20);
			jpfconfirmpw.setBounds(85, 80, 120, 20);
			jbsignup.setBounds(85, 110, 80, 25);
			
			jfsignup.add(jlname);
			jfsignup.add(jtfname);
			jfsignup.add(jlpassword);
			jfsignup.add(jpfpassword);
			jfsignup.add(jlconfirmpw);
			jfsignup.add(jpfconfirmpw);
			jfsignup.add(jbsignup);
			jfsignup.setSize(250, 180);
			jfsignup.setLocationRelativeTo(null);
			jfsignup.setVisible(true);
			
			jfsignup.addWindowListener(
					new WindowAdapter(){
						/**
						 * it happens when close the windows
						 */
						public void windowClosing(WindowEvent e){
							//dispose sign in frame
							jfsignup.dispose();
							//display login frame
							loginf.setvisible(true);
						}
					});
			
			//user sign up
			jbsignup.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							if(e.getSource()==jbsignup){
								if (jtfname.getText().equals("")){
									JOptionPane.showMessageDialog(jfsignup.getContentPane(),
											"用户名不能为空", "请检查！", JOptionPane.WARNING_MESSAGE);
								}else{
									String password=String.valueOf(jpfpassword.getPassword());
									if (password.equals(String.valueOf(jpfconfirmpw.getPassword()))){
										jbsignup.setEnabled(false);
										//send login message to check
										try {
											sconnect=new Socket("localhost", 8888);
											DataOutputStream out=new DataOutputStream(sconnect.getOutputStream());
											
											
											//construct sign up string
											JsonObject message=new JsonObject();
											message.addProperty("name", jtfname.getText());
											message.addProperty("password", password);
											message.addProperty("type", 0);
											Gson cgson=new Gson();
											String str=cgson.toJson(message);
											out.writeUTF(str);
											Thread listenT=new Thread(
													new Runnable(){
														public void run(){
															try {
																DataInputStream in =new DataInputStream(sconnect.getInputStream());
																sconnect.setSoTimeout(500);
																String str=in.readUTF();
																Gson gson=new Gson();
																Connection message=gson.fromJson(str, Connection.class);
																int type=message.gettype();
																if (type==8){//ok!
																	//start user frame
																	userf=new frmuser(jtfname.getText());
																	jfsignup.dispose();
																}else if(type==9){
																	JOptionPane.showMessageDialog(jfsignup.getContentPane(),
																			message.getinform(), "注册失败！", JOptionPane.WARNING_MESSAGE);
																	if (sconnect!=null&&!sconnect.isClosed()){
																		try {
																			sconnect.close();
																		} catch (Exception e2) {
																		}
																	}
																}				
															} catch (Exception e) {
																try {
																	sconnect.close();
																} catch (IOException e1) {
																}
																JOptionPane.showMessageDialog(jfsignup.getContentPane(),
																		e.getMessage(), "接收失败！", JOptionPane.WARNING_MESSAGE);
															}
															jbsignup.setEnabled(true);
														}
													});
											listenT.start();
											
										} catch (Exception e1) {
											jbsignup.setEnabled(true);
											JOptionPane.showMessageDialog(jfsignup.getContentPane(),
													e1.getMessage(), "连接失败！", JOptionPane.WARNING_MESSAGE);
											if (sconnect!=null&&!sconnect.isClosed()){
												try {
													sconnect.close();
												} catch (Exception e2) {
												}
											}
										}
										
									}else{
										JOptionPane.showMessageDialog(jfsignup.getContentPane(),
												"请检测密码设置", "请检查！", JOptionPane.WARNING_MESSAGE);
									}
								}
								
								
								
							}
						}
					});
			
		}
	}
	
	/**
	 * user frame, with list show who can chat with
	 * @author jinpf
	 *
	 */
	public class frmuser{
		private JFrame jfuser;
		private JList jluser;
		private JScrollPane jspuser;
		
		public frmuser(String name){
			jfuser=new JFrame(name);
			
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
							
							
							jfuser.dispose();
							loginf.setvisible(true);
						}
					});
			
		}
	}
	
	
	
	//connect message format
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
	
	public static void main(String args[]){
		new MainFrame();
	}
}
