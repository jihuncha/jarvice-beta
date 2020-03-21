package huni.techtown.org.jarvice.common.data;


import java.util.List;

/**
 * 오늘할 일 list - 앱 데이터
 *
 *
 *
 * **/
public class HelperTodoListObject {
    private String TAG = HelperTodoListObject.class.getSimpleName();

    private long    id;
    private String  todoDate;
    private String  todoTitle;
    private int     todoColumn;             //0 - list 항목포함 /1 - 오직 타이틀컬럼
    private int     todoCheck;              //0 - check 안됨 / 1 - check 됨.
    private String  todoWork;

    private List<HelperTodoListItems> itemList;

    public boolean expanded = false;

    public HelperTodoListObject(){}

    public HelperTodoListObject(String todoDate, String todoTitle, int todoColumn,
                                int todoCheck, String todoWork) {
        this.todoDate = todoDate;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
    }

    //앱용 생성자
    public HelperTodoListObject(String todoDate, String todoTitle,
                                int todoColumn, List<HelperTodoListItems> itemList) {
        this.todoDate = todoDate;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.itemList = itemList;
    }

    //add 용 생성자
    public HelperTodoListObject(long id, String todoDate, String todoTitle, int todoColumn) {
        this.id = id;
        this.todoDate = todoDate;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
    }

    //add용 생성자 - no id
    public HelperTodoListObject(String todoDate, String todoTitle, int todoColumn) {
        this.todoDate = todoDate;
        this.todoTitle = todoTitle;
        this.todoColumn = todoColumn;
        this.todoCheck = todoCheck;
        this.todoWork = todoWork;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(String todoDate) {
        this.todoDate = todoDate;
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

    public List<HelperTodoListItems> getItemList() {
        return itemList;
    }

    public void setItemList(List<HelperTodoListItems> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "HelperTodoListObject{" +
                "id=" + id +
                ", todoDate='" + todoDate + '\'' +
                ", todoTitle='" + todoTitle + '\'' +
                ", todoColumn=" + todoColumn +
                ", todoCheck=" + todoCheck +
                ", todoWork='" + todoWork + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
