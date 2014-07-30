<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<%
	class Test{
		public void t(String a){
			System.out.println(a);
		}
	}
%>
<%
	new Test().t("a");
%>