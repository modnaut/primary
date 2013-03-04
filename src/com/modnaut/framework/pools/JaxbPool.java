package com.modnaut.framework.pools;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.properties.application.Applications;
import com.modnaut.common.properties.sqlmetadata.SqlMetaData;
import com.modnaut.common.properties.viewmetadata.ViewMetaData;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 *       Class to handling jaxb pooling.
 */
public class JaxbPool
{
	private static Logger LOGGER = LoggerFactory.getLogger(JaxbPool.class);

	/**
	 * When coupled with the appropriate KeyedPoolableObjectFactory, GenericKeyedObjectPool provides pooling functionality for keyed objects. A GenericKeyedObjectPool can be viewed as a map of pools, keyed on the (unique) key values provided to the preparePool, addObject or borrowObject methods. Each time a new key value is provided to one of these methods, a new pool is created under the given key to be managed by the containing GenericKeyedObjectPool.
	 */
	private static GenericKeyedObjectPool<Class, JAXBContext> JAXB_CONTEXT_POOL = new GenericKeyedObjectPool<Class, JAXBContext>(new JaxbContextFactory(), new JaxbPoolConfig());
	private static GenericKeyedObjectPool<JaxbPoolKey, Marshaller> MARSHALLER_POOL = new GenericKeyedObjectPool<JaxbPoolKey, Marshaller>(new MarshallerFactory(), new JaxbPoolConfig());
	private static GenericKeyedObjectPool<JaxbPoolKey, Unmarshaller> UNMARSHALLER_POOL = new GenericKeyedObjectPool<JaxbPoolKey, Unmarshaller>(new UnmarshallerFactory(), new JaxbPoolConfig());

	/**
	 * A HashMap of pool keys to be reused internally so that we don't constantly create JaxbPoolKey objects that are used once and then garbage collected
	 * 
	 */
	private static HashMap<Class, JaxbPoolKey> POOL_KEYS = new HashMap<Class, JaxbPoolKey>()
	{
		{
			try
			{
				put(ViewMetaData.class, new JaxbPoolKey(ViewMetaData.class));
			}
			catch (Exception e)
			{
			}

			try
			{
				put(SqlMetaData.class, new JaxbPoolKey(SqlMetaData.class));
			}
			catch (Exception e)
			{
			}

			try
			{
				put(Applications.class, new JaxbPoolKey(Applications.class));
			}
			catch (Exception e)
			{
			}
		}
	};

	/**
	 * Nested inner class, can be used by JaxbPool class
	 */
	private static class JaxbPoolKey
	{

		private Class clazz;

		/**
		 * Takes in a clazz parameter and assigns it to the variable clazz contained within this static class.
		 * 
		 * @param clazz
		 */
		private JaxbPoolKey(Class clazz)
		{
			this.clazz = clazz;
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
			JaxbPoolKey poolKey = (JaxbPoolKey) o;

			// return boolean of whether or not the clazz string is equivalent to the poolKey.clazz string
			return clazz.equals(poolKey.clazz);
		}

		/**
		 * Returns the same integer result for objects that have been called more than once during the execution of a java application or on objects that are equal. For objects that are unequal it will return distinct integers for those objects, though it is not necessarily true that those integers will be unique.
		 * 
		 * The method below takes the hashCode of the Class and multiplies the result by the specified number. This provides additional guarantee that each object will have a truly distinct integers.
		 * 
		 * As much as is reasonably practical, the hashCode method defined by class Object does return distinct integers for distinct objects.
		 * 
		 */
		@Override
		public int hashCode()
		{
			int result = clazz.hashCode();
			result = 5235 * result;
			return result;
		}

		public Class getClazz()
		{
			return clazz;
		}
	}

	/**
	 * Nested inner class, can be used by JaxbPool class
	 */
	private static class MarshallerFactory extends BaseKeyedPoolableObjectFactory<JaxbPoolKey, Marshaller>
	{
		/**
		 * Generates marshaller object based on passed in key (class).
		 * 
		 * @param key
		 */
		@Override
		public Marshaller makeObject(JaxbPoolKey key)
		{
			try
			{
				JAXBContext jaxbContext = JAXB_CONTEXT_POOL.borrowObject(key.getClazz());
				Marshaller marshaller = jaxbContext.createMarshaller();
				JAXB_CONTEXT_POOL.returnObject(key.getClazz(), jaxbContext);
				return marshaller;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// throw new EnrichableException("", "", "", e);
			}

			return null;
		}
	}

	/**
	 * Nested inner class, can be used by JaxbPool class
	 */
	private static class UnmarshallerFactory extends BaseKeyedPoolableObjectFactory<JaxbPoolKey, Unmarshaller>
	{
		/**
		 * Generates unmarshaller object based on passed in key (class). Uses the JAXB_CONTEXT_POOL to get the JaxbContext
		 * 
		 * @param key
		 */
		@Override
		public Unmarshaller makeObject(JaxbPoolKey key)
		{
			try
			{
				JAXBContext jaxbContext = JAXB_CONTEXT_POOL.borrowObject(key.getClazz());
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				JAXB_CONTEXT_POOL.returnObject(key.getClazz(), jaxbContext);
				return unmarshaller;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// throw new EnrichableException("", "", "", e);
			}

			return null;
		}
	}

	/**
	 * Nested inner class, can be used by JaxbPool class
	 */
	private static class JaxbContextFactory extends BaseKeyedPoolableObjectFactory<Class, JAXBContext>
	{
		/**
		 * Generates a JaxbContext object based on the passed in key (class)
		 * 
		 * @param clazz
		 */
		@Override
		public JAXBContext makeObject(Class clazz)
		{
			try
			{
				return JAXBContext.newInstance(clazz);
			}
			catch (JAXBException e)
			{
				e.printStackTrace();
				// throw new EnrichableException("", "", "", e);
			}

			return null;
		}
	}

	/**
	 * Nested inner class, can be used by JaxbPool class.
	 * 
	 * Sets configuration parameters for the ModnautPoolConfig
	 * 
	 * maxIdle: Controls the maximum number of objects that can sit idle in the pool (per key) at any time maxActive: When maxActive reaches 10, the pool is considered to be exhausted. Controls the maximum number of objects (per key) that can allocated by the pool (checked out to client threads, or idle in the pool) at one time maxTotal: Sets a global limit on the number of objects that can be in circulation (active or idle) within the combined set of pools minIdle: Sets a target value for the minimum number of idle objects (per key) that should always be available. whenExhaustedAction: Specifies behavior of exhausted pool. In this case, will create a new object and return it (making maxActive meaningless) timeBetweenEvictionRunsMillis: Indicates how long the eviction thread should sleep before "runs" of examining idle object numTestsPerEvictionRun: Sets the max number of objects to examine during each run of the idle object evictor thread (if any) minEvictableIdleTimeMillis:
	 * Specifies the minimum amount of time that an object may sit idle in the pool before it is eligible for eviction due to idle time
	 */
	private static class JaxbPoolConfig extends GenericKeyedObjectPool.Config
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

	private static JaxbPoolKey getPoolKey(Class clazz)
	{
		JaxbPoolKey poolKey = null;

		if (clazz != null)
		{
			poolKey = POOL_KEYS.get(clazz);
			if (poolKey == null)
			{
				poolKey = new JaxbPoolKey(clazz);
				POOL_KEYS.put(clazz, poolKey);
			}
		}

		return poolKey;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static Unmarshaller getUnmarshaller(Class clazz)
	{
		try
		{
			return UNMARSHALLER_POOL.borrowObject(getPoolKey(clazz));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}

		return null;
	}

	/**
	 * Puts returned unmarshaller back into pool for later use.
	 * 
	 * @param unmarshaller
	 * @param clazz
	 */
	public static void returnUnmarshaller(Unmarshaller unmarshaller, Class clazz)
	{
		try
		{
			UNMARSHALLER_POOL.returnObject(getPoolKey(clazz), unmarshaller);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}
	}

	/**
	 * Grabs marshaller from pool based on class specifications
	 * 
	 * @param clazz
	 * @return
	 */
	public static Marshaller getMarshaller(Class clazz)
	{
		try
		{
			return MARSHALLER_POOL.borrowObject(getPoolKey(clazz));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}

		return null;
	}

	/**
	 * Puts marshaller object back into pool for later use
	 * 
	 * @param marshaller
	 * @param clazz
	 */
	public static void returnMarshaller(Marshaller marshaller, Class clazz)
	{
		try
		{
			MARSHALLER_POOL.returnObject(getPoolKey(clazz), marshaller);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}
	}

	/**
	 * 
	 * @param clazz
	 * @param file
	 * @return
	 */
	public static <T> T unmarshal(Class<T> clazz, File file)
	{
		T result;

		JaxbPoolKey poolKey = getPoolKey(clazz);
		Unmarshaller unmarshaller = null;

		try
		{
			unmarshaller = UNMARSHALLER_POOL.borrowObject(poolKey);
			result = (T) unmarshaller.unmarshal(file);
			UNMARSHALLER_POOL.returnObject(poolKey, unmarshaller);
			return result;
		}
		catch (Exception e)
		{
			try
			{
				UNMARSHALLER_POOL.invalidateObject(poolKey, unmarshaller);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				// throw new EnrichableException("", "", "", ex);
			}

			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}

		return null;
	}

	/**
	 * 
	 * @param url
	 * @param clazz
	 * @return
	 */
	public static <T> T unmarshal(URL url, Class<T> clazz)
	{
		T result;

		JaxbPoolKey poolKey = getPoolKey(clazz);
		Unmarshaller unmarshaller = null;

		try
		{
			unmarshaller = UNMARSHALLER_POOL.borrowObject(poolKey);
			result = (T) unmarshaller.unmarshal(url);
			UNMARSHALLER_POOL.returnObject(poolKey, unmarshaller);
			return result;
		}
		catch (Exception e)
		{
			try
			{
				UNMARSHALLER_POOL.invalidateObject(poolKey, unmarshaller);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				// throw new EnrichableException("", "", "", ex);
			}

			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}

		return null;
	}

	/**
	 * 
	 * @param instance
	 * @param out
	 */
	public static void marshal(Object instance, OutputStream out)
	{
		JaxbPoolKey poolKey = getPoolKey(instance.getClass());
		Marshaller marshaller = null;

		try
		{
			marshaller = MARSHALLER_POOL.borrowObject(poolKey);
			marshaller.marshal(instance, out);
			MARSHALLER_POOL.returnObject(poolKey, marshaller);
		}
		catch (Exception e)
		{
			try
			{
				MARSHALLER_POOL.invalidateObject(poolKey, marshaller);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				// throw new EnrichableException("", "", "", ex);
			}

			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}
	}
}
