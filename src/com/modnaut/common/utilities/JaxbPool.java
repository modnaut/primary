package com.modnaut.common.utilities;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

public class JaxbPool {

    private static GenericKeyedObjectPool<Class, JAXBContext> JAXB_CONTEXT_POOL = new GenericKeyedObjectPool<Class, JAXBContext>(new JaxbContextFactory(), new ModnautPoolConfig());
    private static GenericKeyedObjectPool<PoolKey, Marshaller> MARSHALLER_POOL = new GenericKeyedObjectPool<PoolKey, Marshaller>(new MarshallerFactory(), new ModnautPoolConfig());
    private static GenericKeyedObjectPool<PoolKey, Unmarshaller> UNMARSHALLER_POOL = new GenericKeyedObjectPool<PoolKey, Unmarshaller>(new UnmarshallerFactory(), new ModnautPoolConfig());

    private static class PoolKey {
	private Class clazz;

	private PoolKey(Class clazz) {
	    this.clazz = clazz;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o)
		return true;

	    if (o == null || getClass() != o.getClass())
		return false;

	    PoolKey poolKey = (PoolKey) o;

	    return clazz.equals(poolKey.clazz);
	}

	@Override
	public int hashCode() {
	    int result = clazz.hashCode();
	    result = 5235 * result;
	    return result;
	}

	public Class getClazz() {
	    return clazz;
	}
    }

    private static class MarshallerFactory extends BaseKeyedPoolableObjectFactory<PoolKey, Marshaller> {
	@Override
	public Marshaller makeObject(PoolKey key) throws Exception {
	    JAXBContext jaxbContext = JAXB_CONTEXT_POOL.borrowObject(key.getClazz());
	    Marshaller marshaller = jaxbContext.createMarshaller();
	    JAXB_CONTEXT_POOL.returnObject(key.getClazz(), jaxbContext);
	    return marshaller;
	}
    }

    private static class UnmarshallerFactory extends BaseKeyedPoolableObjectFactory<PoolKey, Unmarshaller> {
	@Override
	public Unmarshaller makeObject(PoolKey key) throws Exception {
	    JAXBContext jaxbContext = JAXB_CONTEXT_POOL.borrowObject(key.getClazz());
	    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	    JAXB_CONTEXT_POOL.returnObject(key.getClazz(), jaxbContext);
	    return unmarshaller;
	}
    }

    private static class JaxbContextFactory extends BaseKeyedPoolableObjectFactory<Class, JAXBContext> {
	@Override
	public JAXBContext makeObject(Class clazz) throws Exception {
	    return JAXBContext.newInstance(clazz);
	}
    }

    private static class ModnautPoolConfig extends GenericKeyedObjectPool.Config {
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

    public static Unmarshaller getUnmarshaller(Class clazz) throws Exception {
	return UNMARSHALLER_POOL.borrowObject(new PoolKey(clazz));
    }

    public static void returnUnmarshaller(Unmarshaller unmarshaller, Class clazz) throws Exception {
	UNMARSHALLER_POOL.returnObject(new PoolKey(clazz), unmarshaller);
    }

    public static Marshaller getMarshaller(Class clazz) throws Exception {
	return MARSHALLER_POOL.borrowObject(new PoolKey(clazz));
    }

    public static void returnMarshaller(Marshaller marshaller, Class clazz) throws Exception {
	MARSHALLER_POOL.returnObject(new PoolKey(clazz), marshaller);
    }

    public static <T> T unmarshal(Class<T> clazz, File file) throws Exception {
	T result;

	PoolKey poolKey = new PoolKey(clazz);
	Unmarshaller unmarshaller = UNMARSHALLER_POOL.borrowObject(poolKey);

	try {
	    result = (T) unmarshaller.unmarshal(file);
	    UNMARSHALLER_POOL.returnObject(poolKey, unmarshaller);
	    return result;
	} catch (Exception e) {
	    UNMARSHALLER_POOL.invalidateObject(poolKey, unmarshaller);
	    throw new RuntimeException(e);
	}
    }

    public static <T> T unmarshal(URL url, Class<T> clazz) throws Exception {
	T result;

	PoolKey poolKey = new PoolKey(clazz);
	Unmarshaller unmarshaller = UNMARSHALLER_POOL.borrowObject(poolKey);

	try {
	    result = (T) unmarshaller.unmarshal(url);
	    UNMARSHALLER_POOL.returnObject(poolKey, unmarshaller);
	    return result;
	} catch (Exception e) {
	    UNMARSHALLER_POOL.invalidateObject(poolKey, unmarshaller);
	    throw new RuntimeException(e);
	}
    }

    public static void marshal(Object instance, OutputStream out) {
	PoolKey poolKey = new PoolKey(instance.getClass());
	Marshaller marshaller = null;

	try {
	    marshaller = MARSHALLER_POOL.borrowObject(poolKey);
	    marshaller.marshal(instance, out);
	    MARSHALLER_POOL.returnObject(poolKey, marshaller);
	} catch (Exception e) {
	    try {
		MARSHALLER_POOL.invalidateObject(poolKey, marshaller);
	    } catch (Exception i) {
		throw new RuntimeException(i);
	    }

	    throw new RuntimeException(e);
	}
    }
}
