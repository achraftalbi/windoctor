'use strict';

angular.module('windoctorApp')
    .controller('CalendarTreatmentController', function ($scope, $stateParams, $modalInstance, Treatment, TreatmentSearch, Doctor,
                                                         ParseLinks, $filter, Event_reason, Event, Patient,Attachment,Fund) {
        $scope.treatments = [];
        $scope.page = 1;
        $scope.attachmentPage = 1;
        $scope.treatment = null;
        $scope.treatmentTotal = null;
        $scope.oldTreatmentPrice = null;
        $scope.oldTreatmentPaidPrice = null;
        $scope.event = null;
        $scope.patient = null;
        $scope.doctors = null;
        $scope.defaultDoctor = null;
        $scope.treatmentToDelete = null;

        $scope.displayTreatments = true;
        $scope.displayAddEditViewPopup = false;
        $scope.displayAddEditViewAttachmentPopup = false;
        $scope.displayTreatmentView = false;
        $scope.displayAllPatientTreatments = false;
        $scope.displayTreatmentToDelete = false;
        $scope.event_reasons = null;
        $scope.defaultEventReason = null;
        $scope.selectedDate = null;
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
            if ($scope.displayAllPatientTreatments === false) {
                Treatment.query({
                        eventId: $stateParams.eventId,
                        page: $scope.page,
                        per_page: 5
                    }, function (result, headers) {
                        console.log("$stateParams.eventId value "+$stateParams.eventId);
                        $scope.selectedDate = new Date($stateParams.selectedDate);
                        $scope.links = ParseLinks.parse(headers('link'));
                        $scope.treatments = result;
                        if ($scope.event === null) {
                            Event.get({id: $stateParams.eventId}, function (result) {
                                $scope.event = result;
                            });
                        }
                    }
                );
            } else {
                Treatment.query({
                        patientId: $scope.event.user.id,
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
                    }
                )
                ;
            }
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

        $scope.search = function () {
            if($scope.searchQuery!==null && $scope.searchQuery!==undefined && $scope.searchQuery.length>0) {
                TreatmentSearch.query({
                    query: $scope.searchQuery, eventId: $scope.event.id,
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
        // Treatment to display all or only treatment events
        $scope.displayAllPatientTreatmentsFunction = function () {
            $scope.displayAllPatientTreatments = true;
            $scope.page = 1;
            $scope.loadPage($scope.page);
        }
        $scope.displayPatientTreatmentsEventFunction = function () {
            $scope.displayAllPatientTreatments = false;
            $scope.page = 1;
            $scope.loadPage($scope.page);
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
            $scope.treatment = {
                treatment_date: $scope.event.event_date,
                description: null,
                price: $scope.defaultEventReason.price,
                paid_price: $scope.defaultEventReason.price,
                id: null,
                eventReason : $scope.defaultEventReason,
                doctor:$scope.defaultDoctor,
                event: {id: $stateParams.eventId}
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
        /***********************                                       ******************/
        /***********************      Manage live capture image        ******************/
        /***********************                                       ******************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.captureAnImage= function () {
            $scope.captureAnImageScreen = true;
        };
        $scope.cancelImageCapture= function () {
            $scope.captureAnImageScreen = false;
        };

        var _video = null,
            patData = null;

        $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

        // Setup a channel to receive a video property
        // with a reference to the video element
        // See the HTML binding in main.html
        $scope.channel = {};

        $scope.webcamError = false;
        $scope.onError = function (err) {
            $scope.$apply(
                function() {
                    $scope.webcamError = err;
                }
            );
        };

        $scope.onSuccess = function () {
            // The video element contains the captured camera data
            _video = $scope.channel.video;
            $scope.$apply(function() {
                $scope.patOpts.w = _video.width;
                $scope.patOpts.h = _video.height;
                //$scope.showDemos = true;
            });
        };

        $scope.onStream = function (stream) {
            // You could do something manually with the stream.
        };

        $scope.makeSnapshot = function() {
            if (_video) {
                var patCanvas = document.querySelector('#snapshot');
                if (!patCanvas) return;

                patCanvas.width = _video.width;
                patCanvas.height = _video.height;
                var ctxPat = patCanvas.getContext('2d');

                var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
                ctxPat.putImageData(idata, 0, 0);

                sendSnapshotToServer(patCanvas.toDataURL("image/png").replace("image/png", "image/octet-stream") );

                patData = idata;
                $scope.captureAnImageScreen = false;
            }
        };

        /**
         * Redirect the browser to the URL given.
         * Used to download the image by passing a dataURL string
         */
        $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
            window.location.href = dataURL;
        };

        var getVideoData = function getVideoData(x, y, w, h) {
            var hiddenCanvas = document.createElement('canvas');
            hiddenCanvas.width = _video.width;
            hiddenCanvas.height = _video.height;
            var ctx = hiddenCanvas.getContext('2d');
            ctx.drawImage(_video, 0, 0, _video.width, _video.height);
            return ctx.getImageData(x, y, w, h);
        };

        /**
         * This function could be used to send the image data
         * to a backend server that expects base64 encoded images.
         *
         * In this example, we simply store it in the scope for display.
         */
        var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
            $scope.setImage ([dataURItoBlob(imgBase64)], $scope.attachment);

        };

        function dataURItoBlob(dataURI) {
            // convert base64/URLEncoded data component to raw binary data held in a string
            var byteString;
            if (dataURI.split(',')[0].indexOf('base64') >= 0)
                byteString = atob(dataURI.split(',')[1]);
            else
                byteString = unescape(dataURI.split(',')[1]);

            // separate out the mime component
            var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

            // write the bytes of the string to a typed array
            var ia = new Uint8Array(byteString.length);
            for (var i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }

            return new Blob([ia], {type:mimeString});
        }

    })
;
