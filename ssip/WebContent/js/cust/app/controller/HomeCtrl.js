Ext.define('ssip.controller.HomeCtrl', {
    extend: 'Ext.app.Controller',
    models: [],
    stores: [],
	views:[
		'main.WestPanel',
		'main.EastPanel',
		'main.CenterPanel',
		'main.NorthPanel',
		'main.SouthPanel'
	],
    init: function() {
    }
});