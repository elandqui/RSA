// Eric Landquist
// Virginia Tech 
// May 16, 1998
// PublicKey.java

// This program will launch the Public Key Cryptography program.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;

class PublicKey extends Frame {

	TextField login;
	TextField password;

	public static final String LOGIN_INFO = "acnt.pkc";
	public static final String KEY_INFO = "key.pkc";
	public static final String WEB_PAGE = "keys.html";
	public static final String TIMEOUT = "timeout.pkc";
	public static final BigInteger HIDE_EXP = new BigInteger("785546588075729494889299498551796638033513654795751346266802407518760686393275938048956203777758087206590825028611052779325599711330954952475521465024135343374185201442298597140077");

	public PublicKey () {
		super("Public Key Cryptography");

		Label loginLabel = new Label("Login                  ");
		Label passLabel = new Label("Password               ");

		login = new TextField();
		password = new TextField();		
		password.setEchoChar('*');

		Panel loginPanel = new Panel();
		loginPanel.setLayout(new GridLayout(2, 2));
		
		loginPanel.add(loginLabel);
		loginPanel.add(login);
		loginPanel.add(passLabel);
		loginPanel.add(password);

		// Creating a couple buttons for entering or exiting the program.
		Button enter = new Button ("Enter");
		Button exit = new Button ("Exit ");

		// Setting up their actions.
		enter.addActionListener (new PublicKeyEventHandler(PublicKeyEventHandler.SCREEN_2, this));
		exit.addActionListener (new PublicKeyEventHandler(PublicKeyEventHandler.QUIT, this));

		this.setLayout(new BorderLayout());
		this.add("Center", loginPanel);
		
		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());

		bottom.add(exit);
		bottom.add(enter);

		this.add("South", bottom);
		this.pack();
	} // end constructor

    	public static void main(String args[]) {
		try {
			RandomAccessFile acnt = new RandomAccessFile(LOGIN_INFO, "rw");
			if (acnt.length() == 0) 
				new CreateAccount(LOGIN_INFO, KEY_INFO);
		}
		catch (IOException ioe){
		}

		PublicKey app = new PublicKey();
		app.setVisible(true);
	} // end main()

} // end class Public Key