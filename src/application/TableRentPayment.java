package application;

import javafx.beans.property.SimpleStringProperty;

public class TableRentPayment {
	
	private final SimpleStringProperty payID;
	private final SimpleStringProperty payNo;
	private final SimpleStringProperty occpID;
	private final SimpleStringProperty stateID;
	private final SimpleStringProperty payDate;
	private final SimpleStringProperty payAmount;
	private final SimpleStringProperty payVIA;
	private final SimpleStringProperty payReceipt;
	private final SimpleStringProperty refid;
	private final SimpleStringProperty occupancy;
	
	public TableRentPayment (String id, String no, String occp, String state, String date, String amount, 
			String via, String receipt,  String ref, String occupant ){
		
		this.payID = new SimpleStringProperty(id);
		this.payNo = new SimpleStringProperty(no);
		this.occpID = new SimpleStringProperty(occp);
		this.stateID = new SimpleStringProperty(state);
		this.payDate = new SimpleStringProperty(date);
		this.payAmount = new SimpleStringProperty(amount);
		this.payVIA = new SimpleStringProperty(via);
		this.payReceipt = new SimpleStringProperty(receipt);
		this.refid = new SimpleStringProperty(ref);
		this.occupancy = new SimpleStringProperty(occupant);
		
	}
	
	public String getPayID() {
		return payID.get();
	}
	public String getPayNo() {
		return payNo.get();
	}
	public String getOccpID() {
		return occpID.get();
	}
	public String getStateID() {
		return stateID.get();
	}
	public String getPayDate() {
		return payDate.get();
	}
	public String getPayAmount() {
		return payAmount.get();
	}
	public String getPayVIA() {
		return payVIA.get();
	}
	public String getPayReceipt() {
		return payReceipt.get();
	}
	public String getRefid() {
		return refid.get();
	}
	public String getOccupancy() {
		return occupancy.get();
	}

	public void setPayID (String id){
		
		payID.set(id);
	}
	public void setPayNo (String no){
		
		payNo.set(no);
	}
	public void setOccpID (String occp){
		occpID.set(occp);
	}

	public void setStateID (String state){
		stateID.set(state);
	}
	
	public void setPayDate (String date){
		payDate.set(date);
	}

	public void setPayAmount (String amount){
		payAmount.set(amount);
	}
	
	public void setPayVIA (String via){
		payVIA.set(via);
	}

	public void setPayReceipt (String receipt){
		payReceipt.set(receipt);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

	public void setOccupancy (String occupant){
		occupancy.set(occupant);
	}

}
