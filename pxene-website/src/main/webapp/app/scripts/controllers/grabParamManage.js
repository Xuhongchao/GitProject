define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.grabParamManageCtrl', [])
        .controller('grabParamManageCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService','remberHistory', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService,remberHistory) {


            gridService.setScope($scope);//初始化表格配置
            //添加分类
            $scope.add = function () {
                //默认uibModal参数为url和controller
                var modalInstance = gridService.uibModal('addParamType.html','addParamTypeCtrl')
            };
            //删除
            $scope.delete = function (row) {
                var param={content_id:row.content_id,industry_num:row.industry_num}
                var url="/appIndustry/deleteIndustry.do";
                gridService.xcConfirm(row.content_name,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                $scope.search();
                            }else if(data.resultCode == "1"){
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }else{
                                gridService.xcConfirm("参数详情中存在内容,无法删除","info")
                                return;
                            }
                        })
                    }
                });
            }

            //保存表格历史记录，获取有无历史记录
            var d=remberHistory.get();          //保存表格历史记录

            //详情页
            $scope.detail = function (obj) {
                // 页面恢复数据，在跳转时保存当前的数据
                var history={
                    url:$location.path(),
                    pageNumber:$scope.paginationOptions.pageNumber,
                    pageSize:$scope.paginationOptions.pageSize,
                    typeNumber:$scope.typeNumber,
                    typeName:$scope.typeName,
                };
                remberHistory.save(history);             //保存表格历史记录

                $location.url("/detailParam?id="+obj.content_id+"&name="+obj.content_name);
            }

            var columnDefs=[
                { name:'参数分类编号', field: 'industry_num',suppressRemoveSort: true,},
                { name:'参数分类', field: 'industry_name'},
                { name:'参数', field: 'content_name'},
                { name:'参数顺序', field: 'content_order'},
                { name:'正则个数', field: 'regCount'},
                { name:'操作', field: 'handle',
                    cellTemplate:'<div class="gridtable detandTran">' +
                    '<button class="btn btn-default" type="button" ng-click="grid.appScope.detail(row.entity)">详情</button>' +
                    '<button class="btn btn-primary ml20" type="button" ng-click="grid.appScope.delete(row.entity)">删除</button></div>'},
            ]
            $scope.getPage = function (refush) {
                if(refush){
                    gridService.jqLoading("destroy")
                }
                gridService.jqLoading()
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    industryNum:$scope.typeNumber && refush?$scope.typeNumber:null,
                    industryName:$scope.typeName && refush?$scope.typeName:undefined,
                    orderRule:$scope.orderRule?$scope.orderRule:'desc',
                    orderName:$scope.orderName?$scope.orderName:undefined,
                }
                var url="/appIndustry/queryByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {

                    //表格叠加出来后进行页码跳转
                    if(d !== ""){                                                       //保存表格历史记录
                        remberHistory.rever(d);
                    }

                    gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    gridService.jqLoading("destroy")
                })
            }
            // $scope.getPage();

            // gridService.startRemverHistory(d);
            // 恢复页面的历史数据
            $scope.$watch('$stateChangeSuccess',function (e) {
                if(d !== ""){
                    $scope.paginationOptions.pageNumber = d.pageNumber;
                    $scope.paginationOptions.pageSize = d.pageSize;
                    $scope.typeNumber=d.typeNumber;
                    $scope.typeName=d.typeName;
                    $scope.getPage("refush");
                }else{
                    $scope.getPage();
                }
            })

        }])
});

