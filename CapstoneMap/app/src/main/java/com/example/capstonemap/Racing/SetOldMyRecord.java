package com.example.capstonemap.Racing;

import com.example.capstonemap.locationUpdate.GetOldRecord;
import com.example.capstonemap.locationUpdate.UserRecordDto;
import com.example.capstonemap.locationUpdate.UserUpdateInfo;

//먼저 setOldMyRecord를 하고 Racing에 있는 함수들을 써야함.
public class SetOldMyRecord {
    public static void setOldMyRecord(UserUpdateInfo userUpdateInfo){
        UserRecordDto myOldRecord= GetOldRecord.getMyOldRecord(userUpdateInfo.getUserId(), userUpdateInfo.getRouteId());
        userUpdateInfo.setOldMyRecord(myOldRecord);
    }
}
