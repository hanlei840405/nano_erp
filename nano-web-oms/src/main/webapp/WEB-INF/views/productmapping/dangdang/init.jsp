<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="product_mapping_dang_dang_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>店铺:</td>
                <td><input name="shopId" id="product_mapping_search_dang_dang_shop_id"/></td>
                <td>平台货号:</td>
                <td><input id="product_mapping_search_dang_dang_outer_id" class="f1 easyui-textbox"/></td>
                <td>平台条形码:</td>
                <td><input id="product_mapping_search_dang_dang_barcode" class="f1 easyui-textbox"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="productMappingDangDangSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="product_mapping_dang_dang_dang_dang_datagrid">
        </table>
    </div>
</div>
<div id="product_mapping_dang_dang_fetch_window" class="easyui-window"
     style="width: 20%;min-width: 200px; height: 250px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:250px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <input type="text" style="display:none;" id="product_mapping_dang_dang_platform_id" value='<c:out value="${platformId }"/>'/>
            <form id="product_mapping_dang_dang_fetch_form" action="productMapping/dangDang/fetch" method="post">
                <input type="text" style="display:none;" name="id" id="product_mapping_dang_dang_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>店铺:</td>
                        <td><input name="shopId" id="product_mapping_dang_dang_shop_id" class="f1 easyui-combobox"/></td>
                    </tr>
                    <tr>
                        <td>本地货号:</td>
                        <td><input name="outerId" id="product_mapping_dang_dang_outer_id" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>本地条形码:</td>
                        <td><input name="barcode" id="product_mapping_dang_dang_barcode" class="f1 easyui-textbox"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="productMappingDangDangFetchSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('product_mapping_dang_dang_fetch_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        productMappingDangDangInit();
    });
</script>