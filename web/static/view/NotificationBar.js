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
			handler: this.destroyMe
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
	},
	afterRender: function() {
		var bar = this;
		bar.getEl().animate({
			duration: 500,
			keyframes: {
				0: {
					opacity: 0.1
				},
				100: {
					opacity: 1
				}
			},
			listeners: {
				afteranimate: function() {
					if(bar.hideAfterMs) {
						Ext.Function.defer(function() {
							bar.destroyMe();
						}, bar.hideAfterMs);
					}
				}
			}
		});
		this.callParent(arguments);
	},
	destroyMe: function() {
		var bar = this;
		if(bar.rendered && !bar.isHidden()) {
			bar.getEl().fadeOut({
				duration: 500,
				listeners: {
					afteranimate: function() {
						bar.ownerCt.removeDocked(bar, true);
					}
				}
			});
		}
	},
	onDestroy: function() {
		this.getEl().stopAnimation();
		this.callParent(arguments);
	}
});