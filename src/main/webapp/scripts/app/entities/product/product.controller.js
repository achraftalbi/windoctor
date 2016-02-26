'use strict';

angular.module('windoctorApp')
    .controller('ProductController', function ($scope, Product, ProductSearch, ParseLinks) {
        $scope.products = [];
        $scope.productsThreshold = [];
        $scope.page = 1;
        $scope.pageThreshold = 1;
        $scope.searchCalled = false;
        $scope.loadAll = function () {
            Product.query({typeProductToGet:1,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.products = result;
            });
        };
        $scope.loadAllThreshold = function () {
            Product.query({typeProductToGet:2,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.linksThreshold = ParseLinks.parse(headers('link'));
                $scope.productsThreshold = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearch();
            }else{
                $scope.loadAll();
            }
        };

        $scope.loadPageThreshold = function (page) {
            $scope.pageThreshold = page;
            $scope.loadAllThreshold();
        };
        $scope.loadAll();
        $scope.loadAllThreshold();

        $scope.delete = function (id) {
            Product.get({id: id}, function (result) {
                $scope.product = result;
                $('#deleteProductConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Product.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProductConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };
        $scope.loadAllSearch = function () {
            ProductSearch.query({query: $scope.searchQuery,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.products = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.product = {name: null, image: null, price: null, amount: null, id: null};
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }

            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }

            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }

            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };
    });
