Ext.Loader.setConfig({
		enabled: true
});
var applicationHelper = {
	APP_URL_SUFFIX : ".ssip",
	APP_BASE_PATH : "",
	URL_REGEXP : new RegExp("^(https?|ftp|file)://.+$",'i'),
	ICON_BASE_PATH:"resource/icon/",
	DEFAULT_ROOT_MENU_ICON:'default_root_menu.png',
	DEFAULT_MENU_TAB_ICON:'default_menu_tab.gif'
};

Ext.application({
    name: 'ssip',
	appFolder:'js/cust/app',
    controllers: [
        'HomeCtrl'
    ],
	launch:function(){
		if(!applicationHelper.APP_BASE_PATH){
			applicationHelper.APP_BASE_PATH = Ext.getElementById("basePath").value;
		}
		this.controllers.addListener('add',this.newControllerAdded,this);
	},
	newControllerAdded:function(idx, ctrlr, token){
		ctrlr.init();
	},
	autoCreateViewport: true
});