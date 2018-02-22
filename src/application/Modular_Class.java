package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Modular_Class {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;
	PreparedStatement ps1 = null;
	ResultSet rs1 = null;
	
	String desc = "Deleted by: " + Globals.G_Employee_ID + " " + Globals.G_Employee_Name;
	
	public TextFormatter<String> getTextFlexiFormatter(int maxEntries, char option) {
		
		UnaryOperator<Change> filter = getFlexiFilter(maxEntries, option);
		TextFormatter<String> tf = new TextFormatter<>(filter);
		return tf;
		
	}
	
	private UnaryOperator<Change> getFlexiFilter (int maxEntries, char option) {
	
		return change -> {
			String text = change.getText();
			if (change.isContentChange()) {
				if (change.getControlNewText().length() > maxEntries)
					return null;
			}
			if (option == 'n') {
				if (text.matches("[0-9\\.,-]+") || text.isEmpty()) {
					return change;
				}
			} else if (option == 'a')
				return change;

			return null;
		};
		
	}

	public void create(String tblname, String condi, Label lblMessage, String values, ActionEvent event)
			throws SQLException {

		String check = "SELECT * FROM " + tblname + " WHERE " + condi + " AND REFERENCE_ID IS NULL";
		ps = dbcon.connect.prepareStatement(check);
		rs = ps.executeQuery();
		
		if (rs.next()) {
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			String query = "INSERT INTO " + tblname + " VALUES ( " + values + " )";

			
			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully added a record.");
				alert.showAndWait();

				((Node) event.getSource()).getScene().getWindow().hide();
				
			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert " + tblname + " : " + e.getMessage());
			} finally {
				ps.close();
			}
		}
	}

	public void createWithFinalize(String tblname, String condi, Label lblMessage, String values, ActionEvent event)
			throws SQLException {

		String check = "SELECT * FROM " + tblname + " WHERE " + condi + " AND REFERENCE_ID IS NULL";
		ps = dbcon.connect.prepareStatement(check);
		rs = ps.executeQuery();
		
		if (rs.next()) {
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			String query = "INSERT INTO " + tblname + " VALUES ( " + values + " )";
			
			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert " + tblname + " : " + e.getMessage());
			} finally {
				ps.close();
			}
		}
	}

	public void createWithPhoto (String tblname, String condi, Label lblMessage, String values, ImageView imgFile, File file, FileInputStream getFile, 
			ActionEvent event ) throws SQLException {
	
        String check = "SELECT * FROM "+tblname+" WHERE "+ condi+" ";
        
        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
            
            String query = "INSERT INTO "+tblname+" VALUES ( "+ values +" )";
            
            try {
                if (imgFile.getImage() != null) {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
                    ps.executeUpdate();
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
    				alert.setTitle("Information");
    				alert.setHeaderText(null);
    				alert.setContentText("You have successfully added a record.");
    				alert.showAndWait();

    				((Node) event.getSource()).getScene().getWindow().hide();
    				
                }
                else {
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
    				alert.setTitle("Information");
    				alert.setHeaderText(null);
    				alert.setContentText("You have successfully added a record.");
    				alert.showAndWait();

    				((Node) event.getSource()).getScene().getWindow().hide();
    				
                }

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert "+tblname+" : " + e.getMessage());
            } finally {
                ps.close();
            }
        }
            
	}

	public void createWithPhotoFinal (String tblname, String condi, Label lblMessage, String values, ImageView imgFile, File file, FileInputStream getFile, 
			ActionEvent event ) throws SQLException {
	
        String check = "SELECT * FROM "+tblname+" WHERE "+ condi+" ";
        
        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
            
            String query = "INSERT INTO "+tblname+" VALUES ( "+ values +" )";
            
            try {
            	
                if (imgFile.getImage() != null) {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
                    ps.executeUpdate();
                    
                }
                else {
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                    
                }

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert "+tblname+" : " + e.getMessage());
            } finally {
                ps.close();
            }
        }
            
	}
	
	public void finalize(String tblname) throws SQLException {
		
		String query = "UPDATE " + tblname + " SET FINALIZED_RECORD = 'Y' WHERE SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " AND FINALIZED_RECORD = 'N' ";
		
		try {
			ps = dbcon.connect.prepareStatement(query);
			ps.executeUpdate();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("You have successfully added a record.");
			alert.showAndWait();

		} catch (Exception e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err " + tblname + " : " + e.getMessage());
		} finally {
			ps.close();
		}
	}

	public void cancelreq(String tblname, Stage primaryStage, Event e) throws SQLException {

		String query = "DELETE FROM " + tblname + " WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText(
				"Clicking CANCEL without FINALIZING the record, will cause deletion of all created records. Are you sure you want to continue?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			
			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();
				primaryStage.close();

			} catch (Exception e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err " + tblname + " : " + e1.getMessage());
			}
		} else {
			e.consume();
		}
	}

	public void cancel(String tblname, Stage primaryStage) throws SQLException {

		String query = "DELETE FROM " + tblname + " WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText(
				"Clicking CANCEL without FINALIZING the record, will cause deletion of all created records. Are you sure you want to continue?");

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK) {
			
			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();
				primaryStage.close();

			}

			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err " + tblname + " : " + e.getMessage());
			} finally {
				ps.close();
			}
			
		} else {
			
			return;

		}
	}

	public void editrecordwithfinalize(String tblname, String condi, Label lblMessage, String col, String condition,
			ActionEvent event, Button update, Button create) throws SQLException {

		String check = "SELECT * FROM " + tblname + " WHERE REFERENCE_ID IS NULL AND " + condi + " ";

		ps = dbcon.connect.prepareStatement(check);
		rs = ps.executeQuery();
		
		if (rs.next()) {
			
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			
			String query = "UPDATE " + tblname + " SET " + col + " WHERE " + condition + " ";

			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();

				update.setVisible(false);
				create.setVisible(true);
				
			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit " + tblname + " : " + e.getMessage());
			} finally {
				ps.close();
			}
		}
	}

	public void editrecord(String tblname, String condi, Label lblMessage, String col, String condition,
			ActionEvent event) throws SQLException {

		String check = "SELECT * FROM " + tblname + " WHERE REFERENCE_ID IS NULL AND " + condi + " ";

		ps = dbcon.connect.prepareStatement(check);
		rs = ps.executeQuery();
		if (rs.next()) {
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			String query = "UPDATE " + tblname + " SET " + col + " WHERE " + condition + " ";

			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully updated a record.");
				alert.showAndWait();
				((Node) event.getSource()).getScene().getWindow().hide();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit " + tblname + " : " + e.getMessage());
			} finally {
				ps.close();
			}
		}
	}
	
	public void editwithPhoto (String tblname, String condi, Label lblMessage, String col, String condition, ImageView imgFile, 
			File file, FileInputStream getFile, String path, ActionEvent event ) throws SQLException {
		
		String check = "SELECT * FROM "+ tblname +" WHERE REFERENCE_ID IS NULL AND "+ condi +" ";

        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
        	
            String query = "UPDATE "+ tblname +" SET "+ col +" WHERE "+ condition +" ";

            try {

                if (imgFile.getImage() != null) {
                    
                    if (getFile == null) 
                    {
                        file = new File(path) ;
                        getFile = new FileInputStream ( file );
                    }
                    
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
                    ps.executeUpdate();
                    
                }
                else {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                }

                Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully updated a record.");
				alert.showAndWait();
				((Node) event.getSource()).getScene().getWindow().hide();
				
                
            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit "+ tblname +" : " + e.getMessage());
            } finally {
                ps.close();
            }
        } 
	}
	
	public void editwithPhotoFinal (String tblname, String condi, Label lblMessage, String col, String condition, ImageView imgFile, 
			File file, FileInputStream getFile, String path, ActionEvent event, Button update, Button create ) throws SQLException {
		
		String check = "SELECT * FROM "+ tblname +" WHERE REFERENCE_ID IS NULL AND "+ condi +" ";

        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
        	
            String query = "UPDATE "+ tblname +" SET "+ col +" WHERE "+ condition +" ";

            try {

                if (imgFile.getImage() != null) {
                    
                    if (getFile == null) 
                    {
                        file = new File(path) ;
                        getFile = new FileInputStream ( file );
                    }
                    
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
                    ps.executeUpdate();
                    
                }
                else {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                }
                
                update.setVisible(false);
				create.setVisible(true);

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit "+ tblname +" : " + e.getMessage());
            } finally {
                ps.close();
            }
        } 
	}

	public void updaterecord(String tblname, String col, String col2, String condi) throws SQLException {

		String query = "INSERT INTO " + tblname + " (" + col + ") SELECT " + col2 + " FROM " + tblname + " WHERE "
				+ condi + " ";

		try {
			ps = dbcon.connect.prepareStatement(query);
			ps.executeUpdate();

		} catch (Exception e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err updaterecord " + tblname + " : " + e.getMessage());
		} finally {
			ps.close();
		}
	}
	
	public void deletemes(String tblname, String colname1, String id, String colname2, String colname3)
			throws SQLException {

		String query = "UPDATE " + tblname + " SET " + colname1 + " = " + id + " , " + colname2 + " = '" + desc + "' "
				+ " WHERE " + colname3 + " = " + id + " ";

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText(
				"Deleting this record might corrupt record from other table/module. Are you sure you want to delete?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			
			try {
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err " + tblname + " del : " + e.getMessage());
			} finally {
				ps.close();
			}

			Alert alert1 = new Alert(AlertType.INFORMATION);
			alert1.setTitle("Information");
			alert1.setHeaderText(null);
			alert1.setContentText("You have successfully deleted a record.");
			alert1.showAndWait();
			
		} else {
			
			return;
			
		}
	}

	public void createWithFileFinal (String tblname, String condi, Label lblMessage, String values, 
			String fileName, byte[] file, ActionEvent event ) throws SQLException {
	
        String check = "SELECT * FROM "+tblname+" WHERE "+ condi+" ";
        
        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
            
            String query = "INSERT INTO "+tblname+" VALUES ( "+ values +" )";
            
            try {
            	
                if (fileName != null) {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBytes(1, file);
                    ps.executeUpdate();
                    
                }
                else {
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                    
                }

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert "+tblname+" : " + e.getMessage());
            } finally {
                ps.close();
            }
        }
            
	}

	public void editwithFileFinal (String tblname, String condi, Label lblMessage, String col, String condition, String FileName, 
			byte[] file, ActionEvent event, Button update, Button create ) throws SQLException {
		
		String check = "SELECT * FROM "+ tblname +" WHERE REFERENCE_ID IS NULL AND "+ condi +" ";

        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
        	
            String query = "UPDATE "+ tblname +" SET "+ col +" WHERE "+ condition +" ";

            try {

                if (FileName != null) {
                    
                    if (file == null) 
                    { 
                        file = new byte[(int) FileName.length()] ;
                         
                    }
                    
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBytes(1, file);
                    ps.executeUpdate();
                    
                }
                else {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                }
                
                update.setVisible(false);
				create.setVisible(true);

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit "+ tblname +" : " + e.getMessage());
            } finally {
                ps.close();
            }
        } 
	}

	public void editwithFile (String tblname, String condi, Label lblMessage, String col, String condition, String FileName, 
			byte[] file, ActionEvent event ) throws SQLException {
		
		String check = "SELECT * FROM "+ tblname +" WHERE REFERENCE_ID IS NULL AND "+ condi +" ";

        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
        	
            String query = "UPDATE "+ tblname +" SET "+ col +" WHERE "+ condition +" ";

            try {

                if (FileName != null) {
                    
                    if (file == null) 
                    { 
                        file = new byte[(int) FileName.length()] ;
                         
                    }
                    
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBytes(1, file);
                    ps.executeUpdate();
                    
                }
                else {
                	
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setString(1, null);
                    ps.executeUpdate();
                }
                
                Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("You have successfully updated a record.");
				alert.showAndWait();
				((Node) event.getSource()).getScene().getWindow().hide();
				
                

            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit "+ tblname +" : " + e.getMessage());
            } finally {
                ps.close();
            }
        } 
	}

	
	
}
