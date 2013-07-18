Ext.define('LocalFoodConnection.view.UserToolbar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.usertoolbar',
	cls: 'userToolbar',
	items: ['->', {
		xtype: 'tbtext',
		text: 'Welcome AwesomeFarm - Danny Cohn',
		style: {
			color: 'white'
		}
	}, {
		xtype: 'button',
		text: 'My Account',
		itemId: 'myAccount'
	}, '|', {
		xtype: 'button',
		text: 'Support',
		itemId: 'support'
	}, '|', {
		xtype: 'button',
		text: 'Logout',
		itemId: 'logout'
	}]
});