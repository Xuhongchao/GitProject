define(['angular'], function (angular) {
    return angular.module('dmpwebApp.controllers.modifydetailAleadyAppCtrl', [])
        .controller('modifydetailAleadyAppCtrl', ['$scope', '$location', '$http','$uibModal','getPage','gridService','$uibModalInstance','objselect','validateService', function ($scope,$location,$http,$uibModal,getPage,gridService,$uibModalInstance,objselect,validateService) {
            var $ctrl=this;
            $ctrl.name=objselect.appname;
            $ctrl.type=objselect.apptype;
            $ctrl.param=objselect.crawl_industry_name
            $scope.urlExample=objselect.crawl_detail_urlexample;
            $scope.urlregexp=objselect.crawl_detail_urlreg;
            $scope.domain=objselect.crawl_detail_domain;
            $scope.crawl_detail_id=objselect.crawl_detail_id;
            $scope.remarks=objselect.crawl_detail_comments;
            $scope.grabBehavior="";
            $scope.select=[]
            $scope.ordername = objselect.crawl_industry_name == "广告ID"?true:false;
            $scope.AdIdContent=""

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

            //抓取行为
            var beparam={
                crawl_app_category_id_str:$location.search().crawl_app_category_id_str
            }
            var beurl="/appCrawlerDone/queryAppCrawlBehavior.do"
            gridService.httpService(beurl,beparam).success(function (data) {
                $scope.grabBehaviors=data.behaviorList;
                for(var i=0;i<$scope.grabBehaviors.length;i++){
                    if($scope.grabBehaviors[i].behavior_id == objselect.crawl_detail_behavior_id){
                        $scope.grabBehavior = $scope.grabBehaviors[i];
                        return;
                    }
                }
            })
            $scope.changeparamreg = function (obj) {
                if(obj.param_reg.param_reg == "NULL"){
                    obj.emptyreg = true;
                    obj.param_key = "";
                    obj.param_content = "";
                }else{
                    obj.emptyreg = false;
                }
            }

            //参数分类
            var url="/appCrawlerDone/queryModifyAppIndustryInfo.do"
            var param={
                crawl_detail_id:objselect.crawl_detail_id
            }
            gridService.httpService(url,param).success(function (data) {
                try {

                    $scope.AdIdContent=data.AdIdContent
                    $scope.paramClassifys = data.allIndustry;
                    for (var i = 0; i < $scope.paramClassifys.length; i++) {
                        if ($scope.paramClassifys[i].industry_id == objselect.crawl_detail_industry_id) {
                            $scope.paramClassify = $scope.paramClassifys[i];
                            $scope.select = $scope.paramClassifys[i];
                            continue;
                        }
                    }

                    if(data.paramNum){
                        $scope.select.industryContentList[0].param_num = data.paramNum;
                    }

                    var param = objselect.crawl_detail_paramreg.split('\t');        //切割表格中的参数正则形成修改时的每行的参数正则
                    var regshow = data.reg.split('\t');                             //切割出每一行的正则具体是哪一个

                    if(objselect.crawl_industry_name == "广告ID"){
                        for (var i = 0; i < $scope.select.industryContentList.length; i++) {
                            if($scope.select.industryContentList[i].content_name == data.AdIdContent) {
                                $scope.indexorder = $scope.select.industryContentList[i].content_order
                                for (var j = 0; j < $scope.select.industryContentList[i].contentDetailList.length; j++) {
                                    if (regshow[0] == $scope.select.industryContentList[i].contentDetailList[j].param_reg) {
                                        // if(param[i].match(regshow[i])){
                                        $scope.select.industryContentList[i].param_key = param[0].split(regshow[0])[0] //正则前边输入框的值
                                        $scope.select.industryContentList[i].param_content = param[0].split(regshow[0])[1] //正则后面输入框的值
                                        $scope.select.industryContentList[i].param_reg = $scope.select.industryContentList[i].contentDetailList[j];
                                        if ($scope.select.industryContentList[i].param_reg.param_reg == "NULL") {
                                            $scope.select.industryContentList[i].emptyreg = true;
                                            $scope.select.industryContentList[i].param_key = ""
                                            $scope.select.industryContentList[i].param_content = ""
                                        }
                                        // }
                                    }
                                }
                            }
                        }
                    }else {
                        for (var i = 0; i < $scope.select.industryContentList.length; i++) {
                            for (var j = 0; j < $scope.select.industryContentList[i].contentDetailList.length; j++) {

                                if (regshow[i] == $scope.select.industryContentList[i].contentDetailList[j].param_reg) {
                                    $scope.select.industryContentList[i].param_key = param[i].split(regshow[i])[0] //正则前边输入框的值
                                    $scope.select.industryContentList[i].param_content = param[i].split(regshow[i])[1] //正则后面输入框的值
                                    $scope.select.industryContentList[i].param_reg = $scope.select.industryContentList[i].contentDetailList[j];
                                    if ($scope.select.industryContentList[i].param_reg.param_reg == "NULL") {
                                        $scope.select.industryContentList[i].emptyreg = true;
                                        $scope.select.industryContentList[i].param_key = ""
                                        $scope.select.industryContentList[i].param_content = ""
                                    }
                                }
                            }
                        }
                    }

                }
                catch(err)
                {
                    if(err.message.match(/\/\S+\//g)) {
                        if (err.message.split(":")[0] == "Invalid regular expression") {
                            var err_reg = err.message.match(/\/\S+\//g)[0].substr(1, err.message.match(/\/\S+\//g)[0].length - 2)
                            // var errorexp = [];
                            // for(var i=0;i<selectedObject.length;i++){
                            //     if(selectedObject[i].param_reg == err_reg){
                            //         errorexp=selectedObject[i]
                            //         break;
                            //     }
                            // }
                            // var txt="<div class='err-regex'>URL样例："+ errorexp.url_example +"<br />"+
                            var txt = "<div class='err-regex'>正则非法：" + err_reg + "<br />" +
                                "错误描述：" + err.message + "" +
                                "<span>点击确定继续。</span></div>"
                            gridService.xcConfirm(txt, "info")
                        }
                    }
                    return;
                }
            })
            $scope.paramtype = function (obj) {
                $scope.select=obj;
            }

            $ctrl.ok = function () {
                var validate = validateService.validate();
                if(validate == true){
                    if(objselect.crawl_industry_name == "广告ID"){
                        for(var i=0;i<$scope.select.industryContentList.length;i++){
                            if($scope.AdIdContent == $scope.select.industryContentList[i].content_name){
                                if($scope.select.industryContentList[i].param_reg){
                                    if($scope.select.industryContentList[i].param_reg.param_reg !== "NULL") {
                                        if (!$scope.select.industryContentList[i].param_key || !$scope.select.industryContentList[i].param_reg || !$scope.select.industryContentList[i].param_content) {
                                            gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                            return;
                                        }
                                    }
                                    if($scope.select.industryContentList[i].param_num){
                                        if(!$scope.select.industryContentList[i].param_num.match(/^[a-zA-Z]*$/g)){
                                            gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                            return;
                                        }
                                    }
                                }else{
                                    if (!$scope.select.industryContentList[i].param_key || !$scope.select.industryContentList[i].param_reg || !$scope.select.industryContentList[i].param_content) {
                                        gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                        return;
                                    }
                                    if($scope.select.industryContentList[i].param_num){
                                        if(!$scope.select.industryContentList[i].param_num.match(/^[a-zA-Z]*$/g)){
                                            gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                            return;
                                        }
                                    }
                                }
                                //参数正则针对特殊的四种情况（idfa，imei，androidid和mac）验证通过后添加的标识分别为1,2,3,4+输入的字母
                                $scope.select.industryContentList[i].param_num = $scope.select.industryContentList[i].param_num_order + ($scope.select.industryContentList[i].param_num?$scope.select.industryContentList[i].param_num:0);
                            }
                        }
                    }else{
                        for(var i=0;i<$scope.select.industryContentList.length;i++){
                            if($scope.select.industryContentList[i].param_reg){
                                if($scope.select.industryContentList[i].param_reg.param_reg !== "NULL") {
                                    if (!$scope.select.industryContentList[i].param_key || !$scope.select.industryContentList[i].param_reg || !$scope.select.industryContentList[i].param_content) {
                                        gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                        return;
                                    }
                                }
                                if($scope.select.industryContentList[i].param_num){
                                    if(!$scope.select.industryContentList[i].param_num.match(/^[a-zA-Z]*$/g)){
                                        gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                        return;
                                    }
                                }
                            }else{
                                if (!$scope.select.industryContentList[i].param_key || !$scope.select.industryContentList[i].param_reg || !$scope.select.industryContentList[i].param_content) {
                                    gridService.xcConfirm("选择的参数分类下的参数正则必须全部填写！", "info");
                                    return;
                                }
                                if($scope.select.industryContentList[i].param_num){
                                    if(!$scope.select.industryContentList[i].param_num.match(/^[a-zA-Z]*$/g)){
                                        gridService.xcConfirm("参数正则末位标识输入框只能输入字母！", "info");
                                        return;
                                    }
                                }
                            }
                            //参数正则针对特殊的四种情况（idfa，imei，androidid和mac）验证通过后添加的标识分别为1,2,3,4+输入的字母
                            $scope.select.industryContentList[i].param_num = $scope.select.industryContentList[i].param_num_order + ($scope.select.industryContentList[i].param_num?$scope.select.industryContentList[i].param_num:0);
                        }
                    }
                    var param={
                        urlExample:$scope.urlExample?$scope.urlExample:undefined,
                        grabBehavior:$scope.grabBehavior.behavior_id?$scope.grabBehavior.behavior_id:undefined,
                        urlregexp:$scope.urlregexp?$scope.urlregexp:undefined,
                        domain:$scope.domain?$scope.domain:undefined,
                        industryContentList:$scope.select,
                        remarks:$scope.remarks,
                        crawl_id:$location.search().id,
                        crawl_detail_id:$scope.crawl_detail_id?$scope.crawl_detail_id:undefined,
                        crawl_app_num:$location.search().appnumber,
                        crawl_app_os:$location.search().sys,
                    }
                    var url="/appCrawlerDone/modifyCrawlInfo.do";
                    gridService.httpService(url,param).success(function (data) {
                        $uibModalInstance.dismiss('cancel');
                        if(data.resultCode == "0"){
                            gridService.xcConfirm("修改成功","info");
                        }
                        if(data.resultCode == "1"){
                            gridService.xcConfirm("修改失败","info");
                            return;
                        }
                        if(data.resultCode == "2"){
                            gridService.xcConfirm("该参数正则已存在请重新输入！","info");
                            return;
                        }
                        getPage();
                    })
                }else{
                    var errqueue = validateService.getQueue();
                    gridService.xcConfirm(errqueue[0].info,"info");
                }
            }
            $ctrl.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }])
});
