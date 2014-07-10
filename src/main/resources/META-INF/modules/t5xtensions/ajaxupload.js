define(["jquery", "blueimp/jquery.fileupload"], function($) {
    return function(spec) {
        var $input = $('#' + spec.inputId);
        var $button = $input.closest('.fileinput-button');
        var filenameId = spec.inputId + '_filename';
        
        $input.fileupload({
            dataType: 'json',
            sequentialUploads: true,
            autoUpload: true,
            url: spec.url,
            send: function(e, data) {
                $button.attr('disabled', 'disabled');
                $('#' + filenameId).remove();
                return true;
            },
            done: function(e, data) {
                $button.removeAttr('disabled');
                var filename = data.files[0].name;
                var size = Math.round(data.files[0].size / 1024);
                $button.after($('<span id="' + filenameId + '" style="margin-left: 16px;">' + filename + ' (' + size + ' KB)</span>'));
            }
        });
    };
});
