<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Exception details</title>
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
				<h2>Exception details</h2> 
				
				<table>
					<tr>
						<td>Schedule id</td>
						<td>Scheduled by</td>
						<td>Requested</td>
						<td>Run date</td>
						<td>Completed</td>
						<td>Template</td>
					</tr>					 
					<tr>
						<td><c:out value="${schedule.scheduleId}" /></td>
						<td><a href="${pageContext.servletContext.contextPath}/search/schedule-detail.do?scheduleId=<c:out value="${schedule.scheduleId}" />"><c:out value="${schedule.scheduledByName}" /></a></td>
						<td><fmt:formatDate  value="${schedule.requestDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.startDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${schedule.endDate}"/></td>
						<td><c:out value="${schedule.templateId}" /></td>
					</tr>					
				</table>					
				<table>					
					<tr>
						<th></th>
						<th>Functional Area</th>
						<th>Message</th>
						<th>Exception</th>			
					</tr>	
					 <c:forEach var="exception" items="${exceptions}"> 
					<tr class="light">
						<td class="light"><!-- <input type="checkbox"/> --></td>
						<td class="light"><c:out value="${exception.functionalArea}" /></td>
						<td class="light"><c:out value="${exception.message}" /></td>
						<td class="light"><c:out value="${exception.exception}" /></td>
					</tr>
					</c:forEach>
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