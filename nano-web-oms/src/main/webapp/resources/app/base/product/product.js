function baseProductInit() {
	$('#base_product_product_datagrid')
			.datagrid(
					{
						title : '商品信息',
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
						url : 'base/product/queryProducts',
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
							field : 'shortName',
							title : '简称',
							width : 150,
							align : 'center'
						}, {
							field : 'fullName',
							title : '全称',
							width : 420,
							align : 'center'
						}, {
							field : 'code',
							title : '货号',
							width : 100,
							align : 'center'
						}, {
							field : 'brandName',
							title : '品牌',
							width : 100,
							align : 'center'
						}, {
							field : 'categoryName',
							title : '类目',
							width : 100,
							align : 'center'
						}, {
							field : 'shopName',
							title : '店铺',
							width : 100,
							align : 'center'
						}, {
							field : 'contactName',
							title : '联系人',
							width : 100,
							align : 'center'
						} ] ],
						toolbar : [
								{
									text : '新增',
									iconCls : 'icon-add',
									handler : function() {
										$('#base_product_edit_form').form(
												'reset');
										baseProductComboboxShow();
										$('#base_product_saveOrUpdate_window')
												.window('open');
									}
								},
								'-',
								{
									text : '修改',
									iconCls : 'icon-edit',
									handler : function() {
										var selected = $(
												'#base_product_product_datagrid')
												.datagrid('getSelected');
										if (!selected) {
											$.notify("请先选择一条数据", "warn");
											return false;
										}
										$('#base_product_id').val(selected.id);
										$('#base_product_shortName').textbox(
												'setValue', selected.shortName);
										$('#base_product_fullName').textbox(
												'setValue', selected.fullName);
										$('#base_product_code').textbox(
												'setValue', selected.code);
										baseProductComboboxShow(selected.id);
										$('#base_product_brand_id').combobox(
												'setValue', selected.brandId);
										$('#base_product_category_id')
												.combobox('setValue',
														selected.categoryId);
										$('#base_product_contact_id').combobox(
												'setValue', selected.contactId);
										$('#base_product_saveOrUpdate_window')
												.window('open');
									}
								},
								'-',
								{
									text : '删除',
									iconCls : 'icon-remove',
									handler : function() {
										var selections = $(
												'#base_product_product_datagrid')
												.datagrid('getSelections');
										console.log(selections);
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
																		'base/product/delete',
																		array,
																		function() {
																			$(
																					'#base_product_product_datagrid')
																					.datagrid(
																							'reload');
																		});
															}
														});
									}
								} ],
						view : detailview,
						detailFormatter : function(index, row) {
							return '<div style="padding:2px"><table class="ddv"></table></div>';
						},
						onExpandRow : function(index, row) {
							var ddv = $(this).datagrid('getRowDetail', index)
									.find('table.ddv');
							ddv
									.datagrid({
										url : 'base/sku/querySkus?productId='
												+ row.id,
										fitColumns : true,
										singleSelect : true,
										rownumbers : true,
										loadMsg : '',
										height : 'auto',
										columns : [ [ {
											field : 'code',
											title : '规格',
											width : 100,
											align : 'center'
										}, {
											field : 'name',
											title : '名称',
											width : 420,
											align : 'center'
										}, {
											field : 'barcode',
											title : '条码',
											width : 100,
											align : 'center'
										}, {
											field : 'color',
											title : '颜色',
											width : 100,
											align : 'center'
										}, {
											field : 'spec',
											title : '规格',
											width : 100,
											align : 'center'
										}, {
											field : 'price',
											title : '指导价',
											width : 100,
											align : 'center'
										}, {
											field : 'cost',
											title : '成本',
											width : 100,
											align : 'center'
										} ] ],
										toolbar : [
												{
													text : '新增',
													iconCls : 'icon-add',
													handler : function() {
														$(
																'#base_product_row_index')
																.val(index);
														$(
																'#base_product_sku_edit_form')
																.form('reset');
														$(
																'#base_product_sku_product_id')
																.val(row.id);
														$(
																'#base_product_sku_saveOrUpdate_window')
																.window('open');
													}
												},
												'-',
												{
													text : '修改',
													iconCls : 'icon-edit',
													handler : function() {
														var selected = ddv
																.datagrid('getSelected');
														if (!selected) {
															$.notify(
																	"请先选择一条数据",
																	"warn");
															return false;
														}
														$(
																'#base_product_row_index')
																.val(index);
														$(
																'#base_product_sku_product_id')
																.val(row.id);
														$(
																'#base_product_sku_id')
																.val(
																		selected.id);
														$(
																'#base_product_sku_name')
																.textbox(
																		'setValue',
																		selected.name);
														$(
																'#base_product_sku_code')
																.textbox(
																		'setValue',
																		selected.code);
														$(
																'#base_product_sku_barcode')
																.textbox(
																		'setValue',
																		selected.barcode);
														$(
																'#base_product_sku_color')
																.textbox(
																		'setValue',
																		selected.color);
														$(
																'#base_product_sku_spec')
																.textbox(
																		'setValue',
																		selected.spec);
														$(
																'#base_product_sku_price')
																.textbox(
																		'setValue',
																		selected.price);
														$(
																'#base_product_sku_cost')
																.textbox(
																		'setValue',
																		selected.cost);
														$('#base_product_sku_image_view').attr('src', 'image?path=' + selected.image);
														$(
																'#base_product_sku_saveOrUpdate_window')
																.window('open');
													}
												},
												'-',
												{
													text : '删除',
													iconCls : 'icon-remove',
													handler : function() {
														var selections = ddv
																.datagrid('getSelections');
														if (!selections.length) {
															$.notify(
																	"请先选择一条数据",
																	"warn");
															return false;
														}
														var array = new Array();
														for (var i = 0; i < selections.length; i++) {
															array
																	.push(selections[i].id);
														}
														$.messager
																.confirm(
																		'Confirm',
																		'确定删除?',
																		function(
																				r) {
																			if (r) {
																				deleteSelectedRows(
																						'base/sku/delete',
																						array,
																						function() {
																							ddv
																									.datagrid('reload');
																						});
																			}
																		});
													}
												} ],
										onResize : function() {
											$('#base_product_product_datagrid')
													.datagrid(
															'fixDetailRowHeight',
															index);
										},
										onLoadSuccess : function() {
											setTimeout(
													function() {
														$(
																'#base_product_product_datagrid')
																.datagrid(
																		'fixDetailRowHeight',
																		index);
													}, 0);
										}
									});
							$('#base_product_product_datagrid').datagrid(
									'fixDetailRowHeight', index);
						}
					});
	// 设置分页控件
	var base_product_product_datagrid_pagination = $(
			'#base_product_product_datagrid').datagrid('getPager');
	$(base_product_product_datagrid_pagination).pagination({
		beforePageText : '第',// 页数文本框前显示的汉字
		afterPageText : '页    共 {pages} 页',
		displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
	$('#base_product_product_datagrid').datagrid('enableFilter');
	$('#base_product_sku_image').change(function() {
		window.URL = window.URL || window.webkitURL;
		if (window.URL) {
			// File API
			$('#base_product_sku_image_view').attr('src', window.URL.createObjectURL(this.files[0]));
			$('#base_product_sku_image_view').on('load', function(e) {
				window.URL.revokeObjectURL(this.src); // 图片加载后，释放object URL
			});

		}
	});
}
/**
 * ajax方式提交表单
 */
function baseProductSubmitForm() {
	$('#base_product_edit_form').form('submit', {
		success : function(data) {
			data = $.parseJSON(data);
			$('#base_product_saveOrUpdate_window').window('close');
			if (data.yesOrNo) {
				$.notify("保存成功", "success");
			} else {
				$.notify("保存失败", "error");
			}
			$('#base_product_product_datagrid').datagrid('reload');
		}
	});
}

function baseProductSearchForm() {
	$('#base_product_product_datagrid').datagrid('load', {
		shortName : $('#base_search_product_shortName').val(),
		fullName : $('#base_search_product_fullName').val(),
		code : $('#base_search_product_code').val(),
		brandId : $('#base_search_product_brand').combobox('getValue'),
		categoryId : $('#base_search_product_category').combotree('getValue'),
		contactId : $('#base_search_product_contact').combobox('getValue'),
		shopId : $('#base_search_product_shop').combobox('getValue')
	});
}

function baseProductComboboxShow(val) {
	$('#base_product_brand_id').combobox({
		url : 'base/brand/queryAllBrands',
		method : 'get',
		valueField : 'id',
		textField : 'name',
		onLoadSuccess : function() {
			if (val) {
				$('#base_product_brand_id').combobox('setValue', val);
			}
		}
	});
	$('#base_product_category_id').combotree({
		url : 'base/category/queryCategories',
		method : 'get',
		valueField : 'id',
		treeField : 'name',
		onLoadSuccess : function() {
            $('#base_product_category_id').combotree('clear');
			if (val) {
				$('#base_product_category_id').combotree('setValue', val);
			}
		}
	});
	$('#base_product_contact_id').combobox({
		url : 'base/contact/queryAllContacts',
		method : 'get',
		valueField : 'id',
		textField : 'name',
		onLoadSuccess : function() {
            $('#base_product_contact_id').combobox('clear');
			if (val) {
				$('#base_product_contact_id').combobox('setValue', val);
			}
		}
	});
}

function baseProductSkuSubmitForm() {
	$('#base_product_sku_edit_form').form(
			'submit',
			{
				success : function(data) {
					data = $.parseJSON(data);
					$('#base_product_sku_saveOrUpdate_window').window('close');
					if (data.yesOrNo) {
						$.notify("保存成功", "success");
					} else {
						$.notify("保存失败", "error");
					}
					$('#base_product_product_datagrid').datagrid(
							'getRowDetail', $('#base_product_row_index').val())
							.find('table.ddv').datagrid('reload');
				}
			});
}