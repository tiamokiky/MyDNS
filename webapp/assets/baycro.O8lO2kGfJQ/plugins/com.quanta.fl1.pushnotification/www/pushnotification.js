cordova.define("com.quanta.fl1.pushnotification.PushNotification", function(require, exports, module) { //require Deferred compitible with jquery
var exec = require('cordova/exec'),
	Deferred = window.Deferred || require('com.quanta.fl1.deferred.Deferred'),
	api_version = '4.1.0',
	update_time = '20140613',
	deviceToken,
	deviceTokenJson,
	notifyDfd,
	clickDfd,
	ajax;

var NativePlugin = {};

NativePlugin.getDeviceToken = function(userId){
	var dfd = new Deferred(),
		success = function(_deviceTokenJson){
			console.log('PushNotification.getDeviceToken success');
			console.log(_deviceTokenJson.device_token);
			dfd.resolve(_deviceTokenJson);
		},
		fail = function (error) {
			dfd.reject(error);
		};
	console.log('PushNotification.getDeviceToken');
	console.log('userId = '+userId);
	if(userId){
		exec(success, fail, 'PushNotification' , 'getDeviceToken', [userId]);
	} else {
		exec(success, fail, 'PushNotification' , 'getDeviceToken', []);
	}
	return dfd.promise();
};
NativePlugin.clearDeviceToken = function(){
	var dfd = new Deferred(),
		success = function(){
			dfd.resolve();
		},
		fail = function (error) {
			dfd.reject(error);
		};
	console.log('PushNotification.clearDeviceToken');
	exec(success, fail, 'PushNotification' , 'clearDeviceToken', []);
	return dfd.promise();
};
NativePlugin.onNotify = function() {
	var success = function(notification){
			console.log('PushNotification.onNotify success:' + JSON.stringify(notification));
			notifyDfd.notify(notification);
		},
		fail = function (error) {
			console.error('PushNotification.onNotify Error:' + JSON.stringify(error));
			notifyDfd.reject(error);
		};
	if(!notifyDfd){
		notifyDfd = new Deferred();
		console.log('PushNotification.onNotify');
		exec(success, fail, 'PushNotification' , 'onNotify', []);
	}
	return notifyDfd.promise();
};
NativePlugin.onClick = function(){
	var success = function(notification){
			console.log('PushNotification.onClick success:' + JSON.stringify(notification));
			clickDfd.notify(notification);
		},
		fail = function (error) {
			console.error('PushNotification.onClick Error:' + JSON.stringify(error));
			clickDfd.reject(error);
		};
	if(!clickDfd){
		clickDfd = new Deferred();
		console.log('PushNotification.onClick');
		exec(success, fail, 'PushNotification' , 'onClick', []);
	}
	return clickDfd.promise();
};
NativePlugin.getVersion = function(){
	var dfd = new Deferred(),
		success = function (version) {
			dfd.resolve(version);
		},
		fail = function (error) {
			console.warn('getVersion Error' + error);
			dfd.reject(error);
		};
	console.log('PushNotification.getVersion');
	exec(success, fail, 'PushNotification', 'getVersion', []);
	return dfd.promise();
};
NativePlugin.setSoundVolume = function(options) {
	var opt = {
		enable: false
	};
	for (var prop in options){
		opt[prop] = options[prop];
	}
	var dfd = new Deferred(),
		success = function (version) {
			dfd.resolve(version);
		},
		fail = function (error) {
			console.warn('setSoundVolume Error' + error);
			dfd.reject(error);
		};
	console.log('PushNotification.setSoundVolume');
	exec(success, fail, 'PushNotification', 'setSoundVolume', [opt.enable]);
	return dfd.promise();
};

NativePlugin.getSoundVolume = function() {
	var dfd = new Deferred(),
		success = function (enable) {
			dfd.resolve(enable);
		},
		fail = function (error) {
			console.warn('getSoundVolume Error' + error);
			dfd.reject(error);
		};
	console.log('PushNotification.getSoundVolume');
	exec(success, fail, 'PushNotification', 'getSoundVolume', []);
	return dfd.promise();
};

function isIOS() {
	return (/iPhone|iPad|iPod/i).test(navigator.userAgent);
}

function myAjax(options){
	var xhr = new XMLHttpRequest(),
		dfd = new Deferred();
	xhr.addEventListener('load', function(event) {
		var currentTarget = event.currentTarget;
		var response;
		if(currentTarget.response){
			response = JSON.parse(currentTarget.response);
		}
		if(currentTarget.status >= 400){
			dfd.reject(null, currentTarget.statusText, null);
		} else {
			dfd.resolve(response, currentTarget.statusText, currentTarget);
		}
	}, false);
	xhr.addEventListener('error', function(event) {
		var currentTarget = event.currentTarget;
		dfd.reject(null, currentTarget.statusText, null);
	}, false);

	xhr.open(options.type, options.url, true);
	for(var header in options.headers){
		xhr.setRequestHeader(header, options.headers[header]);
	}
	xhr.setRequestHeader('Content-Type', options.contentType);
	xhr.send(options.data);
	return dfd;
}

//API wrapper
function PushNotification(options){
	// ajax = $.ajax;
	ajax = myAjax;
	var opt = {
		url: window.moapps.config.server + '/notification/v1/installations',
		channels: [''],
		onNotify: function(message){
			console.log('PushNotification.onNotify');
			console.log(message);  
		},
		onClick: function(message){   
			console.log('PushNotification.onClick'); 
			console.log(message);
		},
		onError: function(error){
			console.log('PushNotification.onError');
			console.warn(error);
		},
		onSubscribed: function (channels){
			console.log('PushNotification.onSubscribed channels:' + JSON.stringify(channels));
		}
	},
	headers = {
		'X-Moapps-App-Id': window.moapps.config.appId,
		'X-Moapps-App-Key': window.moapps.config.appKey
	},
	channels = opt.channels,
	installationId,
	events = {},
	that = this;

	if(typeof options === 'string'){
		opt.url = options;
	} else {
		for (var prop in options){
			opt[prop] = options[prop];
		}
	}

	function _createInstallations() {
		console.log('_createInstallations: ' + opt.url);
		//register an installation on server with device token (push notification)
		return ajax({
			url: opt.url,
			type: 'POST',
			contentType: 'application/json',
			headers: headers,
			data: JSON.stringify({
				deviceType: isIOS() ? 'ios' : 'android',
				deviceToken: deviceToken,
				channels: channels
			})
		})
		.done(function (data, textStatus, jqXHR) {
			if(data.id){
				installationId = data.id;
				//because channels didn't update with 'POST' API
				_updateInstallations();
			} else {
				that.trigger('subscribed', channels);
			}
		})
		.fail(function (jqXHR, textStatus, errorThrown) {
			that.trigger('error', 'register fail:' + textStatus);
		});
	}
	function _updateInstallations() {
		//update installation info(ex:channels)
		console.log('_updateInstallations: ' + opt.url);
		return ajax({
			url: opt.url + '/' + installationId,
			type: 'PUT',
			contentType: 'application/json',
			headers: headers,
			data: JSON.stringify({
				channels: channels
			})
		})
		.done(function (data, textStatus, jqXHR) {
			that.trigger('subscribed', channels);
		})
		.fail(function (jqXHR, textStatus, errorThrown) {
			that.trigger('error', 'subscribed fail:' + textStatus);
		});
	}

	//## register device token to server
	function _getDeviceTokenAndCreateInstallations(){
		var dfd = new Deferred();
		if(deviceToken){
			return _createInstallations();
		} else {
			NativePlugin.getDeviceToken()
			.done(function(deviceTokenJson) {
				deviceToken = deviceTokenJson.device_token;
				_createInstallations()
				.done(function() {
					dfd.resolve();
				})
				.fail(function(error) {
					dfd.reject();
				});
			})
			.fail(function(error) {
				that.trigger('error', 'NativePlugin.getDeviceToken:' + error);
				dfd.reject();
			});
			return dfd;
		}
	}
	function _subscribe(_channels) {
		var dfd = new Deferred();
		channels = _channels || opt.channels;
		if(headers && installationId){
			return _updateInstallations();
		} else {
			return _getDeviceTokenAndCreateInstallations();
		}
	}

	//# public function or variable
	that.getDeviceToken = NativePlugin.getDeviceToken;
	that.getVersion = NativePlugin.getVersion;
	that.clearDeviceToken = NativePlugin.clearDeviceToken;
	that.subscribe = _subscribe;
	that.setSoundVolume = NativePlugin.setSoundVolume;
	that.getSoundVolume = NativePlugin.getSoundVolume;

	that.on = function(event, calblack) {
		events[event] = events[event] || [];
		events[event].push(calblack);
	};
	that.trigger = function(event, data){
		for(var callback in events[event]){
			events[event][callback](data);
		}
	};
	that.off =function(event) {
		if(event){
			events[event] = [];
		} else {
			events = {};
		}
	};

	//# main start
	that.on('error', function(error) {
		opt.onError(error);
	});
	that.on('notify', function(notification) {
		opt.onNotify(notification);
	});
	that.on('click', function(notification) {
		opt.onClick(notification);
	});
	that.on('subscribed', function(notification) {
		opt.onSubscribed(notification);
	});
	NativePlugin.onNotify()
	.progress(function(notification) {
		that.trigger('notify', notification);
	});
	NativePlugin.onClick()
	.progress(function(notification) {
		that.trigger('click', notification);
	});
	NativePlugin.getDeviceToken()
	.done(function(deviceTokenJson) {
		deviceToken = deviceTokenJson.device_token;
	});
}
module.exports = PushNotification;

});
