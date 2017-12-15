define([
        'angular',
        'angular-route',
        'angular-ui-tree',
        'angular-ui-bootstrap',
        'angular-ui-grid',
        'defineService',
        'controllers/index',    // 菜单
        'controllers/grabParamManage',
        'controllers/addParamType',
        'controllers/detailParam',
        'controllers/addParamRegex',
        'controllers/modifyParamRegex',
        'controllers/grabAppClassify',
        'controllers/grabBehaviorManage',
        'controllers/addBehavior',
        'controllers/grabWaitApp',
        'controllers/grabWaitAppIos',
        'controllers/grabWaitAppPc',
        'controllers/grabWaitAppMobile',
        'controllers/modifyWaitApp',
        'controllers/modifyPriority',
        'controllers/addApp',
        'controllers/addWebsite',
        'controllers/addAppClassify',       //v2.1
        'controllers/modifyAppClassify',       //v2.1
        'controllers/downloadApp',
        'controllers/translate',
        'controllers/grabAleadyApp',
        'controllers/modifyAleadyApp',
        'controllers/detailAleadyApp',
        'controllers/modifyAleadyAppDomation',      //v2.1
        'controllers/addAleadyAppDomation',      //v2.1
        'controllers/detailAleadyAppDomation',          //v2.1
        'controllers/addDetailAleadyAPP',
        'controllers/countInfoParam',
        'controllers/detailcountInfoParam',               //v2.1
        'controllers/countInfoAppClassify',
        'controllers/countInfoGrabBehavior',
        'controllers/detailcountInfoGrabBehavior',          //v2.1
        'controllers/countInfoAppSource',
        'controllers/countInfoUserNumber',
        'controllers/countInfoWechat',
        'controllers/modifydetailAleadyApp',
        'daterangepicker',
        'boot',
        'moduleMenu',
        'definedropdown',
        'xcConfirm',
        'qrcode',
        'validation',
        'angularFileUpload',
        'jqueryUiJqLoding',
        'countinfo',
        // 'inputdropdown',
        'remberHistory'
    ]/*deps*/,
    function (angular) {
        return angular.module('dmpwebApp', [
                'ngRoute',
                'ui.tree',
                'ui.bootstrap',
                'ui.grid',
                'ui.grid.pagination',
                'ui.grid.selection',
                'ui.grid.pinning',//滑动表格
                'defineService',
                'dmpwebApp.controllers.indexCtrl',
                'dmpwebApp.controllers.grabParamManageCtrl',
                'dmpwebApp.controllers.addParamTypeCtrl',
                'dmpwebApp.controllers.detailParamCtrl',
                'dmpwebApp.controllers.addParamRegexCtrl',
                'dmpwebApp.controllers.modifyParamRegexCtrl',
                'dmpwebApp.controllers.grabAppClassifyCtrl',
                'dmpwebApp.controllers.grabBehaviorManageCtrl',
                'dmpwebApp.controllers.addBehaviorCtrl',
                'dmpwebApp.controllers.grabWaitAppCtrl',
                'dmpwebApp.controllers.grabWaitAppIosCtrl',
                'dmpwebApp.controllers.grabWaitAppPcCtrl',
                'dmpwebApp.controllers.grabWaitAppMobileCtrl',
                'dmpwebApp.controllers.modifyWaitAppCtrl',
                'dmpwebApp.controllers.modifyPriorityCtrl',
                'dmpwebApp.controllers.addAppCtrl',
                'dmpwebApp.controllers.addWebsiteCtrl',
                'dmpwebApp.controllers.addAppClassifyCtrl',     //v2.1
                'dmpwebApp.controllers.modifyAppClassifyCtrl',     //v2.1
                'dmpwebApp.controllers.downloadAppCtrl',
                'dmpwebApp.controllers.translateCtrl',
                'dmpwebApp.controllers.grabAleadyAppCtrl',
                'dmpwebApp.controllers.modifyAleadyAppCtrl',
                'dmpwebApp.controllers.modifyAleadyAppDomationCtrl',    //v2.1
                'dmpwebApp.controllers.addAleadyAppDomationCtrl',    //v2.1
                'dmpwebApp.controllers.detailAleadyAppCtrl',
                'dmpwebApp.controllers.detailAleadyAppDomationCtrl',        //v2.1
                'dmpwebApp.controllers.addDetailAleadyAPPCtrl',
                'dmpwebApp.controllers.countInfoParamCtrl',
                'dmpwebApp.controllers.detailcountInfoParamCtrl',         //v2.1
                'dmpwebApp.controllers.countInfoAppClassifyCtrl',
                'dmpwebApp.controllers.countInfoGrabBehaviorCtrl',
                'dmpwebApp.controllers.detailcountInfoGrabBehaviorCtrl',          //v2.1
                'dmpwebApp.controllers.countInfoAppSourceCtrl',
                'dmpwebApp.controllers.countInfoUserNumberCtrl',
                'dmpwebApp.controllers.countInfoWechatCtrl',
                'dmpwebApp.controllers.modifydetailAleadyAppCtrl',
                'moduleMenu',
                'definedropdown',
                'validation',
                'angularFileUpload',
                'countinfo',
                // 'inputdropdown'
                'remberHistory',
            ]).config(function($routeProvider) {
            $routeProvider.when('/grabParamManage', {
                templateUrl : 'app/views/grabParamManage.html',
            }).when('/detailParam', {
                templateUrl : 'app/views/detailParam.html',
            }).when('/grabAppClassify', {
                templateUrl : 'app/views/grabAppClassify.html',
            }).when('/grabBehaviorManage', {
                templateUrl : 'app/views/grabBehaviorManage.html',
            }).when('/grabWaitApp', {
                templateUrl : 'app/views/grabWaitApp.html',
            }).when('/grabWaitAppMobile', {
                templateUrl : 'app/views/grabWaitAppMobile.html',
            }).when('/grabWaitAppIos', {
                templateUrl : 'app/views/grabWaitAppIos.html',
            }).when('/grabWaitAppPc', {
                templateUrl : 'app/views/grabWaitAppPc.html',
            }).when('/grabAleadyApp', {
                templateUrl : 'app/views/grabAleadyApp.html',
            }).when('/detailAleadyApp', {
                templateUrl : 'app/views/detailAleadyApp.html',
            }).when('/detailAleadyAppDomation', {
                templateUrl : 'app/views/detailAleadyAppDomation.html',
            }).when('/addDetailAleadyAPP', {
                templateUrl : 'app/views/addDetailAleadyAPP.html',
            }).when('/countInfoParam', {
                templateUrl : 'app/views/countInfoParam.html',
            }).when('/detailcountInfoParam', {
                templateUrl : 'app/views/detailcountInfoParam.html',
            }).when('/countInfoAppClassify', {
                templateUrl : 'app/views/countInfoAppClassify.html',
            }).when('/countInfoGrabBehavior', {
                templateUrl : 'app/views/countInfoGrabBehavior.html',
            }).when('/detailcountInfoGrabBehavior', {
                templateUrl : 'app/views/detailcountInfoGrabBehavior.html',
            }).when('/countInfoAppSource', {
                templateUrl : 'app/views/countInfoAppSource.html',
            }).when('/countInfoUserNumber', {
                templateUrl : 'app/views/countInfoUserNumber.html',
            }).when('/countInfoWechat', {
                templateUrl : 'app/views/countInfoWechat.html',
            }).otherwise({
                redirectTo : "/grabParamManage"
            });
        })

    });
