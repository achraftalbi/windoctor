'use strict';
angular.module('windoctorApp').expandTreatmentsControllerPrescriptions =
    function ($scope, $rootScope, $stateParams, $filter) {


        $scope.initVariables = function(){
            $scope.options = {
                title: 'windoctorApp.prescription.home.title',
                filterPlaceHolder: 'windoctorApp.prescription.filterPlaceHolder',
                labelAll: 'windoctorApp.prescription.allMedications',
                labelSelected: 'windoctorApp.prescription.selectedMedications',
                deselectAll: 'windoctorApp.prescription.deselectAll',
                selectAll: 'windoctorApp.prescription.selectAll',
                helpMessage: 'windoctorApp.prescription.helpMessage',
                /* angular will use this to filter your lists */
                orderProperty: 'name',
                /* this contains the initial list of all items (i.e. the left side) */
                items: [{'id': '50', 'name': 'Germany','selected':false}, {'id': '45', 'name': 'Spain','selected':false}, {'id': '66', 'name': 'Italy','selected':false}, {'id': '30', 'name' : 'Brazil' ,'selected':false},
                    {'id': '41', 'name': 'France' ,'selected':false}, {'id': '34', 'name': 'Argentina','selected':false},
                    {'id': '71', 'name': 'Germany','selected':false}, {'id': '72', 'name': 'Spain','selected':false}, {'id': '73', 'name': 'Italy','selected':false}, {'id': '74', 'name' : 'Brazil' ,'selected':false},
                    {'id': '75', 'name': 'France' ,'selected':false}, {'id': '76', 'name': 'Argentina','selected':false},
                    {'id': '77', 'name': 'Germany','selected':false}, {'id': '78', 'name': 'Spain','selected':false}, {'id': '79', 'name': 'Italy','selected':false}, {'id': '80', 'name' : 'Brazil' ,'selected':false},
                    {'id': '81', 'name': 'France' ,'selected':false}, {'id': '82', 'name': 'Argentina','selected':false}
                ],
                /* this list should be initialized as empty or with any pre-selected items */
                selectedItems: []
            };
        };

        $scope.clickOnPrescriptions = function () {
            if($scope.displayPlanTab){
                $scope.treatmentsPlanAll= $scope.treatments;
            }
            $scope.displayPrescriptionsTab=true;
            $scope.displayOthersTab=true;
            $scope.displayActsTab=true;
            $scope.displayPlanTab=false;
            $scope.displayArchivesTab=false;
            $scope.treatments= $scope.treatmentsAll;
            $scope.displayAddEditViewPopup = false;
            $scope.displayTreatments = true;
            $scope.treatmentTotal = $scope.treatmentsAll[0];
            $scope.treatmentDialogFieldClass = 'form-group col-xs-6 col-md-3';

            $scope.clear();
            $scope.myCanvas();
            $scope.createSelectedElements();
            $scope.displayActsTab=false;
        };
        $scope.unselectTransfer = function(from, to, index) {
            if (index >= 0) {
                for (var i = 0; i < to.length; i++) {
                    if(to[i].selected===true &&
                        to[i].id===from[index].id){
                        to[i].selected = false;
                        break;
                    }
                }
                from.splice(index, 1);
            } else {
                for (var i = 0; i < to.length; i++) {
                    to[i].selected = false;
                }
                from.length = 0;
            }
        };
        $scope.transferToSelected = function(from, to, index) {
            if (index >= 0) {
                if(from[index].selected===false){
                    to.push(from[index]);
                    from[index].selected = true;
                }else{
                    var intexToDetele = -1;
                    for (var i = 0; i < to.length; i++) {
                        if(to[i].id===from[index].id){
                            intexToDetele = i;
                            from[index].selected = false;
                            break;
                        }
                    }
                    if(intexToDetele!==-1){
                        to.splice(intexToDetele, 1);
                    }
                }
            } else {
                for (var i = 0; i < from.length; i++) {
                    if(from[i].selected===false){
                        from[i].selected = true;
                        to.push(from[i]);
                    }
                }
            }
        };
        $scope.editSelectedItem = function (item) {
            console.log("item edited"+item.id);
        };
        $scope.deleteSelectedItem = function (item) {
            console.log("item delted"+item.id);
        };

        $scope.initPrescriptionsPage = function () {
            $scope.initVariables();
        };

        $scope.initPrescriptionsPage();
    };
