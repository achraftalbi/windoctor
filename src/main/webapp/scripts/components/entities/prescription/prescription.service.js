'use strict';

angular.module('windoctorApp')
    .factory('Prescription', function ($resource, DateUtils) {
        return $resource('api/prescriptions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    data.update_date = DateUtils.convertDateTimeFromServer(data.update_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
