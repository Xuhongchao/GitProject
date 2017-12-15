define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.addAppClassifyCtrl', [])
        .controller('addAppClassifyCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,validateService) {
            var $ctrl=this;

            // 一级二级分类交互
            $scope.firstClassifyList=[];
            $scope.firstClassifyList.showflag = true;
            //绑定点击空白的时候搜索内容下拉框消失
            $(window).bind('click',function(e){
                var $div1 = $('input.form-control');    //输入框
                var $target = $(e.target);      //点击的对象
                if($target.attr('class') == $div1.attr('class')){       //如果点击的对象为输入框就不隐藏  否则就隐藏下拉框
                    $scope.$apply(function(){
                        $scope.firstClassifyList.showflag = true;
                    })
                }else{
                    $scope.$apply(function(){
                        $scope.firstClassifyList.showflag = false;
                    })
                }
            })
            // 选择模糊查找出后的数据对象
            $scope.choiceobj = function (obj) {
                $scope.firstClassify = obj.category_name;
                $scope.firstClassifyList.showflag = false;
            }

            $scope.$watch('firstClassify',function(newvalue,oldvalue){
                if(oldvalue !== newvalue){
                    if(!$scope.firstClassifyList.showflag){
                        $scope.firstClassifyList.showflag = true;
                        return;
                    }
                        $scope.searchFirstClassify();
                }
            })
            //在一级分类里输入内容后模糊查找
            $scope.searchFirstClassify = function (e) {
                var param = {
                    category_name: $scope.firstClassify ? $scope.firstClassify : undefined,
                }
                var url = "/appCategory/queryFuzzyParentCategory.do";
                gridService.httpService(url, param).success(function (data) {
                    $scope.firstClassifyList = data.parentCategoryList;
                    if (data.parentCategoryList.length > 0) {
                        $scope.firstClassifyList.showflag = true;
                    } else {
                        $scope.firstClassifyList.showflag = false;
                    }
                })
            }
            //监测一级分类为空的时候让二级分类清空
            $scope.$watch('firstClassify',function(newvalue,oldvalue){
                if(oldvalue !== newvalue && newvalue == ""){
                    $scope.secondClassify = "";
                }
            })


            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    var param={
                        parentCategory:$scope.firstClassify?$scope.firstClassify:undefined,
                        childCategory:$scope.secondClassify?$scope.secondClassify:undefined,
                    }
                    // $scope.aleadysure = true;
                    var url="/appCategory/addCategory.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        // $scope.aleadysure = false;
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
