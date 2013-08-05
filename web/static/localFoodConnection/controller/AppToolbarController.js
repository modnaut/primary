Ext.define('LocalFoodConnection.controller.AppToolbarController', {
	extend: 'Ext.app.Controller',
	init: function () {
		var controller = this;
		controller.control({
			'apptoolbar>button': {
				click: controller.menuButtonClicked
			}
		});
	},
	menuButtonClicked: function(button, event, opts) {
		Globals.fireEvent('openWindow', {
			Class: button.Class,
			Method: button.Method,
			title: button.text,
			singleton: true
		});
	}
});