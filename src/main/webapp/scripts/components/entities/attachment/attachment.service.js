'use strict';

angular.module('windoctorApp')
    .factory('Attachment', function ($resource, DateUtils) {
        return $resource('api/attachments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
