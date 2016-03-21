function baseShopInit() {
    $('#base_shop_shop_datagrid').datagrid({
        title: '店铺信息',
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
        url: 'base/shop/queryShops',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'code', title: '编号', width: 150, align: 'center'},
            {field: 'name', title: '名称', width: 150, align: 'center'},
            {field: 'platformName', title: '平台', width: 150, align: 'center'},
            {field: 'status', title: '状态', width: 150, align: 'center',formatter:function(value,row,index){
                if(value == '1'){
                    return '启动';
                }
                return '停用';
            }},
            {field: 'categoryNames', title: '类目', width: 250, align: 'center'},
            {field: 'memo', title: '备注', width: 250, align: 'center'}
        ]],
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table class="ddv"></table></div>';
        },
        onExpandRow: function(index,row){
            var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
            ddv.datagrid({
                url:'base/shop/queryShopProperties?shopId='+row.id,
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                columns:[[
                    {field:'code',title:'自定义键',width:100,align:'center'},
                    {field:'value',title:'自定义值',width:200,align:'center'}
                ]],
                onResize:function(){
                    $('#base_shop_shop_datagrid').datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                        $('#base_shop_shop_datagrid').datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            $('#base_shop_shop_datagrid').datagrid('fixDetailRowHeight',index);
        },
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_shop_edit_form').form('reset');
                baseShopShowProperties(null);
                baseShopCategoryTree(null);
                baseShopPlatformComboboxShow(null);
                $('#base_shop_saveOrUpdate_window').window('open');
            }
        }, {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_shop_shop_datagrid').treegrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_shop_edit_form').form('reset');
                $('#base_shop_id').val(selected.id);
                $('#base_shop_name').textbox('setValue', selected.name);
                $('#base_shop_code').textbox('setValue', selected.code);
                $('#base_shop_status').combobox('setValue', selected.status);
                $('#base_shop_memo').textbox('setValue', selected.memo);
                baseShopShowProperties(selected.id);
                baseShopCategoryTree(selected.categoryIds);
                baseShopPlatformComboboxShow(selected.platformId);
                $('#base_shop_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_shop_shop_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('base/shop/delete', array, function () {
                            $('#base_shop_shop_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_shop_shop_datagrid_pagination = $('#base_shop_shop_datagrid').datagrid('getPager');
    $(base_shop_shop_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_shop_shop_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseShopSubmitForm() {
    var editIndex = $('#base_shop_property_datagrid').datagrid('getRowIndex',$('#base_shop_property_datagrid').datagrid('getSelected'));
    $('#base_shop_property_datagrid').datagrid('endEdit',editIndex);
    $('#base_shop_property_datagrid').datagrid('acceptChanges');
    var properties = $('#base_shop_property_datagrid').datagrid('getRows');
    var nodes = $('#base_shop_category_tree').tree('getChecked');
    var categories = new Array();
    for(var i = 0;i < nodes.length;i++){
        categories.push(nodes[i].id);
    }
    $('#base_shop_edit_form').form('submit', {
        onSubmit: function (param) {
            param.properties = JSON.stringify(properties);
            param.categories = JSON.stringify(categories);
        },
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_shop_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_shop_shop_datagrid').datagrid('reload');
        }
    });
}

function baseShopSearchForm(){
    $('#base_shop_shop_datagrid').datagrid('load',{
        code : $('#base_search_shop_code').textbox('getValue'),
        name : $('#base_search_shop_name').textbox('getValue'),
        platformId : $('#base_search_shop_platform').combobox('getValue')
    });
}

function baseShopCategoryTree(categoryIds){
    $('#base_shop_category_tree').tree({
        url:'base/category/queryCategoriesForSelect',
        animate:true,
        checkbox:true,
        onLoadSuccess : function(node, data){
            if(categoryIds != null){
                var nodes = $('#base_shop_category_tree').tree('getRoots');
                for(var i = 0;i < nodes.length;i++){
                    setTreeNodesChecked($('#base_shop_category_tree'),nodes[i],categoryIds);
                }
            }
        }
    });
}

function baseShopPlatformComboboxShow(val) {
	$('#base_shop_platform_id').combobox({
		url : 'base/platform/queryPlatformsForPlugin',
		method : 'get',
		valueField : 'id',
		textField : 'name',
		panelHeight : 'auto',
		onLoadSuccess : function() {
			if (val) {
				$('#base_shop_platform_id').combobox('setValue', val);
			}
		}
	});
}

function baseShopShowProperties(shopId) {
    var editIndex = undefined;
    var dg = $('#base_shop_property_datagrid').datagrid({
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
        queryParams: {shopId: shopId},
        url: 'base/shop/queryShopProperties',
        rownumbers: true,
        singleSelect: false,
        columns: [[
            {field: 'ck', checkbox: true},
            {
                field: 'code',
                title: '自定义键',
                width: 200,
                align: 'center',
                editor: {type: 'textbox', options: {required: true}}
            },
            {
                field: 'value',
                title: '自定义值',
                width: 200,
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
            var item = $('#base_shop_property_datagrid').datagrid('getRows');
            if (item) {
                for (var i = item.length - 1; i >= 0; i--) {
                    var index = $('#base_shop_property_datagrid').datagrid('getRowIndex', item[i]);
                    $('#base_shop_property_datagrid').datagrid('deleteRow', index);
                }
            }
            if (param.shopId == null) {
                return false;
            }
            return true;
        },
        onClickRow: onClickRow
    });

    function append() {
        if ($('#base_shop_property_datagrid').datagrid('validateRow', editIndex)) {
            if (editIndex != undefined) {
                $('#base_shop_property_datagrid').datagrid('endEdit', editIndex);
                $('#base_shop_property_datagrid').datagrid('unselectRow', editIndex);
            }
            $('#base_shop_property_datagrid').datagrid('appendRow', {});
            editIndex = $('#base_shop_property_datagrid').datagrid('getRows').length - 1;
            $('#base_shop_property_datagrid').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);

        }
    }

    function removeit() {
        if (editIndex == undefined) {
            return
        }
        $('#base_shop_property_datagrid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
        editIndex = undefined;
    }

    function onClickRow(index) {
        if (editIndex != index) {
            if ($('#base_shop_property_datagrid').datagrid('validateRow', editIndex)) {
                $('#base_shop_property_datagrid').datagrid('endEdit', editIndex);
                $('#base_shop_property_datagrid').datagrid('selectRow', index)
                    .datagrid('beginEdit', index);
                editIndex = index;
            } else {
                $('#base_shop_property_datagrid').datagrid('unselectRow', index);
                $('#base_shop_property_datagrid').datagrid('selectRow', editIndex);
            }
        }
    }
}