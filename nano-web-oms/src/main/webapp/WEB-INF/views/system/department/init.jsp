<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="easyui-layout" style="height: 100%; width: 100%" fit="true">
	<div data-options="region:'center',border:false">
		<table id="system_department_department_tree">
		</table>
	</div>
</div>
<div id="system_department_saveOrUpdate_window" class="easyui-window"
	style="width: 300px; height: 220px"
	data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
	<form id="system_department_edit_form" action="system/department/save"
		method="post">
		<input type="text" style="display:none;" name="id" id="system_department_id" />
		<table cellpadding="5">
			<tr>
				<td>上级部门:</td>
				<td><input name="parentId" id="system_department_parent_id"
					class="easyui-combotree"
					data-options="url:'system/department/queryDepartments',method:'get'" /></td>
			</tr>
			<tr>
				<td>部门名称:</td>
				<td><input name="name" id="system_department_name"
					class="f1 easyui-textbox"/></td>
			</tr>
			<tr>
				<td>排序:</td>
				<td><input name="position" id="system_department_position"
					class="easyui-numberspinner"/></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="systemDepartmentSubmitForm()">提交</a> <a href="javascript:void(0)"
				class="easyui-linkbutton" onclick="clearForm('system_department_edit_form')">重置</a>
		</div>
	</form>
</div>
<script>
	$(function() {
		systemDepartmentInit();
	});
</script>