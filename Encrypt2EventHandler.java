// Eric Landquist
// Virginia Tech
// July 14, 1998

// Encrypt2EventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Encrypt2EventHandler implements ActionListener {
	public static final int E1 = 1;
	public static final int SCREEN_2 = 2;
	public static final int QUIT = 3;

	int id;
	Encrypt2 app;

	Encrypt2EventHandler(int type, Encrypt2 app) {
		this.id = type;
		this.app = app;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (id) {
			case E1:
				app.setVisible(false);
				Encrypt1 encrypt = new Encrypt1(app.offset);
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
