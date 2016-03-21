function baseDictionaryInit() {
    $('#base_dictionary_dictionary_datagrid').datagrid({
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
        url: 'base/dictionary/queryDictionaries',
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
            {field: 'name', title: '名称', width: 200, align: 'center'}
        ]],
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table class="ddv"></table></div>';
        },
        onExpandRow: function(index,row){
            var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
            ddv.datagrid({
                url:'base/dictionary/queryDictionaryItems?dictionaryId='+row.id,
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                columns:[[
                    {field:'code',title:'编号',width:100,align:'right'},
                    {field:'name',title:'名称',width:100,align:'right'}
                ]],
                onResize:function(){
                    $('#base_dictionary_dictionary_datagrid').datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                        $('#base_dictionary_dictionary_datagrid').datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            $('#base_dictionary_dictionary_datagrid').datagrid('fixDetailRowHeight',index);
        },
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_dictionary_edit_form').form('reset');
                baseDictionaryShowItems(null);
                $('#base_dictionary_name').textbox('readonly',false);
                $('#base_dictionary_code').textbox('readonly',false);
                $('#base_dictionary_saveOrUpdate_window').window('open');
            }
        }, {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_dictionary_dictionary_datagrid').treegrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_dictionary_edit_form').form('reset');
                $('#base_dictionary_id').val(selected.id);
                $('#base_dictionary_name').textbox('setValue', selected.name);
                $('#base_dictionary_name').textbox('readonly');
                $('#base_dictionary_code').textbox('setValue', selected.code);
                $('#base_dictionary_code').textbox('readonly');
                baseDictionaryShowItems(selected.id);
                $('#base_dictionary_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_dictionary_dictionary_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('base/dictionary/delete', array, function () {
                            $('#base_dictionary_dictionary_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_dictionary_dictionary_datagrid_pagination = $('#base_dictionary_dictionary_datagrid').datagrid('getPager');
    $(base_dictionary_dictionary_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_dictionary_dictionary_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseDictionarySubmitForm() {
    var editIndex = $('#base_dictionary_item_datagrid').datagrid('getRowIndex',$('#base_dictionary_item_datagrid').datagrid('getSelected'));
    $('#base_dictionary_item_datagrid').datagrid('endEdit',editIndex);
    var insertedChanges = $('#base_dictionary_item_datagrid').datagrid('getChanges','inserted');
    var updatedChanges = $('#base_dictionary_item_datagrid').datagrid('getChanges','updated');
    var deletedChanges = $('#base_dictionary_item_datagrid').datagrid('getChanges','deleted');
    var savedArray = new Array();
    var deletedArray = new Array();
    $('#base_dictionary_item_datagrid').datagrid('acceptChanges');
    for (var i = 0; i < insertedChanges.length; i++) {
        if ($('#base_dictionary_item_datagrid').datagrid('validateRow', i)) {
            savedArray.push(insertedChanges[i]);
        } else {
            return false;
        }
    }
    for (var i = 0; i < updatedChanges.length; i++) {
        if ($('#base_dictionary_item_datagrid').datagrid('validateRow', i)) {
            savedArray.push(updatedChanges[i]);
        } else {
            return false;
        }
    }
    for (var i = 0; i < deletedChanges.length; i++) {
        if ($('#base_dictionary_item_datagrid').datagrid('validateRow', i)) {
            deletedArray.push(deletedChanges[i]);
        } else {
            return false;
        }
    }

    $('#base_dictionary_edit_form').form('submit', {
        onSubmit: function (param) {
            param.saveFunctions = JSON.stringify(savedArray);
            param.deleteFunctions = JSON.stringify(deletedArray);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_dictionary_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_dictionary_dictionary_datagrid').datagrid('reload');
        }
    });
}

function baseDictionarySearchForm(){
    $('#base_dictionary_dictionary_datagrid').datagrid('load',{
        code : $('#base_search_dictionary_code').val(),
        name : $('#base_search_dictionary_name').val()
    });
}

function baseDictionaryShowItems(dictionaryId) {
    var editIndex = undefined;
    var dg = $('#base_dictionary_item_datagrid').datagrid({
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
        queryParams: {dictionaryId: dictionaryId},
        url: 'base/dictionary/queryDictionaryItems',
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
            var item = $('#base_dictionary_item_datagrid').datagrid('getRows');
            if (item) {
                for (var i = item.length - 1; i >= 0; i--) {
                    var index = $('#base_dictionary_item_datagrid').datagrid('getRowIndex', item[i]);
                    $('#base_dictionary_item_datagrid').datagrid('deleteRow', index);
                }
            }
            if (param.dictionaryId == null) {
                return false;
            }
            return true;
        },
        onClickRow: onClickRow
    });

    function append() {
        if ($('#base_dictionary_item_datagrid').datagrid('validateRow', editIndex)) {
            if (editIndex != undefined) {
                $('#base_dictionary_item_datagrid').datagrid('endEdit', editIndex);
                $('#base_dictionary_item_datagrid').datagrid('unselectRow', editIndex);
            }
            $('#base_dictionary_item_datagrid').datagrid('appendRow', {});
            editIndex = $('#base_dictionary_item_datagrid').datagrid('getRows').length - 1;
            $('#base_dictionary_item_datagrid').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);

        }
    }

    function removeit() {
        if (editIndex == undefined) {
            return
        }
        $('#base_dictionary_item_datagrid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }

    function onClickRow(index) {
        if (editIndex != index) {
            if ($('#base_dictionary_item_datagrid').datagrid('validateRow', editIndex)) {
                $('#base_dictionary_item_datagrid').datagrid('endEdit', editIndex);
                $('#base_dictionary_item_datagrid').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#base_dictionary_item_datagrid').datagrid('unselectRow', index);
                $('#base_dictionary_item_datagrid').datagrid('selectRow', editIndex);
            }
        }
    }
}