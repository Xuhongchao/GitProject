angular.module('definedropdown',[]).directive('definedropdown', function($timeout,$compile,$parse) {
    return {
        restrict:'A',
        // transclude:true,
        // replace:true,
        scope : false,
        require:'ngModel',
        link: function(scope, iElem, iAttrs,ctrl){
            var ngOptionsStr = iAttrs.ngOptions,
                ngModelStr = iAttrs.ngModel;

            var tElm = iElem;

            var dpdn_tpl,ngRep='';

            //dropdown模板
            var dpdn_tpl = '<div class="dropdown dropdown-container" data-dropdown-tpl data-dropdown-search="'+iAttrs.dropdownSearch+'" dropdown-extend="'+iAttrs.dropdownExtend+'" data-dropdown-options="'+ngOptionsStr+'" data-dropdown-model="'+(ngModelStr?ngModelStr:'')+'" role="group"></div>';

            var dpdn_elm = angular.element(dpdn_tpl);
            //将模板编译并添加至原select前
            tElm.after($compile(dpdn_elm)(scope));
            // tElm.remove();
            tElm.css("display","none");

            scope.$on('onselectClick',function(event,value){
                if(ngModelStr===value.model){
                    $parse(ngModelStr).assign(scope,value.value);
                    ctrl.$setViewValue(value.value);
                }
            })

            //监测select的disabled属性
            iAttrs.$observe('disabled',function(value){
                if(value)
                    dpdn_elm.children().eq(0).attr("disabled","disabled");
                else
                    dpdn_elm.children().eq(0).removeAttr("disabled","");
            });
            return;
        },
    };
}).directive('dropdownTpl',function($timeout,$parse,$compile){
    return {
        restrict:'A',
        scope : true,
        template:'<button type="button" data-ng-class="{\'search-select-btn\':isSearch}" class="form-control btn-select dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">'
            +		'<span class="dropdown-value" title=\"{{showText}}\">{{showText}}</span>'
            +		'<div><span class="caret"></span></div>'
            // +	'<span class="caret"></span>'
            +	 '</button>'
            // +	 '<div data-search-slt-ipt data-ng-if="isSearch" class="clearfix search-ipt">'
            +	 '</div>'
            +	 '<ul class="dropdown-menu">'
            +	 '<div data-search-slt-ipt data-ng-if="isSearch" class="clearfix search-ipt">'
            +	 '</ul>',
        link:function($scope, tElm, iAttrs,ctrl){
            var tElm = $(tElm);
            var ngOptionsStr = iAttrs.dropdownOptions,
                ngModelStr = iAttrs.dropdownModel,
                dropdownSearch = iAttrs.dropdownSearch == ""?true:false;

            //获取ng-options信息
            var NG_OPTIONS_REGEXP = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/;
            var match = ngOptionsStr.match(NG_OPTIONS_REGEXP);
            /*var dataName = match[7];*/
            var varb = match[7],
                val = match[4],
                tpl = match[1];

            var ngRep = '';
            $scope.statusflag = false;

            $scope.ngModelName = ngModelStr;
            $scope.showText = ngOptionsStr ? ($scope.$parent.$eval($scope.ngModelName) ? $scope.$parent.$eval($scope.ngModelName)[tpl.split('.')[1]] : '') : tElm.find("option[value='"+tElm.val()+"']").text();
            $scope.isSearch = iAttrs.dropdownSearch === 'true' ? true : false;
            $scope.isExtend  = iAttrs.dropdownExtend == ""?true:false;      //只针对知识库v2.1版的转移应用特殊情况下拉框带图片和状态
            $scope.valParma = tpl.split('.')[1];

            $scope.showSearch = function(){
                $timeout(function(){
                    tElm.find('.form-search').focus();
                })
            }

            if(ngOptionsStr){
                $scope.selectClick = function(v){
                    $scope.$emit('onselectClick',{model:ngModelStr,value:v});
                }
            }else{
                $scope[ngModelStr+'_click'] = function(v){
                    tElm.val(v);
                    var text = tElm.find("option[value='"+(v?v:'')+"']").text();
                    dpdn_elm.find(".dropdown-value").text(text);
                }
            }
            if($scope.isExtend){        //只针对知识库v2.1版的转移应用特殊情况下拉框带图片和状态
                if(ngOptionsStr){
                    //制作ng-repeat列表模板
                    ngRep += '<li class="clearfix" ng-repeat="val in renderList" ng-click="ngModelName ? selectClick(val) :\'\'" title="{{val[valParma]}}">'+
                                '<img ng-show="val.app_id !== -1" ng-src="{{val.crawl_app_logo_url | logolurlchange}}">'+     //app应用的图片
                                // '<span ng-show="val.app_id !== -1">{{val.app_status | appStatus}}</span>'+          //app应用的状态
                                '<span ng-class="{notmatch:val.app_id == -1}">{{val[valParma]}}</span>'+      //app应用的名称
                             '</li>'
                    tElm.find('.dropdown-menu').append($compile(ngRep)($scope));
                }
            }else {
                if (ngOptionsStr) {
                    //制作ng-repeat列表模板
                    ngRep += '<li ng-repeat="val in renderList" ng-click="ngModelName ? selectClick(val) :\'\'" title="{{val[valParma]}}"><a href="javascript:;">{{val[valParma]}}</a></li>';
                    tElm.find('.dropdown-menu').append($compile(ngRep)($scope));
                } else {
                    //TODO
                    //将普通select-option转换成ng-repeat
                    var origEle = transclude(scope).filter("option");
                    origEle.each(function (i, e) {
                        var text = e.innerHTML;
                        var val = e.value;
                        ngRep += '<li ng-click="' + ngModelStr + '_click(' + val + ')"><a href="javascript:;">' + text + '</a></li>';
                        tElm.append(e);
                    });
                }
            }

            $scope.$on('$destory',$scope.$watch(ngModelStr,function(newVal,oldVal){
                $parse(ngModelStr).assign($scope.$parent,newVal);
                if(!$scope.$parent.$$phase)
                    $scope.$parent.$apply();
                $scope.showText = $scope.$parent.$eval(ngModelStr) ? $scope.$parent.$eval(ngModelStr)[tpl.split('.')[1]] : '';
            }));

            $scope.renderList = [].concat($scope.$parent.$eval(varb));

            $scope.$on('$destory',$scope.$parent.$watch(varb,function(newVal,oldVal){
                if(newVal!==oldVal){
                    if(ngOptionsStr){
                        $scope.renderList = [].concat($scope.$parent.$eval(varb));
                    }else{
                        //TODO
                        //将普通select-option转换成ng-repeat
                        var origEle = transclude(scope).filter("option");
                        origEle.each(function(i,e){
                            var text = e.innerHTML;
                            var val = e.value;
                            ngRep += '<li ng-click="'+ngModelStr+'_click('+val+')"><a href="javascript:;">'+text+'</a></li>';
                            tElm.append(e);
                        });
                    }
                }
            },true));

            var $element =tElm ,$attr = iAttrs;


            var tpl = match[1];
            /*var dataName = match[7];*/
            var clickArea = $element;
            var scrollArea = $element.find('.dropdown-menu');
            var dataCache = [];
            var origData = [];

            var pageSelectData = {
                pageSize:$attr.selectPage ? $attr.selectPage : 200,
                currentPage:1
            };

            /*添加滚动*/
            var addScrollListener = function(obj,fn){
                if(window.navigator.userAgent.toLowerCase('').indexOf('firefox')!=-1){
                    obj.addEventListener('DOMMouseScroll',function(e){
                        obj.scrollTop += e.detail > 0 ? 60 : -60;
                        fn(e);
                        e.preventDefault();
                    },false)
                }else{
                    obj.onmousewheel=function(e){
                        e = e || window.event;
                        obj.scrollTop += e.wheelDelta>0?-60:60;
                        fn(e);
                        e.returnValue = false ;
                    };
                }
            }

            /*分页处理函数*/
            var pageHandler = function(data){
                var dataArr;
                dataArr = $scope.$eval('renderList') ? $scope.$eval('renderList') : [];
                dataArr = dataArr.concat(data);
                btnFlag = false;
                return dataArr;
            }

            /*获取分页数据*/
            var getPageData = function(page,size,callback){
                var start = (pageSelectData.currentPage-1)*pageSelectData.pageSize,
                    end = pageSelectData.currentPage*pageSelectData.pageSize,
                    origArrLen = $scope.$eval('renderList') ? $scope.$eval('renderList').length : 0,
                    sliceArr = dataCache.slice(start,end),
                    newArr;
                var a = $scope.$parent.$eval(varb)
                newArr = (callback && (newArr = callback(sliceArr))) ? newArr : sliceArr;
                $parse('renderList').assign($scope,newArr);
                var newArrLen = $scope.$eval('renderList').length;
                if(origArrLen !== newArrLen){
                    isChanged = true;
                }else{

                }
            }

            /*判断是否到最后一页*/
            var isEnd = function(){
                return pageSelectData.currentPage*pageSelectData.pageSize > dataCache.length ? true : false;
            }

            var btnFlag = false;

            var isPage = false;


            var isChanged = false;
            var origData = []
            var isSearch = false;
            $scope.$on('searchTextChanged',function(event,info){
                if(info.name === ngModelStr){
                    var text = info.text ? info.text : '';
                    var newData = [];
                    for(var i=0,len=origData.length;i<len;i++){
                        if(origData[i][tpl.split('.')[1]].toUpperCase().indexOf(text.toUpperCase())>-1)
                            newData.push(origData[i]);
                    }

                    isSearch = true;
                    pageSelectData.currentPage = 1;
                    scrollArea.scrollTop(0);
                    dataCache = newData;

                    if($scope.isExtend) {        //只针对知识库v2.1版的转移应用特殊情况下拉框带图片和状态
                        if(newData.length>0){
                            if(newData[newData.length-1].app_id != "-1"){
                                newData.push({
                                    app_id: -1,
                                    crawl_app_name: "没有匹配项",
                                })
                            }
                        }else{
                            newData.push({
                                app_id: -1,
                                crawl_app_name: "没有匹配项",
                            })
                        }
                    }

                    $parse('renderList').assign($scope,newData);
                    getPageData(pageSelectData.pageSize,pageSelectData.currentPage);
                }
            });
            //监测初始化
            $scope.$on('$destory',$scope.$watch('renderList',function(newVal,oldVal){

                if(isChanged){
                    isChanged = false;
                    return;
                }
                if(isSearch){
                    isSearch = false;
                    return;
                }
                if(oldVal!==newVal){
                    var newArr = [];
                    newArr = newVal.slice(0,newVal.length);
                    origData = dataCache = newArr;
                    getPageData(pageSelectData.pageSize,pageSelectData.currentPage);
                    newVal.splice(pageSelectData.pageSize,newVal.length);
                }else if(newVal){
                    var newArr = [];
                    newArr = newVal.slice(0,newVal.length);
                    origData = dataCache = newArr;
                    getPageData(pageSelectData.pageSize,pageSelectData.currentPage);
                    newVal.splice(pageSelectData.pageSize,newVal.length);
                }
            },true));

            var isActive = false;
            var clickHandler = function(event){
                if(isActive){
                    pageSelectData.currentPage = 1;
                    getPageData(pageSelectData.pageSize,pageSelectData.currentPage);
                    isActive = false;
                }
            }

            //失去焦点初始化
            angular.element(document).on('click',function(event){
                clickHandler(event);
            })

            clickArea.on('click',function (event) {
                $timeout(function(){
                    scrollArea.scrollTop(0);
                    isActive = true;
                    if(dropdownSearch){
                        $scope.isSearch = true;
                        $scope.showSearch()
                    }
                });
            })
            addScrollListener(scrollArea[0],function(event){
                var scrollTop = scrollArea.scrollTop(),
                    height = scrollArea[0].offsetHeight;
                    scrollHeight = scrollArea[0].scrollHeight,
                    isDown = event.wheelDelta?(event.wheelDelta<0):(event.detail>0);
                if(scrollHeight>height){
                    if((scrollArea.scrollTop()+height >= scrollHeight) && isDown && !btnFlag){
                        if(!isEnd()){
                            pageSelectData.currentPage++;
                            btnFlag = true;
                            getPageData(pageSelectData.currentPage,pageSelectData.pageSize,pageHandler)
                            if(!$scope.$$phase)
                                $scope.$apply();
                        }
                    }
                }
            });
        }
    }
}).directive('searchSltIpt',function($timeout){
    return {
        restrict : 'A',
        template:'<input type="text"  class="form-control form-search" data-search-slt-ipt-inner data-ng-model="searchText" placeholder="请输入查询的名字">',
        // +'<div class="button-search" data-ng-click="queryByNumber(true)">'
        // +   '<i class="icon-search"></i>'
        // +'</div>',
        // link:function($scope,$element,$attr,ctrl){
        //     $scope.queryByNumber = function () {
        //         $scope.$parent.$parent.grabBehaviorserive($scope.searchText);
        //     }
        // },
    }
}).directive('searchSltIptInner',function($timeout){
    return {
        require: 'ngModel',
        priority:1,
        link:function($scope,$element,$attr,ctrl){
            ctrl.$viewChangeListeners.unshift(function(){
                $scope.$emit('searchTextChanged',{name:$scope.ngModelName,text:$scope.searchText});
            });
        }
    }
});