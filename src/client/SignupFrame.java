package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
 * sign up frame
 * @author jinpf
 *
 */
public class SignupFrame{
	private JFrame jfsignup=new JFrame("注册新用户");
	private JLabel jlname=new JLabel("用 户 名");
	private JLabel jlpassword=new JLabel("密     码");
	private JLabel jlconfirmpw=new JLabel("确认密码");
	private JTextField jtfname=new JTextField();
	private JPasswordField  jpfpassword=new JPasswordField ();
	private JPasswordField  jpfconfirmpw=new JPasswordField ();
	private JButton jbsignup=new JButton("注册");

	private Socket sconnect;
	private LoginFrame loginf;
	/**
	 * sign up frame
	 */
	public SignupFrame(LoginFrame lgf){
		loginf=lgf;
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
															CMessage message=gson.fromJson(str, CMessage.class);
															int type=message.gettype();
															if (type==8){//OK!
																//start user frame
																new UserFrame(jtfname.getText(),message.getID(),sconnect,loginf);
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
