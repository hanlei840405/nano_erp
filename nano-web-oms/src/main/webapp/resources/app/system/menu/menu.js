function systemMenuInit() {
    var tg = $('#system_menu_menu_tree').treegrid({
        url: 'system/menu/queryAllMenusAndFunctions?initFunctions=false',
        idField: 'id',
        treeField: 'text',
        fit: true,
        columns: [[{
            title: '名称',
            field: 'text',
            width: 200
        }, {
            title: '排序',
            field: 'position',
            width: 50
        }]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#system_menu_edit_form').form('reset');
                if(!$('#system_menu_function_datagrid')){
                    $('#system_menu_function_datagrid').datagrid('loadData',[]);
                }
                $('#system_menu_saveOrUpdate_window').window({
                    onClose: function () {
                        for (var i = $('#system_menu_function_datagrid').datagrid("getData").total - 1; i >= 0; i--) {
                            $('#system_menu_function_datagrid').datagrid("deleteRow", i);
                        }
                    }
                });
                $('#system_menu_saveOrUpdate_window').window('open');
                $('#system_menu_parent_id').combotree('reload');
                systemMenuShowFunctions();
            }
        }, {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#system_menu_menu_tree').treegrid('getSelected');
                $('#system_menu_edit_form').form('reset');
                $('#system_menu_id').val(selected.id);
                $('#system_menu_name').textbox('setValue', selected.text);
                if (selected.parentId != null) {
                    $('#system_menu_parent_id').combotree('setValue', selected.parentId);
                }
                $('#system_menu_position').numberspinner('setValue', selected.position);
                $('#system_menu_saveOrUpdate_window').window('open');
                $('#system_menu_parent_id').combotree('reload');

                systemMenuShowFunctions(selected.id);
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#system_menu_menu_tree').datagrid('getSelections');
                if (selections.length == 0) {
                    alert('请选择被删除的数据');
                    return false;
                }
                var array = new Array();
                for (var i = 0; i < selections.length; i++) {
                    console.log(selections[i]);
                    array.push(selections[i].id);
                }
                $.messager.confirm('Confirm', '确定删除?', function (r) {
                    if (r) {
                        deleteSelectedRows('system/menu/delete', array, function () {
                            $('#system_menu_menu_tree').treegrid('reload');
                        });
                    }
                });
            }
        }],
        rownumbers: true,
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 30],//可以设置每页记录条数的列表
        onBeforeLoad: function (row, param) {
            if (row != null) {    // load top level rows
                param.parentId = row.id;    // set id=0, indicate to load new page rows
            }
        }
    });


    //设置分页控件
    var p = tg.datagrid('getPager');
    $(p).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
}
/**
 * ajax方式提交表单
 */
function systemMenuSubmitForm() {
    var editIndex = $('#system_menu_function_datagrid').datagrid('getRowIndex',$('#system_menu_function_datagrid').datagrid('getSelected'));
    $('#system_menu_function_datagrid').datagrid('endEdit',editIndex);
    var insertedChanges = $('#system_menu_function_datagrid').datagrid('getChanges','inserted');
    var updatedChanges = $('#system_menu_function_datagrid').datagrid('getChanges','updated');
    var deletedChanges = $('#system_menu_function_datagrid').datagrid('getChanges','deleted');
    var savedArray = new Array();
    var deletedArray = new Array();
    $('#system_menu_function_datagrid').datagrid('acceptChanges');
    for (var i = 0; i < insertedChanges.length; i++) {
        if ($('#system_menu_function_datagrid').datagrid('validateRow', i)) {
            savedArray.push(insertedChanges[i]);
        } else {
            return false;
        }
    }
    for (var i = 0; i < updatedChanges.length; i++) {
        if ($('#system_menu_function_datagrid').datagrid('validateRow', i)) {
            savedArray.push(updatedChanges[i]);
        } else {
            return false;
        }
    }
    for (var i = 0; i < deletedChanges.length; i++) {
        if ($('#system_menu_function_datagrid').datagrid('validateRow', i)) {
            deletedArray.push(deletedChanges[i]);
        } else {
            return false;
        }
    }

    $('#system_menu_edit_form').form('submit', {
        onSubmit: function (param) {
            param.saveFunctions = JSON.stringify(savedArray);
            param.deleteFunctions = JSON.stringify(deletedArray);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#system_menu_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
                $('#system_menu_menu_tree').treegrid('reload');
            } else {
                $.notify("保存失败", "error");
            }
        }
    });
}

function systemMenuShowFunctions(menuId) {
    var editIndex = undefined;
    var dg = $('#system_menu_function_datagrid').datagrid({
        border: false,
        iconCls: 'icon-edit',//图标
        width: 500,
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
        queryParams: {menuId: menuId},
        url: 'system/function/queryFunctions',
        rownumbers: true,
        singleSelect: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {
                field: 'code',
                title: '编号',
                width: 120,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
            },
            {
                field: 'name',
                title: '名称',
                width: 120,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
            },
            {
                field: 'url',
                title: 'URL',
                width: 240,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
            }
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                append();
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                removeit();
            }
        }],
        onBeforeLoad: function (param) {
            if (param.menuId == null) {
                return false;
            }
            return true;
        },
        onClickRow: onClickRow
    });

    function append() {
        if ($('#system_menu_function_datagrid').datagrid('validateRow', editIndex)) {
            if (editIndex != undefined) {
                $('#system_menu_function_datagrid').datagrid('endEdit', editIndex);
                $('#system_menu_function_datagrid').datagrid('unselectRow', editIndex);
            }
            $('#system_menu_function_datagrid').datagrid('appendRow', {});
            editIndex = $('#system_menu_function_datagrid').datagrid('getRows').length - 1;
            $('#system_menu_function_datagrid').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);

        }
    }

    function removeit() {
        if (editIndex == undefined) {
            return
        }
        $('#system_menu_function_datagrid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }

    function onClickRow(index) {
        if (editIndex != index) {
            if ($('#system_menu_function_datagrid').datagrid('validateRow', editIndex)) {
                $('#system_menu_function_datagrid').datagrid('endEdit', editIndex);
                $('#system_menu_function_datagrid').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#system_menu_function_datagrid').datagrid('unselectRow', index);
                $('#system_menu_function_datagrid').datagrid('selectRow', editIndex);
            }
        }
    }
}
