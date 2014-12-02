<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Search</title>
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
				<h2>Schedule results</h2>   <a href="<c:out value="${pageContext.servletContext.contextPath}" />/search/index.do">New search</a> | <a href="<c:out value="${pageContext.servletContext.contextPath}" />/search/index.do<c:out value="${holder.searchString}"/>">Amend criteria</a></h2>
				
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
				<form:form action="${pageContext.servletContext.contextPath}/search/schedule.do" method="post">
				<td align="right" colspan="4">Schedule all results for (dd-mm-yyyy)
				<input type="text" name="startDate" value="<c:out value="${startDate}"/>"/>
				<br/>
				<span id="errorOnYellow"><c:out value="${message}"/></span>
				</td>
				<td colspan="3"> using template
				<c:forEach var="template" items="${templates}"> 
					<input type="radio" name="templateId" checked="checked" value="${template.referenceDataKey}"/> <a target="_blank" href="<c:out value="${pageContext.servletContext.contextPath}" />/template/template-display.do?template=${template.referenceDataKey}">${template.shortName}</a><br/>
				</c:forEach>
		
				</td>
				<td><input type="submit" value="Schedule" id="submit" /></td>
				</form:form>
			</tr>
					<tr>
						<td colspan="8"><strong>Showing <fmt:formatNumber value="${holder.currentPage * holder.resultsPerPage +1}"/> - <fmt:formatNumber value="${(holder.currentPage * holder.resultsPerPage)+fn:length(holder.results)}"/> of <fmt:formatNumber value="${holder.totalResults  }"/> results</td>				
					</tr>				
					
					<tr>
						<th></th>
						<th>Address</th>
						<th>Rating</th>
						<th>ESTAC</th>
						<th>Country</th>
						<!-- <th>Treatment</th> -->
						<th>EPC date</th>	
						<th>Sold date</th>						
						<th>Consumer segment</th>				
					</tr>	
					 <c:forEach var="result" items="${holder.results}"> 
					<tr class="light">
						<td class="light"><!-- key: <c:out value="${result.addressKey}" />, id:<c:out value="${result.propertyAddressId}" /><input type="checkbox"/> --></td>
						<td class="light"><c:out value="${result.addressLine1}" /> <c:out value="${result.addressLine2}" /> <c:out value="${result.addressLine3}" /> <c:out value="${result.town}" /> <c:out value="${result.postcodeOutcode}" /> <c:out value="${result.postcodeIncode}" /></td>
						<td class="light"><c:out value="${result.rating}" /></td>
						<td class="light"><c:out value="${result.ESTACName}" /></td>
						<td class="light"><c:out value="${result.countryName}" /></td>
						<!-- <td class="light">&nbsp;</td> -->
						<td class="light"><fmt:formatDate  value="${result.inspectionDate}"/></td>	
						<td class="light"><fmt:formatDate  value="${result.saleDate}"/></td>						
						<td class="light"><c:out value="${result.consumerSegment}" /></td>
					</tr>
					</c:forEach>

					<tr>
					<form name="pager" action="${pageContext.servletContext.contextPath}/search/pager.do" method="post">
					  <td colspan="8">
					  
					  Page of <fmt:formatNumber value="${holder.currentPage +1 }"/> of <c:out value="${holder.totalPages }" />
					  <c:if test="${holder.totalPages > 1}">
					  | <span id="checkField"><a href="javascript:validate();">Go to page:</a> <input type="text" size="1" maxlength="3" name="page" value=""/> </span>
					  <c:choose>					  
					  <c:when test="${holder.currentPage != 0}">
					  <a href="${pageContext.servletContext.contextPath}/search/pager.do?page=1">first</a>  
					  </c:when>
					  <c:otherwise>
					  first
					  </c:otherwise>
					  </c:choose>
					  |
					  <c:choose>					  
					  <c:when test="${holder.currentPage != 0}">
					  <a href="${pageContext.servletContext.contextPath}/search/pager.do?page=<fmt:formatNumber value="${holder.currentPage }"/>">previous</a> 
					  </c:when>
					  <c:otherwise>
					  previous
					  </c:otherwise>
					  </c:choose>						  
					  |
					  <c:choose>					  
					  <c:when test="${holder.currentPage+1 != holder.totalPages}">
					  <a href="${pageContext.servletContext.contextPath}/search/pager.do?page=<fmt:formatNumber value="${holder.currentPage +2 }"/>">next</a> 
					  </c:when>
					  <c:otherwise>
					  next
					  </c:otherwise>
					  </c:choose>					  					  					  
					  | 
					  <c:choose>					  
					  <c:when test="${holder.currentPage+1 != holder.totalPages}">
					  <a href="${pageContext.servletContext.contextPath}/search/pager.do?page=<c:out value="${holder.totalPages }" />">last</a> 
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