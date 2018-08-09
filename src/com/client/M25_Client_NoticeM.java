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
	private M25_Ui_Notice gui;		// UI notice ����
	
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
	// Ŭ���̾�Ʈ�� �г��� ����������
	
	static public HashMap<String, String> pw_list;
	
	public M25_Client_NoticeM(M25_Ui_Notice gui, String nick){
		this.gui = gui;
		this.nickName = nick;
	}
	
	public void sendNotice(M25_Notice tmp, String msg){			// ��� ������
		
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
				osw.write("�ۼ�##"	+				
						notice.getNoticeTilte()
						+"@@@@"+notice.getNoticeContent()
						+"@@@@"+notice.getNoticePW()
						+"@@@@"+notice.getNoticeTime()
						+"@@@@"+nickName+"@@@@");
				osw.flush();
			}else if(msg == "modify"){
				osw.write("����##"	+	
						notice.getNoticeTilte()
						+"@@@@"+notice.getNoticeContent()
						+"@@@@"+notice.getNoticePW()
						+"@@@@"+notice.getNoticeTime()
						+"@@@@"+nickName+"@@@@");
				osw.flush();
			}
			System.out.println("[�����Ŵ���] ��������");
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
				System.out.println("[�����Ŵ���] ��Ĺ�� ����");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // 18:22�߰�
	}
	
	
	public void loadNotice(M25_Notice tmp, String a){			// ��� ������		
		
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
			//������ �ƴҽÿ� ����
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
		
		//�߰� 21:04
		if(!commands[0].equals("������ ���Դϴ�.")){
			pw = commands[3];
			gui.noticeTitle.setText(commands[0]);
			gui.noticeContent.setText(commands[1]);			
		}else{
			JOptionPane.showMessageDialog(null, "�̹� ������ �Խù��Դϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			M25_Ui_Notice.jf.dispose();
		}
	}

	
} // class end