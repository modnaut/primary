Ext.application({
	name: 'LocalFoodConnection',
	appFolder: 'static/localFoodConnection',
	controllers: [
	              	'Modnaut.controller.ViewMetaDataController',
	              	'Modnaut.controller.UploaderController',
	              	'LocalFoodConnection.controller.ViewportController',
	              	'LocalFoodConnection.controller.WindowController',
	              	'LocalFoodConnection.controller.AppToolbarController'
	],
	requires: ['LocalFoodConnection.model.Item'],
	autoCreateViewport: true,
	launch: function() {
		Ext.util.CSS.swapStyleSheet('', '/static/css/localFoodConnection.css');
	}
});