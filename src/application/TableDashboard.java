package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class TableDashboard {
	
	private final SimpleStringProperty bldgid;
	private final SimpleStringProperty bldgname;
	private final SimpleStringProperty bldgaddress;
	private final SimpleStringProperty bldgdesc;
	ImageView bldgImage;
	private final SimpleStringProperty refid;
	
	public TableDashboard (String id, String name, String add, String desc, ImageView img, String ref )
	{
		
		this.bldgid = new SimpleStringProperty(id);
		this.bldgname = new SimpleStringProperty(name);
		this.bldgaddress = new SimpleStringProperty(add);
		this.bldgdesc = new SimpleStringProperty(desc);
		this.bldgImage = img;
		this.refid = new SimpleStringProperty(ref);
		
	}
	
	public String getBldgid() {
		return bldgid.get();
	}
	public String getBldgname() {
		return bldgname.get();
	}
	public String getBldgaddress() {
		return bldgaddress.get();
	}
	public String getBldgdesc() {
		return bldgdesc.get();
	}
	public String getRefid() {
		return refid.get();
	}

	public void setBldgid (String id){		
		bldgid.set(id);
	}
	public void setBldgname (String name){
		bldgname.set(name);
	}
	public void setBldgadd (String add){
		bldgaddress.set(add);
	}	
	public void setBldgdesc (String desc){
		bldgdesc.set(desc);
	}
	public void setRefid (String ref){
		refid.set(ref);
	}
	
	public ImageView getBldgImage() {
	    return bldgImage;
	}

	public void setBldgImage(ImageView img) {
	    this.bldgImage = img;
	}


}
