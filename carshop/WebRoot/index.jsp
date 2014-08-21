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
		<button id="test">test</button>
		<button id="d">d</button>
	</div>
</div>
</div>

<div class="modal fade" id="myconfirm" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-body"></div>
      <div class="modal-footer" style="padding: 5px;">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="confirm">Save changes</button>
      </div>
    </div>
  </div>
</div>
adsfasdfasdfadsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdfadsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf
adsfasdfasdf

<div class="myconfirm">
	<div><span style="color:#ee8c28;width:32px;height: 32;" class="glyphicon glyphicon-check"></span></div>
	<div id="confirm_content" class="text-center" style="height: 105px;padding: 40px;"><p class="lead"></p></div>
	<div>
		<button type="button" class="btn btn-default close-btn" id="close">close</button>
		<button type="button" class="btn confirm-btn" id="confirm">confirm</button>
	</div>
</div>
	<div class="layer-mask" id="layer-mask"></div>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/lib/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	$("#test").on("click",function(){
		var msg="确定提交吗？";
		myconfirm(msg,function(){
			console.log("hi");
		});
	});
	function myconfirm(msg,callback){
		var myconfirm=$(".myconfirm");
		myconfirm.find("#confirm_content p").empty();
		myconfirm.find("#confirm_content p").text(msg);
		myconfirm.find("#layer-mask").off("click");
		myconfirm.find("#layer-mask").on("click",function(){
			myconfirm.hide();
			$("#layer-mask").hide();
		});
		myconfirm.find("#confirm").off("click");
		myconfirm.find("#confirm").on("click",function(){
			callback();
			myconfirm.hide();
			$("#layer-mask").hide();
		});
		myconfirm.find("#close").off("click");
		myconfirm.find("#close").on("click",function(){
			myconfirm.hide();
			$("#layer-mask").hide();
		});
		myconfirm.show();
		$("#layer-mask").show();
	}
	
	$("#d").on("click",function(){
		var a="adaf".split("d");
		console.log(a);
	});
});

</script>
</html>