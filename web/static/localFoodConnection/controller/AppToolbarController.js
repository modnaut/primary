Ext.define('LocalFoodConnection.controller.AppToolbarController', {
	extend: 'Ext.app.Controller',
	requires: ['LocalFoodConnection.view.Inventory'],
	init: function () {
		var controller = this;
		controller.control({
			'apptoolbar>button': {
				click: controller.menuButtonClicked
			}
		});
	},
	menuButtonClicked: function(button, event, opts) {
		console.log(arguments);
		return;
		Globals.fireEvent('openWindow', {
			Class: 'com.modnaut.apps.localfoodconnection.InventoryCtrl',
			Method: 'defaultAction',
			title: 'Inventory',
			width: Ext.Element.getViewportWidth() * 0.75,
			height: Ext.Element.getViewportHeight() * 0.75,
			xtype: 'inventory'
		});
	}
});