/**
 * Remex UI 1.0
 * CopyRight (c) 2011 封尘 yangyang8599@163.com
 * Data 2011-11-26
 * 
 * Revision: remex.core.js  2015-12-29 更新
 */
/**
 * Remex Js core model
 */
var RMX = window.RMX = $.extend(window.RMX||{},{
	obtainUrlContext : function(url,flag){
		//如果flag不为空，则根据flag来定位是否有appcontext URI
		var uc = new RegExp(/^[a-zA-z]+:\/\/[a-zA-Z0-9\-\.]+(:[0-9]{1,}){0,1}(\/[^\/]+)([\/\-\w .?%&#=:]*)+$/);
		if(uc.test(url)) {
			var capUri = uc.exec(url)[2];//从url中获取到的context路径
			return flag && capUri.endsWith(flag)? "":capUri;
		}else
			return "";
	},
	getCurrentIframe : function() {
		var iframe_tmp = RMX.getRoandomInt();
		var curIframe;
		$("body").attr("iframe_tmp", iframe_tmp);
		$(window.parent.document).find("iframe").each(function() {
			var tRel = $(this.contentWindow.document).find("body").attr("iframe_tmp");
			if (tRel == iframe_tmp) {
				curIframe = this;
				return;
			}
		});
		return curIframe;
	},
	closeCurrentIframe : function(){
		var closeTabDiv = $(parent.document).find("#"+$(RMX.getCurrentIframe()).attr("id")).find("div[class='tab_close']");
		parent.RUI.handler.tabCloseClick.apply(closeTabDiv);	
	},
	getUrlParam : function(pname) {
		var name, value;
		var str = location.href; //取得整个地址栏
		var num = str.indexOf("?");
		str = str.substr(num + 1); //取得所有参数
		var arr = str.split("&"); //各个参数放到数组里
		for ( var i = 0; i < arr.length; i++) {
			num = arr[i].indexOf("=");
			if (num > 0) {
				name = arr[i].substring(0, num);
				value = arr[i].substr(num + 1);
				if (pname === name)
					return decodeURIComponent(value);
			}
		}
		return "";
	},
	getUrlIntParam : function(name) {
		var ret = RMX.getUrlParam(name);
		if (ret.isInteger())
			return ret;
		else
			return 1;
	},
	putRandomToUrl : function(url) {
		if (url.indexOf("?") < 0)
			return url + "?mid=" + Math.random();
		else
			return url + "&mid=" + Math.random();
	},
	/**
	 * 
	 * @param {Object} url
	 * @param {Object} params
	 * @param {Object} noRandom 如果指定为true则向不向url中添加随即数
	 * @return {TypeName} 
	 */
	getUrl : function(url, params, noRandom) {
		var _hw = url.indexOf("?") < 0;
		var _ps = (noRandom?"":("mid=" + Math.random()))+(!params?"":(_hw&&noRandom?"":"&"+$.param(params||{})));
		return url + (_ps?(_hw?"?":"&")+_ps:"");
	},
	cutString : function(str, len) {
		if (str.length > len)
			return str.substr(0, len - 3) + "…";
		else
			return str;
	},
	delHtml : function(str) {
		if (str)
			return str.replace(/<[^>]+>/gi, '');
		else
			return "";
	},
	evalFastjson : function(fj_json) {
		fj_json.eval = function(ref) {
			return eval(ref);
		};
		RMX._evalFastjson(fj_json, fj_json);
	},
	_evalFastjson : function(fj_json, root, parent) {
		if (fj_json instanceof Array) {
			var l = fj_json.length;
			for ( var i = 0; i < l; i++) {
				RMX._evalFastjson(fj_json[i], root, fj_json);
			}
		} else if (fj_json instanceof Object) {
			for ( var n in fj_json) {
				var fj_o = fj_json[n];
				if (typeof (fj_o) == "string") {
					var fc = fj_o.charAt(0);
					if (fc == "$") {
						$.extend(fj_json, root.eval("fj_json" + fj_o.substr(1)));
					} else if (fc == "@") {
						$.extend(fj_json, root.eval.apply(fj_json[n], [ fj_o.substr(1) ]));
					} else if (fc == "." && fj_o.charAt(1) == ".") {
						$.extend(fj_json, parent);
						//throw "..fastjson 尚未开发好！";
					}

				} else {
					RMX._evalFastjson(fj_json[n], root, fj_json);
				}
			}
		} else
			return;

	},
	/**
	 * 此方法将重构json保障其内部的有效性。把含有json字符串的string都转义为object
	 * @param {Object} json
	 * @return {Object}
	 */
	evalWholeJson : function(json) {
		if (json instanceof Array) {
			var l = json.length;
			for ( var i = 0; i < l; i++) {
				RMX.evalWholeJson(json[i]);
			}
		} else if (json instanceof Object) {
			for ( var n in json) {
				var o = json[n];
				if (typeof (o) == "string") {
					len = o.length - 1;
					if (o.charAt(0) == "{" && o.charAt(len) == "}")
						json[n] = eval("(" + json[n] + ")");
				} else {
					RMX.evalWholeJson(json[n]);
				}
			}
		} else
			return json.charAt(0) == "{"? eval("(" + json + ")"): eval("({" + json + "})");
	},
	/**
	 * 此方法将返回保证其没有null的属性obj，并转换string的"ture"、"false"为对应的boolean
	 * @param {Object} obj
	 * @return {TypeName} 
	 */
	getValidPropsObj : function(obj) {
		if (obj instanceof Array) {
			var i = obj.length;
			while (i--) {
				obj[i] = RMX.getValidPropsObj(obj[i]);
			}
			return obj;
		} else if (obj instanceof Object) {
			var newObj = {};
			for ( var i in obj) {
				if (obj[i]) {
					if (obj[i] instanceof Array)
						newObj[i] = RMX.getValidPropsObj(obj[i]);
					else {
						newObj[i] = obj[i];
						if (obj[i] == "true")
							newObj[i] = true;
						else if (obj[i] == "false")
							newObj[i] = false;
					}
				} else {
				}//不填写则删除了空属性
			}
			return newObj;
		}
	},
	getRoandomInt : function() {
		r = Math.random();
		return Math.round(r * 1000000);
	},
	/**功能函数 对象写入属性
	 * 
	 * @param obj  普通JS对象
	 * @param selector JQUERY ID选择器 例如传入 "#elementid" 选择的就是页面中ID=elementid的DOM对象
	 */
	writeObj2val : function (obj, selector,pres) {
		//tarArea 范围性结果集
		var tarArea = jQuery(selector);
		for ( var prop in obj) {
			var value =obj[prop];
			var propLowerFirstChar=prop.replace(/^\w/g,function(s){	return s.toUpperCase();	});
			if(!!value)
				if(!pres){
//					var t = document.getElementById(prop);
//					if(t) t.value=value;
					tarArea.find("#" + prop).val(value);
				}else{
				//	var presProps ="#" + pres[0] + propLowerFirstChar;
					for(var i=0,c=pres.length;i<c;i++){
						tarArea.find(pres[i] + propLowerFirstChar).val(value);
//						var t =	document.getElementById(pres[i] + propLowerFirstChar);
//						if(t) t.value=value;
					}
					//	presProps+=",#" + pres[i] + propLowerFirstChar;
				//	tarArea.find(presProps).val(value);
				}
		}
	},
	/** */
	copyProperties : function (obj, destSelector,valueMethod,pres) {
		//tarArea 范围性结果集
		var tarArea = jQuery(destSelector);
		var valMethod = $.fn[valueMethod ||  "val"];
		for ( var prop in obj) {
			var value =obj[prop];
			var propLowerFirstChar=prop.replace(/^\w/g,function(s){	return s.toUpperCase();	});
			if(!!value)
				if(!pres){
//					var t = document.getElementById(prop);
//					if(t)t.innerText=value;
					valMethod.apply(tarArea.find("[rid='" + prop+"']"),[value]);
				}else{
				//	var presProps ="#" + pres[0] + propLowerFirstChar;
					for(var i=0,c=pres.length;i<c;i++){
//						var t =	document.getElementById(pres[i] + propLowerFirstChar);
//						if(t)t.innerText=value;
						tarArea.find("[rid='" + pres[i] + propLowerFirstChar+"']").text(value);
					}
					//	presProps+=",#" + pres[i] + propLowerFirstChar;
				//	tarArea.find(presProps).val(value);
				}
		}
	},
	//访问链必须符合js规则。
	flatVal : function(flatStr,value,context/*inner*/,reg){
		var paramNameReg = reg || (/^([A-Za-z_]\w*)|(\.([A-Za-z_]\w*))|(\[(\d+)\])|(\[(\"[^\"]*\")])|(\[(\'[^\']*\')])/g);
		var b=n=e=0,ec,ctx=context, // i是正则本次匹配的开始，e是匹配的末尾,ec是末尾的下一个字符，c是总长度，n是上次匹配末尾的位置
			g=0,//匹配上的组号
			s=flatStr,c=s.length,mr,idx;
		while((mr=paramNameReg.exec(flatStr))){
			if(n!=(b=mr.index)){
				throw {name:'格式异常',message:'链式访问格式异常，字符串中间有非法字符'}
			}

			if((idx=mr[(g=1)]||mr[(g=3)]||mr[(g=5)]||mr[(g=7)]||mr[(g=9)]) && (e=b+mr[0].length)===c){
				idx=g==5?parseInt(idx):idx;
				ctx[idx]=value;
				break;
			}

			if(!ctx[idx])
				ctx[idx]= ((ec=s.charAt(e))=='.')?{} // 新增{}
					: (ec=='['?[] //新增[]
					:undefined); //抛出异常

			if(!ctx[idx])
				throw {name:'格式异常',message:'链式访问格式异常，此处应该为.或['}

			n=e;
			ctx=ctx[idx];
		}
	},
	flat2tree : function(flatDatas,treeSettings) {
		var dnode = {fields: [], subNodes: {}};
		//var defaultNodes = {
		//	nodeName:"id",
		//	fields:['field1','field2'],
		//	subNodes:{
		//		type:'list' or 'object'
		//		nodeName:{
		//			fields:[],
		//			subNodes:{},
		//			subLists:{}
		//		}
		//	},
		//	subLists: {
		//		nodeName: {
		//			fields: [],
		//			subNodes:{},
		//			subLists:{}
		//		}
		//	}
		//};
		var createSettings = function(data,nodeSettings){
			var ns = $.extend(true,{},dnode,nodeSettings);
			_.each(data, function (v, key) {
				if (key.indexOf(".") < 0 && key.indexOf("[") < 0) {
					ns.fields.push(key);
				}else{//进到这里面来的要么有符号. 要么有符号[
					var ctx=ns;//设置根节点
					var paramNameReg = (/^([A-Za-z_][A-Za-z\d_]*)|(\.([A-Za-z_][A-Za-z\d_]*))|(\[([\d\w]+)\])|(\[(\"[^\"]*\")])|(\[(\'[^\']*\')])/g);
					var b=n=e=0,ec, // i是正则本次匹配的开始，e是匹配的末尾,ec是末尾的下一个字符，c是总长度，n是上次匹配末尾的位置
						g=0,//匹配上的组号
						s=key,c=s.length,mr,idx;
					while((mr=paramNameReg.exec(key))){
						if(n!=(b=mr.index)){
							throw {name:'格式异常',message:'链式访问格式异常，字符串中间有非法字符'}
						}

						if((idx=mr[(g=1)]||mr[(g=3)]||mr[(g=5)]||mr[(g=7)]||mr[(g=9)]||mr[(g=10)]) && (e=b+mr[0].length)===c){
							ctx.type = g>3?'list':'object';
							ctx.fields.push({indexName:key,fieldName:idx});
							break;
						}

						//因为还没有到末尾，所以还是要新建一个节点
						if(g<=9){
							ctx = ctx.subNodes[idx]= ctx.subNodes[idx]?ctx.subNodes[idx]:$.extend(true,{},dnode);//新建一个空子节点,并向子节点移动一层。
						}else
							throw {name:'格式异常',message:'链式访问格式异常，此处应该为.或['}

						n=e;
					}
				}
			});
			return ns;
		}
		var _ret=[];
		var _pick =function(data,pickFields,target){
			var _r = target||{};
			_.each(pickFields,function(p,k){
				if(typeof p == 'string')
					_r[p]=data[p];
				else
					_r[p.fieldName]=data[p.indexName];
			});
			return _r;
		};

		var creatNode = function(root,data,nodeSettings){
			var ns = nodeSettings = $.extend(true,nodeSettings||{},{nodeName:'id'});
			var findNode = function(r) {return r[ns.nodeName]==data[ns.nodeName]};

			var curNode= _.find(root,findNode);

			//处理当前节点
			if(!curNode) { // 如果当前节点不存在，则根据配置指定的属性建立一个
				if (_.isArray(root))
					root.push(curNode = _pick(data, ns.fields));
				else
					curNode = _pick(data, ns.fields, root);//取出值放到root里面
			}
			_.each(ns.subNodes,function(sn,idx) {
				!!idx && !_.isArray(curNode[idx]) && (curNode[idx]=(sn.type=='list'?[]:{}));//递归处理List子节点 初始化子数组节点 递归处理Object子节点
				creatNode(curNode[idx], data, sn);
			});

		};

		var doCreateSettings =false;
		_.each(flatDatas,function(data,idx) {
			//根据数据自动完善field,subField,subList参数
			if(!doCreateSettings){
				doCreateSettings = true;
				treeSettings = createSettings(data,treeSettings);
			}
			creatNode(_ret,data, treeSettings);
		});

		return _ret;
	},
	setCookies : function(key,value,path,expires,secure){
		/**设置cookies*/
		/*cookie对象的api = {
		 key:""//不能使用分号（;）、逗号（,）、等号（=）以及空格
		 value:""任意字符串
		 expires: // Date 不设置则当前会话结束后，则失效。
		 path://可访问的目录，不设置则以/为准的所有子页面（含子目录都可以访问）
		 secure: //是否使用https或者其他安全协议进行加密。
		 }

		 两种使用方法:key和value时普通的string，则保存为当前页面路径目录下的k-v cookies

		 当key为对象时，则按如上结构，设置cookie
		 */
		var k,v,e,p,s;
		if(typeof key == "object"){
			k = key.key;
			v = key.value;
			e = key.expires;
			p = key.path||"/";
			s = key.secure;
		}else if(typeof key == "string"){
			k = key;
			v = value;
			p = path||"/";
			e = expires;
			s = secure;
		}

		if(!k || k.indexOf(";")>=0|| k.indexOf(",")>=0|| k.indexOf("=")>=0)
			console.warn("cookie的key设置有误，不能包含;,=及空格。key="+k);

		var str =k+"="+escape(v);

		if(e && e instanceof Date) str = str+";expires="+e.toGMTString();
		if(p && p instanceof String) str = str+";path="+p;
		if(s) str = str+";secure="+(!!e);

		document.cookie=str;
	},
	getCookies : function(key){
		/**根据key来获取cookie
		 key可以是
		 ①一个字符串，直接返回一个value值
		 ②以;分割的字符串；返回一个包含kes和value的对象
		 ③一个对象；填充该对象，如果cookies不存在，则该对象的属性为undefined
		 */
		var cookies = document.cookie.split("; ");
		var _t={};
		//_t作缓存，遍历查询
		var findCookie=function(key){
			if(!_t[key]){
				_.find(cookies,function(i){
					var cp = i.split("=");
					_t[cp[0]] = unescape(cp[1]);
					return cp[0] == key;
				});
			}
			return _t[key];
		}

		if(typeof key =="string"){
			if(key.indexOf(";")>=0){
				var ret={};
				_.each(key.split(";"),function(k){
					ret[k]=findCookie(k);
				});
				return ret;
			}else{
				return findCookie(key);
			}
		}else if(typeof key == "object"){
			_.each(key,function(v,k){
				key[k]=findCookie(k);
			});
			return key;
		}
	},
	delCookies : function(key,path){
		/**删除cookies*/
		path=path||"/";
		var d = new Date();
		d.setDate(d.getDate()-1);
		document.cookie=key+"=null;path="+path+";expires="+d.toGMTString();
	},
	getUser: function () {
		return {
			username: RMX.getCookies("UID")
		};
	}

});
var contextRoot = contextRoot || new RegExp(/^[a-zA-z]+:\/\/[a-zA-Z0-9\-\.]+(:[0-9]{1,}){0,1}(\/[^\/]+)([\/\-\w .?%&#=:]*)+$/).exec(location.href)[2]||"";

/**
 * Remex Js extend for jQuery
 */
(function($) {

})(jQuery);






//基本对象的扩展
/**
 * 扩展String方法
 */
/** @author LIU正数 */
String.prototype.isPositiveInteger = function() {
	return (new RegExp(/^[1-9]\d*$/).test(this));
};
/** 是整数 */
String.prototype.isInteger = function() {return (new RegExp(/^\d+$/).test(this));};
/**是数字*/
String.prototype.isNumber = function(value, element) {return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));};
/**删除前后空格*/
String.prototype.trim = function() {return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");};
/**以pattern开始。pattern是普通字符串*/
String.prototype.startsWith = function(pattern) {return this.indexOf(pattern) === 0;};
/**以pattern结束。pattern是普通字符串 */
String.prototype.endsWith = function(pattern) {	var d = this.length - pattern.length;	return d >= 0 && this.lastIndexOf(pattern) === d;};
/**以pattern开始。pattern是普通字符串 */
String.prototype.replaceSuffix = function(index) {
	return this.replace(/\[[0-9]+\]/, '[' + index + ']').replace('#index#', index);
};
String.prototype.trans = function() {
	return this.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&quot;/g, '"');
};
String.prototype.replaceAll = function(os, ns) {
	return this.replace(new RegExp(os, "gm"), ns);
};

/** 将字符串中{aa_d22}格式的变量根据$data的对象数据进行替换。比如讲aaa{b_1}ccc ,$data={b_1:333},替换成：aaa333ccc*/
String.prototype.replaceTm = function($data) {
	if (!$data)
		return this;
	return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
		return $data[$1.replace(/[{}]+/g, "" )];
	});
};
/** 将字符串中{aa_d22}格式的变量根据页面或入参选择器对象中的input对应的数据进行替换。比如讲aaa{b_1}ccc ,<input id="b_1">,替换成：aaa333ccc*/
String.prototype.replaceTmById = function(_box) {
	var $parent = $(_box) || $(document);
	return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
		var $input = $parent.find("#" + $1.replace(/[{}]+/g, ""));
		return $input.val() ? $input.val() : $1;
	});
};
/**判断字符串中的{aa_c2}这样的变量是否都已经替换完了。*/
String.prototype.isFinishedTm = function() {
	return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this));
};
String.prototype.isValidMail = function() {
	return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
};
/*是空白字符串*/
String.prototype.isSpaces = function() {
	for ( var i = 0; i < this.length; i += 1) {
		var ch = this.charAt(i);
		if (ch != ' ' && ch != "\n" && ch != "\t" && ch != "\r") {
			return false;
		}
	}
	return true;
};
String.prototype.isPhone = function() {	return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));};
String.prototype.isUrl = function() {return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));};
//站外链接
String.prototype.isExternalUrl = function() {return this.isUrl() && this.indexOf("://" + document.domain) == -1;};
/*字符串与日期的转化1*/
String.prototype.parseDate = function(format) {	nd = new Date(this.replaceAll("[年月-]", "/").replaceAll("日",""));	return nd;};
String.prototype.formatDate = function(format) {return this.parseDate(format).format(format);};
String.prototype.simpleDate = function() {	return this.parseDate("yyyy/MM/dd");};
/** * 根据本字符串生成对应的时间。 */
String.prototype.simpleTime = function() {	return this.parseDate("hh:mm:ss");};
/** 移除本字符串的html标签。*/
String.prototype.removeHtml = function(){	return this.replace(/<[^>].*?>/g,"");  };
/**根据长度省略字符串。超过长度的后补"…" */
String.prototype.omit = function(len) {var s = this.trim();if (s.length > len) {return s.substring(0, len - 3) + "…";} else {return s;}};
/** @param lastIndexChar  */
String.prototype.substrEndByLastIndexChar= function(lastIndexChar){
	var last=0;
	if( typeof(lastIndexChar) === "string")
		last = this.lastIndexOf(lastIndexChar);
	else
		for(var i in lastIndexChar){
			if((last = this.lastIndexOf(lastIndexChar[i]))>0)break;
		}
	return this.substring(0, last);
};

String.prototype.substrStartByLastIndexChar= function(lastIndexChar){
	var last=0;
	if( typeof(lastIndexChar) === "string")
		last = this.lastIndexOf(lastIndexChar);
	else
		for(var i in lastIndexChar){
			if((last = this.lastIndexOf(lastIndexChar[i]))>0)break;
		}
	return this.substring(1+last);
};

String.prototype.isCertNo = function (){
	var idNo = this;
	// 错误码
	var errorCodes =[true,"身份证号码位数错误!","身份证出生日期不合法!","身份证地区不合法!","身份证号码校验错误!"	];
	var area = {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
		33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",
		46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
		65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
	var idNoArray = idNo.split("");

// 地区校验
	if(null == area[parseInt(idNo.substr(0,2))]){
		return errorCodes[3];}

	var reg,checkCode;// 正则表达式,校验码
	var sum,re;// 校验总和，余数
// 身份证位数及格式校验
	switch(idNo.length){
		// 15位校验
		case 15:
			if((parseInt(idNo.substr(6,2))+1900)%4 == 0 || ((parseInt(idNo.substr(6,2))+1900)%100 == 0 && (parseInt(idNo.substr(6,2))+1900)% 4 == 0)){
				reg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;
			}else{
				reg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
			}
			if(reg.test(idNo)){return errorCodes[0];}else{return errorCodes[2];}
			break;
		case 18:
			// 18位校验
			// 出生日期合法性校验
			// 闰年((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
			// 平年((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
			if(parseInt(idNo.substr(6,4))%4 == 0 || (parseInt(idNo.substr(6,4))%100 == 0&&parseInt(idNo.substr(6,4))%4 == 0)){
				reg = /^[1-9][0-9]{5}((19[0-9]{2})|(200[0-9]{2})|(201[0-3]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年校验
			}else{
				reg = /^[1-9][0-9]{5}((19[0-9]{2})|(200[0-9]{2})|(201[0-3]{2}))((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年
			}
			if(reg.test(idNo)){
				// 校验位
				sum = (parseInt(idNoArray[0])+parseInt(idNoArray[10]))*7
					+ (parseInt(idNoArray[1])+parseInt(idNoArray[11]))*9
					+ (parseInt(idNoArray[2])+parseInt(idNoArray[12]))*10
					+ (parseInt(idNoArray[3])+parseInt(idNoArray[13]))*5
					+ (parseInt(idNoArray[4])+parseInt(idNoArray[14]))*8
					+ (parseInt(idNoArray[5])+parseInt(idNoArray[15]))*4
					+ (parseInt(idNoArray[6])+parseInt(idNoArray[16]))*2
					+ parseInt(idNoArray[7])*1
					+ parseInt(idNoArray[8])*6
					+ parseInt(idNoArray[9])*3;
				re = sum % 11;		// 余数
				checkCode = "10X98765432";			// 校验码
				var checkNo = checkCode.substr(re,1);			// 校验位
				if(checkNo == idNoArray[17])
					return errorCodes[0];
				else
					return errorCodes[4];

			}else{
				return errorCodes[2];
			}
			break;
		default:
			return errorCodes[1];
			break;
	}
};


/*******扩展date**********/
Date.prototype.format = function(format) {
	format = format ||"yyyy-MM-dd";
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};
Date.prototype.simpleDate = function(splitString) {
	var sd = splitString?this.format("yyyy"+splitString+"MM"+splitString+"dd"):this.format("yyyy-MM-dd");
	return sd=="NaN-aN-aN"?"":sd;
};
Date.prototype.simpleTime = function(splitString) {
	var sd = splitString?this.format("yyyy"+splitString+"MM"+splitString+"dd"):this.format("hh-mm-ss");
	return sd=="NaN-aN-aN"?"":sd;
};
Date.prototype.moveDate = function(oper,format){
	var m;
	if(m=/([yMd])([\+\-])([\dsd]+)/.exec(oper))
		switch (m[1]) {
			case "d":
				if(m[2]=="+")
					this.setDate(this.getDate()+parseInt(m[3]));
				else
					this.setDate(this.getDate()-parseInt(m[3]));
				break;
			case "M":
				if(m[3]=="e") {//月末
					this.setMonth(this.getMonth() + 1);
					this.setDate(1);
					this.setDate(this.getDate()-1);
				}if(m[3]=='s') {//月初
					this.setDate(1);
				}else if(m[2]=="+")
					this.setMonth(this.getMonth()+parseInt(m[3]));
				else
					this.setMonth(this.getMonth()-parseInt(m[3]));
				break;
			case "y":
				if(m[2]=="+")
					this.setYear(this.getFullYear()+parseInt(m[3]));
				else
					this.setYear(this.getFullYear()-parseInt(m[3]));
				break;

			default:
				throw "未实现的操作"+oper;
		}
	return this;
};

