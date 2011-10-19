package com.linkage.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinitegraph.EdgeHandle;
import com.infinitegraph.EdgeKind;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.Vertex;
import com.infinitegraph.indexing.GenericIndex;
import com.infinitegraph.indexing.IndexException;
import com.infinitegraph.indexing.IndexManager;



//manages relationships for global ultimate subject nodes
//does not CREATE. so not *really* a factory
/**
 * @author mgubbala
 *
 */
public class GlobalUltimateFactory {
	private Subject factoryReferenceNode;
	GenericIndex<Subject> guIndex =null;
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(GlobalUltimateFactory.class);
	}
	
	public GlobalUltimateFactory(GraphDatabase graphDb){

		//check for the Reference node for Subjects
		Vertex gobalUltimateRef = graphDb.getNamedVertex("gobalUltimate");
		if(gobalUltimateRef == null)
		{
			  Subject gobalUltimate =new Subject(Relations.GLOBAL_ULTIMATE_REF);
	          
	          graphDb.addVertex(gobalUltimate);
	          gobalUltimate.setProperty(Fields.NAME, Groups.GLOBAL_ULTIMATES);
	          graphDb.nameVertex("gobalUltimate", gobalUltimate);
	          factoryReferenceNode = gobalUltimate;
		
	      	try {
				 guIndex =  IndexManager.<Subject>createGenericIndex("guIndex", Subject.class.getName(), "dunsNumber");
			} catch (IndexException e) {
				e.printStackTrace();
			}
		}
		else
		{
		Iterable<EdgeHandle> edges = gobalUltimateRef.getEdges();
		for (EdgeHandle edgeHandle : edges) {
			if (((RelationWith)edgeHandle.getEdge()).getRelation().equalsIgnoreCase(Relations.GLOBAL_ULTIMATE_REF))
			{
				factoryReferenceNode =(Subject)  edgeHandle.getPeer().getVertex();
			}
		}
		if (factoryReferenceNode == null)
		{
			Subject gobalUltimate =new Subject(Relations.GLOBAL_ULTIMATE_REF);
          
	        graphDb.addVertex(gobalUltimate);
	        gobalUltimate.setProperty(Fields.NAME, Groups.GLOBAL_ULTIMATES);
            graphDb.nameVertex("gobalUltimate", gobalUltimate);
	        factoryReferenceNode = gobalUltimate;
		}
		}
    	try {
			 guIndex =  IndexManager.<Subject>getGenericIndexByName("guIndex");
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}

	//add a new global ultimate subject to the collection
	//connect it to the subject factory node
	//add it to the index
	//does NOT create a new node!
	//TODO - note: we assume there is a transaction created & managed outside this method
	public void addGlobalUltimate(Subject gu) {
		
		String dunsNbr = gu.getDunsNumber();
		logger.info("Adding new Global Ultimate node DUNS: {}", dunsNbr);
		
		//try to remove first
		removeGlobalUltimateByDuns(dunsNbr);
		
		//now add to the collection
		RelationWith rel =new RelationWith(Relations.GLOBAL_ULTIMATE);
		factoryReferenceNode.addEdge(rel, gu, EdgeKind.INCOMING);
		
		try {
			guIndex.put(gu);
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param dunsNbr
	 * @return
	 */
	public Subject getGlobalUltimateByDuns(String dunsNbr){
		Subject subject = null;
		try {
			subject = guIndex.getSingleResult(dunsNbr);
		} catch (IndexException e) {
			e.printStackTrace();
		}
			return subject;
	
	}
	
	//removes a GU from the collection
	//does not delete the node
	public void removeGlobalUltimateByDuns(String dunsNbr) {
		Subject gu = getGlobalUltimateByDuns(dunsNbr);
		if( null != gu ){
			try {
				guIndex.remove(gu);
			} catch (IndexException e) {
				e.printStackTrace();
			}
		}
	}

}
