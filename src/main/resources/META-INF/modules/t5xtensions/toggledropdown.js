define(["jquery"], function($) {
    return function(clientId, dropdownId) {
        $('#' + clientId).bind('click.dropdownoption', function(event) {
            $('#' + dropdownId).dropdown('toggle');
        });        
    };
});