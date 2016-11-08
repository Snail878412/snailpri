jsUtil = {};
/*
extjs frame 的背景颜色
*/
jsUtil.extjsBgColor = "#dfe9f6";
/**
 * 冒泡消息框
 * 		jsUtil.BubbleMsg.show(component,message,pausTime) 默认停留1秒
 * @author Snail
 */
jsUtil.BubbleMsg = {
	msgCt:null,
    createBox:function(s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc" style="font-size=12px;">', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    },
    show:function(cmp, message,pauseTime){
			var el=null;
			if(cmp instanceof  Ext.AbstractComponent){
				el = cmp.getEl();
			}else{
				el = cmp;
			}
            if(!this.msgCt){
                this.msgCt = Ext.DomHelper.insertFirst(el, {id:'bubleMsg-div21',style:'position:absolute;margin:0 auto;z-index:20000;'}, true);
            }
            this.msgCt.alignTo(el, 'c-c');
			var bubleMsgDiv = document.getElementById("bubleMsg-div21");
			var oldMsgBoxEl = bubleMsgDiv.getElementsByTagName("div");
			if(oldMsgBoxEl&&oldMsgBoxEl.length >0){
				bubleMsgDiv.removeChild(oldMsgBoxEl[0]);
			}
            var m = Ext.DomHelper.append(this.msgCt, {html:this.createBox(message)}, true);
			
			if(Ext.isEmpty(pauseTime)){
				pauseTime=1000;
			}
            m.fadeIn('t').fadeOut("b", {
				delay: pauseTime,
				remove: true,
				duration: 500
			});
        }
};