package client;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class ChatFrame {
	private JFrame jfchat=null;
	private JButton jbsendt=new JButton("发 送");
	private JButton jbsendf=new JButton("传文件");
	private JButton jbhistory=new JButton("获 取 历 史 消 息");
	private JTextArea jtshow=new JTextArea();
	private JTextArea jtsend=new JTextArea();
	private JTextArea jthistory=new JTextArea();
	private JScrollPane jspshow=new JScrollPane(jtshow,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane jspsend=new JScrollPane(jtsend,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane jsphistory=new JScrollPane(jthistory,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JTabbedPane tabbedPane =new JTabbedPane();
	private JPanel jphistory=new JPanel();
	private Boolean Visible;
	private int FID;
	private int TID;
	
	public ChatFrame(String name,int fid,int tid) {
		Visible=true;
		FID=fid;
		TID=tid;
		
		jfchat=new JFrame(name);
		Font fnt = new Font("微软雅黑",Font.PLAIN +Font.BOLD,12);
		jfchat.setFont(fnt);
		jbsendt.setFont(fnt);
		jbsendf.setFont(fnt);
		jbhistory.setFont(fnt);
		jtshow.setFont(fnt);
		jtsend.setFont(fnt);
		jthistory.setFont(fnt);
		tabbedPane.setFont(fnt);
		
		jtshow.setLineWrap(true);//设置自动换行
		jtshow.setWrapStyleWord(true);
		jtshow.setEditable(false);
		jtsend.setLineWrap(true);//设置自动换行
		jtsend.setWrapStyleWord(true);
		jthistory.setLineWrap(true);//设置自动换行
		jthistory.setWrapStyleWord(true);
		jthistory.setEditable(false);
		
		jphistory.setLayout(new BorderLayout(3,3));
		jphistory.add(jbhistory,BorderLayout.NORTH);
		jphistory.add(jsphistory,BorderLayout.CENTER);
		
		tabbedPane.addTab("聊天消息：", jspshow);
		tabbedPane.addTab("历史消息：", jphistory);
		tabbedPane.setBounds(15, 10, 285, 230);
		jspsend.setBounds(15, 245, 285, 70);
		jbsendt.setBounds(110, 325, 70, 30);
		jbsendf.setBounds(210, 325, 70, 30);
		
		jfchat.setLayout(null);
		jfchat.add(tabbedPane);
		jfchat.add(jspsend);
		jfchat.add(jbsendt);
		jfchat.add(jbsendf);
		
		jfchat.setResizable(false);//不能最大化
		jfchat.setSize(320, 400);
		jfchat.setLocationRelativeTo(null);//在屏幕中央
		jfchat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//点击关闭按钮隐藏并释放窗体
		jfchat.setVisible(Visible);
	}

}
