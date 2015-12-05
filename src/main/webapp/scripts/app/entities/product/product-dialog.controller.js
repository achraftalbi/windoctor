'use strict';

angular.module('windoctorApp').controller('ProductDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Product', 'Category', 'Fund',
        function ($scope, $stateParams, $modalInstance, entity, Product, Category, Fund) {

            $scope.product = entity;
            $scope.categorys = Category.query();
            $scope.funds;
            $scope.fundContainEnoughMoney = true;
            $scope.load = function (id) {
                Product.get({id: id}, function (result) {
                    $scope.product = result;
                });
            };
            $scope.loadFunds = function () {
                Fund.query(function (result, headers) {
                        $scope.funds = result;
                        if ($scope.funds !== null && $scope.funds.length > 0) {
                            if ($scope.product.fund === null || $scope.product.fund === undefined) {
                                $scope.product.fund = $scope.funds[0];
                            }
                        }
                    }
                );

            };
            $scope.loadFunds();
            var onSaveFinished = function (result) {
                $scope.$emit('windoctorApp:productUpdate', result);
                $modalInstance.close(result);
            };

            $scope.changeFields = function () {
                console.log("$scope.fundContainEnoughMoney 1 " + $scope.fundContainEnoughMoney);
                if ($scope.product.fund === null || $scope.product.fund === undefined
                    || $scope.product.price === null || $scope.product.amount === null) {
                    $scope.fundContainEnoughMoney = true;
                } else {
                    if ($scope.product.price * $scope.product.amount > $scope.product.fund.amount) {
                        $scope.fundContainEnoughMoney = false;
                    } else {
                        $scope.fundContainEnoughMoney = true;
                    }
                }
                console.log("$scope.fundContainEnoughMoney 2 " + $scope.fundContainEnoughMoney);
            };

            $scope.save = function () {
                if ($scope.product.id != null) {
                    Product.update($scope.product, onSaveFinished);
                } else {
                    Product.save($scope.product, onSaveFinished);
                }
            };

            $scope.clear = function () {
                $modalInstance.dismiss('cancel');
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

            $scope.setImage = function ($files, product) {
                if ($files[0]) {
                    var file = $files[0];
                    var fileReader = new FileReader();
                    fileReader.readAsDataURL(file);
                    fileReader.onload = function (e) {
                        var data = e.target.result;
                        var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                        $scope.$apply(function () {
                            product.image = base64Data;
                        });
                    };
                }
            };
        }]);
