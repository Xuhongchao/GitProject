define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyParamRegexCtrl', [])
        .controller('modifyParamRegexCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            var urlExample=objselect.url_example;
            var paramRegex=objselect.param_reg;
            $scope.urlExample = objselect.url_example;
            $scope.paramRegex = objselect.param_reg;
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var check_state=undefined;
                    if(urlExample !== $scope.urlExample || paramRegex !== $scope.paramRegex){
                        check_state=0;
                    }
                    var param={
                        content_id:objselect.content_id,
                        content_detail_id:objselect.content_detail_id,
                        url_example:$scope.urlExample?$scope.urlExample:undefined,
                        param_reg:$scope.paramRegex?$scope.paramRegex:undefined,
                        check_state:check_state,
                    }
                    var url="/appIndustry/modifyRegular.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("修改成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("修改失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该参数正则已存在,修改失败","info");
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
