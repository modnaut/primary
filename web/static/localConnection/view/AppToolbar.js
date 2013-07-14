Ext.define('LocalConnection.view.AppToolbar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.appToolbar',
	requires: ['LocalConnection.view.Search'],
	cls: 'appToolbar',
	defaults: {
		xtype: 'button',
		scale: 'large'
	},
	items: [{
		text: 'Dashboard',
		itemId: 'dashboard',
		iconCls: 'icon-chart_pie'
	}, {
		text: 'Inventory',
		itemId: 'inventory',
		iconCls: 'icon-chart_organisation'
	}, {
		text: 'Reports',
		itemId: 'reports',
		iconCls: 'icon-chart_line'
	}, {
		text: 'Contacts',
		itemId: 'contacts',
		iconCls: 'icon-vcard'
	}, '->', {
		xtype: 'search'
	}]
});