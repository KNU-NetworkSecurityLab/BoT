package com.example.bot_project;

public class NetworkURL {

    public final static StringBuffer AddIoT = new StringBuffer("http://~/iot/addIot");//사용자(IoT)추가
    public final static StringBuffer UpdateIoT = new StringBuffer("http://~/iot/updateIot");//사용자 정보 업데이트
    public final static StringBuffer lampStateUpdate = new StringBuffer("http://~/iot/lampStateUpdate");//전등상태 업데이트
    public final static StringBuffer IoTFind = new StringBuffer("http://~/iot/iotFind");//userid값으로 관련된 iot정보 찾기
    public final static StringBuffer FindArea = new StringBuffer("http://~/iot/iotFindByArea");//area 지역에 있는 iot 정보 찾기
    public final static StringBuffer AllIot = new StringBuffer("http://~/iot/queryAllIoTs");// 모든 iot 정보 검색
    public final static StringBuffer Adduser = new StringBuffer("http://~/api/addUser");//유저 정보 추가(회원가입)
    public final static StringBuffer Login = new StringBuffer("http://~/api/login");//유저 로그인
    public final static StringBuffer AllUsers = new StringBuffer("http://~/api/queryAllUsers");//유저 정보 모두 검색
    public final static StringBuffer Userinfo = new StringBuffer("http://~/api/getUserInfo");//id값으로 유저 정보 검색
}
