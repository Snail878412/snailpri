Ext.define('ssip.view.main.EastPanel', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.eastpanel',
	defaults:{
		frame:true,
		style: {
			borderStyle: 'hidden'
		}
	},
	collapsed:true,
	collapsible:true,
	hidden:true,
	title:'infoPanel',
    items: []
});