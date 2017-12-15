define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addBehaviorCtrl', [])
        .controller('addBehaviorCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService) {
            var $ctrl=this;
            var selectApp=[];         //选择的APP分类名称
            var appclassifys = []      //查询出来的app分类数据
            $ctrl.appclassifys = []

            // var apptypeparam={
            //     paramRegex:var paramRegex?var paramRegex:undefined,
            //     urlExample:$scope.grabBehavior?$scope.grabBehavior:undefined,
            // }
            var apptypeurl="/appBehavior/queryAppCategory.do";
            gridService.httpService(apptypeurl).success(function (data) {
                 appclassifys=data.appCategoryList
                 appclassifys.unshift({category_id:-1,category_name:"全部"})
                 $ctrl.appclassifys = appclassifys
            })



            $ctrl.clickAppClassify = function (obj) {
                selectApp=[];
                if(obj.category_name=="全部"){
                    if(!obj.isSelected){
                        for(var i=1;i< appclassifys.length;i++){
                             appclassifys[i].isSelected = false;
                        }
                    }else{
                        for(var i=1;i< appclassifys.length;i++){
                             appclassifys[i].isSelected = true;
                             selectApp.push( appclassifys[i].category_id)
                        }
                    }
                }else{
                    var noselectAll=false;
                    for(var i=1;i< appclassifys.length;i++){
                        if(! appclassifys[i].isSelected){
                            noselectAll=true;
                            break;
                        }else{
                            noselectAll=false;
                        }

                    }
                    if(noselectAll){
                         appclassifys[0].isSelected = false;
                        for(var i=1;i< appclassifys.length;i++){
                            if( appclassifys[i].isSelected){
                                 selectApp.push( appclassifys[i].category_id)
                            }
                        }
                    }else{
                         appclassifys[0].isSelected = true;
                        for(var i=1;i< appclassifys.length;i++){
                             selectApp.push( appclassifys[i].category_id)
                        }
                    }
                }
            }

            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    if(selectApp.length == 0){
                        gridService.xcConfirm("app分类名称 必须选择！","info");
                        return;
                    }
                    var param={
                        app_category_id_list:selectApp?selectApp:undefined,
                        behavior_name:$scope.grabBehavior?$scope.grabBehavior:undefined,
                    }
                    $scope.aleadysure=true;
                    var url="/appBehavior/addBehavior.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        $scope.aleadysure=false;
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("添加成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("添加失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该行为对应的App分类已存在,无法添加","info");
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
