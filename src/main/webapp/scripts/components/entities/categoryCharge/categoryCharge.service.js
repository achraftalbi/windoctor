'use strict';

angular.module('windoctorApp')
    .factory('CategoryCharge', function ($resource, DateUtils) {
        return $resource('api/categoryCharges/:id', {}, {
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
