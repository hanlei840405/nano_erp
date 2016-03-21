function systemRoleInit() {
    $('#system_role_role_datagrid').datagrid({
        title: '角色信息',
        border: false,
        iconCls: 'icon-edit',//图标
        width: 700,
        height: 'auto',
        nowrap: false,
        striped: true,
        border: true,
        ctrlSelect: true,
        collapsible: false,//是否可折叠的
        fit: true,//自动大小
        selectOnCheck: true,
        checkOnSelect: true,
        idField: 'id',
        url: 'system/role/queryRoles',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [5, 10, 15],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'code', title: '编号', width: 200, align: 'center'},
            {field: 'name', title: '名称', width: 200, align: 'center'}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#system_role_saveOrUpdate_window').window('open');
                $('#system_role_edit_form').form('reset');
                systemRoleShowPriliges(null);
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#system_role_role_datagrid').datagrid('getSelected');
                $('#system_role_id').val(selected.id);
                $('#system_role_code').textbox('setValue', selected.code);
                $('#system_role_name').textbox('setValue', selected.name);
                systemRoleShowPriliges(selected.id);
                $('#system_role_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#system_role_role_datagrid').datagrid('getSelections');
                if (selections.length == 0) {
                    alert('请选择被删除的数据');
                    return false;
                }
                var array = new Array();
                for (var i = 0; i < selections.length; i++) {
                    array.push(selections[i].id);
                }
                $.messager.confirm('Confirm', '确定删除?', function (r) {
                    if (r) {
                        deleteSelectedRows('system/role/delete', array, function () {
                            $('#system_role_role_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var systemrole_role_datagrid_pagination = $('#system_role_role_datagrid').datagrid('getPager');
    $(systemrole_role_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#system_role_role_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function systemRoleSubmitForm() {
    var selections = $('#system_role_privilege_datagrid').datagrid('getSelections');
    var privileges = new Array();
    for(var i = 0;i < selections.length;i++){
        privileges.push(selections[i].id);
    }
    $('#system_role_edit_form').form('submit', {
        onSubmit: function (param) {
            param.privileges = JSON.stringify(privileges);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#system_role_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#system_role_role_datagrid').datagrid('reload');
        }
    });
}

function systemRoleShowPriliges(roleId) {
    var dg = $('#system_role_privilege_datagrid').datagrid({
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
        queryParams: {roleId: roleId},
        url: 'system/privilege/queryPrivilegesForPlugin',
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        columns: [[
            {field: 'code', title: '编号', width: 230, align: 'center'},
            {field: 'name', title: '名称', width: 230, align: 'center'}
        ]],
        onLoadSuccess : function(data){
        	$('#system_role_privilege_datagrid').datagrid('clearChecked');
            for(var i =0;i < data.rows.length;i++){
                var checked = data.rows[i].attributes['checked'];
                if(checked){
                    dg.datagrid('selectRow',i);
                }
            }
        }
    });
}