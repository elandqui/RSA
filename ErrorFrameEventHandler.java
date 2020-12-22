// Eric Landquist
// Virginia Tech

// July 3, 1998

// ErrorFrameEventHandler.java

import java.awt.event.*;
import java.io.*;

class ErrorFrameEventHandler implements ActionListener {

	ErrorFrame app;

	ErrorFrameEventHandler (ErrorFrame app) {
		this.app = app;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		app.setVisible(false);
		app.dispose();
	}

}