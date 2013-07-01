Ext.define('Modnaut.view.Uploader', {
	extend: 'Ext.container.Container',
	alias: 'widget.uploader',
	requires: ['Modnaut.view.UploaderGrid'],
	filePostName: 'file',
	standardUploadFilePostName: 'file',
	fileNameHeader: 'X-File-Name',
	fileId: 1,
	maxFileSize: 104857600,//100MB
	autoStartUpload: true,
	extraParams: {},
	sendMultiPartFormData: false,
	fileQueue: {},
	layout: 'fit',
	items: [{
		xtype: 'uploaderGrid'
	}]
});