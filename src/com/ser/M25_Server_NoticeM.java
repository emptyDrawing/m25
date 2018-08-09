package com.ser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.pub.M25_Notice;


public class M25_Server_NoticeM extends Thread {

	private M25_Server parentS;
	private int  port;
	private ServerSocket servSock;
	private Socket socket;
	
	private int index = 0;
	
	private Object synKey = new Object();
	
	static int key = 0;
	String[] commands;
	String[] commands2;
	
	// int index
	public HashMap<Integer, String> notice_list;

	public M25_Server_NoticeM(int serverNotitcP, M25_Server me) {
		this.parentS = me;
		this.port = serverNotitcP;
		notice_list = new HashMap<Integer, String>();
		
	}


	public void run(){
		try {
			servSock = new ServerSocket(port);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		while(true)
		{
			learnS();
		}	
		
	}

	private void learnS() {
		try {
			Socket sock = servSock.accept();
			this.new M25_Server_NoticeM_Runner(sock).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}	

	class M25_Server_NoticeM_Runner extends Thread{
		
		Socket sock ;
		OutputStream os ;
		
		InputStream is ;
		InputStreamReader isr;
		BufferedReader buffr;
		BufferedInputStream bis;
		BufferedWriter bw;
		
		M25_Notice myNotice;
		OutputStreamWriter osw;
		
		int cnt = 0;
		
		///////////18:06추가
		public M25_Server_NoticeM_Runner(Socket sock2) {
			this.sock = sock2;
			try {
				System.out.println("공지전송 명령보낸 소캣: "+sock);
				this.os = sock.getOutputStream();
				this.is = sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String msg;
			int cnt = 0;
			
			try {
				is = sock.getInputStream();
				isr = new InputStreamReader(is);
				buffr = new BufferedReader(isr);	
				
				os = sock.getOutputStream();
				osw = new OutputStreamWriter(os);	
				
				
				int cont=0;
				char[] mychars = new char[50];
				StringBuffer mystring = new StringBuffer();
				
				
				while(true){
					cont = buffr.read(mychars);
					System.out.println("읽음");
					mystring.append(mychars, 0, cont);	// cont수정요망
					
					if(buffr.ready()){
					}else{
						System.out.println("break");
						break;												
					}
				}
				command(mystring.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			sendNoticelist();
		}// run() end
		
		private void command(String msg) {
			System.out.println("command의 msg        "+msg);
			commands = msg.split("@@@@");
			
			
			System.out.println("commands" + commands[0]+"    commands의 갯수   "+commands.length);
			if(commands.length == 5){							
				saveNotice();
			}else if(commands.length == 1){		
				System.out.println("loadnotice");
				loadNotice(commands[0]);
			}		
		}

		private void saveNotice(){
			myNotice = new M25_Notice();
			commands2 = commands[0].split("##");
			if(commands2[0].equals("작성")){
				myNotice.setNoticeTilte(commands2[1]);
				myNotice.setNoticeContent(commands[1]);
				myNotice.setNoticePW(commands[2]);
				myNotice.setNoticeTime(commands[3]);
				myNotice.setNoticeOwner(commands[4]);
				cnt++;
				synchronized (synKey) {
					myNotice.setIndexNum(index);
					//1~5 , 6~10
					notice_list.put(index++, myNotice.getNoticeTilte());
					notice_list.put(index++, myNotice.getNoticeContent());
					notice_list.put(index++, myNotice.getNoticeOwner());
					notice_list.put(index++, myNotice.getNoticePW());
					notice_list.put(index++, myNotice.getNoticeTime());
				}			
			}else if(commands2[0].equals("수정")){
				myNotice.setNoticeTilte(commands2[1]);
				myNotice.setNoticeContent(commands[1]);
				myNotice.setNoticePW(commands[2]);
				myNotice.setNoticeTime(commands[3]);
				myNotice.setNoticeOwner(commands[4]);
				cnt++;
				synchronized (synKey) {
					myNotice.setIndexNum(index);
					//1~5 , 6~10
					
					notice_list.put(key++, myNotice.getNoticeTilte());
					notice_list.put(key++, myNotice.getNoticeContent());
					notice_list.put(key++, myNotice.getNoticeOwner());
					notice_list.put(key++, myNotice.getNoticePW());
					notice_list.put(key++, myNotice.getNoticeTime());
				}	
			}
			sendNoticelist();
		}		
		
		
		
		private void loadNotice(String commands){
			
			System.out.println("서버매니저 - 로드 명령 받음");
			String[] commands3;
			commands3 = commands.split("\\$\\$");
			
			for (int i = 0; i < notice_list.size()-1; i++) {
				System.out.println(notice_list.get(i));
				if(notice_list.get(i).equals(commands)){
					key = i;
				}
			}			
			if(commands3[0].equals("삭제")){
				cnt++;
				synchronized (synKey) {
//					myNotice.setIndexNum(index);
					//1~5 , 6~10
//					notice_list.remove(key);
					notice_list.put(key++, "삭제된 글입니다.");
					notice_list.put(key++, "");
					notice_list.put(key++, "");
					notice_list.put(key++, "");
					notice_list.put(key++, "");
				}	
				System.out.println("삭제완료");
				try {
					osw.write(notice_list.get(key).toString()+"@@@@"+
							notice_list.get(key+1).toString()+"@@@@"+
							notice_list.get(key+2).toString()+"@@@@"+
							notice_list.get(key+3).toString()+"@@@@"+
							notice_list.get(key+4).toString()+"@@@@");
					osw.flush();					
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					try {
						osw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				sendNoticelist();
			}else{
				try {
					osw.write(
							notice_list.get(key).toString()+"@@@@"+
									notice_list.get(key+1).toString()+"@@@@"+
									notice_list.get(key+2).toString()+"@@@@"+
									notice_list.get(key+3).toString()+"@@@@"+
									notice_list.get(key+4).toString()+"@@@@");
					osw.flush();
					System.out.println("작성완료---"+notice_list.get(key).toString()+
							notice_list.get(key+1).toString()+
							notice_list.get(key+2).toString()+
							notice_list.get(key+3).toString()+
							notice_list.get(key+4).toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						osw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}		
				
			}
			
			
		}
		
		
	}// NoticeM
	
	public void sendNoticelist() {
		// TODO Auto-generated method stub
		parentS.sendList(M25_Server.NOTICE_LIST);
	}
}// M25_Server_NoticeM	
