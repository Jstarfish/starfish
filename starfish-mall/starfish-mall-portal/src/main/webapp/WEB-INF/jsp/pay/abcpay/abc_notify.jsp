<%@ page contentType="text/html; charset=gb2312" %>
<% response.setHeader("Cache-Control", "no-cache"); %>

<%-- <URL><%=request.getAttribute("tMerchantPage")%></URL> --%>

<HTML>
<HEAD>
<meta http-equiv="refresh" content="0; url='<%=request.getAttribute("tMerchantPage")%>'">
</HEAD>
</HTML>