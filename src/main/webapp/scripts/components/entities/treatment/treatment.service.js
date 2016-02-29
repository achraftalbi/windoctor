'use strict';

angular.module('windoctorApp')
    .factory('Treatment', function ($resource, DateUtils) {
        return $resource('api/treatments/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.treatment_date = DateUtils.convertDateTimeFromServer(data.treatment_date);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    })
    .factory('Benefits', function ($resource, DateUtils) {
        return $resource('api/benefits/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.treatment_date = DateUtils.convertDateTimeFromServer(data.treatment_date);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    });
