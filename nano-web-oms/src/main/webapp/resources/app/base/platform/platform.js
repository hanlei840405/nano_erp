function basePlatformInit() {
    $('#base_platform_platform_datagrid').datagrid({
        title: '品牌信息',
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
        url: 'base/platform/queryPlatforms',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'code', title: '编号', width: 200, align: 'center'},
            {field: 'name', title: '名称', width: 200, align: 'center'},
            {field: 'status', title: '状态', width: 200, align: 'center',
                formatter: function(value){
                    if (value == 1){
                        return '启用';
                    } else {
                        return '停用';
                    }
                }}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_platform_edit_form').form('reset');
                $('#base_platform_saveOrUpdate_window').window('open');
        		basePlatformShowPlatformExpresses();
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_platform_platform_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_platform_id').val(selected.id);
                $('#base_platform_code').textbox('setValue', selected.code);
                $('#base_platform_name').textbox('setValue', selected.name);
                $('#base_platform_saveOrUpdate_window').window('open');
        		basePlatformShowPlatformExpresses(selected.id);
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_platform_platform_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('base/platform/delete', array, function () {
                            $('#base_platform_platform_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_platform_platform_datagrid_pagination = $('#base_platform_platform_datagrid').datagrid('getPager');
    $(base_platform_platform_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_platform_platform_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function basePlatformSubmitForm() {
    var editIndex = $('#base_platform_express_datagrid').datagrid('getRowIndex',$('#base_platform_express_datagrid').datagrid('getSelected'));
    $('#base_platform_express_datagrid').datagrid('endEdit',editIndex);
    var insertedChanges = $('#base_platform_express_datagrid').datagrid('getRows');
    var inserts = new Array();
    $('#base_platform_express_datagrid').datagrid('acceptChanges');
    for (var i = 0; i < insertedChanges.length; i++) {
        inserts.push(insertedChanges[i]);
    }
    $('#base_platform_edit_form').form('submit', {
        onSubmit: function (param) {
            param.inserts = JSON.stringify(inserts);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_platform_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_platform_platform_datagrid').datagrid('reload');
        }
    });
}

function basePlatformSearchForm(){
    $('#base_platform_platform_datagrid').datagrid('load',{
        code : $('#base_search_platform_code').val(),
        name : $('#base_search_platform_name').val(),
        status :$('#base_search_platform_status').combobox('getValue')
    });
}

function basePlatformShowPlatformExpresses(platformId) {
    var editIndex = undefined;
    $('#base_platform_express_tree').tree({
    	url:'base/express/queryAllExpresses',
    	onDblClick:function(node){
    		append(node);
    	}
    });
	$('#base_platform_express_datagrid').datagrid({
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
        queryParams: {platformId: platformId},
        url: 'base/express/queryAllExpresses',
        rownumbers: true,
        singleSelect: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {
                field: 'expressId',
                title: 'expressId',
                width: 120,
                align: 'center',
                hidden:true
            },
            {
                field: 'standardCode',
                title: '标准代码',
                width: 120,
                align: 'center'
            },
            {
                field: 'name',
                title: '名称',
                width: 120,
                align: 'center'
            },
            {
                field: 'code',
                title: '平台代码',
                width: 120,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
            },
            {
                field: 'alias',
                title: '别称',
                width: 120,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
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
            var item = $('#base_platform_express_datagrid').datagrid('getRows');
            if (item) {
                for (var i = item.length - 1; i >= 0; i--) {
                    var index = $('#base_platform_express_datagrid').datagrid('getRowIndex', item[i]);
                    $('#base_platform_express_datagrid').datagrid('deleteRow', index);
                }
            }
            if (!param.platformId) {
                return false;
            }
            return true;
        },
        onClickRow: onClickRow
    });

    function append(node) {
        var item = $('#base_platform_express_datagrid').datagrid('getRows');
        if (item) {
            for (var i = item.length - 1; i >= 0; i--) {
                if(item[i].expressId == node.id){
                    return false;
                }
            }
        }

        if ($('#base_platform_express_datagrid').datagrid('validateRow', editIndex)) {
            if (editIndex != undefined) {
                $('#base_platform_express_datagrid').datagrid('endEdit', editIndex);
                $('#base_platform_express_datagrid').datagrid('unselectRow', editIndex);
            }
            $('#base_platform_express_datagrid').datagrid('appendRow', {expressId:node.id,standardCode : node.code,name : node.name,code:node.code,alias:node.name});
            editIndex = $('#base_platform_express_datagrid').datagrid('getRows').length - 1;
            $('#base_platform_express_datagrid').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);

        }

    }

    function removeit() {
        if (editIndex == undefined) {
            return
        }
        $('#base_platform_express_datagrid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }
    
    function onClickRow(index) {
        if (editIndex != index) {
            if ($('#base_platform_express_datagrid').datagrid('validateRow', editIndex)) {
                $('#base_platform_express_datagrid').datagrid('endEdit', editIndex);
                $('#base_platform_express_datagrid').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#base_platform_express_datagrid').datagrid('unselectRow', index);
                $('#base_platform_express_datagrid').datagrid('selectRow', editIndex);
            }
        }
    }
}