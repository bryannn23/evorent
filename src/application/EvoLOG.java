package application;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.io.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EvoLOG {

    public static final int MINOR = 0x01;
    public static final int MAJOR = 0x02;
    public static final int CRITICAL = 0x04;
    public static final int DEBUG = 0x08;
    
    private static String LogFileName = "EVO_logfile";
    private static String LogFilePath = "./";
    private static int LogSeverity = 0x0F; //0x1 - Minor, 0x2 - Major, 0x4 - Critical, 0x8 - Debug
    
    static void setAllowedSeverity(int Severity){
        LogSeverity = Severity;
    }
    
    static int isSeverityAllowed(int Severity) {
        Severity = Severity & LogSeverity;
        return Severity;
    }
    
    private static String getSeverity(int Severity) {
        if (Severity == 0x01) return " MINOR";
        else if (Severity == 0x02) return " MAJOR";
        else if (Severity == 0x04) return " CRITICAL";
        else return " DEBUG";
    }
    
    static String getFileDate() {
    	
    	String filename = LogFilePath + LogFileName;
        Path p = Paths.get(filename);
    	
        try {
        BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class)
                  .readAttributes();
        
        FileTime date = view.creationTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yy");
        return  df.format(date.toMillis());
        } catch (IOException x) {
            System.out.println("Info elgfdi : " + x.toString() + x.getMessage()); 
            return "";
        } 
    }
    
    static int cleanLog() {
        //Path path = FileSystems.getDefault().getPath("", LogFileName);
    	String filename = LogFilePath + LogFileName;
        Path path = Paths.get(filename);
        
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            //System.err.format("%s: no such" + " file %n", path);
            System.out.println("Err elcln : " + x.toString() + x.getMessage());
        } catch (DirectoryNotEmptyException x) {
            //System.err.format("%s not empty%n", path);
            System.out.println("Err elcld : " + x.toString() + x.getMessage());
        } catch (IOException x) {
               // File permission problems are caught here.
            //System.err.println(x);
        	System.out.println("Err elcli : " + x.toString() + x.getMessage());
        }
        
        return 0;
    }
    
    static String getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("MM-dd-yy HH:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    
    static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("MM-dd-yy");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    
    static String getDayOfMonth() {
        DateFormat df = new SimpleDateFormat("dd");
        Date dateobj = new Date();
        return df.format(dateobj);
    }
    
    static void writeLogLn(int Severity, String log_message ) {
        
        if (0 == isSeverityAllowed(Severity)) return;
        String filename = LogFilePath + LogFileName;
        log_message = "[" + getCurrentDateTime() + getSeverity(Severity) + "] " + log_message + "\n";
        byte data[] = log_message.getBytes();
        Path p = Paths.get(filename);
        
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException x) {
            //System.err.println(x); 
            System.out.println("Err elwli : " + x.toString() + x.getMessage());
        }        
    }
    
    static void initializeLog() {
        
        int Severity = MINOR;
        String log_message = "Logging Started for build #" + Globals.G_BUILD_NUM;
                
        LogFileName = LogFileName + getDayOfMonth() + ".txt";
        String filename = LogFilePath + LogFileName;
        
        log_message = "[" + getCurrentDateTime() + getSeverity(Severity) + "] " + log_message + "\n";
        byte data[] = log_message.getBytes();
        Path p = Paths.get(filename);
        
        
        	
	        if (!getFileDate().equals(getCurrentDate())) {
	        	cleanLog();
	        	try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE))) {
	                out.write(data, 0, data.length);
	        	}
		        catch (IOException x) {
		            System.out.println("Err elilci : " + x.toString() + x.getMessage());     
		        }  
	        }
	        else {
	        	try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
	                out.write(data, 0, data.length);
	        	} catch (IOException x) {
		            System.out.println("Err elilai : " + x.toString() + x.getMessage());     
		        }  
	        }
	        
      
    }
	
}
