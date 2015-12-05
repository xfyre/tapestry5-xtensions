define(["jquery","t5/core/events", "t5/core/zone", "bootstrap/modal"], function($, events, zone) {
    return function(spec) {
        var $modal = $('#' + spec.id);
        
        /*
        if (spec.width) {
            var realWidth = spec.width > $(window).width() ? $(window).width() : spec.width;
            $('#' + spec.id + ' > .modal-dialog').width(realWidth);
        }
        */
        
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