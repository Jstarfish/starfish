<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/include/head.jsp" %>
<div class="content">
    <div class="page-width">
        <div class="crumbs"><a href="/web-front">首页</a>&gt;<span>车辆洗美</span></div>
        <ul class="steps">
            <li class="active"><i>1</i><span>选择保养项目</span><em></em></li>
            <li><i>2</i><span>填写核对订单</span><em></em></li>
            <li class="end"><i>3</i><span>订单提交成功</span></li>
        </ul>
        <div class="mod-main">
            <div class="mod-services">
            </div>
        </div>
        <table class="order-td shopping-td">
            <thead>
            <tr>
						<th width="100" class="total-tit ele-vlmiddle">
							<label class="mr20"><input id="selectAll" name="selectAll" type="checkbox" />&nbsp;&nbsp;全选</label></th>
							<th>商品</th>
							<th width="80">单价（元）</th>
							<th width="100">数量</th>
							<th width="90">小计（元）</th>
							<th width="39">操作</th>
						</tr>
            </thead>
            <tbody id="saleCartSvc"></tbody>
			<tbody id="optGoods"></tbody>
        </table>        
		<div class="shopping-info mb20">
            <div>共 <span id="goodsNumber" class="red"></span>件 商品总价: <span id="shoppingPrices" class="red"></span></div>
             <div>亿投车吧门店（<a class="anormal" href="javascript:void(0);">安装及费用标准说明</a>） 服务费: <span id="svcPrices" class="red"></span></div>
        </div>
    </div>
    <div class="cart-toolbar">
        <div class="page-width">
            <!-- <input id="continueBuy" class="btn-border btn-h40" type="button" value="继续购买" />
            <a onClick="onDeleteCart('','svcs')" class="ml10" href="javascript:void(0);">删除选中的服务</a> -->
            <div class="fr">
                <span class="total-price">合计金额： <span id="finalAmount" class="red"></span></span>
                <input id="next" class="btn btn-h40" type="button" value="下一步" />
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/foot.jsp"></jsp:include>
<script type="text/javascript" src="<%=resBaseUrl%>/js-app/saleCart.js"></script>
<script type="text/javascript">
//----------------------------------------------------初始化-----------------------------------------------
var carSvc=extractUrlParams(location.href);
$(function() {
	loadCarSvc();
	$("a.tip3").live("click",function(){
		// 打开对话框-----------------------------------------
		var theDlg = Layer.dialog({
			title : "安装费详细说明",
			src : getAppUrl("/comn/installation/fee/details/jsp"),
			area : [ '800px', '260px' ],
			closeBtn : true,
			maxmin : true, // 最大化、最小化
			btn : [ "知道了" ]
		});
	});
});
// 渲染页面内容
function renderTitleHtml() {
	//获取模板内容
	var tplHtml = $id("addCarSvcTitleTpl").html();
	
	//生成/编译模板
	var theTpl = laytpl(tplHtml);
	
	//根据模板和数据生成最终内容
	var data={};
	var htmlStr = theTpl.render(data);
	
	//使用生成的内容
	$id("saleCartSvc").html(htmlStr);
}
	
	function addSaleCartSvcHtml(data) {
		var tplHtml = $id("addCarSvcTpl").html();
		//生成/编译模板
		var theTpl = laytpl(tplHtml);
		//根据模板和数据生成最终内容
		var htmlStr = theTpl.render(data);
		$("#saleCartSvc tr:eq(0)").after(htmlStr);
	}

	function addSaleCartSvc(svcId,externalUrl){
		var id =$id("carSvc"+svcId);
		if(id.attr("class")=='active'){
			var svcPo = svcParameterPo(svcId, "minus", null);
			if(svcPo != null){
				syncCartSvc(svcPo,function(saleCart){
					if($id("saleCartSvc").children("tr").length==2){
						$id("saleCartSvc").html("");//.children(":first").remove();
						$("input[name='selectAll']").attr("checked",null);
					}
					$id("cartSvcTr"+svcId).remove();
					//
					id.removeAttr("class");
					id.find("img").attr("src",id.attr("data-imgTwo"));
				});
			}
		}else{
			var svcPo = svcParameterPo(svcId, "add", true);
			if(svcPo != null){
				syncCartSvc(svcPo,function(saleCart){
					if(saleCart != null){
						var svcList = saleCart.saleCartSvcList;
						if(svcList!=null && svcList.length>0){
							for (var i = 0; i < svcList.length; i++) {
								var svc = svcList[i];
								if(svc != null){
									if(svc.svcId == svcId){
										if($id("saleCartSvc").find("> tr").length<=0){
											renderTitleHtml();
										}
										addSaleCartSvcHtml(svc);
										doOnCheckbox();
										checkParentObj();
										id.attr("class","active");
										id.find("img").attr("src",id.attr("data-img"));
									}
								}
							}
						}
					}
				});
			}
		}
	}
	
	//加载车辆服务分组和车辆服务列表
	function loadCarSvc() {
		var ajax = Ajax.post("/carSvc/svcGroup/list/do");
		ajax.data();
		ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				//获取模板内容
				var tplHtml = $id("carSvcListTpl").html();
				
				//生成/编译模板
				var theTpl = laytpl(tplHtml);
				
				//根据模板和数据生成最终内容
				var htmlStr = theTpl.render(result.data);
				//使用生成的内容
				$("div.mod-services").html(htmlStr);
				//初始服务列表样式
				$(".mod-services a").each(function(){
					var svcId=$(this).attr("id").split('carSvc').join('');
					var exist=carSvcIndexOf(svcId);
					if(exist){
						$(this).attr("class","active");
						$(this).find("img").attr("src",$(this).attr("data-img"));
					}
				   });
				//if (carSvc != null && carSvc.svcId != null) {
					//addSaleCartSvc(carSvc.svcId,"externalUrl");
				//	}
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();
	}
	//判断是否存在当前服务
	function carSvcIndexOf(carSvcId) {
		var exist = false;
		if (_saleCartSvcList!=null && _saleCartSvcList.length > 0) {
			for (var i = 0; i < _saleCartSvcList.length; i++) {
				if (_saleCartSvcList[i].svcId == carSvcId) {
					exist = true;
				}
			}
		}
		return exist;
	}
		$id("continueBuy").click(function(){
			location.href=getAppUrl("/product/supermarket/list/jsp");
			//window.location.href="/saleOrder/submit/jsp";
		});
		$id("next").click(function() {
			var listPo = selectlocal();
			if(listPo.length>0){
				var ajax = Ajax.post("/saleCart/svc/all/sync/do");
				ajax.data(listPo);
				ajax.done(function(result, jqXhr) {
					if (result.type == "info") {
						if(result.data!=null){
							//alert("跳转去结算页面！");
							location.replace(getAppUrl("/saleOrder/submit/jsp"));
						}else{
							//提示
							Layer.warning(result.message);
						}
					} else {
						Layer.warning(result.message);
					}
				});
				ajax.go();
			}else{
				Layer.warning("请选择服务！");
			}
		});
</script>
</body>
<script type="text/html" id="carSvcListTpl" title="查询车辆服务和分组信息列表">
	<h1>服务项目<span>（可多选）</span></h1>
	{{# for(var i = 0, len = d.length; i < len; i++){ }}
         {{# var svcGroup = d[i]; }}
         <dl>
         <dt>{{svcGroup.name}}：</dt>
         <dd>
           {{# if(!isNoB(svcGroup.svcxList)){ }}
           {{# for(var j = 0;j< svcGroup.svcxList.length; j++){ }}
                {{# var svc =svcGroup.svcxList[j]}}
				{{# if(svc.id!=-1){ }}
                <div class="control-checkbox-analog">
				<a id="carSvc{{svc.id}}" data-img="{{svc.fileBrowseUrlIcon2}}" data-imgTwo="{{svc.fileBrowseUrlIcon}}" href="javascript:void(0)" onClick="addSaleCartSvc({{svc.id}})">
					<img src="{{svc.fileBrowseUrlIcon}}" alt=""><span>{{svc.name}}</span><i></i>
				</a>
				</div>
			{{# } }}
			{{# } }}
         	{{# } }}
        </dd>
        </dl>
	{{# } }}
</script>
<script type="text/html" id="addCarSvcTitleTpl" title="添加购物车">
<tr class="title">
	<th colspan="6"><div class="text-left"><label class="mr20">保养服务</label></div></th>
</tr>
</script>
<script type="text/html" id="nogoodsRowTpl">
</script>
<script type="text/html" id="addCarSvcTpl" title="添加购物车">
{{# var cart = d; }}
{{# if(!isNoB(cart)){ }}
<tr id="cartSvcTr{{cart.svcId}}">
	<td colspan="2">
		<dl class="goods-item goods-item2">
			<dd><input id="checkSvc{{cart.svcId}}" data-id="{{cart.svcId}}" data-type="svc" name="selectCheckbox" class="mr20" type="checkbox" checked/></dd>
			<dt><a href=""><img src="{{cart.svcxAlbumImg}}" alt="" /></a></dt>
			<dd class="text"><a href=""><b>{{cart.carSvcName}}</b><br />{{cart.desc}}</a></dd>
		</dl>
	</td>
	<td>¥&nbsp;{{cart.salePrice}}</td>
	<td>1</td>
	<td><div class="price1">¥&nbsp;{{cart.amountInfo.saleAmount}}</div></td>
	<td><!--<a class="delete" onClick="onDeleteCart({{cart.svcId}},'svc')"></a>--></td>
</tr>
{{# } }}
</script>
<script type="text/html" id="saleCartRowTpl">
{{# var svcList = d; }}
{{# if(!isNoB(svcList)){ }}
<tr class="title">
	<th colspan="6"><div class="text-left"><label class="mr20">保养服务</label></div></th>
</tr>
{{# for(var i=0;i<svcList.length; i++) {}}
{{# var cart = svcList[i]; }}
<tr id="cartSvcTr{{cart.svcId}}">
	<td colspan="2">
		<dl class="goods-item goods-item2">
			<dd><input id="checkSvc{{cart.svcId}}" data-id="{{cart.svcId}}" data-type="svc" name="selectCheckbox" class="mr20" type="checkbox" /></dd>
			<dt><a href=""><img src="{{cart.svcxAlbumImg || null}}" alt="" /></a></dt>
			<dd class="text"><a href=""><b>{{cart.carSvcName}}</b><br />{{cart.desc}}</a></dd>
		</dl>
	</td>
	<td>¥&nbsp;{{cart.salePrice}}</td>
	<td>1</td>
	<td><div class="price1">¥&nbsp;{{cart.amountInfo.saleAmount}}</div></td>
	<td><!--<a class="delete" onClick="onDeleteCart({{cart.svcId}},'svc')"></a>--></td>
</tr>
{{# } }}
{{# } }}
</script>

<script type="text/html" id="optGoodsRowTpl">
{{# var optGoods = d; }}
{{# if(!isNoB(optGoods) && optGoods.length>0){ }}
<tr class="title">
	<th colspan="6"><div class="text-left"><label class="mr20">尖品超市</label></div></th>
</tr>
{{# for(var j=0;j<optGoods.length; j++) { }}
{{# var goods = optGoods[j]; }}
<tr id="cartGoodsTr{{goods.productId}}">
	<td colspan="2">
		<dl class="goods-item goods-item2">
			<dd><input id="checkGoods{{goods.productId}}" data-id="{{goods.productId}}" data-type="goods" name="selectCheckbox" class="mr20" type="checkbox" /></dd>
			<dt><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}"><img src="{{goods.productAlbumImg}}" alt="" /></a></dt>
			<dd class="text"><a href="{{__appBaseUrl}}/product/detail/jsp?productId={{goods.productId}}">{{goods.productName}}</a></dd>
		</dl>
	</td>
	<td>¥&nbsp;{{goods.productAmount}}</td>
	<td>
		<div id="numSpinner{{goods.productId}}" name="numSpinner" class="num-spinner" data-id="{{goods.productId}}">
			<a href="javascript:;" class="decr"><i></i></a><input type="text" name="" value="{{goods.quantity}}"><a href="javascript:;" class="incr"><i></i></a>
		</div> 
	</td>
	<td>
		{{# if(goods.amountInfo!=null){ }}
			<div class="price1">¥&nbsp;{{goods.amountInfo.saleAmount}}</div></td>
		{{# } }}
	<td><a class="delete" onClick="onDeleteCart({{goods.productId}},'goods')"></a></td>
</tr>
{{# } }}
{{# } }}
</script>
</html>