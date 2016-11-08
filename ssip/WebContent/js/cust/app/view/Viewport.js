Ext.define('ssip.view.Viewport', {
    extend: 'Ext.container.Viewport',
	requires: [
		'Ext.layout.container.Border'
	],
    layout: 'border',
	defaults:{
		frame:false,
		bodyStyle: {
			backgroundColor:jsUtil.extjsBgColor
		},
		border:false
	},
    items: [{
		region:'west',
        xtype: 'westpanel',
		margin:'2 0 2 2'
	},{
		xtype:'eastpanel',
		margin:'2 2 2 10',
		region:'east'
    },{
		xtype:'centerpanel',
		margin:'2 2 2 10',
		region:'center'
	},{
		xtype:'northpanel',
		region:'north'
	},{
		xtype:'southpanel',
		margin:'2 2 2 0',
		region:'south'
	}]
});