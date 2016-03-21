function systemDepartmentInit() {
	$('#system_department_department_tree').treegrid({
		url : 'system/department/queryDepartments',
		idField : 'id',
		treeField : 'name',
		fit : true,
		columns : [ [ {
			title : '名称',
			field : 'name',
			width : 400
		}, {
			title : '排序',
			field : 'position',
			width : 50
		} ] ],
		toolbar : [ {
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				$('#system_department_edit_form').form('reset');
				$('#system_department_saveOrUpdate_window').window('open');
				$('#system_department_parent_id').combotree('reload');
			}
		}, {
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
				var selected = $('#system_department_department_tree').treegrid('getSelected');
				$('#system_department_edit_form').form('reset');
				$('#system_department_id').val(selected.id);
				$('#system_department_name').textbox('setValue',selected.name);
				if(selected.parentId != null){
					$('#system_department_parent_id').combotree('setValue', selected.parentId);
				}
				$('#system_department_position').numberspinner('setValue', selected.position);
				$('#system_department_saveOrUpdate_window').window('open');
				$('#system_department_parent_id').combotree('reload');
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var selections = $('#system_department_department_tree').treegrid('getSelections');
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
						deleteSelectedRows('system/department/delete',array,function(){
							$('#system_department_department_tree').treegrid('reload');
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
function systemDepartmentSubmitForm() {
	$('#system_department_edit_form').form('submit', {
		success : function(data) {
			data = $.parseJSON(data);
			$('#system_department_saveOrUpdate_window').window('close');
			console.log(data.yesOrNo);
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
				$('#system_user_user_datagrid').treegrid('reload');
			} else {
				$.notify("保存失败", "error");
			}
		}
	});
}
