Ext.define('ssip.store.TreeMenuStore', {
    extend: 'Ext.data.TreeStore',
    autoLoad: false,
	clearOnLoad:false,
	clearRemovedOnLoad:false,
	nodeParam:'menuId',
    proxy: {
        type: 'ajax',
        url : 'test/getMenus.ssip'
    },
	listeners:{
		'load':function(thisStroe,node,records,successful,eOpts){
			Ext.Array.each(records,function(record){
				record.set('id',record.get("menuId"));
				var icon = record.get("icon");
				if(icon){
					record.set('icon',applicationHelper.ICON_BASE_PATH+record.get("icon"));
				}
				record.set('qtip',record.get("menuDesc"));
			});
		}
	},
	sorters: [{
        property :'menuIndex'
    }]
});