Ext.define('LocalFoodConnection.view.Viewport', {
	extend: 'Ext.container.Viewport',
	requires: ['LocalFoodConnection.view.UserToolbar', 'LocalFoodConnection.view.AppToolbar'],
	layout: {
		type: 'fit'
	},
	items: [{
		xtype: 'panel',
		dockedItems: [{
			dock: 'top',
			dockWeight: 1,
			xtype: 'usertoolbar',
		}, {
			dock: 'top',
			dockWeight: 2,
			xtype: 'apptoolbar'
		}],
		html: ''
	}]
});