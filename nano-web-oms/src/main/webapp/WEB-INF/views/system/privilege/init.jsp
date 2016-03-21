<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'center',border:false">
        <table id="system_privilege_privilege_datagrid">
        </table>
    </div>
</div>
<div id="system_privilege_saveOrUpdate_window" class="easyui-window"
     style="width: 300px; height: 250px"
     data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <form id="system_privilege_edit_form" action="system/privilege/save" method="post">
        <input type="text" style="display:none;" name="id" id="system_privilege_id"/>
        <table cellpadding="5">
            <tr>
                <td>类别:</td>
                <td>
                    <input class="easyui-combobox" id="system_privilege_category" name="category" data-options="
                        valueField: 'value',
                        textField: 'label',
                        required: true,
                        onSelect : systemPrivilegeCategorySelectItem,
                        data: [{
                            label: '菜单权限',
                            value: 'MENU'
                        },{
                            label: '命令权限',
                            value: 'FUNCTION'
                        },{
                            label: '资源权限',
                            value: 'ELEMENT'
                        }]"/>
                </td>
            </tr>
            <tr>
                <td>菜单/命令/资源:</td>
                <td><input name="relatedIds" id="system_privilege_relatedId"
                           class="easyui-combotree"/></td>
            </tr>
            <tr>
                <td>编号:</td>
                <td><input name="code" id="system_privilege_code" class="f1 easyui-textbox"/></td>
            </tr>
            <tr>
                <td>名称:</td>
                <td><input name="name" id="system_privilege_name" class="f1 easyui-textbox"/></td>
            </tr>
        </table>
        <div style="text-align: center; padding: 5px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="systemPrivilegeSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('system_privilege_edit_form')">重置</a>
        </div>
    </form>
</div>
<script>
    $(function () {
        systemPrivilegeInit();
    });
</script>