package com.linkage.graphdb;

import java.io.RandomAccessFile;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinitegraph.GraphDatabase;
import com.linkage.vo.PropertyHelper;

/**
 * @author mgubbala
 *
 */
public class LinkageContextListener implements ServletContextListener {

	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(LinkageContextListener.class);
	}


	private static volatile GraphDatabase graphDb;
	private static RandomAccessFile raf;

	public static GraphDatabase getGraphDatabase()
	{
		logger.trace("Getting graphDb reference");
		return graphDb;
	}
	
	public static RandomAccessFile getRandomAccessFile()
	{
		logger.trace("Getting Random Access file reference");
		return raf;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized( ServletContextEvent event )
	{
		synchronized ( LinkageContextListener.class )
		{
			logger.debug("Starting contextInitialized");
			
			String dbPath = PropertyHelper.getPropertyValue("graph.db.path");
			logger.info("Opening Graph Database One at {}", dbPath);
			graphDb = LinkageContextManager.getGraph();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed( ServletContextEvent event )
	{
		synchronized ( LinkageContextListener.class )
		{
			logger.debug("Starting contextDestroyed");
			GraphDatabase db = graphDb;
			graphDb = null;
			if ( db != null ) {
				logger.info("Shutting down Graph Database");
				db.close();
			}
		}
	}
}
