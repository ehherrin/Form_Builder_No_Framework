package Java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BAEstimationRestController {
	
	@Autowired
	BAEstimationDao baEstimationDao;
	
	@RequestMapping(path="/BAEstimation/mobile/ws/sendNewInvFormData", method= RequestMethod.GET)
	public String sendNewInvFormData(
			@RequestParam(value="srNo", required=true) String srNo,
			@RequestParam(value="newVarNameAndValStr", required=true) String newVarNameAndValStr, 
			@RequestParam(value="invVarNameAndValStr", required=true) String invVarNameAndValStr, 
			@RequestParam(value="ticketNo", required=true) String ticketNo) {
		boolean result = baEstimationDao.insertNewInvFormData(srNo, newVarNameAndValStr, 
				invVarNameAndValStr, ticketNo);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/getPkgSet", method= RequestMethod.GET)
	public String getPkgSet(@RequestParam(value="pkgName", required=true) String pkgName) {
		return baEstimationDao.getPkgSetByName(pkgName);
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/updateStdPkg", method= RequestMethod.GET)
	public String updateStdPkg(@RequestParam(value="pkgName", required=true) String pkgName, 
			@RequestParam(value="editVarNameAndValStr", required=true) String editVarNameAndValStr){
		boolean result = baEstimationDao.updateStdPkg(pkgName, editVarNameAndValStr);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/createStdPkg", method= RequestMethod.GET)
	public String createStdPkg(@RequestParam(value="pkgName", required=true) String pkgName, 
			@RequestParam(value="newVarNameAndValStr", required=true) String newVarNameAndValStr) {
		boolean result = baEstimationDao.createStdPkg(pkgName, newVarNameAndValStr);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/deleteStdPkg", method= RequestMethod.GET)
	public String deleteStdPkg(@RequestParam(value="pkgName", required=true) String pkgName) {
		boolean result = baEstimationDao.deleteStdPkgByName(pkgName);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/createDeptName", method= RequestMethod.GET)
	public String createDeptName(@RequestParam(value="deptName", required=true) String deptName) {
		boolean result = baEstimationDao.insertDeptName(deptName);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/updateDeptName", method= RequestMethod.GET)
	public String updateDeptName(@RequestParam(value="deptNames", required=true) String[] deptNames) {
		boolean result = baEstimationDao.updateDeptName(deptNames);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/delDeptName", method= RequestMethod.GET)
	public String delDeptName(@RequestParam(value="deptName", required=true) String deptName) {
		boolean result = baEstimationDao.delDeptByName(deptName);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/updateContQty", method= RequestMethod.GET)
	public String updateContQty(@RequestParam(value="contQty", required=true) String contQty) {
		boolean result = baEstimationDao.updateContQty(contQty);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/updateLineQty", method= RequestMethod.GET)
	public String updateLineQty(@RequestParam(value="lineQty", required=true) String lineQty) {
		boolean result = baEstimationDao.updateLineQty(lineQty);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/addNewDevice", method= RequestMethod.GET)
	public String addNewDevice(@RequestParam(value="newDevInfo", required=true) String[] newDevInfo) {
		boolean result = baEstimationDao.addNewDev(newDevInfo);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/addInvDevice", method= RequestMethod.GET)
	public String addInvDevice(@RequestParam(value="invDevInfo", required=true) String[] invDevInfo) {
		boolean result = baEstimationDao.addInvDev(invDevInfo);
		return (result ? "Successful." : "Failure.");
	}
		
	@RequestMapping(path="/BAEstimation/mobile/ws/editNewDevice", method= RequestMethod.GET)
	public String editNewDevice(@RequestParam(value="newDevNameInfo", required=true) String[] newDevNameInfo) {
		boolean result = baEstimationDao.updateNewDevInfo(newDevNameInfo);
		return (result ? "Successful." : "Failure.");
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/editInvDevice", method= RequestMethod.GET)
	public String editInvDevice(@RequestParam(value="invDevNameInfo", required=true) String[] invDevNameInfo) {
		boolean result = baEstimationDao.updateInvDevInfo(invDevNameInfo);
		return (result ? "Successful." : "Failure.");
	}

	@RequestMapping(path="/BAEstimation/mobile/ws/getCurrentNewDevSetting", method= RequestMethod.GET)
	public String getCurrentNewDevSetting(
			@RequestParam(value="currentNewDevName", required=true) String currentNewDevName) {
		String csvGrpIds = baEstimationDao.getCurrentNewDevSetting(currentNewDevName);
		return csvGrpIds;
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/getCurrentInvDevSetting", method= RequestMethod.GET)
	public String getCurrentInvDevSetting(
			@RequestParam(value="currentInvDevName", required=true) String currentInvDevName) {
		String csvGrpIds = baEstimationDao.getCurrentInvDevSetting(currentInvDevName);
		return csvGrpIds;
	}
	
	@RequestMapping(path="/BAEstimation/mobile/ws/delNewDev", method= RequestMethod.GET)
	public String delNewDev(
			@RequestParam(value="delNewDevName", required=true) String delNewDevName) {
		boolean result = baEstimationDao.delNewDevSetting(delNewDevName);
		return (result ? "Successful." : "Failure.");
	}

	@RequestMapping(path="/BAEstimation/mobile/ws/delInvDev", method= RequestMethod.GET)
	public String delInvDev(
			@RequestParam(value="delInvDevName", required=true) String delInvDevName) {
		boolean result = baEstimationDao.delInvDevSetting(delInvDevName);
		return (result ? "Successful." : "Failure.");
	}

	@RequestMapping(path="/BAEstimation/mobile/ws/authenticateAdmin", method= RequestMethod.GET)
	public String authenticateAdmin(@RequestParam(value="userId", required=true) String userId){
		boolean result = baEstimationDao.checkUserCredentials(userId);
		return (result ? "Successful." : "Failure.");
	}
	
	/*
	 * This function issues an administrator addition request to the DAO and returns the 
	 * string "Successful" or "failure" based upon what is returned by the DAO.
	 */
	@RequestMapping(path="/BAEstimation/mobile/ws/addAdmin", method= RequestMethod.GET)
	public String addAdmin(@RequestParam(value="addAdminId", required=true) String addAdminId) {
		boolean result = baEstimationDao.addAdmin(addAdminId);
		return (result ? "Successful." : "Failure.");
	}
	
	/*
	 * This function issues an administrator deletion request to the DAO and returns the 
	 * string "Successful" or "failure" based upon what is returned by the DAO.
	 */
	@RequestMapping(path="/BAEstimation/mobile/ws/delAdmin", method= RequestMethod.GET)
	public String delAdmin(
			@RequestParam(value="delAdminId", required=true) String delAdminId) {
		boolean result = baEstimationDao.delAdmin(delAdminId);
		return (result ? "Successful." : "Failure.");
	}
}