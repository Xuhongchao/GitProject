angular.module('inputdropdown',[]).directive('inputdropdown', function($timeout,$compile,$parse) {
    return {
        restrict:'A',
        // transclude:true,
        // replace:true,
        scope : false,
        require:'ngModel',
        link: function(scope, iElem, iAttrs, ctrl){
            var ngName = iAttrs.name;
            var ngModel = iAttrs.ngModel;
            var ngvalidations = iAttrs.validations;
            var ngplaceholder = iAttrs.placeholder;
            var tElm = iElem;
            var dpdn_tpl,ngRep='';

            //dropdown模板
            var dpdn_tpl = '<input input-dropdown type="text" class="form-control" name="'+ ngName +'" validations="'+ngvalidations+'" ng-model="'+ngModel+'" '+
            'ng-model-options="{debounce:500}" ng-keyup="searchFirstClassify($event)" placeholder="'+ngplaceholder+'"/>'

            var dpdn_elm = angular.element(dpdn_tpl);
            //将模板编译并添加至原select前
            tElm.after($compile(dpdn_elm)(scope));
            tElm.remove();

            scope.$on('onselectClick',function(event,value){
                if(ngModel===value.model){
                    $parse(ngModel).assign(scope,value.value);
                    ctrl.$setViewValue(value.value);
                }
            })
            //监测select的disabled属性
           /* iAttrs.$observe('disabled',function(value){
                if(value)
                    dpdn_elm.find(".btn-select").attr("disabled","disabled");
                else
                    dpdn_elm.find(".btn-select").removeAttr("disabled","");
            });*/
            return;
        },
    };
}).directive('inputDropdown',function($timeout,$parse,$compile,$http){
    return {
        restrict:'A',
        scope : true,
        link:function($scope, iElem, iAttrs, ctrl){
            var ngModelStr = iAttrs.ngModel;
            var tElm = iElem;
            $scope.firstClassifyList=[];        //查询到的一级分类
            var ngRep = '';

            var dpdl_tpl_list = '<ul class="first-label-searchlist" ng-show="firstClassifyList.length>0 && firstClassifyList.showflag">'+
                '<li ng-repeat="firstClassify in firstClassifyList" title="{{firstClassify.name}}" ng-click="choiceobj(firstClassify)">{{firstClassify.name}}</li>'+
            '</ul>'

            $scope.choiceobj = function(v){
                $scope.firstClassifyList.showflag = false;
                $scope.$emit('onselectClick',{model:ngModelStr,value:v.name});
            }
            $(window).bind('click',function(e){
                var $div1 = $(tElm);    //输入框
                var $target = $(e.target);      //点击的对象
                if($target.attr('class') == $div1.attr('class')){       //如果点击的对象为输入框就不隐藏  否则就隐藏下拉框
                    $scope.$apply(function(){
                        $scope.firstClassifyList.showflag = true;
                    })
                }else{
                    $scope.$apply(function(){
                        $scope.firstClassifyList.showflag = false;
                    })
                }
            })

            //输入框内容发生变化的时候进行模糊匹配列表是否进行显示、
            $scope.$on('$destory',$scope.$watch(ngModelStr,function(newVal,oldVal){
                if(newVal !== oldVal){

                    //在一级分类里输入内容后模糊查找
                    var param = {
                        industry_name: $scope.paramClassify ? $scope.paramClassify : undefined,
                        content_name: $scope.param ? $scope.param : undefined,
                        content_order: $scope.paramOrder ? $scope.paramOrder : undefined,
                        // firstClassify: $scope.$parent.ngModelStr ? $scope.$parent.ngModelStr : undefined,    //正确的参数
                    }
                    var url = basePath+ "/appIndustry/addIndustry.do";
                    $http.post(url, param).success(function (data) {
                        var data = {
                            list: [
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                                {name: "qweqweqwewqeqweqweqwewqeqweqweqweqw"},
                            ]
                        }
                        $scope.firstClassifyList = data.list;
                        if (data.list.length > 0) {
                            $scope.firstClassifyList.showflag = true;
                            tElm.after($compile(dpdl_tpl_list)($scope));
                        } else {
                            $scope.firstClassifyList.showflag = false;
                        }
                    })
                }
            }));
            //
            // if($scope.firstClassifyList){
            //     ngRep += '<li ng-repeat="firstClassify in firstClassifyList" title="{{firstClassify.name}}" ng-click="choiceobj(firstClassify)">{{firstClassify.name}}</li>';
            //     tElm.find('.dropdown-menu').append($compile(ngRep)($scope));
            // }





            return;
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

            if(ngOptionsStr){
                //制作ng-repeat列表模板
                ngRep += '<li ng-repeat="val in renderList" ng-click="ngModelName ? selectClick(val) :\'\'" title="{{val[valParma]}}"><a href="javascript:;">{{val[valParma]}}</a></li>';
                tElm.find('.dropdown-menu').append($compile(ngRep)($scope));
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