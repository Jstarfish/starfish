<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="base.jsf" %>
<div class="section-left1">
    <ul class="menu">
        <li>
            <h3>订单中心</h3>
            <ul>
                <li><a href="<%=appBaseUrl%>/ucenter/saleOrder/list/jsp">服务订单</a></li>
                <li><a href="<%=appBaseUrl%>/ucenter/ecardOrder/list/jsp">e卡订单</a></li>
                <li><a href="<%=appBaseUrl%>/ucenter/blog/list/jsp">我的博文</a></li>
            </ul>
        </li>
        <li>
            <h3>资产中心</h3>
            <ul>
                <li><a href="<%=appBaseUrl%>/ucenter/ecard/list/jsp">我的e卡</a></li>
                <li><a href="<%=appBaseUrl%>/user/coupon/jsp">商品优惠券</a></li>
                <li><a href="<%=appBaseUrl%>/user/svc/coupon/jsp">服务优惠券</a></li>
                <li><a href="<%=appBaseUrl%>/ucenter/userSvcPackTick/list/jsp">服务套餐票</a></li>
            </ul>
        </li>
        <li>
            <h3>我的车辆</h3>
            <ul>
                <li><a href="<%=appBaseUrl%>/ucenter/userCar/list/jsp">我的车辆</a></li>
            </ul>
        </li>
        <li>
            <h3>账户设置</h3>
            <ul>
                <li><a href="<%=appBaseUrl%>/ucenter/user/info/jsp">基本设置</a></li>
                <li><a href="<%=appBaseUrl%>/ucenter/user/linkWay/jsp">常用联系方式管理</a></li>
                <li><a href="<%=appBaseUrl%>/ucenter/user/safe/jsp">账户安全</a></li>
            </ul>
        </li>
          <li>
            <h3>客户服务</h3>
            <ul>
                  <li><a href="<%=appBaseUrl%>/ucenter/userFeedback/list/jsp">意见反馈</a></li>
            </ul>
        </li>
    </ul>
</div>