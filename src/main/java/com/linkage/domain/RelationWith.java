package com.linkage.domain;

import com.infinitegraph.BaseEdge;

/**
 * @author mgubbala
 *
 */
public class RelationWith extends BaseEdge
{

    private String relation;
    
    public RelationWith(String relation) {
    	this.setRelation(relation);
	}
    
	public String getRelation() {
		fetch();
		return relation;
	}

	public void setRelation(String relation) {
		 markModified();
		this.relation = relation;
	}

	public String getLabel()
    {
        return " "+relation ;
    } 
}