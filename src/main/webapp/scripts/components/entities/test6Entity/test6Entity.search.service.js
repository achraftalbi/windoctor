'use strict';

angular.module('windoctorApp')
    .factory('Test6EntitySearch', function ($resource) {
        return $resource('api/_search/test6Entitys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
