<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="base_member_layout" class="easyui-layout"
	style="height: auto; width: 100%" fit="true">
	<div data-options="region:'north',border:false">
		<table cellpadding="5">
			<tr>
				<td>姓名:</td>
				<td><input id="base_search_member_realName"
					class="f1 easyui-textbox" /></td>
				<td>名称:</td>
				<td><input id="base_search_member_nick"
					class="f1 easyui-textbox" /></td>
				<td>账号:</td>
				<td><input id="base_search_member_account"
					class="f1 easyui-textbox" /></td>
				<td>电子邮箱:</td>
				<td><input id="base_search_member_mail"
					class="f1 easyui-textbox" /></td>
			</tr>
			<tr>
				<td>电话:</td>
				<td><input id="base_search_member_telephone"
					class="f1 easyui-textbox" /></td>
				<td>手机:</td>
				<td><input id="base_search_member_mobile"
					class="f1 easyui-textbox" /></td>
				<td>QQ:</td>
				<td><input id="base_search_member_qq" class="f1 easyui-textbox" /></td>
				<td>MSN:</td>
				<td><input id="base_search_member_msn"
					class="f1 easyui-textbox" /></td>
				<td><a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="baseMemberSearchForm()">查询</a></td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false">
		<table id="base_member_member_datagrid">
		</table>
	</div>
</div>
<div id="base_member_saveOrUpdate_window" class="easyui-window"
	style="width: 35%; min-width: 200px; height: 270px"
	data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
	<div class="easyui-layout" style="height: 270px;" fit="true">
		<div data-options="region:'center'" style="min-height: 50px">
			<form id="base_member_edit_form" action="base/member/save"
				method="post">
				<input type="text" style="display: none;" name="id"
					id="base_member_id" />
				<table cellpadding="5">
					<tr>
						<td>姓名:</td>
						<td><input name="realName" id="base_member_realName"
							class="f1 easyui-textbox" /></td>
						<td>昵称:</td>
						<td><input name="nick" id="base_member_nick"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>账号:</td>
						<td><input name="account" id="base_member_account"
							class="f1 easyui-textbox" /></td>
						<td>电子邮箱:</td>
						<td><input name="mail" id="base_member_mail"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>电话:</td>
						<td><input name="telephone" id="base_member_telephone"
							class="f1 easyui-textbox" /></td>
						<td>手机:</td>
						<td><input name="mobile" id="base_member_mobile"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>QQ:</td>
						<td><input name="qq" id="base_member_qq"
							class="f1 easyui-textbox" /></td>
						<td>MSN:</td>
						<td><input name="msn" id="base_member_msn"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>地址:</td>
						<td colspan="3"><input name="address"
							id="base_member_address" class="f1 easyui-textbox" style="width:100%"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south'"
			style="text-align: center; padding: 5px; height: 40px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="baseMemberSubmitForm()">提交</a> <a href="javascript:void(0)"
				class="easyui-linkbutton"
				onclick="clearForm('base_member_edit_form')">重置</a>
		</div>
	</div>
</div>
<script>
	$(function() {
		baseMemberInit();
	});
</script>