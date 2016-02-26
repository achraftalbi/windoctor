'use strict';

angular.module('windoctorApp')
    .controller('Event_reasonController', function ($scope, Event_reason, Event_reasonSearch, ParseLinks) {
        $scope.event_reasons = [];
        $scope.page = 1;
        $scope.searchCalled = false;
        $scope.loadAll = function () {
            Event_reason.query({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.event_reasons = result;
                console.log('event_reasons length'+$scope.event_reasons.length)
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
        $scope.loadAll();

        $scope.delete = function (id) {
            Event_reason.get({id: id}, function(result) {
                $scope.event_reason = result;
                $('#deleteEvent_reasonConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Event_reason.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEvent_reasonConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            $scope.page = 1;
            $scope.searchCalled = true;
            $scope.loadAllSearch();
        };

        $scope.loadAllSearch = function () {
            Event_reasonSearch.query({query: $scope.searchQuery,page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.event_reasons = result;
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
            $scope.event_reason = {description: null, id: null};
        };
    });
