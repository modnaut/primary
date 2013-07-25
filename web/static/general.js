Ext.Loader.setPath({
	Modnaut: 'static',
	'Ext.ux': 'static/extjs/ux'
});

Ext.require(['Ext.ux.layout.Center', 'Ext.ux.data.PagingMemoryProxy', 'Modnaut.view.NotificationBar']);

Ext.onReady(function(){
	Ext.tip.QuickTipManager.init();
	Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
	    minWidth: 100
	});
});

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
	config: {},
	events: ['SubmitForm'],
    eventListener: function(options) {
    	console.log('eventListener', options);
    	var component = options.component;
    	
    	switch(options.actionType) {
    		case 'submit':
    			var parameters = options.parameters || {};
    			
    			switch(options.eventType) {
    				case 'selectionchange':
    					var selection = options.arguments[1];
        				for(var r = 0, len = selection.length; r < len; r++) {
        					var record = selection[r];
        					var fields = record.fields.getRange();
        					for(var f = 0, flen = fields.length; f < flen; f++) {
        						parameters[fields[f].name + '-' + r ] = record.get(fields[f].name);
        					}
        				}
    					break;
    				case 'ok':
    					parameters.text = options.arguments[0];
    					break;
    				case 'select':
    					var record = options.arguments[1];
    					var idProperty = component.getStore().idProperty;
    					parameters[idProperty] = record.get(idProperty);
//    					var fields = record.fields.getRange();
//    					for(var f = 0, flen = fields.length; f < flen; f++) {
//    						parameters[fields[f].name] = record.get(fields[f].name);
//    					}
    					break;
    			}
    			
				Globals.submitForm({
					component: component,
					parameters: parameters,
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
    },
    encodeBase64: function(data) {
    	var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    	var o1, o2, o3, h1, h2, h3, h4, bits, i = 0, ac = 0, enc = "", tmp_arr = [];

    	if (!data) {
    		return data;
    	}

    	do { // pack three octets into four hexets
    		o1 = data.charCodeAt(i++);
    		o2 = data.charCodeAt(i++);
    		o3 = data.charCodeAt(i++);

    		bits = o1 << 16 | o2 << 8 | o3;

    		h1 = bits >> 18 & 0x3f;
    		h2 = bits >> 12 & 0x3f;
    		h3 = bits >> 6 & 0x3f;
    		h4 = bits & 0x3f;

    		// use hexets to index into b64, and append result to encoded string
    		tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
    	} while (i < data.length);

    	enc = tmp_arr.join('');

    	var r = data.length % 3;

    	return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);

    },
    decodeBase64: function(data) {
    	var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    	var o1, o2, o3, h1, h2, h3, h4, bits, i = 0, ac = 0, dec = "", tmp_arr = [];

    	if (!data) {
    		return data;
    	}

    	data += '';

    	do { // unpack four hexets into three octets using index points in b64
    		h1 = b64.indexOf(data.charAt(i++));
    		h2 = b64.indexOf(data.charAt(i++));
    		h3 = b64.indexOf(data.charAt(i++));
    		h4 = b64.indexOf(data.charAt(i++));

    		bits = h1 << 18 | h2 << 12 | h3 << 6 | h4;

    		o1 = bits >> 16 & 0xff;
    		o2 = bits >> 8 & 0xff;
    		o3 = bits & 0xff;

    		if (h3 == 64) {
    			tmp_arr[ac++] = String.fromCharCode(o1);
    		} else if (h4 == 64) {
    			tmp_arr[ac++] = String.fromCharCode(o1, o2);
    		} else {
    			tmp_arr[ac++] = String.fromCharCode(o1, o2, o3);
    		}
    	} while (i < data.length);

    	dec = tmp_arr.join('');

    	return dec;
    },
    LOG_LEVELS: {
    	trace:	1,
    	debug:	2,
    	info:	3,
    	warn:	4,
    	error:	5
    },
    _logToSql: function(level, message, data) {
    	if(!Ext.isDefined(Globals.LOG_LEVELS[level]))
    		level = 'debug';
    	
    	var encodedData = '';
    	if(data)
    		encodedData= Ext.JSON.encode(data);
    	
    	return;
    	Ext.Ajax.request({
			url: 'ApplicationServlet',
			params: {
				Class: 'com.modnaut.common.controllers.LoggingCtrl',
				Method: level,
				message: message,
				data: encodedData
			}
		});
    },
    trace: function(message, data) {
    	Ext.Function.defer(function() {
	    	console.log.apply(console, arguments);
	    	console.trace();
	    	Globals._logToSql('trace', message, data);
    	}, 100);
    },
    debug: function(message, data) {
    	Ext.Function.defer(function() {
	    	console.debug.apply(console, arguments);
	    	Globals._logToSql('debug', message, data);
    	}, 100);
    },
    info: function(message, data) {
    	Ext.Function.defer(function() {
	    	console.info.apply(console, arguments);
	    	Globals._logToSql('info', message, data);
    	}, 100);
    },
    warn: function(message, data) {
    	Ext.Function.defer(function() {
	    	console.warn.apply(console, arguments);
	    	Globals._logToSql('warn', message, data);
    	}, 100);
    },
    error: function(message, data) {
    	Ext.Function.defer(function() {
	    	console.error.apply(console, arguments);
	    	Globals._logToSql('error', message, data);
    	}, 100);
    },
    _TIMERS: {},
    time: function(name, level) {
    	console.time.apply(console, [name]);
    	Globals._TIMERS[name] = {
    		name: name,
    		level: level,
    		start: Ext.Date.now()
    	};
    },
    timeEnd: function(name) {
    	console.timeEnd.apply(console, [name]);
    	var timer = Globals._TIMERS[name];
    	if(timer) {
    		var duration = Ext.Date.now() - timer.start;
    		Globals._logToSql(timer.level, name + ': ' + duration + 'ms');
    		delete Globals._TIMERS[name];
    	}
    }
});



