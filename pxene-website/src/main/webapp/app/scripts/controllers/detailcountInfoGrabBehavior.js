define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.detailcountInfoGrabBehaviorCtrl', [])
        .controller('detailcountInfoGrabBehaviorCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService) {
            $scope.behavior_id = $location.search().behavior_id;
            var transferSecondType=$location.search().appClassify;
            var paramtype=$location.search().paramtype;
            var startdate=$location.search().startdate;
            var enddate=$location.search().enddate;

            gridService.setScope($scope);

            $scope.transferSecondTypelist=[]//二级分类
            var param={category_id:null}
            var classifyurl="/appCrawlerWait/queryAppChildCategory.do";
            gridService.httpService(classifyurl,param).success(function (data) {
                $scope.transferSecondTypelist=data.appChildCategoryList;
                if($scope.transferSecondTypelist.length>0) {
                    $scope.transferSecondTypelist.unshift({category_id: "-1", category_name: "全部分类"})
                    for (var i = 0; i < $scope.transferSecondTypelist.length; i++) {
                        if (transferSecondType == $scope.transferSecondTypelist[i].category_id) {
                            $scope.transferSecondType = $scope.transferSecondTypelist[i];
                        }
                    }
                }

                //参数分类
                var paramtypeurl="/appIndustry/queryAllIndustry.do"
                gridService.httpService(paramtypeurl).success(function (data) {
                    $scope.paramtypes=data.allIndustry;
                    $scope.paramtypes.unshift({industry_id:"-1",industry_name:"全部分类"})
                    for (var i = 0; i < $scope.paramtypes.length; i++) {
                        if (paramtype == $scope.paramtypes[i].industry_id) {
                            $scope.paramtype = $scope.paramtypes[i];
                        }
                    }

                    $scope.getPage();
                })
            })

            if(startdate && enddate){
                $scope.enddate = enddate;
                $scope.startdate = startdate;
            }else{
                $scope.enddate = moment().format("YYYY-MM-DD");
                $scope.startdate = moment().add(-365, 'days').format("YYYY-MM-DD");
            }
            $scope.initAccount = $scope.startdate + ' 至 ' + $scope.enddate;
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

            var columnDefs=[
                { name:'编号', field:'crawl_detail_num',pinnedLeft:true,width:100},
                /*{ name:'排名', field: 'app_ranking1',width:80,pinnedLeft:true,cellTemplate:"<div class='ranking gridtable specilh69'>" +
                 "<span title='{{row.entity.app_ranking1}}'>{{row.entity.app_ranking1}}</span>" +
                 "<i class='{{grid.appScope.changeImg(row.entity.app_flag)}}' ng-show='grid.appScope.changeImg(row.entity.app_flag)'></i>" +
                 // "<b ng-hide='grid.appScope.changeImg(row.entity.app_flag)'></b></span>" +
                 "<span title='{{row.entity.app_value}}' ng-show='row.entity.app_flag != 0 && row.entity.app_flag != 1'>{{row.entity.app_value}}</span></div>"
                 },*/
                // { name:'logo', field: 'app_logo_url',cellTemplate:'<div class="climblogo gridtable specilh69"><div><img ng-src="{{row.entity.app_logo_url}}"></div></div>'},
                { name:'logo', field: 'crawl_app_logo_url',width:80,pinnedLeft:true,cellTemplate:'<div class="climblogo gridtable specilh69"><div><img ng-src="{{row.entity.crawl_app_logo_url | logolurlchange}}"></div></div>'},
                { name:'APP名称', field: 'crawl_app_name',pinnedLeft:true,width:150},
                { name:'APP二级分类', field: 'category_name',cellFilter:"appCategory",width:200},
                { name:'参数分类', field: 'industry_name',width:150},
                { name:'抓取行为', field: 'behavior_name',width:150},
                { name:'域名', field: 'crawl_detail_domain',width:180},
                { name:'参数正则', field: 'crawl_detail_paramreg',width:150},
                { name:'URL正则', field: 'crawl_detail_urlreg',width:150},
                { name:'URL样例', field: 'crawl_detail_urlexample',width:150},
                { name:'备注', field: 'crawl_detail_comments',width:150},
                { name:'创建时间', field: 'crawl_detail_create_time',width:150},
                { name:'统计访问数', field: 'totalVisits',width:150},
                { name:'统计截止时间', field: 'deadline',width:150},
            ]

            $scope.getPage = function (refush) {
                gridService.jqLoading();
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    startDate:$scope.startdate?$scope.startdate:undefined,
                    endDate:$scope.enddate?$scope.enddate:undefined,
                    behavior_id:$scope.behavior_id?$scope.behavior_id:undefined,
                    crawl_app_name:$scope.appName?$scope.appName:undefined,
                    industry_id:$scope.paramtype?$scope.paramtype.industry_id != "-1"?$scope.paramtype.industry_id:undefined:undefined,
                    category_id:$scope.transferSecondType?$scope.transferSecondType.category_id !== "-1"?$scope.transferSecondType.category_id:undefined:undefined,
                }
                var url="/appStatistics/queryStatisticsCrawlDetail.do";
                gridService.httpService(url,param).success(function (data) {
                    gridService.setconfig(columnDefs,data.list,data.totalcount,"big");        //配置并初始化表格数据
                    gridService.jqLoading("destroy");
                })
            }

        }])
});



