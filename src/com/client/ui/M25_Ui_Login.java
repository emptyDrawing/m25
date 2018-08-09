package com.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.client.M25_Client;




public class M25_Ui_Login implements MouseListener{

	JScrollPane scrollPane;
	ImageIcon icon;
	ImageIcon icon2;
	ImageIcon icon3;
	private JButton loginBtn;
	private JButton exitBtn;
	static String checkNick;
	public static JTextField inputName;	
	static public JTextField inputSevIp;
//	private JTextField inputPort;
	private JPanel background;
	public JFrame jf;
	static M25_Client actor;			//
	
	public M25_Ui_Login(M25_Client me) {
		actor = me;
		jf = new JFrame();		
		icon = new ImageIcon(".\\img\\login2.png");		
    	Font font = new Font("", Font.PLAIN, 15);
		background = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, null);    
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		background.setLayout(null);
		
		inputName = new JTextField("닉네임");
		inputName.setFont(font);
		inputName.setBounds(145, 220, 150, 25);
		inputName.setVisible(true);
		inputName.setOpaque(false);
		
		inputSevIp = new JTextField("localhost");
		inputSevIp.setFont(font);
		inputSevIp.setBounds(145, 260, 150, 25);
		inputSevIp.setVisible(true);
		inputSevIp.setOpaque(false);
		
		
		background.add(inputName);		
		background.add(inputSevIp);		
		
		
		// Login button
		icon2 = new ImageIcon(".\\img\\login_11.png");
		loginBtn = new JButton(icon2);
		loginBtn.setContentAreaFilled(false);
		loginBtn.setFocusPainted(false);
		loginBtn.setBorderPainted(false);//
		loginBtn.setOpaque(false);				
		loginBtn.setBounds(380, 250, 140, 60);
		loginBtn.setVisible(true);
		loginBtn.addMouseListener(this);
		background.add(loginBtn);
		// exit button
		icon3 = new ImageIcon(".\\img\\login_13.png");
		exitBtn = new JButton(icon3);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setFocusPainted(false);
		exitBtn.setBorderPainted(false);//
		exitBtn.setOpaque(false);				
		exitBtn.setBounds(560, 245, 140, 60);
		exitBtn.setVisible(true);		
		exitBtn.addMouseListener(this);		
		background.add(exitBtn);		
		
		
		scrollPane = new JScrollPane(background);
		jf.setContentPane(scrollPane);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension myScreen = kit.getScreenSize();
		jf.setUndecorated(true);
		jf.setBackground(new Color(0,0,0,255));
		
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setBounds((myScreen.width-500)/2, (myScreen.height-280)/2, 700, 350);
		jf.setResizable(false);
		jf.setVisible(true);    	
}
	

//	@Override
	public void mouseClicked(MouseEvent e) {}

//	@Override
	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {
		//버튼을 눌렀을때
		if(e.getSource().equals(loginBtn)){
			new M25_Ui_DialogBtn("checkLogin");
		}else if (e.getSource().equals(exitBtn)) {
			jf.dispose();
		}		
	}

//	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource().equals(loginBtn)){
			icon2 = new ImageIcon(".\\img\\login_11_2.png");
			loginBtn.setIcon(icon2);
		}else if (e.getSource().equals(exitBtn)) {
			icon3 = new ImageIcon(".\\img\\login_13_2.png");
			exitBtn.setIcon(icon3);
		}
	}

//	@Override
	public void mouseExited(MouseEvent e) {
		// 버튼 마우스 이탈
		if(e.getSource().equals(loginBtn)){
			icon2 = new ImageIcon(".\\img\\login_11.png");
			loginBtn.setIcon(icon2);
		}else if (e.getSource().equals(exitBtn)) {
			icon3 = new ImageIcon(".\\img\\login_13.png");
			exitBtn.setIcon(icon3);
		}
	}

	

}
