define(["jquery", "t5/core/events", "t5/core/dom", "bootstrap/modal"], function($) {
    
    return function(spec) {
        var clientId = spec.clientId;
        var title    = spec.title;
        var message  = spec.message;
        var confirm  = spec.confirm;
        var cancel   = spec.cancel;
    
        var modalDialogId   = "bootstrap_confirmation_modal_" + spec.messagehash;
        var confirmButtonId = "bootstrap_confirmation_button_" + spec.messagehash;
        
        if (!$('#' + modalDialogId).length)
            $(document.body).append (
                '<div class="modal fade" role="dialog" id="' + modalDialogId + '">' +
                    '<div class="modal-dialog">' +
                        '<div class="modal-content">' +
                            '<div class="modal-header">' +
                                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
                                '<h3 class="modal-title">' + title + '</h3>' +
                            '</div>' +
                            '<div class="modal-body">' +
                                '<p>' + message + '</p>' +
                            '</div>' +
                            '<div class="modal-footer">' +
                                '<button class="btn ' + spec.confirmClass + '" id="' + confirmButtonId + '">' + confirm + '</button>' +
                                '<button class="btn" data-dismiss="modal">' + cancel + '</button>' + 
                            '</div>' +
                        '</div>' +
                    '</div>' +
                '</div>'                
            );

        $('#' + clientId).data('confirmationPassed', false);
        $('#' + clientId).bind('click', function(event){
            if ($('#' + clientId).data('confirmationPassed')) {
                $('#' + clientId).data('confirmationPassed', false);
                console.log('action on ' + clientId + ' confirmed');
                return true;
            } else {
                event.preventDefault();
                event.stopImmediatePropagation();
                console.log('confirmation on ' + clientId + ' requested');

                $('#' + modalDialogId).modal({keyboard:true});
                $('#' + confirmButtonId).bind('click', function(confirmEvent){
                    $('#' + clientId).data('confirmationPassed', true);
                    $('#' + modalDialogId).modal('hide');
                    $('#' + clientId).click();
                });
                return false;
            }
        });
    };
});