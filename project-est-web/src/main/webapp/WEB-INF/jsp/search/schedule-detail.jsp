<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Schedule detail results</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
	<script>
	function validate()
	{
	
	   var isFound = /^-?\d+$/.test(document.pager.page.value);
	   if (isFound)
	   {
	   	document.pager.submit();
	   }
	   else
	   {
	   	document.getElementById('checkField').style.backgroundColor ='red';
	   	alert('Page must be a number');
	   	document.pager.page.focus();
	   }
	   //document.pager.submit();
	}
	
	</script>
	
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
				<h2>Details of Schedule <c:out value="${schedule.scheduleId}" /> run on <fmt:formatDate pattern="dd-MM-yyyy" value="${schedule.startDate}"/></h2> 
				
				<c:choose>
				<c:when test="${holder.totalResults == 0}">
				<table>
					<tr>
						<th colspan="7">No results returned</th>				
					</tr>
				</table>
				</c:when>
				<c:otherwise>				
				<table>
					<tr>
						<td>Schedule id</td>
						<td>Scheduled by</td>
						<td>Request time</td>
						<td>Run time</td>
						<td>Completed time</td>
						<td>Template</td>
					</tr>					 
					<tr>
						<td><c:out value="${schedule.scheduleId}" /></td>
						<td><c:out value="${schedule.scheduledByName}" /></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${schedule.requestDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${schedule.startDate}"/></td>
						<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${schedule.endDate}"/></td>
						<td><c:out value="${schedule.templateId}" /></td>
					</tr>					
				</table>					
				<table>
				
					<tr>
						<td colspan="10"><strong>Showing <fmt:formatNumber value="${holder.currentPage * holder.resultsPerPage +1}"/> - <fmt:formatNumber value="${(holder.currentPage * holder.resultsPerPage)+fn:length(holder.results)}"/> of <fmt:formatNumber value="${holder.totalResults  }"/> results attached to schedule</strong></td>				
					</tr>				
					
					<tr>
						<th></th>
						<th>Address</th>
						<th>Rating</th>
						<th>Country</th>
						<th>ESTAC</th>
						<th>Local Authority</th>
						<th>EPC date</th>	
						<th>Sold date</th>	
						<th>Status</th>						
						<th>Consumer segment</th>				
					</tr>	
					 <c:forEach var="result" items="${holder.results}"> 
					<tr class="light">
						<td class="light"><!-- key: <c:out value="${result.addressKey}" />, id: <c:out value="${result.propertyAddressId}" /><input type="checkbox"/> --></td>
						<!-- only display link if was a success -->
						  <c:choose>					  
						  <c:when test="${result.workFlowStatus == 'completed'}">
						  <td class="light"><a href="${pageContext.servletContext.contextPath}/search/view-pdf.do?propertyAddressId=<c:out value="${result.propertyAddressId}" />" target="_blank"><c:out value="${result.addressLine1}" /> <c:out value="${result.addressLine2}" /> <c:out value="${result.addressLine3}" /> <c:out value="${result.town}" /> <c:out value="${result.postcodeOutcode}" /> <c:out value="${result.postcodeIncode}" /></a></td>
						  </c:when>
						  <c:otherwise>
						  <td class="light"><c:out value="${result.addressLine1}" /> <c:out value="${result.addressLine2}" /> <c:out value="${result.addressLine3}" /> <c:out value="${result.town}" /> <c:out value="${result.postcodeOutcode}" /> <c:out value="${result.postcodeIncode}" /></td>
						  </c:otherwise>
						  </c:choose>						
						
						<td class="light"><c:out value="${result.rating}" /></td>
						<td class="light"><c:out value="${result.countryName}" /></td>
						<td class="light"><c:out value="${result.ESTACName}" /></td>
						<td class="light"><c:out value="${result.localAuthorityName}" /></td>
						<td class="light"><fmt:formatDate pattern="dd-MM-yyyy"  value="${result.inspectionDate}"/></td>
						<td class="light"><fmt:formatDate pattern="dd-MM-yyyy"  value="${result.saleDate}"/></td>
						<td class="light">
						<!-- only display link if was a failure -->
						  <c:choose>					  
						   <c:when test="${result.workFlowStatus == 'failed' || result.workFlowStatus == 'invalid'}">
						  <a href="${pageContext.servletContext.contextPath}/search/schedule-detail-exception.do?processId=<c:out value="${result.propertyAddressId}" />"><c:out value="${result.workFlowStatus}" /></a>
						  </c:when>
						  <c:otherwise>
						  <c:out value="${result.workFlowStatus}" />
						  </c:otherwise>
						  </c:choose>						
						</td> 
												
						<td class="light"><c:out value="${result.consumerSegment}" /></td>
					</tr>
					</c:forEach>

					<tr>
					<form name="pager" action="${pageContext.servletContext.contextPath}/search/schedule-pager.do" method="post">
					  <td colspan="10">
					  
					  Page of <fmt:formatNumber value="${holder.currentPage +1 }"/> of <c:out value="${holder.totalPages }" />
					  <c:if test="${holder.totalPages > 1}">
					  | <span id="checkField"><a href="javascript:validate();">Go to page:</a> <input type="text" size="1" maxlength="3" name="page" value=""/> </span>
					  <c:choose>					  
					  <c:when test="${holder.currentPage != 0}">
					  <a href="${pageContext.servletContext.contextPath}/search/schedule-pager.do?page=1">first</a>  
					  </c:when>
					  <c:otherwise>
					  first
					  </c:otherwise>
					  </c:choose>
					  |
					  <c:choose>					  
					  <c:when test="${holder.currentPage != 0}">
					  <a href="${pageContext.servletContext.contextPath}/search/schedule-pager.do?page=<fmt:formatNumber value="${holder.currentPage }"/>">previous</a> 
					  </c:when>
					  <c:otherwise>
					  previous
					  </c:otherwise>
					  </c:choose>						  
					  |
					  <c:choose>					  
					  <c:when test="${holder.currentPage+1 != holder.totalPages}">
					  <a href="${pageContext.servletContext.contextPath}/search/schedule-pager.do?page=<fmt:formatNumber value="${holder.currentPage +2 }"/>">next</a> 
					  </c:when>
					  <c:otherwise>
					  next
					  </c:otherwise>
					  </c:choose>					  					  					  
					  | 
					  <c:choose>					  
					  <c:when test="${holder.currentPage+1 != holder.totalPages}">
					  <a href="${pageContext.servletContext.contextPath}/search/schedule-pager.do?page=<c:out value="${holder.totalPages }" />">last</a> 
					  </c:when>
					  <c:otherwise>
					  last
					  </c:otherwise>
					  </c:choose>					  
					  </c:if>
					  </td>	
					  </form>
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