package com.modnaut.framework.utilities;

import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.session.WebSession;

public class UrlMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlMethods.class);
	private static final String DES = "DES";
	private static final String CLASS_AND_METHOD_ENCRYPTION_KEY = "ModnautNinja";
	private static DESKeySpec KEY_SPEC;
	private static SecretKeyFactory KEY_FACTORY;
	private static SecretKey KEY;
	static
	{
		try
		{
			KEY_SPEC = new DESKeySpec(CLASS_AND_METHOD_ENCRYPTION_KEY.getBytes(ICommonConstants.UTF_8));
			KEY_FACTORY = SecretKeyFactory.getInstance(DES);
			KEY = KEY_FACTORY.generateSecret(KEY_SPEC);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void retrievePrettyUrl(WebSession webSession, String className, String methodName)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.CLASS, className);
		parms.put(ICommonConstants.METHOD, methodName);

		String hashPath = DatabaseMethods.getJustDataFirstRowFirstColumn("GET_HASHPATH_BY_CLASS_AND_METHOD", ICommonConstants.COMMON, parms);

		if (!StringUtils.isEmpty(hashPath))
		{
			webSession.getResponse().setHeader("HashPath", hashPath);
		}
	}

	public static String[] retrieveClassAndMethod(String hashPath)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("HashPath", hashPath);
		return DatabaseMethods.getJustDataFirstRow("GET_CLASS_AND_METHOD_FROM_HASHPATH", ICommonConstants.COMMON, parms);
	}

	public static String encrypt(String string)
	{
		String encrypted = string;

		try
		{
			byte[] cleartext = string.getBytes(ICommonConstants.UTF_8);
			Cipher cipher = Cipher.getInstance(DES); // cipher is not thread safe
			cipher.init(Cipher.ENCRYPT_MODE, KEY);
			encrypted = Base64.encodeBase64String(cipher.doFinal(cleartext));
		}
		catch (Exception e)
		{
			encrypted = string;
		}
		LOGGER.debug("Encrypting {} -> {}, {}ms", string, encrypted);
		return encrypted;
	}

	public static String decrypt(String string)
	{
		String decrypted = string;

		try
		{
			byte[] encryptedBytes = Base64.decodeBase64(string);
			Cipher cipher = Cipher.getInstance(DES);// cipher is not thread safe
			cipher.init(Cipher.DECRYPT_MODE, KEY);
			byte[] decryptedBytes = (cipher.doFinal(encryptedBytes));
			decrypted = new String(decryptedBytes);
		}
		catch (Exception e)
		{
			decrypted = string;
		}
		LOGGER.debug("Decrypting {} -> {}, {}ms", string, decrypted);
		return decrypted;
	}
}
