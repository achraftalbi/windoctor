'use strict';

angular.module('windoctorApp')
    .factory('MailTypeSearch', function ($resource) {
        return $resource('api/_search/mailTypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
