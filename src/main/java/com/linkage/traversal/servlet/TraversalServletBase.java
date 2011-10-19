package com.linkage.traversal.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.linkage.vo.DunsUtil;

public abstract class TraversalServletBase extends HttpServlet {
	private static final long serialVersionUID = 2L;

	/**
	 * @param request
	 * @return
	 */
	protected String getDunsParam(HttpServletRequest request){
		String pathString = request.getPathInfo();
		String rawDuns = pathString.substring(pathString.lastIndexOf("/") + 1).trim();
		String dunsNumber = DunsUtil.padDuns(rawDuns);
		return dunsNumber;
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	protected String getLinkageType(HttpServletRequest request){
		String pathString = request.getServletPath();		
		String linkageType = pathString.substring(1);		
		return linkageType;
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	protected String getFileParam(HttpServletRequest request){
		String pathString = request.getPathInfo();
		String filename = pathString.substring(pathString.lastIndexOf("/") + 1).trim();
		return filename;
	}
}
