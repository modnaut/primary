app = Ext.application({
	name: 'Modnaut',
	appFolder: 'static/demoApp',
	launch: function() {
		Ext.create('Ext.container.Viewport', {
			layout: 'fit',
			items: [{
				xtype: 'panel',
				title: 'Users',
				html : 'List of users will go here'
			}]
		});
	}
});