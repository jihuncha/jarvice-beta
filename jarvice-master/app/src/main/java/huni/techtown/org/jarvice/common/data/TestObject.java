package huni.techtown.org.jarvice.common.data;

public class TestObject {
    private String TAG = TestObject.class.getSimpleName();

    private String text;
    private boolean isSelected;

    public TestObject(){}

    public TestObject(String text, boolean isSelected) {
        this.text = text;
        this.isSelected = isSelected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
