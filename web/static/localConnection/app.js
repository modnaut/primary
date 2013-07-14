Ext.application({
	name: 'LocalConnection',
	appFolder: 'static/localConnection',
	controllers: ['Modnaut.controller.ViewMetaDataController', 'Modnaut.controller.UploaderController'],
	autoCreateViewport: true,
	launch: function() {
		Ext.util.CSS.swapStyleSheet('', '/static/css/localConnection.css');
	}
});