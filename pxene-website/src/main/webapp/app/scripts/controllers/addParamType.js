define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addParamTypeCtrl', [])
        .controller('addParamTypeCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService) {
            var $ctrl=this;
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        industry_name:$scope.paramClassify?$scope.paramClassify:undefined,
                        content_name:$scope.param?$scope.param:undefined,
                        content_order:$scope.paramOrder?$scope.paramOrder:undefined,
                    }
                    $scope.aleadysure = true;
                    var url="/appIndustry/addIndustry.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        $scope.aleadysure = false;
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("添加成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("添加失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该参数分类下的参数已存在，添加参数失败","info");
                            return;
                        }
                        if(data.resultCode == "3"){
                            gridService.xcConfirm("该参数顺序已存在，添加参数失败","info");
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
