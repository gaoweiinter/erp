package com.company.erp.formula

import com.company.yes.erp.dev.IDocument;
import com.company.yes.erp.dev.IMidContext;

public class StatusFormula extends EntityContextAction{
    private IDocument doc;

    public StatusFormula(IMidContext context) throws Throwable{
        super(context);
        this.doc = context.getDocument();
    }

    public StatusFormula(IMidContext context, IDocument doc) throws Throwable{
        super(context);
        this.doc = doc;
    }

    public IDocument getDocument() {
        return doc;
    }

    public void changeStatus(boolean isUpdateDoc, String objectTypeCode, String businessTransactionCode, Long objectID,
        Long statusProfileID, StatusInfo statusInfo) throws Throwable{
        
        if(objectID <= 0){
            return;
        }

        List<EGS_ObjectSystemStatus> objectSystemStatuses;
        if(isUpdateDoc){
            objectSystemStatuses = EGS_ObjectSystemStatus.parseRowset(getDocument().getDataTable(EGS_ObjectSystemStatus.EGS_ObjectSystemStatus));
        } else {
            objectSystemStatuses = EGS_ObjectSystemStatus.loader(getMidContext()).SOID(objectID).loadList();
        }

        if(objectSystemStatuses == null || objectSystemStatuses.size() == 0){
            return;
        }

        EGS_ObjectType objectType = EGS_ObjectType.loader(getMidContext()).Code(objetcTypeCode).load();
        if(objectType == null){
            throw new Throwable("系统对象类型" + objectTypeCode + "不存在");
        }
        Long objectStatusProfileID = statusProfileID;
        checkStatusProfile(objectType.getOID(), objectStatusProfileID);

        Long objectTypeID = objectType.getOID();
        Long businessTransactionID = 0L;
        if(!StringUtil.isBlankOrNull(businessTransactionCode)){
            checkBusinessTransaction(objectID, businessTransactionCode);
            EGS_BusinessTransaction businessTransaction = EGS_BusinessTransaction.loader(getMidContext()).Code(businessTransactionCode).load();
            businessTransactionID = businessTransaction.getOID();
        }

        changeStatus_Inner(isUpdateDoc, statusInfo, objectTypeID, businessTransactionID, objectID, objectStatusProfileID, objectTypeCode);
    }

    private void changeStatus_Inner(boolean isUpdateDoc, StatusInfo statusInfo, Long objectTypeID, 
        Long businessTransactionID, Long objectID, Long statusProfileID, String objectTypeCode) throws Throwable{
        if(objectID <= 0 || (businessTransactionID <= 0 && statusInfo == null)){
            return;
        }
        if(businessTransactionID > 0){
            statusInfo = getStatusInfo(businessTransactionID, statusProfileID, statusInfo, objectTypeCode);
        }
        ObjectSystemStatusData objectSystemStatus = null;
        List<EGS_ObjectSystemStatus> systemStatusList = null;
        if(isUpdateDoc){
            systemStatusList = EGS_ObjectSystemStatus.parseRowset(getDocument().getDataTable(EGS_ObjectSystemStatus.EGS_ObjectSystemStatus));
            getDocument().addDirtyTableFlag(EGS_ObjectSystemStatus.EGS_ObjectSystemStatus);
        } else {
            objectSystemStatus = EGS_ObjectSystemStatus.loader(getMidContext()).ObjSysStatusBillID(objectID).noCache().load();
            systemStatusList = objectSystemStatus.egs_objectSystemStatuss();
        }

        if(systemStatusList != null || systemStatusList.size() > 0){
            /* 减少数据库查询操作 */
            Set<Long> ids = systemStatusList.stream().map(t->{
                try{
                    return t.getSystemStatusID();
                } catch (Throwable e) {
                    LogSvr.getInstance().error("changeStatus_Inner ids", e);
                }
                return 0L;
            }).collect(Collectors.toSet());
            Long[] statusIDs = ids.toArray(new Long[ids.size()]);
            List<EGS_SystemStatus> loadList = EGS_SystemStatus.loader(_context).OID(statusIDs).loadList();
            Map<Long, EGS_SystemStatus> mapStatus = loadList.stream().collect(Collectors.toMap(t->{
                try{
                    return t.getOID();
                } catch (Throwable e) {
                    LogSvr.getInstance().error("changeStatus_Inner mapStatus", e);
                }
                return 0L;
            }, Function.identity()));

            Iterator<EGS_ObjectSystemStatus> iterator = systemStatusList.iterator();
            while(iterator.hasNext()){
                EGS_ObjectSystemStatus formStatus = iterator.next();
                Long systemStatusID = formStatus.getSystemStatusID();   
                checkSystemStatusRule(businessTransactionID, systemStatusID, formStatus.getStatusSelect() != 0);
                if(!statusInfo.containsSystemStatus(systemStatusID)){
                    continue;
                }
                boolean isActive = statusInfo.getSystemStatus(systemStatusID);
                EGS_SystemStatus systemStatus = mapStatus.get(systemStatusID);//EGS_SystemStatus.load(getMidContext(), systemStatusID);
                if(systemStatus.getIsSetOnly() != 0 && !isActive){
                    throw new Exception("系统状态" + systemStatus.getCode() + "只能设置，不能删除");
                }
                formStatus.setStatusSelect(isActive ? 1 : 0);
                statusInfo.removeSystemStatus(systemStatusID);
            }
        } 
        
        Iterator<Entry<Long, Boolean>> it = statusInfo.iteratorSystemStatus();
        while (it != null && it.hasNext()) {
            Entry<Long, Boolean> entry = it.next();
            Long systemStatusID = entry.getKey();
            boolean isActive = entry.getValue();
            EGS_SystemStatus systemStatus = EGS_SystemStatus.load(getMidContext(), systemStatusID);
            if(isUpdateDoc){
                getDocument().appendDetail(EGS_ObjectSystemStatus.EGS_ObjectSystemStatus);
                DataTable status = getDcument().getDataTable(EGS_ObjectSystemStatus.EGS_ObjectSystemStatus);
                status.setLong(EGS_ObjectSystemStatus.SOID, objectID);
                status.setLong(EGS_ObjectSystemStatus.SystemStatusID, systemStatusID);
                status.setString(EGS_ObjectSystemStatus.SystemStatusCode, systemStatus.getCode());
                status.setString(EGS_ObjectSystemStatus.SystemStatusCaption, systemStatus.getName());
                status.setInt(EGS_ObjectSystemStatus.StatusSelect, isActive ? 1 : 0);
            } else {
                EGS_ObjectSystemStatus statusDtl = objectSystemStatus.newEGS_ObjectSystemStatus();
                statusDtl.setSOID(objectID);
                statusDtl.setSystemStatusID(systemStatusID);
                statusDtl.setSystemStatusCode(systemStatus.getCode());
                statusDtl.setSystemStatusCaption(systemStatus.getName());
                statusDtl.setStatusSelect(isActive ? 1 : 0);
            }
        }
        if(!isUpdateDoc){
            save(objectSystemStatus);
        }

        if(statusProfileID <= 0){
            return;
        }
        ObjectUserStatusData objectUserStatus = null;
        List<EGS_ObjectUserStatus> objectUserStatusList = null;
        if(isUpdateDoc){
            objectUserStatusList = EGS_ObjectUserStatus.parseRowset(getDocument().getDataTable(EGS_ObjectUserStatus.EGS_ObjectUserStatus));
            getDocument().addDirtyTableFlag(EGS_ObjectUserStatus.EGS_ObjectUserStatus);
        } else {
            objectUserStatus = EGS_ObjectUserStatus.loader(getMidContext()).ObjectStatusBillID(objectID).noCache().load();
            objectUserStatusList = objectUserStatus.egs_objectUserStatuss();    
        }
        
        if(objectUserStatusList != null || objectUserStatusList.size() > 0){
            List<EGS_ObjectUserStatus> userStatusesWithNum = objectUserStatusList.stream().filter(t->{
                try {
                    return t.getUserStatusNumber() > 0;
                } catch (Throwable throwable) {
                    return false;
                }
                
            }).collect(Collectors.toList());
            if(userStatusesWithNum != null && userStatusesWithNum.size() > 0){
                setAndCheckUserStsWithNum(statusInfo,businessTransactionID, objectUserStatusList, statusProfileID);
            } else{
                setAndCheckUserStsNoNum(statusInfo, businessTransactionID, objectUserStatusList, statusProfileID);
            }
        }
        Iterator<Entry<Long, Boolean>> itUserStatus = statusInfo.iteratorUserStatus();
        while (itUserStatus != null && itUserStatus.hasNext()) {
            Entry<Long, Boolean> entry = itUserStatus.next();
            Long userStatusID = entry.getKey();
            boolean isActive = entry.getValue();
            EGS_UserStatus userStatus = EGS_UserStatus.load(getMidContext(), userStatusID);
            if(isUpdateDoc){
                getDocument().appendDetail(EGS_ObjectUserStatus.EGS_ObjectUserStatus);
                DataTable status = getDcument().getDataTable(EGS_ObjectUserStatus.EGS_ObjectUserStatus);
                status.setLong(EGS_ObjectUserStatus.SOID, objectID);
                status.setLong(EGS_ObjectUserStatus.UserStatusID, userStatusID);
                status.setString(EGS_ObjectUserStatus.UserStatusCode, userStatus.getCode());
                status.setString(EGS_ObjectUserStatus.UserStatusCaption, userStatus.getName());
                status.setInt(EGS_ObjectUserStatus.StatusSelect, isActive ? 1 : 0);
            } else {
                EGS_ObjectUserStatus statusDtl = objectUserStatus.newEGS_ObjectUserStatus();
                statusDtl.setSOID(objectID);
                statusDtl.setUserStatusID(userStatusID);
                statusDtl.setUserStatusCode(userStatus.getCode());
                statusDtl.setUserStatusCaption(userStatus.getName());
                statusDtl.setStatusSelect(isActive ? 1 : 0);    

            }
        }
        if(!isUpdateDoc){
            save(objectUserStatus);
        }
    }

    /**
     * 为了让代码清晰，将带状态编号的处理逻辑拆分
     * 该方法里同样包含无状态编号的处理
     * @param statusInfo
     * @param businessTransactionID
     * @param objectUserStatusList
     * @param statusProfileID
     * @throws Throwable
     */
    private void setAndCheckUserStsWithNum(StatusInfo statusInfo, Long businessTransactionID, List<EGS_ObjectUserStatus> objectUserStatusList, Long statusProfileID) throws Throwable {
        //获取当前带编号的对象用户状态
        EGS_ObjectUserStatus curObjectUserStsWithNum = objectUserStatusList.stream().filter(t->{
            try {
                return t.getStatusSelect() == 1 && t.getUserStatusNumber() > 0;
            } catch (Throwable throwable) {
                return false;   
            }
        }).collect(Collectors.toList()).get(0);
        
        Iterator<EGS_ObjectSystemStatus> iteratorUserStatus = objectUserStatusList.iterator();
        List<EGS_UserStatus> userStatusList = EGS_UserStatus.loader(getMidContext()).SOID(statusProfileID).loadList();
        //增加效率
        Map<Long, EGS_UserStatus> userStatusMap = userStatusList.stream().collect(Collector.toMap(t-> {
            try{
                return t.getOID();
            } catch (Throwable throwable) {
                return 0L;
            }
        }, Function.identity()));
        Long preUserStsIDWithNum = curObjectUserStsWithNum.getUserStatusID();
        Long tagUserStsIDWithNum = 0L;
        //记录除当前带编号用户状态的设置个数
        int setOtherCountWithNum = 0;
        while (iteratorUserStatus.hasNext()) {
            EGS_ObjectUserStatus formStatus = iteratorUserStatus.next();
            Long userStatusID = formStatus.getUserStatusID();   
            checkUserStatusRule(businessTransactionID, userStatusID, formStatus.getStatusSelect() != 0);
            if(!statusInfo.containsUserStatus(userStatusID)){
                continue;
            }
            boolean isActive = statusInfo.getUserStatus(userStatusID);
            //用户状态带编号设置和取消记录ID
            if(formStatus.getUserStatusNumber() > 0){
                if(isActive){
                    //设置用户状态
                    if(!curObjectUserStsWithNum.getUserStatusID().equals(formStatus.getUserStatusID())){
                        tagUserStsIDWithNum = formStatus.getUserStatusID();
                        formStatus.setPreUserStatusID(preUserStsIDWithNum);
                        setOtherCountWithNum++;
                    }
                }
            }
            formStatus.setStatusSelect(isActive ? 1 : 0);
            statusInfo.removeUserStatus(userStatusID);
        }
        if(setOtherCountWithNum == 1){
            curObjectUserStsWithNum.setStatusSelect(0);
        }

        check4OnlyPickOneUserSts(null,"",objectUserStatusList);
        checkNumUserStsRange(preUserStsIDWithNum,tagUserStsIDWithNum,userStatusMap);
        
    }

    /**
     * 该方法处理只有无编号的用户状态明细
     * @param statusInfo
     * @param businessTransactionID
     * @param objectUserStatusList
     * @param statusProfileID
     * @throws Throwable
     */
    private void setAndCheckUserStsNoNum(StatusInfo statusInfo, Long businessTransactionID, List<EGS_ObjectUserStatus> objectUserStatusList, Long statusProfileID) throws Throwable {
        Iterator<EGS_ObjectUserStatus> iteratorUserStatus = objectUserStatusList.iterator();
        List<EGS_UserStatus> userStatusList = EGS_UserStatus.loader(getMidContext()).SOID(statusProfileID).loadList();
        //增加效率
        Map<Long, EGS_UserStatus> userStatusMap = userStatusList.stream().collect(Collector.toMap(t-> {
            try{
                return t.getOID();
            } catch (Throwable throwable) {
                return 0L;
            }
        }, Function.identity()));
        
        while (iteratorUserStatus.hasNext()) {
            EGS_ObjectUserStatus formStatus = iteratorUserStatus.next();
            Long userStatusID = formStatus.getUserStatusID();   
            checkUserStatusRule(businessTransactionID, userStatusID, formStatus.getStatusSelect() != 0);
            if(!statusInfo.containsUserStatus(userStatusID)){
                continue;
            }
            boolean isActive = statusInfo.getUserStatus(userStatusID);
            formStatus.setStatusSelect(isActive ? 1 : 0);
            statusInfo.removeUserStatus(userStatusID);
        }
    }

    private void clearStatus(List<EGS_ObjectSystemStatus> objectUserStatuss) throws Throwable {
        if(objectUserStatuss != null && objectUserStatuss.size() > 0){
            for(EGS_ObjectSystemStatus objectUserStatus : objectUserStatuss){ 
                if（objectUserStatus.getUserStatusNumber() > 0 && objectUserStatus.getStatusSelect() > 0）{
                    objectUserStatus.setStatusSelect(0);
                }
            }
        }
    }
}
