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
     * @function [状态码常量集] [State code constant]
     * @version  3.1.5
     */
    angular
        .module('eolinker.constant')
        .constant('ERROR_WARNING', {
            COMMON: '请稍候再试或提交工单反馈'
        })
        .constant('CODE', {
            COMMON: {
                SUCCESS: '000000', //请求成功 Request success
                UNLOGIN: '100001', //用户未登录 User not logged in
                SERVER_ERROR: '100000', //请求失败，服务器错误，稍后再试 Request failed, server error, try again later
                UNAUTH: '100002' //用户缺乏相应的操作权限 The user lacks the corresponding operation authority
            },
            USER: {
                ERROR: '120000', //操作失败，处理失败及服务器出错 The operation failed, the processing failed and the server went wrong
                ILLIGLE_PASSWORD: '120002', //密码格式非法 Illegal password format
                ERROR_PASSWORD: '120003', //用户不存在或用户名密码错误 User does not exist or username password error
                ILLIGLE_INFO: '120004', //用户登录信息格式非法，或者不为（手机号、用户名、邮箱）之一 User login information format is illegal, or not for (mobile phone number, user name, mailbox) one
                ERROR_LOGIN: '100001', //用户尚未登录 User not logged in
                EXIST: '130005', //用户名已存在 User name already exists
                PASSWORD_ERROR: '130006', //用户名已存在 User name already exists
                UNCHANGE: '120007', //新旧密码相同 Same old and new passwords 
                ILLIGLE_NICKNAME: '120008', //用户昵称格式非法，长度须小于20位 User nickname format illegal, length must be less than 20 bits
                ILLIGLE_NAME: '120009', //用户名格式非法 User name format illegal
            },
            API_TEST: {
                ERROR: '210000', //操作失败/列表为空等 The operation failed / list is null   
                ILLEGAL_URI: '210001', //URI地址格式非法 URI address format illegal   
                ILLEGAL_REQUEST_TYPE: '210002', //请求参数的类型非法 The type of request parameter is illegal   
                ERROR_ADD_HISTORY: '210003', //添加测试记录失败 Failed to add test record 
                ILLEGAL_HISTORY_ID: '210004', //测试记录ID格式非法 Test record ID format illegal
            },
            API_GROUP: {
                ERROR: '150000', //操作失败 operation failed
                ILLEGAL_NAME: '150001', //分组名称格式非法 The grouping name format is illegal  
                ILLEGAL_PARENT_ID: '150002', //父分组ID格式非法 The parent group ID is illegal
                ILLEGAL_ID: '150003' //分组ID格式非法 Grouping ID format illegal
            },
            PARTNER: {
                ERROR: '250000', //操作失败/列表为空等 The operation failed / list is null
                ILLEGAL_USERCALL: '250001', //目标邀请人员的信息填写有误，不为（手机号、邮箱、用户名）之一 The information of the target inviting personnel is incorrect, which is not one of the mobile phone number, e-mail address and user name
                EXIST: '250002', //该用户已经被邀请过 The user has been invited    
                ILLEGAL_ID: '250003', //协作成员的关联ID（connID）格式非法 The associated member ID (connID) format is illegal
                ILLEGAL_NICKNAME: '250004', //协作成员的昵称格式非法 The nickname format of the collaboration member is illegal  
                ILLEGAL_TYPE: '250005' //设置的协作成员类型有误 The set of collaboration member types is incorrect  
            },
            MESSAGE: {
                ERROR: '260000', //操作失败/列表为空等 The operation failed / list is null     
                ILLEGAL_ID: '260001' //消息ID格式非法 Message ID format illegal  
            },
            ENV: {
                ERROR: '170000', //操作失败/列表为空等 The operation failed / list is null   
                ILLEGAL_NAME: '170001', //环境名称格式非法 The name of the environment is illegal  
                ILLEGAL_ID: '170002', //环境ID格式非法 Environment ID format illegal       
                ILLEGAL_URI: '170003', //前置URI地址格式非法 The prefix URI address format is illegal     
                ILLEGAL_HEADER_ID: '170004' //header头部ID格式非法 Header head ID format illegal  
            },
            PROJECT: {
                ERROR: "140000", //请求失败 request was aborted
                ILLEGAL_PROJECT_NAME: "140001", //项目名称格式不合法 The name of the project is illegal
                ILLEGAL_PROJECT_TYPE: "140002", //项目类型不合法 The project type is illegal  
                ILLEGAL_PROJECT_VERSION: "140003", //项目版本不合法 The project version is illegal  
                ILLEGAL_PROJECT_ID: "140004", //项目ID不合法 Project ID is illegal
                ILLEGAL_PROJECT_DESCRIPTION: "140005", //项目描述长度不合法 The length of the project description is not legal
                ILLEGAL_PROJECT_SHARE_STATUS: "140006", //分享状态格式非法，只能为0/1 Shared state format is illegal only for 0/1  
                ILLEGAL_PROJECT_LOCK_STATUS: "140007" //加密选项不合法，只能为0/1 The encryption option is illegal and can only be 0/1  
            },
            PROJECT_API: {
                ERROR: '160000', //操作失败/列表为空等 The operation failed / list is null
                ILLEGAL_ID: '160001', //接口ID格式非法 API ID format illegal 
                EXIST: '160002', //已存在相同接口 The same interface already exists
                ILLEGAL_SEARCH: '160003', //搜索关键字格式非法 Search keyword format illegal
                ILLEGAL_HISTORY_ID: '160004' //接口历史记录ID格式非法 API history ID format illegal
            },
            STATUS_CODE: {
                ERROR: '190009', //操作失败/列表为空等 The operation failed / list is null
                ILLEGAL_NAME: '190001', //状态码格式非法 Status code format illegal  
                ILLEGAL_DESC: '190002', //状态码描述格式非法 State code description format illegal    
                ILLEGAL_ID: '190003', //状态码ID格式非法 Status code ID format illegal  
                ILLEGAL_SEARCH: '190004' //状态码搜索提示格式非法 Status code search prompt format illegal  
            },
            STATUS_CODE_GROUP: {
                ERROR: '180000', //操作失败/列表为空等 The operation failed / list is null
                ILLEGAL_NAME: '180001', //分组名称格式非法 The grouping name format is illegal   
                ILLEGAL_ID: '180002', //分组ID格式非法 Grouping ID format illegal   
                ILLEGAL_PARENT_ID: '180003' //父分组ID格式非法 The parent group ID is illegal
            },
            DOC: {
                ERROR: '230000', //操作失败/列表为空等 The operation failed / list is null
                ILLEGAL_GROUP_ID: '230001', //文档分组ID格式非法 Document grouping ID format illegal 
                ILLEGAL_DESC: '230002', //文档描述格式不合法，必须为[0/1]=>[富文本/MARKDOWN] The document description format is not legal and must be [0/1]=>[rich text /MARKDOWN]     
                ILLEGAL_ID: '230003', //文档ID格式非法 Document ID format illegal 
                ILLEGAL_SEARCH: '230004', //搜索关键字长度非法，必须为1-255 Search keyword length illegal, must be 1-255
                GROUP: {
                    ERROR: '220000', //操作失败/列表为空等 The operation failed / list is null
                    ILLEGAL_NAME: '220001', //分组名称格式非法 The grouping name format is illegal  
                    ILLEGAL_ID: '220003', //分组ID格式非法 Grouping ID format illegal      
                    ILLEGAL_PARENT_ID: '220002' //父分组ID格式非法 The parent group ID is illegal
                }
            },
            IMPORT_EXPORT: {
                ERROR: '310000', //导入/导出失败 Import / export failure
                EMPTY: '310001', //导入的数据为空 The imported data is empty
                ILLEGAL_VERSION: '310002', //导入postman数据缺少版本号[1/2]或者版本号不正确 Importing postman data lacks version number [1/2] or version number incorrect
                ILLEGAL_IMPORT: '310003' //导入的RAP数据为空或者数据格式有误 The imported RAP data is null or data format error
            },
            EMPTY: '150008'
        })
})();
