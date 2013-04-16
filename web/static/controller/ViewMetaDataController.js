Ext.define('Modnaut.controller.ViewMetaDataController', {
	extend: 'Ext.app.Controller',
	views: ['Modnaut.view.ViewMetaDataContainer'],
	history: {},
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
			},
			HistoryChange: function(token) {
				controller.getContainerContent(controller.history[token], true);
				console.log('HistoryChange', token, controller.history[token]);
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
		var controller = this;
		var ownerCt = oldComponent.ownerCt;
		if(ownerCt) {
			var componentIndex = ownerCt.items.indexOf(oldComponent);
			var resetActiveTab = false;
			if(ownerCt.is('tabpanel') && ownerCt.getActiveTab() == oldComponent)
				resetActiveTab = true;
			
			controller.removeDescendantFormFields(oldComponent);
			
			ownerCt.remove(oldComponent, true);
			var addedComponent = ownerCt.insert(componentIndex, newConfig);
			if(resetActiveTab) {
				ownerCt.setActiveTab(addedComponent);
			}
			
		}
	},
	removeDescendantFormFields: function(container) {
		//if the component being removed might contain form fields, they need to be removed manually
		//since the ExtJS code misses if a form field is removed indirectly by removing it's owner
		//container
		var containedFormFields = container.query('[isFormField]');
		for(var i = 0, len = containedFormFields.length; i < len; i++) {
			containedFormFields[i].ownerCt.remove(containedFormFields[i]);
		}
	},
	getContainerContent: function(options, isHistoryEvent) {
		var controller = this;
		
		var contentRequestId = Ext.util.Cookies.get('MODNAUT_SESSION_ID') + '-' + Ext.Date.now();
		Globals.time(contentRequestId + '-total', 'info');
		
		options = Ext.clone(options);
		
		var parameters = options.parameters;
		parameters.contentRequestId = contentRequestId;
		
		if(parameters.Class && parameters.Method) {
			parameters.invoke = Globals.encodeBase64(parameters.Class + '|' + parameters.Method);
			delete parameters.Class;
			delete parameters.Method;
		}
		
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
		
		var success =  function(response, action) {
			console.log('success', arguments);
			
			var warningCode = action.response.getResponseHeader('WarningCode');
			switch(warningCode) {
				case '1':
					controller.showLogin(options, isHistoryEvent);
					return;
					break;
			}
			
			if(container.isLoginContainer === true) {
				if(action.response.getResponseHeader('LoginSuccessful') === 'true') {
					container.up('window').close();
					Globals.timeEnd(contentRequestId + '-total');
					controller.getContainerContent(controller.loginInitiator.options, controller.loginInitiator.isHistoryEvent);
					return;
				}
			}
			
			var items = response.items;
			var html = response.html;
			var dockedItems = response.dockedItems;
			var windows = response.windows;
			var sessionId = response.sessionId;
			
			if(sessionId) {
				container.sessionId = sessionId;
			}
			
			//remove old notifications from root
			Globals.time(contentRequestId + '-destroy old notifications', 'debug');
			Ext.each(container.query('>notificationBar'), function(notificationBar) {
				notificationBar.destroyBar(true);
			});
			Globals.timeEnd(contentRequestId + '-destroy old notifications');
			
			Ext.suspendLayouts();
			Globals.time(contentRequestId + '-update component tree', 'debug');
			if(options.itemsToUpdate) {
				for(var itemId in componentsToUpdate) {
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
				if(isHistoryEvent !== true) {
					var historyToken = Ext.Date.now();
					controller.history[historyToken] = options;
					Ext.History.suspendEvents();
					Ext.History.add(historyToken);
					Ext.defer(Ext.History.resumeEvents, 100, Ext.History);
				}
				
				controller.safeSetLoading(container, false);
				if(items) {
					controller.removeDescendantFormFields(container);
					container.removeAll();
					container.add(items);
				} else {
					container.update(html);
				}
			}
			Globals.timeEnd(contentRequestId + '-update component tree');
			
			
			//add new notifications to root
			Globals.time(contentRequestId + '-create new notifications', 'debug');
			if(dockedItems && dockedItems.length) {
				container.addDocked(dockedItems);
			}
			Globals.timeEnd(contentRequestId + '-create new notifications');
			
			Globals.time(contentRequestId + '-flush layouts after succes', 'debug');
			Ext.resumeLayouts(true);
			Globals.timeEnd(contentRequestId + '-flush layouts after succes');
			
			Globals.time(contentRequestId + '-create windows', 'debug');
			if(windows && windows.length) {
				for(var i = 0, len = windows.length; i < len; i++) {
					var windowConfig = windows[i];
					
					var callback = function(button, text, opts) {
						if(windowConfig.listeners && windowConfig.listeners[button]) {
							return windowConfig.listeners[button].call(container, text);
						}
					};
					
					var title = windowConfig.title || '';
					
					switch(windowConfig.type) {
						case 'alert':
							Ext.MessageBox.alert(title, windowConfig.text, callback, container);
							break;
						case 'confirm':
							Ext.MessageBox.confirm(title, windowConfig.text, callback, container);
							break;
						case 'prompt':
							Ext.MessageBox.prompt(title, windowConfig.text, callback, container, windowConfig.multiline, windowConfig.defualtValue);
							break;
					}
				}
			}
			Globals.timeEnd(contentRequestId + '-create windows');
			
			if(response.script) {
				Ext.Function.defer(function(){
					Globals.time(contentRequestId + '-eval scripts', 'debug');
					eval(response.script);
					Globals.timeEnd(contentRequestId + '-eval scripts');
					Globals.timeEnd(contentRequestId + '-total');
				}, 100);
			} else {
				Globals.timeEnd(contentRequestId + '-total');
			}
		};
		
		var failure = function(text, action) {
			console.log('failure', arguments);
			
			Ext.suspendLayouts();
			
			Globals.time(contentRequestId + '-update component tree on failure', 'debug');
			if(options.itemsToUpdate) {
				for(var itemId in componentsToUpdate) {
					var oldComponent = componentsToUpdate[itemId];
					controller.safeSetLoading(oldComponent, false);
					oldComponent.update(text);
				}
			} else {
				controller.safeSetLoading(container, false);
				container.removeAll();
				container.update(text);
			}
			Globals.timeEnd(contentRequestId + '-update component tree on failure');
			
			Globals.time(contentRequestId + '-flush layouts after updating component tree on failure', 'debug');
			Ext.resumeLayouts(true);
			Globals.timeEnd(contentRequestId + '-flush layouts after updating component tree on failure');
		};
		
		
		Ext.suspendLayouts();
		Globals.time(contentRequestId + '-mask components', 'debug');
		if(options.loadMask !== false) {
			if(options.itemsToUpdate) {
				for(var itemId in componentsToUpdate) {
					var component = componentsToUpdate[itemId];
					controller.safeSetLoading(component, true);
				}
			} else {
				controller.safeSetLoading(container, true);
			}
		}
		Globals.timeEnd(contentRequestId + '-mask components');
		
		Globals.time(contentRequestId + '-flush layouts after masking components', 'debug');
		Ext.resumeLayouts(true);
		Globals.timeEnd(contentRequestId + '-flush layouts after masking components');
		
		if(options.download === true) {
			
		} else if (container.getForm()) {
			if(container.getForm().hasUpload() && options.upload !== true) {
				Globals.time(contentRequestId + '-remove descendant fields', 'debug');
				container.getForm().getFields().each(function(field) {
		            if(field.isFileUpload())
		            	field.ownerCt.remove(field);
		        });
				Globals.timeEnd(contentRequestId + '-remove descendant fields');
			}
			
			Globals.time(contentRequestId + '-server response', 'debug');
			Globals.time(contentRequestId + '-form submit', 'debug');
			container.getForm().submit({
				clientValidation: false,
				url: 'ApplicationServlet',
				params: parameters,
				handleResponse: function(response){
					Globals.timeEnd(contentRequestId + '-server response');
					Globals.time(contentRequestId + '-handleResponse', 'debug');
					//copied from ExtJS
					var form = this.form,
			            errorReader = form.errorReader,
			            rs, errors, i, len, records,
			            returnValue;
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
			            
			            returnValue =  {
			                success : rs.success,
			                errors : errors
			            };
			        } else {
				        //end copied from ExtJS
			        	returnValue = Ext.decode(response.responseText, true);
				        if(returnValue === null) {
				        	returnValue = {
				        		success: true,
				        		html: response.responseText
				        	};
				        }
			        }
			        
			        Globals.timeEnd(contentRequestId + '-handleResponse');
			        return returnValue;
				},
				success: function(form, action) {
					success(action.result, action);
				},
				failure: function(form, action) {
					failure(action.result, action);
				}
			});
			Globals.timeEnd(contentRequestId + '-form submit');
		} else {
			Globals.time(contentRequestId + '-server response', 'debug');
			Ext.Ajax.request({
				url: 'ApplicationServlet',
				params: parameters,
				success: function(response, options){
					Globals.timeEnd(contentRequestId + '-server response');
					Globals.time(contentRequestId + '-AJAX success', 'debug');
					container.setLoading(false);
					var decoded = Ext.decode(response.responseText, true);
					if(Ext.isObject(decoded)) {
						success(decoded);
					} else {
						success({
							html: response.responseText
						});
					}
					Globals.timeEnd(contentRequestId + '-AJAX success');
				},
				failure: function(response, options){
					Globals.timeEnd(contentRequestId + '-server response');
					failure('Request to server failed');
				}
			});
		}
	},
	showLogin: function(options, isHistoryEvent) {
		var controller = this;
		
		controller.loginInitiator = {
			options: options,
			isHistoryEvent: isHistoryEvent
		};
		
		controller.loginWindow = Ext.create('Ext.window.Window', {
			modal: true,
			width: Ext.Element.getViewportWidth() * 0.75,
			height: Ext.Element.getViewportHeight() * 0.75,
			closable: false,
			resizable: false,
			draggable: false,
			floating: true,
			border: false,
			layout: 'fit',
			items: [{
				xtype: 'vmdContainer',
				layout: 'fit',
				Class: 'com.modnaut.apps.login.LoginCtrl',
				Method: 'defaultAction',
				isLoginContainer: true
			}]
		});
		controller.loginWindow.show();
	}
});