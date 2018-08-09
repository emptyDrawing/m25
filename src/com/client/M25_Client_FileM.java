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
	
	// ���ε� 	: ���ε�	@�н�(�ø��� ���ϰ��+�̸�)	@pw + "\n"
	// �ٿ�ε� : �ٿ�ε�	@�н�(�ٿ�ε��ҳ� �̸���) 		+ "\n"  
	// ���� 	: ���ε�	@�н�(�����ҳ�+�̸�)		@pw + "\n"
	
	Socket socket;
	M25_Ui_Main gui;
	
	public M25_Client_FileM(M25_Ui_Main gui) {
		this.gui = gui;
	}

	public boolean uploadFile(String path, String fileName, String pw){
		// ��Ͽ��� ������ ��� ����
		File source = null;
		source = new File(path+"/"+fileName);
		byte[] buff = new byte[1024];
		FileInputStream 	fis = null;
		BufferedInputStream buffIns =null;
		
		// ��� �� �������.
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
			
			
			System.out.println("[���ϸŴ���] --> [���ϼ���] �� ���ٽõ�");
			
			// ��ɺ����� -- > �������� path���� ��� ¥���� ---> 
			// �������� �������� ����Ʈ ��� �����
			
			buffOuts.write(("���ε�@"+fileName+"@"+pw+"\n").getBytes());
			buffOuts.flush();
			
			// ��� �ޱ�
			String msg = buffr.readLine();
			if(!msg.equals("����")){
				return false;
			}
			
			//���ϰ���
			fis = new FileInputStream(source);
			buffIns = new BufferedInputStream(fis);

			System.out.println("[���ϸŴ���] ���������غ�");
			Date timec = new Date();
			int cont = 0;
			while(true){
				cont = buffIns.read(buff);
				if(cont == -1){break;}
				os.write(buff, 0, cont);
			}
			Date time2 = new Date();
			System.out.println("[���ϸŴ���] �������� :"+time2.compareTo(timec));
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
				System.out.println("[���ϸŴ���] ��Ĺ�� ����");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// ���ϸ�� �����ؾߵ�.
		
		return true;
	}
	
	public boolean downloadFile(String targetName, String deportPath , String deportName){
		// ��Ͽ��� ������ ���ϰ��+�̸� �� deportPath ���� ����
		// 
		byte[] buff = new byte[10];
		FileOutputStream 	fos 		= null;
		
		
		InputStream 		is 			= null;	// ��Ĺ���� ����
		InputStreamReader	isr			= null;	// ��ɺ����� ��
		BufferedReader 		buffr		= null; // ��ɺ����� ��
		
		BufferedInputStream buffIns 	= null; // ���Ϻ����� ��
		
		OutputStream 		os			= null;
		BufferedOutputStream buffOuts 	= null;
		
		File deportF =null;
		try {
			socket = new Socket(M25_Client.serverIP, M25_Client.serverFileP);
			
			// 1. ��� �������
			os = socket.getOutputStream();
			buffOuts = new BufferedOutputStream(os);
			
			// 2. ��� �ޱ��
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			buffr = new BufferedReader(isr);
			
			// ��ɺ�����
			buffOuts.write(("�ٿ�ε�@"+targetName+"\n").getBytes());
			buffOuts.flush();
			System.out.println("[���ϸŴ���] --> �ٿ�ε� ��û : "+targetName);
			// ��� �ޱ�
			String msg = buffr.readLine();
			if(msg.equals("���Ͼ���")){
				return false;
			}
			/// ���� �����غ�
			buffIns = new BufferedInputStream(is);

			deportF = new File(deportPath, deportName);
			fos = new FileOutputStream(deportF);
			buffOuts = new BufferedOutputStream(fos);
			
			System.out.println("[���ϸŴ���] --> �ٿ�ε� �غ�Ϸ� : "+deportName);
			// �����ϱ�
			int cont = 0;
			while(true){
				cont = buffIns.read(buff);
				if(cont == -1){break;}
				buffOuts.write(buff, 0, cont);
			}
			System.out.println("[���ϸŴ���] ���ϴٿ�ε峡 : "+deportPath+deportName);
			
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
				System.out.println("[���ϸŴ���] ��Ĺ ����");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// finally ��
		return true;
	}// downloadFile() end
	
	
	
	public int deleteFile(String fileName, String pw){	// -1 : ����   0 : �������� ���� ����   1 : ���� ����
		// �н������ ���ϰ�θ� ���� ����
		
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
			buffOs.write(("����"+"@"+fileName+"@"+pw+"\n").getBytes());
			buffOs.flush();
			sleep(100);
			System.out.println("[���ϸŴ���] --> ���� ��û : "+fileName);
			
			//
			String msg =buffr.readLine();
			System.out.println("[���ϸŴ���] --> ���� ��û��� : "+msg);
			if(msg.equals("��й�ȣƲ��"))	{ result = -1 ; }	// �н����� Ʋ��
			else if(msg.equals("���Ͼ���"))	{ result = 0; }	// ���������� ����
			else if(msg.equals("��������")) { result = 1; }
			// �������� ���� ����.
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
				System.out.println("[���ϸŴ���] ��Ĺ ����");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}//M25_Client_FileM
