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
            console.log('length event used '+$scope.event.id);
            Treatment.query({
                    patientId: $scope.event.user.id
                }, function (result, headers) {
                    $scope.selectedDate = new Date($stateParams.selectedDate);
                    $scope.linksTreatment = ParseLinks.parse(headers('link'));
                    $scope.treatmentsAll = result;
                    $scope.treatmentTotal = $scope.treatmentsAll[$scope.treatmentsAll.length-1];
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

                        if($scope.event!==null && $scope.event.id!==null && $scope.event.id!==undefined){
                            for(var i = 0;i<length;i++){
                                console.log('curent tre 1 '+(($scope.treatmentsAll[i].event!==null && $scope.treatmentsAll[i].event!==undefined
                                && $scope.treatmentsAll[i].event.id!==null && $scope.treatmentsAll[i].event.id!==undefined)?$scope.treatmentsAll[i].event.id:'Total'));
                                if($scope.treatmentsAll[i].id!==-1 && $scope.treatmentsAll[i].event.id===$scope.event.id){
                                    console.log('curent tre 2 '+i);
                                    $scope.treatmentsCurrent.push($scope.treatmentsAll[i]);
                                }
                            }
                        }
                        $scope.displayAllPatientTreatments = true;
                        $scope.treatments= $scope.treatmentsAll;
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
            if($scope.event.id===null){
                $scope.displayAllPatientTreatments =true;
            }
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
            /*if(( $scope.treatment.price - $scope.treatment.paid_price) < 0){
                console.log("1 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = true;
            }else{
                console.log("2 $scope.treatment.price - $scope.treatment.paid_price "+($scope.treatment.price - $scope.treatment.paid_price));
                $scope.priceLessThanPaidPrice = false;
            }*/

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
            console.log('save $scope.treatment.id'+$scope.treatment.id+' $scope.treatment.event.id '+$scope.treatment.event.id);
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
            $scope.displayAllPatientTreatments = true;

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


        /*$scope.hexToRgb = function (color) {
            var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
            color = color.replace(shorthandRegex, function(m, r, g, b) {
                return r + r + g + g + b + b;
            });

            var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(color);
            return result ? {
                r: parseInt(result[1], 16),
                g: parseInt(result[2], 16),
                b: parseInt(result[3], 16)
            } : {
                r: 0,
                g: 0,
                b: 0
            };
        }*/

        $scope.myCanvas = function () {

        }
        $scope.myCanvas = function () {
            var c = document.getElementById("myCanvas");
            var ctx = c.getContext("2d");
            ctx.clearRect(0, 0, c.width, c.height);
            //var img = document.getElementById("scream");
            var typeImg = '.png';
            var pathNormalImage = '../../../../assets/images/elements/teethschame/';
            var elements=[

                // Top right Big
                {id:28,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:200,yDraw:-250,zRotation:40,width:null,height:null,x:null,y:null},
                {id:27,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:210,yDraw:-215,zRotation:30,width:null,height:null,x:null,y:null},
                {id:26,img:null,widthDraw:1/3+1/40,heightDraw:1/3+1/40,xDraw:158,yDraw:-239,zRotation:37,width:null,height:null,x:null,y:null},
                {id:25,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:134,yDraw:-236,zRotation:37,width:null,height:null,x:null,y:null},
                {id:24,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:132,yDraw:-215,zRotation:31,width:null,height:null,x:null,y:null},
                {id:23,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:172,yDraw:-137,zRotation:6,width:null,height:null,x:null,y:null},
                {id:22,img:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:172,yDraw:-80,zRotation:-11,width:null,height:null,x:null,y:null},
                {id:21,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:153,yDraw:5,zRotation:-40,width:null,height:null,x:null,y:null},


                // Top children
                {id:65,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:205,yDraw:160,zRotation:-40,width:null,height:null,x:null,y:null},
                {id:64,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:149,yDraw:188,zRotation:-56,width:null,height:null,x:null,y:null},
                {id:63,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:221,yDraw:-77,zRotation:6,width:null,height:null,x:null,y:null},
                {id:62,img:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:203,yDraw:-42,zRotation:-3,width:null,height:null,x:null,y:null},
                {id:61,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:153,yDraw:83,zRotation:-40,width:null,height:null,x:null,y:null},
                {id:51,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:125,yDraw:82,zRotation:-40,width:null,height:null,x:null,y:null},
                {id:52,img:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:48,yDraw:132,zRotation:-65,width:null,height:null,x:null,y:null},
                {id:53,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-35,yDraw:143,zRotation:-87,width:null,height:null,x:null,y:null},
                {id:64,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-155,yDraw:-120,zRotation:-200,width:null,height:null,x:null,y:null},
                {id:65,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-159,yDraw:-147,zRotation:-200,width:null,height:null,x:null,y:null},
                // Bottom children
                {id:75,img:null,widthDraw:1/3-1/40,heightDraw:1/3-1/40,xDraw:120,yDraw:-292,zRotation:67,width:null,height:null,x:null,y:null},
                {id:74,img:null,widthDraw:1/3-1/40,heightDraw:1/3-1/40,xDraw:31,yDraw:-330,zRotation:90,width:null,height:null,x:null,y:null},
                {id:73,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:48,yDraw:-331,zRotation:92,width:null,height:null,x:null,y:null},
                {id:72,img:null,widthDraw:1/3-1/10,heightDraw:1/3-1/10,xDraw:-111,yDraw:-320,zRotation:125,width:null,height:null,x:null,y:null},
                {id:71,img:null,widthDraw:1/3-1/10,heightDraw:1/3-1/10,xDraw:-170,yDraw:-289,zRotation:140,width:null,height:null,x:null,y:null},
                {id:81,img:null,widthDraw:1/3-1/7-1/130,heightDraw:1/3-1/7-1/130,xDraw:-151,yDraw:-289,zRotation:140,width:null,height:null,x:null,y:null},
                {id:82,img:null,widthDraw:1/3-1/8-1/130,heightDraw:1/3-1/8-1/130,xDraw:-200,yDraw:-243,zRotation:155,width:null,height:null,x:null,y:null},
                {id:83,img:null,widthDraw:1/3-1/15,heightDraw:1/3-1/15,xDraw:-270,yDraw:-121,zRotation:185,width:null,height:null,x:null,y:null},
                {id:84,img:null,widthDraw:1/3-1/25,heightDraw:1/3-1/25,xDraw:-266,yDraw:8,zRotation:215,width:null,height:null,x:null,y:null},
                {id:85,img:null,widthDraw:1/3-1/17,heightDraw:1/3-1/17,xDraw:-238,yDraw:8,zRotation:215,width:null,height:null,x:null,y:null},
                // Bottom right Big
                {id:38,img:null,widthDraw:1/2,heightDraw:1/2,xDraw:170,yDraw:-303,zRotation:55,width:null,height:null,x:null,y:null},
                {id:37,img:null,widthDraw:1/2,heightDraw:1/2,xDraw:147,yDraw:-333,zRotation:65,width:null,height:null,x:null,y:null},
                {id:36,img:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:176,yDraw:-333,zRotation:65,width:null,height:null,x:null,y:null},
                {id:35,img:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:180,yDraw:-349,zRotation:70,width:null,height:null,x:null,y:null},
                {id:34,img:null,widthDraw:1/2-1/40,heightDraw:1/2-1/40,xDraw:187,yDraw:-356,zRotation:73,width:null,height:null,x:null,y:null},
                {id:33,img:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:36,yDraw:-415,zRotation:100,width:null,height:null,x:null,y:null},
                {id:32,img:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:-75,yDraw:-417,zRotation:120,width:null,height:null,x:null,y:null},
                {id:31,img:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-185,yDraw:-380,zRotation:140,width:null,height:null,x:null,y:null},
                // Bottom left Big
                {id:41,img:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-156,yDraw:-380,zRotation:140,width:null,height:null,x:null,y:null},
                {id:42,img:null,widthDraw:1/2-1/10,heightDraw:1/2-1/10,xDraw:-244,yDraw:-310,zRotation:160,width:null,height:null,x:null,y:null},
                {id:43,img:null,widthDraw:1/2-1/20,heightDraw:1/2-1/20,xDraw:-321,yDraw:-190,zRotation:185,width:null,height:null,x:null,y:null},
                {id:44,img:null,widthDraw:1/2-1/7,heightDraw:1/2-1/7,xDraw:-326,yDraw:-112,zRotation:200,width:null,height:null,x:null,y:null},
                {id:45,img:null,widthDraw:1/2-1/7,heightDraw:1/2-1/7,xDraw:-301,yDraw:-108,zRotation:200,width:null,height:null,x:null,y:null},
                {id:46,img:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-287,yDraw:-58,zRotation:210,width:null,height:null,x:null,y:null},
                {id:47,img:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-261,yDraw:-18,zRotation:220,width:null,height:null,x:null,y:null},
                {id:48,img:null,widthDraw:1/2-1/8,heightDraw:1/2-1/8,xDraw:-230,yDraw:-15,zRotation:220,width:null,height:null,x:null,y:null},





                // Top left Big
                {id:18,img:null,widthDraw:1/3+1/25,heightDraw:1/3+1/25,xDraw:-175,yDraw:60,zRotation:-118,width:null,height:null,x:null,y:null},
                {id:17,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:-136,yDraw:72,zRotation:-112,width:null,height:null,x:null,y:null},
                {id:16,img:null,widthDraw:1/3+1/40,heightDraw:1/3+1/40,xDraw:-115,yDraw:64,zRotation:-115,width:null,height:null,x:null,y:null},
                {id:15,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:-77,yDraw:70,zRotation:-110,width:null,height:null,x:null,y:null},
                {id:14,img:null,widthDraw:1/3,heightDraw:1/3,xDraw:-40,yDraw:77,zRotation:-103,width:null,height:null,x:null,y:null},
                {id:13,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:22,yDraw:78,zRotation:-80,width:null,height:null,x:null,y:null},
                {id:12,img:null,widthDraw:1/3-1/13,heightDraw:1/3-1/13,xDraw:81,yDraw:54,zRotation:-60,width:null,height:null,x:null,y:null},
                {id:11,img:null,widthDraw:1/3-1/20,heightDraw:1/3-1/20,xDraw:123,yDraw:5,zRotation:-40,width:null,height:null,x:null,y:null}
                /*,
                */

            ];

            var TO_RADIANS = Math.PI/180;
            for(var i=0;i<elements.length;i++){
                var img = new Image();
                img.src = pathNormalImage+elements[i].id+typeImg;
                ctx.rotate(elements[i].zRotation*TO_RADIANS);
                ctx.drawImage(img,elements[i].xDraw,elements[i].yDraw,img.width*elements[i].widthDraw,img.height*elements[i].heightDraw);
                ctx.restore();
                ctx.save();
            }

            ctx.moveTo(100, 165);
            ctx.lineTo(365, -57);
            ctx.moveTo(116, -100);
            ctx.lineTo(452, 300);
            ctx.stroke();

            c.onclick = function(e) {
                if(e.x>0){
                    //var x = e.x;
                    //var y = e.y;
                    //x -= c.offsetLeft;
                    //y -= c.offsetTop;
                    //var x = e.offsetLeft;
                    //var y = e.offsetTop;

                    var rect = c.getBoundingClientRect();
                    var x= e.clientX - rect.left;
                     var   y= e.clientY - rect.top;

                    alert('testOKAK '+c.width+' '+c.height+' '+e.x+' '+e.y+' '+c.offsetLeft+' '+c.offsetTop+' '+(e.pageX-899)+' '+(e.pageY-296)+' posx='+x+' posy='+y+' ');
                }
            }



                /*var imageObj = document.getElementById("scream");
                var canvas = document.getElementById('myCanvas');
                var context = canvas.getContext('2d');
                var x = 50;
                var y = 50;
                context.drawImage(imageObj, x, y);

                var imageData = context.getImageData(x, y, canvas.width,canvas.height);
                var data = imageData.data;
                var rgbColor = $scope.hexToRgb('#00ff00');

                for(var i = 0; i < data.length; i += 4) {
                    if(data[i+3] == 0)
                        continue;
                    data[i + 0] = rgbColor.r;
                    data[i + 1] = rgbColor.g;
                    data[i + 2] = rgbColor.b;
                    data[i + 3] = 255;

                    // red
                    data[i] = 255 - data[i];
                    // green
                    data[i + 1] = 255 - data[i + 1];
                    // blue
                    data[i + 2] = 255 - data[i + 2];
                }

                // overwrite original image
                context.putImageData(imageData, x, y);

                var imageObj2 = document.getElementById("scream");
                var canvas2 = document.getElementById('myCanvas');
                var context2 = canvas.getContext('2d');
                var x2 = 100;
                var y2 = 100;
                context2.drawImage(imageObj2, x2, y2);

                var imageData2 = context2.getImageData(x2, y2, canvas2.width,canvas2.height);
                var data2 = imageData2.data;

                for(i = 0; i < data2.length; i += 4) {
                    // red
                    data2[i] = 255 - data2[i];
                    // green
                    data2[i + 1] = 255 - data2[i + 1];
                    // blue
                    data2[i + 2] = 255 - data2[i + 2];
                }

                // overwrite original image
                context2.putImageData(imageData2, x2, y2);*/


        }

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
