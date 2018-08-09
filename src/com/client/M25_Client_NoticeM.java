package com.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.client.ui.M25_Ui_Notice;
import com.pub.M25_Notice;


public class M25_Client_NoticeM extends Thread {

	private Socket sock;
	private M25_Ui_Notice gui;		// UI notice 정보
	
	private String nickName;
	
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader buffr;
	
	private OutputStream os;
	OutputStreamWriter osw;
	BufferedOutputStream buffOs ;
	String[] commands;	
	static public String pw = "";
	String tmp3;
	// 클라이언트의 닉네임 받을수있음
	
	static public HashMap<String, String> pw_list;
	
	public M25_Client_NoticeM(M25_Ui_Notice gui, String nick){
		this.gui = gui;
		this.nickName = nick;
	}
	
	public void sendNotice(M25_Notice tmp, String msg){			// 명령 보내기
		
		M25_Notice notice = tmp;		
		pw_list = new HashMap<String, String>();
		try {
			sock = new Socket(M25_Client.serverIP, M25_Client.serverNotitcP);
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);			
			os = sock.getOutputStream();
			buffOs = new BufferedOutputStream(os);
			osw = new OutputStreamWriter(os);
			
			pw_list.put(notice.getNoticePW(), notice.getNoticeTilte());
			if(msg == "write"){
				osw.write("작성##"	+				
						notice.getNoticeTilte()
						+"@@@@"+notice.getNoticeContent()
						+"@@@@"+notice.getNoticePW()
						+"@@@@"+notice.getNoticeTime()
						+"@@@@"+nickName+"@@@@");
				osw.flush();
			}else if(msg == "modify"){
				osw.write("수정##"	+	
						notice.getNoticeTilte()
						+"@@@@"+notice.getNoticeContent()
						+"@@@@"+notice.getNoticePW()
						+"@@@@"+notice.getNoticeTime()
						+"@@@@"+nickName+"@@@@");
				osw.flush();
			}
			System.out.println("[공지매니저] 공지전송");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				osw.close();
				buffOs.close();
				os.close();
				buffr.close();
				isr.close();
				is.close();
				sock.close();
				System.out.println("[공지매니저] 소캣도 종료");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // 18:22추가
	}
	
	
	public void loadNotice(M25_Notice tmp, String a){			// 명령 보내기		
		
		M25_Notice notice = tmp;		
		try {
			sock = new Socket(M25_Client.serverIP, M25_Client.serverNotitcP);
			is = sock.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);			
			os = sock.getOutputStream();
			buffOs = new BufferedOutputStream(os);
			osw = new OutputStreamWriter(os);
			String temps[];
			String msg2;
			msg2 = notice.getNoticeTilte();
			temps = msg2.split("\\$\\$");
			
			if(temps.length == 2){
				osw.write(					
						notice.getNoticeTilte()
						);
				
				osw.flush();				
			}else{
				osw.write(					
						notice.getNoticeTilte()
						);
				osw.flush();			
				
			}
			int cont=0;
			char[] mychars = new char[50];
			StringBuffer mystring = new StringBuffer();
			
			while(true){
				cont = buffr.read(mychars);
				if(a.equals("delete")){
					break;
				}
				mystring.append(mychars, 0, cont);
				
				if(buffr.ready()){
				}else{
					System.out.println("break");
					break;												
				}
			}
			//삭제가 아닐시에 실행
			tmp3 = mystring.toString();
				if(mystring.toString().equals("")){
					
				}else{
					loadOutput(mystring.toString());					
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				osw.close();
				buffOs.close();
				os.close();
				buffr.close();
				isr.close();
				is.close();
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadOutput (String string){
		commands = string.split("@@@@");
		
		//추가 21:04
		if(!commands[0].equals("삭제된 글입니다.")){
			pw = commands[3];
			gui.noticeTitle.setText(commands[0]);
			gui.noticeContent.setText(commands[1]);			
		}else{
			JOptionPane.showMessageDialog(null, "이미 삭제된 게시물입니다.", "error", JOptionPane.WARNING_MESSAGE);
			M25_Ui_Notice.jf.dispose();
		}
	}

	
} // class end