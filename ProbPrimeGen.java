// Eric Landquist
// Virginia Tech

// June 8, 1998

// ProbPrimeGen.java

// This program will generate a list of probable primes.

import java.math.BigInteger;
import java.io.*;
import java.util.Random;

class ProbPrimeGen {

	static BufferedReader input;
	static PrintWriter output;
	static BigInteger probPrime;

	public ProbPrimeGen( ) {

	}

// Method	main()
// Purpose
 
	public static void main (String args[]) {

		if ( args.length != 4 ) {
			error("Usage: [output_file] [certainty] [quantity] [length]");
			return;
		}

		try {
			output = new PrintWriter( new FileWriter(args[0]));
		} catch (IOException ioe) {
			
		}

		int certainty = Integer.parseInt(args[1]);

		int quantity = Integer.parseInt(args[2]);

		int length = Integer.parseInt(args[3]);

		generate(output, certainty, quantity, length);	

		System.out.println("Done. Result in: " + args[0]);		
	} // end main

// Method 	getRandBigInt()
// Purpose 	To get a randon BigInteger of a given length.
// Parameters	length - the length of the random BigInteger

	public static BigInteger getRandBigInt(int length) {
		Random intGen = new Random();
		String temp;
		String randInt="";
		while (randInt.length() < length){
			// A random 10 digit number
			temp = String.valueOf((int)Math.abs(intGen.nextInt()%Math.pow(10, 5)));
			if (temp.length() < 10) {
				while (temp.length() < 5) {
					temp = "0" + temp;
				}
			}
			randInt = randInt + temp;
		}

		int extra = length%5;
		if (extra != 0) {
			temp = String.valueOf((int)Math.abs(intGen.nextInt()%Math.pow(10, extra)));
			if (temp.length() < extra) {
				while( temp.length() < extra)
					temp = "0" + temp;
			}
			randInt = randInt + temp;
		}

		if (randInt.charAt(0) == '0') {
			char[] getDigit = randInt.toCharArray();
			getDigit[0] = '1';
			return(new BigInteger( new String(getDigit, 0, getDigit.length)));
		}

		return (new BigInteger(randInt));	
	}


	public static void error (String message) {
		System.out.println("Error: " + message);
	} // end error()

	public static void generate(PrintWriter output, int certainty, int quantity, int length){
		boolean composite = true;
		
		for (int i=1; i< quantity+1; i++) {

			probPrime = getRandBigInt(length);
		
			if (probPrime.mod(new BigInteger("2")).equals(new BigInteger("0"))) 
				probPrime = probPrime.add(new BigInteger("1"));

		
			while (composite) {
				if (probPrime.isProbablePrime(certainty)) {
					composite = false;
					output.println(probPrime.toString());
					output.println();
					System.out.println("Found "+ i + " of " + quantity +".");
				}
				probPrime = probPrime.add(new BigInteger("2"));
			}
			composite = true;
		}
		output.close();
		return;
	}


} // end class ProbPrimeGen



 