Ext.define('LocalFoodConnection.controller.WindowController', {
	extend: 'Ext.app.Controller',
	init: function () {
		var controller = this;
		this.control({
			window: {
				destroy: controller.onWindowDestroy
			}
		});
		
		Globals.on('openWindow', controller.openWindow, controller);
	},
	windows: [],
	openWindow: function(opts) {
		var controller = this;
		
		var config = {
				autoShow: true,
				title: opts.title,
				width: opts.width || Ext.Element.getViewportWidth() * 0.75,
				height: opts.height || Ext.Element.getViewportHeight() * 0.75,
				layout: 'fit',
				singleton: {}
		};
		
		if(opts.xtype) {
			config.items = [{xtype: opts.xtype}];
			config.singleton.xtype = opts.xtype;
		} else if(opts.Class && opts.Method) {
			config.items = [{
				xtype: 'vmdContainer',
				Class: opts.Class,
				Method: opts.Method
			}];
			config.singleton.Class = opts.Class;
			config.singleton.Method = opts.Method;
		}
		
		var existingWindow = controller.findExistingWindow(config.singleton);
		
		if(existingWindow)
			existingWindow.toFront();
		else
			controller.windows.push(Ext.create('Ext.window.Window', config));
	},
	findExistingWindow: function(config) {
		var controller = this;
		var window = null;
		for(var i = 0, len = controller.windows.length; i < len; i++) {
			if(config.xtype && (config.xtype === controller.windows[i].singleton.xtype)) {
				window = controller.windows[i];
				break;
			}
			if(config.Class && config.Method && (config.Class === controller.windows[i].singleton.Class) && (config.Method === controller.windows[i].singleton.Method)) {
				window = controller.windows[i];
				break;
			}
		}
		return window;
	},
	onWindowDestroy: function(window) {
		var controller = this;
		Ext.Array.remove(controller.windows, window);
	}
});