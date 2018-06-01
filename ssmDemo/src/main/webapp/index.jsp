<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>测试</title>
</head>

<body>
	<P>地推通后台管理系统</P>
	<HR>

	<p>测试一</p>
	<form action="${pageContext.request.contextPath }/account/find"
		method="post">
		请输入要查询的账户id：<input path="id"> <input type="submit" value="提交">
	</form>

	<p>测试二</p>
	<a href="${pageContext.request.contextPath }/account/get/3">查询账户</a>

	<p>测试三</p>
	<form action="${pageContext.request.contextPath }/account/list"
		method="post">
		<input type="submit" value="提交">
	</form>

</body>
</html>
