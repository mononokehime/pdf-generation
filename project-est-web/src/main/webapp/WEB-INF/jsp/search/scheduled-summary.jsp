<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Schedule results</title>
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
				<h2>Schedule details</h2>
				<c:choose>
					<c:when test="${message == null}">
				Schedule id: <c:out value="${schedule.scheduleId}"/><br/>
				Requested on: <fmt:formatDate  pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.requestDate}"/><br/>
				Will run on: <fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.startDate}"/><br/>
					</c:when>
					<c:otherwise>
						<c:out value="${message}"/>
					</c:otherwise>			
				</c:choose>


				
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>