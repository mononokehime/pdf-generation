<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Error 500 page</title>
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
				
	<h2>Invalid date entered.</h2>
	<p>This should have been captured before this point. Please contact the system administrator and send them the error which you can see in the source of this page.</p>
		
	<!-- 
	Here is the error to send to the administrator
	<c:out value="${exceptionStack}"/>
	 -->
				

				
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>