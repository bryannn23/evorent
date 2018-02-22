package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableUnitDetail {
	
	private final SimpleStringProperty uID;
	private final SimpleStringProperty uName;
	private final SimpleStringProperty bldgID;
	private final SimpleStringProperty uFloor;
	private final SimpleStringProperty uLocation;
	private final SimpleStringProperty uType;
	private final SimpleStringProperty uArea;
	private final SimpleStringProperty uFeature;
	private final SimpleStringProperty uOtherFeature;
	private final SimpleStringProperty uBedroom;
	private final SimpleStringProperty uBathroom;
	private final SimpleStringProperty uMax;
	private final SimpleStringProperty uCondition;
	private final SimpleStringProperty uRentAmount;
	private final SimpleStringProperty uAssocAmount;
	private final SimpleStringProperty uRemarks;
	ImageView unitPhoto;
	private final SimpleStringProperty refid;

	//private final SimpleStringProperty ;
	
	public TableUnitDetail (String id, String name, String bldg, String floor, String location, 
			String type, String area, String feature, String otherfeature, String bed, String bath, 
			String max, String condi, String rent, String assoc, 
			String rem, ImageView photo1,  String ref){
		
		this.uID = new SimpleStringProperty(id);
		this.uName = new SimpleStringProperty(name);
		this.bldgID = new SimpleStringProperty(bldg);
		this.uFloor = new SimpleStringProperty(floor);
		this.uLocation = new SimpleStringProperty(location);
		this.uType = new SimpleStringProperty(type);
		this.uArea = new SimpleStringProperty(area);
		this.uFeature = new SimpleStringProperty(feature);
		this.uOtherFeature = new SimpleStringProperty(otherfeature);
		this.uBedroom = new SimpleStringProperty(bed);
		this.uBathroom = new SimpleStringProperty(bath);
		this.uMax = new SimpleStringProperty(max);
		this.uCondition = new SimpleStringProperty(condi);
		this.uRentAmount = new SimpleStringProperty(rent);
		this.uAssocAmount = new SimpleStringProperty(assoc);
		this.uRemarks = new SimpleStringProperty(rem);
		this.unitPhoto = photo1;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getUID() {
		return uID.get();
	}
	public String getUName() {
		return uName.get();
	}
	public String getBldgID() {
		return bldgID.get();
	}
	public String getUFloor() {
		return uFloor.get();
	}
	public String getULocation() {
		return uLocation.get();
	}
	public String getUType() {
		return uType.get();
	}
	public String getUArea() {
		return uArea.get();
	}
	public String getUFeature() {
		return uFeature.get();
	}
	public String getUOtherFeature() {
		return uOtherFeature.get();
	}
	public String getUBedroom() {
		return uBedroom.get();
	}
	public String getUBathroom() {
		return uBathroom.get();
	}
	public String getUMax() {
		return uMax.get();
	}
	public String getUCondition() {
		return uCondition.get();
	}
	public String getURentAmount() {
		return uRentAmount.get();
	}
	public String getUAssocAmount() {
		return uAssocAmount.get();
	}
	public String getURemarks() {
		return uRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setUID (String id){
		
		uID.set(id);
	}
	public void setUName (String name){
		uName.set(name);
	}

	public void setBldgID (String bldg){
		bldgID.set(bldg);
	}
	
	public void setUFloor (String floor){
		uFloor.set(floor);
	}

	public void setULocation (String location){
		uLocation.set(location);
	}
	
	public void setUType (String type){
		uType.set(type);
	}

	public void setUArea (String area){
		uArea.set(area);
	}

	public void setUFeature (String feature){
		uFeature.set(feature);
	}

	public void setUOtherFeature (String otherfeature){
		uOtherFeature.set(otherfeature);
	}

	public void setUBedroom (String bed){
		uBedroom.set(bed);
	}

	public void setUBathroom (String bath){
		uBathroom.set(bath);
	}
	
	public void setUMax (String max){
		uMax.set(max);
	}

	public void setUCondition (String condi){
		uCondition.set(condi);
	}

	public void setURentAmount (String rent){
		uRentAmount.set(rent);
	}

	public void setUAssocAmount (String assoc){
		uAssocAmount.set(assoc);
	}

	public void setURemarks (String rem){
		uRemarks.set(rem);
	}
	
	public void setRefid (String ref){
		refid.set(ref);
	}

    public ImageView getUnitPhoto () {
        return unitPhoto;
    }
 
    public void setUnitPhoto (ImageView photo1) {
        this.unitPhoto = photo1;
    }
 
}
