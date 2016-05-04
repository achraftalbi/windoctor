'use strict';

angular.module('windoctorApp')
    .controller('ChargeController', function ($scope, Charge, ChargeSearch,$stateParams,CategoryCharge,Fund,Principal,Entry, ParseLinks,$filter) {
        $scope.charges = [];
        $scope.chargesThreshold = [];
        $scope.page = 1;
        $scope.pageThreshold = 1;
        $scope.searchCalled = false;
        $scope.displayCharge= true;
        $scope.editChargeField= false;
        $scope.addChargeField= false;
        $scope.funds=null;
        $scope.loadAll = function () {
            Charge.query({typeChargeToGet:1,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.charges = result;
                if($scope.categoryCharges===null || $scope.categoryCharges===undefined){
                    $scope.categoryCharges = CategoryCharge.query();
                }
                $scope.loadFunds();
            });
        };
        $scope.loadFunds = function () {
            if($scope.funds===null || $scope.funds===undefined){
                Fund.query(function (result, headers) {
                        $scope.funds = result;
                    }
                );
            }
        };
        $scope.loadAllThreshold = function () {
            Charge.query({typeChargeToGet:2,page: $scope.pageThreshold, per_page: 5}, function (result, headers) {
                $scope.linksThreshold = ParseLinks.parse(headers('link'));
                $scope.chargesThreshold = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            if($scope.searchCalled){
                $scope.loadAllSearch();
            }else{
                $scope.loadAll();
            }
        };

        $scope.editCharge = function (charge) {
            //$scope.$emit('windoctorApp:eventUpdate');
            $scope.charge=charge;
            $scope.displayCharge= false;
            $scope.editChargeField= true;
            angular.module('windoctorApp').expandChargeController
            ($scope, $stateParams, Charge, CategoryCharge, Fund,Principal,Entry,ParseLinks,$filter);
        };

        $scope.addCharge = function () {
            //$scope.$emit('windoctorApp:eventUpdate');
            $scope.charge = {name: null, image: null, price: 0, amount: 0, id: null};
            $scope.charge.fund =  $scope.funds[0];
            $scope.displayCharge = false;
            $scope.addChargeField = true;
            angular.module('windoctorApp').expandChargeController
            ($scope, $stateParams, Charge, CategoryCharge, Fund,Principal,Entry,ParseLinks,$filter);
        };

        $scope.loadPageThreshold = function (page) {
            $scope.pageThreshold = page;
            $scope.loadAllThreshold();
        };
        $scope.loadAll();
        $scope.loadAllThreshold();

        $scope.changeDate = function (modelName, newDate) {
            console.log(modelName + ' has had a date change. New value is TEST  ' + newDate.format());
            console.log(modelName + ' has had a date change. New value is ' + newDate.format().split('T')[0] + 'T00:00:00+00:00');
            $scope.dateValueEntry = moment(new Date(newDate.format().split('T')[0] + 'T00:00:00+00:00')).utc();
            console.log(modelName + ' has had a date change. $scope.dateValueEntry ' + new Date(moment(new Date($scope.dateValueEntry)).utc()));
        }

        $scope.delete = function (id) {
            Charge.get({id: id}, function (result) {
                $scope.charge = result;
                $('#deleteChargeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Charge.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChargeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };
        $scope.loadAllSearch = function () {
            ChargeSearch.query({query: $scope.searchQuery,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.charges = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.charge = {name: null, image: null, price: null, amount: null, id: null};
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

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 10) {
                return text;
            }
            return text ? (text.substring(0, 10) + '...' + text.slice(-10)) : '';
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

        $scope.setImage = function ($files, charge) {
            if ($files[0]) {
                var file = $files[0];
                var fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = function (e) {
                    var data = e.target.result;
                    var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function () {
                        charge.image = base64Data;
                    });
                };
            }
        };

        /********************************************************************************/
        /********************************************************************************/
        /***********************                                       ******************/
        /***********************      Manage live capture image        ******************/
        /***********************                                       ******************/
        /********************************************************************************/
        /********************************************************************************/
        /********************************************************************************/

        $scope.captureAnImage = function () {
            $scope.captureAnImageScreen = true;
        };
        $scope.cancelImageCapture = function () {
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
                function () {
                    $scope.webcamError = err;
                }
            );
        };

        $scope.onSuccess = function () {
            // The video element contains the captured camera data
            _video = $scope.channel.video;
            $scope.$apply(function () {
                $scope.patOpts.w = _video.width;
                $scope.patOpts.h = _video.height;
                //$scope.showDemos = true;
            });
        };

        $scope.onStream = function (stream) {
            // You could do something manually with the stream.
        };

        $scope.makeSnapshot = function () {
            if (_video) {
                var patCanvas = document.querySelector('#snapshot');
                if (!patCanvas) return;

                patCanvas.width = _video.width;
                patCanvas.height = _video.height;
                var ctxPat = patCanvas.getContext('2d');

                var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
                ctxPat.putImageData(idata, 0, 0);

                sendSnapshotToServer(patCanvas.toDataURL("image/png").replace("image/png", "image/octet-stream"));

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
            $scope.setImage([dataURItoBlob(imgBase64)], $scope.charge);

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

            return new Blob([ia], {type: mimeString});
        }


    });
