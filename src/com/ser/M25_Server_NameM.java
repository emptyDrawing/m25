package com.ser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class M25_Server_NameM extends Thread {
	
	static private Object synkey = new Object();
	
	private String nickname;
	private Socket sock;

	private OutputStream os;
	private OutputStreamWriter osw;
	
	private InputStream is ;
	private InputStreamReader isr;
	private BufferedReader buffr;
	
	private M25_Server upper;
	
	public M25_Server_NameM(M25_Server parents, Socket org){
		
		this.upper = parents;
		this.sock = org;
		try {
			is = sock.getInputStream();
			os = sock.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		isr = new InputStreamReader(is);
		buffr = new BufferedReader(isr);
		osw = new OutputStreamWriter(os);
		
		try {
			while(true){
				String msg = buffr.readLine();
				synchronized (synkey) {
					if(M25_Server.NICK_M.containsKey(msg)){
						System.out.println(sock.getInetAddress()+"에서 중복된 닉네임 ["+msg+"]요청");
						osw.write("중복\n");
						osw.flush();
					}else{
						M25_Server.NICK_M.put(msg, this);
						M25_Server.peopleCnt = M25_Server.NICK_M.size();
						System.out.println("현재 등록된 사용자 : "+M25_Server.peopleCnt);
						System.out.println("등록된 닉네임 : "+ msg);
						nickname = msg;
						osw.write("성공\n");
						osw.flush();
						break;
					}// if 조건 끝
				}// 싱크로 필요한 부분
			}// 중복체크 while 끝
			upper.sendListAll();			
			while(true){
				String msg = buffr.readLine();
				if(msg.equals(M25_Server.SOCK_CLOSE)){
					break;
				}
			}
	    } catch (IOException e) {
	    	System.out.println(sock.getInetAddress()+"의 [접속시도 메세지] 읽기 실패..");
		}
		finally{
			try {
				removeClient(nickname);
				buffr.close();
				isr.close();
				is.close();
				this.stop();
			} catch (IOException e) {
				System.out.println("클라이언트간 메인 I/O 크로즈 중에 에러");
			}
		}
	}// run() end
	
	void sendMessage(String msg) {
		
		StringBuffer sendM = new StringBuffer();
		sendM.append(msg);		
		System.out.println("["+nickname+"] 에게 ["+sendM.toString()+"] 보냄");
		sendM.append("\n");

		try {
			osw.write(sendM.toString());
			osw.flush();
		} catch (IOException e) {
			System.out.println("클라이언트 main에 보내주려다가 에러");
		}
	}// sendMessage
	
	
	public void removeClient(String nick){
		M25_Server.NICK_M.remove(nick);
		M25_Server.peopleCnt = M25_Server.NICK_M.size();
		System.out.println(nick+" 사용자가 연결을 종료 하였습니다.");
		System.out.println("현재 등록된 사용자 : "+M25_Server.peopleCnt);
		upper.sendList(M25_Server.ONLINE_LIST);
	}
}// M25_Server_NameM end
