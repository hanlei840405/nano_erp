<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_stock_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>货号:</td>
                <td><input id="base_search_stock_product" class="f1 easyui-textbox"/></td>
                <td>条形码:</td>
                <td><input id="base_search_stock_barcode" class="f1 easyui-textbox"/></td>
                <td>仓库:</td>
                <td><input id="base_search_stock_warehouse" class="f1 easyui-combobox" data-options="url:'base/warehouse/queryAllWarehouses',valueField:'id',textField:'name'"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseStockSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_stock_stock_datagrid">
        </table>
    </div>
</div>
<script>
    $(function () {
        baseStockInit();
    });
</script>