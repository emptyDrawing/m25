package com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.client.ui.M25_Ui_chatRoom;

public class M25_Client_roomM extends Thread {
	private Socket socket;
	private M25_Ui_chatRoom gui;

	private String nickName;

	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader buffr;

	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter buffw;

	private final String END_MESSAGE = "@@�޼�����" ;
	private M25_Client_roomM_Listener subS;
	
	public M25_Client_roomM(M25_Ui_chatRoom gui,String nick) {
		this.gui = gui;
		this.nickName = nick;
	}

	public void enterRoom(String nickname){	// �г��� �̿� �����Ҷ�
		sendMessage(nickname);
	}
	public void sendMessage(String msg){
		if(msg.equals("")|msg.equals("\n")){return;}
		try {
			buffw.write((msg+"\n"+END_MESSAGE+"\n"));
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			socket = new Socket(M25_Client.serverIP, M25_Client.serverRoomP);
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			buffw = new BufferedWriter(osw);
			
			
			enterRoom(nickName);
			subS = new M25_Client_roomM_Listener();
			subS.start();
		} catch (UnknownHostException e) {
			System.out.println("[ä��M] [ä�ù漭��]�� �غ���� �ʾҽ��ϴ�.");
		} catch (IOException e) {
			System.out.println("[ä��M] [ä�ù漭��]�� ���ᵵ�� �����߻�.");
		}
	}
	@SuppressWarnings("deprecation")
	public void closeChatRoom(){
		try {
			subS.stop();
			buffr.close();
			isr.close();
			is.close();
			buffw.close();
			osw.close();
			os.close();
			socket.close();
			gui.instanceTonull();
		} catch (IOException e) {
			System.out.println("ä�ù��Ĺ�� �����ϴٰ� �����߻�.");
		} finally {
			gui = null;
			System.out.println("[ä��M]�� ����.");
			this.stop();
		}
	}

	class M25_Client_roomM_Listener extends Thread{
		StringBuffer msg_buff = new StringBuffer();
		String tmp = null;
		public void run() {
				try {
					while(true){
						tmp = buffr.readLine();
							if(tmp.equals(END_MESSAGE)){
							synchronized (END_MESSAGE) {
							String result = msg_buff.toString();
							String[] tmps = result.split("����Ʈ@@");
							if(tmps.length>1){
								String[] tmp2 = tmps[1].split(",");
									gui.onlineList.removeAll();
									for( int i=0; i<tmp2.length; i++ ){
										String item = tmp2[i].trim();
										System.out.println("�߰��� ���ο� : "+item);
										if(item.equals(nickName)){item += "(��)";}
										gui.onlineList.add(item);
									}
							}else{
								gui.chatRoom.append(result);
							}
							msg_buff.setLength(0);
							}
						}else{
							msg_buff.append(tmp+"\n");
						}
					}
					// ���⿡ �����Ҷ� �־�ߵ� ���
					// ����Ʈ�� �޾Ҵ��� Ȯ��
				} catch (IOException e) {
					System.out.println("ä�ù� Listen ���� ����");
				}
		}// run end
		
	}// M25_Client_roomM_Listener end
}