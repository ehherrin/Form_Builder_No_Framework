<%@ include file='../BAheader.jsp' %>
	<!-- Navigation -->
	<%@ include file='../BAAdminNav.jsp' %>
	<div class="container">
	    <script type="text/javascript" charset="utf-8">
		    $(document).ready(function () {
		    	$('#userTable').DataTable( {
		        	"sDom": '<lf<"addbutton">tip>',
		        	"columnDefs": [{orderable: false, targets: -1}]
		        });
		    });
		    function ExportSR(srNo) {window.open('ExportSR?srNo='+srNo, '_blank');}
		    $(document).ready(function(){
		    	$(function() {$('#srReports').click();});
				var coll = document.getElementsByClassName("collapsible");
				for (var i = 0; i < coll.length; i++) {
					coll[i].addEventListener("click", function() {
				    	this.classList.toggle("active");
				    	var content = this.nextElementSibling;
				    	content.style.display === "block" ? content.style.display = "none" : content.style.display = "block";
				  	});
				}
			});
		</script>
       <!-- Collapsible for Viewing all SR Requirement Requests -->
		<button type="button" class="collapsible" id="srReports">My SR Reports</button>
			<div class="contentFormSettings">
				<div style="width:99%">
				<input type="hidden" name="editDevVarNamesSet" id="editDevVarNamesSet" value="<c:out value="${csvAdminEditDevVarNames}"/>">
				<input type="hidden" name="newDevVarNamesSet" id="newDevVarNamesSet" value="<c:out value="${csvAdminNewDevVarNames}"/>">
				<input type="hidden" name="currentNewDevVarName" id="currentNewDevVarName" value="">
				<input type="hidden" name="_csrf" value="${_csrf.token}">
				<table cellpadding="0" cellspacing="0" border="0" class="display bordered compact stripe hover" id="userTable" width="100%">
					<thead>
			            <tr>
			                <th>Service Request Number</th>
			                <th>Process Name</th>
			                <th>Customer Name</th>
			                <th>Department Name</th>
			                <th>Request Type</th>
			                <th>Created</th>
			                <th>Action</th>
			            </tr>
		            </thead>
		            <tbody>
		            	<c:forEach var="listAllSrData" items="${listAllSrData}">
			            <tr>
			                <td>${listAllSrData.getSrNo()}</td>
			                <td>${listAllSrData.getProcName()}</td>
			                <td>${listAllSrData.getCustName()}</td>
			                <td>${listAllSrData.getDeptName()}</td>
			                <td>${listAllSrData.getReqType()}</td>
			                <td>${listAllSrData.getCreated()}</td>
			                <td><input type="button" class="buttonCard" id="exportButton" value="Export" onclick="ExportSR('${listAllSrData.srNo}')" /></td>	                
			            </tr>
			            </c:forEach> 
		            </tbody>
				</table>
				</div>
			</div>
		<hr style="color:#cccc99; background-color:#cccc99; height:5px; border:none;">
		<!-- Collapsibles for Starter Form Options -->		
  		<div class="row">
    		<div class="col-15" align="left"><label>Starter Form Options:</label></div>
    		<div class="col-85">
    			<!-- Collapsible for Adding a Department Name -->
				<button type="button" class="collapsible" id="addDept">Add Department Name</button>
					<div class="contentFormSettings">
						<div class="row">
						 	<div class="col-25" align="left"><label for="addDeptName">Department Name:</label></div>
						 	<div class="col-75" align="right"><input type="text" id="addDeptName" value="" name="addDeptName"/></div>
						</div>
						<button type="button" onclick="createDeptName()">Confirm</button>
					</div>
				<!-- Collapsible for Editing a Department Name -->
				<button type="button" class="collapsible" id="editDept">Edit Department Name</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="editDeptName">Department Name Being Changed:</label></div>
				    		<div class="col-75">
				    			<select id="editDeptName" name="editDeptName">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllDeptNames}">
										<option value="${item.deptName}">${item.deptName}</option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedDeptName">New Department Name:</label></div>
				    		<div class="col-75"><input type="text" id="editedDeptName" value="" name="lineQty"/></div>
				  		</div>
				  		<button type="button" onclick="updateDeptName()">Confirm</button>
					</div>
				<!-- Collapsible for Deleting a Department Name -->
				<button type="button" class="collapsible" id="deleteDept">Delete Department Name</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="delDeptName">Department Name:</label></div>
				    		<div class="col-75">
				    			<select id="delDeptName" name="delDeptName">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllDeptNames}">
										<option value="${item.deptName}">${item.deptName}</option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="delDeptName()">Confirm</button>		  		
					</div>
				<hr style="color:#cccc99; background-color:#cccc99; height:1px; border:none;">  			
    			<!-- Collapsible for Editing the maximum Quantity of Lines -->
				<button type="button" class="collapsible" id="editLineQty">Edit Quantity of Lines</button>
					<div class="contentFormSettings">
						<div class="row">
						 	<div class="col-25" align="left"><label for="lineQty">Quantity of Lines:</label></div>
						 	<div class="col-75" align="right"><input type="text" id="lineQty" value="" name="lineQty"/></div>
						</div>
						<button type="button" onclick="updateLineQty()">Confirm</button>	
					</div>
    		</div>
  		</div>
  		<div class="row"><h1></h1><h1></h1></div>
  		<hr style="color:#cccc99; background-color:#cccc99; height:5px; border:none;">
  		<!-- Collapsibles for New Form Options -->
  		<div class="row">
    		<div class="col-15" align="left"><label>New Form Options:</label></div>
    		<div class="col-85">
    			<!-- Collapsible for Creating a New Standard Package -->
				<button type="button" class="collapsible" id="newStdPkg">Add Standard Package</button>
					<div class="contentFormSettings">
						<!-- Create a new standard package -->
						<div class="col-25" align="left">
							<label for="pkgName"><font color="crimson"><strong>PACKAGE NAME:</strong></font></label>
			 			</div>
			 			<div class="col-75" align="right">
			 				<input type="text" id="pkgName" name="pkgName" value="" pattern="^.{1,255}$">
			 			</div>				
						<!-- Tables for Devices -->
						<c:forEach var="grpId" items="${listNewGrpIds}">	
							<div class="row"><h1></h1><h1></h1></div>	
							<div class="row">
								<div class="col-15" align="left">
							   		<label for="${grpId.getGrpId()}"> <font color="crimson"><strong>${grpId.getGrpId()}:</strong></font></label>
							 	</div>
								<div class="col-85" align="right">
									<table style="text-align:right; width=1000; font-size:	1px;">
										<c:set var = "colIndex" scope = "page" value = "0"/>
										<tr><td width=500><label> </label></td><td width=500><label> </label></td>
										<td width=500><label> </label></td><td width=500><label> </label></td></tr>
										<c:forEach var="j" begin="0" end="${listAllNewDevSettings.size()}">
											<c:if test="${listAllNewDevSettings[j].getGrpId()==grpId.getGrpId()}">
												<c:if test="${colIndex%2==0}"><tr></c:if>
												<td width=500><label><c:out value="${listAllNewDevSettings[j].getNewDevName()}"/></label></td>
												<td width=500>
													<select id="${listAllNewDevSettings[j].getNewVarName()}" name="${listAllNewDevSettings[j].getNewVarName()}">
														<c:forEach var="i" begin="0" end="${listAllNewDevSettings[j].getNewMaxQty()}"><option value="${i}">${i}</option></c:forEach>
										    		</select>
												</td>
												<c:if test="${colIndex%2==1}"></tr></c:if>
												<c:set var = "colIndex" scope = "page" value = "${colIndex + 1}"/>						
											</c:if>
										</c:forEach>
									</table>
								</div>
							</div>
						</c:forEach>
						<button type="button" onclick="createStdPkg()">Confirm</button>	 
					</div>	
				<!-- Collapsible for Editing a Standard Package -->
				<button type="button" class="collapsible" id="editStdPkg">Edit Standard Package</button>
					<div class="contentFormSettings">
						<div class="row">
						 	<div class="col-25" align="left">
						   		<label for="pkgNameSel"><font color="crimson"><strong>PACKAGE:</strong></font></label>
						 	</div>
						 	<div class="col-75" align="right">
						   		<select id="pkgNameSel" name="pkgNameSel" onchange="setAdminEqptVals()">
						   			<option value=""></option>
						   			<c:forEach var="item" items="${listAllPkgNames}">
										<option value="<c:out value="${item.pkgName}"/>"> <c:out value="${item.pkgName}"/> </option>
									</c:forEach>
						   		</select>
							</div>
						</div>
						<!-- Tables for Devices -->
						<c:forEach var="grpId" items="${listNewGrpIds}">	
							<div class="row"><h1></h1><h1></h1></div>	
							<div class="row">
								<div class="col-15" align="left">
							   		<label for="${grpId.getGrpId()}"> <font color="crimson"><strong>${grpId.getGrpId()}:</strong></font></label>
							 	</div>
								<div class="col-85" align="right">
									<table style="text-align:right; width=1000; font-size:	1px;">
										<c:set var = "colIndex" scope = "page" value = "0"/>
										<tr><td width=500><label> </label></td><td width=500><label> </label></td>
										<td width=500><label> </label></td><td width=500><label> </label></td></tr>
										<c:forEach var="j" begin="0" end="${listAllNewDevSettings.size()}">
											<c:if test="${listAllNewDevSettings[j].getGrpId()==grpId.getGrpId()}">
												<c:if test="${colIndex%2==0}"><tr></c:if>
												<td width=500><label><c:out value="${listAllNewDevSettings[j].getNewDevName()}"/></label></td>
												<td width=500>
													<select id="${listAllNewDevSettings[j].getEditVarName()}" name="${listAllNewDevSettings[j].getEditVarName()}">
														<c:forEach var="i" begin="0" end="${listAllNewDevSettings[j].getNewMaxQty()}"><option value="${i}">${i}</option></c:forEach>
										    		</select>	
												</td>
												<c:if test="${colIndex%2==1}"></tr></c:if>
												<c:set var = "colIndex" scope = "page" value = "${colIndex + 1}"/>						
											</c:if>
										</c:forEach>
									</table>
								</div>
							</div>
						</c:forEach>
						<button type="button" onclick="updateStdPkg()">Confirm</button>
					</div>
				<!-- Collapsible for Deleting Standard Package -->
				<button type="button" class="collapsible" id="deleteStdPkg">Delete Standard Package</button>
					<div class="contentFormSettings">
						<div class="row">
						 	<div class="col-25" align="left"><label for="pkgNameDelSel">Package Name:</label></div>
						 	<div class="col-75" align="right">
						   		<select id="pkgNameDelSel" name="pkgNameDelSel">
						   			<option value=""></option>
						   			<c:forEach var="item" items="${listAllPkgNames}">
										<option value="<c:out value="${item.pkgName}"/>"> <c:out value="${item.pkgName}"/> </option>
									</c:forEach>
						   		</select>
							</div>
						</div>
						<button type="button" onclick="deleteStdPkg()">Confirm</button>
					</div>			
    			<hr style="color:#cccc99; background-color:#cccc99; height:1px; border:none;">
    			<!-- Collapsible for Adding a New Form Item -->
				<button type="button" class="collapsible" id="addNewDevice">Add New Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="addNewDevName">Device Name:</label></div>
				    		<div class="col-75"><input type="text" id="addNewDevName" value="" name="addNewDevName"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="addNewDevMaxVal">Item Max Quantity:</label></div>
				    		<div class="col-75"><input type="text" id="addNewDevMaxVal" value="" name="addNewDevMaxVal"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="addNewDevGrpId">Item Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="addNewDevGrpId" name="addNewDevGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listNewGrpIds}">
										<option value="<c:out value="${item.getGrpId()}"/>"><c:out value="${item.getGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left">
				      			<label for="addNewBusReqGrpId">Item Business Requirement Group Identifier:</label>
				    		</div>
				    		<div class="col-75">
				    			<select id="addNewBusReqGrpId" name="addNewBusReqGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listNewBusReqGrpIds}">
										<option value="<c:out value="${item.getBusReqGrpId()}"/>"><c:out value="${item.getBusReqGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="addNewDevice()">Confirm</button>
					</div>
				<!-- Collapsible for Editing a New Form Item -->
				<button type="button" class="collapsible" id="editNewDevice">Edit New Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="currentNewDevName">Item Being Edited:</label></div>
				    		<div class="col-75">
				    			<select id="currentNewDevName" name="currentNewDevName" onchange="getCurrentNewDevVarName()">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllNewDevSettings}">
										<option value="<c:out value="${item.getNewDevName()}"/>"><c:out value="${item.getNewDevName()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedNewDevName">Item Name:</label></div>
				    		<div class="col-75"><input type="text" id="editedNewDevName" value="" name="editedNewDevName"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedNewDevMaxVal">Item Max Quantity:</label></div>
				    		<div class="col-75"><input type="text" id="editedNewDevMaxVal" value="" name="editedNewDevMaxVal"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedNewDevGrpId">Item Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="editedNewDevGrpId" name="editedNewDevGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listNewGrpIds}">
										<option value="<c:out value="${item.getGrpId()}"/>"><c:out value="${item.getGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left">
				      			<label for="editedNewBusReqGrpId">Item Business Requirement Group Identifier:</label>
				    		</div>
				    		<div class="col-75">
				    			<select id="editedNewBusReqGrpId" name="editedNewBusReqGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listNewBusReqGrpIds}">
										<option value="<c:out value="${item.getBusReqGrpId()}"/>"><c:out value="${item.getBusReqGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="editNewDevice()">Confirm</button>
					</div>
				<!-- Collapsible for Deleting a New Form Item -->
				<button type="button" class="collapsible" id="delNewDevice">Delete New Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="delNewDevName">Item Being Deleted:</label></div>
				    		<div class="col-75">
				    			<select id="delNewDevName" name="delNewDevName">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllNewDevSettings}">
										<option value="<c:out value="${item.getNewDevName()}"/>"><c:out value="${item.getNewDevName()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="delNewDevice()">Confirm</button>
					</div>
    		</div>
  		</div>
  		<div class="row"><h1></h1><h1></h1></div>
  		<hr style="color:#cccc99; background-color:#cccc99; height:5px; border:none;">
  		<!-- Collapsibles for Relocation Form Options -->
  		<div class="row">
    		<div class="col-15" align="left"><label>Relocation Form Options:</label></div>
    		<div class="col-85">
				<!-- Collapsible for Editing the maximum Quantity of Controllers -->
				<button type="button" class="collapsible" id="editContQty">Edit Quantity of Controllers</button>
					<div class="contentFormSettings">
						<div class="row">
						 	<div class="col-25" align="left"><label for="contQty">Quantity of Controllers:</label></div>
						 	<div class="col-75" align="right"><input type="text" id="contQty" value="" name="contQty"/></div>
						</div>
						<button type="button" onclick="updateContQty()">Confirm</button>	
					</div>
    		</div>
  		</div>
  		<div class="row"><h1></h1><h1></h1></div>
  		<hr style="color:#cccc99; background-color:#cccc99; height:5px; border:none;">
  		<!-- Collapsibles for Inventory Form Options -->
  		<div class="row">
    		<div class="col-15" align="left"><label>Current Inventory Form Options:</label></div>
    		<div class="col-85">
    			<!-- Collapsible for Adding a Current Inventory Form Item -->
				<button type="button" class="collapsible" id="addInvDevice">Add Current Inventory Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="addInvDevName">Device Name:</label></div>
				    		<div class="col-75"><input type="text" id="addInvDevName" value="" name="addInvDevName"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="addInvDevMaxVal">Item Max Quantity:</label></div>
				    		<div class="col-75"><input type="text" id="addInvDevMaxVal" value="" name="addInvDevMaxVal"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="addInvDevGrpId">Item Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="addInvDevGrpId" name="addInvDevGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listInvGrpIds}">
										<option value="<c:out value="${item.getGrpId()}"/>"><c:out value="${item.getGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="addInvBusReqGrpId">Item Business Requirement Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="addInvBusReqGrpId" name="addInvBusReqGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listInvBusReqGrpIds}">
										<option value="<c:out value="${item.getBusReqGrpId()}"/>"><c:out value="${item.getBusReqGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="addInvDevice()">Confirm</button>
					</div>
				<!-- Collapsible for Editing an Inventory Form Item -->
				<button type="button" class="collapsible" id="editInvDevice">Edit Current Inventory Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="currentInvDevName">Item Being Edited:</label></div>
				    		<div class="col-75">
				    			<select id="currentInvDevName" name="currentInvDevName" onchange="getCurrentInvDevVarName()">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllInvDevSettings}">
										<option value="<c:out value="${item.getInvDevName()}"/>"><c:out value="${item.getInvDevName()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedInvDevName">Item Name:</label></div>
				    		<div class="col-75"><input type="text" id="editedInvDevName" value="" name="editedInvDevName"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedInvDevMaxVal">Item Max Quantity:</label></div>
				    		<div class="col-75"><input type="text" id="editedInvDevMaxVal" value="" name="editedInvDevMaxVal"/></div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedInvDevGrpId">Item Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="editedInvDevGrpId" name="editedInvDevGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listInvGrpIds}">
										<option value="<c:out value="${item.getGrpId()}"/>"><c:out value="${item.getGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<div class="row">
				    		<div class="col-25" align="left"><label for="editedInvBusReqGrpId">Item Business Requirement Group Identifier:</label></div>
				    		<div class="col-75">
				    			<select id="editedInvBusReqGrpId" name="editedInvBusReqGrpId">
						   			<option value=""></option>
									<c:forEach var="item" items="${listInvBusReqGrpIds}">
										<option value="<c:out value="${item.getBusReqGrpId()}"/>"><c:out value="${item.getBusReqGrpId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="editInvDevice()">Confirm</button>
					</div>
				<!-- Collapsible for Deleting a Inventory Form Item -->
				<button type="button" class="collapsible" id="delInvDevice">Delete Current Inventory Form Hardware</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="delInvDevName">Item Being Deleted:</label></div>
				    		<div class="col-75">
				    			<select id="delInvDevName" name="delInvDevName">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAllInvDevSettings}">
										<option value="<c:out value="${item.getInvDevName()}"/>"><c:out value="${item.getInvDevName()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="delInvDevice()">Confirm</button>
					</div>
    		</div>
  		</div>
  		<div class="row"><h1></h1><h1></h1></div>
  		<hr style="color:#cccc99; background-color:#cccc99; height:5px; border:none;">
  		<!-- Collapsibles for Administrator Management Options -->
  		<div class="row">
    		<div class="col-15" align="left"><label>Administrator Management:</label></div>
    		<div class="col-85">
    			<!-- Collapsible for Adding an Administrator -->
				<button type="button" class="collapsible" id="addAdministrator">Add Administrator</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="addAdmin">Administrator ID:</label></div>
				    		<div class="col-75"><input type="text" id="addAdmin" value="" name="addAdmin"/></div>
				  		</div>
				  		<button type="button" onclick="addAdmin()">Confirm</button>
					</div>
				<!-- Collapsible for Removing an Administrator -->
				<button type="button" class="collapsible" id="delAdministrator">Remove Administrator</button>
					<div class="contentFormSettings">
						<div class="row">
				    		<div class="col-25" align="left"><label for="delAdmin">Administrator ID:</label></div>
				    		<div class="col-75">
				    			<select id="delAdmin" name="delAdmin">
						   			<option value=""></option>
									<c:forEach var="item" items="${listAdmins}">
										<option value="<c:out value="${item.getAdminId()}"/>"><c:out value="${item.getAdminId()}"/></option>
									</c:forEach>
						   		</select>
				    		</div>
				  		</div>
				  		<button type="button" onclick="delAdmin()">Confirm</button>
					</div>
    		</div>
  		</div>
	</div>				
<%@ include file='../BAfooter.jsp' %>