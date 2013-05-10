Ext.define('Modnaut.view.ContactUs', {
    extend: 'Ext.form.Panel',
    xtype: 'contactUs',
    config: {
    	scrollable: true,
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
    }
});
