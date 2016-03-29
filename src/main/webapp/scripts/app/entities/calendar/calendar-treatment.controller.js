'use strict';
angular.module('windoctorApp').expandCalendarEventsControllerToTreatments =
    function ($scope, $stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
              Event_reason, Event, Patient,Attachment,Fund) {


        $scope.initVariables = function(){
            $scope.treatments = [];
            $scope.treatmentsAll = [];
            $scope.treatmentsCurrent = [];
            $scope.treatmentPage = 1;
            $scope.treatmentPer_Page = 5;
            $scope.attachmentPage = 1;
            $scope.treatment = null;
            $scope.treatmentTotal = null;
            $scope.oldTreatmentPrice = null;
            $scope.oldTreatmentPaidPrice = null;
            $scope.treatmentToDelete = null;

            $scope.displayAddEditViewPopup = false;
            $scope.displayAddEditViewAttachmentPopup = false;
            $scope.displayTreatmentView = false;
            $scope.displayAllPatientTreatments = false;
            $scope.displayTreatmentToDelete = false;
            $scope.addNewEventReason = false;
            $scope.newEventReason = {};
            $scope.newEventReasonDescNecessary = false;
            $scope.selectedDate = null;
            $scope.eventReasonSelected = false;
            $scope.oldFundContainEnoughMoney = true;
            $scope.oldFund=null;
            $scope.searchCalledTreatment = false;
            /*Attachments variables */
            $scope.dispalyAttachments = false;
            $scope.attachments = null;
            $scope.attachment = null;
            $scope.viewSelectedAttachment = false;
            $scope.priceLessThanPaidPrice = false;
            $scope.captureAnImageScreen = false;
        }

        // Display treatments Begin
        $scope.loadAllTreatmentsFromServer = function () {
            Treatment.query({
                    patientId: $scope.event.user.id
                }, function (result, headers) {
                    $scope.selectedDate = new Date($stateParams.selectedDate);
                    $scope.linksTreatment = ParseLinks.parse(headers('link'));
                    $scope.treatmentsAll = result;
                    var orderBy = $filter('orderBy');
                    $scope.treatmentsAll = orderBy($scope.treatmentsAll, ['-treatment_date','-id']);
                    if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                        && $scope.treatmentsAll.length>0){
                        //var firstElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                        //var lastElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                        var length = $scope.treatmentsAll.length-1;
                        console.log('length treatments '+length);
                        $scope.treatmentsCurrent=[];
                        $scope.treatments=[];
                        for(var i = 0;i<length;i++){
                                if($scope.treatmentsAll[i].id!==-1 && $scope.treatmentsAll[i].event.id===$scope.event.id){
                                    $scope.treatmentsCurrent.push($scope.treatmentsAll[i]);
                                }
                        }
                        $scope.treatments= $scope.treatmentsAll;
                        $scope.treatmentTotal = $scope.treatmentsAll[$scope.treatmentsAll.length-1];
                    }
                }
            );
        };
        $scope.orderTreatments = function () {
            var orderBy = $filter('orderBy');
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
        };

        /*$scope.populateTreatments = function () {
            if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                && $scope.treatmentsAll.length>0){
                var orderBy = $filter('orderBy');
                $scope.treatmentsAll = orderBy($scope.treatmentsAll, ['-treatment_date','-id']);
                var firstElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                var lastElement = ($scope.treatmentPage-1)*$scope.treatmentPer_Page;
                for(var i = 0;i<$scope.treatmentsAll.length;i++){
                    if($scope.displayAllPatientTreatments === false){
                        if(i>=firstElement && i<=lastElement){
                            $scope.treatments.push($scope.treatments[i]);
                        }
                    }else{

                    }
                }
            }
        }*/

        $scope.loadAllTreatments = function () {
            ///
            if($scope.treatments === null || $scope.treatments === undefined || $scope.treatments.length===0){
                $scope.loadAllTreatmentsFromServer();
            }
            ///

            if ($scope.displayAllPatientTreatments === false) {
                /*Treatment.query({
                        eventId: $scope.event.id,
                        page: $scope.treatmentPage,
                        per_page: 5
                    }, function (result, headers) {
                        $scope.selectedDate = new Date($stateParams.selectedDate);
                        $scope.linksTreatment = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                        $scope.sort ('description');
                    }
                );*/
                $scope.treatments = $scope.treatmentsCurrent;
            } else {
                $scope.treatments = $scope.treatmentsAll;
                /*Treatment.query({
                        patientId: $scope.event.user.id,
                        page: $scope.treatmentPage,
                        per_page: 5
                    }, function (result, headers) {
                        $scope.linksTreatment = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                        if($scope.treatmentPage===1 && $scope.treatments!==null &&
                            $scope.treatments!=undefined && $scope.treatments.length>0){
                            $scope.treatmentTotal = $scope.treatments[$scope.treatments.length-1];
                        }else if($scope.treatmentPage>1){
                            $scope.treatments.push($scope.treatmentTotal);
                        }

                    }
                );*/
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
                    query: $scope.searchQuery, eventId: $scope.event.id,
                    page: $scope.treatmentPage, per_page: 5
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
            //$modalInstance.dismiss('cancel');
        };
        // Treatment to display all or only treatment events
        $scope.displayAllPatientTreatmentsFunction = function () {
            $scope.displayAllPatientTreatments = true;
            $scope.displayTreatments = true;
            $scope.displayAddEditViewPopup=false;
            $scope.treatmentPage = 1;
            $scope.loadPageTreatment($scope.treatmentPage);
        }
        $scope.displayPatientTreatmentsEventFunction = function () {
            $scope.displayAllPatientTreatments = false;
            $scope.displayTreatments = true;
            $scope.displayAddEditViewPopup=false;
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
            $scope.oldTreatmentPrice = null;
            $scope.oldTreatmentPaidPrice = null;
            $scope.attachments=null;
            $scope.dialogPopupTreatment();
            $scope.loadFunds();
        };
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
            $scope.loadFunds();
            $scope.loadAllAttachments(1);
        };

        var onSaveTreatmentFinished = function (result) {
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

            // New feature
            $scope.treatmentsAll.push($scope.treatment);
            $scope.orderTreatments();


            $scope.loadFunds();
            $scope.loadAllAttachments(1);
            if($scope.event.eventStatus.id === 1){
                $scope.event.eventStatus.id = 3;
            }
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
        };
        $scope.viewTreatmentDetail = function (treatment) {
            $scope.treatment = treatment;
            $scope.displayAddEditViewPopup = true;
            $scope.displayTreatments = false;
            $scope.displayTreatments = false;
            $scope.displayTreatmentView = true;
            $scope.loadAllAttachments(1);
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
            $scope.$emit('windoctorApp:eventUpdate', result.event);
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


        /********************************************************************************/
        /********************************************************************************/
        /**************************           Paginator        **************************/
        /********************************************************************************/
        /********************************************************************************/

        /*$scope.pageSize = 4;
        $scope.allItems = $scope.treatments;
        $scope.reverse = false;
        $scope.searched = function (valLists,toSearch) {
            return _.filter(valLists,
                function (i) {
                    return searchUtil(i, toSearch);
                });
        };

        $scope.paged = function (valLists,pageSize)
        {
            var retVal = [];
            for (var i = 0; i < valLists.length; i++) {
                if (i % pageSize === 0) {
                    retVal[Math.floor(i / pageSize)] = [valLists[i]];
                } else {
                    retVal[Math.floor(i / pageSize)].push(valLists[i]);
                }
            }
            return retVal;
        };
        $scope.resetAll = function () {
            $scope.filteredList = $scope.allItems;
            $scope.newEmpId = '';
            $scope.newDescription = '';
            $scope.newEventReasonDescription = '';
            $scope.searchText = '';
            $scope.currentPage = 0;
            $scope.Header = ['','',''];
        }


        $scope.add = function () {
            $scope.allItems.push({
                EmpId: $scope.newEmpId,
                name: $scope.newDescription,
                Email: $scope.newEventReasonDescription
            });
            $scope.resetAll();
        }

        $scope.search = function () {
            $scope.filteredList =
                $scope.searched($scope.allItems, $scope.searchText);

            if ($scope.searchText == '') {
                $scope.filteredList = $scope.allItems;
            }
            $scope.pagination();
        }


        // Calculate Total Number of Pages based on Search Result
        $scope.pagination = function () {
            $scope.filteredList = $scope.treatments;
            console.log('$scope.filteredList '+$scope.filteredList);
            $scope.ItemsByPage = $scope.paged( $scope.filteredList, $scope.pageSize );
        };

        $scope.setPage = function () {
            $scope.currentPage = this.n;
        };

        $scope.firstPage = function () {
            $scope.currentPage = 0;
        };

        $scope.lastPage = function () {
            $scope.currentPage = $scope.ItemsByPage.length - 1;
        };

        $scope.range = function (input, total) {
            var ret = [];
            if (!total) {
                total = input;
                input = 0;
            }
            for (var i = input; i < total; i++) {
                if (i != 0 && i != total - 1) {
                    ret.push(i);
                }
            }
            return ret;
        };

        $scope.sort = function(sortBy){
            $scope.resetAll();

            $scope.columnToOrder = sortBy;

            //$Filter - Standard Service
            $scope.filteredList = $filter('orderBy')($scope.filteredList, ['-treatment_date','-id'], false);
            var iconName;
            if($scope.reverse)
                iconName = 'glyphicon glyphicon-chevron-up';
            else
                iconName = 'glyphicon glyphicon-chevron-down';

            if(sortBy === 'EmpId')
            {
                $scope.Header[0] = iconName;
            }
            else if(sortBy === 'name')
            {
                $scope.Header[1] = iconName;
            }else {
                $scope.Header[2] = iconName;
            }

            $scope.reverse = !$scope.reverse;

            $scope.pagination();
        };

        //By Default sort ny Name

        function searchUtil(item, toSearch) {
            return (item.description.toLowerCase().indexOf(toSearch.toLowerCase()) > -1 || item.eventReason.description.toLowerCase().indexOf(toSearch.toLowerCase()) > -1 || item.EmpId == toSearch) ? true : false;
        }
        */








        $scope.initTreatmentPage = function () {
            $scope.initVariables();
            $scope.loadAllTreatmentsFromServer();
            $scope.initEventReasons();
            $scope.loadFunds();
            $scope.initDoctors();
            console.log('displayTreatmentsPage values '+$scope.displayTreatmentsPage);
            console.log('displayTreatments values '+$scope.displayTreatments);
        }

        $scope.initTreatmentPage();
    };
