define(["jquery", "t5/core/events", "bootstrap/tooltip"], function($, events) {
    return function(clientId) {
        var setupTooltip = function(){
            $('#' + clientId).tooltip({'container':'body'});
        };
        
        $(document.body).on(events.zone.update, setupTooltip);
        setupTooltip();
    };
});
