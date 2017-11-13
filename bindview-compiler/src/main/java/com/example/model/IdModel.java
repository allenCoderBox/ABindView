package com.example.model;

/**
 * @author husongzhen
 * @date 17/11/10
 */

public class IdModel {
    private String viewType;
    private String id;

    public IdModel(String viewType, String id) {
        this.viewType = viewType;
        this.id = id;
    }


    public String getViewType() {
        return viewType;
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "IdModel{" +
                "viewType='" + viewType + '\'' +
                ", id='" + id + '\'' +
                '}';
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

        if (viewType != null ? !viewType.equals(idModel.viewType) : idModel.viewType != null)
            return false;
        return id != null ? id.equals(idModel.id) : idModel.id == null;
    }

    @Override
    public int hashCode() {
        int result = viewType != null ? viewType.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
