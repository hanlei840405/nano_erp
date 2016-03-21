<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_category_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'center',border:false">
        <table id="base_category_category_tree">
        </table>
    </div>
</div>
<div id="base_category_saveOrUpdate_window" class="easyui-window"
     style="width: 20%;min-width: 200px; height: 200px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:200px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_category_edit_form" action="base/category/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_category_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>名称:</td>
                        <td><input name="name" id="base_category_name" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>上级类目:</td>
                        <td>
                            <input name="parentId" id="base_category_parent_id" class="easyui-combotree"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseCategorySubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_category_edit_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseCategoryInit();
    });
</script>