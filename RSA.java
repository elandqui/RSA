// Eric Landquist
// Virginia Tech
// May 15, 1998
// RSA.java

// This program will implement the RSA public key cryptography algorithm.

import java.math.BigInteger;
import java.io.*;

class RSA {

	public RSA () {
	}

// Method:	main
// Purpose:	To run the RSA public key protocol.
// Parameters:	args: stores the input file, output file, if this will be 
//			encryption or decryption, and the file with the 
//			modulus and key information. Instead of the file, 
//			we may instead use a specified modulus and key.

	public static void main(String args[]) {
		BufferedReader input;
		PrintWriter output;
		BufferedReader info;

		if (args.length < 4 || args.length > 5) {
			error("Usage: java RSA [input_file] [output_file] [e, d] [key_file | modulus] [ | key]");
			return;
		}
		
		// Creating the input and output files.
		try {
			input = new BufferedReader( 
				new FileReader(args[0]));
			output = new PrintWriter(
				new FileWriter(args[1]));
		} catch (IOException ioe) {
			error("Could not open file: " + args[0] + " or " + args[1]);
			return;
		}

		// Declaring the modulus and the (public/private) key.
		BigInteger modulus, key;

		// If there are four arguments, then the fourth argument contains modulus information.
		// Then we open the file and dig out the info.
		if (args.length == 4) {
			try {
				info = new BufferedReader(
					new FileReader(args[3]));
	
				modulus = new BigInteger(info.readLine());
				key = new BigInteger(info.readLine());
				info.close();
			} catch (IOException ioe) {
				error("Could not open file: " + args[3]);
				return;
			}
		}
	
		// Otherwise we are given the modulus and the key:
		else {
			modulus = new BigInteger(args[3]);
			key = new BigInteger(args[4]);
		}

		// Now depending on the third argument, we encrypt or decrypt.
		if (args[2].equals("e") || args[2].equals("E")) {
			encrypt(input, output, modulus, key);
			System.out.println("Encrypted message in " + args[1]);
		}

		else if (args[2].equals("d") || args[2].equals("D")) {
			decrypt(input, output, modulus, key);
			System.out.println("Decrypted message in " + args[1]);
		}

		else {
			error("Usage: java RSA [input_file] [output_file] [e, d] [key_file | modulus] [ | key]");
			return;
		}

		try {
			// Close the files.
			input.close();
			output.close();
		} catch (IOException ioe) {
		}
	} // end main

// Method: 	error
// Purpose:	To display an error message.
// Parameter:	message: the error message.

	public static void error (String message) {
		System.out.println("Error: " + message);
	} // end error() 

// Method:	encrypt
// Purpose:	To encrypt a message and put it into the output file.
// Parameters:	input - the input file
//		output - the output file
//		modulus - the modulus
//		key - the key

	public static void encrypt (BufferedReader input, PrintWriter output, BigInteger modulus, BigInteger key) {
		// The block size to work with. We want each block to be a
		// multiple of 3.
		int blocksize = modulus.toString().length() - 1;
		blocksize -= blocksize%3;

		// First we dig out the message, convert each to an integer,
		// and have the integers as a string of integers.
		String ints = stringToInts(input);

		// If the blocksize does not evenly divide the length of 
		// ints, we tack on a bunch of tabs: 009.
		int addTabs = (ints.length()%blocksize)/3;

		while (addTabs > 0) {
			ints += "009";
			addTabs--;
		}

		char[] chars = new char[blocksize];
		// The string that holds the encrypted message.
		String ciphertext = "";
		
		// Now we break up the string into blocks that are less than
		// the modulus, and encrypt each one.
		int length = modulus.toString().length();
		for (int i=0; i<(ints.length())/blocksize; i++) {		
			ints.getChars(i*blocksize, (i+1)*blocksize, chars, 0);

			// The key encryption step.	
			String temp = new BigInteger(new String(chars, 0, blocksize)).modPow(key, modulus).toString();

			// Making sure that all the chunks we add onto the
			// ciphertext are the same length.
			while (temp.length() < length) 	
				temp = "0" + temp;

			// Tacking on the new mini-ciphertext.
			ciphertext = ciphertext + temp;
		}

		// Now the whole encrypted message is in ciphertext, so we 
		// put it in the outfile.
		output.println(ciphertext);
	} // end encrypt()

// Method: 	decrypt
// Purpose:	To decrypt a message and put it into the output file.
// Parameters:	input - the input file
//		output - the output file
//		modulus - the modulus
//		key - the key

	public static void decrypt (BufferedReader input, PrintWriter output, BigInteger modulus, BigInteger key) {
		// First we read in the ciphertext. The way the encryption
		// step is set up, we should have the whole ciphertext in
		// the first read.
		String ciphertext = "";

		try {
	
			ciphertext = input.readLine();

			if (ciphertext == null) {
				output.println("");
				return;
			}
			
			// Now we get the rest of the ciphertext.
			String temp = input.readLine();
			while (temp != null) {
				ciphertext += temp;
				temp = input.readLine();
			}
		} catch (IOException ioe) {

			output.println("");
			return;
		}

		// Divide it into chunks each the length of the modulus,
		// and decrypt each one.
		int length = modulus.toString().length();
		char[] chars = new char[length];
		String plaintext = "";
		for (int i=0; i<ciphertext.length()/length; i++) {
			ciphertext.getChars(i*length, (i+1)*length, chars, 0);

			// The key decryption step.
			String temp = new BigInteger(new String(chars, 0, length)).modPow(key, modulus).toString();

			// Making sure that all the chunks we add onto the
			// plaintext are the same length.
			while (temp.length() < length - 1) 	
				temp = "0" + temp;

			// Tacking on the new numerical plaintext.
			plaintext = plaintext + temp;
		}

		// Now in plaintext we have the decrypted message, but in
		// Unicode 1.1.5 form, so we need to convert it back to 
		// characters.
		// and then send it directly to the output file.
		output.println(intsToString(plaintext));
	} // end decrypt()

// Method:	stringToInts
// Purpose:	To convert the message in input to a String of ints
// Parameter:	input - the input file

	static String stringToInts (BufferedReader input) {
		String message = "";
		// Getting the first line of the message, and checking if there is a message.
		try {
			message = input.readLine();

			if(message == null)
				return "0";

		} catch(IOException ioe) {
			return "0";
		}

		// If there is a message, we proceed.
		String ints = "";
		int temp;
		while (message != null) {
			// Running through the string.
			for(int i=0; i<message.length(); i++) {
				// Convert each charachter to an integer.
				temp = (int)message.charAt(i);
				
				// We we only use Unicode 1.1.5 chars \u0000 
				// through \u00FF, since anything beyond that
				// are international chars.
				// Thus all digits used are three digits
				if (temp > 256)
					temp%=256;

				// Adding on the necessary digits:
				if (temp > 99)
					ints = ints + String.valueOf(temp);

				else if ( temp > 9)
					ints = ints + "0" + String.valueOf(temp);
				
				else 
					ints = ints + "00" + String.valueOf(temp);

				// Done with that character.
			} // end for loop
			
			try {

				// Get the next line.
				message = input.readLine();
			} catch (IOException ioe) {
				error("Could not read line.");
			}
		} // end while loop

		// Now we return the String of integers.
		return ints;

	} // end stringToInts

// Method: 	intsToString
// Purpose:	To convert a String of ints to a message using the encoding
//			of Unicode 1.1.5.
// Parameter:	ints - the String that contains the integers.

	static String intsToString (String ints) {
		String message;

		// The integers are broken up into groups of 3, then
		// converted to chars.
		char[] miniInt = new char[3];
		char[] plaintext = new char[ints.length()/3];
		for (int i=0; i<ints.length()/3; i++) {
			// Put the int into a char array.
			ints.getChars(i*3, (i+1)*3, miniInt, 0);

			// And convert that to a String, then an int, and
			// finally a char.
			plaintext[i] = (char)Integer.parseInt(new String(miniInt, 0, 3));
		}

		// Now the whole deciphered message is in plaintext, so we
		// convert it to a String and send it back.
		return (new String(plaintext, 0, plaintext.length));
	} // end intsToString()

} // end class RSA