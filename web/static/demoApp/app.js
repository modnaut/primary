Ext.application({
	name: 'DemoApp',
	appFolder: 'static/demoApp',
	controllers: ['Modnaut.controller.ViewMetaDataController'],
	launch: function() {
		Ext.History.init(function(){
//			Ext.History.on('change', function(token) {
//				Globals.fireEvent('HistoryChange', token);
//			});
		});
		
		
		
		Ext.create('Ext.container.Viewport', {
			layout: 'fit',
			items: [{
				xtype: 'vmdContainer',
				layout: 'fit',
				Class: 'com.modnaut.apps.farmarkets.MarketListCtrl',
				Method: 'defaultAction',
				parameters: {
					hashPath: window.location.hash.substring(1)
				}
			}]
		});
	}
});