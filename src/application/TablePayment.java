package application;

import javafx.beans.property.SimpleStringProperty;

public class TablePayment {
	
	private final SimpleStringProperty payID;
	private final SimpleStringProperty payParti;
	private final SimpleStringProperty payAmount;
	
	public TablePayment (String id, String parti, String amount ){
		
		this.payID = new SimpleStringProperty(id);
		this.payParti = new SimpleStringProperty(parti);
		this.payAmount = new SimpleStringProperty(amount);
		
	}
	
	public String getPayID() {
		return payID.get();
	}
	public String getPayParti() {
		return payParti.get();
	}
	
	public String getPayAmount() {
		return payAmount.get();
	}

	public void setPayID (String id){
		
		payID.set(id);
	}
	public void setPayParti (String parti){
		payParti.set(parti);
	}

	
	public void setPayAmount (String amount){
		payAmount.set(amount);
	}

}
