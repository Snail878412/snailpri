Ext.define('ssip.model.SystemMenuModel', {
    extend: 'Ext.data.Model',
	fields: [
        {name: 'menuId',type: 'string'},//菜单ID
        {name: 'menuName',type: 'string'},//菜单名称
		{name: 'menuDesc',type: 'string'},//菜单描述
        {name: 'icon',type: 'string'},//菜单图标
		{name: 'menuIndex',type:'int'}
    ]
});