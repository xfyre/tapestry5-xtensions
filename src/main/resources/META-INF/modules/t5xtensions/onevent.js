define(["jquery", "t5/core/events", "t5/core/zone"], function($, events, zone) {
    return function(spec) {
        $(document).ready(function() {
            var element = $('#' + spec.clientId);
            for ( var idx = 0; idx < spec.clientEvent.length; idx++ ) {
                var clientEvent = spec.clientEvent[idx];
                element.on(clientEvent + ".oneventmixin", function(event, data) {
                    if (spec.resetField)
                        $('#' + spec.resetField).val('');
                    
                    if (spec.submitId) {
                        if (event.type == 'autocompleteselect')
                            element.val(data.item.value);
                        
                        var submit = $('#' + spec.submitId);
                        submit.click();
                    } else {
                        var zoneElement = spec.zoneId === '^' ? $(el).closest('.t-zone') : $("#" + spec.zoneId);
        
                        if (!zoneElement) {
                            Tapestry.error( "Could not find zone element '#{zoneId}' to update on #{eventName} of element '#{elementId}",
                                            {
                                                zoneId : spec.zoneId,
                                                eventName : spec.clientEvent,
                                                elementId : spec.clientId
                                            });
                            return;
                        }
                        
                        zoneElement.trigger(events.zone.refresh, {
                            url: spec.url, parameters: { 't:selectedvalue': $(element).val() }
                        });
                    }
                });
            }
        });
    };
});
