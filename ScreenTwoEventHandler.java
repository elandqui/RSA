// Eric Landquist
// Virginia Tech
// June 5, 1998

// ScreenTwoEventHandler.java

import java.awt.event.*;

class ScreenTwoEventHandler implements ActionListener {

	public final static int QUIT = 0;
	public final static int CHOICE = 2;

	int id;
	ScreenTwo app;

	ScreenTwoEventHandler(int type, ScreenTwo app) {
		this.id = type;
		this.app = app;
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
			case CHOICE:
				if (app.selection.getSelectedCheckbox() == app.encrypt) {
					// Then we go to the first encryption screen.
					Encrypt1 encrypt = new Encrypt1(app.offset);
					encrypt.setVisible(true);
				}
		
				else {
					// We go to the first decryption screen.
					new Decrypt1(app.offset);
				}

				app.setVisible(false);
				app.dispose();
				break;							
		} // end switch

	} // end actionPerformed()


} // end ScreenTwoEventHandler