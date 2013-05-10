Ext.define('Modnaut.store.Markets', {
    extend: 'Ext.data.Store',
    requires: 'Modnaut.model.Market',
    config: {
    	storeId: 'Markets',
    	model: 'Modnaut.model.Market',
    	autoLoad: true,
	    proxy: {
	    	type: 'ajax',
	    	url: '/ApplicationServlet',
	    	extraParams: {
	    		Class: 'com.modnaut.apps.farmarkets.MarketListCtrl',
	    		Method: 'getMarketsJson'
	    	},
	    	reader: {
	    		type: 'json',
	    		root: 'markets'
	    	}
	    }
    }
});
