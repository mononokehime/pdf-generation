<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Search history</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/epc.js"></script>
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/mootools.js"></script>
	</head>

	<body class="bodyBackground" onload="hideMenues()">
		<!-- editUser.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 

		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
				<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
		
				</td>
				<td valign="top">
				<h2>Search schedule history</h2>
				<strong>Select your search options below</strong>
				
				<form:form commandName="searchParameters" action="${pageContext.servletContext.contextPath}/search/search-history.do" method="post">				
				<table>
				<tr>
					<td nowrap class="label">Energy rating</td>
					<td><form:select path="rating">
						<form:options items="${ratingsList}" itemValue="referenceDataKey" itemLabel="shortName" />
					</form:select></td>
				</tr>
				<tr>
					<td class="label">Country</td>
					<td><form:select id="country" path="country" onchange="hideMenues()">
						<form:options items="${countriesList}" itemValue="referenceDataKey" itemLabel="shortName" />
					</form:select></td>
				</tr>	
				<tr id="england_tr">
 					<td ><label for="english_centres" class="label">English ESTAC:</label></td>
 					<td>
					<form:select path="ESTAC" id="english_centres">
						<form:option value="" label="--"/>
						<form:options items="${ESTACsList064}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>
				<tr id="wales_tr">
 					<td ><label for="welsh_centres" class="label">Welsh ESTAC:</label></td>
 					<td>
					<form:select path="ESTAC" id="welsh_centres">
						<form:option value="" label="--"/>
						<form:options items="${ESTACsList220}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>
				<tr id="scotland_tr">
 					<td ><label for="scottish_centres" class="label">Scottish ESTAC:</label></td>
 					<td>
					<form:select path="ESTAC" id="scottish_centres">
						<form:option value="" label="--"/>
						<form:options items="${ESTACsList179}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>				
				<tr id="england_la_tr">
 					<td ><label for="english_la_centres" class="label">English LAs:</label></td>
 					<td>
					<form:select path="localAuthority" id="english_la_centres">
						<form:option value="" label="--"/>
						<form:options items="${LAsList064}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>
				<tr id="wales_la_tr">
 					<td ><label for="welsh_la_centres" class="label">Welsh LAs:</label></td>
 					<td>
					<form:select path="localAuthority" id="welsh_la_centres">
						<form:option value="" label="--"/>
						<form:options items="${LAsList220}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>
				<tr id="scotland_la_tr">
 					<td ><label for="scottish_la_centres" class="label">Scottish LAs:</label></td>
 					<td>
					<form:select path="localAuthority" id="scottish_la_centres">
						<form:option value="" label="--"/>
						<form:options items="${LAsList179}" itemValue="referenceDataKey"  itemLabel="shortName" />
					</form:select> 							
					</td>
				</tr>								
				<tr>
					<td class="label">Consumer segment</td>
					<td><form:select path="consumerSegment">
						<form:options items="${consumerSegmentList}" itemValue="referenceDataKey" itemLabel="shortName" />
					</form:select></td>
				</tr>	
				<tr>
					<td class="label" nowrap>Sold date from (dd-mm-yyyy)</td>
					<td><form:input path="fromDate" maxlength="10" /> <span id="errorOnYellow"><form:errors path="fromDate" /></span></td>
				</tr>	
				<tr>
					<td class="label" nowrap>Sold date to (dd-mm-yyyy)</td>
					<td><form:input path="toDate" maxlength="10" /> <span id="errorOnYellow"><form:errors path="toDate" /></span></td>
				</tr>	
				<tr>
					<td class="label">Status</td>
					<td><form:select path="workflowStatus">
						<form:option value="scheduled">Scheduled</form:option>
						<form:option value="completed">Completed</form:option>	
						<form:option value="failed">Failed</form:option>	
						<form:option value="ftp-success">FTP Success</form:option>	
						<form:option value="ftp-failed">FTP Failed</form:option>						
					</form:select></td>
				</tr>				
				<tr>
					<td></td>
					<td><input type="submit" value="Search" id="submit" /></td>
				</tr>		
				</table>

				</form:form>
				</td>
				<td width="5%">
					&nbsp;
				</td>
			</tr>
		</table>
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
		
	</body>
</html>