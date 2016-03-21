<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_sms_template_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>编号:</td>
                <td><input id="base_search_sms_template_code" class="f1 easyui-textbox"/></td>
                <td>名称:</td>
                <td><input id="base_search_sms_template_name" class="f1 easyui-textbox"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseSmsTemplateSearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_sms_template_sms_template_datagrid">
        </table>
    </div>
</div>
<div id="base_sms_template_saveOrUpdate_window" class="easyui-window"
     style="width: 350px;min-width: 200px; height: 280px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:250px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_sms_template_edit_form" action="base/smsTemplate/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_sms_template_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>编号:</td>
                        <td><input name="code" id="base_sms_template_code" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>名称:</td>
                        <td><input name="name" id="base_sms_template_name" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>内容:</td>
                        <td>
                            <input name="template" id="base_sms_template_template" class="f1 easyui-textbox" data-options="multiline:true" style="height:100px"/>
                        </td>
                        <td width="110px">客户姓名、时间等动态信息使用【%s】占位</td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseSmsTemplateSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_sms_template_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseSmsTemplateInit();
    });
</script>