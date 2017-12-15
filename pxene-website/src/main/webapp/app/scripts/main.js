requirejs.config({
    //baseUrl: '',
    paths: {
        'angular': '../../bower_components/angular/angular.min',
        'angular-route': '../../bower_components/angular-route/angular-route',
        'appAll': './appAll',
        'angular-ui-tree': '../../bower_components/angular-ui-tree/dist/angular-ui-tree',
        'angular-ui-bootstrap': '../../bower_components/angular-bootstrap/ui-bootstrap-tpls.min',
        'angular-ui-grid': '../../bower_components/angular-ui-grid/ui-grid',
        'domReady': '../../bower_components/requirejs-domReady/domReady',
        'daterangepicker': '../../bower_components/daterangepicker/daterangepicker',
        'moment': '../../bower_components/daterangepicker/moment',
        'jquery': '../../bower_components/jquery/dist/jquery.min',
        'angularFileUpload': '../../bower_components/file-upload/angular-file-upload',
        'jqueryUiJqLoding': 'lib/jquery-ui-jqLoding',
        // 'alert': '../../bower_components/xcConfirm/js/xcConfirm',
        'boot': '../../bower_components/bootstrap/dist/js/bootstrap',
        // 'menuDisplay': 'lib/menuDisplay',
        // 'scrollArea': 'lib/scrollArea',

        'defineService':'lib/defineService',
        'moduleMenu':'lib/moduleMenu',
        'definedropdown':'lib/definedropdown',
        'xcConfirm': '../../bower_components/xcConfirm/js/xcConfirm',
        'qrcode':'lib/qrcode',
        'validation':'lib/validation',
        'countinfo':'lib/countinfo',
        'inputdropdown':'lib/inputdropdown',
        'remberHistory':'lib/remberHistory'
    },
    shim: {
        'angular': {exports: 'angular'},
        'angular-route': {deps: ['angular'], exports: 'ngRoute'},
        'angular-ui-tree': {deps: ['angular']},
        'angular-ui-bootstrap': {deps: ['angular']},
        'angular-ui-grid': {deps: ['angular']},
        'jquery': {exports: 'jquery'},
        'daterangepicker': {deps: ['jquery','moment']},
        'angularFileUpload': {deps: ['angular'], exports: 'angularFileUpload'},
        'jqueryUiJqLoding': {deps: ['jquery'],exports: 'jqueryUiJqLoding'},
        'alert': {deps: ['jquery'],exports: 'alert'},
        'boot':{deps:['jquery'],exports:'boot'},
        'menuDisplay': {deps:['angular','angular-ui-tree'],exports:'menuDisplay'},
        'scrollArea':{deps:['angular','angular-ui-tree'],exports:'scrollArea'},

        'defineService':{deps:['angular','angular-ui-grid','jquery'],exports:'defineService'},
        'moduleMenu':{deps:['angular','angular-ui-tree']},
        'definedropdown':{deps:['angular'],exports:'definedropdown'},
        'xcConfirm': {deps: ['jquery'],exports: 'xcConfirm'},
        'qrcode': {deps: ['jquery'],exports: 'qrcode'},
        'validation':{deps:['angular','jquery']},
        'countinfo':{deps:['angular','angular-ui-grid']},
        'inputdropdown':{deps:['angular','jquery'],exports:'inputdropdown'},
        'remberHistory':{deps:['angular'],exports:'remberHistory'},

    },
    deps: [
        './bootstrap'
    ],

    urlArgs: 'date=' + (new Date()).getTime(),


});


// 打包时的主入口增加函数
define(["appAll"], function(){})








