define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifyAppClassifyCtrl', [])
        .controller('modifyAppClassifyCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            $scope.firstClassify = objselect.parent_category;
            $scope.secondClassify = objselect.child_category;
            $scope.childCategoryId = objselect.child_category_id;

            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        child_category_id:$scope.childCategoryId?$scope.childCategoryId:undefined,
                    }
                    var url="/appCategory/queryChildCategoryIsUsed.do";
                    gridService.httpService(url,param).success(function (data) {
                                    $uibModalInstance.dismiss('cancel');
                                    if(data.resultCode == "3") {
                                        gridService.xcConfirm("该分类下有关联的APP，" + '<br />' + "修改后关联APP的分类也会变更，是否仍要编辑该分类?", "warning", {
                                            onOk: function (v) {
                                                    var param = {
                                                        child_category_id: $scope.childCategoryId ? $scope.childCategoryId : undefined,
                                                        child_category:$scope.secondClassify?$scope.secondClassify:undefined,
                                                    }
                                                    var url = "/appCategory/modifyChildCategory.do";
                                                    gridService.httpService(url, param).success(function (data) {
                                                        $uibModalInstance.dismiss('cancel');
                                                        if (data.resultCode == "0") {
                                                            gridService.xcConfirm("编辑成功", "info");
                                                            getPage();
                                                        }
                                                        if (data.resultCode == "1") {
                                                            gridService.xcConfirm("编辑失败", "info");
                                                            return;
                                                        }
                                                        if (data.resultCode == "2") {
                                                            gridService.xcConfirm("二级分类已存在", "info");
                                                            return;
                                                        }
                                                    })
                                            }, onCancel: function (v) {

                                            }
                                        })
                                    }
                                    if(data.resultCode == "4"){
                                        var param={
                                            child_category_id:$scope.childCategoryId?$scope.childCategoryId:undefined,
                                            child_category:$scope.secondClassify?$scope.secondClassify:undefined,
                                        }
                                        var url="/appCategory/modifyChildCategory.do";
                                        gridService.httpService(url,param).success(function (data) {
                                            $uibModalInstance.dismiss('cancel');
                                            if(data.resultCode == "0"){
                                                gridService.xcConfirm("编辑成功","info");
                                                getPage();
                                            }
                                            if(data.resultCode == "1"){
                                                gridService.xcConfirm("编辑失败","info");
                                                return;
                                            }
                                            if(data.resultCode == "2"){
                                                gridService.xcConfirm("二级分类已存在","info");
                                                return;
                                            }
                                        })
                                    }
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
