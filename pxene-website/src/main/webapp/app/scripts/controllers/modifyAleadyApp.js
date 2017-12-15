define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyAleadyAppCtrl', [])
        .controller('modifyAleadyAppCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            $scope.version = objselect.crawl_app_version;
            $scope.transferFirstType = objselect.parent_category;
            $scope.transferSecondType = objselect.child_category;

            //一级分类数据来源
            var url = "/appCrawlerWait/queryAppParentCategory.do"
            gridService.httpService(url).success(function (data) {
                $scope.transferFirstTypelist = data.appParentCategoryList;
                for(var i=0;i<$scope.transferFirstTypelist.length;i++){
                    if($scope.transferFirstTypelist[i].category_name == $scope.transferFirstType){
                        $scope.transferFirstType=$scope.transferFirstTypelist[i];
                    }
                }

                //二级分类数据来源
                var url = "/appCrawlerWait/queryAppChildCategory.do"
                var param = {
                    category_id: $scope.transferFirstType.category_id
                }
                gridService.httpService(url,param).success(function (data) {
                    $scope.transferSecondTypelist = data.appChildCategoryList;
                    for(var i=0;i<$scope.transferSecondTypelist.length;i++){
                        if($scope.transferSecondTypelist[i].category_name == $scope.transferSecondType){
                            $scope.transferSecondType=$scope.transferSecondTypelist[i];
                        }
                    }
                })
            })

            /*//二级分类数据来源
            var url = "/appCrawlerWait/queryAppChildCategory.do"
            var param = {
                category_id: null
            }
            gridService.httpService(url,param).success(function (data) {
                $scope.transferSecondTypelist = data.appChildCategoryList;
                for(var i=0;i<$scope.transferSecondTypelist.length;i++){
                    if($scope.transferSecondTypelist[i].category_name == $scope.transferSecondType){
                        $scope.transferSecondType=$scope.transferSecondTypelist[i];
                    }
                }
            })*/

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
            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        crawl_id:objselect.crawl_id?objselect.crawl_id:undefined,
                        crawl_app_version: $scope.version?$scope.version:undefined,
                        crawl_app_category_id:$scope.transferSecondType?$scope.transferSecondType.category_id:undefined,
                    }
                    var url="/appCrawlerDone/modifyAppCrawlerDone.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("编辑成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("编辑失败","info");
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
