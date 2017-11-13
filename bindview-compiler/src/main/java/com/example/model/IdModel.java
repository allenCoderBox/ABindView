package com.example.model;

/**
 * @author husongzhen
 * @date 17/11/10
 */

public class IdModel {
    private String clazz;
    private String id;

    public IdModel(String viewType, String id) {
        this.clazz = viewType;
        this.id = id;
    }


    public String getClazz() {
        return clazz;
    }

    public String getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IdModel idModel = (IdModel) o;

        return (clazz != null ? clazz.equals(idModel.clazz) : idModel.clazz == null) && (id != null ? id.equals(idModel.id) : idModel.id == null);
    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
