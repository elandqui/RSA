// Eric Landquist
// Virginia Tech
// July 3, 1998

// Decrypt1EventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Decrypt1EventHandler implements ActionListener {
	public static final int D2 = 1;
	public static final int SCREEN_2 = 2;
	public static final int QUIT = 3;

	int id;
	Decrypt1 app;

	Decrypt1EventHandler(int type, Decrypt1 app) {
		this.id = type;
		this.app = app;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (id) {
			case D2:
				Decrypt2 decrypt2;
				String message = "";
				RandomAccessFile keyFile;
				RandomAccessFile cipherFile;
				BigInteger modulus;
				BigInteger key;
				
				// Getting the decryption keys.
				if(app.findInfo.getSelectedCheckbox() ==  app.defaultSet) {
					try {
						keyFile = new RandomAccessFile(PublicKey.KEY_INFO, "r");
					} catch(IOException ioe) {
						new ErrorFrame ("Cannot get decrypting keys.");
						break;
					}

					byte[] buffer = new byte[512];
					byte[] modBytes = new byte[171];
					byte[] decBytes = new byte[171];
			
					try  {
						keyFile.read(buffer);
						keyFile.close();
					} catch (IOException ioe) {
						new ErrorFrame("Cannot read key information.");
					}

					System.arraycopy(buffer, 0, modBytes, 0, 171);
					System.arraycopy(buffer, 341, decBytes, 0, 171);

					modulus = new BigInteger(modBytes);
					key = new BigInteger(decBytes).subtract(app.offset);					
				}

				else if (app.findInfo.getSelectedCheckbox() ==  app.fromFileCh) {
					try {
						keyFile = new RandomAccessFile(app.fileField.getText(), "r");
					} catch(IOException ioe) {
						new ErrorFrame ("Cannot get decrypting keys.");
						break;
					}

					byte[] buffer = new byte[512];
					byte[] modBytes = new byte[171];
					byte[] decBytes = new byte[171];

					try {
						keyFile.read(buffer);
						keyFile.close();
					} catch(IOException ioe) {
						new ErrorFrame("Cannot read key information.");
					}

					System.arraycopy(buffer, 0, modBytes, 0, 171);
					System.arraycopy(buffer, 341, decBytes, 0, 171);

					modulus = new BigInteger(modBytes);
					key = new BigInteger(decBytes).subtract(app.offset);
				}

				// The modulus and key are specified.
				else if (app.findInfo.getSelectedCheckbox() ==  app.modAndKey){
					try {
						modulus = new BigInteger(app.modField.getText());
						key = new BigInteger(app.keyField.getText());
					} catch (NumberFormatException nfe) {
						new ErrorFrame("Modulus and/or decryption key not given.");
						break;
					}
					if(message.equals("")){
						try {
							cipherFile = new RandomAccessFile(app.inFile.getText(), "r");
						} catch(IOException ioe) {
							new ErrorFrame("Could not open cipher text.");
							break;
						}
						decrypt2 = new Decrypt2(cipherFile, app.outFile.getText(), modulus, key, app.offset);
					}
					
					else 
						decrypt2 = new Decrypt2(message, app.outFile.getText(), modulus, key, app.offset);
					app.setVisible(false);
					app.dispose();
				}

				else {
					new ErrorFrame("No decryption method selected.");
					return;
				}

				// Find the message.
				message = app.theCipher.getText();
				if( message.equals("")) {
					// Try to get the message from a file.
					if (app.inFile.getText().equals("")) {
						new ErrorFrame("No message to decrypt.");
						break;
					}
					else {
						try {
							cipherFile = new RandomAccessFile(app.inFile.getText(), "r");
						} catch (IOException ioe) {
							new ErrorFrame("Could not open " + app.inFile.getText() +".");
							break;
						}
						decrypt2 = new Decrypt2(cipherFile, app.outFile.getText(), modulus, key, app.offset);
					}
				}

				
				break;
			case SCREEN_2:
				app.setVisible(false);
				ScreenTwo screenTwo = new ScreenTwo(app.offset);
				app.dispose();
				break;

			case QUIT:
				app.setVisible(false);
				app.dispose();
				System.exit(1);
				break;
		}
	}
}