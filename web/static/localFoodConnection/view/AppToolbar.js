Ext.define('LocalFoodConnection.view.AppToolbar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.apptoolbar',
	requires: ['LocalFoodConnection.view.Search'],
	cls: 'appToolbar',
	defaults: {
		xtype: 'button',
		scale: 'large'
	},
	initComponent: function() {
		this.items = Globals.config.mainMenu.menu.concat(this.items);//push on config menu items
		this.callParent(arguments);
	},
	items: ['->', {
		xtype: 'search'
	}]
});