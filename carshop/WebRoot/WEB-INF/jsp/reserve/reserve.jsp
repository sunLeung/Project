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
<div class="container-fluid">
<div class="row">
	<div class="col-xs-12">
		<!-- 创建预约 -->
		<div>
			<h3 style="color:#ee8c28;">新建<small>预约</small></h3>
			
			<!-- 选择车 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">选择车型</span></span>
	  				<select class="form-control" id="select_car" name="carid">
	  					<optgroup label="我的车型">
							<c:if test="${!empty mycar}" >
							<c:forEach var="item" items="${mycar}" varStatus="status"> 
								<option value="${item.id}">${item.model }&nbsp;${item.register_no }</option>
							</c:forEach>
							</c:if>
							<option value="other">手动输入</option>
						</optgroup>
	  				</select>
				</div>
				<div id="other_car" style="display: none;">
					<div class="input-group" >
						<span class="input-group-addon"><span class="inputLabel">车牌</span></span>
						<input class="form-control" id="other_car_num" type="text" placeholder="填写车牌号"/>
					</div>
					<div class="input-group" >
						<span class="input-group-addon"><span class="inputLabel">车架号</span></span>
						<input class="form-control" id="other_car_vin" type="text" placeholder="填写车架号"/>
					</div>
					<div class="alert alert-warning" style="padding: 8px;" role="alert">*请填写车牌或车架号</div>
				</div>
			</div>
			<!-- 选择车 end-->
			
			<!-- 选择店 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">选择4s店</span></span>
	  				<select class="form-control" id="select_shop" name="shopid">
	  					<c:if test="${empty shop}" >
							<option value="-1">没有店铺</option>
						</c:if>
						<c:forEach var="item" items="${shop}" varStatus="status"> 
							<option value="${item.id}">${item.name }</option>
						</c:forEach>
	  				</select>
				</div>
			</div>
			<!-- 选择店 end-->
			
			<!-- 选择时间 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">预约时间</span></span>
	  				<select class="form-control" id="select_time" disabled="disabled" name="timeid">
	  					<option value="-1">请先选择4s店</option>
	  				</select>
				</div>
			</div>
			<!-- 选择时间 end-->
			
			<!-- 选择班组 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">选择班组</span></span>
	  				<select class="form-control" id="select_team" disabled="disabled" name="teamid">
	  					<option value="-1">请先选择预约时间</option>
	  				</select>
				</div>
			</div>
			<!-- 选择班组 end-->
			
			<!-- 选择顾问 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">选择顾问</span></span>
	  				<select class="form-control" id="select_consultant" disabled="disabled" name="consultantid">
	  					<option value="-1">请先选择预约时间</option>
	  				</select>
				</div>
			</div>
			<!-- 选择顾问 end-->
			<div class="text-right">
				<input id="create" type="submit" value="提交预约" class="btn btn-default"/>
			</div>
		</div>
		<div id="success-result" style="padding: 8px;display: none;" class="alert alert-success" role="alert"></div>
		<div id="error-result" style="padding: 8px;display: none;" class="alert alert-danger" role="alert">...</div>
	</div>
</div>
</div>


<div class="container-fluid" style="position: fixed;bottom: 0px;left:1.5px;right:0px;">
	<div class="row">
		<div class="btn-group" style="width: 100%;">
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-plus" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/create.do?openid=${openid}">新建预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-search" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/query.do?openid=${openid}">查询预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-pencil" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/rate.do?openid=${openid}">服务评价</a></div>
	  </div>
	</div>
</div>
</body>
</body>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/lib/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	//手动输入车型是否显示
	var carSelectVal=$("#select_car").val();
	if(carSelectVal=="other"){
		$("#other_car_model").show();
	}else{
		$("#other_car_model").hide();
	}
	
	//加载预约时间
	var selectShop=$("#select_shop").val();
	if(selectShop!=-1){
		loadSelectTime(selectShop);
	}
	
	
	$("#select_car").on("change",function(){
		var val=$(this).val();
		if(val=="other"){
			$("#other_car").show();
		}else{
			$("#other_car").hide();
		}
	});
	
	$("#select_shop").on("change",function(){
		var val=$(this).val();
		loadSelectTime(val);
	});
	
	$("#select_time").on("change",function(){
		var val=$(this).val();
		var shopid=$("#select_shop").val();
		loadTeam(shopid,val);
		loadConsultant(shopid,val);
	});
	
	/**加载预约时间*/
	function loadSelectTime(shopid){
		if(shopid!=""&&shopid!="other"){
			$.ajax({
	             type: "GET",
	             url: "/reserve/getSelectTime.do",
	             data: {shopid:shopid},
	             dataType: "json",
	             success: function(data){
	            	var dataObj=eval(data);
	            	if(dataObj.length>0){
		            	var selectTime=$('#select_time');
		            	selectTime.empty();
		            	$.each(dataObj, function(index, value) {
		            		selectTime.append("<option value='"+value.id+"'>"+value.time+"</option>");
		            	});
	            		selectTime.attr("disabled",false);
	            	}
	            	
	            	var timeid=$("#select_time").val();
	            	var shopid=$("#select_shop").val();
	            	//加载班组和顾问
	            	loadTeam(shopid,timeid);
	            	loadConsultant(shopid,timeid);
	             }
	         	});
			}
		}
	
	/**加载班组*/
	function loadTeam(shopid,timeid){
		if(timeid!=""&&timeid!="other"&&shopid!=""&&shopid!="other"){
			$.ajax({
	             type: "GET",
	             url: "/reserve/getTeam.do",
	             data: {shopid:shopid,timeid:timeid},
	             dataType: "json",
	             success: function(data){
	            	var dataObj=eval(data);
	            	if(dataObj.length>0){
		            	var selectTeam=$('#select_team');
		            	selectTeam.empty();
		            	$.each(dataObj, function(index, value) {
		            		selectTeam.append("<option value='"+value.tid+"' appointmentid='"+value.id+"'>"+value.name+"</option>");
		            	});
		            	selectTeam.attr("disabled",false);
	            	}
	             }
	         	});
			}
		}
	
	
	/**加载顾问*/
	function loadConsultant(shopid,timeid){
		if(timeid!=""&&timeid!="other"&&shopid!=""&&shopid!="other"){
			$.ajax({
	             type: "GET",
	             url: "/reserve/getConsultant.do",
	             data: {shopid:shopid,timeid:timeid},
	             dataType: "json",
	             success: function(data){
	            	var dataObj=eval(data);
	            	if(dataObj.length>0){
		            	var selectConsultant=$('#select_consultant');
		            	selectConsultant.empty();
		            	$.each(dataObj, function(index, value) {
		            		selectConsultant.append("<option value='"+value.id+"'>"+value.name+"</option>");
		            	});
		            	selectConsultant.attr("disabled",false);
	            	}
	             }
	         	});
			}
		}
	
	$("#create").on("click",function(){
		var openid=$("#openid").val();
		var carid=$("#select_car").val();
		var isOther=false;
		var otherCarNum=$("#other_car_num").val();
		var otherCarVin=$("#other_car_vin").val();
		if(carid=="other"){
			if(otherCarNum==""&&otherCarVin==""){
				showResult(0,"车牌号和车架号至少填写一个。");
				return;
			}
		}
		var shopid=$("#select_shop").val();
		if(shopid==-1){
			showResult(0,"请选择商店。");
			return;
		}
		var timeid=$("#select_time").val();
		if(timeid==-1){
			showResult(0,"请选择预约时间。");
			return;
		}
		var teamid=$("#select_team").val();
		var appointmentid=$("#select_team").find("option:selected").attr('appointmentid');
		if(teamid==-1){
			showResult(0,"请选择班组。");
			return;
		}
		var consultantid=$("#select_consultant").val();
		if(consultantid==-1){
			showResult(0,"请选择顾问。");
			return;
		}
		
		$.ajax({
            type: "POST",
            url: "/reserve/save.do",
            data: {openid:openid,appointmentid:appointmentid,carid:carid,isOther:isOther,otherCarNum:otherCarNum,otherCarVin:otherCarVin,shopid:shopid,timeid:timeid,teamid:teamid,consultantid:consultantid},
            dataType: "json",
            success: function(data){
           		var dataObj=eval(data);
           		console.log(dataObj.code);
           		console.log(dataObj.msg);
           		showResult(dataObj.code,dataObj.msg);
            }
        	});
		
	});
	
	function showResult(code,msg){
		var sr=$("#success-result");
		var er=$("#error-result");
		sr.empty();
		er.empty();
		sr.hide();
		er.hide();
		if(code!=1){
			er.text(msg);
			er.show();
		}else{
			sr.text(msg);
			sr.show();
		}
	}
});

</script>
</html>