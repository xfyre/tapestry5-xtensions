define(["jquery", "t5/core/events"], function($, events) {
    return function(clientId) {
        var setupBootstrapFormHelpers = function(){
            $('form input[type="text"].bfh-phone, form input[type="tel"].bfh-phone, span.bfh-phone').each(function () {
                var $phone;
                $phone = $(this);
                $phone.bfhphone($phone.data());
            });
        };
        
        $(document.body).on(events.zone.update, setupBootstrapFormHelpers);
        setupBootstrapFormHelpers();
    };    
});
