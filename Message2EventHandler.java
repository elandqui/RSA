// Eric Landquist
// Virginia Tech
// July 6, 1998

// Message2EventHandler.java

import java.awt.event.*;
import java.io.*;

class Message2EventHandler implements ActionListener {

	public static final int DISPLAY = 1;
	public static final int HIDE = 2;
	public static final int CLEAR = 3;

	Decrypt2 app;
	int type;

	Message2EventHandler(Decrypt2 app, int type) {
		this.app = app;
		this.type = type;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch(type) {
			// This option is to display a message.
			case DISPLAY:
				// If the message now is blank
				if (app.message.equals("") && app.plaintext.getText().equals("") ) {
					String filename = app.inWhatFile.getText();
					// If no filename is given.
					if (filename.equals("")) {
						new ErrorFrame("No file name specified.");
						break;
					}

					// If a message is already displayed.
					else if (!app.plaintext.getText().equals("")) {
						new ErrorFrame("Text already displayed. You may clear the screen and then display contents of a specified file.");
						break;
					}
					else {
						try {
							String temp;
							BufferedReader input = new BufferedReader(new FileReader(filename));
							app.plaintext.setText(temp = input.readLine());
							while (temp != null){
								app.plaintext.append(input.readLine());	
							}
							app.message = app.plaintext.getText();
						} catch(IOException ioe) {
							new ErrorFrame("Cannot read from file: " + filename);
							break;
						}
					}	
				}
				// Otherwise we just set the text.
				else {
					app.plaintext.setText(app.message);
				}
			
				break;

			case HIDE:
				app.message = app.plaintext.getText();
				app.plaintext.setText("");
				break;

			case CLEAR:
				app.plaintext.setText("");
				app.message = "";
				break;
		} // end switch
	} // end actionPerformed
} // end class Message2EventHandler