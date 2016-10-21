cordova.define("com.quanta.qri.moapps.app", function(require, exports, module) { 

var exec = require('cordova/exec'),
	Deferred = window.Deferred || require('com.quanta.fl1.deferred.Deferred');

var NativePlugin = {};
NativePlugin.keepScreenOn = function(enable) {
	var dfd = new Deferred(),
		success = function(){
			console.log('App.keepScreenOn success');
			dfd.resolve();
		},
		fail = function (error) {
			console.log('App.keepScreenOn fail');
			dfd.reject(error);
		};
	exec(success, fail, "moapps", "keepScreenOn", [enable]);
	return dfd.promise();
};

NativePlugin.checkForUpdate = function(options) {
	var opt = options || {
		server: null,
		appId: null,
		appKey: null
	};

	var dfd = new Deferred(),
		success = function(){
			console.log('App.keepScreenOn success');
			dfd.resolve();
		},
		fail = function (error) {
			console.log('App.keepScreenOn fail');
			dfd.reject(error);
		};
	exec(success, fail, "moapps", "checkForUpdate", [opt.server, opt.appId, opt.appKey]);
	return dfd.promise();
	//options = {baseUrl:'',appId:'appId',appkey:''}
};

NativePlugin.getConfig = function(options){
	var opt = options || {
			success: function(){},
			error: function(){}
		},
		dfd = new Deferred(),
		success = function(config){
			console.log('App.getConfig success');
			if(typeof opt.success === 'function'){
				opt.success(config);
			}
			dfd.resolve(config);
		},
		fail = function (error) {
			console.log('App.getConfig fail');
			if(typeof opt.error === 'function'){
				opt.error();
			}
			dfd.reject(error);
		};
	exec(success, fail, "moapps", "getConfig", []);
	return dfd.promise();
};


NativePlugin.exit = function() {
	var dfd = new Deferred(),
		success = function(){
			console.log('App.exit success');
			dfd.resolve();
		},
		fail = function (error) {
			console.log('App.exit fail');
			dfd.reject(error);
		};
	exec(success, fail, "moapps", "exit", []);
	return dfd.promise();
};

module.exports = NativePlugin;

});
