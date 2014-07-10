define(["jquery", "t5/core/utils"], function($, utils) {
    return function(spec) {
        var engine = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace(spec.displayKey),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: { 
                url: spec.url, 
                replace: function(uri, query) { 
                    return utils.extendURL(uri, {"t:input": query}); 
                }, 
                filter: function(response) {
                    return response.matches;
                }
            }
        });
        
        engine.initialize ();
        
        var $ttField    = $('#' + spec.id);
        
        $ttField.typeahead(
            { 
                minLength: 2,
                highlight: true
            },
            {
                name: 'suggestions-list',
                displayKey: spec.displayKey,
                source: engine.ttAdapter(),
                templates: { suggestion: Handlebars.compile(spec.template) }
            }
        );
    };
});