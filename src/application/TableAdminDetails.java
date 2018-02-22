package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableAdminDetails {
	
	private final SimpleStringProperty adminID;
	private final SimpleStringProperty adminName;
	private final SimpleStringProperty adminMobile;
	private final SimpleStringProperty adminBday;
	private final SimpleStringProperty adminEadd;
	private final SimpleStringProperty adminAccount;
	private final SimpleStringProperty adminRemarks;
	ImageView adminPhoto;
	private final SimpleStringProperty refid;
	
	public TableAdminDetails (String id, String name, String mobile, String bday, String eadd, 
			String account, String rem, ImageView photo, String ref){
		
		this.adminID = new SimpleStringProperty(id);
		this.adminName = new SimpleStringProperty(name);
		this.adminMobile = new SimpleStringProperty(mobile);
		this.adminBday = new SimpleStringProperty(bday);
		this.adminEadd = new SimpleStringProperty(eadd);
		this.adminAccount = new SimpleStringProperty(account);
		this.adminRemarks = new SimpleStringProperty(rem);
		this.adminPhoto = photo;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getAdminID() {
		return adminID.get();
	}
	public String getAdminName() {
		return adminName.get();
	}
	public String getAdminMobile() {
		return adminMobile.get();
	}
	public String getAdminBday() {
		return adminBday.get();
	}
	public String getAdminEadd() {
		return adminEadd.get();
	}
	public String getAdminAccount() {
		return adminAccount.get();
	}
	public String getAdminRemarks() {
		return adminRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setAdminID (String id){
		
		adminID.set(id);
	}
	public void setAdminName (String name){
		adminName.set(name);
	}

	public void setAdminMobile (String mobile){
		adminMobile.set(mobile);
	}
	
	public void setAdminBday (String bday){
		adminBday.set(bday);
	}

	public void setAdminEadd (String eadd){
		adminEadd.set(eadd);
	}
	
	public void setAdminAccount (String account){
		adminAccount.set(account);
	}

	public void setAdminRemarks (String rem){
		adminRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

    public ImageView getAdminPhoto () {
        return adminPhoto;
    }

    public void setAdminPhoto (ImageView photo) {
        this.adminPhoto = photo;
    }

    
}
