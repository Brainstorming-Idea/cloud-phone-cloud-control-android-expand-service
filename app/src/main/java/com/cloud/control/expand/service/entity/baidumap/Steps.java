package com.cloud.control.expand.service.entity.baidumap;

import java.util.ArrayList;

/**
 * @author wangyou
 * @desc: 路线分段
 * @date :2021/3/10
 */
public class Steps {
    private Integer leg_index;//途径点序号为从0开始的整数，用于标识step所属的途径点路段 如：若该step属于起点至第一个途径中的路段，则其leg_index为0
    /**
     * 机动转向点，包括基准八个方向、环岛、分歧等
     * 枚举值，返回0-16之间的一个值，共17个枚举值。分别代表的含义是：
     * 0：无效
     * 1：直行
     * 2：右前方转弯
     * 3：右转
     * 4：右后方转弯
     * 5：掉头
     * 6：左后方转弯
     * 7：左转
     * 8：左前方转弯
     * 9：左侧
     * 10：右侧
     * 11：分歧-左
     * 12：分歧中央
     * 13：分歧右
     * 14：环岛
     * 15：进渡口
     * 16：出渡口
     */
    private Integer turn;
    private Long distance;//路段距离 单位：米
    private Long duration;//路段耗时 单位：秒
    /**
     * 若途经多个路段类别，将用英文逗号","分隔，如：-该路段依次途经国道和省道两种道路类型，则road_types:"2,3"
     * -该路段仅途经高速，则road_types:"0"
     * road_types取值范围：
     * 0：高速路
     * 1：城市高速路
     * 2:国道
     * 3：省道
     * 4：县道
     * 5：乡镇村道
     * 6：其他道路
     * 7：九级路
     * 8：航线(轮渡)
     * 9：行人道路
     */
    private String road_types;// 路段途径的道路类型列表
    private String instruction;//路段描述
    private Location start_location;
    private Location end_location;
    private String path;//分段坐标
    private ArrayList<TrafficCondition> traffic_condition;//路况描述

    public Integer getLeg_index() {
        return leg_index;
    }

    public void setLeg_index(Integer leg_index) {
        this.leg_index = leg_index;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getRoad_types() {
        return road_types;
    }

    public void setRoad_types(String road_types) {
        this.road_types = road_types;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Location getStart_location() {
        return start_location;
    }

    public void setStart_location(Location start_location) {
        this.start_location = start_location;
    }

    public Location getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Location end_location) {
        this.end_location = end_location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<TrafficCondition> getTraffic_condition() {
        return traffic_condition;
    }

    public void setTraffic_condition(ArrayList<TrafficCondition> traffic_condition) {
        this.traffic_condition = traffic_condition;
    }
}