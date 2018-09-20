<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Speace X窜天猴,带你实现太空梦</title>
<script type="text/javascript">
	function formReset() {
		document.getElementById("myForm").reset()
	}
</script>
</head>
<body>

	<form id="myForm" action="service" method="post">
		ip地址：<input type="text" name="ip" /> <font color="red">*</font><br />
		端口：<input type="number" name="port" /> <font color="red">*</font><br />
		用户名：<input type="text" name="username" /> <font color="red">*</font><br />
		密码：<input type="password" name="password" /> <font color="red">*</font><br />
		具体的库名：<input type="text" name="db" /> <font size="2" color="gray">（默认不填会扒取整个实例下的全部信息，指定具体的数据库则只会扒取该数据库下的全部信息）</font>
		<br /> <br /> <br /> <input type="submit" value="提交" /> <input
			type="button" onclick="formReset()" value="重置">
	</form>

</body>
</html>