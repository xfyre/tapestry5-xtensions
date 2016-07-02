define(["jquery", "t5/core/events", "intlTelInputUtils"], function($, events, intlTelInputUtils) {
    return function(spec) {
        var $phone   = $('#' + spec.elementId)
        var $country = $('#' + $phone.data('country')) 
        var setupPhoneInput = function(){
            if ($country.length && $country.val())
                $phone.intlTelInput({
                    initialCountry: $country.val().toLowerCase(),
                    utilsScript: spec.utilsPath,
                    allowDropdown: false
                })
            else if (!$country.length && $phone.data('country'))
                $phone.intlTelInput({
                    initialCountry: $phone.data('country').toLowerCase(),
                    utilsScript: spec.utilsPath,
                    allowDropdown: false
                })
            else
                $phone.intlTelInput({
                    initialCountry: 'us',
                    utilsScript: spec.utilsPath,
                    allowDropdown: false
                })
        }
        $(document.body).on(events.zone.update, setupPhoneInput)
        $country.on('change', function(event) { $phone.intlTelInput("setCountry", $country.val().toLowerCase()) })
        $phone.on('blur', function(event) { $phone.val($phone.intlTelInput("getNumber", intlTelInputUtils.numberFormat.INTERNATIONAL)) })
        setupPhoneInput()
    }    
})
