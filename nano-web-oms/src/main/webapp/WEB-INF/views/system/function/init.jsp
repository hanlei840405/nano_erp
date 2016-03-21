<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="easyui-layout" style="height: 100%; width: 100%" fit="true">
	<div data-options="region:'west'" style="width: 200px;" id="system_function_layout_menu">
	</div>
	<div data-options="region:'center',border:false">
		<table id="system_function_function_datagrid">
		</table>
	</div>
</div>
<div id="system_function_saveOrUpdate_window" class="easyui-window"
	style="width: 300px; height: 230px"
	data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false,title:'编辑'">
	<form id="system_function_edit_form" action="system/function/save"
		method="post">
		<input type="text" style="display:none;" name="id" id="system_function_id" />
		<input type="text" style="display:none;" name="menuId" id="system_function_menu_id" />
		<table cellpadding="5">
			<tr>
				<td>编码:</td>
				<td><input name="code" id="system_function_code"
						   class="f1 easyui-textbox"/></td>
			</tr>
			<tr>
				<td>名称:</td>
				<td><input name="name" id="system_function_name"
					class="f1 easyui-textbox"/></td>
			</tr>
			<tr>
				<td>URL:</td>
				<td><input name="url" id="system_function_url"
						   class="f1 easyui-textbox"/></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="systemFunctionSubmitForm()">提交</a> <a href="javascript:void(0)"
				class="easyui-linkbutton" onclick="clearForm('system_function_edit_form')">重置</a>
		</div>
	</form>
</div>
<script>
	$(function() {
		systemFunctionInit();
	});
</script>