define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.countInfoWechatCtrl', [])
        .controller('countInfoWechatCtrl',['$scope','$location','$http','$uibModal','gridService','$compile','$templateCache',function($scope,$location,$http,$uibModal,gridService,$compile,$templateCache){
            gridService.setScope($scope);//初始化表格配置
            $scope.informs = [
                {name:"参数分类统计"},{name:"app分类统计"},{name:"app来源统计"},{name:"抓取行为统计"},{name:"用户数统计"},{name:"微信统计"}
            ]

            $scope.param=false;
            $scope.appclassify=false;
            $scope.appsource=false;
            $scope.behavior=false;
            var columnDefs=[
                { name:'省份', field:'province_name'},
                { name:'数据类型', field:'users_type',cellFilter:"usersType"},
                { name:'微信公众号', field: 'wechat_public_count'},
                { name:'微信文章数', field: 'wechat_article_count'},
            ]


            $scope.appClassifys=[]//app分类
            $scope.sources=[]//app来源
            $scope.grabBehaviors=[]//抓取行为
            $scope.datalist=[];

            $scope.selectTypes = [
                {name:"列表"},{name:"图表"}
            ]

            $scope.enddate = moment().format("YYYY-MM-DD");
            // $scope.startdate = moment().add(-365,'days').format("YYYY-MM-DD");
            $scope.initAccount =$scope.enddate;
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
                    source:$scope.source?$scope.source.app_source?$scope.source.app_source:undefined:undefined,
                    appBehavior:$scope.appBehavior?$scope.appBehavior.behavior_id?$scope.appBehavior.behavior_id:undefined:undefined,
                    indusrtyId:$scope.paramtype?$scope.paramtype.industry_id != "-1"?$scope.paramtype.industry_id:undefined:undefined,
                    // startDate:$scope.startdate?$scope.startdate:undefined,
                    date:$scope.enddate?$scope.enddate:undefined,
                }

                var url="/appStatistics/queryWechatStatistics.do"
                gridService.httpService(url,param).success(function (data) {
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
                var alltitlename=['微信公众号数','微信文章数']
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
                    series[0].data.push($scope.datalist[i].wechat_public_count);
                    series[1].data.push($scope.datalist[i].wechat_article_count);
                    var d=$scope.datalist[i].province_name;
                    var c=$scope.datalist[i].users_type==1?"3G":"4G";
                    xname.push(d+"-"+c);
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
                            end: 30,
                        },
                        {
                            type: 'inside',
                            start: 0,
                            end: 30,
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

        }])

});


