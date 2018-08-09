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
	
	
	// Y or N : 로그인, 채팅방 접속, 채팅방 종료, 파일 다운로드 확인, 프로그램 종료, 게시판 작성취소,
	// Input : 파일 삭제 확인, 게시판 글작성, 게시판 글수정, 게시판 글삭제, 파일 업로드 확인	
	// only msg : 글 삭제 완료, 글 수정&작성 완료, 파일 업로드 완료, 파일 삭제 완료, 닉네임 중복체크	
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
		
		if(a == "checkLogin"){// 로그인 체크 상황
			if(M25_Ui_Login.inputName.getText().equals("") ) {JOptionPane.showMessageDialog(null, "닉네임을 정해주세요", "error", JOptionPane.WARNING_MESSAGE); return;}
		
			
			checkLogin = JOptionPane.showConfirmDialog(null, "닉네임 [ "+M25_Ui_Login.inputName.getText()+" ]\n로그인 하시겠습니까?", "Login", JOptionPane.YES_NO_OPTION);
			if(checkLogin==0){
				if(M25_Ui_Login.inputSevIp.getText().equals("") ) {JOptionPane.showMessageDialog(null, "서버의 IP를 입력해주세요", "error", JOptionPane.WARNING_MESSAGE); return;}
				boolean servCheck = M25_Ui_Login.actor.setSock(M25_Ui_Login.inputSevIp.getText());
				if(servCheck){
					boolean nickCheck = M25_Ui_Login.actor.setNickName();
					if(nickCheck==false){	JOptionPane.showMessageDialog(null, "중복된 닉네임입니다.", "error", JOptionPane.WARNING_MESSAGE);}
				}
				else{
					JOptionPane.showMessageDialog(null, "서버가 아직 준비되지 않았습니다.", "error", JOptionPane.WARNING_MESSAGE);
				}
			}
			
		}else if(a == "checkChatJoin"){
			checkChatJoin = JOptionPane.showConfirmDialog(null, "채팅방에 접속 하시겠습니까?", "Chat", JOptionPane.YES_NO_OPTION);
			if(checkChatJoin == JOptionPane.YES_OPTION){
			}
			
		}else if(a == "checkChatExit"){
			checkChatExit = JOptionPane.showConfirmDialog(this, "채팅을 종료 하시겠습니까?", "Exit", JOptionPane.YES_NO_OPTION);
			if(checkChatExit == JOptionPane.YES_OPTION){
				M25_Ui_chatRoom.actor.sendMessage(M25_Client.SOCK_CLOSE);
				M25_Ui_chatRoom.actor.closeChatRoom();		
			}
			
		}else if(a == "download"){
			Object[] options = {"다운로드", "삭제", "취소"};
			checkFileDownload = JOptionPane.showOptionDialog(null, 
					"["+M25_Ui_Main.testMsg_Down+"]를 다운로드 하시겠습니까?", "Download", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null,
					options,  options[0]	);
			if(checkFileDownload == JOptionPane.YES_OPTION){
				M25_Ui_Main.fileSave();
			}
			if(checkFileDownload == JOptionPane.NO_OPTION){
				String checkPw = JOptionPane.showInputDialog(null, "진행하시려면 비밀번호를 입력하시오", "Delete", JOptionPane.INFORMATION_MESSAGE);
				if(checkPw.equals("")){
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "error", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int tmp = M25_Ui_Main.fileM.deleteFile(M25_Ui_Main.testMsg_Down, checkPw);
				if (tmp==1){
					JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다", "complete", JOptionPane.INFORMATION_MESSAGE);
				}else if(tmp==0){
					JOptionPane.showMessageDialog(null, "서버에 파일이 없습니다.", "error", JOptionPane.INFORMATION_MESSAGE);
				}else if(tmp==-1){
					JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다.", "error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			if(checkFileDownload == JOptionPane.CANCEL_OPTION){
				return;
			}
			
			
			
		}else if(a == "checkProgramExit"){
			checkProgramExit = JOptionPane.showConfirmDialog(null, "종료 하시겠습니까?", "Exit", JOptionPane.YES_NO_OPTION);
			if(checkProgramExit == JOptionPane.YES_OPTION){
				M25_Ui_Main.actor.closeSocket();
			}
			
		}else if(a == "checkWriteCancel"){
			checkWriteCancel = JOptionPane.showConfirmDialog(null, "작성중인 정보는 삭제됩니다.", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(checkWriteCancel == JOptionPane.YES_OPTION){
				M25_Ui_Notice.textTitle.setText("");
				M25_Ui_Notice.textContent.setText("");
				M25_Ui_Notice.jf.dispose();
			}
			
		}else if(a == "checkWriteWinCancel"){
			checkWriteWinCancel = JOptionPane.showConfirmDialog(this, "작성중인 정보는 삭제됩니다.", "Login", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(checkWriteWinCancel == JOptionPane.YES_OPTION){
				M25_Ui_Notice.jf.dispose();
			}
			
		}else if(a == "checkFileDelete"){
			checkFileDelete = JOptionPane.showInputDialog(null, "삭제하시려면 비밀번호를 입력하시오", "Delete", JOptionPane.WARNING_MESSAGE);
			if(checkFileDelete == "password"){
//				M25_Ui_Main.noticeList.remove(M25_Ui_Main.testMsg_Down);        이건 뭔지 모르겠음
				JOptionPane.showMessageDialog(null, "삭제가 완료 되었습니다.", "complete", JOptionPane.INFORMATION_MESSAGE);
				M25_Ui_Notice.jf.dispose();
			}else{
				JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않거나\n사용자가 취소하였습니다.", "error", JOptionPane.WARNING_MESSAGE);
			}
			
		}else if(a == "checkDelete"){
			checkDelete = JOptionPane.showInputDialog(null, "진행하시려면 비밀번호를 입력하시오", "Delete", JOptionPane.INFORMATION_MESSAGE);
			try {
				if(M25_Client_NoticeM.pw_list.get(checkDelete) == null){
					JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않거나\n사용자의 취소 또는 글쓴이가 불일치합니다.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					return;
				}
				if(M25_Ui_Notice.temp.equals(M25_Client_NoticeM.pw_list.get(checkDelete))){
	//			if(checkDelete.equals(M25_Client_NoticeM.pw)){
					JOptionPane.showMessageDialog(null, "삭제가 완료 되었습니다.", "complete", JOptionPane.INFORMATION_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					new M25_Ui_Notice("delete", M25_Ui_Notice.temp);
					M25_Ui_Notice.jf.dispose();
				}else{
					JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않거나\n사용자의 취소 또는 글쓴이가 불일치합니다.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
				}
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "해당 사용자만 글 삭제가 가능합니다.", "error", JOptionPane.WARNING_MESSAGE);
			}
		}else if(a == "checkWrite"){
			
			checkWrite = JOptionPane.showInputDialog(null, "작성하시려면 비밀번호를 입력하시오", "Write", JOptionPane.INFORMATION_MESSAGE);
			if(checkWrite != null){
				JOptionPane.showMessageDialog(null, "작성이 완료되었습니다", "complete", JOptionPane.INFORMATION_MESSAGE);
				M25_Ui_Notice.jf.dispose();
			}else if(checkWrite == null){
				JOptionPane.showMessageDialog(null, "취소되었습니다.", "complete", JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(a == "inputModify"){
			checkModify = JOptionPane.showInputDialog(null, "진행하시려면 비밀번호를 입력하시오", "Modify", JOptionPane.INFORMATION_MESSAGE);
			try {
				// 비밀번호 입력 -> noticeM에 보냄 -> 서버에서 가져온 비밀번호와 대조
				if(checkModify == null){
					JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않거나\n사용자의 취소 또는 글쓴이가 불일치합니다.", "error", JOptionPane.WARNING_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					return;
				}
				if(M25_Ui_Notice.temp.equals(M25_Client_NoticeM.pw_list.get(checkModify))){
					JOptionPane.showMessageDialog(null, "수정을 시작해 주세요", "complete", JOptionPane.INFORMATION_MESSAGE);
					M25_Ui_Notice.jf.dispose();
					new M25_Ui_Notice("modify", M25_Ui_Notice.temp);
				}else{
					JOptionPane.showMessageDialog(null, "비밀번호가 올바르지 않거나\n사용자가 취소하였습니다.", "error", JOptionPane.WARNING_MESSAGE);
				}	
				
			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "해당 사용자만 글 수정이 가능합니다.", "error", JOptionPane.WARNING_MESSAGE);
			}
					
			
		}else if(a == "checkUpload"){
			if(M25_Ui_Main.fileInput.getText().equals("")){
				JOptionPane.showMessageDialog(null, "파일명이 없습니다.", "error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			checkUpload = JOptionPane.showInputDialog(null, "진행하시려면 비밀번호를 입력하시오", "Upload", JOptionPane.INFORMATION_MESSAGE);
			if(!M25_Ui_Main.fileInput.getText().equals("")){
			
				FileDialog tmp1 = M25_Ui_Main.fileOpen;
				boolean result = M25_Ui_Main.fileM.uploadFile(tmp1.getDirectory(), tmp1.getFile(), checkUpload);
				
				if(result){	JOptionPane.showMessageDialog(null, "전송이 완료되었습니다", "complete", JOptionPane.INFORMATION_MESSAGE); }
				else{JOptionPane.showMessageDialog(null, "전송이 실패되었습니다", "complete", JOptionPane.INFORMATION_MESSAGE);  }
				
				M25_Ui_Main.fileInput.setText("");

			}else if(M25_Ui_Main.fileInput == null){
				JOptionPane.showMessageDialog(null, "파일이 선택되지 않았습니다.", "error", JOptionPane.WARNING_MESSAGE);
			}else if(checkUpload == null){
				JOptionPane.showMessageDialog(null, "취소되었습니다.", "error", JOptionPane.WARNING_MESSAGE);
			}else if(M25_Ui_Main.fileOpen.getFile() == null){
				JOptionPane.showMessageDialog(null, "취소되었습니다.", "error", JOptionPane.WARNING_MESSAGE);
			}
					
		}else if(a == "checkNick"){
			JOptionPane.showMessageDialog(null, "이미 존재하는 닉네임입니다!", "error", JOptionPane.WARNING_MESSAGE);
		}
	
	}	
	



}

