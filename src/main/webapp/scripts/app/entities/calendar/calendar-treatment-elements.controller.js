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
                                            console.log('$scope.elementsSelected[j].treatments.length ' + $scope.elementsSelected[j].treatments.length);
                                            for (var k = 0; k < $scope.elementsSelected[j].treatments.length; k++) {
                                                console.log('$scope.elementsSelected[j].treatments[k].id ' + $scope.elementsSelected[j].treatments[k].id);
                                                if ($scope.treatment.id === $scope.elementsSelected[j].treatments[k].id) {
                                                    containTreatment = true;
                                                    treatmentIndex = k;
                                                    if ($scope.elementsSelected[j].treatments.length >= 2) {
                                                        $scope.drawElement($scope.elementsSelected[j].index, 'GREEN');
                                                    } else {
                                                        $scope.drawElement($scope.elementsSelected[j].index, '');
                                                    }
                                                    break;
                                                }
                                            }
                                            if (containTreatment && $scope.elementsSelected[j].treatments.length === 1) {
                                                deleteSelectedElement = j;
                                            }
                                            if(treatmentIndex>-1){
                                                $scope.elementsSelected[j].treatments.splice(treatmentIndex, 1);
                                            }
                                            if(!containTreatment){
                                                $scope.elementsSelected[j].treatments.push($scope.treatment);
                                                $scope.drawElement($scope.elementsSelected[j].index, 'RED');
                                            }
                                            containElement = true;
                                            break;
                                        }
                                    }
                                    if (deleteSelectedElement > -1) {
                                        $scope.elementsSelected.splice(deleteSelectedElement, 1);
                                    }
                                    if (!containElement) {
                                        $scope.elementsSelected.push({
                                            id: $rootScope.elements[i].id,
                                            treatments: [$scope.treatment],
                                            index: i
                                        });
                                        $scope.drawElement(i, 'RED');
                                    }
                                } else {
                                    $scope.elementsSelected.push({
                                        id: $rootScope.elements[i].id,
                                        treatments: [$scope.treatment],
                                        index: i
                                    });
                                    $scope.drawElement(i, 'RED');
                                }
                                $scope.constructTemporaryElements($scope.treatment);
                            } else {
                                if ($scope.elementsSelected !== null && $scope.elementsSelected !== undefined
                                    && $scope.elementsSelected.length > 0) {
                                    for (var j = 0; j < $scope.elementsSelected.length; j++) {
                                        if ($rootScope.elements[i].id === $scope.elementsSelected[j].id) {
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
                            if($scope.elementsSelected[j].treatments!==null && $scope.elementsSelected[j].treatments!==undefined
                                && $scope.elementsSelected[j].treatments.length>0){
                                $scope.elementsSelected[j].treatments.push(treatment);
                            }else{
                                $scope.elementsSelected[j].treatments = [treatment];
                            }
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        $scope.elementsSelected.push({id:parseInt(split[i], 10),treatments:[treatment],index:null});
                    }
                }
            }
        };

        $scope.constructElementBeforeSave = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                treatment.elements=null;
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    for(var k = 0;k<$scope.elementsSelected[j].treatments.length;k++){
                        if(treatment.id===$scope.elementsSelected[j].treatments[k].id){
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
        };

        $scope.constructTemporaryElements = function (treatment) {
            $scope.temporaryElements=null;
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    for(var k = 0;k<$scope.elementsSelected[j].treatments.length;k++){
                        if(treatment.id===$scope.elementsSelected[j].treatments[k].id){
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
            $scope.$apply();
        };

        $scope.drawElementsAfterSave = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                treatment.elements=null;
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    $scope.drawElement($scope.elementsSelected[j].index,'GREEN');
                }
            }
        };

        $scope.drawElementsWhenEdit = function (treatment) {
            if($scope.elementsSelected!==null && $scope.elementsSelected!==undefined
                && $scope.elementsSelected.length>0){
                for(var j = 0;j<$scope.elementsSelected.length;j++){
                    for(var k = 0;k<$scope.elementsSelected[j].treatments.length;k++){
                        if(treatment.id===$scope.elementsSelected[j].treatments[k].id){
                            $scope.drawElement($scope.elementsSelected[j].index,'RED');
                            break;
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
                            $scope.drawElement(j,'GREEN');
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
                        (color==='ORANGE'?$rootScope.elements[index].img_orange:$rootScope.elements[index].img)));
            $scope.ctx.rotate($rootScope.elements[index].zRotation*$scope.TO_RADIANS);
            $scope.ctx.drawImage(img,$rootScope.elements[index].xDraw,$rootScope.elements[index].yDraw,
                img.width*$rootScope.elements[index].widthDraw,
                img.height*$rootScope.elements[index].heightDraw);
            $scope.ctx.restore();
            $scope.ctx.save();
        }


        $scope.createSelectedElements = function () {
            if($scope.treatmentsAll!==null && $scope.treatmentsAll!==undefined
                && $scope.treatmentsAll.length>0){
                    $scope.elementsSelected =[];
                    for(var i = 0;i<$scope.treatmentsAll.length;i++){
                        if($scope.treatmentsAll[i].id!==-1){
                            $scope.addElements($scope.treatmentsAll[i]);
                        }
                    }
                $scope.drawSelectedElements();
            }
        };

    };
