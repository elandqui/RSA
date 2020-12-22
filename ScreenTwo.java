// Eric Landquist

// June 4, 1998


// ScreenTwo.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

class ScreenTwo extends Frame {

	CheckboxGroup selection;
	Checkbox encrypt;
	Checkbox decrypt;

	BigInteger offset;

	public ScreenTwo (BigInteger offset){
		super("Encrypt or Decrypt");

		this.offset = offset;
		
		this.setSize(300, 300);
		this.setVisible(true);
		this.setLayout(new GridLayout(2, 1));
		
		selection = new CheckboxGroup();

		encrypt = new Checkbox("Encrypt         ", false, selection);
		decrypt = new Checkbox("Decrypt         ", false, selection);
		
		this.add(encrypt);
		this.add(decrypt);

		Button ok = new Button (" OK ");
		Button exit = new Button ("Exit");

		exit.addActionListener(new ScreenTwoEventHandler(ScreenTwoEventHandler.QUIT, this));
		ok.addActionListener(new ScreenTwoEventHandler(ScreenTwoEventHandler.CHOICE, this));

		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(exit);

		this.add(buttonPanel);
		this.pack();
	}

//	public static void main(String args[]){
//		new ScreenTwo();
//	}

} // end class ScreenTwo