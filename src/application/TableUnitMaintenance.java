package application;

import javafx.beans.property.SimpleStringProperty;

public class TableUnitMaintenance {
	
	private final SimpleStringProperty mxID;
	private final SimpleStringProperty issueID;
	private final SimpleStringProperty unitID;
	private final SimpleStringProperty unitRef;
	private final SimpleStringProperty umxDate;
	private final SimpleStringProperty umxAct;
	private final SimpleStringProperty umxDesc;
	private final SimpleStringProperty umxRequest;
	private final SimpleStringProperty occpID;
	private final SimpleStringProperty umxContractor;
	private final SimpleStringProperty umxStatus;
	private final SimpleStringProperty umxTotal;
	private final SimpleStringProperty umxRemarks;
	private final SimpleStringProperty refid;
	
	public TableUnitMaintenance (String id, String issue, String unit, String refID, String date, String act, 
			String desc, String request, String occp, String contractor, String status, String total, 
			String rem, String ref){
		
		this.mxID = new SimpleStringProperty(id);
		this.issueID = new SimpleStringProperty(issue);
		this.unitID = new SimpleStringProperty(unit);
		this.unitRef = new SimpleStringProperty(refID);
		this.umxDate = new SimpleStringProperty(date);
		this.umxAct = new SimpleStringProperty(act);
		this.umxDesc = new SimpleStringProperty(desc);
		this.umxRequest = new SimpleStringProperty(request);
		this.occpID = new SimpleStringProperty(occp);
		this.umxContractor = new SimpleStringProperty(contractor);
		this.umxStatus = new SimpleStringProperty(status);
		this.umxTotal = new SimpleStringProperty(total);
		this.umxRemarks = new SimpleStringProperty(rem);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getMxID() {
		return mxID.get();
	}
	public String getIssueID() {
		return issueID.get();
	}
	public String getUnitID() {
		return unitID.get();
	}
	public String getUnitRef() {
		return unitRef.get();
	}
	public String getUmxDate() {
		return umxDate.get();
	}
	public String getUmxAct() {
		return umxAct.get();
	}
	public String getUmxDesc() {
		return umxDesc.get();
	}
	public String getUmxRequest() {
		return umxRequest.get();
	}
	public String getOccpID() {
		return occpID.get();
	}
	public String getUmxContractor() {
		return umxContractor.get();
	}
	public String getUmxStatus() {
		return umxStatus.get();
	}
	public String getUmxTotal() {
		return umxTotal.get();
	}
	public String getUmxRemarks() {
		return umxRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setMxID (String id){
		
		mxID.set(id);
	}
	public void setIssueID (String issue){
		issueID.set(issue);
	}

	public void setUnitID (String unit){
		unitID.set(unit);
	}

	public void setUnitRef (String refID){
		unitRef.set(refID);
	}
	
	public void setUmxDate (String date){
		umxDate.set(date);
	}

	public void setUmxAct (String act){
		umxAct.set(act);
	}
	
	public void setUmxDesc (String desc){
		umxDesc.set(desc);
	}

	public void setUmxRequest (String request){
		umxRequest.set(request);
	}

	public void setOccpID (String occp){
		occpID.set(occp);
	}

	public void setUmxContractor (String contractor){
		umxContractor.set(contractor);
	}

	public void setUmxStatus (String status){
		umxStatus.set(status);
	}

	public void setUmxTotal (String total){
		umxTotal.set(total);
	}

	public void setUmxRemarks (String rem){
		umxRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

}
