package com.linkage.domain;

import com.infinitegraph.BaseVertex;

/**
 * @author mgubbala
 *
 */
public class Subject extends BaseVertex
{
    private String name;
    
    private String dunsNumber;
    private String assnTypeCD;
    private String oprgStat_CD;
    private String hierCode;
    private String busNme;
    private String trdgNme1;
    private String trdgNme2;
    private String physAdrStrAdrLine;
    private String physAdrPostTown;
    private String physAdrPostCode;
    private String wbCtry;
    private String physAdrStrAdrLine2;
    
    public Subject()
    {
    	
    }
    
    public Subject(String name)
    {
    	this.name =name;
    }
    
    public Subject(String name, String dunsNumber)
    {
    this.name =name;
    this.dunsNumber = dunsNumber;
    }
    
	public String getName() {
		fetch();
		return name;
	}
	public void setName(String name) {
		markModified();
		this.name = name;
	}
	public String getDunsNumber() {
		fetch();
		return dunsNumber;
	}
	public void setDunsNumber(String dunsNumber) {
		markModified();
		this.dunsNumber = dunsNumber;
	}
	public String getAssnTypeCD() {
		fetch();
		return assnTypeCD;
	}
	public void setAssnTypeCD(String assnTypeCD) {
		markModified();
		this.assnTypeCD = assnTypeCD;
	}
	public String getOprgStat_CD() {
		fetch();
		return oprgStat_CD;
	}
	public void setOprgStat_CD(String oprgStatCD) {
		markModified();
		oprgStat_CD = oprgStatCD;
	}
	public String getHierCode() {
		fetch();
		return hierCode;
	}
	public void setHierCode(String hierCode) {
		markModified();
		this.hierCode = hierCode;
	}
	public String getBusNme() {
		 fetch();
		return busNme;
	}
	public void setBusNme(String busNme) {
		markModified();
		this.busNme = busNme;
	}
	public String getTrdgNme1() {
		fetch();
		return trdgNme1;
	}
	public void setTrdgNme1(String trdgNme1) {
		markModified();
		this.trdgNme1 = trdgNme1;
	}
	public String getTrdgNme2() {
		fetch();
		return trdgNme2;
	}
	public void setTrdgNme2(String trdgNme2) {
		markModified();
		this.trdgNme2 = trdgNme2;
	}
	public String getPhysAdrStrAdrLine() {
		fetch();
		return physAdrStrAdrLine;
	}
	public void setPhysAdrStrAdrLine(String physAdrStrAdrLine) {
		markModified();
		this.physAdrStrAdrLine = physAdrStrAdrLine;
	}
	public String getPhysAdrPostTown() {
		fetch();
		return physAdrPostTown;
	}
	public void setPhysAdrPostTown(String physAdrPostTown) {
		markModified();
		this.physAdrPostTown = physAdrPostTown;
	}
	public String getPhysAdrPostCode() {
		fetch();
		return physAdrPostCode;
	}
	public void setPhysAdrPostCode(String physAdrPostCode) {
		markModified();
		this.physAdrPostCode = physAdrPostCode;
	}
	public String getWbCtry() {
		fetch();
		return wbCtry;
	}
	public void setWbCtry(String wbCtry) {
		markModified();
		this.wbCtry = wbCtry;
	}
	public String getPhysAdrStrAdrLine2() {
		fetch();
		return physAdrStrAdrLine2;
	}
	public void setPhysAdrStrAdrLine2(String physAdrStrAdrLine2) {
		markModified();
		this.physAdrStrAdrLine2 = physAdrStrAdrLine2;
	}
	@Override
    public String toString()
    {
		fetch();
        return "NAME= " + this.name + "DUNS_NUMBER= " + this.dunsNumber;
    }
}
