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
    eventListener: function(listenerArgs, element, eventType, actionType, params) {
    	switch(eventType) {
    		case 'selectionchange':
    			if(element.linkedFormId) {
    				var container = element.up('vmdContainer');
    				var form = container.down('form#' + element.linkedFormId);
    				if(form) {
    					form.loadRecord(listenerArgs[1][0]);
    				}
    			}
    			break;
    	}
    }
});