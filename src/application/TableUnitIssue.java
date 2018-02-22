package application;

import javafx.beans.property.SimpleStringProperty;

public class TableUnitIssue {
	
	private final SimpleStringProperty unitIssue;
	private final SimpleStringProperty unitID;
	private final SimpleStringProperty unitInspection;
	private final SimpleStringProperty issueName;
	private final SimpleStringProperty issueDesc;
	private final SimpleStringProperty actPlan;
	private final SimpleStringProperty issueStatus;
	private final SimpleStringProperty issueRemark;
	private final SimpleStringProperty refid;
	
	public TableUnitIssue (String id, String bldg, String inspect, String name, String desc, 
			String act, String status, String remark, String ref){
		
		this.unitIssue = new SimpleStringProperty(id);
		this.unitID = new SimpleStringProperty(bldg);
		this.unitInspection = new SimpleStringProperty(inspect);
		this.issueName = new SimpleStringProperty(name);
		this.issueDesc = new SimpleStringProperty(desc);
		this.actPlan = new SimpleStringProperty(act);
		this.issueStatus = new SimpleStringProperty(status);
		this.issueRemark = new SimpleStringProperty(remark);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getUnitIssue() {
		return unitIssue.get();
	}
	public String getUnitID() {
		return unitID.get();
	}
	public String getUnitInspection() {
		return unitInspection.get();
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
	public String getIssueStatus() {
		return issueStatus.get();
	}
	public String getIssueRemark() {
		return issueRemark.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setUnitIssue (String id){
		
		unitIssue.set(id);
	}
	public void setUnitID (String bldg){
		unitID.set(bldg);
	}

	public void setUnitInspection (String inspect){
		unitInspection.set(inspect);
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

	public void setIssueStatus (String status){
		issueStatus.set(status);
	}

	public void setIssueRemark (String remark){
		issueRemark.set(remark);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

}
