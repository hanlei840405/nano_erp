<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_dictionary_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'north',border:false">
        <table cellpadding="5">
            <tr>
                <td>编号:</td>
                <td><input id="base_search_dictionary_code" class="f1 easyui-textbox"/></td>
                <td>名称:</td>
                <td><input id="base_search_dictionary_name" class="f1 easyui-textbox"/></td>
                <td>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseDictionarySearchForm()">查询</a>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="base_dictionary_dictionary_datagrid">
        </table>
    </div>
</div>
<div id="base_dictionary_saveOrUpdate_window" class="easyui-window"
     style="width: 32%;min-width: 300px; height: 300px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:300px;" fit="true">
        <div data-options="region:'north'" style="min-height: 50px">
            <form id="base_dictionary_edit_form" action="base/dictionary/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_dictionary_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>编号:</td>
                        <td><input name="code" id="base_dictionary_code" class="f1 easyui-textbox"/></td>
                        <td>名称:</td>
                        <td><input name="name" id="base_dictionary_name" class="f1 easyui-textbox"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'center'">
            <table id="base_dictionary_item_datagrid"/>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseDictionarySubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_dictionary_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseDictionaryInit();
    });
</script>