'use strict';

angular.module('windoctorApp')
    .controller('FundController', function ($scope, Fund, FundSearch, ParseLinks,Fund_history) {
        $scope.funds = [];
        $scope.fund_historys = [];
        $scope.page = 1;
        $scope.pageHistory = 1;
        $scope.selectedFund;
        $scope.loadAll = function() {
            Fund.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.funds = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadPageHistory = function(pageHistory) {
            $scope.pageHistory = pageHistory;
            $scope.loadHistories();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Fund.get({id: id}, function(result) {
                $scope.fund = result;
                $('#deleteFundConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Fund.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFundConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.displayHistories = function (fund) {
            $scope.pageHistory = 1;
            $scope.selectedFund = fund;
            $scope.loadHistories();
        };
        $scope.loadHistories = function() {
            Fund_history.query({fundId:$scope.selectedFund.id, page: $scope.pageHistory, per_page: 5}, function(result, headers) {
                $scope.linksHistories = ParseLinks.parse(headers('link'));
                $scope.fund_historys = result;
            });
        };

        $scope.search = function () {
            FundSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.funds = result;
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
            $scope.fund = {description: null, amount: null, id: null};
        };
    });
