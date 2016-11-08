 Ext.define('Ext.snail.components.formula.VariableEditPanel', {
	extend:'Ext.window.Window',
	alias: 'widget.variableeditpanel',
    alternateClassName: 'Ext.VariableEdit',
	modal:true,
	closable:false,
	layout:'vbox',
	header :false,
	variableData:null,
	defaults:{
		labelWidth:60,
		width:200,
		padding:'10 10 0 10'
	},
	items:[{
		xtype:'textfield',
		fieldLabel:'变量名称'
	},{
		xtype:'numberfield',
		fieldLabel:'默认值',
		hideTrigger:true,
		decimalPrecision:10
	}],
	buttonAlign:'center',
	closeAction:'destroy',
	callBackFun:null,
	buttons:[{
		text:'确定',
		handler:function(){
			var me = this.up('window');
			var variableName = me.down('textfield').getValue();
			var	variableDefualtValue = me.down('numberfield').getValue();
			if(!variableName){
				Ext.Msg.alert('提示信息','请输入变量名称');
				return;
			}
			me.variableData.name = variableName;
			me.variableData.defaultValue = variableDefualtValue;
			if(me.callBackFun){
				me.callBackFun();
			}
			me.close();
		}
	},{
		text:'取消',
		handler:function(){
			var me = this.up('window');
			me.close();
		}
	}],
	listeners:{
		'show':function(){
			var me = this;
			if(me.variableData){
				me.down('textfield').setValue(me.variableData.name);
				me.down('numberfield').setValue(me.variableData.defaultValue);
			}
		}
	}
 });
Ext.define('Ext.snail.components.formula.VariablePanel', {
	extend:'Ext.panel.Panel',
    alias: 'widget.variablepanel',
    alternateClassName: 'Ext.Variable',
	header:false,
	width: 200,
	minHeight:30,
	frame:true,
	layout:'hbox',
	variableData:{},
	initComponent:function(){
		Ext.snail.components.formula.VariablePanel.superclass.initComponent.call(this);
		this.variableData = {};
		this.addEvents("dblclick");
	},
	onRender: function(ct, position){
		  Ext.snail.components.formula.VariablePanel.superclass.onRender.apply(this, arguments);
		  var c = this.body;
		  c.on("dblclick", this.dblclick, this);
	},
	dblclick:function(e){
		var me = this;
		me.enterEdit();
	},
	enterEdit:function(){
		var varPanel = this;
		Ext.create('Ext.snail.components.formula.VariableEditPanel',{
			variableData:varPanel.variableData,
			callBackFun:function(){
				var me = this;
				varPanel.variableData = me.variableData;
				varPanel.updateVal();
			}
		}).center().show();
	},
	updateVal:function(){
		var me = this;
		var newValue = '变量:'+me.variableData.name;
		if(me.variableData.defaultValue || me.variableData.defaultValue == 0){
			newValue += " default:" + me.variableData.defaultValue;
		}
		me.down('displayfield').setValue(newValue);
	},
	items:[{
		xtype:'displayfield',
		value:''
	}]
 });
 
 Ext.define('Ext.snail.components.formula.VariablesPanel', {
	extend:'Ext.panel.Panel',
    alias: 'widget.variablespanel',
    alternateClassName: 'Ext.Variables',
	width:250,
	frame:true,
	layout:'border',
	variables:[],
	items:[{
		xtype:'panel',
		height:60,
		region:'north',
		frame:true,
		buttons:[{
			text:'添加',
			handler:function(){
				var nextPanel = this.up('panel').next('panel');
				var lastItem = nextPanel.items.last();
				if(!lastItem || lastItem.variableData.name){
					var varPanel = new Ext.snail.components.formula.VariablePanel();
					nextPanel.add(varPanel);
					this.up('panel').up('panel').variables.push(varPanel);
					varPanel.enterEdit();
				}else{
					lastItem.enterEdit();
				}
			}
		},{
			text:'移除',
			tooltip:'移除最后一个变量',
			handler:function(){
				var nextPanel = this.up('panel').next('panel');
				var lastItem = nextPanel.items.last();
				if(lastItem){
					nextPanel.remove(lastItem);
					this.up('panel').up('panel').variables.pop();
				}else{
					Ext.Msg.alert("提示","已经没有可移除的了.");
				}
			}
		}]
	},{
		xtype:'panel',
		region:'center',
		frame:true,
		autoScroll:true,
		defaults:{
			margin:'5 10 5 10'
		},
		layout:'vbox'
	}],
	getVariables:function(){
		var me = this;
		var variableObjs = [];
		Ext.Array.each(me.variables,function(item){
			if(item.variableData && item.variableData.name){
				variableObjs.push(item.variableData);
			}
		});
		return variableObjs;
	}
 });

Ext.define('Ext.snail.components.ArrowLine', {
	extend:'Ext.draw.Component',
    alias: 'widget.arrowline',
    alternateClassName: 'Ext.ArrowLine',
	viewBox: false,
	minWidth:40,
	minHeight:20,
	resizable:true,
	initComponent: function() {
        this.callParent(arguments);
		this.drawChild();
	},
	drawChild:function(){
		var width = this.width||this.minWidth;
		var height = this.height||this.minHeight;
		var linePath = "";
		var arrowLine = "";
		var halfHeight = height/2.0;
		linePath = "M0 "+ halfHeight + " L"+(width-10)+" "+ halfHeight;
		arrowLine = "M"+(width-10)+" " + (halfHeight-5) +" L"+ (width-10) + " " + (halfHeight+5) + " L" + width + " " + halfHeight + " z";
		this.items = [{
			type: "path",
			path: linePath,
			stroke: "blue",
			fill: "blue"
		}, {
			type: "path",
			path: arrowLine,
			stroke: "blue",
			fill: "blue",
		}];
		if(this.surface){
			this.surface.removeAll();
			this.surface.setSize(width, height);
			this.surface.add(this.items);
		}
	},
	listeners:{
		'resize':function(thisComp, width,height, oldWidth, oldHeight, eOpts){
			if(!(oldWidth+ oldHeight) || ((width+height) == (oldWidth+ oldHeight))){
				return;
			}
			this.width = width;
			this.height = height;
			this.drawChild();
		}
	}
 });
 
Ext.define('Ext.snail.components.canvas.ArrowLine', {
	extend:'Ext.panel.Panel',
    alias: 'widget.canvasarrowline',
    alternateClassName: 'Ext.canvas.ArrowLine',
	html: '<canvas></canvas>',
	header:false,
	lineStyle:"#0000aa",
	arrowStyle:"#0000aa",
	bodyStyle:"border:none;background-color:#dfe9f6",
	resizable:true,
	borders: false,
	plain: true,
	text:'',
	drawChild:function(){
		var canvas = this.el.dom.childNodes[0].firstChild;
		if(!canvas){
			return;
		}
        var ctx = canvas.getContext("2d");
		canvas.width = this.width || 40;
		canvas.height = this.height || 20;
		var halfHeight = canvas.height/2.0;
		var lineWidth = canvas.width-12;
		ctx.beginPath();
		ctx.strokeStyle = this.lineStyle;
		ctx.moveTo(0,halfHeight);
        ctx.lineTo(lineWidth,halfHeight);
		ctx.stroke();
		
		ctx.beginPath();
		ctx.strokeStyle = this.arrowStyle;
		ctx.fillStyle = this.arrowStyle;
		ctx.moveTo(lineWidth,(halfHeight-5));
        ctx.lineTo(lineWidth,(halfHeight+5));
		ctx.lineTo(canvas.width-1,halfHeight);
		ctx.lineTo(lineWidth,(halfHeight-5));
		ctx.fill();  
		ctx.stroke();
		
		this.drawText(canvas);
	},
	drawText:function(canvas){
		if(!canvas || !this.text){
			return;
		}
		var halfHeight = canvas.height/2.0;
		var lineWidth = canvas.width-12;
		var ctx = canvas.getContext("2d");
		ctx.fillStyle = "red";  
        ctx.fillText(this.text, (lineWidth/2.0), (halfHeight+3)); 
	},
	onRender:function(){
		this.superclass.onRender.call(this); 
		this.drawChild();
	},
	listeners:{
		'resize':function(thisComp, width,height, oldWidth, oldHeight, eOpts){
			if(!(oldWidth + oldHeight) || (( width + height) == (oldWidth + oldHeight))){
				return;
			}
			this.width = width;
			this.height = height;
			this.drawChild();
		}
	}
});

Ext.define('Ext.snail.components.canvas.Panel', {
	extend:'Ext.panel.Panel',
    alias: 'widget.canvaspanel',
    alternateClassName: 'Ext.canvas.Panel',
	viewBox: false,
	resizable:true,
	layout:'absolute',
	header:false,
	initComponent: function() {
		var canvasId = this.getId()+"-canvas";
		this.html = "<canvas id='"+canvasId+"'></canvas>"
        this.callParent(arguments);
		var canvas ;
	},
	bgColor:"#dfe9f6",
	text:'',
	getCanvas:function(){
		if(!this.canvas){
			this.canvas = document.getElementById(this.getId()+"-canvas");
		}
		return this.canvas;
	},
	drawChilds:function(){
		if(!this.getCanvas()){
			return;
		}
		//画布背景
		this.drawBg(0, 0,this.canvas.width, this.canvas.height,this.bgColor);
		//画直线
		this.drawLine(10,10,50,10,"#0000DD");
		//画箭头线
		this.drawArrow(50,5,50,15,65,10,"#0000DD","#0000DD");
		this.drawText();
	},
	drawBg:function(left,top,width,height,bgColor){
		if(!this.canvas){
			return;
		}
		var ctx = this.canvas.getContext("2d");
		ctx.fillStyle = bgColor;
		ctx.fillRect(left, top,width,height);
		ctx.stroke();
	},
	drawLine:function(startX,startY,endX,endY,lineColor){
		if(!this.canvas){
			return;
		}
		var ctx = this.canvas.getContext("2d");
		ctx.beginPath();
		ctx.strokeStyle = lineColor;
		ctx.moveTo(startX,startY);
        ctx.lineTo(endX,endY);
		ctx.stroke();
	},
	drawArrow:function(x1,y1,x2,y2,x3,y3,lineColor,fillColor){
		if(!this.canvas){
			return;
		}
		var ctx = this.canvas.getContext("2d");
		ctx.beginPath();
		ctx.strokeStyle = lineColor;
		ctx.fillStyle = fillColor;
		ctx.moveTo(x1,y1);
        ctx.lineTo(x2,y2);
		ctx.lineTo(x3,y3);
		ctx.lineTo(x1,y1);
		ctx.fill();  
		ctx.stroke();
	},
	drawText:function(x,y,text,fontColor){
		if(!this.canvas || !text){
			return;
		}
		var ctx = this.canvas.getContext("2d");
		ctx.fillStyle = fontColor;  
        ctx.fillText(text, x, y); 
	},
	onRender:function(){
		this.superclass.onRender.call(this); 
		if(!this.getCanvas()){
			return;
		}
		this.canvas.width = this.width || 40;
		this.canvas.height = this.height || 20;
		this.drawChilds();
	},
	listeners:{
		'resize':function(thisComp, width,height, oldWidth, oldHeight, eOpts){
			if(!this.getCanvas()){
				return;
			}
			if((( width + height) == (this.canvas.width+this.canvas.height))){
				return;
			}
			this.canvas.width = width;
			this.canvas.height = height;
			this.drawChilds();
		}
	}
});
 
Ext.onReady(function(){
    /*(Ext.create('Ext.container.Viewport',{
		layout:'border',
		items: [{
			xtype: 'variablespanel',
			region:'west'
		},{
			xtype: 'panel',
			region:'center',
			frame:true,
			layou:'vbox'
		}]
	});
	*/
	/*Ext.create('Ext.container.Viewport', {
		renderTo: Ext.getBody(),
		layout:'fit',
		items: [{
			xtype: 'panel',
            layout:'hbox',
			frame:true,
            items: [{
				xtype:'arrowline',
				width:200,
				height:40
			},{
				xtype:'panel',
				header:false,
				width:200,
				height:40,
				html:"test"
			}]
		}]
	});*/
	Ext.create('Ext.container.Viewport', {
		renderTo: Ext.getBody(),
		layout:'fit',
		items: [{
			xtype: 'canvaspanel',
			items:[{
				xtype:'panel',
				width:100,
				height:100,
				x:100,
				y:100,
				html:'test',
				frame:true,
				header:false
			}]
		}]
	});
});
