package application;

import javafx.beans.property.SimpleStringProperty;

public class TableBuildingMaintenance {
	
	private final SimpleStringProperty mxID;
	private final SimpleStringProperty bldgID;
	private final SimpleStringProperty mxRef;
	private final SimpleStringProperty mxDate;
	private final SimpleStringProperty mxActivity;
	private final SimpleStringProperty mxDesc;
	private final SimpleStringProperty mxContractor;
	private final SimpleStringProperty mxStatus;
	private final SimpleStringProperty mxTotalCost;
	private final SimpleStringProperty mxRemarks;
	private final SimpleStringProperty refid;
	
	public TableBuildingMaintenance (String id, String bldg, String mref, String date, String activity, String desc, 
			String contractor, String stat, String cost, String rem, String ref){
		
		this.mxID = new SimpleStringProperty(id);
		this.bldgID = new SimpleStringProperty(bldg);
		this.mxRef = new SimpleStringProperty(mref);
		this.mxDate = new SimpleStringProperty(date);
		this.mxActivity = new SimpleStringProperty(activity);
		this.mxDesc = new SimpleStringProperty(desc);
		this.mxContractor = new SimpleStringProperty(contractor);
		this.mxStatus = new SimpleStringProperty(stat);
		this.mxTotalCost = new SimpleStringProperty(cost);
		this.mxRemarks = new SimpleStringProperty(rem);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getMxID() {
		return mxID.get();
	}
	public String getBldgID() {
		return bldgID.get();
	}
	public String getMxRef() {
		return mxRef.get();
	}
	public String getMxDate() {
		return mxDate.get();
	}
	public String getMxActivity() {
		return mxActivity.get();
	}
	public String getMxDesc() {
		return mxDesc.get();
	}
	public String getMxContractor() {
		return mxContractor.get();
	}
	public String getMxStatus() {
		return mxStatus.get();
	}
	public String getMxTotalCost() {
		return mxTotalCost.get();
	}
	public String getMxRemarks() {
		return mxRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setMxID (String id){
		
		mxID.set(id);
	}
	public void setBlgID (String bldg){
		bldgID.set(bldg);
	}

	public void setMxRef (String mref){
		mxRef.set(mref);
	}
	
	public void setMxDate (String date){
		mxDate.set(date);
	}
	
	public void setMxActivity (String activity){
		mxActivity.set(activity);
	}

	public void setMxDesc (String desc){
		mxDesc.set(desc);
	}
	
	public void setMxContractor (String contractor){
		mxContractor.set(contractor);
	}

	public void setMxStatus (String stat){
		mxStatus.set(stat);
	}

	public void setMxTotalCost (String cost){
		mxTotalCost.set(cost);
	}

	public void setMxRemarks (String rem){
		mxRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

}
