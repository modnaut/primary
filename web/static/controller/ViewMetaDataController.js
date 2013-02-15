Ext.define('Modnaut.controller.ViewMetaDataController', {
	extend: 'Ext.app.Controller',
	views: ['Modnaut.view.ViewMetaDataContainer'],
	init: function () {
		var controller = this;
		this.control({
			'vmdContainer': {
				afterlayout: function(container, opts) {
					if(container.firstLayoutDone)
						return;
					
					var parameters = Ext.clone(container.parameters || {});
					if(!(container.Class && container.Method))
						return;
					
					parameters.Class = container.Class;
					parameters.Method = container.Method;
					
					controller.getContainerContent({
						component: container,
						parameters: parameters
					});
					container.firstLayoutDone = true;
				},
				activate: function(container, opts) {
					container.doLayout();
				}
			}
		});
	},
	getContainerContent: function(options) {
		options = Ext.clone(options);
		
		var parameters = options.parameters;
		
		var component = options.component;
		if(!component.is('vmdContainer')) {
			component = component.up('vmdContainer');
		}
		var form = component.down('form');
		var basicForm = null;
		if(form)
			basicForm = form.getForm();
		
		if(component.branchSession === true) {
			parameters.branchSession = true;
			delete component.branchSession;//only want to branch the session the first time it's rendered
		}
		
		if(component.sessionId)
			parameters.sessionId = component.sessionId;
		
		var success =  function(response) {
			console.log('success', arguments);
			var items = response.items;
			var html = response.html;
			var sessionId = response.sessionId;
			
			if(sessionId) {
				component.sessionId = sessionId;
			}
			
			Ext.suspendLayouts();
			if(items) {
				component.removeAll();
				component.add(items);
			} else {
				component.update(html);
			}
			Ext.resumeLayouts(true);
			
			eval(response.script);
		};
		
		var failure = function(text) {
			component.setLoading(false);
			component.removeAll();
			component.update(text);
		};
		
		if(options.download === true) {
			
		} else if (basicForm) {
			if(basicForm.hasUpload() && options.upload !== true) {
				basicForm.getFields().each(function(field) {
		            if(field.isFileUpload())
		            	field.ownerCt.remove(field);
		        });
			}
			
			basicForm.submit({
				clientValidation: false,
				url: 'ApplicationServlet',
				params: parameters,
				handleResponse: function(response){
					//copied from ExtJS
					var form = this.form,
			            errorReader = form.errorReader,
			            rs, errors, i, len, records;
			        if (errorReader) {
			            rs = errorReader.read(response);
			            records = rs.records;
			            errors = [];
			            if (records) {
			                for(i = 0, len = records.length; i < len; i++) {
			                    errors[i] = records[i].data;
			                }
			            }
			            if (errors.length < 1) {
			                errors = null;
			            }
			            return {
			                success : rs.success,
			                errors : errors
			            };
			        }
			        //end copied from ExtJS
			        var decoded = Ext.decode(response.responseText, true);
			        if(decoded) {
			        	return decoded;
			        } else {
			        	return {
			        		success: true,
			        		html: response.responseText
			        	};
			        }
				},
				success: function(form, action) {
					success(action.result);
				},
				failure: function(form, action) {
					failure(action.result);
				}
			});
		} else {
			Ext.Ajax.request({
				url: 'ApplicationServlet',
				params: parameters,
				success: function(response, options){
					component.setLoading(false);
					var decoded = Ext.decode(response.responseText, true);
					if(Ext.isObject(decoded)) {
						success(decoded);
					} else {
						success({
							html: response.responseText
						});
					}
				},
				failure: function(response, options){
					failure('Request to server failed');
				}
			});
		}
	}
});