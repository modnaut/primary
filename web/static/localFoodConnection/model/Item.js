Ext.define('LocalFoodConnection.model.Item', {
	extend: 'Ext.data.Model',
	fields: [{
		name: 'id',
		type: 'int'
	}, {
		name: 'product'
	}, {
		name: 'description'
	}, {
		name: 'category'
	}, {
		name: 'storage'
	}, {
		name: 'source'
	}, {
		name: 'unit'
	}, {
		name: 'active',
		type: 'boolean'
	}],
	proxy: {
		type: 'rest',
		url: 'ApplicationServlet',
		extraParams: {
			Class: 'com.modnaut.apps.localfoodconnection.inventory.Item',
			Method: 'rest'
		},
		reader: {
			type: 'json',
			root: 'data'
		}
	}
});