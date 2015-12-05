'use strict';

angular.module('windoctorApp')
    .factory('Fund_historySearch', function ($resource) {
        return $resource('api/_search/fund_historys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
