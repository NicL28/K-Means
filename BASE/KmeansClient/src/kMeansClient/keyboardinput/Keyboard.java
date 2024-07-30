//********************************************************************
//  Keyboard.java       Author: Lewis and Loftus
//
//  Facilitates keyboard input by abstracting details about input
//  parsing, conversions, and kMeansClient.kMeansClient.exception handling.
//********************************************************************

package kMeansClient.keyboardinput;

import java.io.*;
import java.util.*;

/**
 * Classe che implementa la lettura da tastiera.
 */
public class Keyboard {

	/**
	 * Flag che indica se stampare gli errori.
	 */
	private static boolean printErrors = true;

	/**
	 * Contatore degli errori.
	 */
	private static int errorCount = 0;

	/**
	 * Metodo che restituisce il numero di errori.
	 * @return numero di errori
	 */
	private static int getErrorCount() {
		return errorCount;
	}

	/**
	 * Metodo che resetta il contatore degli errori.
	 * @param count numero di errori
	 */
	private static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Metodo che restituisce il flag che indica se stampare gli errori.
	 * @return flag che indica se stampare gli errori
	 */
	private static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * Metodo che setta il flag che indica se stampare gli errori.
	 * @param flag flag che indica se stampare gli errori
	 */
	private static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * Metodo che stampa un messaggio di errore.
	 * @param str messaggio di errore
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************

	/**
	 * Token corrente.
	 */
	private static String current_token = null;

	/**
	 * Tokenizer della stringa di input.
	 */
	private static StringTokenizer reader;

	/**
	 * Stream di input.
	 */
	private static final BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	/**
	 * Metodo che restituisce il prossimo token.
	 * @return prossimo token
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}

	/**
	 * Metodo che restituisce il prossimo token che può essere già stato letto.
	 * @param skip flag che indica se il token è già stato letto
	 * @return prossimo token
	 */
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}

		return token;
	}

	/**
	 * Metodo che restituisce il prossimo token da input che può provenire dalla linea di input corrente o da una successiva.
	 * Il parametro determina se le linee successive vengono utilizzate.
	 * @param skip flag che indica se il token è già stato letto
	 * @return prossimo token
	 */
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.contains(token)) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Metodo che restituisce true se non ci sono altri token da leggere sulla linea di input corrente.
	 * @return true se non ci sono altri token da leggere sulla linea di input corrente
	 */
	private static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	// ************* Reading Section *********************************

	/**
	 * Metodo che restituisce una stringa letta da input.
	 * @return stringa letta da input
	 */
	public static String readString() {
		StringBuilder str;

		try {
			str = new StringBuilder(getNextToken(false));
			while (!endOfLine()) {
				str.append(getNextToken(false));
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
        return str != null ? str.toString() : null;
	}

	/**
	 * Metodo che restituisce un carattere letto da input.
	 * @return carattere letto da input
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1);
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Metodo che restituisce un intero letto da input.
	 * @return intero letto da input
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Error reading int data, MIN_VALUE value returned.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}
}
