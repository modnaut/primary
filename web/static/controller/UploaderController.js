Ext.define('Modnaut.controller.UploaderController', {
	extend: 'Ext.app.Controller',
	requires: ['Modnaut.data.XHRUpload'],
	views: ['Modnaut.view.Uploader', 'Modnaut.view.UploaderGrid'],
	init: function () {
		var controller = this;
		this.control({
			'uploader': {
				afterrender: function(uploader) {
					controller.initDNDUpload(uploader);
					controller.initStandardUpload(uploader);
				}
			},
			'uploader button#startUpload': {
				click: function(button, event, opts) {
					var uploader = button.up('uploader');
					controller.startAll(uploader);
				}
			},
			'uploader button#abortAll': {
				click: function(button, event, opts) {
					var uploader = button.up('uploader');
					controller.abortAll(uploader);
				}
			},
			'uploader button#abort': {
				click: function(button, event, opts) {
					var uploader = button.up('uploader');
					var selection = controller.getGridSelection(uploader);
					for(var i = 0, len = selection.length; i < len; i++) {
						controller.abort(uploader.fileQueue[selection[i].get('id')]);
					}
				}
			},
			'uploader button#removeAll': {
				click: function(button, event, opts) {
					var uploader = button.up('uploader');
					controller.removeAll(uploader);
				}
			},
			'uploader button#remove': {
				click: function(button, event, opts) {
					var uploader = button.up('uploader');
					var selection = controller.getGridSelection(uploader);
					for(var i = 0, len = selection.length; i < len; i++) {
						controller.remove(uploader.fileQueue[selection[i].get('id')]);
					}
				}
			}
		});
		
	},
	initDNDUpload: function(uploader) {
		var controller = this;
		uploader.el.on({
			dragenter: function(event) {
				event.browserEvent.dataTransfer.dropEffect = 'move';
				return true;
			},
			dragover: function(event) {
				event.browserEvent.dataTransfer.dropEffect = 'move';
				event.stopEvent();
				return true;
			},
			drop: function(event) {
				controller.handleDrop(uploader, event);
			}
		});
		
		//prevent dropping files anywhere other than on the file uploader. Good idea?
		if(!document.body.BodyDragCaptureSet) {
			document.body.BodyDragCaptureSet = true;
			Ext.fly(document.body).on({
				dragenter: function(event) {
					return true;
				},
				dragleave: function(event) {
					return true;
				},
				dragover: function(event) {
					event.stopEvent();
					return true;
				},
				drop: function(event) {
					event.stopEvent();
					return true;
				}
			});
		}
	},
	initStandardUpload: function(uploader) {
		var controller = this;
		if(uploader.standardUploader){
//			uploader.standardUploader.fileInputEl = null; //remove reference to file field. necessary to prevent destroying file field during an active upload.
//			Ext.destroy(uploader.standardUploader);
			uploader.standardUploader.hide();
		}

		uploader.standardUploader = new Ext.ux.form.FileUploadField({
			renderTo: uploader.items.first().el.dom,
			buttonOnly: true,
			name: uploader.standardUploadFilePostName,
			listeners: {
				change: function(fileInput, fileName, opts) {
					controller.standardUploadFileSelected(uploader, fileInput, fileName);
				}
			}
		});
	},
	handleDrop: function(uploader, event) {
		var controller = this;
		
		event.stopEvent();
		var files = event.browserEvent.dataTransfer.files;
		
		if(files === undefined) {
			return true;
		}
		
		for(var i = 0, len = files.length; i < len; i++) {
			controller.processDNDFileUpload(uploader, files[i]);
		}
	},
	processDNDFileUpload: function(uploader, file) {
		var controller = this;
		
		var fileInfo = {
			id:	uploader.fileId++,
			fileName: file.name,
			size: file.size,
			status: 'Pending',
			progress: 0,
			method: 'dnd',
			file: file
		};
		
		if(fileInfo.size > uploader.maxFileSize) {
			controller.alert(uploader, file.name + ' File size exceeds allowed limit');
			return true;
		}
		
		uploader.fileQueue[fileInfo.id] = fileInfo;
		controller.addFileToGrid(uploader, fileInfo);
		if(uploader.autoStartUpload) {
			controller.dndUploadStart(uploader, fileInfo);
		}
	},
	dndUploadStart: function(uploader, fileInfo) {
		var controller = this;
		
		var upload = new Modnaut.data.XHRUpload({
			url: uploader.url,
			filePostName: uploader.filePostName,
			fileNameHeader: uploader.fileNameHeader,
			extraParams: uploader.extraParams,
			sendMultiPartFormData: uploader.sendMultiPartFormData,
			file: fileInfo.file,
			listeners: {
				loadstart: function(event) {
					fileInfo.status = 'Sending';
					controller.updateGrid(uploader, fileInfo);
				},
				uploadProgress: function(event) {
					fileInfo.progress = Math.round((event.loaded / event.total) * 100);
					controller.updateGrid(uploader, fileInfo);
				},
				load: function(event) {
					console.log(arguments);
					fileInfo.status = 'Complete';
					controller.updateGrid(uploader, fileInfo);
				}
			}
		});
		fileInfo.upload = upload;
		upload.send();
	},
	processUploadResult: function(uploader, fileInfo, serverData) {
		var controller = this;
		fileInfo.status = 'Completed';
	},
	standardUploadFileSelected: function(uploader, fileInput, fileName) {
		var controller = this;
		
		var domInput = fileInput.extractFileInput();
		var fileInfo = {
			id: uploader.fileId++,
			fileName: domInput.files[0].name,
			status: 'Pending',
			method: 'standard',
			size: domInput.files[0].size,
			progress: 0
		};
		
		if(fileInfo.size > uploader.maxFileSize){
			controller.alert(uploader, fileInfo.fileName + ' File size exceeds allowed limit');
			return true;
		}
		
		fileInfo.domInput = domInput;
		
		var formEl = uploader.items.items[0].el.appendChild(document.createElement('form'));
		var extraPost;
		
		
		Ext.get(domInput).addCls('uploader');
		Ext.get(domInput).addCls('hidden');
		formEl.appendChild(domInput);
		Ext.get(formEl).addCls('uploader');
		Ext.get(formEl).addCls('hidden');
		fileInfo.form = formEl;
		
		controller.initStandardUpload(uploader); //re-init uploader for multiple simultaneous uploads
		
		uploader.fileQueue[fileInfo.id] = fileInfo;
		controller.addFileToGrid(uploader, fileInfo);
		if(uploader.autoStartUpload){
			controller.standardUploadStart(uploader, fileInfo);
		}
	},
	standardUploadStart: function(uploader, fileInfo) {
		var controller = this;
		controller.doFormUpload(uploader, fileInfo);
		fileInfo.status = 'Sending';
	},
	getGrid: function(uploader) {
		var grid = false;
		gridQuery = uploader.query('uploaderGrid');
		if(gridQuery.length === 1)
			{
			grid = gridQuery[0];
			}
		return grid;
	},
	getGridSelection: function(uploader) {
		var controller = this;
		var selection = [];
		var grid = controller.getGrid(uploader);
		if(grid) {
			selection = grid.getSelectionModel().getSelection();
		}
		return selection;
	},
	addFileToGrid: function(uploader, fileInfo) {
		var controller = this;
		var grid = controller.getGrid(uploader);
		if(grid) {
			grid.getStore().add({
				id: fileInfo.id,
				fileName: fileInfo.fileName,
				size: fileInfo.size,
				status: fileInfo.status,
				progress: fileInfo.progress
			});
		}
	},
	updateGrid: function(uploader, fileInfo) {
		var controller = this;
		var grid = controller.getGrid(uploader);
		if(grid) {
			var record = grid.getStore().getById(fileInfo.id);
			record.set({
				fileName: fileInfo.fileName,
				size: fileInfo.size,
				status: fileInfo.status,
				progress: fileInfo.progress
			});
			record.commit();
		}
	},
	startAll: function(uploader) {
		var controller = this;
		for(id in uploader.fileQueue) {
			fileInfo = uploader.fileQueue[id];
			if(fileInfo.status === 'Pending' || fileInfo.status === 'Aborted') {
				switch(fileInfo.method) {
					case 'dnd':
						controller.dndUploadStart(uploader, fileInfo);
						break;
					case 'standard':
						controller.standardUploadStart(uploader, fileInfo);
						break;
				}
			}
		}
	},
	abortAll: function(uploader) {
		var controller = this;
		for(id in uploader.fileQueue) {
			controller.abort(uploader, uploader.fileQueue[id]);
		}
	},
	abort: function(uploader, fileInfo) {
		var controller = this;
		if(fileInfo.status === 'Sending') {
			switch(fileInfo.method) {
				case 'dnd':
					fileInfo.upload.xhr.abort();
					fileInfo.status = 'Aborted';
					fileInfo.progress = 0;
					controller.updateGrid(uploader, fileInfo);
					break;
			}
		}
	},
	removeAll: function(uploader) {
		var controller = this;
		for(id in uploader.fileQueue) {
			controller.remove(uploader, uploader.fileQueue[id]);
		}
	},
	remove: function(uploader, fileInfo) {
		var controller = this;
		if(fileInfo.status === 'Sending') {
			controller.abort(uploader, fileInfo);
		}
		var grid = controller.getGrid(uploader);
		if(grid) {
			grid.getStore().remove(grid.getStore().getById(fileInfo.id));
		}
		delete uploader.fileQueue[fileInfo.id];
	}
});