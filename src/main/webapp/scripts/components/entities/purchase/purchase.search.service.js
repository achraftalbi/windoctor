'use strict';

angular.module('windoctorApp')
    .factory('PurchaseSearch', function ($resource) {
        return $resource('api/_search/purchases/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
