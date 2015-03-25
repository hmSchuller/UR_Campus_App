package de.ur.mi.mspwddhs.campusapp.secruity;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityMain {
	private String iv = "fedcba9876543210";
	private static IvParameterSpec ivspec;
	private static SecretKeySpec keyspec;
	private static Cipher cipher;

	private String SecretKey = "0123456789abcdef";

	public SecurityMain() {

		ivspec = new IvParameterSpec(iv.getBytes());
		keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String text) throws Exception {
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");
		String returnString = "";
		byte[] encrypted = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			encrypted = cipher.doFinal(padString(text).getBytes());
		} catch (Exception e) {
			throw new Exception("[encrypt] " + e.getMessage());
		}
		
		for(int i = 0; i < encrypted.length; i++){
			returnString += encrypted[i];
		}

		return returnString;
	}

	public static String decrypt(String code) throws Exception {
		if (code == null || code.length() == 0)
			throw new Exception("Empty string");
		String returnString = "";
		byte[] decrypted = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			decrypted = cipher.doFinal(hexToBytes(code));
		} catch (Exception e) {
			throw new Exception("[decrypt] " + e.getMessage());
		}
		
		for(int i = 0; i < decrypted.length; i++){
			returnString += decrypted[i];
		}

		return returnString;
	}

	public static String bytesToHex(byte[] data) {
		if (data == null) {
			return null;
		}

		int len = data.length;
		String str = "";
		for (int i = 0; i < len; i++) {
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
			else
				str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
		}
		return str;
	}

	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}

	private static String padString(String source) {
		char paddingChar = ' ';
		int size = 16;
		int x = source.length() % size;
		int padLength = size - x;
		for (int i = 0; i < padLength; i++) {
			source += paddingChar;
		}
		return source;
	}
}