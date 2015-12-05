'use strict';

angular.module('windoctorApp')
    .factory('Supply_type', function ($resource, DateUtils) {
        return $resource('api/supply_types/:id', {}, {
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
