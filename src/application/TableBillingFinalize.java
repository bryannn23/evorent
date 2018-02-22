package application;

import javafx.beans.property.SimpleStringProperty;

public class TableBillingFinalize {
	
	private final SimpleStringProperty stateID;
	private final SimpleStringProperty stateType;
	private final SimpleStringProperty stateMonth;
	private final SimpleStringProperty stateAmount;
	
	public TableBillingFinalize (String id, String type, String month, String amount ){
		
		this.stateID = new SimpleStringProperty(id);
		this.stateType = new SimpleStringProperty(type);
		this.stateMonth = new SimpleStringProperty(month);
		this.stateAmount = new SimpleStringProperty(amount);
		
	}
	
	public String getStateID() {
		return stateID.get();
	}
	public String getStateType() {
		return stateType.get();
	}
	public String getStateMonth() {
		return stateMonth.get();
	}
	public String getStateAmount() {
		return stateAmount.get();
	}

	public void setStateID (String id){
		
		stateID.set(id);
	}
	public void setStateType (String type){
		stateType.set(type);
	}

	public void setStateMonth (String month){
		stateMonth.set(month);
	}
	
	public void setStateAmount (String amount){
		stateAmount.set(amount);
	}

}
