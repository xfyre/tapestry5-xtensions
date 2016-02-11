define(["jquery", "t5/core/events"], function($, events) {
    return function(clientId) {
        var setupBootstrapFormHelpers = function(){
            $('#' + clientId).each(function () {
                var $phone = $(this)
                console.log("setting up phone input/display for " + $phone.attr('id'))
                $phone.bfhphone($phone.data())
            });
        };
        
        $(document.body).on(events.zone.update, setupBootstrapFormHelpers)
        setupBootstrapFormHelpers()
    }    
})
