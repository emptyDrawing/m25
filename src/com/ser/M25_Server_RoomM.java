package com.ser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class M25_Server_RoomM extends Thread {

	private M25_Server 		upper;
	private int  			port;
	private ServerSocket 	servSock;
	private Socket 			socket;


	private HashMap<String, Socket> people_list = null;	//	String nickname;

	private final String END_MESSAGE = "@@메세지끝" ;
	
	public M25_Server_RoomM(int serverRoomP, M25_Server parentS) {
		this.upper = parentS;
		this.port = serverRoomP;
		people_list = new HashMap<String, Socket>();
	}

	public void run() {
		try {
			Collections.synchronizedMap(people_list);
			servSock = new ServerSocket(port);
			System.out.println("[채팅방서버] 준비완료");
			while(true){
				socket = servSock.accept(); // 여기서 클라이언트 받음
				System.out.println(socket.getInetAddress() + "에서 접속했습니다.");
				M25_Server_RoomM_Listenner runner = new M25_Server_RoomM_Listenner(socket);
				runner.start();
			}
		} catch (IOException e) {
			System.out.println("[채팅방서버] 클라이언트간 소캣연결중 에러");
		}
	}

	private synchronized void sendMessage(String msg, String nick) {
		StringBuffer sendM = new StringBuffer();
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter buffw = null;
		
		if(msg.equals("")|msg.equals("\n")){return;}
		if(nick!=null){ sendM.append("["+nick+"]의 메세지..."); sendM.append("\n");}
		
		sendM.append(msg);
		sendM.append("\n");
		sendM.append(END_MESSAGE);
		sendM.append("\n");
		sendM.trimToSize();
		
		String key = "";
		Iterator<String> iterator = people_list.keySet().iterator();
		while(iterator.hasNext()){
			key = iterator.next();
			try{
				os = people_list.get(key).getOutputStream();
				osw = new OutputStreamWriter(os);
				buffw = new BufferedWriter(osw);
				buffw.write(sendM.toString());
				buffw.flush();
			} catch(IOException e){
				System.out.println("[채팅방서버] ["+key+"]에게 메세지 보내다가 에러");
			}
		}
	}// sendMessage()
	
	private void sendList() {
		StringBuffer sendM = new StringBuffer();
		String tmp = people_list.keySet().toString();
		sendM.append("채팅방리스트@@");
		sendM.append(tmp, 1, tmp.length()-1);
		sendM.trimToSize();
		sendMessage(sendM.toString(), null);
	}
	
	//맵의내용(클라이언트) 저장과 삭제 
	public void addClient(String nick, Socket sock) throws IOException{
		String message="채팅방에 ["+nick+ "] 님이 접속하셨습니다.";
		System.out.println(message);
		people_list.put(nick, sock);
		sendMessage(message+"\n",null);
		sendList();
	}
	
	public void removeClient(String nick){
		String message="채팅방에서 ["+nick + "] 님이 나가셨습니다.";
		System.out.println(message);
		people_list.remove(nick);
		sendMessage(message+"\n",null);
		sendList();
	}

	class  M25_Server_RoomM_Listenner extends Thread {
		private String nick;
		private Socket sock;
		private InputStream is; // 데이터 입력 스트림
		InputStreamReader isr;
		BufferedReader buffr;

		public M25_Server_RoomM_Listenner(Socket socket) {
			sock = socket;
			try {
				is   = sock.getInputStream();
				isr   = new InputStreamReader(is);
				buffr = new BufferedReader(isr);
				nick = buffr.readLine();
				addClient(nick,sock);
			} catch (IOException e) {
				System.out.println("[채팅서버] ["+sock+"] 으로 부터 닉네임받는 도중 에러");
				e.printStackTrace();
			}
		}

		public void run() {
			StringBuffer msg_buff = new StringBuffer();
			String tmp = null;
			try {
				while(true){
					tmp = buffr.readLine();
					if(tmp.equals(M25_Server.SOCK_CLOSE)){
						break;
					}else if(tmp.equals(END_MESSAGE)){
						sendMessage(msg_buff.toString(),nick);
						msg_buff.setLength(0);
					}else{
						msg_buff.append(tmp+"\n");
					}
				}
				removeClient(nick);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					buffr.close();
					isr.close();
					sock.close();
					System.out.println("[채팅방서버] ["+nick+"]의 소캣을 닫았습니다.");
				} catch (IOException e) {
					System.out.println("[채팅방서버] ["+nick+"]의 소캣을 닫다가 에러");
				}
			}
		}// run() end

	}// M25_Server_RoomM_Listenner
	


}//M25_Server_RoomM end


