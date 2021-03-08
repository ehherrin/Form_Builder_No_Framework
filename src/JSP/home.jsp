<%@ include file='../BAheader.jsp' %>
	<!-- Navigation -->
    <%@ include file='../BAnav.jsp' %>
	<script type="text/javascript" charset="utf-8">$(document).ready(function(){generateTicket();setUpCollapsibles();});</script>
	<body>
		<input type="hidden" name="newDevVarNamesSet" id="newDevVarNamesSet" value="<c:out value="${csvNewDevVarNames}"/>">
		<input type="hidden" name="invDevVarNamesSet" id="invDevVarNamesSet" value="<c:out value="${csvInvDevVarNames}"/>">
		<h2>Welcome to the SR Requirements Page!</h2>
		<div class="container">
			<!-- Form Starts Here -->
	  		<form id="userForm" action="<c:url value="/BAEstimation/ServiceRequest?${_csrf.parameterName}=${_csrf.token}"/>" 
	  		method="post" enctype="multipart/form-data">
	  			<input type="hidden" name="ticketNo" id="ticketNo" value="">
	  			<!-- Required fields annotation -->
		  		<div class="row">
		    		<div class="col-25" align="left"><label for="required"><font color="crimson">*</font> = Required</label></div>
		    		<div class="col-75"></div>
		  		</div>
		  		
		  		<button type="button" class="collapsible" id="Starter" style="display: block;">Starter Form<font color="crimson">*</font></button>
				<div class="content">
					<h3> Please provide information regarding the new location.</h3><hr>
			  		<!-- Service Request Number TextBox -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			    			<label for="srNo">Service Request Number<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75"><input type="text" id="srNo" name="srNo" value="" pattern="^.{1,255}$"></div>
			  		</div>
			  		<!-- Process Name TextBox-->
			  		<div class="row">
			    		<div class="col-25" align="center">
			      			<label for="procName">Process Name<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75"><input type="text" id="procName" name="procName" value="" pattern="^.{1,255}$"></div>
			  		</div>
			  		<!-- Customer Name TextBox-->
			  		<div class="row">
			    		<div class="col-25" align="center">
			    	  		<label for="custName">Customer Name<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75"><input type="text" id="custName" name="custName" value="" pattern="^.{1,255}"></div>
			  		</div>		
			  		<!-- Column Location ListBox-->
			  		<div class="row">
			    		<div class="col-25" align="center">
			  				<label for="destCol">Destination Column Location<font color="crimson">*</font></label>
			  			</div>
			  			<div class="col-75">
		            		<input type="text" id="destCol" value="" name="destCol" pattern="^.{1,255}$" 
		            		style="text-transform:uppercase"/>
			  			</div>
			  		</div>
			  		<!-- Process Point ListBox -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			  				<label for="destProcPnt1">Destination Process Point<font color="crimson">*</font></label>
			  			</div>
			  			<div class="col-75">
		            		<input type="text" id="destProcPnt1" value="" name="destProcPnt1" pattern="^.{1,255}$" 
		            		style="text-transform:uppercase"/>
			  			</div>
			  		</div>
			  		<!-- Process Point ListBox -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			  				<label for="destProcPnt2">Second Destination Process Point</label>
			  			</div>
			  			<div class="col-75" align="left">
			  				<input type="text" id="destProcPnt2" value="" name="destProcPnt2" pattern="^.{1,255}$" 
			  				style="text-transform:uppercase"/>
						</div>
			  		</div>
			  		<!-- Line Number DropDown -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			      			<label for="lineNo">Line Number<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75">
			    			<select id="lineNo" name="lineNo">
								<option value=""></option>
								<c:forEach var="i" begin="1" end="${listLineQty}" ><option value="${i}">${i}</option></c:forEach>
							</select>
			    		</div>
			  		</div>
			  		<!-- Department Name DropDown -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			      			<label for="deptName">Department Name<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75">
			    			<select id="deptName" name="deptName">
					   			<option value=""></option>
								<c:forEach var="item" items="${listAllDeptNames}">
									<option value="${item.getDeptName()}">${item.getDeptName()}</option>
								</c:forEach>
					   		</select>
			    		</div>
			  		</div>
			  		<!-- Request Type DropDown -->
			  		<div class="row">
			    		<div class="col-25" align="center">
			      			<label for="reqType">Request Type<font color="crimson">*</font></label>
			    		</div>
			    		<div class="col-75">
			      			<select id="reqType" name="reqType" onchange="buildForm();">
			      				<option value=""></option>
			        			<option value="New">New</option>
			        			<option value="Relocate">Relocate</option>
			        			<option value="Relocate and Customize">Relocate and Customize</option>
			      			</select>
			    		</div>
			  		</div>
			  		<!-- Destination Photo File Input -->
			  		<div class="row">
		    			<div class="col-25" align="center">
		    				<label for="destPhotos">Destination Photo</label>
		    			</div>
		    			<div class="col-75" align="left">
		      				<input type="file" name="destPhotos" id="destPhotos" accept="image/*" multiple/>
		    			</div>
		  			</div>
			  	</div>
		  		
		  		<!-- Collapsible for the New Deployment Form -->
				<button type="button" class="collapsible" id="New" style="display: none;">New Deployment Form<font color="crimson">*</font></button>
				<div class="contentNew">
					<h3>Please specify what equipment is needed at the new location.</h3><hr>
					<%@include file="./new.jsp" %>
				</div>
				
				<!-- Collapsible for the Relocation Form -->
				<button type="button" class="collapsible" id="Relocate" style="display: none;">Relocation Form<font color="crimson">*</font></button>
				<div class="contentRelocate">
					<h3> Please provide information regarding the current location.</h3><hr>
					<%@include file="./relocation.jsp" %>
				</div>

				<!-- Collapsible for the Current Inventory Form -->
				<button type="button" class="collapsible" id="Inventory" style="display: none;">Current Inventory Form<font color="crimson">*</font></button>
				<div class="contentInventory">
					<h3> Please take inventory of the equipment that is at the current location.</h3><hr>
					<%@include file="./inventory.jsp" %>
				</div>
				
				<!--  Submit Button -->
				<div class="row">
				<input style="visibility:hidden;" type="submit" id="btnSubmitButton" value="Submit" />
	  			<button type="button" onclick="validateForms()">Submit</button>
		  		</div>	
	  		</form>
		</div>	 
<%@ include file='../BAfooter.jsp' %>