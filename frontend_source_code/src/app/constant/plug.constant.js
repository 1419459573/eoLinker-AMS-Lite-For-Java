(function () {
    'use strict';
    /*
     * aurhor:广州银云信息科技有限公司
     * 按需插件加载文件存放常量集
     */
    angular
        .module('eolinker.constant')
        .constant('PLUG_CONSTANT', {
            QUERY: [{
                    icon: 'icon-shandian',
                    name: '测试增强插件',
                    desc: '专为接口测试而生，配合接口管理平台实现更强大的功能，支持在线/本地测试（localhost）、支持发送文件请求，以及支持更多的请求协议。',
                    price: 0, //0免费
                    time: 0, //永久
                    key: 'plug',
                    useStatus: 0,
                    href: 'https://www.eolinker.com/#/plug/introduce',
                }, {
                    icon: 'icon-index-rocket',
                    name: '快捷助手',
                    desc: 'eoLinker专业版的第一个插件，只需要按下“Ctrl + ~”即可打开使用帮助功能。',
                    price: 0, 
                    time: 0, 
                    key: 'useTip',
                    disable: 1,
                    module: 'EOPLUG_TIPS',
                    directive: 'plug-tips',
                    useStatus: 0,
                    status: 1 //默认0，1：需要额外手动处理加载
                }, {
                    icon: 'icon-hexin',
                    name: '生成SDK',
                    desc: '根据接口文档智能生成SDK，支持各类主流语言，极大提高对接效率。',
                    price: 0, 
                    time: 0, 
                    key: 'sdk',
                    useStatus: 0,
                    disable: 1,

                }, {
                    icon: 'icon-chouyang',
                    name: 'Mock-请求校验',
                    desc: '对Mock的请求进行校验，支持校验请求方式、参数、参数类型等',
                    price: 0, 
                    time: 0, 
                    key: 'mockRequestValidate',
                    useStatus: 0,
                    disable: 1, //默认0，1：限制个人项目，2：暂未开发

                }
                // , {
                //     icon: 'icon-jingbao',
                //     name: 'WebHook',
                //     desc: '当项目发生变动时，请求目标服务器，方便与各类系统联动',
                //     price: 0, 
                //     time: 0, 
                //     key: 'webHook',
                //     useStatus: 0,
                //     disable: 1,

                // }
                , {
                    icon: 'icon-shuaxin',
                    name: 'MOCK-自动刷新',
                    desc: '每次请求都根据Mock规则返回随机数据',
                    price: 0, 
                    time: 0, 
                    key: 'mockDataAutomatic',
                    useStatus: 0,
                    disable: 1,
                }, {
                    icon: 'icon-daibanshixiang',
                    name: 'API编辑自动定时保存',
                    desc: '在编辑API时，每隔一定的时间间隔便会自动保存一次，防止因误操作丢失数据。',
                    price: 0, 
                    time: 0, 
                    key: 'apiAutomateSave',
                    disable: 1,
                }, {
                    icon: 'icon-chouyang',
                    name: '状态码查重',
                    desc: '添加状态码时进行查重，如果存在相同的状态码，则不允许添加。',
                    price: 0, 
                    time: 0, 
                    key: 'statusCodeCheckIsExist',
                    disable: 1,
                }, {
                    icon: 'icon-chouyang',
                    name: 'API查重',
                    desc: '设定根据API的URL或者名称进行查重，如果存在相同的API，则不允许添加。',
                    price: 0,
                    time: 0, 
                    key: 'apiCheckIsExist',
                    disable: 1,
                }
            ]
        })
        .constant('PLUG_ADDRESS', {
            // jQuery based and standalone scripts
            SCRIPTS: {},
            // Angular based script (use the right module name)
            MODULES: [
                // options {serie: true,insertBefore: '#load_styles_before'}
                {
                    name: 'EOPLUG_TIPS',
                    module: true,
                    directive: 'plug-tips',
                    files: ['plug/tip/index.js?timestamp=1527142021495']

                },
                {
                    name: 'EOPLUG_SDK',
                    module: true,
                    directive: 'plug-sdk',
                    files: ['plug/sdk/index.css?timestamp=1527142021496', 'plug/sdk/index.service.js?timestamp=1527142021495', 'plug/sdk/index.resource.js?timestamp=1527142021495', 'plug/sdk/index.js?timestamp=1527142021495']

                },
                {
                    name: 'EOPLUG_MOCK',
                    module: true,
                    directive: 'plug-mock',
                    files: ['plug/mock/index.css?timestamp=1527142021496', 'plug/mock/index.resource.js?timestamp=1527142021495', 'plug/mock/index.js?timestamp=1527142021495']

                },
                // {
                //     name: 'EOPLUG_WEBHOOK',
                //     module: true,
                //     directive:'plug-web-hook',
                //     files: ['plug/webHook/index.css?timestamp=1527142021496','plug/webHook/index.resource.js?timestamp=1527142021495','plug/webHook/index.js?timestamp=1527142021495']
                // },
                {
                    name: 'EOPLUG_MOCKAUTOMATIC',
                    module: true,
                    directive: 'plug-mock-automatic',
                    files: ['plug/mockAutomatic/index.css?timestamp=1527142021496', 'plug/mockAutomatic/index.js?timestamp=1527142021495']
                }, {
                    name: 'EOPLUG_SAVEAUTOMATIC',
                    module: true,
                    directive: 'plug-save-automatic',
                    files: ['plug/saveAutomatic/index.css?timestamp=1527142021496', 'plug/saveAutomatic/index.js?timestamp=1527142021495']
                }, {
                    name: 'EOPLUG_CHECKCODE',
                    module: true,
                    directive: 'plug-check-code',
                    files: ['plug/checkCode/index.css?timestamp=1527142021496', 'plug/checkCode/index.js?timestamp=1527142021495']
                }, {
                    name: 'EOPLUG_CHECKAPI',
                    module: true,
                    directive: 'plug-check-api',
                    files: ['plug/checkApi/index.css?timestamp=1527142021496', 'plug/checkApi/index.resource.js?timestamp=1527142021495', 'plug/checkApi/index.js?timestamp=1527142021495']
                }
            ]
        });

})();