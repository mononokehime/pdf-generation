<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="cache-control" content="no-cache" />
<title>Login to F&G homes</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
</head>

<body onload="document.forms[0].j_username.focus()"
	class="bodyBackground">
<!-- login.jsp -->
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
<security:authorize ifAllGranted="ROLE_USER">
	<jsp:include page="/WEB-INF/jsp/includes/menue.jsp" />
</security:authorize>
<table class="mainTable" border="0">
	<tr>
		<td width="50%" valign="top">
		<div class="marginTop">
			<div class="explanationBox">
				<p><strong><spring:message code="loginPage.explanation.title1"/></strong></p>
				<p><spring:message code="loginPage.explanation.body.general3"/></p>
				<p><spring:message code="loginPage.explanation.body.general4"/></p>
				<p><spring:message code="loginPage.explanation.body.general5"/><a href='http://www.energysavingtrust.org.uk' class="prinav">www.energysavingtrust.org.uk</a>
					<spring:message code="loginPage.explanation.body.general5a"/>
				</p>
				
				<p><spring:message code="loginPage.explanation.body.lostPassword"/></p>
				<p><spring:message code="loginPage.explanation.body.security"/></p>
			</div>
		</div>
		</td>
		<td align="right" valign="top">
					
			
		
		<security:authorize ifAllGranted="ROLE_USER">
			<div class="boxLogin"><span class="warningMessage"> You
			are already logged-in </span></div>
		</security:authorize>
		<security:authorize ifNotGranted="ROLE_USER">
			<form id="j_spring_security_check" action="<c:out value="${pageContext.servletContext.contextPath}" />/j_spring_security_check"
				method="post">

			<div class="boxLogin">Username&nbsp; <input type="text" name="j_username" maxlength="32" size="25" /> <br />
			<br />

			Password&nbsp; <input type="password" name="j_password" maxlength="12" size="25" /><br />
		
			<c:if test="${loginMessage != null}">
				<span id="errorOnRed"><c:out value="${loginMessage}" /></span>	
				
			</c:if>		
			<br />

			<input type="submit" value="Login" id="submit" />
			</div>
			</form>
			FG_0_5
			<form:form commandName="model" action="${pageContext.servletContext.contextPath}/reset-password.do" method="post">	
			<form:errors />
			<div class="boxLogin"><span class="blueTitle">Reset
			your password</span><br />
			<c:if test="${message != null}">
				<span id="errorOnRed"><c:out value="${message}" /></span>			
			</c:if>
			
			<br />
			Username&nbsp; <form:input path="userName" maxlength="32" /> 
			<br />
			<span id="errorOnRed"><form:errors path="userName" /></span><br />
			<br />

			<input type="submit" value="submit" id="submit" /></div>
			</form:form>  
		</security:authorize></td>
		<td width="10%">&nbsp;</td>
	</tr>
</table>

<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %> 
</body>
</html>