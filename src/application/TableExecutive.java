package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableExecutive {
	
	private final SimpleStringProperty execID;
	private final SimpleStringProperty execName;
	private final SimpleStringProperty execMobileNo;
	private final SimpleStringProperty execBday;
	private final SimpleStringProperty execEadd;
	private final SimpleStringProperty execAccount;
	private final SimpleStringProperty execRemarks;
	ImageView execPhoto;
	private final SimpleStringProperty refid;
	
	public TableExecutive (String id, String name, String mobile, String bday, String eadd,
			String account, String rem, ImageView img, String ref) {
		
		this.execID = new SimpleStringProperty(id);
		this.execName = new SimpleStringProperty(name);
		this.execMobileNo = new SimpleStringProperty(mobile);
		this.execBday = new SimpleStringProperty(bday);
		this.execEadd = new SimpleStringProperty(eadd);
		this.execAccount = new SimpleStringProperty(account);
		this.execRemarks = new SimpleStringProperty(rem);
		this.execPhoto = img;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getExecID() {
		return execID.get();
	}
	public String getExecName() {
		return execName.get();
	}
	public String getExecMobileNo() {
		return execMobileNo.get();
	}
	public String getExecBday() {
		return execBday.get();
	}
	public String getExecEadd() {
		return execEadd.get();
	}
	public String getExecAccount() {
		return execAccount.get();
	}
	public String getExecRemarks() {
		return execRemarks.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setExecID (String id){
		
		execID.set(id);
	}
	public void setExecName (String name){
		execName.set(name);
	}

	public void setExecMobile (String mobile){
		execMobileNo.set(mobile);
	}
	
	public void setExecBday (String bday){
		execBday.set(bday);
	}

	public void setExecEadd (String eadd){
		execEadd.set(eadd);
	}
	
	public void setExecAccount (String account){
		execAccount.set(account);
	}

	public void setExecRemarks (String rem){
		execRemarks.set(rem);
	}

	public void setRefid (String ref){
		refid.set(ref);
	}

    public ImageView getExecPhoto () {
        return execPhoto;
    }

    public void setExecPhoto (ImageView photo) {
        this.execPhoto = photo;
    }

	
}
