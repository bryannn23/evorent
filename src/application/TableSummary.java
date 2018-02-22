package application;

import javafx.beans.property.SimpleStringProperty;

public class TableSummary {
	
	private final SimpleStringProperty expMonth;
	private final SimpleStringProperty expTotal;
	
	public TableSummary (String month, String total ) {
		
		this.expMonth = new SimpleStringProperty(month);
		this.expTotal = new SimpleStringProperty(total);
		
	}
	
	public String getExpMonth() {
		return expMonth.get();
	}
	public String getExpTotal() {
		return expTotal.get();
	}

	public void setExpMonth (String month){
		expMonth.set(month);
	}
	public void setExpTotal (String total){
		expTotal.set(total);
	}
	
}
