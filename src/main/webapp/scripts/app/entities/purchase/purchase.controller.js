'use strict';

angular.module('windoctorApp')
    .controller('PurchaseController', function ($scope, Purchase, PurchaseSearch, ParseLinks) {
        $scope.purchases = [];
        $scope.pagePurchase = 1;
        $scope.loadAllPurchases = function() {
            Purchase.query({page: $scope.pagePurchase, per_page: 5}, function(result, headers) {
                $scope.linksPurchase = ParseLinks.parse(headers('link'));
                $scope.purchases = result;
            });
        };
        $scope.loadPagePurchase = function(page) {
            $scope.pagePurchase = page;
            $scope.loadAllPurchases();
        };
        $scope.loadAllPurchases();

        $scope.deletePurchase = function (id) {
            Purchase.get({id: id}, function(result) {
                $scope.purchase = result;
                $('#deletePurchaseConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Purchase.deletePurchase({id: id},
                function () {
                    $scope.loadAllPurchases();
                    $('#deletePurchaseConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.searchPurchases = function () {
            PurchaseSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.purchases = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAllPurchases();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAllPurchases();
            $scope.clearPurchase();
        };

        $scope.clearPurchase = function () {
            $scope.purchase = {price: null, amount: null, creation_date: null, relative_date: null, id: null};
        };
    });
