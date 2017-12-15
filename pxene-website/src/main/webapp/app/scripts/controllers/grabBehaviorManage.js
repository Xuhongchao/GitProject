define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.grabBehaviorManageCtrl', [])
        .controller('grabBehaviorManageCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService) {
            gridService.setScope($scope);//初始化表格配置
            //删除
            $scope.delete = function (row) {
                var param={behavior_id:row.behavior_id}
                var url="/appBehavior/deleteBehavior.do";
                gridService.xcConfirm(row.behavior_name,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                // var search=true;
                                $scope.search();
                            }else if(data.resultCode == "1"){
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }else{
                                gridService.xcConfirm("该行为被使用，无法删除","info")
                                return;
                            }
                        })
                    }
                });
            }
            //添加
            $scope.add = function () {
                var modalInstance = gridService.uibModal('addBehavior.html','addBehaviorCtrl')
            };

            $scope.appType = function (row) {
                var result="";
                if(row.length>0){
                    for(var i=0;i<row.length;i++){
                        result = result+ row[i].category_name+"   ";
                    }
                }
                return result

            }
            var columnDefs=[
                    { name:'行为编码', field: 'behavior_code'},
                    { name:'抓取行为', field: 'behavior_name'},
                    { name:'App分类名称', field: 'appCategoryList',
                        cellTemplate:'<div class="gridtable" title="{{grid.appScope.appType(row.entity.appCategoryList)}}">' +
                        '<span>{{grid.appScope.appType(row.entity.appCategoryList)}}</span></div>'},
                    { name:'操作', field: 'handle',
                        cellTemplate:'<div class="gridtable detandTran">' +
                        '<button class="btn btn-primary" type="button" ng-click="grid.appScope.delete(row.entity)">删除</button>'},
            ]

            $scope.getPage = function (refush) {
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    behavior_name:$scope.behaviName && refush?$scope.behaviName:undefined,
                    category_name:$scope.typeName && refush?$scope.typeName:undefined,
                }
                var url="/appBehavior/queryByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {
                    gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }
            $scope.getPage();
        }])
});
