'use strict';

angular.module('windoctorApp')
    .controller('PatientChartController', function ($scope, $stateParams, $modalInstance, Treatment, TreatmentSearch, Doctor,
                                                         ParseLinks, $filter, Event_reason, Event, Patient,Attachment,Fund) {
        $scope.treatments = [];
        $scope.page = 1;
        $scope.attachmentPage = 1;
        $scope.treatment = null;
        $scope.treatmentTotal = null;
        $scope.oldTreatmentPaidPrice = null;
        $scope.oldTreatmentPrice = null;
        $scope.patient = null;
        $scope.doctors = null;
        $scope.defaultDoctor = null;
        $scope.treatmentToDelete = null;

        $scope.displayTreatments = true;
        $scope.displayAddEditViewPopup = false;
        $scope.displayAddEditViewAttachmentPopup = false;
        $scope.displayTreatmentView = false;
        $scope.displayTreatmentToDelete = false;
        $scope.event_reasons = null;
        $scope.defaultEventReason = null;
        $scope.eventReasonSelected = false;
        $scope.funds;
        $scope.oldFundContainEnoughMoney = true;
        $scope.oldFund=null;
        $scope.searchCalled = false;
        /*Attachments variables */
        $scope.dispalyAttachments = false;
        $scope.attachments = null;
        $scope.attachment = null;
        $scope.viewSelectedAttachment = false;
        $scope.priceLessThanPaidPrice = false;
        $scope.captureAnImageScreen = false;

        // Display treatments Begin
        $scope.loadAll = function () {
                Treatment.query({
                        patientId: $stateParams.patientId,
                        page: $scope.page,
                        per_page: 5
                    }, function (result, headers) {
                        $scope.links = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                        if($scope.page===1 && $scope.treatments!==null &&
                            $scope.treatments!=undefined && $scope.treatments.length>0){
                            $scope.treatmentTotal = $scope.treatments[$scope.treatments.length-1];
                        }else if($scope.page>1){
                            $scope.treatments.push($scope.treatmentTotal);
                        }
                        Patient.get({id: $stateParams.patientId}, function (result) {
                            $scope.patient = result;
                        });
                        console.log('$scope.treatmentTotal.id '+$scope.treatmentTotal.id);
                    }
                );
        };

        $scope.loadFunds = function () {
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    console.log("$scope.treatment.fund 0 ")
                    if ($scope.funds !== null && $scope.funds.length > 0) {
                        console.log("$scope.treatment.fund 1 ")
                        if ($scope.treatment.fund === null || $scope.treatment.fund === undefined) {
                            $scope.treatment.fund = $scope.funds[0];
                            console.log("$scope.treatment.fund 2 "+$scope.treatment.fund)
                        }
                    }
                }
            );

        };
        $scope.delete = function (id) {
            Treatment.get({id: id}, function (result) {
                $scope.treatmentToDelete = result;
                $scope.displayTreatmentToDelete = true;
                $scope.displayTreatments = false;
            });
        };

        $scope.confirmDelete = function (id) {
            Treatment.delete({id: id},
                function () {
                    $scope.loadPage($scope.page);
                    $scope.displayTreatmentToDelete = false;
                    $scope.displayTreatments = true;
                });
        };

        ///////////////////////
        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.search();
            }else{
                $scope.loadAll();
            }
        };
        $scope.loadAll();
        $scope.searchPatient = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.search();
        };
        /******************************************************************************************************/
        /***** Very important. I let the search to use it for see office benefit. But it must change next.*****/
        $scope.search = function () {
            if($scope.searchQuery!==null && $scope.searchQuery!==undefined && $scope.searchQuery.length>0) {
                TreatmentSearch.query({
                    query: $scope.searchQuery, eventId: 0,
                    page: $scope.page, per_page: 5
                }, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.treatments = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAll();
                    }
                });
            }else{
                $scope.loadAll();
            }
        };


        //////////////////////

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null};
        };
        $scope.clearDeleteTreatment = function () {
            $scope.treatmentToDelete = {
                treatment_date: null,
                description: null,
                price: null,
                paid_price: null,
                id: null
            };
            $scope.displayTreatmentToDelete = false;
            $scope.displayTreatments = true;
        };
        $scope.cancelTreatmentRows = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.changeFields = function () {
            console.log("changeFields 0");
            if ($scope.treatment.fund === null || $scope.treatment.fund === undefined
                || $scope.oldFund === null || $scope.oldTreatmentPaidPrice ==null) {
                $scope.oldFundContainEnoughMoney = true;
                console.log("changeFields 2");
            } else if ($scope.oldFund.id===$scope.treatment.fund.id &&($scope.oldFund.amount - ( $scope.oldTreatmentPaidPrice - $scope.treatment.paid_price)) < 0) {
                $scope.oldFundContainEnoughMoney = false;
                console.log("changeFields 3");
            } else if ($scope.oldFund.id!==$scope.treatment.fund.id &&($scope.oldFund.amount - $scope.oldTreatmentPaidPrice) < 0) {
                $scope.oldFundContainEnoughMoney = false;
                console.log("changeFields 3 - 2 :");
            } else {
                    $scope.oldFundContainEnoughMoney = true;
                console.log("changeFields 4");
            }
            if(( $scope.treatment.price - $scope.treatment.paid_price) < 0){
                console.log("1 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = true;
            }else{
                console.log("2 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = false;
            }

            console.log("$scope.oldFund.amount "+($scope.oldFund.amount ));
            console.log(" $scope.oldTreatmentPaidPrice "+$scope.oldTreatmentPaidPrice );
            console.log(" $scope.treatment.paid_price "+$scope.treatment.paid_price );
            console.log("$scope.oldFund.amount - ( $scope.oldTreatmentPaidPrice - $scope.treatment.paid_price) 4 "+($scope.oldFund.amount - ( $scope.oldTreatmentPaidPrice - $scope.treatment.paid_price)));

        };
        // Display treatments end



        // Add - Edit - View treatments
        $scope.editTreatment = function (treatment) {
            $scope.treatment = treatment;
            $scope.oldTreatmentPrice = $scope.treatment.price;
            $scope.oldTreatmentPaidPrice = $scope.treatment.paid_price;
            $scope.oldFund = $scope.treatment.fund;
            $scope.dialogPopupTreatment();
            $scope.loadAllAttachments(1);
            $scope.loadFunds();
        };
        $scope.dialogPopupTreatment = function (treatment) {
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.initEventReasons();
            $scope.initDoctors();
        }

        $scope.initEventReasons = function () {
            if ($scope.event_reasons === null) {
                Event_reason.query(function (result, headers) {
                        $scope.event_reasons = result;
                        if($scope.event_reasons!==null && $scope.event_reasons.length > 0){
                            $scope.defaultEventReason = $scope.event_reasons[0]
                            console.log(" $scope.defaultEventReason a "+$scope.defaultEventReason);
                        }
                        if($scope.treatment!==null){
                            $scope.treatment.eventReason = $scope.defaultEventReason;
                        }
                    }
                );
            }
        }
        $scope.initDoctors = function () {
            if ($scope.doctors === null) {
                Doctor.query(function (result, headers) {
                        $scope.doctors = result;
                        console.log(" $scope.defaultDoctor 3 "+$scope.defaultDoctor);
                        if($scope.doctors!==null && $scope.doctors.length > 0){
                            $scope.defaultDoctor = $scope.doctors[0]
                            console.log(" $scope.defaultDoctor 4 "+$scope.defaultDoctor);
                        }
                        if($scope.treatment!==null){
                            $scope.treatment.doctor = $scope.defaultDoctor;
                        }
                    }
                );
            }
        }

        var onSaveTreatmentFinished = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            $scope.treatment=result;
            $scope.loadPage($scope.page);
            $scope.oldFund = $scope.treatment.fund;
            $scope.treatmentTotal.price = $scope.treatmentTotal.price + ($scope.treatment.price - $scope.oldTreatmentPrice);
            $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + ($scope.treatment.paid_price - $scope.oldTreatmentPaidPrice);
            $scope.oldTreatmentPrice = $scope.treatment.price;
            $scope.oldTreatmentPaidPrice = $scope.treatment.paid_price;
            $scope.loadFunds();
            $scope.loadAllAttachments(1);
        };

        $scope.saveTreatmentAndClose = function () {
            $scope.saveTreatment();
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
        };

        $scope.reasonValueChanged = function () {
            $scope.treatment.price = $scope.treatment.eventReason.price;
            $scope.treatment.paid_price = $scope.treatment.eventReason.price;
        };

        $scope.saveTreatment = function () {
            if ($scope.treatment.id != null) {
                $scope.treatment = Treatment.update($scope.treatment, onSaveTreatmentFinished);
            } else {
                $scope.treatment = Treatment.save($scope.treatment, onSaveTreatmentFinished);
            }
        };
        $scope.closeAddEditViewPopup = function () {
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.displayTreatmentView = false;
            $scope.cancelImageCapture();
            $scope.clear();
        };
        $scope.viewTreatmentDetail = function (treatment) {
            $scope.treatment = treatment;
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.displayTreatments = false;
            $scope.displayTreatmentView = true;
            $scope.loadAllAttachments(1);
        };


    })
;
