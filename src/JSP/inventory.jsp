<!-- Tables for Devices -->
<c:forEach var="grpId" items="${listInvGrpIds}">	
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
				<c:forEach var="j" begin="0" end="${listAllInvDevSettings.size()}">
					<c:if test="${listAllInvDevSettings[j].getGrpId()==grpId.getGrpId()}">
						<c:if test="${colIndex%2==0}"><tr></c:if>
						<td width=500><label for="${listAllInvDevSettings[j].getInvVarName()}"><c:out value="${listAllInvDevSettings[j].getInvDevName()}"/></label></td>
						<td width=500>
							<select id="${listAllInvDevSettings[j].getInvVarName()}" name="${listAllInvDevSettings[j].getInvVarName()}">
								<c:forEach var="i" begin="0" end="${listAllInvDevSettings[j].getInvMaxQty()}"><option value="${i}">${i}</option></c:forEach>
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