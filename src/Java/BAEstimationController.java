package Java;

import java.sql.SQLException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BAEstimationController {
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	BAEstimationDao baEstimationDao;

	/*
	 * This method will redirect to /BAEstimation/Home if someone lands on /BAEstimation/
	 */
	@RequestMapping(method = { RequestMethod.GET }, path = "/BAEstimation/")
	public String getIndexPage() {
		return "redirect:/BAEstimation/Home";
	}

	/* 
	 * This mapping is for the Home_Form page where the information is filled out.
	 */
	@RequestMapping(method = { RequestMethod.GET }, path = "/BAEstimation/Home")
	public ModelAndView getHomeData() {
		ModelAndView mvHome = new ModelAndView("BAEstimation/home");
		// A list of all standard package names.
		List<Packages> listAllPkgNames = baEstimationDao.getPkgNames();
		// A list containing the maximum controller quantity.
		String listContQty = baEstimationDao.getContQty();
		// A list containing the maximum line quantity.
		String listLineQty = baEstimationDao.getLineQty();
		// A list containing all new form standard package editor variable names.
		String csvNewDevVarNames = baEstimationDao.getCsvNewDevVarNames();
		// A list containing all new form standard package editor variable names.
		String csvInvDevVarNames = baEstimationDao.getCsvInvDevVarNames();
		// A list containing all of the Department Names.
		List<DeptNames> listAllDeptNames = baEstimationDao.getDeptNames();
		// A list containing all of the new device settings.
		List<NewDevSettings> listAllNewDevSettings = baEstimationDao.getNewDevSettings();
		// A list containing all of the current inventory device settings.
		List<InvDevSettings> listAllInvDevSettings = baEstimationDao.getInvDevSettings();
		// A list containing all distinct new grpIds.
		List<NewDevSettings> listNewGrpIds = baEstimationDao.getNewGrpIds();
		// A list containing all distinct inventory grpIds.
		List<InvDevSettings> listInvGrpIds = baEstimationDao.getInvGrpIds();
		// Adding the lists to the Home page view.
		mvHome.addObject("listAllPkgNames", listAllPkgNames);
		mvHome.addObject("listContQty", listContQty);
		mvHome.addObject("listLineQty", listLineQty);
		mvHome.addObject("listAllDeptNames", listAllDeptNames);
		mvHome.addObject("listAllNewDevSettings", listAllNewDevSettings);
		mvHome.addObject("listAllInvDevSettings", listAllInvDevSettings);
		mvHome.addObject("csvNewDevVarNames", csvNewDevVarNames);
		mvHome.addObject("csvInvDevVarNames", csvInvDevVarNames);
		mvHome.addObject("listNewGrpIds", listNewGrpIds);
		mvHome.addObject("listInvGrpIds", listInvGrpIds);
		return mvHome;
	}
	
	/*
	 * This method will receive the posted information from the Service Request object
	 * binding with the Service Request Form and will insert that data into the database. 
	*/
	@RequestMapping(method = { RequestMethod.POST }, path = "/BAEstimation/ServiceRequest")
	public String postServiceRequest(@ModelAttribute("user") ServiceRequest user, BindingResult webResult){
		if (!webResult.hasErrors()) {
			boolean daoResult = baEstimationDao.setServiceRequest(user);
			return (daoResult ? "redirect:/BAEstimation/Confirmation" : "redirect:/BAEstimation/Rejection");
		} else {return "redirect:/BAEstimation/Rejection";}
	}
	
	/*
	 * This method will generate the administrator login page view.
	 */
	@RequestMapping(method = { RequestMethod.GET }, path = "/BAEstimation/AdminLogin")
	public ModelAndView getAdminLoginData(@RequestParam(value="hasFailed", required=false, defaultValue="false") String hasFailed) {
		ModelAndView mvAdminLogin = new ModelAndView("BAEstimation/adminLogin");
		mvAdminLogin.addObject("hasFailed", hasFailed);
		return mvAdminLogin;
	}
	
	/*
	 * This method will generate the administrator page view and will use the listing of
	 * all the Service Request tuples to generate a list on the page.
	 */
	@RequestMapping(method = { RequestMethod.POST }, path = "/BAEstimation/Admin")
	public ModelAndView getAdminData(@ModelAttribute("user") User user, BindingResult webResult) {
		
		if (!webResult.hasErrors() && baEstimationDao.checkUserCredentials(user.getUserId(), user.getUserPassword())) {
			ModelAndView mvAdmin = new ModelAndView("BAEstimation/admin");
			// Remove all stale data (14 days old) from the database.
			baEstimationDao.deleteStaleSRForms();
			// A list of all Service Request objects.
			List<ServiceRequest> listAllSrData = baEstimationDao.getSrFormData();
			// A list of all standard package names
			List<Packages> listAllPkgNames = baEstimationDao.getPkgNames();
			// A list containing all of the Department Names.
			List<DeptNames> listAllDeptNames = baEstimationDao.getDeptNames();
			// A list containing all of the new device settings.
			List<NewDevSettings> listAllNewDevSettings = baEstimationDao.getNewDevSettings();
			// A list containing all of the inv device settings.
			List<InvDevSettings> listAllInvDevSettings = baEstimationDao.getInvDevSettings();
			// A list containing all distinct new grpIds.
			List<NewDevSettings> listNewGrpIds = baEstimationDao.getNewGrpIds();
			// A list containing all distinct inventory grpIds.
			List<InvDevSettings> listInvGrpIds = baEstimationDao.getInvGrpIds();
			// A list containing all distinct newBusReqGrpIds.
			List<NewDevSettings> listNewBusReqGrpIds = baEstimationDao.getNewBusReqGrpIds();
			// A list containing all distinct invBusReqGrpIds.
			List<InvDevSettings> listInvBusReqGrpIds = baEstimationDao.getInvBusReqGrpIds();
			// A list containing all distinct invBusReqGrpIds.
			List<Admin> listAdmins = baEstimationDao.getAdmins();
			// A list containing all administrator page standard package editor variable names.
			String csvAdminEditDevVarNames = baEstimationDao.getCsvAdminEditDevVarNames();
			// A list containing all administrator page standard package editor variable names.
			String csvAdminNewDevVarNames = baEstimationDao.getCsvAdminNewDevVarNames();
			// Adding the lists to the Administrator page view.
			mvAdmin.addObject("listAllSrData", listAllSrData);
			mvAdmin.addObject("listAllPkgNames", listAllPkgNames);
			mvAdmin.addObject("listAllDeptNames", listAllDeptNames);
			mvAdmin.addObject("listAllNewDevSettings", listAllNewDevSettings);
			mvAdmin.addObject("listAllInvDevSettings", listAllInvDevSettings);
			mvAdmin.addObject("csvAdminEditDevVarNames", csvAdminEditDevVarNames);
			mvAdmin.addObject("csvAdminNewDevVarNames", csvAdminNewDevVarNames);
			mvAdmin.addObject("listNewGrpIds", listNewGrpIds);
			mvAdmin.addObject("listInvGrpIds", listInvGrpIds);
			mvAdmin.addObject("listNewBusReqGrpIds", listNewBusReqGrpIds);
			mvAdmin.addObject("listInvBusReqGrpIds", listInvBusReqGrpIds);
			mvAdmin.addObject("listAdmins", listAdmins);
			return mvAdmin;
		}else{return getAdminLoginData("true");}
	}

	/*
	 * This method will export and edit an excel template for a service request 
	 * base upon what information was provided when the user filled out the
	 * Service Request form.
	 */
	@RequestMapping(method={RequestMethod.GET}, path="/BAEstimation/ExportSR")
	public void getExportSR(@RequestParam(value = "srNo", required = true) String srNo, HttpServletResponse response) 
			throws InvalidFormatException, SQLException  {
		List<ServiceRequest> serviceReports = baEstimationDao.getDataBySrNo(srNo);
		List<NewDevSettings> newDevNameAndBusReqGrpIdList = baEstimationDao.getNewDevSettings();
		List<InvDevSettings> invDevNameAndBusReqGrpIdList = baEstimationDao.getInvDevSettings();
		if(!serviceReports.isEmpty() 
				&& !newDevNameAndBusReqGrpIdList.isEmpty() 
				&& !invDevNameAndBusReqGrpIdList.isEmpty()){
			BAServiceRequestWriter exportExcelReport = new BAServiceRequestWriter(serviceReports, 
					newDevNameAndBusReqGrpIdList, invDevNameAndBusReqGrpIdList, response, appContext);
			exportExcelReport.exportExcelFile();
		}
	}
	
	/*
	 * This method will send the user to the Confirmation page if they successfully submitted a
	 * SR Requirements request.
	 */
	@RequestMapping(method = { RequestMethod.GET }, path = "/BAEstimation/Confirmation")
	public ModelAndView getConfirmationData() {
		ModelAndView mvConfirmation = new ModelAndView("/BAEstimation/confirmation");
		return mvConfirmation;
	}
	
	/*
	 * This method will send the user to the rejection page if they did not successfully submit
	 * a SR Requirements request.
	 */
	@RequestMapping(method = { RequestMethod.GET }, path = "/BAEstimation/Rejection")
	public ModelAndView getRejectionData() {
		ModelAndView mvRejection = new ModelAndView("/BAEstimation/rejection");
		return mvRejection;
	}	
}