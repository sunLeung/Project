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
<link rel="stylesheet" href="/lib/star-rating/star-rating.min.css">
<link rel="stylesheet" href="/css/main.css">
</head>
<body>
<!-- 全局保存openid -->
<input id="openid" value=${openid} type="hidden"/>
<!-- 预约评价 -->
<c:if test="${!empty overtimeAppointment}">
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
	<c:forEach var="item" items="${overtimeAppointment}" varStatus="status"> 
		<tr>
			<td>${item.id}</td>
			<td>${item.appointment_day}</td>
			<td>${item.register_no}</td>
			<td>${item.vin}</td>
			<td><button name="rate" aid="${item.id}" class="btn btn-default" data-target="#myModal">评分</button></td>
		</tr>
	</c:forEach>
</table>
</c:if>
<c:if test="${empty overtimeAppointment}">
	<span>暂没有预约可评价</span>
</c:if>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">评价</h4>
      </div>
      <div class="modal-body">
      	<input type="hidden" id="aid" value=""/>
        <label>班组</label>
         <input id="rate_team" value="0" type="number" class="rating"  min=0 max=10 step=1 data-size="xs" >
        <label>顾问</label>
         <input id="rate_consultant" value="0" type="number" class="rating"  min=0 max=10 step=1 data-size="xs" >
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button id="rate_submit" type="button" class="btn btn-primary">提交</button>
      </div>
    </div>
  </div>
</div>


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
<script src="/lib/star-rating/star-rating.js"></script>
<script>
$(document).ready(function(){
	$("button[name='rate']").on("click",function(){
		var aid=$(this).attr("aid");
		$('#myModal').modal('show');
		$("#aid").val(aid);
	});
	
	$('#myModal').on('hidden.bs.modal', function (e) {
		$("#rate_team").rating('clear');
		$("#rate_consultant").rating('clear');
	});
	
	$("#rate_submit").on("click",function(){
		var openid=$("#openid").val();
		var aid=$("#aid").val();
		var tscore=$("#rate_team").val();
		var cscore=$("#rate_consultant").val();
		if(aid!=""){
			$.ajax({
	            type: "GET",
	            url: "/reserve/rating.do",
	            data: {openid:openid,aid:aid,tscore:tscore,cscore:cscore},
	            dataType: "json",
	            success: function(data){
	           		var dataObj=eval(data);
	           		console.log(dataObj.code);
	           		if(dataObj.code==1){
	           			location.reload();
	           			console.log("rating");
	           		}
	            }
	        	});
		}
	});
});

</script>
</html>