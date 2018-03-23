<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/include/head.jsp" />
	<title>商品详情</title>
	<style type="text/css">
		html, body, ul, li, ol, dl, dd, dt, p, h1, h2, h3, h4, h5, h6, form, fieldset, legend, img{ margin:0; padding:0; }
		fieldset, img,input ,button{ border:none; }
		img{display: block;}
		ul, ol { list-style:none; }
		select, input { vertical-align:middle; }
		select, input, textarea { font-size:12px; margin:0; }
		textarea { resize:none; }
		table { border-collapse:collapse;}
		.clearfix:after { content:"."; display:block; height:0; visibility:hidden; clear:both; }
		.clearfix { zoom:1; }
		.clearit { clear:both; height:0; font-size:0; overflow:hidden; }
		a { color:#666; text-decoration:none; }
		a:hover { text-decoration:underline; }
		body{ font-family:"Microsoft YaHei","SimSun"; font-size:12px; color:#666; line-height:18px;}
		.f-l{ float:left;}
		.f-r{ float:right;}
		em,i,sub,u{ font-style:normal;}
		.breadline{ width:1210px; margin:10px auto 10px; font-size:13px;line-height:150%; padding-left:5px; overflow:hidden;zoom:1;}
		.breadline a.current{ color:#E43A3D}
		.green_ps{ color:#079B43;}
		.green_ps:hover{ color:#079B43;}
		.blue_ps{ color:#1b68ac;margin:0 3px;}
		.blue_ps:hover{ color:#1b68ac;}
		.red_ps{ color:#E4393C; margin:0 3px;}
		.red_ps:hover{color:#E4393C; }
		.yzm_btn{ border:1px solid #ccc;display:inline-block; color:#999;  height:20px; line-height:20px; text-align:center; padding:1px 5px; background-color:#F7F7F7; border-radius:2px;}
		.yzm_btn:hover{ border-color:#999; color:#333; text-decoration:none;}
		.yzm_txt{ border:1px solid #ccc;color: #000; height: 25px;line-height: 25px;padding: 2px 4px; width:85px;}
		.s_main_in{ width:1210px; margin:0 auto; margin-top:10px; position:relative;}
		.w{ margin:0 auto; width:900px;}
		.f{ margin:0 auto; width:1000px;}
		.pd_30{ padding:0 15px;}
		.pd_10{ padding:0 5px;}
		/*初始化end*/
		/*header*/
		.top_new{ width:100%; background-color:#F7F7F7; height:30px; border-bottom:1px solid #EEEEEE; font-size:12px;}
		.top_new_in{ width:1210px; height:30px; margin:0 auto;}
		.top_new_in_login{ line-height:30px; padding-left:10px; float:left; margin-right:5px;}
		.top_new_in_left{ float:left; line-height:30px; font-family:"微软雅黑"}
		.top_new_in_left span{margin-right:10px; }
		.top_new_in_left .two a,.top_new_in_left .three a{ color:#E43A3D}
		.top_new_in_left .one{ background:url(../../images/ico/map_flag.png) no-repeat left center; padding-left:20px; display:inline-block; }
		.top_new_in_right{ float:right; line-height:30px; padding-right:10px;}
		.top_new_in_left_a{ color:#e4393c;}
		.top_new_in_right_b{ float:left; display:block; width:85px; text-align:center;}
		.top_new_in_right_b a:hover{color:#e4393c;}
		.line_new{ float:left; color:#ddd!important; margin:0 2px;}
		.hot_zi{ color:#E4393C; font-weight:500}
		.top_new_in_right_ol{ display:none; position:absolute; z-index:888; background:#fdfdfd; width:85px; padding:3px 0; border:1px solid #ddd; border-top:none;}
		.search_section{ width:1210px; overflow:hidden; margin:0 auto;}
		.logo_img{background:url(../../images/site_logo.png) no-repeat; width:206px; height:74px; text-indent:-2000em; display:block; margin-top:13px;}
		.search_section_middle{ width:720px; margin:0 auto; height:100px; float:left; padding-top:17px;}
		.logo2{ float:left; margin-right:50px;}
		.search_section_middle_t{ height:25px;}
		.search_section_middle_t ul li{ float:left; width:50px;  cursor: pointer;height: 25px;line-height: 25px;text-align: center;width: 50px; font-size:14px; color:#333;font-family:"微软雅黑"}
		.search_section_middle_t ul li.current{ background-color:#e4393c; color:#fff; font-weight:600;} 
		.search_section_middle_t ul li.current:hover{ background-color:#e4393c;color:#fff; } 
		.search_section_middle_t ul li:hover{ background-color:#ffeeee; color:#E43A3D; } 
		.search_section_right{ float:left; padding-top:44px; position:relative;}
		.top_new_in_right_ul{ float:left;}
		.search_section_right span{ float:left;width:110px; height:35px; background-color:#F7F7F7; border:1px solid #E4E4E4; display:block;  margin-left:50px; text-align:center; line-height:35px; padding-left:22px; }
		.search_section_right_a{font-size:14px; color:#666;}
		.search_section_right_a:hover{color:#e4393c; }
		.search_section_right_af{background:url(../../images/ico/shopcar.png) no-repeat 12px center;}  
		.search_section_right_af2{background:url(../../images/ico/shopcar.png)  no-repeat 5px center;}  
		.search_section_middle_m{ height:35px; border:3px solid #E43A3D; }
		.search_section_middle_m_01{ width:604px; float:left; height:25px; line-height:25px;padding:5px; color:#666; font-size:14px;}
		.search_section_middle_m_02{ width:10%; background:#E43A3D; color:#fff; float:left; height:35px; line-height:35px;overflow: hidden;text-align: center;transition-duration: 0.2s;transition-property: background;width: 100px; z-index: 500; font-size:16px; font-weight:bold; cursor:pointer;font-family:"微软雅黑"}
		.search_section_middle_b{ font-size:12px; color:#666; margin-top:5px;}
		.search_section_middle_b a:hover{ color:#e4393c;}
		.search_section_middle_b a{ margin:0 3px;}  
		.search_section_middle_b a:hover{ margin:0 3px; text-decoration:none;}  
		
		/*nav*/  
		.nav{ height:38px; width:100%; background-color:#007E0F; }
		.nav-in{ margin:0 auto; height:45px; width:1210px; position:relative; }
		.arrow{width:0; height:0; border-width:5px; border-color:#fff transparent transparent transparent; border-style:solid dashed dashed dashed; display:inline-block; vertical-align:middle; margin-top:2px; margin-left:7px;}
		.fenlei{ width:253px; float:left; height:45px; text-align:left;line-height:45px; position:relative;}
		.fenlei:hover .arrow{ transform:rotate(180deg); transform-origin:5px 2.5px;}
		.fenlei ol{ z-index:9999; position:absolute ; top:45px; left:0;background-color:#FFf; border:2px solid #E43A3D; border-top:0;  width:249px; overflow:hidden; display:none;}
		.fenlei  .fl_01{padding-left:12px;font-size:18px; color:#fff ; font-weight:400;font-family:"微软雅黑"}
		.fenlei  .fl_01:hover{ text-decoration:none}
		.fenlei .xla .title{ font-size:16px; font-weight:400; color:#E43A3D; display:block; line-height:25px; padding-top:7px;font-family:"微软雅黑";}
		.fenlei  .xla li{ line-height:35px; border-bottom:1px solid #e4e4e4; padding:0 20px;}
		.fenlei  .xla li:hover{ background-color:#F5F5F5;}
		.fenlei  .xla_son{ line-height:30px; padding:0 15px;  padding-bottom:5px;}
		.fenlei  .xla_son a{ width:44px; float:left;}
		.fenlei .arrow{ position:absolute; top:20px; right:25px; transition:all 0.3s linear 0s;}
		.nav ul{ float:left;}
		.nav ul li{ float:left;}
		.nav ul li a{ padding:0 25px; display:block; line-height:45px; color:#fff; font-size:18px; font-weight:400;height:45px;font-family:"微软雅黑"}
		.nav ul li a:hover{ background:#b2000b; text-decoration:none;} 
		.nav ul li a.cur{background:#b2000b;}
		.nav_current{ background-color:#006A0D}
		/*footer*/
		#s_ftw{border-top:1px solid #dcdcdc; width:1210px; margin:10px auto 0px;}
		.ftql,#s_ft{margin:0 auto; margin-top:10px;}
		.ft_suggest{text-align:right;}
		.ftql_s{float:left; width:140px; margin:0px 40px 0px 50px;}
		.ftql_s li a:hover{ text-decoration:none;}
		.ftql_s h3{font-size:14px;padding-bottom:8px;width:130px;border-bottom:1px dotted #ccc;font-family:"微软雅黑";}
		.ftql_s ul{padding:5px 0; line-height:20px;}
		.ftql_d{padding-bottom:16px;}
		.ftql_d .str{font-weight:600;font-family:"微软雅黑"; margin-bottom:5px; font-size:14px;}
		.stel{color:#E43A3D;font-size:18px;font-weight:500; font-family:Arial, Helvetica, sans-serif;}
		.ft_quicklinks{padding-top:25px; padding-bottom:10px;}
		.ft_banners2 li,.ft_banners1 li{display:inline-block;}
		.ftql_d .val a{ font-family:Arial, Helvetica, sans-serif; font-size:14px;}
		.copyright em,.copyright i{ font-family:Arial, Helvetica, sans-serif}
		.copyright em{margin:0 3px;}
		.copyright p{ text-align:center;}
		.copyright{ margin-bottom:40px;}
		#warp {width:249px; border:1px solid #e8e8e8;}
		.menu_master {   padding:5px 10px;cursor:pointer;font-weight:500;color:#333; border-top:1px solid #eee; border-bottom:1px solid #eee; font-size:16px; background-color:#F7F7F7;font-family:"Microsoft YaHei" ;}
		.menu_master img { vertical-align:middle; }
		.sub_menu { display:none; }
		.sub_menu ul li a {padding:3px 12px; height:18px;color:#666;text-decoration:none; font-size:14px; line-height:18px; width:87px; display:inline-block;}
		.sub_menu_1_curr{color:#E43A3D!important; text-decoration:underline!important;}
		.sub_menu ul li a:hover{ text-decoration:underline; color:#E43A3D;} 
		.lunbo_tup_t2{ height:240px;}   
		.sub_menu ul{ padding:10px;}
		/*hotsale*/
		.hotsale{ margin-top:15px;}
		.hotsale .tup a{display:block; }
		.hotsale_01{ padding:10px 20px;}
		.xingxi{ border:1px solid #E8E8E8; width:249px;}
		.xingxi .title{ color:#333; font-size:16px; display:block; height:33px; line-height:30px; background:#F7F7F7; padding-left:20px;font-family:"Microsoft YaHei" ;}
		.spu_name{ height:50px; display:block; text-align:center; line-height:50px; font-size:18px; color:#333; border-bottom:1px solid #e8e8e8;font-family:"Microsoft YaHei" ;}
		.spu_name:hover{ color:#333;}
		 .hehe{ display:block; padding:10px 0; position:relative;}
		 .hotsale_01_01{ width:209px;}
		.heihei_01{ font-size:15px; display:block; margin:8px 0;}
		.heihei_01 .heihei_01_01{ background:url(../ico/u1.png) no-repeat; width:11px; height:15px; display:inline-block; vertical-align:middle; margin-bottom:3px; margin-left:10px;}
		.heihei_01 .heihei_01_02{ background:url(../ico/u2.png) no-repeat; width:11px; height:15px; display:inline-block; vertical-align:middle; margin-bottom:3px; margin-left:10px;}
		.heihei_01 .heihei_01_03{ background:url(../ico/u2.png) no-repeat; width:11px; height:15px; display:inline-block; vertical-align:middle; margin-bottom:3px; margin-left:10px;}
		.heihei_01 i{ color:#FF6600; font-style:normal}
		.heihei_01 .heihei_01_04{ color:#008A00; font-style:normal}
		.hehe .price{ font-size:14px; color:#E43A3D; font-weight:bold; padding-top:10px; text-align:left;font-family:"Microsoft YaHei" ;}
		.hehe a:hover{ text-decoration:none;}
		.hot_ico{ position:absolute; width:42px; height:29px; background:url(../../images/ico/ico5.png) no-repeat; text-align:center; line-height:26px; color:#fff; font-size:15px; top:10px; right:0;font-family:"Microsoft YaHei" ;}
		

		/*热销新品*/
		.hot_sale2{ height:auto;  margin-top:15px; border:1px solid #e8e8e8;}
		.hot_sale2 h4{ font-size:14px; color:#333; padding-left:15px; padding-top:10px; font-weight:400;}
		.hot_sale_main{ overflow:hidden; padding:10px 15px;}
		.hot_sale_main span{ display:block; margin:3px 0;}
		.hot_sale_main_1{ float:left; width:199px; padding:0 15px;}
		.hot_sale_main_1_tup a img{display:block; margin:0 auto;}
		.hot_sale_main_title{}
		.hot_sale_main_title i{ color:#E43A3D; font-style:normal; font-weight:400;}
		.hot_sale_main_price em{ font-style:normal; color:#DE3D35; font-weight:700;font-family:"Microsoft YaHei" ;}
		
		
		.g_flei{ margin-right:-1px; margin-top:15px;}
		.g_fl_item{ padding:20px; border:1px  solid #e8e8e8; float:left; margin-left:-1px; margin-top:-1px; overflow:hidden;}
		.g_fl_item:hover{ position:relative; border-color:#E43A3D;}
		.g_fl_item .price{ font-size:18px; color:#E43A3D; font-weight:700; padding-top:10px;font-family:"Microsoft YaHei" ;}
		.g_fl_item .name{ margin-top:3px;}
		.g_fl_item .btn{ padding-top:10px; overflow:hidden;}
		.g_fl_item  .swc_in{ display:inline-block; border-radius:2px; background-color:#E43A3D; color:#fff;  width:80px; padding:5px 0; text-align:center;}
		.g_fl_item .btn em{ display:inline-block; margin-top:5px;}
		.g_fl_item .name a{ width:196px; display:block;}
		.g_fl_item .name a:hover{ text-decoration:none;}
		
		/* select */
		.select{border:#e6e6e6 1px solid;width:100%;font-size:12px; }
		.select li{list-style:none;padding:10px 0 5px 75px}
		.select .select-list{border-bottom:#e6e6e6 1px dashed}
		.select dl{zoom:1;position:relative;line-height:24px;}
		.select dl:after{content:" ";display:block;clear:both;height:0;overflow:hidden}
		.select dt{width:100px;margin-bottom:5px;position:absolute;top:0;left:-100px;text-align:right;color:#666;height:24px;line-height:24px}
		.select dd{float:left;display:inline;margin:0 0 5px 5px;}
		.select a{display:inline;white-space:nowrap;padding:0 10px;text-decoration:none;}
		.select a:hover{color:#E43A3D;}
		.select-result dt{font-weight:bold}
		.select-no{color:#999}
		.select .select-result a{border:1px solid #e8e8e8; padding:2px 10px; padding-right:9px; position:relative;}
		.select .select-result a:hover{background-position:right -15px; border-color:#E43A3D}
		
		
		
		/*商家信息*/
		.xingxi .title2{ text-align:center; padding-left:0; }
		 .hehe .top2  a{ float:left; margin:0 5px;}
		.sjxx_1_a{ padding:5px; border-bottom:1px solid #e8e8e8; overflow:hidden; padding-bottom:15px;}
		.sjxx_1{padding: 15px 8px 8px;}
		.sjxx_1_a img{ margin:0 5px; float:left;}
		.sjxx_1_b p{ font-size:14px; color:#333; line-height:30px;}
		.sjxx_1_b p i{ color:#666; font-size:12px; padding-left:5px;}
		.sjxx_1_b{ padding:10px; border-bottom:1px solid #e8e8e8;font-family:"Microsoft YaHei" ;}
		.sjxx_1_b p span{ color:#1B68AC;}
		.sjxx_up_color{ color:#FF6600;}
		.sjxx_down_color{ color:#008900;}
		.sjxx_1_c{ padding:10px;}
		.sjxx_1_c p{ font-size:14px; line-height:30px;}
		.sjxx_kf{ display:inline-block; width: 80px; height:24px; color:#fff; background-color:#E43A3D; line-height:24px; text-align:center; border-radius:2px; margin-right:10px;}
		.sjxx_kf:hover{ color:#fff;}
		.sjxx_1_c_a{ margin-top:10px;}
		.sjxx_sc{  display:inline-block; width: 80px; height:24px; color:#fff; background-color:#3869CF; line-height:24px; text-align:center; border-radius:2px;}
		.sjxx_sc:hover{ color:#fff;}
		/*end*/
		
		/*商铺筛选条件*/
		.dianp_sx{ border:1px solid #ededed;}
		.dp_sx_title{ font-size:15px; line-height:30px; background-color:#F7F7F7; padding-left:20px;}
		.dp_sx_title strong{ color:#E43A3D; font-weight:600;}
		.dianp_sx  .brand{ position:relative; border-bottom:1px dashed #dedede;}
		.dianp_sx .brand .brand_title{ position:absolute; top:11px; left:20px; color:#999;}
		.dianp_sx .brand .b_t_bd{ padding:0 95px; padding-top:12px;}
		.dianp_sx .brand .b_t_bd a{ margin:0 30px 10px 0;  float:left;}
		.dianp_sx .brand .b_t_bd a:hover{ text-decoration:none;}
		.dianp_sx .brand  .b_t_more{ position:absolute; top:11px;right:40px; display:block; border:1px solid #dedede; padding:0 3px; cursor:pointer;}
		.dianp_sx .brand  .b_t_more:hover{ color:#E43A3D}
		.spu_ad{ height:245px; background-color:pink; margin-top:15px;}
		.sort_inner{  height:39px; border:1px solid #e8e8e8; background:#F7F7F7; margin-top:15px; }
		.sort_inner_li{ height:39px; line-height:39px; float:left; text-align:center;position:relative;}
		.sort_inner_li_01{  }
		.sort_inner_a{ display:block; padding:0 19px;margin-left:-1px;}
		.sort_inner_a:hover{ border-left:1px solid #e8e8e8; border-right:1px solid #e8e8e8;background:#fff; color:#E43A3D; position:relative; padding:0 18px; text-decoration:none;}
		.curre{ border-left:1px solid #e8e8e8; border-right:1px solid #e8e8e8;background:#fff; color:#E43A3D; position:relative; padding:0 18px;}
		.jiagequjian_li input{ width:53px; height:21px;line-height:21px; border:1px solid #DFDFDF; color:#999;}
		.jiagequjian_ul{ padding:8px 0 0 10px; overflow:hidden; width:180px;}
		.jiagequjian_li{ float:left;}
		.jiagequjian_li_btn input{background-color:#E43A3D; color:#fff; border-radius:3px; border:none; cursor:pointer;height:21px; line-height:21px; text-align:center;}
		.jiagequjian_li_btn input:hover{background-color:#F82800}
		.jiagequjian_li_btn{display:none;margin-top:1px;}
		.arrow2{  background:url(../ico/order_arrow.png) no-repeat -398px -139px; width:7px; height:4px; display:block; position:absolute;right:6px;top:18px;}
		.sort_inner_a_ul_li a{ display:block; line-height:40px; padding:0 20px; }
		.sort_inner_a_ul_li a:hover{ text-decoration:none;}
		.sort_inner_a_ul{ border:1px solid #CCCCCC; background:#fff; z-index:2222; position:absolute; overflow:hidden; width:120px; top:-1px; left:0; display:none;}
	    /* filmslide 经过图片放大 */
		.filmslide{width:352px;position:absolute; top:0; left:0;}
		.filmslide .smallpic{height:54px;overflow:hidden;margin:10px 0 0 0; width:312px; position:relative; padding:0 20px;}
		.filmslide .smallpic .abtn,.filmslide .smallpic .smallscroll li,.hgscrollbox .abtn{background:">>"}
		.filmslide .smallpic .abtn{display:block;float:left;}
		.filmslide .smallpic .aleft{background-position:-68px -139px;position:absolute; top:0; left:0; width:17px; height:54px;}
		.filmslide .smallpic .aright{background-position:-85px -139px; position:absolute; top:0; right:0; width:17px; height:54px; }
		.filmslide .smallpic .smallscroll li{width:50px;height:50px;overflow:hidden;float:left;margin:0 4px;border:2px solid #e8e8e8;}
		.filmslide .smallpic .smallscroll li:hover{ border-color:#CC0814;}
		.filmslide .smallpic .smallscroll li#onlickImg{border-color:#CC0814;}
		.bigImg{border:solid 1px #e8e8e8;height:350px;position:relative;}
		.bigImg #winSelector{width:204px;height:204px;}
		#winSelector{position:absolute;cursor:crosshair;filter:alpha(opacity=15);-moz-opacity:0.15;opacity:0.15;background-color:#000;border:1px solid #fff;}
		#bigView{position:absolute;border: 1px solid black;overflow: hidden;width:260px;height:295.652px;top:300px;left:540px;z-index:999;}
		#bigView img{position:absolute;}
		.sharecon{height:20px;line-height:20px;padding-top:10px;}
		.sharecon a{display:inline-block;float:left;background:url(../../images/goods_detail/shareicon.gif) no-repeat;line-height:20px;padding:0 0 0 20px;margin:0 0 0 5px;}
		.sharecon a.kaixin{background-position:0 -18px;}
		.sharecon a.xinlang{background-position:0 -53px;}
		.sharecon a.tengxun{background-position:0 -86px;}
		.s_main_in2{ height:800px;}
		.sp_det_big_box{ width:1000px;}
		.smallscroll{width:310px; overflow:hidden;position:relative; }
		/*经过图片放大end*/
		
		
		.goods_d_box{ padding-left:370px; position:relative; min-height:550px;}
		.g_d_name{ padding-bottom:10px; border-bottom:1px dotted #ccc;}
		.g_d_name h2{ width:840px; font-weight:600; font-family:"Microsoft YaHei" ; margin-bottom:5px;}
		.g_d_name strong{ color:#E43A3D; font-size:16px; font-family:"Microsoft YaHei" ;}
		.g_d_b_son .right{ float:right;}
		.g_d_b_son{ margin-top:15px;}
		<!--left开始-->
		.iteminfo_buying{ width:500px;}
		/*商品详细页*/
		.iteminfo{}
		.iteminfo_tit{ color:#333; font-size:22px; font-family:"微软雅黑",Arial, Helvetica, sans-serif; border-bottom:1px solid #e3e3e3; font-weight:normal; padding-bottom:12px; line-height:32px;}
		.iteminfo_parameter{ padding:8px 0; }
		.iteminfo_parameter_default{ }
		.iteminfo_parameter dt{  float:left; display:inline; width:72px; white-space:nowrap; text-align:right; }
		.iteminfo_parameter  dd{ float:left; margin-left:10px;}
		.iteminfo_mktprice{font-style:normal; margin-left:10px; font-family:"宋体" ;}
		    
		    
		.lh32{ line-height:32px;}
		.iteminfo_price{ color:#c00; font-size:14px; font-family:Arial, Helvetica, sans-serif; vertical-align:middle;}
		.sys_item_price{ color:#c00; font-size:22px; vertical-align:middle;}
		.iteminfo_buying{ background:#f7f7f7; padding:12px 0;}
		.iteminfo_mktprice_red{ font-size:18px; color:#E43A3D; font-weight:bold; font-family:"Microsoft YaHei" ;}
		/*规格*/
		.sys_item_spec{ float:left; width:588px;}
		.sys_item_spec dl.iteminfo_parameter{ padding-top:5px; padding-bottom:5px;}
		.sys_item_spec dl.iteminfo_parameter dt{ line-height:32px;}
		
		/*一下正对图片*/
		.sys_spec_img li{ float:left; height:54px; position:relative; margin:2px 6px 2px 0;}
		.sys_spec_img li a{height:50px; width:50px; padding:1px; border:1px solid #ccc; float:left; background:#fff; display:inline-block; outline:none;}
		.sys_spec_img li a img{ width:50px; height:50px; display:block;}
		.sys_spec_img li a:hover{ border:2px solid #e4393c; padding:0; text-decoration:none;}
		.sys_spec_img li i{ position:absolute; width:10px; height:10px; font-size:0; line-height:0; right:2px; bottom:2px; background:url(../../images/goods_detail/goods/sys_item_selected.gif) no-repeat right bottom; z-index:99; display:none;}
		.sys_spec_img li.selected a{ border:2px solid #e4393c; padding:0;}
		.sys_spec_img li.selected i{ display:block;}
		
		.sys_spec_text{}
		.sys_spec_text li{ float:left; height:28px; position:relative; margin:2px 6px 2px 0; outline:none;}
		.sys_spec_text li a{ height:24px; padding:1px 6px; border:1px solid #ccc; background:#fff; display:inline-block; line-height:24px; font-family:Arial, Helvetica, sans-serif}
		.sys_spec_text li a:hover{ border:2px solid #e4393c; padding:0 5px; text-decoration:none;}
		.sys_spec_text li i{ position:absolute; width:10px; height:10px; font-size:0; line-height:0; right:2px; bottom:2px; background:url(../../images/goods_detail/goods/sys_item_selected.gif) no-repeat right bottom; z-index:99; display:none;}
		.sys_spec_text li.selected a{ border:2px solid #e4393c; padding:0 5px;}
		.sys_spec_text li.selected i{ display:block;}
		/*加减数量*/
		#qty_item_1{ border: 1px solid #ccc;height: 16px; padding: 2px; text-align: center;width: 30px;}   
		.jjsl { padding-top:6px;} 
		.jjsl_a{ line-height:22px;}
		.sp_detail_btn .buy_btn,.sp_detail_btn  .buy_car,.sp_detail_btn   .buy_sel{ display:block;float: left; margin-right:15px;width: 140px;}
		.sp_detail_btn .buy_btn{ background-color: #ffeded;border: 1px solid #CD0917;color:#CD0917;font-size: 16px;height: 38px;line-height: 38px;  overflow: hidden; position: relative;text-align: center;}  
		.sp_detail_btn  .buy_car{background-color:#CD0917;border: 1px solid #CD0917;color: #fff;font-size: 16px;height: 38px;  line-height: 38px; margin-right: 0; overflow: hidden;text-align: center; }   
		.sp_detail_btn  .buy_sel{background-color:#909090;border: 1px solid #747474;color: #fff;font-size: 16px;height: 38px;  line-height: 38px; margin-right: 0; overflow: hidden;position: relative;text-align: center;  left:15px;}   
		.sp_detail_btn{ padding-left:82px; padding-top:30px; font-family:"Microsoft YaHei" ;}    
		 .sp_detail_btn a:hover{ text-decoration:none;}   
		/* 加减数量end*/  
		/* dtsummary */
		.dtsummary{margin-top:15px; width:940px;height:40px;position:relative;}
		.dtsummary li{ float:left; border:1px solid #e8e8e8; margin-left:-1px; border-bottom:none;background-color:#F7F7F7;}
		.dtsummary  a,.dtsummary li a{display:block;float:left;cursor:pointer;}
		.dtsummary li a{height:31px;line-height:31px;border:solid 1px #fff;font-size:14px;width:128px;text-align:center; font-family:"Microsoft YaHei" ;}
		.dtsummary li.current{border-style:solid;border-width:1px;z-index:1; position:relative; background-color:#fff;}
		.dtsummary li a.current span{background:#fff;color:#E43A3D;font-weight:800;}
		.dtsummary li a:hover,.dtsummary li a:hover span{color:#CC0814;text-decoration:none;}
		.pro1 dl{ background-color:#F7F7F7; padding:15px; height:167px; overflow:hidden; padding-bottom:0;}
		.pro1 dl dt{ line-height:22px; font-size:14px;padding-bottom:5px;}
		.pro1 dl dd{ float:left; width:100%; line-height:24px; margin-bottom:3px;}
		.pro2_bd_box .bd{ border:1px solid #e8e8e8;padding:15px; overflow:hidden}
		.pro2_bd_box .bd ul li{ margin-bottom:15px;} 
		.pro2_bd_box .bd ul li p{ line-height:24px; }
		.pro2_bd_box .title{font-size:16px; font-family:"微软雅黑"; border-left:3px solid #E43A3D; color:#333;}
		.pro2_bd_box .title strong{ padding-left:12px;}
		.pro2_bd_box .title_far{ line-height:35px; border:1px solid #e8e8e8; border-bottom:0;}
		.pro_right .yes{ margin-top:15px;}
		.assess_gray{ position:relative; width:100px; background-color:#E6E6E6; height:10px; float:left; margin-top:4px;}
		.assess_gray .red{ position:absolute; width:95px; background-color:#CC0814; top:0; left:0; height:10px;}
		.assess_r span{ display:block;}
		.assess_r{ float:left; margin-left:150px;}
		.assess_r em{ float:left; width:75px;}
		.assess_l{ float:left; margin-left:3px;}
		.assess_l span{ float:left; font-size:25px;line-height: 88px; padding-top:6px; color:#E43A3D}
		.assess_l em{ font-size:40px; color:#E43A3D; font-family:"微软雅黑"; font-weight:bold; float:left;line-height:88px;}
		.assess_l  b{ float:left; line-height:88px;}
		.assess_zhong .red{ width:5px;}
		.assess_ca .red{ width:0;}
		.assess_same{ margin:10px 0;}
		.title_far_ol{ float:left; margin-left:20px;}
		.title_far_ol input{ vertical-align:sub;}
		.title_far_ol li{ float:left; margin-right:10px;}
		.pro2_bd_box .bd2{padding:15px; overflow:hidden}
		.comment_lft_hd{width:84px; text-align:center;}
		.comment_rak{ width:75px; height:30px; line-height:30px; text-align:center; background-color:#F56D6D; border-radius:3px; margin-left:6px;}
		.comment_rak a{ color:#fff;}
		.comment_rak a:hover{ color:#fff;}
		.comment_lft_hd_a{width:84px; height:84px; border-radius:50%; overflow:hidden;}
		.comment_lft{ overflow:hidden; width:84px; float:left; margin-right:30px;}
		.comment_lft_hd span{ line-height:24px;}
		.comment_score_img{ background:url(../../images/goods_detail/newicon20140417.png) no-repeat -109px -239px; width:75px; height:14px;} 
		.comment_rgt{ float:left; width:85%;}
		.comment_rgt_item{ overflow:hidden; margin-bottom:10px;}
		.comment_detail{ width:95%; height:83px;}
		.comment_rgt_detail{ color:#999; padding:0 35px; margin-bottom:0;}
		.pro2_bd2_box_ul li{ overflow:hidden;  padding:35px 0; border-bottom:1px solid #e8e8e8;}
		.shop_detail img{ margin:15px 0 0 85px;}
		/*left end*/
		 
		/*商品商品详情页本店搜索*/
		.sp_ser_self{ padding:5px 0;}
		.sp_ser_self_keyword,.sp_ser_self_price,.sp_ser_self_btn{ padding-left:30px; line-height:32px; margin-top:10px;} 
		#sp_ser_self_keyword_txt{ border:1px solid #e8e8e8; line-height:20px; height:20px; padding-left:3px; width:130px;}
		.sp_ser_self_price_txt{ border:1px solid #e8e8e8; line-height:20px;height:20px; padding-left:3px; width:40px;}
		.sp_ser_self_btn button{ background-color:#DFDFDF; margin-left:48px; border-radius:3px; color:#666; height:25px; line-height:25px; cursor:pointer; width:50px; text-align:center;}
		.sp_ser_self_btn{ margin-bottom:20px;}
		/*商品商品详情页本店搜索end*/
		/*商品商品详情页商品分类*/
		.sp_goods_classify{ padding:20px;}
		.sp_goods_classify li{ float:left; width:104px;line-height:25px;}
		.sp_goods_classify li a{ display:block;}
		.sp_goods_classify li a:hover{ text-decoration:none; }
		.nav{ background:none; width:1210px; margin:0 auto;}
		.nav-in{background:url(../../images/ico/nav_bg.jpg) repeat-x scroll 0 0 rgba(0, 0, 0, 0);}
		.person{ position:relative; margin-top:20px;}
		.presonalsort{width:176px; float:left;background:#f5f5f5; border:2px solid #e6e6e6;}/* peneny */
		.presonalsort dl dt{font-size:20px;padding:0 0 0 14px;font-weight:800;height:36px; color:#333; font-family:"Microsoft YaHei"; line-height:36px; margin-bottom:5px;}
		.presonalsort dl dd a{display:block;height:32px;line-height:32px;padding:0 0 0 30px; font-size:16px;}
		.presonalsort dl dd a.current{ font-weight:bold; background:#dcdcdc;}/* peneny */
		.presonalsort dl dd a:hover{ text-decoration:none;font-weight:bold; background:#dcdcdc;color:#333; }/* peneny */
		.gray_bg{ background-color:#fafafa; padding-top:25px; padding-bottom:20px; overflow:hidden;zoom:1;}/* peneny */
		.person_breadline{ margin-top:0;}
		.person_bd{width:1016px; border:2px solid #e6e6e6; height:auto;float:right; padding-bottom:30px;}
		.person_bd h2{ color:#323232;font-size:22px;line-height:50px; height:50px; background-color:#f5f5f5; padding-left:25px;font-family:"Microsoft YaHei"; border-bottom:1px solid #dcdcdc;}/* peneny */
		
		/* Tab切换 */
		.slideTxtBox{ padding:30px 15px 0px 25px;}
		.slideTxtBox .hd{ height:28px; line-height:28px; padding:0 15px; border-bottom:2px solid #E43A3D; font-size:14px; }
		.slideTxtBox .hd ul{ overflow:hidden; zoom:1;  }
		.slideTxtBox .hd ul li{ float:left; }
		.slideTxtBox .hd ul li a{ display:block; padding:0 15px; font-family:"Microsoft YaHei";}
		.slideTxtBox .hd ul li a:hover{ text-decoration:none;  }
		.slideTxtBox .hd ul li.on a{font-weight:bold; background-color:#E43A3D;color:#fff;}
		
		
		
		  
		/*peneny 修改*/
		.BR_10{clear:both;height:10px;line-height:10px;font-size:0px}
		.BR_20{clear:both;height:20px;line-height:20px;font-size:0px}
		.BR_100{clear:both;height:100px;line-height:100px;font-size:0px}
		.BR_line{border-bottom:1px dashed #c8c8c8;clear:both;font-size:0px;height:5px;line-height:0px;margin:5px 20px;}
		.body_01{ background:#fafafa; color:#323232;}    
		.hd_01{ height:26px; line-height:26px; font-size:14px; color:#323232;}    
		.hd_01 em{ margin-right:15px;}
		.hd_01 span,
		.hd_01 a{color:#323232; margin-right:10px;  float:left; display:inline-block; font-size:18px;}
		.hd_01 span{font-weight:bold;}
		.h3_01{ font-size:20px; height:40px; line-height:40px; background:#dcdcdc; text-align:center; margin-bottom:20px; font-weight:400;}
		.presonalsort dl{ margin-bottom:20px;}
		.presonalsort dl dt{ padding-left:20px;}
		.presonalsort dl dd a{ padding-left:40px;}
		.tab_01{ margin:20px 0px 20px 20px; color:#323232;}
		.tab_01 th{ border-bottom:1px solid #969696; height:36px;padding:0px 5px;}
		.tab_01 td{ overflow:hidden;zoom:1; padding:7px 2px;}
		.tab_01 .sp_red{ color:#ff0000;}
		.inp_but_01{border:1px solid #969696;font-size:14px;height:28px;display:inline-block; float:left; overflow:hidden;zoom:1;}
		.inp_but_01 input{ border:1px solid #d9d9d9; height:26px; line-height:26px; padding:0px 3px; color:#969696;width:180px;}
		.tex_01{ width:443px; height:82px;border:1px solid #969696; overflow:hidden;zoom:1; display:inline-block;}
		.tex_01 textarea{ width:431px; height:70px;border:1px solid #d9d9d9; padding:5px; line-height:20px;}
		.reg_sel{ border:1px solid #ccc; padding:3px 0; font-size:14px; color:#666; margin-right:5px;}
		.sp_line{ display:inline-block; margin:0px 5px; float:left; height:30px; line-height:30px; color:#969696}
		.but_01{ display:inline-block; width:100px; height:35px; background:#d43535;-moz-border-radius:2px;-webkit-border-radius:2px;border-radius:2px; font-size:14px; text-align:center; line-height:35px; color:#ffffff; margin:25px 0px 40px 440px;}
		.but_01:hover{ color:#ffffff;}
		.tab_02{ margin:0px 0px 0px 20px;}
		.tab_02 th{ background:#c8c8c8; height:35px; line-height:35px;}
		.tab_02 td{ padding:10px 2px; border-bottom:1px solid #c8c8c8;}
		.tab_02 td.td_01{ padding-right:15px;}
		.a_but02{ background-position:-42px 0px;}
		.a_but03{ background-position:-85px 0px; width:22px;}
		.p_01{ overflow:hidden;zoom:1; height:30px; line-height:30px; color:#969696; margin-left:20px;}
		.person_bd01{ overflow:hidden;zoom:1; margin:20px;}
		.hd_02{ border-bottom:1px solid #d43535; overflow:hidden;zoom:1; padding:7px 0px;}
		.hd_02 li{ height:24px; line-height:24px; width:105px; display:inline-block; float:left; text-align:center;}
		.hd_02 li.hover{ color:#d43535;}
		.box_01{ border-bottom:1px solid #dcdcdc; overflow:hidden;zoom:1; margin-top:10px; color:#646464; padding-bottom:10px;}
		.box_01 .hd{ border-bottom:1px dashed #dcdcdc; height:34px; line-height:34px; overflow:hidden;zoom:1; margin-bottom:8px;}
		.box_01 .hd h3{ float:left; display:inline-block; margin-right:30px;}
		.box_01a{ width:820px; height:46px; margin-left:10px; float:left;}
		.lab_01{ overflow:hidden;zoom:1;  height:20px; line-height:20px; font-weight:bold; color:#323232; font-size:14px; overflow:hidden;zoom:1; display:block;}
		.lab_01 input{float:left; display:inline-block; margin:4px 10px 0px 0px;}
		.lab_01 em{ display:inline-block; float:left; height:20px; line-height:20px; overflow:hidden}
		.box_01a p{ line-height:24px; margin-left:23px;}
		.sp_read{ display:inline-block; float:right; width:80px; height:46px; line-height:46px; text-align:center;}
		.cz_01{ overflow:hidden;zoom:1; margin:15px 0px 0px 10px; float:left;}
		.cz_01 a,
		.cz_01 label{ float:left; display: inline-block; margin-right:10px;}  
		.box_02{overflow:hidden;zoom:1; margin-top:30px;}
		.box_02 .person_bd{ border:none; width:1208px;}
		.box_02 .recharge_title{ overflow:hidden;zoom:1; padding:0px 0px 10px 20px;}
		.box_02 .recharge_title img {display: inline-block;vertical-align: inherit; float:left; margin:0px; padding:0px;}
		.box_02 .recharge_title span{ display:inline-block; margin:9px 0px 0px 10px; float:left;}
		.box_02 .recharge_a_father{ border:1px solid #e6e6e6;}
		.box_02 .recharge_a_father .recharge_a{ height:50px; background:#e6e6e6;}
		.box_02 .recharge_select_a_li{ margin-left:6px;}
		.box_02 .recharge_btn { border-top:1px dashed #c8c8c8;margin:20px 0px 40px; padding-top:30px;}
		.box_02 .blue_ps a{ color:#1b68ac}
		.ceng_01{ position:fixed; background:#000;filter:alpha(opacity=50);-moz-opacity:0.5;-khtml-opacity:0.5;opacity:0.5;overflow:hidden;zoom:1; left:0px; bottom:0px; top:0px; z-index:999; width:100%; height:100%; display:none;}
		.ceng_02{ overflow:hidden;zoom:1; padding:0px; width:640px; border:2px solid #969696; background:#ffffff;-moz-border-radius:5px;-webkit-border-radius:5px; border-radius:5px;}
		.ceng_02 h3{ background:#e8e8e8; height:40px; line-height:40px; padding-left:20px; font-size:18px; color:#323232;}
		.tab_03{ margin:30px 10px 50px 10px;}
		.tab_03 td{ padding:5px 2px; color:#555555;}
		.tab_03 td strong{ color:#323232; display:inline-block; margin-top:3px;}
		.tab_03 td p{ line-height:24px;}
		.bd_ziliao_top_right p a.a_but06{ font-weight:bold; padding-left:0px;}
		.tab_04 th,
		.tab_05 th{ font-size:16px; padding:10px 2px 10px 10px; border-bottom:1px solid #e6e6e6;}
		.tab_04 td{ font-size:16px; padding:20px 2px 20px 10px; font-size:12px;}
		.tab_04 td span{ display:inline-block; height:30px; line-height:30px; float:left; margin-right:50px;}
		.tab_05 td{ padding:10px 2px 10px 10px;}
		.tab_05 td h4{ color:#969696; font-size:14px;}
		.tab_05 td p{ line-height:24px; margin-left:10px;}
		.tab_04 th span.sp_01{ font-size:16px; color:#008d56; font-weight:bold;}
		.tab_06{ margin-top:0px;}
		.tab_06 td p{ overflow:hidden;zoom:1; display:block; text-align:right; width:970px; font-size:16px; color:#555555; line-height:30px;}
		.tab_06 td p .sp_02{ font-size:14px; color:#323232;}
		.tab_06 td p .sp_02 em{ color:#e4393c; margin-right:5px;}
		.tab_06 td p .sp_03{ font-size:12px; color:#969696;}
		.tab_06 td p .sp_03 em{ color:#e4393c; margin-right:5px;}
		.tab_06 td p.p_02{ font-size:18px; color:#e4393c; font-weight:bold;}
		.tab_07{ margin:0px auto;}
		.tab_07 td{ padding:10px 2px;}
		.tab_07 td.td_02 a{ color:#005baf; width:330px; text-align:left; line-height:20px; margin:10px 0px 0px 10px;}
		.tab_07 td.td_02 p{ float:left; text-align:left; margin-left:10px;width:330px;}
		.tab_07 td.td_02 img{ border:1px solid #c8c8c8;}
		.tab_07 td h4 {color:#969696;font-size:14px;margin:0px 0px 10px 8px;}
		.but_02{ display:inline-block; width:86px; height:25px; margin:5px 15px; background:url(../../images/ico/icon_d.gif) repeat-x; line-height:25px; text-align:center; color:#ffffff; font-size:14px;-moz-border-radius:2px;-webkit-border-radius:2px; border-radius:2px;}
		.but_02:hover{ color:#ffffff;} 
		.but_02a{ width:64px; margin:0px 0px 0px 10px;}
		.but_03{ display:inline-block; width:86px; height:25px; margin:5px 15px; background:url(../../images/ico/icon_d.gif) repeat-x; line-height:25px; text-align:center; color:#ffffff; font-size:14px;-moz-border-radius:2px;-webkit-border-radius:2px; border-radius:2px; background-position: 0px -37px; color:#323232;}
		.but_03:hover{ color:#323232;}
		.h3_02{ margin:0px 0px 0px 23px; height:38px; line-height:38px; background:url(../../images/ico/icon_e.gif) no-repeat; padding-left:40px; font-size:18px; font-weight:400; color:#323232;}
		.cz_02{ overflow:hidden;zoom:1; margin:0px auto; width:920px; padding:0px 25px;}
		.cz_02 .other{ margin-top:3px;}
		.a_but07{ display:inline-block;background:#e4393c; width:80px; height:30px;-moz-border-radius:2px;-webkit-border-radius:2px; border-radius:2px; line-height:30px; color:#ffffff; font-size:14px; text-align:center;}
		.ceng_03{ width:600px; padding:80px 0px 60px; text-align:center; font-size:20px;}
		.ceng_03 .a_but07{ margin:20px 10px 0px; font-size:16px;}
		.tab_07 .sp_red{ color:#e4393c;font-weight:bold;}
		.numb_01{ width:100px; height:28px; overflow:hidden;zoom:1; border:1px solid #c8c8c8;}
		.min,
		.add{ width:28px; height:28px; float:left; display:inline-block; background:#f0f0f0;}
		.text_box{ display:inline-block; width:40px; border-left:1px solid #c8c8c8;border-right:1px solid #c8c8c8; height:28px; line-height:28px; text-align:center; float:left;}
		.cz_03{ overflow:hidden;zoom:1; margin:0px 24px 0px 0px;}
		.cz_03 .a_but07{ margin:0px 10px;}
		.p_03{ height:25px; line-height:25px; margin-left:10px;}
		.p_03 span{ color:#e4393c; margin:0px 5px;}
		.p_04{ height:25px; line-height:25px; overflow:hidden;zoom:1;}
		.p_04 span{ margin-left:50px;}
		.p_04 span em{ color:#e4393c; margin:0px 5px;}
		.cz_04{ width:400px;overflow:hidden;zoom:1; text-align:right; margin-right:50px; height:30px; line-height:30px;}
		.cz_04 p{ margin-right:30px; font-size:14px;}
		.cz_04 p label{ font-size:24px; color:#d43535; font-weight:bold;}
		.p05{ line-height:30px; height:30px; font-size:14px; margin-left:30px;}
		.p05 span{ color:#008d56; font-size:26px; font-family:Arial; margin-left:5px;}
		
		.tab_08 td{ padding:10px;}
		.sp_col01{ font-size:20px; color:#008d56;}
		.sp_col02{ font-size:20px; color:#e4393c;}
		.tab_08 td span{ display:block;}
		.list_a{ width:278px; height:268px; overflow:hidden;zoom:1; border:1px solid #ebebeb; margin-left:20px; background:#fafafa;}
		.list_a dt{ margin-bottom:20px;height:40px; line-height:40px; background:#f5f5f5; border-bottom:1px solid #ebebeb; font-size:16px; color:#323232; font-weight:bold; padding:0px 20px;}
		.list_a dd{ margin-left:20px; height:30px; line-height:30px; font-size:14px;}
		.list_a dd span{ width:70px; display:inline-block; float:left; text-align:right;}
		.list_a dd a{ color:#005baf;}
		.box_03{ width:680px; overflow:hidden;zoom:1; margin-left:15px;float:left;}
		.box_03 h3{ background:url(../../images/ico/icon_g.gif) no-repeat scroll 5px 5px; padding-left:40px;height:42px; line-height:42px; font-size:16px;}
		.box_03 h3 span{ color:#0069c4;}
		.tab_09{border-left:1px solid #dcdcdc;border-bottom:1px solid #dcdcdc;font-size:14px;}
		.tab_09 .tr_01 td{ color:#323232; }
		.tab_09 td{ height:44px; line-height:44px;border-right:1px solid #dcdcdc;border-top:1px solid #dcdcdc; color:#005baf;}
		.tab_09 td span{ color:#323232;}
		.Menubox{ border-bottom:1px solid #e4393c; width:976px; overflow:hidden;zoom:1; margin-left:20px;}
		.Menubox ul{ height:32px; line-height:32px; overflow:hidden;zoom:1; cursor: pointer;}
		.Menubox ul li{ background:#dcdcdc; height:32px; line-height:32px; float:left; color:#555555; font-size:14px; width:120px; text-align:center;}
		.Menubox ul li.hover{ color:#ffffff; background:#e4393c;}
		.Contentbox{ width:976px; margin-left:20px; margin-top:10px;}
		.sel_01{ width:80px; height:30px; border:1px solid #969696;}
		.tab_10 th,
		.tab_10 td{ padding:10px; font-size:14px;}
		.tab_10 td span.sp_04{ background:url(../../images/ico/icon_g.gif) no-repeat scroll 0px -48px; display:inline-block; padding-left:35px; height:44px;}
		.tab_10 td span.sp_05{ background:url(../../images/ico/icon_g.gif) no-repeat scroll 0px -93px; display:inline-block; padding-left:35px; height:44px;}
		.tab_10 td span.sp_06{ background:url(../../images/ico/icon_g.gif) no-repeat scroll 0px -135px; display:inline-block; padding-left:35px; height:44px;}
		.tab_10 td p{ line-height:20px;}
		.tab_10 td p a{ color:#005baf;}
		.tab_10 td p a.a_01{ color:#e4393c;}
		.tab_10 td .p_05{ font-size:12px; color:#969696;}
		.tab_10 .td_03 p{ font-size:12px;}
		.ceng_04{ padding:0px; text-align:left;}
		.ceng_04 h3{ background:#f3f3f3; height:32px; line-height:32px; padding:0px 0px;}
		
		/** 商品咨询*/
		
		#goodsInquiry .consult-pub a {
		    font-family: simsun;
		    font-size: 12px;
		    padding: 4px 10px;
		}
		.consult-pub{
			float:right;
		}
		a.css3-btn, a.css3-btn:hover, a.css3-btn:visited {
		    color: #fff;
		}
		a.css3-btn {
		    background-color: #df3033;
		    background-image: linear-gradient(to bottom, #df3033 0px, #e74649 100%);
		    border-radius: 2px;
		}
		.inqueryType{
			float:left;
			width:100px;
		} 
	</style>
	<style type="text/css">
		/* css 重置 */
		*{margin:0; padding:0; list-style:none; }
		body{ background:#fff; font:normal 12px/22px 宋体;  }
		img{ border:0;  }
		a{ text-decoration:none; color:#333;  }
		a:hover{ color:#1974A1;  }
		.js{width:90%; margin:10px auto 0 auto; }
		.js p{ padding:5px 0; font-weight:bold; overflow:hidden;  }
		.js p span{ float:right; }
		.js p span a{ color:#f00; text-decoration:underline;   }
		.js textarea{ height:100px;  width:98%; padding:5px; border:1px solid #ccc; border-top:2px solid #aaa;  border-left:2px solid #aaa;  }

		/* 本例子css */
		.picFocus{ margin:0 auto;  width:367px; border:1px solid #ccc; padding:5px;  position:relative;  overflow:hidden;  zoom:1;   }
		.picFocus .hd{ width:100%; padding-top:5px;  overflow:hidden; }
		.picFocus .hd ul{ margin-right:-5px;  overflow:hidden; zoom:1; }
		.picFocus .hd ul li{ padding-top:5px; float:left;  text-align:center;  }
		.picFocus .hd ul li img{ width:69px; height:65px; border:2px solid #ddd; cursor:pointer; margin-right:5px;   }
		.picFocus .hd ul li.on{ background:url("images/icoUp.gif") no-repeat center 0; }
		.picFocus .hd ul li.on img{ border-color:#f60;  }
		.picFocus .bd li{ vertical-align:middle; }
		.picFocus .bd img{ width:350px; height:350px; display:block;  }
		</style>
</head>
<body>
	<div class=" s_main_in clearfix" >
	    <div class="breadline clearfix">
		    <div class="f-l" id="goodCatHtml"> </div>
		    <div class="f-r"></div>
	   </div>
	</div>
	<div class=" s_main_in">
	  <div class="goods_d_box clearfix">
	      <div class="filmslide">
		       <div class="picFocus">
					<div class="bd" class="bigImg" id="ablumPic"></div>
					<div class="hd" class="smallpic" id="ablumPicShow"></div>
			  </div>
	     </div>
	    <div class="g_d_name">
	       <h2 id="goodTitle"></h2>
	    </div>
	    <div class="g_d_b_son clearfix">
	      <div class="sys_item_spec">
	        <dl class="clearfix iteminfo_parameter iteminfo_parameter_default lh32">
	          <dt>市场价：</dt>
	          <dd> 
	          	<span class="iteminfo_mktprice_red" >￥</span>
	          	<span class="iteminfo_mktprice_red"  id="salePrice"></span> 
	          	<span class="iteminfo_mktprice">
	          		<del class="sys_item_mktprice">￥</del>
	          		<del class="sys_item_mktprice" id="marketPrice"></del>
	          	</span> 
	          </dd>
	        </dl>
	        
	        <dl class="clearfix iteminfo_parameter lh32">
	          <dt>运费：</dt>
	          <dd> <span > 本店铺，每订单固定运费0.0元 </span> </dd>
	        </dl>
	        <div id="goodSpecsDiv" class="nullCss"></div>
	        <dl class="clearfix iteminfo_parameter lh32">
	          <dt>温情提示：</dt>
	          <dd> <span > 特价商品，不能使用优惠券 </span> </dd>
	        </dl>
	        <dl class="clearfix iteminfo_parameter lh32">
	          <dt>库存状态：</dt>
	          <dd> <span > 有货，可当日出货 </span> </dd>
	        </dl>
	        <div class="sp_detail_btn"> 
		        <a href="#" class="buy_btn">立即购买</a> 
		        <a href="#" class="buy_car" id="buy_car">加入购物车</a>
		        <a href="#" class="buy_sel" id="buy_sel">加入收藏</a>
	        </div>
	      </div>
	      <div class="right">
	        <div class="xingxi"> <span class="title  title2">商家信息</span>
		        <div class="shop_detail"> 
		        	<img id="shopLogo" width="86" height="86" /> 
		        </div>
	            <a class="spu_name" id="shopName"></a>
	          <div class="sjxx_1" style="padding-top:0;">
	            <div class="sjxx_1_b">
	              <p>信誉：<img id="shopGradeLogo" width="71" height="14" style=" display:inline-block;"/></p>
	              <p> <em>店铺等级：</em> <span id="shopGradeName"></span></p>
	            </div>
	            <div class="sjxx_1_c">
	              <p> <em>店铺名称：</em> <span id="shopName1"></span></p>
	              <p> <em>地址：</em> <span id="shopAddress"></span></p>
	              <p> <em>联系电话：</em> <span id="shopTel"></span></p>
	              <div class="sjxx_1_c_a"> <a href="#" class="sjxx_kf">联系客服</a> <a href="#" class="sjxx_sc">进入店铺</a> </div>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
	<div class=" s_main_in clearfix">
		<div class="f-l"> 
			<div class="xingxi hotsale"> 
				<span class="title  ">店内搜索</span>
			                     店内搜索
			</div>
		    <div class="xingxi hotsale"> 
		        <span class="title  ">相关分类</span>
		        <div class="bd">
			          <ul id="siblingDiv">
			            
			          </ul>
			    </div>
		    </div> 
		    <div class="xingxi hotsale"> 
		        <span class="title  ">同类其他品牌</span>
		                         同类其他品牌
		    </div> 
		    <div class="xingxi hotsale"> 
		        <span class="title  ">热卖推荐</span>
		                         热卖推荐
		    </div> 
		</div>
		<div id="theTabsCtrl" class="f-r pro_right" style="width:945px;">
			<ul class="dtsummary">
				<li>
					<a href="#tabs-1">商品详情</a>
				</li>
				<li>
					<a href="#tabs-2">规格参数</a>
				</li>
				<li>
					<a href="#tabs-3">包装清单</a>
				</li>
				<li>
					<a href="#tabs-4">售后保障</a>
				</li>
				<li>
					<a href="#tabs-5">评价</a>
				</li>
				<li>
					<a href="#goodsInquiry">商品咨询</a>
				</li>
			</ul>
			<div id="tabs-1">
				<div class="pro2_bd_box">
		               <div class="title_far">
		               	  <h4 class="title"><strong>商品详情</strong></h4>
		               </div>
		               <div class="bd" id="goodIntroHtml">
			              
			           </div>
		        </div>
			</div>
			<div id="tabs-2">
				<div class="pro2_bd_box">
		               <div class="title_far">
		               	<h4 class="title"><strong>规格参数</strong></h4>
		               </div>
			    </div>
			    <div  id="goodGuige">
			    	
			    </div>
		    </div>
		    <div id="tabs-3">
				<div class="pro2_bd_box">
			        <div class="title_far">
			          <h4 class="title"><strong>包装清单</strong></h4>
			        </div>
			        <div class="bd">
			          <ul>
			            <li>
			              <p id="goodPackList"></p>
			            </li>
			          </ul>
			        </div>
		    	</div>
			</div>
			<div id="tabs-4">
				<div class="pro2_bd_box">
		               <div class="title_far">
		               	<h4 class="title"><strong>售后保障</strong></h4>
		               </div>
			    </div>
			</div>
			 <div id="tabs-5">
				<div class="pro2_bd_box">
		               <div class="title_far">
		               	<h4 class="title"><strong>评价</strong></h4>
		               </div>
			    </div>
			</div>
			<div id="goodsInquiry">
				<div class="filter section" style="border:1px solid #e8e8e8;">
					<div class="filter row">
						<div class="group left aligned" style="padding-left: 10px;">
							<h3 class="title"><span>商品咨询</span></h3>
						</div>
						<div class="group right aligned" style="padding-right: 10px;">
							<div class="consult-pub"><a class="css3-btn" href="#askConsolfForm"><span>发表咨询</span></a></div>
						</div>
					</div>
				</div>
				<div style="border:1px solid #e8e8e8;padding: 10px;">
					<span>温馨提示:因厂家更改产品包装、产地或者更换随机附件等没有任何提前通知，且每位咨询者购买情况、提问时间等不同，为此以下回复仅对提问者3天内有效，其他网友仅供参考！若由此给您带来不便请多多谅解，谢谢！</span>
				</div>
				<div style="border:1px solid #e8e8e8;padding-left: 10px;margin-top: 10px;height:30px;">
					<div class="inqueryType"><input id="all" type="radio" name="inqueryType" checked="checked"/><label for="all">全部咨询</label></div>
					<div class="inqueryType"><input id="consolt" type="radio" name="inqueryType"/><label for="consolt">商品咨询</label></div>
					<div class="inqueryType"><input id="stockAndLogistic" type="radio" name="inqueryType"/><label for="stockAndLogistic">库存及配送</label></div>
					<div class="inqueryType"><input id="pays" type="radio" name="inqueryType"/><label for="pays">支付问题</label></div>
					<div class="inqueryType"><input id="invoiceAndWarranty" type="radio" name="inqueryType"/><label for="invoiceAndWarranty">发票及保修</label></div>
					<div class="inqueryType"><input id="promoteAndGift" type="radio" name="inqueryType"/><label for="promoteAndGift">促销及赠品</label></div>
				</div>
				<div style="border:1px solid #e8e8e8;margin-top: 10px;">
					<div style="border-bottom: 1px solid #e8e8e8;">
						<div style="padding-left: 10px;color: #999999;"><label>会员：</label><span class="spacer"></span><span>132****1111</span><span class="spacer"></span><span>2015-04-01 14:21:09</span></div>
						<div style="padding-left:10px;"><label>咨询内容：</label><span>这个有发票吗？</span></div>
						<div style="padding-left:10px;"><label>咨询回复：</label><span>您好，这个可以有</span></div>
					</div>
					<div style="border-bottom: 1px solid #e8e8e8;">
						<div style="padding-left: 10px;color: #999999;"><label>会员：</label><span class="spacer"></span><span>132****1111</span><span class="spacer"></span><span>2015-04-01 14:21:09</span></div>
						<div style="padding-left:10px;"><label>咨询内容：</label><span>发票类型能开成办公用品吗，不要明细的？</span></div>
						<div style="padding-left:10px;"><label>咨询回复：</label><span>您好，这个可以的</span></div>
					</div>
				</div>
				<div id="askConsolfForm" style="border:1px solid #e8e8e8;margin-top: 10px;">
					<div style="padding-left: 10px;"><h4><strong>发表咨询：</strong></h4></div>
					<div style="padding-left: 14px;"><span>声明：您可在购买前对产品包装、颜色、运输、库存等方面进行咨询，我们有专人进行回复！因厂家随时会更改一些产品的包装、颜色、产地等参数，所以该回复仅在当时对提问者有效，其他网友仅供参考！咨询回复的工作时间为：周一至周五，9:00至18:00，请耐心等待工作人员回复。</span></div>
					<div style="padding-left: 10px;">
						<div class="inqueryType"><h4><strong>咨询类型：</strong></h4></div>
						<div class="inqueryType"><input id="consoltForm" type="radio" name="inqueryTypeForm" checked="checked" value="1"/><label for="consoltForm">商品咨询</label></div>
						<div class="inqueryType"><input id="stockAndLogisticForm" type="radio" name="inqueryTypeForm" value="2"/><label for="stockAndLogisticForm">库存及配送</label></div>
						<div class="inqueryType"><input id="paysForm" type="radio" name="inqueryTypeForm" value="3"/><label for="paysForm">支付问题</label></div>
						<div class="inqueryType"><input id="invoiceAndWarrantyForm" type="radio" name="inqueryTypeForm" value="4"/><label for="invoiceAndWarrantyForm">发票及保修</label></div>
						<div><input id="promoteAndGiftForm" type="radio" name="inqueryTypeForm" value="5"/><label for="promoteAndGiftForm">促销及赠品</label></div>
					</div>
					<div style="padding-left: 10px;">
						<div class="inqueryType"><h4><strong>咨询内容：</strong></h4></div>
						<div><textarea id="inqueryContent" class="normal input three half wide " style="height:150px;margin-top: 10px;"></textarea></div>
					</div>
					<div style="padding-left: 110px;">
						<div><button id="btnCousolt" class="normal button">提交</button></div>
					</div>
				</div>
			</div>
		</div>
   </div>
	<jsp:include page="/WEB-INF/jsp/include/foot.jsp" />
	<script type="text/javascript">
	var jqTabsCtrl,goodsId,urlParams,productId;
	var inquiryForm = FormProxy.newOne();
	inquiryForm.addField({
		id : "inqueryContent",
		required : true,
		rules : ["maxLength[250]"]
	});
	//获取商品信息
	function initProductData(productId){
		 data = {id:productId};
		 var ajax = Ajax.post("/product/get/by/id");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				renderProductDate(result.data);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();	
	}
	function initGoodsAndShopDate(goodsId){
		 data = {id:goodsId};
		 var ajax = Ajax.post("/product/goods/by/id");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var date=result.data
				var shopDate=date.shop;
				//商品分类
				$id("goodCatHtml").html(date.catString);
				//店铺信息
				$id("shopLogo").attr("src",shopDate.fileBrowseUrl);
				$id("shopName").html(shopDate.name);
				$id("shopName1").html(shopDate.name);
				$id("shopAddress").html(shopDate.regionName);
				$id("shopTel").html(shopDate.telNo);
				$id("shopGradeLogo").attr("src",shopDate.shopGrade.fileBrowseUrl);
				$id("shopGradeName").html(shopDate.shopGrade.name);
				//包装清单
				var goodDate=date.good;
				$id("goodPackList").html(goodDate.packList);
				//商品介绍
				var goodIntroDate=date.goodIntro;
				$id("goodIntroHtml").html(goodIntroDate.content);
				var sibling=date.sibling;
				var siblingHtml=""
				for(var i=0;i<sibling.length;i++){
					siblingHtml=siblingHtml+"<li><a>"+sibling[i].name+"</a><li>";
				}
				$id("siblingDiv").html(siblingHtml);
				initGoodsAttr(goodsId);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();		
	}
	function initGoodsAttr(goodsId){
		 data = {
				 id:goodsId,
				 catId:3
				 };
		 var ajax = Ajax.post("/product/goodAttrVal/by/catId");
		  ajax.data(data);
		  ajax.done(function(result, jqXhr) {
			if (result.type == "info") {
				var date=result.data;
				var html=""
				for(var x=0;x<date.length;x++){
					html=html+"<div class='yes pro1' style='display:block;'><dl class='clearfix'><dt class='clearfix' ><em class='f-l'><strong>"+date[x].name+"</strong></em>"
					var attrs=date[x].goodsCatAttrs;
					for(var y=0;y<attrs.length;y++){
						html=html+"<dd> <em>"+attrs[y].attrRef.name+"：</em>"
						var attrsValu=attrs[y].attrRef.goodsAttrVals;
						for(var z=0;z<attrsValu.length;z++){
							html=html+attrsValu[z].attrVal+"<span class='chs spaceholder'/>"
						}
						html=html+"</dd>"
					}
					html=html+"</dt></dl></div>"
				}
				$id("goodGuige").html(html);
			} else {
				Layer.warning(result.message);
			}
		});
		ajax.go();		
	}
	function renderProductDate(data){
		//加载图片
		var albumImgs=data.basic.productAlbumImgs;
		var avlumHtml="<ul>"
		var	ablumShowHtml="<ul class='clearfix  sp_det_big_box'>"
		for(var i=0;i<albumImgs.length;i++){
			avlumHtml=avlumHtml+"<li><a target='_blank'><img  src='"+albumImgs[i].fileBrowseUrl+"' /></a></li>"
			ablumShowHtml=ablumShowHtml+"<li><img  src='"+albumImgs[i].fileBrowseUrl+"'/></li>"
		}
		avlumHtml=avlumHtml+"</ul>";
		ablumShowHtml=ablumShowHtml+"</ul>";
		$id("ablumPic").html(avlumHtml);
		$id("ablumPicShow").html(ablumShowHtml);
		$(".picFocus").slide({ mainCell:".bd ul",effect:"left",autoPlay:true });
		//加载规格
		var basic=data.basic;
		var goodSpecs=data.goodSpecs;
		var htmlStr=""
		for(var j=0;j<goodSpecs.length;j++){
			htmlStr=htmlStr+"<dl class='clearfix iteminfo_parameter sys_item_specpara' name='"+goodSpecs[j].specRef.code+"' data-sid='2'><dt>"+goodSpecs[j].specRef.name+"：</dt><dd><ul class='sys_spec_text'>";
			var specsValue=goodSpecs[j].goodsCatSpecItems;
			for(var k=0;k<specsValue.length;k++){
				htmlStr=htmlStr+"<li liName='"+goodSpecs[j].specRef.code+"' liValue='"+specsValue[k].id+"' id='"+goodSpecs[j].specRef.code+specsValue[k].id+"'><a href='javascript:;' title='"+specsValue[k].value+"'>"+specsValue[k].value+"</a><i></i></li>"
			}
			htmlStr=htmlStr+"</ul></dd></dl>"
		}
		$id("goodSpecsDiv").html(htmlStr);
		addCss();
		//选中规格
		    var specVals=data.basic.specVals;
			var specVal="<span class='chs spaceholder'/>"
			for(var l=0;l<specVals.length;l++){
				var liId=specVals[l].refCode+specVals[l].specItemId;
				$id(liId).attr("class","selected");
				specVal=specVal+"<span class='chs spaceholder'/>"+specVals[l].specVal;
			}
			$id("goodTitle").html(basic.goodsName+specVal);
			$id("salePrice").html(basic.salePrice);
			$id("marketPrice").html(basic.marketPrice);
			goodsId=basic.goodsId;
			initGoodsAndShopDate(basic.goodsId);
	}
	
	function addCss(){
		$(".nullCss .sys_item_specpara").each(function(){
			var i=$(this);
			var p=i.find("ul>li");
			p.click(function(){
				if(!!$(this).hasClass("selected")){
					$(this).removeClass("selected");
				}else{
					$(this).addClass("selected").siblings("li").removeClass("selected");
				}
				getNewProduct();
			})
		})	
	}
	function getGoodsCatSpecItem(){
		var list=new Array();
		$(".nullCss .sys_item_specpara").each(function(){
			var i=$(this);
			var p=i.find("ul>li");
			 p.each(function(){
				if(!!$(this).hasClass("selected")){
					 var specRefCode=$(this).get(0).getAttribute("liName");
					 var specsValue=	parseInt($(this).get(0).getAttribute("liValue"));
					 var specdata = {
						specRefCode: specRefCode,
						specsValue:	specsValue
					};
					list.add(specdata);
				}
			}) 
		})
		return list;
	}
	function getNewProduct(){
		 data = {
				goodsId:goodsId,
				specIds:getGoodsCatSpecItem()
				};
		 console.log(data);	
	}
	
	//发表咨询
	function sendInquiry(){
		var validResult = inquiryForm.validateAll();
		if(validResult){
			var content = textGet("inqueryContent");
			var typeFlag = ParseInt(radioGet("inqueryTypeForm"));
			//
			var hintBox = Layer.progress("正在提交...");
			var ajax = Ajax.post("/interact/inquiry/send/do");
			var postData = {
					productId : productId,
					goodsId : goodsId,
					content : content,
					typeFlag : typeFlag		
				};
			ajax.data(postData);
			ajax.done(function(result, jqXhr) {
				if (result.type == "info") {
					var data = result.data;
					if(data){
						Layer.msgSuccess(result.message);
						//初始化表单数据
						textSet("inqueryContent","")
						radioSet("inqueryTypeForm","1");
					}else{
						Layer.msgSuccess(result.message);
					}					
				} else if(result.type == "warn") {
					Layer.msgWarning(result.message);
				}else if(result.type == "fatal"){
					Layer.msgWarning(result.message);
				}else if(result.type = "error"){
					Layer.msgWarning(result.message);
				}
			});
			ajax.always(function() {
				hintBox.hide();
			});
			ajax.go();
		}
	}
	
	$(function(){
		jqTabsCtrl = $("#theTabsCtrl").tabs({});
		urlParams = extractUrlParams();
		productId = decodeURI(urlParams["productId"]);
		initProductData(productId);
		/**按钮绑定事件*/
		$id("btnCousolt").click(sendInquiry);
	});
	</script>
</body>
</html>