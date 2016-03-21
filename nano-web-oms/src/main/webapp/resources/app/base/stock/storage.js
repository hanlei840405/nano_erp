function baseStorageInit() {
    $('#base_storage_storage_datagrid').datagrid({
        title: '库存信息',
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
        url: 'base/stock/queryStorages',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'no', title: '批次号', width: 150, align: 'center'},
            {field: 'barcode', title: '条形码', width: 150, align: 'center'},
            {field: 'productCode', title: '货号', width: 150, align: 'center'},
            {field: 'warehouseName', title: '仓库', width: 150, align: 'center'},
            {field: 'quantity', title: '库存', width: 100, align: 'center'},
            {field: 'status', title: '状态', width: 200, align: 'center',
                formatter: function(value){
                    if (value == 1){
                        return '入库';
                    } else {
                        return '新建';
                    }
                }}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_storage_edit_form').form('reset');
                $('#base_storage_warehouse_id').combobox('clear');
                $('#base_storage_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_storage_storage_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_storage_id').val(selected.id);
                $('#base_storage_no').textbox('setValue', selected.no);
                $('#base_storage_barcode').textbox('setValue', selected.barcode);
                $('#base_storage_quantity').textbox('setValue', selected.quantity);
                baseStorageWarehouseCombobox();
                $('#base_storage_warehouse_id').combobox('setValue', selected.warehouseId);
                $('#base_storage_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '入库',
            iconCls: 'icon-edit',
            handler: function () {
                var selections = $('#base_storage_storage_datagrid').datagrid('getSelections');
                if(selections.length == 0){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                var array = new Array();
                for(var i = 0;i < selections.length;i++){
                    var row = selections[i];
                    if(row.status == '1'){
                        $.notify("批次号为【" + row.no + "】，条形码为【" + row.barcode + "】已入库，不可重复入库", "error");
                        return false;
                    }
                    array.push(row.id);
                }
                $.ajax({
                    url : 'base/stock/storage/storage',
                    type : 'POST',
                    data : {ids : array},
                    dataType : 'json',
                    success : function(data){
                        $('#base_storage_saveOrUpdate_window').window('close');
                        if (data.yesOrNo) {
                            $.notify("保存成功", "success");
                        } else {
                            $.notify("保存失败 : " + data.errorMsg, "error");
                        }
                        $('#base_storage_storage_datagrid').datagrid('reload');
                    }
                });
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_storage_storage_datagrid').datagrid('getSelections');
                if(!selections.length){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                var array = new Array();
                for (var i = 0; i < selections.length; i++) {
                    var row = selections[i];
                    if(row.status == '1'){
                        $.notify("批次号为【" + row.no + "】，条形码为【" + row.barcode + "】已入库，不可删除", "error");
                        return false;
                    }
                    array.push(row.id);
                }
                $.messager.confirm('Confirm', '确定删除?', function (r) {
                    if (r) {
                        deleteSelectedRows('base/stock/storage/delete', array, function () {
                            $('#base_storage_storage_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_storage_storage_datagrid_pagination = $('#base_storage_storage_datagrid').datagrid('getPager');
    $(base_storage_storage_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_storage_storage_datagrid').datagrid('enableFilter');
}

function baseStorageSubmitForm(){
	$('#base_storage_edit_form').form('submit', {
		success : function(data) {
			data = $.parseJSON(data);
			$('#base_storage_saveOrUpdate_window').window('close');
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
			} else {
                $.notify("保存失败 : " + data.errorMsg, "error");
			}
			$('#base_storage_storage_datagrid').datagrid('reload');
		}
	});
}

function baseStorageSearchForm(){
    $('#base_storage_storage_datagrid').datagrid('load',{
    	productCode : $('#base_search_storage_product').textbox('getValue'),
    	barcode : $('#base_search_storage_barcode').textbox('getValue'),
    	warehouseId : $('#base_search_storage_warehouse').combobox('getValue')
    });
}

function baseStorageWarehouseCombobox(){
	$('#base_storage_warehouse_id').combobox({
		url : 'base/warehouse/queryWarehousesBySku',
		queryParams:{barcode:$('#base_storage_barcode').textbox('getValue')},
		valueField:'id',
		textField:'name'
	});
}