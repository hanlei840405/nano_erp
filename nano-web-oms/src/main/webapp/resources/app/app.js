/**
 * Created by ASUS on 2015/5/1.
 */
/**
 * 动态添加选项卡
 */
function addTab(node) {
	if (!node.url) {
		return;
	}
	if ($('#tt').tabs('exists', node.text)) {
		$('#tt').tabs('select', node.text);
	} else {
		$('#tt').tabs('add', {
			title : node.text,
			href : node.url,
			closable : true,
			cache : true,
			fit : true
		});
	}
}

function setTreeNodesChecked(tree,node,array){
	for(var i = 0;i < array.length;i++){
		if(node.id == array[i]){
			tree.tree('check',node.target);
			break;
		}
	}
	if(!tree.tree('isLeaf')){
		baseShopCategoryTreeNodes(node);
	}
}
