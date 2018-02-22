package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableOccupancyDetail {
	
	private final SimpleStringProperty occpID;
	private final SimpleStringProperty tenantID;
	private final SimpleStringProperty unitID;
	private final SimpleStringProperty occpDelivery;
	private final SimpleStringProperty occpStart;
	private final SimpleStringProperty occpEnd;
	private final SimpleStringProperty occpBilling;
	private final SimpleStringProperty occpRent;
	private final SimpleStringProperty occpStatus;
	private final SimpleStringProperty occpDeposit;
	private final SimpleStringProperty occpRemDeposit;
	private final SimpleStringProperty occpNoTenant;
	private final SimpleStringProperty occpFellowTenant;
	private final SimpleStringProperty occpRemarks;
	private final SimpleStringProperty occpPhoto;
	private final SimpleStringProperty refid;
	private final SimpleStringProperty octenant;
	private final SimpleStringProperty ocunit;
	
	public TableOccupancyDetail (String id, String tenant, String unit, String delivery, String start, 
			String end, String billing, String rent, String status, String deposit, String remdeposit, 
			String nooftenant, String fellowtenant, String remarks, String photo,  String ref, String ot, String ou){
		
		this.occpID = new SimpleStringProperty(id);
		this.tenantID = new SimpleStringProperty(tenant);
		this.unitID = new SimpleStringProperty(unit);
		this.occpDelivery = new SimpleStringProperty(delivery);
		this.occpStart = new SimpleStringProperty(start);
		this.occpEnd = new SimpleStringProperty(end);
		this.occpBilling = new SimpleStringProperty(billing);
		this.occpRent = new SimpleStringProperty(rent);
		this.occpStatus = new SimpleStringProperty(status);
		this.occpDeposit = new SimpleStringProperty(deposit);
		this.occpRemDeposit = new SimpleStringProperty(remdeposit);
		this.occpNoTenant = new SimpleStringProperty(nooftenant);
		this.occpFellowTenant = new SimpleStringProperty(fellowtenant);
		this.occpRemarks = new SimpleStringProperty(remarks);
		this.occpPhoto = new SimpleStringProperty(photo);;
		this.refid = new SimpleStringProperty(ref);
		this.octenant = new SimpleStringProperty(ot);
		this.ocunit = new SimpleStringProperty(ou);
		
	}
	
	public String getOccpID() {
		return occpID.get();
	}
	public String getTenantID() {
		return tenantID.get();
	}
	public String getUnitID() {
		return unitID.get();
	}
	public String getOccpDelivery() {
		return occpDelivery.get();
	}
	public String getOccpStart() {
		return occpStart.get();
	}
	public String getOccpEnd() {
		return occpEnd.get();
	}
	public String getOccpBilling() {
		return occpBilling.get();
	}
	public String getOccpRent() {
		return occpRent.get();
	}
	public String getOccpStatus() {
		return occpStatus.get();
	}
	public String getOccpDeposit() {
		return occpDeposit.get();
	}
	public String getOccpRemDeposit() {
		return occpRemDeposit.get();
	}
	public String getOcpNoTenant() {
		return occpNoTenant.get();
	}
	public String getOccpFellowTenant() {
		return occpFellowTenant.get();
	}
	public String getOccpRemarks() {
		return occpRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}
	public String getOctenant() {
		return octenant.get();
	}
	public String getOcunit() {
		return ocunit.get();
	}

    public String getOccpPhoto() {
        return occpPhoto.get();
    }

	public void setOccpID (String id){
		
		occpID.set(id);
	}
	public void setTenantID (String tenant){
		tenantID.set(tenant);
	}

	public void setUnitID (String unit){
		unitID.set(unit);
	}
	
	public void setOccpDelivery (String delivery){
		occpDelivery.set(delivery);
	}

	public void setOccpStart (String start){
		occpStart.set(start);
	}
	
	public void setOccpEnd (String end){
		occpEnd.set(end);
	}

	public void setOccpBilling (String billing){
		occpBilling.set(billing);
	}

	public void setOccpRent (String rent){
		occpRent.set(rent);
	}

	public void setOccpStatus (String status){
		occpStatus.set(status);
	}

	public void setOccpDeposit (String deposit){
		occpDeposit.set(deposit);
	}

	public void setOccpRemDeposit (String remdeposit){
		occpRemDeposit.set(remdeposit);
	}
	
	public void setOccpNoTenant (String nooftenant){
		occpNoTenant.set(nooftenant);
	}

	public void setOccpFellowTenant (String fellowtenant){
		occpFellowTenant.set(fellowtenant);
	}

	public void setOccpRemarks (String remarks){
		occpRemarks.set(remarks);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

	public void setOctenant (String ot){
		octenant.set(ot);
	}

	public void setOcunit (String ou){
		ocunit.set(ou);
	}

 
    public void setOccpPhoto (String photo) {
        occpPhoto.set(photo);
    }
 
}
