(function() {
    "use strict";
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
     * @function [markdown指令js] [markdown command js] 
     
     * @service  $timeout [注入$timeout服务] [inject $timeout service]
     * @param    resultHtml [markdown编译结果存储位置] [markdown compile result storage location]
     */
    angular.module('eolinker.directive')

    .directive("markdown", editorFn);

    editorFn.$inject = ['$timeout']

    function editorFn($timeout) {

        return {
            restrict: 'AE',
            require: '?ngModel', //ng-model绑定markdown初始化数据 ng-model binds markdown to initialize data
            scope: {
                resultHtml: '='
            },
            link: function($scope, element, attrs, ngModel) {
                var textarea = !!attrs.editId ? attrs.editId : 'editormd-js';
                var editor = null;
                var timer = null;

                /**
                 * @function [初始化markdown] [Initialize markdown]
                 */
                $scope.$on('$changeNoteType', function(e, attr) { 
                    if (editor == null) {
                        editor = editormd(textarea, {
                            height: 445,
                            saveHTMLToTextarea: true,
                            autoFocus: false,
                            placeholder: '',
                            toolbarIcons: function() { //设置markdown导航栏标签功能 Set the markdown navigation bar tag function
                                // Or return editormd.toolbarModes[name]; // full, simple, mini
                                // Using "||" set icons align right.
                                return ["undo", "redo", "|",
                                    "bold", "del", "italic", "quote", "|",
                                    "h1", "h2", "h3", "h4", "h5", "h6", "|",
                                    "list-ul", "list-ol", "|",
                                    "link", "code", "table", "|",
                                    "watch"
                                ]
                            },
                            path: "./libs/editor.md/lib/", // Autoload modules mode, codemirror, marked... dependents libs path
                            imageUpload: true,
                            imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                            imageUploadURL: "",
                            onload: function() { //markdown初始化加载重置内容 markdown initializes the load to reset the content
                                try {
                                    if (!!ngModel.$viewValue) {
                                        editor.setMarkdown(ngModel.$viewValue);
                                    }
                                } catch (e) {

                                }
                            },
                            onchange: function() { //markdown内容改变执行函数 markdown contents change execution function
                                timer = $timeout(function() {
                                    //editor.getMarkdown();       // 获取 Markdown 源码 Get the Markdown source code
                                    //editor.getHTML();           // 获取 Textarea 保存的 HTML 源码 Get the HTML source code saved by Textarea
                                    //editor.getPreviewedHTML();  // 获取预览窗口里的 HTML，在开启 watch 且没有开启 saveHTMLToTextarea 时使用 Get the HTML in the preview window and use it when opening watch and not opening saveHTMLToTextarea
                                    $scope.resultHtml = editor.getPreviewedHTML();
                                    ngModel.$setViewValue(editor.getMarkdown());
                                }, 0, true)
                            }
                        });
                    }
                })
                
                /**
                 * @function [重置markdown] [Reset markdown]
                 */
                $scope.$on('$resetMarkdown', function() { 
                    if (editor) {
                        editor.setMarkdown('');
                    }
                });

                /**
                 * @function [页面更改消除计时器] [The page changes the elimination timer]
                 */
                $scope.$on('$destroy', function() { 
                    if (timer) {
                        $timeout.cancel(timer);
                    }
                });
            }
        }

    };

})();
