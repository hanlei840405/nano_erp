<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<div class="easyui-layout" style="height: 100%; width: 100%" fit="true">
	<div data-options="region:'center',border:false">
		<table id="system_group_group_tree"/>
	</div>
	<div data-options="region:'east',border:false" style="width: 350px; padding: 10px;">
		<p>【组】具有继承功能，即下级组具有上级组的所有功能。</p>
	</div>
</div>
<div id="system_group_saveOrUpdate_window" class="easyui-window"
	 style="width: 50%; height: 400px"
	 data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false,title:'编辑'">
	<div class="easyui-layout" style="height:400px;" fit="true">
		<div data-options="region:'north'" style="min-height: 50px">
			<form id="system_group_edit_form" action="system/group/save" method="post">
				<input type="text" style="display:none;" name="id" id="system_group_id"/>
				<table cellpadding="5">
					<tr>
						<td>上级组:</td>
						<td>
							<input name="parentId" id="system_group_parent_id" class="easyui-combotree"
								   data-options="url:'system/group/queryGroups',method:'get',onSelect : systemGroupParentSelectItem"/>
						</td>
						<td>名称:</td>
						<td>
							<input name="name" id="system_group_name" class="f1 easyui-textbox"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center'">
			<div class="easyui-tabs" data-options="region:'west',fit:true,border:false">
				<div title="角色">
					<table id="system_group_role_datagrid"/>
				</div>
				<div title="人员">
					<ul class="easyui-tree" id="system_group_user_tree"></ul>
				</div>
			</div>
		</div>
		<div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="systemGroupSubmitForm()">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('system_group_edit_form')">重置</a>
		</div>
	</div>
</div>
<script>
	$(function () {
		systemGroupInit();
	});
</script>