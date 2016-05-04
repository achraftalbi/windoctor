'use strict';

angular.module('windoctorApp')
    .factory('CategoryActSearch', function ($resource) {
        return $resource('api/_search/categoryActs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
