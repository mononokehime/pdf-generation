<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Change password</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/epc.js"></script>
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/mootools.js"></script>
	</head>

	<body class="bodyBackground">
		<!-- changePassword.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 
		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
					<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
				</td>
				<td align="left" valign="top">
				<form:form commandName="model" action="${pageContext.servletContext.contextPath}/user-management/change-password.do" method="post">
			
					
					<form:hidden path="userId"/>
					<table class="changePassword">
							<tr>
								<td colspan="2" align="right">
								<span id="errorOnRed"><form:errors path="passwordModel.oldPassword" /></span><br/>
								Old password: <form:password path="passwordModel.oldPassword" maxlength="12"/> 
								
								
								</td>
							</tr>					
							<tr>
								<td colspan="2" align="right">
								<span id="errorOnRed"><form:errors path="passwordModel.newPassword" /></span><br/>
								New password: <form:password path="passwordModel.newPassword" maxlength="12"/> 
								</td>
							</tr>
							<tr>
								<td colspan="2" align="right">
								<span id="errorOnRed"><form:errors path="passwordModel.newPasswordConfirm"/></span><br/>
								Confirm password: <form:password path="passwordModel.newPasswordConfirm" maxlength="12"/> 
								</td>
							</tr>
				
							
							<tr><td colspan="2"><input id="submit" type="submit" value="Change"/></td></tr>

							
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