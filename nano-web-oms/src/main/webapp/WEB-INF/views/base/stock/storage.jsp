<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_storage_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>货号:</td>
                <td><input id="base_search_storage_product" class="f1 easyui-textbox"/></td>
                <td>条形码:</td>
                <td><input id="base_search_storage_barcode" class="f1 easyui-textbox"/></td>
                <td>仓库:</td>
                <td><input id="base_search_storage_warehouse" class="f1 easyui-combobox" data-options="url:'base/warehouse/queryAllWarehouses',valueField:'id',textField:'name'"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseStorageSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_storage_storage_datagrid">
        </table>
    </div>
</div>
<div id="base_storage_saveOrUpdate_window" class="easyui-window"
     style="width: 350px;min-width: 200px; height: 280px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:250px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_storage_edit_form" action="base/stock/storage/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_storage_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>批次号:</td>
                        <td><input name="no" id="base_storage_no" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>条形码:</td>
                        <td><input name="barcode" id="base_storage_barcode" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>仓库:</td>
                        <td><input name="warehouseId" id="base_storage_warehouse_id" class="f1 easyui-combobox" data-options="onShowPanel:baseStorageWarehouseCombobox"/></td>
                    </tr>
                    <tr>
                        <td>数量:</td>
                        <td>
                            <input name="quantity" id="base_storage_quantity" class="f1 easyui-textbox"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseStorageSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_storage_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseStorageInit();
    });
</script>