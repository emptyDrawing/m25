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
						System.out.println(sock.getInetAddress()+"���� �ߺ��� �г��� ["+msg+"]��û");
						osw.write("�ߺ�\n");
						osw.flush();
					}else{
						M25_Server.NICK_M.put(msg, this);
						M25_Server.peopleCnt = M25_Server.NICK_M.size();
						System.out.println("���� ��ϵ� ����� : "+M25_Server.peopleCnt);
						System.out.println("��ϵ� �г��� : "+ msg);
						nickname = msg;
						osw.write("����\n");
						osw.flush();
						break;
					}// if ���� ��
				}// ��ũ�� �ʿ��� �κ�
			}// �ߺ�üũ while ��
			upper.sendListAll();			
			while(true){
				String msg = buffr.readLine();
				if(msg.equals(M25_Server.SOCK_CLOSE)){
					break;
				}
			}
	    } catch (IOException e) {
	    	System.out.println(sock.getInetAddress()+"�� [���ӽõ� �޼���] �б� ����..");
		}
		finally{
			try {
				removeClient(nickname);
				buffr.close();
				isr.close();
				is.close();
				this.stop();
			} catch (IOException e) {
				System.out.println("Ŭ���̾�Ʈ�� ���� I/O ũ���� �߿� ����");
			}
		}
	}// run() end
	
	void sendMessage(String msg) {
		
		StringBuffer sendM = new StringBuffer();
		sendM.append(msg);		
		System.out.println("["+nickname+"] ���� ["+sendM.toString()+"] ����");
		sendM.append("\n");

		try {
			osw.write(sendM.toString());
			osw.flush();
		} catch (IOException e) {
			System.out.println("Ŭ���̾�Ʈ main�� �����ַ��ٰ� ����");
		}
	}// sendMessage
	
	
	public void removeClient(String nick){
		M25_Server.NICK_M.remove(nick);
		M25_Server.peopleCnt = M25_Server.NICK_M.size();
		System.out.println(nick+" ����ڰ� ������ ���� �Ͽ����ϴ�.");
		System.out.println("���� ��ϵ� ����� : "+M25_Server.peopleCnt);
		upper.sendList(M25_Server.ONLINE_LIST);
	}
}// M25_Server_NameM end
