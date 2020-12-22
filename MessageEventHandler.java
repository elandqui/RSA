// Eric Landquist
// Virginia Tech

// June 16, 1998

// MessageEventHandler.java

import java.awt.event.*;
import java.io.*;

class MessageEventHandler implements ActionListener {

	public static final int DISPLAY = 1;
	public static final int HIDE = 2;
	public static final int CLEAR = 3;

	Encrypt1 app;
	int type;

	MessageEventHandler(Encrypt1 app, int type) {
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
				if (app.message.equals("") && app.screen.getText().equals("") ) {
					String filename = app.inFile.getText();
					// If no filename is given.
					if (filename.equals("")) {
						new ErrorFrame("No file name specified.");
						break;
					}

					// If a message is already displayed.
					else if (!app.screen.getText().equals("")) {
						new ErrorFrame("Text already displayed. You may clear the screen and then display contents of a specified file.");
						break;
					}
					else {
						try {
							String temp;
							BufferedReader input = new BufferedReader(new FileReader(filename));
							app.screen.setText(temp = input.readLine());
							while (temp != null){
								app.screen.append(input.readLine());	
							}
							app.message = app.screen.getText();
						} catch(IOException ioe) {
							new ErrorFrame("Cannot read from file: " + filename);
							break;
						}
					}	
				}
				// Otherwise we just set the text.
				else {
					app.screen.setText(app.message);
				}
			
				break;

			case HIDE:
				app.message = app.screen.getText();
				app.screen.setText("");
				break;

			case CLEAR:
				app.screen.setText("");
				app.message = "";
				break;
		} // end switch
	} // end actionPerformed
} // end class MessageEventHandler