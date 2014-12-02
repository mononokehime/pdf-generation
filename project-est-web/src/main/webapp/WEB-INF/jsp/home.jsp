<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>F&G Homes start page</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
	</head>

	<body class="bodyBackground">
		<!-- editUser.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 

		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
				<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
		
				</td>
				<td valign="top">
				
	<h2>Where do you want to go?</h2>
	<a href="<c:out value="${pageContext.servletContext.contextPath}" />/search/index.do">Search and schedule mail outs</a> - find properties to send PDF reports to<br/>
		<a href="<c:out value="${pageContext.servletContext.contextPath}" />/search/history-index.do">Search mail out history</a> - search the history of PDF reports<br/>
		<a href="${pageContext.servletContext.contextPath}/file-admin/landmark-upload.do">Upload Landmark data</a><br/>
		<a href="${pageContext.servletContext.contextPath}/file-admin/land-registry-upload.do">Upload Land Registry data</a><br/>	
		<!-- <li><a href="search-exceptions.html">View exceptions</a> - view exceptions for data imports and PDF generation</li> -->
		
		<security:authorize ifAllGranted="ROLE_SUPER_USER">
			<a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/index.do">User Management - manage users</a>
		</security:authorize>		
		
	
				

				
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>