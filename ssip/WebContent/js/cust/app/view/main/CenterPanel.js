Ext.define('ssip.view.main.CenterPanel', {
    extend: 'Ext.tab.Panel',
	alias: 'widget.centerpanel',
	defaults:{
		frame:false,
		bodyStyle: {
			backgroundColor:jsUtil.extjsBgColor
		},
		border:false
	},
    items: [{
		xtype:'panel',
		title:'主页',
		icon:applicationHelper.ICON_BASE_PATH +'/home.gif'
	}],
	frame:true,
	border:true,
	style: {
		borderWidth:'1px',
		borderStyle: 'solid'
	},
	createTabPanel:function(record){
		var url = applicationHelper.APP_BASE_PATH + record.get("url") + applicationHelper.APP_URL_SUFFIX;
		var iconPath = applicationHelper.ICON_BASE_PATH;
		var icon = record.get('icon');
		if(icon){
			iconPath = icon;
		}else{
			iconPath += applicationHelper.DEFAULT_MENU_TAB_ICON;
		}
		return Ext.create('Ext.panel.Panel',{
			id : record.get("menuId"),
			title : record.get("menuName"),
			bodyStyle: {
				backgroundColor:jsUtil.extjsBgColor
			},
			border:false,
			frame : false,
			layout : 'border',
			icon:iconPath,
			tooltip:record.get("menuDesc"),
			loader : {
				url : url,
				loadMask : '正在加载页面.....',
				scripts : true,
				autoLoad : true
			},
			closable : true
		});
	},
	openOrCreateTab:function(record){
		var me = this;
		var url = record.get("url");
		if(!url){
			Ext.Msg.alert("提示","没有找到入口地址,请确认!");
			return;
		}
		//路径为完整的URL,则直接打开一个新的窗口.
		if (applicationHelper.URL_REGEXP.test(url)) {
			window.open(url);
			return;
		}
		
		var tabId = record.get("menuId");
		var tabObj = me.queryById(tabId);
		if(tabObj){ //已经打开了该页面,则直接设置为当前活动tab就可以了
			me.setActiveTab(tabObj);
			return;
		}
		//若没有打开该页面,则新增一个tab并设置为当前活动tab
		tabObj = me.createTabPanel(record);
		me.add(tabObj).show();
	}
});