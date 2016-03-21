function baseCategoryInit() {
    $('#base_category_category_tree').treegrid({
        title: '类目信息',
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
        url: 'base/category/queryCategories',
        idField : 'id',
        treeField : 'name',
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'name', title: '名称', width: 200}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_category_edit_form').form('reset');
                baseCategoryComboboxShow();
                $('#base_category_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_category_category_tree').treegrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_category_id').val(selected.id);
                $('#base_category_code').textbox('setValue', selected.code);
                $('#base_category_name').textbox('setValue', selected.name);
                baseCategoryComboboxShow(selected.parentId);
                $('#base_category_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_category_category_tree').treegrid('getSelections');
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
                        deleteSelectedRows('base/category/delete', array, function () {
                            $('#base_category_category_tree').treegrid('reload');
                        });
                    }
                });
            }
        }]

    });
    $('#base_category_category_tree').treegrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseCategorySubmitForm() {
    $('#base_category_edit_form').form('submit', {
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_category_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_category_category_tree').treegrid('reload');
        }
    });
}

function baseCategorySearchForm(){
    $('#base_category_category_treegrid').treegrid('load',{
        code : $('#base_search_category_code').val(),
        name : $('#base_search_category_name').val()
    });
}

function baseCategoryComboboxShow(val){
    $('#base_category_parent_id').combotree({
        url:'base/category/queryCategories',
        method:'get',
        valueField:'id',
        treeField:'name',
        onLoadSuccess:function(){
            if(val){
                $('#base_category_parent_id').combotree('setValue',val);
            }
        }
    });
}