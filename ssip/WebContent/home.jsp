<%@ include file="/commonjsp/sevletBaseInfo.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd" >

<html>
  <head>
	<base href="<%=basePath%>">
    <title>蜗牛系统集成平台主页</title>
	 
	<link href="<%=basePath%>resource/css/common.css" rel="stylesheet" type="text/css">
	 
	<script type="text/javascript" src="<%=extBasePath %>build/bootstrap.js"></script>
	 
  	<script type="text/javascript" src="<%=custJsBasePath%>app.js"></script>
	<script type="text/javascript" src="<%=jsBasePath%>plugin/jsUtil.js"></script>
	
  </head>
  <body>
  	<input type="hidden" value="${ currentUser.userName }" id="currentUserName" />
  	<input type="hidden" value="${ currentUser.userId }" id="currentUserId"/>
  	<input type="hidden" value="<%=basePath %>" id="basePath"/>
	
  </body>
</html>
