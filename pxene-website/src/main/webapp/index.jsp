<%@ page import="java.util.Date" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<% String basePath = request.getContextPath(); %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>世界树-蓬景知识库</title>
    <link rel="icon" href="http://www.pxene.com/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="http://www.pxene.com/favicon.ico" type="image/x-icon" />
    <link rel="bookmark" href="http://www.pxene.com/favicon.ico" type="image/x-icon" />

    <link rel="stylesheet" type="text/css" href="<%= basePath %>/app/styles/main-build.css">

    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/bower_components/bootstrap/dist/css/bootstrap.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/bower_components/angular-ui-tree/dist/angular-ui-tree.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/bower_components/angular-ui-grid/ui-grid.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/bower_components/daterangepicker/daterangepicker-bs3.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="<%=basePath%>/bower_components/xcConfirm/css/xcConfirm.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/app/styles/public.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/app/styles/iconfont.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%= basePath %>/app/styles/model.css">--%>

    <script src="<%=basePath%>/bower_components/echarts/echarts.min.js"></script>
    <script type="text/javascript">
      basePath = '<%=basePath%>';
    </script>
  </head>
  <body>

  <nav class="nav navbar-default"></nav>

  <div ng-controller="indexCtrl" class="container-fulid clearfix">

      <nav class="navbar-default"><div class="nav-logo"><i class="icon-logo-zhishiku"></i></div></nav>

      <div class="menu" module-menu="listMenu"></div>

      <div class="content pt50" ng-view ></div>

  </div>
  <footer></footer>
  </body>
  <script type="text/javascript" src="<%= basePath %>/bower_components/requirejs/require.js?date=<%= new Date().getTime() %>"  data-main="<%= basePath %>/app/scripts/main-build.js?date=<%= new Date().getTime() %>"></script>
  <%--<script type="text/javascript" src="<%= basePath %>/bower_components/requirejs/require.js?date=<%= new Date().getTime() %>"  data-main="<%= basePath %>/app/scripts/main.js?date=<%= new Date().getTime() %>"></script>--%>
</html>
