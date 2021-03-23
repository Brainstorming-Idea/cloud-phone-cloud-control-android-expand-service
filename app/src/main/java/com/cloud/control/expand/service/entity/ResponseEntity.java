/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.control.expand.service.entity;

import java.io.Serializable;

/**
 * Author：abin
 * Date：2020/10/15
 * Description： 基础后台返回数据类
 */
public class ResponseEntity<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987833L;

    /**
     * status : 0
     * msg : 接口返回成功
     * retCode : 30011 未购买扩展服务
     * data :
     */

    private int status;
    private String msg;
    private int retCode;
    private T data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", retCode=" + retCode +
                ", data=" + data +
                '}';
    }
}
