(function() {
    'use strict';
    /**
     * @name eolinker open source，eolinker开源版本
     * @link https://www.eolinker.com
     * @package eolinker
     * @author www.eolinker.com 广州银云信息科技有限公司 2015-2018

     * eolinker，业内领先的Api接口管理及测试平台，为您提供最专业便捷的在线接口管理、测试、维护以及各类性能测试方案，帮助您高效开发、安全协作。
     * 如在使用的过程中有任何问题，可通过[图片]http://help.eolinker.com寻求帮助
     *
     * 注意！eolinker开源版本遵循GPL V3开源协议，仅供用户下载试用，禁止“一切公开使用于商业用途”或者“以eoLinker开源版本为基础而开发的二次版本”在互联网上流通。
     * 注意！一经发现，我们将立刻启用法律程序进行维权。
     * 再次感谢您的使用，希望我们能够共同维护国内的互联网开源文明和正常商业秩序。
     *
     * @function [按需加载文件存放常量集] [Loading files on demand to store const sets]
     * @version  3.2.0
     */
    angular
        .module('eolinker.constant')
        .constant('APP_REQUIRES', {
            // jQuery based and standalone scripts
            SCRIPTS: {},
            // Angular based script (use the right module name)
            MODULES: [
                // options {serie: true,insertBefore: '#load_styles_before'}
                {
                    name: 'CLIPBOARD',
                    files: ['vendor/clipboard/dist/clipboard.min.js?timestamp=33332']
                }, {
                    name: 'JQUERY',
                    files: [
                        "vendor/wangEditor/dist/js/lib/jquery-2.2.1.js?timestamp=33332"
                    ]
                }, {
                    name: 'PARTICLES',
                    files: ['vendor/particles.js/particles.min.js?timestamp=33332']
                }, {
                    name: 'CLIPBOARD',
                    files: ['vendor/clipboard/dist/clipboard.min.js?timestamp=33332']
                }, {
                    name: 'WANG_EDITOR',
                    files: [
                        'libs/wangEditor/dist/js/wangEditor.min.js?timestamp=33332',
                        "libs/wangEditor/dist/js/lib/plupload.full.min.js?timestamp=33332",
                        "libs/wangEditor/dist/js/lib/qiniu.min.js?timestamp=33332"
                    ]
                }, {
                    name: 'MARKDOWN_CSS',
                    files: [
                        "libs/editor.md/images/loading.gif?timestamp=33332",
                        "libs/editor.md/fonts/fontawesome-webfont.*",
                        "libs/editor.md/css/editormd.min.css?timestamp=33332"
                    ]
                }, {
                    name: 'MARKDOWN',
                    files: [
                        "libs/editor.md/images/loading.gif?timestamp=33332",
                        "libs/editor.md/fonts/fontawesome-webfont.*",
                        "libs/editor.md/css/editormd.min.css?timestamp=33332",
                        "libs/editor.md/editormd.min.js?timestamp=33332"
                    ]
                }, {
                    name: 'MOCK',
                    files: [
                        "vendor/mockjs/dist/mock.js?timestamp=33332"
                    ]
                }, {
                    name: 'QINIU_UPLOAD',
                    files: ['libs/angular-qiniu-upload/src/qupload.js?timestamp=33332', "libs/angular-local-storage/dist/angular-local-storage.js?timestamp=33332" ]
                }, {
                    name: 'HIGH_LIGHT',
                    files: ['libs/highlight.js/styles/rainbow.css?timestamp=33332','libs/highlight.js/lib/highlight.pack.js?timestamp=33332']
                }, {
                    name: 'IMG_CROP',
                    files: [
                        "libs/imgCrop/ng-img-crop.js?timestamp=33332"
                    ]
                }, {
                    name: 'ACE_EDITOR',
                    files: [
                        "libs/ace-builds/src/ace.js?timestamp=33332"
                    ]
                }
            ]
        })

        /**
         * [html 懒加载]
         * @type {constant}
         */
        .constant('HTML_LAZYLOAD', [{
            name: 'PAGINATION',
            files: [
                "libs/pagination/pagination.js?timestamp=33332"
            ]
        }, {
            name: 'LAZY_EDITOR',
            files: [
                "libs/editor.md/plugins/link-dialog/link-dialog.js?timestamp=33332",
                "libs/editor.md/plugins/table-dialog/table-dialog.js?timestamp=33332",
                "libs/editor.md/lib/**"
            ]
        }]);

})();