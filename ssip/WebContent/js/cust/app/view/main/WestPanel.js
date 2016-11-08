Ext.define('ssip.view.main.WestPanel', {
    extend: 'Ext.panel.Panel',
	alias: 'widget.westpanel',
	defaults:{
		frame:false,
		bodyStyle: {
			backgroundColor:jsUtil.extjsBgColor
		},
		border:false
	},
	minWidth:180,
	width:180,
	collapsible:true,
	frame:true,
	border:true,
	style: {
		borderWidth:'1px',
		borderStyle: 'solid'
	},
	title:'系统菜单',
	layout: {
        type: 'accordion',
        titleCollapse: true,
        animate: false,
		fill:true,
		multi:false,
		collapseFirst:true,
		hideCollapseTool:true
    },
	tools:[{
		type:'refresh',
		tooltip: '系统菜单刷新',
		handler: function(event, toolEl, panel){
			var menuStore = this.up('panel').menuStore;
			if(menuStore){
				menuStore.load();
			}
		}
	}],
	menuStore:null,
	items:[],
	menuHandler:function(thisView, record,item,index, event, eOpts){
		var viewPort = this.up('viewport');
		if(!viewPort){
			jsUtil.BubbleMsg.show(Ext.getBody(),"没有找到viewport");
			return;
		}
		var centerPanel = viewPort.down('centerpanel');
		if(!centerPanel){
			jsUtil.BubbleMsg.show(Ext.getBody(),"没有找到centerpanel");
			return;
		}
		centerPanel.openOrCreateTab(record);
	},
	createChildItem:function(record){
		var iconPath = applicationHelper.ICON_BASE_PATH;
		var icon = record.get('icon');
		if(icon){
			iconPath = icon;
		}else{
			iconPath += applicationHelper.DEFAULT_ROOT_MENU_ICON;
		}
		var childMenuStore = Ext.create('ssip.store.TreeMenuStore', {
								defaultRootId:record.get('menuId'),
								root: {
									expanded: true
								}
							});
		var childMenu = Ext.create('Ext.tree.Panel', {
							title:record.get("menuName"),
							viewConfig:{
								frame:true
							},
							frameHeader:false,
							header:{
								style: {
									width: '95%',
									margin: '5px'
								}
							},
							store: childMenuStore,
							autoScroll:true,
							rowLines:true,
							frame:true,
							useArrows:true,
							icon:iconPath,
							rootVisible: false,
							displayField:'menuName',
							listeners:{
								'itemclick':this.menuHandler
							}
						});
		return childMenu;
	},
    initComponent:function() {
		var me = this;
		var myMask = new Ext.LoadMask(me, {msg:"正在刷新..."});
		this.menuStore = Ext.create('ssip.store.SystemMenuStore', {
			listeners:{
				'beforeload':function(){
					myMask.show();
				},
				'load':function(thisStore,records,successful,eOpts){
					if(!successful || successful != true){
						myMask.hide();
						return;
					}
					me.removeAll();
					var menuItems = [];
					records = Ext.Array.sort(records,function(a,b){
						return a.get("menuIndex") - b.get("menuIndex");
					});
					Ext.Array.each(records,function(item, index, countriesItSelf){
						menuItems.push(me.createChildItem(item));
					});
					me.add(menuItems); 
					myMask.hide();
				}
			}
		});
		me.callParent(arguments);
	}
});