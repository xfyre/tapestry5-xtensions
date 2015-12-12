define(["jquery"], function($) {
    return function(spec) {
        if (spec.autosubmit)
            $('#' + spec.clientId).on('change.segmentedcontrol', function(event) {
                $('#' + spec.submitId).click();
            });
        
        if (spec.showCheckmark) {
            var $updateIcons = function() {
                $('#' + spec.clientId).find('input[type="radio"]').each(function() {
                    var $iconElement = $(this).closest('label').find('span[data-segment-icon]')
                    if ($(this).is(':checked')) {
                        $iconElement.removeClass('glyphicon glyphicon-unchecked')
                        $iconElement.addClass('glyphicon glyphicon-check')
                    } else {
                        $iconElement.removeClass('glyphicon glyphicon-check')
                        $iconElement.addClass('glyphicon glyphicon-unchecked')
                    }
                })
            }
            $('#' + spec.clientId).find('input[type="radio"]').on('change', $updateIcons)
            $updateIcons()
        }
    };
});