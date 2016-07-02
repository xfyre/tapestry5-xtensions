define(["jquery", "t5/core/utils"], function($, utils) {
    return function(spec) {
        var $ttField    = $('#' + spec.id)
        var engine = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace(spec.displayKey),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            limit: 8,
            remote: { 
                url: spec.url, 
                replace: function(uri, query) { 
                    return utils.extendURL(uri, {"t:input": query}); 
                }, 
                filter: function(response) {
                    return response.matches;
                },
                ajax: {
                    beforeSend: function() { $ttField.addClass("tt-progress")    },
                    complete:   function() { $ttField.removeClass("tt-progress") }
                }
            }
        })
        
        engine.initialize ()
        
        $ttField.typeahead(
            { 
                minLength: spec.minLength,
                highlight: true,
                hint: spec.showHint
            },
            {
                name: 'suggestions-list',
                displayKey: spec.displayKey,
                source: engine.ttAdapter(),
                templates: { suggestion: Handlebars.compile(spec.template) }
            }
        )
        
        if (spec.identifierField) {
            var $identifierField = $('#' + spec.identifierField);
            
            $ttField.on('typeahead:selected', function(event, suggestion, dataset) {
                $identifierField.val(suggestion[spec.identifierKey])
                $ttField.trigger('typeaheadmixin:selected')
            })
            
            $ttField.on('typeahead:opened', function(event) {
                if (!$identifierField.val() || !$ttField.val()) {
                    $identifierField.val(null);
                }
            })
            
            $ttField.on('typeahead:closed', function(event) {
                if (!$identifierField.val() || !$ttField.val()) {
                    $identifierField.val(null);
                }
            })            
        }
    }
})
