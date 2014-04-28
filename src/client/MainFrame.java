package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MainFrame {
	private frmlogin loginf;
	private frmsignup signupf;
	private frmuser userf;
	
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
								userf=new frmuser(jtfname.getText());
							}
						}
					});
		}
		/**
		 * set frame visible
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
			
			jbsignup.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							if(e.getSource()==jbsignup){
								
								userf=new frmuser(jtfname.getText());
								jfsignup.dispose();
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
	
	public static void main(String args[]){
		new MainFrame();
	}
}
