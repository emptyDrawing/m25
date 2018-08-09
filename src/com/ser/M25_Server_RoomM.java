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

	private final String END_MESSAGE = "@@�޼�����" ;
	
	public M25_Server_RoomM(int serverRoomP, M25_Server parentS) {
		this.upper = parentS;
		this.port = serverRoomP;
		people_list = new HashMap<String, Socket>();
	}

	public void run() {
		try {
			Collections.synchronizedMap(people_list);
			servSock = new ServerSocket(port);
			System.out.println("[ä�ù漭��] �غ�Ϸ�");
			while(true){
				socket = servSock.accept(); // ���⼭ Ŭ���̾�Ʈ ����
				System.out.println(socket.getInetAddress() + "���� �����߽��ϴ�.");
				M25_Server_RoomM_Listenner runner = new M25_Server_RoomM_Listenner(socket);
				runner.start();
			}
		} catch (IOException e) {
			System.out.println("[ä�ù漭��] Ŭ���̾�Ʈ�� ��Ĺ������ ����");
		}
	}

	private synchronized void sendMessage(String msg, String nick) {
		StringBuffer sendM = new StringBuffer();
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter buffw = null;
		
		if(msg.equals("")|msg.equals("\n")){return;}
		if(nick!=null){ sendM.append("["+nick+"]�� �޼���..."); sendM.append("\n");}
		
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
				System.out.println("[ä�ù漭��] ["+key+"]���� �޼��� �����ٰ� ����");
			}
		}
	}// sendMessage()
	
	private void sendList() {
		StringBuffer sendM = new StringBuffer();
		String tmp = people_list.keySet().toString();
		sendM.append("ä�ù渮��Ʈ@@");
		sendM.append(tmp, 1, tmp.length()-1);
		sendM.trimToSize();
		sendMessage(sendM.toString(), null);
	}
	
	//���ǳ���(Ŭ���̾�Ʈ) ����� ���� 
	public void addClient(String nick, Socket sock) throws IOException{
		String message="ä�ù濡 ["+nick+ "] ���� �����ϼ̽��ϴ�.";
		System.out.println(message);
		people_list.put(nick, sock);
		sendMessage(message+"\n",null);
		sendList();
	}
	
	public void removeClient(String nick){
		String message="ä�ù濡�� ["+nick + "] ���� �����̽��ϴ�.";
		System.out.println(message);
		people_list.remove(nick);
		sendMessage(message+"\n",null);
		sendList();
	}

	class  M25_Server_RoomM_Listenner extends Thread {
		private String nick;
		private Socket sock;
		private InputStream is; // ������ �Է� ��Ʈ��
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
				System.out.println("[ä�ü���] ["+sock+"] ���� ���� �г��ӹ޴� ���� ����");
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
					System.out.println("[ä�ù漭��] ["+nick+"]�� ��Ĺ�� �ݾҽ��ϴ�.");
				} catch (IOException e) {
					System.out.println("[ä�ù漭��] ["+nick+"]�� ��Ĺ�� �ݴٰ� ����");
				}
			}
		}// run() end

	}// M25_Server_RoomM_Listenner
	


}//M25_Server_RoomM end


