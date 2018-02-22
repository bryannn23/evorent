package application;

import javafx.beans.property.SimpleStringProperty;

public class TableUserTrackLogs {
	
	private final SimpleStringProperty userid;
	private final SimpleStringProperty username;
	private final SimpleStringProperty userdate;
	
	public TableUserTrackLogs (String id, String name, String date) {
		
		this.userid = new SimpleStringProperty(id);
		this.username = new SimpleStringProperty(name);
		this.userdate = new SimpleStringProperty(date);
		
	}
	
	public String getUserid() {
		return userid.get();
	}
	public String getUsername() {
		return username.get();
	}
	public String getUserdate() {
		return userdate.get();
	}

	public void setUserid (String id){
		
		userid.set(id);
	}
	public void setUsername (String name){
		username.set(name);
	}

	public void serUserdate (String date){
		userdate.set(date);
	}
	
}
