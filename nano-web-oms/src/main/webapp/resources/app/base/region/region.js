function baseRegionInit() {
    $('#base_region_region_tree').treegrid({
        title: '行政区划信息',
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
        url: 'base/region/queryRegions',
        idField : 'id',
        treeField : 'name',
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'name', title: '行政名称', width: 200},
            {field: 'code', title: '行政代码', width: 100,align:'center'},
            {field: 'expressNames', title: '到达快递[代码,优先级]', width: 700,align:'center'}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_region_edit_form').form('reset');
                baseRegionComboboxShow();
                $('#base_region_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '导入行政区划代码',
            iconCls: 'icon-edit',
            handler: function () {
                $('#base_region_upload_window').window('open');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_region_region_tree').treegrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_region_id').val(selected.id);
                $('#base_region_code').textbox('setValue', selected.code);
                $('#base_region_name').textbox('setValue', selected.name);
                baseRegionComboboxShow(selected.parentId);
                $('#base_region_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '设置快递优先级',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_region_region_tree').treegrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_region_express_datagrid').datagrid({
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
                    idField: 'expressId',
                    url: 'base/express/queryAllExpresses',
                    queryParams : {regionId : selected.id},
                    frozenColumns:[[
                        {field:'ck',checkbox:true}
                    ]],
                    singleSelect: false,
                    columns: [[
                        {field: 'expressCode', title: '快递代码', width: 100},
                        {field: 'expressName', title: '快递名称', width: 100},
                        {field: 'priority', title: '优先级', width: 100,editor: {type: 'numberspinner', options: {required: true,min:0,max:9,editable: false}}}
                    ]],
                    onClickRow: function(index){
                        $('#base_region_express_datagrid').datagrid('selectRow', index).datagrid('beginEdit', index);
                    }
                });
                $('#base_region_express_priority_window').window('open');

            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_region_region_tree').treegrid('getSelections');
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
                        deleteSelectedRows('base/region/delete', array, function () {
                            $('#base_region_region_tree').treegrid('reload');
                        });
                    }
                });
            }
        }]

    });
    $('#base_region_region_tree').treegrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseRegionUploadForm() {
    $('#base_region_upload_form').form('submit', {
    	error: function(){
    		$.notify("保存失败", "error");
    	},
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_region_upload_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_region_upload_form').form('reset');
            $('#base_region_region_tree').treegrid('reload');
        }
    });
}

function baseRegionSubmitForm() {
    $('#base_region_edit_form').form('submit', {
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_region_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_region_region_tree').treegrid('reload');
        }
    });
}

function baseRegionExpressPrioritySubmitForm(){
    var selected = $('#base_region_region_tree').treegrid('getSelected');
    $('#base_region_express_datagrid').datagrid('acceptChanges');
    var regionExpresses = $('#base_region_express_datagrid').datagrid('getRows');
    $.ajax({
        url : 'base/region/configRegionExpressPriority',
        type : 'POST',
        data : {regionExpresses : JSON.stringify(regionExpresses),id:selected.id},
        dataType : 'json',
        success : function(data){
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败 : " + data.errorMsg, "error");
            }
            $('#base_region_express_priority_window').window('close');
        }
    });
}

function baseRegionComboboxShow(val){
    $('#base_region_parent_id').combotree({
        url:'base/region/queryRegions',
        method:'get',
        valueField:'id',
        treeField:'name',
        onLoadSuccess:function(){
            if(val){
                $('#base_region_parent_id').combotree('setValue',val);
            }
        }
    });
}