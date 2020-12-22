// Eric Landquist
// Virginia Tech 
// June 5, 1998

// ErrorFrame.java

import java.awt.*;
import java.awt.event.*;

class ErrorFrame extends Frame {

	String error;

	ErrorFrame(String error){
		super("Error.");
		this.error = error;

		Label errorMessage = new Label(error);

		Button ok = new Button("OK");
		ok.addActionListener(new ErrorFrameEventHandler(this));

		this.add("Center", errorMessage);

		Panel buttonPanel = new Panel();
		buttonPanel.add(ok);

		this.add("South", buttonPanel);


		this.pack();
		this.setVisible(true);		
	}
} // end class ErrorFrame