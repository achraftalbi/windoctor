'use strict';

angular.module('windoctorApp')
    .controller('AttachmentController', function ($scope, Attachment, AttachmentSearch, ParseLinks) {
        $scope.attachments = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Attachment.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.attachments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Attachment.get({id: id}, function(result) {
                $scope.attachment = result;
                $('#deleteAttachmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Attachment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAttachmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AttachmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.attachments = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.attachment = {description: null, image: null, id: null};
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
    });
