(function() {
    //author：广州银云信息科技有限公司
    angular.module('eolinker')
        .component('homePlugSidebar', {
            templateUrl: 'app/component/content/home/content/plug/sidebar/index.html',
            controller: indexController,
            bindings: {
                object: '<'
            }
        })

    indexController.$inject = ['$scope','$state'];

    function indexController($scope, $state) {
        var vm = this;
        vm.data = {
            component: {
                groupCommonObject: {}
            },
            info: {
                menu: [{ name: '全部插件', status: -1}, { name: '已启用', status: 1 ,tipHtml:'<span>&nbsp;[{{item.useableNum}}]</span>'}, { name: '未启用', status: 0 }]
            },
            interaction:{
                request:{
                    status: $state.params.status || -1
                }
            },
            fun: {
                init: null, //初始化功能函数
                menu: null, //项目菜单选择功能函数
            }
        }
        vm.data.fun.menu = function(status,arg) {
            vm.data.interaction.request.status = arg.item.status;
            $state.go('home.plug.default',{status:arg.item.status});
        }
        vm.$onInit = function() {
            vm.data.component.groupCommonObject = {
                funObject: {
                    unTop:true
                },
                mainObject: {
                    baseInfo: {
                        title:'分类',
                        name: 'name',
                        id: 'status',
                        current: vm.data.interaction.request
                    },
                    baseFun: {
                        click: vm.data.fun.menu
                    }
                }
            }
        }
    }

})();
