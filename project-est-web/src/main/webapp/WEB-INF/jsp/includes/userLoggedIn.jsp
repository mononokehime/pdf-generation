<div class="menueBox">
		<div>
			<strong>Welcome <security:authentication property="principal.firstName"/>&nbsp;<security:authentication property="principal.familyName"/></strong><br/><br/>
		</div>
		<div>
			Username: <security:authentication property="principal.userName"/><br/><br/>
			<!-- Access Level: <br/><br/><br/><br/> -->
			Roles:
			<security:authentication property="principal.userRoleString" />
		</div>
		
		<div>
			<strong><a href="${pageContext.request.contextPath}/user-management/change-password.do">Change password</a></strong>
			<br/><br/>
		</div> 
		<div>
			<strong><a href="${pageContext.request.contextPath}/j_spring_security_logout">Logout</a></strong>
			<br/><br/>
		</div>
		
</div>