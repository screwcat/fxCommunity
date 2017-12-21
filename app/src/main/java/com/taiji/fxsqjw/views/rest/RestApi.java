package com.taiji.fxsqjw.views.rest;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lightkin on 17-7-24.
 */

public interface RestApi {

    //登录
    @GET("communityPolice/auth.appLogin.do")
    Observable<Map> userLogin(@Query("userId") String username, @Query("passwd") String password);

    //实时上传位置
    @GET("communityPolice/instruction.realtimeLocFromApp.do")
    Observable<String> updateCurrentLocation(@Query("userId") String username, @Query("latitude") double lat, @Query("longitude") double lon, @Query("captureTime") long captureTime);

    //获取责任区
    @GET("communityPolice/map.checkingSignMap.do")
    Observable<List> checkingSignMap(@Query("userId") String username);

    //获取打卡情况
    @GET("communityPolice/map.checkingSignList.do")
    Observable<List> checkingSignList(@Query("userId") String username);

    //重点人员列表
    @GET("communityPolice/instruction.stressObjectQuery.do")
    Observable<Map> stressObjectQuery(@Query("currentPage") int currentPage, @Query("searchContent") String searchContent, @Query("currentUserId") String currentUserId);

    //人员基本信息添加
    @GET("dataColletcion/data.personInfo.do")
    Observable<Map> personMessage(@Query("userId") String userId, @Query("mldzid") String mldzid, @Query("credentialType") String credential_type, @Query("credentialCode") String credential_code, @Query("phone") String phone, @Query("work") String work, @Query("unit") String unit, @Query("location") String location, @Query("isRent") String is_rent, @Query("liverType") String liver_type);

    //寄居人口添加
    @GET("dataColletcion/data.personLodgeInfo.do")
    Observable<Map> entrustLive(@Query("userId") String userId, @Query("personId") String person_id, @Query("lodgeReason") String lodge_reason, @Query("lodgeType") String lodge_type, @Query("beginTime") String begin_time);

    //暂住人口添加
    @GET("dataColletcion/data.personStayInfo.do")
    Observable<Map> shackLive(@Query("userId") String userId, @Query("personId") String person_id, @Query("censusRegisterType") String census_register_type, @Query("shackTime") String shack_time);

    //房屋基本信息添加
    @GET("dataColletcion/data.houseInfo.do")
    Observable<Map> houseMessage(@Query("userId") String userId, @Query("mldzid") String mldzid, @Query("houseCharacter") String house_character, @Query("houseType") String house_type, @Query("houseUse") String house_use, @Query("isRent") String is_rent, @Query("ownerCredentialType") String owner_credential_type, @Query("address") String address, @Query("roomNumber") String room_number, @Query("square") String square, @Query("ownerCredentialCode") String owner_credential_code, @Query("ownerPhone") String owner_phone);

    //隐患列表
    @GET("communityPolice/instruction.instructQuery.do")
    Observable<Map> getInstruct(@Query("currentPage") int currentPage, @Query("searchContent") String searchContent, @Query("userId") String userId);

    //出租房屋信息添加
    @GET("dataColletcion/data.houseRentInfo.do")
    Observable<Map> hireHouse(@Query("userId") String userId, @Query("houseId") String house_id, @Query("credentialType") String credential_type, @Query("roomNum") String room_num, @Query("rentSquare") String rent_square, @Query("rentTime") String rent_time, @Query("money") String money, @Query("relation") String relation, @Query("credentialCode") String credential_code, @Query("phone") String phone);

    //待办任务列表
    @GET("communityPolice/instruction.waitProcess.do")
    Observable<Map> getWaitProcess(@Query("currentPage") int currentPage, @Query("currentUser") String currentUser, @Query("searchContent") String searchContent, @Query("signingStatus") String signingStatus);

    //任务内容页
    @GET("communityPolice/instruction.waitProcessDetail.do")
    Observable<Map> waitProcessDetail(@Query("taskId") String taskId);

    //签收任务
    @GET("communityPolice/instruction.waitProcessSign.do")
    Observable<Map> waitProcessSign(@Query("receiverId") String userId, @Query("noticeId") String taskId);

    //任务接收人列表
    @GET("communityPolice/instruction.instructDispatcherQuery.do")
    Observable<Map> instructDispatcherQuery(@Query("policeNum") String policeNum);

    //三级消防隐患列表
    @GET("communityPolice/instruction.threeLevelFireDetail.do")
    Observable<Map> threeLevelFireDetail(@Query("unitCode") String unitCode);

    //三级消防隐患详情
    @GET("communityPolice/instruction.threeLevelFireDetailInfo.do")
    Observable<Map> threeLevelFireDetailInfo(@Query("id") String id);

    //三级消防隐患提交
    @GET("communityPolice/instruction.hiddenDangerDealResult.do")
    Observable<Map> hiddenDangerDealResult(@Query("id") String id, @Query("dealProject") String dealProject, @Query("dealResult") String dealResult);

    //三级消防单位列表
    @GET("communityPolice/instruction.threeLevelFireQuery.do")
    Observable<Map> threeLevelFireQuery(@Query("currentPage") String currentPage, @Query("userId") String userId);

    //校园安全详细
    @GET("communityPolice/instruction.campusSecurityDetail.do")
    Observable<Map> campusSecurityDetail(@Query("unitCode") String unitCode);

    //校园安全隐患详情
    @GET("communityPolice/instruction.campusSecurityDetailInfo.do")
    Observable<Map> campusSecurityDetailInfo(@Query("id") String id);

    //校园安全隐患提交
    @GET("communityPolice/instruction.hiddenDangerDealResult.do")
    Observable<Map> campusSecurityDealResult(@Query("id") String id, @Query("dealProject") String dealProject, @Query("dealResult") String dealResult);

    //校园安全单位列表
    @GET("communityPolice/instruction.campusSecurityQuery.do")
    Observable<Map> campusSecurityQuery(@Query("currentPage") String currentPage, @Query("userId") String userId);


    //重点对象提交
    @GET("communityPolice/instruction.stressObjectWrite.do")
    Observable<Map> stressObjectWrite(@Query("userId") String userId, @Query("objectId") String objectId, @Query("describe") String describe, @Query("xsjz") String xsjz);

    //安全隐患详情
    @GET("communityPolice/instruction.instructQueryDetail.do")
    Observable<Map> instructQueryDetail(@Query("uuid") String uuid, @Query("receiverId") String receiverId);

    //确认隐患
    @GET("communityPolice/instruction.instructSign.do")
    Observable<Map> instructSign(@Query("uuid") String uuid, @Query("reportContent") String reportContent, @Query("status") String status);

    //发布任务
    @GET("communityPolice/instruction.instructDispatch.do")
    Observable<Map> instructDispatch(@Query("noticeTitle") String noticeTitle, @Query("noticeContent") String noticeContent, @Query("senderId") String senderId);

    //地址搜索
    @GET("dataColletcion/data.addressQuery.do")
    Observable<List> addressSearchTask(@Query("dzmc") String dzmc);

    //待办任务小红点数字
    @GET("communityPolice/instruction.waitProcessNum.do")
    Observable<Map> waitProcessNum(@Query("receiverId") String receiverId);

    //三级消防单位列表搜索
    @GET("communityPolice/instruction.threeLevelFireQuery.do")
    Observable<Map> threeLevelFireQueryFrame(@Query("curPage") String curPage, @Query("searchContent") String searchContent);

    //校园安全单位列表搜索
    @GET("communityPolice/instruction.campusSecurityQuery.do")
    Observable<Map> campusSecurityQueryFrame(@Query("currentPage") String currentPage, @Query("searchContent") String searchContent);
}
