package com.client.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.client.M25_Client;
import com.client.M25_Client_roomM;


public class M25_Ui_chatRoom extends JFrame {

	ImageIcon icon;
	ImageIcon icon2;
//	public static JFrame jf;
	JPanel jpanel;
	
	// 접속현황
	public List onlineList;
	
	// 채팅방 TextArea
	public TextArea chatRoom;
	private Font chatRoomFont;
	TextArea chatInput;
	
	static M25_Client_roomM actor;
	
	private static M25_Ui_chatRoom instance;
	public static synchronized M25_Ui_chatRoom getChatRoom() {
		if (instance == null){
			instance = new M25_Ui_chatRoom();
		}
		return instance;
	}
	
	public void instanceTonull(){
		instance = null;
//		actor = null;
		this.dispose();
	}
	
	
	
	private M25_Ui_chatRoom() {
		// 여기서 people의 닉네임을 가져워서 넣어야됨.
		actor = new M25_Client_roomM(this, M25_Client.people.getNickName());
		
		int sizeX = 450;
		int sizeY = 500;
		
		
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
		
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension myScreen = kit.getScreenSize();
		
		this.setLayout(null);
		
		
		//chatroom start
		icon = new ImageIcon(".\\img\\chatting.png");
		JLabel chatTitle = new JLabel(icon);
		chatTitle.setBounds(10, 10, 100, 35);
		chatTitle.setVisible(true);		
		
		chatRoom = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		chatRoom.setBounds(10, 45, 260, 300);
		chatRoom.setVisible(true);			
		chatRoom.setEditable(false);
		chatRoomFont = new Font("나눔바른고딕", Font.PLAIN, 14);
		chatRoom.setFont(chatRoomFont);
		
		
		//chatroo11m end
		
		//online list start
		icon = new ImageIcon(".\\img\\online.png");
		JLabel online = new JLabel(icon);
		online.setBounds(290, 10, 100, 35);
		online.setVisible(true);
		
		onlineList = new List();
		onlineList.setBounds(290, 45, 130, 300);
		onlineList.setVisible(true);
		
		
		//input chat start
		String[] fontStyle = {"B", "+", "-", "C", "S"};
		Font font2 = new Font("", Font.PLAIN, 1);
		for (int i = 0; i < fontStyle.length; i++) {
//			icon2 = new ImageIcon(".\\img\\button_0"+(i+1)+".png");
			JButton testBtn = new JButton(new ImageIcon(".\\img\\button_0"+(i+1)+".png"));			
			testBtn.setText(fontStyle[i]);			
			testBtn.setBounds(10+(i*50), 355, 62, 20);
			testBtn.setVisible(true);
			testBtn.addActionListener(subBtnActor);
			testBtn.setBorderPainted(false);
			testBtn.setContentAreaFilled(false);
			testBtn.setFocusPainted(false);
			testBtn.setFont(font2);
			jpanel.add(testBtn);
		}
		
		chatInput = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		chatInput.setBounds(10, 380, 290, 70);
		chatInput.setVisible(true);	
		//input chat end
		
		//send start
		JButton sendLa = new JButton(new ImageIcon(".\\img\\send.png"));
		sendLa.setContentAreaFilled(false);
		sendLa.setBorderPainted(false);
		sendLa.setBounds(315, 380, 100, 70);
		sendLa.setVisible(true);
		sendLa.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				actor.sendMessage(chatInput.getText());
				chatInput.setText("");
			}
		});
		
		//send end		
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().setBackground(Color.white);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				new M25_Ui_DialogBtn("checkChatExit");
			}
		});
		
		jpanel.add(sendLa);
		jpanel.add(chatInput);
		jpanel.add(online);
		jpanel.add(onlineList);
		jpanel.add(chatRoom);
		jpanel.add(chatTitle);
		jpanel.setBounds(0, 0, sizeX, sizeY);
		jpanel.setVisible(true);
		
		this.add(jpanel);
		this.setBounds((myScreen.width-sizeX)/2, (myScreen.height-sizeY)/2, sizeX, sizeY);		
		this.setVisible(true);

		
		// 소캣 연결-- 방접속 시도
		actor.start();
	}

	private ActionListener subBtnActor = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JButton tmp = (JButton)e.getSource();
			String msg = tmp.getText();
			int font_Size =chatRoomFont.getSize();
			
			if(msg.equals("B")){
				if(chatRoomFont.isBold()){ 
					chatRoomFont = chatRoomFont.deriveFont(Font.PLAIN); 
				}else{ 
					chatRoomFont = chatRoomFont.deriveFont(Font.BOLD); 
				}
				chatRoom.setFont(chatRoomFont);
			}else if(msg.equals("+")){
				font_Size += 2 ;
				if(font_Size > 24 ){font_Size=24;}
				chatRoomFont= chatRoomFont.deriveFont((float)font_Size);
				chatRoom.setFont(chatRoomFont);
			}else if(msg.equals("-")){
				font_Size -= 2;
				if(font_Size < 8 ){font_Size=8;}
				chatRoomFont= chatRoomFont.deriveFont((float)font_Size);
				chatRoom.setFont(chatRoomFont);
			}else if(msg.equals("C")){
				int check = JOptionPane.showConfirmDialog(null, "대화방 내용을 [삭제]하겠습니까?", "대화방 내용삭제", JOptionPane.YES_NO_OPTION);
				if(check == JOptionPane.YES_OPTION){ chatRoom.setText(""); }
				
			}else if(msg.equals("S")){
				int check = JOptionPane.showConfirmDialog(null, "대화방 내용을 [저장]하겠습니까?", "대화방 내용저장", JOptionPane.YES_NO_OPTION);
				if(check == JOptionPane.YES_OPTION){ saveChatRoom(); }
			}
		}
	};
	
	
	private void saveChatRoom(){
		FileDialog f_dia = new FileDialog(this, "대화내용 저장", FileDialog.SAVE);
		f_dia.setVisible(true);		
		
		File file = new File(f_dia.getDirectory(), f_dia.getFile());
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter buffw = null;

		String msg = chatRoom.getText();
		
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos);
			buffw = new BufferedWriter(osw);
			
			buffw.write(msg);
			buffw.flush();
			System.out.println("[채팅방] 채팅방 내용을 저장했습니다.");
			
			
		} catch (FileNotFoundException e) {
			System.out.println("[채팅방] 채팅방 내용을 저장할 파일이 없습니다.");
		} catch (IOException e) {
			System.out.println("[채팅방] 파일을 저장하다가 오류가 발생했습니다.");
		}finally{
			try {
				buffw.close();
				osw.close();
				fos.close();
			} catch (IOException e) {
				System.out.println("[채팅방] 스트림을 닫는 도중에 오류가 발생했습니다.");
			}
		}
	}// saveChatRoom() end

}
