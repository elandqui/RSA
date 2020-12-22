// Eric Landquist
// Virginia Tech
// June 30, 1998

// Encrypt1EventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Encrypt1EventHandler implements ActionListener {
	public static final int E2 = 1;
	public static final int SCREEN_2 = 2;
	public static final int QUIT = 3;

	int id;
	Encrypt1 app;

	Encrypt1EventHandler(int type, Encrypt1 app) {
		this.id = type;
		this.app = app;
	}

	// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (id) {
			case E2:
				Encrypt2 encrypt2;
				// If no filename was specified, check the screen.
				if (app.inFile.getText().equals("")){
					// If no message was written in the text area to be encrypted.
					if (app.screen.getText().equals("")) {
						new ErrorFrame("No message to encrypt.");
						break;
					}

					// If there is a message, we encrypt it, but we first need to find
					// the modulus and key.
					// Get that info from either a file or stright from the input.
					if (!app.nameField.getText().equals("")) {
						// From here, get the info from a hash table.
					}
				
					else {
						// Check to make sure there are values entered.
						if ( app.modulus.getText().equals("") || app.key.getText().equals("")){
							new ErrorFrame("Missing key information: Need modulus and key, or a recipient.");
							break;
						}
						encrypt2 = new Encrypt2(app.screen.getText(), app.outFile.getText(), new BigInteger(app.modulus.getText()), new BigInteger(app.key.getText()), app.offset, app.charSet);
					}
				}

				else {
					try {
						RandomAccessFile inFile = new RandomAccessFile(app.inFile.getText(), "r");
					
						encrypt2 = new Encrypt2(inFile, app.outFile.getText(), new BigInteger(app.modulus.getText()), new BigInteger(app.key.getText()), app.offset);
					} catch(IOException ioe) {
						new ErrorFrame ("Could not open " + app.inFile.getText());
						break;
					}
				}

				app.setVisible(false);
				app.dispose();

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
