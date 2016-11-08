Ext.define('ssip.store.SystemMenuStore', {
    extend: 'Ext.data.Store',
	model: 'ssip.model.SystemMenuModel',
    autoLoad: true,
    proxy: {
        type: 'ajax',
        url : 'test/getMenus.ssip'
    },
	sorters: [{
        property :'menuIndex'
    }]
});