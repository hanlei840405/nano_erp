function systemUserInit() {
	$('#system_user_department_tree').tree({
		animate: true,
		onClick: function (node) {
			$('#system_user_user_datagrid').datagrid('load',{
				departmentId: node.id
			});
		}
	});
	$('#system_user_user_datagrid').datagrid({
		title:'用户信息',
		border:false,
		iconCls:'icon-edit',//图标
		width: 700,
		height: 'auto',
		nowrap: false,
		striped: true,
		border: true,
		ctrlSelect : true,
		collapsible:false,//是否可折叠的
		fit: true,//自动大小
		selectOnCheck: true,
		checkOnSelect: true,
		idField:'id',
		url: 'system/user/queryUsers',
		pagination:true,
		rownumbers:true,
		singleSelect:false,
		columns: [[
			{ field:'ck',checkbox:true },
			{field: 'code', title: '工号', width: 100,align:'center'},
			{field: 'name', title: '姓名', width: 100,align:'center'},
			{field: 'departmentName', title: '部门', width: 100,align:'center'},
			{field: 'locked', title: '锁定', width: 100,align:'center',formatter:function(value,row,index){
				if(value + "" == "true"){
					return '是';
				}
				return '否'
			}}
		]],
		toolbar: [{
			text : '新增',
			iconCls: 'icon-add',
			handler: function(){
				$('#system_user_edit_form').form('reset');
				systemUserShowRoles(null);
				$('#system_user_saveOrUpdate_window').window('open');
			}
		},'-',{
			text : '修改',
			iconCls: 'icon-edit',
			handler: function(){
				var selected = $('#system_user_user_datagrid').datagrid('getSelected');
				$('#system_user_edit_form').form('reset');
				$('#system_user_id').val(selected.id);
				$('#system_user_department_id').combotree('setValue',selected.departmentId);
				$('#system_user_code').textbox('setValue', selected.code);
				$('#system_user_name').textbox('setValue', selected.name);
				$('#system_user_locked').combobox('select',selected.locked);
				systemUserShowRoles(selected.id);
				$('#system_user_saveOrUpdate_window').window('open');
			}
		},'-',{
			text : '删除',
			iconCls: 'icon-remove',
			handler: function(){
				var selections = $('#system_user_user_datagrid').datagrid('getSelections');
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
						deleteSelectedRows('system/user/delete',array,function(){
							$('#system_user_user_datagrid').datagrid('reload');
						});
					}
				});
			}
		}]

	});
	//设置分页控件
	var system_user_user_datagrid_pagination = $('#system_user_user_datagrid').datagrid('getPager');
	$(system_user_user_datagrid_pagination).pagination({
		pageSize: 10,//每页显示的记录条数，默认为10
		pageList: [5,10,15],//可以设置每页记录条数的列表
		beforePageText: '第',//页数文本框前显示的汉字
		afterPageText: '页    共 {pages} 页',
		displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
	$('#system_user_user_datagrid').datagrid('enableFilter',[
		{
			field:'locked',
			type:'combobox',
			options:{
				panelHeight:'auto',
				data:[{value:'',text:'All'},{value:'true',text:'是'},{value:'false',text:'否'}],
				onChange:function(value){
					if (value == ''){
						$('#system_user_user_datagrid').datagrid('removeFilterRule', 'locked');
					} else {
						$('#system_user_user_datagrid').datagrid('addFilterRule', {
							field: 'locked',
							op: 'equal',
							value: value
						});
					}
					$('#system_user_user_datagrid').datagrid('doFilter');
				}
			}
		}
	]);
}
/**
 * ajax方式提交表单
 */
function systemUserSubmitForm() {
	var selects = $('#system_user_role_datagrid').datagrid('getSelections');
	var array = new Array();
	for(var i = 0;i < selects.length;i++){
		array.push(selects[i].id);
	}
	$('#system_user_role_ids').val(array);
	$('#system_user_edit_form').form('submit', {
		success : function(data) {
			data = $.parseJSON(data);
			$('#system_user_saveOrUpdate_window').window('close');
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
			} else {
				$.notify("保存失败", "error");
			}
			$('#system_user_user_datagrid').datagrid('reload');
		}
	});
}

function systemUserShowRoles(userId) {
	var dg = $('#system_user_role_datagrid').datagrid({
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
		queryParams: {userId: userId},
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
        	$('#system_user_role_datagrid').datagrid('clearChecked');
			for(var i =0;i < data.rows.length;i++){
				var checked = data.rows[i].attributes['checked'];
				if(checked){
					dg.datagrid('selectRow',i);
				}
			}
		}
	});
}