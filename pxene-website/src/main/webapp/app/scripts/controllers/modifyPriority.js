define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyPriorityCtrl', [])
        .controller('modifyPriorityCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            var sys = objselect.sys;
            $scope.priority = objselect.app_priority;
            if(sys == "pc" || sys == "mobile"){
                $scope.prioritylist = [{name:"1"},{name:"2"}];
            }else{
                $scope.prioritylist = [{name:"1"},{name:"2"},{name:"3"}];
            }
            for(var i=0;i<$scope.prioritylist.length;i++){
                if($scope.prioritylist[i].name== $scope.priority){
                    $scope.priority = $scope.prioritylist[i];
                }
            }
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        app_id:objselect.app_id,
                        app_priority:$scope.priority.name?$scope.priority.name:undefined
                    }
                    var url="/appCrawlerWait/modifyPriority.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("修改成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("修改失败","info");
                            return;
                        }
                        getPage();
                    })
                }else{
                    var errqueue = validateService.getQueue();
                    gridService.xcConfirm(errqueue[0].info,"info");
                }
            }
            $ctrl.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }])
});
