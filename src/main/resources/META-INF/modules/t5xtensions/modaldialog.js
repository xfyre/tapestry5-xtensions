define(["jquery","t5/core/events", "t5/core/zone", "bootstrap/modal"], function($, events, zone) {
    return function(spec) {
        var $modal = $('#' + spec.id);
        
        var realWidth = spec.width > $(window).width() ? $(window).width() : spec.width;
        var realHeight = spec.height > $(window).height() ? $(window).height() : spec.height;
        var marginLeft = - (realWidth / 2);
        
//        $modal.width(realWidth);
//        $('div.modal-body').attr('style', 'max-height: ' + (realHeight - 60) + 'px;');
//        $modal.css({marginLeft:marginLeft});
//        if (spec.height > $(window).height())
//            $modal.css({top:0});
        
        $modal.on('hidden.bs.modal', function(event) {
            $modal.remove();
            
            if (spec.updateZoneLink) {
                var $zone = $('#' + spec.zoneId);
                $zone.trigger(events.zone.refresh, {url: spec.updateZoneLink});
            }
        });
        $modal.modal();
    };
});