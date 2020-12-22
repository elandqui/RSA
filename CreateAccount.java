// Eric Landquist
// Virginia Tech 

// July 6, 1998

//CreateAccount.java

import java.io.*;
import java.awt.*;
import java.awt.event.*;

class CreateAccount extends Frame{

	RandomAccessFile loginFile;
	RandomAccessFile keyFile;
	Label status;

	TextField login;
	TextField password;
	TextField password2;

	CreateAccount(String account, String keys) {
		super("Create your account");

		try{
			loginFile = new RandomAccessFile(PublicKey.LOGIN_INFO, "rw");
			keyFile = new RandomAccessFile(PublicKey.KEY_INFO, "rw");
		} catch(IOException ioe) {
		}

		Label loginLabel = new Label("Login                  ");
		Label passLabel = new Label("Password               ");
		Label pass2Label = new Label("Verify Password      ");

		login = new TextField();
		password = new TextField();		
		password.setEchoChar('*');
		password2 = new TextField();
		password2.setEchoChar('*');

		Panel loginPanel = new Panel();
		loginPanel.setLayout(new GridLayout(3, 2));
		
		loginPanel.add(loginLabel);
		loginPanel.add(login);
		loginPanel.add(passLabel);
		loginPanel.add(password);
		loginPanel.add(pass2Label);
		loginPanel.add(password2);

		// Creating a couple buttons for entering or exiting the program.
		Button enter = new Button ("OK");
		Button exit = new Button ("Exit ");

		// Setting up their actions.
		enter.addActionListener (new CreateAccountEventHandler(CreateAccountEventHandler.OK, this));
		exit.addActionListener (new CreateAccountEventHandler(CreateAccountEventHandler.QUIT, this));

		this.setLayout(new BorderLayout());
		this.add("North", loginPanel);
		
		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());

		bottom.add(exit);
		bottom.add(enter);

		this.add("Center", bottom);

		status = new Label("");
		this.add("South", status);

		this.pack();
		this.setVisible(true);
	}
}