//Eric Landquist
//Virginia Tech

// July 6, 1998

//CreateAccountEventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;
import java.util.Random;

class CreateAccountEventHandler implements ActionListener {

	public static final int OK = 1;
	public static final int QUIT = 2;

	int type;
	CreateAccount app;

	CreateAccountEventHandler ( int type, CreateAccount app) {
		this.type = type;
		this.app = app;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (type) {
			// If you want to exit the program.
			case QUIT:
				app.setVisible(false);
	 			app.dispose();
			  	System.exit(1);
	  			break;

			case OK:
				// Now we put the info in the file, and create all the keys.
				app.status.setText("Checking password ... ");

				if (!app.password.getText().equals(app.password2.getText()) ) {
					new ErrorFrame("Passwords do not match. Try Again.");
					break;
				}

				app.status.setText("Password confirmed. Creating keys. This will take several minutes.");
				BigInteger[] keys = createKeys();

				// We will store the data in a file as follows: 
				// modulus in the first 171 bytes.
				// encrypting key in the next 170 bytes.
				// decrypting key int the last 171 bytes.
				// Total of 512 bytes, or 0.5KB.

				byte[] binKeys = new byte[512];
				byte[] modByte = keys[0].toByteArray();
				byte[] encByte = keys[1].toByteArray();
				
				// The modulus will have 171 bytes, the encrypting exponent 170,
				// and the decrypting exponent 171 bytes, for a total of 512 bytes or 0.5KB.
				System.arraycopy(modByte, 0, binKeys, 171-modByte.length, modByte.length);
				System.arraycopy(encByte, 0, binKeys, 341-encByte.length, encByte.length);
				// We will hide the decryption key, so we don't put it in yet.

				app.status.setText("Keys Created. Creating account information.");

				// Apply the encryption protocol to the account information and put them in a file.
				BigInteger logInt = new BigInteger(Encrypt2.stringToInts(app.login.getText())).modPow(keys[1], keys[0]);				
				BigInteger passInt = new BigInteger(Encrypt2.stringToInts(app.password.getText())).modPow(keys[1], keys[0]);

				// Now we store this information in the binary file LOGIN_INFO.
				try {
					RandomAccessFile loginInfo = new RandomAccessFile(PublicKey.LOGIN_INFO, "rw");
				
					byte[] accntBytes = new byte[512];
					byte[] logByte = logInt.toByteArray();
					byte[] passByte = passInt.toByteArray();
					System.arraycopy(logByte, 0, accntBytes, 256-logByte.length, logByte.length);
					System.arraycopy(passByte, 0, accntBytes, 512-passByte.length, passByte.length);

					loginInfo.write(accntBytes);
					loginInfo.close();

					//Now we want to hide the decryption key.
					keys[2] = keys[2].add(new BigInteger(Encrypt2.stringToInts(app.password.getText())).modPow(new BigInteger(String.valueOf(PublicKey.HIDE_EXP)), keys[0]));
					byte[] decByte = keys[2].toByteArray();
					System.arraycopy(decByte, 0, binKeys, 512-decByte.length, decByte.length);
					RandomAccessFile keyInfo = new RandomAccessFile(PublicKey.KEY_INFO, "rw");
					keyInfo.write(binKeys);				
					keyInfo.close();

				} catch (IOException ioe) {
					new ErrorFrame("Could not store account information.");
					break;
				}
				app.status.setText("Creating a web page in HTML format with your key information.");

				// Modulus and encrypting exponent.
				createHTML(keys[0].toString(), keys[1].toString(), PublicKey.WEB_PAGE);

				app.status.setText("Done. Web page is in file. " + PublicKey.WEB_PAGE + " Ready to log in!");
				
				app.setVisible(false);
				app.dispose();
				break;
		}
	}


//
//
//
	public static void createHTML(String modulus, String encrypt, String filename) {
		try {
			PrintWriter webPage  = new PrintWriter(new FileWriter(filename));
			webPage.print("<html> \n <head> \n <title> Modulus and Public Key. </title> \n </head> \n <body bgcolor=\"#FFFFFF\"> \n <b>Modulus:</b> \n" + modulus + "\n\n <p><b>Encrypting Exponent:</b> \n" + encrypt + "\n\n </body> \n </html> ");
			webPage.close();
		} catch (IOException ioe){
			new ErrorFrame("Could not create web page.");
			return;
		}
		return;
	} // end createHTML

//
//
//
	public static BigInteger[] createKeys() {
		// modulus, encrypt, decrypt.
		BigInteger[] keys = new BigInteger[3];

		BigInteger one = new BigInteger("1");

		BigInteger prime1 = generate(50, 50); // Change to 200
		BigInteger prime2 = generate(50, 50); // Change to 200
		keys[0] = prime1.multiply(prime2);
		keys[1] = generate(5, 100);	// Change to 400
		try {
			keys[2] = keys[1].modInverse(prime1.subtract(one).multiply(prime2.subtract(one)));
		} catch (ArithmeticException ae) {
			boolean noInverse = false;
			do {
				keys[1] = generate(20, 100); // Change to 400
				try {
					keys[2] = keys[1].modInverse(prime1.subtract(one).multiply(prime2.subtract(one)));
				} catch (ArithmeticException ae2) {
					noInverse = true;
				}		
			} while (noInverse);
		}
		return keys;
	}

	public static BigInteger generate(int certainty, int length){
		BigInteger two = new BigInteger("2");

		BigInteger probPrime = getRandBigInt(length);
		
		if (probPrime.mod(two).equals(new BigInteger("0"))) 
			probPrime = probPrime.add(new BigInteger("1"));
		while (true) {
			if (probPrime.isProbablePrime(certainty)) {
				return probPrime;
			}
			probPrime = probPrime.add(two);
		}
	}


// Method 	getRandBigInt()
// Purpose 	To get a randon BigInteger of a given length.
// Parameters	length - the length of the random BigInteger

	public static BigInteger getRandBigInt(int length) {
		BigInteger randBigInt = new BigInteger(((int)(length*Math.log(10)/Math.log(2))), new Random());
		while (randBigInt.toString().length()<length) {	
			randBigInt = new BigInteger(((int)(length*Math.log(10)/Math.log(2))), new Random());
		}
		return randBigInt;
	}

}