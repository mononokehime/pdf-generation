<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Schedule history results</title>
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
				<h2>Previous Schedule details</h2>
				<c:choose>
					<c:when test="${fn:length(schedules) > 0}">					
				<table>
					<tr>
						<td><b>Schedule id</b></td>
						<td><b>Scheduled by</b></td>
						<td><b>Requested</b></td>
						<td><b>Run date</b></td>
						<td><b>Completed</b></td>
						<td><b>Template</b></td>
					</tr>
					<c:forEach var="schedule" items="${schedules}"> 
					<tr>
						<td><a href="${pageContext.servletContext.contextPath}/search/schedule-detail.do?scheduleId=<c:out value="${schedule.scheduleId}" />"><c:out value="${schedule.scheduleId}" /></a></td>
						<td><c:out value="${schedule.scheduledByName}" /></td>
						<td><fmt:formatDate  value="${schedule.requestDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.startDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.endDate}"/></td>
						<td><c:out value="${schedule.templateId}" /></td>
					</tr>					
					</c:forEach>
				</table>	
				  </c:when>
				  <c:otherwise>	
				<table>
					<tr>
						<td colspan="6">No schedules yet</td>			
					</tr>
				</table>
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