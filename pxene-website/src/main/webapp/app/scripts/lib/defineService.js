//http请求和grid表格动态表头展示,alert提示框和弹窗模块，以及过滤器
angular.module('defineService',['ui.grid','ui.grid.pagination'])
.factory('gridService',function($timeout,$http,$templateCache,$uibModal,uiGridPaginationService,uiGridConstants,$parse,$location){
        var $scope="";
        var newColumnDefs=[];
        var aleadysearch=false;
        var contact = [];       //恢复历史数据保存的url
        var listMenu = [];      //目录结构

        function initColumnDefs() {
            $scope.paginationOptions = {
                pageNumber: 1,
                pageSize: 10,
                sort: null,
                flag:true
            };
            $scope.gridOptions = {
                hpage:1,
                shownumber:1,
                enableHorizontalScrollbar :  0,
                enableVerticalScrollbar : 0,
                paginationPageSizes: [10, 15, 20],
                paginationPageSize: 10,
                enableRowHeaderSelection : false,
                enableRowSelection : false,
                rowHeight:39,
                headerRowHeight:39,
                enablePaginationControls: true,
                totalItems : 0, // 总数量
                enableSorting: true,
                gridOverflowVisible:true,//去掉表格的超出隐藏的样式
                useExternalPagination: true,//是否使用分页按钮
                useExternalSorting: true,//是否使用额外的排序
                columnDefs: [],
                onRegisterApi: function (gridApi,col) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            $scope.paginationOptions.sort = null;
                        } else {
                            $scope.paginationOptions.sort = sortColumns[0].sort.direction;
                        }
                        // if(!$scope.typeName && !$scope.typeNumber){
                        //     aleadysearch = false;
                        // }
                        if(aleadysearch){
                            $scope.getPage(aleadysearch);
                        }else{
                            $scope.getPage();
                        }
                    });
                    gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        if($scope.paginationOptions.flag){
                            $scope.paginationOptions.pageNumber = newPage;
                            $scope.paginationOptions.pageSize = pageSize;
                            // if(!$scope.typeName && !$scope.typeNumber){
                            //     aleadysearch = false;
                            // }
                            if(aleadysearch){
                                $scope.getPage(aleadysearch);
                            }else{
                                $scope.getPage();
                            }
                        }else{
                            $scope.paginationOptions.flag=true;
                        }
                    });
                    // $scope.gridApi.grid.flagScrollingHorizontally=function (scrollEvent) {
                    //     if (!self.isScrollingVertically && !self.isScrollingHorizontally) {
                    //         self.api.core.raise.scrollBegin(scrollEvent);
                    //     }
                    //     self.isScrollingHorizontally = true;
                    //     if (self.options.scrollDebounce === 0 || !scrollEvent.withDelay) {
                    //         debouncedHorizontalMinDelay(scrollEvent);
                    //     }
                    //     else {
                    //         debouncedHorizontal(scrollEvent);
                    //     }
                    // }
                    // $scope.gridApi.grid.flagScrollingHorizontally()

                },
                // customScroller: function myScrolling(uiGridViewport, scrollHandler) {
                //     uiGridViewport.on('scroll', function myScrollingOverride(event) {
                //         $scope.scroll.top = uiGridViewport[0].scrollTop;
                //         $scope.scroll.left = uiGridViewport[0].scrollLeft;
                //
                //         // You should always pass the event to the callback since ui-grid needs it
                //         scrollHandler(event);
                //     });
                // }

            };
        }
        function initEmptyGrid($scope) {
            var watch = $scope.$watch('gridOptions.totalItems',function(newValue){
                if(newValue == "0"){
                    var t_html='<span class="emptyGridMessage">没有匹配的结果,请再次尝试...</span>'
                    $(".ui-grid-contents-wrapper").css("display","none").after(t_html)
                    $(".ui-grid-pager-panel").css("display","none")
                    // $(".ui-grid-contents-wrapper").append(t_html).addClass("grid-viewport-empty")
                    // $(".ui-grid-pager-panel").addClass("grid-pager-panel-empty")
                    // $(".ui-grid-contents-wrapper").empty().append(t_html).addClass("grid-viewport-empty");
                    // $(".ui-grid-contents-wrapper").find(".ui-grid-pager-panel").empty().append(t_html).addClass("grid-pager-panel-empty")
                    // $(".myGrid").find(".ui-grid-viewport").append(t_html).addClass("grid-viewport-empty");
                    // $(".myGrid").find(".ui-grid-pager-panel").addClass("grid-pager-panel-empty");
                }else{
                    $(".ui-grid-contents-wrapper").css("display","block")
                    $(".emptyGridMessage").remove()
                    $(".ui-grid-pager-panel").css("display","block")
                    // $(".myGrid").find(".ui-grid-viewport").removeClass("grid-viewport-empty").find(".emptyGridMessage").remove();
                    // $(".myGrid").find(".ui-grid-pager-panel").removeClass("grid-pager-panel-empty");
                }
            });
        }
        function rowHeaderSelection(type) {
            if(type=="checkbox"){
                $scope.gridOptions.enableRowHeaderSelection = true;
                $scope.gridOptions.multiSelect = true;
                $templateCache.put('ui-grid/selectionSelectAllButtons',
                    "<input type=\"checkbox\" ng-checked=\"grid.selection.selectAll\" class=\"ui-grid-selection-row-header-buttons ui-grid-icon-ok\" ng-class=\"{'ui-grid-all-selected': grid.selection.selectAll}\" ng-click=\"headerButtonClick($event)\">"
                );
                $templateCache.put('ui-grid/selectionRowHeaderButtons',
                    "<input type=\"checkbox\" ng-checked=\"row.isSelected\" class=\"ui-grid-selection-row-header-buttons ui-grid-icon-ok  new Date().getTime()\" ng-class=\"{'ui-grid-row-selected': row.isSelected}\" ng-click=\"selectButtonClick(row, $event)\">"
                );

            }else if(type=="radio"){
                $scope.gridOptions.enableRowHeaderSelection = true;
                $scope.gridOptions.multiSelect = false;
                $templateCache.put('ui-grid/selectionSelectAllButtons',
                   "<div></div>"
                );
                $templateCache.put('ui-grid/selectionRowHeaderButtons',
                    "<input type=\"radio\" ng-checked=\"row.isSelected\" class=\"ui-grid-selection-row-header-buttons ui-grid-icon-ok new Date().getTime()\" ng-class=\"{'ui-grid-row-selected': row.isSelected}\" ng-click=\"selectButtonClick(row, $event)\">"
                );
            }
            $templateCache.get('ui-grid/selectionRowHeaderButtons')
        }
        function packagePagination(type){
            /* 在ui-grid.js里面的配置
            在服务uiGridPaginationService里的methods: { pagination: { 添加方法GotoPage: function (index,current) {
                grid.options.shownumber=index;
                if(!current){
                    grid.options.hpage=1;
                }
                grid.options.paginationCurrentPage=current;
                return grid.options.enablePagination ? grid.options.paginationCurrentPage : null;
            }} }
            然后在服务uiGridPager里
            添加$scope.GotoPage = function (index,currentpage) {
                options.shownumber=index
                $scope.paginationApi.GotoPage(index,currentpage);
            }*/
            if(type == "jia-wang"){
                // shownumber为当前页码的样式，hpage为第一个页码数
                /*在ui-grid.js里面的 $scope.pageNextPageClick方法里添加
                if(options.shownumber==1){
                    // $scope.shownumber=2;
                    options.shownumber=2;
                }else if(options.shownumber==2){
                    // $scope.shownumber=3;
                    options.shownumber=3;
                }else{
                    options.hpage=options.hpage+1;
                }
                $scope.pagePreviousPageClick里添加
                if(options.shownumber==3){
                    // $scope.shownumber=2;
                    options.shownumber=2;
                }else if(options.shownumber==2){
                    // $scope.shownumber=1;
                    options.shownumber=1;
                }else{
                    options.hpage=options.hpage-1;
                }
                在$scope.pageFirstPageClick里添加
                options.shownumber=1;
                options.hpage=1;
                在 $scope.pageLastPageClick里添加
                if($scope.paginationApi.getTotalPages()==1){
                    // $scope.shownumber=1;
                    options.shownumber=1;
                    options.hpage=1;
                }else if($scope.paginationApi.getTotalPages()==2){
                    // $scope.shownumber=2;
                    options.shownumber=2;
                    options.hpage=1;
                }else{
                    options.hpage=1;
                    // $scope.shownumber=3;
                    options.shownumber=3
                    if($scope.paginationApi.getTotalPages()>3){
                        options.hpage=$scope.paginationApi.getTotalPages()-2;
                    }
                }*/
                $templateCache.put('ui-grid/pagination',
                    "<div role=\"contentinfo\" class=\"clearfix mt10 ui-grid-pager-panel\" ui-grid-pager ng-show=\"grid.options.enablePaginationControls\"><div role=\"navigation\" class=\"ui-grid-pager-container pull-right grid-pager-content\"><div role=\"menubar\" class=\"ui-grid-pager-control pull-right\">" +
                    // "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-first\" ui-grid-one-bind-title=\"aria.pageToFirst\" ui-grid-one-bind-aria-label=\"aria.pageToFirst\" ng-click=\"pageFirstPageClick()\" ng-disabled=\"cantPageBackward()\"><div ng-class=\"grid.isRTL() ? 'last-triangle' : 'first-triangle'\"><div ng-class=\"grid.isRTL() ? 'last-bar-rtl' : 'first-bar'\"></div></div></button> <button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-previous\" ui-grid-one-bind-title=\"aria.pageBack\" ui-grid-one-bind-aria-label=\"aria.pageBack\" ng-click=\"pagePreviousPageClick()\" ng-disabled=\"cantPageBackward()\"><div ng-class=\"grid.isRTL() ? 'last-triangle prev-triangle' : 'first-triangle prev-triangle'\"></div></button> " +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-first\" ui-grid-one-bind-title=\"aria.pageToFirst\" ui-grid-one-bind-aria-label=\"aria.pageToFirst\" ng-click=\"pageFirstPageClick()\" ng-disabled=\"cantPageBackward()\">«</button> " +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-previous\" ui-grid-one-bind-title=\"aria.pageBack\" ui-grid-one-bind-aria-label=\"aria.pageBack\" ng-click=\"pagePreviousPageClick()\" ng-disabled=\"cantPageBackward()\">‹</button> " +
                    "<div class=\"pager-panel-list\">" +
                    "<button ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==1}\"  ng-click=\"GotoPage(1,grid.options.hpage)\">{{grid.options.hpage}}{{}}</button>" +
                    "<button ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==2}\"  ng-click=\"GotoPage(2,grid.options.hpage+1)\" ng-show=\"paginationApi.getTotalPages()>1 && grid.options.hpage<paginationApi.getTotalPages()\">{{grid.options.hpage+1}}</button>" +
                    "<button ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==3}\"  ng-click=\"GotoPage(3,grid.options.hpage+2)\" ng-show=\"paginationApi.getTotalPages()>2 && grid.options.hpage+1<paginationApi.getTotalPages()\">{{grid.options.hpage+2}}</button>" +
                    "</div>" +
                    // "<abbr ui-grid-one-bind-title=\"paginationOf\">/</abbr> {{ paginationApi.getTotalPages() }}</span>" +
                    // "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-next\" ui-grid-one-bind-title=\"aria.pageForward\" ui-grid-one-bind-aria-label=\"aria.pageForward\" ng-click=\"pageNextPageClick()\" ng-disabled=\"cantPageForward()\"><div ng-class=\"grid.isRTL() ? 'first-triangle next-triangle' : 'last-triangle next-triangle'\"></div></button> " +
                    // "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-last\" ui-grid-one-bind-title=\"aria.pageToLast\" ui-grid-one-bind-aria-label=\"aria.pageToLast\" ng-click=\"pageLastPageClick()\" ng-disabled=\"cantPageToLast()\"><div ng-class=\"grid.isRTL() ? 'first-triangle' : 'last-triangle'\"><div ng-class=\"grid.isRTL() ? 'first-bar-rtl' : 'last-bar'\"></div></div></button></div>" +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-next\" ui-grid-one-bind-title=\"aria.pageForward\" ui-grid-one-bind-aria-label=\"aria.pageForward\" ng-click=\"pageNextPageClick()\" ng-disabled=\"cantPageForward()\">›</button> " +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-last\" ui-grid-one-bind-title=\"aria.pageToLast\" ui-grid-one-bind-aria-label=\"aria.pageToLast\" ng-click=\"pageLastPageClick()\" ng-disabled=\"cantPageToLast()\">»</button></div>" +
                    // "<div class=\"ui-grid-pager-row-count-picker\" ng-if=\"grid.options.paginationPageSizes.length > 1\"><span ui-grid-one-bind-id-grid=\"'items-per-page-label'\" class=\"ui-grid-pager-row-count-label\">&nbsp;{{sizesLabel}}</span><span class=\"pageselecFrame\"><select  class=\"ui-grid-selectpagesize dropdown-select-pagesize\" ui-grid-one-bind-aria-labelledby-grid=\"'items-per-page-label'\" ng-model=\"grid.options.paginationPageSize\" ng-options=\"o as o for o in grid.options.paginationPageSizes\"></select><span class=\"pagesizeCaret caret\"></span></span><span>{{totalItemsLabel}}</span></div>" +
                    "<div class=\"ui-grid-pager-row-count-picker\" ng-if=\"grid.options.paginationPageSizes.length > 1\">" +
                    "<span ui-grid-one-bind-id-grid=\"'items-per-page-label'\" class=\"ui-grid-pager-row-count-label\">" +
                    "&nbsp;{{sizesLabel}}</span>" +
                    "<span class=\"pageselecFrame\">" +
                    "<div class=\"dropdown-select-pagesize\" ng-click=\"showpagination()\">{{grid.options.paginationPageSize}}</div>"+
                    "<span class=\"pagesizeCaret caret\"></span>" +
                    "<ul class=\"pagsize-select-list\" ng-show=\"pagesizeflag\"><li ng-click=\"choicePagsizelist(o)\" ng-repeat=\"o in grid.options.paginationPageSizes\">{{o}}</li></ul>"+
                    "</span>" +
                    "<span>{{totalItemsLabel}}</span>" +
                    "</div>" +
                    // "<span ng-if=\"grid.options.paginationPageSizes.length <= 1\" class=\"ui-grid-pager-row-count-label\">{{grid.options.paginationPageSize}}&nbsp;{{sizesLabel}}</span></div><div class=\"ui-grid-pager-count-container\"><div class=\"ui-grid-pager-count\"><span ng-show=\"grid.options.totalItems > 0\">{{showingLow}} <abbr class=\"ui-totalitems-pagination\" ui-grid-one-bind-title=\"paginationThrough\">-</abbr> {{showingHigh}} {{paginationOf}} {{grid.options.totalItems}} {{totalItemsLabel}}</span></div></div></div>"
                    "<span ng-if=\"grid.options.paginationPageSizes.length <= 1\" class=\"ui-grid-pager-row-count-label\">{{grid.options.paginationPageSize}}&nbsp;{{sizesLabel}}</span></div><div class=\"ui-grid-pager-count-container\"><div class=\"ui-grid-pager-count\"><span>{{paginationOf}} {{grid.options.totalItems}} {{totalItemsLabel}}</span></div></div></div>"

                );
            }
            if(type == "quan-hu"){
                $templateCache.put('ui-grid/pagination',
                    "<div role=\"contentinfo\" class=\"clearfix mt10 ui-grid-pager-panel\" ui-grid-pager ng-show=\"grid.options.enablePaginationControls\"><div role=\"navigation\" class=\"ui-grid-pager-container pull-right grid-pager-content\"><div role=\"menubar\" class=\"ui-grid-pager-control pull-right\">" +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-first mr10\" ui-grid-one-bind-title=\"aria.pageToFirst\" ui-grid-one-bind-aria-label=\"aria.pageToFirst\" ng-click=\"pageFirstPageClick()\" ng-disabled=\"cantPageBackward()\">首页</button> " +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-previous\" ui-grid-one-bind-title=\"aria.pageBack\" ui-grid-one-bind-aria-label=\"aria.pageBack\" ng-click=\"pagePreviousPageClick()\" ng-disabled=\"cantPageBackward()\">上一页</button> " +
                    "<div class=\"pager-panel-list\">" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==1}\"  ng-click=\"GotoPage(1,grid.options.hpage)\">{{grid.options.hpage}}</button>" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==2}\"  ng-click=\"GotoPage(2,grid.options.hpage+1)\" ng-show=\"paginationApi.getTotalPages()>1 && grid.options.hpage<paginationApi.getTotalPages()\">{{grid.options.hpage+1}}</a>" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==3}\"  ng-click=\"GotoPage(3,grid.options.hpage+2)\" ng-show=\"paginationApi.getTotalPages()>2 && grid.options.hpage+1<paginationApi.getTotalPages()\">{{grid.options.hpage+2}}</a>" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==4}\"  ng-click=\"GotoPage(4,grid.options.hpage+3)\" ng-show=\"paginationApi.getTotalPages()>3 && grid.options.hpage+2<paginationApi.getTotalPages()\">{{grid.options.hpage+3}}</a>" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-class=\"{ngcurrent:grid.options.shownumber==5}\"  ng-click=\"GotoPage(5,grid.options.hpage+4)\" ng-show=\"paginationApi.getTotalPages()>4 && grid.options.hpage+3<paginationApi.getTotalPages()\">{{grid.options.hpage+4}}</a>" +
                    "<a ui-grid-one-bind-title=\"aria.pageSelected\" ui-grid-one-bind-aria-label=\"aria.pageSelected\" class=\"ui-grid-pager-control-input\" ng-model=\"grid.options.paginationCurrentPage\" min=\"1\" max=\"{{ paginationApi.getTotalPages() }}\" ng-show=\"paginationApi.getTotalPages()>5 && grid.options.hpage+4<paginationApi.getTotalPages()\">...</a>" +
                    "</div>" +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-next\" ui-grid-one-bind-title=\"aria.pageForward\" ui-grid-one-bind-aria-label=\"aria.pageForward\" ng-click=\"pageNextPageClick()\" ng-disabled=\"cantPageForward()\">下一页</button> " +
                    "<button type=\"button\" role=\"menuitem\" class=\"ui-grid-pager-last ml10\" ui-grid-one-bind-title=\"aria.pageToLast\" ui-grid-one-bind-aria-label=\"aria.pageToLast\" ng-click=\"pageLastPageClick()\" ng-disabled=\"cantPageToLast()\">末页</button></div>" +
                    "<div class=\"ui-grid-pager-row-count-picker\" ng-if=\"grid.options.paginationPageSizes.length > 1\">" +
                    "<span ui-grid-one-bind-id-grid=\"'items-per-page-label'\" class=\"ui-grid-pager-row-count-label\">" +
                    "&nbsp;{{sizesLabel}}</span>" +
                    "<span class=\"pageselecFrame\">" +
                    "<div class=\"dropdown-select-pagesize\" ng-click=\"showpagination()\">{{grid.options.paginationPageSize}}</div>"+
                    "<span class=\"pagesizeCaret caret\"></span>" +
                    "<ul class=\"pagsize-select-list\" ng-show=\"pagesizeflag\"><li ng-click=\"choicePagsizelist(o)\" ng-repeat=\"o in grid.options.paginationPageSizes\">{{o}}</li></ul>"+
                    "</span>" +
                    "<span>{{totalItemsLabel}}{{'记录'}}</span>" +
                    "</div>" +
                    "<span ng-if=\"grid.options.paginationPageSizes.length <= 1\" class=\"ui-grid-pager-row-count-label\">{{grid.options.paginationPageSize}}&nbsp;{{sizesLabel}}</span></div><div class=\"ui-grid-pager-count-container\"><div class=\"ui-grid-pager-count\"><span ng-show=\"grid.options.totalItems > 0\">{{'当前显示 '}}{{showingLow}}<span class=\"ui-totalitems-pagination\" ui-grid-one-bind-title=\"paginationThrough\">{{' 到 '}}</span> {{showingHigh}} {{'条,'}} {{paginationOf}} {{grid.options.totalItems}} {{totalItemsLabel}}{{'记录'}}</span></div></div></div>"

                );
                $templateCache.put('ui-grid/uiGridHeaderCell',
                    "<div role=\"columnheader\" ng-class=\"{ 'sortable': sortable}\" ui-grid-one-bind-aria-labelledby-grid=\"col.uid + '-header-text ' + col.uid + '-sortdir-text'\" aria-sort=\"{{col.sort.direction == asc ? 'ascending' : ( col.sort.direction == desc ? 'descending' : (!col.sort.direction ? 'none' : 'other'))}}\"><div role=\"{{sortable?button:''}}\" tabindex=\"0\" class=\"ui-grid-cell-contents ui-grid-header-cell-primary-focus\" col-index=\"renderIndex\" title=\"TOOLTIP\" ng-style=\"{textAlign:col.cellCenter}\"><span class=\"ui-grid-header-cell-label\" ui-grid-one-bind-id-grid=\"col.uid + '-header-text'\">{{ col.displayName CUSTOM_FILTERS }}</span><span class=\"sortSpan\" ui-grid-one-bind-id-grid=\"col.uid + '-sortdir-text'\" ui-grid-visible=\"col.sort.direction\" aria-label=\"{{getSortDirectionAriaLabel()}}\">" +
                    "<i style=\"color:#fb8446\" ng-class=\"{ 'ui-grid-icon-up-dir': col.sort.direction == asc, 'ui-grid-icon-down-dir': col.sort.direction == desc, 'ui-grid-icon-blank': !col.sort.direction }\" title=\"{{isSortPriorityVisible() ? i18n.headerCell.priority + ' ' + ( col.sort.priority + 1 )  : null}}\" aria-hidden=\"true\"></i>" +
                    "<i style=\"color:#cecece\" ng-class=\"{ 'ui-grid-icon-down-dir': col.sort.direction == asc, 'ui-grid-icon-up-dir': col.sort.direction == desc, 'ui-grid-icon-blank': !col.sort.direction }\" title=\"{{isSortPriorityVisible() ? i18n.headerCell.priority + ' ' + ( col.sort.priority + 1 )  : null}}\" aria-hidden=\"true\"></i>" +
                    "<sub ui-grid-visible=\"isSortPriorityVisible()\" class=\"ui-grid-sort-priority-number\"><!--{{col.sort.priority + 1}}--></sub></span></div><div role=\"button\" tabindex=\"0\" ui-grid-one-bind-id-grid=\"col.uid + '-menu-button'\" class=\"ui-grid-column-menu-button\" ng-if=\"grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false\" ng-click=\"toggleMenu($event)\" ng-class=\"{'ui-grid-column-menu-button-last-col': isLastCol}\" ui-grid-one-bind-aria-label=\"i18n.headerCell.aria.columnMenuButtonLabel\" aria-haspopup=\"true\"><i class=\"ui-grid-icon-angle-down\" aria-hidden=\"true\">&nbsp;</i></div><div ui-grid-filter></div></div>"
                );
            }
        }
        function configColumnDefs(newColumnDefs){
            for(var i=0;i<newColumnDefs.length;i++){
                newColumnDefs[i].enableColumnMenu = false;     //是否隐藏头部菜单
                newColumnDefs[i].cellTooltip = true;        //列的title
                newColumnDefs[i].headerTooltip = true;      //头部title
                newColumnDefs[i].cellCenter = "center";     //头部和列内容居中
                if(!newColumnDefs[i].suppressRemoveSort){
                    newColumnDefs[i].enableSorting = false;
                }else{
                    newColumnDefs[i].sort = {direction: uiGridConstants.DESC};
                }
            }
        }

        function filterColumnDefs(columnDefs,list){
            if(list){
                // newColumnDefs=[];
                for(var i=0;i<columnDefs.length;i++){
                    if (columnDefs[i].field == "handle") {
                        columnDefs.splice(i,1);
                        return;
                    }
                    /*for(key in list[0]){
                        if(columnDefs[i].field == key) {
                            newColumnDefs.push(columnDefs[i])
                        }
                    }*/
                }
            }
        }
        function refushPage(){
            if($scope.gridApi.grid.options.paginationCurrentPage != 1){
                    $scope.gridApi.grid.options.paginationCurrentPage=1;
                    $scope.paginationOptions.pageNumber=1;
                    // $scope.paginationOptions.shownumber=1;
                    $scope.paginationOptions.flag=false;
                    $scope.gridApi.pagination.GotoPage(1)
                    $scope.getPage("refush");
                }else{
                    $scope.getPage("refush");
                }
        }
        function xcConfirm() {
            var url="";
            var name="";
            var param={};
            var arguments=arguments[0]
            if(arguments.length < 3){
                 window.wxc.xcConfirm(arguments[0], arguments[1]);
            }else{
                if(arguments.length < 4) {
                    if(typeof(arguments[0])=="string" && arguments[1] == "info" && typeof(arguments[2])=="object"){
                        window.wxc.xcConfirm(arguments[0], window.wxc.xcConfirm.typeEnum.info,arguments[2])
                    }else {
                        if(arguments[1] == "warning"){
                            window.wxc.xcConfirm(arguments[0],window.wxc.xcConfirm.typeEnum.warning,arguments[2])
                        }else{
                            for (var i = 0; i < arguments.length; i++) {
                                if (typeof(arguments[i]) == "string") {
                                    if (arguments[i].substr(arguments[i].length - 3) == ".do") {
                                        url = basePath + arguments[i];
                                    } else {
                                        name = arguments[i];
                                    }
                                } else {
                                    param = arguments[i];
                                }
                            }
                            window.wxc.xcConfirm("是否确定删除  " + name + "?", window.wxc.xcConfirm.typeEnum.warning, {
                                onOk: function (v) {
                                    $http.post(url, param).success(function (data) {
                                        if (data.resultCode == 0) {
                                            window.wxc.xcConfirm("删除成功", window.wxc.xcConfirm.typeEnum.info, {
                                                onOk: function (v) {
                                                    $scope.$apply(function () {
                                                        $scope.getPage();
                                                    })
                                                }
                                            });
                                        } else {
                                            window.wxc.xcConfirm("删除失败", window.wxc.xcConfirm.typeEnum.info);
                                        }
                                    })
                                }
                            })
                        }
                    }
                }else{
                    for (var i = 0; i < arguments.length; i++) {
                        if (typeof(arguments[i]) == "string") {
                            if (arguments[i].substr(arguments[i].length - 3) == ".do") {
                                url = arguments[i];
                            } else {
                                name = arguments[i];
                            }
                        } else {
                            param = arguments[i];
                        }
                    }
                    window.wxc.xcConfirm("是否确定删除  " + name + "?", window.wxc.xcConfirm.typeEnum.warning,arguments[3])
                }
            }
        }
        function uibModal() {
            var arguments=arguments[0];
            var modal={
                animation: false,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: 'app/template/viewDialog/'+arguments[0],
                controller: arguments[1],
                controllerAs: '$ctrl',
                resolve: {
                    getPage: function () {
                        // return $scope.getPage;
                        // return $scope.search;
                        if(!$scope.search){
                            return $scope.getPage;
                        }else{
                            return $scope.searchPage;
                        }
                    },
                }
            }
            if(arguments[2]){
                modal.resolve = Object.assign(modal.resolve,arguments[2]);
            }
            /*if(arguments.length>2){

            }*/
            return $uibModal.open(modal);
        }

        return{
            setconfig:function(columnDefs,list,totalCount){
                newColumnDefs=columnDefs;
                // filterColumnDefs(columnDefs,list);      //过滤需要显示的表头
                configColumnDefs(newColumnDefs);		//配置过滤后的表头到grid

                if(arguments.length>3){
                    $scope.gridOptions.enableHorizontalScrollbar = 1;
                    $scope.gridOptions.gridOverflowVisible = false;
                }else{
                    $scope.gridOptions.enableHorizontalScrollbar = 0;
                }
                $scope.gridOptions.columnDefs=newColumnDefs;
                $scope.gridOptions.totalItems=totalCount;
                $scope.gridOptions.data = list;
            },
            //alert弹出框，删除confirm的时候参数为(提示的名字,参数,url),如果添加自定义提示语在加第四个参数{}；其他作为alert提示的时候参数为("提示语","info/success/error")
            xcConfirm:function(){
                xcConfirm(arguments);
            },
            httpService:function(url,param){
                var $this=this;
                var url=url?basePath+url:"";
                return $http.post(url,param).error(function(data, status, headers, config){
                    var d=config.url.split("/")[config.url.split("/").length-1];
                    $this.xcConfirm("请求："+d+"<br />"+"返回错误状态码: "+status,"info")
                });
            },
            refushPage:function(search){
                aleadysearch = search;
                refushPage();
            },
            //配置并初始化表格，参数为(columnDefs,data.list,data.totalCount);
            setScope:function (scope) {
                $scope=scope;
                initColumnDefs();
                initEmptyGrid($scope);
                packagePagination('quan-hu');//翻页码数变由原来的三个页码修改为五个页码显示
            },
            //每行选择标志为复选框或者单选框
            rowHeaderSelection:function (type) {
                rowHeaderSelection(type)
            },
            //弹窗，默认两个参数（templateUrl,controller）,如果添加另外自定义方法第三个参数为{obj:function(){}}
            uibModal:function () {
                var d=uibModal(arguments)
                return d;
            },
            //loading效果
            jqLoading:function (value) {
                if(value){
                    $.fn.jqLoading("destroy");
                }else{
                    $.fn.jqLoading({ height: 100, width: 240, text: "LOADING..." });
                }
            },
            //获取目录结构
            getMenu:function(list){
                listMenu = list;
            },
            //开始恢复页面的历史数据
            startRemverHistory:function(d){
                $scope.$watch('$stateChangeSuccess',function (e) {
                    if(d !== ""){
                        $scope.paginationOptions.pageNumber = d.pageNumber;
                        $scope.paginationOptions.pageSize = d.pageSize;
                        $scope.typeNumber=d.typeNumber;
                        $scope.typeName=d.typeName;
                        $scope.getPage("refush");
                    }/*else{
                        $scope.getPage();
                    }*/
                })
            },


        }
    }
)
.directive('onFinishRenderFilters', function ($timeout) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            if (scope.$last === true) {              //监测最后一个是否repeat完。
                    scope.$emit('ngRepeatFinished');
            }
        }
    };
})
.directive('searchwatch',function(searcharray,gridService,$timeout){
    return {
        restrict: 'A',
        require:'ngModel',
        link: function($scope, element, attr,ctrl) {

            $scope.searcharray=searcharray;
            $scope.searcharray.push(attr.ngModel);
            ctrl.$viewChangeListeners.unshift(function(val){
                    $scope.$emit('searchTextChangedsearch',{val:$scope.$eval(attr.ngModel)});
            })

            var oldvalu="";
             $scope.$on('searchTextChangedsearch',function(event,val){
             if(val.val !== oldvalu){
             if(search){
             if(val.val == ""){
             search=false;
             for(var i=0;i<$scope.searcharray.length;i++){
             if($scope.$eval($scope.searcharray[i])){
             search=true;
             }
             }
             }
             gridService.refushPage(search)
             }
             }
             oldvalu = val;
             });

            //search函数存在searchPage一定存在，为了刷新页面并不返回第一页而且带着搜索框里的参数
            $scope.searchPage = function(){
                $scope.getPage("refush");
                return;
            }

            //搜索带着参数保证页码跳转到第一页
            var search=false;
            $scope.search = function(){
                search=true;
                gridService.refushPage(search)
            }
            $scope.enterSearch = function(e){
                if(e.keyCode==13){
                    $scope.search();
                }
            }
        }
    }

}).factory('searcharray',function(){
    var searcharray=[];
    return searcharray
})
//无数据
.filter('noData', function() {
        return function(val) {
            if (val|| val=='0'|| val == 0) {
                if(''!==val){
                    return val;
                }
            }
            return '--.--';
        };
    })
// 校验状态
.filter('checkStatus',function () {
    return function(val){
        if(val == "0"){
            return "待校验"
        }
        if(val == "1"){
           return "校验成功"
        }
        if(val == "2"){
            return "校验失败"
        }
    }
})
// 主域名状态
.filter('domationcheckStatus',function () {
    return function(val){
        if(val == "0"){
            return "不导出"
        }
        if(val == "1"){
            return "导出"
        }
    }
})
//APP状态
.filter('appStatus',function () {
    return function(val){
        if(val == "0" || val == 0){
            return "未爬取"
        }
        if(val == "1" || val == 1){
            return "未转移"
        }
        if(val == "2" || val == 2){
            return "已转移"
        }
        if(val == "3" || val == 3){
            return "爬取失败"
        }
    }
})
//操作系统
    .filter('ostype',function () {
        return function(val){
            var valarry = val.split(",")
            var result="";
            for(var i=0;i<valarry.length;i++){
                result = result + sys(valarry[i])+","
            }

            return result.substring(0,result.length-1)
            function sys(val){
                if(val == "2" || val == 2){
                    return "安卓"
                }
                if(val == "3" || val == 3){
                    return "IOS"
                }
                if(val == "4" || val == 2){
                    return "PC网站"
                }
                if(val == "5" || val == 3){
                    return "移动网站"
                }
            }
        }
    })
    //app分类
    .filter('appCategory',function () {
        return function(val){
            var result="";
            if(val && val.length>0){
                for(var i=0;i<val.length;i++){
                    result = result+ val[i]+"   ";
                }
            }
            return result
        }
    })
    //数据类型1代表3G,2代表4G
    .filter('usersType',function () {
        return function(val){
            if(val == "1" || val == 1){
                return "3G"
            }
            if(val == "2" || val == 2){
                return "4G"
            }
        }
    })
    //app分类的权重过滤
    .filter('weightfilter',function () {
        return function(val){
            var array="";
            if(val){
                var d=val.split(",")
                for(var i=0;i<d.length;i++){
                    if(d[i].match(/:/g)){
                        if(i == d.length-1){
                            array=array+d[i].split(":")[0]
                        }else{
                            array=array+d[i].split(":")[0]+","
                        }
                    }else{
                        if(i == d.length-1){
                            array=array+d[i]
                        }else{
                            array=array+d[i]+","
                        }
                    }
                }
                return array;
            }
        }
    })
//logo变化清缓存
.filter('logolurlchange',function () {
    return function(val){
        if(val == "" || val == undefined){
            return "app/images/default-logo.png"
        }
        return val
    }
})