Ext.application({
	name: 'DemoApp',
	appFolder: 'static/demoApp',
	controllers: ['Modnaut.controller.ViewMetaDataController'],
	launch: function() {
		Ext.create('Ext.container.Viewport', {
			layout: 'fit',
			items: [{
				xtype: 'vmdContainer',
				layout: 'fit',
				Class: 'com.modnaut.apps.helloworld.HelloWorldCtrl',
				Method: 'defaultAction'
			}]
		});
	}
});