function baseSmsInit() {
    $('#base_sms_sms_datagrid').datagrid({
        title: '短信信息',
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
        url: 'base/sms/querySmses',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'telephone', title: '电话', width: 100, align: 'center'},
            {field: 'templateName', title: '模板', width: 100, align: 'center'},
            {field: 'content', title: '内容', width: 400, align: 'center'}
        ]],
        toolbar: [{
            text: '查看',
            iconCls: 'icon-print',
            handler: function () {
                var selected = $('#base_sms_sms_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_sms_id').val(selected.id);
                $('#base_sms_telephone').textbox('setValue', selected.telephone);
                $('#base_sms_templateName').textbox('setValue', selected.templateName);
                $('#base_sms_content').textbox('setValue', selected.content);
                $('#base_sms_view_window').window('open');
            }
        }]

    });
    //设置分页控件
    var base_sms_sms_datagrid_pagination = $('#base_sms_sms_datagrid').datagrid('getPager');
    $(base_sms_sms_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_sms_sms_datagrid').datagrid('enableFilter');
}

function baseSmsSearchForm(){
    $('#base_sms_sms_datagrid').datagrid('load',{
    	telephone : $('#base_search_sms_telephone').textbox('getValue'),
    	templateId : $('#base_search_sms_template').textbox('getValue')
    });
}