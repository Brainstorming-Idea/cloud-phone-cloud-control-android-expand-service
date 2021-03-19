package com.cloud.control.expand.service.entity.baidumap;

import java.util.ArrayList;

/**
 * @author wangyou
 * @desc: 路线规划返回的信息
 * @date :2021/3/11
 */
public class RoutePlan extends BMapBaseResponse {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        private Location origin;
        private Location destination;
        private ArrayList<Routes> routes;

        public Location getOrigin() {
            return origin;
        }

        public void setOrigin(Location origin) {
            this.origin = origin;
        }

        public Location getDestination() {
            return destination;
        }

        public void setDestination(Location destination) {
            this.destination = destination;
        }

        public ArrayList<Routes> getRoutes() {
            return routes;
        }

        public void setRoutes(ArrayList<Routes> routes) {
            this.routes = routes;
        }
    }

    @Override
    public String toString() {
        return "RoutePlan{" +
                "result=" + result +
                '}';
    }
}