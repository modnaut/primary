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
		
		Globals.on({
			SubmitForm: function(options) {
				controller.getContainerContent(options);
			}
		});
	},
	safeSetLoading: function(component, value) {
		if(component.isVisible(true))
			component.setLoading(value);
	},
	findNewComponentConfig: function(items, itemId) {
		var controller = this;
		for(var i = 0, len = items.length; i < len; i++) {
			if(items[i].itemId == itemId) {
				return items[i];
			} else if(items[i].items) {
				return controller.findNewComponentConfig(items[i].items, itemId);
			}
		}
	},
	replaceComponent: function(oldComponent, newConfig) {
		var ownerCt = oldComponent.ownerCt;
		if(ownerCt) {
			var form = ownerCt.form;
			var componentIndex = ownerCt.items.indexOf(oldComponent)
			var resetActiveTab = false;
			if(ownerCt.is('tabpanel') && ownerCt.getActiveTab() == oldComponent)
				resetActiveTab = true;
			ownerCt.remove(oldComponent, true);
			var addedComponent = ownerCt.insert(componentIndex, newConfig);
			if(resetActiveTab) {
				ownerCt.setActiveTab(addedComponent);
			}
			if(form) {
				form.remove(oldComponent);
				form.add(addedComponent);
			}
		}
	},
	getContainerContent: function(options) {
		var controller = this;
		
		options = Ext.clone(options);
		
		var parameters = options.parameters;
		
		var component = options.component;
		var container = component;
		if(!container.is('vmdContainer')) {
			container = component.up('vmdContainer');
		}
		
		if(container.branchSession === true) {
			parameters.branchSession = true;
			delete container.branchSession;//only want to branch the session the first time it's rendered
		}
		
		if(container.sessionId)
			parameters.sessionId = container.sessionId;
		
		var componentsToUpdate = {};
		if(options.itemsToUpdate) {
			var itemsToUpdate = options.itemsToUpdate.split(',');
			for(var i = 0, len = itemsToUpdate.length; i < len; i++) {
				var itemId = Ext.String.trim(itemsToUpdate[i]);
				itemsToUpdate[i] = itemId;//set trimmed form back into array;
				var componentToUpdate = container.query('#' + itemId)[0];
				if(componentToUpdate) {
					componentsToUpdate[itemId] = componentToUpdate;
				}
			}
			parameters.itemsToUpdate = itemsToUpdate.join(',');
		}
		
		var success =  function(response) {
			console.log('success', arguments);
			var items = response.items;
			var html = response.html;
			var sessionId = response.sessionId;
			
			if(sessionId) {
				container.sessionId = sessionId;
			}
			
			Ext.suspendLayouts();
			if(options.itemsToUpdate) {
				for(itemId in componentsToUpdate) {
					var oldComponent = componentsToUpdate[itemId];
					controller.safeSetLoading(oldComponent, false);
					if(items) {
						var newConfig = controller.findNewComponentConfig(items, itemId);
						if(newConfig) {
							controller.replaceComponent(oldComponent, newConfig);
						}
					} else {
						oldComponent.update(html);
					}
				}
			} else {
				controller.safeSetLoading(container, false);
				if(items) {
					container.removeAll();
					container.add(items);
				} else {
					container.update(html);
				}
			}
			Ext.resumeLayouts(true);
			
			eval(response.script);
		};
		
		var failure = function(text) {
			if(options.itemsToUpdate) {
				for(itemId in componentsToUpdate) {
					var oldComponent = componentsToUpdate[itemId];
					controller.safeSetLoading(oldComponent, false);
					oldComponent.update(text);
				}
			} else {
				controller.safeSetLoading(container, false);
				container.removeAll();
				container.update(text);
			}
		};
		
		
		if(options.loadMask !== false) {
			if(options.itemsToUpdate) {
				for(itemId in componentsToUpdate) {
					var component = componentsToUpdate[itemId];
					controller.safeSetLoading(component, true);
				}
			} else {
				controller.safeSetLoading(container, true);
			}
		}
		
		if(options.download === true) {
			
		} else if (container.getForm()) {
			if(container.getForm().hasUpload() && options.upload !== true) {
				container.getForm().getFields().each(function(field) {
		            if(field.isFileUpload())
		            	field.ownerCt.remove(field);
		        });
			}
			
			container.getForm().submit({
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
					container.setLoading(false);
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