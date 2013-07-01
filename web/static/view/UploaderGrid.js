Ext.define('Modnaut.view.UploaderGrid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.uploaderGrid',
	tbar: [{
		text: 'Start Upload',
		itemId: 'startUpload'
	}, {
		text: 'Abort',
		itemId: 'abort'
	}, {
		text: 'Abort All',
		itemId: 'abortAll'
	}, {
		text: 'Remove',
		itemId: 'remove'
	}, {
		text: 'Remove All',
		itemId: 'removeAll'
	}],
	selModel: {
		selType: 'rowmodel',
		mode: 'multi'
	},
	store: {
		fields: ['id', 'fileName', 'size', 'status', 'progress'],
		idProperty: 'id'
	},
	columns: [{
		text: 'File Name',
		dataIndex: 'fileName',
		flex: 1
	}, {
		text: 'Size',
		dataIndex: 'size',
		flex: 1
	}, {
		text: 'Status',
		dataIndex: 'status',
		flex: 1
	}, {
		text: 'Progress',
		dataIndex: 'progress',
		flex: 1
	}]
});