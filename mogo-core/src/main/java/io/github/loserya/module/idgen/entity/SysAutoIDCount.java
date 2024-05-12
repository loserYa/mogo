package io.github.loserya.module.idgen.entity;

public class SysAutoIDCount {

    private String id;

    private Long maxId = 0L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }
}
