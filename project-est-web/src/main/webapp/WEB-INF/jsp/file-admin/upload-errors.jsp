<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Upload job errors</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />

	
	</head>
	<c:set var="nextResults"/>
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
				<h2>Total Errors for batch: <c:out value="${summary.errorCount}" /></h2>  
				Only occur when address key and/or ESTAC cannot be found (064 is England, 220 is Scotland)
				<table>
		
				<tr>
					<td><b>Reason</b></td>
					<td><b>Address</b></td>
					<td><b>Post code</b></td>
					<td><b>Address key</b></td>
					<td><b>ESTAC</b></td>
					<td><b>Local Authority</b></td>
					<td><b>Country</b></td>
					<td><b>Centre</b></td>
					<td nowrap><b>Address matches for post code<br/>Only shown when no address match</b></td>					
				</tr>					
				 <c:forEach var="result" items="${results}"> 
				 	<c:set var="nextResults" value="${result.rownum}"/>
				<tr>
					<td valign="top"><c:out value="${result.workFlowStatus}" /></td>
					<td valign="top"><c:out value="${result.addressLine1}" /> <c:out value="${result.addressLine2}" /> <c:out value="${result.addressLine3}" /> <br/><c:out value="${result.town}" /></td>
					<td valign="top"><c:out value="${result.postcodeOutcode}" /> <c:out value="${result.postcodeIncode}" /></td>
					<td valign="top">
					  <c:choose>					  
					  <c:when test="${result.addressKey == null}">
					  no key (cannot create pdf)<br />
					 <!--  <c:out value="${result.propertyAddressId}" /> -->
					  </c:when>
					  <c:otherwise>
					  <c:out value="${result.addressKey}" /><br />
					 <!--  <c:out value="${result.propertyAddressId}" /> -->
					  </c:otherwise>
					  </c:choose>					
					</td>
					<td valign="top"><c:out value="${result.ESTAC}" /></td>
					<td valign="top"><c:out value="${result.localAuthority}" /></td>
					<td valign="top"><c:out value="${result.country}" /></td>
					<td valign="top"><c:out value="${result.centre.centreName}" /></td>
					<td nowrap valign="top">
						<!-- no need to show all infor for duplicates -->
						  <c:choose>					  
						   <c:when test="${result.workFlowStatus == 'invalid'}">
							<c:forEach var="nextResult" items="${result.possibleAddressMatches}"> 
						    <c:out value="${nextResult.address}" /><br/>
						    </c:forEach>
						  </c:when>
						  <c:otherwise>
						  
						  </c:otherwise>
						  </c:choose>						

					</td>					
				</tr>	
				</c:forEach>	
				<c:if test="${summary.errorCount > nextResults}">			
				<tr>
					<td colspan="9"><b><a href="<c:out value="${pageContext.servletContext.contextPath}" />/file-admin/upload-errors.do?processId=<c:out value="${summary.uploadSummaryId}" />&amp;startPoint=<c:out value="${nextResults}" />">next results</a></b></td>
				</tr>
				</c:if>			
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