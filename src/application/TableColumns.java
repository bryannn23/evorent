package application;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableColumns {

	public void TableAdminDetails(TableView<TableAdminDetails> tbl) {

		TableColumn<TableAdminDetails, String> tcadminID = new TableColumn<>(" ID");
		tcadminID.setCellValueFactory(new PropertyValueFactory<>("adminID"));

		TableColumn<TableAdminDetails, String> tcadminName = new TableColumn<>("NAME");
		tcadminName.setCellValueFactory(new PropertyValueFactory<>("adminName"));

		TableColumn<TableAdminDetails, String> tcadminMobile = new TableColumn<>("MOBILE NUMBER");
		tcadminMobile.setCellValueFactory(new PropertyValueFactory<>("adminMobile"));

		TableColumn<TableAdminDetails, String> tcadminBday = new TableColumn<>("BIRTHDAY");
		tcadminBday.setCellValueFactory(new PropertyValueFactory<>("adminBday"));

		TableColumn<TableAdminDetails, String> tcadminEadd = new TableColumn<>("EMAIL ADDRESS");
		tcadminEadd.setCellValueFactory(new PropertyValueFactory<>("adminEadd"));

		TableColumn<TableAdminDetails, String> tcadminAccount = new TableColumn<>("ACCOUNT");
		tcadminAccount.setCellValueFactory(new PropertyValueFactory<>("adminAccount"));

		TableColumn<TableAdminDetails, String> tcadminRemarks = new TableColumn<>("REMARKS");
		tcadminRemarks.setCellValueFactory(new PropertyValueFactory<>("adminRemarks"));

		TableColumn<TableAdminDetails, String> tcadminPhoto = new TableColumn<>("PHOTO");
		tcadminPhoto.setCellValueFactory(new PropertyValueFactory<>("adminPhoto"));

		tbl.getColumns().addAll(tcadminPhoto, tcadminMobile, tcadminBday, tcadminAccount, tcadminRemarks );

	}
	
	public void TableAdministrator(TableView<TableAdministrator> tbl) {

		TableColumn<TableAdministrator, String> tcadminid = new TableColumn<>("ID");
		tcadminid.setCellValueFactory(new PropertyValueFactory<>("adminid"));

		TableColumn<TableAdministrator, String> tcadminname = new TableColumn<>("NAME");
		tcadminname.setCellValueFactory(new PropertyValueFactory<>("adminname"));

		TableColumn<TableAdministrator, String> tcadminaddress = new TableColumn<>("ADDRESS");
		tcadminaddress.setCellValueFactory(new PropertyValueFactory<>("adminaddress"));

		TableColumn<TableAdministrator, String> tcadmincomid = new TableColumn<>("COMPANY ID");
		tcadmincomid.setCellValueFactory(new PropertyValueFactory<>("admincomid"));

		TableColumn<TableAdministrator, String> tcadminemailadd = new TableColumn<>("EMAIL ADDRESS");
		tcadminemailadd.setCellValueFactory(new PropertyValueFactory<>("adminemailadd"));

		TableColumn<TableAdministrator, String> tcadmincontact = new TableColumn<>("CONTACT INFO");
		tcadmincontact.setCellValueFactory(new PropertyValueFactory<>("admincontact"));

		TableColumn<TableAdministrator, String> tcadminbuilding = new TableColumn<>("BUILDING");
		tcadminbuilding.setCellValueFactory(new PropertyValueFactory<>("adminbuilding"));

		tbl.getColumns().addAll(tcadminname, tcadminaddress, tcadmincomid, tcadminemailadd, tcadmincontact, tcadminbuilding );

	}

	public void TableAdminMngmt(TableView<TableAdminManagement> tbl) {

		TableColumn<TableAdminManagement, String> tcmngmtID = new TableColumn<>(" ID");
		tcmngmtID.setCellValueFactory(new PropertyValueFactory<>("mngmtID"));

		TableColumn<TableAdminManagement, String> tcadminName = new TableColumn<>("ADMINISTRATOR");
		tcadminName.setCellValueFactory(new PropertyValueFactory<>("adminName"));

		TableColumn<TableAdminManagement, String> tcbldgName = new TableColumn<>("BUILDING");
		tcbldgName.setCellValueFactory(new PropertyValueFactory<>("bldgName"));

		TableColumn<TableAdminManagement, String> tcmngmtStatus = new TableColumn<>("STATUS");
		tcmngmtStatus.setCellValueFactory(new PropertyValueFactory<>("mngmtStatus"));

		TableColumn<TableAdminManagement, String> tcmngmtRemarks = new TableColumn<>("REMARKS");
		tcmngmtRemarks.setCellValueFactory(new PropertyValueFactory<>("mngmtRemarks"));

		tbl.getColumns().addAll(tcadminName, tcbldgName, tcmngmtStatus, tcmngmtRemarks );

	}

	public void TableBilling (TableView<TableBilling> tbl) {

		TableColumn<TableBilling, String> tcstateID = new TableColumn<>(" ID");
		tcstateID.setCellValueFactory(new PropertyValueFactory<>("stateID"));

		TableColumn<TableBilling, String> tcstateDetail = new TableColumn<>("BILL DETAILS");
		tcstateDetail.setCellValueFactory(new PropertyValueFactory<>("stateDetail"));

		TableColumn<TableBilling, String> tcstateBal = new TableColumn<>("BALANCE");
		tcstateBal.setCellValueFactory(new PropertyValueFactory<>("stateBal"));

		TableColumn<TableBilling, String> tcstateAmount = new TableColumn<>("AMOUNT");
		tcstateAmount.setCellValueFactory(new PropertyValueFactory<>("stateAmount"));

		tbl.getColumns().addAll(tcstateDetail, tcstateBal, tcstateAmount );

	}

	public void TableBillingRent (TableView<TableBilling> tbl) {

		TableColumn<TableBilling, String> tcstateID = new TableColumn<>(" ID");
		tcstateID.setCellValueFactory(new PropertyValueFactory<>("stateID"));

		TableColumn<TableBilling, String> tcstateDetail = new TableColumn<>("BILL DETAILS");
		tcstateDetail.setCellValueFactory(new PropertyValueFactory<>("stateDetail"));

		TableColumn<TableBilling, String> tcstateBal = new TableColumn<>("BALANCE");
		tcstateBal.setCellValueFactory(new PropertyValueFactory<>("stateBal"));

		TableColumn<TableBilling, String> tcstateAmount = new TableColumn<>("AMOUNT");
		tcstateAmount.setCellValueFactory(new PropertyValueFactory<>("stateAmount"));

		tbl.getColumns().addAll(tcstateDetail, tcstateAmount );

	}

	public void TableBillingFinalize (TableView<TableBillingFinalize> tbl) {

		TableColumn<TableBillingFinalize, String> tcstateID = new TableColumn<>(" ID");
		tcstateID.setCellValueFactory(new PropertyValueFactory<>("stateID"));

		TableColumn<TableBillingFinalize, String> tcstateType = new TableColumn<>("BILL DETAILS");
		tcstateType.setCellValueFactory(new PropertyValueFactory<>("stateType"));

		TableColumn<TableBillingFinalize, String> tcstateMonth = new TableColumn<>("MONTH");
		tcstateMonth.setCellValueFactory(new PropertyValueFactory<>("stateMonth"));

		TableColumn<TableBillingFinalize, String> tcstateAmount = new TableColumn<>("AMOUNT");
		tcstateAmount.setCellValueFactory(new PropertyValueFactory<>("stateAmount"));

		tbl.getColumns().addAll(tcstateType, tcstateMonth, tcstateAmount );

	}

	public void TableBillingStatement(TableView<TableBillingStatement> tbl) {

		TableColumn<TableBillingStatement, String> tcstateID = new TableColumn<>(" ID");
		tcstateID.setCellValueFactory(new PropertyValueFactory<>("stateID"));

		TableColumn<TableBillingStatement, String> tcstateNo = new TableColumn<>("STATEMENT NUMBER");
		tcstateNo.setCellValueFactory(new PropertyValueFactory<>("stateNo"));

		TableColumn<TableBillingStatement, String> tcoccpID = new TableColumn<>("OCCUPANCY");
		tcoccpID.setCellValueFactory(new PropertyValueFactory<>("occpID"));

		TableColumn<TableBillingStatement, String> tcstateType = new TableColumn<>("TYPE");
		tcstateType.setCellValueFactory(new PropertyValueFactory<>("stateType"));

		TableColumn<TableBillingStatement, String> tcstateDate = new TableColumn<>("DATE");
		tcstateDate.setCellValueFactory(new PropertyValueFactory<>("stateDate"));

		TableColumn<TableBillingStatement, String> tcstateMonth = new TableColumn<>("MONTH");
		tcstateMonth.setCellValueFactory(new PropertyValueFactory<>("stateMonth"));

		TableColumn<TableBillingStatement, String> tcstateDueDate = new TableColumn<>("DUE DATE");
		tcstateDueDate.setCellValueFactory(new PropertyValueFactory<>("stateDueDate"));

		TableColumn<TableBillingStatement, String> tcstateAmount = new TableColumn<>("AMOUNT");
		tcstateAmount.setCellValueFactory(new PropertyValueFactory<>("stateAmount"));

		TableColumn<TableBillingStatement, String> tcstateStatus = new TableColumn<>("STATUS");
		tcstateStatus.setCellValueFactory(new PropertyValueFactory<>("stateStatus"));

		tbl.getColumns().addAll(tcstateNo, tcoccpID, tcstateType, tcstateDate, tcstateMonth, tcstateDueDate, tcstateAmount, tcstateStatus );

	}

	public void TableBuildingDetail(TableView<TableBuildingDetail> tbl) {

		TableColumn<TableBuildingDetail, String> tcbldgID = new TableColumn<>("ID");
		tcbldgID.setCellValueFactory(new PropertyValueFactory<>("bldgID"));

		TableColumn<TableBuildingDetail, String> tcbldgName = new TableColumn<>("NAME");
		tcbldgName.setCellValueFactory(new PropertyValueFactory<>("bldgName"));

		TableColumn<TableBuildingDetail, String> tcbldgAddress = new TableColumn<>("ADDRESS");
		tcbldgAddress.setCellValueFactory(new PropertyValueFactory<>("bldgAddress"));

		TableColumn<TableBuildingDetail, String> tcbldgContact = new TableColumn<>("CONTACT INFO");
		tcbldgContact.setCellValueFactory(new PropertyValueFactory<>("bldgContact"));

		TableColumn<TableBuildingDetail, String> bldgDateEst = new TableColumn<>("DATE ESTABLISHED");
		bldgDateEst.setCellValueFactory(new PropertyValueFactory<>("bldgDateEst"));

		TableColumn<TableBuildingDetail, String> tcbldgFloors = new TableColumn<>("FLOORS");
		tcbldgFloors.setCellValueFactory(new PropertyValueFactory<>("bldgFloors"));

		TableColumn<TableBuildingDetail, String> tcbldgFeature = new TableColumn<>("FEATURE");
		tcbldgFeature.setCellValueFactory(new PropertyValueFactory<>("bldgFeature"));

		TableColumn<TableBuildingDetail, String> tcbldgCondition = new TableColumn<>("CONDITION");
		tcbldgCondition.setCellValueFactory(new PropertyValueFactory<>("bldgCondition"));

		TableColumn<TableBuildingDetail, String> tcbldgRemarks = new TableColumn<>("REMARKS");
		tcbldgRemarks.setCellValueFactory(new PropertyValueFactory<>("bldgRemarks"));

		TableColumn<TableBuildingDetail, String> tcbldgPhoto1 = new TableColumn<>("PHOTO1");
		tcbldgPhoto1.setCellValueFactory(new PropertyValueFactory<>("bldgPhoto1"));

		TableColumn<TableBuildingDetail, String> tcbldgPhoto2 = new TableColumn<>("PHOTO2");
		tcbldgPhoto2.setCellValueFactory(new PropertyValueFactory<>("bldgPhoto2"));

		TableColumn<TableBuildingDetail, String> tcbldgPhoto3 = new TableColumn<>("PHOTO3");
		tcbldgPhoto3.setCellValueFactory(new PropertyValueFactory<>("bldgPhoto3"));

		TableColumn<TableBuildingDetail, String> tcbldgPhoto4 = new TableColumn<>("PHOTO4");
		tcbldgPhoto4.setCellValueFactory(new PropertyValueFactory<>("bldgPhoto4"));

		tbl.getColumns().addAll(tcbldgName, tcbldgAddress, tcbldgContact, bldgDateEst, tcbldgFloors, 
				tcbldgFeature, tcbldgCondition, tcbldgRemarks, tcbldgPhoto1, tcbldgPhoto2, tcbldgPhoto3, 
				tcbldgPhoto4 );

	}

	public void TableBuildingIssue(TableView<TableBuildingIssue> tbl) {

		TableColumn<TableBuildingIssue, String> tcbldgIssue = new TableColumn<>(" ID");
		tcbldgIssue.setCellValueFactory(new PropertyValueFactory<>("bldgIssue"));

		TableColumn<TableBuildingIssue, String> tcbldgID = new TableColumn<>("BUILDING");
		tcbldgID.setCellValueFactory(new PropertyValueFactory<>("bldgID"));

		TableColumn<TableBuildingIssue, String> tcbldgInspection = new TableColumn<>("INSPECTION DATE");
		tcbldgInspection.setCellValueFactory(new PropertyValueFactory<>("bldgInspection"));

		TableColumn<TableBuildingIssue, String> tcissueName = new TableColumn<>("NAME");
		tcissueName.setCellValueFactory(new PropertyValueFactory<>("issueName"));

		TableColumn<TableBuildingIssue, String> tcissueDesc = new TableColumn<>("DESCRIPTION");
		tcissueDesc.setCellValueFactory(new PropertyValueFactory<>("issueDesc"));

		TableColumn<TableBuildingIssue, String> tcactPlan = new TableColumn<>("ACTIVITY PLAN");
		tcactPlan.setCellValueFactory(new PropertyValueFactory<>("actPlan"));

		TableColumn<TableBuildingIssue, String> tcissueRemark = new TableColumn<>("REMARKS");
		tcissueRemark.setCellValueFactory(new PropertyValueFactory<>("issueRemark"));

		TableColumn<TableBuildingIssue, String> tcissueStatus = new TableColumn<>("STATUS");
		tcissueStatus.setCellValueFactory(new PropertyValueFactory<>("issueStatus"));

		tbl.getColumns().addAll(tcbldgID, tcbldgInspection, tcissueName, tcissueDesc, tcactPlan, tcissueRemark, 
				tcissueStatus );

	}

	public void TableBuildingMaintenance(TableView<TableBuildingMaintenance> tbl) {

		TableColumn<TableBuildingMaintenance, String> tcmxID = new TableColumn<>(" ID");
		tcmxID.setCellValueFactory(new PropertyValueFactory<>("mxID"));

		TableColumn<TableBuildingMaintenance, String> tcbldgID = new TableColumn<>("BUILDING");
		tcbldgID.setCellValueFactory(new PropertyValueFactory<>("bldgID"));

		TableColumn<TableBuildingMaintenance, String> tcmxRef = new TableColumn<>("REFERENCE ID");
		tcmxRef.setCellValueFactory(new PropertyValueFactory<>("mxRef"));

		TableColumn<TableBuildingMaintenance, String> tcmxDate = new TableColumn<>("DATE");
		tcmxDate.setCellValueFactory(new PropertyValueFactory<>("mxDate"));

		TableColumn<TableBuildingMaintenance, String> tcmxActivity = new TableColumn<>("ACTIVITY");
		tcmxActivity.setCellValueFactory(new PropertyValueFactory<>("mxActivity"));

		TableColumn<TableBuildingMaintenance, String> tcmxDesc = new TableColumn<>("DESCRIPTION");
		tcmxDesc.setCellValueFactory(new PropertyValueFactory<>("mxDesc"));

		TableColumn<TableBuildingMaintenance, String> tcmxContractor = new TableColumn<>("CONTRACTOR");
		tcmxContractor.setCellValueFactory(new PropertyValueFactory<>("mxContractor"));

		TableColumn<TableBuildingMaintenance, String> tcmxStatus = new TableColumn<>("STATUS");
		tcmxStatus.setCellValueFactory(new PropertyValueFactory<>("mxStatus"));

		TableColumn<TableBuildingMaintenance, String> tcmxTotalCost = new TableColumn<>("TOTAL COST");
		tcmxTotalCost.setCellValueFactory(new PropertyValueFactory<>("mxTotalCost"));

		TableColumn<TableBuildingMaintenance, String> tcmxRemarks = new TableColumn<>("REMARKS");
		tcmxRemarks.setCellValueFactory(new PropertyValueFactory<>("mxRemarks"));

		tbl.getColumns().addAll(tcbldgID, tcmxRef, tcmxDate, tcmxActivity, tcmxDesc, tcmxContractor, tcmxStatus, 
				tcmxTotalCost, tcmxRemarks );

	}

	public void TableExecutive(TableView<TableExecutive> tbl) {

		TableColumn<TableExecutive, String> tcexecid = new TableColumn<>("ID");
		tcexecid.setCellValueFactory(new PropertyValueFactory<>("execID"));

		TableColumn<TableExecutive, String> tcexecname = new TableColumn<>("NAME");
		tcexecname.setCellValueFactory(new PropertyValueFactory<>("execName"));

		TableColumn<TableExecutive, String> tcexecaddress = new TableColumn<>("MOBILE NUMBER");
		tcexecaddress.setCellValueFactory(new PropertyValueFactory<>("execMobileNo"));

		TableColumn<TableExecutive, String> tcexeccomid = new TableColumn<>("BIRTHDAY");
		tcexeccomid.setCellValueFactory(new PropertyValueFactory<>("execBday"));

		TableColumn<TableExecutive, String> tcexecemailadd = new TableColumn<>("EMAIL ADDRESS");
		tcexecemailadd.setCellValueFactory(new PropertyValueFactory<>("execEadd"));

		TableColumn<TableExecutive, String> tcexeccontact = new TableColumn<>("ACCOUNT");
		tcexeccontact.setCellValueFactory(new PropertyValueFactory<>("execAccount"));

		TableColumn<TableExecutive, String> tcexecRemarks = new TableColumn<>("REMARKS");
		tcexecRemarks.setCellValueFactory(new PropertyValueFactory<>("execRemarks"));

		TableColumn<TableExecutive, String> tcexecPhoto = new TableColumn<>("PHOTO");
		tcexecPhoto.setCellValueFactory(new PropertyValueFactory<>("execPhoto"));
		
		tbl.getColumns().addAll(tcexecPhoto, tcexecaddress, tcexeccomid, tcexeccontact   );

	}

	public void TableOccupancyDetail(TableView<TableOccupancyDetail> tbl) {

		TableColumn<TableOccupancyDetail, String> tcoccpID = new TableColumn<>("ID");
		tcoccpID.setCellValueFactory(new PropertyValueFactory<>("occpID"));

		TableColumn<TableOccupancyDetail, String> tctenantID = new TableColumn<>("TENANT");
		tctenantID.setCellValueFactory(new PropertyValueFactory<>("tenantID"));

		TableColumn<TableOccupancyDetail, String> tcunitID = new TableColumn<>("UNIT");
		tcunitID.setCellValueFactory(new PropertyValueFactory<>("unitID"));

		TableColumn<TableOccupancyDetail, String> tcoccpDelivery = new TableColumn<>("DELIVERY");
		tcoccpDelivery.setCellValueFactory(new PropertyValueFactory<>("occpDelivery"));

		TableColumn<TableOccupancyDetail, String> tcoccpStart = new TableColumn<>("START");
		tcoccpStart.setCellValueFactory(new PropertyValueFactory<>("occpStart"));

		TableColumn<TableOccupancyDetail, String> tcoccpEnd = new TableColumn<>("END");
		tcoccpEnd.setCellValueFactory(new PropertyValueFactory<>("occpEnd"));

		TableColumn<TableOccupancyDetail, String> tcoccpBilling = new TableColumn<>("BILLING");
		tcoccpBilling.setCellValueFactory(new PropertyValueFactory<>("occpBilling"));

		TableColumn<TableOccupancyDetail, String> tcoccpRent = new TableColumn<>("RENT");
		tcoccpRent.setCellValueFactory(new PropertyValueFactory<>("occpRent"));

		TableColumn<TableOccupancyDetail, String> tcoccpStatus = new TableColumn<>("STATUS");
		tcoccpStatus.setCellValueFactory(new PropertyValueFactory<>("occpStatus"));

		TableColumn<TableOccupancyDetail, String> tcoccpDeposit = new TableColumn<>("DEPOSIT");
		tcoccpDeposit.setCellValueFactory(new PropertyValueFactory<>("occpDeposit"));

		TableColumn<TableOccupancyDetail, String> tcoccpRemDeposit = new TableColumn<>("REMAINING DEPOSIT");
		tcoccpRemDeposit.setCellValueFactory(new PropertyValueFactory<>("occpRemDeposit"));

		TableColumn<TableOccupancyDetail, String> tcoccpNoTenant = new TableColumn<>("NUMBER OF TENANT");
		tcoccpNoTenant.setCellValueFactory(new PropertyValueFactory<>("occpNoTenant"));

		TableColumn<TableOccupancyDetail, String> tcoccpFellowTenant = new TableColumn<>("FELLOW TENANT");
		tcoccpFellowTenant.setCellValueFactory(new PropertyValueFactory<>("occpFellowTenant"));

		TableColumn<TableOccupancyDetail, String> tcoccpRemarks = new TableColumn<>("REMARKS");
		tcoccpRemarks.setCellValueFactory(new PropertyValueFactory<>("occpRemarks"));

		TableColumn<TableOccupancyDetail, String> tcoccpPhoto = new TableColumn<>("PHOTO");
		tcoccpPhoto.setCellValueFactory(new PropertyValueFactory<>("occpPhoto"));

		tbl.getColumns().addAll(tctenantID, tcoccpDeposit );

	}
	
	public void TablePayment (TableView<TablePayment> tbl) {

		TableColumn<TablePayment, String> tcpayID = new TableColumn<>(" ID");
		tcpayID.setCellValueFactory(new PropertyValueFactory<>("payID"));

		TableColumn<TablePayment, String> tcpayParti = new TableColumn<>("PARTICULARS");
		tcpayParti.setCellValueFactory(new PropertyValueFactory<>("payParti"));

		TableColumn<TablePayment, String> tcpayAmount = new TableColumn<>("AMOUNT");
		tcpayAmount.setCellValueFactory(new PropertyValueFactory<>("payAmount"));

		tbl.getColumns().addAll(tcpayParti, tcpayAmount );

	}
	
	public void TableRentPayment(TableView<TableRentPayment> tbl) {

		TableColumn<TableRentPayment, String> tcpayID = new TableColumn<>("ID");
		tcpayID.setCellValueFactory(new PropertyValueFactory<>("payID"));

		TableColumn<TableRentPayment, String> tcpayNo = new TableColumn<>("RECEIPT NO");
		tcpayNo.setCellValueFactory(new PropertyValueFactory<>("payNo"));

		TableColumn<TableRentPayment, String> tcoccpID = new TableColumn<>("TENANT");
		tcoccpID.setCellValueFactory(new PropertyValueFactory<>("occpID"));

		TableColumn<TableRentPayment, String> tcstateID = new TableColumn<>("STATEMENT NO");
		tcstateID.setCellValueFactory(new PropertyValueFactory<>("stateID"));

		TableColumn<TableRentPayment, String> tcpayDate = new TableColumn<>("DATE");
		tcpayDate.setCellValueFactory(new PropertyValueFactory<>("payDate"));

		TableColumn<TableRentPayment, String> tcpayAmount = new TableColumn<>("AMOUNT");
		tcpayAmount.setCellValueFactory(new PropertyValueFactory<>("payAmount"));

		TableColumn<TableRentPayment, String> tcpayVIA = new TableColumn<>("VIA");
		tcpayVIA.setCellValueFactory(new PropertyValueFactory<>("payVIA"));

		TableColumn<TableRentPayment, String> tcpayReceipt = new TableColumn<>("RECEIPT STATUS");
		tcpayReceipt.setCellValueFactory(new PropertyValueFactory<>("payReceipt"));

		tbl.getColumns().addAll(tcpayNo, tcoccpID, tcstateID, tcpayDate, tcpayAmount, tcpayVIA, tcpayReceipt );

	}

	public void Dashboard(TableView<TableDashboard> tbl) {

		TableColumn<TableDashboard, String> tcbldgid = new TableColumn<>("BLDG ID");
		tcbldgid.setCellValueFactory(new PropertyValueFactory<>("bldgid"));

		TableColumn<TableDashboard, String> tcbldgname = new TableColumn<>("BLDG NAME");
		tcbldgname.setCellValueFactory(new PropertyValueFactory<>("bldgname"));
		
		TableColumn<TableDashboard, String> tcbldgaddress = new TableColumn<>("BLDG ADDRESS");
		tcbldgaddress.setCellValueFactory(new PropertyValueFactory<>("bldgaddress"));

		TableColumn<TableDashboard, String> tcbldgdesc = new TableColumn<>("BLDG DESCRIPTION");
		tcbldgdesc.setCellValueFactory(new PropertyValueFactory<>("bldgdesc"));

		TableColumn<TableDashboard, String> tcimg = new TableColumn<>("IMAGE");
		tcimg.setCellValueFactory(new PropertyValueFactory<>("bldgImage"));
	        
		tbl.getColumns().addAll( tcimg, tcbldgname,  tcbldgaddress);

	}
	
	public void TableTenantDetail(TableView<TableTenantDetail> tbl) {

		TableColumn<TableTenantDetail, String> tctenantid = new TableColumn<>("ID");
		tctenantid.setCellValueFactory(new PropertyValueFactory<>("tenantid"));

		TableColumn<TableTenantDetail, String> tctenantName = new TableColumn<>("NAME");
		tctenantName.setCellValueFactory(new PropertyValueFactory<>("tenantName"));

		TableColumn<TableTenantDetail, String> tctenantAddress = new TableColumn<>("ADDRESS");
		tctenantAddress.setCellValueFactory(new PropertyValueFactory<>("tenantAddress"));

		TableColumn<TableTenantDetail, String> tctenantMobile = new TableColumn<>("MOBILE NO");
		tctenantMobile.setCellValueFactory(new PropertyValueFactory<>("tenantMobile"));

		TableColumn<TableTenantDetail, String> tctenantLandline = new TableColumn<>("LANDLINE NO");
		tctenantLandline.setCellValueFactory(new PropertyValueFactory<>("tenantLandline"));

		TableColumn<TableTenantDetail, String> tctenantEAdd = new TableColumn<>("EMAIL ADDRESS");
		tctenantEAdd.setCellValueFactory(new PropertyValueFactory<>("tenantEAdd"));

		TableColumn<TableTenantDetail, String> tctenantBday = new TableColumn<>("BIRTHDAY");
		tctenantBday.setCellValueFactory(new PropertyValueFactory<>("tenantBday"));

		TableColumn<TableTenantDetail, String> tctenantGender = new TableColumn<>("GENDER");
		tctenantGender.setCellValueFactory(new PropertyValueFactory<>("tenantGender"));

		TableColumn<TableTenantDetail, String> tctenantAcct = new TableColumn<>("ACCOUNT");
		tctenantAcct.setCellValueFactory(new PropertyValueFactory<>("tenantAcct"));

		TableColumn<TableTenantDetail, String> tctenantPhoto = new TableColumn<>("PHOTO");
		tctenantPhoto.setCellValueFactory(new PropertyValueFactory<>("tenantPhoto"));

		tbl.getColumns().addAll(tctenantPhoto, tctenantName,  tctenantMobile, tctenantLandline );

	}
	
	public void TableUnitDetail(TableView<TableUnitDetail> tbl) {

		TableColumn<TableUnitDetail, String> tcuID = new TableColumn<>("ID");
		tcuID.setCellValueFactory(new PropertyValueFactory<>("uID"));

		TableColumn<TableUnitDetail, String> tcuName = new TableColumn<>("NAME");
		tcuName.setCellValueFactory(new PropertyValueFactory<>("uName"));

		TableColumn<TableUnitDetail, String> tcbldgID = new TableColumn<>("BUILDING");
		tcbldgID.setCellValueFactory(new PropertyValueFactory<>("bldgID"));

		TableColumn<TableUnitDetail, String> tcuFloor = new TableColumn<>("FLOOR");
		tcuFloor.setCellValueFactory(new PropertyValueFactory<>("uFloor"));

		TableColumn<TableUnitDetail, String> tcuLocation = new TableColumn<>("LOCATION");
		tcuLocation.setCellValueFactory(new PropertyValueFactory<>("uLocation"));

		TableColumn<TableUnitDetail, String> tcuType = new TableColumn<>("TYPE");
		tcuType.setCellValueFactory(new PropertyValueFactory<>("uType"));

		TableColumn<TableUnitDetail, String> tcuArea = new TableColumn<>("AREA");
		tcuArea.setCellValueFactory(new PropertyValueFactory<>("uArea"));

		TableColumn<TableUnitDetail, String> tcuFeature = new TableColumn<>("FEATURES");
		tcuFeature.setCellValueFactory(new PropertyValueFactory<>("uFeature"));

		TableColumn<TableUnitDetail, String> tcuOtherFeature = new TableColumn<>("OTHER FEATURES");
		tcuOtherFeature.setCellValueFactory(new PropertyValueFactory<>("uOtherFeature"));

		TableColumn<TableUnitDetail, String> tcuBedroom = new TableColumn<>("BEDROOM");
		tcuBedroom.setCellValueFactory(new PropertyValueFactory<>("uBedroom"));

		TableColumn<TableUnitDetail, String> tcuBathroom = new TableColumn<>("BATHROOM");
		tcuBathroom.setCellValueFactory(new PropertyValueFactory<>("uBathroom"));

		TableColumn<TableUnitDetail, String> tcuMax = new TableColumn<>("MAXIMUM CAPACITY");
		tcuMax.setCellValueFactory(new PropertyValueFactory<>("uMax"));

		TableColumn<TableUnitDetail, String> tcuCondition = new TableColumn<>("CONDITION");
		tcuCondition.setCellValueFactory(new PropertyValueFactory<>("uCondition"));

		TableColumn<TableUnitDetail, String> tcuRentAmount = new TableColumn<>("RENT AMOUNT");
		tcuRentAmount.setCellValueFactory(new PropertyValueFactory<>("uRentAmount"));

		TableColumn<TableUnitDetail, String> tcuAssocAmount = new TableColumn<>("ASSOCIATION AMOUNT");
		tcuAssocAmount.setCellValueFactory(new PropertyValueFactory<>("uAssocAmount"));

		TableColumn<TableUnitDetail, String> tcuRemarks = new TableColumn<>("REMARKS");
		tcuRemarks.setCellValueFactory(new PropertyValueFactory<>("uRemarks"));

		TableColumn<TableUnitDetail, String> tcunitPhoto = new TableColumn<>("PHOTO");
		tcunitPhoto.setCellValueFactory(new PropertyValueFactory<>("unitPhoto"));

		tbl.getColumns().addAll(tcunitPhoto, tcuName,  tcuFeature, tcuCondition );

	}
	
	public void TableUnitIssue(TableView<TableUnitIssue> tbl) {

		TableColumn<TableUnitIssue, String> tcunitIssue = new TableColumn<>("ID");
		tcunitIssue.setCellValueFactory(new PropertyValueFactory<>("unitIssue"));

		TableColumn<TableUnitIssue, String> tcunitID = new TableColumn<>("UNIT");
		tcunitID.setCellValueFactory(new PropertyValueFactory<>("unitID"));

		TableColumn<TableUnitIssue, String> tcunitInspection = new TableColumn<>("INSPECTION DATE");
		tcunitInspection.setCellValueFactory(new PropertyValueFactory<>("unitInspection"));

		TableColumn<TableUnitIssue, String> tcissueName = new TableColumn<>("ISSUE");
		tcissueName.setCellValueFactory(new PropertyValueFactory<>("issueName"));

		TableColumn<TableUnitIssue, String> tcissueDesc = new TableColumn<>("DESCRIPTION");
		tcissueDesc.setCellValueFactory(new PropertyValueFactory<>("issueDesc"));

		TableColumn<TableUnitIssue, String> tcactPLan = new TableColumn<>("ACTIVITY PLAN");
		tcactPLan.setCellValueFactory(new PropertyValueFactory<>("actPlan"));

		TableColumn<TableUnitIssue, String> tcissueStatus = new TableColumn<>("STATUS");
		tcissueStatus.setCellValueFactory(new PropertyValueFactory<>("issueStatus"));

		TableColumn<TableUnitIssue, String> tcissueRemark = new TableColumn<>("REMARKS");
		tcissueRemark.setCellValueFactory(new PropertyValueFactory<>("issueRemark"));


		tbl.getColumns().addAll(tcunitID, tcunitInspection, tcissueName, tcissueDesc, tcactPLan, tcissueStatus, 
				tcissueRemark );

	}
	
	public void TableUnitMaintenance(TableView<TableUnitMaintenance> tbl) {

		TableColumn<TableUnitMaintenance, String> tcmxID = new TableColumn<>("ID");
		tcmxID.setCellValueFactory(new PropertyValueFactory<>("mxID"));

		TableColumn<TableUnitMaintenance, String> tcissueID = new TableColumn<>("ISSUE");
		tcissueID.setCellValueFactory(new PropertyValueFactory<>("issueID"));

		TableColumn<TableUnitMaintenance, String> tcunitID = new TableColumn<>("UNIT");
		tcunitID.setCellValueFactory(new PropertyValueFactory<>("unitID"));

		TableColumn<TableUnitMaintenance, String> tcunitRef = new TableColumn<>("REFERENCE ID");
		tcunitRef.setCellValueFactory(new PropertyValueFactory<>("unitRef"));

		TableColumn<TableUnitMaintenance, String> tcumxDate = new TableColumn<>("DATE");
		tcumxDate.setCellValueFactory(new PropertyValueFactory<>("umxDate"));

		TableColumn<TableUnitMaintenance, String> tcumxAct = new TableColumn<>("ACTIVITY");
		tcumxAct.setCellValueFactory(new PropertyValueFactory<>("umxAct"));

		TableColumn<TableUnitMaintenance, String> tcumxDesc = new TableColumn<>("DESCRIPTION");
		tcumxDesc.setCellValueFactory(new PropertyValueFactory<>("umxDesc"));

		TableColumn<TableUnitMaintenance, String> tcumxRequest = new TableColumn<>("REQUEST BY");
		tcumxRequest.setCellValueFactory(new PropertyValueFactory<>("umxRequest"));

		TableColumn<TableUnitMaintenance, String> tcoccpID = new TableColumn<>("OCCUPANCY");
		tcoccpID.setCellValueFactory(new PropertyValueFactory<>("occpID"));

		TableColumn<TableUnitMaintenance, String> tcumxContractor = new TableColumn<>("CONTRACTOR");
		tcumxContractor.setCellValueFactory(new PropertyValueFactory<>("umxContractor"));

		TableColumn<TableUnitMaintenance, String> tcumxStatus = new TableColumn<>("STATUS");
		tcumxStatus.setCellValueFactory(new PropertyValueFactory<>("umxStatus"));

		TableColumn<TableUnitMaintenance, String> tcumxTotal = new TableColumn<>("TOTAL");
		tcumxTotal.setCellValueFactory(new PropertyValueFactory<>("umxTotal"));

		TableColumn<TableUnitMaintenance, String> tcumxRemarks = new TableColumn<>("REMARKS");
		tcumxRemarks.setCellValueFactory(new PropertyValueFactory<>("umxRemarks"));

		tbl.getColumns().addAll(tcissueID, tcunitID, tcunitRef, tcumxDate, tcumxAct , tcumxDesc, tcumxRequest, tcoccpID,
				tcumxContractor, tcumxStatus, tcumxTotal, tcumxRemarks );

	}
	
	public void TableUnitType(TableView<TableUnitType> tbl) {

		TableColumn<TableUnitType, String> tcutID = new TableColumn<>("ID");
		tcutID.setCellValueFactory(new PropertyValueFactory<>("utID"));

		TableColumn<TableUnitType, String> tcutName = new TableColumn<>("NAME");
		tcutName.setCellValueFactory(new PropertyValueFactory<>("utName"));
		
		TableColumn<TableUnitType, String> tcbldgID = new TableColumn<>("BUILDING");
		tcbldgID.setCellValueFactory(new PropertyValueFactory<>("bldgID"));

		TableColumn<TableUnitType, String> tcutDesc = new TableColumn<>("DESCRIPTION");
		tcutDesc.setCellValueFactory(new PropertyValueFactory<>("utDesc"));

		TableColumn<TableUnitType, String> tcutArea = new TableColumn<>("AREA");
		tcutArea.setCellValueFactory(new PropertyValueFactory<>("utArea"));

		TableColumn<TableUnitType, String> tcutPrice = new TableColumn<>("PRICE");
		tcutPrice.setCellValueFactory(new PropertyValueFactory<>("utPrice"));

		TableColumn<TableUnitType, String> tcutMax = new TableColumn<>("MAXIMUM CAPACITY");
		tcutMax.setCellValueFactory(new PropertyValueFactory<>("utMax"));

		tbl.getColumns().addAll( tcutName,  tcbldgID, tcutDesc, tcutArea, tcutPrice, tcutMax );

	}
	
	public void TableUser(TableView<TableUser> tbl) {

		TableColumn<TableUser, String> tcuserid = new TableColumn<>("ID");
		tcuserid.setCellValueFactory(new PropertyValueFactory<>("userid"));

		TableColumn<TableUser, String> tcusername = new TableColumn<>("USERNAME");
		tcusername.setCellValueFactory(new PropertyValueFactory<>("username"));
		
		TableColumn<TableUser, String> tcpassword = new TableColumn<>("PASSWORD");
		tcpassword.setCellValueFactory(new PropertyValueFactory<>("password"));

		TableColumn<TableUser, String> tcpincode = new TableColumn<>("PINCODE");
		tcpincode.setCellValueFactory(new PropertyValueFactory<>("pincode"));

		TableColumn<TableUser, String> tcempname = new TableColumn<>("NAME");
		tcempname.setCellValueFactory(new PropertyValueFactory<>("empname"));
		
		TableColumn<TableUser, String> tcaccess = new TableColumn<>("ACCESS");
		tcaccess.setCellValueFactory(new PropertyValueFactory<>("access"));

		TableColumn<TableUser, String> tcstatus = new TableColumn<>("STATUS");
		tcstatus.setCellValueFactory(new PropertyValueFactory<>("status"));

		tbl.getColumns().addAll( tcusername, tcaccess, tcstatus );

	}
		
	public void TableUserTrackLogs(TableView<TableUserTrackLogs> tbl) {

		TableColumn<TableUserTrackLogs, String> tcuserid = new TableColumn<>("ID");
		tcuserid.setCellValueFactory(new PropertyValueFactory<>("userid"));

		TableColumn<TableUserTrackLogs, String> tcusername = new TableColumn<>("NAME");
		tcusername.setCellValueFactory(new PropertyValueFactory<>("username"));
		
		TableColumn<TableUserTrackLogs, String> tcuserdate = new TableColumn<>("DATE");
		tcuserdate.setCellValueFactory(new PropertyValueFactory<>("userdate"));

		tbl.getColumns().addAll( tcusername,  tcuserdate);

	}
	
	public void TableContractor(TableView<TableContractor> tbl) {

		TableColumn<TableContractor, String> tccontrID = new TableColumn<>("ID");
		tccontrID.setCellValueFactory(new PropertyValueFactory<>("contrID"));

		TableColumn<TableContractor, String> tccontrName = new TableColumn<>("NAME");
		tccontrName.setCellValueFactory(new PropertyValueFactory<>("contrName"));
		
		TableColumn<TableContractor, String> tccontrAddress = new TableColumn<>("ADDRESS");
		tccontrAddress.setCellValueFactory(new PropertyValueFactory<>("contrAddress"));

		TableColumn<TableContractor, String> tccontrContPerson = new TableColumn<>("CONTACT PERSON");
		tccontrContPerson.setCellValueFactory(new PropertyValueFactory<>("contrContPerson"));

		TableColumn<TableContractor, String> tccontrContNo = new TableColumn<>("CONTACT NUMBER");
		tccontrContNo.setCellValueFactory(new PropertyValueFactory<>("contrContNo"));
		
		TableColumn<TableContractor, String> tccontrRemarks = new TableColumn<>("REMARKS");
		tccontrRemarks.setCellValueFactory(new PropertyValueFactory<>("contrRemarks"));

		tbl.getColumns().addAll( tccontrName,  tccontrAddress, tccontrContPerson, tccontrContNo, tccontrRemarks );

	}

	public void TableExpense(TableView<TableExpense> tbl) {

		TableColumn<TableExpense, String> tcexpID = new TableColumn<>(" ID");
		tcexpID.setCellValueFactory(new PropertyValueFactory<>("expID"));

		TableColumn<TableExpense, String> tcexpRef = new TableColumn<>("REFERENCE");
		tcexpRef.setCellValueFactory(new PropertyValueFactory<>("expRef"));

		TableColumn<TableExpense, String> tcexpContractor = new TableColumn<>("CONTRACTOR");
		tcexpContractor.setCellValueFactory(new PropertyValueFactory<>("expContractor"));

		TableColumn<TableExpense, String> tcexpDate = new TableColumn<>("TRANSACTION DATE");
		tcexpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));

		TableColumn<TableExpense, String> tcexpCOA = new TableColumn<>("CHART OF ACCOUNT");
		tcexpCOA.setCellValueFactory(new PropertyValueFactory<>("expCOA"));

		TableColumn<TableExpense, String> tcexpDesc = new TableColumn<>("DESCRIPTION");
		tcexpDesc.setCellValueFactory(new PropertyValueFactory<>("expDesc"));

		TableColumn<TableExpense, String> tcexpAmount = new TableColumn<>("AMOUNT");
		tcexpAmount.setCellValueFactory(new PropertyValueFactory<>("expAmount"));

		tbl.getColumns().addAll( tcexpRef, tcexpContractor, tcexpDate, tcexpCOA, tcexpDesc, tcexpAmount );

	}

	public void TableSummary(TableView<TableSummary> tbl) {

		TableColumn<TableSummary, String> tcexpMonth = new TableColumn<>("MONTH");
		tcexpMonth.setCellValueFactory(new PropertyValueFactory<>("expMonth"));

		TableColumn<TableSummary, String> tcexpTotal = new TableColumn<>("TOTAL");
		tcexpTotal.setCellValueFactory(new PropertyValueFactory<>("expTotal"));

		tbl.getColumns().addAll( tcexpMonth, tcexpTotal );

	}
	
}
