package com.trn.ns.page.factory;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;

import com.trn.ns.page.constants.NSDBDatabaseConstants;
import com.trn.ns.page.constants.PatientXMLConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.utilities.DBUtil;
import com.trn.ns.web.page.WebActions;


public class DatabaseMethods extends WebActions {

	public DatabaseMethods(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	//	################################################
	//	user account
	//	################################################
	public String getTheme(){

		String value="";
		try {
			String query="SELECT [Theme] FROM [dbo].[UserAccount] where [Username]='scan';";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("Theme");

			DBUtil.closeDBConnection();
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return value;
	}

	public void updateTheme(String themeValue,String username){

		String updateQuery ="UPDATE [dbo].[UserAccount] SET [Theme]='"+themeValue+"' WHERE [Username]='"+username+"'";		

		try {

			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery);			

		} catch (SQLException e) {

			printStackTrace(e.getMessage());
		}

	}

	//Update User details query
	public void updateUserDetails(String Firstname, String Lastname, String Username)throws SQLException{

		String query="update [dbo].[UserAccount] set Firstname='"+Firstname+"',Lastname='"+Lastname+"' where Username='"+Username+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	//Updated with additional salt value field
	public void addUserInDB(String displayName, String username, String password, String saltValue, String pwdExpiryDate, String lastName, String middleName, String firstName, String email, String status, String desc, String createDatetime,String theme, String isAdmin,String secretKey,String desktopMode,String MobileMode,String viewerBackground){

		String insertQuery ="INSERT INTO [dbo].[UserAccount] VALUES('"+displayName+"','"+username+"','"+password+"','"+saltValue+"','"+pwdExpiryDate+"','"+lastName+"','"+middleName+"','"+firstName+"','"+email+"','"+status+"','"+desc+"','"+createDatetime+"','"+theme+"','"+isAdmin+"','"+secretKey+"','"+desktopMode+"','"+MobileMode+"','"+viewerBackground+"')";

		String deleteQuery = "DELETE FROM [dbo].[UserAccount] WHERE username='"+username+"'";

		try {
			LOGGER.info("Executing the query "+deleteQuery);
			DBUtil.runQuery("IF (SELECT COUNT(*) FROM [dbo].[UserAccount] WHERE username='"+username+"') > 0 	"+deleteQuery);
			LOGGER.info("Executing the query "+insertQuery);
			DBUtil.runQuery(insertQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * @author payal
	 * @param username_1
	 * @return: password from database
	 * Description: This method returns the encrypted password from database
	 */
	public String getPasswordFromDB(String userName) throws SQLException{
		String pass = "";
		String runQuery = "SELECT Password from [dbo].[UserAccount] WHERE username='"+userName+"'";
		LOGGER.info("Executing the query "+runQuery);
		ResultSet result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			pass = result.getString(1);
		}
		return pass;
	}


	/**
	 * @author payal
	 * @param username, encrypted password and salt values
	 * @return: null
	 * Description: This method update the password and salt values
	 */
	public void updatePassword(String userName,String encrPass, String encrSalt) throws SQLException{
		String query="update UserAccount set Password = N'"+encrPass+"' , Salt = N'"+encrSalt+"' where Username = N'"+userName+"'";
		//Updating password as Abc@2018#
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public int getUsersCount() throws SQLException {

		String query = "select count(*) from dbo.UserAccount;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		DBUtil.closeDBConnection();

		return value;
	}

	public void deleteAllUsers() throws SQLException {

		String query = "delete from dbo.TokenInformation";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

		query = "delete from dbo.UserAccount";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public void deleteAllUsers(String username) throws SQLException {

		String query = "delete from dbo.TokenInformation";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

		query = "DELETE FROM [dbo].[UserAccount] WHERE Username <> '"+username+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	//Getting the user type
	public int getUserType(int accountID) throws SQLException {

		String query = "select IsAdmin from dbo.UserAccount where AccountID = '"+accountID+"';";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		return value;
	}

	//Deleting the newly added user
	public void deleteAddedUser(int accountID) throws SQLException {
		String query = "delete from dbo.UserAccount where AccountID = '"+accountID+"';";
		LOGGER.info("Executing the query "+query);
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	//Getting last inserted accountID 
	public int getLastInsertedAccountID() throws SQLException{
		String query = "SELECT MAX(accountID) FROM [dbo].UserAccount ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		return value;
	}

	//Getting first inserted accountID 
	public int getFirstInsertedAccountID() throws SQLException{
		String query = "SELECT MIN(accountID) FROM [dbo].UserAccount ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		return value;
	}


	public boolean checkUserPresence(String username) throws SQLException {
		String query = "select count(*) from dbo.UserAccount where Username='"+username+"';";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		DBUtil.closeDBConnection();

		return (value>0)?true:false;
	}

	public void deleteUser(String username) throws SQLException {

		String deleteQuery ="DELETE FROM [dbo].[UserAccount] WHERE Username='"+username+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

	}


	//	################################################
	//	config table
	//	################################################

	public void updateWWWLSyncValue(String syncValue) throws SQLException{

		updateConfigTable("DefaultWindowLevelSyncMode",syncValue);


	}

	/**
	 * @author payal
	 * @param Percentile value that need to set
	 * @return: null
	 * Description: This method update the default slice position percentage
	 * @throws SQLException 
	 */
	//Update User details query
	public void updateDefaultSlicePosition(String value) throws SQLException{

		updateConfigTable("DefaultSlicePosition", value);

	}

	public String getWWWLDefaultSyncMode() throws SQLException{


		String value = getValueFromConfigSettings("DefaultWindowLevelSyncMode");
		return value;
	}


	/**
	 * @author payal
	 * @param null
	 * @return: default value that is present in database
	 * Description: This method select the default slice position percentage
	 * @throws SQLException 
	 */
	public String getDefaultSlicePosition() throws SQLException{

		String value = getValueFromConfigSettings("DefaultSlicePosition");
		return value;
	}

	/**
	 * @author payal
	 * @param null
	 * @return: default value that is present in database
	 * Description: This method select the default Sync Mode value
	 */
	public boolean getDefaultSyncMode(){

		String value="";
		boolean status = false ;
		try {

			value = getValueFromConfigSettings("DefaultSyncMode");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	/**
	 * @author payal
	 * @param Sync Mode value that need to set
	 * @return: null
	 * Description: This method update the default Sync Mode value
	 * @throws SQLException 
	 */
	//Update default sync mode
	public void updateDefaultSyncMode(String value) throws SQLException{

		updateConfigTable("DefaultSyncMode", value);

	}

	/*
	 * @author Vivek
	 * @param NA
	 * @return: value of DefaultViewboxSync in configSetting table
	 * Description: This method is used to get value of DefaultViewboxSync in configSetting table
	 */
	public String getDefaultViewboxSyncMode() throws SQLException{


		String value = getValueFromConfigSettings("DefaultViewboxSyncType");
		return value;
	}

	/*
	 * @author Vivek
	 * @param String 
	 * @return: NA
	 * Description: This method is used to set value of DefaultViewboxSync in configSetting table
	 */

	public void updateDefaultViewboxSyncMode(String value) throws SQLException{

		updateConfigTable("DefaultViewboxSyncType", value);

	}

	public void updateDevMode(String value) throws SQLException{

		updateConfigTable("DevMode", value);
	}

	public boolean getDevMode() throws SQLException{

		boolean value=false;
		String query="select value from [dbo].[ConfigSettings] where [Key]='DevMode'";
		LOGGER.info("Executing the query "+query);
		ResultSet result= DBUtil.getResultSet(query);

		while(result.next())
			value=result.getBoolean(1);

		return value;

	}

	public void updateConfigTable(String key, String value) throws SQLException{

		String query="update [dbo].[ConfigSettings] set Value='"+value+"' where [Key]='"+key+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public String getValueFromConfigSettings(String key) throws SQLException{

		String value="";
		String query="SELECT [Value] FROM [dbo].[ConfigSettings]  where [Key] = '"+key+"' ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString("Value");

		return value;


	}

	public void updateSendAcceptedFindingsDefault(String value) throws SQLException{

		updateConfigTable("SendAcceptedFindingsDefault", value);

	}

	public void updateSendRejectedFindingsDefault(String value) throws SQLException{
		updateConfigTable("SendRejectedFindingsDefault", value);

	}

	public int getHiddenColumnFlagValue(String key) throws SQLException{


		int value = 0;
		String query="SELECT [DefaultHidden] FROM [dbo].[displayTableColumns]  where [ColumnName] = '"+key+"' ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);


		while(result.next())
			value=result.getInt(1);
		return value;
	}
	public void updateSendPendingFindingsDefault(String value) throws SQLException{
		updateConfigTable("SendPendingFindingsDefault", value);

	}

	public int getDataLockExpireTimeDefault() throws SQLException {
		int value=0;
		String query="SELECT [Value] FROM [dbo].[ConfigSettings]  where [Key] = 'DataLockExpireTime'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);	   
		while(result.next())
			value=result.getInt(1);

		return value;
	}



	//	################################################
	//	GSPS level
	//	################################################

	public void deleteDrawnAnnotation(String username) throws SQLException{

		String deleteQuery ="DELETE FROM [dbo].[GSPSLevel] WHERE ContentCreatorName='"+username+"' AND ContentLabel='GSPS_"+username+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
		LOGGER.info("Deleted the annotation successfully !!");

	}

	public void deleteDrawnAnnotationForMachineData(String username) throws SQLException{

		String deleteQuery ="DELETE FROM [dbo].[GSPSLevel] WHERE ContentCreatorName='"+username+"' AND ContentDescription like '%_"+username+"_%'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
		LOGGER.info("Deleted the annotation successfully !!");

	}

	//Return the last row value from GSPSGraphicObject Table
	public int getLastRowNumFromGSPSGraphicObjectTable() throws SQLException{

		return getLastRowNum("GSPSGraphicObject", "GraphicObjectID");

	}	

	//Return the last row value from GSPSLevelTable
	public int getLastRowNumFromGSPSGraphicLevelTable() throws SQLException{

		return getLastRowNum("GSPSLevel", "GSPSLevelID");

	} 


	public int getLastRowNum(String table, String column) throws SQLException {

		int rowNum = 0 ;

		String runQuery = "SELECT MAX("+column+") FROM [dbo].["+table+"]";
		LOGGER.info("Executing the query "+runQuery);
		ResultSet result= DBUtil.getResultSet(runQuery);
		while (result.next()) 
			rowNum = result.getInt(1);
		return rowNum;
	}

	/**
	 * @author payal
	 * @param Row number from which we want to delete annotation
	 * @return: null
	 * Description: This method removes the added annotation in GSPS data
	 */
	public void deleteDrawnAnnotationFromGSPSData(int rowNumGraphicObject, int rowNumGraphicLevel, int batchRecodID) throws SQLException{
		String deleteQuery ="DELETE FROM [dbo].[GSPSGraphicObject] WHERE GraphicObjectID > '"+rowNumGraphicObject+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
		LOGGER.info("Deleted the annotation successfully !!");
		deleteQuery ="DELETE FROM [dbo].[GSPSLevel] WHERE GSPSLevelID > '"+rowNumGraphicLevel+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
		LOGGER.info("Deleted the annotation successfully !!");
		deleteQuery ="DELETE FROM [dbo].[Batch] WHERE BatchID > '"+batchRecodID+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
		LOGGER.info("Deleted the annotation successfully !!");

	}

	/**
	 * @author payal
	 * @param Added text in text annotation
	 * @return: null
	 * Description: This method removes the added text annotation in GSPS data
	 */
	public void deleteTextAnnotationFromGSPSData(String addedText) throws SQLException{


		String deleteQuery = "DELETE FROM [dbo].[GSPSTextObject] WHERE UnformattedText='"+addedText+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
	}

	public void deleteDrawnAnnotationOnMachineData(String username) throws SQLException{

		String deleteQuery ="DELETE FROM [dbo].[GSPSLevel] WHERE ContentCreatorName='"+username+"' AND ContentLabel='_"+username+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

	}


	public HashMap<String, Integer> getBatchIDAndBatchMachineID() {


		HashMap<String,Integer> batchMachines = new HashMap<String, Integer>();
		String query = "SELECT [BatchMachineID],[BatchID] FROM [dbo].[BatchMachines] WHERE [MachineID]=1";

		try {
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next()) {
				batchMachines.put(NSDBDatabaseConstants.BATCH_MACHCINEID,result.getInt(1));
				batchMachines.put(NSDBDatabaseConstants.BATCH_ID,result.getInt(2));
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return batchMachines;

	}

	public String getStudyInstanceIDFromBatchTable(int batchID) {

		String studyInstanceID = new String();
		String query = "SELECT [StudyInstanceUID] FROM [dbo].[Batch] WHERE [BatchID]="+batchID;

		try {
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next()) 
				studyInstanceID=result.getString(1);			

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return studyInstanceID;
	}


	public void deleteDataFromBatchBatchMachinesAndWiaResult(int batchID, int batchMachineID) throws SQLException {

		String query="DELETE from Batch WHERE [BatchID]="+batchID;
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
		query="DELETE from BatchMachines WHERE [MachineID]=1";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
		query="DELETE FROM [dbo].[WiaResultElements] WHERE [BatchMachineID]="+batchMachineID;
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public boolean verifyStudyIsWiaSource(String studyInstanceUID) {

		Boolean status = false;
		try {
			String query="select * from WiaSourceElements where StudyInstanceUID ='"+studyInstanceUID+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
			{
				status=true;
				break;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;


	}

	public void updateWIAResult(String studyInstanceUID) throws SQLException {
		String query="UPDATE [dbo].[StudyLevel] SET [WIACloudResult] = 0 WHERE [StudyInstanceUID]='"+studyInstanceUID+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public String getBatchMachineID(String patientName,String machineName) throws SQLException {


		String studyUID= getStudyInstanceUID(patientName);


		String query= "select BatchMachines.BatchMachineID from BatchMachines where BatchID IN (select BatchID from batch where StudyInstanceUID='"+studyUID+"') and MachineID = (select MachineID from Machines where Description='"+machineName+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		String value = null;
		while(result.next())
			value= result.getString(1);

		return value;

	}

	public int getBatchMachineID(int batchID) throws SQLException {


		String query= "select batchMachineID from BatchMachines where BatchID ="+batchID;
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value = 0;
		while(result.next())
			value= result.getInt(1);

		return value;

	}



	//to get BatchUID from Batch table
	public String getBatchUIDFromBatchTable(String patientName) throws SQLException {
		String studyUID= getStudyInstanceUID(patientName);
		String query= "select Batch.BatchUID from Batch where StudyInstanceUID ='"+studyUID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		String value = "";
		while(result.next()) {
			value= result.getString(1);

		}

		DBUtil.closeDBConnection();
		return value;
	}

	public List<String> getBatchUIDsFromBatchTable(String patientName) throws SQLException {
		String studyUID= getStudyInstanceUID(patientName);
		String query= "select Batch.BatchUID from Batch where StudyInstanceUID ='"+studyUID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		List<String> value = new ArrayList<String>();
		while(result.next()) {
			value.add(result.getString(1));

		}

		DBUtil.closeDBConnection();
		return value;
	}

	public List<String> getBatchIDsFromBatchTable(String patientName) throws SQLException {
		String studyUID= getStudyInstanceUID(patientName);
		String query= "select Batch.BatchID from Batch where StudyInstanceUID ='"+studyUID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		List<String> value = new ArrayList<String>();
		while(result.next()) {
			value.add(result.getString(1));			
		}

		DBUtil.closeDBConnection();
		return value;
	}

	public String getLastUpdateFromBatchMachineTable(String batchID) throws SQLException {

		String query= "select LastUpdate from BatchMachines where BatchID='"+batchID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		String value = "";
		while(result.next()) {
			value = result.getString(1);			
		}

		DBUtil.closeDBConnection();
		return value;
	}

	public void updateLastUpdateFromBatchMachineTable(String batchID, String newValue) throws SQLException {

		String query="UPDATE BatchMachines SET LastUpdate='"+newValue+"' where BatchID='"+batchID+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public int getCountOfRowsFromGSPSGraphicGroup(String patientID) throws SQLException{
		int GSPSLevelID= getGSPSLevelID(patientID);
		String query = "(select count(*) from dbo.GSPSGraphicGroup where GSPSLevelID="+GSPSLevelID+")";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		DBUtil.closeDBConnection();

		return value;
	}

	public void updateMachineTitle(String machineUID,String value) throws SQLException {
		String query="UPDATE [dbo].[Machines] SET [Description]="+ value+" "+"WHERE [MachineUID]='"+machineUID+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public int getGSPSLevelID(String patientID) throws SQLException {

		int value=0;
		String query = "select s.GSPSLevelID from GSPSLevel as s ,InstanceLevel as b , SeriesLevel as c,StudyLevel as d, patientlevel as e where e.PatientID='"+patientID+"' and d.PatientLevelID=e.PatientLevelID and d.StudyLevelID=c.StudyLevelID and c.Modality='PR' and b.SeriesLevelID=c.SeriesLevelID and s.InstanceLevelID=b.InstanceLevelID";
		LOGGER.info("Executing the query "+query);
		try {
			ResultSet result = DBUtil.getResultSet(query);
			while (result.next()) {
				value=result.getInt(1);
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return value;
	}


	public int getGraphicGroupID(String GroupName) throws SQLException {

		int graphicGroupID=0;
		String query="select GraphicGroupID from GSPSGraphicGroup where GraphicGroupLabel='"+GroupName+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			graphicGroupID=result.getInt(1);
		return graphicGroupID;
	}

	public int getCountOfGraphicObjectIDAndType(String patientID,String GroupName) throws SQLException{
		int GSPSLevelID= getGSPSLevelID(patientID);
		long GroupID=getGraphicGroupID(GroupName);
		int count=0;
		try {
			String query= "(select  GraphicObjectID,GraphicType from GSPSGraphicObject where GraphicGroupID ="+GroupID+" and gspsAnnotationID in (select gspsAnnotationID from GSPSAnnotation as a inner join GSPSLevel as s on s.GSPSLevelID = a.GSPSLevelID where s.GSPSLevelID ="+GSPSLevelID+"))"
					+ " UNION "
					+ "(select TextObjectID as GraphicObjectID , 'text' as GraphicType from GSPSTextObject where GraphicGroupID ="+GroupID+" and  gspsAnnotationID in (select gspsAnnotationID from GSPSAnnotation  as a inner join GSPSLevel as s on s.GSPSLevelID = a.GSPSLevelID where s.GSPSLevelID ="+GSPSLevelID +"));";
			LOGGER.info("Executing the query "+query);
			ResultSet result =	DBUtil.getResultSet(query);

			while (result.next()) 
				count++;
		}
		catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return count;
	}

	public List<String> getGraphicGroupLabel() throws SQLException {

		List<String> allGroupLabel=new ArrayList<String>();

		String query="select GraphicGroupLabel from GSPSGraphicGroup";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while(result.next())
			allGroupLabel.add(result.getString(1));

		return allGroupLabel;
	}

	public boolean verifyGroupLabelInDB(String GroupName) throws SQLException
	{
		boolean status=false;
		try{
			for(int i=0;i<=getGraphicGroupLabel().size();i++)
				if ((getGraphicGroupLabel().get(i)).equalsIgnoreCase(GroupName))
				{
					status=true;
					break;
				}}
		catch(Exception e)
		{
			printStackTrace(e.getMessage());
		}
		return status;

	}

	//	################################################
	//	Series level
	//	################################################

	public HashMap<String, String> getFrameReferenceUIDOfPatient(String patientName) {

		List<String> value=new ArrayList<String>();
		String query="select b.SeriesLevelID,b.FrameReferenceUID from StudyLevel as a , SeriesLevel as b , PatientLevel as c where c.PatientLevelID = a.PatientLevelID and a.StudyLevelID = b.StudyLevelID and a.PatientLevelID = (select PatientLevel.PatientLevelID from PatientLevel where PatientName='"+patientName+"');";
		HashMap<String, String> seriesAndUID = new HashMap<String,String>();

		try {
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next()) {
				value.add(result.getString(1));
				seriesAndUID.put(result.getString(1), result.getString(2));
			}

			DBUtil.closeDBConnection();

		} catch (SQLException e) {
			printStackTrace(e.getMessage());

		}
		return seriesAndUID;
	}

	public int updateFrameReferenceUIDOfPatient(String oldFrameUID, String newFrameUID) throws SQLException {

		int value=0;
		String query ="select SeriesLevelID from SeriesLevel where FrameReferenceUID='"+oldFrameUID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		while(result.next()) 
			value=result.getInt(1);

		DBUtil.closeDBConnection();
		query ="update SeriesLevel set FrameReferenceUID ='"+newFrameUID+"' where FrameReferenceUID='"+oldFrameUID+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

		return value;
	}

	/**
	 * @author Vivek
	 * @param Column name
	 * @return: if column name is present in table
	 * Description: This method is used to verify if Column is present in Table
	 */
	public Boolean verifyColumnExistInTable(String tableName, String columnName){

		Boolean status = false;
		String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='"+tableName+"'  ORDER BY ORDINAL_POSITION";
		try {
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
			{
				status=columnName.equalsIgnoreCase(result.getString("COLUMN_NAME"));
				if(status==true)
					break;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	/**
	 * @author payal
	 * @param Username
	 * @return: null
	 * Description: This method verifies the presence of added annotations for given user
	 */
	public boolean verifyPresenceOfDrawnAnnotation(String username) throws SQLException{
		boolean status = false ;
		String numberOfRows =  "SELECT COUNT(ContentLabel) FROM [dbo].[GSPSLevel] WHERE ContentCreatorName='"+username+"' AND ContentLabel='GSPS_"+username+"'";

		LOGGER.info("Executing the query "+numberOfRows);
		ResultSet result= DBUtil.getResultSet(numberOfRows);

		while (result.next()) {
			int id = result.getInt(1);
			if(id>0)
				status = true ;
		}
		return status;
	}

	public void  updateFrameReferenceUIDOfPatient(String oldFrameUID, String newFrameUID,int seriesID) throws SQLException {

		String query="update SeriesLevel set FrameReferenceUID ='"+oldFrameUID+"' where FrameReferenceUID='"+newFrameUID+"' and SeriesLevelID="+seriesID;
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public void  updateFrameReferenceUIDOfPatient(int seriesID, String newFrameUID) throws SQLException {

		String query = "update SeriesLevel set FrameReferenceUID ='"+newFrameUID+"' where SeriesLevelID="+seriesID;
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public void  updateFrameRefIDOfPatient(String patientName, String newFrameUID) throws SQLException {


		String query = "update SeriesLevel set FrameReferenceUID ='"+newFrameUID+"' from SeriesLevel inner join StudyLevel on SeriesLevel.StudyLevelId = StudyLevel.StudyLevelId WHERE StudyLevel.StudyInstanceUid ='"+getStudyInstanceUID(patientName)+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	/**
	 * @author vaishali
	 * @param selectorName 
	 * Description: Update the column using TRUE/FALSE value will result stack/unstack images 
	 */
	public void updateImageNumber(String imgNumber, String SOPInstanceUID){

		String updateQuery ="UPDATE [dbo].[InstanceLevel] SET ImageNumber='"+imgNumber+"' WHERE SOPInstanceUID ='"+SOPInstanceUID+"'";		
		try {
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery);			

		} catch (SQLException e) {

			printStackTrace(e.getMessage());
		}
	}

	/**
	 * @author vaishali
	 * @param patientName = patient name of which image numbers and SOPInstanceUID is to be fetched and whichSeries = series number of which image numbers and SOPInstanceUID is to be fetched
	 * whichSeries = series order present in SeriesLevel table is different from series order render on viewer. Here whichSeries refers to series order from database
	 * Description: Get list of image number and SOPInstanceUID of patient
	 */
	public HashMap<String,String> getAllImagesAndInstanceUIDOfPatient(String patientName, int whichSeries) throws SQLException {

		List<Integer> value=new ArrayList<Integer>();
		HashMap<String,String> imageWithUID= new HashMap<String,String>();
		String query1 = "select b.SeriesLevelID from SeriesLevel as b ,StudyLevel as c, PatientLevel as d where  c.PatientLevelID =d.PatientLevelID and c.StudyLevelID = b.StudyLevelID  and c.PatientLevelID = (select PatientLevel.PatientLevelID from PatientLevel where PatientName='"+patientName+"')";
		String query2 = "select a.ImageNumber,a.SOPInstanceUID from instancelevel as a where a.seriesLevelID=";

		try {
			LOGGER.info("Executing the query "+query1);
			ResultSet result = DBUtil.getResultSet(query1);
			while(result.next()) 
				value.add(result.getInt(1));

			LOGGER.info("Executing the query "+query2+value.get(whichSeries-1));
			result = DBUtil.getResultSet(query2+value.get(whichSeries-1));
			while(result.next()) 
				imageWithUID.put(result.getString(1),result.getString(2));
			DBUtil.closeDBConnection();  

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}  
		return imageWithUID; 
	}

	public HashMap<String,String> getAllImagesAndInstanceUIDOfPatient(String patientName, String SeriesDescription,int whichSeries) throws SQLException {

		List<Integer> value=new ArrayList<Integer>();
		HashMap<String,String> imageWithUID= new HashMap<String,String>();
		String query1 = "select b.SeriesLevelID from SeriesLevel as b ,StudyLevel as c, PatientLevel as d where b. c.PatientLevelID =d.PatientLevelID and c.StudyLevelID = b.StudyLevelID  and c.PatientLevelID = (select PatientLevel.PatientLevelID from PatientLevel where PatientName='"+patientName+"')";
		String query2 = "select a.ImageNumber,a.SOPInstanceUID from instancelevel as a where a.seriesLevelID=";

		try {
			LOGGER.info("Executing the query "+query1);
			ResultSet result = DBUtil.getResultSet(query1);
			while(result.next()) 
				value.add(result.getInt(1));

			LOGGER.info("Executing the query "+query2+value.get(whichSeries-1));
			result = DBUtil.getResultSet(query2+value.get(whichSeries-1));
			while(result.next()) 
				imageWithUID.put(result.getString(1),result.getString(2));
			DBUtil.closeDBConnection();  

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}  
		return imageWithUID; 
	}

	/**
	 * @author payala2
	 * @param Patientname, PatientExternalIDValue,AcquisitionDeviceValue,StudyAcquisitionSiteValue,ImageAcquisitionDateTimeValue,TargetValue,DetectorValue
	 * @return: update the database
	 * Description: This method update database for missing values
	 */	
	public void updateDBTextOverlayInFullMode(String PatientName,String PatientExternalIDValue,String AcquisitionDeviceValue,String StudyAcquisitionSiteValue,String ImageAcquisitionDateTimeValue,String TargetValue, String DetectorValue){

		String updateQueryPatientInfo ="UPDATE [dbo].[PatientLevel] SET [PatientExternalID]='"+PatientExternalIDValue+"' WHERE [PatientName]='"+PatientName+"'";		

		String updateQueryStudyInfo = "UPDATE [dbo].[StudyLevel] SET [AcquisitionDevice]='"+AcquisitionDeviceValue+"',[StudyAcquisitionSite]='"+StudyAcquisitionSiteValue+"' "
				+ "FROM [dbo].[PatientLevel] as a, [dbo].[StudyLevel] as b WHERE a.[PatientLevelID]=b.[PatientLevelID] and [PatientName]='"+PatientName+"'";	

		String updateQueryInstanceInfo = "UPDATE [dbo].[InstanceLevel] SET [ImageAcquisitionDateTime]='"+ImageAcquisitionDateTimeValue+"',[Target]='"+TargetValue+"',[Detector]='"+DetectorValue+"' "
				+ "FROM [dbo].[PatientLevel] as a, [dbo].[StudyLevel] as b, [dbo].[SeriesLevel] as c,[dbo].[InstanceLevel] as d "
				+ "WHERE a.[PatientLevelID]=b.[PatientLevelID] and b.[StudyLevelID] = c.[StudyLevelID] and c.[SeriesLevelID]=d.[SeriesLevelID] and "
				+ "a.[PatientName]='"+PatientName+"'";
		try {
			LOGGER.info("Executing the query "+updateQueryPatientInfo);
			DBUtil.runQuery(updateQueryPatientInfo);
			LOGGER.info("Executing the query "+updateQueryStudyInfo);
			DBUtil.runQuery(updateQueryStudyInfo);
			LOGGER.info("Executing the query "+updateQueryInstanceInfo);
			DBUtil.runQuery(updateQueryInstanceInfo);
			LOGGER.info("Updated the missing details for PatientInfo, StudyInfo and InstanceInfo successfully");

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}

	}


	/**
	 * @author santwanab
	 * @param selectorName 
	 * Description: Update the column using TRUE/FALSE value will result stack/unstack images 
	 */
	public void updateValueInSelectorTypeTable(String selectorName){
		String updateQuery ="UPDATE [dbo].[SelectorTypes] SET UsePresentation='"+selectorName+"' WHERE name ='choose-one-display-element'";		
		try {
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery);			

		} catch (SQLException e) {

			printStackTrace(e.getMessage());
		}


	}

	/**
	 * @author payala2
	 * @param Patient name and Study description
	 * @return: null
	 * Description: This method add the studies in mention patient data
	 */
	public void addStudiesInDB(String patientName, String studyDescription){
		String value="";
		String patientLevelID =  "SELECT PatientLevelID FROM [dbo].[PatientLevel] WHERE PatientName='"+patientName+"'";

		try {

			LOGGER.info("Executing the query "+patientLevelID);
			ResultSet result= DBUtil.getResultSet(patientLevelID);
			while(result.next())
				value=result.getString("PatientLevelID");

			for(int num=1;num<=20;num++){

				String insertQuery = "INSERT INTO [dbo].[StudyLevel] (PatientLevelID,StudyDescription,StudyInstanceUID) "
						+ "VALUES('"+value+"','"+studyDescription+"','"+num+"')";
				LOGGER.info("Executing the query "+insertQuery);
				DBUtil.runQuery(insertQuery);
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * @author payala2
	 * @param Patient name and Study description
	 * @return: null
	 * Description: This method removes the added studies in mention patient data
	 */
	public void removeAddedStudiesInDB(String patientName, String studyDescription){
		String value="";
		String patientLevelID =  "SELECT PatientLevelID FROM [dbo].[PatientLevel] WHERE PatientName='"+patientName+"'";

		try {
			LOGGER.info("Executing the query "+patientLevelID);
			ResultSet result= DBUtil.getResultSet(patientLevelID);
			while(result.next())
				value=result.getString("PatientLevelID");

			String deleteQuery = "DELETE FROM [dbo].[StudyLevel] WHERE StudyDescription='"+studyDescription+"' AND PatientLevelID='"+value+"' ";
			LOGGER.info("Executing the query "+deleteQuery);
			DBUtil.runQuery("IF (SELECT COUNT(*) FROM [dbo].[StudyLevel] WHERE StudyDescription='"+studyDescription+"') > 0 	"+deleteQuery);			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void updateMultiFrameTimeInDB(String PatientName,int multiframeRecommendedFrameTime){
		String updateMultiFrameQuery = "UPDATE [dbo].[InstanceLevel] SET [MultiframeRecommendedFrameTime]="+multiframeRecommendedFrameTime+" "
				+ "FROM [dbo].[PatientLevel] as a, [dbo].[StudyLevel] as b, [dbo].[SeriesLevel] as c,[dbo].[InstanceLevel] as d "
				+ "WHERE a.[PatientLevelID]=b.[PatientLevelID] and b.[StudyLevelID] = c.[StudyLevelID] and c.[SeriesLevelID]=d.[SeriesLevelID] and "
				+ "a.[PatientName]='"+PatientName+"'";
		try {

			LOGGER.info("Executing the query "+updateMultiFrameQuery);
			DBUtil.runQuery(updateMultiFrameQuery);
			LOGGER.info("Updated the multi frame recommended frame time successfully");

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
	}

	// get bestfit layout from DB
	public int getBestFitLayoutForStudy(String studyUID) throws SQLException {

		String query = "select a.BatchMachineID from Batch as b , BatchMachines as a where StudyInstanceUID='"+studyUID+"' and a.BatchID = b.BatchID";
		String batchMachineID="";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		while(result.next())
			batchMachineID=result.getString(1);

		return DBUtil.getResultFromFunction(studyUID,batchMachineID);


	}

	public String getStudyLevelID(String patientName) throws SQLException {

		String value="";

		ResultSet result = DBUtil.getResultSet("select StudyLevelID from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID");

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getLockExpireTime(String patientName) throws SQLException {

		String value = "";

		ResultSet result = DBUtil.getResultSet("select LockExpireTime from StudyLockStatus where StudyLevelID="+getStudyLevelID(patientName));

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getStudyInstanceUID(String patientName) throws SQLException {

		String value="";
		String query="select StudyInstanceUID from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getAccessionNumber(String patientName) throws SQLException {

		String value="";
		String query="select AccessionNumber from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public List<String> getStudyInstances(String patientName) throws SQLException {

		List<String> value= new ArrayList<String>();
		String query="select StudyInstanceUID from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value.add(result.getString(1));

		return value;
	}

	public String getStudyInstanceUIDUsingPatientID(String patientID) throws SQLException {

		String value="";
		String query="select StudyInstanceUID from StudyLevel as a, patientlevel as b where b.PatientID='"+patientID+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getStudyDescription(String patientName) throws SQLException {

		String value="";
		String query="select StudyDescription from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getModailitiesInStudy(String patientName) throws SQLException {

		String value="";
		String query="select ModalitiesInStudy from StudyLevel as a, patientlevel as b where b.PatientName='"+patientName+"' and b.PatientLevelID= a.PatientLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}


	public List<String> getSeriesInstanceUID(String patientName) throws SQLException {


		String studyUID= getStudyInstanceUID(patientName);

		List<String> value=new ArrayList<String>();
		String query="select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID+"' and b.StudyLevelID = a.StudyLevelID";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value.add(result.getString(1));

		return value;
	}

	public String getSeriesInstanceUID(String patientName,String seriesDes) throws SQLException {


		String studyUID= getStudyInstanceUID(patientName);

		String value="";
		String query="select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID+"' and b.StudyLevelID = a.StudyLevelID and LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(a.SeriesDescription,CHAR(32),'()'),')(',''),'()',CHAR(32))))='"+seriesDes+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}
	public String getSeriesInstanceID(String patientName,String seriesnumber) throws SQLException {


		//String studyUID= getStudyInstanceUID(patientName);

		String value="";
		String query="select a.seriesinstanceuid from serieslevel a where a.studylevelid=(select b.StudyLevelid from StudyLevel b where b.PatientLevelID=(select c.patientlevelid from PatientLevel c where c.PatientName='"+patientName+"')) and a.SeriesNumber="+seriesnumber+" and a.modality='ct'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}
	/*
	 * @author Vivek
	 * @param String  
	 * @return: A hash map with instance level ID and corresponding error message while importing
	 * Description: This method is used to get error message while importing unsupported data 
	 */
	public HashMap<String, Object> getErrorMessageWithIntance(String patientName) throws SQLException{

		HashMap<String,Object> messageWithInstance= new HashMap<String,Object>();
		String query = "SELECT e.InstanceLevelID,e.ErrorMessage FROM [dbo].[RejectedInstance] as e,[dbo].[InstanceLevel] as d,[dbo].[PatientLevel] as a,[dbo].[StudyLevel] as b,[dbo].[SeriesLevel] as c WHERE a.[PatientLevelID]=b.[PatientLevelID] and b.[StudyLevelID] = c.[StudyLevelID] and c.[SeriesLevelID]=d.[SeriesLevelID]  and e.[InstanceLevelID] = d.[InstanceLevelID] and a.[PatientName]='"+patientName+"';";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		while(result.next())
		{
			messageWithInstance.put(result.getString("InstanceLevelID"), result.getString("ErrorMessage"));
		}
		return messageWithInstance;
	}

	//	################################################
	// Log Table
	//	################################################
	/* @author Vivek
	 * @param service Name 
	 * @return: if logging Service is present in serviceID column of log table
	 * Description: This method is used to verify if logging service is present in log table
	 */
	public Boolean verifyLoggingServiceInLogTable(String serviceName){

		Boolean status = false;
		try {

			String query="SELECT [ServiceID] FROM  [NSDB].[dbo].[Log]";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
			{
				status=serviceName.equalsIgnoreCase(result.getString("ServiceID"));
				if(status==true)
					break;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	/*
	 * @author Vivek
	 * @param NA
	 * @return: No of record in Log table
	 * Description: This method is used to get no of record in log table
	 */
	public int getNumberRowsInLogTable(){

		int numberOfRows=0;
		try {
			String query="SELECT COUNT (ServiceID) AS COUNTLOG from  [NSDB].[dbo].[Log];";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while (result.next()) {
				numberOfRows= result.getInt(1);
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return numberOfRows;
	}


	/**
	 * @author santwanab
	 * Description: This method truncate log table
	 * @throws SQLException 
	 */
	public void truncateLogTable() throws SQLException {
		String truncateTableQuery = "Truncate table log";
		LOGGER.info("Executing the query "+truncateTableQuery);
		DBUtil.runQuery(truncateTableQuery);
	}

	/**
	 * @author santwanab
	 * Description: This method will return the 'SendUserFeedbackToEnvoyResponse is OK'/accept/reject message from log table
	 * @throws SQLException 
	 */
	public String getFullMessageFromLogContainsString(String messageValue) throws SQLException {

		String query="SELECT Message from Log where Message like '"+messageValue+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString("Message");
		}  	
		return message ;
	}
	
	
	public List<String> getLogsofEvent(String event) throws SQLException {

		String query="select message from log where WorkFlowID = '"+event+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		List<String> message = new ArrayList<String>();
		while (rs.next()) {
			message.add(rs.getString("Message"));
		}  	
		return message ;
	}
	
	
	public List<String> getLogsofEventContainsMsg(String message) throws SQLException {

		String query="select ServiceID, WorkFlowID, message from log where message like '%"+message+"%'";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		List<String> values = new ArrayList<String>();
		while (rs.next()) {
			
			values.add(rs.getString(1));
			values.add(rs.getString(2));
			values.add(rs.getString(3));
		}  	
		return values ;
	}

	public List<String> getLogsMessage() throws SQLException {

		String query="select message from log";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		List<String> message = new ArrayList<String>();
		while (rs.next()) {
			message.add(rs.getString("Message"));
		}  	
		return message ;
	}
	
	public boolean verifyLogForErrorMeassge(String message) throws SQLException{
		boolean value=false;
		String query="SELECT COUNT(*) FROM [dbo].[Log] WHERE [WorkFlowID]='Rejected Instance' AND [ServiceID] ='AddInstance' AND [Message] like '"+message+"';";
		LOGGER.info("Executing the query "+query);
		ResultSet result= DBUtil.getResultSet(query);

		while(result.next())
			if(result.getInt(1)>0)
				value=true;
		return value;

	}

	public List<String> getLogsCount(String message) throws SQLException{
		List<String> value=new ArrayList<String>();
		String query="SELECT [Message] FROM [dbo].[Log] WHERE [Message] like '%"+message+"%';";
		LOGGER.info("Executing the query "+query);
		ResultSet result= DBUtil.getResultSet(query);

		while(result.next())
			value.add(result.getString(1));

		return value;

	}


	//	################################################
	//	User Feedback 
	//	################################################

	public String getUserFeedback(String studyInstanceID) throws SQLException {

		String query = "select feedback from UserFeedback where StudyInstanceUID='"+studyInstanceID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString(1);

		}  	
		return message ;

	}

	public String getUserFeedback(String patientName,String seriesDesc,String machineName) throws SQLException {

		String batchMachineID =getBatchMachineID(patientName, machineName);		
		String studyID = getStudyInstanceUID(patientName);

		String query = "select feedback from UserFeedback where StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"') and BatchMachineID="+batchMachineID ;
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString(1);
			if (rs.wasNull()) 
				return null;

		}  	
		return message ;
	}

	public String getUserFeedback(String patientName,String seriesDesc,String machineName,String username) throws SQLException {

		String batchMachineID =getBatchMachineID(patientName, machineName);		
		String studyID = getStudyInstanceUID(patientName);

		String query = "select feedback from UserFeedback where username = '"+username+"' and StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"') and BatchMachineID="+batchMachineID ;
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString(1);
			if (rs.wasNull()) 
				return null;

		}  	
		return message ;
	}


	public String getUserFeedback(String patientName,String seriesDesc) throws SQLException {

		String studyID = getStudyInstanceUID(patientName);
		String query = "select feedback from UserFeedback where StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString(1);
			if (rs.wasNull()) 
				return null;

		}  	
		return message ;
	}

	public String getUserFeedbackUsingPatientID(String patientID,String seriesDesc) throws SQLException {

		String studyID = getStudyInstanceUIDUsingPatientID(patientID);
		String query = "select feedback from UserFeedback where StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet rs = DBUtil.getResultSet(query);
		String message = "";
		while (rs.next()) {
			message = rs.getString(1);
			if (rs.wasNull()) 
				return null;

		}  	
		return message ;
	}


	public void setUserFeedback(String patientName,String seriesDesc,String feedback) throws SQLException {

		String studyID = getStudyInstanceUID(patientName);
		String query = "update [dbo].[UserFeedback] set [Feedback] = '"+feedback+"' where StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public void updateSendAcceptedFindings(String username,String value) throws SQLException{

		Boolean Value=Boolean.valueOf(value);
		String query="update [dbo].[UserFeedbackPreferences] set [SendAcceptedFindings]='"+Value+"' where [UserAccountID]=(SELECT [UserAccountID] from [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);


	}

	public void updateSendRejectedFindings(String username,String value) throws SQLException{

		Boolean Value=Boolean.valueOf(value);
		String query="update [dbo].[UserFeedbackPreferences] set [SendRejectedFindings]='"+Value+"' where [UserAccountID]=(SELECT [UserAccountID] from [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);


	}

	public void updateSendPendingFindings(String username,String value) throws SQLException{


		Boolean Value=Boolean.valueOf(value);
		String query="update [dbo].[UserFeedbackPreferences] set [SendPendingFindings]='"+Value+"'"
				+ " where [UserAccountID]=(SELECT [UserAccountID] from [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);


	} 

	public boolean getSendAcceptedFindings(String Username1){

		String value="";
		boolean status = false ;
		try {
			String query="SELECT [SendAcceptedFindings] FROM [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+Username1+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
				value=result.getString("SendAcceptedFindings");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public boolean getSendRejectedFindings(String Username1){

		String value="";
		boolean status = false ;
		try {
			String query="SELECT [SendRejectedFindings] FROM [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where  a.[AccountID]=b.[UserAccountID] and a.[Username]='"+Username1+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("SendRejectedFindings");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;



	}

	public boolean getSendPendingFindings(String Username1) throws SQLException{

		String value="";
		boolean status = false ;
		try {
			String query="SELECT [SendPendingFindings] FROM [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+Username1+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("SendPendingFindings");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}
		}finally {
			DBUtil.closeDBConnection();
		}
		return status;
	}

	public boolean getCollapseFindingsInOutputPanel(String username) throws SQLException{

		boolean value=false;
		try {
			String query="SELECT [CollapseFindingsInOutputPanel] FROM [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getBoolean("CollapseFindingsInOutputPanel");

		}finally {
			DBUtil.closeDBConnection();
		}
		return value;
	}

	public boolean getSendAcceptedFindingsDefault() throws SQLException{

		String value="";
		boolean status = false ;
		try {
			String query="SELECT [Value] FROM [dbo].[ConfigSettings]  where [Key] = 'SendAcceptedFindingsDefault' ;";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("Value");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}
		} finally {
			DBUtil.closeDBConnection();
		}
		return status;
	}

	public boolean getSendRejectedFindingsDefault() throws SQLException{

		String value="";
		boolean status = false ;
		try {

			String query="SELECT [Value] FROM [dbo].[ConfigSettings]  where [Key] = 'SendRejectedFindingsDefault' ;";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("Value");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}
		} finally {
			DBUtil.closeDBConnection();
		}
		return status;
	}

	public boolean getSendPendingFindingsDefault() throws SQLException{

		String value="";
		boolean status = false ;
		try {

			String query="SELECT [Value] FROM [dbo].[ConfigSettings]  where [Key] = 'SendPendingFindingsDefault' ;";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("Value");
			if(value.equalsIgnoreCase("true")){
				status = true ;
			}else if(value.equalsIgnoreCase("false")|| value.isEmpty()){
				status = false ;
			}

		} finally {
			DBUtil.closeDBConnection();
		}
		return status;
	}

	public void updateShowPendingResultsDialog(String username,String value) throws SQLException{

		String query ="update [dbo].[UserFeedbackPreferences] set [ShowPendingResultsDialog]='"+value+"' where [UserAccountID]=(SELECT [UserAccountID] from [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"')";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  

	}

	public void deleteUserFeedbackPreference() throws SQLException{

		int accountID=getFirstInsertedAccountID();
		String query ="delete from [dbo].[UserFeedbackPreferences] where [UserAccountID]="+accountID;
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  

	}


	public void updateCollapseFindingsInOutputPanel(String username,String value) throws SQLException{

		Boolean Value=Boolean.valueOf(value);
		String query="update [dbo].[UserFeedbackPreferences] set [CollapseFindingsInOutputPanel]='"+Value+"' where [UserAccountID]=(SELECT [UserAccountID] from [dbo].[UserAccount] as a, [dbo].[UserFeedbackPreferences] as b  where a.[AccountID]=b.[UserAccountID] and a.[Username]='"+username+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);


	}

	//	################################################
	// Event Map
	//	################################################
	//verify group present in DB
	public boolean verifyingGroupinDB(String domain, String group){

		boolean flag = false;
		String query = "select * from dbo.ActiveDirectoryDomains where Domain = '" + domain +"'";		
		LOGGER.info("Executing the query "+query);
		try {

			ResultSet result =  DBUtil.getResultSet(query);
			String sqlResult;
			if(result.next()){
				sqlResult = result.getString("groups");
				if(containsIgnoreCase(sqlResult, group))
					flag = true;
			}

		}catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return flag;
	}

	public String getMapedMousebutton(String buttonId) throws SQLException{
		String value="";
		try {

			String query="SELECT [MouseButton] FROM [dbo].[mouseEventTypeConfiguration] where [ButtonId] = '"+ buttonId+"';";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);

			while(result.next())
				value=result.getString("MouseButton");
		} finally {
			DBUtil.closeDBConnection();
		}
		return value;
	}

	public boolean deleteActionInEventMap(String buttonId) throws SQLException{
		boolean value= false;
		String deleteQuery ="DELETE FROM [dbo].[Eventmap] WHERE [ButtonId] = '"+ buttonId+"';";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

		ResultSet result = DBUtil.getResultSet("SELECT * FROM [dbo].[Eventmap] where [ButtonId] = '"+ buttonId+"';");
		if(!result.next())
			value = true;

		return value;
	}

	public boolean truncateActionInEventMap() throws SQLException{
		boolean value= false;
		String deleteQuery ="TRUNCATE TABLE [dbo].[Eventmap];";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

		deleteQuery ="SELECT * FROM [dbo].[Eventmap]";
		LOGGER.info("Executing the query "+deleteQuery);
		ResultSet result = DBUtil.getResultSet(deleteQuery);
		if(!result.next())
			value = true;

		return value;
	}

	public void addActionInEventMap(String buttonId, String action) throws SQLException{

		String insertQuery ="INSERT INTO [dbo].[Eventmap] (Action, ButtonId) VALUES('"+action+"','"+buttonId+"')";
		LOGGER.info("Executing the query "+insertQuery);
		DBUtil.runQuery(insertQuery);
	}

	public Boolean verifyActionInEventMap(String buttonId, String action) throws SQLException{

		Boolean status = false;
		try {
			String query="SELECT * FROM [dbo].[Eventmap] WHERE ButtonId ='"+buttonId+"'";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
			{
				status=action.equalsIgnoreCase(result.getString("Action"));
				if(status==true)
					break;
			}
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return status;
	}

	public HashMap<Integer, String> getDataFromMouseEventType() throws SQLException{

		HashMap<Integer, String> data = new HashMap<Integer,String>();	

		try {
			String query="SELECT [ButtonId],[MouseButton] FROM [NSDB].[dbo].[MouseEventTypeConfiguration];";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while (result.next()) {
				data.put(result.getInt(1),result.getString(2));
			}

		} finally {
			DBUtil.closeDBConnection();
		}
		return data;
	}

	public HashMap<Integer, String> getDataFromEventMap() throws SQLException{

		HashMap<Integer, String> data = new HashMap<Integer,String>();	

		try {
			String query="SELECT [Action],[ButtonId] FROM [NSDB].[dbo].[EventMap];";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while (result.next()) {
				data.put(result.getInt(2),result.getString(1));
			}

		}finally {
			DBUtil.closeDBConnection();
		}
		return data;
	}

	public List<String> getTableColumnName(String tableName) {

		String query = "SELECT COLUMN_NAME  FROM INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='"+tableName+"';";
		LOGGER.info("Executing the query "+query);
		List<String> columnsName = new ArrayList<String>();

		try {
			ResultSet result = DBUtil.getResultSet(query);
			while (result.next()) {
				columnsName.add(result.getString(1));
			}
			DBUtil.closeDBConnection();
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return columnsName;
	}

	public void updateButtonID(String key,String value) throws SQLException {

		String query = "update [dbo].[EventMap] set ButtonID='"+value+"' where [Action]='"+key+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}


	//	################################################
	//	RT 
	//	################################################	

	public void deleteRTafterPerformingAnyOperation(String username) throws SQLException {
		String query = "Delete  FROM [NSDB].[dbo].[RtStruct] where DerivingUser = '"+username+"';";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	//	################################################
	//	Study Lock 
	//	################################################	

	public void deleteAllStudyLockStatus() throws SQLException {
		String query = "delete from dbo.StudyLockStatus";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public void deleteUserSetLayout() throws SQLException {

		String query = "delete from dbo.MachineUserLayouts";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	//to get count from table StudyLockStatus 
	public int getStudyLockStatusCount(String patientName) throws SQLException {

		String query = "(select count(*) from [dbo].[StudyLockStatus] where StudyLevelID="+getStudyLevelID(patientName)+")";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		DBUtil.closeDBConnection();

		return value;
	}


	//	################################################
	//	Client configs
	//	################################################

	//Adding Config key in the ClientConfiguration table if not added else updating existing config key with negative values 
	public void updateClientConfiguration(String configKey) throws SQLException{

		int value = 0;
		String str = "SELECT COUNT(*) FROM [dbo].[ClientConfiguration] WHERE ConfigKey='"+configKey+"'";
		ResultSet result= DBUtil.getResultSet(str);
		String query = "";

		while(result.next()) 
			value=result.getInt(1);

		if(value!= 0)
			query ="update [dbo].[ClientConfiguration] set ConfigStringJSON =  '{\"imageMap\":[{\"key\":\"logo-terarecon.svg\",\"value\":\"terarecon.svg\"},{\"key\":\"logo-northstar-header.svg\",\"value\":\"northstar.svg\"}],\"theme\":\"light\",\"radialMenu\":false,\"acceptRejectMenu\":false,\"sendToPACS\":false,\"logout\":false,\"headerVisible\":false}'  where ConfigKey = '"+configKey+"'";				
		else
			query = "INSERT INTO [dbo].[ClientConfiguration] (ConfigKey, ConfigStringJSON) VALUES ('"+configKey+"', '{\"imageMap\":[{\"key\":\"logo-terarecon.svg\",\"value\":\"terarecon.svg\"},{\"key\":\"logo-northstar-header.svg\",\"value\":\"northstar.svg\"}],\"theme\":\"dark\",\"radialMenu\":true,\"acceptRejectMenu\":true,\"sendToPACS\":true,\"logout\":true,\"headerVisible\":true}')";

		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	//Deleting config key from ClientConfiguration table
	public void deleteClientConfiguration(String configKey) throws SQLException{

		String deleteQuery = "DELETE FROM [dbo].[ClientConfiguration] WHERE ConfigKey ='"+configKey+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery("IF (SELECT COUNT(*) FROM [dbo].[ClientConfiguration] WHERE ConfigKey ='"+configKey+"') > 0" + deleteQuery);

	}


	/*
	 * Need to Fix this method - Megha
	 */

	public String getSopInstanceUID(String SeriesDescriptions) throws SQLException {

		String query= "Select a.SOPInstanceUID from InstanceLevel as a where imagenumber=48 and a.SeriesLevelID=(select SeriesLevelID from SeriesLevel where SeriesDescription='"+SeriesDescriptions+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,"NSDB");
		String value = null;
		while(result.next())
			value= result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	/*
	 * Need to Fix this method - Megha
	 */
	public String getMimeType(String SeriesDescriptions) throws SQLException {

		String query= "Select a.MIMEType from InstanceLevel as a where imagenumber=48 and a.SeriesLevelID=(select SeriesLevelID from SeriesLevel where LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(SeriesDescription,CHAR(32),'()'),')(',''),'()',CHAR(32))))='"+SeriesDescriptions+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value= result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}


	public void truncateTable(String dbName,String tableName) throws SQLException {
		String truncateTableQuery = "Truncate table "+tableName;
		LOGGER.info("Executing the query "+truncateTableQuery);
		DBUtil.runQuery(truncateTableQuery,dbName);
	}

	public String getSopClassUID(String SeriesDescriptions,int imageNumber,String modality) throws SQLException {

		String query= "Select a.SOPClassUID from InstanceLevel as a where imagenumber="+imageNumber+" " +"and a.SeriesLevelID=(select SeriesLevelID from SeriesLevel where SeriesDescription='"+SeriesDescriptions+"' and Modality='"+modality+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query ,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value= result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	//to get machineUID  from Machine table
	public String getMachineUIDFromMachineTable(String machineName) throws SQLException {
		String query= "select MachineUID from Machines where Description='"+machineName+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value=result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	public String getMachineInfo(String machineName) throws SQLException {
		String query= "select info from Machines where Description='"+machineName+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value=result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	public List<String> getAllMachinesName() throws SQLException {
		String query= "select Name from Machines";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		List<String> values = new ArrayList<String>();
		while(result.next())
			values.add(result.getString(1));
		DBUtil.closeDBConnection();
		return values;

	}

	public List<String> getMachineName(String machineDesc) throws SQLException {

		String query= "select Name from Machines where description = '"+machineDesc+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		List<String> values = new ArrayList<String>();
		while(result.next())
			values.add(result.getString(1));
		DBUtil.closeDBConnection();
		return values;

	}

	public String getMachineUIDFromMachineTable(int id) throws SQLException {
		
		String query= "select MachineUID from Machines where MachineId='"+id+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value=result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}
	

	public String getSeriesInstanceUID(String patientName,String seriesDes,String modality) throws SQLException {


		String studyUID= getStudyInstanceUID(patientName);

		String value="";
		String query="select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID+"' and b.StudyLevelID = a.StudyLevelID and LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(a.SeriesDescription,CHAR(32),'()'),')(',''),'()',CHAR(32))))='"+seriesDes+"'and a.Modality='"+modality+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public String getSopInstanceUID(String SeriesDescriptions,int imageNumber,String modality) throws SQLException {

		String query= "Select a.SOPInstanceUID from InstanceLevel as a where imagenumber="+imageNumber+" " +"and a.SeriesLevelID=(select SeriesLevelID from SeriesLevel where SeriesDescription='"+SeriesDescriptions+"'and Modality='"+modality+"')";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value= result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	public String getMimeType(String SeriesDescriptions, int imageNumber,String modality) throws SQLException {

		String baseQuery="Select a.MIMEType from InstanceLevel as a where a.SeriesLevelID=(select SeriesLevelID from SeriesLevel where SeriesDescription='"+SeriesDescriptions+"' and Modality='"+modality+"')";
		String query;
		if(imageNumber>0)
		{
			query= baseQuery+ " and imagenumber="+imageNumber;
		}
		else
		{
			query= baseQuery;
		}

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value= result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	public void setUserFeedback(String patientName,String seriesDesc,String feedback,String Modality) throws SQLException {

		String studyID = getStudyInstanceUID(patientName);
		String query = "update [dbo].[UserFeedback] set [Feedback] = '"+feedback+"' where StudyInstanceUID='"+studyID+"' and SeriesInstanceUID IN (select a.SeriesInstanceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyID+"' and b.StudyLevelID = a.StudyLevelID and a.SeriesDescription='"+seriesDesc+"' and Modality='"+Modality+"')";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	//Enable sopclassUID for different result type(CAD/SR/BasicTextSR)

	public void setSOPClassUID(String description,boolean value) throws SQLException {

		String query = "update [dbo].[SOPClassUID] set [Enabled] = '"+value+"' where Description='"+description+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}
	public int getRowsCount(String tableName) throws SQLException {

		String query = "select count(*) from dbo."+tableName;
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		DBUtil.closeDBConnection();

		return value;
	}


	public void deleteDBEntry(String table, String column, int value) throws SQLException {


		String deleteQuery ="DELETE FROM [dbo].["+table+"] WHERE "+column+" > '"+value+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);
	}

	public void addGroupsInDB(String patientID, int GraphicGroupID, String GraphicGroupLabel) throws SQLException{

		int GSPSLevelID=getGSPSLevelID(patientID);
		String insertQuery ="INSERT INTO [dbo].[GSPSGraphicGroup] (GSPSLevelID,GraphicGroupID,GraphicGroupLabel) VALUES('"+GSPSLevelID+"','"+GraphicGroupID+"','"+GraphicGroupLabel+"')";
		try {
			LOGGER.info("Executing the query "+insertQuery);
			DBUtil.runQuery(insertQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void updateDerivedIDInDB(String GraphicGroupLabel, String value) throws SQLException{

		String updateQuery ="UPDATE [dbo].[GSPSGraphicGroup] SET [DerivedId]='"+value+"' WHERE [GraphicGroupLabel]='"+GraphicGroupLabel+"'";		


		LOGGER.info("Executing the query "+updateQuery);
		DBUtil.runQuery(updateQuery);



	}

	public void updateGroupIDInGSPSGraphicObject(String patientID,int value) throws SQLException{

		int GSPSLevelID=getGSPSLevelID(patientID);
		String updateQuery ="UPDATE [dbo].[GSPSGraphicObject] SET [GraphicGroupID]='"+value+"' WHERE [GSPSAnnotationID] IN (Select GSPSAnnotationID from GSPSAnnotation where GSPSLevelID='"+GSPSLevelID+"') and GraphicGroupID='-1'";	

		try {
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void updateGroupIDInGSPSTextObject(String patientID ,int value) throws SQLException{

		int GSPSLevelID=getGSPSLevelID(patientID);
		String updateQuery ="UPDATE [dbo].[GSPSTextObject] SET [GraphicGroupID]='"+value+"' WHERE [GSPSAnnotationID] IN (Select GSPSAnnotationID from GSPSAnnotation where GSPSLevelID='"+GSPSLevelID+"') and GraphicGroupID='-1'";	

		try {
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	//get Max and Min value of LUT bar from database

	public String getMaxOrMinValueOfLUTBar(String minOrMaxValue, String patientName,String patientID, String resultDescription) throws SQLException{

		String runQuery = "";
		if(!patientName.isEmpty())		
			runQuery = "select PatientLevelID from patientLevel where PatientName ='"+patientName+"'";
		else
			runQuery = "select PatientLevelID from PatientLevel where PatientID ='"+patientID+"'";

		int patientLevelID =0;
		LOGGER.info("Executing the query "+runQuery);
		ResultSet result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			patientLevelID = result.getInt(1);
		}

		runQuery = "select i.InstanceLevelID from InstanceLevel as i, StudyLevel as st, SeriesLevel as se,PatientLevel as pa " + 
				"where pa.PatientLevelID = "+patientLevelID+" and st.PatientLevelID = pa.PatientLevelID and " + 
				"st.StudyLevelID = se.StudyLevelID and se.SeriesDescription ='"+resultDescription+"' and " + 
				"i.SeriesLevelID = se.SeriesLevelID";

		int instanceLevelID =0;
		LOGGER.info("Executing the query "+runQuery);
		result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			instanceLevelID = result.getInt(1);
		}

		runQuery = "SELECT "+minOrMaxValue+" from PMAPStorage as sto, PMAPRealWorldValueMapping as pr where sto.InstanceLevelId = "+instanceLevelID+" and " + 
				"pr.PmapRealWorldValueMappingId = sto.PmapStorageId";

		String value = "";
		LOGGER.info("Executing the query "+runQuery);
		result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			value = result.getString(1);
		}
		return value;
	}


	public String getValue(String query) throws SQLException {

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		String value = "";
		while(result.next())
			value=result.getString(1);

		return value;

	}

	public void updateValue(String query) throws SQLException {

		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}
	//Fet all unique modalities from Modality column of SeriesLevel table of NSDB
	public List<String> getAllModalities(){
		List<String> modalities = new ArrayList<String>();

		try {
			String query="select Modality from SeriesLevel";
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
				modalities.add(result.getString("Modality"));

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return modalities.stream().distinct().collect(Collectors.toList()); //return unique modalities 
	}

	public void deleteCloneFromSeriesLevelForCAD(String cloneResult) throws SQLException {

		String query = "delete SeriesLevel where SeriesDescription like '"+cloneResult+"%'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public String getIssuerOfPatientID(String patientID) throws SQLException {

		String value="";
		String query="Select IssuerOfPatientID from PatientLevel where PatientID='"+patientID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getString(1);

		return value;
	}

	public void updateAccessionNoInStudyTable(String patientName, String accessionNo){
		String value="";
		String patientLevelID =  "SELECT PatientLevelID FROM [dbo].[PatientLevel] WHERE PatientName='"+patientName+"'";

		try {
			LOGGER.info("Executing the query "+patientLevelID);
			ResultSet result= DBUtil.getResultSet(patientLevelID);
			while(result.next())
				value=result.getString(NSDBDatabaseConstants.PATIENT_LEVEL_ID);

			String updateQuery = "update StudyLevel set AccessionNumber='"+accessionNo +"' where  PatientLevelID='"+value+"' ";
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updatePatientID(String patientName, String patientID){
		try {
			String updateQuery =  "UPDATE PatientLevel set PatientID='"+patientID+"' where PatientName='"+patientName+"'";
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteCopyFilterPreference() throws SQLException{

		String query ="delete from [dbo].[CopyFilter]";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  

	}

	public int getUserIDFromUserAccount(String username) throws SQLException{
		String query = "SELECT AccountID FROM [dbo].UserAccount where username='"+username+"' ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		return value;
	}

	public void SetOrRemoveType2InMachineTable(boolean RemoveOrSet, String MachineNAME) throws SQLException{


		String var ="";

		if(RemoveOrSet)
			var=ViewerPageConstants.Type2;

		String query="update [dbo].[Machines] set Interactive='"+var+"' where MachineUID='"+MachineNAME+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);





	}

	public HashMap<String,String> getSeriesLevelID(String patientName, int whichSeries) throws SQLException {

		List<Integer> value=new ArrayList<Integer>();
		HashMap<String,String> imageWithUID= new HashMap<String,String>();
		String query1 = "select b.SeriesLevelID from SeriesLevel as b ,StudyLevel as c, PatientLevel as d where  c.PatientLevelID =d.PatientLevelID and c.StudyLevelID = b.StudyLevelID  and c.PatientLevelID = (select PatientLevel.PatientLevelID from PatientLevel where PatientName='"+patientName+"')";
		String query2 = "select a.ImageNumber,a.SOPInstanceUID from instancelevel as a where a.seriesLevelID=";

		try {
			LOGGER.info("Executing the query "+query1);
			ResultSet result = DBUtil.getResultSet(query1);
			while(result.next()) 
				value.add(result.getInt(1));

			LOGGER.info("Executing the query "+query2+value.get(whichSeries-1));
			result = DBUtil.getResultSet(query2+value.get(whichSeries-1));
			while(result.next()) 
				imageWithUID.put(result.getString(1),result.getString(2));
			DBUtil.closeDBConnection();  

		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}  
		return imageWithUID; 
	}

	public void updateImagePosition(double imageposition, String instanceID) {
		String updateQuery =  "UPDATE [dbo].[InstanceLevel] SET [ImagepositionX] ="+imageposition+" where [NSDB].[dbo].[InstanceLevel].SOPInstanceUID = '"+instanceID+"' ";
		try {

			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<List<String>> getSeriesAndInstanceInfoForMultiphase(String patientName,String studyDesc) throws SQLException{


		List<String> studyUID= getStudyInstances(patientName);

		//Getting all the series for study
		String query="select a.SeriesLevelID , a.SeriesDescription from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID.get(0)+"' and b.StudyLevelID = a.StudyLevelID and b.StudyDescription='"+studyDesc+"' and a.SeriesOrigin=0";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		List<Integer> value=new ArrayList<Integer>();	
		List<String> seriesDesc = new ArrayList<String>();
		while(result.next()) {
			value.add(result.getInt(1));
			seriesDesc.add(result.getString(2));
		}


		List<List<String>> seriesGrouped = new ArrayList<List<String>>();		
		for(int i=0;i<value.size();i++) {			
			List<String> seriesList = new ArrayList<String>();
			List<String> firstResult = getInstanceDetails(value.get(i));
			List<Double> fPos = getImagePos(value.get(i));


			for(int j=0;j<value.size();j++) {

				if(i!=j) {										


					List<String> secondResult = getInstanceDetails(value.get(j));			

					if(firstResult.equals(secondResult) && (!secondResult.get(0).equals("0"))) {

						List<Double> secondPos = getImagePos(value.get(j));


						for(int images =0 ;images<secondPos.size();images++)
						{
							if(fPos.get(0).equals(secondPos.get(0)) && fPos.get(1).equals(secondPos.get(1))&& fPos.get(2).equals(secondPos.get(2))) {
								seriesList.add(seriesDesc.get(j));
								value.remove(j);
								seriesDesc.remove(j);
								j=j-1;
								break;
							}

						}


					}

				}
			}
			if(seriesList.size()>0) {
				seriesList.add(0, seriesDesc.get(i));
				seriesGrouped.add(seriesList);		
				value.remove(i);
				seriesDesc.remove(i);
				i=i-1;

			}



		}

		return seriesGrouped;




	}

	public List<List<String>> getGroupedForMultiphaseWhenImageposNotEqual(String patientName,String studyDesc) throws SQLException{


		List<String> studyUID= getStudyInstances(patientName);

		//Getting all the series for study
		String query="select a.SeriesLevelID , a.SeriesDescription, a.FrameReferenceUID from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID.get(0)+"' and b.StudyLevelID = a.StudyLevelID and b.StudyDescription='"+studyDesc+"' and a.SeriesOrigin=0";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		List<Integer> value=new ArrayList<Integer>();	
		List<String> seriesDesc = new ArrayList<String>();
		List<String> frameRefUID = new ArrayList<String>();
		while(result.next()) {
			value.add(result.getInt(1));
			seriesDesc.add(result.getString(2));
			frameRefUID.add(result.getString(3));
		}


		List<List<String>> seriesGrouped = new ArrayList<List<String>>();		
		for(int i=0;i<value.size();i++) {			

			List<String> seriesList = new ArrayList<String>();
			List<Double> fPos = getImagePos(value.get(i));

			for(int j=0;j<value.size();j++) {

				if(i!=j) {	

					if(frameRefUID.get(i).equals(frameRefUID.get(j))) {

						List<Double> secondPos = getImagePos(value.get(j));						
						if(!fPos.equals(secondPos)) {
							List<List<String>> firstResult = getInstanceDetails(value.get(i),
									"ImageType", 
									"SopClassUid",
									"ImagerPixelSpacingCol",
									"ImagerPixelSpacingRow",
									"PixelSpacingCol",
									"PixelSpacingRow",
									"SamplesPerPixel",
									"ImageOrientationPatientXx",
									"ImageOrientationPatientXy",
									"ImageOrientationPatientXz",
									"ImageOrientationPatientYx",
									"ImageOrientationPatientYy",
									"ImageOrientationPatientYz");
							List<List<String>> secondResult = getInstanceDetails(value.get(j),
									"ImageType", 
									"SopClassUid",
									"ImagerPixelSpacingCol",
									"ImagerPixelSpacingRow",
									"PixelSpacingCol",
									"PixelSpacingRow",
									"SamplesPerPixel",
									"ImageOrientationPatientXx",
									"ImageOrientationPatientXy",
									"ImageOrientationPatientXz",
									"ImageOrientationPatientYx",
									"ImageOrientationPatientYy",
									"ImageOrientationPatientYz");

							if(firstResult.equals(secondResult) && (!secondResult.get(0).equals("0"))) {

								seriesList.add(seriesDesc.get(j));
								value.remove(j);
								seriesDesc.remove(j);
								j=j-1;

							}

						}


					}

				}}

			if(seriesList.size()>0) {
				seriesList.add(0, seriesDesc.get(i));
				seriesGrouped.add(seriesList);		
				value.remove(i);
				seriesDesc.remove(i);
				i=i-1;

			}



		}

		return seriesGrouped;




	}

	public List<String> getResultsForPatient(String patientName,String studyDesc) throws SQLException{


		List<String> studyUID= getStudyInstances(patientName);

		//Getting all the results for study
		String query="select a.SeriesDescription from SeriesLevel as a , StudyLevel as b where b.StudyInstanceUID ='"+studyUID.get(0)+"' and b.StudyLevelID = a.StudyLevelID and b.StudyDescription='"+studyDesc+"' and a.SeriesOrigin=1";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		List<String> seriesDesc = new ArrayList<String>();
		while(result.next()) {
			seriesDesc.add(result.getString(1));
		}

		return seriesDesc;
	}

	private List<String> getInstanceDetails(int series) throws SQLException{

		String query = "select count(ImageNumber), SOPClassUID from InstanceLevel  where SeriesLevelID="+series+" group by SOPClassUID";
		LOGGER.info("Executing the query "+query);
		ResultSet result1 = DBUtil.getResultSet(query);
		List<String> instanceDetails = new ArrayList<String>();
		while(result1.next()) {
			instanceDetails.add(new Integer(result1.getInt(1)).toString());
			instanceDetails.add(result1.getString(2));
			break;
		}
		return instanceDetails;


	}

	private List<List<String>> getInstanceDetails(int series , String... columns) throws SQLException{

		String query = " from InstanceLevel  where SeriesLevelID="+series;

		String column ="";
		for(int i=0;i<columns.length;i++) {

			column = column + columns[i];
			if(i<columns.length-1)
				column= column+", ";
		}
		query = "select "+column + query;

		LOGGER.info("Executing the query "+query);
		ResultSet result1 = DBUtil.getResultSet(query);
		List<String> instanceDetails = new ArrayList<String>();
		List<List<String>> allDetails = new ArrayList<List<String>>();
		while(result1.next()) {
			for(int i=1;i<=columns.length;i++) 
				instanceDetails.add(result1.getString(i));

			allDetails.add(instanceDetails);

		}
		return allDetails;

	}

	private List<Double> getImagePos(Integer series) throws SQLException{

		String query = "select ImagePositionX, ImagepositionY, ImagepositionZ from InstanceLevel where SeriesLevelID ="+series;
		LOGGER.info("Executing the query "+query);
		ResultSet result1 = DBUtil.getResultSet(query);
		List<Double> instanceDetails = new ArrayList<Double>();
		while(result1.next()) {
			instanceDetails.add(result1.getDouble(1));
			instanceDetails.add(result1.getDouble(2));
			instanceDetails.add(result1.getDouble(3));
			break;
		}
		return instanceDetails;

	}

	public int getNumberOfSlice(String seriesDesc) throws SQLException {

		String query = "select SeriesLevelID from SeriesLevel where SeriesDescription='"+seriesDesc+"'";
		ResultSet result = DBUtil.getResultSet(query);
		int series= 0;
		while(result.next()) {
			series= result.getInt(1);

		}


		query = "select max(ImageNumber) from InstanceLevel  where SeriesLevelID="+series+" group by SOPClassUID";
		LOGGER.info("Executing the query "+query);
		result = DBUtil.getResultSet(query);
		int totalNumberOfImages = 0;
		while(result.next()) {
			totalNumberOfImages = result.getInt(1);

		}
		return totalNumberOfImages;

	}


	public String getGSPSLevelInfo(String contentDescription, String column) throws SQLException {

		String value="";
		String query = "select "+column+" from GSPSLevel where ContentDescription='"+contentDescription+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while (result.next()) {
			value=result.getString(1);
		}

		return value;
	}


	public void updateAcquisitionDate(String patientName,String date){


		try {
			String studyInstanceUID=getStudyInstanceUID(patientName);
			String updateQuery = "UPDATE dbo.StudyLevel set StudyDate='"+date+"' where StudyInstanceUID='"+studyInstanceUID+"'";
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getAcquisitionDate(String patientName){

		String value="";
		try {
			String studyInstanceUID=getStudyInstanceUID(patientName);
			String selectQuery = "Select StudyDate from dbo.StudyLevel where StudyInstanceUID='"+studyInstanceUID+"'";
			LOGGER.info("Executing the query "+selectQuery);
			ResultSet result = DBUtil.getResultSet(selectQuery);
			while (result.next()) {
				value=result.getString(1);
			}

			return value;

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;

	}


	//to get machineUID  from Machine table
	public String getMachineIDFromMachineTable(String machineName) throws SQLException {
		String query= "select MachineID from Machines where Description='"+machineName+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query,TEST_PROPERTIES.get("dbName"));
		String value = null;
		while(result.next())
			value=result.getString(1);

		DBUtil.closeDBConnection();
		return value;

	}

	public boolean verifyUserIsAdminUser(String username) throws SQLException

	{  
		boolean status=false;

		String query = "select IsAdmin from UserAccount where Username='"+username+"'";
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next()) {
			status= result.getBoolean(1);
		}
		return status;

	}

	public void deletePatient(String patientName) throws SQLException {

		String deleteQuery ="DELETE FROM [dbo].[PatientLevel] WHERE PatientName='"+patientName+"'";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

	}
	
	
	public void deletePatientData(String patientName) throws SQLException {

		
		List<String> studyIDs = getStudyInstances(patientName);
		for(int i =0 ;i<studyIDs.size();i++) {				
			
			String deleteQuery ="DELETE FROM [dbo].[Batch] WHERE StudyInstanceUID='"+studyIDs.get(i)+"'";
			LOGGER.info("Executing the query "+deleteQuery);
			DBUtil.runQuery(deleteQuery);
			
			
		}
		
		deletePatient(patientName);
		
		
			
	}
	
	public void deleteMachine(String machineName) throws SQLException {
		
		if(!machineName.equalsIgnoreCase("object-importer")) {
				String deleteQuery ="DELETE FROM [dbo].[Machines] WHERE MachineUID='"+machineName+"'";
				LOGGER.info("Executing the query "+deleteQuery);
				DBUtil.runQuery(deleteQuery);
			}
			
			
	}
	
	
		

	public void updateContextInfoEnable(String value) throws SQLException{

		updateConfigTable(NSDBDatabaseConstants.CONTEXT_INFO_ENABLED, value);

	}

	public String getContextInfo(String patientName) throws SQLException {

		String value="";
		String query = "select ContextInfo from PatientLevel where PatientName='"+patientName+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while (result.next()) {
			value=result.getString(1);
		}

		return value;
	}

	public void updateSecretKey(String username,String mfaKey) throws SQLException {

		String updateMfaKey ="update UserAccount set MfaSecretKey='"+mfaKey+"' where username='"+username+"'";
		LOGGER.info("Executing the query "+updateMfaKey);
		DBUtil.runQuery(updateMfaKey);

	}

	public void convertUserIntoAdmin(String username) throws SQLException {

		String adminQuery ="update UserAccount set isAdmin='"+NSDBDatabaseConstants.SYNC_ON+"' where username='"+username+"'";
		LOGGER.info("Executing the query "+adminQuery);
		DBUtil.runQuery(adminQuery);

	}

	public String getMFAColumnValue(String username) throws SQLException{

		return getValue("Select "+NSDBDatabaseConstants.MFA_COLUMN_NAME+" from "+NSDBDatabaseConstants.USER_ACCOUNT_TABLE+" where Username='"+username+"'");

	}

	public HashMap<Integer,String> getAllBatchInfo() {

		HashMap<Integer,String> batchInfo = new HashMap<Integer,String>();
		String query = "SELECT [BatchID],[BatchUID] FROM [dbo].[Batch]";

		try {
			LOGGER.info("Executing the query "+query);
			ResultSet result = DBUtil.getResultSet(query);
			while(result.next())
				batchInfo.put(result.getInt(1),result.getString(2));
		} catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return batchInfo;

	}

	public List<String> getSourceFromWiaTable(int batchMachineID) throws SQLException {


		List<String> value= new ArrayList<String>();
		String query = "select name from WiaSourceElements where BatchMachineID="+batchMachineID;
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while (result.next()) {
			value.add(result.getString(1));
		}

		return value;


	}

	public List<String> getResultsFromWiaTable(int batchMachineID) throws SQLException {


		List<String> value= new ArrayList<String>();
		String query = "select name from WiaResultElements where BatchMachineID="+batchMachineID;
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while (result.next()) {
			value.add(result.getString(1));
		}

		return value;


	}

	public void cleanDB() throws SQLException {

		String query ="delete from PatientLevel";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  

		query ="delete from Batch";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	 

		query ="delete from Machines where MachineID <> 1;";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	

		query ="delete from Log";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  

		query ="delete from UserFeedback;";
		LOGGER.info("Executing the query "+query);	
		DBUtil.runQuery(query);	  
	}

	public String getPMAPStorageId(String patientName,String patientID, String resultDescription) throws SQLException{

		String runQuery = "";
		if(!patientName.isEmpty())		
			runQuery = "select PatientLevelID from patientLevel where PatientName ='"+patientName+"'";
		else
			runQuery = "select PatientLevelID from PatientLevel where PatientID ='"+patientID+"'";

		int patientLevelID =0;
		LOGGER.info("Executing the query "+runQuery);
		ResultSet result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			patientLevelID = result.getInt(1);
		}

		runQuery = "select i.InstanceLevelID from InstanceLevel as i, StudyLevel as st, SeriesLevel as se,PatientLevel as pa " + 
				"where pa.PatientLevelID = "+patientLevelID+" and st.PatientLevelID = pa.PatientLevelID and " + 
				"st.StudyLevelID = se.StudyLevelID and se.SeriesDescription ='"+resultDescription+"' and " + 
				"i.SeriesLevelID = se.SeriesLevelID";

		int instanceLevelID =0;
		LOGGER.info("Executing the query "+runQuery);
		result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			instanceLevelID = result.getInt(1);
		}

		runQuery = "SELECT PmapStorageId from PMAPStorage  where InstanceLevelId = "+instanceLevelID;

		String value = "";
		LOGGER.info("Executing the query "+runQuery);
		result= DBUtil.getResultSet(runQuery);
		while(result.next()){
			value = result.getString(1);
		}
		return value;
	}

	public String getValueFromPMAPStorage(String patientName,String patientID, String resultDescription,String columnName) throws SQLException {

		String storageID = getPMAPStorageId(patientName,patientID,resultDescription);
		String value="";
		String query = "select "+columnName+" from PMAPStorage where PmapStorageId='"+storageID+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);
		while (result.next()) {
			value=result.getString(1);
		}

		return value;
	}

	public void updateContextInfo(String patientName,String value) throws SQLException {

		String query = "update PatientLevel set ContextInfo='"+value+"' where  PatientName='"+patientName+"'";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);
	}

	public int getSeriesLevelID(String seriesDescription) throws SQLException {


		int value=0;
		String query="select SeriesLevelID from SeriesLevel where SeriesDescription like '%"+seriesDescription+"%'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		while(result.next())
			value=result.getInt(1);

		return value;

	}

	public List<Integer> getInstanceLevelID(String seriesDescription) throws SQLException {


		int value=getSeriesLevelID(seriesDescription);
		String query="select InstanceLevelID from InstanceLevel where SeriesLevelID="+value;
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		List<Integer> instanceID = new ArrayList<Integer>();
		while(result.next())
			instanceID.add(result.getInt(1));

		return instanceID;

	}
	public int getRenderingModeForUser(String username,String columnName) throws SQLException{
		String query = "SELECT "+columnName+" FROM [dbo].UserAccount where username='"+username+"' ;";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		int value=0;
		while(result.next())
			value=result.getInt(1);
		return value;
	}

	public void updateRenderingModeForUser(String modeSelection, int value, String username)throws SQLException{

		String query="update UserAccount set "+modeSelection+"="+value+" where username='"+username+"';";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public void updateWiaCloudResultInStudyTable(String studyDescription,int value) throws SQLException
	{
		String query="update StudyLevel set WIACloudResult="+value+" where StudyDescription='"+studyDescription+"';";
		LOGGER.info("Executing the query "+query);
		DBUtil.runQuery(query);

	}

	public Date getSeriesOrResultDateFromSeriesLevel(String seriesOrResult) throws SQLException {
		Date value = null;
		String query="SELECT SeriesDate FROM SeriesLevel  where SeriesDescription = '"+seriesOrResult+"'";
		LOGGER.info("Executing the query "+query);

		ResultSet result = DBUtil.getResultSet(query);	   
		while(result.next())
			value=result.getDate(1);

		//DateFormat targetFormat = new SimpleDateFormat(ViewerPageConstants.OUTPUT_PANEL_DATEFORMAT);
		//String currentDate=targetFormat.format(value);

		return value;
	}

	public int VerifyEntryInRecentlyViewedPatientTable() throws InterruptedException, SQLException{
		int value=0;

		String query= "SELECT * FROM dbo.RecentlyViewedPatient";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		while(result.next())
			value=result.getInt(1);


		return value;
	}

	public boolean VerifyStudyLevlIdEntryInViewedPatientTable(String patientName) throws InterruptedException, SQLException{
		boolean value=false;

		String query= "select StudyLevelId from dbo.RecentlyViewedPatient where StudyLevelID=(select StudyLevelId from dbo.StudyLevel where PatientLevelID=(select PatientLevelID from dbo.PatientLevel where PatientName='"+patientName+"'))";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);
		while(result.next())
			value=result.getBoolean(1);

		return value;
	}




	public void deletePatientFromRecentlyViewedPatientTable(String patientName) throws SQLException {

		String deleteQuery ="delete from dbo.RecentlyViewedPatient where StudyLevelID=(select StudyLevelId from dbo.StudyLevel where PatientLevelID=(select PatientLevelID from dbo.PatientLevel where PatientName='"+patientName+"'))";
		LOGGER.info("Executing the query "+deleteQuery);
		DBUtil.runQuery(deleteQuery);

	}


	public List<Integer> getPMAPInstance(String seriesDes) throws SQLException {

		int levelID = getSeriesLevelID(seriesDes);
		String query="select InstanceLevelID from Instancelevel where SeriesLevelID ="+levelID+"  and SOPInstanceUID in (select ReferencedSopInstanceUid from PMAPPerFrameData)";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		List<Integer> value = new ArrayList<Integer>();
		while(result.next())
			value.add(result.getInt(1));

		return value;

	}

	//update patient sex field in Patient Level table.
	public void updatePatientSex(String patientName, String patientSex){
		try {
			String updateQuery =  "UPDATE PatientLevel set PatientSex='"+patientSex+"' where PatientName='"+patientName+"'";
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getInstanceInfo(String columnName,String sopInstanceUID) throws SQLException {


		String query="select "+columnName+" from InstanceLevel where SOPInstanceUID='"+sopInstanceUID+"'";
		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		String value = "";
		while(result.next())
			value = result.getString(1);

		return value;

	}

	public String getInstanceUID(String patientName, String seriesDes, String column, String columnVal) throws SQLException {


		String query="select SOPInstanceUID from instanceLevel where "+column+" = '"+columnVal+"' and SeriesLevelID in("
				+"select SeriesLevel.SeriesLevelID from PatientLevel , StudyLevel , SeriesLevel where PatientLevel.PatientName ='"+patientName+"' and StudyLevel.PatientLevelID =PatientLevel.PatientLevelID "
				+"and SeriesLevel.StudyLevelID = StudyLevel.StudyLevelID and LTRIM(RTRIM(REPLACE(REPLACE(REPLACE(SeriesDescription,CHAR(32),'()'),')(',''),'()',CHAR(32)))) ='"+seriesDes+"')";

		LOGGER.info("Executing the query "+query);
		ResultSet result = DBUtil.getResultSet(query);

		String value = "";
		while(result.next())
			value = result.getString(1);

		return value;

	}

	public HashMap<String,String> getKeyboardShortcutsFromDB(){

		String query = "select keys, command from dbo.ApplicationShortcuts";		
		HashMap<String,String> keyValue= new HashMap<String,String>();

		try {

			ResultSet result =  DBUtil.getResultSet(query);
			while(result.next())				
				keyValue.put(result.getString("keys"), result.getString("command"));

		}catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return keyValue;

	}

	public String getPatientID(String patientName) {

		String query = "select PatientID from PatientLevel where PatientName ='"+patientName+"'";		
		String value="";

		try {

			ResultSet result =  DBUtil.getResultSet(query);
			while(result.next())				
				value = result.getString(1);

		}catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return value;
	}

	public HashMap<String,Integer> getAnnotationNames(){


		String query = "select AnnotationId, DisplayName from Annotation";		
		HashMap<String,Integer> keyValue= new HashMap<String,Integer>();

		try {

			ResultSet result =  DBUtil.getResultSet(query);
			while(result.next())				
				keyValue.put(result.getString(2), result.getInt(1));

		}catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return keyValue;


	}

	public List<Integer> getAnnotationsEnabled(int accountID){


		String query = "select AnnotationId from UserAnnotation where AccountId="+accountID;		
		List<Integer> annotationID= new ArrayList<Integer>();

		try {

			ResultSet result =  DBUtil.getResultSet(query);
			while(result.next())				
				annotationID.add(result.getInt(1));

		}catch (SQLException e) {
			printStackTrace(e.getMessage());
		}
		return annotationID;


	}

	public List<String> getWWWLOverlayOptionsFromDB(String modality){
		List<String> value = new ArrayList<String>();	

		try {
			ResultSet result = DBUtil.getResultSet("SELECT [Name] from [dbo].[WindowingPreset] WHERE [Modality]='"+modality+"'");
			while(result.next()){
				value.add(result.getString(PatientXMLConstants.PATIENT_NAME));
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printStackTrace(e.getMessage());
		}
		return value;
	}

	public void updateBackgroundColor(String userName, String bgColor){
		try {
			String updateQuery =  "UPDATE UserAccount set ViewerBackgroundColor='"+bgColor+"' where Username='"+userName+"'";

			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateIssuerOfPatientID(String patientName, String issuerID){
		try {
			String updateQuery =  "UPDATE PatientLevel set IssuerOfPatientID='"+issuerID+"' where PatientName='"+patientName+"'";
			LOGGER.info("Executing the query "+updateQuery);
			DBUtil.runQuery(updateQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addPluginInfo(String displayName, String path,String exe){
		try {
			
			String insertQuery ="INSERT INTO [dbo].[Apps] VALUES('"+displayName+"','"+path+"','"+exe+"')";
			LOGGER.info("Executing the query "+insertQuery);
			DBUtil.runQuery(insertQuery,TEST_PROPERTIES.get("dbName"));
			DBUtil.closeDBConnection();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
