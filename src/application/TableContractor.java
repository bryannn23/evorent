package application;

import javafx.beans.property.SimpleStringProperty;

public class TableContractor {
	
	private final SimpleStringProperty contrID;
	private final SimpleStringProperty contrName;
	private final SimpleStringProperty contrAddress;
	private final SimpleStringProperty contrContPerson;
	private final SimpleStringProperty contrContNo;
	private final SimpleStringProperty contrRemarks;
	private final SimpleStringProperty refid;
	
	public TableContractor (String id, String name, String address, String contperson, String contno,
			 String rem,  String ref) {
		
		this.contrID = new SimpleStringProperty(id);
		this.contrName = new SimpleStringProperty(name);
		this.contrAddress = new SimpleStringProperty(address);
		this.contrContPerson = new SimpleStringProperty(contperson);
		this.contrContNo = new SimpleStringProperty(contno);
		this.contrRemarks = new SimpleStringProperty(rem);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getContrID() {
		return contrID.get();
	}
	public String getContrName() {
		return contrName.get();
	}
	public String getContrAddress() {
		return contrAddress.get();
	}
	public String getContrContPerson() {
		return contrContPerson.get();
	}
	public String getContrContNo() {
		return contrContNo.get();
	}
	public String getContrRemarks() {
		return contrRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setContrID (String id){
		
		contrID.set(id);
	}
	public void setContrName (String name){
		contrName.set(name);
	}

	public void setContrAddress (String address){
		contrAddress.set(address);
	}
	
	public void setContrContPerson (String contperson){
		contrContPerson.set(contperson);
	}

	public void setContrContNo (String contno){
		contrContNo.set(contno);
	}
	
	public void setContrRemarks (String rem){
		contrRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

	
}
