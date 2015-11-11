define(["jquery", "t5/core/events", "t5/core/zone", "blueimp/jquery.fileupload"], function($, events, zone) {
    return function(spec) {
        var $input      = $('#' + spec.inputId)
        var $button     = $input.closest('.fileinput-button')
        var $dropZone	= $('#' + spec.dropZone)
        var $filename   = $('#' + spec.inputId + '_filename')
        var zoneId = spec.zone

        $input.fileupload({
            dataType: 'json',
            sequentialUploads: true,
            dropZone: $dropZone,
            autoUpload: true,
            url: spec.url,
            send: function(e, data) {
            	if (spec.sizeLimit) {
            		for (var i = 0; i < data.files.length; i++) {
            			if (data.files[i].size > spec.sizeLimit) {
            				alert("File size " + Math.round(data.files[i].size/1024) + "KB exceeds maximum allowed size " + Math.round(spec.sizeLimit/1024) + "KB")
            				return false
            			}
            		}
            	}

                $button.attr('disabled', 'disabled')
                if (spec.progress) $('#' + spec.inputId + '_progress').show()

                return true
            },
            done: function(e, data) {
                $button.removeAttr('disabled')
                var filename = data.files[0].name
                var size = Math.round(data.files[0].size/1024)
                $filename.val(filename + ' (' + size + ' KB)')
                
                if (spec.submit) {
                	$('#' + spec.submit).click()
                } else if (spec.zone) {
                    var zoneElement = spec.zone === '^' ? $input.closest('.t-zone') : $("#" + spec.zone)
                    
                    if (!zoneElement) {
                        Tapestry.error("Could not find zone element '#{zoneId}' to update on upload from '#{elementId}",
                                        {
                                            zoneId : spec.zone,
                                            elementId : spec.inputId
                                        })
                        return;
                    }

                    zoneElement.trigger(events.zone.refresh, { url: spec.zoneUpdateUrl })                
                }

                if (spec.progress) $('#' + spec.inputId + '_progress').hide()
            },
            progressall: function(e, data) {
            	if (spec.progress) {
                	var progress = parseInt(data.loaded / data.total * 100, 10)
            		$('#' + spec.inputId + '_progress .progress-bar').css('width', progress + '%')            	
            	}
            }
        });
    };
});
