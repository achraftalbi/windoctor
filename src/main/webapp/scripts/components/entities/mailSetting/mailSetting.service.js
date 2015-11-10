'use strict';

angular.module('windoctorApp')
    .factory('MailSetting', function ($resource, DateUtils) {
        return $resource('api/mailSettings/:id', {}, {
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
