package com.linkage.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinitegraph.EdgeHandle;
import com.infinitegraph.EdgeKind;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.Vertex;
import com.infinitegraph.indexing.GenericIndex;
import com.infinitegraph.indexing.IndexException;
import com.infinitegraph.indexing.IndexIterable;
import com.infinitegraph.indexing.IndexManager;
import com.linkage.vo.LinkageInfoVo;

/**
 * @author mgubbala
 *
 */
public class SubjectFactory {
	
	private GraphDatabase myGraphDB;
	private Subject factoryReferenceSubject=null;
	
	private static GenericIndex<Subject> dunsIndex =null;
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(SubjectFactory.class);
		try {
			dunsIndex = IndexManager.<Subject>getGenericIndexByName("dunsNumber");
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}
	

	public SubjectFactory(GraphDatabase graphDb){
		this.myGraphDB = graphDb;
		
		//check for the Reference node for Subjects
		Vertex rootnode = graphDb.getNamedVertex("rootnode");
		if(rootnode == null)
		{
			Subject rootNode = new Subject("rootnode");
            /* Subject reference */
			Subject subjectRef =new Subject("subjectref");
            RelationWith rel =new RelationWith(Relations.SUBJECT_REF);
            myGraphDB.addVertex(rootNode);
            myGraphDB.addVertex(subjectRef);
            rootNode.addEdge(rel, subjectRef, EdgeKind.OUTGOING);
            
            myGraphDB.nameVertex("rootnode", rootNode);
            myGraphDB.nameVertex("subjectref", subjectRef);
            factoryReferenceSubject = subjectRef;
		}
		else 
		{
		Iterable<EdgeHandle> edges = rootnode.getEdges();
		for (EdgeHandle edgeHandle : edges) {
			if (((RelationWith)edgeHandle.getEdge()).getRelation().equalsIgnoreCase(Relations.SUBJECT_REF))
			{
				factoryReferenceSubject =(Subject)  edgeHandle.getPeer().getVertex();
			}
		}
		if (factoryReferenceSubject == null)
		{
			Subject subjectRef =new Subject("subjectref");
	          RelationWith rel =new RelationWith(Relations.SUBJECT_REF);
	          
	          myGraphDB.addVertex(subjectRef);
	          subjectRef.setProperty(Fields.NAME, Groups.SUBJECTS);
	          rootnode.addEdge(rel, subjectRef, EdgeKind.OUTGOING);
	          myGraphDB.nameVertex("subjectref", subjectRef);
	          factoryReferenceSubject = subjectRef;
		}
		}
	}
	
	/**
	 * @param duns
	 * @param busNme
	 * @return
	 */
	public Subject createSubjectStub(String duns, String busNme){
		Subject subject =new Subject();
		subject.setDunsNumber(duns);
		subject.setBusNme(busNme);
		
		myGraphDB.addVertex(subject);
		RelationWith rel =new RelationWith(Relations.SUBJECT);
		factoryReferenceSubject.addEdge(rel, subject, EdgeKind.OUTGOING);
		
		try {
			dunsIndex.put(subject);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		
		return subject;
	}

	//create a new subject 
	//add it to the graph
	//connect it to the subject factory node
	//add it to the index
	//TODO - note: we assume there is a transaction created & managed outside this method
	public Subject createSubject(LinkageInfoVo vo) {
		logger.trace("Adding new Subject vertex DUNS: {}", vo.getDunsNbr());
		
		Subject subject = new Subject();
		subject.setDunsNumber(vo.getDunsNbr());
		if(null != vo.getAssnTypeCd())
		subject.setAssnTypeCD( vo.getAssnTypeCd());
		subject.setOprgStat_CD(vo.getOprgStCd());
		subject.setHierCode(vo.getHierCode());
		subject.setBusNme(vo.getBusName());
		if(null != vo.getTrdgNme1())
		subject.setTrdgNme1(vo.getTrdgNme1());
		if(null != vo.getTrdgNme2())
		subject.setTrdgNme2(vo.getTrdgNme2());
		if(null != vo.getPhysAdrStrAdrLine())
		subject.setPhysAdrStrAdrLine(vo.getPhysAdrStrAdrLine());
		subject.setPhysAdrPostTown(vo.getPhysAdrPostTown());
		subject.setPhysAdrPostCode(vo.getPhysAdrPostCode());
		subject.setWbCtry(vo.getWbCtry());
		if(null != vo.getPhysAdrStrAdrLine2())
		subject.setPhysAdrStrAdrLine2(vo.getPhysAdrStrAdrLine2());
		
		myGraphDB.addVertex(subject);
		RelationWith rel =new RelationWith(Relations.SUBJECT);
		factoryReferenceSubject.addEdge(rel, subject, EdgeKind.OUTGOING);
		
		 myGraphDB.nameVertex(vo.getDunsNbr(), subject);
		
		 try {
			dunsIndex.put(subject);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		 
		return subject;
	}
	
	
	//update an existing subject node (stub or full) with
	//new data
	/**
	 * @param newVo
	 * @param stub
	 * @return
	 */
	public Subject updateSubject(Subject existing, LinkageInfoVo vo) {

		logger.trace("Updating new Subject vertex DUNS: {}", vo.getDunsNbr());
		
		if(null != vo.getAssnTypeCd())
		existing.setAssnTypeCD( vo.getAssnTypeCd());
		existing.setOprgStat_CD(vo.getOprgStCd());
		existing.setHierCode(vo.getHierCode());
		existing.setBusNme(vo.getBusName());
		if(null != vo.getTrdgNme1())
		existing.setTrdgNme1(vo.getTrdgNme1());
		if(null != vo.getTrdgNme2())
		existing.setTrdgNme2(vo.getTrdgNme2());
		if(null != vo.getPhysAdrStrAdrLine())
		existing.setPhysAdrStrAdrLine(vo.getPhysAdrStrAdrLine());
		existing.setPhysAdrPostTown(vo.getPhysAdrPostTown());
		existing.setPhysAdrPostCode(vo.getPhysAdrPostCode());
		existing.setWbCtry(vo.getWbCtry());
		if(null != vo.getPhysAdrStrAdrLine2())
		existing.setPhysAdrStrAdrLine2(vo.getPhysAdrStrAdrLine2());
		
		return existing;
	}
	
	
	/**
	 * @param dunsNbr
	 * @return
	 * @throws IndexException
	 */
	public Subject getSubjectByDuns(String dunsNbr) throws IndexException{
		 GenericIndex<Subject> dunsIndex = IndexManager.<Subject>getGenericIndexByName("dunsNumber");
		 Subject subject = dunsIndex.getSingleResult(dunsNbr);
		 return subject;
	}

}
