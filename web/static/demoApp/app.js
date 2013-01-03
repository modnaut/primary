window.app = window.app = Ext.create('Ext.app.Application', {
	name: 'DemoApp',
	appFolder: 'static/demoApp',
	controllers: ['Modnaut.controller.ViewMetaDataController'],
	launch: function() {
		Ext.create('Ext.container.Viewport', {
			layout: 'fit',
			items: [{
				xtype: 'vmdContainer',
				layout: 'fit',
				class: 'com.modnaut.apps.helloworld.HelloWorldChangeCtrl',
				method: 'defaultAction'
			}]
		});
	}
});