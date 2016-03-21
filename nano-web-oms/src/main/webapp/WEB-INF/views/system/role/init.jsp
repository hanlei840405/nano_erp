<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="system_role_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'center',border:false">
        <table id="system_role_role_datagrid">
        </table>
    </div>
    <div data-options="region:'east',title:'East',split:false,collapsed:true" style="width: 300px;">
        <ul class="easyui-tree" id="system_role_privilege_tree" data-options="url: 'system/menu/queryAllMenusAndFunctions?initFunctions=true'"></ul>
    </div>
    <!--
    <div data-options="region:'east',title:'East',split:false,collapsed:true" style="width: 300px;" id="system_role_layout_privilege_according">
    </div>
    -->
</div>
<div id="system_role_saveOrUpdate_window" class="easyui-window"
     style="width: 40%; height: 400px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:400px;" fit="true">
        <div data-options="region:'north'" style="min-height: 50px">
            <form id="system_role_edit_form" action="system/role/save" method="post">
                <input type="text" style="display:none;" name="id" id="system_role_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>编号:</td>
                        <td><input name="code" id="system_role_code" class="f1 easyui-textbox"/></td>
                        <td>名称:</td>
                        <td><input name="name" id="system_role_name" class="f1 easyui-textbox"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'">
            <table id="system_role_privilege_datagrid"/>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="systemRoleSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('system_role_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        systemRoleInit();
    });
</script>