<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>Home</title>
<link rel="shortcut icon" href="resources/favicon.ico"/>
<link rel="bookmark" href="resources/favicon.ico"/>
<link rel="stylesheet" type="text/css"
	href="resources/easyui/themes/ui-pepper-grinder/easyui.css">
<link rel="stylesheet" type="text/css" href="resources/easyui/color.css">
<link rel="stylesheet" type="text/css" href="resources/easyui/icon.css">
<style type="text/css">
* {
	font-size: 12px;
}

.icon-filter {
	background:
		url('../../resources/easyui/extends/datagrid-filter/filter.png')
		no-repeat center center;
}
</style>
<script type="text/javascript" src="resources/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="resources/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="resources/easyui/extends/datagrid-filter/datagrid-filter.js"></script>
<script type="text/javascript"
	src="resources/easyui/extends/datagridview/datagrid-detailview.js"></script>
<script type="text/javascript" src="resources/notify/notify.js"></script>
<script type="text/javascript" src="resources/app/app.js"></script>
<script type="text/javascript" src="resources/app/formUtil.js"></script>
<script type="text/javascript" src="resources/app/system/user/user.js"></script>
<script type="text/javascript"
	src="resources/app/system/department/department.js"></script>
<script type="text/javascript"
	src="resources/app/system/privilege/privilege.js"></script>
<script type="text/javascript" src="resources/app/system/menu/menu.js"></script>
<script type="text/javascript" src="resources/app/system/role/role.js"></script>
<script type="text/javascript" src="resources/app/system/group/group.js"></script>
<script type="text/javascript" src="resources/app/base/brand/brand.js"></script>
<script type="text/javascript"
	src="resources/app/base/category/category.js"></script>
<script type="text/javascript"
	src="resources/app/base/dictionary/dictionary.js"></script>
<script type="text/javascript"
	src="resources/app/base/express/express.js"></script>
<script type="text/javascript" src="resources/app/base/mark/mark.js"></script>
<script type="text/javascript" src="resources/app/base/member/member.js"></script>
<script type="text/javascript" src="resources/app/base/note/note.js"></script>
<script type="text/javascript"
	src="resources/app/base/platform/platform.js"></script>
<script type="text/javascript"
	src="resources/app/base/contact/contact.js"></script>
<script type="text/javascript"
	src="resources/app/base/product/product.js"></script>
<script type="text/javascript"
	src="resources/app/base/region/region.js"></script>
<script type="text/javascript"
	src="resources/app/base/shop/shop.js"></script>
<script type="text/javascript"
	src="resources/app/base/sms/sms.js"></script>
<script type="text/javascript"
	src="resources/app/base/smsTemplate/smsTemplate.js"></script>
<script type="text/javascript"
	src="resources/app/base/stock/stock.js"></script>
<script type="text/javascript"
	src="resources/app/base/stock/storage.js"></script>
<script type="text/javascript"
	src="resources/app/base/warehouse/warehouse.js"></script>
<script type="text/javascript"
	src="resources/app/productmapping/taobao/taobao.js"></script>
<script type="text/javascript"
	src="resources/app/productmapping/jingdong/jingdong.js"></script>
<script type="text/javascript"
	src="resources/app/productmapping/dangdang/dangdang.js"></script>
</head>
<body class="easyui-layout" fit="true">
	<div data-options="region:'north',title:'-'" style="height: 75px">north
		region</div>
	<div data-options="region:'west',title:'导航'"
		style="width: 180px; padding: 10px;">
		<ul class="easyui-tree"
			data-options="url:'system/menu/queryMenuByPrivilege',method:'get',animate:true,onClick:addTab"></ul>
	</div>
	<div id="center" data-options="region:'center',border:false">
		<div id="tt" class="easyui-tabs"
			data-options="plain:true,narrow:true,pill:true,border:false,fit:true">
			<div title="HOME" style="padding: 10px"></div>
		</div>
	</div>
	<div data-options="region:'south',border:false"
		style="height: 20px; background: #A9FACD; padding: 2px;">south
		region</div>
</body>
</html>
