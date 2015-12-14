define(["jquery", "t5/core/events"], function($, events) {
    return function(clientId) { $('#' + clientId).selectpicker() }
})
