<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
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
<input id="openid" value="${openid}" type="hidden"/>
<div class="container-fluid" style="margin-bottom: 55px;">
<div class="row">
	<div class="col-xs-12" style="padding-left: 5px;padding-right: 5px;">
		<!-- 创建预约 -->
		<div>
			<h3 style="color:#ee8c28;">新建<small>预约</small></h3>
			<div class="carousel slide">
			<!-- Wrapper for slides -->
			<div class="carousel-inner" role="listbox">
			
			<!-- 第一页 -->
			<!-- 选择车 begin-->
			<div class="item active" style="border: 1px solid #ee8c28;padding: 5px;border-radius:4px;height: 385px;">
				<div class="input-group" style="margin-bottom: 10px;">
	  				<span class="input-group-addon"><span class="inputLabel">我的车型</span></span>
	  				<select class="form-control" id="select_mycar" name="carid">
	  					<optgroup label="我的车型">
							<c:if test="${!empty mycar}" >
							<c:forEach var="item" items="${mycar}" varStatus="status"> 
								<option value="${item.id}" modelCode="${item.model_code}">${item.model }</option>
							</c:forEach>
							</c:if>
							<option value="-1">其他</option>
						</optgroup>
	  				</select>
				</div>
				<div class="input-group" style="margin-bottom: 10px;">
	  				<span class="input-group-addon"><span class="inputLabel">厂牌</span></span>
	  				<select class="form-control" id="select_brand" name="carid">
	  					<%
	  						List<Map<String,Object>> carBrands=(List<Map<String,Object>>)request.getAttribute("carBrands");
	  						if(carBrands!=null&&carBrands.size()>0){
	  							String l="";
	  							for(int i=0;i<carBrands.size();i++){
	  								Map<String,Object> map=carBrands.get(i);
	  								String type=(String)map.get("type");
	  								String t=type.substring(0, 1);
	  								if(!t.equals(l)){
	  									l=t;
	  									if(i==0){
			  								%>
			  									<optgroup label="<%=l %>">
			  								<%
	  									}else{
			  								%>
			  									</optgroup>
			  									<optgroup label="<%=l %>">
			  								<%
	  									}
	  								}
	  								%>
	  								<option value="<%=map.get("id") %>" ><%=map.get("name") %></option>
	  								<%
	  							}
	  							%>
									</optgroup>
								<%
	  						}else{
	  							%>
	  								<option value="-1">暂无数据</option>
	  							<%
	  						} %>
	  				</select>
				</div>
				<div class="input-group" style="margin-bottom: 10px;">
	  				<span class="input-group-addon"><span class="inputLabel">车系</span></span>
	  				<select class="form-control" id="select_series" name="carid">
	  					<option value="-1">暂无数据</option>
	  				</select>
				</div>
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">型号</span></span>
	  				<select class="form-control" id="select_model" name="carid">
	  					<option value="-1">暂无数据</option>
	  				</select>
				</div>
				<div class="input-group"  style="margin-bottom: 10px;">
					<span class="input-group-addon"><span class="inputLabel">其他车型</span></span>
					<input class="form-control" id="other_car_model" type="text" placeholder="手动填写车型"/>
				</div>
				<div id="other_car">
					<div class="input-group" >
						<span class="input-group-addon"><span class="inputLabel">车牌</span></span>
						<input class="form-control" id="car_num" type="text" placeholder="填写车牌号"/>
					</div>
					<div class="input-group" >
						<span class="input-group-addon"><span class="inputLabel">车架号</span></span>
						<input class="form-control" id="car_vin" type="text" placeholder="填写车架号"/>
					</div>
					<div class="alert alert-warning" style="padding: 8px;" role="alert">*请填写车牌或车架号</div>
				</div>
				<div class="text-right">
					<button id="next" class="btn btn-default" style="color:#ee8c28;">下一步<span class="glyphicon glyphicon-chevron-right"></span></button>
				</div>
			</div>
			<!-- 选择车 end-->
			<!-- 第一页 end -->
			
			<!-- 第二页 begin -->
			<div class="item" style="border: 1px solid #ee8c28;padding: 5px;border-radius:4px;height: 385px;">
			<!-- 选择店 begin-->
			<div style="margin-bottom: 10px;">
				<div class="input-group">
	  				<span class="input-group-addon"><span class="inputLabel">选择4s店</span></span>
	  				<select class="form-control" id="select_shop" name="shopid">
	  					<c:if test="${empty shop}" >
							<option value="-1">没有店铺</option>
						</c:if>
						<c:forEach var="item" items="${shop}" varStatus="status"> 
							<option value="${item.own_no }">${item.print_title }</option>
						</c:forEach>
	  				</select>
				</div>
				<div id="shop_details" class="alert alert-warning" style="padding: 8px;display:none;" role="alert">
					<table>
						<tr>
							<td style="font-weight: bolder;width:50px;vertical-align: top;">地址：</td>
							<td id="shop_address"></td>
						</tr>
						<tr>
							<td style="font-weight: bolder;width:50px;vertical-align: top;">电话：</td>
							<td id="shop_telephone"></td>
						</tr>
					</table>
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
			<button id="prev" class="btn btn-default pull-left" style="position: absolute;bottom: 5px;color:#ee8c28;"><span class="glyphicon glyphicon-chevron-left"></span>上一步</button>
			<!-- 选择顾问 end-->
			<input id="create" class="btn btn-default pull-right" style="position: absolute;bottom: 5px;right:5px;color:#ee8c28;" type="submit" value="提交预约" data-loading-text="预约中..." class="btn btn-default"/>
			</div>
			<!-- 第二页 end -->
			</div>
		</div>
		</div>
		<div id="success-result" style="padding: 8px;display: none;" class="alert alert-success" role="alert"></div>
		<div id="error-result" style="padding: 8px;display: none;" class="alert alert-danger" role="alert">...</div>
	</div>
</div>
</div>


<div class="container-fluid" style="position: fixed;bottom: 0px;left:1.5px;right:0px;z-index:1070;">
	<div class="row">
		<div class="btn-group" style="width: 100%;">
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-plus" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/create.do?openid=${openid}">新建预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-search" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/query.do?openid=${openid}">查询预约</a></div>
		  <div class="col-xs-4 text-center btn btn-default"><span class="glyphicon glyphicon-pencil" style="color:#ee8c28;margin-right:3px;"></span><a class="navi_text" href="/reserve/rate.do?openid=${openid}">服务评价</a></div>
	  </div>
	</div>
</div>

<!-- 二次确认框 -->
<div class="myconfirm">
	<div id="confirm_content" class="text-center" style="padding:20px;"><p></p></div>
	<div>
		<button type="button" class="btn btn-default close-btn" id="close">取消</button>
		<button type="button" class="btn confirm-btn" id="confirm">确定</button>
	</div>
</div>
<div class="layer-mask" id="layer-mask"></div>
</body>

<!-- scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/lib/bootstrap-3.2.0-dist/js/bootstrap.min.js"></script>
<script>
$(document).ready(function(){
	//驱动翻页逻辑
	$('.carousel').carousel();
	$('.carousel').carousel('pause');
	$("#prev").on("click",function(){
		$('.carousel').carousel('prev');
	});
	$("#next").on("click",function(){
		//判断是否已经选择好车辆数据
		var select_mycar=$("#select_mycar").val();
		var select_model=$("#select_model").val();
		var other_car_model=$("#other_car_model").val();
		if(select_mycar==-1&&select_model==-1&&other_car_model==""){
			$("#selectCarResult").empty();
			var msg="*请选择\"我的车型\"或者\"其他车型\",如果没找到想要车型,请手动填写车型。";
			myconfirm(msg,function(){});
			return;
		}
		var car_num=$("#car_num").val();
		var car_vin=$("#car_vin").val();
		if(car_num==""&&car_vin==""){
			$("#selectCarResult").empty();
			$("#selectCarResult").text("*车牌和车架号必须填写其中之一");
			return;
		}
		
		$('.carousel').carousel('next');
	});
	
	//加载客户车辆信息
	loadMycar();
	
	$("#select_mycar").on("change",function(){
		loadMycar();
	});
	
	//加载车系数据
	$("#select_brand").on("change",function(){
		loadCarSeries();
	});
	//加载车型数据
	$("#select_series").on("change",function(){
		loadCarModel();
	});
	
	loadCarSeries();
	loadCarModel();
	
	function loadCarSeries(){
		var carBrandid=$("#select_brand").val();
		if(carBrandid!=-1){
			$.ajax({
	            type: "GET",
	            url: "/reserve/getCarSeries.do",
	            data: {carBrandid:carBrandid},
	            dataType: "json",
	            success: function(data){
	           		var dataObj=eval(data);
	           		var l="";
	           		var element;
	           		$("#select_series").empty();
	           		if(dataObj.length>0){
		           		for(i=0;i<dataObj.length;i++){
							var map=dataObj[i];
							var type=map.type;
							var t=type.substring(0, 1);
							if(t!=l){
								l=t;
								if(i==0){
	  								element+="<optgroup label='"+l+"'>";
								}else{
									element+="</optgroup><optgroup label='"+l+"'>";
								}
							}
							element+="<option value='"+map.ID+"' >"+map.NAME+"</option>";
		           		}
		           		$("#select_series").append(element);
	           		}else{
	           			$("#select_series").append("<option value='-1'>暂无数据</option>");
	           		}
	           		loadCarModel();
	           		console.log(dataObj);
	            }
	        });
		}
	}
	
	function loadCarModel(){
		var carSeriesid=$("#select_series").val();
		if(carSeriesid!=-1){
			$.ajax({
	            type: "GET",
	            url: "/reserve/getCarModel.do",
	            data: {carSeriesid:carSeriesid},
	            dataType: "json",
	            success: function(data){
	           		var dataObj=eval(data);
	           		var l="";
	           		var element="";
	           		$("#select_model").empty();
	           		if(dataObj.length>0){
		           		for(i=0;i<dataObj.length;i++){
							var map=dataObj[i];
							var type=map.type;
							var t=type.substring(0, 1);
							if(t!=l){
								l=t;
								if(i==0){
	  								element+="<optgroup label='"+l+"'>";
								}else{
									element+="</optgroup><optgroup label='"+l+"'>";
								}
							}
							element+="<option value='"+map.ID+"' modelCode='"+map.MODEL_CODE+"'>"+map.MODEL+"</option>";
		           		}
		           		$("#select_model").append(element);
	           		}else{
	           			$("#select_model").append("<option value='-1'>暂无数据</option>");
	           		}
	           		console.log(dataObj);
	            }
	        });
		}else{
			$("#select_model").empty();
			$("#select_model").append("<option value='-1'>暂无数据</option>");
		}
	}
	
	function loadMycar(){
		var mycarid=$("#select_mycar").val();
		//var modelcode=$("#select_mycar").find("option:selected").attr("modelcode");
		if(mycarid!=-1){
			$("#select_brand").attr("disabled",true);
			$("#select_series").attr("disabled",true);
			$("#select_model").attr("disabled",true);
			$("#other_car_model").attr("disabled",true);
			$.ajax({
	            type: "GET",
	            url: "/reserve/getMycarInfo.do",
	            data: {mycarid:mycarid},
	            dataType: "json",
	            success: function(data){
	           		var dataObj=eval(data);
	           		var num=data.REGISTER_NO;
	           		var vin=data.VIN;
	           		if(num){
		           		$("#car_num").val(num);
	           		}else{
	           			$("#car_num").val();
	           		}
	           		if(vin){
		           		$("#car_vin").val(vin);
		           		$("#car_vin").val();
	           		}
	            }
	        });
		}else{
			$("#select_brand").attr("disabled",false);
			$("#select_series").attr("disabled",false);
			$("#select_model").attr("disabled",false);
			$("#other_car_model").attr("disabled",false);
			$("#car_num").val("");
			$("#car_vin").val("");
		}
	}
	
	//加载预约时间
	var selectShop=$("#select_shop").val();
	if(selectShop!=-1){
		loadSelectTime(selectShop);
		loadShopDetail(selectShop);
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
		loadShopDetail(val);
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
		            	var other=dataObj[0].id;
		            	if(other=='other'){
		            		selectTime.attr("disabled",true);
		            		selectTime.append("<option value='other'>预约已满</option>");
		            	}else{
		            		selectTime.attr("disabled",false);
			            	$.each(dataObj, function(index, value) {
			            		selectTime.append("<option value='"+value.id+"'>"+value.begin+" - "+ value.end+"</option>");
			            	});
		            	}
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
			}else{
				var selectTeam=$('#select_team');
				selectTeam.empty();
				selectTeam.append("<option value='other' appointmentid='other'>预约已满</option>");
				selectTeam.attr("disabled",true);
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
			}else{
				var selectConsultant=$('#select_consultant');
				selectConsultant.empty();
				selectConsultant.append("<option value='other'>预约已满</option>");
				selectConsultant.attr("disabled",true);
			}
		}
	
	
	
	$("#create").on("click",function(){
		var btn = $(this);
		var openid=$("#openid").val();
		var selectMycar=$("#select_mycar").val();
		var model="";
		var modelCode="";
		//选择了自己的车型
		if(selectMycar!=-1){
			modelCode=$("#select_mycar").find("option:selected").attr("modelcode");
			model=$("#select_mycar").find("option:selected").text();
		}else{//选择了其他车
			//判断是否手动填写其他车型
			var other_car_model=$("#other_car_model").val();
			if(other_car_model==null||other_car_model==""){
				modelCode=$("#select_model").find("option:selected").attr("modelcode");
				model=$("#select_model").find("option:selected").text();
			}else{//手动填写了
				model=other_car_model;
			}
		}
		
		var carNum=$("#car_num").val();
		var carVin=$("#car_vin").val();
		if(carNum==""&&carVin==""){
			showResult(0,"车牌号和车架号至少填写一个。");
			return;
		}
		var shopid=$("#select_shop").val();
		var shopName=$("#select_shop").find("option:selected").text();
		if(shopid==-1){
			showResult(0,"请选择4S店。");
			return;
		}
		var timeid=$("#select_time").val();
		if(timeid==-1){
			showResult(0,"请选择预约时间。");
			return;
		}
		var teamid=$("#select_team").val();
		var appointmentid=$("#select_team").find("option:selected").attr('appointmentid');
		var teamName=$("#select_team").find("option:selected").text();
		if(teamid==-1){
			showResult(0,"请选择班组。");
			return;
		}
		var consultantid=$("#select_consultant").val();
		var consultantName=$("#select_consultant").find("option:selected").text();
		if(consultantid==-1){
			showResult(0,"请选择顾问。");
			return;
		}
		var msg="<div class='text-left'><span>车型:"+model+"<br>车牌:"+carNum+"<br>车架号:"+carVin+"<br>4S店:"+shopName+"<br>时间:"+timeid+"<br>班组:"+teamName+"<br>顾问:"+consultantName+"<br></span></div><strong>确定预约？</strong>";
		myconfirm(msg,function(){
			btn.button('loading');
			$.ajax({
	            type: "POST",
	            url: "/reserve/save.do",
	            data: {openid:openid,model:model,modelCode:modelCode,carNum:carNum,carVin:carVin,shopid:shopid,timeid:timeid,teamid:teamid,consultantid:consultantid,appointmentid:appointmentid},
	            dataType: "json",
	            success: function(data){
	            	btn.button('reset');
	           		var dataObj=eval(data);
	           		console.log(dataObj.code);
	           		console.log(dataObj.msg);
	           		showResult(dataObj.code,dataObj.msg);
	            }
	        	});
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
	
	function loadShopDetail(shopid){
		console.log(shopid);
		$.ajax({
            type: "POST",
            url: "/reserve/getShopDetail.do",
            data: {shopid:shopid},
            dataType: "json",
            success: function(data){
           		var dataObj=eval(data);
           		$("#shop_details").show();
           		$("#shop_address").text(dataObj.ADDRESS);
           		var tel="";
           		if(dataObj.TELEPHONE!=null){
           			tel=dataObj.TELEPHONE;
           		}
           		$("#shop_telephone").text(tel);
            }
        });
	}
	
	/**二次确认框*/
	function myconfirm(msg,callback){
		var myconfirm=$(".myconfirm");
		myconfirm.find("#confirm_content p").empty();
		myconfirm.find("#confirm_content p").html(msg);
		$("#layer-mask").off("click");
		$("#layer-mask").on("click",function(){
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
});

</script>
</html>