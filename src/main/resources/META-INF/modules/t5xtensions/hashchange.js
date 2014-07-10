define(["jquery", "t5/core/events", "t5/core/zone"], function($, events, zone){
    var hashchangeHandlers = {};
    var hashchangeInstalled = false;
    
    return function(spec) {
        var token   = spec.token;
        var linkId  = spec.linkId;
        var zoneId  = spec.zoneId;
        
        var el          = $('#'+linkId);
        var zoneElement = $('#'+zoneId);
        var url         = $(el).attr('href');
        
        var setupHandlers = function() {
            // unbind default click handler
            el.unbind('click');
            
            // perform hash change
            el.bind('click', function(event) {
                location.hash = '#'+token;
                return false;
            });
            
            hashchangeHandlers['#'+token] = function() {
                el.trigger(events.zone.refresh, {url: url});
            };
        };
        
        setupHandlers();
        
        $(document.body).unbind(events.zone.update + "." + token);
        $(document.body).bind(events.zone.update + "." + token, function(){
            setupHandlers();
        });

        if (!hashchangeInstalled) {
            $(window).hashchange(function() {
                if (hashchangeHandlers[location.hash] != null) {
                    var hashchangeHandler = hashchangeHandlers[location.hash];
                    hashchangeHandler();
                }
            });
            hashchangeInstalled = true;
        }
        
        if (!spec.ajax && (location.hash == ('#'+token)))
            el.trigger(events.zone.refresh, {url: url});
        
    };    
});
