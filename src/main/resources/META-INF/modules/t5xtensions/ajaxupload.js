define(["jquery", "t5/core/events", "t5/core/zone", "blueimp/jquery.fileupload"], function($, events, zone) {
    return function(spec) {
        var $input      = $('#' + spec.inputId);
        var $button     = $input.closest('.fileinput-button');
        var $filename   = $('#' + spec.inputId + '_filename' );
        var zoneId = spec.zone;
        
        $input.fileupload({
            dataType: 'json',
            sequentialUploads: true,
            autoUpload: true,
            url: spec.url,
            send: function(e, data) {
                $button.attr('disabled', 'disabled');
                return true;
            },
            done: function(e, data) {
                $button.removeAttr('disabled');
                var filename = data.files[0].name;
                var size = Math.round(data.files[0].size / 1024);
                $filename.val(filename + ' (' + size + ' KB)');
                
                if (spec.zone) {
                    var zoneElement = spec.zone === '^' ? $input.closest('.t-zone') : $("#" + spec.zone);
                    
                    if (!zoneElement) {
                        Tapestry.error("Could not find zone element '#{zoneId}' to update on upload from '#{elementId}",
                                        {
                                            zoneId : spec.zone,
                                            elementId : spec.inputId
                                        });
                        return;
                    }
                    
                    zoneElement.trigger(events.zone.refresh, { url: spec.zoneUpdateUrl });                
                }
            }
        });
    };
});
