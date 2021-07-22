package com.cloud.control.expand.service.entity;

public class HostBean {

    /**
     * sn : 111111
     * mac : 127.0.0.1
     * room : 1
     * frame : 1
     * host : 1
     * lowAddr : 1
     * blade : 12
     * card : 34
     * deviceBoard : RK_3399
     * runStatus : 10
     * bmcIp : 1
     * hostIp : 1
     * hostPort : 12
     * virtualPhones : [{"containerId":"322a7d11b9a83622d61e0fa668681b1ce4b54a9f7bd05d0c66c1a25f4f1afd6b","virtualIp":"172.17.0.2","videoMappingPort":"9100","innerNetPort":"32002"}]
     * containerId : 322a7d11b9a83622d61e0fa668681b1ce4b54a9f7bd05d0c66c1a25f4f1afd6b
     * virtualIp : 172.17.0.2
     * videoMappingPort : 9100
     * innerNetPort : 32002
     */

    //            {
//                "hostSn":       "RK3928C1502007361",
//                    "mac":  "",
//                    "room": "B3",
//                    "frame":        "F1",
//                    "host": "F3",
//                    "blade":        5,
//                    "card": 3,
//                    "boardType":    "VIRTUAL_RK3399",
//                    "runStatus":    "10",
//                    "bmcIp":        "192.168.50.62",
//                    "hostIp":       "192.168.11.10",
//                    "hostPort":     9000,
//                    "hostMemory":   0,
//                    "hostStorage":  0,
//                    "hostCpuCores": 0,
//                    "virtualSn":    "VM_RKiy1p74w85xc",
//                    "virtualIp":    "192.168.11.5",
//                    "virtualMemory":        2048,
//                    "virtualStorage":       16384,
//                    "virtualCpuCores":      4
//            }
    private String hostSn;
    private String mac;
    private String room;
    private String frame;
    private String host;
    private String lowAddr;
    private String blade;
    private String card;
    private String boardType;
    private String runStatus;
    private String bmcIp;
    private String hostIp;
    private String hostPort;
    private String hostMemory;
    private String hostStorage;
    private String hostCpuCores;
    private String containerId;
    private String virtualIp;
    private String virtualSn;
    private String videoMappingPort;//拉流映射端口
    private String innerVideoPort;//拉流内部端口
    private String businessPort;//业务映射端口
    private String innerBusinessPort;//业务端口
    private String innerNetPort;
    private String innerNetIp;//

    private String highAddr;
    private String outerNetIp;
    private String outerNetPort;

    private String iemi;
    private String model;
    private String phoneManufacturer;
    private String versionNumber;

    private String virtualMemory;
    private String virtualStorage;
    private String virtualCpuCores;

    public String getHostSn() {
        return hostSn;
    }

    public void setHostSn(String hostSn) {
        this.hostSn = hostSn;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLowAddr() {
        return lowAddr;
    }

    public void setLowAddr(String lowAddr) {
        this.lowAddr = lowAddr;
    }

    public String getBlade() {
        return blade;
    }

    public void setBlade(String blade) {
        this.blade = blade;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public String getBmcIp() {
        return bmcIp;
    }

    public void setBmcIp(String bmcIp) {
        this.bmcIp = bmcIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getHostMemory() {
        return hostMemory;
    }

    public void setHostMemory(String hostMemory) {
        this.hostMemory = hostMemory;
    }

    public String getHostStorage() {
        return hostStorage;
    }

    public void setHostStorage(String hostStorage) {
        this.hostStorage = hostStorage;
    }

    public String getHostCpuCores() {
        return hostCpuCores;
    }

    public void setHostCpuCores(String hostCpuCores) {
        this.hostCpuCores = hostCpuCores;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getVirtualIp() {
        return virtualIp;
    }

    public void setVirtualIp(String virtualIp) {
        this.virtualIp = virtualIp;
    }

    public String getVirtualSn() {
        return virtualSn;
    }

    public void setVirtualSn(String virtualSn) {
        this.virtualSn = virtualSn;
    }

    public String getVideoMappingPort() {
        return videoMappingPort;
    }

    public void setVideoMappingPort(String videoMappingPort) {
        this.videoMappingPort = videoMappingPort;
    }

    public String getInnerVideoPort() {
        return innerVideoPort;
    }

    public void setInnerVideoPort(String innerVideoPort) {
        this.innerVideoPort = innerVideoPort;
    }

    public String getBusinessPort() {
        return businessPort;
    }

    public void setBusinessPort(String businessPort) {
        this.businessPort = businessPort;
    }

    public String getInnerBusinessPort() {
        return innerBusinessPort;
    }

    public void setInnerBusinessPort(String innerBusinessPort) {
        this.innerBusinessPort = innerBusinessPort;
    }

    public String getInnerNetPort() {
        return innerNetPort;
    }

    public void setInnerNetPort(String innerNetPort) {
        this.innerNetPort = innerNetPort;
    }

    public String getInnerNetIp() {
        return innerNetIp;
    }

    public void setInnerNetIp(String innerNetIp) {
        this.innerNetIp = innerNetIp;
    }

    public String getHighAddr() {
        return highAddr;
    }

    public void setHighAddr(String highAddr) {
        this.highAddr = highAddr;
    }

    public String getOuterNetIp() {
        return outerNetIp;
    }

    public void setOuterNetIp(String outerNetIp) {
        this.outerNetIp = outerNetIp;
    }

    public String getOuterNetPort() {
        return outerNetPort;
    }

    public void setOuterNetPort(String outerNetPort) {
        this.outerNetPort = outerNetPort;
    }

    public String getIemi() {
        return iemi;
    }

    public void setIemi(String iemi) {
        this.iemi = iemi;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhoneManufacturer() {
        return phoneManufacturer;
    }

    public void setPhoneManufacturer(String phoneManufacturer) {
        this.phoneManufacturer = phoneManufacturer;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVirtualMemory() {
        return virtualMemory;
    }

    public void setVirtualMemory(String virtualMemory) {
        this.virtualMemory = virtualMemory;
    }

    public String getVirtualStorage() {
        return virtualStorage;
    }

    public void setVirtualStorage(String virtualStorage) {
        this.virtualStorage = virtualStorage;
    }

    public String getVirtualCpuCores() {
        return virtualCpuCores;
    }

    public void setVirtualCpuCores(String virtualCpuCores) {
        this.virtualCpuCores = virtualCpuCores;
    }


}
