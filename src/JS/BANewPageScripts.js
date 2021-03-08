/*
 * This functions builds a jQuery command that will clear all device 
 * quantity selections on the web page.
 */ 
function zeroAllNew(){
	var devNewVarNameArray = $("#newDevVarNamesSet").val().split(",");
	for(var i=0; i<devNewVarNameArray.length; i++){$("#"+devNewVarNameArray[i]).val("0");}
}

/*
 * This functions builds a jQuery command, from currently saved standard package configurations, 
 * that will set all device quantity selections on the web page accordingly.
 */
function setNewEqptVals() {
	var pkgName = $('#newPkgSelection').val();
	if (pkgName === ""){zeroAllNew();}
	else{
		$.ajax({
	        type: "GET", url: "mobile/ws/getPkgSet", data: "pkgName="+pkgName, dataType: "text", 
	        async: true, beforeSend: function(){}, complete: function(){}, 
	        success:function(data){
	        	zeroAllNew();
	        	if (data != "") {
	        		var varNamesAndValsMap = [];
	        		var varNamesAndValsPairArray = data.split("\^");
	        		var tempString = "";
	        		
	        		for(var i=0; i<varNamesAndValsPairArray.length; i++){
	        			tempString = varNamesAndValsPairArray[i];
	        			var separatedNameAndValsArray = tempString.split(",");
	        			varNamesAndValsMap.push(separatedNameAndValsArray);
	        			$("#"+varNamesAndValsMap[i][0]).val(varNamesAndValsMap[i][1]);
	        		}
	        	}else{zeroAllNew();}
	        }
	    });
	}
}