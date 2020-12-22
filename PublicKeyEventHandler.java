// Eric Landquist
// Virginia Tech
// June 5, 1998

// PublicKeyEventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class PublicKeyEventHandler implements ActionListener {

	public final static int QUIT = 1;
	public final static int SCREEN_2 = 2;

	int id;
	PublicKey app;
	
	static int attempts;

	PublicKeyEventHandler(int type, PublicKey app) {
		this.id = type;
		this.app = app;
		this.attempts = 0;
	}

	// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (id) {
			// If you want to exit the program.
			case QUIT:
				app.setVisible(false);
	 			app.dispose();
			  	System.exit(1);
	  			break;

			// Goes to the second screen.
			case SCREEN_2:
				RandomAccessFile loginFile;
				RandomAccessFile keyFile;
				long time;
				// Only three chances to login are allowed for an hour.
				if (attempts == 3) {
					try {

						File fileTimeOut = new File(app.TIMEOUT);
						long length = fileTimeOut.length();
					
						if (length == 0) {
							PrintWriter timeout = new PrintWriter(new FileWriter(app.TIMEOUT));
							time = System.currentTimeMillis();
							timeout.println(time);
							new ErrorFrame("Three failed attempts. Cannot log in.");
							System.out.println("Three failed attempts. Cannot log in.");
							app.setVisible(false);
	 						app.dispose();
							timeout.close();
							System.exit(1);
							break;
						}
						else {

							BufferedReader check = new BufferedReader(new FileReader(app.TIMEOUT));
							time = Long.parseLong(check.readLine());
							// An hour in milliseconds.
							if (time-System.currentTimeMillis() < 3600000 ){
								new ErrorFrame("Three failed attempts. Cannot log in.");
								System.out.println("Three failed attempts. Cannot log in.");
								app.setVisible(false);
		 						app.dispose();
								check.close();
								System.exit(1);
								break;
							}
							else {
								attempts = 0;
								check.close();
							}
						}
					} catch(IOException ioe) {
					}
				}
				// First check the login and password.
				byte[] modBytes, encBytes, keyBuffer, accntBytes;
				
				try {
					// First check to make sure the person can log in.
					File fileTimeOut = new File(app.TIMEOUT);
					long length = fileTimeOut.length();
					
					BufferedReader check = new BufferedReader(new FileReader(app.TIMEOUT));
					time = Long.parseLong(check.readLine());
					// An hour in milliseconds.
					if (time-System.currentTimeMillis() < 3600000 ){
						new ErrorFrame("Three failed attempts. Cannot log in.");
						System.out.println("Three failed attempts. Cannot log in.");
						app.setVisible(false);
		 				app.dispose();
						check.close();
						System.exit(1);
						break;
					}
				} catch(IOException ioe){
				}

				try {
					// Otherwise, if all is cool, we proceed.
					loginFile = new RandomAccessFile(app.LOGIN_INFO, "r");
					keyFile = new RandomAccessFile(app.KEY_INFO, "r"); 
					modBytes = new byte[171];
					encBytes = new byte[170];

					// Dig out the key and account info.
					keyBuffer = new byte[512];
					accntBytes = new byte[512];

					keyFile.read(keyBuffer);
					loginFile.read(accntBytes);					
				} catch(IOException ioe) {
					new ErrorFrame("Unable to open account information.");
					break;
				}
				System.arraycopy(keyBuffer, 0, modBytes, 0, 171);
				System.arraycopy(keyBuffer, 171, encBytes, 0, 170);
									
				// The user's encryption modulus and encryption key.
				BigInteger modulus = new BigInteger(modBytes);
				BigInteger key = new BigInteger(encBytes);
				
				// Now we apply the encryption protocol to the login name and password to check it.									
				BigInteger logInt = new BigInteger(Encrypt2.stringToInts(app.login.getText())).modPow(key, modulus);				
				BigInteger passInt = new BigInteger(Encrypt2.stringToInts(app.password.getText())).modPow(key, modulus);

				// Dig out the account information.			
				byte[] logByte = new byte[256];
				byte[] passByte = new byte[256];
				System.arraycopy(accntBytes, 0, logByte, 0, logByte.length);
				System.arraycopy(accntBytes, 256, passByte, 0, passByte.length);					
				
				// The real login and password.
				BigInteger login = new BigInteger(logByte);
				BigInteger password = new BigInteger(passByte);

				// Compare the two. If they match, then enter the program
				if (login.equals(logInt) && password.equals(passInt)) {			
					app.setVisible(false);

					// The quantity to be subtracted from the stored decryption key
					// to get the actual decryption key.
					BigInteger offset = password.modPow(app.HIDE_EXP, modulus);	// Program dies here.
					
					ScreenTwo screenTwo = new ScreenTwo(offset);
					
					app.dispose();
					try {
						loginFile.close();
						keyFile.close();
					} catch(IOException ioe){
						new ErrorFrame("Unable to close account information.");
						break;
					}
				}
				else {
					new ErrorFrame("Wrong login and/or password.");
					attempts++;
				}
				
				break;			
		}
	}
}