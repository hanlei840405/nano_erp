<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<div id="base_region_layout" class="easyui-layout" style="height: auto; width: 100%" fit="true">
    <div data-options="region:'center',border:false">
        <table id="base_region_region_tree">
        </table>
    </div>
</div>
<div id="base_region_saveOrUpdate_window" class="easyui-window"
     style="width: 20%;min-width: 200px; height: 200px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:200px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_region_edit_form" action="base/region/save" method="post">
                <input type="text" style="display:none;" name="id" id="base_region_id"/>
                <table cellpadding="5">
                    <tr>
                        <td>行政代码:</td>
                        <td><input name="code" id="base_region_code" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>行政名称:</td>
                        <td><input name="name" id="base_region_name" class="f1 easyui-textbox"/></td>
                    </tr>
                    <tr>
                        <td>隶属于:</td>
                        <td>
                            <input name="parentId" id="base_region_parent_id" class="easyui-combotree" style="width:100%"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseRegionSubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_region_edit_form')">重置</a>
        </div>
    </div>
</div>
<div id="base_region_upload_window" class="easyui-window"
     style="width: 20%;min-width: 200px; height: 200px"
     data-options="title:'编辑',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:200px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <form id="base_region_upload_form" action="base/region/upload" method="post" enctype="multipart/form-data">
                <input class="easyui-filebox" name="file" data-options="prompt:'选择行政区域EXCEL'" style="width:100%">
            </form>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseRegionUploadForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_region_upload_form')">重置</a>
        </div>
    </div>
</div>
<div id="base_region_express_priority_window" class="easyui-window"
     style="width: 400px;min-width: 200px; height: 200px"
     data-options="title:'快递优先级',iconCls:'icon-save',modal:true,closed:true,minimizable:false,maximizable:false,collapsible:false">
    <div class="easyui-layout" style="height:200px;" fit="true">
        <div data-options="region:'center'" style="min-height: 50px">
            <table id="base_region_express_datagrid">
            </table>
        </div>
        <div data-options="region:'south'" style="text-align: center; padding: 5px;height: 40px">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="baseRegionExpressPrioritySubmitForm()">提交</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm('base_region_upload_form')">重置</a>
        </div>
    </div>
</div>
<script>
    $(function () {
        baseRegionInit();
    });
</script>