Ext.define('Modnaut.data.XHRUpload', {
	mixins: {
        observable: 'Ext.util.Observable'
    },
	constructor: function(config) {
		var me = this;
		me.mixins.observable.constructor.call(me, config);
		Ext.apply(me, config, {
			method: 'POST',
			fileNameHeader: 'X-File-Name',
			filePostName: 'file',
			contentTypeHeader: 'text/plain; charset=x-user-defined-binary',
			extraParams: {},
			extraParamsPrefix: 'extraParam_',
			sendMultiPartFormData: false
		});
		me.addEvents('loadstart', 'progress', 'abort', 'error', 'load', 'loadend');
	},
	send: function(config) {
		var me = this;
		
		Ext.apply(me, config);
		
		me.xhr = new XMLHttpRequest();
		me.xhr.addEventListener('loadstart', Ext.Function.bind(me.relayXHREvent, me), false);
		me.xhr.addEventListener('progress', Ext.Function.bind(me.relayXHREvent, me), false);
		me.xhr.addEventListener('progressabort', Ext.Function.bind(me.relayXHREvent, me), false);
		me.xhr.addEventListener('error', Ext.Function.bind(me.relayXHREvent, me), false);
		me.xhr.addEventListener('load', Ext.Function.bind(me.relayXHREvent, me), false);
		me.xhr.addEventListener('loadend', Ext.Function.bind(me.relayXHREvent, me), false);
		
		me.xhr.upload.addEventListener('loadstart', Ext.Function.bind(me.relayUploadEvent, me), false);
		me.xhr.upload.addEventListener('progress', Ext.Function.bind(me.relayUploadEvent, me), false);
		me.xhr.upload.addEventListener('progressabort', Ext.Function.bind(me.relayUploadEvent, me), false);
		me.xhr.upload.addEventListener('error', Ext.Function.bind(me.relayUploadEvent, me), false);
		me.xhr.upload.addEventListener('load', Ext.Function.bind(me.relayUploadEvent, me), false);
		me.xhr.upload.addEventListener('loadend', Ext.Function.bind(me.relayUploadEvent, me), false);
		
		me.xhr.open(me.method, me.url, true);
		
		if(typeof(FileReader) !== 'undefined' && me.sendMultiPartFormData ){
			me.reader = new FileReader();
			me.reader.addEventListener('load', Ext.Function.bind(me.sendFileUpload, me), false);
			me.reader.readAsBinaryString(me.file);
		} else {
			me.xhr.overrideMimeType(me.contentTypeHeader);
			me.xhr.setRequestHeader(me.fileNameHeader, me.file.name);
			for(key in me.extraParams){
				me.xhr.setRequestHeader(me.extraParamsPrefix + key, me.extraParams[key]);
			}
//			xhr.setRequestHeader('X-File-Size', files.size); //this may be useful
			me.xhr.send(me.file);
		}
		return true;
	},
	relayXHREvent: function(event) {
		var me = this;
		me.fireEvent(event.type, event);
	},
	relayUploadEvent: function(event) {
		var me = this;
		me.fireEvent('upload' + event.type, event);
	},
	sendFileUpload: function() {
		var me = this;
		
		var boundary = (1000000000000+Math.floor(Math.random()*8999999999998)).toString();
		var data = '';
		
		for(key in me.extraParams){
			data += '--' + boundary + '\r\nContent-Disposition: form-data; name="' + key + '"\r\nContent-Type: text/plain;\r\n\r\n' + me.extraParams[key] + '\r\n';
		}
		
		data += '--' + boundary + '\r\nContent-Disposition: form-data; name="' + me.filePostName + '"; filename="' + me.file.name + '"\r\nContent-Type: ' + me.file.type + '\r\nContent-Transfer-Encoding: base64\r\n\r\n' + window.btoa(me.reader.result) + '\r\n' + '--' + boundary + '--\r\n\r\n';
		
		me.xhr.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);
		me.xhr.send(data);
	}
});