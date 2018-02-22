package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableBuildingDetail {
	
	private final SimpleStringProperty bldgID;
	private final SimpleStringProperty bldgName;
	private final SimpleStringProperty bldgAddress;
	private final SimpleStringProperty bldgContact;
	private final SimpleStringProperty bldgDateEst;
	private final SimpleStringProperty bldgFloors;
	private final SimpleStringProperty bldgFeature;
	private final SimpleStringProperty bldgCondition;
	private final SimpleStringProperty bldgRemarks;
	ImageView bldgPhoto1;
	ImageView bldgPhoto2;
	ImageView bldgPhoto3;
	ImageView bldgPhoto4;
	private final SimpleStringProperty refid;
	
	public TableBuildingDetail (String id, String name, String address, String contact, String date, 
			String floors, String feature, String condi, String rem, ImageView photo1, ImageView photo2, 
			ImageView photo3, ImageView photo4, String ref){
		
		this.bldgID = new SimpleStringProperty(id);
		this.bldgName = new SimpleStringProperty(name);
		this.bldgAddress = new SimpleStringProperty(address);
		this.bldgContact = new SimpleStringProperty(contact);
		this.bldgDateEst = new SimpleStringProperty(date);
		this.bldgFloors = new SimpleStringProperty(floors);
		this.bldgFeature = new SimpleStringProperty(feature);
		this.bldgCondition = new SimpleStringProperty(condi);
		this.bldgRemarks = new SimpleStringProperty(rem);
		this.bldgPhoto1 = photo1;
		this.bldgPhoto2 = photo2;
		this.bldgPhoto3 = photo3;
		this.bldgPhoto4 = photo4;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getBldgID() {
		return bldgID.get();
	}
	public String getBldgName() {
		return bldgName.get();
	}
	public String getBldgAddress() {
		return bldgAddress.get();
	}
	public String getBlgContact() {
		return bldgContact.get();
	}
	public String getBldgDateEst() {
		return bldgDateEst.get();
	}
	public String getBldgFloors() {
		return bldgFloors.get();
	}
	public String getBldgFeature() {
		return bldgFeature.get();
	}
	public String getBldgCondition() {
		return bldgCondition.get();
	}
	public String getAdminRemarks() {
		return bldgRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setBldgID (String id){
		
		bldgID.set(id);
	}
	public void setBldgName (String name){
		bldgName.set(name);
	}

	public void setBldgAddress (String address){
		bldgAddress.set(address);
	}
	
	public void setBldgContact (String contact){
		bldgContact.set(contact);
	}

	public void setBldgDateEst (String date){
		bldgDateEst.set(date);
	}
	
	public void setBldgFloors (String floors){
		bldgFloors.set(floors);
	}

	public void setBldgFeature (String feature){
		bldgFeature.set(feature);
	}

	public void setBldgCondition (String condi){
		bldgCondition.set(condi);
	}

	public void setBldgRemarks (String rem){
		bldgRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

    public ImageView getBldgPhoto1 () {
        return bldgPhoto1;
    }
 
    public void setBldgPhoto1 (ImageView photo1) {
        this.bldgPhoto1 = photo1;
    }

    public ImageView getBldgPhoto2 () {
        return bldgPhoto2;
    }
 
    public void setBldgPhoto2 (ImageView photo2) {
        this.bldgPhoto2 = photo2;
    }

    public ImageView getBldgPhoto3 () {
        return bldgPhoto3;
    }
 
    public void setBldgPhoto3 (ImageView photo3) {
        this.bldgPhoto3 = photo3;
    }

    public ImageView getBldgPhoto4 () {
        return bldgPhoto4;
    }
 
    public void setBldgPhoto4 (ImageView photo4) {
        this.bldgPhoto4 = photo4;
    }

    
}
