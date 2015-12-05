'use strict';

angular.module('windoctorApp')
    .controller('Fund_historyController', function ($scope, Fund_history, Fund_historySearch, ParseLinks) {
        $scope.fund_historys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Fund_history.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.fund_historys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Fund_history.get({id: id}, function(result) {
                $scope.fund_history = result;
                $('#deleteFund_historyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Fund_history.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFund_historyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Fund_historySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fund_historys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.fund_history = {old_amount: null, new_amount: null, type_operation: null, amount_movement: null, id: null};
        };
    });
