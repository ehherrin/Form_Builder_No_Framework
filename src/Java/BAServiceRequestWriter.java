package Java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;

public class BAServiceRequestWriter {
	List<ServiceRequest> serviceReports;
	List<NewDevSettings> newDevNameAndBusReqGrpIdList;
	List<InvDevSettings> invDevNameAndBusReqGrpIdList;
	HttpServletResponse response;
	ApplicationContext appContext;
    public void exportExcelFile() throws SQLException{
    	try{
			// Input from the home form.
    		Pair<String, String> srNo 
    		= Pair.of(serviceReports.get(0).getSrNo(), "SR Number");
			// Input from the home form.
	    	Pair<String, String> procName 
    		= Pair.of(serviceReports.get(0).getProcName(), "Process Name");
    		// This is the filename for the excel file.
	    	String srFilename = srNo.getFirst() + "_BRD.xlsx";
	    	// Set the content type.
    		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    		// Set the filename.
			response.addHeader("Content-disposition", "attachment; filename=" + srFilename);
			// Grab the original template file.
	        File file = appContext.getResource("META-INF\\templates\\SRTemplateV3.xlsx").getFile();
	        // Read from the original template file.
	        FileInputStream fip = new FileInputStream(file);
	        // Create a new workbook from the template file.
	        XSSFWorkbook workbook = new XSSFWorkbook(fip); 
	        // Open the first sheet on the new workbook.
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			// CONSTANTS from the home form.
			Pair<String, String> destCol;
			Pair<String, String> destProcPnt1;
			Pair<String, String> destProcPnt2;
			Pair<String, String> lineNo;
			Pair<String, String> deptName;
			Pair<String, String> reqType;
			
	    	// CONSTANTS from the new form.
	    	String newDevSelString;
			Map<String, Integer> newDevNameAndValMap = new HashMap<String, Integer>();
			String[] newDevNameAndValTempArray1;
			String[] newDevNameAndValTempArray2;
	    	
	    	// CONSTANTS from the relocation form.
	    	Pair<String, String> reloColLoc;
	    	Pair<String, String> reloProcPt1;
	    	Pair<String, String> reloProcPt2;
	    	Pair<String, String> simProcPt;
	    	Pair<String, String> curStaTyp;
	    	Pair<String, String> nodeIdNum;
	    	Pair<String, String> cntlrQty; 
	    	
	    	// CONSTANTS from the inventory form.
	    	String invDevSelString;
	    	Map<String, Integer> invDevNameAndValMap = new HashMap<String, Integer>();
			String[] invDevNameAndValTempArray1;
			String[] invDevNameAndValTempArray2;
	    	
	    	Integer GLOBAL_START = 57;
	    	Integer TRUE_START = 57;
	    	Integer HARDWARE_ROWS_MAX = 5;
	    	
	        CellStyle allBorders = workbook.createCellStyle();
	        allBorders.setBorderBottom(CellStyle.BORDER_THIN);
	        allBorders.setBorderTop(CellStyle.BORDER_THIN);
	        allBorders.setBorderLeft(CellStyle.BORDER_THIN);
	        allBorders.setBorderRight(CellStyle.BORDER_THIN);
	        allBorders.setWrapText(true);
	        	        
        	/*
        	 * Here is the code the generates the Title Bar for "DEVELOPMENT"
        	 */
	        
	        Row developmentTitleRow 			= sheet.createRow(GLOBAL_START);
	        Cell developmentBusinessProcCell 	= developmentTitleRow.createCell(1);
	        developmentTitleRow.createCell(2);
	        developmentTitleRow.createCell(3);
	        developmentTitleRow.createCell(4);
	        developmentTitleRow.createCell(5);
	        developmentTitleRow.createCell(6);
			developmentTitleRow.createCell(7);
	        Cell developmentHardwareCell 		= developmentTitleRow.createCell(8);
	        // Declare the Bar's cell range.
	        CellRangeAddress mergedDevTitleBar = new CellRangeAddress(GLOBAL_START, GLOBAL_START, 
	        		developmentBusinessProcCell.getColumnIndex(), developmentHardwareCell.getColumnIndex());
	        // Declare the Bar style.
			CellStyle titleBarStyle = workbook.createCellStyle();
			// Declare the font, make it bold, and apply it.
			XSSFFont bar_font = workbook.createFont();
			bar_font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			titleBarStyle.setFont(bar_font);
			// Add fill color and pattern.
			titleBarStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			titleBarStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// Apply the style to each cell in the row.
			for(int i = mergedDevTitleBar.getFirstColumn(); i <= mergedDevTitleBar.getLastColumn(); i++){
				developmentTitleRow.getCell(i).setCellStyle(titleBarStyle);
			}
			developmentTitleRow.getCell(mergedDevTitleBar.getFirstColumn()).setCellValue("DEVELOPMENT");
			sheet.addMergedRegion(mergedDevTitleBar);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedDevTitleBar, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedDevTitleBar, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedDevTitleBar, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedDevTitleBar, sheet, workbook);
			
			/*
			 * End of Title Bar code segment.
			 */

			GLOBAL_START++;
	        
	        for(ServiceRequest report : serviceReports){
		    	String detailsAndComments = "";
		    	
		    	// Pull the data from the database.
		    	reqType 				= Pair.of(report.getReqType(), "reqType");
		    	lineNo					= Pair.of(report.getLineNo(), "lineNo");
				deptName				= Pair.of(report.getDeptName(), "deptName");
		    	simProcPt				= Pair.of(report.getSimProcPt(), "simProcPt");
		    	curStaTyp				= Pair.of(report.getCurStaTyp(), "curStaTyp");
		    	nodeIdNum				= Pair.of(report.getNodeIdNum(), "nodeIdNum");
		    	cntlrQty				= Pair.of(report.getCntlrQty(), "cntlrQty");
		    	
		    	//write the stuff based on stuff logic creating new rows as you go
		    	
		    	Row devInfoRow = sheet.createRow(GLOBAL_START);
		    	Cell devInfoBusinessProcCell = devInfoRow.createCell(1);
		    	Cell devInfoReqNoCell = devInfoRow.createCell(2);
		    	Cell devInfoBusReqDescCell = devInfoRow.createCell(3);
		    	Cell devInfoPriorityCell = devInfoRow.createCell(4);
		    	Cell devInfoBlankCell = devInfoRow.createCell(5);
		    	Cell devInfoDateCell = devInfoRow.createCell(6);
		    	Cell devInfoVersionCell = devInfoRow.createCell(7);
		    	Cell devInfoDetailCommentCell = devInfoRow.createCell(8);
		    	
		    	// Merge 4 and 5
		    	CellRangeAddress mergedPriorityBlank = new CellRangeAddress(GLOBAL_START, GLOBAL_START, 
		    			devInfoPriorityCell.getColumnIndex(), devInfoBlankCell.getColumnIndex());
				sheet.addMergedRegion(mergedPriorityBlank);
		    	
				// Pull the details and comments data from the defined variables.
				detailsAndComments += "LineNumber: " + lineNo.getFirst() 
						+ "\nDepartment Name: " + deptName.getFirst();
				
				if ( reqType.getFirst().equals("Relocate") || reqType.getFirst().equals("Relocate and Customize") ){
					if (!simProcPt.getFirst().equals("")){
						detailsAndComments += "\nSimilar Process Point: " + simProcPt.getFirst();
					}
					detailsAndComments += "\nCurrent Station Type: " + curStaTyp.getFirst()
							+ "\nNode Identification Number: " + nodeIdNum.getFirst() 
							+ "\nNumber of Controllers: " + cntlrQty.getFirst();
				}
				
				// Dynamically fill out the businessProc and detailComment Cell based on what was entered in the form 
		    	devInfoBusinessProcCell.setCellValue(procName.getFirst() + " - " + reqType.getFirst());
		    	devInfoDetailCommentCell.setCellValue(detailsAndComments);
		    	
		    	// Set the border style for everything
		    	devInfoBusinessProcCell.setCellStyle(allBorders);
		    	devInfoReqNoCell.setCellStyle(allBorders);
		    	devInfoBusReqDescCell.setCellStyle(allBorders);
		    	devInfoDateCell.setCellStyle(allBorders);
		    	devInfoVersionCell.setCellStyle(allBorders);
		    	devInfoDetailCommentCell.setCellStyle(allBorders);
		    	RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
		    	
		    	GLOBAL_START++;
	        } //59 -> ??
	        	        
	        /*
        	 * Here is the code the generates the Title Bar for "DEVELOPMENT"
        	 */
	        
	        Row hardwareTitleRow 			= sheet.createRow(GLOBAL_START);
	        Cell hardwareBusinessProcCell 	= hardwareTitleRow.createCell(1);
	        hardwareTitleRow.createCell(2);
	        hardwareTitleRow.createCell(3);
	        hardwareTitleRow.createCell(4);
	        hardwareTitleRow.createCell(5);
	        hardwareTitleRow.createCell(6);
	        hardwareTitleRow.createCell(7);
	        Cell hardwareHardwareCell 		= hardwareTitleRow.createCell(8);
	        // Declare the Bar's cell range.
	        CellRangeAddress mergedHardwareTitleBar = new CellRangeAddress(GLOBAL_START, GLOBAL_START, 
	        		hardwareBusinessProcCell.getColumnIndex(), hardwareHardwareCell.getColumnIndex());
			// Apply the bar font.
			titleBarStyle.setFont(bar_font);
			// Add fill color and pattern.
			titleBarStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			titleBarStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// Apply the style to each cell in the row.
			for(int i = mergedHardwareTitleBar.getFirstColumn(); i <= mergedHardwareTitleBar.getLastColumn(); i++){
				hardwareTitleRow.getCell(i).setCellStyle(titleBarStyle);
			}
			hardwareTitleRow.getCell(mergedHardwareTitleBar.getFirstColumn()).setCellValue("HARDWARE");
			sheet.addMergedRegion(mergedHardwareTitleBar);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedHardwareTitleBar, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedHardwareTitleBar, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedHardwareTitleBar, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedHardwareTitleBar, sheet, workbook);
			
			/*
			 * End of Title Bar code segment.
			 */
	        
	        //increment global start again
			
			GLOBAL_START++;
			GLOBAL_START++;
			for(ServiceRequest report : serviceReports){
				// Gather input from the home form.
		    	srNo 						= Pair.of(report.getSrNo(), "srNo");
		    	procName 					= Pair.of(report.getProcName(), "procName");
		    	destCol 					= Pair.of(report.getDestCol(), "destCol");
		    	destProcPnt1 				= Pair.of(report.getDestProcPnt1(), "destProcPnt1");
		    	destProcPnt2 				= Pair.of(report.getDestProcPnt2(), "destProcPnt2");
		    	reqType 					= Pair.of(report.getReqType(), "reqType");
		    	
		    	// Gather input from the new form.
		    	newDevSelString = report.getNewDevSel();
				newDevNameAndValTempArray1 = newDevSelString.split("\\^");
				for (String item : newDevNameAndValTempArray1){
					newDevNameAndValTempArray2 = item.split(",");
					newDevNameAndValMap.put(newDevNameAndValTempArray2[0], Integer.parseInt(newDevNameAndValTempArray2[1]));
				}
		    	
		    	// Gather input from the relocation form.
		    	reloColLoc 					= Pair.of(report.getReloColLoc(), "reloColLoc");
		    	reloProcPt1 				= Pair.of(report.getReloProcPt1(), "reloProcPt1");
		    	reloProcPt2 				= Pair.of(report.getReloProcPt2(), "reloProcPt2");
		    	
		    	// Gather input from the inventory form.
		    	invDevSelString = report.getInvDevSel();
				invDevNameAndValTempArray1 = invDevSelString.split("\\^");
				for (String item : invDevNameAndValTempArray1){
					invDevNameAndValTempArray2 = item.split(",");
					invDevNameAndValMap.put(invDevNameAndValTempArray2[0], Integer.parseInt(invDevNameAndValTempArray2[1]));
				}
		    	
		    	/*
		    	 * New Form elements.
		    	*/
				
				 Map<String, Integer> cpuElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName()) 
								 && item.getBusReqGrpId().equals("CPU")){
							 cpuElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> scannerElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName()) 
								 && item.getBusReqGrpId().equals("SCANNER")){
							 scannerElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> utilityElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName()) 
								 && item.getBusReqGrpId().equals("UTILITY")){
							 utilityElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> readerElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName()) 
								 && item.getBusReqGrpId().equals("READER")){
							 readerElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> storageElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName()) 
								 && item.getBusReqGrpId().equals("STORAGE")){
							 storageElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> legacyElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : newDevNameAndValMap.entrySet()){
					 boolean isLegacy = true;
					 for(NewDevSettings item : newDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getNewDevName())){isLegacy = false;}
					 }
					 if(isLegacy){legacyElements.put(entry.getKey(), entry.getValue());}
				 }
				 
				 
	    			
		    	/*
		    	 * Inventory form elements.
		    	*/
				 Map<String, Integer> iCpuElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName()) 
								 && item.getBusReqGrpId().equals("CPU")){
							 iCpuElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> iScannerElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName()) 
								 && item.getBusReqGrpId().equals("SCANNER")){
							 iScannerElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> iUtilityElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName()) 
								 && item.getBusReqGrpId().equals("UTILITY")){
							 iUtilityElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> iReaderElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName()) 
								 && item.getBusReqGrpId().equals("READER")){
							 iReaderElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> iStorageElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName()) 
								 && item.getBusReqGrpId().equals("STORAGE")){
							 iStorageElements.put(entry.getKey(), entry.getValue());
						 }
					 }
				 }
				 
				 Map<String, Integer> iLegacyElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> entry : invDevNameAndValMap.entrySet()){
					 boolean isLegacy = true;
					 for(InvDevSettings item : invDevNameAndBusReqGrpIdList){
						 if(entry.getKey().equals(item.getInvDevName())){isLegacy = false;}
					 }
					 if(isLegacy){iLegacyElements.put(entry.getKey(), entry.getValue());}
				 }
			
		    			
		    	/*
		    	 * Subtraction of Inventory form element values from New form element values
		    	 * to get the remaining amount of equipment needed at the deployment location
		    	 * 
		    	 * if -> new - inventory > 0 -> sNew = new - inventory
		    	 * 
		    	 * else -> sNew = 0
		    	 * 
		    	*/
				 Map<String, Integer> sCpuElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : cpuElements.entrySet()){
					 boolean nCpuMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iCpuElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nCpuMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sCpuElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sCpuElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nCpuMatchIsFound){sCpuElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
				 
				 Map<String, Integer> sScannerElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : scannerElements.entrySet()){
					 boolean nScannerMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iScannerElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nScannerMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sScannerElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sScannerElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nScannerMatchIsFound){sScannerElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
				 
				 Map<String, Integer> sUtilityElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : utilityElements.entrySet()){
					 boolean nUtilityMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iUtilityElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nUtilityMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sUtilityElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sUtilityElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nUtilityMatchIsFound){sUtilityElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
				 
				 Map<String, Integer> sReaderElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : readerElements.entrySet()){
					 boolean nReaderMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iReaderElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nReaderMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sReaderElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sReaderElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nReaderMatchIsFound){sReaderElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
				 
				 Map<String, Integer> sStorageElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : storageElements.entrySet()){
					 boolean nStorageMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iStorageElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nStorageMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sStorageElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sStorageElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nStorageMatchIsFound){sStorageElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
				 
				 Map<String, Integer> sLegacyElements = new HashMap<String, Integer>();
				 for(Map.Entry<String, Integer> nEntry : legacyElements.entrySet()){
					 boolean nLegacyMatchIsFound = false;
					 for(Map.Entry<String, Integer> iEntry : iLegacyElements.entrySet()){
						 if(iEntry.getKey().equals(nEntry.getKey())){
							 nLegacyMatchIsFound = true;
							 if (nEntry.getValue() - iEntry.getValue() > 0){
								 sLegacyElements.put(iEntry.getKey(), nEntry.getValue() - iEntry.getValue());
							 }else{sLegacyElements.put(iEntry.getKey(), 0);}
						 }
					 }
					 if(!nLegacyMatchIsFound){sLegacyElements.put(nEntry.getKey(), nEntry.getValue());}
				 }
		    	
		    	/*
		    	 * New form logic array used to determine 
		    	 * what new equipment is requested, if any. 
		    	*/
				 
				 Boolean needsPC = false;
				 for(Map.Entry<String, Integer> entry : cpuElements.entrySet()){
					 if(entry.getValue() > 0){needsPC = true;}
				 }
				 
				 Boolean needsScnr = false;
				 for(Map.Entry<String, Integer> entry : scannerElements.entrySet()){
					 if(entry.getValue() > 0){needsScnr = true;}
				 }
				 
				 Boolean needsUtilities = false;
				 for(Map.Entry<String, Integer> entry : utilityElements.entrySet()){
					 if(entry.getValue() > 0){needsUtilities = true;}
				 }
				 
				 Boolean needsReader = false;
				 for(Map.Entry<String, Integer> entry : readerElements.entrySet()){
					 if(entry.getValue() > 0){needsReader = true;}
				 }
				 
				 Boolean needsStorage = false;
				 for(Map.Entry<String, Integer> entry : storageElements.entrySet()){
					 if(entry.getValue() > 0){needsStorage = true;}
				 }
				 
				 Boolean needsLegacy = false;
				 for(Map.Entry<String, Integer> entry : legacyElements.entrySet()){
					 if(entry.getValue() > 0){needsLegacy = true;}
				 }
						
		        /*
		         * Relocation form logic array used to determine
		         * what equipment is being moved, if any.
		        */
				 
				 Boolean movingPC = false;
				 for(Map.Entry<String, Integer> entry : iCpuElements.entrySet()){
					 if(entry.getValue() > 0){movingPC = true;}
				 }
				 
				 Boolean movingScnr = false;
				 for(Map.Entry<String, Integer> entry : iScannerElements.entrySet()){
					 if(entry.getValue() > 0){movingScnr = true;}
				 }
				 
				 Boolean movingUtilities = false;
				 for(Map.Entry<String, Integer> entry : iUtilityElements.entrySet()){
					 if(entry.getValue() > 0){movingUtilities = true;}
				 }
				 
				 Boolean movingReader = false;
				 for(Map.Entry<String, Integer> entry : iReaderElements.entrySet()){
					 if(entry.getValue() > 0){movingReader = true;}
				 }
				 
				 Boolean movingStorage = false;
				 for(Map.Entry<String, Integer> entry : iStorageElements.entrySet()){
					 if(entry.getValue() > 0){movingStorage = true;}
				 }
				 
				 Boolean movingLegacy = false;
				 for(Map.Entry<String, Integer> entry : iLegacyElements.entrySet()){
					 if(entry.getValue() > 0){movingLegacy = true;}
				 }
    		
		        /*
		         * Customization form logic array used to
		         * determine what additional equipment is being
		         * requested, if any.
		        */
				 
				 Boolean missingPC = false;
				 for(Map.Entry<String, Integer> entry : sCpuElements.entrySet()){
					 if(entry.getValue() > 0){missingPC = true;}
				 }
				 
				 Boolean missingScnr = false;
				 for(Map.Entry<String, Integer> entry : sScannerElements.entrySet()){
					 if(entry.getValue() > 0){missingScnr = true;}
				 }
				 
				 Boolean missingUtilities = false;
				 for(Map.Entry<String, Integer> entry : sUtilityElements.entrySet()){
					 if(entry.getValue() > 0){missingUtilities = true;}
				 }
				 
				 Boolean missingReader = false;
				 for(Map.Entry<String, Integer> entry : sReaderElements.entrySet()){
					 if(entry.getValue() > 0){missingReader = true;}
				 }
				 
				 Boolean missingStorage = false;
				 for(Map.Entry<String, Integer> entry : sStorageElements.entrySet()){
					 if(entry.getValue() > 0){missingStorage = true;}
				 }
				 
				 Boolean missingLegacy = false;
				 for(Map.Entry<String, Integer> entry : sLegacyElements.entrySet()){
					 if(entry.getValue() > 0){missingLegacy = true;}
				 }
			
		        
		        // Boolean array used to determine what, in general, the customer needs.
		        Boolean[] custNeeds = new Boolean[]
		        		{needsPC, needsScnr, needsUtilities, needsReader, needsStorage, needsLegacy};
        
		        // Boolean array used to determine what, in general, the customer is moving.
		        Boolean[] custMoving = new Boolean[]
		        		{movingPC, movingScnr, movingUtilities, movingReader, movingStorage, movingLegacy};
		        
		        // Boolean array used to determine what, in general, the customer is missing.
		        Boolean[] custMissing = new Boolean[]
		        		{missingPC, missingScnr, missingUtilities, missingReader, missingStorage, missingLegacy};		
	        
		        /*
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         * ****************EDITING OF THE HEADING INFORMATION BEGINS HERE******************
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         */
		        
		        // Edit the SR Number at R0 C8 with "SR_NO".
		        Row row0 = sheet.getRow(0);
		        Cell row0cell8 = row0.getCell(8);	    	        
		        row0cell8.setCellValue(srNo.getFirst());
		        
		        // Edit the file name at R1 C8 with "SR_NO-PROCESS_NAME_BRD.xlsx".
		        Row row1 = sheet.getRow(1);
		        Cell row1cell8 = row1.getCell(8);	    	        
		        row1cell8.setCellValue(srFilename);
		        
		        // Edit the title box with the "SR_NO - PROCESS_NAME".
		        Row row13 = sheet.getRow(13);
		        Cell row13cell1 = row13.getCell(1);
		        row13cell1.setCellValue(srNo.getFirst() + " - " + procName.getFirst());
		        
		        // Edit the Requirements.
		        Row row54 = sheet.getRow(54);
		        Cell row54cell1 = row54.getCell(1);
		        row54cell1.setCellValue(srNo.getFirst() + " - " + procName.getFirst());

		        /*
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         * **************EDITING OF THE BUSINESS PROCESS TITLE BEGINS HERE*****************
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         */
		        
		        // Enter the first service request's location information.
		        Row row62 = sheet.createRow(GLOBAL_START - 1);
		        Cell row62cell8 = row62.createCell(8);
		        Cell row62cell1 = row62.createCell(1);
		        row62cell1.setCellValue(procName.getFirst() + " - " + reqType.getFirst());
		        row62cell1.setCellStyle(allBorders);
		        
		        String locationInformation = "";
		        // If new request.
		        if (reqType.getFirst().equals("New")){
		        	locationInformation += 
	        			"\n\tAt:  " + destCol.getFirst() + 
	        			" / " + destProcPnt1.getFirst();
	        		if (!destProcPnt2.getFirst().equals("")){
		        		locationInformation += " | " + destProcPnt2.getFirst();
		        	}
		        }
		        
		        // If relocation or relocate and customize request.
		        else if (reqType.getFirst().equals("Relocate") | reqType.getFirst().equals("Relocate and Customize")){
		        	locationInformation = 
			        	"\n\tFrom: " + reloColLoc.getFirst() + 
			        	" / " + reloProcPt1.getFirst();
		        	// If between two process points
		        	if (!reloProcPt2.getFirst().equals("")){
		        		locationInformation += " | " + reloProcPt2.getFirst();
		        	}
	        		locationInformation += 
	        			"\n\tTo:  " + destCol.getFirst() + 
	        			" / " + destProcPnt1.getFirst();
	        		if (!destProcPnt2.getFirst().equals("")){
		        		locationInformation += " | " + destProcPnt2.getFirst();
		        	}
		        }
				Cell row62cell2 = row62.createCell(2);
				row62cell2.setCellStyle(allBorders);
				
				Cell row62cell3 = row62.createCell(3);
				row62cell3.setCellStyle(allBorders);
				
				Cell row62cell4 = row62.createCell(4);
				Cell row62cell5 = row62.createCell(5);
				CellRangeAddress mergedRow62Cell4And5 = new CellRangeAddress(GLOBAL_START - 1, GLOBAL_START - 1, 
						row62cell4.getColumnIndex(), row62cell5.getColumnIndex());
				sheet.addMergedRegion(mergedRow62Cell4And5);
				RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedRow62Cell4And5, sheet, workbook);
				RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedRow62Cell4And5, sheet, workbook);
				RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedRow62Cell4And5, sheet, workbook);
				RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedRow62Cell4And5, sheet, workbook);

				Cell row62cell6 = row62.createCell(6);
				row62cell6.setCellStyle(allBorders);
				
				Cell row62cell7 = row62.createCell(7);
				row62cell7.setCellStyle(allBorders);
				
		        row62cell8.setCellValue(locationInformation);
		        row62cell8.setCellStyle(allBorders);
		        
		        row62.setHeight((short)-1);
		        
		        /*
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         * *****************EDITING OF THE HARDWARE SECTION BEGINS HERE********************
		         * ********************************************************************************
		         * ********************************************************************************
		         * ********************************************************************************
		         */
		        
		        /*
		         * THE LOGIC:
		         * 
		         * 1) Determine the structure of the hardware section by obtaining the request type information.
		         * 
		         * 2) If we have one or more "New" requests, the pattern will be: 
		         * 		One title row followed by n hardware listing rows (Number of true values in the custNeeds Array). 
		         * 		Once k title/listing patterns have been written, then the vendor section will follow.
		         * 
		         * 3) If we have one or more "Relocate" requests, the pattern will be:
		         * 		One title row followed by n hardware listing rows (Number of true values in the custNeeds Array). 
		         * 		Once k title/listing patterns have been written, then the vendor section will follow.
		         * 
		         * 4) If we have one or more "Relocate and Customize" requests, the pattern will be:
		         * 		One title row followed by n hardware listing rows (Number of true values in the custNeeds Array). 
		         * 		Once k title/listing patterns have been written,  a series of j hardware listing rows will follow
		         * 		(where j is the number of values in the custMissing array). Then the vendor section will follow.
		         */
		        
		        
		        /*
		         * Loop that will write new deployment information.
		        */
		        if (reqType.getFirst().equals("New")){
		        	Integer HARDWARE_ROWS_START = GLOBAL_START;
		        	Integer HARDWARE_ROWS_END = HARDWARE_ROWS_START + HARDWARE_ROWS_MAX - 1;
		        	Integer BIG_BOX_END = HARDWARE_ROWS_START - 1;
			        Integer custNeedsIndex = 0;
			        for(int rowNo = HARDWARE_ROWS_START; rowNo <= HARDWARE_ROWS_END; rowNo++){
			        	if (custNeedsIndex < custNeeds.length){
				        	// If they requested some equipment.
				        	if(custNeeds[custNeedsIndex]){
	        					Row writingRow = sheet.createRow(rowNo);
	        					writingRow.createCell(1);
	        					Cell reqNoCell = writingRow.createCell(2);
	        					reqNoCell.setCellStyle(allBorders);
	        					Cell busReqDescriptionCell = writingRow.createCell(3);
	        					busReqDescriptionCell.setCellStyle(allBorders);
	        					
	        					Cell priorityCell = writingRow.createCell(4);
	        					Cell blankCell = writingRow.createCell(5);
	        					CellRangeAddress mergedPriorityBlank = new CellRangeAddress(rowNo, rowNo, 
	        							priorityCell.getColumnIndex(), blankCell.getColumnIndex());
	        					sheet.addMergedRegion(mergedPriorityBlank);
	        					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);

	        					Cell dateCell = writingRow.createCell(6);
	        					dateCell.setCellStyle(allBorders);
	        					Cell versionCell = writingRow.createCell(7);
	        					versionCell.setCellStyle(allBorders);
	        			        Cell hardwareCell = writingRow.createCell(8);
	        			        hardwareCell.setCellStyle(allBorders);
	        			        
				        		// Find out what they requested.
				        		switch(custNeedsIndex){
				        			// If customer requested a PC.
				        			case 0:
			        			        String listOfCPUHardware = "";
			        			        for(Map.Entry<String, Integer> entry : cpuElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("PC hardware required to implement station.");
			        			        		listOfCPUHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfCPUHardware);
			        			        BIG_BOX_END++;
				        				break;
				        				
				        			// If customer requested a Scanner.
				        			case 1:
			        			        String listOfScnrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : scannerElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Scanner hardware required to implement station.");
			        			        		listOfScnrHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfScnrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Power, Network, or wiring.
				        			case 2:
			        			        String listOfUtilHardware = "";
			        			        for(Map.Entry<String, Integer> entry : utilityElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Utility hardware required to implement station.");
			        			        		listOfUtilHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfUtilHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested a Reader.
				        			case 3:
			        			        String listOfRdrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : readerElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Reader hardware required to implement station.");
			        			        		listOfRdrHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfRdrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Storage.
				        			case 4:
			        			        String listOfStorageHardware = "";
			        			        for(Map.Entry<String, Integer> entry : storageElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Storage hardware required to implement station.");
			        			        		listOfStorageHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfStorageHardware);
			        			        BIG_BOX_END++;
				        				break;
				        			// If customer requested Legacy.
				        			case 5:
			        			        String listOfLegacyHardware = "";
			        			        for(Map.Entry<String, Integer> entry : legacyElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Legacy hardware required to implement station.");
			        			        		listOfLegacyHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfLegacyHardware);
			        			        BIG_BOX_END++;
				        				break;
				        		}
				        		custNeedsIndex++;
				        	}
				        	// else they did not request some equipment.
				        	else{
				        		custNeedsIndex++;
				        		rowNo--;
				        	}
			        	}
			        }
			        //merge the cells for big box
					CellRangeAddress mergedBusinessProcCells = new CellRangeAddress(HARDWARE_ROWS_START, BIG_BOX_END, 1, 1);
					sheet.addMergedRegion(mergedBusinessProcCells);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					GLOBAL_START = BIG_BOX_END + 2;
		        }
		        
		        /*
		         * Loop that will write relocation information 
		        */
		        else if (reqType.getFirst().equals("Relocate")){
		        	Integer HARDWARE_ROWS_START = GLOBAL_START;
		        	Integer HARDWARE_ROWS_END = HARDWARE_ROWS_START + HARDWARE_ROWS_MAX - 1;
		        	Integer BIG_BOX_END = HARDWARE_ROWS_START - 1;
			        Integer custRelocateIndex = 0;
			        for(int rowNo = HARDWARE_ROWS_START; rowNo <= HARDWARE_ROWS_END; rowNo++){
			        	if (custRelocateIndex < custNeeds.length){
				        	// If they requested some equipment.
				        	if(custMoving[custRelocateIndex]){
	        					Row writingRow = sheet.createRow(rowNo);
	        					writingRow.createCell(1);
	        					Cell reqNoCell = writingRow.createCell(2);
	        					reqNoCell.setCellStyle(allBorders);
	        					Cell busReqDescriptionCell = writingRow.createCell(3);
	        					busReqDescriptionCell.setCellStyle(allBorders);
	        					
	        					Cell priorityCell = writingRow.createCell(4);
	        					Cell blankCell = writingRow.createCell(5);
	        					CellRangeAddress mergedPriorityBlank = new CellRangeAddress(rowNo, rowNo, 
	        							priorityCell.getColumnIndex(), blankCell.getColumnIndex());
	        					sheet.addMergedRegion(mergedPriorityBlank);
	        					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);

	        					Cell dateCell = writingRow.createCell(6);
	        					dateCell.setCellStyle(allBorders);
	        					Cell versionCell = writingRow.createCell(7);
	        					versionCell.setCellStyle(allBorders);
	        			        Cell hardwareCell = writingRow.createCell(8);
	        			        hardwareCell.setCellStyle(allBorders);
	
				        		// Find out what they requested.
				        		switch(custRelocateIndex){
				        			// If customer requested a PC.
				        			case 0:
			        			        String listOfCPUHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iCpuElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("PC hardware that requires relocation.");
			        			        		listOfCPUHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfCPUHardware);
			        			        BIG_BOX_END++;
				        				break;
				        				
				        			// If customer requested a Scanner.
				        			case 1:
			        			        String listOfScnrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iScannerElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Scanner hardware that requires relocation.");
			        			        		listOfScnrHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfScnrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Power, Network, or wiring.
				        			case 2:
			        			        String listOfUtilHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iUtilityElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Utility hardware that requires relocation.");
			        			        		listOfUtilHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfUtilHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested a Reader.
				        			case 3:
			        			        String listOfRdrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iReaderElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Reader hardware that requires relocation.");
			        			        		listOfRdrHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfRdrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Storage.
				        			case 4:
			        			        String listOfStorageHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iStorageElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Storage hardware that requires relocation.");
			        			        		listOfStorageHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfStorageHardware);
			        			        BIG_BOX_END++;
				        				break;
				        			// If customer requested Legacy.
				        			case 5:
			        			        String listOfLegacyHardware = "";
			        			        for(Map.Entry<String, Integer> entry : iLegacyElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Legacy hardware that requires relocation.");
			        			        		listOfLegacyHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfLegacyHardware);
			        			        BIG_BOX_END++;
				        				break;
				        		}
				        		custRelocateIndex++;
				        	}
				        	// else they did not request some equipment.
				        	else{
				        		custRelocateIndex++;
				        		rowNo--;
				        	}
			        	}
			        }
			        //merge the cells for big box
					CellRangeAddress mergedBusinessProcCells = new CellRangeAddress(HARDWARE_ROWS_START, BIG_BOX_END, 1, 1);
					sheet.addMergedRegion(mergedBusinessProcCells);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					GLOBAL_START = BIG_BOX_END + 2;
		        }
			
		        
		        /*
		         * Loop that will write relocate and customize information 
		        */
		        else if (reqType.getFirst().equals("Relocate and Customize")){
		        	Integer HARDWARE_ROWS_START = GLOBAL_START;
		        	Integer HARDWARE_ROWS_END = HARDWARE_ROWS_START + HARDWARE_ROWS_MAX - 1;
		        	Integer BIG_BOX_END = HARDWARE_ROWS_START - 1;
		        	// What they want at the new location
		        	Integer custNeedsIndex = 0;
			        for(int rowNo = HARDWARE_ROWS_START; rowNo <= HARDWARE_ROWS_END; rowNo++){
			        	if (custNeedsIndex < custNeeds.length){
				        	// If they requested some equipment.
				        	if(custNeeds[custNeedsIndex]){
	        					Row writingRow = sheet.createRow(rowNo);
	        					writingRow.createCell(1);
	        					Cell reqNoCell = writingRow.createCell(2);
	        					reqNoCell.setCellStyle(allBorders);
	        					Cell busReqDescriptionCell = writingRow.createCell(3);
	        					busReqDescriptionCell.setCellStyle(allBorders);
	        					
	        					Cell priorityCell = writingRow.createCell(4);
	        					Cell blankCell = writingRow.createCell(5);
	        					CellRangeAddress mergedPriorityBlank = new CellRangeAddress(rowNo, rowNo, 
	        							priorityCell.getColumnIndex(), blankCell.getColumnIndex());
	        					sheet.addMergedRegion(mergedPriorityBlank);
	        					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);

	        					Cell dateCell = writingRow.createCell(6);
	        					dateCell.setCellStyle(allBorders);
	        					Cell versionCell = writingRow.createCell(7);
	        					versionCell.setCellStyle(allBorders);
	        			        Cell hardwareCell = writingRow.createCell(8);
	        			        hardwareCell.setCellStyle(allBorders);
	
				        		// Find out what they requested.
				        		switch(custNeedsIndex){
				        			// If customer requested a PC.
				        			case 0:
			        			        String listOfCPUHardware = "";
			        			        for(Map.Entry<String, Integer> entry : cpuElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("PC hardware required to implement station.");
			        			        		listOfCPUHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfCPUHardware);
			        			        BIG_BOX_END++;
				        				break;
				        				
				        			// If customer requested a Scanner.
				        			case 1:
			        			        String listOfScnrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : scannerElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Scanner hardware required to implement station.");
			        			        		listOfScnrHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfScnrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Power, Network, or wiring.
				        			case 2:
			        			        String listOfUtilHardware = "";
			        			        for(Map.Entry<String, Integer> entry : utilityElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Utility hardware required to implement station.");
			        			        		listOfUtilHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfUtilHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested a Reader.
				        			case 3:
			        			        String listOfRdrHardware = "";
			        			        for(Map.Entry<String, Integer> entry : readerElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Reader hardware required to implement station.");
			        			        		listOfRdrHardware +=  entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfRdrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Storage.
				        			case 4:
			        			        String listOfStorageHardware = "";
			        			        for(Map.Entry<String, Integer> entry : storageElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Storage hardware required to implement station.");
			        			        		listOfStorageHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfStorageHardware);
			        			        BIG_BOX_END++;
				        				break;
			        				// If customer requested Storage.
				        			case 5:
			        			        String listOfLegacyHardware = "";
			        			        for(Map.Entry<String, Integer> entry : legacyElements.entrySet()){
			        			        	if(entry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Legacy hardware required to implement station.");
			        			        		listOfLegacyHardware += entry.getValue() + " x " +  entry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfLegacyHardware);
			        			        BIG_BOX_END++;
				        				break;
				        		}
				        		custNeedsIndex++;
				        	}
				        	// else they did not request some equipment.
				        	else{
				        		custNeedsIndex++;
				        		rowNo--;
				        	}
			        	}
			        }
			        //merge the cells for big box
					CellRangeAddress mergedBusinessProcCells = new CellRangeAddress(HARDWARE_ROWS_START, BIG_BOX_END, 1, 1);
					sheet.addMergedRegion(mergedBusinessProcCells);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
					
			        HARDWARE_ROWS_START = HARDWARE_ROWS_START + (BIG_BOX_END - HARDWARE_ROWS_START) + 1;
		        	HARDWARE_ROWS_END = HARDWARE_ROWS_START + HARDWARE_ROWS_MAX - 1;
		        	BIG_BOX_END = HARDWARE_ROWS_START - 1;
		        	// What is still needed after moving the old equipment.
		        	Integer custMissingIndex = 0;
			        for(int rowNo = HARDWARE_ROWS_START; rowNo <= HARDWARE_ROWS_END; rowNo++){
			        	if (custMissingIndex < custNeeds.length){
				        	// If they requested some equipment.
				        	if(custMissing[custMissingIndex]){
	        					Row writingRow = sheet.createRow(rowNo);
	        					writingRow.createCell(1);
	        					Cell reqNoCell = writingRow.createCell(2);
	        					reqNoCell.setCellStyle(allBorders);
	        					Cell busReqDescriptionCell = writingRow.createCell(3);
	        					busReqDescriptionCell.setCellStyle(allBorders);
	        					
	        					Cell priorityCell = writingRow.createCell(4);
	        					Cell blankCell = writingRow.createCell(5);
	        					CellRangeAddress mergedPriorityBlank = new CellRangeAddress(rowNo, rowNo, 
	        							priorityCell.getColumnIndex(), blankCell.getColumnIndex());
	        					sheet.addMergedRegion(mergedPriorityBlank);
	        					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
	        					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);

	        					Cell dateCell = writingRow.createCell(6);
	        					dateCell.setCellStyle(allBorders);
	        					Cell versionCell = writingRow.createCell(7);
	        					versionCell.setCellStyle(allBorders);
	        			        Cell hardwareCell = writingRow.createCell(8);
	        			        hardwareCell.setCellStyle(allBorders);
	
				        		// Find out what they requested.
				        		switch(custMissingIndex){
				        			// If customer requested a PC.
				        			case 0:
			        			        String listOfCPUHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sCpuElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining PC hardware required to implement station.");
			        			        		listOfCPUHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfCPUHardware);
			        			        BIG_BOX_END++;
				        				break;
				        				
				        			// If customer requested a Scanner.
				        			case 1:
			        			        String listOfScnrHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sScannerElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining Scanner hardware required to implement station.");
			        			        		listOfScnrHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfScnrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Power, Network, or wiring.
				        			case 2:
			        			        String listOfUtilHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sUtilityElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining Utility hardware required to implement station.");
			        			        		listOfUtilHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfUtilHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested a Reader.
				        			case 3:
			        			        String listOfRdrHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sReaderElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining Reader hardware required to implement station.");
			        			        		listOfRdrHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfRdrHardware);
			        			        BIG_BOX_END++;
				        				break;
					        		// If customer requested Storage.
				        			case 4:
			        			        String listOfStorageHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sStorageElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining Storage hardware required to implement station.");
			        			        		listOfStorageHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfStorageHardware);
			        			        BIG_BOX_END++;
				        				break;
			        				// If customer requested Legacy.
				        			case 5:
			        			        String listOfLegacyHardware = "";
			        			        for(Map.Entry<String, Integer> sEntry : sLegacyElements.entrySet()){
			        			        	if(sEntry.getValue() != 0){
			        			        		priorityCell.setCellValue("Must");
			        			        		busReqDescriptionCell.setCellValue("Remaining Legacy hardware required to implement station.");
			        			        		listOfLegacyHardware += sEntry.getValue() + " x " +  sEntry.getKey() + "\n";
			        			        	}
			        			        }
			        			        hardwareCell.setCellValue(listOfLegacyHardware);
			        			        BIG_BOX_END++;
				        				break;
				        		}
				        		custMissingIndex++;
				        	}
				        	// else they did not request some equipment.
				        	else{
				        		custMissingIndex++;
				        		rowNo--;
				        	}
			        	}
			        }
			        //merge the cells for big box
					CellRangeAddress mergedBusinessProcCells2 = new CellRangeAddress(HARDWARE_ROWS_START, BIG_BOX_END, 1, 1);
					sheet.addMergedRegion(mergedBusinessProcCells2);
					RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedBusinessProcCells2, sheet, workbook);
					RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedBusinessProcCells2, sheet, workbook);
					RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedBusinessProcCells2, sheet, workbook);
					RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedBusinessProcCells2, sheet, workbook);
					GLOBAL_START = BIG_BOX_END + 2;
		        }
			}
			
			
			 /*
	         * ********************************************************************************
	         * ********************************************************************************
	         * ********************************************************************************
	         * *********EDITING OF THE NETWORK/POWER/VENDOR WORK SECTION BEGINS HERE***********
	         * *******************...YOU ARE NOW OUTSIDE THE GIANT FOR LOOP********************
	         * ********************************************************************************
	         * ********************************************************************************
	         */
	        
	        Integer NPV_ROWS_START = GLOBAL_START - 1;
        	Integer NPV_ROWS_END = NPV_ROWS_START + HARDWARE_ROWS_MAX;
        	
        	/*
        	 * Here is the code the generates the Title Bar for "NETWORK, POWER, VENDOR WORK"
        	 */
	        
        	// Create the Network, Power, Vendor Work Bar.
	        Row titleRow 				= sheet.createRow(NPV_ROWS_START);
	        Cell titleBusinessProcCell 	= titleRow.createCell(1);
			titleRow.createCell(2);
			titleRow.createCell(3);
			titleRow.createCell(4);
			titleRow.createCell(5);
			titleRow.createCell(6);
			titleRow.createCell(7);
	        Cell titleHardwareCell 		= titleRow.createCell(8);
	        // Declare the Bar's cell range.
	        CellRangeAddress mergedTitleBar = new CellRangeAddress(NPV_ROWS_START, NPV_ROWS_START, 
	        		titleBusinessProcCell.getColumnIndex(), titleHardwareCell.getColumnIndex());
			// Apply the font.
			titleBarStyle.setFont(bar_font);
			// Add fill color and pattern.
			titleBarStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			titleBarStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			// Apply the style to each cell in the row.
			for(int i = mergedTitleBar.getFirstColumn(); i <= mergedTitleBar.getLastColumn(); i++){
				titleRow.getCell(i).setCellStyle(titleBarStyle);
			}
			titleRow.getCell(mergedTitleBar.getFirstColumn()).setCellValue("NETWORK, POWER, VENDOR WORK");
			sheet.addMergedRegion(mergedTitleBar);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedTitleBar, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedTitleBar, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedTitleBar, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedTitleBar, sheet, workbook);
			
			/*
			 * End of Title Bar code segment.
			 */
			
			// Create the description row.
			
			NPV_ROWS_START = NPV_ROWS_START + 1;
			
	        Row descriptionRow 						= sheet.createRow(NPV_ROWS_START);
	        Cell descriptionBusinessProcCell 		= descriptionRow.createCell(1);
	        Cell descriptionReqNoCell 				= descriptionRow.createCell(2);
	        Cell descriptionBusReqDescriptionCell 	= descriptionRow.createCell(3);
	        Cell descriptionPriorityCell 			= descriptionRow.createCell(4);
	        Cell descriptionBlankCell 				= descriptionRow.createCell(5);
	        Cell descriptionDateCell 				= descriptionRow.createCell(6);
	        Cell descriptionVersionCell 			= descriptionRow.createCell(7);
	        Cell descriptionHardwareCell 			= descriptionRow.createCell(8);
	        
	        descriptionBusinessProcCell.setCellStyle(allBorders);
	        descriptionReqNoCell.setCellStyle(allBorders);
	        descriptionBusReqDescriptionCell.setCellStyle(allBorders);
	        descriptionPriorityCell.setCellStyle(allBorders);
	        descriptionBlankCell.setCellStyle(allBorders);
	        descriptionDateCell.setCellStyle(allBorders);
	        descriptionVersionCell.setCellStyle(allBorders);
	        descriptionHardwareCell.setCellStyle(allBorders);
	        
	        CellRangeAddress mergedDescPriorityBlank = new CellRangeAddress(NPV_ROWS_START, NPV_ROWS_START, 
	        		descriptionPriorityCell.getColumnIndex(), descriptionBlankCell.getColumnIndex());
			sheet.addMergedRegion(mergedDescPriorityBlank);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedDescPriorityBlank, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedDescPriorityBlank, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedDescPriorityBlank, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedDescPriorityBlank, sheet, workbook);
	        
			// Create the information rows
			
	        NPV_ROWS_START = NPV_ROWS_START + 1;
					
	        for(int rowNo = NPV_ROWS_START; rowNo <= NPV_ROWS_END; rowNo++){
   				Row writingRow = sheet.createRow(rowNo);
   		        writingRow.createCell(1);
   		        Cell reqNoCell = writingRow.createCell(2);
   				Cell busReqDescriptionCell = writingRow.createCell(3);
    			Cell priorityCell = writingRow.createCell(4);
    			Cell blankCell = writingRow.createCell(5);
    			Cell dateCell = writingRow.createCell(6);
    			Cell versionCell = writingRow.createCell(7);
    			Cell hardwareCell = writingRow.createCell(8);
    			
    			reqNoCell.setCellStyle(allBorders);
    			busReqDescriptionCell.setCellStyle(allBorders);
    			dateCell.setCellStyle(allBorders);
    			versionCell.setCellStyle(allBorders);
    			hardwareCell.setCellStyle(allBorders);
    					
    			CellRangeAddress mergedPriorityBlank = new CellRangeAddress(rowNo, rowNo, 
    					priorityCell.getColumnIndex(), blankCell.getColumnIndex());
    			sheet.addMergedRegion(mergedPriorityBlank);
    			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
    			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
    			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
    			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedPriorityBlank, sheet, workbook);
    			        
	        }
	        //merge the cells for big box.
			CellRangeAddress mergedBusinessProcCells = new CellRangeAddress(NPV_ROWS_START, NPV_ROWS_END, 1, 1);
			sheet.addMergedRegion(mergedBusinessProcCells);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedBusinessProcCells, sheet, workbook);
			
			NPV_ROWS_START = NPV_ROWS_END + 1;
			
			//Create the End of Requirements Bar
			
			Row finalRow 				= sheet.createRow(NPV_ROWS_START);
	        Cell finalBusinessProcCell 	= finalRow.createCell(1);
			finalRow.createCell(2);
			finalRow.createCell(3);
			finalRow.createCell(4);
			finalRow.createCell(5);
			finalRow.createCell(6);
			finalRow.createCell(7);
	        Cell finalHardwareCell 		= finalRow.createCell(8);
			
			// Merge the cells for end of requirements row.
			CellRangeAddress mergedNPVFinalRow = new CellRangeAddress(NPV_ROWS_START, NPV_ROWS_START, 
					finalBusinessProcCell.getColumnIndex(), finalHardwareCell.getColumnIndex());
			CellStyle mergedNPVFinalRowStyle = workbook.createCellStyle();
			mergedNPVFinalRowStyle.setAlignment(CellStyle.ALIGN_CENTER);
			for(int i = mergedNPVFinalRow.getFirstColumn(); i <= mergedNPVFinalRow.getLastColumn(); i++){
				finalRow.getCell(i).setCellStyle(mergedNPVFinalRowStyle);
			}
			finalRow.getCell(mergedNPVFinalRow.getFirstColumn()).setCellValue("END OF REQUIREMENTS");
			sheet.addMergedRegion(mergedNPVFinalRow);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedNPVFinalRow, sheet, workbook);
			RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedNPVFinalRow, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedNPVFinalRow, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedNPVFinalRow, sheet, workbook);
			
			
			// Add thick outer borders to the requirements form.
			CellRangeAddress requirementsForm = new CellRangeAddress(TRUE_START, NPV_ROWS_START, 
					finalBusinessProcCell.getColumnIndex(), finalHardwareCell.getColumnIndex());
			RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, requirementsForm, sheet, workbook);
			RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, requirementsForm, sheet, workbook);
			RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, requirementsForm, sheet, workbook);
			
			/*
			 * ********************************************************************************
	         * ********************************************************************************
	         * ********************************************************************************
	         * *INSERTION OF THE PHOTOS THAT WERE UPLOADED FOR DPHOTOS AND RPHOTOS STARTS HERE*
	         * ********************************************************************************
	         * ********************************************************************************
	         * ********************************************************************************
			 */
			// Variables that carry over with every report loop iteration
			Integer PIC_ROWS_START = NPV_ROWS_START + 2;
			Integer PIC_COLS_START = 1;
			Integer PIC_ROWS_TRUE_START = PIC_ROWS_START;
			Integer PIC_COLS_TRUE_START = PIC_COLS_START;
			
			// Variables that refresh with every report loop iteration
			Boolean destPhotosExist = false;
			Boolean reloPhotosExist = false;
			for(ServiceRequest report : serviceReports){
				destPhotosExist = false;
				reloPhotosExist = false;
				Blob[] destPhotos = {report.getDestPhoto1(), report.getDestPhoto2(), report.getDestPhoto3()};
				Blob[] reloPhotos = {report.getReloPhoto1(), report.getReloPhoto2(), report.getReloPhoto3()};
				
				for (Blob Photo : reloPhotos){
					if (Photo != null && Photo.length() > 0){
						reloPhotosExist = true;
					}
				}
				
				for (Blob Photo : destPhotos){
					if (Photo != null && Photo.length() > 0){
						destPhotosExist = true;
						reqType 				= Pair.of(report.getReqType(), "reqType");
						lineNo					= Pair.of(report.getLineNo(), "lineNo");
						deptName				= Pair.of(report.getDeptName(), "deptName");
						simProcPt				= Pair.of(report.getSimProcPt(), "simProcPt");
						curStaTyp				= Pair.of(report.getCurStaTyp(), "curStaTyp");
						nodeIdNum				= Pair.of(report.getNodeIdNum(), "nodeIdNum");
						cntlrQty				= Pair.of(report.getCntlrQty(), "cntlrQty");
						
						Row pictureRow 						= sheet.createRow(PIC_ROWS_START);
				        Cell pictureBusinessProcCell 		= pictureRow.createCell(1);
				        pictureRow.createCell(2);
				        pictureRow.createCell(3);
				        pictureRow.createCell(4);
		    			pictureRow.createCell(5);
		    			pictureRow.createCell(6);
		    			pictureRow.createCell(7);
		    			pictureRow.createCell(8);

			        	pictureRow.getCell(PIC_COLS_TRUE_START).setCellValue(procName.getFirst() + " - " + reqType.getFirst() + " | Destination Photos");
			        	PIC_COLS_START++;
			        	
				        pictureBusinessProcCell.setCellStyle(allBorders);
				        
				        try {
				        	// Add the image to the workbook.
				        	byte[] reportBytes = Photo.getBytes(1, (int) (Photo.length() - 1));
				        	int pictureIdx = workbook.addPicture(reportBytes, Workbook.PICTURE_TYPE_JPEG);
							CreationHelper helper = workbook.getCreationHelper();
							Drawing drawing = sheet.createDrawingPatriarch();
							ClientAnchor anchor = helper.createClientAnchor();
							anchor.setRow1(PIC_ROWS_START);
							anchor.setRow2(PIC_ROWS_START + 11);
							anchor.setCol1(PIC_COLS_START);
							anchor.setCol2(PIC_COLS_START + 2);
							drawing.createPicture(anchor, pictureIdx);
						} catch (SerialException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				    }
					PIC_COLS_START = PIC_COLS_START + 1;
				}
				if(destPhotosExist && reloPhotosExist){
			        PIC_ROWS_START = PIC_ROWS_START + 11;
				}
				PIC_COLS_START = PIC_COLS_TRUE_START;
				for (Blob Photo : reloPhotos){
					if (Photo != null && Photo.length() > 0){
						reqType 				= Pair.of(report.getReqType(), "reqType");
						lineNo					= Pair.of(report.getLineNo(), "lineNo");
						deptName				= Pair.of(report.getDeptName(), "deptName");
						simProcPt				= Pair.of(report.getSimProcPt(), "simProcPt");
						curStaTyp				= Pair.of(report.getCurStaTyp(), "curStaTyp");
						nodeIdNum				= Pair.of(report.getNodeIdNum(), "nodeIdNum");
						cntlrQty				= Pair.of(report.getCntlrQty(), "cntlrQty");
						
						Row pictureRow 						= sheet.createRow(PIC_ROWS_START);
				        Cell pictureBusinessProcCell 		= pictureRow.createCell(1);
				        pictureRow.createCell(2);
				        pictureRow.createCell(3);
				        pictureRow.createCell(4);
		    			pictureRow.createCell(5);
		    			pictureRow.createCell(6);
		    			pictureRow.createCell(7);
		    			pictureRow.createCell(8);

			        	pictureRow.getCell(PIC_COLS_TRUE_START).setCellValue(procName.getFirst() + " - " + reqType.getFirst() + " | Current Location Photos");
			        	PIC_COLS_START++;
			        	
				        pictureBusinessProcCell.setCellStyle(allBorders);
				        
				        try {
				        	// Add the image to the workbook.
				        	byte[] reportBytes = Photo.getBytes(1, (int) (Photo.length() - 1));
				        	int pictureIdx = workbook.addPicture(reportBytes, Workbook.PICTURE_TYPE_JPEG);
							CreationHelper helper = workbook.getCreationHelper();
							Drawing drawing = sheet.createDrawingPatriarch();
							ClientAnchor anchor = helper.createClientAnchor();
							anchor.setRow1(PIC_ROWS_START);
							anchor.setRow2(PIC_ROWS_START + 11);
							anchor.setCol1(PIC_COLS_START);
							anchor.setCol2(PIC_COLS_START + 2);
							drawing.createPicture(anchor, pictureIdx);
						} catch (SerialException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}				        
				        PIC_COLS_START = PIC_COLS_START + 1;
				        
				    }
				}
				CellRangeAddress pictures = new CellRangeAddress(PIC_ROWS_TRUE_START, PIC_ROWS_START + 10, 
						PIC_COLS_TRUE_START, PIC_COLS_TRUE_START + 6);
				RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, pictures, sheet, workbook);
				RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, pictures, sheet, workbook);
				RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, pictures, sheet, workbook);
				RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, pictures, sheet, workbook);

				PIC_ROWS_START = PIC_ROWS_START + 11;
				PIC_COLS_START = PIC_COLS_TRUE_START;
			}			
			// Add thick outer borders to the requirements form.
			
			// AutoSize all the rows that were edited.
	        for(int i = TRUE_START; i < GLOBAL_START + 1; i++){
	        	Row autoSizeRowNo = sheet.getRow(i);
	        	if( autoSizeRowNo != null){
	        		autoSizeRowNo.setHeight((short)-1);
	        	}
	        }
			
	        // Send the Excel workbook to the user.
	        workbook.write(response.getOutputStream());
	        // Clean up the mess.
	    	response.flushBuffer();
	    	fip.close();
		}catch (IOException exception) {
    		System.out.println(exception);
    	}
    }
			
    /*
     * This is the constructor for the BAServiceRequestWriter object
     */
	public BAServiceRequestWriter(List<ServiceRequest> serviceReports, List<NewDevSettings> newDevNameAndBusReqGrpIdList, List<InvDevSettings> invDevNameAndBusReqGrpIdList, HttpServletResponse response, ApplicationContext appContext){
		this.serviceReports = serviceReports;
		this.newDevNameAndBusReqGrpIdList = newDevNameAndBusReqGrpIdList;
		this.invDevNameAndBusReqGrpIdList = invDevNameAndBusReqGrpIdList;
		this.response = response;
		this.appContext = appContext;
	}
}