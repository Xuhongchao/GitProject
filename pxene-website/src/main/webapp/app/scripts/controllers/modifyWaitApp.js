define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyWaitAppCtrl', [])
        .controller('modifyWaitAppCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            $scope.appName = objselect.app_name;
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        app_id:objselect.app_id?objselect.app_id:undefined,
                        app_name:$scope.appName?$scope.appName:undefined,
                    }
                    var url="/appCrawlerWait/modifyWaitCrawlAppName.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("编辑成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("编辑失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该主域名和附加参数已存在，请重新输入！","info");
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
