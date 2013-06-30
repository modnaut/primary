Ext.define('Modnaut.controller.UploaderController', {
	extend: 'Ext.app.Controller',
	requires: ['Modnaut.data.XHRUpload'],
	views: ['Modnaut.view.Uploader', 'Modnaut.view.UploaderGrid'],
	init: function () {
		var controller = this;
		this.control({
			'uploader': {
				render: function(uploader) {
					controller.initDNDUpload(uploader);
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
			uploader.standardUploader.uploader.fileInput = null; //remove reference to file field. necessary to prevent destroying file field during an active upload.
			Ext.destroy(uploader.standardUploader);
		}

		uploader.standardUploader = new Ext.ux.form.FileUploadField({
			renderTo: uploader.items.items[0].el.dom,
//			buttonText: me.standardButtonText,
			buttonOnly: true,
			name: uploader.standardUploadFilePostName,
			listeners: {
				fileselected: function() {
					controller.standardUploadFileSelected(uploader);
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
			name: file.name,
			size: file.size,
			status: 'queued',
			method: 'dnd',
			file: file
		};
		
		if(fileInfo.size > uploader.maxFileSize) {
			controller.alert(uploader, file.name + ' File size exceeds allowed limit');
			uploader.fireEvent('fileselectionerror', uploader, Ext.apply({}, fileInfo), 'File size exceeds allowed limit');
			return true;
		}
		
		if(uploader.fireEvent('fileselected', uploader, Ext.apply({}, fileInfo)) !== false) {
			uploader.fileQueue[fileInfo.id] = fileInfo;
			if(uploader.autoStartUpload) {
				controller.dndUploadStart(uploader, fileInfo);
			}
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
				uploadstart: function(event) {
					uploader.fireEvent('uploadstart', uploader, Ext.apply({}, fileInfo));
				},
				uploadprogress: function(event) {
					uploader.fireEvent('uploadprogress', uploader, fileInfo.id, event.loaded, event.total);
				},
				loadstart: function(event) {
					fileInfo.status = 'started';
					uploader.fireEvent('start', uploader, Ext.apply({}, fileInfo));
				},
				progress: function(event) {
					uploader.fireEvent('progress', uploader, Ext.apply({}, fileInfo), event.loaded, event.total);
				},
				abort: function(event) {
					fileInfo.status = 'aborted';
					uploader.fireEvent('abort', uploader, Ext.apply({}, fileInfo), 'XHR upload aborted');
				},
				error: function(event) {
					fileInfo.status = 'error';
					uploader.fireEvent('error', uploader, Ext.apply({}, fileInfo), 'XHR upload error');
				},
				load: function(event) {
					controller.processUploadResult(uploader, fileInfo, upload.xhr.responseText);
				}
			}
		});
		fileInfo.upload = upload;
		upload.send();
	},
	processUploadResult: function(uploader, fileInfo, serverData) {
		var controller = this;
		var uploadCompleteData = {};
		if(uploader.fireEvent('uploadcomplete', uploader, Ext.apply({}, fileInfo), serverData, uploadCompleteData) !== false) {
			fileInfo.status = 'completed';
		} else {
			controller.alert(uploader, 'Error uploading file:' + fileInfo.name);
			fileInfo.status = 'error';
			uploader.fireEvent('uploaderror', uploader, Ext.apply({}, fileInfo), serverData, uploadCompleteData);
		}
	},
	standardUploadFileSelected: function(uploader, fileBrowser, fileName) {
		var controller = this;
		
		var fileInfo = {
			id: uploader.fileId++,
			name: fileName,
			status: 'queued',
			method: 'standard',
			size: '0'
		};
		
		if(Ext.isDefined(fileBrowser.fileInput.dom.files) ){
			fileInfo.size = fileBrowser.fileInput.dom.files[0].size;
		};
		
		if(fileInfo.size > uploader.maxFileSize){
			controller.alert(uploader, file.name + ' File size exceeds allowed limit');
			uploader.fireEvent('fileselectionerror', uploader, Ext.apply({}, fileInfo), 'File size exceeds allowed limit');
			return true;
		}
		
		fileInfo.fileBrowser = fileBrowser;
		
		var formEl = uploader.items.items[0].el.appendChild(document.createElement('form'));
		var extraPost;
		
		fileInfo.fileBrowser.fileInput.addClass('au-hidden');
		formEl.appendChild(fileBrowser.fileInput);
		formEl.addClass('au-hidden');
		fileInfo.form = formEl;
		
		controller.initStandardUpload(uploader); //re-init uploader for multiple simultaneous uploads
		
		if(uploader.fireEvent('fileselected', uploader, Ext.apply({}, fileInfo) !== false) ) {
			if(uploader.autoStartUpload){
				controller.standardUploadStart(uploader, fileInfo);
			}
			uploader.fileQueue[fileInfo.id] = fileInfo;
		}
	},
	standardUploadStart: function(uploader, fileInfo) {
		var controller = this;
		controller.doFormUpload(uploader, fileInfo);
		fileInfo.status = 'started';
		uploader.fireEvent('uploadstart', uploader, Ext.apply({}, fileInfo));
	}
});