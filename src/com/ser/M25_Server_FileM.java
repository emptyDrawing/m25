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
	// ���ε� 	: ���ε�	@�н�(�ø��� �̸��� �޾���+�̸�)	@pw
	// �ٿ�ε� : �ٿ�ε�	@�н�(�ٿ�ε��ҳ� �̸��� �޾���)   
	// ���� 	: ���ε�	@�н�(�����ҳ�+�̸�)				@pw    

	private M25_Server upper;
	private int  portC;
//	private int  portF;

	private ServerSocket servSockC;
//	private ServerSocket servSockF;

	// �����̸� - pw
	HashMap<String, String> file_list; // �����̸� / pw

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
		// file_list �����ϱ�
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
				if(cont == -1){ System.out.println("[���ϼ���] -> ���ϸ���Ʈ���� ���� �б� ����"); break; }
				tmpSBuff.append(Arrays.copyOf(mybuff, cont));
			}
		
		} catch (FileNotFoundException e) {
			System.out.println("[���ϼ���] -> ���ϸ���Ʈ���� ����");
		} catch (IOException e) {
			System.out.println("[���ϼ���] -> ���ϸ���Ʈ���� �б� ����");
		}
		
		System.out.println();
		
	}

	private void learnS() {
		try {
			servSockC = new ServerSocket(portC);
			System.out.println("[���ϼ���] �غ�Ϸ�.");
			while(true){
				Socket sock = servSockC.accept();
				M25_Server_FileM_Runner_C runner = new M25_Server_FileM_Runner_C(sock);
				runner.start();
			}
		} catch (IOException e) {
			System.out.println("[���ϼ���] -��Ĺ���ῡ�� ���� �߻�");
			e.printStackTrace();
		}
	}	

	public void sendFilelist(){
		upper.sendList(M25_Server.FILE_LIST);
	}
	
	
	
	
	
	
	
	// ���� �ް� �ٷ� ���ư��³�.
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
				System.out.println("�������� ��ɺ��� ��Ĺ: "+sock);
				this.os_c = sock.getOutputStream();
				this.is_c = sock.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// ������ end

		@Override
		public void run() {
			isr_c = new InputStreamReader(is_c);
			buffr_c = new BufferedReader(isr_c);

			try {
				String msg = buffr_c.readLine();
				System.out.println(sock.getInetAddress()+"�� ���� �޼��� : [���ϼ���] <-- "+msg);
				command(msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}// run() end

		private void command(String msg) {

			commands = msg.split("@");
			if(commands[0].equals("���ε�")){
				saveFile();
			}else if(commands[0].equals("�ٿ�ε�")){
				sendFile();
			}else if(commands[0].equals("����")){
				deleteFile();
			}
		}// command() end

		// ���ε� 	: ���ε�	@�н�(�ø��� �̸��� �޾���+�̸�)	@pw
		private void saveFile(){
			BufferedInputStream buffInS	  = null;
			FileOutputStream fos		  = null;
			BufferedOutputStream buffOutS = null;
			int cont = 0;
			try {
				os_c.write("����\n".getBytes());
				buffInS = new BufferedInputStream(is_c);

				tmpFile = new File(DEFAULT_PATH, commands[1].trim());
				fos = new FileOutputStream(tmpFile);
				buffOutS = new BufferedOutputStream(fos);
				
				synchronized (synFkey) {
					while(true){
						cont = buffInS.read(myByts);
						if(cont==-1){System.out.println("[���ϼ���] ["+tmpFile.getName()+"] ���ε峡");break;}
						buffOutS.write(myByts, 0, cont);
					}
					file_list.put(tmpFile.getName(), commands[2]);
				}// ��ũ�� �κ�
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
			}// �������� ��

		}//saveFile() end

		// �ٿ�ε� : �ٿ�ε�	@�н�(�ٿ�ε��ҳ� �̸��� �޾���)   
		private void sendFile(){
			
			tmpFile = new File(DEFAULT_PATH, commands[1]);
			
			// ������ ������ false ����
			if(!tmpFile.exists()){
				try {
					os_c.write("���Ͼ���\n".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileInputStream fis = null;
			BufferedInputStream buffIns  = null;
			try {
				os_c.write("����\n".getBytes());
				fis = new FileInputStream(tmpFile);
				buffIns = new BufferedInputStream(fis);
//				System.out.println("���� ������ �غ� ��");
				
				int cont = 0;
				synchronized (synFkey) {
					while(true){
						cont = buffIns.read(myByts);
//						System.out.println("����������");
						if(cont==-1){System.out.println("[���ϼ���] ["+tmpFile.getName()+"] ���ε峡");break;}
						os_c.write(myByts, 0, cont);
					}
				}// ��ũ�� �κ�
				
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
			}// �������� ��
		}// sendFile() end


		// ���� 	: ���ε�	@�н�(�����ҳ�+�̸�)				@pw    
		private void deleteFile(){ // �׽�Ʈ �ؾߵ�.
			tmpFile = new File(DEFAULT_PATH, commands[1]);

			if(!tmpFile.exists()){// ������ ������ false ����
				try {
					System.out.println("[���ϼ���] ["+tmpFile.getName()+"] ������û ���� : ���� ����");
					os_c.write("���Ͼ���\n".getBytes());
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
						System.out.println("[���ϼ���] ["+tmpFile.getName()+"] ���� ��");
						os_c.write("��������\n".getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{// ��� Ʋ����
					try {
						System.out.println("[���ϼ���] ["+tmpFile.getName()+"] ������û ���� : ��й�ȣ Ʋ��");
						os_c.write("��й�ȣƲ��\n".getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sendFilelist();
			}
		}// deleteFile() end

	}// M25_Server_FileM_Runner_C end

}// M25_Server_FileM end
