<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="base_note_layout" class="easyui-layout"
	style="height: auto; width: 100%" fit="true">
	<div data-options="region:'center',border:false">
		<table id="base_note_note_datagrid">
		</table>
	</div>
</div>
<div id="base_note_saveOrUpdate_window" class="easyui-window"
	style="width: 20%; min-width: 200px; height: 250px"
	data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
	<div class="easyui-layout" style="height: 250px;" fit="true">
		<div data-options="region:'center'" style="min-height: 50px">
			<form id="base_note_edit_form" action="base/note/save"
				method="post">
				<input type="text" style="display: none;" name="id"
					id="base_note_id" />
				<table cellpadding="5">
					<tr>
						<td>内容:</td>
						<td><input name="content" id="base_note_content"
							class="f1 easyui-textbox" data-options="multiline:true" style="height:100px;width:180px"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south'"
			style="text-align: center; padding: 5px; height: 40px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="baseNoteSubmitForm()">提交</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				onclick="clearForm('base_note_edit_form')">重置</a>
		</div>
	</div>
</div>
<script>
	$(function() {
		baseNoteInit();
	});
</script>