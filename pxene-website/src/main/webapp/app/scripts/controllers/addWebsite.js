define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addWebsiteCtrl', [])
        .controller('addWebsiteCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService','appsourcequery','sys', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService,appsourcequery,sys) {
            var $ctrl=this;
            var sys=sys=="pc"?4:5;
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    gridService.jqLoading()
                    var param={
                        app_type:sys,
                        app_name:$scope.websiteName?$scope.websiteName:undefined,
                        app_source:$scope.source?$scope.source:undefined,
                    }
                    $scope.aleadysure=true;
                    var url="/appCrawlerWait/addAppOrWeb.do";
                    gridService.httpService(url,param).success(function (data) {
                        gridService.jqLoading("destroy")
                        $uibModalInstance.dismiss('cancel');
                        $scope.aleadysure=false;
                        appsourcequery();
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("添加成功","info");
                            /*var param={
                                app_id:data.appId,
                                appList:data.appList,
                            }
                            gridService.httpService("/appCrawlerWait/crawlApp.do",param).success(function (data) {
                                if(data.crawlState == "done"){
                                    getPage();
                                }
                            })*/
                            // getPage();
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("添加失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该网站已存在无法添加","info");
                            return;
                        }
                        if(data.resultCode == "3"){
                            gridService.xcConfirm("该网站已转移无法添加","info");
                            return;
                        }
                        if(data.resultCode == "4"){
                            gridService.xcConfirm("该网站已存在，已为其更新优先级和来源","info");
                        }
                        getPage();
                    })
                }else{
                    var errqueue = validateService.getQueue();
                    gridService.xcConfirm(errqueue[0].info,"info");
                    return;
                }
            }
            $ctrl.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }])
});
