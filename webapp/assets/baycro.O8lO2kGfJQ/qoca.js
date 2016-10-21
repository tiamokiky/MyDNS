
var Qoca = {};

var isWaitForNativeCallback = false;

function _waitNativeAction(action, callback, data) {
  var iframe = document.createElement('iframe');
  var req = encodeURIComponent(action) + '$' + encodeURIComponent(callback);
  if (data) {
    req = req + '$' + encodeURIComponent(data);
  }

  iframe.setAttribute( 'src', 'js://' + req );
  iframe.style.display = 'none';
  document.documentElement.appendChild(iframe);

  setTimeout(function (){
    iframe.parentNode.removeChild(iframe);
    iframe = null;
    isWaitForNativeCallback = false;
    }, 80);
}

function _currentDateTimeString(){
  var now = new Date();
  var yyyy = now.getFullYear().toString();
  var mm = (now.getMonth()+1).toString(); // getMonth() is zero-based
  var dd  = now.getDate().toString();
  var hh = now.getHours().toString();
  var min = now.getMinutes().toString();
  var sec = now.getSeconds().toString();

  return yyyy + (mm[1]?mm:"0"+mm[0]) + (dd[1]?dd:"0"+dd[0]) +
  (hh[1]?hh:"0"+hh[0]) + (min[1]?min:"0"+min[0]) + (sec[1]?sec:"0"+sec[0]);
}

function _getLogJson(){
  var log = {};
  log.uuid = Qoca.uuid;
  log.startTime = _currentDateTimeString();
  log.sid = Qoca.uuid.substring(0, 6) + log.startTime;
  log.isToggle = true;
  return log;
}

function _currentUUIDCb(data) {
  try {
    var jsonObject = JSON.parse(data);
    if (jsonObject){
      Qoca.uuid = jsonObject.id;
    }
  }catch(err) {
      console.log('_currentUUIDCb ' + err);
  }
}


Qoca.init = function(){
  Qoca.uuid = "";
  Qoca.getUserInfo(_currentUUIDCb);
}

Qoca.getUserInfo = function(callback) {
  try {
    var retrieveCb = {callback: 'callBackFromNative'};
    if(!isWaitForNativeCallback) {
      if(window.QOCAJSInterface) {
        if (window.QOCAJSInterface.retrieveUserInfo){
          isWaitForNativeCallback = true;
          window.QOCAJSInterface.retrieveUserInfo(JSON.stringify(retrieveCb));
          window.callBackFromNative = callback;
          _waitNativeAction('getUserInfo', callback);
        }else
        {
          callback('QOCAJSInterface.retrieveUserInfo None');
        }
      }else
      {
        callback('QOCAJSInterface None');
      }
    }
  } catch (err) {
      console.log('getUserInfo ' + err);
  }
}

Qoca.usageLog = function(appName, version, startValue, tag, mode, callback) {
  try {
    var log = _getLogJson();
    log.appName = appName;
    log.version = version;
    log.startValue = startValue;
    log.tag = tag;
    if(window.QOCAJSInterface) {
      if (window.QOCAJSInterface.usageLog){
        window.QOCAJSInterface.usageLog(JSON.stringify(log), mode);
        if (callback) callback(JSON.stringify(log));
      }else{
        if (callback) callback(JSON.stringify(log));
      }
    }else{
        if (callback) callback(JSON.stringify(log));
    }
  } catch (err) {
    console.log('usageLog' + err);
  }

}

var module = {};
module.exports = {
  Qoca: Qoca
};
