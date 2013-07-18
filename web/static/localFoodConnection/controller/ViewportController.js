Ext.define('LocalFoodConnection.controller.ViewportController', {
	extend: 'Ext.app.Controller',
	init: function () {
		var controller = this;
		this.control({
		});
		
		Globals.on('openWindow', controller.openWindow);
	},
	openWindow: function(opts) {
		Ext.create('Ext.window.Window', {
			autoShow: true,
			title: opts.title,
			width: opts.width,
			height: opts.height,
			items: [{
				xtype: opts.xtype
			}]
		});
	}
});