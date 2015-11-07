'use strict';

angular.module('windoctorApp')
    .factory('MailSettingSearch', function ($resource) {
        return $resource('api/_search/mailSettings/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
