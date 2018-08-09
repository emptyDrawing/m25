package com.client.ui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.List;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.client.M25_Client;
import com.client.M25_Client_FileM;

public class M25_Ui_Main implements ActionListener {

	ImageIcon icon;
	ImageIcon icon2;
	
	JButton joinBtn;
	JButton writeBtn;
	static TextField fileInput;
	Button upload;
	static FileDialog fileOpen;
	static FileDialog fileSave;
	static String testMsg_Up;
	static String testMsg_Down;
	
	public static JFrame jf;
	
	JPanel background;
	public List fileList;
	static public List noticeList;
	static String mainTemp;
	public List onlineList;
	JScrollPane scrollPane;
	
	int selNum;

	
	M25_Ui_chatRoom chatRoom;
	static M25_Client actor;
	static M25_Client_FileM fileM;
	
	public M25_Ui_Main(M25_Client m25_Client) {

		actor = m25_Client;
		fileM = new M25_Client_FileM(this);

		jf = new JFrame();
		icon2 = new ImageIcon(".\\img\\background.jpg");
		background = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(icon2.getImage(), 0, 0, null);    
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension myScreen = kit.getScreenSize();
		scrollPane = new JScrollPane(background);
        jf.setContentPane(scrollPane);
        jf.setLayout(null);
		background.setLayout(null);
		int sizeX = 700;
		int sizeY = 500;
		
		// notice
		icon = new ImageIcon(".\\img\\notice.png");
		JLabel notice = new JLabel(icon);
		notice.setBounds(10, 10, 100, 35);
		notice.setVisible(true);		
		Font font = new Font("", Font.BOLD, 14);
		
		noticeList = new List();					//////////////////////////////////////////////////////////
		noticeList.setBounds(10, 50, 450, 250);
		noticeList.setVisible(true);		
		noticeList.addActionListener(this);
		noticeList.setFont(font);
		// notice end		
		
		
		// OnlineList start
		icon = new ImageIcon(".\\img\\online.png");
		JLabel online = new JLabel(icon);
		online.setBounds(490, 10, 100, 35);
		online.setVisible(true);
		
		onlineList = new List();
		onlineList.setBounds(490, 50, 170, 250);
		onlineList.setVisible(true);
		onlineList.add("테스트");
		
		//OnlineList end		
		
		//FileList start		
		icon = new ImageIcon(".\\img\\filelist.png");
		JLabel file = new JLabel(icon);
		file.setBounds(10, 310, 100, 35);
		file.setVisible(true);
		
		fileList = new List();						//////////////////////////////////////////////////////////
		fileList.setBounds(10, 350, 410, 100);
		fileList.setVisible(true);
		fileList.addActionListener(this);
		
		fileInput = new TextField();
		fileInput.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fileOpen();
			}
		});		
		fileInput.setBounds(110, 317, 220, 23);		
		fileInput.setVisible(true);
		fileInput.setEditable(false);
		
		upload = new Button("upload");
		upload.setBounds(340, 315, 80, 25);
		upload.addActionListener(new ActionListener() {			
//			@Override
			public void actionPerformed(ActionEvent arg0) {		
				new M25_Ui_DialogBtn("checkUpload");	
			}
		});		
		
		upload.setVisible(true);
		//FileList end
		
		icon = new ImageIcon(".\\img\\chatjoin1.png");
		joinBtn = new JButton(icon);
		joinBtn.setBounds(450, 325, 205, 64);
		joinBtn.validate();
		joinBtn.setContentAreaFilled(false);
		joinBtn.setBorderPainted(false);
		joinBtn.setFocusPainted(false);
		joinBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				icon = new ImageIcon(".\\img\\chatjoin1.png");
				joinBtn.setIcon(icon);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				icon = new ImageIcon(".\\img\\chatjoin2.png");
				joinBtn.setIcon(icon);				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {		
				icon = new ImageIcon(".\\img\\chatjoin3.png");
				joinBtn.setIcon(icon);
				chatRoom = M25_Ui_chatRoom.getChatRoom();			
			}			
		});
			
		
		icon = new ImageIcon(".\\img\\writenotice1.png");
		writeBtn = new JButton(icon);
		writeBtn.setBounds(450, 390, 205, 64);
		writeBtn.validate();
		writeBtn.setContentAreaFilled(false);
		writeBtn.setBorderPainted(false);
		writeBtn.setFocusPainted(false);
		writeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				icon = new ImageIcon(".\\img\\writenotice1.png");
				writeBtn.setIcon(icon);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				icon = new ImageIcon(".\\img\\writenotice2.png");
				writeBtn.setIcon(icon);
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				icon = new ImageIcon(".\\img\\writenotice3.png");
				writeBtn.setIcon(icon);
				new M25_Ui_Notice("write", "a");
			}
		});

		
		jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		background.add(writeBtn);
		background.add(joinBtn);		
		background.add(upload);
		background.add(fileInput);
		background.add(file);
		background.add(fileList);
		background.add(onlineList);
		background.add(online);
		background.add(noticeList);
		background.add(notice);
		
		background.setVisible(true);
		background.setBounds(0, 0, sizeX, sizeY);
		jf.add(background);
		
		jf.setBounds((myScreen.width-sizeX)/2, (myScreen.height-sizeY)/2, sizeX, sizeY);
		jf.setVisible(true);
		jf.setLayout(null);
		jf.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actor.closeSocket();
			}
		});
	}
	
	public void endframe(){
		if(chatRoom != null ){System.out.println("[채팅M] 종료"); chatRoom.instanceTonull(); chatRoom.dispose();}
		if(fileM.isAlive() ) {System.out.println("[파일M] 종료"); fileM.stop(); fileM =null; }
		jf.dispose();
	}
	
	public static void fileOpen() {
		Frame f = new Frame();
		fileOpen = new FileDialog(f, "업로드", 0);
		fileOpen.setVisible(true);
		if(fileOpen.getFile() == null){
			JOptionPane.showMessageDialog(null, "파일을 선택해주세요.", "error", JOptionPane.WARNING_MESSAGE);			
		}else{
			fileInput.setText(fileOpen.getFile());
		}
	}
	
	public static void fileSave(){
		Frame f = new Frame();
		fileSave = new FileDialog(f, "다운로드", 1);
		fileSave.setVisible(true);
		if(fileSave.getFile() == null){
			JOptionPane.showMessageDialog(null, "파일을 선택해주세요.", "error", JOptionPane.WARNING_MESSAGE);			
		}else{
			fileM.downloadFile(testMsg_Down, fileSave.getDirectory(), fileSave.getFile() );
		}	
	}

	
//	@Override
	public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(noticeList)){
				if(arg0.getActionCommand().equals("삭제된 글입니다.")){
					JOptionPane.showMessageDialog(null, "이미 삭제된 게시물입니다..", "error", JOptionPane.WARNING_MESSAGE);	
					M25_Ui_Notice.jf.dispose();
				}else{
					new M25_Ui_Notice("read", arg0.getActionCommand());						
				}
			}else if(arg0.getSource() == fileList){
				testMsg_Down = fileList.getSelectedItem();
				new M25_Ui_DialogBtn("download");
			}
		
		
	}

	
	

}




