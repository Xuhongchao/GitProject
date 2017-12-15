define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addAleadyAppDomationCtrl', [])
        .controller('addAleadyAppDomationCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService','objselect', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService,objselect) {
            var $ctrl=this;
            var appname=objselect.appname;
            var appnumber=objselect.appnumber;
            //v2.1
            //使用端
            var firstparam = {
                crawl_app_num: appnumber
            }
            var url = "/appCrawlerDone/queryTransferType.do"
            gridService.httpService(url, firstparam).success(function (data) {
                $scope.userpointlists = data.list;
            })


           /* //一级分类发生改变，就从新查找二级分类数据
            $scope.queryfirstClassify = function(obj){
                $scope.transferSecondType="";       //一级分类做切换，二级分类清空重新选择
                var secondparam = {
                    crawl_app_category_id_str: "21,15,10"
                }
                var url = "/appCrawlerDone/queryAppCrawlBehavior.do"
                gridService.httpService(url, secondparam).success(function (data) {
                    $scope.transferSecondTypelist = data.behaviorList;
                })
            }*/


            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        type_num:$scope.userpoint.type_num,
                        crawl_app_num:appnumber,
                        crawl_app_domain:$scope.maindomain?$scope.maindomain:undefined,
                        crawl_app_attach_param:$scope.additionParameters?$scope.additionParameters:undefined,
                    }
                    var url="/appCrawlerDone/addDomain.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("添加成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("添加失败","info");
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
