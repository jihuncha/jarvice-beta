package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;

/**
 * 항목 데이터 - 서버데이터 앱에서 가공
 * 오늘의 할일의 상세 항목 데이터
 *
 *
 * **/
public class HelperTodoListItems implements Serializable {
    private String TAG = HelperTodoListItems.class.getSimpleName();

    //나중에 수정/삭제시 확인용으로 사용하기위한 id 및 타이틀
    private long id;
    private String itemTitle;
    private String  todoDate;
    private int     todoColumn;

    private String itemName;
    private int itemCheck;

    public HelperTodoListItems() {
    }

    public HelperTodoListItems(String itemName, int itemCheck) {
        this.itemName = itemName;
        this.itemCheck = itemCheck;
    }

    public HelperTodoListItems(String itemTitle, String itemName, int itemCheck) {
        this.itemTitle = itemTitle;
        this.itemName = itemName;
        this.itemCheck = itemCheck;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCheck() {
        return itemCheck;
    }

    public void setItemCheck(int itemCheck) {
        this.itemCheck = itemCheck;
    }

    public String getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
    }

    public int getTodoColumn() {
        return todoColumn;
    }

    public void setTodoColumn(int todoColumn) {
        this.todoColumn = todoColumn;
    }

    @Override
    public String toString() {
        return "HelperTodoListItems{" +
                "id=" + id +
                ", itemTitle='" + itemTitle + '\'' +
                ", todoDate='" + todoDate + '\'' +
                ", todoColumn=" + todoColumn +
                ", itemName='" + itemName + '\'' +
                ", itemCheck=" + itemCheck +
                '}';
    }
}
