function baseStockInit() {
    $('#base_stock_stock_datagrid').datagrid({
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
        url: 'base/stock/queryStocks',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'barcode', title: '条形码', width: 150, align: 'center'},
            {field: 'productCode', title: '货号', width: 150, align: 'center'},
            {field: 'warehouseName', title: '仓库', width: 150, align: 'center'},
            {field: 'quantity', title: '库存', width: 100, align: 'center'},
            {field: 'safeQuantity', title: '安全库存', width: 100, align: 'center'}
        ]]

    });
    //设置分页控件
    var base_stock_stock_datagrid_pagination = $('#base_stock_stock_datagrid').datagrid('getPager');
    $(base_stock_stock_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_stock_stock_datagrid').datagrid('enableFilter');
}

function baseStockSearchForm(){
    $('#base_stock_stock_datagrid').datagrid('load',{
    	productCode : $('#base_search_stock_product').textbox('getValue'),
    	barcode : $('#base_search_stock_barcode').textbox('getValue'),
    	warehouseId : $('#base_search_stock_warehouse').combobox('getValue')
    });
}