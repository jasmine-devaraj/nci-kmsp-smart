/*
 * Developed by Altum, Inc.
 * 12100 Sunset Hills Rd, Suite 101, Reston, VA 20190 USA
 * Copyright (c) 2000-2010 Altum, Inc.
 * All rights reserved.
 */
package com.altum.beans;

public class Code {
	private String id;
	private String typeId;
	private int orgId;
	private String name;

	/** Constructor */
	public Code() {
		//empty
	}
	/** Constructor */
	public Code(int organizationId, String typeId, String id){
		this.orgId=organizationId;
		this.typeId=typeId;
		this.id=id;
	}

//Simple Bean API
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + orgId;
		result = PRIME * result + ((typeId == null) ? 0 : typeId.hashCode());
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
