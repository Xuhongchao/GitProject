define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyAleadyAppDomationCtrl', [])
        .controller('modifyAleadyAppDomationCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            var appname=objselect.appname;
            var appnumber=objselect.appnumber;

            var userpoint = objselect.domain_code.substring(0,1);
            $scope.maindomain = objselect.domain_domain;
            $scope.maindomainid = objselect.domain_id;
            $scope.additionParameters = objselect.domain_attach_param;

            //使用端
            var param = {
                crawl_app_num: appnumber
            }
            var url = "/appCrawlerDone/queryTransferType.do"
            gridService.httpService(url, param).success(function (data) {
                $scope.userpointlists = data.list;
                if($scope.userpointlists.length>0){
                    for(var i=0;i<$scope.userpointlists.length;i++){
                       if($scope.userpointlists[i].type_num == userpoint){
                           $scope.userpoint=$scope.userpointlists[i];
                       }
                    }
                }
            })

            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        domain_id:$scope.maindomainid,
                        crawl_app_num:appnumber,
                        crawl_app_domain:$scope.maindomain,
                        crawl_app_attach_param:$scope.additionParameters,
                        type_num:$scope.userpoint.type_num,
                    }
                    var url="/appCrawlerDone/modifyDomain.do";
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
                        if(data.resultCode == "3"){
                            gridService.xcConfirm("该主域名对应的端已存在，请重新输入！","info");
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
