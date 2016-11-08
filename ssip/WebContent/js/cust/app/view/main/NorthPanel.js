Ext.define('ssip.view.main.NorthPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.menu.Menu'
     ],
	alias: 'widget.northpanel',
	defaults:{
		frame:false,
		bodyStyle: {
			backgroundColor:jsUtil.extjsBgColor
		},
		border:false
	},
	style: {
		borderWidth:'0px 0px 1px 0px',
		borderStyle: 'solid'
	},
	layout:{
		type:'hbox'
	},
	minHeight:80,
	height:80,
    items: [{
		xtype:'panel',
		width:'80%',
		height:'100%',
		padding:'20 0 10 20',
		layout:{
			type:'hbox'
		},
		
		items:[{
			style: {
				backgroundColor:jsUtil.extjsBgColor
			},
			xtype:'displayfield',
			value:'<div style="font:bold normal 22px 宋体; letter-spacing:5px"> 蜗牛系统集成平台 </div>',
			height:'100%'
		}]
	},{
		xtype:'panel',
		width:'20%',
		layout:{
			type:'hbox',
			pack:'center'
		},
		style: {
			backgroundColor:jsUtil.extjsBgColor
		},
		bodyStyle: {
			backgroundColor:jsUtil.extjsBgColor
		},
		height:'100%',
		padding:'50 0 0 20',
		items:[{
			xtype:'splitbutton',
			frame:true,
			shadow:'frame',
			text:'快捷操作',
			tooltip: '快捷操作菜单',
			menu: {
				items: [
					{text: '用户信息', handler: function(){ jsUtil.BubbleMsg.show(Ext.getBody(),"暂时未实现用户信息的功能,敬请期待"); }},
					{text: '修改密码', handler: function(){ jsUtil.BubbleMsg.show(Ext.getBody(),"暂时未实现修改密码的功能,敬请期待"); }},
					{text: '系统设置', handler: function(){ jsUtil.BubbleMsg.show(Ext.getBody(),"暂时未实现系统设置的功能,敬请期待"); }},
					{text: '安全退出', handler: function(){ jsUtil.BubbleMsg.show(Ext.getBody(),"暂时未实现安全退出的功能,敬请期待"); }}
				]
			},
			handler: function() {
				this.showMenu();
			}
		},{
			xtype:'tbspacer',
			width: 2
		},{
			xtype:'button',
			frame:true,
			text:'帮助',
			icon: applicationHelper.ICON_BASE_PATH + 'help.gif' ,
			tooltip: '获取帮助信息',
			handler: function(event, toolEl, panel){
				jsUtil.BubbleMsg.show(Ext.getBody(),"暂时未实现获取帮助信息的功能,敬请期待");
			}
		}]
	}]
});