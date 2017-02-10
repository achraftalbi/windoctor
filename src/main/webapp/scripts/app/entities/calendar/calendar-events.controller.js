'use strict';

angular.module('windoctorApp')
    .controller('CalendarEventsController', function ($scope, $rootScope,$stateParams, Event, EventSearch,EventDTO, Patient, ParseLinks,
                                                      PatientSearch, $filter, Principal,$translate,Treatment, TreatmentSearch,
                                                      Doctor,Event_reason,Attachment,Fund,CategoryAct,Plan,$http) {
        $scope.events = [];
        $scope.page = 1;
        $scope.numberPageEvents = 5;
        $scope.selectedDate = null;
        $scope.selectedDateString = null;
        $scope.account = null;
        $scope.userCanAddRequest = false;
        $scope.userNotPatient = false;
        $scope.eventsEmpty = false;
        $scope.selectedDateObject = new Date($stateParams.selectedDate);
        $scope.searchCalled = false;
        $scope.displayEventsPage = true;
        $scope.displayDialogEventPage = false;
        $scope.searchQueryPatientDialog='';
        $scope.displayTreatmentsPage = false;
        $scope.initTextDrawingInCanvas = false;
        console.log('selectedDateString '+moment(new Date($stateParams.selectedDate)).format('YYYY-MM-DDT00:00:00.000').replace('T00:00:00.000',''));
        $scope.loadAll = function () {
            $scope.events = [];
            $scope.eventsEmpty = false;
            Event.query({
                selectedDate: moment(new Date($stateParams.selectedDate)).format('YYYY-MM-DDT00:00:00.000').replace('T00:00:00.000',''),
                page: $scope.page,
                per_page: $scope.numberPageEvents
            }, function (result, headers) {
                $scope.selectedDate = $filter('date')($stateParams.selectedDate, 'MMM dd yyyy');
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.events = result;
                console.log('$scope.events.length '+$scope.events.length);
                if($scope.events!==null && $scope.events!== undefined && $scope.events.length===0){
                    $scope.eventsEmpty = true;
                }
                console.log('events eventsEmpty '+$scope.eventsEmpty);
                $scope.categoryActs = CategoryAct.query();
            });
        };

        $scope.setClassTypeStructure = function(){
            $scope.typeStructureId = $scope.account.typeStructureId;
            $scope.firstClassTypeStructure = $scope.typeStructureId===null || $scope.typeStructureId===undefined||$scope.typeStructureId<=1000 ?
                'form-group col-xs-9':'form-group col-xs-12';
            $scope.segondClassTypeStructure = $scope.typeStructureId===null || $scope.typeStructureId===undefined||$scope.typeStructureId<=1000 ?
                'form-group col-xs-3':'form-group putHiddenCss';
            $scope.hideCss = $scope.typeStructureId===null || $scope.typeStructureId===undefined||$scope.typeStructureId<=1000 ?
                '':'putHiddenCss';
        };

        Principal.identity(true).then(function (account) {
            $scope.account = account;
            console.log('$scope.account.currentUserPatient ' + $scope.account.currentUserPatient);
            console.log('$scope.account.maxEventsReached ' + $scope.account.maxEventsReached);
            $scope.userCanAddRequest = $scope.account.currentUserPatient && !$scope.account.maxEventsReached;
            $scope.userNotPatient = !$scope.account.currentUserPatient;
            $scope.setClassTypeStructure();
        });

        $("a").tooltip();

        $scope.loadPageEvents = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearch();
            }else{
                $scope.loadAll();
            }
        };
        $scope.loadAll();

        $scope.delete = function (event) {
            Event.get({id: event.id}, function (result) {
                $scope.event = result;
                $scope.deleteMessage();
                $('#deleteEventConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEventConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };

        $scope.loadAllSearch = function () {
            if($scope.searchQuery===null || $scope.searchQuery===undefined || $scope.searchQuery===''){
                $scope.loadAll();
            }else {
                EventSearch.query({query: $scope.searchQuery,selectedDate: moment(new Date($stateParams.selectedDate)).format('YYYY-MM-DDT00:00:00.000').replace('T00:00:00.000',''),
                    page: $scope.page, per_page: 5}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.events = result;
                }, function (response) {
                    if (response.status === 404) {
                        $scope.loadAll();
                    }
                });
            }
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.event = {event_date: null, description: null, id: null};
        };

        $scope.cancelDelete = function () {
            $scope.clear();
        };

        $scope.returnToEventsPage = function () {
            $scope.displayEventsPage = true;
            $scope.displayDialogEventPage = false;
            $scope.showListPatients=false;
            //Treat treatment page
            $scope.displayTreatmentsPage = false;
        };

        $scope.editEvent = function (event) {
            //$scope.$emit('windoctorApp:eventUpdate');
            if($scope.account.currentUserPatient && ($scope.account.id!==event.user.id || ($scope.account.id===event.user.id && event.eventStatus.id!==7))){
                return;
            }else{
                $scope.event=event;
                console.log('$scope.event.id '+$scope.event.id);
                $scope.searchPatientDialogText = { description: null};
                $scope.displayEventsPage = false;
                $scope.displayDialogEventPage = true;
                angular.module('windoctorApp').expandCalendarEventsControllerToDialog
                ($scope, $stateParams, Event, EventDTO , Patient, ParseLinks, PatientSearch, $filter, Principal);
            }
        };

        $scope.addEvent = function (status) {
            //$scope.$emit('windoctorApp:eventUpdate');
            $scope.event = {event_date: null, description: null, id: null,eventStatus:{id:status}};
            $scope.searchPatientDialogText = { description: null};
            $scope.displayEventsPage = false;
            $scope.searchQueryPatientDialog='';
            $scope.displayDialogEventPage = true;
            angular.module('windoctorApp').expandCalendarEventsControllerToDialog
            ($scope, $stateParams, Event, EventDTO , Patient, ParseLinks, PatientSearch, $filter, Principal);
        };

        $scope.displayManageTreatmentsPage = function (event) {
            if((!$scope.account.currentUserPatient)){
                $scope.event=event;
                $scope.displayEventsPage = false;
                $scope.displayTreatmentsPage = true;
                if($scope.event.user.phoneNumber===null || $scope.event.user.phoneNumber===undefined
                    || $scope.event.user.phoneNumber.length!==14){
                    $scope.event.user.phoneNumber = '00212' + $scope.event.user.phoneNumber;
                }
                $scope.displayTreatments = true;
                console.log('call displayManageTreatmentsPage'+$scope.displayManageTreatmentsPage);
                angular.module('windoctorApp').expandCalendarEventsControllerToTreatments
                ($scope, $rootScope, $stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
                    Event_reason, Event, Patient,Attachment,Fund,Plan,$http);
            }
        };

        $scope.editPatient = function () {
            //$scope.$emit('windoctorApp:eventUpdate');
            $scope.patient=$scope.event.user;
            if($scope.patient.smoking===null || $scope.patient.smoking === undefined){
                $scope.patient.smoking = false;
            }
            if($scope.patient.bleedingWhileBrushing===null || $scope.patient.bleedingWhileBrushing === undefined){
                $scope.patient.bleedingWhileBrushing = false;
            }
            if($scope.patient.toothSensitivity===null || $scope.patient.toothSensitivity === undefined){
                $scope.patient.toothSensitivity = false;
            }
            $scope.displayTreatmentsPage= false;
            $scope.editPatientField= true;
            angular.module('windoctorApp').expandPatientController
            ($scope, $stateParams, Patient);
        };

        $scope.changeDate = function (modelName, newDate) {
            console.log(modelName + ' has had a date change. New value is TEST  ' + newDate.format());
            console.log(modelName + ' has had a date change. New value is ' + newDate.format().split('T')[0] + 'T00:00:00+00:00');
            $scope.dateValue = moment(new Date(newDate.format().split('T')[0] + 'T00:00:00+00:00')).utc();
            console.log(modelName + ' has had a date change. $scope.dateValue ' + new Date(moment(new Date($scope.dateValue)).utc()));
        }

        //End Patient pages treatement
        //Annul an appointment
        $scope.annul = function (event) {
            // Annul an appointment
            event.eventStatus.id = 2;
            $scope.save(event);
        };
        $scope.annulByPatient = function (event) {
            // Annul an appointment
            event.eventStatus.id = 10;
            $scope.save(event);
        };
        $scope.accept = function (event) {
            // Accept an appointment
            event.eventStatus.id = 1;
            $scope.save(event);
        };
        $scope.reject = function (event) {
            // Reject an appointment
            var eventTmp = {};
            angular.copy(event, eventTmp);
            console.log("$scope.events.length before " + $scope.events.length);
            $scope.events.splice($scope.events.indexOf(event), 1);
            console.log("$scope.events.length next " + $scope.events.length);
            eventTmp.eventStatus.id = 6;
            $scope.save(eventTmp);
        };
        var onSaveFinished = function (result) {
            $scope.$emit('windoctorApp:eventUpdate', result);
            //$modalInstance.close(result);
        };

        $scope.save = function (event) {
            var eventTmp = {};
            //var userTmp = {};
            angular.copy(event, eventTmp);
            eventTmp.user={id:event.user.id};
            if (eventTmp.id != null) {
                Event.update(eventTmp, onSaveFinished);
            } else {
                Event.save(eventTmp, onSaveFinished);
            }
        };


        $scope.deleteMessage = function () {
            $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            console.log("event to delete  "+$scope.event);
            if($scope.event.eventStatus.id===1){
                // In progress
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===2){
                // Annuled
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAnnuled',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===4){
                // Abondonned
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAbondonned',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===7){
                // Request
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.request.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===8){
                // Visit
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.visit.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else if($scope.event.eventStatus.id===10 || $scope.event.eventStatus.id===11){
                // Annuled by patient
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.questionAnnuledByPatient',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }else{
                $scope.messageToDeleted = $translate.instant('windoctorApp.calendar.appointment.delete.question',{ param : $filter('date')($scope.event.event_date, 'HH:mm', '+0000')});
            }
            return $scope.messageToDeleted;
        };

        /******************************************************************************************/
        /******************************************************************************************/
        /***********************                                                 ******************/
        /***********************      Manage live capture image  attachment      ******************/
        /***********************                                                 ******************/
        /******************************************************************************************/
        /******************************************************************************************/
        /******************************************************************************************/

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
        };

        /********************************************************************************/
        /********************************************************************************/
        /***********************                                       ******************/
        /*********************** Patient : Manage live capture image   ******************/
        /***********************                                       ******************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.setPicturePatient = function ($files, patient) {
            if ($files[0]) {
                var file = $files[0];
                var fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = function (e) {
                    var data = e.target.result;
                    var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function () {
                        patient.picture = base64Data;
                    });
                };
            }
        };

        $scope.makeSnapshotPatient = function () {
            if (_video) {
                var patCanvas = document.querySelector('#snapshotPatient');
                if (!patCanvas) return;

                patCanvas.width = _video.width;
                patCanvas.height = _video.height;
                var ctxPat = patCanvas.getContext('2d');

                var idata = getVideoDataPatient($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
                ctxPat.putImageData(idata, 0, 0);

                sendSnapshotToServerPatient(patCanvas.toDataURL("image/png").replace("image/png", "image/octet-stream"));

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

        var getVideoDataPatient = function getVideoDataPatient(x, y, w, h) {
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
        var sendSnapshotToServerPatient = function sendSnapshotToServerPatient(imgBase64) {
            $scope.setPicturePatient([dataURItoBlob(imgBase64)], $scope.patient);

        };

    });
