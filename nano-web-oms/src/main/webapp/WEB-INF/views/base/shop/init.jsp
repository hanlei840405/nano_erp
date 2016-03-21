<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_shop_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>编号:</td>
                <td><input id="base_search_shop_code" class="f1 easyui-textbox"/></td>
                <td>名称:</td>
                <td><input id="base_search_shop_name" class="f1 easyui-textbox"/></td>
                <td>平台:</td>
                <td><input id="base_search_shop_platform" class="f1 easyui-combobox" data-options="url:'base/platform/queryPlatformsForPlugin',valueField:'id',textField:'name',panelHeight : 'auto'"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseShopSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_shop_shop_datagrid">
        </table>
    </div>
</div>
<div id="base_shop_saveOrUpdate_window" class="easyui-window"
     style="width: 600px;min-width: 400px; height: 560px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:520px;">
    	<div class="easyui-layout" data-options="region:'center',border:false" style="width:100%" fit="true">
	        <div data-options="region:'center'" style="width:200px">
	            <form id="base_shop_edit_form" action="base/shop/save" method="post">
	                <input type="text" style="display:none;" name="id" id="base_shop_id"/>
	                <table cellpadding="4">
	                    <tr>
	                        <td>编号:</td>
	                        <td><input name="code" id="base_shop_code" class="f1 easyui-textbox"/></td>
	                    </tr>
	                    <tr>
	                        <td>名称:</td>
	                        <td><input name="name" id="base_shop_name" class="f1 easyui-textbox"/></td>
	                    </tr>
	                    <tr>
	                        <td>平台:</td>
	                        <td>
		                        <input name="platformId" id="base_shop_platform_id" class="f1 easyui-combobox"/>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td>状态:</td>
			                <td>
			                    <select name="status" id="base_shop_status" class="f1 easyui-combobox" style="width:150px" data-options="panelHeight : 'auto'">
			                        <option value=""></option>
			                        <option value="1">启用</option>
			                        <option value="0">停用</option>
			                    </select>
			                </td>
	                    </tr>
	                    <tr>
	                        <td>备注:</td>
	                        <td><input name="memo" id="base_shop_memo" class="f1 easyui-textbox" data-options="multiline:true" style="height:60px"/></td>
	                    </tr>
	                </table>
	            </form>
	        </div>
			<div data-options="region:'east',title:'类目',border:false" style="width:300px">
				<ul class="easyui-tree" id="base_shop_category_tree"></ul>
			</div>
	        <div data-options="region:'south',border:false" style="height:300px">
	            <table id="base_shop_property_datagrid"/>
	        </div>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseShopSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_shop_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseShopInit();
    });
</script>