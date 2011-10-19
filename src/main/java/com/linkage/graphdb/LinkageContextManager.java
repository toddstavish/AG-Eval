package com.linkage.graphdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinitegraph.AccessMode;
import com.infinitegraph.ConfigurationException;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.GraphFactory;
import com.infinitegraph.StorageException;
import com.infinitegraph.Transaction;
import com.infinitegraph.indexing.GenericIndex;
import com.infinitegraph.indexing.IndexException;
import com.infinitegraph.indexing.IndexManager;
import com.linkage.domain.Subject;
import com.linkage.vo.PropertyHelper;

public class LinkageContextManager {
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(LinkageContextManager.class);
	}

	private static volatile GraphDatabase db;
	private static RandomAccessFile raf;
	
	//double checked locking - is broken, but ok for POC
	public static void createGraph() throws StorageException, ConfigurationException {
		
		try {
			GraphFactory.delete("Linkage.Graph", PropertyHelper.getPropertyValue("graph.properties.path"));
		} catch (StorageException e) {
			logger.warn(e.getMessage());
		}
		
			GraphFactory.create("Linkage.Graph", PropertyHelper.getPropertyValue("graph.properties.path"));
			GraphDatabase graphDb = GraphFactory.open("Linkage.Graph", PropertyHelper.getPropertyValue("graph.properties.path"));
			Transaction tx = graphDb.beginTransaction(AccessMode.READ_WRITE);      
			try {
				GenericIndex<Subject> dunsIndex =  IndexManager.<Subject>createGenericIndex("dunsNumber", Subject.class.getName(), "dunsNumber");
			} catch (IndexException e) {
				e.printStackTrace();
			}
			tx.commit();
			graphDb.close();
	}
	
		
	//double checked locking - is broken, but ok for POC
	public static GraphDatabase getGraph() {
		if(null == db){
			@SuppressWarnings("unchecked")
			GraphDatabase graphDb=null;
			try {
				graphDb = GraphFactory.open("Linkage.Graph", PropertyHelper.getPropertyValue("graph.properties.path"));
			} catch (StorageException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			registerShutdownHook( graphDb );
			db = graphDb;
		}
		return db;
	}
	
	public static RandomAccessFile getRaf() {
		if(null == raf){			
			RandomAccessFile raccessFile = null;
			String atlasFile = PropertyHelper.getPropertyValue("update.row.filename");
			logger.info("Opening Atlas file {}", atlasFile);
			try {
				raccessFile = new RandomAccessFile(new File(atlasFile), "r");
			} catch (FileNotFoundException e) {
				logger.info("Error loading Atlas file {}", atlasFile);
				e.printStackTrace();
			}		
			raf = raccessFile;
		}
		return raf;
	}
	
	public static String getGrapPropsPath() {		       
        return PropertyHelper.getPropertyValue("graph.db.path");
	}
	
	
	private static void registerShutdownHook( final GraphDatabase graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.close();
            }
        } );
    }

}
