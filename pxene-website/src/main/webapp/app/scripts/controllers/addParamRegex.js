define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addParamRegexCtrl', [])
        .controller('addParamRegexCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService) {
            var $ctrl=this;
            var id=$location.search().id;
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        content_id:id,
                        param_reg:$scope.paramRegex?$scope.paramRegex:undefined,
                        url_example:$scope.urlExample?$scope.urlExample:undefined,
                    }
                    $scope.aleadysure = true;
                    var url="/appIndustry/addRegular.do";
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
                            gridService.xcConfirm("该参数正则已存在,无法添加","info");
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
