<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="easyui-layout" style="height: 100%; width: 100%" fit="true">
    <div data-options="region:'center',border:false">
        <table id="system_menu_menu_tree"/>
    </div>
</div>
<div id="system_menu_saveOrUpdate_window" class="easyui-window"
     style="width: 58%; height: 400px"
     data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false,title:'编辑'">
    <div class="easyui-layout" style="height:400px;" fit="true">
        <div data-options="region:'north'" style="min-height: 50px">
            <form id="system_menu_edit_form" action="system/menu/save" method="post">
                <input type="text" style="display:none;" name="id" id="system_menu_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>上级菜单:</td>
                        <td>
                            <input name="parentId" id="system_menu_parent_id" class="easyui-combotree"
                                   data-options="url:'system/menu/queryAllMenusAndFunctions?initFunctions=false',method:'get'"/>
                        </td>
                        <td>菜单名称:</td>
                        <td>
                            <input name="name" id="system_menu_name" class="f1 easyui-textbox"/>
                        </td>
                        <td>排序:</td>
                        <td>
                            <input name="position" id="system_menu_position" class="easyui-numberspinner"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'">
            <div class="easyui-tabs" data-options="region:'west',fit:true,border:false">
                <div title="命令">
                    <table id="system_menu_function_datagrid"/>
                </div>
                <div title="资源">
                    <table id="system_menu_element_datagrid"/>
                </div>
            </div>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="systemMenuSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('system_menu_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        systemMenuInit();
    });
</script>