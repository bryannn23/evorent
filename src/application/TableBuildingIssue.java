package application;

import javafx.beans.property.SimpleStringProperty;

public class TableBuildingIssue {
	
	private final SimpleStringProperty bldgIssue;
	private final SimpleStringProperty bldgID;
	private final SimpleStringProperty bldgInspection;
	private final SimpleStringProperty issueName;
	private final SimpleStringProperty issueDesc;
	private final SimpleStringProperty actPlan;
	private final SimpleStringProperty issueRemark;
	private final SimpleStringProperty issueStatus;
	private final SimpleStringProperty refid;
	
	public TableBuildingIssue (String id, String bldg, String inspect, String name, String desc, 
			String act, String remark, String status, String ref){
		
		this.bldgIssue = new SimpleStringProperty(id);
		this.bldgID = new SimpleStringProperty(bldg);
		this.bldgInspection = new SimpleStringProperty(inspect);
		this.issueName = new SimpleStringProperty(name);
		this.issueDesc = new SimpleStringProperty(desc);
		this.actPlan = new SimpleStringProperty(act);
		this.issueRemark = new SimpleStringProperty(remark);
		this.issueStatus = new SimpleStringProperty(status);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getBldgIssue() {
		return bldgIssue.get();
	}
	public String getBldgID() {
		return bldgID.get();
	}
	public String getBldgInspection() {
		return bldgInspection.get();
	}
	public String getIssueName() {
		return issueName.get();
	}
	public String getIssueDesc() {
		return issueDesc.get();
	}
	public String getActPlan() {
		return actPlan.get();
	}
	public String getIssueRemark() {
		return issueRemark.get();
	}
	public String getIssueStatus() {
		return issueStatus.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setBldgIssue (String id){
		
		bldgIssue.set(id);
	}
	public void setBldgID (String bldg){
		bldgID.set(bldg);
	}

	public void setBldgInspection (String inspect){
		bldgInspection.set(inspect);
	}
	
	public void setIssueName (String name){
		issueName.set(name);
	}

	public void setIssueDesc (String desc){
		issueDesc.set(desc);
	}
	
	public void setActPlan (String act){
		actPlan.set(act);
	}

	public void setIssueRemark (String remark){
		issueRemark.set(remark);
	}

	public void setIssueStatus (String status){
		issueStatus.set(status);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

}
