define(["jquery", "t5/core/events"], function($, events) {
    return function(clientId) {
        var setupSelect = function() {
            $('#' + clientId).selectpicker()
        }
        $(document.body).on(events.zone.update, setupSelect)
        setupSelect()
    };
});
