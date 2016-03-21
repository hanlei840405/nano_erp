function baseExpressInit() {
    $('#base_express_express_datagrid').datagrid({
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
        url: 'base/express/queryExpresses',
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
            {field: 'contact', title: '联系人', width: 200, align: 'center'},
            {field: 'telephone', title: '电话', width: 200, align: 'center'},
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
                $('#base_express_edit_form').form('reset');
                baseExpressRegionTree(new Array());
            }
        }, '-', {
            text: '修改',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_express_express_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $('#base_express_id').val(selected.id);
                $('#base_express_code').textbox('setValue', selected.code);
                $('#base_express_name').textbox('setValue', selected.name);
                $('#base_express_contact').textbox('setValue', selected.contact);
                $('#base_express_telephone').textbox('setValue', selected.telephone);
            	var regionIds = selected.regionIds.concat();
                baseExpressRegionTree(regionIds);
            }
        }, '-', {
            text: '停用/启用',
            iconCls: 'icon-edit',
            handler: function () {
                var selected = $('#base_express_express_datagrid').datagrid('getSelected');
                if(!selected){
                    $.notify("请先选择一条数据", "warn");
                    return false;
                }
                $.ajax({
                	url : 'base/express/start-stop',
                	type : 'POST',
                	data : {id : selected.id},
                	dataType: "json",
                	success: function (data) {
                        if (data.yesOrNo) {
                            $.notify("保存成功", "success");
                        } else {
                            $.notify("保存失败", "error");
                        }
                        $('#base_express_express_datagrid').datagrid('reload');
                    }
                });
            }
        }, '-', {
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                var selections = $('#base_express_express_datagrid').datagrid('getSelections');
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
                        deleteSelectedRows('base/express/delete', array, function () {
                            $('#base_express_express_datagrid').datagrid('reload');
                        });
                    }
                });
            }
        }]

    });
    //设置分页控件
    var base_express_express_datagrid_pagination = $('#base_express_express_datagrid').datagrid('getPager');
    $(base_express_express_datagrid_pagination).pagination({
        beforePageText: '第',//页数文本框前显示的汉字
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });
    $('#base_express_express_datagrid').datagrid('enableFilter');
}
/**
 * ajax方式提交表单
 */
function baseExpressSubmitForm() {
    $('#base_express_edit_form').form('submit', {
    	onSubmit:function(param){
    		var checkeds = $('#base_express_region_tree').tree('getChecked');
    		var array = new Array();
    		for(var i = 0;i < checkeds.length;i++){
    			array.push(checkeds[i].id);
    		}
    		if(checkeds.length != 0){
    			param.regionIds = JSON.stringify(array);
    		}
    	},
        success: function (data) {
            data = $.parseJSON(data);
            $('#base_express_saveOrUpdate_window').window('close');
            if (data.yesOrNo) {
                $.notify("保存成功", "success");
            } else {
                $.notify("保存失败", "error");
            }
            $('#base_express_express_datagrid').datagrid('reload');
        }
    });
}

function baseExpressSearchForm(){
    $('#base_express_express_datagrid').datagrid('load',{
        code : $('#base_search_express_code').val(),
        name : $('#base_search_express_name').val(),
        status :$('#base_search_express_status').combobox('getValue')
    });
}

function baseExpressRegionTree(regionIds){
    $('#base_express_region_tree').tree({
    	url: 'base/region/queryRegions',
    	checkbox:true,
    	animate:true,
    	onLoadSuccess : function(node, data){
    		var nodes;
    		if(!node){
        		nodes = $('#base_express_region_tree').tree('getRoots');
    		}else{
    			nodes = $('#base_express_region_tree').tree('getChildren',node.target);
    		}

        	for(var j = regionIds.length - 1;j >= 0;j--){
        		for(var i = 0;i < nodes.length;i++){
                	if(nodes[i].id == regionIds[j]){
                		$('#base_express_region_tree').tree('check',nodes[i].target);
                		var a = regionIds.indexOf(regionIds[j]); 
                		regionIds.splice(a, 1);
                	}
                }
    		}
            $('#base_express_saveOrUpdate_window').window('open');
        }
    });
}