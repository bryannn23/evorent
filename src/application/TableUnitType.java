package application;

import javafx.beans.property.SimpleStringProperty;

public class TableUnitType {
	
	private final SimpleStringProperty utID;
	private final SimpleStringProperty utName;
	private final SimpleStringProperty bldgID;
	private final SimpleStringProperty utDesc;
	private final SimpleStringProperty utArea;
	private final SimpleStringProperty utPrice;
	private final SimpleStringProperty utMax;
	private final SimpleStringProperty refid;
	
	public TableUnitType (String id, String name, String bldg, String desc, String area, String price, String max, String ref )
	{
		
		this.utID = new SimpleStringProperty(id);
		this.utName = new SimpleStringProperty(name);
		this.bldgID = new SimpleStringProperty(bldg);
		this.utDesc = new SimpleStringProperty(desc);
		this.utArea = new SimpleStringProperty(area);
		this.utPrice = new SimpleStringProperty(price);
		this.utMax = new SimpleStringProperty(max);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getUtID() {
		return utID.get();
	}
	public String getUtName() {
		return utName.get();
	}
	public String getBldgID() {
		return bldgID.get();
	}
	public String getUtDesc() {
		return utDesc.get();
	}
	public String getUtArea() {
		return utArea.get();
	}
	public String getUtPrice() {
		return utPrice.get();
	}
	public String getUtMax() {
		return utMax.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setUtID (String id){		
		utID.set(id);
	}
	public void setUtName (String name){
		utName.set(name);
	}
	public void setBldgID (String bldg){
		bldgID.set(bldg);
	}	
	public void setUtDesc (String desc){
		utDesc.set(desc);
	}	
	public void setUtArea (String area){
		utArea.set(area);
	}	
	public void setUtPrice (String price){
		utPrice.set(price);
	}	
	public void setUtMax (String max){
		utMax.set(max);
	}
	public void setRefid (String ref){
		refid.set(ref);
	}
	

}
