// Eric Landquist
// Virginia Tech

// June 15, 1998

// Decrypt2.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Decrypt2 extends Frame {

	static String message;
	static String outFile;
	static BigInteger modulus;
	static BigInteger key;

	static TextArea plaintext;

	static Label percentage;
	static Label inWhatFile;
	
	BigInteger offset;

	public Decrypt2(String message, String outFile, BigInteger modulus, BigInteger key, BigInteger offset){
		super("Decryption Process");

		this.message = message;
		this.outFile = outFile;
		this.modulus = modulus;
		this.key = key;
		this.offset= offset;

		Label process = new Label("Decryption Process");

		plaintext = new TextArea(outFile);

		percentage = new Label("0 %");
		
		Label plaintextIn = new Label("Decrypted message in: ");
		Label inWhatFile = new Label();

		Button display = new Button("Display");
		Button hide = new Button(" Hide  ");
		Button clear = new Button(" Clear "); 

		display.addActionListener(new Message2EventHandler(this, Message2EventHandler.DISPLAY));
		hide.addActionListener(new Message2EventHandler(this, Message2EventHandler.HIDE));
		clear.addActionListener(new Message2EventHandler(this, Message2EventHandler.CLEAR));

		Button menu = new Button("Main Menu");
		Button back = new Button(" < Back  ");
		Button logout = new Button(" Logout  ");

		menu.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.SCREEN_2, this));
		back.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.D1, this));
		logout.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.QUIT, this));

		this.setLayout(new BorderLayout());
		
		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		top.add("North", percentage);
		top.add("Center", process);

		Panel middle = new Panel();
		middle.setLayout(new BorderLayout());

		Panel buttons1 = new Panel();
		buttons1.setLayout(new FlowLayout());
		buttons1.add(display);
		buttons1.add(hide);
		buttons1.add(clear);
		
		middle.add("North", plaintext);
		middle.add("Center", buttons1);
		
		Panel bottom = new Panel();
		bottom.setLayout(new GridLayout(2, 1));
		Panel bottomtop = new Panel();
		bottomtop.setLayout(new FlowLayout());
		bottomtop.add(plaintextIn);
		bottomtop.add(inWhatFile);
		bottom.add(bottomtop);

		Panel bottombottom = new Panel();
		bottombottom.setLayout(new FlowLayout());
		bottombottom.add(menu);
		bottombottom.add(back);
		bottombottom.add(logout);

		bottom.add(bottombottom);

		this.add("North", top);
		this.add("Center", middle);
		this.add("South", bottom);

		this.pack();
		this.setVisible(true);

		// Now we begin decrypting!!!
		decrypt(message, modulus, key);
	}

	public Decrypt2(RandomAccessFile inputFile, String outFile, BigInteger modulus, BigInteger key, BigInteger offset){
		super("Decryption Process");

		this.message = message;
		this.outFile = outFile;
		this.modulus = modulus;
		this.key = key;
		this.offset= offset;

		Label process = new Label("Decryption Process");

		plaintext = new TextArea();

		percentage = new Label("0 %");
		
		Label plaintextIn = new Label("Decrypted message in: ");
		Label inWhatFile = new Label(outFile);

		Button display = new Button("Display");
		Button hide = new Button(" Hide  ");
		Button clear = new Button(" Clear "); 

		display.addActionListener(new Message2EventHandler(this, Message2EventHandler.DISPLAY));
		hide.addActionListener(new Message2EventHandler(this, Message2EventHandler.HIDE));
		clear.addActionListener(new Message2EventHandler(this, Message2EventHandler.CLEAR));

		Button menu = new Button("Main Menu");
		Button back = new Button(" < Back  ");
		Button logout = new Button(" Logout  ");

		menu.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.SCREEN_2, this));
		back.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.D1, this));
		logout.addActionListener(new Decrypt2EventHandler(Decrypt2EventHandler.QUIT, this));

		this.setLayout(new BorderLayout());
		
		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		top.add("North", percentage);
		top.add("Center", process);

		Panel middle = new Panel();
		middle.setLayout(new BorderLayout());

		Panel buttons1 = new Panel();
		buttons1.setLayout(new FlowLayout());
		buttons1.add(display);
		buttons1.add(hide);
		buttons1.add(clear);
		
		middle.add("North", plaintext);
		middle.add("Center", buttons1);
		
		Panel bottom = new Panel();
		bottom.setLayout(new GridLayout(2, 1));
		Panel bottomtop = new Panel();
		bottomtop.setLayout(new FlowLayout());
		bottomtop.add(plaintextIn);
		bottomtop.add(inWhatFile);
		bottom.add(bottomtop);

		Panel bottombottom = new Panel();
		bottombottom.setLayout(new FlowLayout());
		bottombottom.add(menu);
		bottombottom.add(back);
		bottombottom.add(logout);

		bottom.add(bottombottom);

		this.add("North", top);
		this.add("Center", middle);
		this.add("South", bottom);

		this.pack();
		this.setVisible(true);

		// Now we begin decrypting!!!
		decrypt(inputFile, modulus, key);
	}

// Method: 	decrypt
// Purpose:	To decrypt a message and put it into the output file.
// Parameters:	message - the input file
//		output - the output file
//		modulus - the modulus
//		key - the key

	public static void decrypt (String message, BigInteger modulus, BigInteger key) {
		RandomAccessFile output;
		try {
			output = new RandomAccessFile(outFile, "rw");
		} catch (IOException ioe) {
			new ErrorFrame("Could not open output file.");
			return;
		}

		// The block size to work with.
		int blocksize = modulus.toByteArray().length;

		// We have a buffer, which will allow us to read in 100 encrypted blocks.
		byte[] buffer = new byte[100*blocksize];
		
		// This buffer is the one that the plaintext will be going into.
		byte[] plainBuffer = new byte[100*(blocksize-1)];
		
		// Keeping track of the process of the decryption.
		int percent=0;

		// First we read in the ciphertext. The way the encryption
		// step is set up, we should have the whole ciphertext in
		// the first read.
		String ciphertext = "";

		// Divide it into chunks each the length of the modulus,
		// and decrypt each one.
		int length = modulus.toString().length();
		char[] chars = new char[length];
		String plaintext = "";
		for (int i=0; i<ciphertext.length()/length; i++) {
			ciphertext.getChars(i*length, (i+1)*length, chars, 0);

			// The key decryption step.
			String temp = new BigInteger(new String(chars, 0, length)).modPow(key, modulus).toString();

			// Making sure that all the chunks we add onto the
			// plaintext are the same length.
			while (temp.length() < length - 1) 	
				temp = "0" + temp;

			// Tacking on the new numerical plaintext.
			plaintext = plaintext + temp;

			percent = (int)(100*(i+1)/(ciphertext.length()/length));
			percentage.setText(percent + " %");
		}

		// Now in plaintext we have the decrypted message, but in
		// Unicode 1.1.5 form, so we need to convert it back to 
		// characters, and then to bytes.
		// and then send it directly to the output file.
		 
		// output.write(intsToString(plaintext));
	} // end decrypt()

// Method: 	decrypt
// Purpose:	To decrypt a message and put it into the output file.
// Parameters:	input - the input file
//		output - the output file
//		modulus - the modulus
//		key - the key

	public static void decrypt (RandomAccessFile input, BigInteger modulus, BigInteger key) {
		RandomAccessFile output;
		try {
			output = new RandomAccessFile(outFile, "rw");
		} catch (IOException ioe) {
			new ErrorFrame("Could not open output file.");
			return;
		}

		// The block size to work with.
		int blocksize = modulus.toByteArray().length;

		// We have a buffer, which will allow us to read in 100 encrypted blocks.
		byte[] buffer = new byte[100*blocksize];
		
		// Another buffer that will hold 100 plaintext blocks
		byte[] plainBuffer = new byte[100*(blocksize-1)];
		
		// A piece of the ciphertext.
		byte[] tempCipher = new byte[blocksize];
		
		// A piece of the plaintext.
		byte[] tempMessage = new byte[blocksize-1];
		
		// Keeping track of the process of the decryption.
		int percent=0;

		long fileLength;

		try {
			fileLength = input.length();
		} catch(IOException ioe) {
			new ErrorFrame("Could not find length.");
			fileLength = 100*blocksize;
		}

		for (int i=0; i < Math.ceil((double)fileLength/(double)(100*blocksize)); i++) {

 			try {
				int bytesRead = input.read(buffer);

				for (int j=0; j<Math.ceil((double)bytesRead/(double)blocksize); j++) {

					// Copying a part of the arry into a temp array.
					System.arraycopy(buffer, j*blocksize, tempCipher, 0, blocksize);

					// The key decryption step.
					tempMessage = new BigInteger(tempCipher).modPow(key, modulus).toByteArray();

					System.out.println(tempMessage.length);

					// Stuffing the plaintext back into the buffer.
					System.arraycopy(tempMessage, 0, plainBuffer, j*(blocksize-1), blocksize-1);

					// Updating the percentage.
					percent = (int)(100*(100*blocksize*i+blocksize*(j+1))/fileLength);
					percentage.setText(percent + " %");
				}
				output.write(tempMessage);

			} catch (IOException ioe) {
				new ErrorFrame ("Could not write to or read file.");
				return;
			}
		}

		try {
			output.close();
		} catch(IOException ioe) {
			new ErrorFrame("Could not close ouput file.");
			return;
		}

	} // end decrypt()

// Method: 	intsToString
// Purpose:	To convert a String of ints to a message using the encoding
//			of Unicode 1.1.5.
// Parameter:	ints - the String that contains the integers.

	static String intsToString (String ints) {
		String message;

		// The integers are broken up into groups of 3, then
		// converted to chars.
		char[] miniInt = new char[3];
		char[] plaintext = new char[ints.length()/3];
		for (int i=0; i<ints.length()/3; i++) {
			// Put the int into a char array.
			ints.getChars(i*3, (i+1)*3, miniInt, 0);

			// And convert that to a String, then an int, and
			// finally a char.
			plaintext[i] = (char)Integer.parseInt(new String(miniInt, 0, 3));
		}

		// Now the whole deciphered message is in plaintext, so we
		// convert it to a String and send it back.
		return (new String(plaintext, 0, plaintext.length));
	} // end intsToString()
}