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
			handler: this.destroyBar
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
							bar.destroyBar();
						}, bar.hideAfterMs);
					}
				}
			}
		});
		this.callParent(arguments);
	},
	destroyBar: function(immediate) {
		var bar = this;
		
		var removeBar = function() {
			bar.ownerCt.removeDocked(bar, true);
		};
		
		if(immediate !== true && bar.rendered && !bar.isHidden()) {
			bar.getEl().fadeOut({
				duration: 500,
				listeners: {
					afteranimate: removeBar
				}
			});
		} else {
			removeBar();
		}
	},
	onDestroy: function() {
		this.getEl().stopAnimation();
		this.callParent(arguments);
	}
});