define(["jquery", "t5/core/events"], function($, events) {
    return function(spec) {
        var $phone   = $('#' + spec.elementId)
        var $country = $('#' + $phone.data('country')) 
        var setupPhoneInput = function(){
            if ($country.length && $country.val())
                $phone.intlTelInput({
                    initialCountry: $country.val().toLowerCase(),
                    allowDropdown: false,
                    nationalMode: false
                })
            else if (!$country.length && $phone.data('country'))
                $phone.intlTelInput({
                    initialCountry: $phone.data('country').toLowerCase(),
                    allowDropdown: false,
                    nationalMode: false
                })
            else
                $phone.intlTelInput({
                    initialCountry: 'us',
                    allowDropdown: false,
                    nationalMode: false
                })

            $.fn.intlTelInput.loadUtils(spec.utilsPath)
        }
        $(document.body).on(events.zone.update, setupPhoneInput)
        setupPhoneInput()

        $country.on('change', function(event) { $phone.intlTelInput("setCountry", $country.val().toLowerCase()) })
        $phone.on('blur', function(event) { $phone.val($phone.intlTelInput("getNumber", 1)) }) // intlTelInputUtils.numberFormat.INTERNATIONAL
    }    
})
