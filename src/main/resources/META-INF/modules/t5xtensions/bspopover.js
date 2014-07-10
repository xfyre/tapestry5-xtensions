define(["jquery", "t5/core/events", "bootstrap/popover"], function($, events) {
    return function(clientId) {
        var setupPopover = function(){
            $('#' + clientId).popover({'container':'body'});
        };
        
        $(document.body).on(events.zone.update, setupPopover);
        setupPopover();
    };    
});
