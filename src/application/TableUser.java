package application;

import javafx.beans.property.SimpleStringProperty;

public class TableUser {
	
	private final SimpleStringProperty userid;
	private final SimpleStringProperty username;
	private final SimpleStringProperty password;
	private final SimpleStringProperty pincode;
	private final SimpleStringProperty empname;
	private final SimpleStringProperty access;
	private final SimpleStringProperty status;
	private final SimpleStringProperty refid;
	
	public TableUser (String id, String user, String pass, String ename, String uaccess, 
			String ustatus, String pin, String ref){
		
		this.userid = new SimpleStringProperty(id);
		this.username = new SimpleStringProperty(user);
		this.password = new SimpleStringProperty(pass);
		this.empname = new SimpleStringProperty(ename);
		this.access = new SimpleStringProperty(uaccess);
		this.status = new SimpleStringProperty(ustatus);
		this.pincode = new SimpleStringProperty(pin);
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getUserid() {
		return userid.get();
	}
	public String getUsername() {
		return username.get();
	}
	public String getPassword() {
		return password.get();
	}
	public String getEmpname() {
		return empname.get();
	}
	public String getAccess() {
		return access.get();
	}
	public String getStatus() {
		return status.get();
	}
	public String getPincode() {
		return pincode.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setUserid (String id){
		
		userid.set(id);
	}
	public void setUsername (String user){
		username.set(user);
	}

	public void setPassword (String pass){
		password.set(pass);
	}
	
	public void setEmpname (String ename){
		empname.set(ename);
	}

	public void setAccess (String uaccess){
		access.set(uaccess);
	}
	
	public void setStatus (String ustatus){
		status.set(ustatus);
	}

	public void setPincode (String pin){
		pincode.set(pin);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

}
