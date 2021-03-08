function setUpCollapsibles(){
	var starterCollapsible = document.getElementById("Starter");
	var starterCollapsibleInfo = starterCollapsible.nextElementSibling;
	var coll = document.getElementsByClassName("collapsible");
	for (var i = 0; i < coll.length; i++) {
		coll[i].addEventListener("click", function() {
	    	this.classList.toggle("active");			    	
	    	var content = this.nextElementSibling;
	    	content.style.display === "block" ? content.style.display = "none" : content.style.display = "block";
	  	});
	}
	$('#Starter').click();
}

function collapseAndHideAll(){
	var newCollapsible = document.getElementById("New");
	var relocateCollapsible = document.getElementById("Relocate");
	var inventoryCollapsible = document.getElementById("Inventory");
	var newCollapsibleInfo = newCollapsible.nextElementSibling;
	var relocateCollapsibleInfo = relocateCollapsible.nextElementSibling;
	var inventoryCollapsibleInfo = inventoryCollapsible.nextElementSibling;
	if ($('#New').hasClass('active')){
		$('#New').click();
	}
	newCollapsible.style.display = "none";
	if ($('#Relocate').hasClass('active')){
		$('#Relocate').click();
	}
	relocateCollapsible.style.display = "none";
	if ($('#Inventory').hasClass('active')){
		$('#Inventory').click();
	}
	inventoryCollapsible.style.display = "none";
}

function generateTicket(){
	var timeStamp = Math.floor(Date.now());
	var ticketNo = timeStamp.toString();
	$("#ticketNo").val(ticketNo);
}

function buildForm() {
	var requestTypeInput = document.getElementById("reqType");
	var requestType = requestTypeInput.value;
	var newCollapsible = document.getElementById("New");
	var relocateCollapsible = document.getElementById("Relocate");
	var inventoryCollapsible = document.getElementById("Inventory");
	
	switch(requestType){		
    	case "":
    		collapseAndHideAll();
    		break;
    	//New
    	case "New":
    		collapseAndHideAll();
    		newCollapsible.style.display = "block";
    		break;
    	//Relocation
    	case "Relocate":
    		collapseAndHideAll();
    		relocateCollapsible.style.display = "block";
    		inventoryCollapsible.style.display = "block";
    		break;
    	//Relocation and Customize
    	case "Relocate and Customize":
    		collapseAndHideAll();
    		newCollapsible.style.display = "block";
    		relocateCollapsible.style.display = "block";
    		inventoryCollapsible.style.display = "block";
    		break;
	}
}

function sendNewInvFormData(){
	var srNo = $("#srNo").val();
	var newVarNameAndValStr = "";
	var devNewVarNameArray = $("#newDevVarNamesSet").val().split(",");
	var invVarNameAndValStr = "";
	var devInvVarNameArray = $("#invDevVarNamesSet").val().split(",");
	var ticketNo = $("#ticketNo").val();
	for (var i=0; i<devNewVarNameArray.length; i++){
		if (i==0){
			newVarNameAndValStr += $("label[for='"+devNewVarNameArray[i]+"']").html()+","+$("#"+devNewVarNameArray[i]).val();
		}else{
			newVarNameAndValStr += "^"+$("label[for='"+devNewVarNameArray[i]+"']").html()+","+$("#"+devNewVarNameArray[i]).val();
		}
	}
	for (var i=0; i<devInvVarNameArray.length; i++){
		if (i==0){
			invVarNameAndValStr += $("label[for='"+devInvVarNameArray[i]+"']").html()+","+$("#"+devInvVarNameArray[i]).val();
		}else{
			invVarNameAndValStr += "^"+$("label[for='"+devInvVarNameArray[i]+"']").html()+","+$("#"+devInvVarNameArray[i]).val();
		}
	}
	
	$.ajax({
        type: "GET",
        url: "mobile/ws/sendNewInvFormData", 
        data: "srNo=" + srNo 
        + "&newVarNameAndValStr=" + newVarNameAndValStr 
        + "&invVarNameAndValStr=" + invVarNameAndValStr
        + "&ticketNo=" + ticketNo, 
        dataType: "text",
        async : true,
        beforeSend: function() {},
        complete: function() {},
        success:function(data) {
        	if (data === "Successful."){
        		$("#btnSubmitButton").trigger('click');
        	}
        	else{alert("Collection of Form Data " + data);}
        }
    });
}

function validateStarterForm(){
	try{
		var starterFormElements =
			[
				["Service Request Number", 		$('#srNo').val()], 
				["Process Name", 				$('#procName').val()], 
				["Customer Name", 				$('#custName').val()], 
				["Destination Column Location", $('#destCol').val()], 
				["Destination Process Point", 	$('#destProcPnt1').val()], 
				["Line Number", 				$('#lineNo').val()], 
				["Department Name", 			$('#deptName').val()], 
				["Request Type", 				$('#reqType').val()]
			]
		
		//Check if all required elements of the starter form have input.
		for(var i = 0 ; i < starterFormElements.length; i++)
		{
			if (starterFormElements[i][1] == ""){
				alert(starterFormElements[i][0] + " must be filled out.");
				return false;
			}
		}
		
		const destPhotos = document.getElementById('destPhotos');
	
		if (destPhotos.files.length > 0) {
			for (var i = 0; i <= destPhotos.files.length - 1; i++) {
				
				var fsize = destPhotos.files.item(i).size; 
				var filePath = destPhotos.files.item(i).name;
				var extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
				
				if( i == 3){
					alert("You are providing too many pictures, the limit is three.");
					return false;
				}
				
				if (fsize > 3097152){
					alert("File too Big, please select a file less than 3MB");
					return false;
				}
				
				if( extension != "jpg" && extension != "jpeg" ){
			        alert('Please upload a destination photo that is a .jpeg or a .jpg.');
			        return false;
				}
			}
		}
		return true;
	}catch(err) {
		alert("Error: " + err.description);
		console.log( err.stack );
	    return false;
	}
	
}

function validateNewForm(){
	try{
		//array of all element values for the New form.
		var devNewVarNameArray = $("#newDevVarNamesSet").val().split(",");
		var isValid = false;
		
		for(var i = 0 ; i < devNewVarNameArray.length; i++)
		{
			if($("#"+devNewVarNameArray[i]).val() > 0){
				isValid = true;
			}
		}
		if(!isValid){
			alert("You have not selected anything on the new deployement form.");
			return isValid;
		}
		else{
			return isValid;
		}
	}catch(err) {
		alert("Error: " + err.description);
		console.log( err.stack );
	    return false;
	}
}

function validateRelocateForm(){
	try{
		//array of all element values for the starter form.
		var RelocationFormElements =
			[
				["Current Column Location", 	$('#reloColLoc').val()], 
				["Current Process Point", 		$('#reloProcPt1').val()], 
				["Current Station Type", 		$('#curStaTyp').val()], 
				["Node Identification Number", 	$('#nodeIdNum').val()], 
				["Number of Controllers", 		$('#cntlrQty').val()]
			]
		
		//Check if all required elements of the starter form have input.
		for(var i = 0 ; i < RelocationFormElements.length; i++)
		{
			if (RelocationFormElements[i][1] == ""){
				alert(RelocationFormElements[i][0] + " must be filled out.");
				return false;
			}
		}
		
		const reloPhotos = document.getElementById('reloPhotos');
		
		if (reloPhotos.files.length > 0) {
			for (var i = 0; i <= reloPhotos.files.length - 1; i++) {
				
				var fsize = reloPhotos.files.item(i).size; 
				var filePath = reloPhotos.files.item(i).name;
				var extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
							
				if( i == 3){
					alert("You are providing too many pictures, the limit is three.");
					return false;
				}
				
				if (fsize > 3097152){
					alert("File too Big, please select a file less than 3MB");
					return false;
				}
				
				if( extension != "jpg" && extension != "jpeg" ){
			        alert('Please upload a current location photo that is a .jpeg or a .jpg.');
			        return false;
				}
			}
		}
		
		return true;
	}catch(err) {
		alert("Error: " + err.description);
		console.log( err.stack );
	    return false;
	}
}

function validateInventoryForm(){
	try{
		//array of all element values for the New form.
		var devInvVarNameArray = $("#invDevVarNamesSet").val().split(",");
		var isValid = false;
		
		for(var i = 0 ; i < devInvVarNameArray.length; i++)
		{
			if($("#"+devInvVarNameArray[i]).val() > 0){
				isValid = true;
			}
		}
		if(!isValid){
			alert("You have not selected anything on the current inventory form.");
			return isValid;
		}
		else{
			return isValid;
		}
	}catch(err) {
		alert("Error: " + err.description);
		console.log( err.stack );
	    return false;
	}
}

function validateForms(){
	try{
		var isStarterValid = validateStarterForm();
		if($('#reqType').val() == "New"){
			if(isStarterValid){
				var isNewValid;
				isNewValid = validateNewForm();
				if(isNewValid){
					sendNewInvFormData();
				}
			}
		}
		else if($('#reqType').val() == "Relocate"){
			if(isStarterValid){
				var isRelocationValid = validateRelocateForm();
				if(isRelocationValid){
					var isInventoryValid = validateInventoryForm();
					if(isInventoryValid){
						sendNewInvFormData();
					}
				}
			}
		}
		else if($('#reqType').val() == "Relocate and Customize"){
			if(isStarterValid){
				var isNewValid = validateNewForm();
				if(isNewValid){
					var isRelocationValid = validateRelocateForm();
					if(isRelocationValid){
						var isInventoryValid = validateInventoryForm();
						if(isInventoryValid){
							sendNewInvFormData();
						}
					}
				}
			}
		}
	}catch(err) {
		alert("Error: " + err.description);
		console.log( err.stack );
	    return false;
	}
}
