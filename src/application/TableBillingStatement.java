package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;

public class TableBillingStatement {
	
	private final SimpleStringProperty stateID;
	private final SimpleStringProperty stateNo;
	private final SimpleStringProperty occpID;
	private final SimpleStringProperty stateType;
	private final SimpleStringProperty stateDate;
	private final SimpleStringProperty stateMonth;
	private final SimpleStringProperty stateDueDate;
	private final SimpleStringProperty stateAmount;
	private final SimpleStringProperty stateStatus;
	private final SimpleStringProperty refid;
	private final BooleanProperty chkselect;
	private final SimpleStringProperty stateOccp;
	
	public TableBillingStatement (String id, String no, String occp, String type, String date, 
			String month, String duedate, String amount, String status,  String ref, String sOccp ){
		
		this.stateID = new SimpleStringProperty(id);
		this.stateNo = new SimpleStringProperty(no);
		this.occpID = new SimpleStringProperty(occp);
		this.stateType = new SimpleStringProperty(type);
		this.stateDate = new SimpleStringProperty(date);
		this.stateMonth = new SimpleStringProperty(month);
		this.stateDueDate = new SimpleStringProperty(duedate);
		this.stateAmount = new SimpleStringProperty(amount);
		this.stateStatus = new SimpleStringProperty(status);
		this.refid = new SimpleStringProperty(ref);
        this.chkselect = new SimpleBooleanProperty (false);
		this.stateOccp = new SimpleStringProperty(sOccp);
		
	}
	
	public String getStateID() {
		return stateID.get();
	}
	public String getStateNo() {
		return stateNo.get();
	}
	public String getOccpID() {
		return occpID.get();
	}
	public String getStateType() {
		return stateType.get();
	}
	public String getStateDate() {
		return stateDate.get();
	}
	public String getStateMonth() {
		return stateMonth.get();
	}
	public String getStateDueDate() {
		return stateDueDate.get();
	}
	public String getStateAmount() {
		return stateAmount.get();
	}
	public String getStateStatus() {
		return stateStatus.get();
	}
	public String getRefid() {
		return refid.get();
	}
	public String getStateOccp() {
		return stateOccp.get();
	}

	public void setStateID (String id){
		
		stateID.set(id);
	}
	public void setStateNo (String no){
		stateNo.set(no);
	}

	public void setOccpID (String occp){
		occpID.set(occp);
	}
	
	public void setStateType (String type){
		stateType.set(type);
	}

	public void setStateDate (String date){
		stateDate.set(date);
	}
	
	public void setStateMonth (String month){
		stateMonth.set(month);
	}

	public void setStateDueDate (String duedate){
		stateDueDate.set(duedate);
	}

	public void setStateAmount (String amount){
		stateAmount.set(amount);
	}

	public void setStateStatus (String status){
		stateStatus.set(status);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

	public void setStateOccp (String sOccp) {
		stateOccp.set(sOccp);
	}

    public ObservableBooleanValue getChkselectProperty () {
        return chkselect;
    }

    public void setChkselect(Boolean checked) {
        this.chkselect.set(checked);
    }

}
