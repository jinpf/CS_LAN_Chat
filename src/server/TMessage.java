package server;

/**
 * transfer chat message format
 * @author jinpf
 *
 */
public class TMessage {
	//type :1 user,ID stands for user id; 2 group,ID stands for group id
	private int type;
	private int FID;
	private int TID;
	private String message;
	
	public int gettype(){
		return type;
	}
	
	public int getFID(){
		return FID;
	}
	
	public int getTID(){
		return TID;
	}
	
	public String getmessage(){
		return message;
	}

}
