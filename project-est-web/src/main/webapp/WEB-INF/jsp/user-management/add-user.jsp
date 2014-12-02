<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Add User</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/epc.js"></script>
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/mootools.js"></script>
	</head>

	<body class="bodyBackground" onload="hideMenuesEdit()">
		<!-- editUser.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 

		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
				<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
		
				</td>
				<td valign="top">
				<form:form commandName="model" action="${pageContext.servletContext.contextPath}/user-management/add-user.do" method="post">
				
					
						<form:hidden path="userId" />
						<form:hidden path="active" />
						<table class="formBox">
						<tr id="field_tr">
   							<td  align="left" colspan="2"><strong>All fields are required</strong></td>
						</tr>					
						<tr id="field_tr">
   							<td class="tdLabel"><label for="Forename" class="label">Forename:</label></td>
   							<td>
								<form:input path="firstName" maxlength="16"/> <span id="errorOnYellow"><form:errors path="firstName" /></span><td>
							</td>
						</tr>	
						<tr id="field_tr">
   							<td class="tdLabel"><label for="Surname" class="label">Surname:</label></td>
   							<td>
								<form:input path="familyName" maxlength="32"/> <span id="errorOnYellow"><form:errors path="familyName" /></span>
							</td>
						</tr>
						<tr id="field_tr">
   							<td class="tdLabel"><label for="Username" class="label">Username:</label></td>
   							<td>
								<form:input path="userName" maxlength="32" /><span id="errorOnYellow"><form:errors path="userName" /></span>
							</td>
						</tr>	
						<tr id="field_tr">
   							<td class="tdLabel"><label for="Email" class="label">Email:</label></td>
   							<td>
								<form:input path="emailAddress" maxlength="32" /><span id="errorOnYellow"><form:errors path="emailAddress" /></span>
							</td>
						</tr>
						<tr>
						      <td class="tdLabel"><label for="Roles" class="label">Roles:</label></td>
						      <td>
							
						      <form:select path="userRoleId" multiple="true" items="${roleMap}" size="3"/>

						      
						      	<span id="errorOnYellow"><form:errors path="userRoleId" /></span>
						           
						      </td>
						  </tr>						

							<tr>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
							
							<tr>
								<td colspan="2" align="right">
								<input type="submit" id="submit"  value="Save Changes" class="button"/>

								</td>
							</tr>

						</table>
					
					</form:form>
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>