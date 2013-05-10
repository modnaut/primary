Ext.define('Modnaut.view.Home', {
    extend: 'Ext.tab.Panel',
    xtype: 'home',
    requires: [
		'Modnaut.view.MarketList',
		'Modnaut.view.ContactUs'
	],
    config: {
        tabBarPosition: 'bottom',
        items: [{
            xtype: 'marketList',
        	title: 'Market List',
        	iconCls: 'user'
        }, {
        	xtype: 'contactUs',
        	title: 'Contact',
        	iconCls: 'user'
        }]
    }
});
