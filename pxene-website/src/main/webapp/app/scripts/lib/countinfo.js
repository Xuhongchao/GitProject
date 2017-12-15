// 菜单导航模块
angular.module('countinfo',['ui.grid','ui.grid.pagination','ui.grid.selection']).directive('countinfo',function ($location,$http,gridService,$compile,$timeout,remberHistory) {
    return {
        restrict:'A',
        templateUrl:"app/template/viewModule/countinfo.html",
        link:function ($scope,element,attrs) {
            gridService.setScope($scope);//初始化表格配置
            $scope.informs = [
                {name:"参数分类统计"},{name:"app分类统计"},{name:"app来源统计"},{name:"抓取行为统计"},{name:"用户数统计"},{name:"微信统计"}
            ]
            $scope.echartstitle={}

            $scope.param=false;
            $scope.appclassify=false;
            $scope.appsource=false;
            $scope.behavior=false;
            var columnDefs=[
                { name:'参数分类', field:'industry_name'},
                { name:'app数量', field:'appCount'},
                { name:'抓取行为数', field: 'crawlBehaviorCount'},
            ]
            if(attrs.countinfo == "param"){//参数分类统计
                $scope.echartstitle.name = "参数分类统计"
                $scope.param=true;
                columnDefs.push({ name:'操作', field: 'handle',
                    cellTemplate:'<div class="gridtable detandTran">' +
                    '<button class="btn btn-default" type="button" ng-click="grid.appScope.detail(row.entity,\'param\')">详情</button>'
                    })
            }else if(attrs.countinfo == "appclassify"){//app分类统计
                $scope.echartstitle.name = "app分类统计"
                $scope.appclassify=true;
                columnDefs.shift()
                columnDefs.unshift({ name:'app分类', field:'category_name'})
            }else if(attrs.countinfo == "appsource"){
                $scope.echartstitle.name = "app来源统计"
                $scope.appsource=true;
                columnDefs.shift()
                columnDefs.unshift({ name:'app来源', field:'crawl_app_source'})
            }else if(attrs.countinfo == "behavior"){
                $scope.echartstitle.name = "抓取行为统计"
                $scope.behavior=true;
                columnDefs.shift()
                columnDefs.unshift({ name:'抓取行为', field:'behavior_name'})
                columnDefs.push({ name:'操作', field: 'handle',
                    cellTemplate:'<div class="gridtable detandTran">' +
                    '<button class="btn btn-default" type="button" ng-click="grid.appScope.detail(row.entity,\'behavior\')">详情</button>'
                })
            }

            //保存表格历史记录，获取有无历史记录
            // var d=remberHistory.get();

            //参数分类统计详情跳转
            $scope.detail = function(row,param){

                // 页面恢复数据，在跳转时保存当前的数据
                /*var history={
                    url:$location.path(),
                    pageNumber:$scope.paginationOptions.pageNumber,
                    pageSize:$scope.paginationOptions.pageSize,
                    typeNumber:$scope.typeNumber,
                    typeName:$scope.typeName,
                };
                remberHistory.save(history);             //保存表格历史记录*/

                if(param == "param"){
                    $location.url("/detailcountInfoParam?industry_id="+row.industry_id+"&appClassify="+$scope.appClassify.category_id+"&grabBehavior="+$scope.grabBehavior.behavior_id+
                    "&enddate="+$scope.enddate+"&startdate="+$scope.startdate)
                }
                if(param == "behavior"){
                    $location.url("/detailcountInfoGrabBehavior?behavior_id="+row.behavior_id+"&appClassify="+$scope.appClassify.category_id+"&paramtype="+$scope.paramtype.industry_id+
                        "&enddate="+$scope.enddate+"&startdate="+$scope.startdate)
                }
            }

            $scope.appClassifys=[]//app分类
            $scope.sources=[]//app来源
            $scope.grabBehaviors=[]//抓取行为
            $scope.datalist=[];

            $scope.selectTypes = [
                {name:"列表"},{name:"图表"}
            ]

            $scope.enddate = moment().format("YYYY-MM-DD");
            $scope.startdate = moment().add(-365,'days').format("YYYY-MM-DD");
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

            var classifyurl="/appCrawlerWait/queryAppChildCategory.do";
            var param={category_id:null}
            gridService.httpService(classifyurl,param).success(function (data) {
                $scope.appClassifys=data.appChildCategoryList;
                $scope.appClassifys.unshift({category_id:"-1",category_name:"全部分类"})
                $scope.appClassify=$scope.appClassifys[0];
            })

            //app来源
            var sourceurl = "/appCrawlerDone/queryAppSource.do"
            gridService.httpService(sourceurl).success(function (data) {
                $scope.sources=data.appSource;
                $scope.sources.unshift({source_id:"-1",crawl_app_source:"全部来源"})
                $scope.source=$scope.sources[0];
            })

            //抓取行为
            var behaviorurl="/appBehavior/queryAllBehavior.do"
            gridService.httpService(behaviorurl).success(function (data) {
                $scope.grabBehaviors=data.allBehavior;
                $scope.grabBehaviors.unshift({behavior_id:"-1",behavior_name:"全部行为"})
                $scope.grabBehavior= $scope.grabBehaviors[0]
            })

            //参数分类
            var paramtypeurl="/appIndustry/queryAllIndustry.do"
            gridService.httpService(paramtypeurl).success(function (data) {
                $scope.paramtypes=data.allIndustry;
                $scope.paramtypes.unshift({industry_id:"-1",industry_name:"全部分类"})
                $scope.paramtype= $scope.paramtypes[0]
            })

            $scope.pageInfo = 0;
            $scope.showflag = true;
            $scope.changeClassify = function (obj,index) {
                $scope.pageInfo = index
                if(obj.name=="列表"){
                    $scope.showflag = true;
                    $scope.getPage()
                }else if(obj.name=="图表"){
                    $scope.showflag = false;
                    $scope.initecharts()
                }
            }

            $scope.checkSys = function (val) {
                switch (val.name){
                    case "参数分类统计" : $location.url("/countInfoParam"); break;
                    case "app分类统计" : $location.url("/countInfoAppClassify"); break;
                    case "app来源统计" : $location.url("/countInfoAppSource"); break;
                    case "抓取行为统计" : $location.url("/countInfoGrabBehavior"); break;
                    case "用户数统计" : $location.url("/countInfoUserNumber"); break;
                    case "微信统计" : $location.url("/countInfoWechat"); break;
                    default : $location.url("/countInfoParam"); break;
                }
            }

            $scope.getPage = function () {
                gridService.jqLoading()
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    categoryId:$scope.appClassify?$scope.appClassify.category_id != "-1"?$scope.appClassify.category_id:undefined:undefined,
                    source:$scope.source?$scope.source.source_id !== "-1"?$scope.source.source_id:undefined:undefined,
                    appBehavior:$scope.grabBehavior?$scope.grabBehavior.behavior_id != "-1"?$scope.grabBehavior.behavior_id:undefined:undefined,
                    indusrtyId:$scope.paramtype?$scope.paramtype.industry_id != "-1"?$scope.paramtype.industry_id:undefined:undefined,
                    startDate:$scope.startdate?$scope.startdate:undefined,
                    endDate:$scope.enddate?$scope.enddate:undefined,
                }

                if($scope.param){
                    var url="/appStatistics/queryIndustry.do";
                }else if($scope.appclassify){
                    var url="/appStatistics/queryAppCategory.do";
                }else if($scope.appsource){
                    var url="/appStatistics/queryAppSource.do";
                }else if($scope.behavior){
                    var url="/appStatistics/queryAppBehavior.do";
                }
                gridService.httpService(url,param).success(function (data) {

                    //表格叠加出来后进行页码跳转
                    /*if(d !== ""){                                                       //保存表格历史记录
                     remberHistory.rever(d);
                     }*/

                    $scope.datalist=data.list;
                    if(!$scope.showflag){
                        $scope.initecharts()
                    }else{
                        gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    }
                    gridService.jqLoading("destroy")
                })
            }
            $scope.getPage();

            // 恢复页面的历史数据
            /*$scope.$watch('$stateChangeSuccess',function (e) {
                if(d !== ""){debugger
                    $scope.paginationOptions.pageNumber = d.pageNumber;
                    $scope.paginationOptions.pageSize = d.pageSize;
                    $scope.typeNumber=d.typeNumber;
                    $scope.typeName=d.typeName;
                    $scope.getPage("refush");
                }else{
                    $scope.getPage();
                }
            })*/

            $scope.search = function(){
                gridService.refushPage()
            }
            $scope.enterSearch = function(e){
                if(e.keyCode==13){
                    $scope.search();
                }
            }

            $scope.initecharts = function () {
                var xname=[];
                var alltitlename=['APP数量','抓取行为数']
                var series=[{
                        name:alltitlename[0],
                        type:'bar',
                        data:[],
                    },
                    {
                        name:alltitlename[1],
                        type:'bar',
                        data:[],
                    }];
                for(var i=0;i<$scope.datalist.length;i++){
                    series[0].data.push($scope.datalist[i].appCount)
                    series[1].data.push($scope.datalist[i].crawlBehaviorCount)
                    if($scope.param){
                        xname.push($scope.datalist[i].industry_name);
                    }else if($scope.appclassify){
                        xname.push($scope.datalist[i].category_name);
                    }else if($scope.appsource){
                        xname.push($scope.datalist[i].crawl_app_source);
                    }else if($scope.behavior){
                        xname.push($scope.datalist[i].behavior_name);
                    }
                }
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('mainchart'));
                // 指定图表的配置项和数据
                option = {
                    color: ['#A4DDFF','#FFA06F'],
                    tooltip : {
                        trigger: 'axis',
                        // formatter: function (val) {
                        //     var tmp=val[0].name;
                        //     var tmp_list=""
                        //     for(var i=0;i<val.length;i++){
                        //         if(!val[i].value){
                        //             val[i].value=0
                        //         }
                        //         tmp_list=tmp_list+val[i].seriesName+ ':'+ val[i].value +'<br />'
                        //     }
                        //     return tmp+'<br />'+tmp_list
                        // }
                    },
                    legend: {
                        right:20,
                        top:20,
                        itemGap:40,
                        data:alltitlename
                    },
                    dataZoom:[
                    {
                        show: true,
                        start: 0,
                        end: 30
                    },
                    {
                        type: 'inside',
                        start: 0,
                        end: 30
                    }],
                    xAxis : [
                        {
                            type : 'category',
                            data : xname
                        }
                    ],
                    yAxis : [
                        {
                            type : 'value'
                        }
                    ],
                    series : series
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        }
    }
})
