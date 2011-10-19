package com.linkage.traversal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.infinitegraph.AccessMode;
import com.infinitegraph.GraphDatabase;
import com.infinitegraph.Transaction;
import com.linkage.domain.Subject;
import com.linkage.graphdb.LinkageContextListener;
import com.linkage.traversal.SimpleTraversal;
import com.linkage.vo.LinkageInfoVo;

/**
 * Servlet implementation class GetFullFamily
 */
public class GetFullFamily extends TraversalServletBase {
	private static final long serialVersionUID = 2L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFullFamily() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dunsNumber = this.getDunsParam(request);
		String linkageType = this.getLinkageType(request);
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");	
		out.println("<html>");
		out.println("<head><title>Full Family</title></head>");
		out.println("<body>");
		long startTime = System.currentTimeMillis();
		GraphDatabase graph= LinkageContextListener.getGraphDatabase();
	 
		Transaction transaction = graph.beginTransaction(AccessMode.READ);
		try {
			LinkageInfoVo subj = SimpleTraversal.getNode(dunsNumber);
			LinkageInfoVo globalUltimateNode = SimpleTraversal.getGlobalUltimateNode(dunsNumber);
			List<Subject> children = (List<Subject>) SimpleTraversal.getChildrenHierarchy(dunsNumber);

			if(children != null && children.size() > 0) {
				out.println("</br");			
				out.println("<h3>Given Node Duns Number= " 
						+ subj.getDunsNbr() + ": " 
						+ subj.getBusName()
						+"</h3>");
				out.println("</br");
				
				if("fullfamilyattrib".equals(linkageType)) {
					out.println("<h3>Global Ultimate Node Duns Number= " 
							+ globalUltimateNode.getDunsNbr() + ": " 
							+ globalUltimateNode.getBusName() + ": "
							+ globalUltimateNode.getAssnTypeCd() + ": "
							+ globalUltimateNode.getOprgStCd() + ": "
							+ globalUltimateNode.getWbCtry() + ": "
							+ globalUltimateNode.getHierCode() + ": "
							+ globalUltimateNode.getTrdgNme1() + ": "
							+ globalUltimateNode.getTrdgNme2() + ": "
							+ globalUltimateNode.getPhysAdrStrAdrLine() + ": "
							+ globalUltimateNode.getPhysAdrStrAdrLine2() + ": "
							+ globalUltimateNode.getPhysAdrPostTown() + ": "
							+ globalUltimateNode.getPhysAdrPostTown()
							+"</h3>");				
					
				} else {
					
					out.println("<h3>Global Ultimate Node Duns Number= " 
							+ globalUltimateNode.getDunsNbr() + ": " 
							+ globalUltimateNode.getBusName()
							+"</h3>");
				}
				int n=0;
				for(Subject child:children) {
					n++;
					out.println("");
					if("fullfamilyattrib".equals(linkageType)) {
						out.println("<h3>" 
								+ child.getDunsNumber()+ ": " 
								+ child.getBusNme() + ": "
								+ child.getAssnTypeCD() + ": "
								+ child.getOprgStat_CD() + ": "
								+ child.getWbCtry() + ": "
								+ child.getHierCode() + ": "
								+ child.getTrdgNme1() + ": "
								+ child.getTrdgNme2() + ": "
								+ child.getPhysAdrStrAdrLine() + ": "
								+ child.getPhysAdrStrAdrLine2() + ": "
								+ child.getPhysAdrPostTown() + ": "
								+ child.getPhysAdrPostTown()
								+"</h3>");
						
					} else {
						
						out.println("<h3>" 
								+ child.getDunsNumber()  + ": " 
								+ child.getBusNme()
								+ "</h3>");
					}
					
					
				}
				out.println("Number retrieved: " + n);
			} else {
				out.println("No children found for DUNS: " + dunsNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
		transaction.commit();
		}
		long endTime = System.currentTimeMillis();
		out.println("Processing time = " + (endTime-startTime) + " milliseconds");
		out.println("</body>");
		out.println("</html>");		
		out.close();
	}

}
