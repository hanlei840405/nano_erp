<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="base_product_layout" class="easyui-layout"
	style="height: auto; width: 100%" fit="true">
	<div data-options="region:'north',border:false">
		<table cellpadding="2">
			<tr>
				<td>简称:</td>
				<td><input id="base_search_product_shortName"
					class="f1 easyui-textbox" style="width:120px"/></td>
				<td>全称:</td>
				<td><input id="base_search_product_fullName"
					class="f1 easyui-textbox" style="width:120px"/></td>
				<td>货号:</td>
				<td><input id="base_search_product_code"
					class="f1 easyui-textbox" style="width:120px"/></td>
				<td>品牌:</td>
				<td><input id="base_search_product_brand"
					class="f1 easyui-combobox"
					data-options="url:'base/brand/queryAllBrands',
				        method:'get',
				        valueField:'id',
				        textField:'name'" style="width:120px"/>
				</td>
				<td>类目:</td>
				<td><input id="base_search_product_category"
					class="f1 easyui-combotree"
					data-options="url:'base/category/queryCategories',
				        method:'get',
				        valueField:'id',
				        treeField:'name'" style="width:150px"/>
				</td>
				<td>联系人:</td>
				<td><input id="base_search_product_contact"
					class="f1 easyui-combobox"
					data-options="url:'base/contact/queryAllContacts',
				        method:'get',
				        valueField:'id',
				        textField:'name'" style="width:120px"/>
				</td>
				<td><a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="baseProductSearchForm()">查询</a></td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false">
		<table id="base_product_product_datagrid">
		</table>
	</div>
</div>
<div id="base_product_saveOrUpdate_window" class="easyui-window"
	style="width: 500px; height: 250px"
	data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
	<div class="easyui-layout" style="height: 250px;" fit="true">
		<div data-options="region:'center'" style="min-height: 90px">
			<form id="base_product_edit_form" action="base/product/save"
				method="post">
				<input type="text" style="display: none;" name="id"
					id="base_product_id" />
				<table cellpadding="5">
					<tr>
						<td>简称:</td>
						<td><input name="shortName" id="base_product_shortName"
							class="f1 easyui-textbox" /></td>
						<td>全称:</td>
						<td><input name="fullName" id="base_product_fullName"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>货号:</td>
						<td><input name="code" id="base_product_code"
							class="f1 easyui-textbox" /></td>
						<td>品牌:</td>
						<td><select name="brandId" id="base_product_brand_id"
							class="f1 easyui-combobox" style="width: 100%;"></select></td>
					</tr>
					<tr>
						<td>类目:</td>
						<td><select name="categoryId" id="base_product_category_id"
							class="f1 easyui-combotree" style="width: 100%;"></select></td>
						<td>联系人:</td>
						<td><select name="contactId" id="base_product_contact_id"
							class="f1 easyui-combobox" style="width: 100%;"></select></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south'"
			style="text-align: center; padding: 5px; height: 40px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="baseProductSubmitForm()">提交</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				onclick="clearForm('base_product_edit_form')">重置</a>
		</div>
	</div>
</div>
<div id="base_product_sku_saveOrUpdate_window" class="easyui-window"
	style="width: 480px; min-width: 200px; height: 330px"
	data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
	<div class="easyui-layout" style="height: 270px;" fit="true">
		<div data-options="region:'center'" style="min-height: 90px">
			<input type="text" style="display: none;" id="base_product_row_index" />
			<form id="base_product_sku_edit_form" action="base/sku/save"
				enctype="multipart/form-data" method="post">
				<input type="text" style="display: none;" name="productId"
					id="base_product_sku_product_id" />
				<input type="text" style="display: none;" name="id"
					id="base_product_sku_id" />
				<table cellpadding="5">
					<tr>
						<td>名称:</td>
						<td><input name="name" id="base_product_sku_name"
							class="f1 easyui-textbox" /></td>
						<td>编码:</td>
						<td><input name="code" id="base_product_sku_code"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>条码:</td>
						<td><input name="barcode" id="base_product_sku_barcode"
							class="f1 easyui-textbox" /></td>
						<td rowspan="5" style="vertical-align: top;">图片:</td>
						<td><input type="file"
							name="file" id="base_product_sku_image" style="width:150px;"/>
						</td>
					</tr>
					<tr>
						<td>颜色:</td>
						<td><input name="color" id="base_product_sku_color"
							class="f1 easyui-textbox" /></td>
						<td rowspan="4">
							<img id="base_product_sku_image_view" width="150px"/>
						</td>
					</tr>
					<tr>
						<td>规格:</td>
						<td><input name="spec" id="base_product_sku_spec"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>指导价:</td>
						<td><input name="price" id="base_product_sku_price"
							class="f1 easyui-textbox" /></td>
					</tr>
					<tr>
						<td>成本:</td>
						<td><input name="cost" id="base_product_sku_cost"
							class="f1 easyui-textbox" /></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south'"
			style="text-align: center; padding: 5px; height: 40px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="baseProductSkuSubmitForm()">确认</a> <a href="javascript:void(0)"
				class="easyui-linkbutton"
				onclick="clearForm('base_product_sku_edit_form')">重置</a>
		</div>
	</div>
</div>
<script>
	$(function() {
		baseProductInit();
	});
</script>