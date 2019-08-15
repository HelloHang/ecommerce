package com.product.ehcache;

import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class CustomCacheLoaderWriter implements CacheLoaderWriter
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomCacheLoaderWriter.class);

	@Override
	public Map loadAll(Iterable keys) throws BulkCacheLoadingException, Exception
	{
		return null;
	}

	@Override
	public void writeAll(Iterable iterable) throws BulkCacheWritingException, Exception
	{

	}

	@Override
	public void deleteAll(Iterable keys) throws BulkCacheWritingException, Exception
	{

	}

	@Override
	public Object load(Object o) throws Exception
	{
		LOG.info("Get data form custom cache.");
		return null;
	}

	@Override
	public void write(Object o, Object o2) throws Exception
	{
		LOG.info("Writer data to custom cache.");
	}

	@Override
	public void delete(Object o) throws Exception
	{

	}
}
