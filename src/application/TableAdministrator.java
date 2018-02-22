package application;

import javafx.beans.property.SimpleStringProperty;

public class TableAdministrator {
	
	private final SimpleStringProperty adminid;
	private final SimpleStringProperty adminname;
	private final SimpleStringProperty adminaddress;
	private final SimpleStringProperty admincomid;
	private final SimpleStringProperty adminemailadd;
	private final SimpleStringProperty admincontact;
	private final SimpleStringProperty adminbuilding;
	private final SimpleStringProperty refid;
	
	public TableAdministrator (String id, String name, String address, String comid, String emailadd, String contact, String building, String ref){
		
		this.adminid = new SimpleStringProperty(id);
		this.adminname = new SimpleStringProperty(name);
		this.adminaddress = new SimpleStringProperty(address);
		this.admincomid = new SimpleStringProperty(comid);
		this.adminemailadd = new SimpleStringProperty(emailadd);
		this.admincontact = new SimpleStringProperty(contact);
		this.adminbuilding = new SimpleStringProperty(building);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getAdminid() {
		return adminid.get();
	}
	public String getAdminname() {
		return adminname.get();
	}
	public String getAdminaddress() {
		return adminaddress.get();
	}
	public String getAdmincomid() {
		return admincomid.get();
	}
	public String getAdminemailadd() {
		return adminemailadd.get();
	}
	public String getAdmincontact() {
		return admincontact.get();
	}
	public String getAdminbuilding() {
		return adminbuilding.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setAdminid (String id){
		
		adminid.set(id);
	}
	public void setAdminname (String name){
		adminname.set(name);
	}

	public void setAdminaddress (String address){
		adminaddress.set(address);
	}
	
	public void setAdmincomid (String comid){
		admincomid.set(comid);
	}

	public void setAdminemailadd (String emailadd){
		adminemailadd.set(emailadd);
	}
	
	public void setAdmincontact (String contact){
		admincontact.set(contact);
	}

	public void setAdminbuilding (String building){
		adminbuilding.set(building);
	}
	
	public void setRefid (String ref){
		refid.set(ref);
	}

}
