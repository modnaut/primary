Ext.define('Modnaut.model.Market', {
	extend: 'Ext.data.Model',
    config: {
        fields: [
            {name: 'marketId'},
            {name: 'name'},
            {name: 'address1'},
            {name: 'city'},
            {name: 'state'},
            {name: 'zip'},
            {name: 'latitude'},
            {name: 'longitude'},
        ]
    }
});
