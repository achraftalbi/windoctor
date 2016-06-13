'use strict';


angular.module('windoctorApp').expandChargeController =
    function ($scope, $stateParams, Charge, CategoryCharge, Fund,Principal,Entry,ParseLinks,$filter) {
        $scope.saveEntryOnGoing = false;
        $scope.fundContainEnoughMoney = true;
        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:chargeUpdate', result);
            $scope.charge = result;
        };

        var onSaveAndCloseFinished = function (result) {
            $scope.$emit('windoctorApp:chargeUpdate', result);
            $scope.clear();
        };

        $scope.changeFields = function () {
            console.log("$scope.fundContainEnoughMoney 1 " + $scope.fundContainEnoughMoney);
            if ($scope.entry.fund === null || $scope.entry.fund === undefined
                || $scope.entry.price === null || $scope.entry.amount === null) {
                $scope.fundContainEnoughMoney = true;
            } else {
                var oldTotal = $scope.entryOld.fund===null?0:
                    ($scope.entryOld.fund.id===$scope.entry.fund.id?($scope.entryOld.price * $scope.entryOld.amount):0);
                if ((($scope.entry.price * $scope.entry.amount)-oldTotal)
                    > $scope.entry.fund.amount) {
                    $scope.fundContainEnoughMoney = false;
                } else {
                    $scope.fundContainEnoughMoney = true;
                }
            }
            console.log("$scope.fundContainEnoughMoney 2 " + $scope.fundContainEnoughMoney);
        };

        $scope.save = function () {
            if ($scope.charge.id !== null) {
                Charge.update($scope.charge, onSaveFinished);
            } else {
                Charge.save($scope.charge, onSaveFinished);
            }
        };

        $scope.saveAndClose = function () {
            if ($scope.charge.id !== null) {
                Charge.update($scope.charge, onSaveAndCloseFinished);
            } else {
                Charge.save($scope.charge, onSaveAndCloseFinished);
            }
        };

        $scope.clear = function () {
            $scope.displayCharge= true;
            if($scope.addChargeField){
                $scope.page = 1;
                $scope.loadAll();
            }
            $scope.loadPageThreshold(1);
            $scope.addChargeField= false;
            $scope.editChargeField= false;
        };


        /********************************************************************************/
        /********************************************************************************/
        /***********************                                        *****************/
        /***********************         Entry treatments            *****************/
        /***********************                                        *****************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.entrys = [];
        $scope.pageEntry = 1;
        $scope.addEntryField = false;
        $scope.editEntryField = false;
        $scope.loadAllEntrys = function() {
            Entry.query({chargeId: $scope.charge.id,page: $scope.pageEntry, per_page: 5}, function(result, headers) {
                $scope.linksEntry = ParseLinks.parse(headers('link'));
                $scope.entrys = result;
            });
        };

        $scope.loadPageEntry = function(page) {
            $scope.pageEntry = page;
            $scope.loadAllEntrys();
        };
        $scope.loadAllEntrys();

        $scope.deleteEntry = function (entry) {
            $scope.cancelEntry();
            $scope.entry = entry;
            $('#deleteEntryConfirmation').modal('show');
        };

        $scope.confirmDeleteEntry = function (entry) {
            Entry.delete({id: entry.id},
                function () {
                    $scope.charge.price = $scope.charge.price - (entry.price*entry.amount);
                    $scope.charge.amount = $scope.charge.amount - entry.amount;
                    Fund.query(function (result, headers) {
                            $scope.funds = result;
                            $scope.clearEntry();
                            $scope.loadAllEntrys();
                            $('#deleteEntryConfirmation').modal('hide');
                        }
                    );
                });
        };

        $scope.closeDeleteEntry = function (entry) {
            $scope.entry = entry;
            $('#deleteEntryConfirmation').modal('hide');
        };

        $scope.clearEntry = function () {
            $scope.entry = {price: null, amount: null, creation_date: null, relative_date: null, fund: null, id: null};
        };

        $scope.addEntry = function () {
            $scope.fundContainEnoughMoney = true;
            $scope.entry = {price: null, amount: null, creation_date: null, relative_date: null, id: null};
            $scope.entry.fund =  $scope.funds[0];
            $scope.dateValueEntry = moment(new Date()).utc();
            $scope.addEntryField = true;
            $scope.entryOld = {price:0,amount:0,fund:null};
        };

        $scope.cancelEntry = function () {
            if($scope.editEntryField){
                $scope.entry.price = $scope.entryOld.price;
                $scope.entry.amount = $scope.entryOld.amount;
                $scope.entry.fund = $scope.entryOld.fund;
                $scope.entry.relative_date = $scope.entryOld.relative_date;
            }
            $scope.addEntryField = false;
            $scope.editEntryField = false;
            $scope.fundContainEnoughMoney = true;
            $scope.clearEntry();
            $scope.entryOld = {price:0,amount:0,fund:null};
        };

        $scope.editEntry = function (entry) {
            $scope.fundContainEnoughMoney = true;
            $scope.entry = entry;
            $scope.entryOld = {price:0,amount:0,fund:null};
            angular.copy($scope.entry, $scope.entryOld);
            $scope.dateValueEntry = new Date($filter('date')($scope.entry.relative_date, 'yyyy-MM-dd HH:mm', '+0000'));
            $scope.editEntryField = true;
        };

        var onSaveEntryFinishedUpdate = function (result) {
            $scope.$emit('windoctorApp:entryUpdate', result);
            var fundEntryId = $scope.entry.fund.id;
            $scope.funds=null;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    for(var i= 0;$scope.funds.length;i++){
                        if($scope.funds[i].id===fundEntryId){
                            $scope.entry.fund = $scope.funds[i];
                            break;
                        }
                    }
                    $scope.editEntryField = false;
                    $scope.saveEntryOnGoing = false;
                }
            );
            $scope.charge.price = $scope.charge.price +
                ((result.price*result.amount) -($scope.entryOld.price*$scope.entryOld.amount));
            $scope.charge.amount = $scope.charge.amount + (result.amount-$scope.entryOld.amount);
        };

        var onSaveEntryFinished = function (result) {
            $scope.$emit('windoctorApp:entryUpdate', result);
            $scope.pageEntry = 1;
            $scope.loadAllEntrys();
            $scope.charge.price = $scope.charge.price + (result.price*result.amount);
            $scope.charge.amount = $scope.charge.amount + result.amount;
            $scope.entry.fund.amount = $scope.entry.fund.amount-($scope.entry.price * $scope.entry.amount);
            var fundEntryId = $scope.entry.fund.id;
            $scope.funds=null;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    for(var i= 0;$scope.funds.length;i++){
                        if($scope.funds[i].id===fundEntryId){
                            $scope.entry.fund = $scope.funds[i];
                            break;
                        }
                    }
                    $scope.addEntryField = false;
                    $scope.saveEntryOnGoing = false;
                }
            );
        };

        $scope.saveEntry = function () {
            $scope.entry.relative_date = new Date($scope.dateValueEntry);
            console.log(' has had a date change. $scope.dateValueEntry ' + $scope.entry.relative_date);
            for(var i= 0;$scope.funds.length;i++){
                if($scope.funds[i].id===$scope.entry.fund.id){
                    $scope.entry.fund = $scope.funds[i];
                    break;
                }
            }
            $scope.saveEntryOnGoing = true;
            if ($scope.entry.id != null) {
                Entry.update($scope.entry, onSaveEntryFinishedUpdate);
            } else {
                $scope.entry.charge = {id: $scope.charge.id};
                Entry.save($scope.entry, onSaveEntryFinished);
            }
        };

    };
