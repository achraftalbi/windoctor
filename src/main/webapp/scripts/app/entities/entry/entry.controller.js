'use strict';

angular.module('windoctorApp')
    .controller('EntryController', function ($scope, Entry, EntrySearch, ParseLinks) {
        $scope.entrys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Entry.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.entrys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Entry.get({id: id}, function(result) {
                $scope.entry = result;
                $('#deleteEntryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Entry.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEntryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EntrySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.entrys = result;
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
            $scope.entry = {price: null, amount: null, creation_date: null, relative_date: null, id: null};
        };
    });
