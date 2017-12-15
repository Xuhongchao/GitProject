define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.countInfoUserNumberCtrl', [])
        .controller('countInfoUserNumberCtrl',['$scope','$location','$http','$uibModal','gridService','$compile','$templateCache',function($scope,$location,$http,$uibModal,gridService,$compile,$templateCache){
            gridService.setScope($scope)
            $scope.informs = [
                {name:"参数分类统计"},{name:"app分类统计"},{name:"app来源统计"},{name:"抓取行为统计"},{name:"用户数统计"}
                ,{name:"微信统计"}
            ]

            $scope.classifys = [{name:"按省份和数据类型",isSelected:true},{name:"按APP"},{name:"按APP,省份和数据类型"},
                {name:"按URL"},{name:"按URL，省份和数据类型"},{name:"按参数分类，省份和数据类型"}]

            $scope.selectTypes = [
                {name:"列表"},{name:"图表"}
            ]

            $scope.startdate = moment().format("YYYY-MM-DD");
            $scope.initAccount = $scope.startdate;
            $scope.initTime = function() {
                $('#appGrab_date').daterangepicker({
                    format: 'YYYY-MM-DD',
                    startDate: $scope.startdate,
                    endDate: $scope.enddate,
                    singleDatePicker: true,
                }, function (start, end) {
                    $scope.startdate = start.format("YYYY-MM-DD");
                    $scope.enddate = end.format("YYYY-MM-DD");
                    gridService.refushPage();
                })
            }
            $scope.initTime();

            //app分类
            var classifyurl="/appCrawlerWait/queryAppCategory.do";
            gridService.httpService(classifyurl).success(function (data) {
                $scope.appClassifys=data;
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

            $scope.one = true;
            $scope.last = true;
            $scope.pageInfo = 0;
            $scope.showflag = true;
            $scope.datalist=[];
            $scope.datalistfieldList=[];
            var order=0;
            var url="/appStatistics/queryUsersOne.do";
            var columnDefs=[
                { name:'省份', field: 'province_name'},
                { name:'3G', field:'3G'},
                { name:'4G', field:'4G'},
            ]
            $scope.clickradio = function (index) {
                $scope.pageInfo = 0;
                order=index
                columnDefs=[];
                if(index == 0){
                    $scope.one = true;
                    url="/appStatistics/queryUsersOne.do";
                    columnDefs=[
                        { name:'省份', field: 'province_name'},
                        { name:'3G', field:'3G'},
                        { name:'4G', field:'4G'},
                    ]
                }else{
                    $scope.one = false;
                    if(index == 1){
                        url="/appStatistics/queryUsersTwo.do";
                        columnDefs=[
                            { name:'app名称', field: 'app_name'},
                            { name:'用户数', field:'users_count'},
                        ]
                    }else if(index == 2){
                        url="/appStatistics/queryUsersThree.do";
                        columnDefs=[
                            { name:'App名称', field: 'app_name'},
                            { name:'数据类型', field:'users_type',cellFilter:"usersType"},
                        ]
                    }else if(index ==3){
                        url="/appStatistics/queryUsersFour.do";
                        columnDefs=[
                            { name:'URL编号', field: 'users_crawl_detail_num'},
                            { name:'用户数', field:'users_count'},
                        ]
                    }else if(index ==4){
                        url="/appStatistics/queryUsersFive.do";
                        columnDefs=[
                            { name:'URL编号', field: 'users_crawl_detail_num'},
                            // { name:'用户数', field:'users_count'},
                            { name:'数据类型', field:'users_type',cellFilter:"usersType"},
                        ]
                    }else if(index ==5){
                        $scope.one = true;
                        $scope.last = false;
                        url="/appStatistics/queryUsersSix.do";
                        columnDefs=[
                            { name:'参数分类', field: 'industry_name'},
                            { name:'数据类型', field:'users_type',cellFilter:"usersType"},
                        ]
                    }
                }
                $scope.getPage()
            }

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

            // app的系统类型0安卓和IOS1固网2安卓3IOS
            $scope.getPage = function (refush) {
                gridService.jqLoading()
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    categoryId:$scope.appClassify?$scope.appClassify.category_id != "-1"?$scope.appClassify.category_id:undefined:undefined,
                    source:$scope.source?$scope.source.source_id!=="-1"?$scope.source.source_id:undefined:undefined,
                    appBehavior:$scope.grabBehavior?$scope.grabBehavior.behavior_id != "-1"?$scope.grabBehavior.behavior_id:undefined:undefined,
                    indusrtyId:$scope.paramtype?$scope.paramtype.industry_id != "-1"?$scope.paramtype.industry_id:undefined:undefined,
                    appName:$scope.appname && refush?$scope.appname:undefined,
                    date:$scope.startdate?$scope.startdate:undefined,
                }
                gridService.httpService(url,param).success(function (data) {
                    if(data.totalcount == 0 || data.list.length == 0){
                        var t_html='<span class="emptyGridMessage">请求数据为空,请再次尝试...</span>'
                        $("#mainchart").css("display","none").after(t_html)
                        $(".chartheader").css("visibility","hidden")
                    }else{
                        $("#mainchart").css("display","block")
                        $(".emptyGridMessage").remove()
                        $(".chartheader").css("visibility","visible")
                    }
                    var arraycolumnDefs=[];
                    $scope.datalist=data.list;
                    $scope.datalistfieldList=data.fieldList;
                    if(order == 2 || order == 4 || order == 5){
                        var array=[];
                        for(var i=0;i<data.fieldList.length;i++) {
                            array.push({name:data.fieldList[i],field:data.fieldList[i]})
                        }
                        arraycolumnDefs=columnDefs.concat(array)
                        $scope.datalist=data.list;
                        if(!$scope.showflag){
                            $scope.initecharts()
                        }else{
                            gridService.setconfig(arraycolumnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                        }
                        gridService.jqLoading("destroy")
                        return;
                    }
                    if(!$scope.showflag){
                        $scope.initecharts()
                    }else{
                        gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                    }
                    gridService.jqLoading("destroy")
                })
            }
            $scope.getPage()

            $scope.initecharts = function () {
                var xname=[];
                var alltitlename=[]
                var series=[];
                    if(order == 0){
                        alltitlename=['3G','4G']
                        series=[{
                                name:alltitlename[0],
                                type:'bar',
                                barMaxWidth: '40%',
                                data:[],
                            },
                            {
                                name:alltitlename[1],
                                type:'bar',
                                barMaxWidth: '40%',
                                data:[],
                            }];
                        for(var i=0;i<$scope.datalist.length;i++) {
                            series[0].data.push($scope.datalist[i]["3G"])
                            series[1].data.push($scope.datalist[i]["4G"])
                            xname.push($scope.datalist[i].province_name);
                        }
                    }else if(order == 1){
                        alltitlename=['用户数']
                        series=[{
                            name:alltitlename[0],
                            type:'bar',
                            barMaxWidth: '40%',
                            data:[],
                        }];
                        for(var i=0;i<$scope.datalist.length;i++) {
                            series[0].data.push($scope.datalist[i].users_count)
                            xname.push($scope.datalist[i].app_name);
                        }
                    }else if(order == 2 || order == 4 || order == 5){
                        alltitlename=$scope.datalistfieldList
                        for(var i=0;i<alltitlename.length;i++){
                            series.push({
                                name:alltitlename[i],
                                type:'bar',
                                barMaxWidth: '40%',
                                data:[],
                            })
                            for(var j=0;j<$scope.datalist.length;j++){
                                var c=true;
                                for(name in $scope.datalist[j]){
                                    if(alltitlename[i]  == name){
                                        series[i].data.push($scope.datalist[j][name])
                                        c=false;
                                        break;
                                    }else if(name !== "app_name" && name !== "users_app_num" && name !== "users_type" && name !== "$$hashKey"){
                                    }
                                }
                                if(c){
                                    series[i].data.push("")
                                }
                            }
                        }
                        for(var i=0;i<$scope.datalist.length;i++) {
                            if(order == 4){
                                var d=$scope.datalist[i].users_crawl_detail_num;
                                var c=$scope.datalist[i].users_type==1?"3G":"4G";
                                xname.push(d+"-"+c);
                            }else if(order == 5){
                                var d=$scope.datalist[i].industry_name;
                                var c=$scope.datalist[i].users_type==1?"3G":"4G";
                                xname.push(d+"-"+c);
                            }else{
                                var d=$scope.datalist[i].app_name;
                                var c=$scope.datalist[i].users_type==1?"3G":"4G";
                                xname.push(d+"-"+c);
                            }
                        }
                    }else if(order == 3){
                        alltitlename=['用户数']
                        series=[{
                            name:alltitlename[0],
                            type:'bar',
                            barMaxWidth: '40%',
                            data:[],
                        }];
                        for(var i=0;i<$scope.datalist.length;i++) {
                            series[0].data.push($scope.datalist[i].users_count)
                            xname.push($scope.datalist[i].users_crawl_detail_num);
                        }
                    }
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('mainchart'));
                // 指定图表的配置项和数据
                option = {
                    // color: ['#A4DDFF','#FFA06F'],
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
                    grid: {
                        top:'100px',
                    },
                    legend: {
                        right:20,
                        top:20,
                        // itemGap:10,
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
                // if(order == 2 || order == 4 || order == 5) {
                //     option.dataZoom = [
                //         {
                //             show: true,
                //             start: 60,
                //             end: 100
                //         },
                //         {
                //             type: 'inside',
                //             start: 94,
                //             end: 100
                //         },
                //     ]
                // }
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }

        }])

});


