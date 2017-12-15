define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.detailParamCtrl', [])
        .controller('detailParamCtrl',['$scope','$location','$http','$uibModal','gridService','$compile','$templateCache',function($scope,$location,$http,$uibModal,gridService,$compile,$templateCache){
            gridService.setScope($scope)
            gridService.rowHeaderSelection('checkbox')
            $scope.id= $location.search().id
            $scope.name= $location.search().name

            // remberHistory.save(history);

            var columnDefs=[
                { name:'URL样例', field: 'url_example'},
                { name:'参数正则', field: 'param_reg'},
                { name:'校验状态', field: 'check_state',cellFilter:'checkStatus',width:180},
                { name:'操作', field: 'handle',width:180,
                    cellTemplate:'<div class="gridtable detandTran">' +
                    '<button class="btn btn-default" type="button" ng-click="grid.appScope.modify(row.entity)">修改</button>' +
                    '<button class="btn btn-primary ml20" type="button" ng-click="grid.appScope.delete(row.entity)">删除</button></div>'},
            ]
            $scope.getPage = function () {
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    content_id:$scope.id?$scope.id:undefined,
                }
                var url="/appIndustry/queryContentDetail.do";
                gridService.httpService(url,param).success(function (data) {
                    gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }

            $scope.getPage();

            //添加
            $scope.addParamRegex = function () {
                var modalInstance = gridService.uibModal('addParamRegex.html','addParamRegexCtrl')
            };

            //修改
            $scope.modify = function (obj) {
                var obj = obj;
                var modalInstance = gridService.uibModal('modifyParamRegex.html','modifyParamRegexCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            }

            //删除
            $scope.delete = function (row) {
                var param={content_detail_id:row.content_detail_id}
                var url="/appIndustry/deleteRegular.do";
                gridService.xcConfirm("正则："+row.param_reg,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url, param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功", "info");
                            } else if (data.resultCode == "1") {
                                gridService.xcConfirm("删除失败", "info")
                                return;
                            } else {
                                gridService.xcConfirm("该参数正则已经被使用,无法删除!", "info")
                                return;
                            }
                            $scope.getPage();
                        })
                    }
                })
            }
            $scope.paramJudge = function () {
                var selectRow = $scope.gridApi.selection.getSelectedRows()
                var pass = [];
                var noPass = [];
                var regjudge = [];
                if (selectRow.length > 0) {
                    try
                    {
                        for(var i=0;i<selectRow.length;i++){
                            var reg=selectRow[i].param_reg;
                            var regexp = new RegExp(reg, "g");
                            var url =escape(selectRow[i].url_example);
                            url=unescape(unescape(unescape(unescape(url))))
                            if(regexp.test(url)){
                                pass.push(selectRow[i])
                                regjudge.push({content_detail_id:selectRow[i].content_detail_id,check_state:1})
                            }else{
                                noPass.push(selectRow[i])
                                regjudge.push({content_detail_id:selectRow[i].content_detail_id,check_state:2})
                            }
                        }

                    }
                    catch(err)
                    {
                        if(err.message.match(/\/\S+\//g)){
                            if(err.message.split(":")[0] == "Invalid regular expression") {
                                var err_reg = err.message.match(/\/\S+\//g)[0].substr(1,err.message.match(/\/\S+\//g)[0].length-2)
                                var txt="<div class='err-regex'>正则非法："+ err_reg +"<br />"+
                                    "错误描述：" + err.message + ""+
                                    "<span>点击确定继续。</span></div>"
                                gridService.xcConfirm(txt,"info")
                                }
                            }
                        return;
                    }
                    var param={
                        regList:regjudge
                    }
                    var url="/appIndustry/checkReg.do";
                    gridService.httpService(url,param).success(function (data) {
                        $scope.getPage();
                    })
                    gridService.xcConfirm("<span style='color:#FF7E33'>"+pass.length+"&nbsp;</span>"+"个校验成功 ,"+ "<span style='color:#FF7E33'>"+noPass.length+"&nbsp;</span>"+"个校验失败","info")
                    $scope.gridApi.selection.clearSelectedRows();
                    return;
                }else {
                    gridService.xcConfirm("请先选择要校验的样例","info")
                }
            }

        }])

});


