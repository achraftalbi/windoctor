'use strict';

angular.module('windoctorApp')
    .factory('Fund', function ($resource, DateUtils) {
        return $resource('api/funds/:id', {}, {
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
