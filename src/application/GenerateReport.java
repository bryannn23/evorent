 package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.swing.JRViewer;

public class GenerateReport extends JFrame {

    private static final long serialVersionUID = 1L;
 

    public void billingStatement(String datecreate, String stateno, String compName, String address, 
            String phone, String logo, String total,String duedate, String tname, 
            String tcompname, String taddress, String tphone, String custno,
            String[][] sTable) throws JRException, ClassNotFoundException, SQLException {
     
        DefaultTableModel tableModel;
        String reportSrcFile = "./Report/Billing_Statement.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        
        String[] columnNames = {"Particulars", "BALANCE",  "Amount" };

        tableModel = new DefaultTableModel(sTable, columnNames);
               
        HashMap<String, Object> parameters = new HashMap<String, Object>();
 
        parameters.put("DATE CREATED", datecreate);
        parameters.put("COMPANY NAME", compName);
        parameters.put("STATEMENT NO", stateno);
        parameters.put("ADDRESS", address);
        parameters.put("LOGO", logo);
        parameters.put("PHONE", phone);
       // parameters.put("WEBSITE", website);
        parameters.put("TNAME", tname);
        parameters.put("TCOMPANYNAME", tcompname);
        parameters.put("TADDRESS", taddress);
        parameters.put("TPHONE", tphone);
        parameters.put("TCUSTNO", custno);
        parameters.put("TOTAL", total);
        parameters.put("DUEDATE", duedate);
        
         
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters,
                new JRTableModelDataSource(tableModel));
        
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
      
    }
    
    public void receipt(String date, String receiptno, String compName, String address, String total, 
    	String tname, String tcompname, String taddress,String totalInWords, String remarks, String preparedBy,
           String parti1, String parti2, String parti3, String parti4, String parti5, String parti6, String parti7, 
           String amt1, String amt2, String amt3, String amt4, String amt5, String amt6, String amt7, 
           String[][] sTable)
        		   throws JRException, ClassNotFoundException, SQLException {

        DefaultTableModel tableModel;
        String reportSrcFile = "./Report/PaymentReceipt.jrxml";
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        
        String[] columnNames = {"Particulars",  "Amount" };

        tableModel = new DefaultTableModel(sTable, columnNames);
             
        HashMap<String, Object> parameters = new HashMap<String, Object>();
 
        parameters.put("DATE", date);
        parameters.put("COMPANY NAME", compName);
        parameters.put("RECEIPT NO", receiptno);
        parameters.put("COMPANY ADDRESS", address);
       // parameters.put("COMPANY TIN", TIN);
       // parameters.put("COMPANY WEBSITE", website);
        parameters.put("TENANT NAME", tname);
        parameters.put("BUSINESS NAME", tcompname);
        parameters.put("TENANT ADDRESS", taddress);
        parameters.put("TOTAL TO WORDS", totalInWords);
        parameters.put("REMARKS", remarks);
        parameters.put("TOTAL", total);
        parameters.put("PREPARED BY", preparedBy);
        parameters.put("PARTI1", parti1);
        parameters.put("PARTI2", parti2);
        parameters.put("PARTI3", parti3);
        parameters.put("PARTI4", parti4);
        parameters.put("PARTI5", parti5);
        parameters.put("PARTI6", parti6);
        parameters.put("PARTI7", parti7);
        parameters.put("AMT1", amt1);
        parameters.put("AMT2", amt2);
        parameters.put("AMT3", amt3);
        parameters.put("AMT4", amt4);
        parameters.put("AMT5", amt5);
        parameters.put("AMT6", amt6);
        parameters.put("AMT7", amt7);
        
		
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters,
        		 new JRTableModelDataSource(tableModel) );
        
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
      
    }
    
    public void SummaryOfExpenses(String companyname, String address, String contact, String title1, String title2, 
    		java.sql.Date startDate, java.sql.Date endDate,  
    		String preparedby, String building ) throws JRException, ClassNotFoundException, SQLException {
 
        String reportSrcFile = "./Report/SummaryExpenses.jrxml";
 
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
 
        parameters.put("CompanyName", companyname);
        parameters.put("Address", address);
        parameters.put("ContactInfo", contact);
        parameters.put("Title1", title1);
        parameters.put("Title2", title2);
        parameters.put("StartDate", startDate);
        parameters.put("EndDate", endDate);
        parameters.put("PreparedBy", preparedby);
        parameters.put("BuildingName", building);
        
		Connection con = DBconnection.con();
 
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, con);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
        
 
    } 

    public void SummaryOfCashIn(String companyname, String address, String contact, String title1, String title2, 
    		java.sql.Date startDate, java.sql.Date endDate,  
    		String preparedby, String building ) throws JRException, ClassNotFoundException, SQLException {
 
        String reportSrcFile = "./Report/SummaryRentPayment.jrxml";
 
        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
 
        parameters.put("CompanyName", companyname);
        parameters.put("Address", address);
        parameters.put("ContactInfo", contact);
        parameters.put("Title1", title1);
        parameters.put("Title2", title2);
        parameters.put("StartDate", startDate);
        parameters.put("EndDate", endDate);
        parameters.put("PreparedBy", preparedby);
        parameters.put("BuildingName", building);
        
		Connection con = DBconnection.con();
 
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
 
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, con);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 500);
        this.setVisible(true);
        
 
    } 
    
    
}
