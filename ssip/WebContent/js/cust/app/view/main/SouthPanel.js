Ext.define('ssip.view.main.SouthPanel', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.southpanel',
	defaults:{
		frame:true,
		style: {
			borderStyle: 'hidden'
		}
	},
	style: {
		borderWidth:'1px 0px 0px 0px',
		borderStyle: 'solid'
	},
	height:50,
	html:'<div id="footer"><p>X ICP备xxxxxxxxx号  copyright©2014-2020 Snail <br>建议IE8.0,1024×768以上分辨率浏览本网站<br></p> </div></div>',
    items: []
});