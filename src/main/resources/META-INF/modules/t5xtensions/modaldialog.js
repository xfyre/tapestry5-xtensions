define(["jquery","t5/core/events", "t5/core/zone", "bootstrap/modal"], function($, events, zone) {
    return function(spec) {
        var $modal = $('#' + spec.id)

        $modal.on('hidden.bs.modal', function(event) {
            $modal.remove()
            if (spec.updateZoneLink) {
                $('#' + spec.updateZone).trigger(events.zone.refresh, {url: spec.updateZoneLink})
            }
        })

        if (spec.monitorZone)
            $('#' + spec.monitorZone).on(events.zone.didUpdate, function(event){
                if (spec.hide && !$modal.find('form[data-submission-failed="true"]').length) $modal.modal('hide') 
            })

        $modal.modal()
    }
});

