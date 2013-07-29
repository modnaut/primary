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
		name: 'available'
	}]
});