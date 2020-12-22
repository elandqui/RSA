// Eric Landquist
// Virginia Tech

// June 5, 1998

// Encrypt1.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

class Encrypt1 extends Frame{

	TextArea screen;
	TextField inFile;
	TextField outFile;

	static CheckboxGroup charSet;
	static Checkbox ascii;
	static Checkbox intl;

	CheckboxGroup recipient;
	Checkbox name;
	Checkbox modexp;

	TextField nameField;
	TextField modulus;
	TextField key;

	// The message that will be encrypted.
	String message;

	// The file to which it will be written.
	String toFile;

	BigInteger offset;

	public Encrypt1 (BigInteger offset) {	

		super("Encryption Information");
		this.setSize(500, 500);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.offset = offset;

		Label messageStuff = new Label("Message");
		
		screen = new TextArea("", 20, 0);
		screen.setEditable(true);

		charSet = new CheckboxGroup();
		ascii= new Checkbox("ASCII/English", true, charSet);
		intl = new Checkbox("Unicode/International", false, charSet);

		Button display = new Button("Display");
		Button hide = new Button("Hide");
		Button clear = new Button("Clear");

		display.addActionListener(new MessageEventHandler(this, MessageEventHandler.DISPLAY));
		hide.addActionListener(new MessageEventHandler(this, MessageEventHandler.HIDE));
		clear.addActionListener(new MessageEventHandler(this, MessageEventHandler.CLEAR));

		recipient = new CheckboxGroup();
		// Put all info in a hashtable.
		name = new Checkbox("Name", false, recipient);
		modexp = new Checkbox("Modulus", false, recipient);


		this.add("Center", screen);

		// Where the plaintext message is.
		inFile = new TextField(20);

		// Where the encrypted message will be put.
		outFile = new TextField(20);

		Label fromFile = new Label ("From File:");
		Label toFile = new Label("To File:");

		Label toWho = new Label("Recipient");
		nameField = new TextField(20);
		modulus = new TextField(20);
		key = new TextField(20);
		Label nameLabel = new Label("Name      ");
		Label keyLabel = new Label("Public Key");
		Label modLabel = new Label("Modulus   ");

		Button ok = new Button("Encrypt!");
		Button back = new Button(" < Back ");
		Button logout = new Button (" Logout ");

		ok.addActionListener(new Encrypt1EventHandler(Encrypt1EventHandler.E2, this));
		back.addActionListener(new Encrypt1EventHandler(Encrypt1EventHandler.SCREEN_2, this));
		logout.addActionListener(new Encrypt1EventHandler(Encrypt1EventHandler.QUIT, this));

		// Fitting everything together

		this.setLayout(new BorderLayout());
		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		Panel topBottom = new Panel();
		top.add("North", messageStuff);
		top.add("Center", screen);
		topBottom.setLayout(new FlowLayout());
		topBottom.add(ascii);
		topBottom.add(intl);
		topBottom.add(display);
		topBottom.add(hide);
		topBottom.add(clear);
		top.add("South", topBottom);

		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());

		Panel bottomLeft = new Panel();
		bottomLeft.setLayout(new GridLayout(2, 2));
		bottomLeft.add(fromFile);
		bottomLeft.add(inFile);
		bottomLeft.add(toFile);
		bottomLeft.add(outFile);

		Panel bottomRight = new Panel();
		bottomRight.setLayout(new GridLayout(3,2));
		bottomRight.add(name);
		bottomRight.add(nameField);
		bottomRight.add(modexp);
		bottomRight.add(modulus);
		bottomRight.add(keyLabel);
		bottomRight.add(key);

		bottom.add(bottomLeft);
		bottom.add(bottomRight);

		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(ok);
		buttons.add(back);
		buttons.add(logout);

		this.add("North", top);
		this.add("Center", bottom);
		this.add("South", buttons);
		this.pack();
		this.setVisible(true);
	}

} // end class Encrypt1