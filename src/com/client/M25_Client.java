package com.client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;

import com.client.ui.M25_Ui_Login;
import com.client.ui.M25_Ui_Main;
import com.pub.M25_People;

public class M25_Client extends Thread{


	static String serverIP ;
	static int serverPort = 4000;
	static int serverFileP = 4001;
	static int serverFileP2 = 4002;
	static int serverNotitcP = 4003;
	static int serverRoomP = 4004;


	static public M25_People 	people;

	public  Socket 				mainS;
	public 	OutputStream 		mainOs;
	private OutputStreamWriter 	osw ;

	public 	InputStream 		mainIs;
	private InputStreamReader 	isr ;
	private BufferedReader 		buffr; 

	public 	M25_Ui_Login 		myLoginUi;
	public 	M25_Ui_Main 		myMainUi;


	public static final String SOCK_CLOSE = "##exit##";
	static final String LIST_EMPTY = "##�����##";
	static final String ONLINE_LIST ="�¶���";
	static final String FILE_LIST ="����";
	static final String NOTICE_LIST ="����";


	public M25_Client() {
		people = new M25_People();
	}

	public static void main(String[] args) {
		M25_Client me =	new M25_Client();// �����ּ� msg�� �ޱ�
		me.myLoginUi = new M25_Ui_Login(me);
	}// main end

	// ��� list
	public void run() {
		// �α��� �Ѿ ����..
		myMainUi = new M25_Ui_Main(this);
		System.out.println("[Ŭ���̾�Ʈ] �α��� ����");
		new M25_Client_lintener().start();

	}// run() end

	class M25_Client_lintener extends Thread{
		String msg;
		@SuppressWarnings("deprecation")
		public void run() {
			try {
				while(true){
					// M25_Sever�� ���� ��� �����...
					msg = buffr.readLine();
					String[] tmp = msg.split("����Ʈ@@");
					if(tmp.length>1){
						String[] tmp2 = tmp[1].split(",");
						if(tmp[0].equals(ONLINE_LIST)){
							myMainUi.onlineList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(tmp2[i].trim().equals(people.getNickName())){tmp2[i] += "(��)";}
								myMainUi.onlineList.add(tmp2[i].trim());
							}
						}else if(tmp[0].equals(FILE_LIST)){
							myMainUi.fileList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(!tmp2[i].trim().equals(LIST_EMPTY) )	
								{ myMainUi.fileList.add(tmp2[i].trim()); }
							}// ����Ʈ �ݺ� ��� ��

						}else if(tmp[0].equals(NOTICE_LIST)){
							myMainUi.noticeList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(!tmp2[i].equals("null") )	
								{ myMainUi.noticeList.add(tmp2[i]);}
							}// ����Ʈ �ݺ� ��� ��
						}
					}

				}// while end
			} catch (IOException e) {
				System.out.println("[Ŭ���̾�Ʈ] �������� ���ο��� ������");
			}finally{
				this.stop();
			}
		}// run end
	}// M25_Client_roomM_lintener end

	public boolean setSock(String msg){
		serverIP = msg;
		try {
			mainS = new Socket(serverIP, serverPort);
			mainIs = mainS.getInputStream();
			isr = new InputStreamReader(mainIs);
			buffr = new BufferedReader(isr);
			mainOs = mainS.getOutputStream();
			osw = new OutputStreamWriter(mainOs);
		} catch (UnknownHostException e) {
			System.out.println("�������� �ʴ� IP �Դϴ�.");
			return false;
		} catch (IOException e) {
			System.out.println("������ ���� �۵����� �ʽ��ϴ�.");
			return false;
		}
		System.out.println("��Ĺ�� ���������� ����������ϴ�.");
		return true;
	}

	public boolean setNickName(){
		try {
			String msg = "";
			msg = myLoginUi.inputName.getText();
			osw.write(msg+"\n");
			osw.flush();
			String result = buffr.readLine();
			if(result.equals("����")){
				myLoginUi.jf.setVisible(false);
				people.setNickName(msg);
				people.setSock(mainS);
				this.start();		// ������ ��Ĺ ���� ����.
				return true;
			}else{
				myLoginUi.inputName.setText("");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}//setNickName()

	public void closeSocket() {
			try {
				if(!mainS.isClosed()){
				osw.write(SOCK_CLOSE+"\n");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
				try {
					osw.close();
					buffr.close();
					isr.close();
					mainIs.close();
					mainOs.close();
					mainS.close();
				} catch (IOException e) {
					System.out.println("[Ŭ���̾�Ʈ] ��Ĺ�� �ݴµ��߿� ����");
				}
			}
		System.out.println("closeSocket");
		myLoginUi.jf.dispose();
		myMainUi.endframe();
		System.out.println("���θ��߱���");
		this.stop();
		System.exit(0);
	}// closeSocket()

}// M25_Client end
