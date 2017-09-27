'use strict';

angular.module('windoctorApp')
    .factory('Prescription_medication', function ($resource, DateUtils) {
        return $resource('api/prescription_medications/:id', {}, {
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
