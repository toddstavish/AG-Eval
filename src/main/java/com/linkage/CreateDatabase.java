package com.linkage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.graphdb.LinkageContextManager;

public class CreateDatabase {
    static Logger logger;
    static{
        logger = LoggerFactory.getLogger(CreateDatabase.class);
    }
    
    public static void main(String[] args) throws Exception {
        logger.info("Starting CreateDatabase");
        LinkageContextManager.createGraph();
        logger.info("Completed CreateDatabase");
    }	
}
