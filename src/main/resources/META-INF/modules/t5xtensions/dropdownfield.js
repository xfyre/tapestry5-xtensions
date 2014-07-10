define(["jquery"], function($) {
    return function(spec) {
        $('#' + spec.menuId + " a[role='menuitem']").on('click.dropdownfield', function(event) {
            var link  = $(event.currentTarget);
            var value = link.attr('data-value');
            var label = link.attr('data-label');
            $('#' + spec.labelId).html(label);
            $('#' + spec.hiddenId).val(value);
            $('#' + spec.buttonId).dropdown('toggle');
            
            return false;
        });
    };
});