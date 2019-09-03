(function() {
    'use strict';

    angular.module('eolinker.resource')
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
     * @function [接口管理接口服务定义js] [API management interface service definition js]
     
     * @service  $resource [注入$resource服务] [Inject the $resource service]
     * @constant serverUrl [注入前缀URL] [Inject the prefix URL]
     */
    .factory('ApiManagementResource', ApiManagementResource)

    ApiManagementResource.$inject = ['$resource', 'serverUrl'];
    function ApiManagementResource($resource, serverUrl) {
        var data = {
            info: {
                api: [],
                method: 'POST'
            }
        }
        data.info.api['Import'] = $resource(serverUrl + 'Import/:operate', {

            }, {
                Eoapi: {
                    params: { operate: 'importEoapi' },
                    method: data.info.method
                },
                Postman: {
                    params: { operate: 'importPostMan' },
                    method: data.info.method
                },
                Dhc: {
                    params: { operate: 'importDHC' },
                    method: data.info.method
                },
                Rap: {
                    params: { operate: 'importRAP' },
                    method: data.info.method
                },
                Swagger: {
                    params: { operate: 'importSwagger' },
                    method: data.info.method
                }
            }

        );

        data.info.api['Export'] = $resource(serverUrl + 'Export/:operate', {

            }, {
                Project: {
                    params: { operate: 'exportProjectData' },
                    method: data.info.method
                }
            }

        );

        data.info.api['Project'] = $resource(serverUrl + 'Project/:operate', {

            }, {
                Query: {
                    params: { operate: 'getProjectList' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editProject' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addProject' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteProject' },
                    method: data.info.method
                },
                Detail: {
                    params: { operate: 'getProject' },
                    method: data.info.method
                },
                Dump: {
                    params: { operate: 'dumpProject' },
                    method: data.info.method
                },
                GetProjectLogList: {
                    params: { operate: 'getProjectLogList' },
                    method: data.info.method,
                }
            }

        );

        data.info.api['Api'] = $resource(serverUrl + 'Api/:operate', {

            }, {
                Query: {
                    params: { operate: 'getApiList' },
                    method: data.info.method
                },
                All: {
                    params: { operate: 'getAllApiList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addApi' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'removeApi' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editApi' },
                    method: data.info.method
                },
                Search: {
                    params: { operate: 'searchApi' },
                    method: data.info.method
                },
                Detail: {
                    params: { operate: 'getApi' },
                    method: data.info.method
                },
                HistoryList: {
                    params: { operate: 'getApiHistoryList' },
                    method: data.info.method,
                    cancellable:true
                },
                DeleteHistory: {
                    params: { operate: 'deleteApiHistory' },
                    method: data.info.method,
                    cancellable:true
                },
                toggleHistory: {
                    params: { operate: 'toggleApiHistory' },
                    method: data.info.method,
                    cancellable:true
                },
                Mock: {
                    params: { operate: 'getApiMockData' },
                    method: data.info.method,
                    cancellable:true
                },
                RefreshMock: {
                    params: { operate: 'editApiMockData' },
                    method: data.info.method,
                    cancellable:true
                },
                Check: {
                    params: { operate: 'checkApiExist' },
                    method: data.info.method,
                    cancellable:true
                },
                Import: {
                    params: { operate: 'importApi' },
                    method: data.info.method,
                    cancellable:true
                },
                Export: {
                    params: { operate: 'exportApi' },
                    method: data.info.method,
                    cancellable: true
                },
                Move:{
                    params: { operate: 'changeApiGroup' },
                    method: data.info.method,
                    cancellable:true
                }
            }

        );

        data.info.api['Doc'] = $resource(serverUrl + 'Document/:operate', {

            }, {
                Query: {
                    params: { operate: 'getDocumentList' },
                    method: data.info.method,
                    cancellable:true
                },
                All: {
                    params: { operate: 'getAllDocumentList' },
                    method: data.info.method,
                    cancellable:true
                },
                Add: {
                    params: { operate: 'addDocument' },
                    method: data.info.method,
                    cancellable:true
                },
                Delete: {
                    params: { operate: 'deleteDocuments' },
                    method: data.info.method,
                    cancellable:true
                },
                Edit: {
                    params: { operate: 'editDocument' },
                    method: data.info.method,
                    cancellable:true
                },
                Detail: {
                    params: { operate: 'getDocument' },
                    method: data.info.method,
                    cancellable:true
                },
                Search: {
                    params: { operate: 'searchDocument' },
                    method: data.info.method,
                    cancellable:true
                }
            }

        );
        data.info.api['DocGroup'] = $resource(serverUrl + 'DocumentGroup/:operate', {

            }, {
                Query: {
                    params: { operate: 'getGroupList' },
                    method: data.info.method,
                    cancellable:true
                },
                Add: {
                    params: { operate: 'addGroup' },
                    method: data.info.method,
                    cancellable:true
                },
                Delete: {
                    params: { operate: 'deleteGroup' },
                    method: data.info.method,
                    cancellable:true
                },
                Edit: {
                    params: { operate: 'editGroup' },
                    method: data.info.method,
                    cancellable:true
                },
                Sort: {
                    params: { operate: 'sortDocumentGroup' },
                    method: data.info.method,
                    cancellable:true
                },
                Import: {
                    params: { operate: 'importGroup' },
                    method: data.info.method,
                    cancellable: true
                },
                Export: {
                    params: { operate: 'exportGroup' },
                    method: data.info.method,
                    cancellable: true
                }
            }

        );

        data.info.api['Trash'] = $resource(serverUrl + 'Api/:operate', {

            }, {
                Query: {
                    params: { operate: 'getRecyclingStationApiList' },
                    method: data.info.method
                },
                Clean: {
                    params: { operate: 'cleanRecyclingStation' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteApi' },
                    method: data.info.method
                },
                Recover: {
                    params: { operate: 'recoverApi' },
                    method: data.info.method
                }
            }

        );

        data.info.api['Test'] = $resource(serverUrl + 'Test/:operate', {

            }, {
                Get: {
                    params: { operate: 'get' },
                    method: data.info.method
                },
                Post: {
                    params: { operate: 'post' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'delete' },
                    method: data.info.method
                },
                Patch: {
                    params: { operate: 'patch' },
                    method: data.info.method
                },
                Head: {
                    params: { operate: 'head' },
                    method: data.info.method
                },
                Options: {
                    params: { operate: 'options' },
                    method: data.info.method
                },
                Put: {
                    params: { operate: 'put' },
                    method: data.info.method
                },
                DeleteHistory: {
                    params: { operate: 'deleteTestHistory' },
                    method: data.info.method
                },
                DeleteAllHistory: {
                    params: { operate: 'deleteAllTestHistory' },
                    method: data.info.method
                },
                TestHistoryList: {
                    params: { operate: 'getTestHistoryList' },
                    method: data.info.method,
                    cancellable:true
                },
                AddHistory:{
                    params:{operate:'addTestHistory'},
                    method: data.info.method,
                    cancellable:true
                }
            }

        );

        data.info.api['Star'] = $resource(serverUrl + 'Api/:operate', {

            }, {
                Add: {
                    params: { operate: 'addStar' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'removeStar' },
                    method: data.info.method
                }
            }

        );

        data.info.api['Code'] = $resource(serverUrl + 'StatusCode/:operate', {

            }, {
                Query: {
                    params: { operate: 'getCodeList' },
                    method: data.info.method
                },
                All: {
                    params: { operate: 'getAllCodeList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addCode' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteCode' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editCode' },
                    method: data.info.method
                },
                Search: {
                    params: { operate: 'searchStatusCode' },
                    method: data.info.method
                },
                Import: {
                    params: { operate: 'addStatusCodeByExcel' },
                    method: data.info.method,
                    cancellable: true,
                    transformRequest: angular.identity,
                    headers: { "Content-Type": undefined }
                }
            }

        );
        data.info.api['Partner'] = $resource(serverUrl + 'Partner/:operate', {

            }, {
                Query: {
                    params: { operate: 'getPartnerList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'invitePartner' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'removePartner' },
                    method: data.info.method
                },
                Quit: {
                    params: { operate: 'quitPartner' },
                    method: data.info.method
                },
                Search: {
                    params: { operate: 'getPartnerInfo' },
                    method: data.info.method
                },
                SetType: {
                    params: { operate: 'editPartnerType' },
                    method: data.info.method
                },
                SetNickName: {
                    params: { operate: 'editPartnerNickName' },
                    method: data.info.method
                }
            }

        );
        
        data.info.api['Env'] = $resource(serverUrl + 'Env/:operate', {

            }, {
                Query: {
                    params: { operate: 'getEnvList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addEnv' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteEnv' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editEnv' },
                    method: data.info.method
                }
            }

        );

        data.info.api['ApiGroup'] = $resource(serverUrl + 'Group/:operate', {

            }, {
                Query: {
                    params: { operate: 'getGroupList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addGroup' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteGroup' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editGroup' },
                    method: data.info.method
                },
                Sort: {
                    params: { operate: 'sortGroup' },
                    method: data.info.method
                },
                Import: {
                    params: { operate: 'importGroup' },
                    method: data.info.method,
                    cancellable: true
                },
                Export: {
                    params: { operate: 'exportGroup' },
                    method: data.info.method,
                    cancellable: true
                }
            }

        );
        
        data.info.api['CodeGroup'] = $resource(serverUrl + 'StatusCodeGroup/:operate', {

            }, {
                Query: {
                    params: { operate: 'getGroupList' },
                    method: data.info.method
                },
                Add: {
                    params: { operate: 'addGroup' },
                    method: data.info.method
                },
                Delete: {
                    params: { operate: 'deleteGroup' },
                    method: data.info.method
                },
                Edit: {
                    params: { operate: 'editGroup' },
                    method: data.info.method
                },
                Sort: {
                    params: { operate: 'sortGroup' },
                    method: data.info.method
                },
                Import: {
                    params: { operate: 'importGroup' },
                    method: data.info.method,
                    cancellable: true
                },
                Export: {
                    params: { operate: 'exportGroup' },
                    method: data.info.method,
                    cancellable: true
                }
            }

        );

        data.info.api['Update'] = $resource(serverUrl + 'Update/:operate', {

            }, {
                autoUpdate: {
                    params: { operate: 'autoUpdate' },
                    method: data.info.method
                },
                manualUpdate: {
                    params: { operate: 'manualUpdate' },
                    method: data.info.method
                },
                Check: {
                    params: { operate: 'checkUpdate' },
                    method: data.info.method
                }
            }

        );

        data.info.api['Backup'] = $resource(serverUrl + 'Backup/:operate', {

            }, {
                backupProject: {
                    params: { operate: 'backupProject' },
                    method: data.info.method
                }
            }

        );

        data.info.api['AutomatedTestCase'] = $resource(serverUrl + 'AutomatedTestCase/:operate', {

        }, {
            Query: {
                params: { operate: 'getTestCaseList' },
                method: data.info.method,
                cancellable: true
            },
            Add: {
                params: { operate: 'addTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Delete: {
                params: { operate: 'deleteTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Edit: {
                params: { operate: 'editTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Search: {
                params: { operate: 'searchTestCase' },
                method: data.info.method,
                cancellable: true
            }
        });
        data.info.api['AutomatedTestGroup'] = $resource(serverUrl + 'AutomatedTestCaseGroup/:operate', {

        }, {
            Query: {
                params: { operate: 'getGroupList' },
                method: data.info.method,
                cancellable: true
            },
            Add: {
                params: { operate: 'addGroup' },
                method: data.info.method,
                cancellable: true
            },
            Delete: {
                params: { operate: 'deleteGroup' },
                method: data.info.method,
                cancellable: true
            },
            Edit: {
                params: { operate: 'editGroup' },
                method: data.info.method,
                cancellable: true
            },
            Sort: {
                params: { operate: 'sortGroup' },
                method: data.info.method,
                cancellable: true
            },
            Import: {
                params: { operate: 'importGroup' },
                method: data.info.method,
                cancellable: true
            },
            Export: {
                params: { operate: 'exportGroup' },
                method: data.info.method,
                cancellable: true
            }
        });

        data.info.api['AutomatedTestCaseSingle'] = $resource(serverUrl + 'AutomatedTestCaseSingle/:operate', {

        }, {
            Query: {
                params: { operate: 'getSingleTestCaseList' },
                method: data.info.method,
                cancellable: true
            },
            Add: {
                params: { operate: 'addSingleTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Delete: {
                params: { operate: 'deleteSingleTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Edit: {
                params: { operate: 'editSingleTestCase' },
                method: data.info.method,
                cancellable: true
            },
            Info:{
                params: { operate: 'getSingleTestCaseInfo' },
                method: data.info.method,
                cancellable: true
            }
        });

        return data.info.api;
    }
})();
