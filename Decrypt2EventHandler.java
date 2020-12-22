// Eric Landquist
// Virginia Tech
// July 3, 1998

// Decrypt2EventHandler.java

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.io.*;

class Decrypt2EventHandler implements ActionListener {
	public static final int D1 = 1;
	public static final int SCREEN_2 = 2;
	public static final int QUIT = 3;

	int id;
	Decrypt2 app;

	Decrypt2EventHandler(int type, Decrypt2 app) {
		this.id = type;
		this.app = app;
	}

// Method: 	actionPerformed
// Purpose:	To handle events from the screens.
// Parameters:	e - The action performed

	public void actionPerformed(ActionEvent e) {
		switch (id) {
			case D1:
				app.setVisible(false);
				Decrypt1 decrypt = new Decrypt1(app.offset);
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
