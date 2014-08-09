<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="/lib/bootstrap-3.2.0-dist/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/main.css">
</head>
<body>
<!-- 全局保存openid -->
<input id="openid" value=${openid} type="hidden"/>
<!-- 查询预约 -->
<c:if test="${!empty myappointment}">
<table class="table">
	<thead>
	<tr>
		<th>预约ID</th>
		<th>预约时间</th>
		<th>车牌</th>
		<th>车架号</th>
		<th>操作</th>
	</tr>
	</thead>
	<c:forEach var="item" items="${myappointment}" varStatus="status"> 
		<tr>
			<td>${item.id}</td>
			<td>${item.appointment_day}</td>
			<td>${item.register_no}</td>
			<td>${item.vin}</td>
			<td><button class="btn btn-default" name="delete" aid="${item.id}">取消</button></td>
		</tr>
	</c:forEach>
</table>
</c:if>
<c:if test="${empty myappointment}">
	<span>没有预约信息</span>
</c:if>

<div class="btn-group" style="width: 100%;position: fixed;bottom: 0px;left:1px;right:0px;">
  <a href="/reserve/create.do?openid=${openid}" class="btn btn-default">新建预约</a>
  <a href="/reserve/query.do?openid=${openid}" class="btn btn-default">查询预约</a>
  <a href="/reserve/rate.do?openid=${openid}" class="btn btn-default">服务评价</a>
</div>
</body>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/lib/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	$("button[name='delete']").on("click",function(){
		var aid=$(this).attr("aid");
		$.ajax({
            type: "GET",
            url: "/reserve/delete.do",
            data: {aid:aid},
            dataType: "json",
            success: function(data){
           		var dataObj=eval(data);
           		console.log(dataObj.code);
           		if(dataObj.code==1){
           			location.reload();
           			console.log("remove");
           		}
            }
        	});
	});
});

</script>
</html>