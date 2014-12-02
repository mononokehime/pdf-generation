<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/includes/tag-libs.jsp" %> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="cache-control" content="no-cache" />
		<title>Search User</title>
<link href="<c:out value="${pageContext.servletContext.contextPath}" />/css/epc.css" rel="stylesheet"
	type="text/css" />
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/epc.js"></script>
		<script type="text/javascript" src="<c:out value="${pageContext.servletContext.contextPath}" />/scripts/mootools.js"></script>
		<script language="javascript">
				
		
		        function User() {
		        }
		        
		        function writeUsersList() {
			    var userList = document.getElementById("userList");
			    
			    var text = '<table cellpadding="0" border="0" class="searchResult" >\n';
				    
			    for(i = 0; i < nbUser; i++) {
				    var u = users[i];
				    
				    if(acceptUser(u)) {
					    text += '<tr>\n';
				      text += '<td width="30%">' + u.userName + '</td>\n';
				      text += '<td width="25%">' + u.emailAddress + '</td>\n';
				      text += '<td width="25%"></td>\n';
				      text += '<td><a href="displayUser.action?userEPC.userId='+u.userId+'"><strong>Edit</strong></a></td>\n';
				      text += '<td ><a href="deleteUser.action?userEPC.userId='+u.userId+'"><strong>Delete</strong></a></td>\n';
					    text += '</tr>\n';
				    }
			    }
			    text += "</table>";
			    
			    userList.innerHTML = text;
			    
		        }
		        
		        function acceptUser(u) {
			    var filter = document.forms["filter"];
			    
			    var accept = true;
			    accept = accept && filterOnValue(filter.userName.value, u.userName); 
			    accept = accept && filterOnValue(filter.elements["emailAddress"].value, u.emailAddress); 
			    
			    
			        return accept;
		        }
		        
		        function filterOnValue(search, value) {
			    var b = true;
			    if (search != "") {
				    b = value.toUpperCase().indexOf(search.toUpperCase()) != -1;
			    }
			    
			    return b;
		        }
		        
		        var users = new Array();
		        var nbUser = 0;
				<c:forEach var="model" items="${models}">	      
		        
				users[nbUser] = new User();
				users[nbUser].userName = "<c:out value="userName"/>";
				users[nbUser].emailAddress = "<c:out value="emailAddress"/>";
				users[nbUser].userId = "<c:out value="userId"/>";
				nbUser++;
		        </c:forEach>
		        </script>
	</head>

	<body class="bodyBackground">
		<!-- addUser.jsp -->
		<%@ include file="/WEB-INF/jsp/includes/header.jsp" %> 
		<%@ include file="/WEB-INF/jsp/includes/menue.jsp" %> 
		<table class="mainTable">
			<tr>
				<td width="20%" valign="top">
				<%@ include file="/WEB-INF/jsp/includes/userLoggedIn.jsp" %>
				</td>
				<td valign="top">
					<table cellpadding="0" class="tableSearch" width="100%">
						<tr>
							<td><strong>Search Users For:</strong></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>
						    <form commandName="model" action="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/search-users.do" method="post">
						    
						    
						        <table cellpadding="0" border="0" width="100%">
						        	<tr>
							      <td width="30%"><span id="errorOnYellow"><form:errors path="model.userName" /></span></td>
							      <td width="25%" ><span id="errorOnYellow"><form:errors path="model.emailAddress" /></span></td>
							      <td width="25%" ></td>
							      <td>&nbsp;</td>
							      <td>&nbsp;</td>
							  </tr>						        
						        	<tr>
							      <td width="30%">Username</td>
							      <td width="25%" >Email</td>
							      <td width="25%" ></td>
							      <td>&nbsp;</td>
							      <td>&nbsp;</td>
							  </tr>
							  <tr>
							      <td width="30%"><input type="text" name="userName" value="<c:out value="${model.userName}"/>" maxlength="32"/></td>
							      <td width="25%"><input type="text" name="emailAddress" value="<c:out value="${model.emailAddress}"/>" maxlength="32"/></td>
							        <td width="25%"><!--<input type="text" name="organisation" onkeyup="writeUsersList();"/>--></td>
							      <td>&nbsp;</td>
							      <td><input type="submit" value="search" id="submit" /></td>
							  </tr>
						        </table>
						     </form>
                                                
						     </td>
                                            </tr>
                                            <c:if test="${models != null}">
                                            	<c:if test="${!empty models}">
                                            
                                            <tr><td>
                                            <table>
										  <tr>
										      <th width="30%">Username</th>
										      <th width="25%">Email</th>
										        <td width="25%"><!--<input type="text" name="organisation" onkeyup="writeUsersList();"/>--></td>
										      <td></td>
										      <td>
										  
										      </td>
										  </tr>                                             
                                            <!--- iterate through users -->
                                             <c:forEach var="model" items="${models}">	
										  <tr>
										      <td width="30%"><c:out  value="${model.userName}"/></td>
										      <td width="25%"><c:out  value="${model.emailAddress}"/></td>
										        <td width="25%"><!--<input type="text" name="organisation" onkeyup="writeUsersList();"/>--></td>
										      <td><a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/add-user.do?userId=<c:out  value="${model.userId}"/>">Edit</a></td>
										      <td>
										     <c:choose> 
												  <c:when test="${model.active}" > 
												   <a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/change-user-status.do?userId=<c:out  value="${model.userId}"/>&active=0">disable</a> 
												  </c:when> 
												  <c:otherwise> 
												    <a href="<c:out value="${pageContext.servletContext.contextPath}" />/user-management/change-user-status.do?userId=<c:out  value="${model.userId}"/>&active=1">enable</a>
												  </c:otherwise> 
											</c:choose> 
										      </td>
										  </tr>      
										  </c:forEach>   
										  </table>
										  </td>
										  </tr>
										        </c:if>  
										        <c:if test="${empty models}">
                                           <tr><td> <table>
					    										  <tr>
					    										      <td>No results found</td>
					    										      
					    										  </tr> 
					</table></td>
										  </tr>
										        </c:if>
											</c:if>
											</td>
											
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