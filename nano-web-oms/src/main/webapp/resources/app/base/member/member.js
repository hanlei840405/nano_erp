function baseMemberInit() {
    $('#base_member_member_datagrid').datagrid({
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
        url: 'base/member/queryMembers',
        pagination: true,
        pageSize: 10,//每页显示的记录条数，默认为10
        pageList: [10, 20, 50],//可以设置每页记录条数的列表
        rownumbers: true,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        singleSelect: false,
        columns: [[
            {field: 'account', title: '账户', width: 150, align: 'center'},
            {field: 'realName', title: '姓名', width: 150, align: 'center'},
            {field: 'nick', title: '昵称', width: 150, align: 'center'},
            {field: 'outlay', title: '总花费', width: 150, align: 'center'},
            {field: 'vantages', title: '积分', width: 150, align: 'center'},
            {field: 'telephone', title: '电话', width: 150, align: 'center'},
            {field: 'mobile', title: '手机', width: 150, align: 'center'},
            {field: 'mail', title: '电子邮箱', width: 150, align: 'center'},
            {field: 'qq', title: 'QQ', width: 150, align: 'center'},
            {field: 'msn', title: 'MSN', width: 150, align: 'center'},
            {field: 'address', title: '地址', width: 400, align: 'center'}
        ]],
        toolbar: [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                $('#base_member_edit_form').form('reset');
                $('#base_member_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_member_member_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_member_id').val(selected.id);
                $('#base_member_realName').textbox('setValue', selected.realName);
                $('#base_member_nick').textbox('setValue', selected.nick);
                $('#base_member_account').textbox('setValue', selected.account);
                $('#base_member_mail').textbox('setValue', selected.mail);
                $('#base_member_telephone').textbox('setValue', selected.telephone);
                $('#base_member_mobile').textbox('setValue', selected.mobile);
                $('#base_member_qq').textbox('setValue', selected.qq);
                $('#base_member_msn').textbox('setValue', selected.msn);
                $('#base_member_address').textbox('setValue', selected.address);
                $('#base_member_saveOrUpdate_window').window('open');
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_member_member_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('base/member/delete', array, function () {
                            $('#base_member_member_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_member_member_datagrid_pagination = $('#base_member_member_datagrid').datagrid('getPager');
    $(base_member_member_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_member_member_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseMemberSubmitForm() {
    $('#base_member_edit_form').form('submit', {
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_member_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_member_member_datagrid').datagrid('reload');
        }
    });
}

function baseMemberSearchForm(){
    $('#base_member_member_datagrid').datagrid('load',{
    	realName : $('#base_search_member_realName').val(),
    	nick : $('#base_search_member_nick').val(),
    	telephone : $('#base_search_member_telephone').val(),
    	mobile : $('#base_search_member_mobile').val(),
    	qq : $('#base_search_member_qq').val(),
    	mail : $('#base_search_member_mail').val(),
    	msn : $('#base_search_member_msn').val(),
    	account : $('#base_search_member_account').val()
    });
}