cordova.define("com.quanta.fl1.led.Led", function(require, exports, module) { 
var exec = require('cordova/exec'),
    Deferred = window.Deferred || require('com.quanta.fl1.deferred.Deferred');
var NativePlugin = {};

NativePlugin.play = function(script_path, media_path){
    var dfd = new $.Deferred(),
        success = function(){
            console.log('LED.play success');
            dfd.resolve();
        },
        fail = function (error) {
            console.log('LED.play failure');
            dfd.reject(error);
        };
    exec(success, fail, "led", "play",[script_path, media_path]);
    return dfd.promise();
};
NativePlugin.stop = function(){
    var dfd = new $.Deferred(),
        success = function(){
            console.log('LED.stop success');
            dfd.resolve();
        },
        fail = function (error) {
            console.log('LED.stop failure');
            dfd.reject(error);
        };
    exec(success, fail, "led", "stop", []);
    return dfd.promise();

};
 
module.exports = NativePlugin;


});
