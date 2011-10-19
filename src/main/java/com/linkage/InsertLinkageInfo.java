package com.linkage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinitegraph.AccessMode;
import com.infinitegraph.EdgeKind;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.Transaction;
import com.linkage.csv.CsvReader;
import com.linkage.domain.GlobalUltimateFactory;
import com.linkage.domain.RelationWith;
import com.linkage.domain.Relations;
import com.linkage.domain.Subject;
import com.linkage.domain.SubjectFactory;
import com.linkage.graphdb.LinkageContextManager;
import com.linkage.vo.LinkageInfoVo;

public class InsertLinkageInfo {
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(InsertLinkageInfo.class);
	}
	
	//maximum number of lines per transaction. commit after this many...
	private static final int LINES_PER_TRANSACTION = 1000;

	/**
	 * main program starting point to insert the corporate linkage
	 * (GLR) data file to the neo4j graph database.
	 * @param args
	 * args[0] should be the path to the input data file
	 * args[1] should be the number of header rows to skip (optional)
	 * args[2] should be the number of lines per transaction
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		logger.info("Starting InsertLinkageInfo");
		if (args.length < 1 || null == args[0] || null == args[0]){
			logger.error("Error, at least 1 argument expected. Input data file location" +
					", (optional) number of rows to skip, (optional) number of lines per transaction.");
			return;
		}
		
		logger.info("Running in {}", System.getProperty("user.dir"));
		
		logger.info("Opening data file");
		CsvReader input = new CsvReader(args[0]);
		
		if (args.length>1 && args[1]!=null){
			//skip rows based on command line argument
			int skip = Integer.parseInt(args[1]);
			logger.info("Skipping {} header rows.", skip);
			for(int i=0; i< skip; i++){
				input.readLine();
			}
		}
		
		//default to 1000;
		int maxTxCount = LINES_PER_TRANSACTION;
		if (args.length>2 && args[2]!=null){
			//number of lines per transaction
			maxTxCount = Integer.parseInt(args[2]);
		}
		logger.info("Setting maxTxCount to {}", maxTxCount);
		
		//logger.info("Opening Graph Database {}", LinkageContextManager.getDbPath());
	//	LinkageContextManager.createGraph();
		GraphDatabase graphDb = LinkageContextManager.getGraph();
        try {
        	long startTime = System.currentTimeMillis();
        	insertLinkageNodes(graphDb, input, maxTxCount);
        	long endTime = System.currentTimeMillis();
        	System.out.printf("Time taken to insert:%f\n",(double)(endTime-startTime));
        }finally{
        	//a little clean up
        	logger.info("Cleaning up");
        	logger.info("Shutting down graphdb database");
        	
        	graphDb.close();
        	input.close();
        }

	}	
	

	public static void insertLinkageNodes(GraphDatabase graphDb, CsvReader inputFile, final int linesPerTx) {
				
		//count the total number of lines & nodes processed
		int countTotal = 0;
		//count the number of lines in the current transaction. commit every so often
		int countTx = 0;
		
		Transaction tx = null;
		try {
						
			//setup
			//set up the various Group reference nodes
			//this should only be done 1 time per database
			//TODO refactor this to be in various node factories
			logger.info("Getting Group Reference Nodes");
			//need a transaction
			tx = graphDb.beginTransaction(AccessMode.READ_WRITE);
			SubjectFactory subjFac = new SubjectFactory(graphDb);
			GlobalUltimateFactory guFac = new GlobalUltimateFactory(graphDb);						
			tx.commit();
			logger.debug("Finished getting Group Reference Nodes");
			
			 			 
			// loop through each record in the input file
			tx = graphDb.beginTransaction(AccessMode.READ_WRITE);
			Subject newSubject = null;			
			String[] data;
			while( (data=inputFile.readLine()) != null) {
				
				//1. turn the input data strings in to a value object
				//representing the new subject node to be added
				LinkageInfoVo subjVo = new LinkageInfoVo(data, true);
				
				//if(lstGlobalDuns.contains(subjVo.getGlobalUltimateDunsNbr()))
				{
					//count the total number of lines processed
					countTotal++;
					//count the number of lines in this transaction. commit every so often
					countTx++;
				//2. check if the subject to be processed is already in the graph
				String subjDunsNbr = subjVo.getDunsNbr();
				Subject existing = subjFac.getSubjectByDuns(subjDunsNbr);
				if(null != existing ){
					//subject is already present -- need to do an update
					logger.trace("Subject with DUNS: {} already subject in graph", subjDunsNbr);
					//do the update!!
					newSubject = subjFac.updateSubject(existing, subjVo);
					
				} else {
					//subject not already in graph - new subject DUNS
					logger.trace("Subject with DUNS: {} not in graph. Adding", subjDunsNbr);
					newSubject = subjFac.createSubject(subjVo);
				}
				
				//3. create a relationship to the parent (owner) node
				//	the "owner" is the Published Owner DunsNbr
				String publishedOwnerDunsNbr = subjVo.getPublishedOwnerDunsNbr();
				if( publishedOwnerDunsNbr != null && !publishedOwnerDunsNbr.equals("") && !publishedOwnerDunsNbr.equals("null")){
					Subject owner = subjFac.getSubjectByDuns(publishedOwnerDunsNbr);
					if( null == owner ){
						//the owner is not yet present. create a stub subject to represent this owner.
						logger.trace("Owner node not present. Creating Stub. Case DUNS: {}.  Owner DUNS: {}", subjDunsNbr, publishedOwnerDunsNbr);
						owner = subjFac.createSubjectStub(publishedOwnerDunsNbr, "Owner for DUNS: "+ subjDunsNbr);
					}					
					//make it easier to re-run the import - delete existing relationship before adding new one
		/*			Relationship r1 = newSubject.getSingleRelationship(RelationTypes.OWNER, Direction.OUTGOING);
					if(null != r1) r1.delete();

					//link to the owner node
					newSubject.createRelationshipTo(owner, RelationTypes.OWNER);*/
					//link to the owner node
					RelationWith rel =new RelationWith(Relations.OWNER);
					newSubject.addEdge(rel, owner, EdgeKind.OUTGOING);
				}
				
				//4. check if there is a factual owner that is different from published owner
				String facOwnerDuns = subjVo.getFactualOwnerDunsNbr();
				if( facOwnerDuns != null && !facOwnerDuns.equals("") && !facOwnerDuns.equals(publishedOwnerDunsNbr)){
					logger.debug("Factual owner {} for case DUNS {}", facOwnerDuns, subjDunsNbr);
					//ok, there is a different factual owner.
					//create a relationship to the factual owner.
					Subject facOwner = subjFac.getSubjectByDuns(facOwnerDuns);
					if( null == facOwner ) {
						//the owner is not yet present. create a stub subject to represent this owner.
						logger.trace("Owner node not present. Creating Stub. Case DUNS: {}. Factual Owner DUNS: {}", subjDunsNbr, facOwnerDuns);
						facOwner = subjFac.createSubjectStub(facOwnerDuns, "Owner for DUNS: "+ subjDunsNbr);
					}
					
				/*	//make it easier to re-run the import - delete existing relationship before adding new one
					Relationship r1 = newSubject.getSingleRelationship(RelationTypes.OWNER_FACTUAL, Direction.OUTGOING);
					if(null != r1) r1.delete();
					
					//link to the owner node
					newSubject.createRelationshipTo(facOwner, RelationTypes.OWNER_FACTUAL);*/
					
					RelationWith rel =new RelationWith(Relations.OWNER_FACTUAL);
					newSubject.addEdge(rel, facOwner, EdgeKind.OUTGOING);
				}
								
				//5. check if there is a prior owner - not required for POC
				//TODO!!
				
				//6. check if this is a Global Ultimate
				String globalUltimateDuns = subjVo.getGlobalUltimateDunsNbr();
				if( globalUltimateDuns != null && globalUltimateDuns.equals( subjDunsNbr )){
					//case duns is same as global ultimate. link to global ultimate "factory"
					guFac.addGlobalUltimate(newSubject);
				}
				
				if (countTx >= linesPerTx){
                                    //System.out.println("Committing tx. countTotal: {}" +countTotal);
                                    //logger.info("Committing tx. countTotal: {}", countTotal);
					tx.commit();
					tx=graphDb.beginTransaction(AccessMode.READ_WRITE);
					countTx = 0;
				}
				
			}
			}
			tx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tx.rollback();
		} finally {
			logger.info("Committing final tx. countTotal: {}", countTotal);
			if(!tx.isCommitted())
			tx.commit();
			graphDb.close();
		}
		
		
		//logger.info("Finishing createCompanyInfoNodes");
	}

}
