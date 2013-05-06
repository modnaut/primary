/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

// DO NOT DELETE - this directive is required for Sencha Cmd packages to work.
//@require @packageOverrides

//<debug>
Ext.Loader.setPath({
    'Ext': 'touch/src',
    'Modnaut': 'app'
});
//</debug>

Ext.application({
    name: 'Modnaut',

    requires: [
        'Ext.MessageBox'
    ],

    views: [
        'Main'
    ],
    
    profiles: ['Phone', 'Tablet'],

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },

    launch: function() {
        // Destroy the #appLoadingIndicator element
        Ext.fly('appLoadingIndicator').destroy();

        Ext.create("Ext.tab.Panel", {
            fullscreen: true,
            tabBarPosition: 'bottom',
            items: [{
            	title: 'Contact',
            	iconCls: 'user',
            	xtype: 'formpanel',
            	url: '/ApplicationServlet',
            	baseParams: {
            		Class: 'com.modnaut.apps.mobile.ContactUsCtrl',
            		Method: 'submitMobile'
            	},
            	layout: 'vbox',
            	items: [{
            		xtype: 'fieldset',
            		title: 'Contact Us',
            		instructions: '(email address is optional)',
            		items: [{
            			xtype: 'textfield',
            			label: 'Name'
            		}, {
            			xtype: 'emailfield',
            			label: 'Email'
            		}, {
            			xtype: 'textareafield',
            			label: 'Message'
            		}]
            	}, {
            		xtype: 'button',
            		text: 'Send',
            		ui: 'confirm',
            		handler: function() {
            			this.up('formpanel').submit();
            		}
            	}]
            }, {
					xtype: 'nestedlist',
					title: 'Blog',
					iconCls: 'star',
					displayField: 'title',
					store: {
						type: 'tree',
						fields: ['title', 'link', 'author', 'contentSnippet', 'content', {name: 'leaf', defaultValue: true}],
						root: {
							leaf: false
						},
						proxy: {
							type: 'jsonp',
							url: 'https://ajax.googleapis.com/ajax/services/feed/load?v=1.0&q=http://feeds.feedburner.com/SenchaBlog',
							reader: {
								type: 'json',
								rootProperty: 'responseData.feed.entries'
							}
						}
					},
					detailCard: {
						xtype: 'panel',
						scrollable: true,
						styleHtmlContent: true
					},
					listeners: {
						itemtap: function(nestedList, list, index, element, node) {
							this.getDetailCard().setHtml(node.get('content'));
						}
					}
            }]
        });
    },

    onUpdated: function() {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
                    window.location.reload();
                }
            }
        );
    }
});