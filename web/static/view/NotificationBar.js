Ext.define('Modnaut.view.NotificationBar', {
	extend: 'Ext.toolbar.Toolbar',
	alias: 'widget.notificationBar',
	requires: ['Ext.toolbar.*'],
	cls: 'notificationBar',
	initComponent: function() {
		this.items = [{
			itemId: 'message',
			flex: 1,
			xtype: 'tbtext',
			cls: 'notificationBar-message',
			text: this.text || ''
		}, {
			text: 'X',
			scope: this,
			handler: this.hideBar
		}];
		
		switch(this.type) {
			case 'error':
				this.cls += ' notificationBar-error';
				break;
			case 'success':
				this.cls += ' notificationBar-success';
				break;
			case 'warning':
				this.cls += ' notificationBar-warning';
				break;
		}
		
		this.callParent(arguments);
		this.messageItem = this.child('#message');
	},
	showBar: function(message) {
		this.messageItem.setText(message);
		this.show();
		this.getEl().setOpacity(1, {
			duration: 1000
		});
	},
	hideBar: function() {
		if(this.rendered && !this.isHidden()) {
			Ext.suspendLayouts();
			this.hide();
			this.getEl().setOpacity(0.25, false);
			this.removeCls('notificationBar-error notificationBar-success notificationBar-warning');
			this.messageItem.update('');
			Ext.resumeLayouts(true);
		}
	},
	beforeRender: function() {
		this.callParent(arguments);
		if(!this.text) {
			this.hide();
		}
	},
	onDestroy: function() {
		this.getEl().stopAnimation();
		this.callParent(arguments);
	}
});