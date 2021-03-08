package Java;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Repository;

@Repository
public class BAEstimationDao {

	static Logger log = LoggerFactory.getLogger(BAEstimationDao.class);

	private NamedParameterJdbcTemplate baEstimationJdbcTemplate;

	/* 
	 * SQL command to insert all information from the "starter" section of the Service Request object
	 * into the database.
	*/
	private static final String INSERT_STARTER_REQ = 
			"INSERT INTO BAESTIMATION.STARTERREQ "		
			+ "(SR_NO, PROC_NAME, CUST_NAME, DEST_COL, DEST_PROC_PNT1, DEST_PROC_PNT2, LINE_NO, "
			+ "DEPT_NAME, REQ_TYPE, DEST_PHOTO1, DEST_PHOTO2, DEST_PHOTO3, TICKET_NO, MODIFIED, CREATED) "

			+ "VALUES " 
			+ "(:srNo, :procName, :custName, :destCol, :destProcPnt1, :destProcPnt2, :lineNo, "
			+ ":deptName, :reqType, :destPhoto1, :destPhoto2, :destPhoto3, :ticketNo, :modified, :created)";
	
	/* 
	 * SQL command to insert all information from the "relocation" section of the Service Request object
	 * into the database.
	*/
	private static final String INSERT_RELOCATION_REQ = 
			"INSERT INTO BAESTIMATION.RELOCATIONREQ "
			+ "(SR_NO, RELO_COL_LOC, RELO_PROC_PT1, RELO_PROC_PT2, SIM_PROC_PT, CUR_STA_TYP, NODE_ID_NUM, "
			+ "CNTLR_QTY, RELO_PHOTO1, RELO_PHOTO2, RELO_PHOTO3, TICKET_NO, MODIFIED, CREATED) "

			+ "VALUES " 
			+ "(:srNo, :reloColLoc, :reloProcPt1, :reloProcPt2, :simProcPt, :curStaTyp, :nodeIdNum, "
			+ ":cntlrQty, :reloPhoto1, :reloPhoto2, :reloPhoto3, :ticketNo, :modified, :created)";

	/* 
	 * SQL command to insert all information from the "new" section of the Service Request object
	 * into the database.
	*/
	private static final String INSERT_NEW_QTY = 
			"INSERT INTO BAESTIMATION.NEWQTY "
			+ "(SR_NO, NEW_DEV_SEL, TICKET_NO, MODIFIED, CREATED) "
			+ "VALUES " 
			+ "(:srNo, :newDevSel, :ticketNo, :modified, :created)";
	
	/* 
	 * SQL command to insert all information from the starter section of the Service Request object
	 * into the database.
	*/
	private static final String INSERT_INVENTORY_QTY = 
			"INSERT INTO BAESTIMATION.INVENTORYQTY "
			+ "(SR_NO, INV_DEV_SEL, TICKET_NO, MODIFIED, CREATED) "
			+ "VALUES " 
			+ "(:srNo, :invDevSel, :ticketNo, :modified, :created)";
	
	/* 
	 * SQL command to retrieve all Service Request tuples from the database.
	*/
	private static final String SELECT_SR_FORM =
			"SELECT " 
			+"SR.SR_NO, SR.PROC_NAME, SR.CUST_NAME, SR.DEST_COL, SR.DEST_PROC_PNT1, SR.DEST_PROC_PNT2, "
			+"SR.LINE_NO, SR.DEPT_NAME, SR.REQ_TYPE, SR.DEST_PHOTO1, SR.DEST_PHOTO2, SR.DEST_PHOTO3, SR.MODIFIED, SR.CREATED, "
			+"NQ.NEW_DEV_SEL, "
			+"RR.RELO_COL_LOC, RR.RELO_PROC_PT1, RR.RELO_PROC_PT2, RR.SIM_PROC_PT, RR.CUR_STA_TYP, RR.NODE_ID_NUM, "
			+"RR.CNTLR_QTY, RR.RELO_PHOTO1, RR.RELO_PHOTO2, RR.RELO_PHOTO3, "
			+"IQ.INV_DEV_SEL "

			+"FROM "
			+"BAESTIMATION.STARTERREQ SR LEFT JOIN BAESTIMATION.NEWQTY NQ ON SR.SR_NO=NQ.SR_NO AND SR.TICKET_NO=NQ.TICKET_NO "
			+"LEFT JOIN BAESTIMATION.RELOCATIONREQ RR ON SR.SR_NO=RR.SR_NO AND SR.TICKET_NO=RR.TICKET_NO "
			+"LEFT JOIN BAESTIMATION.INVENTORYQTY IQ ON SR.SR_NO=IQ.SR_NO AND SR.TICKET_NO=IQ.TICKET_NO";
	
	/* 
	 * SQL command to retrieve a Service Request tuple by a provided SR_NO.
	*/
	private static final String SELECT_BY_SR_NO =
			"SELECT " 
			+"SR.SR_NO, SR.PROC_NAME, SR.CUST_NAME, SR.DEST_COL, SR.DEST_PROC_PNT1, SR.DEST_PROC_PNT2, "
			+"SR.LINE_NO, SR.DEPT_NAME, SR.REQ_TYPE, SR.DEST_PHOTO1, SR.DEST_PHOTO2, SR.DEST_PHOTO3, SR.MODIFIED, SR.CREATED, "
			
			+"NQ.NEW_DEV_SEL, "
			
			+"RR.RELO_COL_LOC, RR.RELO_PROC_PT1, RR.RELO_PROC_PT2, RR.SIM_PROC_PT, RR.CUR_STA_TYP, RR.NODE_ID_NUM, "
			+"RR.CNTLR_QTY, RR.RELO_PHOTO1, RR.RELO_PHOTO2, RR.RELO_PHOTO3, "
			
			+"IQ.INV_DEV_SEL "

			+"FROM "
			+"BAESTIMATION.STARTERREQ SR LEFT JOIN BAESTIMATION.NEWQTY NQ ON SR.SR_NO=NQ.SR_NO AND SR.TICKET_NO=NQ.TICKET_NO "
			+"LEFT JOIN BAESTIMATION.RELOCATIONREQ RR ON SR.SR_NO=RR.SR_NO AND SR.TICKET_NO=RR.TICKET_NO "
			+"LEFT JOIN BAESTIMATION.INVENTORYQTY IQ ON SR.SR_NO=IQ.SR_NO  AND SR.TICKET_NO=IQ.TICKET_NO "
			
			+"WHERE "
			+"SR.SR_NO = :srNo";
	
	/*
	 * SQL command to select all package settings from the PKGSETTINGS table.
	 */
	private static final String SEL_ALL_PKG_SETTINGS = 
			"SELECT VAR_NAMES_AND_VALS FROM BAESTIMATION.PKGSETTINGS WHERE PKG_NAME = :pkgName";
	
	/*
	 * SQL command to select all package names from the database.
	 */
	private static final String SEL_ALL_PKG_NAMES = 
			"SELECT PKG_NAME FROM BAESTIMATION.PKGSETTINGS";
	
	/*
	 * SQL command to select all new device names, max quantities, etc... from the database.
	 */
	private static final String SEL_ALL_NEW_DEV_TUPLES = 
			"SELECT * FROM BAESTIMATION.NEWDEVSETTINGS";
	
	/*
	 * SQL Command to select all inventory device names and max quantities from the database.
	 */
	private static final String SEL_ALL_INV_DEV_TUPLES = 
			"SELECT * FROM BAESTIMATION.INVDEVSETTINGS";
	
	/*
	 * SQL command to select all new form group ID's from the database
	 */		
	private static final String SEL_NEW_GRP_IDS = 
			"SELECT DISTINCT GRP_ID FROM BAESTIMATION.NEWDEVSETTINGS";
	
	/*
	 * SQL command to select all new form business requirement ID's from the database
	 */		
	private static final String SEL_NEW_BUS_REQ_GRP_IDS = 
			"SELECT DISTINCT BUS_REQ_GRP_ID FROM BAESTIMATION.NEWDEVSETTINGS";
	
	/*
	 * SQL command to select all inventory form group ID's from the database
	 */	
	private static final String SEL_INV_GRP_IDS = 
			"SELECT DISTINCT GRP_ID FROM BAESTIMATION.INVDEVSETTINGS";
	
	/*
	 * SQL command to select all inventory form business requirement group ID's from the database
	 */		
	private static final String SEL_INV_BUS_REQ_GRP_IDS = 
			"SELECT DISTINCT BUS_REQ_GRP_ID FROM BAESTIMATION.INVDEVSETTINGS";
	
	/*
	 * SQL command to select all new form device variable names from the database
	 */
	private static final String SEL_NEW_DEV_VAR_NAMES = 
			"SELECT DISTINCT NEW_VAR_NAME FROM BAESTIMATION.NEWDEVSETTINGS";
	
	/*
	 * SQL command to select a new form device variable name from the database
	 */
	private static final String GET_NEW_DEV_SETTING = 
			"SELECT DISTINCT GRP_ID, BUS_REQ_GRP_ID FROM BAESTIMATION.NEWDEVSETTINGS WHERE NEW_DEV_NAME=:currentNewDevName";
	
	/*
	 * SQL command to select a new form device variable name from the database
	 */
	private static final String GET_INV_DEV_SETTING = 
			"SELECT DISTINCT GRP_ID, BUS_REQ_GRP_ID FROM BAESTIMATION.INVDEVSETTINGS WHERE INV_DEV_NAME=:currentInvDevName";
	
	/*
	 * SQL command to Delete a New Device Setting.
	 */
	private static final String DEL_NEW_DEV_SETTING = 
			"DELETE FROM BAESTIMATION.NEWDEVSETTINGS WHERE NEW_DEV_NAME=:delNewDevName";
	
	/*
	 * SQL command to Delete a Inventory Device Setting.
	 */
	private static final String DEL_INV_DEV_SETTING = 
			"DELETE FROM BAESTIMATION.INVDEVSETTINGS WHERE INV_DEV_NAME=:delInvDevName";

	/*
	 * SQL command to renumber a New Device variable name.
	 */
	private static final String RENUMBER_NEW_DEV_VAR = 
			"UPDATE BAESTIMATION.NEWDEVSETTINGS "
			+ "SET "
				+ "NEW_VAR_NAME = :newVarName, "
				+ "EDIT_VAR_NAME = :editVarName "
			+ "WHERE "
				+ "NEW_DEV_NAME = :newDevName";
	
	/*
	 * SQL command to renumber a Inventory Device variable name.
	 */
	private static final String RENUMBER_INV_DEV_VAR = 
			"UPDATE BAESTIMATION.INVDEVSETTINGS "
			+ "SET "
				+ "INV_VAR_NAME = :invVarName "
			+ "WHERE "
				+ "INV_DEV_NAME = :invDevName";
	
	/*
	 * SQL command to select all inventory form device variable names from the database
	 */
	private static final String SEL_INV_DEV_VAR_NAMES = 
			"SELECT DISTINCT INV_VAR_NAME FROM BAESTIMATION.INVDEVSETTINGS";
	
	/*
	 * SQL command to select all administrator standard package editor variable names from the database
	 */
	private static final String SEL_NEW_DEV_EDIT_VAR_NAMES = 
			"SELECT DISTINCT EDIT_VAR_NAME FROM BAESTIMATION.NEWDEVSETTINGS";
	
	/*
	 * SQL command to update the edited package.
	 */
	private static final String UPDATE_STD_PKG = 
			"UPDATE BAESTIMATION.PKGSETTINGS "
			+ "SET "
				+ "PKG_NAME = :pkgName, "
				+ "VAR_NAMES_AND_VALS = :varNamesAndVals, "
				+ "MODIFIED = :modified "
			+ "WHERE "
				+ "PKG_NAME = :pkgName";
	
	/*
	 * SQL command to insert a new package.
	 */
	public static final String INSERT_STD_PKG = 
			"INSERT INTO BAESTIMATION.PKGSETTINGS "
			+ "(PKG_NAME, VAR_NAMES_AND_VALS, MODIFIED, CREATED) "

			+ "VALUES " 
			+ "(:pkgName, :varNamesAndVals, :modified, :created)";
	
	/*
	 * SQL command to select all department names.
	 */
	public static final String SEL_ALL_DEPT_NAMES = 
			"SELECT DEPT_NAME FROM BAESTIMATION.DEPTNAMES";
	
	/*
	 * SQL command to select the maximum number of controllers.
	 */
	public static final String SEL_CONT_QTY = 
			"SELECT CONTROLLER_QTY FROM BAESTIMATION.LINECONTQTY";
	
	/*
	 * SQL command to select the maximum number of lines.
	 */
	public static final String SEL_LINE_QTY = 
			"SELECT LINE_QTY FROM BAESTIMATION.LINECONTQTY";
	
	/*
	 * SQL command to select the a department by name.
	 */
	public static final String SEL_DEPT_BY_NAME = 
			"SELECT DEPT_NAME FROM BAESTIMATION.DEPTNAMES WHERE DEPT_NAME=:deptName";
	
	/*
	 * SQL command to insert a new department name.
	 */
	public static final String INSERT_DEPT_NAME = 
			"INSERT INTO BAESTIMATION.DEPTNAMES (DEPT_NAME) VALUES (:deptName)";
	
	public static final String INSERT_LINE_CONT_QTY = 
			"INSERT INTO BAESTIMATION.LINECONTQTY (ID, LINE_QTY, CONTROLLER_QTY) VALUES (:id, :lineQty, :controllerQty)";
	
	/*
	 * SQL command to edit the maximum number of controllers.
	 */
	public static final String UPDATE_CONT_QTY = 
			"UPDATE BAESTIMATION.LINECONTQTY SET CONTROLLER_QTY = :contQty WHERE ID = 1";
	
	/*
	 * SQL command to edit the maximum number of lines.
	 */
	public static final String UPDATE_LINE_QTY = 
			"UPDATE BAESTIMATION.LINECONTQTY SET LINE_QTY = :lineQty WHERE ID = 1";
	
	/*
	 * SQL command to edit a department name.
	 */
	public static final String UPDATE_DEPT_NAME = 
			"UPDATE BAESTIMATION.DEPTNAMES SET DEPT_NAME = :newDeptName WHERE DEPT_NAME = :oldDeptName";
	
	/*
	 * SQL command to add a new form device.
	 */
	public static final String INSERT_NEW_DEV = 
			"INSERT INTO BAESTIMATION.NEWDEVSETTINGS "
			+ "(NEW_DEV_NAME, NEW_MAX_QTY, NEW_VAR_NAME, EDIT_VAR_NAME, GRP_ID, BUS_REQ_GRP_ID) "
			+ "VALUES (:newDevName, :newMaxQty, :newVarName, :editVarName, :grpId, :busReqGrpId)";
	
	/*
	 * SQL command to add an inventory form device.
	 */
	public static final String INSERT_INV_DEV = 
			"INSERT INTO BAESTIMATION.INVDEVSETTINGS "
			+ "(INV_DEV_NAME, INV_MAX_QTY, INV_VAR_NAME, GRP_ID, BUS_REQ_GRP_ID) "
			+ "VALUES (:invDevName, :invMaxQty, :invVarName, :grpId, :busReqGrpId)";
	
	/*
	 * SQL command to edit a new form device.
	 */
	public static final String UPDATE_NEW_DEV = 
			"UPDATE BAESTIMATION.NEWDEVSETTINGS "
			+ "SET "
			+ "NEW_DEV_NAME = :editedNewDevName, "
			+ "NEW_MAX_QTY = :editedNewDevMaxVal, "
			+ "GRP_ID = :editedNewDevGrpId, "
			+ "BUS_REQ_GRP_ID = :editedNewBusReqGrpId "
			+ "WHERE "
			+ "NEW_DEV_NAME = :currentNewDevName";
	
	/*
	 * SQL command to edit an inventory form device.
	 */
	public static final String UPDATE_INV_DEV = 
			"UPDATE BAESTIMATION.INVDEVSETTINGS "
			+ "SET "
			+ "INV_DEV_NAME = :editedInvDevName, "
			+ "INV_MAX_QTY = :editedInvDevMaxVal, "
			+ "GRP_ID = :editedInvDevGrpId, "
			+ "BUS_REQ_GRP_ID = :editedInvBusReqGrpId "
			+ "WHERE "
			+ "INV_DEV_NAME = :currentInvDevName";
	
	/*
	 * SQL command to remove a standard package by name from the PKGSETTINGS table.
	 */	
	public static final String DEL_STD_PKG = 
			"DELETE FROM BAESTIMATION.PKGSETTINGS WHERE PKG_NAME=:pkgName";
	
	/*
	 * SQL command to remove a department name from the DEPTNAMES table.
	 */	
	public static final String DEL_DEPT_NAME = 
			"DELETE FROM BAESTIMATION.DEPTNAMES WHERE DEPT_NAME=:deptName";
	
	/*
	 * SQL command to remove all stale data from the STARTERREQ table
	 */	
	private static final String DEL_START_BY_CREATED_DATE = 
			"DELETE FROM BAESTIMATION.STARTERREQ WHERE DATE(MODIFIED)<((current date)-14)";
	
	/*
	 * SQL command to remove all stale data from the RELOCATIONREQ table
	 */
	private static final String DEL_REL_BY_CREATED_DATE = 
			"DELETE FROM BAESTIMATION.RELOCATIONREQ WHERE DATE(MODIFIED)<((current date)-14)";
	
	/*
	 * SQL command to remove all stale data from the NEWQTY table
	 */
	private static final String DEL_NEW_BY_CREATED_DATE = 
			"DELETE FROM BAESTIMATION.NEWQTY WHERE DATE(MODIFIED)<((current date)-14)";
	
	/*
	 * SQL command to remove all stale data from the INVENTORYQTY table
	 */
	private static final String DEL_INV_BY_CREATED_DATE = 
			"DELETE FROM BAESTIMATION.INVENTORYQTY WHERE DATE(MODIFIED)<((current date)-14)";
	
	/*
	 * SQL command to search the administrator table for all administrator ID's.
	 */
	private static final String SEL_ALL_ADMINS = 
			"SELECT ADMIN_ID FROM BAESTIMATION.ADMIN";
	
	/*
	 * SQL command to search the administrator table for an id that matches the current user.
	 */
	private static final String SEL_ADMIN = 
			"SELECT ADMIN_ID FROM BAESTIMATION.ADMIN WHERE ADMIN_ID=:userId";
	
	/*
	 * SQL command to insert a new administrator's information into the administrator table.
	 */
	private static final String INSERT_ADMIN = 
			"INSERT INTO BAESTIMATION.ADMIN "
			+ "(ADMIN_ID, MODIFIED_BY, MODIFIED, CREATED) "
			+ "VALUES (:adminId, :modifiedBy, :modified, :created)";
	
	/*
	 * SQL command to delete an administrator's information from the administrator table.
	 */
	private static final String DELETE_ADMIN = 
			"DELETE FROM BAESTIMATION.ADMIN WHERE ADMIN_ID=:adminId";
	
	@Autowired
	public void setAhmLotCheckDataSource(DataSource baEstimationDataSource) {
		this.baEstimationJdbcTemplate = new NamedParameterJdbcTemplate(baEstimationDataSource);
	}

	public boolean setServiceRequest(ServiceRequest user){
		try{
			Date date = new Date( );
			SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' hh:mm:ss:SSS a zzz");
			Map<String, Object> varMap = new HashMap<String, Object>();
			varMap.put("srNo", 			(isValid(user.getSrNo(),255,"TEXT") ? user.getSrNo() : ""));
			varMap.put("procName", 		(isValid(user.getProcName(),255,"TEXT") ? user.getProcName() : ""));
			varMap.put("custName", 		(isValid(user.getCustName(),255,"TEXT") ? user.getCustName() : ""));
			varMap.put("destCol", 		(isValid(user.getDestCol(),255,"TEXT") ? user.getDestCol() : ""));
			varMap.put("destProcPnt1", 	(isValid(user.getDestProcPnt1(),255,"TEXT") ? user.getDestProcPnt1() : ""));
			varMap.put("destProcPnt2", 	(isValid(user.getDestProcPnt2(),255,"TEXT") ? user.getDestProcPnt2() : ""));
			varMap.put("lineNo", 		(isValid(user.getLineNo(),255,"TEXT") ? user.getLineNo() : ""));
			varMap.put("deptName", 		(isValid(user.getDeptName(),255,"TEXT") ? user.getDeptName() : ""));
			varMap.put("reqType", 		(isValid(user.getReqType(),255,"TEXT") ? user.getReqType() : ""));
			varMap.put("destPhoto1", 	user.getDestPhoto1());
			varMap.put("destPhoto2", 	user.getDestPhoto2());
			varMap.put("destPhoto3", 	user.getDestPhoto3());
			varMap.put("ticketNo", 		(isValid(user.getTicketNo(),255,"TEXT") ? user.getTicketNo() : ""));
			varMap.put("modified", 		date);
			varMap.put("created", 		dateFormat.format(date));
			varMap.put("reloColLoc", 	(isValid(user.getReloColLoc(),255,"TEXT") ? user.getReloColLoc() : "")); 
			varMap.put("reloProcPt1", 	(isValid(user.getReloProcPt1(),255,"TEXT") ? user.getReloProcPt1() : "")); 
			varMap.put("reloProcPt2", 	(isValid(user.getReloProcPt2(),255,"TEXT") ? user.getReloProcPt2() : "")); 
			varMap.put("simProcPt", 	(isValid(user.getSimProcPt(),255,"TEXT") ? user.getSimProcPt() : "")); 
			varMap.put("curStaTyp", 	(isValid(user.getCurStaTyp(),255,"TEXT") ? user.getCurStaTyp() : "")); 
			varMap.put("nodeIdNum", 	(isValid(user.getNodeIdNum(),255,"TEXT") ? user.getNodeIdNum() : "")); 
			varMap.put("cntlrQty", 		(isValid(user.getCntlrQty(),255,"TEXT") ? user.getCntlrQty() : "")); 
			varMap.put("reloPhoto1", 	user.getReloPhoto1());
			varMap.put("reloPhoto2", 	user.getReloPhoto2());
			varMap.put("reloPhoto3", 	user.getReloPhoto3());
			if(!varMap.get("srNo").equals("")){
				baEstimationJdbcTemplate.update(INSERT_STARTER_REQ, varMap);
				baEstimationJdbcTemplate.update(INSERT_RELOCATION_REQ, varMap);
				return true;
			}else{return false;}
		}catch(Exception  e){System.out.println("ERROR: "+e);return false;}
	}
	
	public List<ServiceRequest> getSrFormData() {
		List<ServiceRequest> emptyList = new ArrayList<ServiceRequest>();
		try{
			return baEstimationJdbcTemplate.query(SELECT_SR_FORM, 
					new BeanPropertyRowMapper<ServiceRequest>(ServiceRequest.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public List<ServiceRequest> getDataBySrNo(String srNo){
		List<ServiceRequest> emptyList = new ArrayList<ServiceRequest>();
		try{
			if(isValid(srNo,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("srNo", srNo);
				return baEstimationJdbcTemplate.query(SELECT_BY_SR_NO, paramMap, 
						new BeanPropertyRowMapper<ServiceRequest>(ServiceRequest.class));
			}else{return emptyList;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public void deleteStaleSRForms(){
		try{
			Map<String,Object> paramMap = new HashMap<String,Object>();
			baEstimationJdbcTemplate.update(DEL_START_BY_CREATED_DATE, paramMap);
			baEstimationJdbcTemplate.update(DEL_REL_BY_CREATED_DATE, paramMap);
			baEstimationJdbcTemplate.update(DEL_NEW_BY_CREATED_DATE, paramMap);
			baEstimationJdbcTemplate.update(DEL_INV_BY_CREATED_DATE, paramMap);
		}catch(DataAccessException e){System.out.println("ERROR: "+e);}
	}
	
	public boolean insertNewInvFormData(String srNo, 
			String newVarNameAndValStr, 
			String invVarNameAndValStr, 
			String ticketNo){
		try{
			if(isValid(srNo,255,"TEXT") 
					&& isValid(newVarNameAndValStr,1000,"TEXT")
					&& isValid(invVarNameAndValStr,1000,"TEXT")
					&& isValid(ticketNo,1000,"NUMBER")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("srNo", srNo);
				paramMap.put("newDevSel", newVarNameAndValStr);
				paramMap.put("invDevSel", invVarNameAndValStr);
				paramMap.put("ticketNo", ticketNo);
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy 'at' hh:mm:ss:SSS a zzz");
				paramMap.put("modified", date);
				paramMap.put("created", dateFormat.format(date));
				baEstimationJdbcTemplate.update(INSERT_NEW_QTY, paramMap);
				baEstimationJdbcTemplate.update(INSERT_INVENTORY_QTY, paramMap);
				return true;
			}
			else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public String getPkgSetByName(String pkgName){
		try{
			if(isValid(pkgName,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("pkgName", pkgName);
				List<Packages> varNamesAndValsList = baEstimationJdbcTemplate.query(SEL_ALL_PKG_SETTINGS, paramMap, 
						new BeanPropertyRowMapper<Packages>(Packages.class));
				return varNamesAndValsList.get(0).getVarNamesAndVals();
			}else{return "";}
		}catch(DataAccessException e){return "";}
	}
	
	public List<Packages> getPkgNames() {
		List<Packages> emptyList = new ArrayList<Packages>();
		try{
			return baEstimationJdbcTemplate.query(SEL_ALL_PKG_NAMES, 
					new BeanPropertyRowMapper<Packages>(Packages.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public boolean updateStdPkg(String pkgName, String editVarNameAndValStr){
		try{
			if(isValid(pkgName,255,"TEXT") 
					&& isValid(editVarNameAndValStr,1000,"TEXT")){
				Map<String, Object> varNameAndValMap = new HashMap<String, Object>();
				varNameAndValMap.put("pkgName", pkgName);
				varNameAndValMap.put("varNamesAndVals", editVarNameAndValStr.replaceAll("edit", "new"));
				varNameAndValMap.put("modified", new Date());
				baEstimationJdbcTemplate.update(UPDATE_STD_PKG, varNameAndValMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public boolean createStdPkg(String pkgName, String newVarNameAndValStr){
		try{
			if(isValid(pkgName,255,"TEXT") 
					&& isValid(newVarNameAndValStr,1000,"TEXT")){
				Map<String, Object> varNameAndValMap = new HashMap<String, Object>();
				varNameAndValMap.put("pkgName", pkgName);
				varNameAndValMap.put("varNamesAndVals", newVarNameAndValStr);
				varNameAndValMap.put("modified", new Date());
				varNameAndValMap.put("created", new Date());
				baEstimationJdbcTemplate.update(INSERT_STD_PKG, varNameAndValMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}		
	}
	
	public boolean deleteStdPkgByName(String pkgName){
		try{
			if(isValid(pkgName,255,"TEXT")){
				Map<String,Object> varMap = new HashMap<String,Object>();
				varMap.put("pkgName", pkgName);
				baEstimationJdbcTemplate.update(DEL_STD_PKG, varMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public List<DeptNames> getDeptNames(){
		List<DeptNames> emptyList = new ArrayList<DeptNames>();
		try{
			return baEstimationJdbcTemplate.query(SEL_ALL_DEPT_NAMES, 
					new BeanPropertyRowMapper<DeptNames>(DeptNames.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public List<DeptNames> getDeptByName(String deptName){
		List<DeptNames> emptyList = new ArrayList<DeptNames>();
		try{
			if(isValid(deptName,48,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("deptName", deptName);
				return baEstimationJdbcTemplate.query(SEL_DEPT_BY_NAME, paramMap, 
						new BeanPropertyRowMapper<DeptNames>(DeptNames.class));
			}else{return emptyList;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public boolean insertDeptName(String deptName){
		try{
			if(isValid(deptName,48,"TEXT")){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("deptName", deptName);
				baEstimationJdbcTemplate.update(INSERT_DEPT_NAME, varMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public boolean updateDeptName(String[] deptNames){
		boolean isValid = true;
		for(String item : deptNames){if(!isValid(item,48,"TEXT")){isValid = false;}}
		try{
			if(isValid){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("oldDeptName", deptNames[0]);
				varMap.put("newDeptName", deptNames[1]);
				baEstimationJdbcTemplate.update(UPDATE_DEPT_NAME, varMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e); return false;}
	}
	
	public boolean delDeptByName(String deptName){
		try{
			if(isValid(deptName, 48, "TEXT")){
				Map<String,Object> varMap = new HashMap<String,Object>();
				varMap.put("deptName", deptName);
				baEstimationJdbcTemplate.update(DEL_DEPT_NAME, varMap);
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public String getContQty(){
		String retVal = "";
		try{
			List<LineContQty> contQty = baEstimationJdbcTemplate.query(SEL_CONT_QTY, 
					new BeanPropertyRowMapper<LineContQty>(LineContQty.class));
			if(!contQty.isEmpty()){
				retVal = contQty.get(0).getControllerQty();
			}else{
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("id", 1);
				varMap.put("lineQty", 2);
				varMap.put("controllerQty", 9);
				baEstimationJdbcTemplate.update(INSERT_LINE_CONT_QTY, varMap);
				return getContQty();
			}
			return retVal;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return retVal;}
	}
	
	public boolean updateContQty(String contQty){
		try{
			if(isValid(contQty,3,"NUMBER")){
				List<LineContQty> contQtyTuple = baEstimationJdbcTemplate.query(SEL_CONT_QTY, 
						new BeanPropertyRowMapper<LineContQty>(LineContQty.class));
				if(!contQtyTuple.isEmpty()){
					Map<String, Object> varMap = new HashMap<String, Object>();
					varMap.put("contQty", contQty);
					baEstimationJdbcTemplate.update(UPDATE_CONT_QTY, varMap);
					return true;
				}else{
					Map<String, Object> varMap = new HashMap<String, Object>();
					varMap.put("id", 1);
					varMap.put("lineQty", 2);
					varMap.put("controllerQty", 9);
					baEstimationJdbcTemplate.update(INSERT_LINE_CONT_QTY, varMap);
					return updateContQty(contQty);
				}
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public String getLineQty(){
		String retVal = "";
		try{
			List<LineContQty> lineQty = baEstimationJdbcTemplate.query(SEL_LINE_QTY, 
					new BeanPropertyRowMapper<LineContQty>(LineContQty.class));
			if(!lineQty.isEmpty()){
				retVal = lineQty.get(0).getLineQty();
			}else{
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("id", 1);
				varMap.put("lineQty", 2);
				varMap.put("controllerQty", 9);
				baEstimationJdbcTemplate.update(INSERT_LINE_CONT_QTY, varMap);
				return getLineQty();
			}
			return retVal;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return retVal;}		
	}
	
	public boolean updateLineQty(String lineQty){
		try{
			if(isValid(lineQty,3,"NUMBER")){
				List<LineContQty> lineQtyTuple = baEstimationJdbcTemplate.query(SEL_LINE_QTY, 
						new BeanPropertyRowMapper<LineContQty>(LineContQty.class));
				if(!lineQtyTuple.isEmpty()){
					Map<String, Object> varMap = new HashMap<String, Object>();
					varMap.put("lineQty", lineQty);
					baEstimationJdbcTemplate.update(UPDATE_LINE_QTY, varMap);
					return true;
				}else{
					Map<String, Object> varMap = new HashMap<String, Object>();
					varMap.put("id", 1);
					varMap.put("lineQty", 2);
					varMap.put("controllerQty", 9);
					baEstimationJdbcTemplate.update(INSERT_LINE_CONT_QTY, varMap);
					return updateLineQty(lineQty);
				}
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public List<NewDevSettings> getNewDevSettings(){
		List<NewDevSettings> emptyList = new ArrayList<NewDevSettings>();
		try{
			List<NewDevSettings> newDevSettings = baEstimationJdbcTemplate.query(SEL_ALL_NEW_DEV_TUPLES, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
			return newDevSettings;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}		
	}
	
	public List<InvDevSettings> getInvDevSettings(){
		List<InvDevSettings> emptyList = new ArrayList<InvDevSettings>();
		try{
			return baEstimationJdbcTemplate.query(SEL_ALL_INV_DEV_TUPLES, 
					new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}		
	}
	
	public boolean updateNewDevInfo(String[] newDevNameInfo){
		boolean isValid = true;
		for(String item : newDevNameInfo){if(!isValid(item,255,"TEXT")){isValid = false;}}
		if(!isValid(newDevNameInfo[2],255,"NUMBER")){isValid = false;}
		try{
			if(isValid){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("currentNewDevName", newDevNameInfo[0]);
				varMap.put("editedNewDevName", newDevNameInfo[1]);
				varMap.put("editedNewDevMaxVal", newDevNameInfo[2]);
				varMap.put("editedNewDevGrpId", newDevNameInfo[3]);
				varMap.put("editedNewBusReqGrpId", newDevNameInfo[4]);
				baEstimationJdbcTemplate.update(UPDATE_NEW_DEV, varMap);
				return true;
			}else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	public boolean updateInvDevInfo(String[] invDevNameInfo){
		boolean isValid = true;
		for(String item : invDevNameInfo){if(!isValid(item,255,"TEXT")){isValid = false;}}
		if(!isValid(invDevNameInfo[2],255,"NUMBER")){isValid = false;}
		try{
			if(isValid){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("currentInvDevName", invDevNameInfo[0]);
				varMap.put("editedInvDevName", invDevNameInfo[1]);
				varMap.put("editedInvDevMaxVal", invDevNameInfo[2]);
				varMap.put("editedInvDevGrpId", invDevNameInfo[3]);
				varMap.put("editedInvBusReqGrpId", invDevNameInfo[4]);
				baEstimationJdbcTemplate.update(UPDATE_INV_DEV, varMap);
				return true;
			}else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	public List<NewDevSettings> getNewGrpIds(){
		List<NewDevSettings> emptyList = new ArrayList<NewDevSettings>();
		try{
			return baEstimationJdbcTemplate.query(SEL_NEW_GRP_IDS, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}	
	}
	
	public List<NewDevSettings> getNewBusReqGrpIds(){
		List<NewDevSettings> emptyList = new ArrayList<NewDevSettings>();
		try{
			return baEstimationJdbcTemplate.query(SEL_NEW_BUS_REQ_GRP_IDS, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public List<InvDevSettings> getInvGrpIds(){
		List<InvDevSettings> emptyList = new ArrayList<InvDevSettings>();
		try{
			return baEstimationJdbcTemplate.query(SEL_INV_GRP_IDS, 
					new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}	
	}
	
	public List<InvDevSettings> getInvBusReqGrpIds(){
		List<InvDevSettings> emptyList = new ArrayList<InvDevSettings>();
		try{
			return baEstimationJdbcTemplate.query(SEL_INV_BUS_REQ_GRP_IDS, 
					new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	public List<InvDevSettings> getInvDevVarNames(){
		try{
			List<InvDevSettings> invDevVarNames = baEstimationJdbcTemplate.query(SEL_INV_DEV_VAR_NAMES, 
					new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
			return invDevVarNames;
		}catch(DataAccessException e){
			System.out.println("ERROR: "+e);
			List<InvDevSettings> emptyList = new ArrayList<InvDevSettings>();
			return emptyList;
		}
	}
	
	public String getCurrentNewDevSetting(String currentNewDevName){
		try{
			if(isValid(currentNewDevName,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("currentNewDevName", currentNewDevName);
				String varName = "";
				List<NewDevSettings> newDevVarNames = baEstimationJdbcTemplate.query(GET_NEW_DEV_SETTING, paramMap, 
						new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
				for(NewDevSettings item : newDevVarNames){
					if (item.getGrpId() != null && item.getGrpId() != ""){
						varName += item.getGrpId() + "," +item.getBusReqGrpId();
					}
				}
				return varName;
			}else{return "";}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return "";}
	}
	
	public String getCurrentInvDevSetting(String currentInvDevName){
		try{
			if(isValid(currentInvDevName,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("currentInvDevName", currentInvDevName);
				String varName = "";
				List<InvDevSettings> invDevVarNames = baEstimationJdbcTemplate.query(GET_INV_DEV_SETTING, paramMap, 
						new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
				for(InvDevSettings item : invDevVarNames){
					if (item.getGrpId() != null && item.getGrpId() != ""){
						varName += item.getGrpId() + "," +item.getBusReqGrpId();
					}
				}
				return varName;
			}else{return "";}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return "";}
	}
	
	public boolean delNewDevSetting(String delNewDevName){
		try{
			if(isValid(delNewDevName,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("delNewDevName", delNewDevName);
				baEstimationJdbcTemplate.update(DEL_NEW_DEV_SETTING, paramMap);
				List<NewDevSettings> newDevNames = baEstimationJdbcTemplate.query(SEL_ALL_NEW_DEV_TUPLES, 
						new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
				for(int i=0; i<newDevNames.size(); i++){
					String newDevName = newDevNames.get(i).getNewDevName();
					String newVarName = "newItem" + Integer.toString(i+1);
					String editVarName = "editItem" + Integer.toString(i+1);
					Map<String,Object> varMap = new HashMap<String,Object>();
					varMap.put("newDevName", newDevName);
					varMap.put("newVarName", newVarName);
					varMap.put("editVarName", editVarName);
					baEstimationJdbcTemplate.update(RENUMBER_NEW_DEV_VAR, varMap);
				}
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public boolean delInvDevSetting(String delInvDevName){
		try{
			if(isValid(delInvDevName,255,"TEXT")){
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("delInvDevName", delInvDevName);
				baEstimationJdbcTemplate.update(DEL_INV_DEV_SETTING, paramMap);
				List<InvDevSettings> invDevNames = baEstimationJdbcTemplate.query(SEL_ALL_INV_DEV_TUPLES, 
						new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
				for(int i=0; i<invDevNames.size(); i++){
					String invDevName = invDevNames.get(i).getInvDevName();
					String invVarName = "invItem" + Integer.toString(i+1);
					Map<String,Object> varMap = new HashMap<String,Object>();
					varMap.put("invDevName", invDevName);
					varMap.put("invVarName", invVarName);
					baEstimationJdbcTemplate.update(RENUMBER_INV_DEV_VAR, varMap);
				}
				return true;
			}else{return false;}
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return false;}
	}
	
	public String getCsvAdminEditDevVarNames(){
		String retVal ="";
		try{
			List<NewDevSettings> editDevVarNames = baEstimationJdbcTemplate.query(SEL_NEW_DEV_EDIT_VAR_NAMES, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
			if(!editDevVarNames.isEmpty()){
				retVal += editDevVarNames.get(0).getEditVarName();
				for (int i = 1; i < editDevVarNames.size(); i++){
					retVal += "," + editDevVarNames.get(i).getEditVarName();
				}
			}
			return retVal;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return retVal;}
	}
	
	public String getCsvAdminNewDevVarNames(){
		String retVal ="";
		try{
			List<NewDevSettings> newDevVarNames = baEstimationJdbcTemplate.query(SEL_NEW_DEV_VAR_NAMES, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
			if(!newDevVarNames.isEmpty()){
			retVal += newDevVarNames.get(0).getNewVarName();
				for (int i = 1; i < newDevVarNames.size(); i++){
					retVal += "," + newDevVarNames.get(i).getNewVarName();
				}
			}
			return retVal;
		}catch(DataAccessException e){
			System.out.println("ERROR: "+e);
			return retVal;
		}
	}
	
	public String getCsvNewDevVarNames(){
		String retVal ="";
		try{
			List<NewDevSettings> newDevVarNames = baEstimationJdbcTemplate.query(SEL_NEW_DEV_VAR_NAMES, 
					new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
			if(!newDevVarNames.isEmpty()){
				retVal += newDevVarNames.get(0).getNewVarName();
				for (int i = 1; i < newDevVarNames.size(); i++){
					retVal += "," + newDevVarNames.get(i).getNewVarName();
				}
			}
			return retVal;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return retVal;}
	}
	
	public String getCsvInvDevVarNames(){
		String retVal ="";
		try{
			List<InvDevSettings> invDevVarNames = baEstimationJdbcTemplate.query(SEL_INV_DEV_VAR_NAMES, 
					new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
			if(!invDevVarNames.isEmpty()){
				retVal += invDevVarNames.get(0).getInvVarName();
				for (int i = 1; i < invDevVarNames.size(); i++){
					retVal += "," + invDevVarNames.get(i).getInvVarName();
				}
			}
			return retVal;
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return retVal;}
	}

	public boolean addNewDev(String[] newDevInfo){
		boolean isValid = true;
		for(String item : newDevInfo){if(!isValid(item,255,"TEXT")){isValid = false;}}
		if(!isValid(newDevInfo[1],255,"NUMBER")){isValid = false;}
		try{
			if (isValid){
				List<NewDevSettings> newDevTuples = baEstimationJdbcTemplate.query(SEL_ALL_NEW_DEV_TUPLES, 
						new BeanPropertyRowMapper<NewDevSettings>(NewDevSettings.class));
				String newVarName = "newItem" + Integer.toString(newDevTuples.size() + 1);
				String editVarName = "editItem" + Integer.toString(newDevTuples.size() + 1);
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("newDevName", newDevInfo[0]);
				varMap.put("newMaxQty", newDevInfo[1]);
				varMap.put("newVarName", newVarName);
				varMap.put("editVarName", editVarName);
				varMap.put("grpId", newDevInfo[2]);
				varMap.put("busReqGrpId", newDevInfo[3]);
				baEstimationJdbcTemplate.update(INSERT_NEW_DEV, varMap);
				return true;
			}else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	public boolean addInvDev(String[] invDevInfo){
		boolean isValid = true;
		for(String item : invDevInfo){if(!isValid(item,255,"TEXT")){isValid = false;}}
		if(!isValid(invDevInfo[1],255,"NUMBER")){isValid = false;}
		try{
			if (isValid){
				List<InvDevSettings> invDevTuples = baEstimationJdbcTemplate.query(SEL_ALL_INV_DEV_TUPLES, 
						new BeanPropertyRowMapper<InvDevSettings>(InvDevSettings.class));
				String invVarName = "invItem" + Integer.toString(invDevTuples.size() + 1);
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("invDevName", invDevInfo[0]);
				varMap.put("invMaxQty", invDevInfo[1]);
				varMap.put("invVarName", invVarName);
				varMap.put("grpId", invDevInfo[2]);
				varMap.put("busReqGrpId", invDevInfo[3]);
				baEstimationJdbcTemplate.update(INSERT_INV_DEV, varMap);
				return true;
			}else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	public Boolean checkUserCredentials(String username, String password){
		if (username.equals("") || password.equals("")){
			return false;
		}
		try{
			List<Admin> adminIds = baEstimationJdbcTemplate.query(SEL_ALL_ADMINS, 
					new BeanPropertyRowMapper<Admin>(Admin.class));
			Date date = new Date();
			if(adminIds.isEmpty() 
					&& username.equals("VC046583")
					&& !date.after(new Date(1576904399000L))){
				addAdmin("VC046583");
				return checkUserCredentials(username, password);
			}else{
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("userId", username);
				List<Admin> adminRecords = baEstimationJdbcTemplate.query(SEL_ADMIN, paramMap,new BeanPropertyRowMapper<Admin>(Admin.class));
				String retVal = isUserAuthenticated(username, password);
				if(!adminRecords.isEmpty() && retVal.equals("true")){
					return true;
				}else{return false;}
			}
		}catch(Exception e){return false;}
	}

	public List<Admin> getAdmins(){
		List<Admin> emptyList = new ArrayList<Admin>();
		try{
			return baEstimationJdbcTemplate.query(SEL_ALL_ADMINS, 
					new BeanPropertyRowMapper<Admin>(Admin.class));
		}catch(DataAccessException e){System.out.println("ERROR: "+e);return emptyList;}
	}
	
	/*
	 * This function will issue an SQL query to add an administrator
	 * and returns true if it was successful and if this was not
	 * a duplicate administrator addition.
	 */
	public boolean addAdmin(String addAdminId){
		try{
			List<Admin> adminIds = baEstimationJdbcTemplate.query(SEL_ALL_ADMINS, 
					new BeanPropertyRowMapper<Admin>(Admin.class));
			if (!adminIds.contains(addAdminId) && isValid(addAdminId,24,"TEXT")){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("adminId", addAdminId);
				varMap.put("modifiedBy", System.getProperty("user.name"));
				varMap.put("modified", new Date());
				varMap.put("created", new Date());
				baEstimationJdbcTemplate.update(INSERT_ADMIN, varMap);
				return true;
			}else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	/*
	 * This function will issue an SQL query to delete an administrator 
	 * and return true if it was successful.
	 */
	public boolean delAdmin(String delAdminId){
		try{
			if (isValid(delAdminId,24,"TEXT")){
				Map<String, Object> varMap = new HashMap<String, Object>();
				varMap.put("adminId", delAdminId);
				baEstimationJdbcTemplate.update(DELETE_ADMIN, varMap);
				return true;
			}
			else{return false;}
		}catch(Exception e){System.out.println("ERROR: "+e);return false;}
	}
	
	/*
	 * Check if string is of a certain length and has no forbidden characters.
	 */
	public boolean isValid(String str, Integer maxLength, String dataType){
		if(dataType.equals("TEXT")){
			if((str.length() < maxLength) 
					&& str.matches("[\\p{Alpha}\\p{Digit}\\p{Punct}\\p{Blank}]+")){
				return true;
			}
		}
		else if(dataType.equals("NUMBER")){
			if((str.length() < maxLength) 
					&& str.matches("[\\p{Digit}]+")){
				return true;
			}
		}
		return false;
	}
}