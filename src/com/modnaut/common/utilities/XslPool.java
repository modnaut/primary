package com.modnaut.common.utilities;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.modnaut.common.servlet.ApplicationServlet;

public class XslPool
{

	private static GenericKeyedObjectPool<PoolKey, Transformer> TRANSFORMER_POOL = new GenericKeyedObjectPool<PoolKey, Transformer>(new TransformerFactory(), new ModnautPoolConfig());

	private static class PoolKey
	{
		private String xslFileName;

		private PoolKey(String xslFileName)
		{
			this.xslFileName = xslFileName;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o)
				return true;

			if (o == null || getClass() != o.getClass())
				return false;

			PoolKey poolKey = (PoolKey) o;

			return xslFileName.equals(poolKey.xslFileName);
		}

		@Override
		public int hashCode()
		{
			int result = xslFileName.hashCode();
			result = 5235 * result;
			return result;
		}
	}

	private static class TransformerFactory extends BaseKeyedPoolableObjectFactory<PoolKey, Transformer>
	{
		@Override
		public Transformer makeObject(PoolKey key) throws TransformerConfigurationException
		{
			javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
			Source xsltSource = new StreamSource(ApplicationServlet.getRealPath() + "WEB-INF/xsl/" + key.xslFileName);
			Transformer transformer = factory.newTransformer(xsltSource);
			return transformer;
		}
	}

	private static class ModnautPoolConfig extends GenericKeyedObjectPool.Config
	{
		{
			maxIdle = 3;
			maxActive = 10;
			maxTotal = 100;
			minIdle = 1;
			whenExhaustedAction = GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
			timeBetweenEvictionRunsMillis = 1000L * 60L * 10L;
			numTestsPerEvictionRun = 50;
			minEvictableIdleTimeMillis = 1000L * 60L * 5L; // 30 min.
		}
	}

	public static void transform(Source input, OutputStream outputStream, String xslFileName, HashMap<String, Object> parameters) throws Exception
	{
		TRANSFORMER_POOL.clear();
		PoolKey key = new PoolKey(xslFileName);
		Transformer transformer = TRANSFORMER_POOL.borrowObject(key);
		if (parameters != null)
		{
			for (String parameter : parameters.keySet())
			{
				transformer.setParameter(parameter, parameters.get(parameter));
			}
		}
		try
		{
			transformer.transform(input, new StreamResult(outputStream));
			transformer.reset();// reset to be sure parameters aren't retained
			TRANSFORMER_POOL.returnObject(key, transformer);
		}
		catch (Exception e)
		{
			try
			{
				// if an exception occurred during transformation, invalidate
				// the transformer in case it's the cause of the problem
				TRANSFORMER_POOL.invalidateObject(key, transformer);
			}
			catch (RuntimeException i)
			{
				throw new RuntimeException(i);
			}
			throw new RuntimeException(e);
		}
	}

	public static void marshalAndTransform(Object input, OutputStream outputStream, String xslFileName, HashMap<String, Object> parameters, boolean prettyPrintJson) throws Exception
	{
		Marshaller marshaller = JaxbPool.getMarshaller(input.getClass());
		JAXBSource source = new JAXBSource(marshaller, input);
		ByteArrayOutputStream baos = null;

		if (prettyPrintJson)
		{
			baos = new ByteArrayOutputStream();
			transform(source, baos, xslFileName, parameters);
		}
		else
		{
			transform(source, outputStream, xslFileName, parameters);
		}

		JaxbPool.returnMarshaller(marshaller, input.getClass());

		if (prettyPrintJson)
		{
			String json = baos.toString();
			try
			{
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(json);
				String prettyJsonString = gson.toJson(jsonElement);
				outputStream.write(prettyJsonString.getBytes());
			}
			catch (Exception e)
			{
				System.out.println(json);
				e.printStackTrace();
				outputStream.write(json.getBytes());
			}
		}
	}
}
