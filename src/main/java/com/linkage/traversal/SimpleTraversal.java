package com.linkage.traversal;


import java.util.ArrayList;
import java.util.List;

import com.infinitegraph.EdgeHandle;
import com.infinitegraph.EdgeKind;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.indexing.GenericIndex;
import com.infinitegraph.indexing.IndexException;
import com.infinitegraph.indexing.IndexManager;
import com.infinitegraph.navigation.Guide;
import com.infinitegraph.navigation.Hop;
import com.infinitegraph.navigation.NavigationResultHandler;
import com.infinitegraph.navigation.Navigator;
import com.infinitegraph.navigation.Path;
import com.infinitegraph.navigation.Qualifier;
import com.linkage.domain.RelationWith;
import com.linkage.domain.Relations;
import com.linkage.domain.Subject;
import com.linkage.graphdb.LinkageContextListener;
import com.linkage.vo.DunsUtil;
import com.linkage.vo.LinkageInfoVo;

public class SimpleTraversal {
	
	static GraphDatabase graph =null;
	
	static  {
			graph = LinkageContextListener.getGraphDatabase();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*String dunsNumber = DunsUtil.padDuns("001008986");
		Transaction transaction = graph.beginTransaction(AccessMode.READ);
		Iterable<Edge> edges = graph.getEdges();
		long edgesCount=0, verticesCount=0;
		for (Edge edge : edges) {
			edgesCount++;
		}
		
		Iterable<Vertex> vertices = graph.getVertices();
		for (Vertex vertex : vertices) {
			verticesCount++;
		}
		
		System.out.println("Vertices Count: "+ verticesCount +
				"\n Edges count" +edgesCount);
		LinkageInfoVo info=SimpleTraversal.getNode(dunsNumber);
		
		System.out.println("*********************************Start Node Details:*********************************");
		System.out.println(info.getDunsNbr());
		System.out.println(info.getBusName());
		System.out.println(info.getWbCtry());
		System.out.println("*********************************End Node Details:*********************************");
		
		
		LinkageInfoVo owner = SimpleTraversal.getOwner(info);
		if (owner!=null)
		{
		System.out.println("\n********************************* Start Owner Details: *********************************");
		System.out.println(owner.getDunsNbr());
		System.out.println(owner.getBusName());
		System.out.println(owner.getWbCtry());
		System.out.println("*********************************End Owner Details:*********************************");
		}
		
		System.out.println("\n********************************* Start GlobalUltimate Details:*********************************");
		System.out.println(getGlobalUltimateNode(dunsNumber).getDunsNbr());
		System.out.println(getGlobalUltimateNode(dunsNumber).getBusName());
		System.out.println("\n********************************* End GlobalUltimate Details:*********************************");
		
		
		System.out.println("\n********************************* Start Children Hierarcy Details:*********************************");
		LinkageInfoVo globalUltimateNode = SimpleTraversal.getGlobalUltimateNode(dunsNumber);
		
		Subject result = null;
		try {
			GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
			result = dunsIndex.getSingleResult(dunsNumber);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		List<Subject>lst=new ArrayList<Subject>();
		List<Subject> childrenHierarchy = chidrenHierarchy(result, lst);
		System.out.println(childrenHierarchy.size());
		
		System.out.println("\n********************************* End Children Hierarcy Details:*********************************");
		
		//147037154  949689020
		//getChildren("001009828");
		
		transaction.commit();
		if (graph!=null)
			graph.close();*/
	}
	
	/**
	 * @param dunsNumber
	 * @return
	 */
	public static LinkageInfoVo getNode(String dunsNumber) {
		LinkageInfoVo nodeProperties = null;
		try {
			dunsNumber =DunsUtil.padDuns(dunsNumber);
			GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
			Subject result = dunsIndex.getSingleResult(dunsNumber);
			nodeProperties = getNodeProperties(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		}
		return nodeProperties;
	}
	
	
	/**
	 * @param subject
	 * @return
	 */
	public static LinkageInfoVo getNodeProperties(Subject subject) {
		LinkageInfoVo infoVo = new LinkageInfoVo();
		if(subject != null) {
			infoVo.setDunsNbr(subject.getDunsNumber());
			if( subject.getAssnTypeCD()!=null)
				infoVo.setAssnTypeCd(subject.getAssnTypeCD());			
			infoVo.setOprgStCd(subject.getOprgStat_CD());			
			infoVo.setHierCode(subject.getHierCode());			
			infoVo.setBusName(subject.getBusNme());
			if( subject.getTrdgNme1()!=null)
				infoVo.setTrdgNme1(subject.getTrdgNme1());
			if( subject.getTrdgNme2()!=null)
				infoVo.setTrdgNme2(subject.getTrdgNme2());
			if( subject.getPhysAdrStrAdrLine()!=null)
				infoVo.setPhysAdrStrAdrLine(subject.getPhysAdrStrAdrLine());
			if( subject.getPhysAdrPostTown()!=null)
				infoVo.setPhysAdrPostTown( subject.getPhysAdrPostTown());
			if( subject.getPhysAdrPostCode()!=null)
				infoVo.setPhysAdrPostCode(subject.getPhysAdrPostCode());
			if( subject.getWbCtry()!=null)
				infoVo.setWbCtry(subject.getWbCtry());
			if( subject.getPhysAdrStrAdrLine2()!=null)
			infoVo.setPhysAdrStrAdrLine2(subject.getPhysAdrStrAdrLine2());
		}
		return infoVo;
	}
	
	static LinkageInfoVo owner;
	/**
	 * @param infoVO
	 * TODO: need to modify. No need of navigator
	 * @return
	 */
	public static LinkageInfoVo getOwner(LinkageInfoVo infoVO) {
		
		try {
			
			GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
			Subject result = dunsIndex.getSingleResult(infoVO.getDunsNbr());
			Navigator query =result.navigate(Guide.SIMPLE_BREADTH_FIRST, Qualifier.ANY, Qualifier.ANY, new NavigationResultHandler() {
				
				@Override
				public void handleResultPath(Path path, Navigator navigator) {
					for (Hop hop : path) {
						if(hop.hasEdge())
						{
						RelationWith edge = (RelationWith)hop.getEdge();
						if (edge.getRelation().equalsIgnoreCase(Relations.OWNER))
						{
							Subject vertex =(Subject) hop.getVertex();
							owner = getNodeProperties(vertex);
							System.out.println(vertex.getDunsNumber());
						}
						}
					}
				}
			});
			query.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			 
		}
		return owner;
	}
	
	
	/**
	 * @author mgubbala
	 *
	 */
	private static class GlobalUltimateNavigationHandler implements NavigationResultHandler
	{
		private LinkageInfoVo globalUltimateNode;

			@Override
			public void handleResultPath(Path path, Navigator navigator) {
				for (Hop hop : path) {
					if(hop.hasEdge())
					{
					RelationWith edge = (RelationWith)hop.getEdge();
					if (edge.getRelation().equalsIgnoreCase(Relations.GLOBAL_ULTIMATE))
					{
						globalUltimateNode = getNode( edge.getTarget().getVertex().getProperty("dunsNumber").toString());
					}
					}
			}
			}
		
		public LinkageInfoVo getGlobalUltimateNode()
		{
			return globalUltimateNode;
		}

	};
	/**
	 * @param dunsNumber
	 * @param graphDb
	 * @return
	 */
	public static LinkageInfoVo getGlobalUltimateNode(String dunsNumber) {
		GlobalUltimateNavigationHandler handler = new GlobalUltimateNavigationHandler();
		if(null != dunsNumber) {
			Subject result = null;
			try {
				GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
				result = dunsIndex.getSingleResult(dunsNumber);
			} catch (IndexException e) {
				e.printStackTrace();
			}
			if (result==null)
			{
				System.out.println(dunsNumber);
			}
			Navigator query = result.navigate( Guide.SIMPLE_DEPTH_FIRST,
				Qualifier.FOREVER, Qualifier.FOREVER, handler );
		query.start();
		}
		return handler.getGlobalUltimateNode();
	}
	
	/**
	 * @param dunsNumber
	 */
	public static List<Subject> getChildrenHierarchy(String dunsNumber) {
		
		Subject result = null;
		try {
			GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
			result = dunsIndex.getSingleResult(dunsNumber);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		List<Subject>lst=new ArrayList<Subject>();
		List<Subject> childrenHierarchy = chidrenHierarchy(result, lst);
		return childrenHierarchy;
	}
	
	/**
	 * @param dunsNumber
	 */
	private static void getChildren(String dunsNumber) {
		IncomingQualifier myQualifier=new IncomingQualifier();
		Subject result = null;
		try {
			GenericIndex<Subject>dunsIndex =  IndexManager.<Subject>getGenericIndexByName("dunsNumber");
			result = dunsIndex.getSingleResult(dunsNumber);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		Iterable<EdgeHandle> edges = result.getEdges(myQualifier);
		for (EdgeHandle edgeHandle : edges) {
			if (((RelationWith)edgeHandle.getEdge()).getRelation().equalsIgnoreCase(Relations.OWNER))
			System.out.println(edgeHandle.getEdge().getOrigin().getVertex().getProperty("dunsNumber"));
		}
	}
	
	
	static IncomingQualifier myQualifier=new IncomingQualifier();
 
	/**
	 * @param subject
	 * @param lstSubjects
	 */
	public static List<Subject> chidrenHierarchy(Subject subject, List<Subject>lstSubjects)
	  {
		  Iterable<EdgeHandle> edges = subject.getEdges(myQualifier);
			for (EdgeHandle edgeHandle : edges) {
				if (((RelationWith)edgeHandle.getEdge()).getRelation().equalsIgnoreCase(Relations.OWNER))
				{
					Subject child = (Subject)edgeHandle.getEdge().getOrigin().getVertex();
					lstSubjects.add(child);
					chidrenHierarchy(child, lstSubjects);
				}
			}
		  return lstSubjects;
	  }
	  
	/**
	 * @author mgubbala
	 *
	 */
	private static class IncomingQualifier implements Qualifier
	{
	    public boolean qualify(Path currentPath)
	    {
	        for(Hop h : currentPath) {
	            if(h.getEdgeHandle()!=null )
	            {
	            	 if( h.getEdgeHandle().getKind() == EdgeKind.INCOMING)
	            	 return true;
	            }
	        }
	        return false;
	    }
	}

}
