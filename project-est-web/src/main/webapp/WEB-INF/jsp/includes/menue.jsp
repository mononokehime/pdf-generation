
<!-- FIX for IE 6 -->
<script type="text/javascript"><!--//--><![CDATA[//><!--
    
    sfHover = function() {
      var sfEls = document.getElementById("nav").getElementsByTagName("li");
      for (var i=0; i<sfEls.length; i++) {
        sfEls[i].onmouseover=function() {
          this.className+=" sfhover";
        }
        sfEls[i].onmouseout=function() {
          this.className=this.className.replace(new RegExp(" sfhover\\b"), "");
        }
      }
    }
    
    if (window.attachEvent) window.attachEvent("onload", sfHover);

    function openWin(url,w,h) {
       newWin=window.open(url,"newWin","width="+w+",height="+h+",status=no,toolbar=no,menubar=no,scrollbars=yes,left=100,top=100,screenX=100,screenY=100,resizable = 1");
    }
    
//--><!]]></script>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableMenue">
	<tr id="row_height">
		<td class="banner">
			<ul id="nav">
				<li>
					<a href="${pageContext.request.contextPath}/home.do">Home</a>
				</li>
				<security:authorize ifAllGranted="ROLE_SUPER_USER">
				<li>
					<a href="#">User Management</a>
					<ul>
						<li class="submenu">
							<a href="${pageContext.request.contextPath}/user-management/index.do">User Management Home</a>							
						</li>					
						<li class="submenu">
							<a href="${pageContext.request.contextPath}/user-management/add-user.do">Add New User</a>							
						</li>
						<li>
							<a href="${pageContext.request.contextPath}/user-management/search-users.do">Search Users</a>
						</li>
					</ul>
				</li>
				</security:authorize>

				<security:authorize ifAnyGranted="ROLE_USER, ROLE_SUPER_USER">
				<li>
					<a href="#">Schedule</a>
					<ul>
						
						<li class="submenu">
							<a href="${pageContext.servletContext.contextPath}/search/index.do">Search and Schedule</a>							
						</li>											
						<li>
							<a href="${pageContext.servletContext.contextPath}/search/history-index.do">Search History</a>
						</li>	
						<li>
							<a href="${pageContext.servletContext.contextPath}/search/schedule-history.do">Schedule History Summaries</a>
						</li>	
					</ul>
				</li>
				<li>
					<a href="#">Uploads</a>
					<ul>

						<li class="submenu">
							<a href="${pageContext.servletContext.contextPath}/file-admin/index.do">Upload Summaries</a>							
						</li>						
						<li class="submenu">
							<a href="${pageContext.servletContext.contextPath}/file-admin/land-registry-upload.do">Upload Land Registry Data</a>
							
						</li>						
						<li  class="submenu">
							<a href="${pageContext.servletContext.contextPath}/file-admin/landmark-upload.do">Upload Landmark Data</a>
						</li>
					</ul>
				</li>
				</security:authorize>				
				
				
		</td>
		<td width="5%" class="banner">
		<!-- 
			<strong>
				<a
					href='mailto:ithelpdesk@est.org.uk?subject=EPC Support&body=I logged in as email. The page I am asking for support on is . The time of my problem was <%=new java.util.Date()%>. %0DI am having problems when I :'
					class="prinav">Support
				</a>
				</strong> -->
		</td>
	</tr>
</table>