Ext.define('LocalFoodConnection.view.Inventory', {
	extend: 'Ext.container.Container',
	alias: 'widget.inventory',
	requires: ['LocalFoodConnection.model.Item'],
	layout: {
		type: 'vbox',
		align: 'stretch',
		pack:'start'
	},
	items: [{
		xtype: 'container',
		layout: {
			type: 'hbox',
			align: 'start',
			pack: 'center'
		},
		margin: '40 0',
		items: [{
			xtype: 'button',
			text: 'Add New Product',
			margin: '0 40'
		}, {
			xtype: 'textfield',
			emptyText: 'Search'
		}, {
			xtype: 'button',
			text: 'List',
			enableToggle: true,
			toggleGroup: 'inventoryViewType',
			pressed: true,
			margin: '0 0 0 40'
		}, {
			xtype: 'button',
			text: 'Grid',
			enableToggle: true,
			toggleGroup: 'inventoryViewType'
		}]
	}, {
		xtype: 'grid',
		columns: [{
			text: 'ID',
			dataIndex: 'id'
		}, {
			text: 'Product',
			dataIndex: 'product'
		}],
		store: {
			model: 'LocalFoodConnection.model.Item',
			data: [{
				id: 1,
				product: 'Abalone',
				description: 'Edible mushroom native to East Asia. Buna shimeji',
				category: 'Mushroom',
				storage: 'Fresh',
				source: 'Foraged',
				unit: 'Pounds (lbs)',
				Qty: '20'
			}, {
				id: 2
			}]
		}
	}]
});