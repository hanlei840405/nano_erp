function systemPrivilegeInit() {
    $('#system_privilege_privilege_datagrid').datagrid({
        title: '权限信息',
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
        url: 'system/privilege/queryPrivileges',
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
                $('#system_privilege_saveOrUpdate_window').window('open');
                $('#system_privilege_edit_form').form('reset');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#system_privilege_privilege_datagrid').datagrid('getSelected');
                $('#system_privilege_edit_form').form('reset');
                $('#system_privilege_id').val(selected.id);
                $('#system_privilege_code').textbox('setValue', selected.code);
                $('#system_privilege_name').textbox('setValue', selected.name);
                $('#system_privilege_category').combobox('setValue',selected.category);
                var rec = {value:selected.category};
                systemPrivilegeCategorySelectItem(rec);
                $('#system_privilege_relatedId').combotree('setValues',selected.relatedIds);
                $('#system_privilege_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#system_privilege_privilege_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('system/privilege/delete', array, function () {
                            $('#system_privilege_privilege_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var system_privilege_privilege_datagrid_pagination = $('#system_privilege_privilege_datagrid').datagrid('getPager');
    $(system_privilege_privilege_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#system_privilege_privilege_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function systemPrivilegeSubmitForm() {
	console.log($('#system_privilege_edit_form'));
    $('#system_privilege_edit_form').form('submit', {
        success: function (data) {
            data = $.parseJSON(data);
            $('#system_privilege_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#system_privilege_privilege_datagrid').datagrid('reload');
        }
    });
}

function systemPrivilegeCategorySelectItem(rec) {
    if (rec.value == 'MENU') {
        $('#system_privilege_relatedId').combotree({
            url: 'system/menu/queryAllMenusAndFunctions?initFunctions=false',
            valueField:'id',
            textField:'text',
            panelHeight:'auto',
            required: true,
            multiple:true
        });
    } else if (rec.value == 'FUNCTION') {
        $('#system_privilege_relatedId').combotree({
            url: 'system/menu/queryAllMenusAndFunctions?initFunctions=true',
            valueField:'id',
            textField:'text',
            panelHeight:'auto',
            required: true,
            multiple:true,
            onlyLeafCheck:true
        });
    } else {
        $('#system_privilege_relatedId').combotree({
            url: 'get_data.php',
            valueField:'id',
            textField:'text',
            panelHeight:'auto',
            required: true,
            multiple:true,
            onlyLeafCheck:true
        });
    }
}