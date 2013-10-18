/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ProjectCoding {
	private int applID;
	private int fy;
	private String projNum;
	private String projTitle;
	private String orgName;
	private String city;
	private String state;
	private String pi;
	private String po;
	private String cancerActivity;
	private String division;
	private Date projStartDate;
	private Date projEndDate;
	private double amountOblig;
	private String typeId;
	private String code;
	private String codeName;
	private String percentRel;
	private String strRcdcCodeList;
	private List<String> rcdcCodeList;

	/** Constructor */
	public ProjectCoding() {
		rcdcCodeList = new ArrayList();
	}

//Simple Bean API
	public int getApplID() {
		return applID;
	}
	public void setApplID(int applID) {
		this.applID = applID;
	}
	public int getFy() {
		return fy;
	}
	public void setFy(int fy) {
		this.fy = fy;
	}
	public String getProjNum() {
		return projNum;
	}
	public void setProjNum(String projNum) {
		this.projNum = projNum;
	}
	public String getProjTitle() {
		return projTitle;
	}
	public void setProjTitle(String projTitle) {
		this.projTitle = projTitle;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPi() {
		return pi;
	}
	public void setPi(String pi) {
		this.pi = pi;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}
	public String getCancerActivity() {
		return cancerActivity;
	}
	public void setCancerActivity(String cancerActivity) {
		this.cancerActivity = cancerActivity;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public Date getProjStartDate() {
		return projStartDate;
	}
	public void setProjStartDate(Date projStartDate) {
		this.projStartDate = projStartDate;
	}
	public Date getProjEndDate() {
		return projEndDate;
	}
	public void setProjEndDate(Date projEndDate) {
		this.projEndDate = projEndDate;
	}
	public double getAmountOblig() {
		return amountOblig;
	}
	public void setAmountOblig(double amountOblig) {
		this.amountOblig = amountOblig;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getPercentRel() {
		return percentRel;
	}
	public void setPercentRel(String percentRel) {
		this.percentRel = percentRel;
	}
	public String getStrRcdcCodeList(){
		return strRcdcCodeList;
	}
	public void setStrRcdcCodeList(String val){
		strRcdcCodeList = val;
		setRcdcCodeList(val);
	}
	public List<String> getRcdcCodeList(){
		return rcdcCodeList;
	}
	public void setRcdcCodeList(String val){
		if(val!=null){
			StringTokenizer st = new StringTokenizer(val, ";");
			while(st.hasMoreTokens()){
				rcdcCodeList.add(st.nextToken());
			}
		}
	}
	public void setRcdcCodeList(List<String> val){
		rcdcCodeList = val;
	}
}
