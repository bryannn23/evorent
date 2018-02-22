package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableTenantDetail {
	
	private final SimpleStringProperty tenantID;
	private final SimpleStringProperty tenantName;
	private final SimpleStringProperty tenantAddress;
	private final SimpleStringProperty tenantMobile;
	private final SimpleStringProperty tenantLandline;
	private final SimpleStringProperty tenantEAdd;
	private final SimpleStringProperty tenantBday;
	private final SimpleStringProperty tenantGender;
	private final SimpleStringProperty tenantAcct;
	private final SimpleStringProperty tenantStat;
	ImageView tenantPhoto;
	private final SimpleStringProperty refid;
	
	public TableTenantDetail (String id, String name, String address, String mobile, String landline, 
			String eadd, String bday, String gen, String acct, String stat, ImageView photo, String ref){
		
		this.tenantID = new SimpleStringProperty(id);
		this.tenantName = new SimpleStringProperty(name);
		this.tenantAddress = new SimpleStringProperty(address);
		this.tenantMobile = new SimpleStringProperty(mobile);
		this.tenantLandline = new SimpleStringProperty(landline);
		this.tenantEAdd = new SimpleStringProperty(eadd);
		this.tenantBday = new SimpleStringProperty(bday);
		this.tenantGender = new SimpleStringProperty(gen);
		this.tenantAcct = new SimpleStringProperty(acct);
		this.tenantStat = new SimpleStringProperty(stat);
		this.tenantPhoto = photo;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getTenantID() {
		return tenantID.get();
	}
	public String getTenantName() {
		return tenantName.get();
	}
	public String getTenantAddress() {
		return tenantAddress.get();
	}
	public String getTenantMobile() {
		return tenantMobile.get();
	}
	public String getTenantLandline() {
		return tenantLandline.get();
	}
	public String getTenantEAdd() {
		return tenantEAdd.get();
	}
	public String getTenantBday() {
		return tenantBday.get();
	}
	public String getTenantGender() {
		return tenantGender.get();
	}
	public String getTenantAcct() {
		return tenantAcct.get();
	}
	public String getTenantStat() {
		return tenantStat.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setTenantID (String id){
		
		tenantID.set(id);
	}
	public void setTenantName (String name){
		tenantName.set(name);
	}

	public void setTenantAddress (String address){
		tenantAddress.set(address);
	}
	
	public void setTenantMobile (String mobile){
		tenantMobile.set(mobile);
	}

	public void setTenantLandline (String landline){
		tenantLandline.set(landline);
	}
	
	public void setTenantEAdd (String eadd){
		tenantEAdd.set(eadd);
	}

	public void setTenantBday (String bday){
		tenantBday.set(bday);
	}

	public void setTenantGender (String gen){
		tenantGender.set(gen);
	}

	public void setTenantAcct (String acct){
		tenantAcct.set(acct);
	}

	public void setTenantStat (String stat){
		tenantStat.set(stat);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

    public ImageView getTenantPhoto () {
        return tenantPhoto;
    }

    public void setTenantPhoto (ImageView photo) {
        this.tenantPhoto = photo;
    }

    
}
