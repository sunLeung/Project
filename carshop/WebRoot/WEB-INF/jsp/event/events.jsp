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
<input id="openid" value="${openid}" type="hidden"/>
<div class="container-fluid" style="margin-bottom: 55px;">
<div class="row">
	<div class="col-xs-12">
		<h3 style="color:#ee8c28;">活动<small>中心</small></h3>
		<c:if test="${!empty events}">
			<c:forEach var="item" items="${events}" varStatus="status"> 
				<div class="eventContaint" isJoin="${item.isjoin}" eventid="${item.id }" >
					<c:if test="${!empty item.title_img}">
						<img class="eventImg img-responsive" src="/img/${item.title_img}"/>
					</c:if>
					<p style="font-weight: bolder;margin: 10px 0 0 5px;">${item.name }</p>
					<p style="color:#999;margin: 0 0 0 5px;">活动时间:<fmt:formatDate value="${item.signup_from }" pattern="yyyy-MM-dd" /> 至 <fmt:formatDate value="${item.signup_to }" pattern="yyyy-MM-dd" /><span class="glyphicon glyphicon-chevron-right pull-right " style="color: #ee8c28;position: relative;top: -6px;right: 15px;"></span></p>
				</div>
			</c:forEach>
		</c:if>
		<c:if test="${empty events}">
			<span>没有活动信息</span>
		</c:if>
	</div>
</div>
</div>


<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header" style="padding: 6px;overflow-x: hidden;">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel" style="color:#999;">活动详情</h4>
      </div>
      <div class="modal-body" style="overflow-x: hidden;"></div>
      <div class="modal-footer" style="padding: 6px;">
        <button id="join_event" type="button" class="btn" style="background-color: #ee8c28;color: #fff;">参与</button>
      </div>
    </div>
  </div>
</div>


<!-- 二次确认框 -->
<div class="myconfirm">
	<div id="confirm_content" class="text-center" style="height: 105px;padding: 50px;"><p class="lead"></p></div>
	<div>
		<button type="button" class="btn btn-default close-btn" id="close">close</button>
		<button type="button" class="btn confirm-btn" id="confirm">confirm</button>
	</div>
</div>
</body>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<script src="/lib/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	//显示活动详情
	$(".eventContaint").on("click",function(){
		$("#myModal").modal("show");
		var isJoin=$(this).attr("isJoin");
		var eventid=$(this).attr("eventid");
		$("#join_event").attr("eventid",eventid);
		if(isJoin=="true"){
			$("#join_event").attr("disabled","true");
			$("#join_event").text("已参与");
		}else{
			$("#join_event").removeAttr("disabled");
			$("#join_event").text("参与");
		}
		$.ajax({
            type: "GET",
            url: "/event/getEventDetail.do",
            data: {eventid:eventid},
            dataType: "json",
            success: function(data){
           		var dataObj=eval(data);
           		$(".modal-body").empty();
           		$(".modal-body").append(dataObj.DESCRIPTION);
           		$(".modal-body").find("img").addClass("eventImg");
            }
        });
	});
	
	//参与活动
	$("#join_event").on("click",function(){
		var eventid=$(this).attr("eventid");
		var openid=$("#openid").val();
		$.ajax({
            type: "GET",
            url: "/event/join.do",
            data: {openid:openid,eventid:eventid},
            dataType: "json",
            success: function(data){
           		var dataObj=eval(data);
           		console.log(dataObj.code);
           		if(dataObj.code==1){
           			$("#join_event").attr("disabled","true");
        			$("#join_event").text("已参与");
           		}
            }
        });
	});
	
	/**二次确认框*/
	function myconfirm(msg,callback){
		var myconfirm=$(".myconfirm");
		myconfirm.find("#confirm_content p").empty();
		myconfirm.find("#confirm_content p").text(msg);
		myconfirm.find("#confirm").off("click");
		myconfirm.find("#confirm").on("click",function(){
			callback();
			myconfirm.hide();
		});
		myconfirm.find("#close").off("click");
		myconfirm.find("#close").on("click",function(){
			myconfirm.hide();
		});
		myconfirm.show();
	}
});

</script>
</html>