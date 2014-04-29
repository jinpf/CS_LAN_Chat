package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * login frame
 * @author jinpf
 *
 */
public class LoginFrame{
	private Socket sconnect;
	private JFrame jflogin=new JFrame("登录");
	private JLabel jlname=new JLabel("用户名");
	private JLabel jlpassword=new JLabel("密  码");
	private JTextField jtfname=new JTextField();
	private JPasswordField  jpfpassword=new JPasswordField ();
	private JButton jbsignup=new JButton("注册");
	private JButton jblogin=new JButton("登录");
	private static LoginFrame loginf;
	/**
	 * login frame
	 */
	public LoginFrame(){
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
							new SignupFrame(loginf);
							
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
														CMessage message=gson.fromJson(str, CMessage.class);
														int type=message.gettype();
														if (type==8){//OK!
															//start user frame
															new UserFrame(jtfname.getText(),message.getID(),sconnect,loginf);
															jflogin.setVisible(false);
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
	
	public static void main(String args[]){
		loginf=new LoginFrame();
	}
}