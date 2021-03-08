<%@ include file='../BAheader.jsp' %>
	<!-- Navigation -->
	<%@ include file='../BAnav.jsp' %>
	
	<div class="container">
			<input type="hidden" name="hasFailed" id="hasFailed" value="${hasFailed}">
			<script type="text/javascript">if($("#hasFailed").val() === "true"){alert("Authentication Failed.")}</script>
			<form id="loginForm" action="<c:url value="/BAEstimation/Admin"/>" method="post"
			onkeydown="return event.key != 'Enter';">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="row">
    				<div class="col-40" align="right"><label for="userId">User ID:</label></div>
					<div class="col-60" align="left"><input type="text" id="userId" value="" name="userId" style="width: 50%"/></div>
	  			</div>
	  			<div class="row">
    				<div class="col-40" align="right"><label for="userPassword">Password:</label></div>
					<div class="col-60" align="left"><input type="password" id="userPassword" value="" name="userPassword"  style="width: 50%"/></div>
	  			</div>
	  			<div class="row">
	  				<input type="submit" id="loginButton" value="Login" />
	  			</div>
			</form>
		</div>
<%@ include file='../BAfooter.jsp' %>