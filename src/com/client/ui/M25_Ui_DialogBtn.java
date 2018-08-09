package com.client.ui;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.client.M25_Client;
import com.client.M25_Client_NoticeM;


public class M25_Ui_DialogBtn extends JFrame {
	
	Dialog check;
	Label text = null;
	Button yBtn;
	Button nBtn;
	TextField passwordInput;
	String tempMsg;
	static String testMsg;
	
	
	// Y or N : �α���, ä�ù� ����, ä�ù� ����, ���� �ٿ�ε� Ȯ��, ���α׷� ����, �Խ��� �ۼ����,
	// Input : ���� ���� Ȯ��, �Խ��� ���ۼ�, �Խ��� �ۼ���, �Խ��� �ۻ���, ���� ���ε� Ȯ��	
	// only msg : �� ���� �Ϸ�, �� ����&�ۼ� �Ϸ�, ���� ���ε� �Ϸ�, ���� ���� �Ϸ�, �г��� �ߺ�üũ	
	//JOptionPane. YES(int 0) / OK / NO(int 1) / CANCEL / CLOSED
	
	int checkLogin;
	int checkChatJoin;
	int checkChatExit; 
	int checkFileDownload; 
	int checkProgramExit; 
	int checkWriteCancel; 
	int checkWriteWinCancel; 
	
	String checkFileDelete;
	static String checkWrite;
	String checkModify;
	String checkUpload;
	String checkDelete;
	
	public M25_Ui_DialogBtn(String a) {		
		
		if(a == "checkLogin"){// �α��� üũ ��Ȳ
			if(M25_Ui_Login.inputName.getText().equals("") ) {JOptionPane.showMessageDialog(null, "�г����� �����ּ���", "error", JOptionPane.WARNING_MESSAGE); return;}
		
			
			checkLogin = JOptionPane.showConfirmDialog(null, "�г��� [ "+M25_Ui_Login.inputName.getText()+" ]\n�α��� �Ͻðڽ��ϱ�?", "Login", JOptionPane.YES_NO_OPTION);
			if(checkLogin==0){
				if(M25_Ui_Login.inputSevIp.getText().equals("") ) {JOptionPane.showMessageDialog(null, "������ IP�� �Է����ּ���", "error", JOptionPane.WARNING_MESSAGE); return;}
				boolean servCheck = M25_Ui_Login.actor.setSock(M25_Ui_Login.inputSevIp.getText());
				if(servCheck){
					boolean nickCheck = M25_Ui_Login.actor.setNickName();
					if(nickCheck==false){	JOptionPane.showMessageDialog(null, "�ߺ��� �г����Դϴ�.", "error", JOptionPane.WARNING_MESSAGE);}
				}
				else{
					JOptionPane.showMessageDialog(null, "������ ���� �غ���� �ʾҽ��ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		}else if(a == "checkChatJoin"){
			checkChatJoin = JOptionPane.showConfirmDialog(null, "ä�ù濡 ���� �Ͻðڽ��ϱ�?", "Chat", JOptionPane.YES_NO_OPTION);
			if(checkChatJoin == JOptionPane.YES_OPTION){
			}
			
		}else if(a == "checkChatExit"){
			checkChatExit = JOptionPane.showConfirmDialog(this, "ä���� ���� �Ͻðڽ��ϱ�?", "Exit", JOptionPane.YES_NO_OPTION);
			if(checkChatExit == JOptionPane.YES_OPTION){
				M25_Ui_chatRoom.actor.sendMessage(M25_Client.SOCK_CLOSE);
				M25_Ui_chatRoom.actor.closeChatRoom();		
			}
			
		}else if(a == "download"){
			Object[] options = {"�ٿ�ε�", "����", "���"};
			checkFileDownload = JOptionPane.showOptionDialog(null, 
					"["+M25_Ui_Main.testMsg_Down+"]�� �ٿ�ε� �Ͻðڽ��ϱ�?", "Download", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,  options[0]	);
			if(checkFileDownload == JOptionPane.YES_OPTION){
				M25_Ui_Main.fileSave();
			}
			if(checkFileDownload == JOptionPane.NO_OPTION){
				String checkPw = JOptionPane.showInputDialog(null, "�����Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Delete", JOptionPane.INFORMATION_MESSAGE);
				if(checkPw.equals("")){
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���.", "error", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int tmp = M25_Ui_Main.fileM.deleteFile(M25_Ui_Main.testMsg_Down, checkPw);
				if (tmp==1){
					JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�", "complete", JOptionPane.INFORMATION_MESSAGE);
				}else if(tmp==0){
					JOptionPane.showMessageDialog(null, "������ ������ �����ϴ�.", "error", JOptionPane.INFORMATION_MESSAGE);
				}else if(tmp==-1){
					JOptionPane.showMessageDialog(null, "��й�ȣ�� Ʋ�Ƚ��ϴ�.", "error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			if(checkFileDownload == JOptionPane.CANCEL_OPTION){
				return;
			}
			
			
			
		}else if(a == "checkProgramExit"){
			checkProgramExit = JOptionPane.showConfirmDialog(null, "���� �Ͻðڽ��ϱ�?", "Exit", JOptionPane.YES_NO_OPTION);
			if(checkProgramExit == JOptionPane.YES_OPTION){
				M25_Ui_Main.actor.closeSocket();
			}
			
		}else if(a == "checkWriteCancel"){
			checkWriteCancel = JOptionPane.showConfirmDialog(null, "�ۼ����� ������ �����˴ϴ�.", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(checkWriteCancel == JOptionPane.YES_OPTION){
				M25_Ui_Notice.textTitle.setText("");
				M25_Ui_Notice.textContent.setText("");
				M25_Ui_Notice.jf.dispose();
			}
			
		}else if(a == "checkWriteWinCancel"){
			checkWriteWinCancel = JOptionPane.showConfirmDialog(this, "�ۼ����� ������ �����˴ϴ�.", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(checkWriteWinCancel == JOptionPane.YES_OPTION){
				M25_Ui_Notice.jf.dispose();
			}
			
		}else if(a == "checkFileDelete"){
			checkFileDelete = JOptionPane.showInputDialog(null, "�����Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Delete", JOptionPane.WARNING_MESSAGE);
			if(checkFileDelete == "password"){
//				M25_Ui_Main.noticeList.remove(M25_Ui_Main.testMsg_Down);        �̰� ���� �𸣰���
				JOptionPane.showMessageDialog(null, "������ �Ϸ� �Ǿ����ϴ�.", "complete", JOptionPane.INFORMATION_MESSAGE);
				M25_Ui_Notice.jf.dispose();
			}else{
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �ùٸ��� �ʰų�\n����ڰ� ����Ͽ����ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}
			
		}else if(a == "checkDelete"){
			checkDelete = JOptionPane.showInputDialog(null, "�����Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Delete", JOptionPane.INFORMATION_MESSAGE);
			try {
				if(M25_Client_NoticeM.pw_list.get(checkDelete) == null){
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �ùٸ��� �ʰų�\n������� ��� �Ǵ� �۾��̰� ����ġ�մϴ�.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					return;
				}
				if(M25_Ui_Notice.temp.equals(M25_Client_NoticeM.pw_list.get(checkDelete))){
	//			if(checkDelete.equals(M25_Client_NoticeM.pw)){
					JOptionPane.showMessageDialog(null, "������ �Ϸ� �Ǿ����ϴ�.", "complete", JOptionPane.INFORMATION_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					new M25_Ui_Notice("delete", M25_Ui_Notice.temp);
					M25_Ui_Notice.jf.dispose();
				}else{
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �ùٸ��� �ʰų�\n������� ��� �Ǵ� �۾��̰� ����ġ�մϴ�.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
				}
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "�ش� ����ڸ� �� ������ �����մϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}
		}else if(a == "checkWrite"){
			
			checkWrite = JOptionPane.showInputDialog(null, "�ۼ��Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Write", JOptionPane.INFORMATION_MESSAGE);
			if(checkWrite != null){
				JOptionPane.showMessageDialog(null, "�ۼ��� �Ϸ�Ǿ����ϴ�", "complete", JOptionPane.INFORMATION_MESSAGE);
				M25_Ui_Notice.jf.dispose();
			}else if(checkWrite == null){
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ�.", "complete", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(a == "inputModify"){
			checkModify = JOptionPane.showInputDialog(null, "�����Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Modify", JOptionPane.INFORMATION_MESSAGE);
			try {
				// ��й�ȣ �Է� -> noticeM�� ���� -> �������� ������ ��й�ȣ�� ����
				if(checkModify == null){
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �ùٸ��� �ʰų�\n������� ��� �Ǵ� �۾��̰� ����ġ�մϴ�.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					return;
				}
				if(M25_Ui_Notice.temp.equals(M25_Client_NoticeM.pw_list.get(checkModify))){
					JOptionPane.showMessageDialog(null, "������ ������ �ּ���", "complete", JOptionPane.INFORMATION_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					new M25_Ui_Notice("modify", M25_Ui_Notice.temp);
				}else{
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �ùٸ��� �ʰų�\n����ڰ� ����Ͽ����ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
				}	
				
			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "�ش� ����ڸ� �� ������ �����մϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}
					
			
		}else if(a == "checkUpload"){
			if(M25_Ui_Main.fileInput.getText().equals("")){
				JOptionPane.showMessageDialog(null, "���ϸ��� �����ϴ�.", "error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			checkUpload = JOptionPane.showInputDialog(null, "�����Ͻ÷��� ��й�ȣ�� �Է��Ͻÿ�", "Upload", JOptionPane.INFORMATION_MESSAGE);
			if(!M25_Ui_Main.fileInput.getText().equals("")){
			
				FileDialog tmp1 = M25_Ui_Main.fileOpen;
				boolean result = M25_Ui_Main.fileM.uploadFile(tmp1.getDirectory(), tmp1.getFile(), checkUpload);
				
				if(result){	JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�", "complete", JOptionPane.INFORMATION_MESSAGE); }
				else{JOptionPane.showMessageDialog(null, "������ ���еǾ����ϴ�", "complete", JOptionPane.INFORMATION_MESSAGE);  }
				
				M25_Ui_Main.fileInput.setText("");

			}else if(M25_Ui_Main.fileInput == null){
				JOptionPane.showMessageDialog(null, "������ ���õ��� �ʾҽ��ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}else if(checkUpload == null){
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}else if(M25_Ui_Main.fileOpen.getFile() == null){
				JOptionPane.showMessageDialog(null, "��ҵǾ����ϴ�.", "error", JOptionPane.WARNING_MESSAGE);
			}
					
		}else if(a == "checkNick"){
			JOptionPane.showMessageDialog(null, "�̹� �����ϴ� �г����Դϴ�!", "error", JOptionPane.WARNING_MESSAGE);
		}
	
	}	
	



}

