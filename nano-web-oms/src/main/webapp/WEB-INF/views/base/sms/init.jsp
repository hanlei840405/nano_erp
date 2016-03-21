<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_sms_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>电话:</td>
                <td><input id="base_search_sms_telephone" class="f1 easyui-textbox"/></td>
                <td>模板:</td>
                <td><input id="base_search_sms_template" class="f1 easyui-combobox" data-options="url:'base/smsTemplate/queryAllSmsTemplates',valueField:'id',textField:'name'"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseSmsSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_sms_sms_datagrid">
        </table>
    </div>
</div>
<div id="base_sms_view_window" class="easyui-window"
     style="width: 350px;min-width: 200px; height: 280px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:250px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_sms_edit_form" action="base/sms/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_sms_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>电话:</td>
                        <td><input id="base_sms_telephone" class="f1 easyui-textbox" data-options="readonly:true"/></td>
                    </tr>
                    <tr>
                        <td>模板:</td>
                        <td><input id="base_sms_templateName" class="f1 easyui-textbox" data-options="readonly:true"/></td>
                    </tr>
                    <tr>
                        <td>内容:</td>
                        <td>
                            <input id="base_sms_content" class="f1 easyui-textbox" data-options="multiline:true,readonly:true" style="height:100px"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseSmsInit();
    });
</script>