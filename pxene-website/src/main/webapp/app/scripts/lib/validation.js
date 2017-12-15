// var app = angular.module('form-example1', []);
// app.controller('Controller',function($scope, $http, validateService){
// 	$scope.submit = function(){
// 		var validate = validateService.validate();
// 		if(validate == true){
// 		}else{
//
// 			console.log('验证失败！');
// 			var errqueue = validateService.getQueue();
// 			console.log(errqueue)
// 			alert(errqueue[0].info)
// 		}
// 	}
// });
angular.module('validation', []).directive('validations', function(validateService) {
	return {
		restrict : 'A',
		require: 'ngModel',
		link: function(scope, el, attrs, ctrl) {
			validateService.setScope(scope);
			scope.validations = {},
			scope.validations.errQueue = [];			//错误队列

			//根据dom中的name属性来对应输入框的名称
			scope.validations.validateName = {
                paramClassify:"参数分类",
                param:"参数",
                paramOrder:"参数顺序",
                urlExample:"URL样例",
                grabBehavior:"抓取行为",
                transfer:"应用选择",
                appName:"APP名称",
                source:"来源",
                maindomain:"主域名",
                additionParameters:"附加参数",
                priority:"优先级",
                urlregexp:"URL正则",
                domain:"域名",
                paramRegex:"参数正则",
                paramkey:"参数正则",
                paramcontent:"参数正则",
                remarks:"备注",
                firstClassify:"一级分类",
                secondClassify:"二级分类",
                transferApp:"搜索转移的应用",
                userpoint:"使用端",
                websiteName:"网站名称",
                version:"版本号"
			}
			
			//验证信息配置
			scope.validations.validateConfig = {
			    required : {
					func : function(val){
                        for(status in val){
							if(val[status]=="全部状态" || val[status]=="全部行为" || val[status]=="全部来源" || val[status]=="全部分类"){
								return false;
							}
						}
						return val ? true : false;
					},
					alertText : "{0} 不能为空！"
				}, 
				minlength: {
					func: function(val, len){
						this.alertText = "{0} 字符长度不能小于"+len+' !';
						return String(val).length >= len ? true : false;
					},
					alertText : ""
				},
                maxlength: {
                    func: function(val, len){
                        this.alertText = "{0} 字符长度不能大于"+len+' !';
                        return String(val).length <= len ? true : false;
                    },
                    alertText : ""
                },
				mobile : {
					regex : /^1[3|5|7|8][0-9]{9}$/,
					alertText : "{0} 必须为手机号码！"
				},
                //正整数
                positiveInteger:{
                    regex:/^[1-9]*[1-9][0-9]*$/,
                    alertText : "{0} 必须为正整数"
                },
                param : {
					func : function(val){
						return val ? true : false;
					},
					alertText : "{0}字段不能为空！"
				},
                paramClassify : {
                    func : function(val){
                        return val ? true : false;
                    },
                    alertText : "{0}字段不能为空！"
                },
                number:{
                    regex : /^[1|2|3]$/,
                    alertText : "{0} 只能有1,2,3三个优先级！"
				},
                paramregvalidation:{
                    func : function(val){
                    	if(scope.paramobj.param_reg){
							if(scope.paramobj.param_reg.param_reg == "NULL"){
                                return true;
                            }
						}
                        return val ? true : false;
                    },
                    alertText : "{0} 不能为空！"
				},
                nullstatus:{
                    func : function(val,len){
						if(val == "NULL"){
							return true;
						}
						if(val == "null"){
							return false;
						}
						return true;
                    },
                    alertText : "{0} 只能输入大写字母 NULL！"
				}

        };

			//监视指令所在视图或数据发生变化
			ctrl.$viewChangeListeners.unshift(function(){
				validateService.validateOne(el,attrs);
			});
		}
	};
}).factory('validateService',function(){
	return function(){
		var scope;
		var isInQueue = function(errObj){
			var errQueue = scope.validations.errQueue;
			for(var i = 0,len = errQueue.length; i < len; i++){
				if(errObj.name == errQueue[i].name){
					return i;
				}
			}
			return -1;
		},
		addQueue = function(errObj){
			var errQueue = scope.validations.errQueue;
			isInQueue(errObj) === -1 && errQueue.push(errObj);
		},
		removeQueue = function(errObj){
			var errQueue = scope.validations.errQueue,
				index = isInQueue(errObj);
			index !== -1 && errQueue.splice(index, 1);
		},
		isEmptyQueue= function(){
			var errQueue = scope.validations.errQueue;
			return errQueue[0] ? false : true;
		},
		typeOf = function (o){  
			var _toString = Object.prototype.toString;
			var _type = {
				"undefined" : "undefined",
				"number" : "number",
				"boolen" : "boolen",
				"string" : "string",
				"[object Function]" : "function",
				"[object RegExp]" : "regexp",
				"[object Array]" : "array",
				"[object Date]" : "date",
				"[object Error]" : "error"
			}
			return _type[typeof o] || _type[_toString.call(o)] || (o ? "object" : 'null');
		},
		parseValid = function (str){
			var valids = str.split(';'),
				ret = {};
			for (var i = 0,length = valids.length; i < length; i++) {
				var n = valids[i];
				if(n){
					if(n.indexOf(':')>-1){
						var t = n.split(':');
						ret[t[0]] = t[1];
					}else{
						ret[n] = null;
					}
				}
			}
			return ret;
		};
		return {
			validateOne : function(el,attr){
				var val = scope.$eval(attr.ngModel)?scope.$eval(attr.ngModel):scope.$eval(attr['ng-model'])?scope.$eval(attr['ng-model']):"",
					valids = parseValid(attr.validations),
					validateConfig = scope.validations.validateConfig,
					name = attr.name;	
				/* if(!name){
					//字段无name属性报错
					new Error('缺少name属性！');
				} */
				for(var i in valids){
					var validObj = validateConfig[i],
						params = valids[i] || null;
					if(!validObj){
						new Error('未知验证规则');
					}
					if(validObj.func){
						if(!validObj.func(val, params)){
							addQueue({
								name : scope.validations.validateName[name],
								element : el,
								info:validObj.alertText.replace(/\{0\}/g, scope.validations.validateName[name]),
							});
							if(!el.hasClass('err-tip')){
								var d=el.html().split(" ");
								if(d[0] == "<option" && d[d.length-1].split("<")[d[d.length-1].split("<").length-1]=="/option>"){
                                    el.next().addClass("err-tip")
								}
								//el.parent().append('<span>'+validObj.alertText.replace(/\{0\}/g, name)+'</span>');
								el.addClass('err-tip');
							}
							return false;
						}else{
							removeQueue({name : scope.validations.validateName[name]});
							if(el.hasClass('err-tip')){
                                var d=el.html().split(" ");
                                if(d[0] == "<option" && d[d.length-1].split("<")[d[d.length-1].split("<").length-1]=="/option>"){
                                    el.next().removeClass("err-tip")
                                }
								//el.parent().find('span:last').remove();
								el.removeClass('err-tip');								
							}						
						}
					}else if(validObj.regex){
						if(typeOf(validObj.regex) !== "regexp"){
							if(typeOf(validObj.regex) === "string"){
								validObj.regex = new RegExp(validObj.regex.replace(/[\/|\\]/g, ''),'g');
							}else{
								new Error('不合法的正则表达式！');
							}
						}
						if(!validObj.regex.test(val)){
							addQueue({
                                name : scope.validations.validateName[name],
								element : el,
                                info:validObj.alertText.replace(/\{0\}/g, scope.validations.validateName[name]),
							});
							if(!el.hasClass('err-tip')){
                                var d=el.html().split(" ");
                                if(d[0] == "<option" && d[d.length-1].split("<")[d[d.length-1].split("<").length-1]=="/option>"){
                                    el.next().addClass("err-tip")
                                }
								//el.parent().append('<span>'+validObj.alertText.replace(/\{0\}/g, name)+'</span>');
								el.addClass('err-tip');
								
							}	
							return false;
						}else{
							removeQueue({name : scope.validations.validateName[name]});
							if(el.hasClass('err-tip')){
                                var d=el.html().split(" ");
                                if(d[0] == "<option" && d[d.length-1].split("<")[d[d.length-1].split("<").length-1]=="/option>"){
                                    el.next().removeClass("err-tip")
                                }
								//el.parent().find('span:last').remove();
								el.removeClass('err-tip');
							}
						}
					}
				}
				return true;
			},
			//一次性验证所有需要验证的字段
			validateAll : function(){
				var elements = $('[validations]'),
					self = this;
				var attrconfig={};
				elements.each(function(){
				for(var i=0;i<angular.element(this)[0].attributes.length;i++){
					attrconfig[angular.element(this)[0].attributes[i].name]=angular.element(this)[0].attributes[i].nodeValue;
				}				
					self.validateOne(angular.element(this),attrconfig)							    
				});
			},
			//获取验证后的结果，验证通过返回`true`，否则返回`false`
			validate : function(){
				scope.validations.errQueue = [];
				this.validateAll();
				return isEmptyQueue() ? true : false;
			},
			getQueue: function(){
				return scope.validations.errQueue;
			},
			addRule :function(name, rule){
				if(!scope.validations.validateConfig[name]){
					scope.validations.validateConfig[name] = rule;
				}
			},
			setScope : function(s){
				scope = s;
			}
		}
	}(); 
});
