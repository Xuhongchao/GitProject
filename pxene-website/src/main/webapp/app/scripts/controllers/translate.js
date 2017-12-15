define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.translateCtrl', [])
        .controller('translateCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            var app_status="";
            var sys="";
            if(sys == "Android" || sys == "IOS"){
                sys = sys=="Android"?2:3;
            }
            if(sys == "pc" || sys == "mobile"){
                sys = sys=="pc"?4:5;
            }
            var childselect= "";            //选择的待转移的app

            //搜索转移的应用的数据来源
            var url = "/appCrawlerWait/queryTransferAppByName.do"
            gridService.httpService(url).success(function (data) {
                $scope.transferApplist = data.list;
                $scope.transferApplist.push({
                    "app_id": -1,
                    "crawl_app_name": "没有匹配项",
                })
            })

            //一级分类数据来源
            var url = "/appCrawlerWait/queryAppParentCategory.do"
            gridService.httpService(url).success(function (data) {
                $scope.transferFirstTypelist = data.appParentCategoryList;
            })


            //一级分类发生改变，就从新查找二级分类数据
            $scope.queryfirstClassify = function(obj){
                $scope.transferSecondType="";       //一级分类做切换，二级分类清空重新选择
                var param = {
                    category_id: obj.category_id
                }
                var url = "/appCrawlerWait/queryAppChildCategory.do"
                gridService.httpService(url, param).success(function (data) {
                    $scope.transferSecondTypelist = data.appChildCategoryList;
                })
            }

            $scope.queryappclassify = function(obj){
                var param = {
                    app_id: obj.app_id
                }
                var url = "/appCrawlerWait/queryAppCategoryByAppId.do"
                gridService.httpService(url, param).success(function (data) {
                    $scope.transferFirstType = undefined;
                    $scope.transferSecondType = undefined;
                    for(var i=0;i<data.appCategory.length;i++){
                        $scope.transferFirstType  = {category_name:data.appCategory[i].parent_category_name}
                        $scope.transferSecondType = {category_name:data.appCategory[i].child_category_name}
                    }
                })
            }

            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        app_id:objselect.app_id?objselect.app_id:undefined,
                        searchId:$scope.transferApp.app_id?$scope.transferApp.app_id:-1,
                        category_id:$scope.transferSecondType.category_id?$scope.transferSecondType.category_id:undefined,
                    }
                    $scope.firstsearch = true;
                    var url="/appCrawlerWait/appTransfer.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        $scope.firstsearch = false;
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("转移成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("转移失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该名称所在的系统已经转移,转移失败","info");
                            return;
                        }
                        if(data.resultCode == "3"){
                            gridService.xcConfirm("该名称为空,无法转移","info");
                            return;
                        }
                        if(data.resultCode == "4"){
                            gridService.xcConfirm("该名称已存在,无法转移","info");
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
