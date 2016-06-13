'use strict';


angular.module('windoctorApp').expandProductController =
    function ($scope, $stateParams, Product, Category, Fund,Principal,Purchase,ParseLinks,$filter,Consumption) {

        $scope.fundContainEnoughMoney = true;
        $scope.savePurchaseOnGoing = false;
        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:productUpdate', result);
            $scope.product = result;
        };

        var onSaveAndCloseFinished = function (result) {
            $scope.$emit('windoctorApp:productUpdate', result);
            $scope.clear();
        };

        $scope.changeFields = function () {
            console.log("$scope.fundContainEnoughMoney 1 " + $scope.fundContainEnoughMoney);
            if ($scope.purchase.purchase_fund === null || $scope.purchase.purchase_fund === undefined
                || $scope.purchase.price === null || $scope.purchase.amount === null) {
                $scope.fundContainEnoughMoney = true;
            } else {
                var oldTotal = $scope.purchaseOld.purchase_fund===null?0:
                    ($scope.purchaseOld.purchase_fund.id===$scope.purchase.purchase_fund.id?($scope.purchaseOld.price * $scope.purchaseOld.amount):0);
                if ((($scope.purchase.price * $scope.purchase.amount)-oldTotal)
                    > $scope.purchase.purchase_fund.amount) {
                    $scope.fundContainEnoughMoney = false;
                } else {
                    $scope.fundContainEnoughMoney = true;
                }
            }
            console.log("$scope.fundContainEnoughMoney 2 " + $scope.fundContainEnoughMoney);
        };

        $scope.save = function () {
            if ($scope.product.id !== null) {
                Product.update($scope.product, onSaveFinished);
            } else {
                Product.save($scope.product, onSaveFinished);
            }
        };

        $scope.saveAndClose = function () {
            if ($scope.product.id !== null) {
                Product.update($scope.product, onSaveAndCloseFinished);
            } else {
                Product.save($scope.product, onSaveAndCloseFinished);
            }
        };

        $scope.clear = function () {
            $scope.displayStock= true;
            if($scope.addProductField){
                $scope.page = 1;
                $scope.loadAll();
            }
            $scope.loadPageThreshold(1);
            $scope.addProductField= false;
            $scope.editProductField= false;
        };


        /********************************************************************************/
        /********************************************************************************/
        /***********************                                        *****************/
        /***********************         Purchase treatments            *****************/
        /***********************                                        *****************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.purchases = [];
        $scope.pagePurchase = 1;
        $scope.addPurchaseField = false;
        $scope.editPurchaseField = false;
        $scope.loadAllPurchases = function() {
            Purchase.query({productId: $scope.product.id,page: $scope.pagePurchase, per_page: 5}, function(result, headers) {
                $scope.linksPurchase = ParseLinks.parse(headers('link'));
                $scope.purchases = result;
            });
        };

        $scope.loadPagePurchase = function(page) {
            $scope.pagePurchase = page;
            $scope.loadAllPurchases();
        };
        $scope.loadAllPurchases();

        $scope.deletePurchase = function (purchase) {
            $scope.cancelPurchase();
            $scope.purchase = purchase;
            $('#deletePurchaseConfirmation').modal('show');
        };

        $scope.confirmDeletePurchase = function (purchase) {
            Purchase.delete({id: purchase.id},
                function () {
                    $scope.product.price = $scope.product.price - (purchase.price*purchase.amount);
                    $scope.product.amount = $scope.product.amount - purchase.amount;
                    Fund.query(function (result, headers) {
                            $scope.funds = result;
                            $scope.clearPurchase();
                            $scope.loadAllPurchases();
                            $('#deletePurchaseConfirmation').modal('hide');
                        }
                    );
                });
        };

        $scope.closeDeletePurchase = function (purchase) {
            $scope.purchase = purchase;
            $('#deletePurchaseConfirmation').modal('hide');
       };

        $scope.clearPurchase = function () {
            $scope.purchase = {price: null, amount: null, creation_date: null, relative_date: null, purchase_fund: null, id: null};
        };

        $scope.addPurchase = function () {
            $scope.fundContainEnoughMoney = true;
            $scope.purchase = {price: null, amount: null, creation_date: null, relative_date: null, id: null};
            $scope.purchase.purchase_fund =  $scope.funds[0];
            $scope.dateValuePurchase = moment(new Date()).utc();
            $scope.addPurchaseField = true;
            $scope.purchaseOld = {price:0,amount:0,purchase_fund:null};
        };

        $scope.cancelPurchase = function () {
            if($scope.editPurchaseField){
                $scope.purchase.price = $scope.purchaseOld.price;
                $scope.purchase.amount = $scope.purchaseOld.amount;
                $scope.purchase.purchase_fund = $scope.purchaseOld.purchase_fund;
                $scope.purchase.relative_date = $scope.purchaseOld.relative_date;
            }
            $scope.addPurchaseField = false;
            $scope.editPurchaseField = false;
            $scope.fundContainEnoughMoney = true;
            $scope.clearPurchase();
            $scope.purchaseOld = {price:0,amount:0,purchase_fund:null};
        };

        $scope.editPurchase = function (purchase) {
            $scope.fundContainEnoughMoney = true;
            $scope.purchase = purchase;
            $scope.purchaseOld = {price:0,amount:0,purchase_fund:null};
            angular.copy($scope.purchase, $scope.purchaseOld);
            $scope.dateValuePurchase = new Date($filter('date')($scope.purchase.relative_date, 'yyyy-MM-dd HH:mm', '+0000'));
            $scope.editPurchaseField = true;
        };

        var onSavePurchaseFinishedUpdate = function (result) {
            $scope.$emit('windoctorApp:purchaseUpdate', result);
            var fundPurchaseId = $scope.purchase.purchase_fund.id;
            $scope.funds=null;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    for(var i= 0;$scope.funds.length;i++){
                        if($scope.funds[i].id===fundPurchaseId){
                            $scope.purchase.purchase_fund = $scope.funds[i];
                            break;
                        }
                    }
                    $scope.editPurchaseField = false;
                    $scope.savePurchaseOnGoing = false;
                }
            );
            $scope.product.price = $scope.product.price +
                ((result.price*result.amount) -($scope.purchaseOld.price*$scope.purchaseOld.amount));
            $scope.product.amount = $scope.product.amount + (result.amount-$scope.purchaseOld.amount);
        };

        var onSavePurchaseFinished = function (result) {
            $scope.$emit('windoctorApp:purchaseUpdate', result);
            $scope.pagePurchase = 1;
            $scope.loadAllPurchases();
            $scope.product.price = $scope.product.price + (result.price*result.amount);
            $scope.product.amount = $scope.product.amount + result.amount;
            $scope.purchase.purchase_fund.amount = $scope.purchase.purchase_fund.amount-($scope.purchase.price * $scope.purchase.amount);
            var fundPurchaseId = $scope.purchase.purchase_fund.id;
            $scope.funds=null;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    for(var i= 0;$scope.funds.length;i++){
                        if($scope.funds[i].id===fundPurchaseId){
                            $scope.purchase.purchase_fund = $scope.funds[i];
                            break;
                        }
                    }
                    $scope.addPurchaseField = false;
                    $scope.savePurchaseOnGoing = false;
                }
            );
        };

        $scope.savePurchase = function () {
            $scope.purchase.relative_date = new Date($scope.dateValuePurchase);
            console.log(' has had a date change. $scope.dateValuePurchase ' + $scope.purchase.relative_date);
            for(var i= 0;$scope.funds.length;i++){
                if($scope.funds[i].id===$scope.purchase.purchase_fund.id){
                    $scope.purchase.purchase_fund = $scope.funds[i];
                    break;
                }
            }
            $scope.savePurchaseOnGoing = true;
            if ($scope.purchase.id != null) {
                Purchase.update($scope.purchase, onSavePurchaseFinishedUpdate);
            } else {
                $scope.purchase.purchase_product = {id: $scope.product.id};
                Purchase.save($scope.purchase, onSavePurchaseFinished);
            }
        };

        /********************************************************************************/
        /********************************************************************************/
        /***********************                                        *****************/
        /***********************         Consumption treatments            *****************/
        /***********************                                        *****************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.consumptions = [];
        $scope.pageConsumption = 1;
        $scope.addConsumptionField = false;
        $scope.editConsumptionField = false;
        $scope.productContainEnoughAmount = true;
        $scope.loadAllConsumptions = function() {
            Consumption.query({productId: $scope.product.id,page: $scope.pageConsumption, per_page: 5}, function(result, headers) {
                $scope.linksConsumption = ParseLinks.parse(headers('link'));
                $scope.consumptions = result;
            });
        };

        $scope.loadPageConsumption = function(page) {
            $scope.pageConsumption = page;
            $scope.loadAllConsumptions();
        };
        $scope.loadAllConsumptions();

        $scope.deleteConsumption = function (consumption) {
            $scope.cancelConsumption();
            $scope.consumption = consumption;
            $('#deleteConsumptionConfirmation').modal('show');
        };

        $scope.confirmDeleteConsumption = function (consumption) {
            Consumption.delete({id: consumption.id},
                function () {
                    $scope.product.amount = $scope.product.amount + consumption.amount;
                    $scope.clearConsumption();
                    $scope.loadAllConsumptions();
                    $('#deleteConsumptionConfirmation').modal('hide');
                });
        };

        $scope.closeDeleteConsumption = function (consumption) {
            $scope.consumption = consumption;
            $('#deleteConsumptionConfirmation').modal('hide');
        };

        $scope.clearConsumption = function () {
            $scope.consumption = {amount: null, creation_date: null, relative_date: null, id: null};
        };

        $scope.addConsumption = function () {
            $scope.productContainEnoughAmount = true;
            $scope.consumption = {amount: null, creation_date: null, relative_date: null, id: null};
            $scope.dateValueConsumption = moment(new Date()).utc();
            $scope.addConsumptionField = true;
            $scope.consumptionOld = {amount:0};
        };

        $scope.cancelConsumption = function () {
            $scope.productContainEnoughAmount = true;
            if($scope.editConsumptionField){
                $scope.consumption.amount = $scope.consumptionOld.amount;
                $scope.consumption.relative_date = $scope.consumptionOld.relative_date;
            }
            $scope.addConsumptionField = false;
            $scope.editConsumptionField = false;
            $scope.clearConsumption();
            $scope.consumptionOld = {amount:0};
        };

        $scope.editConsumption = function (consumption) {
            $scope.productContainEnoughAmount = true;
            $scope.consumption = consumption;
            $scope.consumptionOld = {amount:0};
            angular.copy($scope.consumption, $scope.consumptionOld);
            $scope.dateValueConsumption = new Date($filter('date')($scope.consumption.relative_date, 'yyyy-MM-dd HH:mm', '+0000'));
            $scope.editConsumptionField = true;
        };

        var onSaveConsumptionFinishedUpdate = function (result) {
            $scope.$emit('windoctorApp:consumptionUpdate', result);
            $scope.addConsumptionField = false;
            $scope.editConsumptionField = false;
            $scope.funds=null;
            $scope.product.amount = $scope.product.amount - (result.amount-$scope.consumptionOld.amount);
        };

        var onSaveConsumptionFinished = function (result) {
            $scope.$emit('windoctorApp:consumptionUpdate', result);
            $scope.addConsumptionField = false;
            $scope.editConsumptionField = false;
            $scope.pageConsumption = 1;
            $scope.loadAllConsumptions();
            $scope.product.amount = $scope.product.amount - result.amount;
        };

        $scope.saveConsumption = function () {
            $scope.consumption.relative_date = new Date($scope.dateValueConsumption);
            if ($scope.consumption.id != null) {
                Consumption.update($scope.consumption, onSaveConsumptionFinishedUpdate);
            } else {
                $scope.consumption.consumption_product = {id: $scope.product.id};
                Consumption.save($scope.consumption, onSaveConsumptionFinished);
            }
        };

        $scope.changeAmount = function () {
            if ($scope.consumption.amount === null) {
                $scope.productContainEnoughAmount = true;
            } else {
                if (($scope.consumption.amount-$scope.consumptionOld.amount)
                    > $scope.product.amount) {
                    $scope.productContainEnoughAmount = false;
                } else {
                    $scope.productContainEnoughAmount = true;
                }
            }
            console.log("$scope.productContainEnoughAmount 2 " + $scope.productContainEnoughAmount);
        };

    };
