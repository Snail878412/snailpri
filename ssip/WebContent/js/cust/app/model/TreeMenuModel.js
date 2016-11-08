Ext.define('ssip.model.TreeMenuModel', {
    extend: 'Ext.data.Model',
	fields: [
        {name: 'menuId',type: 'string'},//菜单ID
        {name: 'menuName',type: 'string'},//菜单名称
		{name: 'menuDesc',type: 'string'},//菜单描述
        {name: 'icon',type: 'string'},//菜单图标
        {name: 'children'},//子菜单
		{name: 'id',mapping: 'menuId'},
		{name: 'menuIndex',type:'int'},
		{name: 'leaf',convert:function(v, record){
			if(!record){
				return true;
			}
			var children = record.get("children");
			return (!children || children.length < 1);
		}}
    ]
});