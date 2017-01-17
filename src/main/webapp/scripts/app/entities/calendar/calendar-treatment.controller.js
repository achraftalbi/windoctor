'use strict';
angular.module('windoctorApp').expandCalendarEventsControllerToTreatments =
    function ($scope, $rootScope,$stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
              Event_reason, Event, Patient,Attachment,Fund,Plan) {


        $scope.initVariables = function(){
            $scope.treatments = [];
            $scope.treatmentsAll = [];
            $scope.treatmentsPlanAll = [];
            $scope.treatmentsCurrent = [];
            $scope.plansArchived = [];
            $scope.treatmentPage = 1;
            $scope.treatmentPer_Page = 5;
            $scope.attachmentPage = 1;
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null,elements:null};
            $scope.treatmentTotal = null;
            $scope.oldTreatmentPrice = null;
            $scope.oldTreatmentPaidPrice = null;
            $scope.treatmentToDelete = null;

            $scope.displayAddEditViewPopup = false;
            $scope.displayAddEditViewAttachmentPopup = false;
            $scope.displayTreatmentView = false;
            $scope.displayAllPatientTreatments = true;
            $scope.displayTreatmentToDelete = false;
            $scope.addNewEventReason = false;
            $scope.newEventReason = {};
            $scope.newEventReasonDescNecessary = false;
            $scope.selectedDate = null;
            $scope.eventReasonSelected = false;
            $scope.oldFundContainEnoughMoney = true;
            $scope.oldFund=null;
            $scope.searchCalledTreatment = false;
            $scope.saveTreatmentNotClose = false;
            /*Attachments variables */
            $scope.dispalyAttachments = false;
            $scope.attachments = null;
            $scope.attachment = null;
            $scope.viewSelectedAttachment = false;
            $scope.priceLessThanPaidPrice = false;
            $scope.captureAnImageScreen = false;
            $scope.saveTreatmentOnGoing = false;
            $scope.archiveCurrentPlanFlag = false;
            $scope.elementsSelected =[];
            $scope.selectedElement = null;
            $scope.TO_RADIANS = Math.PI/180;


            // Patient attachments
            $scope.displayAllPatientAttachments=false;
            $scope.displayActsTab=true;
            $scope.displayPlanTab=false;
            $scope.displayArchivesTab=false;
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6 col-md-3';

            $scope.paidPriceTreatment = {
                treatmentIdToExecute : null,
                paidPriceExecuted : 0,
                paidPriceErrorNumber : 0
            };
        };

        $scope.clickOnActs = function () {
            $scope.displayActsTab=true;
            $scope.displayPlanTab=false;
            $scope.displayArchivesTab=false;
            $scope.treatments= $scope.treatmentsAll;
            $scope.treatmentTotal = $scope.treatmentsAll[0];
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6 col-md-3';
            $scope.clear();
            $scope.myCanvas();
            $scope.createSelectedElements();
        };

        $scope.clickOnPlan = function () {
            $scope.displayPlanTab=true;
            $scope.displayActsTab=false;
            $scope.displayArchivesTab=false;
            $scope.treatments= $scope.treatmentsPlanAll;
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.treatmentTotal = $scope.treatmentsPlanAll[0];
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6';
            $scope.clear();
            $scope.myCanvas();
            $scope.createSelectedElements();
        };

        $scope.clickOnArchives = function () {
            $scope.displayArchivesTab=true;
            $scope.displayActsTab=true;
            $scope.displayPlanTab=false;
            $scope.treatments= $scope.treatmentsAll;
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.treatmentTotal = $scope.treatmentsAll[0];
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6 col-md-3';
            if($scope.plansArchived===null || $scope.plansArchived===undefined
                ||$scope.plansArchived.length===0){
                Plan.query( {patientId:$scope.event.user.id}, function(result) {
                    $scope.plansArchived = result;
                });
            }
            $scope.clear();
            $scope.myCanvas();
            $scope.createSelectedElements();
            $scope.displayActsTab=false;
        };

        // Display treatments Begin
        $scope.loadAllTreatmentsFromServer = function () {
            console.log('length event used '+$scope.event.id);
            Treatment.getSimple({
                    patientId: $scope.event.user.id
                }, function (result, headers) {
                    $scope.selectedDate = new Date($stateParams.selectedDate);
                    $scope.linksTreatment = ParseLinks.parse(headers('link'));
                    $scope.treatmentsAll = result.treatmentsList;
                    $scope.treatmentsPlanAll = result.treatmentsPlanList;
                    $scope.treatmentTotal = $scope.treatmentsAll[$scope.treatmentsAll.length-1];
                    var orderBy = $filter('orderBy');
                    $scope.treatmentsAll = orderBy($scope.treatmentsAll, ['-treatment_date','-id']);
                    var totalTreatmentPlanElement =$scope.treatmentsPlanAll[$scope.treatmentsPlanAll.length-1];
                        $scope.treatmentsPlanAll.splice($scope.treatmentsPlanAll.length-1, 1);
                    $scope.treatmentsPlanAll = orderBy($scope.treatmentsPlanAll, ['-id']);
                    $scope.treatmentsPlanAll.splice(0, 0,totalTreatmentPlanElement);
                    if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                        && $scope.treatmentsAll.length>0){
                        var length = $scope.treatmentsAll.length;
                        console.log('length treatments '+length);
                        $scope.treatmentsCurrent=[];
                        $scope.treatments=[];

                        $scope.elementsSelected =[];
                        for(var i = 0;i<length;i++){
                            if($scope.event!==null && $scope.event.id!==null && $scope.event.id!==undefined) {
                                if ($scope.treatmentsAll[i].id !== -1 && $scope.treatmentsAll[i].event.id === $scope.event.id) {
                                    $scope.treatmentsCurrent.push($scope.treatmentsAll[i]);
                                }
                            }
                            if($scope.treatmentsAll[i].id!==-1){
                                $scope.addElements($scope.treatmentsAll[i]);
                            }
                        }
                        $scope.displayAllPatientTreatments = true;
                        $scope.treatments= $scope.treatmentsAll;
                        $scope.drawSelectedElements();
                    }
                    if($scope.treatmentsPlanAll!==null && $scope.treatmentsPlanAll!==undefined
                        && $scope.treatmentsPlanAll.length>0){
                        var length = $scope.treatmentsPlanAll.length;
                        if($scope.treatmentsAll===null || $scope.treatmentsAll===undefined
                            || $scope.treatmentsAll.length===0){
                            $scope.elementsSelected =[];
                        }
                        $scope.displayPlanTab=true;
                        $scope.displayActsTab=false;
                        for(var i = 0;i<length;i++){

                            if($scope.treatmentsPlanAll[i].id!==-1){
                                if($scope.treatmentsPlanAll[i].status===null || $scope.treatmentsPlanAll[i].status===undefined
                                    ||$scope.treatmentsPlanAll[i].status.id!=3){
                                    $scope.addElements($scope.treatmentsPlanAll[i]);
                                }
                            }
                        }
                        $scope.displayPlanTab=false;
                        $scope.displayActsTab=true;
                    }
                }
            );
        };

        $scope.orderTreatments = function () {
            var orderBy = $filter('orderBy');
            if($scope.displayPlanTab){
                var totalTreatmentPlanElement =$scope.treatmentsPlanAll[0];
                $scope.treatmentsPlanAll.splice(0, 1);
                $scope.treatmentsPlanAll = orderBy($scope.treatmentsPlanAll, ['-id']);
                $scope.treatmentsPlanAll.splice(0, 0,totalTreatmentPlanElement);
                $scope.treatments= $scope.treatmentsPlanAll;
            }else{
                $scope.treatmentsAll = orderBy($scope.treatmentsAll, ['-treatment_date','-id']);
                if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                    && $scope.treatmentsAll.length>0){
                    //var firstElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                    //var lastElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                    $scope.treatmentsCurrent=[];
                    $scope.treatments=[];
                    for(var i = 0;i<$scope.treatmentsAll.length;i++){
                        if($scope.treatmentsAll[i].id!==-1 && $scope.treatmentsAll[i].event.id===$scope.event.id){
                            $scope.treatmentsCurrent.push($scope.treatmentsAll[i]);
                        }
                    }
                    $scope.treatments= $scope.treatmentsAll;
                }
            }
        };

        $scope.loadAllTreatments = function () {
            ///
            if($scope.treatments === null || $scope.treatments === undefined || $scope.treatments.length===0){
                $scope.loadAllTreatmentsFromServer();
            }
            ///
            if($scope.event.id===null){
                $scope.displayAllPatientTreatments =true;
            }
            if($scope.displayActsTab === true){
                if ($scope.displayAllPatientTreatments === false) {
                    $scope.treatments = $scope.treatmentsCurrent;
                } else {
                    $scope.treatments = $scope.treatmentsAll;
                }
                $scope.displayAllPatientAttachments=false;
            }
        };

        $scope.loadFunds = function () {
            if($scope.funds===null || $scope.funds===undefined
                || $scope.funds.length===0){
                Fund.query(function (result, headers) {
                        $scope.funds = result;
                        console.log("$scope.treatment.fund 0 ")
                        if($scope.funds!==null && $scope.funds.length > 0){
                            $scope.defaultFund = $scope.funds[0];
                        }
                    }
                );
            }
        };

        $scope.addNewEventReasonChange = function () {
            if(!$scope.addNewEventReason ){
                angular.copy($scope.treatment.event_reason, $scope.newEventReason);
                $scope.newEventReason.id = null;
                $scope.newEventReason.description = null;
                $scope.newEventReason.price = 0;
            }

            $scope.addNewEventReason = !$scope.addNewEventReason;
        };

        var onSaveFinishedNewEventReason = function (result) {
            $scope.addNewEventReason = !$scope.addNewEventReason;
            $scope.newEventReason.description = null;
            $scope.newEventReason.price = null;
            $scope.newEventReasonDescNecessary = false;
            if($scope.event_reasons===null
                || $scope.event_reasons===undefined){
                $scope.event_reasons = [result];
            }else{
                $scope.event_reasons.push(result);
                var orderBy = $filter('orderBy');
                $scope.event_reasons = orderBy($scope.event_reasons, 'description', false);
            }
        };

        $scope.saveNewEventReason = function () {
            console.log('$scope.newEventReasonDescription '+$scope.newEventReason.description);
            console.log('$scope.newEventReasonPrice '+$scope.newEventReason.price);
            if($scope.newEventReason.description === null
                || $scope.newEventReason.description === undefined
                || $scope.newEventReason.description.length===0 ){
                $scope.newEventReasonDescNecessary = true;
            } else {
                $scope.newEventReasonDescNecessary = false;
                if($scope.newEventReason.price === null){
                    $scope.newEventReason.price = 0;
                }
                Event_reason.save($scope.newEventReason, onSaveFinishedNewEventReason);
            }
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
                    $scope.loadPageTreatment($scope.treatmentPage);
                    $scope.displayTreatmentToDelete = false;
                    $scope.displayTreatments = true;
                });
        };

        ///////////////////////
        $scope.loadPageTreatment = function (page) {
            $scope.treatmentPage = page;
            if($scope.searchCalledTreatment){
                $scope.searchTreatments();
            }else{
                $scope.loadAllTreatments();
                $scope.createSelectedElements();
                if($scope.saveTreatmentNotClose){
                    $scope.drawElementsWhenEdit($scope.treatment);
                    $scope.saveTreatmentNotClose = false;
                }
            }
        };
        $scope.searchTreatments = function () {
            $scope.treatmentPage = 1;
            $scope.searchCalledTreatment = true;
            $scope.searchTreatments();
        };

        $scope.searchTreatments = function () {
            if($scope.searchQuery!==null && $scope.searchQuery!==undefined && $scope.searchQuery.length>0) {
                TreatmentSearch.query({
                    query: $scope.searchQuery, patientId: $scope.event.user.id
                }, function (result, headers) {
                    $scope.linksTreatment = ParseLinks.parse(headers('link'));
                    $scope.treatments = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAllTreatments();
                    }
                });
            }else{
                $scope.loadAllTreatments();
            }
        };


        //////////////////////

        $scope.refresh = function () {
            $scope.loadAllTreatments();
            $scope.clear();
        };

        $scope.refreshAll = function () {
            $scope.displayPlanTab=false;
            $scope.displayActsTab=true;
            $scope.loadAllTreatmentsFromServer();
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
        };
        $scope.clear = function () {
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null,elements:null};
            $scope.paidPriceTreatment = {
                treatmentIdToExecute : null,
                paidPriceExecuted : 0,
                paidPriceErrorNumber : 0
            };
        };
        $scope.clearDeleteTreatment = function () {
            $scope.treatmentToDelete = {
                treatment_date: null,
                description: null,
                price: null,
                paid_price: null,
                id: null,
                elements: null
            };
            $scope.displayTreatmentToDelete = false;
            $scope.displayTreatments = true;
        };
        $scope.cancelTreatmentRows = function () {
            //$modalInstance.dismiss('cancel');
        };
        // Treatment to display all or only treatment events
        $scope.displayAllPatientTreatmentsFunction = function () {
            $scope.clear();
            $scope.displayAllPatientTreatments = true;
            $scope.displayTreatments = true;
            $scope.displayAddEditViewPopup=false;
            $scope.displayAllPatientAttachments=false;
            $scope.treatmentPage = 1;
            $scope.loadPageTreatment($scope.treatmentPage);
        }
        $scope.displayPatientTreatmentsEventFunction = function () {
            $scope.displayAllPatientTreatments = false;
            $scope.displayTreatments = true;
            $scope.displayAddEditViewPopup=false;
            $scope.displayAllPatientAttachments=false;
            $scope.treatmentPage = 1;
            $scope.loadPageTreatment($scope.treatmentPage);
        }

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
            /*if(( $scope.treatment.price - $scope.treatment.paid_price) < 0){
                console.log("1 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = true;
            }else{
                console.log("2 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = false;
            }*/

            //console.log("$scope.oldFund.amount "+($scope.oldFund.amount ));
            //console.log(" $scope.oldTreatmentPaidPrice "+$scope.oldTreatmentPaidPrice );
            //console.log(" $scope.treatment.paid_price "+$scope.treatment.paid_price );
            //console.log("$scope.oldFund.amount - ( $scope.oldTreatmentPaidPrice - $scope.treatment.paid_price) 4 "+($scope.oldFund.amount - ( $scope.oldTreatmentPaidPrice - $scope.treatment.paid_price)));

        };
        // Display treatments end



        // Add - Edit - View treatments

        $scope.addNewTreatment = function () {
            console.log('$scope.defaultEventReason '+$scope.defaultEventReason);
            $scope.treatment = {
                treatment_date: $scope.event.event_date,
                description: null,
                price: $scope.defaultEventReason.price,
                paid_price: $scope.defaultEventReason.price,
                id: null,
                eventReason : $scope.defaultEventReason,
                fund : $scope.defaultFund,
                doctor:$scope.defaultDoctor,
                event: {id: $scope.event.id}
            };
            if($scope.displayPlanTab){
                $scope.treatment.status={id:1};
            }
            $scope.temporaryElements=null;
            $scope.oldTreatmentPrice = null;
            $scope.oldTreatmentPaidPrice = null;
            $scope.attachments=null;
            $scope.dialogPopupTreatment();
            $scope.loadFunds();
            $scope.createSelectedElements();
            $scope.saveTreatmentOnGoing = false;
            $scope.displayAllPatientAttachments=false;
            if($scope.treatment.fund!==null && $scope.treatment.fund!==undefined
                && $scope.treatment.fund.id!==null && $scope.treatment.fund.id!==undefined){
                for(var i=0;i<$scope.funds.length;i++){
                    if($scope.funds[i].id===$scope.treatment.fund.id){
                        $scope.treatment.fund = $scope.funds[i];
                        break;
                    }
                }
            }
        };
        $scope.editTreatment = function (treatment) {
            $scope.treatment = treatment;
            $scope.oldTreatmentPrice = $scope.treatment.price;
            $scope.oldTreatmentPaidPrice = $scope.treatment.paid_price;
            $scope.oldFund = $scope.treatment.fund;
            $scope.dialogPopupTreatment();
            $scope.drawElementsWhenEdit($scope.treatment);
            $scope.loadAllAttachments(1);
            $scope.loadFunds();
            $scope.saveTreatmentOnGoing = false;
            $scope.temporaryElements=null;
            if($scope.treatment.elements!==null && $scope.treatment.elements!==undefined
                && $scope.treatment.elements.length>0){
                var split = $scope.treatment.elements.split(',');
                var tabLength = split.length;
                for(var i = 0;i<split.length;i++){
                    $scope.temporaryElements = (tabLength===1 || $scope.temporaryElements===null?'':$scope.temporaryElements +', ')+split[i];
                }
            }
            $scope.displayAllPatientAttachments=false;
            if($scope.treatment.fund!==null && $scope.treatment.fund!==undefined
                && $scope.treatment.fund.id!==null && $scope.treatment.fund.id!==undefined){
                for(var i=0;i<$scope.funds.length;i++){
                    if($scope.funds[i].id===$scope.treatment.fund.id){
                        $scope.treatment.fund = $scope.funds[i];
                        break;
                    }
                }
            }
        };
        $scope.dialogPopupTreatment = function (treatment) {
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.initEventReasons();
            $scope.initDoctors();
        }

        $scope.initEventReasons = function () {
            if($scope.event_reasons===null || $scope.event_reasons===undefined
                || $scope.event_reasons.length===0){
                Event_reason.query(function (result, headers) {
                        $scope.event_reasons = result;
                        if($scope.event_reasons!==null && $scope.event_reasons.length > 0){
                            $scope.defaultEventReason = $scope.event_reasons[0];
                            console.log(" $scope.defaultEventReason a "+$scope.defaultEventReason);
                        }
                    }
                );
            }
        }

        $scope.initDoctors = function () {
            if($scope.doctors===null || $scope.doctors===undefined
                || $scope.doctors.length===0){
                Doctor.query(function (result, headers) {
                        $scope.doctors = result;
                        console.log(" $scope.defaultDoctor 3 "+$scope.defaultDoctor);
                        if($scope.doctors!==null && $scope.doctors.length > 0){
                            $scope.defaultDoctor = $scope.doctors[0]
                            console.log(" $scope.defaultDoctor 4 "+$scope.defaultDoctor);
                        }
                    }
                );
            }
        }


        var onSaveTreatmentFinishedUpdate = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            $scope.treatment=result;
            $scope.loadPageTreatment($scope.treatmentPage);
            $scope.oldFund = $scope.treatment.fund;
            if($scope.oldTreatmentPrice===null){
                $scope.treatmentTotal.price = $scope.treatmentTotal.price + $scope.treatment.price;
            }else{
                $scope.treatmentTotal.price = $scope.treatmentTotal.price + ($scope.treatment.price - $scope.oldTreatmentPrice);
            }
            if($scope.oldTreatmentPaidPrice===null){
                $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + $scope.treatment.paid_price;
            }else{
                $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + ($scope.treatment.paid_price - $scope.oldTreatmentPaidPrice);
            }
            $scope.oldTreatmentPrice = $scope.treatment.price;
            $scope.oldTreatmentPaidPrice = $scope.treatment.paid_price;

            var oldFundId = $scope.treatment.fund.id;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    console.log("$scope.treatment.fund 0 ")
                    if($scope.funds!==null && $scope.funds.length > 0){
                        $scope.defaultFund = $scope.funds[0];
                        for(var i=0;i<$scope.funds.length;i++){
                            if($scope.funds[i].id===oldFundId){
                                $scope.treatment.fund = $scope.funds[i];
                                break;
                            }
                        }
                    }
                    $scope.saveTreatmentOnGoing = false;
                }
            );
            $scope.loadAllAttachments(1);
        };

        var onSaveTreatmentFinished = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            $scope.treatment=result;
            console.log('save $scope.treatment.id'+$scope.treatment.id+' $scope.treatment.event.id '+$scope.treatment.event.id);
            if($scope.displayPlanTab) {
                $scope.treatmentsPlanAll.push($scope.treatment);
            }else{
                $scope.treatmentsAll.push($scope.treatment);
            }
            $scope.loadPageTreatment($scope.treatmentPage);
            $scope.oldFund = $scope.treatment.fund;
            if($scope.oldTreatmentPrice===null){
                $scope.treatmentTotal.price = $scope.treatmentTotal.price + $scope.treatment.price;
            }else{
                $scope.treatmentTotal.price = $scope.treatmentTotal.price + ($scope.treatment.price - $scope.oldTreatmentPrice);
            }
            if($scope.oldTreatmentPaidPrice===null){
                $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + $scope.treatment.paid_price;
            }else{
                $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + ($scope.treatment.paid_price - $scope.oldTreatmentPaidPrice);
            }
            $scope.oldTreatmentPrice = $scope.treatment.price;
            $scope.oldTreatmentPaidPrice = $scope.treatment.paid_price;

            // New feature
            $scope.orderTreatments();
            $scope.displayAllPatientTreatments = true;
            var oldFundId = $scope.treatment.fund.id;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    console.log("$scope.treatment.fund 0 ")
                    if($scope.funds!==null && $scope.funds.length > 0){
                        $scope.defaultFund = $scope.funds[0];
                        for(var i=0;i<$scope.funds.length;i++){
                            if($scope.funds[i].id===oldFundId){
                                $scope.treatment.fund = $scope.funds[i];
                                break;
                            }
                        }
                    }
                    $scope.saveTreatmentOnGoing = false;
                }
            );

            $scope.loadAllAttachments(1);
            if($scope.event.eventStatus.id === 1){
                $scope.event.eventStatus.id = 3;
            }
        };

        $scope.saveTreatmentAndClose = function () {
            $scope.saveTreatment();
            $scope.drawElementsAfterSave($scope.treatment);
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
        };

        $scope.reasonValueChanged = function () {
            $scope.treatment.price = $scope.treatment.eventReason.price;
            $scope.treatment.paid_price = $scope.treatment.eventReason.price;
        };

        $scope.saveTreatmentCall = function () {
            $scope.saveTreatmentNotClose = true;
            $scope.saveTreatment();
        };

        $scope.saveTreatment = function () {
            $scope.constructElementBeforeSave($scope.treatment);
            if($scope.treatment.fund!==null && $scope.treatment.fund!==undefined
                && $scope.treatment.fund.id!==null && $scope.treatment.fund.id!==undefined){
                for(var i=0;i<$scope.funds.length;i++){
                    if($scope.funds[i].id===$scope.treatment.fund.id){
                        $scope.treatment.fund = $scope.funds[i];
                        break;
                    }
                }
            }
            $scope.saveTreatmentOnGoing = true;
            if ($scope.treatment.id != null) {
                $scope.treatment = Treatment.update($scope.treatment, onSaveTreatmentFinishedUpdate);
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
            $scope.myCanvas();
            $scope.createSelectedElements();
        };
        $scope.viewTreatmentDetail = function (treatment) {
            $scope.treatment = treatment;
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.displayTreatments = false;
            $scope.displayTreatmentView = true;
            $scope.loadAllAttachments(1);
        };

        $scope.deleteTreatmentInPlan = function (treatment) {
            Treatment.delete({id: treatment.id},
                function () {
                    var treatmentIndex = -1;
                    for (var k = 0; k < $scope.treatmentsPlanAll.length; k++) {
                        if (treatment.id === $scope.treatmentsPlanAll[k].id) {
                            treatmentIndex = k;
                            break;
                        }
                    }
                    if(treatmentIndex>-1){
                        $scope.treatmentTotal.price = $scope.treatmentTotal.price - treatment.price;
                        $scope.treatmentsPlanAll.splice(treatmentIndex, 1);
                    }
                    $scope.clearTreatmentElements(treatment);
                    $scope.createSelectedElements();
                });
        };

        $scope.changePaidPrice = function () {

            if($scope.paidPriceTreatment.paidPriceExecuted === null
                || $scope.paidPriceTreatment.paidPriceExecuted===undefined) {
                $scope.paidPriceTreatment.paidPriceErrorNumber = 1;
            }else if($scope.paidPriceTreatment.paidPriceExecuted <0){
                $scope.paidPriceTreatment.paidPriceErrorNumber = 2;
            }else if($scope.paidPriceTreatment.paidPriceExecuted >10000000){
                $scope.paidPriceTreatment.paidPriceErrorNumber = 3;
            }else{
                $scope.paidPriceTreatment.paidPriceErrorNumber = 0;
            }
        };
        $scope.executeTreatmentInPlan = function (treatment) {
            $scope.paidPriceTreatment.treatmentIdToExecute = treatment.id;
            $scope.paidPriceTreatment.paidPriceExecuted = 0;
            $scope.paidPriceTreatment.paidPriceErrorNumber = 0;
        };
        $scope.closeExecuteTreatmentInPlan = function (treatment) {
            $scope.paidPriceTreatment = {
                treatmentIdToExecute : null,
                paidPriceExecuted : 0,
                paidPriceErrorNumber : 0
            };
        };
        $scope.confirmExecuteTreatmentInPlan = function (treatment) {
            treatment.status.id=3;
            treatment.treatment_date=$scope.event.event_date;
            treatment.paid_price=$scope.paidPriceTreatment.paidPriceExecuted;
            Treatment.update(treatment, onSaveTreatmentFinishedUpdate);
            $scope.paidPriceTreatment = {
                treatmentIdToExecute : null,
                paidPriceExecuted : 0,
                paidPriceErrorNumber : 0
            };
            var treatmentTmp={};
            angular.copy(treatment, treatmentTmp);
            $scope.treatmentsAll.push(treatmentTmp);
            $scope.treatmentsAll[0].price=$scope.treatmentsAll[0].price+treatment.price;
            $scope.treatmentsAll[0].paid_price=$scope.treatmentsAll[0].paid_price+treatment.paid_price;
            $scope.displayPlanTab=false;
            $scope.displayActsTab=true;
            $scope.orderTreatments();
            $scope.displayPlanTab=true;
            $scope.displayActsTab=false;
            $scope.clickOnPlan();

        };

        $scope.archiveCurrentPlan = function () {
            $scope.archiveCurrentPlanFlag = false;
            if($scope.treatmentsPlanAll!==null && $scope.treatmentsPlanAll!==undefined
                && $scope.treatmentsPlanAll.length>1){
                $scope.archiveCurrentPlanFlag = true;
            }else{
                $scope.archiveCurrentPlanFlag = false;
            }
            $('#archivePlanPopup').modal('show');
        };

        $scope.confirmArchiveCurrentPlan = function () {
            $scope.paidPriceTreatment = {
                treatmentIdToExecute : null,
                paidPriceExecuted : 0,
                paidPriceErrorNumber : 0
            };
            Plan.get({id: $scope.treatmentsPlanAll[1].plan.id}, function(result) {
                $scope.treatmentsPlanAll=[];
                $scope.treatmentTotal.price=0;
                $scope.treatmentTotal.paid_price=0;
                $scope.treatmentsPlanAll.push($scope.treatmentTotal);
                $scope.treatments=$scope.treatmentsPlanAll;
                $scope.plansArchived.push(result);
                var orderBy = $filter('orderBy');
                $scope.plansArchived = orderBy($scope.plansArchived, ['-number']);
                $('#archivePlanPopup').modal('hide');
            });
        };

        $scope.closeArchiveCurrentPlan = function () {
            $('#archivePlanPopup').modal('hide');
        };

        /********************************************************************************/
        /********************************************************************************/
        /***********************                                       ******************/
        /***********************          Attachments functions        ******************/
        /***********************                                       ******************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.loadAllAttachments = function (attachmentPage){
                $scope.attachmentPage = attachmentPage;
                Attachment.query({treatmentId:$scope.treatment.id,page: $scope.attachmentPage, per_page: 5}, function (result, headers) {
                    $scope.linkAttachments = ParseLinks.parse(headers('link'));
                    $scope.attachments = result;
                });
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

        $scope.setImage = function ($files, attachment) {
            if ($files[0]) {
                var file = $files[0];
                var fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = function (e) {
                    var data = e.target.result;
                    var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        attachment.image = base64Data;
                    });
                };
            }
        };

        $scope.addNewAttachment = function () {
            $scope.attachment = {description: null, image: null, id: null,treatment: {id: $scope.treatment.id}};
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = false;
        };
        $scope.editAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
        };
        $scope.dialogPopupAttachment = function () {
            $scope.displayAddEditViewAttachmentPopup = true;
        }

        var onSaveAttachmentFinished = function (result) {
            $scope.$emit('windoctorApp:attachmentUpdate', result);
            $scope.loadAllAttachments($scope.attachmentPage);
            $scope.displayAddEditViewAttachmentPopup = false;
        };

        $scope.saveAttachment = function () {
            if ($scope.attachment.id != null) {
                $scope.attachment = Attachment.update($scope.attachment, onSaveAttachmentFinished);
            } else {
                $scope.attachment = Attachment.save($scope.attachment, onSaveAttachmentFinished);
            }
        };

        $scope.deleteAttachment = function (id) {
            Attachment.delete({id: id},
                function () {
                    $scope.loadAllAttachments($scope.attachmentPage);
                });
        };
        $scope.editAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = false;
        };
        $scope.viewAttachment = function (attachment) {
            $scope.attachment = attachment;
            $scope.dialogPopupAttachment();
            $scope.viewSelectedAttachment = true;
        };
        $scope.clearAttachmentDialog = function () {
            $scope.attachment = {description: null, image: null, id: null,treatment: null};
            $scope.displayAddEditViewAttachmentPopup = false;
            $scope.viewSelectedAttachment = false;
        };

        $scope.declareElementsFunctions = function () {
            angular.module('windoctorApp').expandTreatmentsControllerElements
            ($scope, $rootScope, $stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
                Event_reason, Event, Patient,Attachment,Fund);
        };

        // *********Treat all patient attachments *********//
        $scope.displayAllAttachments = function () {
            $scope.displayAllPatientTreatments = false;
            $scope.displayTreatments = false;
            $scope.displayAddEditViewPopup=false;
            $scope.displayAllPatientAttachments=true;
        }


        $scope.initTreatmentPage = function () {
            $scope.initVariables();
            $scope.declareElementsFunctions();
            $scope.initCanvas();
            $scope.loadAllTreatmentsFromServer();
            $scope.initEventReasons();
            $scope.loadFunds();
            $scope.initDoctors();
            console.log('displayTreatmentsPage values '+$scope.displayTreatmentsPage);
            console.log('displayTreatments values '+$scope.displayTreatments);
        };

        $scope.initTreatmentPage();
    };
