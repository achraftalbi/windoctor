'use strict';

angular.module('windoctorApp')
    .factory('MailType', function ($resource, DateUtils) {
        return $resource('api/mailTypes/:id', {}, {
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
