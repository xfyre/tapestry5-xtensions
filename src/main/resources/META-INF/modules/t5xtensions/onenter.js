define(["jquery"], function($) {
    return function(spec) {
        $(document).ready(function() {
            $('#' + spec.formId).on('keypress', function(event) {
                if (event.keyCode == 13) {
                    var target = $(event.target);
                    if (!target.is("textarea") && !target.is(":button,:submit")) {
                        event.preventDefault();
                        $('#' + spec.submitId).click();
                        return false;
                    }
                }
            });
        });
    };
});
