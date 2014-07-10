define(["jquery"], function($) {
    return function(clientId, attrname, attrvalue) {
        $('#' + clientId).attr(attrname, attrvalue);
    };
});