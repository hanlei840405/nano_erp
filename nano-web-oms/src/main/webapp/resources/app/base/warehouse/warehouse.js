function baseWarehouseInit() {
	$('#base_warehouse_warehouse_datagrid')
			.datagrid(
					{
						title : '品牌信息',
						border : false,
						iconCls : 'icon-edit',// 图标
						width : 700,
						height : 'auto',
						nowrap : false,
						striped : true,
						border : true,
						ctrlSelect : true,
						collapsible : false,// 是否可折叠的
						fit : true,// 自动大小
						selectOnCheck : true,
						checkOnSelect : true,
						idField : 'id',
						url : 'base/warehouse/queryWarehouses',
						pagination : true,
						pageSize : 10,// 每页显示的记录条数，默认为10
						pageList : [ 10, 20, 50 ],// 可以设置每页记录条数的列表
						rownumbers : true,
						frozenColumns : [ [ {
							field : 'ck',
							checkbox : true
						} ] ],
						singleSelect : false,
						columns : [ [ {
							field : 'code',
							title : '编号',
							width : 200,
							align : 'center'
						}, {
							field : 'name',
							title : '名称',
							width : 200,
							align : 'center'
						}, {
							field : 'brandNames',
							title : '涵盖品牌',
							width : 200,
							align : 'center'
						}, {
							field : 'status',
							title : '状态',
							width : 200,
							align : 'center',
							formatter : function(value) {
								if (value == 1) {
									return '启用';
								} else {
									return '停用';
								}
							}
						} ] ],
						toolbar : [
								{
									text : '新增',
									iconCls : 'icon-add',
									handler : function() {
										$('#base_warehouse_edit_form').form(
												'reset');
										baseWarehouseBrandCombobox();
										$('#base_warehouse_saveOrUpdate_window')
												.window('open');
									}
								},
								'-',
								{
									text : '修改',
									iconCls : 'icon-edit',
									handler : function() {
										var selected = $(
												'#base_warehouse_warehouse_datagrid')
												.datagrid('getSelected');
										if (!selected) {
											$.notify("请先选择一条数据", "warn");
											return false;
										}
										$('#base_warehouse_id')
												.val(selected.id);
										$('#base_warehouse_code').textbox(
												'setValue', selected.code);
										$('#base_warehouse_name').textbox(
												'setValue', selected.name);
										$('#base_warehouse_status').combobox(
												'setValue', selected.status);
										baseWarehouseBrandCombobox(selected.brandIds);
										$('#base_warehouse_saveOrUpdate_window')
												.window('open');
									}
								},
								'-',
								{
									text : '删除',
									iconCls : 'icon-remove',
									handler : function() {
										var selections = $(
												'#base_warehouse_warehouse_datagrid')
												.datagrid('getSelections');
										if (!selections.length) {
											$.notify("请先选择一条数据", "warn");
											return false;
										}
										var array = new Array();
										for (var i = 0; i < selections.length; i++) {
											array.push(selections[i].id);
										}
										$.messager
												.confirm(
														'Confirm',
														'确定删除?',
														function(r) {
															if (r) {
																deleteSelectedRows(
																		'base/warehouse/delete',
																		array,
																		function() {
																			$(
																					'#base_warehouse_warehouse_datagrid')
																					.datagrid(
																							'reload');
																		});
															}
														});
									}
								} ]

					});
	// 设置分页控件
	var base_warehouse_warehouse_datagrid_pagination = $(
			'#base_warehouse_warehouse_datagrid').datagrid('getPager');
	$(base_warehouse_warehouse_datagrid_pagination).pagination({
		beforePageText : '第',// 页数文本框前显示的汉字
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
	$('#base_warehouse_warehouse_datagrid').datagrid('enableFilter');
}

/**
 * ajax方式提交表单
 */
function baseWarehouseSubmitForm() {
	$('#base_warehouse_edit_form').form('submit', {
		success : function(data) {
			data = $.parseJSON(data);
			$('#base_warehouse_saveOrUpdate_window').window('close');
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
			} else {
				$.notify("保存失败", "error");
			}
			$('#base_warehouse_warehouse_datagrid').datagrid('reload');
		}
	});
}

function baseWarehouseSearchForm() {
	$('#base_warehouse_warehouse_datagrid').datagrid('load', {
		code : $('#base_search_warehouse_code').val(),
		name : $('#base_search_warehouse_name').val(),
		status : $('#base_search_warehouse_status').combobox('getValue')
	});
}

function baseWarehouseBrandCombobox(values) {
	$('#base_warehouse_brand_id').combobox({
		url : 'base/brand/queryAllBrands',
		valueField : 'id',
		textField : 'name',
		panelHeight : 'auto',
		multiple : true,
		onLoadSuccess : function() {
			$('#base_warehouse_brand_id').combobox('clear');
			if (values) {
				$('#base_warehouse_brand_id').combobox('setValues', values);
			}
		}
	});
}