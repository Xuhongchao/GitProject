define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.grabAleadyAppCtrl', [])
        .controller('grabAleadyAppCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService','remberHistory', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService,remberHistory) {
            gridService.setScope($scope);//初始化表格配置
            $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(odd) .ui-grid-cell").addClass("specilh106")
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(even) .ui-grid-cell").addClass("specilh106")
                $(".ui-grid-pinned-container-left>.ui-grid-render-container-left>.ui-grid-viewport").addClass("specilh106")
            })

            $scope.transferFirstTypelist=[]//一级分类
            var classifyurl="/appCrawlerWait/queryAppParentCategory.do";
            gridService.httpService(classifyurl).success(function (data) {
                $scope.transferFirstTypelist=data.appParentCategoryList;
                $scope.transferFirstTypelist.unshift({category_id:"-1",category_name:"全部分类"})
                if(!$scope.transferFirstType){
                    $scope.transferFirstType=$scope.transferFirstTypelist[0];
                }
            })

            $scope.transferSecondTypelist=[]//二级分类
            var param={category_id:null}
            var classifyurl="/appCrawlerWait/queryAppChildCategory.do";
            gridService.httpService(classifyurl,param).success(function (data) {
                $scope.transferSecondTypelist=data.appChildCategoryList;
                $scope.transferSecondTypelist.unshift({category_id:"-1",category_name:"全部分类"})
                if(!$scope.transferSecondType){
                    $scope.transferSecondType=$scope.transferSecondTypelist[0];
                }
            })

            //一级分类发生改变，就从新查找二级分类数据
            $scope.queryfirstClassify = function(obj){
                $scope.transferSecondType="";       //一级分类做切换，二级分类清空重新选择
                var param = {
                    category_id:$scope.transferFirstType?$scope.transferFirstType.category_id !== "-1"?$scope.transferFirstType.category_id:null:null,
                }
                var url = "/appCrawlerWait/queryAppChildCategory.do"
                gridService.httpService(url, param).success(function (data) {
                    $scope.transferSecondTypelist = data.appChildCategoryList;
                    if ($scope.transferSecondTypelist.length>0) {
                    $scope.transferSecondTypelist.unshift({category_id: "-1", category_name: "全部分类"})
                    $scope.transferSecondType = $scope.transferSecondTypelist[0];
                    }
                })
                $scope.search()
            }

            $scope.querysecondClassify = function(){
                $scope.getPage()
            }

            // $scope.enddate = moment().format("YYYY-MM-DD");
            // $scope.startdate = moment().add(-365,'days').format("YYYY-MM-DD");
            // $scope.initAccount = $scope.startdate + ' 至 ' + $scope.enddate;
            $scope.initTime = function() {
                $('#appGrab_date').daterangepicker({
                    format: 'YYYY-MM-DD',
                    startDate: $scope.startdate,
                    endDate: $scope.enddate,
                }, function (start, end) {
                    $scope.startdate = start.format("YYYY-MM-DD");
                    $scope.enddate = end.format("YYYY-MM-DD");
                    gridService.refushPage();
                })
            }
            $scope.initTime();

            //删除
            $scope.delete = function (row) {
                var param={crawl_id:row.crawl_id}
                var url="/appCrawlerDone/deleteAppCrawlerDone.do";
                gridService.xcConfirm(row.crawl_app_name,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                $scope.search();
                            }else if(data.resultCode == "1"){
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }else{
                                gridService.xcConfirm("该App详情中存在内容,无法删除!","info")
                                return;
                            }
                        })
                    }
                });
            }
            //编辑
            $scope.modify = function (row) {
                var obj = row;
                gridService.uibModal('modifyAleadyApp.html','modifyAleadyAppCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            };

            //保存表格历史记录，获取有无历史记录
            var d=remberHistory.get();          //保存表格历史记录

            //详情
            $scope.detail = function (row) {
                // 页面恢复数据，在跳转时保存当前的数据
                var history={
                    url:$location.path(),
                    pageNumber:$scope.paginationOptions.pageNumber,
                    pageSize:$scope.paginationOptions.pageSize,
                    startdate:$scope.startdate,
                    enddate:$scope.enddate,
                    appName:$scope.appName,
                    appNumber:$scope.appNumber,
                    transferFirstType:$scope.transferFirstType,
                    transferSecondType:$scope.transferSecondType,
                };
                remberHistory.save(history);             //保存表格历史记录

                $location.url("/detailAleadyApp?appname="+row.crawl_app_name+"&apptype="+row.child_category+"&id="+row.crawl_id+"&app_id="+row.crawl_app_id+"&crawl_app_category_id_str="+row.child_category_id+
                "&appnumber="+row.crawl_app_num+"&sys="+row.crawl_app_os+"&crawl_app_logo_url="+row.crawl_app_logo_url)
            }

            //是否导出
            $scope.switchChange = function (row) {
                /*if(row.crawl_app_domain == "" || row.crawl_app_attach_param == ""){
                    gridService.xcConfirm("主域名和附加参数都不能为空，请去编辑里填写！","info");
                    return;
                }*/

                if(row.crawl_app_isexport==1){
                    $scope.crawl_app_isexport = 0;
                }else{
                    $scope.crawl_app_isexport = 1;
                }
                var param={
                    crawl_id: row.crawl_id?row.crawl_id:undefined,
                    isexport: $scope.crawl_app_isexport
                }
                var url="/appCrawlerDone/updateExport.do";
                gridService.httpService(url,param).success(function (data) {
                    if(data.resultCode == "0"){
                        $scope.getPage("refush");
                    }
                    if(data.resultCode == "1"){
                        gridService.xcConfirm("导出失败","info");
                        return;
                    }
                    if(data.resultCode == "2"){
                        gridService.xcConfirm("主域名和附加参数为空,无法导出","info");
                        return;
                    }
                })
            }

            $scope.exportApp = function(){
                window.open("/pxene-website-info/appCrawlerDone/exportApp.do");
            }

            $scope.exportRegular = function(){
                window.open("/pxene-website-info/appCrawlerDone/exportRules.do");
            }

            var columnDefs=[
                { name:'APP编号', field: 'crawl_app_num',suppressRemoveSort: true,width:90,pinnedLeft:true },
                { name:'logo', field: 'crawl_app_logo_url',width:80,pinnedLeft:true,
                    // cellTemplate:'<div class="gridtable aleadyapplogo"><div class="logoimg"><img src="{{row.entity.crawl_app_logo_url}}"></div></div>'},
                    cellTemplate:'<div class="gridtable climblogo specilh69"><div class="logoimg"><img ng-src="{{row.entity.crawl_app_logo_url | logolurlchange}}"></div></div>'},
                { name:'APP名称', field: 'crawl_app_name',width:110, pinnedLeft:true},
                { name:'关联App名称', field: 'relatedAppName',width:150},
                // { name:'App分类', field: 'appCategoryList',width:150,cellFilter:"appCategory"},
                { name:'APP一级分类', field: 'parent_category',width:120},
                { name:'APP二级分类', field: 'child_category',width:120},
                { name:'APP应用市场分类', field: 'relatedAppCategoryName',width:150},
                { name:'版本号', field: 'crawl_app_version',width:150},
                { name:'来源', field: 'crawl_app_source',width:150},
                // { name:'主域名', field: 'crawl_app_domain',width:150},          //在v2.1版本删除
                // { name:'附加参数', field: 'crawl_app_attach_param',width:90},
                { name:'操作系统', field: 'crawl_app_os',cellFilter:"ostype",width:150},
                { name:'创建时间', field: 'crawl_app_create_time',width:150,suppressRemoveSort: true},
                { name:'抓取行为数', field: 'crawl_behavior_num',width:150},
                { name:'访问数总和', field: 'totalVisits',width:150},
                { name:'统计截止时间', field: 'deadline',width:150},
                { name:'是否导出', field: 'crawl_app_isexport',width:120,cellTemplate:'<div class="gridtable specilh69 joinblack ">'+
                '<div class="switch-btn" ng-class="{\'switch-btn-pause\':row.entity.crawl_app_isexport==\'0\'?true:false}" ng-click="grid.appScope.switchChange(row.entity)">'+
                '<span class="switch-btn-op" ng-class="{\'switch-btn-hide\':row.entity.crawl_app_isexport==\'0\'?false:true}"></span>'+
                '<span class="switch-btn-toggle"><i class="switch-btn-in"></i></span>'+
                '<span class="switch-btn-cl" ng-class="{\'switch-btn-hide\':(row.entity.crawl_app_isexport==\'0\'?false:true)}"></span>'+
                '</div>'+
                '</div>'
                },
                { name:'操作', field: 'handle',width:190,pinnedRight:true,
                    cellTemplate:'<div class="gridtable specilh69">' +
                    '<button type="button" class="btn btn-default" ng-click="grid.appScope.detail(row.entity)">详情</button> ' +
                    '<button type="button" class="btn btn-translate" ng-click="grid.appScope.modify(row.entity)">编辑</button> ' +
                    '<button type="button" class="btn btn-primary" ng-click="grid.appScope.delete(row.entity)">删除</button> '
               },
            ]
            $scope.getPage = function (refush) {
                if(refush){
                    gridService.jqLoading("destroy")
                }
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    parentCategoryId:$scope.transferFirstType?$scope.transferFirstType.category_id !== "-1"?$scope.transferFirstType.category_id:undefined:undefined,
                    childCategoryId:$scope.transferSecondType?$scope.transferSecondType.category_id !== "-1"?$scope.transferSecondType.category_id:undefined:undefined,
                    appName:$scope.appName && refush?$scope.appName:undefined,
                    appNumber:$scope.appNumber && refush?$scope.appNumber:undefined,
                    startDate:$scope.startdate?$scope.startdate:undefined,
                    endDate:$scope.enddate?$scope.enddate:undefined,
                    orderRule:$scope.orderRule?$scope.orderRule:'desc',
                    orderName:$scope.orderName?$scope.orderName:"APP编号",
                }
                var url="/appCrawlerDone/queryByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {

                    //表格叠加出来后进行页码跳转
                    if(d !== ""){                                                       //保存表格历史记录
                        remberHistory.rever(d);
                    }

                    gridService.setconfig(columnDefs,data.list,data.totalcount,"big");        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }
            // $scope.getPage();

            // 恢复页面的历史数据
            $scope.$watch('$stateChangeSuccess',function (e) {
                if(d !== ""){
                    $scope.paginationOptions.pageNumber = d.pageNumber;
                    $scope.paginationOptions.pageSize = d.pageSize;
                    $scope.startdate=d.startdate;
                    $scope.enddate=d.enddate;
                    if($scope.startdate && $scope.enddate){
                        $scope.initAccount = $scope.startdate + ' 至 ' + $scope.enddate;
                        $scope.initTime();
                    }
                    $scope.appName=d.appName;
                    $scope.appNumber=d.appNumber;
                    $scope.transferFirstType=d.transferFirstType;
                    $scope.transferSecondType=d.transferSecondType;
                    $scope.getPage("refush");
                }else{
                    $scope.getPage();
                }
            })
        }])
});
