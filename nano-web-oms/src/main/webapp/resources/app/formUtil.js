/**
 * Created by Administrator on 2015/5/12.
 */
function clearForm(form_id) {
    $('#' + form_id).form('reset');
}

function deleteSelectedRows(url,records,callback){
    $.ajax({
        url : url,
        emthod : 'POST',
        data : {ids : records},
        dataType : 'json',
        success : function(data){
            if(data.yesOrNo){
                $.notify("删除成功", "success");
                callback();
            }else{
                $.notify("删除失败 : " + data.errorMsg, "error");
            }
        }
    });
}