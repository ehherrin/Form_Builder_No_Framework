<!-- Column Location ListBox-->
<div class="row">
	<div class="col-25" align="center"><label for="reloColLoc">Current Column Location<font color="crimson">*</font></label></div>
	<div class="col-75">
		<input type="text" id="reloColLoc" name="reloColLoc" value="" pattern="^.{1,255}$" style="text-transform: uppercase" />
	</div>
</div>
<!-- Process Point TextBox -->
<div class="row">
	<div class="col-25" align="center">
		<label for="reloProcPt1">Current Process Point<font	color="crimson">*</font></label></div>
	<div class="col-75">
		<input type="text" id="reloProcPt1" name="reloProcPt1" value="" name="reloProcPt1" pattern="^.{1,255}$" 
		style="text-transform: uppercase" />
	</div>
</div>
<!-- Process Point TextBox -->
<div class="row">
	<div class="col-25" align="center"><label for="reloProcPt2">Second Current Process Point</label></div>
	<div class="col-75" align="left">
		<input type="text" id="reloProcPt2" value="" name="reloProcPt2" value="" name="reloProcPt2"	pattern="^.{1,255}$" 
		style="text-transform: uppercase" />
	</div>
</div>
<!-- Similar Process Point TextBox -->
<div class="row">
	<div class="col-25" align="center"><label for="simProcPt">Similar Process Point</label></div>
	<div class="col-75"><input type="text" id="simProcPt" name="simProcPt" pattern="^.{1,255}$"></div>
</div>
<!-- Current Station Type DropDown -->
<div class="row">
	<div class="col-25" align="center"><label for="curStaTyp">Current Station Type<font	color="crimson">*</font></label></div>
	<div class="col-75">
		<select id="curStaTyp" name="curStaTyp">
   			<option value=""></option>
			<c:forEach var="item" items="${listAllPkgNames}"><option value="${item.pkgName}">${item.pkgName}</option></c:forEach>
			<option value="Other">Other</option>
   		</select>
	</div>
</div>
<!-- Node Identification Number Input Field -->
<div class="row">
	<div class="col-25" align="center"><label for="nodeIdNum">Node Identification Number<font color="crimson">*</font></label></div>
	<div class="col-75"><input type="text" id="nodeIdNum" name="nodeIdNum" pattern="^.{1,255}$"></div>
</div>
<!-- Number of Controllers DropDown -->
<div class="row">
	<div class="col-25" align="center"><label for="cntlrQty">Number of Controllers<font	color="crimson">*</font></label></div>
	<div class="col-75">
		<select id="cntlrQty" name="cntlrQty">
			<option value=""></option>
			<c:forEach var="i" begin="0" end="${listContQty}" ><option value="${i}">${i}</option></c:forEach>
		</select>		
	</div>
</div>
<!-- Node Identification Number File Input Field -->
<div class="row">
	<div class="col-25" align="center"><label for="reloPhotos">Current Location Photo</label></div>
	<div class="col-75" align="left"><input type="file" name="reloPhotos" id="reloPhotos" accept="image/*" multiple/></div>
</div>