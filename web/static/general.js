Ext.Loader.setPath({
	Modnaut: 'static',
	'Ext.ux': 'static/extjs/ux'
});

Ext.require(['Ext.ux.layout.Center', 'Ext.ux.data.PagingMemoryProxy']);


//Avoid 'console' errors in browsers that lack a console.
(function() {
	var method;
	var noop = function () {};
	var methods = [
		'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
		'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
		'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
		'timeStamp', 'trace', 'warn'
	];
	var length = methods.length;
	var console = (window.console = window.console || {});
	
	while (length--) {
		method = methods[length];
		// Only stub undefined methods.
		if (!console[method]) {
			console[method] = noop;
		}
	}
}());


Ext.define('Globals', {
    singleton: true,
    mixins: {
		observable: 'Ext.util.Observable'
	},
	constructor: function(config){
		this.callParent(arguments);
    	this.mixins.observable.constructor.call(this, config);
	},
	events: ['SubmitForm'],
    eventListener: function(options) {
    	console.log('eventListener', options);
    	var component = options.component;
    	switch(options.actionType) {
    		case 'submit':
				Globals.submitForm({
					component: component,
					parameters: options.parameters,
					itemsToUpdate: options.itemsToUpdate
				});
				break;
    		case 'loadRecord':
    			if(options.eventType == 'selectionchange') {
    				var form = component.up('form');
    				if(form) {
    					var fields = form.query('[isFormField=true]');
    					Ext.each(fields, function(field){
    						field.suspendEvents(false);
    					});
    					form.loadRecord(options.arguments[1][0]);
    					Ext.each(fields, function(field){
    						field.resumeEvents();
    					});
    				}
    			}
    			break;
    	}
    },
    submitForm: function(options) {
    	Globals.fireEvent('SubmitForm', options);
    }
});