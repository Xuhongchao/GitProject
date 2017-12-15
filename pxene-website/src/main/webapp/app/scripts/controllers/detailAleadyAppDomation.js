define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.detailAleadyAppDomationCtrl', [])
        .controller('detailAleadyAppDomationCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService) {
            $scope.craw_id=$location.search().id;
            $scope.app_id=$location.search().app_id;
            $scope.appname=$location.search().appname;
            $scope.apptype=$location.search().apptype;
            $scope.logourl=$location.search().crawl_app_logo_url;
            $scope.crawl_app_category_id_str=$location.search().crawl_app_category_id_str;
            $scope.appnumber=$location.search().appnumber;
            $scope.sys=$location.search().sys;

            gridService.setScope($scope);

            /*$scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(odd) .ui-grid-cell").addClass("specilh69")
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(even) .ui-grid-cell").addClass("specilh69")
                $(".ui-grid-pinned-container-left>.ui-grid-render-container-left>.ui-grid-viewport").addClass("specilh69")
            })*/

            //主域名和url详情切换
            $scope.checkSys = function(param){
                if(param == "domation"){
                    $location.url("/detailAleadyAppDomation?appname="+$scope.appname+"&apptype="+$scope.apptype+"&id="+ $scope.craw_id+"&app_id="+$scope.app_id+"&crawl_app_category_id_str="+$scope.crawl_app_category_id_str+
                        "&appnumber="+$scope.appnumber+"&sys="+$scope.sys+"&crawl_app_logo_url="+$scope.logourl)
                }
                if(param == "urldetail"){
                    $location.url("/detailAleadyApp?appname="+$scope.appname+"&apptype="+$scope.apptype+"&id="+ $scope.craw_id+"&app_id="+$scope.app_id+"&crawl_app_category_id_str="+$scope.crawl_app_category_id_str+
                        "&appnumber="+$scope.appnumber+"&sys="+$scope.sys+"&crawl_app_logo_url="+$scope.logourl)
                }
            }

            //删除
            $scope.delete = function (row) {
                var param={domain_id:row.domain_id}
                var url="/appCrawlerDone/delDomain.do";
                gridService.xcConfirm(row.domain_code,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                $scope.getPage();
                            }else if(data.resultCode == "1"){
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }
                        })
                    }
                });
            }

            //修改
            $scope.modify = function (row) {
                var obj = row;
                obj.appname=$scope.appname;
                obj.appnumber=$scope.appnumber;
                gridService.uibModal('modifyAleadyAppDomation.html','modifyAleadyAppDomationCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            };

            //添加
            $scope.add = function () {
                var obj = {};
                obj.appname=$scope.appname;
                obj.appnumber=$scope.appnumber;
                var modalInstance = gridService.uibModal('addAleadyAppDomation.html','addAleadyAppDomationCtrl',{
                    objselect: function () {
                        return obj
                    }
                })
            }

            //v2.1版
            var columnDefs=[
                { name:'编号', field: 'domain_code'},
                { name:'域名', field: 'domain_domain'},
                { name:'附加参数', field: 'domain_attach_param'},
                { name:'状态', field: 'domain_isexport',cellFilter:'domationcheckStatus'},
                { name:'操作', field: 'handle',
                    cellTemplate:'<div class="gridtable">' +
                    '<button type="button" class="btn btn-default" ng-click="grid.appScope.modify(row.entity)">修改</button> ' +
                    '<button type="button" class="btn btn-primary" ng-click="grid.appScope.delete(row.entity)">删除</button> '
                },
            ]
            $scope.getPage = function (refush) {
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    crawl_app_num:$scope.appnumber,
                }
                var url="/appCrawlerDone/queryDomainByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {
                    gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }
            $scope.getPage();

            $scope.regexJudge = function () {
                var pass = [];
                var noPass = [];
                var noflag=false;
                var passflag=false;
                var regularArray=[];
                var regjudge = [];
                var selectedObject = $scope.gridApi.selection.getSelectedRows();
                if (selectedObject.length > 0) {
                    try{
                        for(var i=0;i<selectedObject.length;i++) {
                            var returnObj = [];
                            var paramRegex = selectedObject[i].crawl_detail_paramreg;
                            var urlRegex = selectedObject[i].crawl_detail_urlreg;
                            var url =escape(selectedObject[i].crawl_detail_urlexample);
                            var domain = selectedObject[i].crawl_detail_domain;
                            url=unescape(unescape(unescape(unescape(url))))
                            if(paramRegex){
                                var paramRegexs = paramRegex.split("\t");
                                if (selectedObject[i].crawl_detail_num.substring(1,5) == "0055") {
                                    angular.forEach(paramRegexs, function (value, index) {
                                        if (index == 0) {

                                            if (value != "NULL") {
                                                var regex = new RegExp(value, "g");
                                                if (!regex.test(url)) {
                                                    returnObj.push({"paramRegex": value});      //不通过状态
                                                    noflag=true;
                                                }else{
                                                    passflag=true;
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    angular.forEach(paramRegexs, function (value, key) {
                                        if (value != "NULL") {

                                            var regex = new RegExp(value, "g");
                                            if (!regex.test(url)) {
                                                returnObj.push({"paramRegex": value});      //不通过状态
                                                noflag=true;
                                            }else{
                                                passflag=true;
                                            }
                                        }
                                    });
                                }
                            }
                            // var paramRegexs = paramRegex.split("\t");
                            var str=domain+urlRegex;
                            var regex = new RegExp(str, "g");
                            if (!regex.test(url)) {
                                returnObj.push({"urlRegex": urlRegex});     //不通过状态
                                noflag=true;
                            }else{
                                passflag=true;
                            }

                            if(noflag){
                                noPass.push(selectedObject[i])
                                noflag=false;
                                regjudge.push({crawl_detail_id:selectedObject[i].crawl_detail_id,crawl_detail_status:2})
                            }else{
                                pass.push(selectedObject[i])
                                passflag=false;
                                regjudge.push({crawl_detail_id:selectedObject[i].crawl_detail_id,crawl_detail_status:1})
                            }
                        }
                    }
                    catch(err)
                    {
                        if(err.message.match(/\/\S+\//g)){
                            if(err.message.split(":")[0] == "Invalid regular expression"){
                                var err_reg = err.message.match(/\/\S+\//g)[0].substr(1,err.message.match(/\/\S+\//g)[0].length-2)
                                // var errorexp = [];
                                // for(var i=0;i<selectedObject.length;i++){
                                //     if(selectedObject[i].param_reg == err_reg){
                                //         errorexp=selectedObject[i]
                                //         break;
                                //     }
                                // }
                                // var txt="<div class='err-regex'>URL样例："+ errorexp.url_example +"<br />"+
                                var txt="<div class='err-regex'>正则非法："+ err_reg +"<br />"+
                                    "错误描述：" + err.message + ""+
                                    "<span>点击确定继续。</span></div>"
                                gridService.xcConfirm(txt,"info")
                            }
                        }
                        return;
                    }
                }else {
                    gridService.xcConfirm("请先选择要校验的抓取信息","info")
                    return;
                }
                gridService.xcConfirm("<span style='color:#FF7E33'>"+pass.length+"&nbsp;</span>"+"个校验成功 ,"+ "<span style='color:#FF7E33'>"+noPass.length+"&nbsp;</span>"+"个校验失败","info")
                $scope.gridApi.selection.clearSelectedRows();

                var param={
                    regList:regjudge
                }
                var url="/appCrawlerDone/checkReg.do";
                gridService.httpService(url,param).success(function (data) {
                    $scope.getPage();
                })
            }

        }])
});
