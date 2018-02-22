package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Globals {
	
	final static int G_BUILD_NUM = 1002;
	
	final static int G_DEVELOPER_RIGHTS = 0;
	final static int G_EXECUTIVE_RIGHTS = 1	;
	final static int G_ADMIN_RIGHTS = 2	;
	final static int G_MEMBER_RIGHTS = 3;
	
	public static String G_IP_connection;
	public static String G_Employee_Name;
	public static int G_Employee_ID;
	public static int G_Employee_Access_ID;
	public static int G_Employee_Status;
	public static int G_Profile_ID;
	
	public static void setDB_Location(String fileName) {
		
		try {
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			content = content.trim();
			G_IP_connection = "jdbc:mysql://" + content + ":3306/EVORENT?autoReconnect=true&useSSL=false";
			
			
		} catch (IOException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err globals dbcon : " +e.getMessage());
		}
		
		
	}
}
