// Eric Landquist
// Virginia Tech

// June 6, 1998

// Encrypt2.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Encrypt2 extends Frame{

	static TextArea cipherText;

	static String message;
	static String outFile;
	static BigInteger modulus;
	static BigInteger key;

	static Label process;
	static Label cipherFileName;
	static Label percentage;

	BigInteger offset;

	public Encrypt2(String message, String outFile, BigInteger modulus, BigInteger key, BigInteger offset, CheckboxGroup method) {
		super("Encryption Process");
		this.message = message;
		this.outFile = outFile;
		this.modulus = modulus;
		this.key = key;
		this.offset = offset;

		this.setSize(500, 500);
		this.setVisible(true);

		Button back = new Button("<< Back ");
		Button exit = new Button("  Exit  ");
		Button menu = new Button("Main Menu");

		back.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.E1, this));
		exit.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.QUIT, this));
		menu.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.SCREEN_2, this));

		percentage = new Label("0 %");
		process = new Label("Encryption Process");
		
		cipherText = new TextArea();

		Label cipherIn = new Label("Encrypted Message in: ");
		cipherFileName = new Label(outFile);

		this.setLayout(new BorderLayout());

		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		top.add("North", percentage);
		top.add("Center", process);
		top.add("South", cipherText);

		Panel middle = new Panel();
		middle.setLayout(new FlowLayout());
		middle.add(cipherIn);
		middle.add(cipherFileName);

		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());
		bottom.add(back);
		bottom.add(exit);
		bottom.add(menu);

		this.add("North", top);
		this.add("Center", middle);
		this.add("South", bottom);

		this.pack();
	
		encrypt(method);
	}

	public Encrypt2(RandomAccessFile inFile, String outfile, BigInteger modulus, BigInteger key, BigInteger offset) {
		super("Encryption Process");
		this.message = message;
		this.outFile = outFile;
		this.modulus = modulus;
		this.key = key;
		this.offset = offset;

		this.setSize(500, 500);
		this.setVisible(true);

		Button back = new Button("<< Back ");
		Button exit = new Button("  Exit  ");
		Button menu = new Button("Main Menu");

		back.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.E1, this));
		exit.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.QUIT, this));
		menu.addActionListener(new Encrypt2EventHandler(Encrypt2EventHandler.SCREEN_2, this));

		percentage = new Label("0 %");
		process = new Label("Encryption Process");
		
		cipherText = new TextArea();

		Label cipherIn = new Label("Encrypted Message in: ");
		cipherFileName = new Label(outFile);

		this.setLayout(new BorderLayout());

		Panel top = new Panel();
		top.setLayout(new BorderLayout());
		top.add("North", percentage);
		top.add("Center", process);
		top.add("South", cipherText);

		Panel middle = new Panel();
		middle.setLayout(new FlowLayout());
		middle.add(cipherIn);
		middle.add(cipherFileName);

		Panel bottom = new Panel();
		bottom.setLayout(new FlowLayout());
		bottom.add(back);
		bottom.add(exit);
		bottom.add(menu);

		this.add("North", top);
		this.add("Center", middle);
		this.add("South", bottom);

		this.pack();
	
		encrypt(inFile);
	}

// Method:	encrypt()
// Purpose:	To run the encryption of the message.
// Parameters:	method - the type of data: 1 or 2 byte characters

	public static void encrypt(CheckboxGroup method) {
		RandomAccessFile output;

		// The block size to work with.
		int blocksize = modulus.toByteArray().length-1;

		// A piece of the plaintext.
		byte[] tempBytes = new byte[blocksize];
		
		// A piece of the ciphertext.
		byte[] tempCipher;
		// The whole plaintext.
		byte[] buffer;
		// The whole ciphertext.
		byte[] cipherBuffer;

		try {
			output = new RandomAccessFile(outFile, "rw");
		} catch(IOException ioe) { 
			new ErrorFrame("Cannot open output file.");
			return;
		} catch (NullPointerException nfe) {
			new ErrorFrame("No output file specified.");
			return;
		}

		// Keeps track of the percentage of the file has been encrypted.
		int percent = 0;		

		// Convert the string to bytes somehow.
//		if (method.getSelectedCheckbox() == Encrypt1.ascii){
			buffer = message.getBytes();
			// The plaintext blocksize, after encrypted could be the same length as the modulus, so we make this buffer large enough.
			cipherBuffer = new byte[(blocksize+1)*(int)Math.ceil((double)buffer.length/(double)blocksize)];
//		}
//		else {
//		}
		
		for (int i=0; i<Math.ceil((double)buffer.length/(double)blocksize); i++) {
			// Now we break up the string into blocks that are less than
			// the modulus, and encrypt each one.
			System.arraycopy(buffer, i*blocksize, tempBytes, 0, blocksize);

			// The key encryption step.
			tempCipher = new BigInteger(tempBytes).modPow(key, modulus).toByteArray();

			System.out.println(tempCipher.length);

			for(int k=0; k<(blocksize+1-tempCipher.length); k++)
					cipherBuffer[i*blocksize+1+k] = 0;

			System.out.println(cipherBuffer[0]);

			// Stuffing the ciphertext back into the buffer.
			System.arraycopy(tempCipher, 0, cipherBuffer, i*(blocksize+1)+(blocksize+1-tempCipher.length), tempCipher.length);

			// Updating the percentage.
			percent = (int)(100*(i+1)/(buffer.length/blocksize));
			percentage.setText(percent + " %");
		}

		// Now the whole encrypted message is back in buffer, so we
		// put it in the outfile.
		try {
			output.write(cipherBuffer);
			output.close();
		} catch (IOException ioe) {
			new ErrorFrame("Could not close file. Encrypted message is in the text area.");
			cipherText.setText(new String(buffer));
			return;
		} catch (NullPointerException nfe) {
			new ErrorFrame("No output file specified. Writing encrypted message in text area.");
		}

		cipherText.setText(new String(cipherBuffer));

	} // end encrypt();

// Method:	encrypt()
// Purpose:	To run the encryption of the message.
// Parameters:	inFile - the file containing the message we want to encrypt.

	public static void encrypt(RandomAccessFile inFile) {
		String ciphertext="";
		long fileLength;
		long totalRead=0;
		RandomAccessFile output;

		// The block size to work with.
		int blocksize = modulus.toByteArray().length-1;

		byte[] tempBytes = new byte[blocksize];
		byte[] tempCipher;

		try {
			output = new RandomAccessFile(outFile, "rw");
			fileLength = inFile.length(); 
		} catch(IOException ioe) {
			return;
		}

		// Keeps track of the percentage of the file has been encrypted.
		int percent = 0;
		
		int bytesRead = 100*blocksize;

		// A buffer that can hold 100 blocks of plaintext.
		byte[] buffer = new byte[bytesRead];
		
		// A buffer that can hold 100 blocks of ciphertext.
		byte[] cipherBuffer = new byte[100*(blocksize+1)];

		while (bytesRead != -1) {
			try {
				bytesRead = inFile.read(buffer);
				totalRead += bytesRead;
			} catch (IOException ioe) {
				new ErrorFrame("Cannot read from file.");
				break;
			}
			int i=0;
			if(bytesRead == -1){
				new ErrorFrame ("No message to encrypt");
				return;
			}

			for(i=0; i<Math.ceil((double)bytesRead/(double)blocksize); i++){

				// Now we break up the string into blocks that are less than
				// the modulus, and encrypt each one.
				System.arraycopy(buffer, i*blocksize, tempBytes, 0, blocksize);

				// The key encryption step.
				tempCipher = new BigInteger(tempBytes).modPow(key, modulus).toByteArray();

				for(int k=0; k<(blocksize+1-tempCipher.length); k++)
					cipherBuffer[i*blocksize+1+k] = 0;

				// Stuffing the ciphertext back into the buffer.
				System.arraycopy(tempCipher, 0, cipherBuffer, i*(blocksize+1)+(blocksize+1-tempCipher.length), tempCipher.length);

				// Updating the percentage.
				percent = (int)(100*totalRead/fileLength);
				percentage.setText(percent + " %");
			}

			// Now the whole encrypted message is in ciphertext, so we
			// put it in the outfile.
			try {
				output.write(cipherBuffer);
			} catch (IOException ioe) {
				new ErrorFrame("Could not write to file. Terminating encryption.");
				return;
			}

			cipherText.append(new String(cipherBuffer));
		}
		try {
			output.close();
		} catch(IOException ioe){
			new ErrorFrame("Could not close file.");
		}

	} // end encrypt();

// Method:	stringToInts
// Purpose:	To convert the message in input to a String of ints
// Parameter:	message - the message that will be encrypted

	static String stringToInts (String message) {
		// Checking if there is a message.
		if(message == null)
			return "0";

		// If there is a message, we proceed.
		String ints = "";
		int temp;

		// Running through the string.
		for(int i=0; i<message.length(); i++) {
			// Convert each charachter to an integer.
			temp = (int)message.charAt(i);
				
			// We we only use Unicode 1.1.5 chars \u0000
			// through \u00FF, since anything beyond that
			// are international chars.
			// Thus all digits used are three digits
			if (temp > 256)
				temp%=256;

			// Adding on the necessary digits:
			if (temp > 99)
				ints = ints + String.valueOf(temp);

			else if ( temp > 9)
				ints = ints + "0" + String.valueOf(temp);
				
			else
				ints = ints + "00" + String.valueOf(temp);

			// Done with that character.
		} // end for loop

		// Now we return the String of integers.
		return ints;

	} // end stringToInts

} // end class Encrypt2