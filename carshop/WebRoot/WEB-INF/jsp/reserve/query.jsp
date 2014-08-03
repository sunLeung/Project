<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
</head>
<body>
<!-- 全局保存openid -->
<input id="openid" value=${openid} type="hidden"/>
<!-- 查询预约 -->
<table>
	<tr>
		<td>预约ID</td>
		<td>预约时间</td>
		<td>车牌</td>
		<td>车架号</td>
		<td>操作</td>
	</tr>
	<c:forEach var="item" items="${myappointment}" varStatus="status"> 
		<tr>
			<td>${item.id}</td>
			<td>${item.appointment_day}</td>
			<td>${item.register_no}</td>
			<td>${item.vin}</td>
			<td><button name="delete" aid="${item.id}">操作</button></td>
		</tr>
	</c:forEach>
</table>

</body>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>

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
           			$(this).parent().parent().remove();
           			console.log("remove");
           		}
            }
        	});
	});
});

</script>
</html>