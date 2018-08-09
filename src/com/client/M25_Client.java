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
	static final String LIST_EMPTY = "##비었음##";
	static final String ONLINE_LIST ="온라인";
	static final String FILE_LIST ="파일";
	static final String NOTICE_LIST ="공지";


	public M25_Client() {
		people = new M25_People();
	}

	public static void main(String[] args) {
		M25_Client me =	new M25_Client();// 서버주소 msg로 받기
		me.myLoginUi = new M25_Ui_Login(me);
	}// main end

	// 명령 list
	public void run() {
		// 로그인 넘어간 시점..
		myMainUi = new M25_Ui_Main(this);
		System.out.println("[클라이언트] 로그인 성공");
		new M25_Client_lintener().start();

	}// run() end

	class M25_Client_lintener extends Thread{
		String msg;
		@SuppressWarnings("deprecation")
		public void run() {
			try {
				while(true){
					// M25_Sever로 부터 명령 대기중...
					msg = buffr.readLine();
					String[] tmp = msg.split("리스트@@");
					if(tmp.length>1){
						String[] tmp2 = tmp[1].split(",");
						if(tmp[0].equals(ONLINE_LIST)){
							myMainUi.onlineList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(tmp2[i].trim().equals(people.getNickName())){tmp2[i] += "(나)";}
								myMainUi.onlineList.add(tmp2[i].trim());
							}
						}else if(tmp[0].equals(FILE_LIST)){
							myMainUi.fileList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(!tmp2[i].trim().equals(LIST_EMPTY) )	
								{ myMainUi.fileList.add(tmp2[i].trim()); }
							}// 리스트 반복 출력 끝

						}else if(tmp[0].equals(NOTICE_LIST)){
							myMainUi.noticeList.removeAll();
							for( int i=0; i<tmp2.length; i++ ){
								if(!tmp2[i].equals("null") )	
								{ myMainUi.noticeList.add(tmp2[i]);}
							}// 리스트 반복 출력 끝
						}
					}

				}// while end
			} catch (IOException e) {
				System.out.println("[클라이언트] 서버와의 메인연결 끉어짐");
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
			System.out.println("존재하지 않는 IP 입니다.");
			return false;
		} catch (IOException e) {
			System.out.println("서버가 아직 작동하지 않습니다.");
			return false;
		}
		System.out.println("소캣이 정상적으로 만들어졌습니다.");
		return true;
	}

	public boolean setNickName(){
		try {
			String msg = "";
			msg = myLoginUi.inputName.getText();
			osw.write(msg+"\n");
			osw.flush();
			String result = buffr.readLine();
			if(result.equals("성공")){
				myLoginUi.jf.setVisible(false);
				people.setNickName(msg);
				people.setSock(mainS);
				this.start();		// 서버와 소캣 연결 유지.
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
					System.out.println("[클라이언트] 소캣을 닫는도중에 에러");
				}
			}
		System.out.println("closeSocket");
		myLoginUi.jf.dispose();
		myMainUi.endframe();
		System.out.println("메인멈추기전");
		this.stop();
		System.exit(0);
	}// closeSocket()

}// M25_Client end
