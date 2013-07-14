Ext.define('LocalConnection.view.Viewport', {
	extend: 'Ext.container.Viewport',
	requires: ['LocalConnection.view.UserToolbar', 'LocalConnection.view.AppToolbar'],
	layout: {
		type: 'fit'
	},
	items: [{
		xtype: 'panel',
		dockedItems: [{
			dock: 'top',
			dockWeight: 1,
			xtype: 'userToolbar',
		}, {
			dock: 'top',
			dockWeight: 2,
			xtype: 'appToolbar'
		}],
		html: ''
	}]
});