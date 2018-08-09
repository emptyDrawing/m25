package com.ser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class M25_Server extends Thread{

	ServerSocket mainS;
	Socket clientS;
	int serM_port;

	static int	peopleCnt;
	static HashMap<String, M25_Server_NameM> NICK_M;		// Nick-Sock
	static int serverPort = 4000;
	static int serverFileP = 4001;
	static int serverFileP2 = 4002; // ��ǻ� �Ⱦ�
	static int serverNotitcP = 4003;
	static int serverRoomP = 4004;

	static final String SOCK_CLOSE = "##exit##";
	static final String LIST_EMPTY = "##�����##";
	static final String ONLINE_LIST ="�¶���";
	static final String FILE_LIST ="����";
	static final String NOTICE_LIST ="����";
	
	static final String DEFAULT_FILE_LIST 	= "./bin/file_list.bin";
	static final String DEFAULT_NOTICE_LIST = "./bin/notice_list.bin";
	
	M25_Server_FileM fileM;
	M25_Server_NoticeM noticeM; 
	M25_Server_RoomM roomM;
	
	OutputStream os;
	BufferedOutputStream osw;
	
	InputStream is;
	BufferedInputStream buffIns;

	File file_list_File; 
	File notice_list_File;

	
	public M25_Server(int port){
		serM_port = port;
		peopleCnt = 0;
		NICK_M = new HashMap<String, M25_Server_NameM>();
		
		file_list_File = new File(DEFAULT_FILE_LIST);
		notice_list_File = new File(DEFAULT_NOTICE_LIST);
		try {
		if(!file_list_File.exists())	{ 	file_list_File.createNewFile(); 	}
		if(!notice_list_File.exists())	{ 	notice_list_File.createNewFile();	}
		} catch (IOException e) {
			System.out.println("���ϸ���Ʈ, ��������Ʈ ���� ����� ����");
		}
		fileM = new M25_Server_FileM(serverFileP,serverFileP2, this);
		noticeM = new M25_Server_NoticeM(serverNotitcP, this);
		roomM  = new M25_Server_RoomM(serverRoomP, this);
	}

	public void run(){
		try {
			mainS = new ServerSocket(serM_port);
			System.out.println("[���μ���] �غ�Ϸ�");
			while(true){
				clientS = mainS.accept(); 
				System.out.println(clientS.getInetAddress()+"�� ������ �õ��Ͽ����ϴ�.");
				M25_Server_NameM nameM = new M25_Server_NameM(this, clientS);
				nameM.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		M25_Server me = new M25_Server(serverPort);
		me.start();
		me.fileM.start();
		me.noticeM.start();
		me.roomM.start();
	}// main end
	
	void sendMessageS(String msg, String subject) {
		StringBuffer sendM = new StringBuffer();
		sendM.append(msg);
		String key = "";
		Iterator<String> iterator = NICK_M.keySet().iterator();
		
		while(iterator.hasNext()){
			key = iterator.next();
			NICK_M.get(key).sendMessage(msg);
		}
		System.out.println("[���μ���] Ŭ���̾�Ʈ�� ���� ���� --> "+subject);
		
	}// sendMessage()

	void sendList(String msg) {
		StringBuffer sendM = new StringBuffer();
		String tmp ="";
		if(msg.equals(ONLINE_LIST)){
			tmp = NICK_M.keySet().toString();
		}else if(msg.equals(FILE_LIST)){
			if(fileM.file_list.size()==0){
				fileM.file_list.put(LIST_EMPTY, LIST_EMPTY);
				fileM.file_list.keySet();
			}
			tmp = fileM.file_list.keySet().toString();
			
			
		}else if(msg.equals(NOTICE_LIST)){
			
			//19:46 �׽�Ʈ
			for(Integer key : noticeM.notice_list.keySet()){
				 
	            String value = noticeM.notice_list.get(key);
	 
	        }
			StringBuffer tmp_buff = new StringBuffer(noticeM.notice_list.keySet().toString());
			tmp_buff.deleteCharAt(tmp_buff.length()-1);
			tmp_buff.deleteCharAt(0);
			tmp_buff.trimToSize();
			String[] tmps = tmp_buff.toString().split(",");
			tmp_buff.setLength(0);
			tmp_buff.append("[");
			for(int i = 0; i<tmps.length; i++){
				if(i%5 == 0){
					tmp_buff.append(noticeM.notice_list.get(i));
					tmp_buff.append(",");
				}
			}
			tmp_buff.append("]");
			tmp = tmp_buff.toString();
		}
		
		sendM.append(msg);
		sendM.append("����Ʈ@@");
		sendM.append(tmp, 1, tmp.length()-1);
		
		
		sendM.trimToSize();
		sendMessageS(sendM.toString(), msg+"����Ʈ");
		
	}// sendList()
	private void sendOnlinelist() {
		sendList(ONLINE_LIST);
	}

	void sendListAll(){
		this.sendOnlinelist();
		fileM.sendFilelist();
		noticeM.sendNoticelist();
	}

	void saveDataAll(){
		file_list_Save();
		notice_list_Save();
	}

	void file_list_Save() {
		StringBuffer saveTmp = new StringBuffer(fileM.file_list.toString()); 
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(file_list_File);
			osw = new OutputStreamWriter(fos);
			osw.write(saveTmp.toString());
			
		} catch (FileNotFoundException e) {
			System.out.println("���ϸ���Ʈ �����ο� ������ �������� �ʽ��ϴ�.");
		} catch (IOException e) {
			System.out.println("���ϸ���Ʈ �������");
		}finally{
			try {
				osw.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	void notice_list_Save() {
		StringBuffer saveTmp = new StringBuffer(noticeM.notice_list.toString()); 
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream(notice_list_File);
			osw = new OutputStreamWriter(fos);
			osw.write(saveTmp.toString());
			
		} catch (FileNotFoundException e) {
			System.out.println("��������Ʈ �����ο� ������ �������� �ʽ��ϴ�.");
		} catch (IOException e) {
			System.out.println("��������Ʈ �������");
		}finally{
			try {
				osw.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}// E25_Server
