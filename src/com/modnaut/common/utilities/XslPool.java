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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.modnaut.common.servlet.ApplicationServlet;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 *       Class to handle xsl pooling. Will automatically create Transformer objects for XSL files as requested, keeping around a set amount and ballooning temporarily when demand is higher
 */

public class XslPool
{
	private static Logger LOGGER = LoggerFactory.getLogger(XslPool.class);
	private static final String WEB_DIRECTORY = "WEB-INF/xsl/";

	/**
	 * When coupled with the appropriate KeyedPoolableObjectFactory, GenericKeyedObjectPool provides pooling functionality for keyed objects. A GenericKeyedObjectPool can be viewed as a map of pools, keyed on the (unique) key values provided to the preparePool, addObject or borrowObject methods. Each time a new key value is provided to one of these methods, a new pool is created under the given key to be managed by the containing GenericKeyedObjectPool.
	 */
	private static GenericKeyedObjectPool<XslPoolKey, Transformer> TRANSFORMER_POOL = new GenericKeyedObjectPool<XslPoolKey, Transformer>(new TransformerFactory(), new XslPoolConfig());

	/**
	 * A HashMap of pool keys to be reused internally so that we don't constantly create JaxbPoolKey objects that are used once and then garbage collected
	 * 
	 */
	private static HashMap<String, XslPoolKey> POOL_KEYS = new HashMap<String, XslPoolKey>()
	{
		{
			try
			{
				put("ViewMetaData.xsl", new XslPoolKey("ViewMetaData.xsl"));
			}
			catch (Exception e)
			{
			}

			try
			{
				put("Application.xsl", new XslPoolKey("Application.xsl"));
			}
			catch (Exception e)
			{
			}
		}
	};

	/**
	 * Nested inner class, can be used by XslPool class
	 */
	private static class XslPoolKey
	{
		private String xslFileName;

		/**
		 * Takes in a xslFileName parameter and assigns it to the variable xslFileName contained within this static class.
		 * 
		 * @param xslFileName
		 */
		private XslPoolKey(String xslFileName)
		{
			this.xslFileName = xslFileName;
		}

		/**
		 * Override method that works in conjunction with the hashCode() override method below. Provides absolute certainty checks for the object passed in to determine equality of the object that currently exists.
		 * 
		 * Also used to avoid duplicates in a hash set.
		 * 
		 * @param o
		 */
		@Override
		public boolean equals(Object o)
		{
			// hashCode returned must be the same
			if (this == o)
				return true;

			if (o == null || getClass() != o.getClass())
				return false;

			// if previous checks are not met object is casted as a PoolKey object
			XslPoolKey poolKey = (XslPoolKey) o;

			// return boolean of whether or not the xslFileName string is equivalent to the poolKey.xslFileName string
			return xslFileName.equals(poolKey.xslFileName);
		}

		/**
		 * Returns the same integer result for objects that have been called more than once during the execution of a java application or on objects that are equal. For objects that are unequal it will return distinct integers for those objects, though it is not necessarily true that those integers will be unique.
		 * 
		 * The method below takes the hashcode of the xslFileName and multiplies the result by the specified number. This provides additional guarantee that each object will have a truly distinct integers.
		 * 
		 * As much as is reasonably practical, the hashCode method defined by class Object does return distinct integers for distinct objects.
		 * 
		 */
		@Override
		public int hashCode()
		{
			int result = xslFileName.hashCode();
			result = 5235 * result;
			return result;
		}
	}

	/**
	 * Nested inner class, can be used by XslPool class
	 */
	private static class TransformerFactory extends BaseKeyedPoolableObjectFactory<XslPoolKey, Transformer>
	{
		/**
		 * Used to prepare the transformer object. Grabs xsl file based on passed in parameter from the web directory and returns transformer to be used in marshalling and unmarshalling.
		 * 
		 * @param key
		 */
		@Override
		public Transformer makeObject(XslPoolKey key) throws TransformerConfigurationException
		{
			javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
			Source xsltSource = new StreamSource(ApplicationServlet.getRealPath() + WEB_DIRECTORY + key.xslFileName);
			Transformer transformer = factory.newTransformer(xsltSource);
			return transformer;
		}
	}

	/**
	 * Nested inner class, can be used by XslPool class.
	 * 
	 * Sets configuration parameters for the ModnautPoolConfig
	 * 
	 * maxIdle: Controls the maximum number of objects that can sit idle in the pool (per key) at any time maxActive: When maxActive reaches 10, the pool is considered to be exhausted. Controls the maximum number of objects (per key) that can allocated by the pool (checked out to client threads, or idle in the pool) at one time maxTotal: Sets a global limit on the number of objects that can be in circulation (active or idle) within the combined set of pools minIdle: Sets a target value for the minimum number of idle objects (per key) that should always be available. whenExhaustedAction: Specifies behavior of exhausted pool. In this case, will create a new object and return it (making maxActive meaningless) timeBetweenEvictionRunsMillis: Indicates how long the eviction thread should sleep before "runs" of examining idle object numTestsPerEvictionRun: Sets the max number of objects to examine during each run of the idle object evictor thread (if any) minEvictableIdleTimeMillis:
	 * Specifies the minimum amount of time that an object may sit idle in the pool before it is eligible for eviction due to idle time
	 */
	private static class XslPoolConfig extends GenericKeyedObjectPool.Config
	{
		{
			maxIdle = 3;
			maxActive = 10;
			maxTotal = 100;
			minIdle = 1;
			whenExhaustedAction = GenericKeyedObjectPool.WHEN_EXHAUSTED_GROW;
			timeBetweenEvictionRunsMillis = 1000L * 60L * 10L; // 60 mins.
			numTestsPerEvictionRun = 50;
			minEvictableIdleTimeMillis = 1000L * 60L * 5L; // 30 min.
		}
	}

	private static XslPoolKey getPoolKey(String xslFileName)
	{
		XslPoolKey poolKey = null;

		if (xslFileName != null)
		{
			poolKey = POOL_KEYS.get(xslFileName);
			if (poolKey == null)
			{
				poolKey = new XslPoolKey(xslFileName);
				POOL_KEYS.put(xslFileName, poolKey);
			}
		}

		return poolKey;
	}

	/**
	 * Creates new PoolKey object based on the xslFileName. Then uses this object to borrow an existing Transformer object from the pool. Adds the parameters to the recently borrowed Tranformer object and then transforms xml file (Source input) into a result based on the xslFile and parameters. Lastly, clears and returns the borrowed transformer object back to the pool to be re-used later.
	 * 
	 * Leveraged by the marshalAndTransform method below.
	 * 
	 * @param input
	 * @param outputStream
	 * @param xslFileName
	 * @param parameters
	 * @throws Exception
	 */
	public static void transform(Source input, OutputStream outputStream, String xslFileName, HashMap<String, Object> parameters) throws Exception
	{
		TRANSFORMER_POOL.clear();
		XslPoolKey key = getPoolKey(xslFileName);
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
			// perform xslt transformation
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

	/**
	 * Pulls all pieces together: java class, xml, xsl and transforms to html. Outputs final result to screen.
	 * 
	 * @param input
	 * @param outputStream
	 * @param xslFileName
	 * @param parameters
	 * @param prettyPrintJson
	 * @throws Exception
	 */
	public static void marshalAndTransform(Object input, OutputStream outputStream, String xslFileName, HashMap<String, Object> parameters, boolean prettyPrintJson) throws Exception
	{
		// create jaxb context and instantiate the marshaller - will be borrowed from existing jaxbPool
		Marshaller marshaller = JaxbPool.getMarshaller(input.getClass());
		JAXBSource source = new JAXBSource(marshaller, input);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		transform(source, baos, xslFileName, parameters);
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
				LOGGER.info(json);
				LOGGER.error("Failed to pretty print JSON", e);
				outputStream.write(json.getBytes());
			}
		}
		else
		{
			outputStream.write(baos.toByteArray());
		}
	}
}
