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

	<body class="bodyBackground"">
		<!-- confirmUserAdded.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 

		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
				<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
				</td>
				<td valign="top">


						<table class="formBox">
							<tr>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="2">
									
									<strong>User successfuly <c:out value="${message}"/></strong>
							
								</td>
							</tr>
							<tr>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>

							<tr>
								<td>
									Username: <c:out value="${model.userName}"/>
								</td>
								<td>
									
								</td>
							</tr>
							<tr>
								<td>
									Forename: <c:out value="${model.firstName}"/>
								</td>
								<td>
								
								</td>
							</tr>
							<tr>
								<td>
									Surname: <c:out value="${model.familyName}"/>
								</td>
								<td>
								
								</td>
							</tr>
							<tr>
								<td>
									Email: <c:out value="${model.emailAddress}"/>
								</td>
								<td>
						
								</td>
							</tr>

							<tr>
								<td>
									Status: 
										     <c:choose> 
												  <c:when test="${model.active}" > 
												    enabled
												  </c:when> 
												  <c:otherwise> 
												    disabled
												  </c:otherwise> 
											</c:choose> 									
								</td>
								<td>
			
								</td>
							</tr>
							
								<tr>
									<td>
									Role(s):
									<c:forEach var="role" items="${model.userRoleSet}">
									<c:out value="${role.roleName}"/>&nbsp;
						      							   
							        </c:forEach>	
									</td>
									<td>

									</td>
								</tr>
							
							<tr>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>



						</table>
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</body>
</html>