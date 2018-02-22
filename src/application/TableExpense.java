package application;

import javafx.beans.property.SimpleStringProperty;

public class TableExpense {
	
	private final SimpleStringProperty expID;
	private final SimpleStringProperty expRef;
	private final SimpleStringProperty expContractor;
	private final SimpleStringProperty expDate;
	private final SimpleStringProperty expCOA;
	private final SimpleStringProperty expDesc;
	private final SimpleStringProperty expAmount;
	private final SimpleStringProperty refid;
	
	public TableExpense (String id, String refer, String cont, String date, String coa, String desc,
			 String amount,  String ref) {
		
		this.expID = new SimpleStringProperty(id);
		this.expRef = new SimpleStringProperty(refer);
		this.expContractor = new SimpleStringProperty(cont);
		this.expDate = new SimpleStringProperty(date);
		this.expCOA = new SimpleStringProperty(coa);
		this.expDesc = new SimpleStringProperty(desc);
		this.expAmount = new SimpleStringProperty(amount);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getExpID() {
		return expID.get();
	}
	public String getExpRef() {
		return expRef.get();
	}
	public String getExpContractor() {
		return expContractor.get();
	}
	public String getExpDate() {
		return expDate.get();
	}
	public String getExpCOA() {
		return expCOA.get();
	}
	public String getExpDesc() {
		return expDesc.get();
	}
	public String getExpAmount() {
		return expAmount.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setExpID (String id){
		expID.set(id);
	}
	public void setExpRef (String refer){
		expRef.set(refer);
	}
	public void setExpContractor (String cont) {
		expContractor.set(cont);
	}

	public void setExpDate (String date){
		expDate.set(date);
	}
	
	public void setExpCOA (String coa){
		expCOA.set(coa);
	}

	public void setExpDesc (String desc){
		expDesc.set(desc);
	}
	
	public void setExpAmount (String amount){
		expAmount.set(amount);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

	
}
