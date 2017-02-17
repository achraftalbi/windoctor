'use strict';
angular.module('windoctorApp').expandCalendarEventsControllerToTreatments =
    function ($scope, $rootScope,$stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
              Event_reason, Event, Patient,Attachment,Fund,Plan,$http) {


        $scope.initVariables = function(){
            $scope.treatments = [];
            $scope.treatmentsAll = [];
            $scope.treatmentsPlanAll = [];
            $scope.treatmentsCurrent = [];
            $scope.plansArchived = [];
            $scope.treatmentPage = 1;
            $scope.treatmentPer_Page = 5;
            $scope.attachmentPage = 1;
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null,elements:null,sorting_key:null, createTreatmentForEachElement:false};
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
            $scope.deleteTreatmentOnGoing = false;
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
            $scope.checkFields = {
                sendMail : false,
                savePdf : false,
                printPdf : false,
                displayCopySystem : false
            };
            $scope.sortableOptions = {
                disabled: true,
                'ui-floating': false,
                update: function(e, ui) {
                    $scope.updateSortingKey(e,ui);
                }
            };
            $scope.currentPlanNumber = null;
        };
        var onSaveTreatmentFinishedUpdateSorting = function (result) {
            console.log('saving sorting for the selected treatment');
        };
        $scope.updateSortingKey = function (e, ui) {
            if ($scope.treatments[ui.item.sortable.index].id === -1
                || ui.item.sortable.index===0 || ui.item.sortable.dropindex===0) {
                ui.item.sortable.cancel();
            }else{
                console.log(' index values '+ui.item.sortable.index);
                console.log(' dropindex values '+ui.item.sortable.dropindex);
                var idMoved = $scope.treatments[ui.item.sortable.index].id;
                $scope.treatments[ui.item.sortable.index].sorting_key=ui.item.sortable.dropindex;
                Treatment.update($scope.treatments[ui.item.sortable.index], onSaveTreatmentFinishedUpdateSorting);
                console.log('idMoved $scope.treatments['+ui.item.sortable.index+'].sorting_key before '+$scope.treatments[ui.item.sortable.index].sorting_key);
                var orderBy = $filter('orderBy');
                $scope.treatments = orderBy($scope.treatments, ['sorting_key']);
                var i = 0;
                console.log(' idMoved '+idMoved);
                for (var k = 0; k < $scope.treatments.length; k++) {
                    console.log(' k '+k);
                    console.log(' i '+i);
                    console.log(' $scope.treatments[k].price '+$scope.treatments[k].price);
                    console.log(' $scope.treatments[k].id '+$scope.treatments[k].id);
                    console.log(' $scope.treatments[k].sorting_key before '+$scope.treatments[k].sorting_key);
                    if (idMoved !== $scope.treatments[k].id && -1 !== $scope.treatments[k].id) {
                        i++;
                        if (i === ui.item.sortable.dropindex) {
                            i++;
                        }
                        $scope.treatments[k].sorting_key = i;
                    }
                    if(-1 === $scope.treatments[k].id){
                        $scope.treatments[k].sorting_key = -1;
                    }
                    console.log(' $scope.treatmentsPlanAll[k].sorting_key after '+$scope.treatments[k].sorting_key);
                }
                $scope.treatments = orderBy($scope.treatments, ['sorting_key']);
                $scope.treatmentsPlanAll = $scope.treatments;
            }
            console.log('ui.item.sortable.index '+ui.item.sortable.index);
            console.log('ui.item.sortable.dropindex '+ui.item.sortable.dropindex);
        };
        $scope.clickOnActs = function () {
            if($scope.displayPlanTab){
                $scope.treatmentsPlanAll= $scope.treatments;
            }
            $scope.displayActsTab=true;
            $scope.displayPlanTab=false;
            $scope.displayArchivesTab=false;
            $scope.treatments= $scope.treatmentsAll;
            $scope.treatmentTotal = $scope.treatmentsAll[0];
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.sortableOptions.disabled=true;
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
            $scope.sortableOptions.disabled=false;
            $scope.treatmentTotal = $scope.treatmentsPlanAll[0];
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6';
            if($scope.treatmentsPlanAll.length>1 && $scope.treatmentsPlanAll[1].plan.pdf_document!==null
                && $scope.treatmentsPlanAll[1].plan.pdf_document!==undefined){
                $scope.checkFields.displayCopySystem = true;
            }
            $scope.clear();
            $scope.myCanvas();
            $scope.createSelectedElements();
        };

        $scope.clickOnArchives = function () {
            if($scope.displayPlanTab){
                $scope.treatmentsPlanAll= $scope.treatments;
            }
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
                    if(result.treatmentsPlanList!==null && result.treatmentsPlanList!==undefined
                        && result.treatmentsPlanList.length>1){
                        $scope.currentPlanNumber = result.treatmentsPlanList[0].plan.number;
                    }
                    $scope.treatmentTotal = $scope.treatmentsAll[$scope.treatmentsAll.length-1];
                    var orderBy = $filter('orderBy');
                    $scope.treatmentsAll = orderBy($scope.treatmentsAll, ['-treatment_date','-id']);
                    var totalTreatmentPlanElement =$scope.treatmentsPlanAll[$scope.treatmentsPlanAll.length-1];
                    $scope.treatmentsPlanAll.splice($scope.treatmentsPlanAll.length-1, 1);
                    $scope.treatmentsPlanAll = orderBy($scope.treatmentsPlanAll, ['sorting_key']);
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
                $scope.treatmentsPlanAll = orderBy($scope.treatmentsPlanAll, ['sorting_key']);
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
            $scope.loadAllTreatmentsFromServer();
            $scope.clickOnActs();
        };

        $scope.clear = function () {
            $scope.treatment = {treatment_date: null, description: null, price: null, paid_price: null,doctor:null, id: null,elements:null,sorting_key:null, createTreatmentForEachElement:false};
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
                paid_price: $scope.displayPlanTab?0:$scope.defaultEventReason.price,
                id: null,
                eventReason : $scope.defaultEventReason,
                fund : $scope.defaultFund,
                doctor:$scope.defaultDoctor,
                event: {id: $scope.event.id},//,user:{id:$scope.event.user}},
                sorting_key:null,
                createTreatmentForEachElement:false
            };
            if($scope.displayPlanTab){
                $scope.treatment.status={id:1};
                $scope.treatment.createTreatmentForEachElement=$scope.displayPlanTab?
                    $scope.account.create_plan_act_for_each_element:$scope.account.create_normal_act_for_each_element;
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
            if($scope.event!==null && $scope.event.id!==null && $scope.event.eventStatus.id === 1 && $scope.displayActsTab){
                $scope.event.eventStatus.id = 3;
            }
        };

        var onSaveTreatmentFinished = function (result) {
            $scope.$emit('windoctorApp:treatmentUpdate', result);
            var newTreatmentsCreated = result.treatmentsList;
            if($scope.displayPlanTab) {
                $scope.treatmentsPlanAll = $scope.treatments;
                var treatmentsPlanAllLength = $scope.treatmentsPlanAll.length;
                for(var j=0;j<newTreatmentsCreated.length;j++){
                    newTreatmentsCreated[j].sorting_key = treatmentsPlanAllLength;
                    $scope.treatmentsPlanAll.push(newTreatmentsCreated[j]);
                    treatmentsPlanAllLength++;
                }
                $scope.treatments = $scope.treatmentsPlanAll;
                if($scope.currentPlanNumber===null || $scope.currentPlanNumber===undefined){
                    $scope.currentPlanNumber = $scope.treatment.plan.number;
                }
            }else{
                for(var j=0;j<newTreatmentsCreated.length;j++){
                    $scope.treatmentsAll.push(newTreatmentsCreated[j]);
                }
            }
            //console.log('save $scope.treatment.id'+$scope.treatment.id+' $scope.treatment.event.id '+$scope.treatment.event.id);
            for(var j=0;j<newTreatmentsCreated.length;j++){
                $scope.oldFund = newTreatmentsCreated[j].fund;
                $scope.treatmentTotal.price = $scope.treatmentTotal.price + newTreatmentsCreated[j].price;
                $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + newTreatmentsCreated[j].paid_price;
                /*if($scope.oldTreatmentPrice===null){
                }else{
                    $scope.treatmentTotal.price = $scope.treatmentTotal.price + (newTreatmentsCreated[j].price - $scope.oldTreatmentPrice);
                }
                if($scope.oldTreatmentPaidPrice===null){
                }else{
                    $scope.treatmentTotal.paid_price = $scope.treatmentTotal.paid_price + (newTreatmentsCreated[j].paid_price - $scope.oldTreatmentPaidPrice);
                }
                $scope.oldTreatmentPrice = newTreatmentsCreated[j].price;
                $scope.oldTreatmentPaidPrice = newTreatmentsCreated[j].paid_price;*/
            }

            // New feature
            var oldFundId = $scope.oldFund.id;
            Fund.query(function (result, headers) {
                    $scope.funds = result;
                    console.log("$scope.treatment.fund 0 ")
                    if($scope.funds!==null && $scope.funds.length > 0){
                        $scope.defaultFund = $scope.funds[0];
                        for(var i=0;i<$scope.funds.length;i++){
                            if($scope.funds[i].id===oldFundId){
                                for(var j=0;j<newTreatmentsCreated.length;j++){
                                    newTreatmentsCreated[j].fund = $scope.funds[i];
                                }
                                break;
                            }
                        }
                    }
                    $scope.saveTreatmentOnGoing = false;
                }
            );

            //$scope.loadAllAttachments(1);
            $scope.loadPageTreatment($scope.treatmentPage);
            $scope.orderTreatments();
            $scope.displayAllPatientTreatments = true;
            if($scope.event!==null && $scope.event.id!==null && $scope.event.eventStatus.id === 1 && $scope.displayActsTab){
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
            if ($scope.treatment.id !== null) {
                $scope.treatment.createTreatmentForEachElement = false;
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
            $scope.deleteTreatmentOnGoing = true;
            Treatment.delete({id: treatment.id},
                function () {
                    var treatmentIndex = -1;
                    for (var k = 0; k < $scope.treatments.length; k++) {
                        if (treatment.id === $scope.treatments[k].id) {
                            treatmentIndex = k;
                            break;
                        }
                    }
                    if(treatmentIndex>-1){
                        $scope.treatmentTotal.price = $scope.treatmentTotal.price - treatment.price;
                        $scope.treatments.splice(treatmentIndex, 1);
                        var i = 0;
                        for (var k = 0; k < $scope.treatments.length; k++) {
                            if ($scope.treatments[k].id !== -1) {
                                i++;
                                $scope.treatments[k].sorting_key = i;
                            }
                        }
                    }
                    $scope.clearTreatmentElements(treatment);
                    $scope.createSelectedElements();
                    $scope.deleteTreatmentOnGoing = false;
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
            $scope.oldTreatmentPrice = treatment.price;
            $scope.oldTreatmentPaidPrice = treatment.paid_price;
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
            if($scope.event!==null && $scope.event.eventStatus.id === 1){
                $scope.event.eventStatus.id = 3;
            }
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
                $scope.currentPlanNumber = null;
                $scope.treatmentsPlanAll=[];
                $scope.treatmentTotal.price=0;
                $scope.treatmentTotal.paid_price=0;
                $scope.treatmentsPlanAll.push($scope.treatmentTotal);
                $scope.treatments=$scope.treatmentsPlanAll;
                if($scope.treatmentsPlanAll.length>1 && $scope.treatmentsPlanAll[1].plan.pdf_document!==null
                    && $scope.treatmentsPlanAll[1].plan.pdf_document!==undefined){
                    $scope.checkFields.displayCopySystem = true;
                }else{
                    $scope.checkFields.displayCopySystem = false;
                }
                if($scope.plansArchived!==null && $scope.plansArchived!==undefined
                    &&$scope.plansArchived.length>0){
                    $scope.plansArchived.push(result);
                    var orderBy = $filter('orderBy');
                    $scope.plansArchived = orderBy($scope.plansArchived, ['-number']);
                }
                $scope.clickOnPlan();
                $('#archivePlanPopup').modal('hide');
            });
        };


        $scope.clearVariablesUsedInPdf = function () {
            $scope.checkFields = {
                sendMail : false,
                savePdf : false,
                printPdf : false,
                displayCopySystem : false
            };
            if($scope.treatmentsPlanAll.length>1 && $scope.treatmentsPlanAll[1].plan.pdf_document!==null
                && $scope.treatmentsPlanAll[1].plan.pdf_document!==undefined){
                $scope.checkFields.displayCopySystem = true;
            }
            console.log("clearVariablesUsedInPdf clicked");
        };

        $scope.launchPlanEstimation = function () {
            var url = '../windoctor/servlet/ServletPdf?planId='+$scope.treatmentsPlanAll[1].plan.id
            +($scope.checkFields.printPdf?'&livePdf=true':'')
            +'&savePdf='+$scope.checkFields.savePdf
            +'&sendMail='+$scope.checkFields.sendMail;
            if($scope.checkFields.printPdf){
                window.open(url, "_blank");
                if($scope.checkFields.savePdf===true){
                    $scope.treatmentsPlanAll[1].plan.pdf_document = {id:1};
                    $scope.checkFields.displayCopySystem = true;
                }
            }else{
                //document.location =url;
                $http.get(url).success(function (data, status, headers, config) {
                    if($scope.checkFields.savePdf===true){
                        $scope.treatmentsPlanAll[1].plan.pdf_document = {id:1};
                        $scope.checkFields.displayCopySystem = true;
                    }
                });
            }
        };

        $scope.changeCreateActForEachToothSelectedStatus = function () {
            $scope.treatment.createTreatmentForEachElement = !$scope.treatment.createTreatmentForEachElement;
            console.log('$scope.treatment.createTreatmentForEachElement ' + $scope.treatment.createTreatmentForEachElement);
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
