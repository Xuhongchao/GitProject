define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.grabAppClassifyCtrl', [])
        .controller('grabAppClassifyCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService) {
            gridService.setScope($scope);//初始化表格配置
            $scope.analysis = function (row) {
                var result="";
                if(row.length>0){
                    for(var i=0;i<row.length;i++){
                        result = result+ row[i].keyword_name+"   ";
                    }
                }
                return result
            }

            // v2.1版本
            //添加分类
            $scope.add = function () {
                //默认uibModal参数为url和controller
                var modalInstance = gridService.uibModal('addAppClassify.html','addAppClassifyCtrl')
            };
            //编辑分类
            $scope.modify = function (row) {
                var obj = row;
                gridService.uibModal('modifyAppClassify.html','modifyAppClassifyCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            };
            //删除分类
            $scope.delete = function (row) {
                var param={child_category_id:row.child_category_id}
                var url="/appCategory/delChildCategory.do";
                gridService.xcConfirm(row.child_category,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                $scope.search();
                            }else if(data.resultCode == "1"){
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }else{
                                gridService.xcConfirm("该APP分类下有关联的APP，无法删除该APP分类。","info")
                                return;
                            }
                        })
                    }
                });
            }

            var columnDefs=[
                { name:'一级分类', field: 'parent_category'},
                { name:'二级分类', field: 'child_category'},
                { name:'操作', field: 'handle',width:200,
                    cellTemplate:'<div class="gridtable pt8">' +
                    '<button class="btn btn-default" type="button" ng-click="grid.appScope.modify(row.entity)">编辑</button>' +
                    '<button class="btn btn-primary ml20" type="button" ng-click="grid.appScope.delete(row.entity)">删除</button></div>'},
            ]

            $scope.getPage = function (refush) {
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    category_name:$scope.typeName && refush?$scope.typeName:undefined,
                    orderRule:$scope.orderRule?$scope.orderRule:'desc',
                    orderName:$scope.orderName?$scope.orderName:undefined,
                }
                var url="/appCategory/queryByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {
                    gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }
            $scope.getPage();
        }])
});
