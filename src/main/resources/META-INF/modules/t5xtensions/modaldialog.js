define(["jquery","t5/core/events", "t5/core/zone", "bootstrap/modal"], function($, events, zone) {
    return function(spec) {
        var $modal = $('#' + spec.id);
        
        var realWidth = spec.width > $(window).width() ? $(window).width() : spec.width;
        var realHeight = spec.height > $(window).height() ? $(window).height() : spec.height;
        var marginLeft = - (realWidth / 2);

        var $modalDialog = $('#' + spec.id + ' > .modal-dialog');
        $modalDialog.width(realWidth);
//        $modal.css({marginLeft:marginLeft});
//        $('div.modal-body').attr('style', 'max-height: ' + (realHeight - 60) + 'px;');
//        if (spec.height > $(window).height())
//            $modal.css({top:0});
        
        $modal.on('hidden.bs.modal', function(event) {
            $modal.remove();
            
            if (spec.updateZoneLink) {
                var $zone = $('#' + spec.zoneId);
                $zone.trigger(events.zone.refresh, {url: spec.updateZoneLink});
            }
        });

        if (spec.hide && spec.zoneId) {
            var $zone = $('#' + spec.zoneId);
            $zone.on(events.zone.update, function(){ $modal.modal('hide'); })
        }

        $modal.modal();
    };
});