<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'west'" style="width: 200px; padding: 10px;">
        <ul class="easyui-tree" id="system_user_department_tree" data-options="url: 'system/department/queryDepartments'"></ul>
    </div>
    <div data-options="region:'center',border:false">
        <table id="system_user_user_datagrid">
        </table>
    </div>
</div>
<div id="system_user_saveOrUpdate_window" class="easyui-window"
     style="width: 40%; height: 400px"
     data-options="iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false,title:'初始密码：123456'">
    <div class="easyui-layout" style="height:400px;" fit="true">
        <div data-options="region:'north'" style="min-height: 90px">
            <form id="system_user_edit_form" action="system/user/save" method="post">
                <input type="text" style="display:none;" name="id" id="system_user_id"/>
                <input type="text" style="display:none;" name="roleIds" id="system_user_role_ids"/>
                <table cellpadding="5">
                    <tr>
                        <td>部门:</td>
                        <td><input name="departmentId" id="system_user_department_id" class="easyui-combotree"
                                   data-options="url:'system/department/queryDepartments',method:'get'"/></td>
                        <td>工号:</td>
                        <td><input name="code" id="system_user_code" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>姓名:</td>
                        <td><input name="name" id="system_user_name" class="f1 easyui-textbox"/></td>
                        <td>锁定用户:</td>
                        <td>
                            <select id="system_user_locked" class="f1 easyui-combobox" name="locked"
                                    style="width:150px">
                                <option value="false">否</option>
                                <option value="true">是</option>
                            </select>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'">
            <table id="system_user_role_datagrid"/>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="systemUserSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        systemUserInit();
    });
</script>