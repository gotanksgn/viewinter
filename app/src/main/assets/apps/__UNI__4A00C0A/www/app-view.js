(function(t){var r={};function e(n){if(r[n])return r[n].exports;var o=r[n]={i:n,l:!1,exports:{}};return t[n].call(o.exports,o,o.exports,e),o.l=!0,o.exports}e.m=t,e.c=r,e.d=function(t,r,n){e.o(t,r)||Object.defineProperty(t,r,{enumerable:!0,get:n})},e.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},e.t=function(t,r){if(1&r&&(t=e(t)),8&r)return t;if(4&r&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(e.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&r&&"string"!=typeof t)for(var o in t)e.d(n,o,function(r){return t[r]}.bind(null,o));return n},e.n=function(t){var r=t&&t.__esModule?function(){return t["default"]}:function(){return t};return e.d(r,"a",r),r},e.o=function(t,r){return Object.prototype.hasOwnProperty.call(t,r)},e.p="./",e(e.s="bbfe")})({"02b3":function(t,r,e){"use strict";var n,o=function(){var t=this,r=t.$createElement,e=t._self._c||r;return e("v-uni-view",{attrs:{_i:0}},[e("kps-image-cutter",{attrs:{_i:1},on:{ok:function(r){return t.$handleViewEvent(r)},cancel:function(r){return t.$handleViewEvent(r)}}})],1)},i=[];e.d(r,"b",(function(){return o})),e.d(r,"c",(function(){return i})),e.d(r,"a",(function(){return n}))},"06e7":function(t,r,e){"use strict";Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var n=o(e("6a4f"));function o(t){return t&&t.__esModule?t:{default:t}}var i={data:function(){return{wxsProps:{}}},components:{kpsImageCutter:n.default}};r.default=i},"089e":function(t,r,e){var n=e("24fb");r=n(!1),r.push([t.i,".ksp-image-cutter[data-v-f0bff8d2]{position:absolute;width:100%;height:100%;top:0;bottom:0;z-index:1000}.toolbar[data-v-f0bff8d2]{position:absolute;width:100%;height:120rpx;left:0rpx;bottom:0rpx;box-sizing:border-box;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#000;background-color:#000}.btn-cancel[data-v-f0bff8d2]{position:absolute;left:100rpx;top:25rpx;font-size:30rpx;line-height:30rpx;padding-top:20rpx;padding-bottom:20rpx;padding-left:35rpx;padding-right:35rpx;color:#fff;background-color:#000}.btn-ok[data-v-f0bff8d2]{position:absolute;right:100rpx;top:25rpx;font-size:30rpx;line-height:30rpx;padding-top:20rpx;padding-bottom:20rpx;padding-left:35rpx;padding-right:35rpx;color:#fff;background-color:#55d6ba}.body[data-v-f0bff8d2]{position:absolute;left:0rpx;right:0rpx;top:0rpx;bottom:100rpx;background-color:#000;overflow:hidden}.mask[data-v-f0bff8d2]{position:absolute;left:0rpx;right:0rpx;top:0rpx;bottom:0rpx;background-color:#000;opacity:.4}.plank[data-v-f0bff8d2]{position:absolute;left:0rpx;right:0rpx;top:0rpx;bottom:0rpx}.image[data-v-f0bff8d2]{position:absolute}.frame[data-v-f0bff8d2]{position:absolute}.canvas[data-v-f0bff8d2]{position:absolute;display:block;left:0;top:0}.rect[data-v-f0bff8d2]{position:absolute;left:-2px;top:-2px;width:100%;height:100%;border-width:2px;border-style:solid;border-color:#fff}.line-one[data-v-f0bff8d2]{position:absolute;width:100%;height:1px;background-color:#fff;left:0;top:33.3%}.line-two[data-v-f0bff8d2]{position:absolute;width:100%;height:1px;background-color:#fff;left:0;top:66.7%}.line-three[data-v-f0bff8d2]{position:absolute;width:1px;height:100%;background-color:#fff;top:0;left:33.3%}.line-four[data-v-f0bff8d2]{position:absolute;width:1px;height:100%;background-color:#fff;top:0;left:66.7%}.frame-left[data-v-f0bff8d2]{position:absolute;height:100%;width:8px;left:-4px;top:0}.frame-right[data-v-f0bff8d2]{position:absolute;height:100%;width:8px;right:-4px;top:0}.frame-top[data-v-f0bff8d2]{position:absolute;width:100%;height:8px;top:-4px;left:0}.frame-bottom[data-v-f0bff8d2]{position:absolute;width:100%;height:8px;bottom:-4px;left:0}.frame-left-top[data-v-f0bff8d2]{position:absolute;width:20px;height:20px;left:-6px;top:-6px;border-left-style:solid;border-left-color:red;border-left-width:4px;border-top-style:solid;border-top-color:red;border-top-width:4px}.frame-left-bottom[data-v-f0bff8d2]{position:absolute;width:20px;height:20px;left:-6px;bottom:-6px;border-left-style:solid;border-left-color:red;border-left-width:4px;border-bottom-style:solid;border-bottom-color:red;border-bottom-width:4px}.frame-right-top[data-v-f0bff8d2]{position:absolute;width:20px;height:20px;right:-6px;top:-6px;border-right-style:solid;border-right-color:red;border-right-width:4px;border-top-style:solid;border-top-color:red;border-top-width:4px}.frame-right-bottom[data-v-f0bff8d2]{position:absolute;width:20px;height:20px;right:-6px;bottom:-6px;border-right-style:solid;border-right-color:red;border-right-width:4px;border-bottom-style:solid;border-bottom-color:red;border-bottom-width:4px}",""]),t.exports=r},"24fb":function(t,r,e){"use strict";function n(t,r){var e=t[1]||"",n=t[3];if(!n)return e;if(r&&"function"===typeof btoa){var i=o(n),a=n.sources.map((function(t){return"/*# sourceURL=".concat(n.sourceRoot||"").concat(t," */")}));return[e].concat(a).concat([i]).join("\n")}return[e].join("\n")}function o(t){var r=btoa(unescape(encodeURIComponent(JSON.stringify(t)))),e="sourceMappingURL=data:application/json;charset=utf-8;base64,".concat(r);return"/*# ".concat(e," */")}t.exports=function(t){var r=[];return r.toString=function(){return this.map((function(r){var e=n(r,t);return r[2]?"@media ".concat(r[2]," {").concat(e,"}"):e})).join("")},r.i=function(t,e,n){"string"===typeof t&&(t=[[null,t,""]]);var o={};if(n)for(var i=0;i<this.length;i++){var a=this[i][0];null!=a&&(o[a]=!0)}for(var d=0;d<t.length;d++){var l=[].concat(t[d]);n&&o[l[0]]||(e&&(l[2]?l[2]="".concat(e," and ").concat(l[2]):l[2]=e),r.push(l))}},r}},"29c9":function(t,r,e){"use strict";var n=e("a416"),o=e.n(n);o.a},3600:function(t,r,e){var n=e("089e");"string"===typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);var o=e("7f7e").default;o("1709e68e",n,!0,{sourceMap:!1,shadowMode:!1})},3827:function(t,r,e){"use strict";var n,o=function(){var t=this,r=t.$createElement,e=t._self._c||r;return e("v-uni-view",{attrs:{_i:0}},[t._$g(1,"i")?e("v-uni-video",{style:t._$g(1,"s"),attrs:{src:t._$g(1,"a-src"),controls:!0,autoplay:"true",_i:1}}):e("v-uni-web-view",{attrs:{src:t._$g(2,"a-src"),_i:2}})],1)},i=[];e.d(r,"b",(function(){return o})),e.d(r,"c",(function(){return i})),e.d(r,"a",(function(){return n}))},"38c0":function(t,r,e){"use strict";e.r(r);var n=e("3827"),o=e("8eec");for(var i in o)"default"!==i&&function(t){e.d(r,t,(function(){return o[t]}))}(i);var a,d=e("f0c5"),l=Object(d["a"])(o["default"],n["b"],n["c"],!1,null,null,null,!1,n["a"],a);r["default"]=l.exports},5764:function(t,r,e){"use strict";"undefined"===typeof Promise||Promise.prototype.finally||(Promise.prototype.finally=function(t){var r=this.constructor;return this.then((function(e){return r.resolve(t()).then((function(){return e}))}),(function(e){return r.resolve(t()).then((function(){throw e}))}))}),uni.restoreGlobal&&uni.restoreGlobal(weex,plus,setTimeout,clearTimeout,setInterval,clearInterval),__definePage("pages/common/video/video",(function(){return Vue.extend(e("38c0").default)})),__definePage("components/yx-form-avatar/yx-form-avatar-cutter",(function(){return Vue.extend(e("ae65").default)}))},"69c1":function(t,r,e){"use strict";Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var n={data:function(){return{wxsProps:{}}},components:{}};r.default=n},"6a4f":function(t,r,e){"use strict";e.r(r);var n=e("9117"),o=e("a706");for(var i in o)"default"!==i&&function(t){e.d(r,t,(function(){return o[t]}))}(i);e("88f8");var a,d=e("f0c5"),l=Object(d["a"])(o["default"],n["b"],n["c"],!1,null,"f0bff8d2",null,!1,n["a"],a);r["default"]=l.exports},"7f7e":function(t,r,e){"use strict";function n(t,r){for(var e=[],n={},o=0;o<r.length;o++){var i=r[o],a=i[0],d=i[1],l=i[2],f=i[3],p={id:t+":"+o,css:d,media:l,sourceMap:f};n[a]?n[a].parts.push(p):e.push(n[a]={id:a,parts:[p]})}return e}e.r(r),e.d(r,"default",(function(){return b}));var o="undefined"!==typeof document;if("undefined"!==typeof DEBUG&&DEBUG&&!o)throw new Error("vue-style-loader cannot be used in a non-browser environment. Use { target: 'node' } in your Webpack config to indicate a server-rendering environment.");var i={},a=o&&(document.head||document.getElementsByTagName("head")[0]),d=null,l=0,f=!1,p=function(){},c=null,s="data-vue-ssr-id",g="undefined"!==typeof navigator&&/msie [6-9]\b/.test(navigator.userAgent.toLowerCase());function b(t,r,e,o){f=e,c=o||{};var a=n(t,r);return u(a),function(r){for(var e=[],o=0;o<a.length;o++){var d=a[o],l=i[d.id];l.refs--,e.push(l)}r?(a=n(t,r),u(a)):a=[];for(o=0;o<e.length;o++){l=e[o];if(0===l.refs){for(var f=0;f<l.parts.length;f++)l.parts[f]();delete i[l.id]}}}}function u(t){for(var r=0;r<t.length;r++){var e=t[r],n=i[e.id];if(n){n.refs++;for(var o=0;o<n.parts.length;o++)n.parts[o](e.parts[o]);for(;o<e.parts.length;o++)n.parts.push(h(e.parts[o]));n.parts.length>e.parts.length&&(n.parts.length=e.parts.length)}else{var a=[];for(o=0;o<e.parts.length;o++)a.push(h(e.parts[o]));i[e.id]={id:e.id,refs:1,parts:a}}}}function x(){var t=document.createElement("style");return t.type="text/css",a.appendChild(t),t}function h(t){var r,e,n=document.querySelector("style["+s+'~="'+t.id+'"]');if(n){if(f)return p;n.parentNode.removeChild(n)}if(g){var o=l++;n=d||(d=x()),r=v.bind(null,n,o,!1),e=v.bind(null,n,o,!0)}else n=x(),r=w.bind(null,n),e=function(){n.parentNode.removeChild(n)};return r(t),function(n){if(n){if(n.css===t.css&&n.media===t.media&&n.sourceMap===t.sourceMap)return;r(t=n)}else e()}}var m=function(){var t=[];return function(r,e){return t[r]=e,t.filter(Boolean).join("\n")}}();function v(t,r,e,n){var o=e?"":j(n.css);if(t.styleSheet)t.styleSheet.cssText=m(r,o);else{var i=document.createTextNode(o),a=t.childNodes;a[r]&&t.removeChild(a[r]),a.length?t.insertBefore(i,a[r]):t.appendChild(i)}}function w(t,r){var e=j(r.css),n=r.media,o=r.sourceMap;if(n&&t.setAttribute("media",n),c.ssrId&&t.setAttribute(s,r.id),o&&(e+="\n/*# sourceURL="+o.sources[0]+" */",e+="\n/*# sourceMappingURL=data:application/json;base64,"+btoa(unescape(encodeURIComponent(JSON.stringify(o))))+" */"),t.styleSheet)t.styleSheet.cssText=e;else{while(t.firstChild)t.removeChild(t.firstChild);t.appendChild(document.createTextNode(e))}}var k=/([+-]?\d+(\.\d+)?)[r|u]px/g,y=/var\(--status-bar-height\)/gi,_=/var\(--window-top\)/gi,$=/var\(--window-bottom\)/gi,C=!1;function j(t){if(!uni.canIUse("css.var")){!1===C&&(C=plus.navigator.getStatusbarHeight());var r={statusBarHeight:C,top:window.__WINDOW_TOP||0,bottom:window.__WINDOW_BOTTOM||0};t=t.replace(y,r.statusBarHeight+"px").replace(_,r.top+"px").replace($,r.bottom+"px")}return t.replace(k,(function(t,r){return uni.upx2px(r)+"px"}))}},"88f8":function(t,r,e){"use strict";var n=e("3600"),o=e.n(n);o.a},"8eec":function(t,r,e){"use strict";e.r(r);var n=e("fb7f"),o=e.n(n);for(var i in n)"default"!==i&&function(t){e.d(r,t,(function(){return n[t]}))}(i);r["default"]=o.a},9117:function(t,r,e){"use strict";var n,o=function(){var t=this,r=t.$createElement,e=t._self._c||r;return e("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t._$g(0,"v-show"),expression:"_$g(0,'v-show')"}],staticClass:t._$g(0,"sc"),attrs:{_i:0}},[e("v-uni-canvas",{style:t._$g(1,"s"),attrs:{"canvas-id":"target",_i:1}}),e("v-uni-view",{staticClass:t._$g(2,"sc"),attrs:{_i:2}},[t._$g(3,"i")?e("v-uni-image",{staticClass:t._$g(3,"sc"),style:t._$g(3,"s"),attrs:{src:t._$g(3,"a-src"),_i:3},on:{load:function(r){return t.$handleViewEvent(r)}}}):t._e(),t._$g(4,"i")?e("v-uni-view",{staticClass:t._$g(4,"sc"),attrs:{_i:4}}):t._e(),e("v-uni-view",{staticClass:t._$g(5,"sc"),attrs:{_i:5},on:{touchstart:function(r){return t.$handleViewEvent(r)},touchmove:function(r){return t.$handleViewEvent(r)},touchend:function(r){return t.$handleViewEvent(r)},touchcancel:function(r){return t.$handleViewEvent(r)}}},[e("v-uni-view",{staticClass:t._$g(6,"sc"),style:t._$g(6,"s"),attrs:{_i:6},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}},[t._$g(7,"i")?e("v-uni-canvas",{staticClass:t._$g(7,"sc"),style:t._$g(7,"s"),attrs:{"canvas-id":"canvas",_i:7}}):t._e(),e("v-uni-view",{staticClass:t._$g(8,"sc"),attrs:{_i:8}}),e("v-uni-view",{staticClass:t._$g(9,"sc"),attrs:{_i:9}}),e("v-uni-view",{staticClass:t._$g(10,"sc"),attrs:{_i:10}}),e("v-uni-view",{staticClass:t._$g(11,"sc"),attrs:{_i:11}}),e("v-uni-view",{staticClass:t._$g(12,"sc"),attrs:{_i:12}}),e("v-uni-view",{staticClass:t._$g(13,"sc"),attrs:{_i:13},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(14,"sc"),attrs:{_i:14},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(15,"sc"),attrs:{_i:15},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(16,"sc"),attrs:{_i:16},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(17,"sc"),attrs:{_i:17},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(18,"sc"),attrs:{_i:18},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(19,"sc"),attrs:{_i:19},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}}),e("v-uni-view",{staticClass:t._$g(20,"sc"),attrs:{_i:20},on:{touchstart:function(r){return t.$handleViewEvent(r,{stop:!0,prevent:!0})}}})],1)],1)],1),e("v-uni-view",{staticClass:t._$g(21,"sc"),attrs:{_i:21}},[e("v-uni-button",{staticClass:t._$g(22,"sc"),attrs:{_i:22},on:{click:function(r){return t.$handleViewEvent(r)}}},[t._v("返回")]),e("v-uni-button",{staticClass:t._$g(23,"sc"),attrs:{_i:23},on:{click:function(r){return t.$handleViewEvent(r)}}},[t._v("确定")])],1)],1)},i=[];e.d(r,"b",(function(){return o})),e.d(r,"c",(function(){return i})),e.d(r,"a",(function(){return n}))},a06a:function(t,r,e){var n=e("24fb");r=n(!1),r.push([t.i,'@charset "UTF-8";\n/**\r\n * 这里是uni-app内置的常用样式变量\r\n *\r\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\r\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\r\n *\r\n */\n/**\r\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\r\n *\r\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\r\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */.text-title{color:#666;font-size:28rpx;font-weight:300}.form-item-avatar{width:120rpx;height:120rpx;margin-top:15rpx;margin-bottom:15rpx;border-top-left-radius:60rpx;border-top-right-radius:60rpx;border-bottom-right-radius:60rpx;border-bottom-left-radius:60rpx}',""]),t.exports=r},a310:function(t,r,e){"use strict";e.r(r);var n=e("06e7"),o=e.n(n);for(var i in n)"default"!==i&&function(t){e.d(r,t,(function(){return n[t]}))}(i);r["default"]=o.a},a416:function(t,r,e){var n=e("a06a");"string"===typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);var o=e("7f7e").default;o("3afaa1c6",n,!0,{sourceMap:!1,shadowMode:!1})},a706:function(t,r,e){"use strict";e.r(r);var n=e("69c1"),o=e.n(n);for(var i in n)"default"!==i&&function(t){e.d(r,t,(function(){return n[t]}))}(i);r["default"]=o.a},ae65:function(t,r,e){"use strict";e.r(r);var n=e("02b3"),o=e("a310");for(var i in o)"default"!==i&&function(t){e.d(r,t,(function(){return o[t]}))}(i);e("29c9");var a,d=e("f0c5"),l=Object(d["a"])(o["default"],n["b"],n["c"],!1,null,null,null,!1,n["a"],a);r["default"]=l.exports},b997:function(t,r,e){var n=e("24fb");r=n(!1),r.push([t.i,'\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n/* ==================\n          按钮\n ==================== */.cu-btn{position:relative;border:0upx;display:-webkit-inline-box;display:-webkit-inline-flex;display:inline-flex;-webkit-box-align:center;-webkit-align-items:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center;box-sizing:border-box;padding:0 30upx;font-size:28upx;height:64upx;line-height:1;text-align:center;text-decoration:none;overflow:visible;margin-left:0;-webkit-transform:translate(0upx);transform:translate(0upx);margin-right:0}.cu-btn::after{display:none}.cu-btn:not([class*="bg-"]){background-color:#f0f0f0}.cu-btn[class*="line"]{background-color:initial}.cu-btn[class*="line"]::after{content:" ";display:block;width:200%;height:200%;position:absolute;top:0;left:0;border:1upx solid currentColor;-webkit-transform:scale(.5);transform:scale(.5);-webkit-transform-origin:0 0;transform-origin:0 0;box-sizing:border-box;border-radius:12upx;z-index:1;pointer-events:none}.cu-btn.round[class*="line"]::after{border-radius:1000upx}.cu-btn[class*="lines"]::after{border:6upx solid currentColor}.cu-btn[class*="bg-"]::after{display:none}.cu-btn.sm{padding:0 20upx;font-size:20upx;height:48upx}.cu-btn.lg{padding:0 40upx;font-size:32upx;height:80upx}.cu-btn.cuIcon.sm{width:48upx;height:48upx}.cu-btn.cuIcon{width:64upx;height:64upx;border-radius:500upx;padding:0}uni-button.cuIcon.lg{width:80upx;height:80upx}.cu-btn.shadow-blur::before{top:4upx;left:4upx;-webkit-filter:blur(6upx);filter:blur(6upx);opacity:.6}.cu-btn.button-hover{-webkit-transform:translate(1upx,1upx);transform:translate(1upx,1upx)}.block{display:block}.cu-btn.block{display:-webkit-box;display:-webkit-flex;display:flex}.cu-btn[disabled]{opacity:.6;color:#fff}\n/* ColorUI */\n\n/* ==================\n        初始化\n ==================== */\n/*通用 */.full-width{width:750rpx}.tabbar-height{height:84rpx}.container{padding:40rpx}\n/* ==================\n          布局\n ==================== */\n/*  -- flex弹性布局 -- */\n.flex{display:-webkit-box;display:-webkit-flex;display:flex}\n.flex-row{\r\ndisplay:-webkit-box;display:-webkit-flex;display:flex;\r\n-webkit-box-orient:horizontal;-webkit-box-direction:normal;-webkit-flex-direction:row;flex-direction:row}.flex-column{\r\ndisplay:-webkit-box;display:-webkit-flex;display:flex;\r\n-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;flex-direction:column}.flex-wrap{-webkit-flex-wrap:wrap;flex-wrap:wrap}.flex-nowrap{-webkit-flex-wrap:nowrap;flex-wrap:nowrap}.flex-between{-webkit-box-pack:justify;-webkit-justify-content:space-between;justify-content:space-between}.flex-start{-webkit-box-pack:start;-webkit-justify-content:flex-start;justify-content:flex-start}.flex-center{-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center}.flex-end{-webkit-box-pack:end;-webkit-justify-content:flex-end;justify-content:flex-end}.flex-vtop{-webkit-box-align:start;-webkit-align-items:flex-start;align-items:flex-start}.flex-vcenter{-webkit-box-align:center;-webkit-align-items:center;align-items:center}.flex-vbottom{-webkit-box-align:end;-webkit-align-items:flex-end;align-items:flex-end}.flex-1{-webkit-box-flex:1;-webkit-flex:1;flex:1}.flex-2{-webkit-box-flex:2;-webkit-flex:2;flex:2}.flex-3{-webkit-box-flex:3;-webkit-flex:3;flex:3}.flex-4{-webkit-box-flex:4;-webkit-flex:4;flex:4}.flex-5{-webkit-box-flex:5;-webkit-flex:5;flex:5}.flex-6{-webkit-box-flex:6;-webkit-flex:6;flex:6}.flex-7{-webkit-box-flex:7;-webkit-flex:7;flex:7}.flex-8{-webkit-box-flex:8;-webkit-flex:8;flex:8}.flex-9{-webkit-box-flex:9;-webkit-flex:9;flex:9}.flex-10{-webkit-box-flex:10;-webkit-flex:10;flex:10}.flex-11{-webkit-box-flex:11;-webkit-flex:11;flex:11}.flex-12{-webkit-box-flex:12;-webkit-flex:12;flex:12}.flex-13{-webkit-box-flex:13;-webkit-flex:13;flex:13}\n/*  -- 内外边距 -- */.margin-0{margin:0}.margin-10{margin:10rpx}.margin-20{margin:20rpx}.margin, \r\n.margin-30{margin:30rpx}.margin-40{margin:40rpx}.margin-50{margin:50rpx}.margin-60{margin:60rpx}.margin-top-10{margin-top:10rpx}.margin-top-20{margin-top:20rpx}.margin-top,\r\n.margin-top-30{margin-top:30rpx}.margin-top-40{margin-top:40rpx}.margin-top-50{margin-top:50rpx}.margin-top-60{margin-top:60rpx}.margin-right-10{margin-right:10rpx}.margin-right-20{margin-right:20rpx}.margin-right,\r\n.margin-right-30{margin-right:30rpx}.margin-right-40{margin-right:40rpx}.margin-right-50{margin-right:50rpx}.margin-right-60{margin-right:60rpx}.margin-bottom-10{margin-bottom:10rpx}.margin-bottom-20{margin-bottom:20rpx}.margin-bottom,\r\n.margin-bottom-30{margin-bottom:30rpx}.margin-bottom-40{margin-bottom:40rpx}.margin-bottom-50{margin-bottom:50rpx}.margin-bottom-60{margin-bottom:60rpx}.margin-left-10{margin-left:10rpx}.margin-left-20{margin-left:20rpx}.margin-left,\r\n.margin-left-30{margin-left:30rpx}.margin-left-40{margin-left:40rpx}.margin-left-50{margin-left:50rpx}.margin-left-60{margin-left:60rpx}.margin-lr-10{margin-left:10rpx;margin-right:10rpx}.margin-lr-20{margin-left:20rpx;margin-right:20rpx}.margin-lr,\r\n.margin-lr-30{margin-left:30rpx;margin-right:30rpx}.margin-lr-40{margin-left:40rpx;margin-right:40rpx}.margin-lr-50{margin-left:50rpx;margin-right:50rpx}.margin-lr-60{margin-left:60rpx;margin-right:60rpx}.margin-tb-10{margin-top:10rpx;margin-bottom:10rpx}.margin-tb-20{margin-top:20rpx;margin-bottom:20rpx}.margin-tb,\r\n.margin-tb-30{margin-top:30rpx;margin-bottom:30rpx}.margin-tb-40{margin-top:40rpx;margin-bottom:40rpx}.margin-tb-50{margin-top:50rpx;margin-bottom:50rpx}.margin-tb-60{margin-top:60rpx;margin-bottom:60rpx}.padding-0{padding:0}.padding-10{padding:10rpx}.padding-20{padding:20rpx}.padding,\r\n.padding-30{padding:30rpx}.padding-40{padding:40rpx}.padding-50{padding:50rpx}.padding-60{padding:60rpx}.padding-top-10{padding-top:10rpx}.padding-top-20{padding-top:20rpx}.padding-top,\r\n.padding-top-30{padding-top:30rpx}.padding-top-40{padding-top:40rpx}.padding-top-50{padding-top:50rpx}.padding-top-60{padding-top:60rpx}.padding-right-10{padding-right:10rpx}.padding-right-20{padding-right:20rpx}.padding-right,\r\n.padding-right-30{padding-right:30rpx}.padding-right-40{padding-right:40rpx}.padding-right-50{padding-right:50rpx}.padding-right-60{padding-right:60rpx}.padding-bottom-10{padding-bottom:10rpx}.padding-bottom-20{padding-bottom:20rpx}.padding-bottom,\r\n.padding-bottom-30{padding-bottom:30rpx}.padding-bottom-40{padding-bottom:40rpx}.padding-bottom-50{padding-bottom:50rpx}.padding-bottom-60{padding-bottom:60rpx}.padding-left-10{padding-left:10rpx}.padding-left-20{padding-left:20rpx}.padding-left,\r\n.padding-left-30{padding-left:30rpx}.padding-left-40{padding-left:40rpx}.padding-left-50{padding-left:50rpx}.padding-left-60{padding-left:60rpx}.padding-lr-10{padding-left:10rpx;padding-right:10rpx}.padding-lr-20{padding-left:20rpx;padding-right:20rpx}.padding-lr,\r\n.padding-lr-30{padding-left:30rpx;padding-right:30rpx}.padding-lr-40{padding-left:40rpx;padding-right:40rpx}.padding-lr-50{padding-left:50rpx;padding-right:50rpx}.padding-lr-60{padding-left:60rpx;padding-right:60rpx}.padding-tb-10{padding-top:10rpx;padding-bottom:10rpx}.padding-tb-20{padding-top:20rpx;padding-bottom:20rpx}.padding-tb,\r\n.padding-tb-30{padding-top:30rpx;padding-bottom:30rpx}.padding-tb-40{padding-top:40rpx;padding-bottom:40rpx}.padding-tb-50{padding-top:50rpx;padding-bottom:50rpx}.padding-tb-60{padding-top:60rpx;padding-bottom:60rpx}\n/* ==================\r\n          背景\r\n ==================== */.line-red::after,\r\n.lines-red::after{border-color:#e54d42}.line-orange::after,\r\n.lines-orange::after{border-color:#f37b1d}.line-yellow::after,\r\n.lines-yellow::after{border-color:#fbbd08}.line-olive::after,\r\n.lines-olive::after{border-color:#8dc63f}.line-green::after,\r\n.lines-green::after{border-color:#39b54a}.line-cyan::after,\r\n.lines-cyan::after{border-color:#1cbbb4}.line-blue,\r\n.lines-blue::after{border-color:#0081ff}.line-purple::after,\r\n.lines-purple::after{border-color:#6739b6}.line-mauve::after,\r\n.lines-mauve::after{border-color:#9c26b0}.line-pink::after,\r\n.lines-pink::after{border-color:#e03997}.line-brown::after,\r\n.lines-brown::after{border-color:#a5673f}.line-grey::after,\r\n.lines-grey::after{border-color:#8799a3}.line-gray::after,\r\n.lines-gray::after{border-color:#aaa}.line-black::after,\r\n.lines-black::after{border-color:#333}.line-white::after,\r\n.lines-white::after{border-color:#fff}.bg-red{background-color:#e54d42;color:#fff}.bg-orange{background-color:#f37b1d;color:#fff}.bg-yellow{background-color:#fbbd08;color:#333}.bg-olive{background-color:#8dc63f;color:#fff}.bg-green{background-color:#39b54a;color:#fff}.bg-cyan{background-color:#1cbbb4;color:#fff}.bg-blue{background-color:#0081ff;color:#fff}.bg-purple{background-color:#6739b6;color:#fff}.bg-mauve{background-color:#9c26b0;color:#fff}.bg-pink{background-color:#e03997;color:#fff}.bg-brown{background-color:#a5673f;color:#fff}.bg-grey{background-color:#8799a3;color:#fff}.bg-gray{background-color:#f0f0f0;color:#333}.bg-black{background-color:#333;color:#fff}.bg-white{background-color:#fff;color:#666}.bg-shadeTop{background-image:-webkit-linear-gradient(#000,rgba(0,0,0,.01));background-image:linear-gradient(#000,rgba(0,0,0,.01));color:#fff}.bg-shadeBottom{background-image:-webkit-linear-gradient(rgba(0,0,0,.01),#000);background-image:linear-gradient(rgba(0,0,0,.01),#000);color:#fff}.bg-red-light{color:#e54d42;background-color:#fadbd9}.bg-orange-light{color:#f37b1d;background-color:#fde6d2}\n/* .bg-yellow-light {\r\n\tcolor: #fbbd08;\r\n\tbackground-color: #fef2ced2;\r\n}\r\n */.bg-olive-light{color:#8dc63f;background-color:#e8f4d9}\n/* .bg-green-light {\r\n\tcolor: #39b54a;\r\n\tbackground-color: #d7f0dbff;\r\n} */.bg-cyan-light{color:#1cbbb4;background-color:#d2f1f0}.bg-blue-light{color:#0081ff;background-color:#cce6ff}.bg-purple-light{color:#6739b6;background-color:#e1d7f0}.bg-mauve-light{color:#9c26b0;background-color:#ebd4ef}.bg-pink-light{color:#e03997;background-color:#f9d7ea}.bg-brown-light{color:#a5673f;background-color:#ede1d9}.bg-grey-light{color:#8799a3;background-color:#e7ebed}.bg-gradual-red{background-image:-webkit-linear-gradient(45deg,#f43f3b,#ec008c);background-image:linear-gradient(45deg,#f43f3b,#ec008c);color:#fff}.bg-gradual-orange{background-image:-webkit-linear-gradient(45deg,#ff9700,#ed1c24);background-image:linear-gradient(45deg,#ff9700,#ed1c24);color:#fff}.bg-gradual-green{background-image:-webkit-linear-gradient(45deg,#39b54a,#8dc63f);background-image:linear-gradient(45deg,#39b54a,#8dc63f);color:#fff}.bg-gradual-purple{background-image:-webkit-linear-gradient(45deg,#9000ff,#5e00ff);background-image:linear-gradient(45deg,#9000ff,#5e00ff);color:#fff}.bg-gradual-pink{background-image:-webkit-linear-gradient(45deg,#ec008c,#6739b6);background-image:linear-gradient(45deg,#ec008c,#6739b6);color:#fff}.bg-gradual-blue{background-image:-webkit-linear-gradient(45deg,#0081ff,#1cbbb4);background-image:linear-gradient(45deg,#0081ff,#1cbbb4);color:#fff}\n/* ==================\n          文本\n ==================== */\n/* \r\n.text {margin-bottom: 20rpx; line-height: 32rpx;}\r\n.text-h1 {font-size: 64rpx; line-height: 128rpx;}\r\n.text-h2 {font-size: 52rpx; line-height: 104rpx;}\r\n.text-h3 {font-size: 40rpx; line-height: 80rpx;}\r\n.text-h4 {font-size: 36rpx; line-height: 72rpx;}\r\n.text-h5 {font-size: 32rpx; line-height: 64rpx;}\r\n.text-h6 {font-size: 28rpx; line-height: 56rpx;}\r\n.text-h7 {font-size: 24rpx; line-height: 48rpx;}\r\n.text-h8 {font-size: 20rpx; line-height: 40rpx;}\r\n */.text{margin-bottom:20rpx;line-height:32rpx}.text-h1{font-size:60rpx}.text-h2{font-size:40rpx}.text-h3{font-size:34rpx}.text-h4{font-size:30rpx}.text-h5{font-size:25rpx}.text-h6{font-size:22rpx}.text-lineheight-30{line-height:30rpx}.text-lineheight-40{line-height:40rpx}.text-lineheight-50{line-height:50rpx}.text-lineheight-60{line-height:60rpx}.text-link{color:#2b79da}.text-bold{font-weight:700}.text-bold-500{font-weight:500}.text-bold-600{font-weight:600}.text-center{text-align:center}.text-left{text-align:left}.text-right{text-align:right}.text-red,\r\n.line-red,\r\n.lines-red{color:#e54d42}.text-orange,\r\n.line-orange,\r\n.lines-orange{color:#f37b1d}.text-yellow,\r\n.line-yellow,\r\n.lines-yellow{color:#fbbd08}.text-olive,\r\n.line-olive,\r\n.lines-olive{color:#8dc63f}.text-green,\r\n.line-green,\r\n.lines-green{color:#39b54a}.text-cyan,\r\n.line-cyan,\r\n.lines-cyan{color:#1cbbb4}.text-blue,\r\n.line-blue,\r\n.lines-blue{color:#0081ff}.text-purple,\r\n.line-purple,\r\n.lines-purple{color:#6739b6}.text-mauve,\r\n.line-mauve,\r\n.lines-mauve{color:#9c26b0}.text-pink,\r\n.line-pink,\r\n.lines-pink{color:#e03997}.text-brown,\r\n.line-brown,\r\n.lines-brown{color:#a5673f}.text-grey,\r\n.line-grey,\r\n.lines-grey{color:#8799a3}.text-gray,\r\n.line-gray,\r\n.lines-gray{color:#aaa}.text-black,\r\n.line-black,\r\n.lines-black{color:#333}.text-black-light,\r\n.line-black-light,\r\n.lines-black-light{color:#666}.text-white,\r\n.line-white,\r\n.lines-white{color:#fff}\n/* ==================\n          边框\n ==================== */.solid{border-top-width:1rpx;border-top-color:rgba(0,0,0,.1);border-left-width:1rpx;border-left-color:rgba(0,0,0,.1);border-right-width:1rpx;border-right-color:rgba(0,0,0,.1);border-bottom-width:1rpx;border-bottom-color:rgba(0,0,0,.1)}.solid-top{border-top-width:1rpx;border-top-color:rgba(0,0,0,.1)}.solids-top{border-top-width:1px;border-top-color:rgba(0,0,0,.1)}.solid-left{border-left-width:1rpx;border-left-color:rgba(0,0,0,.1)}.solids-left{border-left-width:1px;border-left-color:rgba(0,0,0,.1)}.solid-right{border-right-width:1rpx;border-right-color:rgba(0,0,0,.1)}.solids-right{border-right-width:1px;border-right-color:rgba(0,0,0,.1)}.solid-bottom{border-bottom-width:1rpx;border-bottom-color:rgba(0,0,0,.1)}.solids-bottom{border-bottom-width:1px;border-bottom-color:rgba(0,0,0,.1)}\n/* ==================\n          按钮\n ==================== */.yx-btn{position:relative;border-width:0;-webkit-box-align:center;-webkit-align-items:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center;padding:0 30rpx;height:88rpx;line-height:1;text-align:center;text-decoration:none;-webkit-transform:translate(0rpx);transform:translate(0rpx);border-top-left-radius:5px;border-top-right-radius:5px;border-bottom-left-radius:5px;border-bottom-right-radius:5px;color:#fff}.yx-btn__sm{padding:0 20rpx;font-size:20rpx;height:48rpx}.yx-btn__lg{padding:0 40rpx;font-size:32rpx;height:80rpx}.my-logo{height:120rpx;width:120rpx}.my-header{width:750rpx;position:fixed;left:0;top:0;z-index:99}.my-footer{width:750rpx;position:fixed;left:0;bottom:0;z-index:99}.yx-footer{width:750rpx;position:fixed;left:0;bottom:0;z-index:99}.yx-split-line{width:670rpx;height:1rpx;background-color:#ebebeb \r\n  /* margin-left: 40rpx; */\r\n/*  margin-top: 40rpx;\r\n  margin-bottom: 40rpx; */}.yx-split-stripe{width:1000rpx;height:16rpx;margin-left:-100rpx;\r\n  /* margin-top: 40rpx; */background-color:#f4f5f6}.yx-list-item-line{position:absolute;left:30rpx;right:30rpx;bottom:0;height:1rpx;background-color:#ebebeb}.yx-bottom-blank{width:750rpx;height:200rpx}.yx-avatar-round-md{width:140rpx;height:140rpx;border-width:1rpx;border-color:#f5f5f5;border-top-left-radius:70rpx;border-top-right-radius:70rpx;border-bottom-right-radius:70rpx;border-bottom-left-radius:70rpx}.yx-avatar-md{width:120rpx;height:120rpx;border-width:1rpx;border-color:#f5f5f5;border-top-left-radius:20rpx;border-top-right-radius:20rpx;border-bottom-right-radius:20rpx;border-bottom-left-radius:20rpx}.yx-avatar-sm{width:90rpx;height:90rpx;border-width:1rpx;border-color:#f5f5f5;border-top-left-radius:12rpx;border-top-right-radius:12rpx;border-bottom-right-radius:12rpx;border-bottom-left-radius:12rpx}.yx-button-color{\r\n /* height: 100rpx;\r\n  top: 20rpx;\r\n  left: 50rpx;\r\n  width: 650rpx; */background-image:-webkit-linear-gradient(left,#55d6ba,#55d6ba);background-image:linear-gradient(90deg,#55d6ba,#55d6ba);border-width:0;\r\nfont-size:17px\r\n}',""]),t.exports=r},b9f4:function(t,r,e){var n=e("b997");"string"===typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);var o=e("7f7e").default;o("12e04bea",n,!0,{sourceMap:!1,shadowMode:!1})},bbfe:function(t,r,e){"use strict";function n(){function t(t){var r=e("c1a2");r.__inject__&&r.__inject__(t)}"function"===typeof t&&t(),UniViewJSBridge.publishHandler("webviewReady")}e("5764"),"undefined"!==typeof plus?n():document.addEventListener("plusready",n)},c1a2:function(t,r,e){"use strict";e.r(r);var n=e("b9f4"),o=e.n(n);for(var i in n)"default"!==i&&function(t){e.d(r,t,(function(){return n[t]}))}(i);r["default"]=o.a},f0c5:function(t,r,e){"use strict";function n(t,r,e,n,o,i,a,d,l,f){var p,c="function"===typeof t?t.options:t;if(l){c.components||(c.components={});var s=Object.prototype.hasOwnProperty;for(var g in l)s.call(l,g)&&!s.call(c.components,g)&&(c.components[g]=l[g])}if(f&&((f.beforeCreate||(f.beforeCreate=[])).unshift((function(){this[f.__module]=this})),(c.mixins||(c.mixins=[])).push(f)),r&&(c.render=r,c.staticRenderFns=e,c._compiled=!0),n&&(c.functional=!0),i&&(c._scopeId="data-v-"+i),a?(p=function(t){t=t||this.$vnode&&this.$vnode.ssrContext||this.parent&&this.parent.$vnode&&this.parent.$vnode.ssrContext,t||"undefined"===typeof __VUE_SSR_CONTEXT__||(t=__VUE_SSR_CONTEXT__),o&&o.call(this,t),t&&t._registeredComponents&&t._registeredComponents.add(a)},c._ssrRegister=p):o&&(p=d?function(){o.call(this,this.$root.$options.shadowRoot)}:o),p)if(c.functional){c._injectStyles=p;var b=c.render;c.render=function(t,r){return p.call(r),b(t,r)}}else{var u=c.beforeCreate;c.beforeCreate=u?[].concat(u,p):[p]}return{exports:t,options:c}}e.d(r,"a",(function(){return n}))},fb7f:function(t,r,e){"use strict";Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var n={data:function(){return{wxsProps:{}}},components:{}};r.default=n}});