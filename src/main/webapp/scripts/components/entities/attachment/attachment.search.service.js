'use strict';

angular.module('windoctorApp')
    .factory('AttachmentSearch', function ($resource) {
        return $resource('api/_search/attachments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
