package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableAdminManagement {
	
	private final SimpleStringProperty mngmtID;
	private final SimpleStringProperty adminName;
	private final SimpleStringProperty bldgName;
	private final SimpleStringProperty mngmtStatus;
	private final SimpleStringProperty mngmtRemarks;
	private final SimpleStringProperty refid;
	
	public TableAdminManagement (String id, String name, String bldg, String status, 
			String remarks, String ref){
		
		this.mngmtID = new SimpleStringProperty(id);
		this.adminName = new SimpleStringProperty(name);
		this.bldgName = new SimpleStringProperty(bldg);
		this.mngmtStatus = new SimpleStringProperty(status);
		this.mngmtRemarks = new SimpleStringProperty(remarks);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getMngmtID() {
		return mngmtID.get();
	}
	public String getAdminName() {
		return adminName.get();
	}
	public String getBldgName() {
		return bldgName.get();
	}
	public String getMngmtStatus() {
		return mngmtStatus.get();
	}
	
	public String getMngmtRemarks() {
		return mngmtRemarks.get();
	}
	
	public String getRefid() {
		return refid.get();
	}

	public void setMngmtID (String id){
		
		mngmtID.set(id);
	}
	public void setAdminName (String name){
		adminName.set(name);
	}

	public void setBldgName (String bldg){
		bldgName.set(bldg);
	}
	
	public void setMngmtStatus (String status){
		mngmtStatus.set(status);
	}

	public void setMngmtRemarks (String remarks){
		mngmtRemarks.set(remarks);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}
    
}
