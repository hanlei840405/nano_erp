function systemGroupInit() {
	$('#system_group_group_tree').treegrid({
		url : 'system/group/queryGroups',
		idField : 'id',
		treeField : 'name',
		fit : true,
		columns : [ [ {
			title : '名称',
			field : 'name',
			width : 800
		} ] ],
		toolbar : [ {
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				$('#system_group_edit_form').form('reset');
				$('#system_group_saveOrUpdate_window').window('open');
				$('#system_group_parent_id').combotree('reload');
				systemGroupShowRolesAndUsers(null);
			}
		}, {
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
				var selected = $('#system_group_group_tree').treegrid('getSelected');
				$('#system_group_edit_form').form('reset');
				$('#system_group_id').val(selected.id);
				$('#system_group_code').textbox('setValue',selected.code);
				$('#system_group_name').textbox('setValue',selected.name);
				if(selected.parentId != null){
					$('#system_group_parent_id').combotree('setValue', selected.parentId);
				}
				$('#system_group_saveOrUpdate_window').window('open');
				$('#system_group_parent_id').combotree('reload');
				systemGroupShowRolesAndUsers(selected.id);
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var selections = $('#system_group_group_tree').datagrid('getSelections');
				if(selections.length == 0){
					alert('请选择被删除的数据');
					return false;
				}
				var array = new Array();
				for(var i = 0;i < selections.length;i++){
					array.push(selections[i].id);
				}
				$.messager.confirm('Confirm', '确定删除?', function(r){
					if (r){
						deleteSelectedRows('system/group/delete',array,function(){
							$('#system_group_group_tree').treegrid('reload');
						});
					}
				});
			}
		} ]
	});
}
/**
 * ajax方式提交表单
 */
function systemGroupSubmitForm() {
	var roleSelections = $('#system_group_role_datagrid').datagrid('getSelections');
	var roles = new Array();
	for(var i = 0;i < roleSelections.length;i++){
		roles.push(roleSelections[i].id);
	}
	var userCheckeds = $('#system_group_user_tree').tree('getChecked');
	var users = new Array();
	for(var i = 0;i < userCheckeds.length;i++){
		users.push(userCheckeds[i].id);
	}
	$('#system_group_edit_form').form('submit', {
		onSubmit: function (param) {
			param.roles = JSON.stringify(roles);
			param.users = JSON.stringify(users);
		},
		success : function(data) {
			data = $.parseJSON(data);
			$('#system_group_saveOrUpdate_window').window('close');
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
				$('#system_group_group_tree').treegrid('reload');
			} else {
				$.notify("保存失败", "error");
			}
		}
	});
}

function systemGroupShowRolesAndUsers(groupId) {
	var dg = $('#system_group_role_datagrid').datagrid({
		border: false,
		iconCls: 'icon-edit',//图标
		height: 'auto',
		nowrap: false,
		striped: true,
		border: false,
		ctrlSelect: true,
		collapsible: false,//是否可折叠的
		fit: true,//自动大小
		selectOnCheck: true,
		checkOnSelect: true,
		idField: 'id',
		queryParams: {groupId: groupId},
		url: 'system/role/queryRolesForPlugin',
		rownumbers: true,
		frozenColumns:[[
			{field:'ck',checkbox:true}
		]],
		columns: [[
			{field: 'code', title: '编号', width: 230, align: 'center'},
			{field: 'name', title: '名称', width: 230, align: 'center'}
		]],
		onLoadSuccess : function(data){
        	$('#system_group_role_datagrid').datagrid('clearChecked');
			for(var i =0;i < data.rows.length;i++){
				var checked = data.rows[i].attributes['checked'];
				if(checked){
					dg.datagrid('selectRow',i);
				}
			}
		}
	});
	$('#system_group_user_tree').tree({
		url : 'system/user/queryUsersForPlugin',
		queryParams: {groupId: groupId},
		checkbox : true,
		onlyLeafCheck : true
	});
}

function systemGroupParentSelectItem(rec){
	systemGroupShowRolesAndUsers(rec.id);
}