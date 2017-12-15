define(['angular'], function () {
    return angular.module('dmpwebApp.controllers.addDetailAleadyAPPCtrl', [])
        .controller('addDetailAleadyAPPCtrl', ['$scope','$location','$http','$uibModal','uiGridConstants','gridService','validateService', function ($scope,$location,$http,$uibModal,uiGridConstants,gridService,validateService) {
            $scope.appClassifys=[];
            $scope.appnumber=$location.search().appnumber;
            $scope.appname=$location.search().appname;
            $scope.apptype=$location.search().apptype;
            $scope.logourl=$location.search().crawl_app_logo_url;

            $scope.matchSpecil = function(val){
                var v=val.toUpperCase();
                if(v == "IDFA"){
                    return 1;
                }
                if(v == "IMEI"){
                    return 2;
                }
                if(v == "ANDROIDID"){
                    return 3;
                }
                if(v == "MAC"){
                    return 4;
                }
            }

            $scope.matchregular = function (obj) {
                if(obj){
                    var d=obj.match(/[^\/]+/g);
                    $scope.domain = d[1];
                    if(obj.indexOf("?") !== -1 && obj.substring(obj.indexOf("?")+1)){
                       // $scope.urlregexp = obj.match(/[^?]+/g)[0].split(d[1])[1]
                    }else{
                        // $scope.urlregexp="";
                        $scope.placeholdertext="请输入URL正则"
                    }
                }
            }

            //抓取行为
            var b=0;
            $scope.aleadyfocus = false;
            $scope.grabBehaviorserive = function(behavior) {
                if(b==1){
                    return;
                }
                b=1;
                    var param = {
                        crawl_app_category_id_str: $location.search().crawl_app_category_id_str,
                        search:behavior?behavior:undefined
                    }
                    var url = "/appCrawlerDone/queryAppCrawlBehavior.do"
                    gridService.httpService(url, param).success(function (data) {
                        $scope.appClassifys = data.behaviorList;
                        if($scope.appClassifys.length==0){
                            $scope.searchtext = ""
                            $scope.grabBehavior = ""
                        }
                        b=0
                    })
            }
            $scope.grabBehaviorserive();

            //使用端
            var url="/appCrawlerDone/queryTransferType.do"
            var param={
                crawl_app_num:$scope.appnumber
            }
            gridService.httpService(url,param).success(function (data) {
                $scope.userpointlist=data.list;
            })

            //参数分类
            var url="/appCrawlerDone/queryAppIndustryInfo.do"
            var param={
                crawl_detail_id:undefined
            }
            gridService.httpService(url,param).success(function (data) {
                $scope.types=data.allIndustry;
            })

            $scope.selectTypes = [];    //选择的参数分类
            $scope.params = [];         //参数
            $scope.paramRegexs = [];    //参数正则

            $scope.allSelectTypes=[];      //存放不同参数分类的正则和备注
            $scope.selectTypesIndex = 0;       //显示参数分类的index

            $scope.fenleixuanze=[];

            $scope.selectClassify = function (obj) {
                var isSelected=obj.isSelected;
                var specilflag = $scope.matchSpecil(obj.industry_name)?true:false//对特殊的四种情况进行匹配

                $scope.selectTypesIndex = 0
                $scope.selectTypes=[];
                // $scope.allSelectTypes=[];
                for(var i=0;i<$scope.types.length;i++){
                    if($scope.types[i].isSelected){
                        $scope.selectTypes.push($scope.types[i])
                    }
                }
                //四种特殊情况和其他普通情况选择互斥
                if($scope.selectTypes.length==1){
                    if(isSelected && specilflag){
                        for(var i=0;i<$scope.types.length;i++){
                            if($scope.matchSpecil($scope.types[i].industry_name)){
                                $scope.types[i].clickFlag = false;
                            }else{
                                $scope.types[i].clickFlag = true;
                            }
                        }
                    }
                    if(isSelected && !specilflag){
                        for(var i=0;i<$scope.types.length;i++){
                            if($scope.matchSpecil($scope.types[i].industry_name)){
                                $scope.types[i].clickFlag = true;
                            }else{
                                $scope.types[i].clickFlag = false;
                            }
                        }
                    }
                }
                if(!isSelected && !$scope.selectTypes.length){
                    for(var i=0;i<$scope.types.length;i++){
                        $scope.types[i].clickFlag = false;
                    }
                }
            }

            $scope.changeClassify = function (obj,index) {
                $scope.selectTypesIndex = index
                if($scope.selectTypes.length>0){
                }
            }

            // var saveparam_key=[];
            // var saveparam_content=[];
            $scope.changeparamreg = function (obj,index) {
                if(obj.param_reg.param_reg == "NULL"){
                    obj.emptyreg = true;
                    // saveparam_key[index] = obj.param_key;
                    // saveparam_content[index] = obj.param_content;
                    obj.param_key = "";
                    obj.param_content = "";
                }else{
                    obj.emptyreg = false;
                    // if(saveparam_key[index] || saveparam_content[index]){
                    //     obj.param_key = saveparam_key[index];
                    //     obj.param_content = saveparam_content[index];
                    // }
                }
            }

            $scope.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    if($scope.selectTypes.length == 0){
                        gridService.xcConfirm("参数分类名称 必须选择！","info");
                        return;
                    }

                    for(var i=0;i<$scope.selectTypes.length;i++){
                        if($scope.selectTypes[i].industryContentList){
                            for(var j=0;j<$scope.selectTypes[i].industryContentList.length;j++){
                                if($scope.selectTypes[i].industryContentList[j].param_reg){
                                    if($scope.selectTypes[i].industryContentList[j].param_reg.param_reg !== "NULL") {
                                        if (!$scope.selectTypes[i].industryContentList[j].param_key || !$scope.selectTypes[i].industryContentList[j].param_reg || !$scope.selectTypes[i].industryContentList[j].param_content) {
                                            gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                            return;
                                        }
                                        if($scope.selectTypes[i].industryContentList[j].param_num){
                                             if(!$scope.selectTypes[i].industryContentList[j].param_num.match(/^[a-zA-Z]*$/g)){
                                             gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                             return;
                                             }
                                        }
                                    }
                                }else{
                                    if (!$scope.selectTypes[i].industryContentList[j].param_key || !$scope.selectTypes[i].industryContentList[j].param_reg || !$scope.selectTypes[i].industryContentList[j].param_content) {
                                        gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                        return;
                                    }
                                    if($scope.selectTypes[i].industryContentList[j].param_num){
                                        if(!$scope.selectTypes[i].industryContentList[j].param_num.match(/^[a-zA-Z]*$/g)){
                                            gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                            return;
                                        }
                                    }
                                }

                                //参数正则针对特殊的四种情况（idfa，imei，androidid和mac）验证通过后添加的标识分别为1,2,3,4+输入的字母
                                $scope.selectTypes[i].industryContentList[j].param_num = $scope.selectTypes[i].industryContentList[j].param_num_order + ($scope.selectTypes[i].industryContentList[j].param_num?$scope.selectTypes[i].industryContentList[j].param_num:0);
                            }
                        }
                    }

                    var param={
                        urlExample:$scope.urlExample?$scope.urlExample:undefined,
                        grabBehavior:$scope.grabBehavior.behavior_id?$scope.grabBehavior.behavior_id:undefined,
                        urlregexp:$scope.urlregexp?$scope.urlregexp:undefined,
                        domain:$scope.domain?$scope.domain:undefined,
                        industryContentList:$scope.selectTypes,
                        remarks:$scope.remarks,
                        crawl_id:$location.search().id,
                        crawl_app_num:$location.search().appnumber,
                        // crawl_app_os:$location.search().sys,
                        crawl_detail_type:$scope.userpoint.type_num,
                    }
                    $scope.aleadysure=true;
                    var url="/appCrawlerDone/addCrawlInfo.do";
                    gridService.httpService(url,param).success(function (data) {
                        $scope.aleadysure=false;
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("抓取信息已保存","info", {
                                onOk: function (v) {
                                    $scope.$apply(function () {
                                        $location.url("/detailAleadyApp?crawl_app_category_id_str="+$location.search().crawl_app_category_id_str+"&id="+$location.search().id+
                                            "&appnumber="+$location.search().appnumber+"&sys="+$location.search().sys+"&appname="+$location.search().appname+"&apptype="
                                            +$location.search().apptype+"&crawl_app_logo_url="+$location.search().crawl_app_logo_url)
                                    })
                                }
                            });
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("抓取信息保存失败","info");
                            $scope.aleadysure=false;
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该详情下的域名,参数正则,url正则已存在 无法添加!","info");
                            $scope.aleadysure=false;
                            return;
                        }
                    })
                }else{
                    var errqueue = validateService.getQueue();
                    gridService.xcConfirm(errqueue[0].info,"info");
                }
            }

            $scope.cancel = function () {
                window.wxc.xcConfirm("确认返回抓取详情页吗？", window.wxc.xcConfirm.typeEnum.warning, {
                    onOk: function (v) {
                        $scope.$apply(function () {
                            $location.url("/detailAleadyApp?crawl_app_category_id_str="+$location.search().crawl_app_category_id_str+"&id="+$location.search().id+
                                "&appnumber="+$location.search().appnumber+"&sys="+$location.search().sys+"&appname="+$location.search().appname+"&apptype="
                                +$location.search().apptype+"&crawl_app_logo_url="+$location.search().crawl_app_logo_url)
                        })
                    }
                })
            }

            $scope.queryhavior = function(val){
                $scope.grabBehaviorserive(val)
            }
            $scope.enterSearch = function(e,val){
                if(e.keyCode==13){
                    $scope.queryhavior(val);
                }
            }
            $scope.clickhavior = function (val) {
                $scope.grabBehavior = val
                $scope.searchtext = val.behavior_name
                $scope.aleadyfocus = !$scope.aleadyfocus
            }
            $scope.blurhavior = function (val) {
                $scope.grabBehaviorserive(val)
            }
            $scope.showhavior = function(val){
                $scope.aleadyfocus = true;
                if(val==""){
                    $scope.grabBehaviorserive()
                }
            }


        }])
});
