function baseContactInit() {
    $('#base_contact_contact_datagrid').datagrid({
        title: '品牌信息',
        border: false,
        iconCls: 'icon-edit',// 图标
        width: 700,
        height: 'auto',
        nowrap: false,
        striped: true,
        border: true,
        ctrlSelect: true,
        collapsible: false,// 是否可折叠的
        fit: true,// 自动大小
        selectOnCheck: true,
        checkOnSelect: true,
        idField: 'id',
        url: 'base/contact/queryContacts',
        pagination: true,
        pageSize: 10,// 每页显示的记录条数，默认为10
        pageList: [10, 20, 50],// 可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'code', title: '编号', width: 200, align: 'center'},
            {field: 'name', title: '名称', width: 200, align: 'center'},
            {field: 'userNames', title: '关联员工', width: 200, align: 'center'},
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                baseContactShowUsers();
                $('#base_contact_edit_form').form('reset');
                $('#base_contact_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_contact_contact_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_contact_id').val(selected.id);
                $('#base_contact_code').textbox('setValue', selected.code);
                $('#base_contact_name').textbox('setValue', selected.name);
                baseContactShowUsers(selected.id);
                $('#base_contact_status').combobox('setValue', selected.status);
                $('#base_contact_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_contact_contact_datagrid').datagrid('getSelections');
                if(!selections.length){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                var array = new Array();
                for (var i = 0; i < selections.length; i++) {
                    array.push(selections[i].id);
                }
                $.messager.confirm('Confirm', '确定删除?', function (r) {
                    if (r) {
                        deleteSelectedRows('base/contact/delete', array, function () {
                            $('#base_contact_contact_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    // 设置分页控件
    var base_contact_contact_datagrid_pagination = $('#base_contact_contact_datagrid').datagrid('getPager');
    $(base_contact_contact_datagrid_pagination).pagination({
        beforePageText: '第',// 页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_contact_contact_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseContactSubmitForm() {
    var insertedChanges = $('#base_contact_user_datagrid').datagrid('getRows');
    var inserts = new Array();
    for (var i = 0; i < insertedChanges.length; i++) {
        inserts.push(insertedChanges[i].id);
    }
    $('#base_contact_edit_form').form('submit', {
        onSubmit: function (param) {
            param.inserts = JSON.stringify(inserts);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_contact_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_contact_contact_datagrid').datagrid('reload');
        }
    });
}

function baseContactSearchForm(){
    $('#base_contact_contact_datagrid').datagrid('load',{
        code : $('#base_search_contact_code').val(),
        name : $('#base_search_contact_name').val(),
        status :$('#base_search_contact_status').combobox('getValue')
    });
}

function baseContactShowUsers(contactId) {
    var editIndex = undefined;
    $('#base_contact_user_tree').tree({
    	url : 'system/user/queryUsersForPlugin',
    	onDblClick:function(node){
            if(node.departmentName){
                append(node);
            }
    	}
    });
	$('#base_contact_user_datagrid').datagrid({
        border: false,
        iconCls: 'icon-edit',// 图标
        width: 500,
        height: 'auto',
        nowrap: false,
        striped: true,
        border: false,
        ctrlSelect: true,
        collapsible: false,// 是否可折叠的
        fit: true,// 自动大小
        selectOnCheck: true,
        checkOnSelect: true,
        idField: 'id',
        queryParams: {contactId: contactId},
        url: 'base/contact/queryContactUsers',
        rownumbers: true,
        singleSelect: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {
                field: 'id',
                title: 'id',
                width: 120,
                align: 'center',
                hidden:true
            },
            {
                field: 'departmentName',
                title: '部门',
                width: 120,
                align: 'center'
            },
            {
                field: 'name',
                title: '名称',
                width: 120,
                align: 'center'
            }
        ]],
        toolbar: [{
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                removeit();
            }
        }],
        onBeforeLoad: function (param) {
            var item = $('#base_contact_user_datagrid').datagrid('getRows');
            if (item) {
                for (var i = item.length - 1; i >= 0; i--) {
                    var index = $('#base_contact_user_datagrid').datagrid('getRowIndex', item[i]);
                    $('#base_contact_user_datagrid').datagrid('deleteRow', index);
                }
            }
            if (!param.contactId) {
                return false;
            }
            return true;
        }
    });

    function append(node) {
        var item = $('#base_contact_user_datagrid').datagrid('getRows');
        if (item) {
            for (var i = item.length - 1; i >= 0; i--) {
                if(item[i].userId == node.id){
                    return false;
                }
            }
        }
        $('#base_contact_user_datagrid').datagrid('appendRow', {id:node.id,name : node.text,departmentName : node.departmentName});

    }

    function removeit() {
    	var selections = $('#base_contact_user_datagrid').datagrid('getSelections');
        if(!selections.length){
            $.notify("请先选择一条数据", "warn");
            return false;
        }
        var index = $('#base_contact_user_datagrid').datagrid('getRowIndex', selections[0]);
        $('#base_contact_user_datagrid').datagrid('deleteRow',index);
    }
}