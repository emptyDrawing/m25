package com.ser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;


public class M25_Server_FileM extends Thread {
	// 업로드 	: 업로드	@패스(올릴놈 이름만 받았음+이름)	@pw
	// 다운로드 : 다운로드	@패스(다운로드할놈 이름만 받았음)   
	// 삭제 	: 업로드	@패스(삭제할놈+이름)				@pw    

	private M25_Server upper;
	private int  portC;
//	private int  portF;

	private ServerSocket servSockC;
//	private ServerSocket servSockF;

	// 파일이름 - pw
	HashMap<String, String> file_list; // 파일이름 / pw

	static private Object synFkey = new Object();
	private static  String DEFAULT_PATH = "./data/";

	public M25_Server_FileM(int fileCP, int fileFP, M25_Server me) {
		this.upper = me;
		this.portC = fileCP;
		//	this.portF = fileFP;
		file_list = new HashMap<String, String>();
	}

	public void run(){
//		setting();
		learnS();
	}

	private void setting() {
		// file_list 셋팅하기
		File tmp = new File(M25_Server.DEFAULT_FILE_LIST); 
		if(tmp.exists()){
			getlistFromFile(tmp);
			
		}
		
	
	}

	private void getlistFromFile(File listSrc) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader buffr = null;
		StringBuffer tmpSBuff = new StringBuffer();
		try {
			fis = new  FileInputStream(listSrc);
			isr = new InputStreamReader(fis);
			buffr = new BufferedReader(isr);
	
			char[] mybuff = new char[10];
			
			int cont = 0;
			while(true){
				cont = buffr.read(mybuff, 0, cont);
				if(cont == -1){ System.out.println("[파일서버] -> 파일리스트파일 부터 읽기 성공"); break; }
				tmpSBuff.append(Arrays.copyOf(mybuff, cont));
			}
		
		} catch (FileNotFoundException e) {
			System.out.println("[파일서버] -> 파일리스트파일 없음");
		} catch (IOException e) {
			System.out.println("[파일서버] -> 파일리스트파일 읽기 실패");
		}
		
		System.out.println();
		
	}

	private void learnS() {
		try {
			servSockC = new ServerSocket(portC);
			System.out.println("[파일서버] 준비완료.");
			while(true){
				Socket sock = servSockC.accept();
				M25_Server_FileM_Runner_C runner = new M25_Server_FileM_Runner_C(sock);
				runner.start();
			}
		} catch (IOException e) {
			System.out.println("[파일서버] -소캣연결에서 문제 발생");
			e.printStackTrace();
		}
	}	

	public void sendFilelist(){
		upper.sendList(M25_Server.FILE_LIST);
	}
	
	
	
	
	
	
	
	// 접속 받고 바로 돌아가는놈.
	class M25_Server_FileM_Runner_C extends Thread{

		Socket sock ;
		File tmpFile;
		String tmpPw;
		
		OutputStream os_c;
		InputStream is_c;
		
		InputStreamReader isr_c;
		BufferedReader buffr_c;

		String[] commands = null;
		byte[] myByts = new byte[1024];

		public M25_Server_FileM_Runner_C(Socket sock2) {
			this.sock = sock2;
			try {
				System.out.println("파일전송 명령보낸 소캣: "+sock);
				this.os_c = sock.getOutputStream();
				this.is_c = sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// 생성자 end

		@Override
		public void run() {
			isr_c = new InputStreamReader(is_c);
			buffr_c = new BufferedReader(isr_c);

			try {
				String msg = buffr_c.readLine();
				System.out.println(sock.getInetAddress()+"이 보낸 메세지 : [파일서버] <-- "+msg);
				command(msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}// run() end

		private void command(String msg) {

			commands = msg.split("@");
			if(commands[0].equals("업로드")){
				saveFile();
			}else if(commands[0].equals("다운로드")){
				sendFile();
			}else if(commands[0].equals("삭제")){
				deleteFile();
			}
		}// command() end

		// 업로드 	: 업로드	@패스(올릴놈 이름만 받았음+이름)	@pw
		private void saveFile(){
			BufferedInputStream buffInS	  = null;
			FileOutputStream fos		  = null;
			BufferedOutputStream buffOutS = null;
			int cont = 0;
			try {
				os_c.write("성공\n".getBytes());
				buffInS = new BufferedInputStream(is_c);

				tmpFile = new File(DEFAULT_PATH, commands[1].trim());
				fos = new FileOutputStream(tmpFile);
				buffOutS = new BufferedOutputStream(fos);
				
				synchronized (synFkey) {
					while(true){
						cont = buffInS.read(myByts);
						if(cont==-1){System.out.println("[파일서버] ["+tmpFile.getName()+"] 업로드끝");break;}
						buffOutS.write(myByts, 0, cont);
					}
					file_list.put(tmpFile.getName(), commands[2]);
				}// 싱크로 부분
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					buffInS.close();
					buffOutS.close();
					fos.close();
					sendFilelist();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}// 파일전송 끝

		}//saveFile() end

		// 다운로드 : 다운로드	@패스(다운로드할놈 이름만 받았음)   
		private void sendFile(){
			
			tmpFile = new File(DEFAULT_PATH, commands[1]);
			
			// 파일이 없으면 false 리턴
			if(!tmpFile.exists()){
				try {
					os_c.write("파일없음\n".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileInputStream fis = null;
			BufferedInputStream buffIns  = null;
			try {
				os_c.write("성공\n".getBytes());
				fis = new FileInputStream(tmpFile);
				buffIns = new BufferedInputStream(fis);
//				System.out.println("파일 보내기 준비 끝");
				
				int cont = 0;
				synchronized (synFkey) {
					while(true){
						cont = buffIns.read(myByts);
//						System.out.println("파일전송중");
						if(cont==-1){System.out.println("[파일서버] ["+tmpFile.getName()+"] 업로드끝");break;}
						os_c.write(myByts, 0, cont);
					}
				}// 싱크로 부분
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					buffIns.close();
					fis.close();
					os_c.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}// 파일전송 끝
		}// sendFile() end


		// 삭제 	: 업로드	@패스(삭제할놈+이름)				@pw    
		private void deleteFile(){ // 테스트 해야됨.
			tmpFile = new File(DEFAULT_PATH, commands[1]);

			if(!tmpFile.exists()){// 파일이 없으면 false 리턴
				try {
					System.out.println("[파일서버] ["+tmpFile.getName()+"] 삭제요청 실패 : 파일 없음");
					os_c.write("파일없음\n".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				boolean result = file_list.get(commands[1]).equals(commands[2]);
				if(result){
					synchronized (synFkey) {
						file_list.remove(tmpFile.getName());
						tmpFile.delete();
					}
					try {
						System.out.println("[파일서버] ["+tmpFile.getName()+"] 삭제 끝");
						os_c.write("삭제성공\n".getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{// 비번 틀릴때
					try {
						System.out.println("[파일서버] ["+tmpFile.getName()+"] 삭제요청 실패 : 비밀번호 틀림");
						os_c.write("비밀번호틀림\n".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sendFilelist();
			}
		}// deleteFile() end

	}// M25_Server_FileM_Runner_C end

}// M25_Server_FileM end
