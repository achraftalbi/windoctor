'use strict';

angular.module('windoctorApp')
    .controller('PatientController', function ($scope,$rootScope, Patient, PatientSearch, $filter,
                                               Event_reason,ParseLinks,$stateParams, Treatment, TreatmentSearch,
                                               Attachment,Fund, Doctor,LANGUAGES,Principal) {
        $scope.patients = [];
        $scope.page = 1;
        $scope.searchCalled = false;
        $scope.captureAnImageScreen = false;
        $scope.displayPatientPage = true;
        $scope.displayChartPage = false;
        $scope.displayTreatments = false;
        $scope.displayAllPatientTreatments = true;
        $scope.loadAll = function() {
            Patient.query({page: $scope.page, per_page: 5}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.patients = result;
                console.log("username "+$rootScope.username);
            });
        };
        $scope.setClassTypeStructure = function(){
            $scope.typeStructureId = $scope.account.typeStructureId;
            $scope.firstClassTypeStructure = $scope.typeStructureId===null || $scope.typeStructureId===undefined||$scope.typeStructureId<=1000 ?
                'form-group col-xs-9':'form-group col-xs-12';
            $scope.segondClassTypeStructure = $scope.typeStructureId===null || $scope.typeStructureId===undefined||$scope.typeStructureId<=1000 ?
                'form-group col-xs-3':'form-group putHiddenCss';
        };

        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.setClassTypeStructure();
        });


        $scope.loadAll();

        $scope.delete = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $('#deletePatientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Patient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePatientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PatientSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.patients = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.page = 1;
                    $scope.loadAll();
                }
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearchPatient();
            }else{
                $scope.loadAll();
            }
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearchPatient();
        };

        $scope.loadAllSearchPatient = function () {
            if($scope.searchQuery===null || $scope.searchQuery===undefined || $scope.searchQuery===''){
                $scope.loadAll();
            }else{
                PatientSearch.query({query: $scope.searchQuery,
                    page: $scope.page, per_page: 5}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.patients = result;
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
            $scope.patient = {login: null, password: null, firstName: null, lastName: null, email: null, activated: null, blocked: null, picture: null, id: null};
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
        $scope.isUndefinedOrNull = function(val) {
            var test=$rootScope.isUndefinedOrNull(val);
            return test;
        }
        $scope.hideDeleteButton = function(val) {
            console.log("first"+(val===true));
            console.log("segond"+(val==='true'));
            return val===true;
        }

        $scope.displayPatientChartPage = function (patient) {
            $scope.event={id:null,user:patient}
            $scope.displayPatientPage = false;
            $scope.displayChartPage = true;
            $scope.displayTreatments = true;
            console.log('call displayManageTreatmentsPage'+$scope.displayManageTreatmentsPage);
            angular.module('windoctorApp').expandCalendarEventsControllerToTreatments
            ($scope, $rootScope, $stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
                Event_reason, Event, Patient,Attachment,Fund);
        };

        $scope.returnToEventsPage = function (patient) {
            $scope.displayPatientPage = true;
            $scope.displayChartPage = false;
            $scope.displayTreatments = false;
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

    });
