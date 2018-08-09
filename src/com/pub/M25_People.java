package com.pub;

import java.net.Socket;

public class M25_People {

	private String nickName;
	private Socket sock;
	
	
	public M25_People(){
		
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public Socket getsock() {
		return sock;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public void setSock(Socket sock) {
		this.sock = sock;
	}
	
}
