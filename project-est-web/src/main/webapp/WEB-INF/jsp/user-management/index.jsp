<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>User management home</title>
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
				<h2>Manage Users</h2>
					<a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/search-users.do">Search Users</a><br/>
					<a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/add-user.do">Add New User</a>
				
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>