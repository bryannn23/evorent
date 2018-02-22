package application;

import javafx.beans.property.SimpleStringProperty;

public class TableBilling {
	
	private final SimpleStringProperty stateID;
	private final SimpleStringProperty stateDetail;
	private final SimpleStringProperty stateBal;
	private final SimpleStringProperty stateAmount;
	
	public TableBilling (String id, String det, String bal, String amount ){
		
		this.stateID = new SimpleStringProperty(id);
		this.stateDetail = new SimpleStringProperty(det);
		this.stateBal = new SimpleStringProperty(bal);
		this.stateAmount = new SimpleStringProperty(amount);
		
	}
	
	public String getStateID() {
		return stateID.get();
	}
	public String getStateDetail() {
		return stateDetail.get();
	}
	public String getStateBal() {
		return stateBal.get();
	}
	public String getStateAmount() {
		return stateAmount.get();
	}

	public void setStateID (String id){
		
		stateID.set(id);
	}
	public void setStateDetail (String det){
		stateDetail.set(det);
	}

	public void setStateBal (String bal){
		stateBal.set(bal);
	}
	
	public void setStateAmount (String amount){
		stateAmount.set(amount);
	}

}
