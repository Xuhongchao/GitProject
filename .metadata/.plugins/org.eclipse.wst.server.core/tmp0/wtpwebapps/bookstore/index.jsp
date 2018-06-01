<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>测试</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/jQuery/jquery.min.js"></script>
<script type="text/javascript">
	/*按钮事件*/
	$(function() {
		$('button').click(function() {
			$.ajax({
				type : 'post',
				url : '/SSM/account/list',
				data : {
					payload : $("#payload").val(),
					content : $("#content").val()
				},
				dataType : "json",
				success : function(data, status, xhr) {
					console.log(data);//返回值
					console.log(status);//状态
					console.log(xhr);//obj
					console.log(xhr.getResponseHeader("Content-Type"));//application/octet-stream
					console.log(xhr.getResponseHeader("Center-Length"));//null
				}
			});
		});
	});
</script>


</head>

<body>
	<P>地推通后台管理系统</P>
	<HR>

	<button>Change Content</button>

</body>
</html>
