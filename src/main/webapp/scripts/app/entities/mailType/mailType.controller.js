'use strict';

angular.module('windoctorApp')
    .controller('MailTypeController', function ($scope, MailType, MailTypeSearch, ParseLinks) {
        $scope.mailTypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            MailType.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.mailTypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            MailType.get({id: id}, function(result) {
                $scope.mailType = result;
                $('#deleteMailTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MailType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMailTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MailTypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.mailTypes = result;
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
            $scope.mailType = {
                label: null,
                description: null,
                content: null,
                id: null
            };
        };
    });
