'use strict';
angular.module('windoctorApp').expandTreatmentsControllerElements =
    function ($scope, $rootScope,$stateParams, Treatment, TreatmentSearch, Doctor, ParseLinks, $filter,
              Event_reason, Event, Patient,Attachment,Fund) {

        $scope.myCanvas = function () {
            for(var i=0;i<$rootScope.elements.length;i++){
                /*var img = new Image();
                img.src = $scope.pathNormalImage+$scope.elements[i].id+$scope.typeImg;*/
                $scope.ctx.rotate($rootScope.elements[i].zRotation*$scope.TO_RADIANS);
                $scope.ctx.drawImage($rootScope.elements[i].img,$rootScope.elements[i].xDraw,$rootScope.elements[i].yDraw,
                    $rootScope.elements[i].img.width*$rootScope.elements[i].widthDraw,
                    $rootScope.elements[i].img.height*$rootScope.elements[i].heightDraw);
                $scope.ctx.restore();
                $scope.ctx.save();
            }

            $scope.ctx.moveTo(100, 165);
            $scope.ctx.lineTo(365, -57);
            $scope.ctx.moveTo(116, -100);
            $scope.ctx.lineTo(452, 300);
            $scope.ctx.stroke();
        };

        $scope.initCanvas = function () {
            $scope.c = document.getElementById("myCanvas");
            $scope.ctx = $scope.c.getContext("2d");
            $scope.ctx.clearRect(0, 0, $scope.c.width, $scope.c.height);
            $scope.typeImg = '.png';
            $scope.pathNormalImage = '../../../../assets/images/elements/teethschame/';
            if(!$scope.initTextDrawingInCanvas){
                $scope.ctx.fillStyle = "black";
                $scope.ctx.font = "bold 24px Arial";
                $scope.ctx.fillText("1", 20, 40);
                $scope.ctx.fillText("2", 265, 40);
                $scope.ctx.fillText("3", 265, 340);
                $scope.ctx.fillText("4", 20, 340);
                $scope.ctx.fillText("5", 130, 180);
                $scope.ctx.fillText("6", 165, 180);
                $scope.ctx.fillText("7", 165, 220);
                $scope.ctx.fillText("8", 130, 220);
                $scope.initTextDrawingInCanvas=true;
            }
            $scope.myCanvas();
            $scope.myCanvas();
            $scope.c.onclick = function (e) {
                if (e.x > 0) {
                    var rect = $scope.c.getBoundingClientRect();
                    var x = e.clientX - rect.left;
                    var y = e.clientY - rect.top;
                    for (var i = 0; i < $rootScope.elements.length; i++) {

                        if (x > $rootScope.elements[i].x && x < ($rootScope.elements[i].x + $rootScope.elements[i].width)
                            && y > $rootScope.elements[i].y && y < ($rootScope.elements[i].y + $rootScope.elements[i].height)) {

                            //alert($rootScope.elements[i].id);
                            if ($scope.displayAddEditViewPopup) {
                                if ($scope.elementsSelected !== null && $scope.elementsSelected !== undefined
                                    && $scope.elementsSelected.length > 0) {
                                    var containElement = false;
                                    var deleteSelectedElement = -1;
                                    for (var j = 0; j < $scope.elementsSelected.length; j++) {
                                        if ($rootScope.elements[i].id === $scope.elementsSelected[j].id) {
                                            var containTreatment = false;
                                            var treatmentIndex = -1;
                                            var treatments = null;
                                            if($scope.displayActsTab){
                                                console.log('$scope.elementsSelected[j].treatments ' + $scope.elementsSelected[j].treatments);
                                                treatments = $scope.elementsSelected[j].treatments;
                                            }else if($scope.displayPlanTab){
                                                console.log('$scope.elementsSelected[j].treatmentsPlan ' + $scope.elementsSelected[j].treatmentsPlan);
                                                treatments = $scope.elementsSelected[j].treatmentsPlan;
                                            }
                                            if(treatments!==null && treatments!==undefined
                                                &&treatments.length>0){
                                                for (var k = 0; k < treatments.length; k++) {
                                                    console.log('treatments[k].id ' + treatments[k].id);
                                                    if ($scope.treatment.id === treatments[k].id) {
                                                        containTreatment = true;
                                                        treatmentIndex = k;
                                                        if (treatments.length >= 2) {
                                                            if($scope.displayActsTab){
                                                                $scope.drawElement($scope.elementsSelected[j].index, 'GREEN');
                                                            }else if($scope.displayPlanTab){
                                                                if ($scope.elementsSelected[j].treatments !== null && $scope.elementsSelected[j].treatments !== undefined
                                                                    && $scope.elementsSelected[j].treatments.length > 0) {
                                                                    $scope.drawElement($scope.elementsSelected[j].index, 'ORANGE');
                                                                } else {
                                                                    $scope.drawElement($scope.elementsSelected[j].index, 'YELLOW');
                                                                }
                                                            }
                                                        } else {
                                                            if($scope.displayActsTab){
                                                                $scope.drawElement($scope.elementsSelected[j].index, '');
                                                            }else if($scope.displayPlanTab){
                                                                if ($scope.elementsSelected[j].treatments !== null && $scope.elementsSelected[j].treatments !== undefined
                                                                    && $scope.elementsSelected[j].treatments.length > 0) {
                                                                    $scope.drawElement($scope.elementsSelected[j].index, 'GREEN');
                                                                } else {
                                                                    $scope.drawElement($scope.elementsSelected[j].index, '');
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            if (containTreatment && treatments.length === 1) {
                                                deleteSelectedElement = j;
                                            }
                                            if(treatmentIndex>-1){
                                                treatments.splice(treatmentIndex, 1);
                                            }
                                            if(!containTreatment){
                                                if(treatments === null || treatments === undefined){
                                                    if($scope.displayActsTab){
                                                        $scope.elementsSelected[j].treatments = [];
                                                        treatments = $scope.elementsSelected[j].treatments;
                                                    }else if($scope.displayPlanTab){
                                                        $scope.elementsSelected[j].treatmentsPlan = [];
                                                        treatments = $scope.elementsSelected[j].treatmentsPlan;
                                                    }
                                                }
                                                treatments.push($scope.treatment);
                                                $scope.drawElement($scope.elementsSelected[j].index, 'RED');
                                            }
                                            containElement = true;
                                            break;
                                        }
                                    }
                                    /*if (deleteSelectedElement > -1) {
                                        $scope.elementsSelected.splice(deleteSelectedElement, 1);
                                    }*/
                                    if (!containElement) {
                                        if($scope.displayActsTab){
                                            $scope.elementsSelected.push({
                                                id: $rootScope.elements[i].id,
                                                treatments: [$scope.treatment],
                                                treatmentsPlan: null,
                                                index: i
                                            });
                                        }else if($scope.displayPlanTab){
                                            $scope.elementsSelected.push({
                                                id: $rootScope.elements[i].id,
                                                treatments: null,
                                                treatmentsPlan: [$scope.treatment],
                                                index: i
                                            });
                                        }
                                        $scope.drawElement(i, 'RED');
                                    }
                                } else {
                                    if($scope.displayActsTab){
                                        $scope.elementsSelected.push({
                                            id: $rootScope.elements[i].id,
                                            treatments: [$scope.treatment],
                                            treatmentsPlan: null,
                                            index: i
                                        });
                                    }else if($scope.displayPlanTab){
                                        $scope.elementsSelected.push({
                                            id: $rootScope.elements[i].id,
                                            treatments: null,
                                            treatmentsPlan: [$scope.treatment],
                                            index: i
                                        });
                                    }
                                    $scope.drawElement(i, 'RED');
                                }
                                $scope.constructTemporaryElements($scope.treatment);
                            } else {
                                if ($scope.elementsSelected !== null && $scope.elementsSelected !== undefined
                                    && $scope.elementsSelected.length > 0 && $scope.displayActsTab) {
                                    for (var j = 0; j < $scope.elementsSelected.length; j++) {
                                        if ($rootScope.elements[i].id === $scope.elementsSelected[j].id &&
                                            $scope.elementsSelected[j].treatments!== null && $scope.elementsSelected[j].treatments !== undefined
                                            && $scope.elementsSelected[j].treatments.length > 0) {
                                            if ($scope.selectedElement !== null && $scope.selectedElement !== undefined) {
                                                $scope.drawElement($scope.selectedElement.index, 'GREEN');
                                            }
                                            $scope.drawElement(i, 'BLUE');
                                            $scope.selectedElement = $scope.elementsSelected[j];
                                            var orderBy = $filter('orderBy');
                                            $scope.treatments = orderBy($scope.elementsSelected[j].treatments, ['-treatment_date', '-id']);
                                            $scope.$apply();
                                            break;
                                        }
                                    }
                                }
                            }


                            break;

                        }
                    }
                    //alert('testOKAK '+$scope.c.width+' '+$scope.c.height+' '+e.x+' '+e.y+' '+$scope.c.offsetLeft+' '+$scope.c.offsetTop+' '+(e.pageX-899)+' '+(e.pageY-296)+' posx='+x+' posy='+y+' ');
                }

            }
        };

        $scope.addElements = function (treatment) {
            if(treatment.elements!==null && treatment.elements!==undefined
                && treatment.elements.length>0){
                var split = treatment.elements.split(',');
                for(var i = 0;i<split.length;i++){
                    var flag = false;
                    for(var j = 0;j<$scope.elementsSelected.length;j++){
                        if($scope.elementsSelected[j].id===parseInt(split[i], 10)){
                            if($scope.displayActsTab){
                                if($scope.elementsSelected[j].treatments!==null && $scope.elementsSelected[j].treatments!==undefined
                                    && $scope.elementsSelected[j].treatments.length>0){
                                    $scope.elementsSelected[j].treatments.push(treatment);
                                }else{
                                    $scope.elementsSelected[j].treatments = [treatment];
                                }
                            }else if($scope.displayPlanTab){
                                if($scope.elementsSelected[j].treatmentsPlan!==null && $scope.elementsSelected[j].treatmentsPlan!==undefined
                                    && $scope.elementsSelected[j].treatmentsPlan.length>0){
                                    $scope.elementsSelected[j].treatmentsPlan.push(treatment);
                                }else{
                                    $scope.elementsSelected[j].treatmentsPlan = [treatment];
                                }
                            }
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        if($scope.displayActsTab){
                            $scope.elementsSelected.push({id:parseInt(split[i], 10),treatments:[treatment],treatmentsPlan:null,index:null});
                        }else if($scope.displayPlanTab){
                            $scope.elementsSelected.push({id:parseInt(split[i], 10),treatments:null,treatmentsPlan:[treatment],index:null});
                        }
                    }
                }
            }
        };

        $scope.constructElementBeforeSave = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                treatment.elements=null;
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    var treatments = null;
                    if($scope.displayActsTab){
                        treatments = $scope.elementsSelected[j].treatments;
                    }else if($scope.displayPlanTab){
                        treatments = $scope.elementsSelected[j].treatmentsPlan;
                    }
                    if(treatments!==null && treatments!==undefined
                        && treatments.length>0){
                        for(var k = 0;k<treatments.length;k++){
                            if(treatment.id===treatments[k].id){
                                if(treatment.elements===null){
                                    treatment.elements =  $scope.elementsSelected[j].id+'';
                                }else{
                                    treatment.elements =  treatment.elements + ',' + $scope.elementsSelected[j].id + '' ;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };

        $scope.constructTemporaryElements = function (treatment) {
            $scope.temporaryElements=null;
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    var treatments = null;
                    if($scope.displayActsTab){
                        treatments = $scope.elementsSelected[j].treatments;
                    }else if($scope.displayPlanTab){
                        treatments = $scope.elementsSelected[j].treatmentsPlan;
                    }
                    if(treatments!==null && treatments!==undefined
                        && treatments.length>0){
                        for(var k = 0;k<treatments.length;k++){
                            if(treatment.id===treatments[k].id){
                                if($scope.temporaryElements===null){
                                    $scope.temporaryElements =  $scope.elementsSelected[j].id+'';
                                }else{
                                    $scope.temporaryElements =  $scope.temporaryElements + ', ' + $scope.elementsSelected[j].id + '' ;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            $scope.$apply();
        };

        $scope.drawElementsAfterSave = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                treatment.elements=null;
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    if($scope.displayActsTab){
                        if($scope.elementsSelected[j].treatments!==null && $scope.elementsSelected[j].treatments!==undefined
                            && $scope.elementsSelected[j].treatments.length>0){
                            $scope.drawElement($scope.elementsSelected[j].index,'GREEN');
                        }else{
                            $scope.drawElement($scope.elementsSelected[j].index,'');
                        }
                    }else if($scope.displayPlanTab){
                        if($scope.elementsSelected[j].treatmentsPlan!==null && $scope.elementsSelected[j].treatmentsPlan!==undefined
                            && $scope.elementsSelected[j].treatmentsPlan.length>0){
                            if($scope.elementsSelected[j].treatments!==null && $scope.elementsSelected[j].treatments!==undefined
                                && $scope.elementsSelected[j].treatments.length>0){
                                $scope.drawElement($scope.elementsSelected[j].index,'ORANGE');
                            }else{
                                $scope.drawElement($scope.elementsSelected[j].index,'YELLOW');
                            }
                        }else if($scope.elementsSelected[j].treatments!==null && $scope.elementsSelected[j].treatments!==undefined
                            && $scope.elementsSelected[j].treatments.length>0){
                            $scope.drawElement($scope.elementsSelected[j].index,'GREEN');
                        }
                    }
                }
            }
        };

        $scope.drawElementsWhenEdit = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    var treatments =null;
                    if($scope.displayActsTab){
                        treatments = $scope.elementsSelected[j].treatments;
                    }else if($scope.displayPlanTab){
                        treatments = $scope.elementsSelected[j].treatmentsPlan;
                    }
                    if(treatments!==null && treatments!==undefined
                        &&treatments.length>0){
                        for(var k = 0;k<treatments.length;k++){
                            if(treatment.id===treatments[k].id){
                                $scope.drawElement($scope.elementsSelected[j].index,'RED');
                                break;
                            }
                        }
                    }
                }
            }
        };
        $scope.drawSelectedElements = function () {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                for(var i = 0;i<$scope.elementsSelected.length;i++){
                    for(var j = 0;j<$rootScope.elements.length;j++) {
                        if($rootScope.elements[j].id === $scope.elementsSelected[i].id){
                            if($scope.displayActsTab){
                                if($scope.elementsSelected[i].treatments!==null && $scope.elementsSelected[i].treatments!==undefined
                                    && $scope.elementsSelected[i].treatments.length>0){
                                    $scope.drawElement(j,'GREEN');
                                }else{
                                    $scope.drawElement(j,'');
                                }
                            }else if($scope.displayPlanTab){
                                if($scope.elementsSelected[i].treatmentsPlan!==null && $scope.elementsSelected[i].treatmentsPlan!==undefined
                                    && $scope.elementsSelected[i].treatmentsPlan.length>0){
                                    if($scope.elementsSelected[i].treatments!==null && $scope.elementsSelected[i].treatments!==undefined
                                        && $scope.elementsSelected[i].treatments.length>0){
                                        $scope.drawElement(j,'ORANGE');
                                    }else{
                                        $scope.drawElement(j,'YELLOW');
                                    }
                                }else if($scope.elementsSelected[i].treatments!==null && $scope.elementsSelected[i].treatments!==undefined
                                    && $scope.elementsSelected[i].treatments.length>0){
                                    $scope.drawElement(j,'GREEN');
                                }
                            }
                            $scope.elementsSelected[i].index = j;
                        }
                    }
                }
            }
        };

        $scope.drawElement = function (index, color) {
            var img = color==='GREEN'?$rootScope.elements[index].img_green:
                (color==='BLUE'?$rootScope.elements[index].img_blue:
                    (color==='RED'?$rootScope.elements[index].img_red:
                        (color==='YELLOW'?$rootScope.elements[index].img_yellow:
                            (color==='ORANGE'?$rootScope.elements[index].img_orange:$rootScope.elements[index].img))));
            $scope.ctx.rotate($rootScope.elements[index].zRotation*$scope.TO_RADIANS);
            $scope.ctx.drawImage(img,$rootScope.elements[index].xDraw,$rootScope.elements[index].yDraw,
                img.width*$rootScope.elements[index].widthDraw,
                img.height*$rootScope.elements[index].heightDraw);
            $scope.ctx.restore();
            $scope.ctx.save();
        }


        $scope.createSelectedElements = function () {
            $scope.elementsSelected = null;
            var displayPlanTab = $scope.displayPlanTab;
            var displayActsTab = $scope.displayActsTab;
            if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                && $scope.treatmentsAll.length>0){
                var length = $scope.treatmentsAll.length;
                $scope.elementsSelected =[];
                $scope.displayPlanTab=false;
                $scope.displayActsTab=true;
                for(var i = 0;i<length;i++){
                    if($scope.treatmentsAll[i].id!==-1){
                        $scope.addElements($scope.treatmentsAll[i]);
                    }
                }
            }
            if($scope.treatmentsPlanAll!==null && $scope.treatmentsPlanAll!==undefined
                && $scope.treatmentsPlanAll.length>0){
                var length = $scope.treatmentsPlanAll.length;
                if($scope.treatmentsAll===null || $scope.treatmentsAll===undefined
                    || $scope.treatmentsAll.length===0){
                    $scope.elementsSelected =[];
                }
                $scope.displayPlanTab=true;
                $scope.displayActsTab=false;
                for(var i = 0;i<length;i++){
                    if($scope.treatmentsPlanAll[i].id!==-1){
                        $scope.addElements($scope.treatmentsPlanAll[i]);
                    }
                }
            }
            $scope.displayPlanTab = displayPlanTab;
            $scope.displayActsTab = displayActsTab;
            if($scope.elementsSelected !==null){
                $scope.drawSelectedElements();
            }
        };

        $scope.clearTreatmentElements = function (treatment) {
            if(treatment.elements!==null && treatment.elements!==undefined
                && treatment.elements.length>0){
                var split = treatment.elements.split(',');
                for(var i = 0;i<split.length;i++){
                    for(var j = 0;j<$scope.elementsSelected.length;j++){
                        if($scope.elementsSelected[j].id===parseInt(split[i], 10)){
                            var treatments = null;
                            if($scope.displayActsTab){
                                treatments = $scope.elementsSelected[j].treatments;
                            }else if($scope.displayPlanTab){
                                treatments = $scope.elementsSelected[j].treatmentsPlan;
                            }
                            if(treatments.length==1){
                                for(var k = 0;k<$rootScope.elements.length;k++) {
                                    if($rootScope.elements[k].id === $scope.elementsSelected[j].id){
                                        $scope.drawElement(k,'');
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    };
