function productMappingTaoBaoInit() {
    $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid({
        title: '淘宝铺货',
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
        url: 'productMapping/taoBao/queryTaoBaoProductSkuMapping',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'platformProductId', title: '商品平台数字ID', width: 100, align: 'center'},
            {field: 'productOuterId', title: '货号', width: 100, align: 'center'},
            {field: 'num', title: '数量', width: 100, align: 'center'},
            {field: 'listTime', title: '上架时间', width: 150, align: 'center'},
            {field: 'deListTime', title: '下架时间', width: 150, align: 'center'},
            {field: 'title', title: '平台标题', width: 400, align: 'center'},
            {field: 'productPrice', title: '平台售价', width: 100, align: 'center'},
            {field: 'shopName', title: '所属店铺', width: 100, align: 'center'},
            {field: 'contact', title: '商品联系人', width: 100, align: 'center'},
            {field: 'approveStatus', title: '上下架状态', width: 100, align: 'center',
                formatter: function(value){
                    if (value == 1){
                        return '上架';
                    } else {
                        return '下架';
                    }
                }},
            {field: 'platformSkuId', title: 'SKU平台ID', width: 100, align: 'center'},
            {field: 'skuOuterId', title: 'SKU平台编号', width: 100, align: 'center'},
            {field: 'barcode', title: 'SKU平台条码', width: 100, align: 'center'},
            {field: 'properties', title: 'SKU平台属性', width: 200, align: 'center'},
            {field: 'quantity', title: 'SKU平台数量', width: 100, align: 'center'},
            {field: 'skuPrice', title: 'SKU平台价格', width: 100, align: 'center'},
            {field: 'spec', title: 'SKU本地规格', width: 100, align: 'center'}
        ]],
        toolbar: [{
            text: '铺货',
            iconCls: 'icon-add',
            handler: function () {
                $('#product_mapping_tao_bao_shop_id').combobox({
                    url:'base/shop/queryAllShops',
                    queryParams : {platformId:$('#product_mapping_tao_bao_platform_id').val()},
                    valueField : 'id',
                    textField : 'name',
                    onlyLeafCheck:true,
                    panelHeight:'auto',
                    onLoadSuccess:function(){
                        $('#product_mapping_tao_bao_shop_id').combobox('clear');
                    }
                });
                $('#product_mapping_tao_bao_fetch_form').form('reset');
                $('#product_mapping_tao_bao_fetch_window').window('open');
            }
        }, '-', {
            text: '上/下架',
            iconCls: 'icon-edit',
            handler: function () {
                var selections = $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('getSelections');
                if(!selections.length){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
            }
        }, '-', {
            text: '同步库存',
            iconCls: 'icon-edit',
            handler: function () {
                var selections = $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('getSelections');
                if(!selections.length){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                var array = new Array();
                for (var i = 0; i < selections.length; i++) {
                    array.push(selections[i].id);
                }
                $.messager.confirm('Confirm', '确定同步库存?', function (r) {
                    if (r) {

                        $.ajax({
                            url : 'productMapping/taoBao/synchStock',
                            emthod : 'GET',
                            data : {ids : array},
                            dataType : 'json',
                            success : function(data){
                                if(data.yesOrNo){
                                    $.notify("同步淘宝平台成功", "success");
                                    callback();
                                }else{
                                    $.notify("同步淘宝平台失败 : " + data.errorMsg, "error");
                                }
                            }
                        });
                        $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('reload');
                    }
                });
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('product_mapping/tao_bao/delete', array, function () {
                            $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var product_mapping_tao_bao_tao_bao_datagrid_pagination = $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('getPager');
    $(product_mapping_tao_bao_tao_bao_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('enableFilter');
    $('#product_mapping_search_tao_bao_shop_id').combobox({
        url:'base/shop/queryAllShops',
        queryParams : {platformId:$('#product_mapping_tao_bao_platform_id').val()},
        valueField : 'id',
        textField : 'name',
        onlyLeafCheck:true,
        panelHeight:'auto'
    });
}
/**
 * ajax方式提交表单
 */
function productMappingTaoBaoFetchSubmitForm() {
    $('#product_mapping_tao_bao_fetch_form').form('submit', {
        success: function (data) {
            data = $.parseJSON(data);
            $('#product_mapping_tao_bao_fetch_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('reload');
        }
    });
}

function productMappingTaoBaoSearchForm(){
    $('#product_mapping_tao_bao_tao_bao_datagrid').datagrid('load',{
        productOuterId : $('#product_mapping_search_tao_bao_outer_id').val(),
        barcode : $('#product_mapping_search_tao_bao_barcode').val(),
        shopId :$('#product_mapping_search_tao_bao_shop_id').combobox('getValue')
    });
}