package client;

/**
 * message format
 * @author jinpf
 *
 */
public class CMessage {
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
