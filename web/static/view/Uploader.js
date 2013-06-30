Ext.define('Modnaut.view.Uploader', {
	extend: 'Ext.container.Container',
	alias: 'widget.uploader',
	requires: ['Modnaut.view.UploaderGrid'],
	filePostName: 'file',
	standardUploadFilePostName: 'file',
	fileNameHeader: 'X-File-Name',
	fileId: 1,
	maxFileSize: 3145728,//MB
	autoStartUpload: true,
	extraParams: {},
	sendMultiPartFormData: false,
	fileQueue: {},
	layout: 'fit',
	items: [{
		xtype: 'uploaderGrid'
	}]
});