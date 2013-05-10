Ext.define('Modnaut.view.MarketList', {
    extend: 'Ext.dataview.DataView',
    xtype: 'marketList',
    requires: 'Modnaut.store.Markets',
    config: {
        itemTpl: [
                  	'<div>',
                  		'<h3 style="margin-bottom: 0px;">{name}</h3>',
                  		'<img src="https://maps.googleapis.com/maps/api/staticmap?center={latitude},{longitude}&sensor=false&zoom=13&size=100x100&markers={latitude},{longitude}"/>',
	                 '</div>'
        ],
        store: 'Markets'
    }
});
