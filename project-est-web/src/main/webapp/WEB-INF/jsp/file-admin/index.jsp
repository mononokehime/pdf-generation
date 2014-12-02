<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Upload Completion Summaries</title>
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
				<h2>Recent File Upload Completion Summary</h2>  
				Note that this page is a summary of files that have been uploaded and processed by the server. Usually there is a 24 hour delay. (click error count for details)
				<c:choose>
					<c:when test="${fn:length(excelResults) > 0}">
				<table width="100%">
				<tr>
				<td valign="top">
				<table >
				<tr>
					<td colspan="5"><b>Land Registry Uploads</b></td>
				</tr>
				<tr>
					<td><b>Id</b></td>
					<td><b>Start time</b></td>
					<td><b>End time</b></td>
					<td><b>Successfully<br/>entered</b></td>
					<td><b>Errors</b></td>
				</tr>					
				 <c:forEach var="result" items="${excelResults}"> 
				<tr>
					<td><c:out value="${result.uploadSummaryId}" /></td>
					<td><fmt:formatDate pattern="dd-MM-yyyy  HH:mm:ss" value="${result.startTime}"/></td>
					<td><fmt:formatDate pattern="dd-MM-yyyy  HH:mm:ss" value="${result.endTime}"/></td>
					<td><c:out value="${result.numberOfRows-result.errorCount}" /></td>
					<td>
						<!-- only display link if was a failure -->
						  <c:choose>					  
						   <c:when test="${result.errorCount > 0}">
						  <a href="${pageContext.request.contextPath}/file-admin/upload-errors.do?processId=<c:out value="${result.uploadSummaryId}" />&amp;startPoint=0"><c:out value="${result.errorCount}" /></a>
						  </c:when>
						  <c:otherwise>
						  <c:out value="${result.errorCount}" />
						  </c:otherwise>
						  </c:choose>					
					
					</td>
				</tr>	
				</c:forEach>				
			
				</table>
				  </c:when>
				  <c:otherwise>
				<table width="100%">
				<tr>
				<td valign="top">
				<table>
				<tr>
					<td colspan="5"><b>Land Registry Uploads</b></td>
				</tr>
				<tr>
					<td colspan="5">No uploads yet</td>
				</tr>					
			
			
				</table>
				  </c:otherwise>
				  </c:choose>
				</td>
				<td valign="top">
				<table>
				<tr>
					<td colspan="5" valign="top"><b>Landmark Uploads</b></td>
				</tr>
				<c:choose>
					<c:when test="${fn:length(epcResults) > 0}">				
				<tr>
					<td><b>Id</b></td>
					<td><b>Start time</b></td>
					<td><b>End time</b></td>
					<td><b>Successfully<br/>entered</b></td>
					<td><b>Errors</b></td>
				</tr>					
				 <c:forEach var="result" items="${epcResults}"> 
				<tr>
					<td><c:out value="${result.uploadSummaryId}" /></td>
					<td><fmt:formatDate pattern="dd-MM-yyyy  HH:mm:ss" value="${result.startTime}"/></td>
					<td><fmt:formatDate pattern="dd-MM-yyyy  HH:mm:ss" value="${result.endTime}"/></td>
					<td><c:out value="${result.numberOfRows-result.errorCount}" /></td>
					<td><a href="${pageContext.request.contextPath}/file-admin/upload-errors.do?processId=<c:out value="${result.uploadSummaryId}" />&amp;startPoint=0"><c:out value="${result.errorCount}" /></a></td>
				</tr>	
				</c:forEach>	
				  </c:when>
				  <c:otherwise>	
				<tr>
					<td colspan="5">No Landmark Uploads yet</td>			
				</tr>				  
				</c:otherwise>
				  </c:choose>				  
				</table>				
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