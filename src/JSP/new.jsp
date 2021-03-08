<input type="hidden" name="newDevVarNamesSet" id="newDevVarNamesSet" value="<c:out value="${csvNewDevVarNames}"/>">
<!-- DropDown for Package Choices -->
<div class="row">
 	<div class="col-15" align="left">
   		<label for="newPkgSelection"><font color="crimson"><strong>PACKAGE:</strong></font></label>
 	</div>
 	<div class="col-85" align="right">
   		<select id="newPkgSelection" name="newPkgSelection" onchange="setNewEqptVals()">
   			<option value=""></option>
			<c:forEach var="stdpkg" items="${listAllPkgNames}"><option value="${stdpkg.pkgName}">${stdpkg.pkgName}</option></c:forEach>
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
						<td width=500><label for="${listAllNewDevSettings[j].getNewVarName()}"><c:out value="${listAllNewDevSettings[j].getNewDevName()}"/></label></td>
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