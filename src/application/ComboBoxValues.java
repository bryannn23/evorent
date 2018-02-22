package application;

import javafx.scene.control.ComboBox;

public class ComboBoxValues {

	public void bldgIssue (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Reported", "For Scheduling", "On-going Maintenance", "On-hold", "Cancelled", "Completed", "Others" );
		
	}
	
	public void condition (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("NEW", "WELL MAINTAINED", "NEEDS RENOVATION", "UNDER RENOVATION", "RENOVATED", "RETIRED", "OTHERS");
	}
	
	public void bldgMxActivity (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Others Maintenance", "Building Interior Inspection", "Building Exterior Inspection", 
				"General Unit Inspection", "Sidings (Wash , Re-paint, Waterproofing)", "Windows (Wash, Re-caulk)", 
				"Doors (Wash, Re-paint)", "Decks and Stairs (Clean, Paint)", "Plumbing", "Electrical (Maintenance)",
				"Pest Control", "Others Repairs and Fixtures", "Interior Makeover", "Signage (Clean, Repair)" ,
				"Lighting (Clean, Change)", "Roof (Clean, Waterproofing, Leaks, Repairs)",  
				"Foundation (Repair)", "Gutter (Clean, Repair, Re-paint)" );
	}
	
	public void bldgMXStatus (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Scheduled for Maintenance", "Waiting for Contractor", "On-Going Maintenance", "Still/Terminated Maintenance", 
				"Maintenance Completed" );
	}
	
	public void unitLocation (ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("Not Applicable", "North", "South", "East", "West", "North East", "North West", "South East", "South West" );
	}
	
	public void unitFeatures (ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("Bare", "Partially Furnished", "Furnished", "Fully Furnished" );
	}
	
	public void unitIssue (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("For Scheduling", "On-going Maintenance", "On-hold", "Cancelled", "Completed", "Others" );
		
	}
	public void unitMxActivity (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Others Maintenance", "Unit Interior Inspection", "Unit Exterior Inspection", 
				"Walls (Wash , Re-paint)", "Windows (Wash, Re-caulk)", "Doors (Wash, Re-paint)", 
				"Plumbing (Maintenance)", "Electrical (Maintenance)", "Pest Control",
				"Others Repairs and Fixtures", "Interior Makeover", "Lighting (Clean, Change)", 
				"Foundation (Repair)",  "Furniture (Repair, Change)", "Appliances (Repair, Change)"  );
	}
	
	public void unitMXStatus (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Scheduled for Maintenance", "Waiting for Contractor", "Undergoing Maintenance", 
				"Still/Terminated Maintenance", "Maintenance Completed" );
	}

	public void unitRequest (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Admin Initiative", "Tenant Request" );
	}
	
	public void occpStatus (ComboBox<String> cmb) {
		
		cmb.getItems().addAll("New Tenant", "Unit Transfer", "Building Transfer", "End of Contract", "Renewed Contract", 
				"Others" );
	}
	
	public void statementType ( ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("Rent/Lease", "Association", "Electricity", "Water", "Repairs", "Others" );
	}
	
	public void monthName ( ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", 
				"October", "November", "December" );
	}
	
	public void statementStatus ( ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("For Printing", "For Distribution", "For Re-printing", "Delivered", "Paid", "Overdue" );
	} 
	
	public void paymentVIA ( ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("Post Dated Check", "Cash", "Bank Deposit", "Others" );
	}
	
	public void paymentReceipt ( ComboBox<String> cmb ) {
		
		cmb.getItems().addAll("For Printing", "For Delivery", "Issued", "Others" );
	}
	
	public void expCOA ( ComboBox<String> cmb) {
		
		cmb.getItems().addAll("Other Cost", "Management Fees", "Agent Rent Collection Fees", "Insurance", "Cost of Advertising", 
				"Cost of Utilities", "Provision for Depreciation", "Cost of Maintenance", "Cost of Repairs and Replacement");
	}
	
	
}
