package com.modnaut.common.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.modnaut.common.interfaces.ICommonConstants;

public class CommonMethods
{
	public static void printHashMap(HashMap<String, String> parms)
	{
		String valueList = ICommonConstants.NONE;

		if (parms != null)
		{
			for (Entry<String, String> entry : parms.entrySet())
			{
				String keyvalue = ICommonConstants.NONE;
				String key = entry.getKey();
				String value = entry.getValue();

				keyvalue += key + ":" + value;
				valueList += keyvalue + "\n";
			}
		}

		System.out.println(valueList);
	}

	public static String encryptString(String password, int interation_number, String passwordSalt) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException
	{
		// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
		return byteToBase64(getHash(interation_number, password, base64ToByte(passwordSalt)));
	}

	/**
	 * From a password, a number of iterations and a salt, returns the corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest MESSAGE_DIGEST = MessageDigest.getInstance(ICommonConstants.SHA_1);

		// Initialize the MessageDigest Object used for hashing
		MESSAGE_DIGEST.reset();
		MESSAGE_DIGEST.update(salt); // Add the salting to make DICTIONARY ATTACKS harder.
		byte[] input = MESSAGE_DIGEST.digest(password.getBytes(ICommonConstants.UTF_8));
		// Hash the password a few times. This is expensive, but makes the attack very hard from an attacker standpoint. We still only salt once though...
		for (int i = 0; i < iterationNb; i++)
		{
			MESSAGE_DIGEST.reset();
			input = MESSAGE_DIGEST.digest(input);
		}
		return input;
	}

	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * 
	 * @param data
	 *            String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	private static byte[] base64ToByte(String data) throws IOException
	{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
	}

	/**
	 * From a byte[] returns a base 64 representation
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	private static String byteToBase64(byte[] data)
	{
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}
}
