define(["jquery", "t5/core/events", "bootstrap/popover"], function($, events) {
    return function(params) {
        var setupPopover = function(){
            if (params.eventLink) {
                $('#' + params.clientId).popover({
                    'container' : 'body',
                    'content'   : function() {
                        $.ajax({
                            'url'     : params.eventLink, 
                            'success' : function(response) {
                                $('.popover-content').html(response.content);
                            }
                        });
                    }
                });
            } else {
                $('#' + params.clientId).popover({'container':'body'});
            }
        };
        
        $(document.body).on(events.zone.update, setupPopover);
        setupPopover();
    };    
});
