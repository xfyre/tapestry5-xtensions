define(["jquery"], function($) {
    return function(spec) {
        $('#' + spec.clientId).on('change.segmentedcontrol', function(event) {
            $('#' + spec.submitId).click();
        });        
    };
});