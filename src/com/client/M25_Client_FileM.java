package com.client;

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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import com.client.ui.M25_Ui_Main;

public class M25_Client_FileM extends Thread{
	
	// 업로드 	: 업로드	@패스(올릴놈 파일경로+이름)	@pw + "\n"
	// 다운로드 : 다운로드	@패스(다운로드할놈 이름만) 		+ "\n"  
	// 삭제 	: 업로드	@패스(삭제할놈+이름)		@pw + "\n"
	
	Socket socket;
	M25_Ui_Main gui;
	
	public M25_Client_FileM(M25_Ui_Main gui) {
		this.gui = gui;
	}

	public boolean uploadFile(String path, String fileName, String pw){
		// 목록에서 파일을 경로 받음
		File source = null;
		source = new File(path+"/"+fileName);
		byte[] buff = new byte[1024];
		FileInputStream 	fis = null;
		BufferedInputStream buffIns =null;
		
		// 명령 및 보내기용.
		OutputStream os = null;
		BufferedOutputStream buffOuts = null;
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader buffr = null;
		
		try {
			socket = new Socket(M25_Client.serverIP, M25_Client.serverFileP);

			is = socket.getInputStream();
			
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);
			
			os = socket.getOutputStream();
			buffOuts = new BufferedOutputStream(os);
			
			
			System.out.println("[파일매니저] --> [파일서버] 로 접근시도");
			
			// 명령보내기 -- > 보낼때는 path에서 경로 짜르고 ---> 
			// 서버에서 받을때는 디폴트 경로 만들고
			
			buffOuts.write(("업로드@"+fileName+"@"+pw+"\n").getBytes());
			buffOuts.flush();
			
			// 명령 받기
			String msg = buffr.readLine();
			if(!msg.equals("성공")){
				return false;
			}
			
			//파일관련
			fis = new FileInputStream(source);
			buffIns = new BufferedInputStream(fis);

			System.out.println("[파일매니저] 파일전송준비");
			Date timec = new Date();
			int cont = 0;
			while(true){
				cont = buffIns.read(buff);
				if(cont == -1){break;}
				os.write(buff, 0, cont);
			}
			Date time2 = new Date();
			System.out.println("[파일매니저] 파일전송 :"+time2.compareTo(timec));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				buffIns.close();
				fis.close();
				buffOuts.close();
				os.close();
				socket.close();
				System.out.println("[파일매니저] 소캣도 종료");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 파일목록 정리해야됨.
		
		return true;
	}
	
	public boolean downloadFile(String targetName, String deportPath , String deportName){
		// 목록에서 저장할 파일경로+이름 을 deportPath 에서 받음
		// 
		byte[] buff = new byte[10];
		FileOutputStream 	fos 		= null;
		
		
		InputStream 		is 			= null;	// 소캣에게 받음
		InputStreamReader	isr			= null;	// 명령보낼때 씀
		BufferedReader 		buffr		= null; // 명령보낼때 씀
		
		BufferedInputStream buffIns 	= null; // 파일보낼때 씀
		
		OutputStream 		os			= null;
		BufferedOutputStream buffOuts 	= null;
		
		File deportF =null;
		try {
			socket = new Socket(M25_Client.serverIP, M25_Client.serverFileP);
			
			// 1. 명령 보내기용
			os = socket.getOutputStream();
			buffOuts = new BufferedOutputStream(os);
			
			// 2. 명령 받기용
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);
			
			// 명령보내기
			buffOuts.write(("다운로드@"+targetName+"\n").getBytes());
			buffOuts.flush();
			System.out.println("[파일매니저] --> 다운로드 요청 : "+targetName);
			// 명령 받기
			String msg = buffr.readLine();
			if(msg.equals("파일없음")){
				return false;
			}
			/// 파일 받을준비
			buffIns = new BufferedInputStream(is);

			deportF = new File(deportPath, deportName);
			fos = new FileOutputStream(deportF);
			buffOuts = new BufferedOutputStream(fos);
			
			System.out.println("[파일매니저] --> 다운로드 준비완료 : "+deportName);
			// 수행하기
			int cont = 0;
			while(true){
				cont = buffIns.read(buff);
				if(cont == -1){break;}
				buffOuts.write(buff, 0, cont);
			}
			System.out.println("[파일매니저] 파일다운로드끝 : "+deportPath+deportName);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				buffIns.close();
				is.close();
				buffOuts.close();
				fos.close();
				socket.close();
				System.out.println("[파일매니저] 소캣 종료");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// finally 음
		return true;
	}// downloadFile() end
	
	
	
	public int deleteFile(String fileName, String pw){	// -1 : 실패   0 : 서버에서 삭제 오류   1 : 삭제 성공
		// 패스워드와 파일경로를 전달 받음
		
		OutputStream 	os = null;
		BufferedOutputStream buffOs = null;
		
		InputStream 	is = null;
		InputStreamReader isr = null;
		BufferedReader  buffr = null;
		int result = 10;
		try {
			socket = new Socket(M25_Client.serverIP, M25_Client.serverFileP);
			os = socket.getOutputStream();
			buffOs = new BufferedOutputStream(os);
			
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);
			
			//
			buffOs.write(("삭제"+"@"+fileName+"@"+pw+"\n").getBytes());
			buffOs.flush();
			sleep(100);
			System.out.println("[파일매니저] --> 삭제 요청 : "+fileName);
			
			//
			String msg =buffr.readLine();
			System.out.println("[파일매니저] --> 삭제 요청결과 : "+msg);
			if(msg.equals("비밀번호틀림"))	{ result = -1 ; }	// 패스워드 틀림
			else if(msg.equals("파일없음"))	{ result = 0; }	// 서버에서의 오류
			else if(msg.equals("삭제성공")) { result = 1; }
			// 서버에서 삭제 성공.
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				buffr.close();
				isr.close();
				is.close();
				os.close();
				socket.close();
				System.out.println("[파일매니저] 소캣 종료");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}//M25_Client_FileM
