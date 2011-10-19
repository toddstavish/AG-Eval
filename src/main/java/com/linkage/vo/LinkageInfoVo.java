package com.linkage.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author mgubbala
 *
 */
public class LinkageInfoVo {
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(LinkageInfoVo.class);
	}
	
	private String dunsNbr;
	private String assnTypeCd;	
	private String oprgStCd;	
	private String hierCode;	
	private String busName;
	private String trdgNme1;
	private String trdgNme2;
	private String physAdrStrAdrLine;
	private String physAdrPostTown;	
	private String physAdrPostCode;
	private String wbCtry;
	private String physAdrStrAdrLine2;
	private String glblUltDunsNbr;
	private String pubdOwnrDunsNbr;
	private String ownrDunsNbr;
	
	public LinkageInfoVo(String[] data, boolean isAdd) {		
		
		if(!isAdd) {
			// For updates - data from Atlas
			logger.debug("Retrieving update data from atlas file");
			this.setDunsNbr(data[0]);
			this.setAssnTypeCd(null);		
			this.setOprgStCd(data[12]);		
			this.setHierCode(data[2]);		
			this.setBusName(Util.cleanString(data[1]));
			this.setTrdgNme1(Util.cleanString(data[10]));
			this.setTrdgNme2(Util.cleanString(data[11]));
			this.setPhysAdrStrAdrLine(Util.cleanString( data[3] ));
			this.setPhysAdrPostTown(Util.cleanString(data[6]));		
			this.setPhysAdrPostCode(Util.cleanString(data[5]));
			this.setWbCtry(Util.cleanString(data[7]));
			this.setPhysAdrStrAdrLine2(Util.cleanString(data[4]));
			this.setGlobalUltimateDunsNbr(data[8]);
			this.setPublishedOwnerDunsNbr(data[9]);
			
		} else {
			// For add - data from GLR linkage
			logger.debug("Retrieving insert data from GLR file");
			this.setDunsNbr(data[1]);
			this.setAssnTypeCd(Util.cleanString( data[2] ));
			this.setFactualOwnerDunsNbr(data[3]);
			this.setPublishedOwnerDunsNbr(data[4]);		
			this.setGlobalUltimateDunsNbr(data[6]);
			this.setOprgStCd(data[7]);		
			this.setHierCode(data[13]);		
			this.setBusName(data[16]);
			this.setTrdgNme1(Util.cleanString( data[17] ));
			this.setTrdgNme2(Util.cleanString( data[18] ));
			this.setPhysAdrStrAdrLine(Util.cleanString( data[19] ));
			this.setPhysAdrPostTown(data[20]);		
			this.setPhysAdrPostCode(data[23]);
			this.setWbCtry(data[24]);
			this.setPhysAdrStrAdrLine2(Util.cleanString( data[25] ));
		}	
			
	}
	public LinkageInfoVo() {
		// TODO Auto-generated constructor stub
	}
	
	public String getDunsNbr() {
		return dunsNbr;
	}
	
	public void setDunsNbr(String dunsNbr) {
		this.dunsNbr = DunsUtil.padDuns(dunsNbr);
	}


	
	public String getAssnTypeCd() {
		return assnTypeCd;
	}
	public void setAssnTypeCd(String assnTypeCd) {
		this.assnTypeCd = assnTypeCd;
	}
	public String getFactualOwnerDunsNbr() {
		return ownrDunsNbr;
	}
	public void setFactualOwnerDunsNbr(String ownrDunsNbr) {
		this.ownrDunsNbr = DunsUtil.padDuns(ownrDunsNbr);
	}
	public String getPublishedOwnerDunsNbr() {
		return pubdOwnrDunsNbr;
	}
	public void setPublishedOwnerDunsNbr(String pubdOwnrDunsNbr) {
		this.pubdOwnrDunsNbr = DunsUtil.padDuns(pubdOwnrDunsNbr);
	}
	
	public String getGlobalUltimateDunsNbr() {
		return glblUltDunsNbr;
	}
	public void setGlobalUltimateDunsNbr(String glblUltDunsNbr) {
		this.glblUltDunsNbr = DunsUtil.padDuns(glblUltDunsNbr);
	}
	public String getOprgStCd() {
		return oprgStCd;
	}
	public void setOprgStCd(String oprgStCd) {
		this.oprgStCd = oprgStCd;
	}	
	
	public String getHierCode() {
		return hierCode;
	}
	public void setHierCode(String hierCode) {
		this.hierCode = hierCode;
	}
	
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getTrdgNme1() {
		return trdgNme1;
	}
	public void setTrdgNme1(String trdgNme1) {
		this.trdgNme1 = trdgNme1;
	}
	public String getTrdgNme2() {
		return trdgNme2;
	}
	public void setTrdgNme2(String trdgNme2) {
		this.trdgNme2 = trdgNme2;
	}
	public String getPhysAdrStrAdrLine() {
		return physAdrStrAdrLine;
	}
	public void setPhysAdrStrAdrLine(String physAdrStrAdrLine) {
		this.physAdrStrAdrLine = physAdrStrAdrLine;
	}
	public String getPhysAdrPostTown() {
		return physAdrPostTown;
	}
	public void setPhysAdrPostTown(String physAdrPostTown) {
		this.physAdrPostTown = physAdrPostTown;
	}
	
	public String getPhysAdrPostCode() {
		return physAdrPostCode;
	}
	public void setPhysAdrPostCode(String physAdrPostCode) {
		this.physAdrPostCode = physAdrPostCode;
	}
	public String getWbCtry() {
		return wbCtry;
	}
	public void setWbCtry(String wbCtry) {
		this.wbCtry = wbCtry;
	}
	public String getPhysAdrStrAdrLine2() {
		return physAdrStrAdrLine2;
	}
	public void setPhysAdrStrAdrLine2(String physAdrStrAdrLine2) {
		this.physAdrStrAdrLine2 = physAdrStrAdrLine2;
	}
	@Override
	public String toString() {
		return "LinkageInfoVo [assnTypeCd=" + assnTypeCd + ", busName="
				+ busName + ", dunsNbr=" + dunsNbr + ", glblUltDunsNbr="
				+ glblUltDunsNbr + ", hierCode=" + hierCode + ", oprgStCd="
				+ oprgStCd + ", ownrDunsNbr=" + ownrDunsNbr
				+ ", physAdrPostCode=" + physAdrPostCode + ", physAdrPostTown="
				+ physAdrPostTown + ", physAdrStrAdrLine=" + physAdrStrAdrLine
				+ ", physAdrStrAdrLine2=" + physAdrStrAdrLine2
				+ ", pubdOwnrDunsNbr=" + pubdOwnrDunsNbr + ", trdgNme1="
				+ trdgNme1 + ", trdgNme2=" + trdgNme2 + ", wbCtry=" + wbCtry
				+ "]";
	}
	
	


}
