/* 
 * Sets the value of all known "edit" devices to zero with a dynamically built JQuery command.
 */ 
function zeroAllAdmin(){
	var devEditNewVarNameArray = $("#editDevVarNamesSet").val().split(",");
	for (var i=0; i<devEditNewVarNameArray.length; i++){$("#"+devEditNewVarNameArray[i]).val("0");}
}

/* 
 * Sets the value of all known "New Form" devices to zero with a dynamically built JQuery command.
 */ 
function zeroAllNewAdmin(){
	var devNewVarNameArray = $("#newDevVarNamesSet").val().split(",");
	for (var i=0; i<devNewVarNameArray.length; i++){$("#"+devNewVarNameArray[i]).val("0");}
}

/*
 * Sets the "edit" device values based on the database memory of the current
 * "New Form" device values.
 */
function setAdminEqptVals() {
	var pkgName = $('#pkgNameSel').val();
	if (pkgName === ""){zeroAllAdmin();}
	else{
		$.ajax({
	        type: "GET",
	        url: "mobile/ws/getPkgSet", 
	        data: "pkgName="+pkgName, 
	        dataType: "text",
	        async : true,
	        beforeSend: function() {},
	        complete: function() {},
	        success:function(data) {
	        	zeroAllAdmin();
	        	if (data != "") {
	        		var devEditVarNameArray = $("#editDevVarNamesSet").val().split(",");
	        		var editDataCopy = data;
	        		editDataCopy = editDataCopy.replace(new RegExp('new','g'),"edit");
	        		var editVarNamesAndValsMap = [];
	        		var editVarNamesAndValsPairArray = editDataCopy.split("\^");
	        		var editTempString = "";
	        		var varNamesAndValsMap = [];
	        		var varNamesAndValsPairArray = data.split("\^");
	        		var tempString = "";
	        		
	        		for ( var i=0; i<varNamesAndValsPairArray.length; i++){
	        			tempString = varNamesAndValsPairArray[i];
	        			var separatedNameAndValsArray = tempString.split(",");
	        			varNamesAndValsMap.push(separatedNameAndValsArray);
	        		}
	        		for ( var i=0; i<editVarNamesAndValsPairArray.length; i++){
	        			tempString = editVarNamesAndValsPairArray[i];
	        			var editSeparatedNameAndValsArray = tempString.split(",");
	        			editVarNamesAndValsMap.push(editSeparatedNameAndValsArray);
	        		}      		
	        		for ( var i=0; i<editVarNamesAndValsPairArray.length; i++){
	        			$("#"+editVarNamesAndValsMap[i][0]).val(varNamesAndValsMap[i][1]);
	        		}
	        	}
	        	else{zeroAllAdmin();}
	        }
	    });
	}
}

/*
 * This function will build a string named "editVarNameAndValStr" based upon the current values
 * stored in the "edit" device quantity choice list. Each device-value pair is separated by a 
 * comma, while each individual device is separated by a caret symbol. This string is then sent to
 * the BAEstimationRestController for insertion into the database.
 */
function updateStdPkg() {
	var editVarNameAndValStr = "";
	var devEditVarNameArray = $("#editDevVarNamesSet").val().split(",");
	var pkgName = $('#pkgNameSel').val();
	
	for ( var i=0; i<devEditVarNameArray.length; i++){
		if (i==0){
			editVarNameAndValStr += devEditVarNameArray[i]+","+$("#"+devEditVarNameArray[i]).val();
		}else{
			editVarNameAndValStr += "^"+devEditVarNameArray[i]+","+$("#"+devEditVarNameArray[i]).val();
		}
	}
	$.ajax({
        type: "GET",
        url: "mobile/ws/updateStdPkg", 
        data: "pkgName="+pkgName+"&editVarNameAndValStr="+editVarNameAndValStr, 
        dataType: "text",
        async: true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Package Update " + data);
        	$('#pkgNameSel').val("");
        	zeroAllAdmin();
        }
    });
}

function createStdPkg(){
	var newVarNameAndValStr = "";
	var devNewVarNameArray = $("#newDevVarNamesSet").val().split(",");
	var pkgName = $('#pkgName').val();
	
	for ( var i=0; i<devNewVarNameArray.length; i++){
		if (i==0){
			newVarNameAndValStr += devNewVarNameArray[i]+","+$("#"+devNewVarNameArray[i]).val();
		}else{
			newVarNameAndValStr += "^"+devNewVarNameArray[i]+","+$("#"+devNewVarNameArray[i]).val();
		}
	}
		
	$.ajax({
        type: "GET",
        url: "mobile/ws/createStdPkg", 
        data: "pkgName="+pkgName+"&newVarNameAndValStr="+newVarNameAndValStr, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Package Creation " + data);
        	if ( data === "Successful."){
        		$('#pkgNameSel').append('<option value=\"' + $('#pkgName').val() + '\">' + $('#pkgName').val() + '</option>')
        		$('#pkgNameDelSel').append('<option value=\"' + $('#pkgName').val() + '\">' + $('#pkgName').val() + '</option>')
        	}
        	$('#pkgName').val("");
        	zeroAllNewAdmin();
        }
    });
}

function deleteStdPkg(){
	var pkgName = $('#pkgNameDelSel').val();
	$.ajax({
        type: "GET",
        url: "mobile/ws/deleteStdPkg", 
        data: "pkgName="+pkgName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Package Deletion " + data);
        	if ( data === "Successful."){
        		$('#pkgNameSel option[value=\"' + pkgName + '\"').remove()
        		$('#pkgNameDelSel option[value=\"' + pkgName + '\"').remove()
        	}
        	$('#pkgNameDelSel').val("");
        	zeroAllAdmin();
        }
    });
}

function updateContQty(){
	var contQty = $('#contQty').val();
	$.ajax({
        type: "GET",
        url: "mobile/ws/updateContQty", 
        data: "contQty="+contQty, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Controller Quantity Update " + data);
        	$('#contQty').val("");
        }
    });
}
	
function updateLineQty(){
	var lineQty = $('#lineQty').val();
	$.ajax({
        type: "GET",
        url: "mobile/ws/updateLineQty", 
        data: "lineQty="+lineQty, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Line Quantity Update " + data);
        	$('#lineQty').val("");
        }
    });
}

function createDeptName(){
	var deptName = $('#addDeptName').val();
	$.ajax({
        type: "GET",
        url: "mobile/ws/createDeptName", 
        data: "deptName="+deptName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Department Creation " + data);
        	if ( data === "Successful."){
        		$('#editDeptName').append('<option value=\"' + deptName + '\">' + deptName + '</option>')
        		$('#delDeptName').append('<option value=\"' + deptName + '\">' + deptName + '</option>')
        	}
        	$('#addDeptName').val("");
        }
    });
}

function updateDeptName() {
	var deptNames = [$('#editDeptName').val(),$('#editedDeptName').val()];

	$.ajax({
        type: "GET",
        url: "mobile/ws/updateDeptName", 
        data: "deptNames="+deptNames, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Department Name Update " + data);
        	if ( data === "Successful."){
        		$('#delDeptName option[value=\"' + $('#editDeptName').val() + '\"').remove()
        		$('#editDeptName option[value=\"' + $('#editDeptName').val() + '\"').remove()
            	$('#editDeptName').append('<option value=\"' + $('#editedDeptName').val() + '\">' + $('#editedDeptName').val() + '</option>')
        		$('#delDeptName').append('<option value=\"' + $('#editedDeptName').val() + '\">' + $('#editedDeptName').val() + '</option>')
        	}
        	$('#editDeptName').val("");
        	$('#editedDeptName').val("");
        }
    });
}

function delDeptName(){
	var deptName = $('#delDeptName').val();
	$.ajax({
        type: "GET",
        url: "mobile/ws/delDeptName", 
        data: "deptName="+deptName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Department Name Deletion " + data);
        	if ( data === "Successful."){
        		$('#delDeptName option[value=\"' + deptName + '\"').remove()
        		$('#editDeptName option[value=\"' + deptName + '\"').remove()
        	}
        	$('#delDeptName').val("");
        }
    });
}

function addNewDevice(){
	var addNewDevName = $('#addNewDevName').val();
	var addNewDevMaxVal = $('#addNewDevMaxVal').val();
	var addNewDevGrpId = $('#addNewDevGrpId').val();
	var addNewBusReqGrpId = $('#addNewBusReqGrpId').val();
	var dataArray = [addNewDevName, addNewDevMaxVal, addNewDevGrpId, addNewBusReqGrpId];
	var dataString = "newDevInfo=" + dataArray;
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/addNewDevice", 
        data: dataString, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("New Form Device Name and Max Quantity Addition " + data);
        	$('#addNewDevName').val("");
        	$('#addNewDevMaxVal').val("");
        	$('#addNewDevGrpId').val("");
        	$('#addNewBusReqGrpId').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function addInvDevice(){
	var addInvDevName = $('#addInvDevName').val();
	var addInvDevMaxVal = $('#addInvDevMaxVal').val();
	var addInvDevGrpId = $('#addInvDevGrpId').val();
	var addInvBusReqGrpId = $('#addInvBusReqGrpId').val();
	var dataArray = [addInvDevName, addInvDevMaxVal, addInvDevGrpId, addInvBusReqGrpId];
	var dataString = "invDevInfo=" + dataArray;
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/addInvDevice", 
        data: dataString, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Current Inventory Form Device Name and Max Quantity Addition " + data);
        	$('#addInvDevName').val("");
        	$('#addInvDevMaxVal').val("");
        	$('#addInvDevGrpId').val("");
        	$('#addInvBusReqGrpId').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function editNewDevice(){
	var currentNewDevName = $('#currentNewDevName').val();
	var editedNewDevName = $('#editedNewDevName').val();
	var editedNewDevMaxVal = $('#editedNewDevMaxVal').val();
	var editedNewDevGrpId = $('#editedNewDevGrpId').val();
	var editedNewBusReqGrpId = $('#editedNewBusReqGrpId').val();
	var dataArray = [currentNewDevName, editedNewDevName, editedNewDevMaxVal, editedNewDevGrpId, editedNewBusReqGrpId];
	var dataString = "newDevNameInfo=" + dataArray;
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/editNewDevice", 
        data: dataString, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("New Form Device Name and Max Quantity Edit " + data);
        	if ( data === "Successful."){
        		$('#currentNewDevName option[value=\"' + $('#currentNewDevName').val() + '\"').remove()
            	$('#currentNewDevName').append('<option value=\"' + $('#editedNewDevName').val() + '\">' + $('#editedNewDevName').val() + '</option>')
        	}
        	$('#currentNewDevName').val("");
        	$('#editedNewDevName').val("");
        	$('#editedNewDevMaxVal').val("");
        	$('#editedNewDevGrpId').val("");
        	$('#editedNewBusReqGrpId').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function editInvDevice(){
	var currentInvDevName = $('#currentInvDevName').val();
	var editedInvDevName = $('#editedInvDevName').val();
	var editedInvDevMaxVal = $('#editedInvDevMaxVal').val();
	var editedInvDevGrpId = $('#editedInvDevGrpId').val();
	var editedInvBusReqGrpId = $('#editedInvBusReqGrpId').val();
	var dataArray = [currentInvDevName, editedInvDevName, editedInvDevMaxVal, editedInvDevGrpId, editedInvBusReqGrpId];
	var dataString = "invDevNameInfo=" + dataArray;
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/editInvDevice", 
        data: dataString, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Current Inventory Form Device Name and Max Quantity Edit " + data);
        	if ( data === "Successful."){
        		$('#currentInvDevName option[value=\"' + $('#currentInvDevName').val() + '\"').remove()
            	$('#currentInvDevName').append('<option value=\"' + $('#editedInvDevName').val() + '\">' + $('#editedInvDevName').val() + '</option>')
        	}
        	$('#currentInvDevName').val("");
        	$('#editedInvDevName').val("");
        	$('#editedInvDevMaxVal').val("");
        	$('#editedInvDevGrpId').val("");
        	$('#editedInvBusReqGrpId').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function getCurrentNewDevVarName(){
	var currentNewDevName = $('#currentNewDevName').val();  
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/getCurrentNewDevSetting", 
        data: "currentNewDevName=" + currentNewDevName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	if ( data != "" && data != null){
        		var idArray = data.split(",");
        		$('#editedNewDevGrpId').val(idArray[0]);
        		$('#editedNewBusReqGrpId').val(idArray[1]);
        	}
        }
    });
}

function getCurrentInvDevVarName(){
	var currentInvDevName = $('#currentInvDevName').val();  
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/getCurrentInvDevSetting", 
        data: "currentInvDevName=" + currentInvDevName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	if ( data != "" && data != null){
        		var idArray = data.split(",");
        		$('#editedInvDevGrpId').val(idArray[0]);
        		$('#editedInvBusReqGrpId').val(idArray[1]);
        	}
        }
    });
}

function delNewDevice(){
	var delNewDevName = $('#delNewDevName').val();  
	$.ajax({
        type: "GET",
        url: "mobile/ws/delNewDev", 
        data: "delNewDevName=" + delNewDevName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	alert("Item Deletion " + data);
        	if ( data === "Successful."){
        		$('#currentNewDevName option[value=\"' + $('#currentNewDevName').val() + '\"').remove()
        		$('#delNewDevName option[value=\"' + $('#delNewDevName').val() + '\"').remove()
        	}
        	$('#delNewDevName').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function delInvDevice(){
	var delInvDevName = $('#delInvDevName').val();  
	$.ajax({
        type: "GET",
        url: "mobile/ws/delInvDev", 
        data: "delInvDevName=" + delInvDevName, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
    		alert("Item Deletion " + data);
        	if ( data === "Successful."){
        		$('#currentInvDevName option[value=\"' + $('#currentInvDevName').val() + '\"').remove()
        		$('#delInvDevName option[value=\"' + $('#delInvDevName').val() + '\"').remove()
        	}
        	$('#delInvDevName').val("");
        	if ( data === "Successful."){
        		location.reload();
        	}
        }
    });
}

function addAdmin(){
	var addAdminId = $('#addAdmin').val();  
	$.ajax({
        type: "GET",
        url: "mobile/ws/addAdmin", 
        data: "addAdminId=" + addAdminId, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
    		alert("Administrator Addition " + data);
        	if ( data === "Successful."){
            	$('#delAdmin').append('<option value=\"' + $('#addAdmin').val() + '\">' + $('#addAdmin').val() + '</option>')
        	}
        	$('#addAdmin').val("");
        }
    });
}

function delAdmin(){
	var delAdminId = $('#delAdmin').val();  
	$.ajax({
        type: "GET",
        url: "mobile/ws/delAdmin", 
        data: "delAdminId=" + delAdminId, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
    		alert("Administrator Deletion " + data);
        	if ( data === "Successful."){
        		$('#delAdmin option[value=\"' + $('#delAdmin').val() + '\"').remove()
        	}
        	$('#delAdmin').val("");
        }
    });
}