<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_warehouse_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>编号:</td>
                <td><input id="base_search_warehouse_code" class="f1 easyui-textbox"/></td>
                <td>名称:</td>
                <td><input id="base_search_warehouse_name" class="f1 easyui-textbox"/></td>
                <td>状态:</td>
                <td>
                    <select id="base_search_warehouse_status" class="f1 easyui-combobox"
                            style="width:150px">
                        <option value=""></option>
                        <option value="1">启用</option>
                        <option value="0">停用</option>
                    </select>
                </td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseWarehouseSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_warehouse_warehouse_datagrid">
        </table>
    </div>
</div>
<div id="base_warehouse_saveOrUpdate_window" class="easyui-window"
     style="width: 20%;min-width: 200px; height: 250px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:250px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_warehouse_edit_form" action="base/warehouse/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_warehouse_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>编号:</td>
                        <td><input name="code" id="base_warehouse_code" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>名称:</td>
                        <td><input name="name" id="base_warehouse_name" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>涵盖品牌:</td>
                        <td>
                            <input name="brandIds" id="base_warehouse_brand_id" class="f1 easyui-combobox"/>
                        </td>
                    </tr>
                    <tr>
                        <td>状态:</td>
                        <td>
                            <select id="base_warehouse_status" class="f1 easyui-combobox" name="status" data-options="editable:false,panelHeight:'auto'"
                                    style="width:150px">
                                <option value="1">启用</option>
                                <option value="0">停用</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseWarehouseSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_warehouse_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseWarehouseInit();
    });
</script>