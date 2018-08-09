package com.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.client.M25_Client;
import com.client.M25_Client_NoticeM;
import com.pub.M25_Notice;
import com.pub.M25_People;

public class M25_Ui_Notice implements ActionListener {

	int sizeX = 500;
	int sizeY = 600;
	
	public static JFrame jf;
	JPanel jpanel;
	JLabel line;
	JLabel topTitle;
	JButton cancel = null;
	JButton modify = null;
	JButton modify2 = null;
	JButton close = null;
	JButton write = null;
	JButton delete = null;
	ImageIcon icon;
	public TextField noticeTitle;
	public TextArea noticeContent;
	public static String temp;
	public static String temp2;
	
	Font titleFont = new Font("", Font.BOLD, 20);	
	
	
	// 글 제목 불러오기 (수정 & 보기 공통)
	static JLabel textTitle;
	// 글 내용 불러오기 (수정 & 보기 공통)
	static JLabel textContent;
	
	M25_Client_NoticeM actor;
	M25_Client client;
	
	public M25_Ui_Notice(String args, String arg0) {// args : read / write  	arg0: 임시
		jf = new JFrame();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension myScreen = kit.getScreenSize();
		
		
		final ImageIcon background = new ImageIcon(".\\img\\background.jpg");
		jpanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background.getImage(), 0, 0, null);    
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		jpanel.setLayout(null);
		
		
		jf.setLayout(null);
		
		// 제목&내용 추후 이미지 교체
		textTitle = new JLabel(new ImageIcon(".\\img\\title.png"));
		textTitle.setBounds(5, 63, 50, 35);
		textTitle.setVisible(true);
		
		textContent = new JLabel(new ImageIcon(".\\img\\content.png"));
		textContent.setBounds(5, 100, 50, 35);
		textContent.setVisible(true);
		
		line = new JLabel(new ImageIcon(".\\img\\linetemp.png"));
		line.setBounds(10, 55, 465, 1);
		line.setVisible(true);		
		
		if(args == "write" ){
			topTitle = new JLabel(new ImageIcon(".\\img\\writetitle.png"));
			noticeWrite();			
		}else if(args == "modify"){
			topTitle = new JLabel(new ImageIcon(".\\img\\noticeimg.png"));
			noticeRead("modify", arg0);		
		}else if(args == "read"){
			topTitle = new JLabel(new ImageIcon(".\\img\\noticeimg.png"));
			noticeRead("read", arg0);
		}else if(args == "delete"){
			topTitle = new JLabel(new ImageIcon(".\\img\\noticeimg.png"));
			noticeRead("delete", arg0);
		}
		topTitle.setBounds(10, 10, 100, 35);
		topTitle.setVisible(true);
		jpanel.add(topTitle);	
		jpanel.add(textTitle);
		jpanel.add(textContent);
		jpanel.add(line);
		
		jf.add(jpanel);
		jpanel.setBounds(0, 0, sizeX, sizeY);
		jpanel.setVisible(true);	
		jf.setBounds((myScreen.width-sizeX)/2, (myScreen.height-sizeY)/2, sizeX, sizeY);
		jf.setVisible(true);					
	}
	
	
	public void noticeWrite(){
			
		noticeTitle = new TextField();
		noticeTitle.setBounds(60, 70, 400, 20);
		noticeTitle.setVisible(true);
		
		M25_People nick = new M25_People();
		noticeContent = new TextArea();
		noticeContent.setBounds(60, 100, 400, 400);		
		
		jf.getContentPane().setBackground(Color.white);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);						
		
		cancel = new JButton(new ImageIcon(".\\img\\cancel.jpg"));
		write = new JButton(new ImageIcon(".\\img\\write.jpg"));
		
		cancel.setBounds(310, 510, 70, 40);
		cancel.setVisible(true);
		write.setBounds(390, 510, 70, 40);
		write.setVisible(true);	
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {				
				new M25_Ui_DialogBtn("checkWriteWinCancel");						
			}
		});
		
		noticeTitle.setEditable(true);
		noticeContent.setEditable(true);
		
		cancel.addActionListener(this);
		write.addActionListener(this);
		
		
		jpanel.add(write);
		jpanel.add(cancel);
		jpanel.add(noticeTitle);
		jpanel.add(noticeContent);
	}
	
	
	
	public void noticeRead(String MorR, String readTitle){			
		
		noticeTitle = new TextField();
		noticeTitle.setBounds(60, 70, 400, 20);
		noticeTitle.setVisible(true);
			
		noticeContent = new TextArea();
		noticeContent.setBounds(60, 100, 400, 400);		
		
		noticeTitle.setText("");
		noticeContent.setText("");
		
		jf.getContentPane().setBackground(Color.white);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		
		close = new JButton(new ImageIcon(".\\img\\close.jpg"));	
		modify = new JButton(new ImageIcon(".\\img\\modify.jpg"));
		delete = new JButton(new ImageIcon(".\\img\\delete.jpg"));
		modify2 = new JButton(new ImageIcon(".\\img\\modify.jpg"));
		
		delete.setBounds(230, 510, 70, 40);
		modify.setBounds(310, 510, 70, 40);
		modify2.setBounds(310, 510, 70, 40);
		close.setBounds(390, 510, 70, 40);
		
		modify.setVisible(true);		
		close.setVisible(true);
		delete.setVisible(true);
		
		noticeTitle.setEditable(false);
		noticeContent.setEditable(false);
		
		if(MorR == "read"){			
			actor = new M25_Client_NoticeM(this, "nickname");
			M25_Notice tmpNotice = new M25_Notice();
			tmpNotice.setNoticeTilte(readTitle);
			actor.loadNotice(tmpNotice, "");
			temp = readTitle;
			
			
		}else if(MorR == "modify"){
			actor = new M25_Client_NoticeM(this, "nickname");
			M25_Notice tmpNotice = new M25_Notice();
			tmpNotice.setNoticeTilte(readTitle);
			actor.loadNotice(tmpNotice, "");		
			
			modify2.setVisible(true);	
			modify.setVisible(false);	
			delete.setVisible(false);			
			noticeTitle.setEditable(true);
			noticeContent.setEditable(true);
				
		}else if (MorR == "delete"){
			actor = new M25_Client_NoticeM(this, "nickname");
			M25_Notice tmpNotice = new M25_Notice();
			tmpNotice.setNoticeTilte("삭제$$"+readTitle);
			actor.loadNotice(tmpNotice, "delete");
			jf.dispose();
		}
		
		
		
		//작성 불가능		
		jf.add(delete);
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {				
				jf.dispose();						
			}
		});
		delete.addActionListener(this);
		close.addActionListener(this);
		modify.addActionListener(this);
		modify2.addActionListener(this);
		
		jpanel.add(delete);
		jpanel.add(modify);
		jpanel.add(modify2);
		jpanel.add(close);
		jpanel.add(noticeTitle);
		jpanel.add(noticeContent);		
	}	
	
	public void actionPerformed(ActionEvent e) {
		actor = new M25_Client_NoticeM(this, "nickname");
		if(e.getSource() == cancel){
			new M25_Ui_DialogBtn("checkWriteWinCancel");		
		}else if(e.getSource() == close){
			jf.dispose();
		}else if(e.getSource() == write){
			
			temp = noticeTitle.getText();
			temp2 = noticeContent.getText();
			
			long longTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
			
			M25_Ui_DialogBtn tmp = new M25_Ui_DialogBtn("checkWrite");
			
			if(tmp.checkWrite != null){
				M25_Notice tmpNotice = new M25_Notice();
				tmpNotice.setNoticeTime(sdf.format(longTime));			// 시간
				tmpNotice.setNoticeTilte(temp);							// 제목
				tmpNotice.setNoticeContent(temp2);						// 내용
				tmpNotice.setNoticeOwner("");							// 닉네임
				tmpNotice.setNoticePW(tmp.checkWrite);					// 비밀번호
				actor.sendNotice(tmpNotice, "write");
				
			}	
			
		}else if(e.getSource() == modify){
			temp = noticeTitle.getText();
			new M25_Ui_DialogBtn("inputModify");		
			
		}else if(e.getSource() == modify2){
			temp = noticeTitle.getText();
			temp2 = noticeContent.getText();
			
			long longTime = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
			
			
			M25_Notice tmpNotice1 = new M25_Notice();
			tmpNotice1.setNoticeTime(sdf.format(longTime));							// 시간
			tmpNotice1.setNoticeTilte(temp);										// 제목
			tmpNotice1.setNoticeContent(temp2);										// 내용
			tmpNotice1.setNoticeOwner("notice nickname");							// 닉네임
			tmpNotice1.setNoticePW(M25_Ui_DialogBtn.checkWrite);					// 비밀번호
			actor.sendNotice(tmpNotice1, "modify");
			JOptionPane.showMessageDialog(null, "수정이 완료 되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
			jf.dispose();
		}else if(e.getSource() == delete){
			temp = noticeTitle.getText();
			new M25_Ui_DialogBtn("checkDelete");
		}
	}

}
