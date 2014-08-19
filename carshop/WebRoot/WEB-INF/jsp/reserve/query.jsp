<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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
<div class="container-fluid" style="margin-bottom: 55px;">
<div class="row">
	<div class="col-xs-12">
		<h3 style="color:#ee8c28;">查询<small>预约</small></h3>
		
		<!-- 查询预约 -->
		<c:if test="${!empty myappointment}">
		<c:forEach var="item" items="${myappointment}" varStatus="status"> 
		<div class="callout callout-info">
			<table>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">时间：</td>
					<td><fmt:formatDate value="${item.appointment_start}" pattern="yyyy-MM-dd HH:mm" />至<fmt:formatDate value="${item.appointment_end}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">车型：</td>
					<td>${item.model}</td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">车牌：</td>
					<td>${item.register_no}</td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">车架：</td>
					<td>${item.vin}</td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">4S店：</td>
					<td>${item.print_title}</td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">地址：</td>
					<td>${item.address}</td>
				</tr>
				<tr>
					<td style="font-weight:bolder;width:45px;vertical-align: top;text-align: right;">电话：</td>
					<td>${item.telephone}</td>
				</tr>
			</table>
			<p class="text-right"><button class="btn btn-default" name="delete" aid="${item.id}">取消预约</button></p>
		</div>
		</c:forEach>
		</c:if>
		<c:if test="${empty myappointment}">
			<span>没有预约信息</span>
		</c:if>
	</div>
</div>
</div>

<div class="container-fluid" style="position: fixed;bottom: 0px;left:1.5px;right:0px;z-index:1000;">
	<div class="row">
		<div class="btn-group" style="width: 100%;">
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-plus" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/create.do?openid=${openid}">新建预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-search" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/query.do?openid=${openid}">查询预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-pencil" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/rate.do?openid=${openid}">服务评价</a></div>
	  </div>
	</div>
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