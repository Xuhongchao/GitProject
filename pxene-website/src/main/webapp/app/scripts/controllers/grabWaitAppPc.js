define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.grabWaitAppPcCtrl', [])
        .controller('grabWaitAppPcCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService','$upload','$timeout','$compile','$templateCache', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService,$upload,$timeout,$compile,$templateCache) {
            $scope.appCategoryNamelist=[];//应用市场分类
            $scope.appClassifys=[]//app分类
            $scope.sources=[]//来源
            $scope.sys = "pc";

            gridService.setScope($scope)
            gridService.rowHeaderSelection('radio')
            $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(odd) .ui-grid-cell").addClass("specilh69")
                $(".ui-grid-render-container-body .ui-grid-viewport .ui-grid-row:nth-child(even) .ui-grid-cell").addClass("specilh69")
                $(".ui-grid-pinned-container-left>.ui-grid-render-container-left>.ui-grid-viewport").addClass("specilh69")
            })

            //查询应用市场App分类
            var classifyurl="/appCrawlerWait/queryCrawlAppCategory.do";
            var param={appType:4};
            gridService.httpService(classifyurl,param).success(function (data) {
                $scope.appCategoryNamelist=data.crawlAppCategoryList;
                $scope.appCategoryNamelist.unshift({category_id:"-1",app_category_name:"全部分类"})
                $scope.appCategoryName=$scope.appCategoryNamelist[0];
            })

            var classifyurl="/appCrawlerWait/queryAppCategory.do";
            gridService.httpService(classifyurl).success(function (data) {
                $scope.appClassifys=data;
                $scope.appClassifys.unshift({category_id:"-1",category_name:"全部分类"})
                $scope.appClassify=$scope.appClassifys[0];
            })

            $scope.appsourcequery=function(sys) {
                var sourceurl = "/appCrawlerWait/queryAppSource.do"
                var param = {
                    appType: $scope.sys == "pc" ? 4 : 5,
                }
                gridService.httpService(sourceurl, param).success(function (data) {
                    $scope.sources = data;
                    $scope.sources.unshift({source_id: "-1", app_source: "全部来源"})
                    $scope.source = $scope.sources[0];
                })
            }
            $scope.appsourcequery();
            //tab标签做切换
            $scope.checkSys = function (val,source) {
                if(val == $scope.sys){
                    return;
                }
                $scope.sys = val;
                $scope.source = undefined;
                $scope.appsourcequery()
                if(val == "Android"){
                    $location.url("/grabWaitApp?statu="+$scope.statu.id+"&appName="+$scope.appName)
                }
                if(val == "IOS"){
                    $location.url("/grabWaitAppIos?statu="+$scope.statu.id+"&appName="+$scope.appName)
                }
                if(val == "mobile"){
                    $location.url("/grabWaitAppMobile?statu="+$scope.statu.id+"&appName="+$scope.appName)
                }
            }

            $scope.status = [
                {ways:"全部状态",id:-1},
                {ways:"未转移",id:"1"},
                {ways:"已转移",id:"2"},
            ]
            $scope.statu = $scope.status[0]

            //查询条件和上一个页面保持一致
            if($location.search().statu){
                for(var i=0;i<$scope.status.length;i++){
                    if($scope.status[i].id == $location.search().statu){
                        $scope.statu = $scope.status[i];
                    }
                }
            }
            $scope.appName = $location.search().appName !== "undefined" ?$location.search().appName:undefined;


            $scope.flag=0;
            $scope.$watch('sources',function (newvalue,oldvalue){
                if(newvalue != oldvalue){
                    $scope.flag=$scope.flag + 1
                }
            })
            $scope.$watch('appClassifys',function (newvalue,oldvalue){
                if(newvalue != oldvalue){
                    $scope.flag= $scope.flag + 1
                }
            })
            $scope.$watch('appCategoryNamelist',function (newvalue,oldvalue){
                if(newvalue != oldvalue){
                    $scope.flag= $scope.flag + 1
                }
            })
            $scope.$watch('flag',function (newvalue,oldvalue){
                if(newvalue != oldvalue && newvalue == 3){
                    $scope.search()
                }
            })


            $scope.orblack="";

            //是否加入黑名单
            $scope.switchChange = function (row) {
                if(row.app_status == 2){
                    gridService.xcConfirm("转移后的网站无法加入黑名单","info");
                    return;
                }
                if(row.app_is_blacklist==1){
                    $scope.orblack = 0;
                }else{
                    $scope.orblack = 1;
                }
                var param={
                    app_id: row.app_id?row.app_id:undefined,
                    app_is_blacklist: $scope.orblack
                }
                var url="/appCrawlerWait/updateBlacklist.do";
                gridService.httpService(url,param).success(function (data) {
                    if(data.resultCode == "0"){
                        $scope.getPage("refush");
                    }
                    if(data.resultCode == "1"){
                        gridService.xcConfirm("加入失败","info");
                        return;
                    }
                })
            }

            // 表单提示的时候tab标签转换为数字
            $scope.syschangenumber = function(sys){
                if(sys=='Android'){
                    return 2;
                }
                if(sys=='IOS'){
                    return 3;
                }
                if(sys=='pc'){
                    return 4;
                }
                if(sys=='mobile'){
                    return 5;
                }
            }
            //排名切换标志
            $scope.changeImg = function(status){
                if(status=='0'){
                    return "icon-new2";
                }
                if(status=="1"){
                    return "";
                }
                if(status=="2"){
                    return "icon-down";
                }
                if(status=="3"){
                    return "icon-up";
                }
            }
            //编辑
            $scope.modify = function(row){
                var obj = row;
                gridService.uibModal('modifyWaitApp.html','modifyWaitAppCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            }
            //下载
            $scope.download = function (row) {
                if(row.app_status == 0 || row.app_status == 3 || row.app_is_blacklist !== 0){
                    gridService.xcConfirm("加入黑名单，未爬取，和爬取失败的网站 不能下载","info");
                    return;
                }
                var obj = row;
                gridService.uibModal('downloadApp.html','downloadAppCtrl', {
                    objselect: function () {
                        return obj
                    }
                })
            }
            //转移
            $scope.translate = function (row) {
                if(row.app_status !== 1 || row.app_is_blacklist !== 0){
                    gridService.xcConfirm("加入黑名单，已转移，爬取失败，和未爬取的网站 不能转移","info");
                    return;
                }
                var obj = row;
                gridService.uibModal('translate.html','translateCtrl', {
                    objselect: function () {
                        obj.sys = $scope.sys
                        return obj
                    }
                })
            }

            //app分类解析
            $scope.appType = function (row) {
                var result="";
                if(row.length>0){
                    for(var i=0;i<row.length;i++){
                        result = result+ row[i]+"   ";
                    }
                }
                return result
            }

            var columnDefs=[
                { name:'优先级', field:'app_priority'},
                // { name:'logo', field: 'app_logo_url',cellTemplate:'<div class="climblogo gridtable specilh69"><div><img ng-src="{{row.entity.app_logo_url}}"></div></div>'},
                { name:'网站名', field: 'app_name',},
                { name:'创建时间', field: 'app_create_time'},
                { name:'来源', field: 'app_source'},
                { name:'状态', field: 'app_status',cellFilter:'appStatus'},
                { name:'是否加入黑名单', field: 'app_is_blacklist',width:120,cellTemplate:'<div class="joinblack gridtable specilh69" ng-class="{\'notallow\':row.entity.app_status==2}">'+
                '<div class="switch-btn" ng-class="{\'switch-btn-pause\':row.entity.app_is_blacklist==\'0\'?true:false}" ng-click="grid.appScope.switchChange(row.entity)">'+
                '<span class="switch-btn-op" ng-class="{\'switch-btn-hide\':row.entity.app_is_blacklist==\'0\'?false:true}"></span>'+
                '<span class="switch-btn-toggle"><i class="switch-btn-in"></i></span>'+
                '<span class="switch-btn-cl" ng-class="{\'switch-btn-hide\':(row.entity.app_is_blacklist==\'0\'?false:true)}"></span>'+
                '</div>'+
                '</div>'
                },
                { name:'操作', field: 'handlep',width:140,cellTemplate:'<div class="gridtable handle specilh69">' +
                '<button type="button" class="btn btn-translate mr10" ng-click="grid.appScope.translate(row.entity)" ng-class="{\'blacktrue\':row.entity.app_status !== 1 || row.entity.app_is_blacklist !== 0}">转移</button>' +
                '<button type="button" class="btn btn-primary" ng-click="grid.appScope.delete(row.entity)" ng-class="{\'blacktrue\':row.entity.app_status == 2}">删除</button>' +
                '</div>'
                },
            ]
            // app的系统类型0安卓和IOS1固网2安卓3IOS
            $scope.getPage = function (refush) {
                gridService.jqLoading();
                var sysnumber = $scope.syschangenumber($scope.sys);
                var param={
                    startPage: ($scope.paginationOptions.pageNumber - 1) * $scope.paginationOptions.pageSize,
                    pageSize: $scope.paginationOptions.pageSize,
                    categoryId:$scope.appClassify.category_id != "-1"?$scope.appClassify.category_id:undefined,
                    source:$scope.source?$scope.source.source_id !== "-1"?$scope.source.source_id:undefined:undefined,
                    appCategoryName:$scope.appCategoryName.category_id !== "-1"?$scope.appCategoryName.app_category_name:undefined,
                    status:$scope.statu.id?$scope.statu.id:-1,
                    appName:$scope.appName && refush?$scope.appName:undefined,
                    appType:sysnumber,
                }

                var url="/appCrawlerWait/queryWebByPageAndNum.do";
                gridService.httpService(url,param).success(function (data) {
                        gridService.setconfig(columnDefs,data.list,data.totalcount);        //配置并初始化表格数据
                        gridService.jqLoading("destroy");
                })
            }

            //修改优先级
            $scope.modifyPriority = function () {
                var selectRow = $scope.gridApi.selection.getSelectedRows()[0];
                selectRow.sys = $scope.sys;
                if(!selectRow){
                    gridService.xcConfirm("请选择一条网站，更改优先级","info");
                    return;
                }
                gridService.uibModal('modifyPriority.html','modifyPriorityCtrl',
                    {objselect: function () {
                        return selectRow
                    }})
            }

            //删除
            $scope.delete = function (row) {
                if(row.app_status == 2){
                    gridService.xcConfirm("已转移的网站 不能删除","info");
                    return;
                }

                var param={app_id:row.app_id}
                var url="/appCrawlerWait/delCrawlWaitApp.do";
                gridService.xcConfirm(row.app_name,param,url,{
                    onOk: function (v) {
                        gridService.httpService(url,param).success(function (data) {
                            if (data.resultCode == "0") {
                                gridService.xcConfirm("删除成功","info");
                                $scope.search();
                            }else{
                                gridService.xcConfirm("删除失败","info")
                                return;
                            }
                        })
                    }
                });
            }

            //添加
            $scope.addApp = function () {
                if($scope.sys == "Android" || $scope.sys == "IOS" ){
                    gridService.uibModal('addApp.html','addAppCtrl',{
                        appsourcequery: function () {
                            return $scope.appsourcequery;
                        },
                        sys:function(){
                            return $scope.sys;
                        }
                    });
                }else{
                    gridService.uibModal('addWebsite.html','addWebsiteCtrl',{
                        appsourcequery: function () {
                            return $scope.appsourcequery;
                        },
                        sys:function(){
                            return $scope.sys;
                        }
                    });
                }

            };

            // 导入
            $scope.onFileSelect1 = function($files) {
                // if($files==null||$files.length<=0 || $files[0].size>5*1024*1024){
                //     return;
                // }
                // $scope.files1.push($files[0]);
                // var file=document.getElementById('export_input');
                // var str=file.value;
                // str=str.substring(str.lastIndexOf('\\')+1);
                // $scope.fileName=str;
                // for(var i=0;i<$scope.fileName1.length;i++){
                //     if($scope.fileName1[i].name == str){
                //         window.wxc.xcConfirm(""+str+"文件已经存在!", window.wxc.xcConfirm.typeEnum.warning);
                //         return ;
                //     }
                // }
                var percent=0;
                var end=false;
                var tmp='<div id="progressbg">' +
                        '<div class="progress progress-striped active" ng-class="{\'progressflag\':progressflag}">'+
                        '<div class="progress-bar progress-bar-success" role="progressbar"'+
                        'aria-valuenow=percent aria-valuemin="0" aria-valuemax="100">'+
                        // 'style="width:"+percent+"%;">'+
                        // '<span class="sr-only">percent+% 完成</span>'+
                        '<span></span>'+
                        '</div>'+
                        '</div>' +
                        '</div>'
                // $('body').append(tmp)
                // $scope.progressflag = true;
                $upload.upload({
                    url: basePath+"/appCrawlerWait/importApp.do?appType="+ $scope.syschangenumber($scope.sys),
                    method: 'POST',
                    file:$files[0],
                }).success(function (data) {
                    end=true;
                    // var value=setInterval(function () {
                        // if(percent >= 100){
                            // clearInterval(value)
                            // $('#progressbg').remove()
                                if(data.resultCode == 0){
                                    gridService.xcConfirm("文件导入成功！","info");
                                }else if(data.resultCode == 1){
                                    gridService.xcConfirm("部分文件导入失败！","info");
                                }else if(data.resultCode == 2){
                                    gridService.xcConfirm("导入的文件内容不符合规范！","info");
                                }
                            // var url="/appCrawlerWait/crawlImportApp.do";
                            // gridService.httpService(url).success(function (data) {
                            //     if(data.crawlState == "done"){
                            //         $scope.getPage();
                            //     }
                            // })
                            $scope.getPage();
                            $scope.appsourcequery();
                            $("#export_input").val('');
                    //         percent=0;
                    //     }
                    //     percent=(percent%2 ==0) ? percent+2 : percent+1;
                    //     $('.progress-bar-success').css("width",percent+"%")
                    // },100)
                }).error(function(data, status, headers, config){
                    var d=config.url.split("/")[config.url.split("/").length-1];
                    gridService.xcConfirm("请求："+d+"<br />"+"返回错误状态码: "+status,"info")
                    $("#export_input").val('');
                });
                //
                // var value=setInterval(function () {
                //     if(percent == 90 || end){
                //         clearInterval(value)
                //     }
                //     percent=percent+1;
                //     $('.progress-bar-success').css("width",percent+"%")
                // },1000)

            };
        }])
});
