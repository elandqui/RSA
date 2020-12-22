// Eric Landquist
// Virginia Tech

// June 6, 1998

// Decrypt1.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

class Decrypt1 extends Frame{

	String message;
	String inFileName;
	String outFileName;

	Label cipherText;
	TextArea theCipher;

	Label fromFile;
	TextField inFile;

	Label toFile;
	TextField outFile;

	Label decryptionInfo;
	CheckboxGroup findInfo;
	Checkbox defaultSet;
	Checkbox fromFileCh;
	Checkbox modAndKey;

	TextField fileField;
	TextField keyField;
	TextField modField;

	BigInteger offset;	

	public Decrypt1(BigInteger offset) {
		super("Decryption Information");
		this.setSize(500, 500);
		this.setVisible(true);
		this.offset = offset;

		cipherText = new Label("Encrypted Message:");

		theCipher = new TextArea();
		theCipher.setEditable(true);
		
		Button ok = new Button("Decrypt!");
		Button back = new Button(" < Back ");
		Button exit = new Button(" Logout ");

		ok.addActionListener(new Decrypt1EventHandler(Decrypt1EventHandler.D2, this));
		back.addActionListener(new Decrypt1EventHandler(Decrypt1EventHandler.SCREEN_2, this));
		exit.addActionListener(new Decrypt1EventHandler(Decrypt1EventHandler.QUIT, this));

		fromFile = new Label("From File: ");
		inFile = new TextField();

		toFile = new Label("To File: ");
		outFile = new TextField();

		decryptionInfo = new Label("Decryption Information");

		findInfo = new CheckboxGroup();
		defaultSet = new Checkbox("Default", true, findInfo);
		fromFileCh = new Checkbox("File", false, findInfo);
		modAndKey = new Checkbox("Modulus", false, findInfo);
		Label keyLabel = new Label("Private Key");
		
		fileField = new TextField();
		keyField = new TextField();
		modField = new TextField();

		this.setLayout(new BorderLayout());

		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		top.add("North", cipherText);
		top.add("Center", theCipher);

		Panel middle = new Panel();
		middle.setLayout(new FlowLayout());
	
		Panel left = new Panel();
		left.setLayout(new GridLayout(2, 2));
		left.add(fromFile);
		left.add(inFile);
		left.add(toFile);
		left.add(outFile);

		Panel right = new Panel();
		right.setLayout(new GridLayout(4, 2));
		right.add(defaultSet);
		right.add(new Label(""));
		right.add(fromFileCh);
		right.add(fileField);
		right.add(modAndKey);
		right.add(modField);
		right.add(keyLabel);
		right.add(keyField);
		
		middle.add(left);
		middle.add(right);

		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());
		bottom.add(ok);
		bottom.add(back);
		bottom.add(exit);

		this.add("North", top);
		this.add("Center", middle);		
		this.add("South", bottom);

		this.pack();

	}

} // end class Decrypt1