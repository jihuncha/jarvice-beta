package huni.techtown.org.jarvice.common.data;


import java.util.List;

/**
 * 오늘할 일 list - 앱 데이터
 *
 *
 *
 * **/
public class HelperTodoListDefaultObject {
    private String TAG = HelperTodoListDefaultObject.class.getSimpleName();

    private long    id;
    private String  todoTitle;
    private int     todoColumn;             //0 - list 항목포함 /1 - 오직 타이틀컬럼
    private int     todoCheck;              //0 - check 안됨 / 1 - check 됨.
    private String  todoWork;

    private List<HelperTodoListItems> itemList;

    public HelperTodoListDefaultObject(){}

    public HelperTodoListDefaultObject(String todoTitle, int todoColumn, int todoCheck, String todoWork) {
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
    }

    public HelperTodoListDefaultObject(long id, String todoTitle, int todoColumn, int todoCheck, String todoWork) {
        this.id = id;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
    }

    public HelperTodoListDefaultObject(long id, String todoTitle, int todoColumn, int todoCheck, String todoWork, List<HelperTodoListItems> itemList) {
        this.id = id;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
        this.itemList = itemList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public int getTodoColumn() {
        return todoColumn;
    }

    public void setTodoColumn(int todoColumn) {
        this.todoColumn = todoColumn;
    }

    public int getTodoCheck() {
        return todoCheck;
    }

    public void setTodoCheck(int todoCheck) {
        this.todoCheck = todoCheck;
    }

    public String getTodoWork() {
        return todoWork;
    }

    public void setTodoWork(String todoWork) {
        this.todoWork = todoWork;
    }

    public List<HelperTodoListItems> getItemList() {
        return itemList;
    }

    public void setItemList(List<HelperTodoListItems> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "HelperTodoListDefaultObject{" +
                "id=" + id +
                ", todoTitle='" + todoTitle + '\'' +
                ", todoColumn=" + todoColumn +
                ", todoCheck=" + todoCheck +
                ", todoWork='" + todoWork + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
